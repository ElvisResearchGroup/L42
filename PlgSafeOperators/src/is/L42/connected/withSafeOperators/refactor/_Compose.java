/*
All composition operations are 
expected to run only on normalized LCs, and to produce a normalized LC.
in a normalized LC,
-Ps reports all the transitivelly implemented interfaces
-all methods are methods with type (mwt), with refine if declared in an implemented interface
-all the nested classes are after the methods in the list of members

When run on well typed LCs, it will produce a well typed LC.

Definition for sum: 
p|-L1 sum L2=L
is a weakly associative and weakly commutative operation.
Note: sum of coherent LCs can produce a (well typed but) non-coherent LC

#weak associativity
sum is a partial function:
if a+(b+c)=d and (a+b)+c=d' then d=d'
but is not guaranteed a+(b+c)=(a+b)+c 

#weak associativity
a+b sim b+a
sim allows differences in the order of implemented
interfaces, declared methods and nested classes.  

_______
#define
p|-L sum L'=L2
  where
  empty|-L sumPs L'=L1;Css
  empty;0;p.evilPush(L1);L;L';Css|-L sumAll L'=L2

_______
#define M[Ms]=M? //note, this is similar to nc.C(ncs) but is 'empty' instead of 'undefined'
nc[nc1..ncn]=nci if nci.C=nc.C
mwt[mwt1..mwtn]=mwti if mwti.ms=mwt.ms
otherwise=empty

_______
#define
Cs|-L1 sumPs L2= L;Css
{interface?1 implements Ps1 mwts1 nc1..nck} sumPs {interface?2 implements Ps2 mwts2 ncs2}
= { {interface?1,interface?2} implements Ps1,Ps2 mwts, ncs};Css
 where
 Cs|-nci sumPs nci[ncs2]= nci';Cssi
 mwts=mwts1,mwts2\dom(mwts1)
 ncs=nc1',..nck',ncs2\dom(nc1..nck)
 if {interface?1,interface?2}=interface
   Css=Css1,..,Cssk,Cs
 else
   Css=Css1,..,Cssk

_______
#define Cs|-nc1 sumPS nc2=nc;Css
Cs|-C:L1 sumPS C:L2 = C: Cs.C|-L1 sumPS L2 
Cs|-nc sumPS empty = nc;empty

SArg::=Cs;n;p;LC1;LC2;Css  //sum arguments
// Cs=path from top,
//n=how many nested inside library literals inside method bodies
//p= program including a approximate result as p.top()
//LC1 and LC2= the top libraries
//Css= paths from top of all interfaces composed by the sum 

_______
#define
SArg|-L1 sumAll L2=L
SArg|- {interface?1 implements Ps1 mwt1..mwtn nc1..nck} 
    sumAll {interface?2 implements Ps2 mwts2 ncs2}
    =SArg|-Isum({interface? implements Ps mwts ncs})
  where
  interface?=interface?1 mwts1 +interface?2 mwts2
  Ps=collect(SArg.p,Ps1, Ps2)
  SArg.p|-mwti sumAll mwti[mwts2]=mwti'
  mwts=mwt1',..,mwtn',mwts2\dom(mwt1..mwtn)
  SArg|-nci sumAll nci[ncs2]= nci'
  ncs=nc1'..ncn' ncs2\dom(nc1..nck)
  SArg|-Isum(L)=L'

_______
#define SArg|-nc1 sumAll nc2=nc
SArg|-C:L1 sumAll C:L2 = C: SArg'[with p=p.push(C)]|-L1 sumAll L2
if SArg.n=0 SArg'=SArg[with Cs=SArg.Cs.C]
else SArg'=SArg[with n=SArg.n+1]
_______
#define SArg|-Isum(L)=L[with mwts=SArg|-IsumDeep(mwts)]
  forall Pi in L.Ps, we name 
    This0.Csi=Pi[remove n outer][from This0.Cs]//if the result is not This0, it implements an outer interface
    mwtsi = p|-LC1(Csi).mwts + LC2(Csi).mwts//could be cached
    mwtsi'=mwtsi[from Psi]\dom(L.mwts)
  mwts0=[with msi in (mwts1',..,mwtsn').mss
    SArg.p|-max(msi[mwts1'],..,msi[mwtsn'])]
  mwts=[with mwti in L.mwts
    SArg.p|-mwti sumAll addRefine(mwti[mwts0]))
    ],mwts0\dom(L.mwts)  
  if any mwtsi existed:  
    mss={mwt.ms: mwt in mwts, mwt is refine}
    //check still 1 no refine
    forall msi in mss exists exactly 1 Pi in L.Ps such that
      p(Pi)(ms)=mh//no refine
   
    //check for all refine method valid <=; keep in mind p.top() is not normalized
    forall msi in mss, mwts(msi)=mwt
      forall Pi in Ps where Csi exists,
        mwt.ms in dom(p(Pi))
        mwt1=LC1(Csi)(mwt.ms), mwt2=LC2(Csi)(mwt.ms)
        check p|-mwt.mh << mwt1.mh and p|-mwt.mh << mwt2.mh
  
_______
#define SArg|-IsumDeep(L)= SArg|-Isum(L0)
  L0=L[with ncs=ncs]
  ncs= SArg|-IsumDeep(L.ncs)

_______
#define SArg|-IsumDeep(C:L)= C: L0
SArg|-IsumDeep(C:L)= C: SArg'[with p=Sarg.p.push(C)]|-IsumDeep(L)
if SArg.n=0 SArg'=SArg[with Cs=SArg.Cs.C]
else SArg'=SArg[with n=SArg.n+1]

_______
#define SArg|-IsumDeep(refine? mh e?)= refine? mh e?'
  e?'= explore the structure of e? and
    replace every L with 
    SArg[witn n=SArg.n+1][with p=SArg.p.push(L)]|-IsumDeep(L)

_______
#define interface?1 mwts1+interface?2 mwts2=interface?
interface?1 mwts1 + interface?2 mwts2 = interface?2 mwts2 + interface?1 mwts1
interface? mwts1+interface? mwts2=interface?
otherwise
mwts1 + interface mwts2=interface
  where
  mwts1.e?s = {empty}
  class notin mwts1.mhs.mdfs
  
_______
#define p|-mwt1 sumAll mwt2=mwt
p|-mwt1 sumAll mwt2
    ={mwt1.refine?,mwt2.refine?} p|-max(mwt1.mh,mwt2.mh) {mwt1.e?,mwt2.e?}
  where
  empty in {mwt1.e?,mwt2.e?}
//This allows a non refine member to become refine

  
  
 ---------
 Example of difficoult case:
    {A:{interface<B} B:{interface} C:{}}+{A:{interface} C:{<A}}
    =must be
    {A:{interface<B} B:{interface} C:{<A,B}}
    indeed
    {A:{interface<B} B:{interface} C:{<A}} +A,B,empty
    =
    {A:{interface<B} B:{interface} C:{<A,B}}
  so it seams to work for this case...
  
  //what happens if
   * {I:{interface} J:{interface} A:{<I,J}}+{{I:{interface method S m()} J:{interface method S m()}}

  
  
  Notation
A->B::=A1:B1.. An:Bn
and define functional access, dom
well formedness on single key,
comma raw composition and a[b] update composition

    
*/
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
import newTypeSystem.TypeSystem;
import programReduction.Program;
import tools.Map;

