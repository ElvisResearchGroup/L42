package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import coreVisitors.FromInClass;
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
    return null;
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
    if(csComm[0].isPrivate()){throw new AssertionError("GETAMESSAGE");}
    boolean privateState=ExtractInfo.hasPrivateState(l0);
    if(privateState){throw new AssertionError("GETAMESSAGE");}
    //all its methods have no implementation, that is:
    //for all Mi,i=0..n: Mi is of form h or Mi is of form C:_
    for(Member m:l0.getMs()){
      m.match(
        nc->null,
        mi->{throw new AssertionError("GETAMESSAGE");},
        mt->{if(mt.getInner().isPresent()){throw new AssertionError("GETAMESSAGE");}return null;}
        );
    }
    //(b) L[H=~H'] holds
    boolean headerOk=l0.isInterface()==l0Dest.isInterface();
    if(!headerOk || l0.isInterface()){
      if(ExtractInfo.isVirginInterface(l, cs)){headerOk=true;}
    }
    if(!headerOk){throw new AssertionError("GETAMESSAGE");}
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
    if(!_miPrime.isPresent()){throw new AssertionError("GETAMESSAGE");}
    Member miPrime=_miPrime.get();
    //if the member is not of the same type is an error
    if(!mi.getClass().equals(miPrime.getClass())){throw new AssertionError("GETAMESSAGE");}
    mi.match(
      nc->redirectOkNc(s,p,l,nc,(NestedClass)miPrime,currentPP,result),
      errMi->{throw Assertions.codeNotReachable("Should be catched before as in fully abstract source");},
      mt->redirectOkMt(s,p,l,mt,(MethodWithType)miPrime,currentPP,result));    
  }
  private static Void redirectOkMt(List<PathPath> s, Program p, ClassB l, MethodWithType mt, MethodWithType mtPrime, PathPath currentPP, List<PathPath> result) {
    redirectOkType(s,p,l,mt.getMt().getReturnType(),mtPrime.getMt().getReturnType(),result);
    for(int i=0;i<mt.getMt().getTs().size();i+=1){
      redirectOkType(s,p,l,mt.getMt().getTs().get(i),mtPrime.getMt().getTs().get(i),result);
    }
    redirectOkExceptions(s,p,l,mt.getMt().getExceptions(),mtPrime.getMt().getExceptions(),result);
    return null;
  }
  private static void redirectOkExceptions(List<PathPath> s, Program p, ClassB l, List<Path> exceptions, List<Path> exceptions2, List<PathPath> result) {
    // TODO Auto-generated method stub
    
  }
  private static void redirectOkType(List<PathPath> s, Program p, ClassB l, Type t, Type tPrime, List<PathPath> result) {
    if(!t.getClass().equals(tPrime.getClass())){throw new AssertionError("GETAMESSAGE");}
    t.match(
      normType->{
        NormType ntP=(NormType)tPrime;
        if(!normType.getMdf().equals(ntP.getMdf())){throw new AssertionError("GETAMESSAGE");}
        if(!normType.getPh().equals(ntP.getPh())){throw new AssertionError("GETAMESSAGE");}
        redirectOkPath(s,p,normType.getPath(),ntP.getPath(),result);
        return null;
      },
      hType->{
        HistoricType htP=(HistoricType)tPrime;
        if(!hType.getSelectors().equals(htP.getSelectors())){throw new AssertionError("GETAMESSAGE");}
        if(hType.isForcePlaceholder()!=htP.isForcePlaceholder()){throw new AssertionError("GETAMESSAGE");}
        redirectOkPath(s,p,hType.getPath(),htP.getPath(),result);
        return null;
      });
  }
  private static void redirectOkPath(List<PathPath> s, Program p, Path path, Path path2, List<PathPath> result) {
    // TODO Auto-generated method stub
    
  }
  private static Void redirectOkNc(List<PathPath> s, Program p, ClassB l, NestedClass nc, ClassB.NestedClass miPrime, PathPath currentPP, List<PathPath> result) {
    // TODO Auto-generated method stub
    return null;
  }

}