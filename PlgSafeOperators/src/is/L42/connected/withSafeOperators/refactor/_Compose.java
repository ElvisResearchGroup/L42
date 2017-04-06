/*
All composition operations are 
expected to run only on normalized LCs, and to produce a normalized LC.
When run on well typed LCs, it will produce a well typed LC.
Definiton for sum: formally 
p|-L1 sum L2=L
will be a weakly associative and weakly commutative operation.
Note: sum of coherent LCs can produce a (well typed but) not coherent LC

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
p|-L sum L'=L3
  where
  empty|-L sum1 L'=L1,Cs->Ps, Cs->mhs
  empty|-L1 sum2 Cs->Ps=L2
  empty;p.evilPush(L2)|-L2 sum3 P->mhs = L3  

Notation
A->B::=A1:B1.. An:Bn
and define functional access, dom
well formedness on single key,
comma raw composition and a[b] update composition
_______
#define
Cs|-L1 sum1 L2=L; Cs->Ps; Cs->mhs
{interface?1 implements Ps1 mwt1..mwtn nc1..nck}+{interface?2 implements Ps2 mwts2 ncs2}
  ={interface? implements Ps mwts ncs}; Cs->Ps; Cs->mhs
  where
  interface?=interface?1 mwts1 +interface?1 mwts2
  Ps=Ps1, Ps2
  mwti[mwts2]=mwti',mh?i
  mwts=mwt1'..mwtn' mwts2\dom(mwt1..mwtn)
  Cs|-nci[ncs2]= nci';Cs->Psi;Cs->mhsi
  ncs=nc1'..ncn' ncs2\dom(nc1..nck)
  if interface?=empty, 
    Cs->Ps= Cs->Ps1,..,Cs->Psk
    Cs->mwts= Cs:mh?1..mh?n,Cs->ms1,..,Cs->msk 
  else
    Cs->Ps= Cs->Ps1,..,Cs->Psk,Cs:Ps
    Cs->mwts= Cs:mh?1..mh?n,Cs->ms1,..,Cs->msk,Cs:mwts.mhs 

_______
#define inteface?1 mwts1+inteface?2 mwts2=interface?
inteface?1 mwts1 + inteface?2 mwts2 = inteface?2 mwts2 + inteface?1 mwts1
inteface? mwts1+inteface? mwts2=interface?
otherwise
mwts1 + interface mwts2=interface
  where
  mwts1.e?s = {empty}
  class notin mwts1.mhs.mdfs

_______
#define mwt[mwts]=mwt';mh?
mwt[mwt1..mwtn]=mwt +mwti if mwt.ms=mwti.ms
mwt[mwts]=mwt if mwt.ms notin dom(mwts) 

_______
#define Cs|-nc[ncs]=nc';Cs->Ps;Cs->mhs
Cs|-nc[nc1..ncn]=Cs|-nc + nci if nc.C=nci.C
Cs|-nc[ncs]=nc;empty;empty if nc.C notin dom(nc)

_______
#define mwt1+mwt2=mwt; mh?
refine? mh + refine?' mh' e= {refine?,refine?'} mh' e;mh
refine? mh e? +refine?' mh'= {refine?,refine?'} mh e?; mh'
//This allows a non refine member to become refine
//in case mh=mh', we can avoid to generate the element in the map

_______
#define Cs|-nc1+nc2=nc;Cs->Ps;Cs->mhs
C:L1+C:L2=C: L;Cs->Ps;Cs->mhs
  where
   Cs.C|-L1+L2=L;Cs->Ps;Cs->mhs

_______
#define
Cs|-L1 sum2 Cs->Ps=L2

Cs,n|-{interface? implements Ps mwts ns} sum2 Cs->Ps
  ={interface? 
    implements Ps,(Ps'[from Pi])
    Cs,n|-mwts sum2 Cs->Ps
    Cs,n|-ns sum2 Cs->Ps
    }
  where
  Pi in Ps,
  Pi[remove n outer][from This0.Cs]=This0.Csi //if the result is not This0, it implements an outer interface
  Csi:Ps' in Cs->Ps
  
Otherwise, if no such Pi in Ps
Cs|-{interface? implements Ps mwts ns} sum2 Cs->Ps
  ={interface? implements Ps
    Cs,n;p|-mwts sum2 Cs->Ps
    Cs,n;p|-ns sum2 Cs->Ps
    }
    
_______
#define
Cs,0|-C:L sum2 Cs->Ps = C: Cs.C,0|-L sum2 Cs->Ps
Cs,n+1|-C:L sum2 Cs->Ps = C: Cs,n+2|-L sum2 Cs->Ps
_______
#define
Cs,n|-refine? mh e? sum2 Cs->Ps = refine? mh Cs,n+1|-e? sum2 Cs->Ps

_______
#define Cs,n|-e+Cs->Ps= e'
  clone the structure and replace 
  all L with Cs,n|-L+Cs->Ps

_______
#define
Cs,n;p|-L2 sum3 Cs->mhs = L3  

Cs,n;p|-{interface? implements Ps mwts ns} sum3 Cs->mhs
  ={interface? 
    implements p|-Ps
    p|-(Cs,n;p|-mwts sum3 Cs->mhs)+mhs
    Cs,n;p|-ns sum3 Cs->mhs
    }
  where
  either
    Pi in Ps,
    Pi[remove n outer][from This0.Cs]=This0.Csi //if the result is not This0, it implements an outer interface
    Csi:Ps' in Cs->mhs
  or
    n=0 and Cs:Ps' in Cs->mhs
Otherwise,
Cs|-{interface? implements Ps mwts ns} sum3 Cs->mhs
  ={interface? implements Ps
    p|-(Cs,n;p|-mwts sum3 Cs->mhs)+mhs
    Cs,n;p|-ns sum3 Cs->mhs
    }

_______
#define p|-P1..Pn = Ps
  Pi in Ps if forall Pj in {Pi+1..Pn} not p.equiv(Pi,Pj)  
  undefined if p.equiv(Pi,This0) //circularity error

_______
#define p|-mwts+mhs=mwts'
p|-mwt1..mwtn+mhs=mwts'
  =p|-mwt1[mhs]..p|-mwtn[mhs] addRefine(mhs\dom(mwt1..mwtn))

_______
#define p|-mwt[mhs]=mwt'
p|-mwt[mhs]=mwt if mwt.ms notin dom(mhs)
p|-mwt[mh1..mhn]= p|-mwt+mhi if mwt.ms =mhi.ms

_______
#define p|-mwt+mh=mwt'
p|-mwt+mh=mwt
  where
  p|-mwt.mh<mh
p|-mwt+mh=mwt.with[mh=mh]
  where
  p|-mh<mwt.mh
  mwt.e?=empty
 
 ---------
 Example of difficoult case:
    {A:{interface<B} B:{interface} C:{}}+{A:{interface} C:{<A}}
    =must be
    {A:{interface<B} B:{interface} C:{<A,B}}
    indeed
    {A:{inteface<B} B:{interface} C:{<A}} +A,B,empty
    =
    {A:{inteface<B} B:{interface} C:{<A,B}}
  so it seams to work for this case...
    
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