public class _Compose {

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
    SumOut res = nestedCompose(pData,a,b,a,b,Collections.emptyList());
    if(res.isOk()){return res.toOk().res;}
    SumErr err=res.toError();
    //meth clash
    //  meth refine fail
    //class clash
    //induced circular implement
    if(err.meth1!=null){
      //will be meth clash?
      }
    return null;
    }
    
  //return the composed nested that should fit in position path
  public static SumOut nestedCompose(Program pData,
      ClassB topA, ClassB topB,ClassB a,ClassB b,
      List<Ast.C> path){
    SumOut[] res={new SumOk(null)};
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
    if(isClassClash(path,topA,topB,a,b)){return new SumErr(null,null,path);}
    boolean isInterface=a.isInterface() || b.isInterface();
    res[0].toOk().res=new ClassB(newDoc,isInterface,newTs,newMwt,newNested,newP,a.getPhase().acc(b.getPhase()),0);
    for(Function<Program, SumErr> f:res[0].toOk().lateChecks){
    SumErr err=f.apply(pData);
      if(err==null){continue;}
      return err;      
      }
    return res[0];
    }
  private static List<MethodWithType> newMwts(Program pData,
      ClassB topA, ClassB topB, ClassB a, ClassB b,
      List<Ast.C> path, SumOut[] out) {
    List<MethodWithType> newMwt=new ArrayList<>();
    List<MethodWithType> bmwts=new ArrayList<>(b.mwts());
    for(MethodWithType mai : a.mwts()){    
      MethodWithType mbi=Util._findAndRemove(bmwts, mai.getMs());
      if(mbi!=null){
        SumOutM res =methodCompose(pData,topA,topB,mai,mbi, path);
        assert res!=null;
        if(!res.isOk()){out[0]=res.toError();return null;}
    assert out[0].isOk();
    newMwt.add(res.toOkM().res);
    out[0].toOk().accumulateChecks(res.toOkM());
    }
  else {newMwt.add(mai);}
  }
  //all the bmwts survived are to be added
  newMwt.addAll(bmwts);
  return newMwt;
  }
  private static List<NestedClass> newNesteds(Program pData,
      ClassB topA, ClassB topB, ClassB a, ClassB b,
      List<Ast.C> path, SumOut[] out) {
    List<NestedClass> newNested=new ArrayList<>();
    List<NestedClass> bns=new ArrayList<>(b.ns());
    for(NestedClass nai : a.ns()){
      NestedClass nbi=Util._findAndRemove(bns, nai.getName());
      if(nbi!=null){
        SumOut res =nestedCompose(pData,topA,topB,(ClassB)nai.getE(),(ClassB)nbi.getE(), Util.push(path,nai.getName()));
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
  
  private static SumErr recomputeImplements(Program p,List<C> path,List<Type> newTs) {
    p=p.navigate(path);
    try{
      List<Path> res = programReduction.Methods.collect(p,Map.of(t->t.getNT().getPath(), newTs));
      newTs.clear();
      for(Path pi:res){newTs.add(pi.toImmNT());}
      return null;
      }
    catch(ast.ErrorMessage.CircularImplements ci){
      return new SumErr(null,null,path); 
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
  
public static SumOutM methodCompose(Program p,
    ClassB topA, ClassB topB,
    MethodWithType ma, MethodWithType mb,
    List<C> path) {
  List<Type>excRes=excRes(p,ma.getMt().getExceptions(),mb.getMt().getExceptions());
  NormType resA=ma.getMt().getReturnType().getNT();
  NormType resB=mb.getMt().getReturnType().getNT();
  NormType res;
  if(null==TypeSystem.subtype(p,resB,resA)){
    res=resB;
    }
  else if(null==TypeSystem.subtype(p,resA,resB)){
    res=resA;
    }
  else{ return new SumErr(ma,mb,path);}
   hgjhgkjgkjg
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

class SumOkM implements SumOutM{
  MethodWithType res; SumOkM(MethodWithType res){this.res=res;}
  @Override public SumOkM toOkM() {return this;}
  @Override public boolean isOk() {return true;}
  }
class SumOk implements SumOut{
  ClassB res; SumOk(ClassB res){this.res=res;}
  List<Function<Program,SumErr>> lateChecks=new ArrayList<>();//location of where it was wrong, or null
  public void accumulateChecks(SumOk that){this.lateChecks.addAll(that.lateChecks);}
  @Override public boolean isOk() { return true;}
  @Override public SumOk toOk() {return this;}
  }

class SumErr implements SumOutM,SumOut{
  public SumErr(MethodWithType meth1,MethodWithType meth2,List<Ast.C> path){
    this.meth1=meth1;this.meth2=meth2;this.path=path;}
  MethodWithType  meth1;
  MethodWithType  meth2;
  List<Ast.C> path;
  public boolean isOk() { return false;}
  public SumErr toError() {return this;}
  }

interface SumOutM{
  boolean isOk();
  default SumOkM toOkM() {throw new Error();}
  default SumErr toError() {throw new Error();}
}
interface SumOut{
  boolean isOk();
  default SumOk toOk() {throw new Error();}
  default SumErr toError() {throw new Error();}
}
/*interface OkOr2<T extends OkOr2<T,E>, E extends OkOr2<T,E> >{
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
*/
