package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import coreVisitors.FromInClass;
import facade.Configuration;
import introspection.FindUsage;
import introspection.IntrospectionAdapt;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
import is.L42.connected.withSafeOperators.Pop.PopNFrom;
import tools.Assertions;
import ast.ErrorMessage;
import ast.ExpCore.*;
import ast.Ast.NormType;
import ast.Ast.Doc;
import ast.Ast.HistoricType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMx;
import ast.Util.PathPath;
import auxiliaryGrammar.Program;
public class Redirect {
  public static ClassB redirect(Program p,ClassB cb, Path internal,Path external){
    //call redirectOk, if that is ok, no other errors?
    //should cb be normalized first?
    assert external.isPrimitive() || external.outerNumber()>0;
    p=p.addAtTop(Configuration.typeSystem.typeExtraction(p, cb));//TODO: is it ok? if so add in docs
    List<PathPath>toRedirect=redirectOk(Collections.emptyList(),p,cb,internal,external);
    return IntrospectionAdapt.applyMapPath(p,cb,toRedirect);
  }
  public static List<PathPath> redirectOk(List<PathPath>s,Program p,ClassB l, Path csPath,Path path){
    PathPath currentPP=new PathPath(csPath,path);
    if(s.contains(currentPP)){return Collections.emptyList();}
    List<String>cs=csPath.getCBar();
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(l, cs, Optional.empty());
    Boolean[] csPrivate=new Boolean[]{false};
    ClassB l0=Program.extractCBar(cs,l,csPrivate);//L(Cs)[from Cs]=L0={H M0 ... Mn}//No, from does not work here
    l0=(ClassB)new PopNFrom(cs.size()).visit(l0);
    //path exists by construction.
    ClassB l0Dest;
    if(path.isCore()){
      ClassB l0Raw = p.extract(path);
      Path toFrom=path.setNewOuter(path.outerNumber()-1);
      toFrom=toFrom.popC();//as in IntrospectionAdapt
      l0Dest=(ClassB)FromInClass.of(l0Raw,toFrom);
      }//p(Path)[from Path]=L0'={H' M0' ... Mn', _}//reordering of Ms allowed here
    else{
      assert path.isPrimitive();
      l0Dest=new ClassB(Doc.empty(),Doc.empty(),path.equals(Path.Any()),Collections.emptyList(),Collections.emptyList(),Stage.None);
    }
    //(a)Cs is public in L, and Cs have no private state;
    boolean isPrivate=csPrivate[0];
    boolean isPrivateState=ExtractInfo.hasPrivateState(l0);
    //all its methods have no implementation, that is:
    //for all Mi,i=0..n: Mi is of form h or Mi is of form C:_
   boolean isNoImplementation=ExtractInfo.isNoImplementation(l0);
    //(b) L[H=~H'] holds
    boolean headerOk=l0.isInterface()==l0Dest.isInterface();
    Boolean isFreeInterface=null;
    Boolean isBox=null;
    if(!headerOk && l0.isInterface()){
      isFreeInterface=ExtractInfo.isFreeInterface(l,l0, cs);
      if(isFreeInterface){headerOk=true;}
    }
    if(!headerOk && !l0.isInterface()){
      isBox=ExtractInfo.isBox(l,l0, cs);
      if(isBox){headerOk=true;}
    }
    //(c) S,Cs->Path;p|-L[Paths=~Paths']:S'
    //(d) S;p|-L[M0=~M0' Cs->Path]:S0 ... S;p|-L[Mn=~Mn' Cs->Path]:Sn
    //(e) S'=Cs->Path,S0..Sn
    List<PathPath>result=new ArrayList<PathPath>();
    result.add(currentPP);
    Set<Path>unexpectedI=redirectOkImpl(s,currentPP,p,l,l0.getSupertypes(),l0Dest.getSupertypes(),result);
    List<Member> unexpectedMembers=new ArrayList<>();
    for(Member mi:l0.getMs()){
      Optional<Member> miPrime = Program.getIfInDom(l0Dest.getMs(),mi);
      if(miPrime.isPresent() && miPrime.get().getClass().equals(mi.getClass())){
          redirectOk(s,p,l,mi,miPrime.get(),currentPP,result);
      }
      else{unexpectedMembers.add(mi);}
    }
    boolean isOk=true;
    if(!unexpectedMembers.isEmpty()){isOk=false;}
    if(!unexpectedI.isEmpty()){isOk=false;}
    if(!headerOk){isOk=false;}
    if(isPrivate){isOk=false;}
    if(isOk){return result;}
    List<Path>unexpectedInterfaces=new ArrayList<>(unexpectedI);
    Collections.sort(unexpectedInterfaces,(pa,pb)->pa.toString().compareTo(pb.toString()));
    ClassKind kindSrc = ExtractInfo.classKind(l,cs,l0, isBox, isFreeInterface, isPrivateState, isNoImplementation);
    ClassKind kindDest = ExtractInfo.classKind(null,null,l0Dest,null,null,null,null);
    throw Errors42.errorSourceUnfit(currentPP.getPath1().getCBar(),path,
        kindSrc,kindDest,unexpectedMembers, headerOk, unexpectedInterfaces, isPrivate);
  }
  private static Set<Path> redirectOkImpl(List<PathPath> s, PathPath currentPP, Program p, ClassB l, List<Path> paths, List<Path> pathsPrime, List<PathPath> result) {
    //(paths ok)//and I can not use it for exceptions since opposite subset relation
    //S;p|-L[Paths=~Paths']:S'
    //Path subsetof Path'
    //or Paths=Path, Paths'=Path' and S;p|-L[Path=~Path']:S'
    List<PathPath> sPrime=new ArrayList<>(s);
    sPrime.add(currentPP);
    if(paths.isEmpty()){return Collections.emptySet();}
    Set<Path> ps=new HashSet<>(paths);
    Set<Path> psPrime=new HashSet<>(pathsPrime);
    ps.removeAll(pathsPrime);
    psPrime.removeAll(paths);
    if(ps.isEmpty()){return Collections.emptySet();}
    if(ps.size()!=1){return ps;}
    if(psPrime.size()!=1){return ps;}
    boolean pathOk=redirectOkPath(sPrime, p, l,ps.iterator().next(),psPrime.iterator().next(), result);
    if(pathOk){return Collections.emptySet();}
    return ps;
    }
  private static void redirectOk(List<PathPath> s, Program p, ClassB l, Member mi, Member miPrime, PathPath currentPP, List<PathPath> result) {
    //from before I know the members mi, miPrime are of the same class.
    mi.match(
      nc->redirectOkNc(s,p,l,nc,(NestedClass)miPrime,currentPP,result),
      errMi->{throw Assertions.codeNotReachable("Should be catched before as in fully abstract source");},
      mt->redirectOkMt(s,p,l,mt,(MethodWithType)miPrime,currentPP,result));
  }
  private static Void redirectOkMt(List<PathPath> s, Program p, ClassB l, MethodWithType mt, MethodWithType mtPrime, PathPath currentPP, List<PathPath> result) {
    List<PathPath> sPrime=new ArrayList<>(s);
    sPrime.add(currentPP);
    boolean isMdfOk=mt.getMt().getMdf()==mtPrime.getMt().getMdf();
    boolean isRetTypeOk=redirectOkType(sPrime,p,l,mt.getMt().getReturnType(),mtPrime.getMt().getReturnType(),result);
    List<Integer> wrongTypes=new ArrayList<>();
    for(int i=0;i<mt.getMt().getTs().size();i+=1){
      boolean isOkType=redirectOkType(sPrime,p,l,mt.getMt().getTs().get(i),mtPrime.getMt().getTs().get(i),result);
      if(!isOkType){wrongTypes.add(i);}
    }
    Set<Path> badExc=redirectOkExceptions(sPrime,p,l,mt.getMt().getExceptions(),mtPrime.getMt().getExceptions(),result);
    if(!badExc.isEmpty() || !wrongTypes.isEmpty()|| !isRetTypeOk ||!isMdfOk){
      throw Errors42.errorMethodClash(currentPP.getPath1().getCBar(),mt,mtPrime, badExc.isEmpty(), wrongTypes, isRetTypeOk, isMdfOk);
    }
    return null;
  }
  private static Set<Path> redirectOkExceptions(List<PathPath> s, Program p, ClassB l, List<Path> exceptions, List<Path> exceptionsPrime, List<PathPath> result) {
    if(exceptionsPrime.isEmpty()){return Collections.emptySet();}
    Set<Path> exc=new HashSet<>(exceptions);
    Set<Path> excPrime=new HashSet<>(exceptionsPrime);
    excPrime.removeAll(exceptions);
    if(excPrime.isEmpty()){return  Collections.emptySet();}
    exc.removeAll(exceptionsPrime);
    if(exc.size()!=1){return exc;}
    if(excPrime.size()!=1){return exc;}//ok not excPrime
    boolean pathOk=redirectOkPath(s, p, l, exc.iterator().next(),exceptionsPrime.iterator().next(), result);
    if(pathOk){ return Collections.emptySet();}
    return exc;
  }
  private static boolean redirectOkType(List<PathPath> s, Program p, ClassB l, Type t, Type tPrime, List<PathPath> result) {
    if(!t.getClass().equals(tPrime.getClass())){return false;}//incompatible internal/external types t1 t2
    Boolean[] pathOk={true};
    t.match(
      normType->{
        NormType ntP=(NormType)tPrime;
        if(!normType.getMdf().equals(ntP.getMdf())){return false;}//incompatible internal/external types t1 t2
        if(!normType.getPh().equals(ntP.getPh())){return false;}//incompatible internal/external types t1 t2
        pathOk[0]=redirectOkPath(s,p,l,normType.getPath(),ntP.getPath(),result);
        return null;
      },
      hType->{
        HistoricType htP=(HistoricType)tPrime;
        if(!hType.getSelectors().equals(htP.getSelectors())){return false;}//incompatible internal/external types t1 t2
        if(hType.isForcePlaceholder()!=htP.isForcePlaceholder()){return false;}//incompatible internal/external types t1 t2
        pathOk[0]=redirectOkPath(s,p,l,hType.getPath(),htP.getPath(),result);
        return null;
      });
    return pathOk[0];
  }
  private static boolean redirectOkPath(List<PathPath> s, Program p, ClassB l,Path cs, Path path, List<PathPath> result) {
    //S;p|-L[Outern::Cs =~Outern::Cs]:emptyset  holds with n>0
    if(cs.isPrimitive() ||cs.outerNumber()>0){
      if(!cs.equals(path)){return false;}
      return true;
    }
    //otherwise
    //S;p|-L[Outer0::Cs =~ Path ]: S'
    //if S;p|-L[redirect Cs->Path]:S'
    List<PathPath> res = redirectOk(s,p,l,cs,path);
    result.addAll(res);
    return true;
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
  static void checkPrivacyCoupuled(ClassB cbFull,ClassB cbClear, List<String> path) {
  //start from a already cleared out of private states
  //check if all private nested classes are USED using IsUsed on cbClear
  //this also verify that no private nested classes are used as
  //type in public methods of public classes.
  //collect all PublicPath.privateMethod
  //use main->introspection.FindUsage
  List<Path>prPath=ExtractInfo.collectPrivatePathsAndSubpaths(cbFull,path);
  List<PathMx>prMeth=ExtractInfo.collectPrivateMethodsOfPublicPaths(cbFull,path);
  List<Path>coupuledPaths=new ArrayList<>();
  for(Path pi:prPath){
    Set<Path> used = ExtractInfo.IsUsed.of(cbClear,pi);
    if(used.isEmpty()){continue;}
    coupuledPaths.add(pi);
  }
  Set<PathMx> usedPrMeth = FindUsage.of(Program.empty(),prMeth, cbClear);
  if(coupuledPaths.isEmpty() && usedPrMeth.isEmpty()){return;}
  List<PathMx> ordered=new ArrayList<>(usedPrMeth);
  Collections.sort(ordered,(px1,px2)->px1.toString().compareTo(px2.toString()));
  throw Errors42.errorPrivacyCoupuled(coupuledPaths, ordered);
  }

}