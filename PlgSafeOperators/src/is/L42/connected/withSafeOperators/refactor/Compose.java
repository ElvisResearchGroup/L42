/*
All composition operations are
expected to run only on normalized LCs, and to produce a normalized LC.
In a normalized LC,
-Ps reports all the transitivelly implemented interfaces
-all methods in implemented interfaces are explicitly declared (with their type) in this library
-all the nested classes are after the methods in the list of members

When run on well typed LCs, it will produce a well typed LC  or fail for locally explicable reasons. Note: sum of coherent LCs can produce a (well typed but) non-coherent LC


#weak associativity
sum is a partial function:
if a+(b+c)=d and (a+b)+c=d' then d=d'
but is not guaranteed a+(b+c)=(a+b)+c

#weak commutativity
a+b sim b+a
sim allows differences in the order of implemented
interfaces, declared methods and nested classes.
*/


package is.L42.connected.withSafeOperators.refactor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;
import coreVisitors.CloneVisitor;
import coreVisitors.TestShapeVisitor;
import facade.PData;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.location.Lib;
import is.L42.connected.withSafeOperators.location.Location;
import is.L42.connected.withSafeOperators.location.Method;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import newTypeSystem.TypeSystem;
import programReduction.Methods;
import programReduction.Program;
import tools.Map;


interface ComposeSpec{
//TODO: comments at the start of the class:
//plg comments can ONLY be summed with empty comments, otherwise
//we may break typing. Redirect, in the same way, can not redirect classes
//containing plg comments!
/**<pre>#define L1 ++p L2 = L0
L1 ++p L2 = norm(p.evilPush(L0)) //TODO: in the implementation we just return L0 for now
  with L0=L1 +p.evilPush(L0) L2
  and norm(p.evilPush(L0))=L0 except for order of implemented interfaces and members
  //note, we need to refresh the private names here
</pre>*/void compose();

/**<pre>#define L1 +p L2 = L0
{interface?1 implements Ts mwts ncs} +p {interface?2 implements Ts' mwt1..mwtn nc1..nck}
   ={ (interface?1 mwts + interface?2 + mwt1..mwtn)
      implements (Ts\Ts',Ts')
      (mwts\dom(mwt1..mwtn) mwt1[mwts] * mwt1 .. mwtn[mwts] * mwtn)
      (ncs\dom(nc1..nck) nc1[ncs] * nc1 .. nck[ncs] * nck) }
  with *=interface?1,_  +p  interface?2,_
</pre>*/void innerCompose();

/**<pre>#define interface?,M? +p interface?1,M1 = M2
interface?,empty +p interface?1,M = M //M is the metavariable for member, introduced in notation and grammar
interface?1,C:L1 +p interface?2,C:L2 = C: L1 +p.push(C) L2
interface?1,refine?1 mh1 e?1 +p interface?2,refine?2 mh2 e?2= {refine?1,refine?2} mhi e?i
//we originally chose that allowing refine+non refine sum was more evil than good,
//but we want to implement interface methods without mentioning such interface, so we relaxed it
  with {i,j}={1,2} :
    p|-mhi<=mhj and e?j=empty,
    if interface?j=interface then p|-mhj<=mhi//that is mhi equiv mhj
//interface can not loose, implemented can not loose.
</pre>*/void sumMember();


/**<pre>#define interface?1 mwts1+interface?2 mwts2=interface?
interface?1 mwts1 + interface?2 mwts2 = interface?2 mwts2 + interface?1 mwts1
interface mwts1+interface mwts2=interface
mwts1 + interface mwts2=interface
  where
  mwts1.e?s = {empty}
  class notin mwts1.mhs.mdfs
  mwts1.mss not uniquely named
mwts1 +mwts2=empty
  with size({n| refine? mh in (mwts1,mwts2), mh.ms= m__n(xs)})<=1//note, since is not "mh e" we are asking for the abstract methods only

</pre>*/void isSumResultInterface();

/**<pre>#define p|-mh1<=mh2  //remember that p.equiv( P,P) hold even if p( P) undefined
p|-mh1<=mh2
  where p|-mh1.T<=mh2.T and parameters and exceptions are equiv, and mdfs are equal
  //similar to mh1[with T=mh2.T]=mh2
</pre>*/void methodTypeSubtype();

/**<pre>#define M[Ms]=M?
nc[nc1..ncn]=nci if nci.C=nc.C
mwt[mwt1..mwtn]=mwti if mwti.ms=mwt.ms
otherwise=empty
</pre>*/void selectMember();
}

