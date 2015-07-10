package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import coreVisitors.FromInClass;
import ast.ErrorMessage;
import ast.ExpCore.*;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
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
  private static void redirectOk(List<PathPath> s, Program p, ClassB l, Member mi, Optional<Member> ifInDom, PathPath currentPP, List<PathPath> result) {
    // TODO Auto-generated method stub

  }

}