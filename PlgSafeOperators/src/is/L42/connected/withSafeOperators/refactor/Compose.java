package is.L42.connected.withSafeOperators.refactor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.location.Location;
import programReduction.Program;
import tools.Map;

public class Compose {

  //will be needed for other operations... may be sum need to cooperate? late checks are an issue...
  /*public static boolean matchNested(Program p, ClassB top, List<Member> ms, List<Ast.C> current, NestedClass nc1, NestedClass nc2) {
    NestedClass resNc=nc1.withDoc(nc1.getDoc().sum(nc2.getDoc()));
    SumResN res = nestedCompose(p, top, top,(ClassB)nc1.getE(), (ClassB)nc2.getE(),current);
    if(!res.isOk()){return false;}
    SumOkN resOk=res.toOk();
    resOk.
  }*/
  //public static boolean matchMwt(Program p, ClassB topA, ClassB topB, List<Member> ms, List<Ast.C> current, Member m, Member oms) {


  public static ClassB compose(Program pData,ClassB a,ClassB b){
    b=privateMangling.RefreshUniqueNames.refresh(b);
    return alradyRefreshedCompose(pData,a,b);
    }
  public static ClassB alradyRefreshedCompose(Program pData,ClassB a,ClassB b){
    SumResN res = nestedCompose(pData,a,b,a,b,Collections.emptyList());
    if(res.isOk()){return res.toOk().res;}
    SumErr err=res.toError();
    //meth clash
    //  meth refine fail
    //class clash
    //induced circular implement
    if(err.meth!=null){
      //will be meth clash?
      }
    return null;
    }
    
  //return the composed nested that should fit in position path
  public static SumResN nestedCompose(Program pData,
      ClassB topA, ClassB topB,ClassB a,ClassB b,
      List<Ast.C> path){
    SumResN[] res={new SumOkN()};
    List<NestedClass> newNested = newNesteds(pData, topA, topB, a, b, path, res);
    if(!res[0].isOk()){return res[0];}
    List<MethodWithType> newMwt = newMwts(pData, topA, topB, a, b, path, res);
    if(!res[0].isOk()){return res[0];}    
    List<Type> newTs = new ArrayList<>(a.getSupertypes());
    newTs.addAll(b.getSupertypes());
    if(implementsNeedRecomputing(newTs,path)){
      res[0].toOk().lateChecks.add(pD->recomputeImplements(pD, path, newTs));
      }
    Doc newDoc=a.getDoc1().sum(b.getDoc1());
    Position newP=a.getP().sum(b.getP());
    if(isClassClash(path,topA,topB,a,b)){return new SumErrN(null,path);}
    boolean isInterface=a.isInterface() || b.isInterface();
    res[0].toOk().res=new ClassB(newDoc,isInterface,newTs,newMwt,newNested,newP,a.getPhase().acc(b.getPhase()),0);
    for(Function<Program, SumErrN> f:res[0].toOk().lateChecks){
    SumErrN err=f.apply(pData);
      if(err==null){continue;}
      return err;      
      }
    return res[0];
    }
  private static List<MethodWithType> newMwts(Program pData,
      ClassB topA, ClassB topB, ClassB a, ClassB b,
      List<Ast.C> path, SumResN[] out) {
    List<MethodWithType> newMwt=new ArrayList<>();
    List<MethodWithType> bmwts=new ArrayList<>(b.mwts());
    for(MethodWithType mai : a.mwts()){    
      MethodWithType mbi=Util._findAndRemove(bmwts, mai.getMs());
      if(mbi!=null){
        SumResM res =methodCompose(pData,topA,topB,mai,mbi, path);
        if(!res.isOk()){out[0]=(SumResN)res.toError();return null;}
    assert out[0].isOk();
    newMwt.add(res.toOk().res);
    out[0].toOk().accumulateChecks(res.toOk());
    }
  else {newMwt.add(mai);}
  }
  //all the bmwts survived are to be added
  newMwt.addAll(bmwts);
  return newMwt;
  }
  private static List<NestedClass> newNesteds(Program pData,
      ClassB topA, ClassB topB, ClassB a, ClassB b,
      List<Ast.C> path, SumResN[] out) {
    List<NestedClass> newNested=new ArrayList<>();
    List<NestedClass> bns=new ArrayList<>(b.ns());
    for(NestedClass nai : a.ns()){
      NestedClass nbi=Util._findAndRemove(bns, nai.getName());
      if(nbi!=null){
        SumResN res =nestedCompose(pData,topA,topB,(ClassB)nai.getE(),(ClassB)nbi.getE(), Util.push(path,nai.getName()));
        if(!res.isOk()){ out[0]=res;return null;}
        newNested.add(nai.withDoc(nai.getDoc().sum(nbi.getDoc())).withE(res.toOk().res));
        assert out[0].isOk();
        out[0].toOk().accumulateChecks(res.toOk());
        }
      else {
        ClassB cb=(ClassB)nai.getE();
        if(implementsNeedRecomputing(cb.getSupertypes(),path)){
          List<Type>newTs=new ArrayList<>(cb.getSupertypes());
          out[0].toOk().lateChecks.add(pD->recomputeImplements(pD,path,newTs));
          newNested.add(nai.withE(cb.withSupertypes(newTs)));
          }
        else {newNested.add(nai);}
        }
    }
    //all the bns survived are to be added
    for(NestedClass bn:bns){
      ClassB cb=(ClassB)bn.getE();
      if(implementsNeedRecomputing(cb.getSupertypes(),path)){
      List<Type>newTs=new ArrayList<>(cb.getSupertypes());
      out[0].toOk().lateChecks.add(pD->recomputeImplements(pD,path,newTs));
      newNested.add(bn.withE(cb.withSupertypes(newTs)));
      }
      else {newNested.add(bn);}
    }    
    return newNested;
    }
  
