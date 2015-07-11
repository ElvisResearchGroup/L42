package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import coreVisitors.FromInClass;
import introspection.IntrospectionAdapt;
import tools.Assertions;
import ast.ErrorMessage;
import ast.ExpCore.*;
import ast.Ast.NormType;
import ast.Ast.HistoricType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathPath;
import auxiliaryGrammar.Program;
public class Redirect {
  public static ClassB redirect(Program p,ClassB cb, Path internal,Path external){
    //call redirectOk, if that is ok, no other errors?
    //should cb be normalized first?
    p=p.addAtTop(cb);//TODO: is it ok? if so add in docs
    List<PathPath>toRedirect=redirectOk(Collections.emptyList(),p,cb,internal,external);
    return IntrospectionAdapt.applyMapPath(p,cb,toRedirect);
  }
  public static List<PathPath> redirectOk(List<PathPath>s,Program p,ClassB l, Path csPath,Path path){
    PathPath currentPP=new PathPath(csPath,path);
    if(s.contains(currentPP)){return Collections.emptyList();}
    List<String>cs=csPath.getCBar();
    ExtractInfo.checkExistsPathMethod(l, cs, Optional.empty());
    ast.Ast.Doc[] csComm=new ast.Ast.Doc[]{null};
    ClassB l0=(ClassB)FromInClass.of(Program.extractCBar(cs,l,csComm),csPath);//L(Cs)[from Cs]=L0={H M0 ... Mn}
    //path exists by construction.
    ClassB l0Dest=(ClassB)FromInClass.of(p.extract(path),path);//p(Path)[from Path]=L0'={H' M0' ... Mn', _}//reordering of Ms allowed here
    //(a)Cs is public in L, and Cs have no private state;
    if(csComm[0].isPrivate()){throw new AssertionError("GETAMESSAGE");}//could disappear with pre-normalization
    boolean privateState=ExtractInfo.hasPrivateState(l0);
    if(privateState){throw new AssertionError("GETAMESSAGE");}//src not fully abstract
    //all its methods have no implementation, that is:
    //for all Mi,i=0..n: Mi is of form h or Mi is of form C:_
    for(Member m:l0.getMs()){
      m.match(
        nc->null,
        mi->{throw new AssertionError("GETAMESSAGE");},//src not fully abstract
        mt->{if(mt.getInner().isPresent()){throw new AssertionError("GETAMESSAGE");}return null;}//src not fully abstract
        );
    }
    //(b) L[H=~H'] holds
    boolean headerOk=l0.isInterface()==l0Dest.isInterface();
    if(!headerOk || l0.isInterface()){
      if(ExtractInfo.isVirginInterface(l, cs)){headerOk=true;}
    }
    if(!headerOk){throw new AssertionError("GETAMESSAGE");}//classClash?
    //(c) S;p|-L[M0=~M0' Cs->Path]:S0 ... S;p|-L[Mn=~Mn' Cs->Path]:Sn
    //(d) S'=Cs->Path,S0..Sn
    List<PathPath>result=new ArrayList<PathPath>();
    result.add(currentPP);
    for(Member mi:l0.getMs()){
      redirectOk(s,p,l,mi,Program.getIfInDom(l0Dest.getMs(),mi),currentPP,result);
    }
    return result;
  }
  private static void redirectOk(List<PathPath> s, Program p, ClassB l, Member mi, Optional<Member> _miPrime, PathPath currentPP, List<PathPath> result) {
    if(!_miPrime.isPresent()){throw new AssertionError("GETAMESSAGE");}//incompatibleSrcDest, method not found
    Member miPrime=_miPrime.get();
    //if the member is not of the same type is an error
    if(!mi.getClass().equals(miPrime.getClass())){throw new AssertionError("GETAMESSAGE");}//incompatibleSrcDest or MethodClash?
    mi.match(
      nc->redirectOkNc(s,p,l,nc,(NestedClass)miPrime,currentPP,result),
      errMi->{throw Assertions.codeNotReachable("Should be catched before as in fully abstract source");},
      mt->redirectOkMt(s,p,l,mt,(MethodWithType)miPrime,currentPP,result));    
  }
  private static Void redirectOkMt(List<PathPath> s, Program p, ClassB l, MethodWithType mt, MethodWithType mtPrime, PathPath currentPP, List<PathPath> result) {
    List<PathPath> sPrime=new ArrayList<>(s);
    sPrime.add(currentPP);
    redirectOkType(sPrime,p,l,mt.getMt().getReturnType(),mtPrime.getMt().getReturnType(),result);
    for(int i=0;i<mt.getMt().getTs().size();i+=1){
      redirectOkType(sPrime,p,l,mt.getMt().getTs().get(i),mtPrime.getMt().getTs().get(i),result);
    }
    redirectOkExceptions(sPrime,p,l,mt.getMt().getExceptions(),mtPrime.getMt().getExceptions(),result);
    return null;
  }
  private static void redirectOkExceptions(List<PathPath> s, Program p, ClassB l, List<Path> exceptions, List<Path> exceptionsPrime, List<PathPath> result) {
    if(exceptionsPrime.isEmpty()){return;}
    Set<Path> exc=new HashSet<>(exceptions);
    Set<Path> excPrime=new HashSet<>(exceptionsPrime);
    if(exc.containsAll(excPrime)){return;}
    if(exceptions.size()!=1){
      int numInternal=0; for(Path pi:exceptions){if (pi.outerNumber()==0){numInternal+=1;}}
      if(numInternal==0){//if all external, then is
        throw new AssertionError("GETAMESSAGE");//incompatible srcDest: more exceptions are thrown then expected
        }
      if(numInternal==1){//and there are externals, 
        throw new AssertionError("GETAMESSAGE");//incompatible srcDest: internal/external exceptions can not cooperate in satisfy external requirements
      }//else, more then 1 internal
      throw new AssertionError("GETAMESSAGE");//incompatible srcDest:  only one internal exception allowed to satisfy external requirements
      }
    if(exceptionsPrime.size()!=1){throw new AssertionError("GETAMESSAGE");}//incompatible srcDest: more exceptions are thrown then expected
    redirectOkPath(s, p, l, exceptions.get(0),exceptionsPrime.get(0), result);
    
  }
  private static void redirectOkType(List<PathPath> s, Program p, ClassB l, Type t, Type tPrime, List<PathPath> result) {
    if(!t.getClass().equals(tPrime.getClass())){throw new AssertionError("GETAMESSAGE");}//incompatible internal/external types t1 t2
    t.match(
      normType->{
        NormType ntP=(NormType)tPrime;
        if(!normType.getMdf().equals(ntP.getMdf())){throw new AssertionError("GETAMESSAGE");}//incompatible internal/external types t1 t2
        if(!normType.getPh().equals(ntP.getPh())){throw new AssertionError("GETAMESSAGE");}//incompatible internal/external types t1 t2
        redirectOkPath(s,p,l,normType.getPath(),ntP.getPath(),result);
        return null;
      },
      hType->{
        HistoricType htP=(HistoricType)tPrime;
        if(!hType.getSelectors().equals(htP.getSelectors())){throw new AssertionError("GETAMESSAGE");}//incompatible internal/external types t1 t2
        if(hType.isForcePlaceholder()!=htP.isForcePlaceholder()){throw new AssertionError("GETAMESSAGE");}//incompatible internal/external types t1 t2
        redirectOkPath(s,p,l,hType.getPath(),htP.getPath(),result);
        return null;
      });
  }
  private static void redirectOkPath(List<PathPath> s, Program p, ClassB l,Path cs, Path path, List<PathPath> result) {
    //S;p|-L[Outern::Cs =~Outern::Cs]:emptyset  holds with n>0
    if(cs.outerNumber()>0){
      if(!cs.equals(path)){throw new AssertionError("GETAMESSAGE");}//incompatible external types t1 t2
      return;
    }
    //otherwise
    //S;p|-L[Outer0::Cs =~ Path ]: S'
    //if S;p|-L[redirect Cs->Path]:S'
    List<PathPath> res = redirectOk(s,p,l,cs,path);
    result.addAll(res);
  }
  private static Void redirectOkNc(List<PathPath> s, Program p, ClassB l, NestedClass nc, ClassB.NestedClass miPrime, PathPath currentPP, List<PathPath> result) {
    //S;p|-L[C:L1=~C:L1' Cs->Path]:S'
    //if S,Cs->Path;p|-L[redirect Cs::C->Path::C]:S'
    Path src=currentPP.getPath1().pushC(nc.getName());
    Path dest=currentPP.getPath2().pushC(nc.getName());
    List<PathPath> res = redirectOk(s,p,l,src,dest);
    result.addAll(res);
    return null;
  }

}