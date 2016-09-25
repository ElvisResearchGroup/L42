package programReduction;

import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.xml.internal.txw2.output.StreamSerializer;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import ast.Util.CachedStage;
import coreVisitors.CloneVisitor;
import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.HistoricType;
import ast.Ast.MethodType;
import ast.Ast.Type;
public class Norm {

  static Type nextT(Ast.Path p,MethodWithType mwt,Ast.MethodSelectorX msx){
    String x=msx.getX();
    if(x.isEmpty()){return mwt.getMt().getReturnType();}
    if(x.equals("this")){
      return new NormType(mwt.getMt().getMdf(),p,Ph.None);
      }
    int i=mwt.getMs().getNames().indexOf(x);
    assert i!=-1:
    mwt.getMs()+" but searched for "+ x+" in "+p;
    return mwt.getMt().getTs().get(i);
    }
  static ExpCore.ClassB.MethodWithType select(Ast.MethodSelector ms, List<ExpCore.ClassB.MethodWithType> list){
    for(MethodWithType mwt:list){if(mwt.getMs().equals(ms)){return mwt;}}
    return null;
  }
  static NormType resolve(Program p,Type t){
    try{
      return _resolve(p,t);//TODO: if needed check/fix force placeholders here
    }
    catch(StackOverflowError err){
      throw new ast.ErrorMessage.HistoricTypeCircularDefinition((HistoricType)t);
      }
    }
  static NormType _resolve(Program p,Type t){//may stack overflow
//-resolve(p,mdf P)=mdf P
    if( t instanceof NormType){return (NormType) t;}
    HistoricType ht=(HistoricType) t;
//-resolve(p,P::ms)=resolve(p,T)//and avoid circularity
//  methods(p,P)(ms)=refine? _ method T ms e?
    List<MethodWithType> methods = p.methods(ht.getPath());
    MethodWithType mwt = select(ht.getSelectors().get(0).getMs(),methods);
    if(mwt==null){
      //throw new ast.ErrorMessage.NormImpossible(t, cause, null);
      throw new ast.ErrorMessage.HistoricTypeNoTarget(ht,methods );
      //TODO: what about the name targetType
      }
    Type nextT=nextT(ht.getPath(),mwt,ht.getSelectors().get(0));
    NormType nextNT= _resolve(p,nextT);
    if (ht.getSelectors().size()==1){return nextNT;}
    return _resolve(p,new HistoricType(nextNT.getPath(),ht.getSelectors().subList(1, ht.getSelectors().size()) , ht.isForcePlaceholder()/*may disappear?*/));
//-resolve(p,P::ms::x)=resolve(p,T)//and avoid circularity
//  methods(p,P)(ms)=refine? _ method _ _( _ T x _) e? 
//-resolve(p,P::msx::msxs)=resolve(p,P'::msxs) //here be carefull for possible infinite recursion 
//  resolve(p,P::msx)= _ P'              
  }
  private static MethodType resolve(Program p, MethodType mt) {
    Type rt=resolve(p,mt.getReturnType());
    List<Type>pts=Map.of(t->resolve(p,t),mt.getTs());
    return mt.withReturnType(rt).withTs(pts);
    }
  static ExpCore norm(Program p,ExpCore e){
    return e.accept(new CloneVisitor(){
      protected Type liftT(Type t){ return resolve(p,t); }
      public ExpCore visit(ClassB s) {
        if(s.getPhase()!=Phase.None){return s;}
        return norm(p.evilPush(s));
        }});
    }
  static ExpCore.ClassB norm(Program p){
    //-norm(p)={interface? implements Ps' norm(p,Ms') }
    //p.top()={interface? implements Ps Ms} //Ms is free var and is ok
    ClassB l=p.top();
    //Ps'=collect(p,Ps)
    List<Path> ps1 = Methods.collect(p,l.getSupertypes());
    //Ms'=methods(p,This0), {C:e in Ms} //norm now put all the nested classes in the back.
    List<ClassB.Member> ms1 = Stream.concat(
      p.methods(Path.outer(0)).stream(),
      l.getMs().stream().filter(m->m instanceof ClassB.NestedClass)
      ).map(m->norm(p,m)).collect(Collectors.toList());
    //return l.withSupertypes(ps1).withMs(ms1).withUniqueId(p.getFreshId()).withPhase(Phase.Norm);
    return new ClassB(l.getDoc1(),l.getDoc2(),l.isInterface(),ps1,ms1,l.getP(),l.getStage(),Phase.Norm,p.getFreshId());
    }
  @SuppressWarnings("unchecked")
  static <T extends ExpCore.ClassB.Member> T norm(Program p,T m){
    return m.match(
      nc->(T)nc.withE(norm(p,nc.getE())),//modify here to decrese performance but reduce evilpushes, by doing push(C) in case e is L
      mi->(T)Assertions.codeNotReachable(),
      mt->(T)mt.withMt(resolve(p,mt.getMt()))
               .with_inner(mt.get_inner().map(e->norm(p,e)))
      );
    }

  static Program auxMultiNorm(Program p, List<List<String>>topPaths){
    ClassB lTop=p.top();
    for(List<String> csi:topPaths){
//  pi = p.navigate(Csi)
//  Li = norm(pi)//norming the top
//  L = p.top()[Cs1=L1..Csn=Ln] //replace the nested classes in paths Csi with libraries Li.
      Program pi=p.navigate(csi);
      ClassB li=norm(pi);
      lTop=lTop.onClassNavigateToPathAndDo(csi, _l->li);
      }
    return p.updateTop(lTop);
    }
  
  static Program multiNorm(Program p, Paths paths){
//- multiNorm(p,empty) = p
      if(paths.isEmpty()){return p;}
//- multiNorm(p, Cs1..Csn)/*a single Css*/= p.update(L)
      Paths popped=paths.pop();
      if(popped.isEmpty()){
        return auxMultiNorm(p,paths.current);
        }
//- multiNorm(p, Css,Csss) =multiNorm(p',Css) 
//  p'=p.growFellow(multiNorm(p.pop(), Csss))
      Program rec=multiNorm(p.pop(),popped);
      Program p1=p.growFellow(rec);
      return auxMultiNorm(p1,paths.current);
      }
    }

