package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import coreVisitors.FromInClass;
import facade.Configuration;
import introspection.FindUsage;
import introspection.IntrospectionAdapt;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
import is.L42.connected.withSafeOperators.Pop.PopNFrom;
import tools.Assertions;
import tools.Map;
import ast.ErrorMessage;
import ast.ExpCore;
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
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
public class Redirect {
  public static ClassB redirect(Program p,ClassB cb, Path internal,Path external){
    //call redirectOk, if that is ok, no other errors?
    //should cb be normalized first?
    assert external.isPrimitive() || external.outerNumber()>0;
    p=p.addAtTop(cb,null);
    List<PathPath>toRedirect=redirectOk(p,cb,internal,external);
    return applyMapPath(p,cb,toRedirect);
  }
  public static ClassB applyMapPath(Program p,ClassB cb, List<PathPath> mapPath) {
    cb=Rename.renameUsage(mapPath,cb);
       //TODO: use this when ready RenameMembers.of(mapPath, cb);
    for(PathPath pp:mapPath){
      cb=IntrospectionAdapt.remove(pp.getPath1(),cb);
    }
    return cb;
  }
  public static List<PathPath> redirectOk(Program p,ClassB l, Path csPath,Path path){
    List<PathPath>setVisited=new ArrayList<>();
    redirectOk(setVisited,p,l,csPath,path);
    return setVisited;
  }
  public static void redirectOk(List<PathPath>setVisited,Program p,ClassB l, Path csPath,Path path){
    PathPath currentPP=new PathPath(csPath,path);
    if(setVisited.contains(currentPP)){return;}
    setVisited.add(currentPP);
    Errors42.checkCoherentMapping(setVisited);
    List<String>cs=csPath.getCBar();
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(l, cs, Optional.empty());
    Boolean[] csPrivate=new Boolean[]{false};
    ClassB l0NoFrom=Program.extractCBar(cs,l,csPrivate);//L(Cs)[from Cs]=L0={H M0 ... Mn}//No, from does not work here
    //path exists by construction.
    ClassB l0DestNoFrom;
    if(path.isCore()){
      assert path.outerNumber()>0:
        path;
      l0DestNoFrom= p.extractCb(path);
      }//p(Path)[from Path]=L0'={H' M0' ... Mn', _}//reordering of Ms allowed here
    else{
      assert path.isPrimitive();
      l0DestNoFrom=new ClassB(Doc.empty(),Doc.empty(),path.equals(Path.Any()),Collections.emptyList(),Collections.emptyList());
    }
    //(a)Cs is public in L, and Cs have no private state;
    boolean isPrivate=csPrivate[0];
    boolean isPrivateState=ExtractInfo.hasPrivateState(l0NoFrom);
    //all its methods have no implementation, that is:
    //for all Mi,i=0..n: Mi is of form h or Mi is of form C:_
   boolean isNoImplementation=ExtractInfo.isNoImplementation(l0NoFrom);
    //(b) L[H=~H'] holds
    boolean headerOk=l0NoFrom.isInterface()==l0DestNoFrom.isInterface();
    ClassKind kindSrc=null;
    if(!headerOk && !l0NoFrom.isInterface()){
      kindSrc=ExtractInfo.classKind(l,cs,l0NoFrom,null,isPrivateState, isNoImplementation);
      if(kindSrc==ClassKind.FreeTemplate){headerOk=true;}
    }
    //(c) S,Cs->Path;p|-L[Paths=~Paths']:S'
    //(d) S;p|-L[M0=~M0' Cs->Path]:S0 ... S;p|-L[Mn=~Mn' Cs->Path]:Sn
    //(e) S'=Cs->Path,S0..Sn
    if(!isNoImplementation){//unexpectedMembers stay empty if there is implementation
      if(kindSrc==null){kindSrc = ExtractInfo.classKind(l,cs,l0NoFrom, null, isPrivateState, isNoImplementation);}
      ClassKind kindDest = ExtractInfo.classKind(null,null,l0DestNoFrom,null,null,null);
      assert kindSrc!=ClassKind.FreeTemplate 
          || kindSrc!=ClassKind.Template
          || kindSrc!=ClassKind.Interface:
            kindSrc;
      throw Errors42.errorSourceUnfit(currentPP.getPath1().getCBar(),path,  
        kindSrc,kindDest,Collections.emptyList(), headerOk, Collections.emptyList(), isPrivate);
    }
    Set<Path>unexpectedI=redirectOkImpl(setVisited,currentPP,p,l,
        Map.of(pi->From.fromP(pi,csPath), l0NoFrom.getSupertypes()),
        Map.of(pi->From.fromP(pi,path),l0DestNoFrom.getSupertypes()));
    List<Path>unexpectedInterfaces=new ArrayList<>(unexpectedI);
    Collections.sort(unexpectedInterfaces,(pa,pb)->pa.toString().compareTo(pb.toString()));
    List<Member> unexpectedMembers=new ArrayList<>();
    for(Member mi:l0NoFrom.getMs()){
      Optional<Member> miPrime = Program.getIfInDom(l0DestNoFrom.getMs(),mi);
      if(miPrime.isPresent() && miPrime.get().getClass().equals(mi.getClass())){
        Member miGet=miPrime.get();
        if(miGet instanceof MethodWithType){
          MethodWithType mwt=(MethodWithType)miGet;
          mwt=From.from(mwt, path);//this is what happens in p.method
          //mwt=Norm.of(p,mwt,true);
          miGet=mwt;
        }
          redirectOk(setVisited,p,l,From.from(mi,csPath),miGet,currentPP);
      }
      else{unexpectedMembers.add(mi);}
    }
    boolean isOk=true;
    if(!unexpectedMembers.isEmpty()){isOk=false;}
    if(!unexpectedI.isEmpty()){isOk=false;}
    if(!headerOk){isOk=false;}
    if(isPrivate){isOk=false;}
    if(isOk){return;}
    if(kindSrc==null){kindSrc = ExtractInfo.classKind(l,cs,l0NoFrom, null, isPrivateState, isNoImplementation);}
    ClassKind kindDest = ExtractInfo.classKind(null,null,l0DestNoFrom,null,null,null);
    throw Errors42.errorSourceUnfit(currentPP.getPath1().getCBar(),path,
        kindSrc,kindDest,unexpectedMembers, headerOk, unexpectedInterfaces, isPrivate);
  }
  
