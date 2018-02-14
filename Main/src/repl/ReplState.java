package repl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Expression;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
import caching.Loader;
import caching.Phase1CacheKey;
import coreVisitors.CloneVisitor;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import facade.L42.ExecutionStage;
import profiling.Timer;
import programReduction.Program;
import programReduction.ProgramReduction;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class ReplState {
  String originalS;
  String code;
  ast.ExpCore.ClassB desugaredL;
  ProgramReduction reduction;
  //ProgramReduction reduction=new ProgramReduction(null);
  Program p;
  ReplState oldRepl;
  public ReplState(String originalS, ast.ExpCore.ClassB desugaredL,Program p,ProgramReduction reduction) {
    this.originalS = originalS;
    this.desugaredL = desugaredL;
    this.p=p;
    this.reduction=reduction;
    }
public static ReplState start(String code, Loader cacheC42){
  Program p=Phase1CacheKey._handleCache(code);
  try{
    boolean cached=p!=null;
    if(!cached){
      Timer.activate("RunningWithoutParsedCache");
      p= L42.parseAndDesugar("Repl",code);
      }
    ProgramReduction pr = new ProgramReduction(cacheC42,!cached);
    ReplState res=new ReplState(code,p.top(),p,pr);
    res.desugaredL=res.reduction.allSteps(res.p);
    res.p=res.p.updateTop(res.desugaredL);
    res.code=code.substring(1, code.length()-1); //to remove start and end {}
    return res;
    }
    catch(org.antlr.v4.runtime.misc.ParseCancellationException parser){
      System.out.println(parser.getMessage());
      return null;
      }
    catch(ErrorMessage msg){
      ErrorFormatter.topFormatErrorMessage(msg);
      return null;
      }
  }
  public ReplState add(String code){
    Expression.ClassB cbEmpty=new ClassB(Doc.empty(),new ast.Ast.InterfaceHeader(),Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),Position.noInfo);
    //parse
    Expression.ClassB codeTmp=(ClassB) Parser.parse("Repl","{"+code+"}");
    Position fullP=codeTmp.getP();
    //new original
    ClassB inner=new ClassB(Doc.empty(),new Ast.TraitHeader(),Collections.emptyList(),
      Collections.emptyList(),Collections.emptyList(), fullP);
    ClassReuse newOriginal = new ClassReuse(inner, null,null);
    List<ast.Expression.ClassB.Member> newOriginalMs=new ArrayList<>();
    newOriginalMs.addAll(codeTmp.getMs());
    newOriginal.withInner(newOriginal.getInner().withMs(newOriginalMs));
    //new src to desugar
    List<ClassB.Member>newMs=new ArrayList<>();
    int nestedAdded=0;
    for( Member m:this.desugaredL.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      fullP=fullP.sum(nc.getP());
      newMs.add(new ClassB.NestedClass(Doc.empty(),nc.getName(),cbEmpty,nc.getP()));
      nestedAdded+=1;
      }
    newMs.addAll(codeTmp.getMs());
    codeTmp=codeTmp.withMs(newMs).withP(fullP);
    Expression code2=Desugar.of(codeTmp);
    ExpCore.ClassB code3=(ExpCore.ClassB)code2.accept(new InjectionOnCore());
    List<Member> resultMs=new ArrayList<>(this.desugaredL.getMs());
    for(int i=nestedAdded;i<code3.getMs().size();i++){
      resultMs.add(code3.getMs().get(i));
      }
    code3=code3.withMs(resultMs);
    //call the repl and return
    ReplState res=new ReplState(this.originalS+"\n"+code,code3,this.p.updateTop(code3),this.reduction);
    res.desugaredL=res.reduction.allSteps(res.p);
    res.p=res.p.updateTop(res.desugaredL);
    res.oldRepl=this;
    res.code=code;
    return res;
    }
  }