  private static SumErrN recomputeImplements(Program p,List<C> path,List<Type> newTs) {
    p=p.navigate(path);
    try{
      List<Path> res = programReduction.Methods.collect(p,Map.of(t->t.getNT().getPath(), newTs));
      newTs.clear();
      for(Path pi:res){newTs.add(pi.toImmNT());}
      return null;
      }
    catch(ast.ErrorMessage.CircularImplements ci){
      return new SumErrN(null,path); 
      }
    }
  private static boolean implementsNeedRecomputing(List<Type> ts,List<C> path) {
    int pSize=path.size();
    for(Type t:ts){
      if (t.getNT().getPath().outerNumber()<=pSize){return true;}
      }
    return false;
    }
//handles sum of two classes with private state and sum class/interface invalid
  public static boolean isClassClash(
          List<Ast.C>current,
          ClassB topA,ClassB topB,
          ClassB currentA,ClassB currentB){
       boolean privateA=ExtractInfo.hasPrivateState(currentA);
       boolean privateB=ExtractInfo.hasPrivateState(currentB);
       boolean twoPrivateState=privateA &&privateB;
       boolean isAllOk= !twoPrivateState && currentA.isInterface()==currentB.isInterface();
       if (isAllOk){return false;}
       ExtractInfo.ClassKind kindA=ExtractInfo.classKind(topA,current,currentA,null,privateA,null);
       ExtractInfo.ClassKind kindB=ExtractInfo.classKind(topB,current,currentB,null,privateB,null);
       boolean isClassInterfaceSumOk=currentA.isInterface()==currentB.isInterface();
       if(!isClassInterfaceSumOk){
         isClassInterfaceSumOk=kindA==ExtractInfo.ClassKind.FreeTemplate||kindB==ExtractInfo.ClassKind.FreeTemplate;
         }
       isAllOk= !twoPrivateState && isClassInterfaceSumOk;
       if (isAllOk){return false;}
       return true;
      }
  private static List<Type> excRes(Program p, List<Type>a,List<Type>b){
    List<Type> res = excFilter(p,a,b);
    res.addAll(excFilter(p,b,a));
    return res;
    }
  private static List<Type> excFilter(Program p, List<Type>mayStay,List<Type>other){
    List<Type>res=new ArrayList<>();
    //res={ti in mayStay | exists tj in other s.t. p|-ti<=tj }
    for(Type ti:mayStay){
      for(Type tj:other){
        if(p.subtypeEq(ti.getNT().getPath(),tj.getNT().getPath())){
          res.add(ti);
          }
        }
      }
    return res;
    }
  
public static SumResM methodCompose(Program pData,
  ClassB topA, ClassB topB,
  MethodWithType ma, MethodWithType mb,
  List<C> path) {
  List<Type>excRes=excRes(pData,ma.getMt().getExceptions(),mb.getMt().getExceptions());
  NormType res=ma.getMt().getReturnType().getNT();
  NormType resB=mb.getMt().getReturnType().getNT();
  //should I refactor and remove ph/add ph in mdf? if(res.getMdf()!=resB.getMdf())
  if(pData.subtypeEq(resB.getPath(),res.getPath())){
    res=resB;
    }
  
// TODO Auto-generated method stub
//a+b= c,(cs|-P1<=P2)s or error?
return null;
}
}
class Util{
//TODO: put in functions, and use everywhere?
static <T> List<T> push(List<T> that, T elem){
  List<T> res=new ArrayList<T>(that);
  res.add(elem);
  return res;
  }
static NestedClass _findAndRemove(List<NestedClass> ns,Ast.C that){
  for(int i=0;i<ns.size();i++){
    NestedClass ni=ns.get(i);
    if(!ni.getName().equals(that)){continue;}
    ns.remove(i);
    return ni;
    }
  return null;
  }
static MethodWithType _findAndRemove(List<MethodWithType> mwts,Ast.MethodSelector that){
for(int i=0;i<mwts.size();i++){
  MethodWithType mwti=mwts.get(i);
  if(!mwti.getMs().equals(that)){continue;}
  mwts.remove(i);
  return mwti;
  }
return null;
}

}