public class Compose {
  ClassB leftTop;
  ClassB rightTop;
  List<Ast.C> stackCs=new ArrayList<>(16);
  private void popC(){
    assert !stackCs.isEmpty();
    stackCs.remove(stackCs.size()-1);
    }
  private void pushC(Ast.C c){
    stackCs.add(c);
    }
  public Compose(ClassB left,ClassB right){this.leftTop=left;this.rightTop=right;}
  /**{@link ComposeSpec#selectMember}*/
  public static MethodWithType _extractMwt(MethodWithType mwt,List<MethodWithType>mwts){
    for(MethodWithType mwti:mwts){if (mwti.getMs().equals(mwt.getMs())){return mwti;}}
    return null;
    }
  /**{@link ComposeSpec#selectMember}*/
  public static NestedClass  _extractNc(NestedClass nc,List<NestedClass>ncs){
    for(NestedClass nci:ncs){if (nci.getName().equals(nc.getName())){return nci;}}
    return null;
    }

/**{@link ComposeSpec#sumMember}*/
 ClassB.NestedClass sumNc(ClassB.NestedClass _nc1,Program p, ClassB.NestedClass nc2) throws MethodClash, ClassClash {
  if (_nc1==null){return nc2;}
  Program pi=p.push(nc2.getName());
  pushC(nc2.getName());
  try{
    ClassB l=innerCompose(pi,(ClassB)_nc1.getE(),(ClassB)nc2.getE());
    return _nc1
      .withE(l)
      .withDoc(_nc1.getDoc().sum(nc2.getDoc()))
      .withP(_nc1.getP().sum(nc2.getP()));
    }
  finally{popC();}
  }
  /**{@link ComposeSpec#compose}*/
  public static ClassB compose(PData pData,ClassB a,ClassB b) throws MethodClash, SubtleSubtypeViolation, ClassClash{
    return new Compose(a,b).compose(pData.p,a,b);}
  /**{@link ComposeSpec#compose}*/
  public ClassB compose(Program pp,ClassB a,ClassB b) throws MethodClash, SubtleSubtypeViolation, ClassClash{
    b=privateMangling.RefreshUniqueNames.refresh(b);
    return composeRefreshed(pp,a,b);
    }
  /**{@link ComposeSpec#compose}*/
  public ClassB composeRefreshed(Program pp,ClassB a,ClassB b) throws MethodClash, SubtleSubtypeViolation, ClassClash{
    ClassB forP=onlySubtypeCompose(a, b);
    Program p=pp.evilPush(forP);
    ClassB res=innerCompose(p,a,b);
    checkSubtleSubtypeViolation(pp.evilPush(res));
    return res;
    }
  private  void checkSubtleSubtypeViolation(Program p) throws RefactorErrors.SubtleSubtypeViolation {
    try{
      RefactorErrors.SubtleSubtypeViolation err=_checkSubtleSubtypeViolation(p);
      if(err!=null){throw err;}
      }
    catch(ErrorMessage em){
      throw new RefactorErrors.SubtleSubtypeViolation().msg(em.toString());
      }
    }
  private static RefactorErrors.SubtleSubtypeViolation _checkSubtleSubtypeViolation(Program p) {
    ClassB l=p.top();
    List<Ast.Type> ps1 = Methods.collect(p,l.getSupertypes());
    boolean superOk=superOk(p,l.getSupertypes(),ps1);
    if(!superOk){
      return new RefactorErrors.SubtleSubtypeViolation().msg("In "+l.getP()+"\n"+l.getSupertypes()+" does not contains all of "+ps1);
      }
    for(MethodWithType m :p.methods(Path.outer(0))){
      //there must be equivalent in l
      Member mi = l._getMember(m.getMs());
      if(mi==null){return new RefactorErrors.SubtleSubtypeViolation().msg("In "+l.getP()+"\n"+" Selector "+m.getMs()+" not found");}
      if(m.get_inner()==null){continue;}
      //we trust the mt to be the same
      //we trust the e is the same except for nested L, where we use recursion
      ExpCore body=m.getInner();
      RefactorErrors.SubtleSubtypeViolation[]out={null};
      body.accept(new TestShapeVisitor(){
        @Override public Boolean visit(ClassB cb){
          if(out[0]!=null){return false;}
          out[0]=_checkSubtleSubtypeViolation(p.evilPush(cb));
          return out[0]==null;
        }});
      if(out[0]!=null){return out[0];}
      }
    for(NestedClass nci:l.ns()){
      RefactorErrors.SubtleSubtypeViolation out=_checkSubtleSubtypeViolation(p.push(nci.getName()));
      if(out!=null){return out;}
      }
    return null;
    }

private static boolean superOk(Program p,List<Type> all, List<Type> some) {
//check all.containsAll(some)
  for(Type tAll:all)out:{
    for(Type tSome:some){
      if (p.equiv(tAll,tSome)){break out;}
      }
    return false;
    }
  return true;
  }
/**{@link ComposeSpec#sumMember}*/
public static  MethodWithType sumMwtij(Program p,MethodWithType mwti,MethodWithType mwt1,MethodWithType mwt2){
  return mwti.withDoc(mwt1.getDoc().sum(mwt2.getDoc())).withP(mwt1.getP().sum(mwt2.getP()));
  }

/**{@link ComposeSpec#sumMember}*/
public  MethodWithType sumMwt(Program p,boolean interface1,MethodWithType mwt1,boolean interface2,MethodWithType mwt2) throws MethodClash{
  if (mwt1==null){return mwt2;}
  MethodType mt1=mwt1.getMt();
  MethodType mt2=mwt2.getMt();
  boolean refine=mt1.isRefine() ||mt2.isRefine();
  mt1=mt1.withRefine(refine);
  mt2=mt2.withRefine(refine);
  mwt1=mwt1.withMt(mt1);
  mwt2=mwt2.withMt(mt2);
  return sumMwtAux(p,interface1,mwt1,interface2,mwt2);
  }

/**{@link ComposeSpec#sumMember}*/
public  MethodWithType sumMwtAux(Program p,boolean interface1,MethodWithType mwt1,boolean interface2,MethodWithType mwt2) throws MethodClash{
    //assign to i,j: if one has body, is i. Else, we need to check for
    //if only one is interface, is i.
    //if both interface, methods must be equiv
    //if no interfaces, try mt1<=mt2, then try the other way
    MethodType mt1=mwt1.getMt();
    MethodType mt2=mwt2.getMt();
//    if(mt1.isRefine()!=mt2.isRefine()){
//      throw makeMethodClash(mwt1, mwt2).msg("sum of refine and non refine methods:\n"+mwt1+"\n"+mwt2);
//      }
    if(mwt1.get_inner()!=null){
      assert !interface1 && !interface2;
      if(mwt2.get_inner()!=null){
        throw makeMethodClash(mwt1, mwt2).msg("both sides are implemented");
        }
      checkMtGt(p,mwt1,mwt2,mt1,mt2);
      return sumMwtij(p,mwt1,mwt1,mwt2);}
    if(mwt2.get_inner()!=null){checkMtGt(p,mwt1,mwt2,mt2,mt1);return sumMwtij(p,mwt2,mwt1,mwt2);}
    if(interface1 && !interface2){checkMtGt(p,mwt1,mwt2,mt1,mt2);return sumMwtij(p,mwt1,mwt1,mwt2);}
    if(interface2 && !interface1){checkMtGt(p,mwt1,mwt2,mt2,mt1);return sumMwtij(p,mwt2,mwt1,mwt2);}
    if(interface1 && interface2){
      checkMtEq(p,mwt1,mwt2,mt1,mt2);
      return sumMwtij(p,mwt1,mwt1,mwt2);
      }
    assert !interface1 && !interface2;
    if(mtGT(p,mt1,mt2)){return sumMwtij(p,mwt1,mwt1,mwt2);}
    if(mtGT(p,mt2,mt1)){return sumMwtij(p,mwt2,mwt1,mwt2);}
    throw makeMethodClash(mwt1, mwt2).msg("Neither of the method is subtype of the other: "+mwt1.getMt()+" "+mwt2.getMt());
    }
private MethodClash makeMethodClash(MethodWithType mwt1, MethodWithType mwt2) {
  return new MethodClash(Method.of(mwt1,leftTop,stackCs),Method.of(mwt2,leftTop,stackCs));
  }
private ClassClash makeClassClash() {
  return new ClassClash(
    Lib.newFromLibrary(leftTop).navigateCs(stackCs),
    Lib.newFromLibrary(rightTop).navigateCs(stackCs)
    );
}

/**{@link ComposeSpec#methodTypeSubtype}*/
public static boolean mtGT(Program p, MethodType mt1, MethodType mt2) {
  if (!p.subtypeEq(mt1.getReturnType(), mt2.getReturnType())){return false;}
  return mtEqRest(p,mt1,mt2);
}
/**{@link ComposeSpec#methodTypeSubtype}*/
private void checkMtGt(Program p, MethodWithType mwt1, MethodWithType mwt2,MethodType mt1, MethodType mt2) throws MethodClash {
  if(!mtGT(p,mt1,mt2)){throw makeMethodClash(mwt1, mwt2);}
  }
/**{@link ComposeSpec#methodTypeSubtype()}*/
private void checkMtEq(Program p, MethodWithType mwt1, MethodWithType mwt2,MethodType mt1, MethodType mt2) throws MethodClash  {
  if (!p.equiv(mt1.getReturnType(), mt2.getReturnType())){
    throw makeMethodClash(mwt1, mwt2);
    }
  if(!mtEqRest(p,mwt1.getMt(),mwt2.getMt())){
    throw makeMethodClash(mwt1, mwt2).msg("Incompatible types:\n  left: "+mt1+"\n  right: "+mt2);
    }
}
/**{@link ComposeSpec#methodTypeSubtype}*/
private static boolean mtEqRest(Program p, MethodType mt1, MethodType mt2) {
  if(mt1.getMdf()!=mt2.getMdf()){return false;}
  assert mt1.getTs().size()==mt2.getTs().size();
  for(int i=0;i<mt1.getTs().size();i++){
    if(!p.equiv(mt1.getTs().get(i), mt2.getTs().get(i))){return false;}
  }
  assert mt1.getExceptions().size()==mt2.getExceptions().size();
  for(int i=0;i<mt1.getExceptions().size();i++){
    if(!p.equiv(mt1.getExceptions().get(i), mt2.getExceptions().get(i))){return false;}
  }
  return true;
}

public  ClassB onlySubtypeCompose(ClassB a,ClassB b){
  List<Type> impls=new ArrayList<>(a.getSupertypes());
  for(Type ti:b.getSupertypes()){impls.remove(ti);}
  impls.addAll(b.getSupertypes());
  List<MethodWithType>mwts=Collections.emptyList();
  List<NestedClass>ncs=new ArrayList<>(a.ns());
  for(NestedClass nci: b.ns()){Functions._findAndRemove(ncs,nci.getName());}
  for(NestedClass nci: b.ns()){
    NestedClass ncj=_extractNc(nci,a.ns());
    if(ncj==null){ncs.add(nci);}
    else {
      ClassB l=onlySubtypeCompose((ClassB)nci.getE(),(ClassB)ncj.getE());
      ncs.add(nci.withE(l));
      }
    }
  return new ClassB(Doc.empty(),false,impls,mwts,ncs,a.getP().sum(b.getP()),Phase.Norm,-2);
  }

/**{@link ComposeSpec#innerCompose}*/
public ClassB innerCompose(Program p,ClassB a,ClassB b) throws MethodClash, ClassClash{
  boolean interf=isSumResultInterface(a, b);
  List<Type> impls=p.top().getSupertypes();
  List<MethodWithType>mwts=new ArrayList<>(a.mwts());
  for(MethodWithType mwti: b.mwts()){Functions._findAndRemove(mwts,mwti.getMs());}
  for(MethodWithType mwti: b.mwts()){
    mwts.add(sumMwt(p,a.isInterface(),_extractMwt(mwti,a.mwts()),b.isInterface(),mwti));
    }
  List<NestedClass>ncs=new ArrayList<>(a.ns());
  for(NestedClass nci: b.ns()){Functions._findAndRemove(ncs,nci.getName());}
  for(NestedClass nci: b.ns()){
    ncs.add(sumNc(_extractNc(nci,a.ns()),p,nci));
    }
  return new ClassB(a.getDoc1().sum(b.getDoc1()),interf,impls,mwts,ncs,a.getP().sum(b.getP()),Phase.Norm,-2);
  }

//handles sum of two classes with private state and sum class/interface invalid
  /**{@link ComposeSpec#isSumResultInterface}*/
  public boolean isSumResultInterface(ClassB currentA,ClassB currentB) throws ClassClash{
    if(currentA.isInterface()&&currentB.isInterface()){return true;}
    if(!currentA.isInterface()&&!currentB.isInterface()){
      boolean privateA=ExtractInfo.hasPrivateState(currentA);
      boolean privateB=ExtractInfo.hasPrivateState(currentB);
      if (privateA && privateB){throw makeClassClash();}
      return false;
      }
    if(currentA.isInterface()){
      ClassB tmp=currentA; currentA=currentB;currentB=tmp;
      }
    assert !currentA.isInterface();
    boolean implA=!ExtractInfo.isNoImplementation(currentA);
    boolean privateA=ExtractInfo.hasPrivateState(currentA);
    boolean classA=!currentA.mwts().stream().allMatch(mwt->mwt.getMt().getMdf()!=Mdf.Class);
    if(implA ||privateA||classA){throw makeClassClash();}
    return true;
    }
}