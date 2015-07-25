package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import tools.Assertions;
import tools.Map;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.HistoricType;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ErrorMessage.NotOkToStar;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import coreVisitors.From;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import coreVisitors.NormE;
import facade.Configuration;

public class Norm {

    public static MethodImplemented of(Program p,MethodImplemented mi) {
    return mi.withInner(of(p,mi.getInner()));
  }
  public static NestedClass of(Program p,NestedClass nc) {
    Program p2=p;
    p2=p2.navigateInTo(nc.getName());
    return nc.withInner(of(p2,nc.getInner()));
  }
  public static ExpCore of(Program p,ExpCore e) {
    return NormE.of(p, e);
  }
  public static ClassB ofAllMethodsOf(Program p,ClassB cb,boolean isOnlyType) {
    //Program p1=p.addAtTop(cb);
    List<Member> ms = new ArrayList<Member>();
    for( Member m:cb.getMs()){
      m.match(
        nt->ms.add(nt),
        mi->{if(!isOnlyType){ms.add(Norm.of(p,mi));}return null;},
        mt->ms.add(Norm.of(p,mt,isOnlyType))
        );
    }
    return cb.withMs(ms);
  }
  public static MethodWithType of(Program p,MethodWithType mt,boolean isOnlyType) {
    MethodType t=mt.getMt();
    List<Type> ts=new ArrayList<Type>();
      for(Type tt:t.getTs()){ts.add(of(p,tt));}
    MethodType mt2=new MethodType(
        t.getDocExceptions(),
        t.getMdf(),
        ts,//already normalized before
        t.getTDocs(),
       of(p,t.getReturnType()),
       Map.of(pi->of(p,pi),t.getExceptions())
       );
    Optional<ExpCore> eInner =mt.getInner();
    if(isOnlyType && eInner.isPresent()){Optional.of((ExpCore)forOnlyType);}
    if(!isOnlyType){eInner = Map.of(e->of(p,e),mt.getInner());}
    return new MethodWithType(mt.getDoc(),mt.getMs(),mt2,eInner);
  }
  private static final ClassB forOnlyType=new ClassB(Doc.factory("Only the type was asked\n"),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),Stage.None);


  public static NormType of(Program p, Type t) {
    try{
      NormType result= t.match(nt->nt, ht->resolve(p,ht));
      return result.withPath(of(p,result.getPath()));
    }
    catch(ErrorMessage.ProgramExtractOnMetaExpression ne){
      throw new ErrorMessage.NormImpossible(t, ne, p.getInnerData());
    }
  }

  private static NormType resolve(Program p, HistoricType t) {
  if(t.getSelectors().size()==1){return resolvePh(resolveOne(p,t),t.isForcePlaceholder());}
  assert t.getSelectors().size()>1;
  return resolvePh(resolveMany(p,t),t.isForcePlaceholder());
  }
  private static NormType resolvePh(NormType nt,boolean forcePh){
    if(!forcePh){return nt;}
    return nt.withPh(Ph.Ph);
  }
  private static NormType resolveMany(Program p, HistoricType t) {
    HistoricType tOne=new HistoricType(t.getPath(), t.getSelectors().subList(0,1),false);
    NormType nt=resolveOne(p,tOne);
    HistoricType tNext=new HistoricType(nt.getPath(),t.getSelectors().subList(1, t.getSelectors().size()),false);
    return resolve(p,tNext);
  }

  private static NormType resolveOne(Program p, HistoricType t) {
  //ClassB cb=p.extract(t.getPath());
   Path pt=t.getPath();
   pt=Norm.of(p, pt);
  MethodWithType mt = p.method(pt, t.getSelectors().get(0).getMs(),true);
  String x=t.getSelectors().get(0).getX();
  if(x.isEmpty()){
    return of(p,mt.getMt().getReturnType());
    }
  if(x.equals("this")){
    return new NormType(mt.getMt().getMdf(),t.getPath(),Ph.None);
    }
  int i=mt.getMs().getNames().indexOf(x);
  assert i!=-1:
    mt.getMs()+" but searched for "+ x+" in "+t;
  Type ti=mt.getMt().getTs().get(i);
  return of(p,ti);
  }

  public static  Path of(Program p,Path path){
    if(path.isPrimitive()){return path;}
    assert path.isCore():path;
    int n=path.outerNumber();
    if(n==0){return path;}
    //String outern=path.getRowData().get(0);
    if(path.getRowData().size()==1){return path;}
    String c=path.getRowData().get(1);
    ClassB myLib=p.get(n);
    List<Member> ms=myLib.getMs();
    Optional<Member> om=Program.getIfInDom(ms, c);
    boolean isPass=false;
    if(om.isPresent()){
      NestedClass m=(NestedClass)om.get();
      if(m.getInner() instanceof ExpCore.WalkBy){isPass=true;};
      }
    if(!isPass){return path;}
    List<String> result=new ArrayList<String>();
    result.add("Outer"+(n-1));
    result.addAll(path.getRowData().subList(2,path.getRowData().size()));
    return of(p,new Path(result));
  }
  /*public static void ctorOkAndAllCanBeNormalized(Program p,ClassB ct){
    //also check that all the members can be normalized!

    if(ct.getStage()==Stage.Less){throw new ErrorMessage.NotOkToStar(ct,null,"Is still StageLess");}
    if(!Configuration.typeExtraction.isCt(ct)){throw new ErrorMessage.NotOkToStar(ct,null,"Full type can not  be extracted yet");}
    if(!IsCompiled.of(ct)){throw new ErrorMessage.NotOkToStar(ct,null,"Not fully compiled");}
    //classTypes well formedness:
    HashSet<Object>domCt=new HashSet<>();
    for(Member m:ct.getMs()){m.match(
      nc->domCt.add(nc.getName()),
      mi->domCt.add(mi.getS()),
      mt->domCt.add(mt.getMs())
      );}
    if(domCt.size()!=ct.getMs().size()){
      throw new ErrorMessage.NotWellFormed(ct.accept(new InjectionOnSugar()),null,"repetition in nested class name or method selectors");
      }

    //All nested class names $\C$ in a class must be unique.
//All methods in a given class must be uniquely identified by their name $\m$ and the sequence of their parameter names $\xMany$.
    checkCtorAndThatAllCanBeNormalized(p, ct);
  }*/
  /*public static void checkCtorAndThatAllCanBeNormalized(Program p, ClassB ct) {
    MethodWithType ctor=null;
    Program p2=p.addAtTop(ct);
    for(Member m:ct.getMs()){
      if(!(m instanceof MethodWithType)){continue;}
      MethodWithType mt=(MethodWithType)m;
      mt = normMethodOrRetrow(ct, mt, p2,false);
      if(mt.getMt().getMdf()!=Mdf.Type){continue;}
      if(!mt.isFieldGenerated()){continue;}
      ctor=mt;
      }
    if(ctor==null){return;}
    //ctor = normMethodOrRetrow(ct, ctor, p2);
    boolean haveNotImmOrType=false;
    boolean haveLentRead=false;
    for(Type t:ctor.getMt().getTs()){
      assert t instanceof NormType;
      NormType nt=(NormType)t;
      if(nt.getMdf()!=Mdf.Immutable&&nt.getMdf()!=Mdf.Type){
        haveNotImmOrType=true;
        }
      if(nt.getMdf()==Mdf.Lent||nt.getMdf()==Mdf.Readable){
        haveLentRead=true;
        }
      }
    Mdf mdf=((NormType)ctor.getMt().getReturnType()).getMdf();
    if(mdf==Mdf.Capsule||mdf==Mdf.Immutable){
      if(haveNotImmOrType){throw new ErrorMessage.NotOkToStar(ct,ctor,"Not valid to declare immutable or capsule constructor");}
    }
    if(haveLentRead){
      if(mdf!=Mdf.Lent && mdf!=Mdf.Readable){throw new ErrorMessage.NotOkToStar(ct,ctor,"Declared constructor must be lent or read");}
    }
  }*/
  public static MethodWithType normMethodOrRetrow(ClassB ct,
      MethodWithType mt, Program p2,boolean isOnlyType) {
    try{mt=of(p2, mt,isOnlyType);}
    catch(ErrorMessage em){
      ErrorMessage e=new ErrorMessage.NotOkToStar(ct,mt,"Norm impossible");
      e.initCause(em);
      throw e;
    }
    catch(StackOverflowError em){
      ErrorMessage e=new ErrorMessage.NotOkToStar(ct,mt,"Norm impossible");
      e.initCause(em);
      throw e;
    }
    return mt;
  }

}