//-------------------Data structures----------

interface SumResN extends OkOr<SumOkN,SumOk,SumErr>{}
interface SumResM extends OkOr<SumOkM,SumOk,SumErr>{}
class SumOkN extends SumOk implements SumResN{
  ClassB res;
  public SumOkN toOk() {return this;}
  }
class SumOkM extends SumOk implements SumResM{
  MethodWithType res;
  public SumOkM toOk() {return this;}
  }
class SumOk implements OkOr2<SumOk,SumErr>{
  List<Function<Program,SumErrN>> lateChecks=new ArrayList<>();//location of where it was wrong, or null
  public void accumulateChecks(SumOk that){this.lateChecks.addAll(that.lateChecks);}
  public boolean isOk() { return true;}
  public SumOk toOk() {return this;}
  }
class SumErrN extends SumErr implements SumResN{
    public SumErrN(MethodWithType meth, List<C> path) {super(meth, path);}
    }
class SumErrM extends SumErr implements SumResM{
public SumErrM(MethodWithType meth, List<C> path) {super(meth, path);}
}
abstract class SumErr implements OkOr2<SumOk,SumErr>{
  public SumErr(MethodWithType meth,List<Ast.C> path){this.meth=meth;this.path=path;}
  MethodWithType  meth;
  List<Ast.C> path;
  public boolean isOk() { return false;}
  public SumErr toError() {return this;}
  }
interface OkOr2<T extends OkOr2<T,E>, E extends OkOr2<T,E> >{
  boolean isOk();
  default T toOk() {throw new Error();}
  default E toError() {throw new Error();}
  }
interface OkOr<T extends T0,T0 extends OkOr2<T0,E>,E extends OkOr2<T0,E>> extends OkOr2<T0,E>{
  default T toOk() {throw new Error();}
  }
class MyOk implements OkOr2<MyOk,MyErr>{
  public boolean isOk() { return true;}
  public MyOk toOk() {return this;}
  }
class MyErr implements OkOr2<MyOk,MyErr>{
  public boolean isOk() { return false;}
  public MyErr toError() {return this;}
  }