  private static Set<Path> redirectOkImpl(List<PathPath> s, PathPath currentPP, Program p, ClassB l, List<Path> paths, List<Path> pathsPrime) {
    //(paths ok)//and I can not use it for exceptions since opposite subset relation
    //S;p|-L[Paths=~Paths']:S'
    //Path subsetof Path'
    //or Paths=Path, Paths'=Path' and S;p|-L[Path=~Path']:S'
    if(paths.isEmpty()){return Collections.emptySet();}
    Set<Path> ps=new HashSet<>(paths);
    Set<Path> psPrime=new HashSet<>(pathsPrime);
    ps.removeAll(pathsPrime);
    psPrime.removeAll(paths);
    if(ps.isEmpty()){return Collections.emptySet();}
    if(ps.size()!=1){return ps;}
    if(psPrime.size()!=1){return ps;}
    boolean pathOk=redirectOkPath(s, p, l,ps.iterator().next(),psPrime.iterator().next());
    if(pathOk){return Collections.emptySet();}
    return ps;
    }
  public static void redirectOk(List<PathPath> s, Program p, ClassB l, Member mi, Member miPrime, PathPath currentPP) {
    //from before I know the members mi, miPrime are of the same class.
    mi.match(
      nc->redirectOkNc(s,p,l,nc,(NestedClass)miPrime,currentPP),
      errMi->{
        throw Assertions.codeNotReachable("Should be catched before as in fully abstract source");
        },
      mt->redirectOkMt(s,p,l,mt,(MethodWithType)miPrime,currentPP));
  }
  private static Void redirectOkMt(List<PathPath> s, Program p, ClassB l, MethodWithType mt, MethodWithType mtPrime, PathPath currentPP) {
    boolean isMdfOk=mt.getMt().getMdf()==mtPrime.getMt().getMdf();
    boolean isRetTypeOk=redirectOkType(s,p,l,mt.getMt().getReturnType(),mtPrime.getMt().getReturnType());
    List<Integer> wrongTypes=new ArrayList<>();
    for(int i=0;i<mt.getMt().getTs().size();i+=1){
      boolean isOkType=redirectOkType(s,p,l,mt.getMt().getTs().get(i),mtPrime.getMt().getTs().get(i));
      if(!isOkType){wrongTypes.add(i);}
    }
    Set<Path> badExc=redirectOkExceptions(s,p,l,mt.getMt().getExceptions(),mtPrime.getMt().getExceptions());
    if(!badExc.isEmpty() || !wrongTypes.isEmpty()|| !isRetTypeOk ||!isMdfOk){
      throw Errors42.errorMethodClash(currentPP.getPath1().getCBar(),mt,mtPrime, badExc.isEmpty(), wrongTypes, isRetTypeOk, isMdfOk);
    }
    return null;
  }
  private static Set<Path> redirectOkExceptions(List<PathPath> s, Program p, ClassB l, List<Path> exceptions, List<Path> exceptionsPrime) {
    if(exceptionsPrime.isEmpty()){return Collections.emptySet();}
    Set<Path> exc=new HashSet<>(exceptions);
    Set<Path> excPrime=new HashSet<>(exceptionsPrime);
    excPrime.removeAll(exceptions);
    if(excPrime.isEmpty()){return  Collections.emptySet();}
    exc.removeAll(exceptionsPrime);
    if(exc.size()!=1){return exc;}
    if(excPrime.size()!=1){return exc;}//ok not excPrime
    boolean pathOk=redirectOkPath(s, p, l, exc.iterator().next(),exceptionsPrime.iterator().next());
    if(pathOk){ return Collections.emptySet();}
    return exc;
  }
  private static boolean redirectOkType(List<PathPath> s, Program p, ClassB l, Type t, Type tPrime) {
    if(!t.getClass().equals(tPrime.getClass())){return false;}//incompatible internal/external types t1 t2
    Boolean[] pathOk={true};
    t.match(
      normType->{
        NormType ntP=(NormType)tPrime;
        if(!normType.getMdf().equals(ntP.getMdf())){return false;}//incompatible internal/external types t1 t2
        if(!normType.getPh().equals(ntP.getPh())){return false;}//incompatible internal/external types t1 t2
        pathOk[0]=redirectOkPath(s,p,l,normType.getPath(),ntP.getPath());
        return null;
      },
      hType->{
        HistoricType htP=(HistoricType)tPrime;
        if(!hType.getSelectors().equals(htP.getSelectors())){return false;}//incompatible internal/external types t1 t2
        if(hType.isForcePlaceholder()!=htP.isForcePlaceholder()){return false;}//incompatible internal/external types t1 t2
        pathOk[0]=redirectOkPath(s,p,l,hType.getPath(),htP.getPath());
        return null;
      });
    return pathOk[0];
  }
  private static boolean redirectOkPath(List<PathPath> s, Program p, ClassB l,Path cs, Path path) {
    //S;p|-L[Outern::Cs =~Outern::Cs]:emptyset  holds with n>0
    if(cs.isPrimitive() ||cs.outerNumber()>0){
      if(!cs.equals(path)){return false;}
      return true;
    }
    //otherwise
    //S;p|-L[Outer0::Cs =~ Path ]: S'
    //if S;p|-L[redirect Cs->Path]:S'
    redirectOk(s,p,l,cs,path);
    return true;
  }
  private static Void redirectOkNc(List<PathPath> s, Program p, ClassB l, NestedClass nc, ClassB.NestedClass miPrime, PathPath currentPP) {
    //S;p|-L[C:L1=~C:L1' Cs->Path]:S'
    //if S,Cs->Path;p|-L[redirect Cs::C->Path::C]:S'
    Path src=currentPP.getPath1().pushC(nc.getName());
    Path dest=currentPP.getPath2().pushC(nc.getName());
    redirectOk(s,p,l,src,dest);
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