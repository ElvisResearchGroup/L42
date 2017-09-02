package repl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Expression;
import ast.Ast.Doc;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
import coreVisitors.CloneVisitor;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import facade.L42.ExecutionStage;
import programReduction.ProgramReduction;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class ReplState {
  String originalS;
  ast.Expression.ClassReuse originalL;
  ast.ExpCore.ClassB desugaredL;
  public ReplState(String originalS, ast.Expression.ClassReuse originalL, ast.ExpCore.ClassB desugaredL) {
    this.originalS = originalS;
    this.originalL = originalL;
    this.desugaredL = desugaredL;
    }
public static ReplState start(String code){
  try{
    Expression.ClassReuse code1=(ClassReuse) Parser.parse("Repl",code);
    auxiliaryGrammar.WellFormedness.checkAll(code1);
    Expression.ClassReuse code2=(ClassReuse)Desugar.of(code1);
    assert auxiliaryGrammar.WellFormedness.checkAll(code2);
    ExpCore.ClassB code3=(ExpCore.ClassB)code2.accept(new InjectionOnCore());
    assert coreVisitors.CheckNoVarDeclaredTwice.of(code3);
    // TODO: will die after new reduction Refresh of position identities, it is used to generate correct Java code.
    code3=(ExpCore.ClassB)code3.accept(new CloneVisitor(){
      @Override public ExpCore visit(ExpCore.ClassB cb){
        Position p=cb.getP();
        cb=cb.withP(new Position(p.getFile(),p.getLine1(),p.getPos1(),p.getLine2(),p.getPos2(),p.get_next()));
        return super.visit(cb);
        }
      });
    ExpCore.ClassB result= new ProgramReduction().allSteps(code3);
    return new ReplState(code, code2,result);
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
    try{
      //parse
      Expression.ClassB codeTmp=(ClassB) Parser.parse("Repl","{"+code+"}");  
      Position fullP=codeTmp.getP();
      //new original
      ClassReuse newOriginal = this.originalL;      
      List<ast.Expression.ClassB.Member> newOriginalMs=newOriginal.getInner().getMs();
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
      // TODO: will die after new reduction Refresh of position identities, it is used to generate correct Java code.
      code3=(ExpCore.ClassB)code3.accept(new CloneVisitor(){
        @Override public ExpCore visit(ExpCore.ClassB cb){
          Position p=cb.getP();
          cb=cb.withP(new Position(p.getFile(),p.getLine1(),p.getPos1(),p.getLine2(),p.getPos2(),p.get_next()));
          return super.visit(cb);
          }
       });
      //integrate new desugared src with old desugared code
      List<Member> resultMs=new ArrayList<>(this.desugaredL.getMs());
      for(int i=nestedAdded;i<code3.getMs().size();i++){
        resultMs.add(code3.getMs().get(i));
        }
      code3=code3.withMs(resultMs);
      //call the repl and return
      ExpCore.ClassB result= new ProgramReduction().allSteps(code3);
      return new ReplState(this.originalS+"\n"+code, newOriginal,result);
      }
    catch(ParseCancellationException parser){
      System.out.println(parser.getMessage());
      return null;
      }
    catch(ErrorMessage msg){
      ErrorFormatter.topFormatErrorMessage(msg);
      return null;
      } 
    }
  }
