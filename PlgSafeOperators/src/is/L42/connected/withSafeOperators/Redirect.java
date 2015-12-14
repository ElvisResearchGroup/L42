package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import coreVisitors.FromInClass;
import facade.Configuration;
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
import ast.Util.CachedStage;
import ast.Util.PathPath;
import ast.Util.PathSPath;
import ast.Util.SPathSPath;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
public class Redirect {
  private static List<PathPath> verifiedForErrorMsg;
  public static ClassB redirect(Program p,ClassB cb, Path internal,Path external){
    //call redirectOk, if that is ok, no other errors?
    //should cb be normalized first?
    assert external.isPrimitive() || external.outerNumber()>0;
    p=p.addAtTop(cb);
    List<PathPath>toRedirect=redirectOk(p,cb,internal,external);
    return applyMapPath(p,cb,toRedirect);
  }
  public static ClassB applyMapPath(Program p,ClassB cb, List<PathPath> mapPath) {
    cb=Rename.renameUsage(mapPath,cb);
     CollectedLocatorsMap coll=new CollectedLocatorsMap();
    for(PathPath pp:mapPath){
      cb=Redirect.remove(pp.getPath1(),cb);
    }
    return cb;
  }
  public static ClassB remove(Path path1, ClassB l) {
    if(path1.equals(Path.outer(0))){
      return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),new CachedStage());
      }
    return (ClassB)l.accept(new coreVisitors.CloneVisitor(){
      List<String> cs=path1.getCBar();
      public List<Member> liftMembers(List<Member> s) {
        List<Member> result=new ArrayList<Member>();
        for(Member m:s){m.match(
          nc->manageNC(nc,result),
          mi->result.add(liftM(m)),
          mt->result.add(liftM(m))
          );}
        return result;
        }
      private boolean manageNC(NestedClass nc, List<Member> result) {
        assert !cs.isEmpty();
        String top=cs.get(0);
        if(!top.equals(nc.getName())){return result.add(nc);}//out of path
        if(cs.size()==1){return true;}
        List<String> csLocal=cs;
        cs=cs.subList(1,cs.size());
        try{return result.add(this.visit(nc));}
        finally{cs=csLocal;}
      }
    });
  }
  
  public static List<PathPath> redirectOk(Program p,ClassB cbTop,Path internal,Path external){
    List<PathPath> verified=new ArrayList<>();
    verifiedForErrorMsg=verified;
    List<PathSPath> ambiguities=new ArrayList<>();
    List<SPathSPath> exceptions=new ArrayList<>();
    ambiguities.add(new PathSPath(internal,Arrays.asList(external)));
    for(PathSPath current=choseUnabigus(ambiguities); current!=null;current=choseUnabigus(ambiguities)){
      PathSPath _current=current;//closure final limitations
      assert ambiguitiesOk(ambiguities);
      assert verified.stream().allMatch(pp->!pp.getPath1().equals(_current.getPath())):
        verified+" "+_current.getPath();
      redirectOkAux(p,current,cbTop,ambiguities,exceptions);
      assert current.getPaths().size()==1;
      assert verified.stream().allMatch(pp->!pp.getPath1().equals(_current.getPath())):
        verified+" "+_current.getPath();
      verified.add(new PathPath(current.getPath(),current.getPaths().get(0)));
      accumulateVerified(ambiguities,verified);
    }
    assert choseUnabigus(ambiguities)==null;
    if(!ambiguities.isEmpty()){
      throw Errors42.errorIncoherentRedirectMapping(verified, ambiguities,null,Collections.emptyList());
      }
    checkExceptionOk(exceptions,verified);
    return verified;
    }
  private static boolean ambiguitiesOk(List<PathSPath> ambiguities) {
    return ambiguities.stream().allMatch(e1->ambiguities.stream().allMatch(e2->(e1==e2|| !e1.getPath().equals(e2.getPath()))));
  }
  private static void checkExceptionOk(List<SPathSPath> exceptions, List<PathPath> verified) {
    for(SPathSPath exc:exceptions){
     List<Path> src = exc.getMwt1().getMt().getExceptions(); 
     src=Map.of(pi->traspose(verified,pi),src);
     if(!src.containsAll(exc.getMwt2().getMt().getExceptions())){
       throw Errors42.errorMethodClash(exc.getSrc().getCBar(), exc.getMwt1(),exc.getMwt2(),true,
           Collections.emptyList(),false,false,false);
       }
    }
  }
  private static Path traspose(List<PathPath> verified, Path pi) {
    if (pi.isPrimitive()){return pi;}
    if(pi.outerNumber()>0){return pi;}
    PathPath selectPP = selectPP(verified,pi);
    assert selectPP!=null:verified+"  "+pi;
    pi=selectPP.getPath2();
    return pi;
  }
  /*private static void lessEqual(List<PathSPath> ambiguities, List<PathPath> verified) {
    Iterator<PathSPath> it = ambiguities.iterator();
    while(it.hasNext()){
      Path pi=it.next().getPath();
      for(PathPath pp:verified){if(pp.getPath1().equals(pi)){it.remove();}}
    }
  }*/
  private static void accumulateVerified(List<PathSPath> ambiguities, List<PathPath> verified) {
    assert ambiguitiesOk(ambiguities);
    for(PathPath pp:verified){
      PathSPath psp=selectPSP(ambiguities,pp.getPath1());
      if(psp==null){continue;}
      //ambiguities.add(new PathSPath(pp.getPath1(),Arrays.asList(pp.getPath2())));
      if(psp.getPaths().contains(pp.getPath2())){ambiguities.remove(psp);}
      else{
        List<Path> ps=new ArrayList<>(psp.getPaths());
        ps.add(pp.getPath2());
        throw Errors42.errorIncoherentRedirectMapping(verified, ambiguities,psp.getPath(),ps);
        }
    }
  }
  private static PathSPath selectPSP(List<PathSPath> set,Path key){
    for(PathSPath elem:set){if(elem.getPath().equals(key)){return elem;}}
    return null;
  }
  private static PathPath selectPP(List<PathPath> set,Path key){
    for(PathPath elem:set){if(elem.getPath1().equals(key)){return elem;}}
    return null;
  }
  private static void redirectOkAux(Program p, PathSPath current, ClassB cbTop, List<PathSPath> ambiguities, List<SPathSPath> exceptions) {
    assert current.getPaths().size()==1;
    List<String>cs=current.getPath().getCBar();
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(cbTop, cs, Optional.empty());
    Boolean[] csPrivate=new Boolean[]{false};
    ClassB currentIntCb=Program.extractCBar(cs,cbTop,csPrivate);
    //path exists by construction.
    Path path=current.getPaths().get(0);
    ClassB currentExtCb;
    if(path.isCore()){
      assert path.outerNumber()>0:
        path;
      currentExtCb= p.extractCb(path);
      }
    else{
      assert path.isPrimitive();
      currentExtCb=new ClassB(Doc.empty(),Doc.empty(),path.equals(Path.Any()),Collections.emptyList(),Collections.emptyList(),new CachedStage());
    }
    assert !csPrivate[0];
    boolean isPrivateState=ExtractInfo.hasPrivateState(currentIntCb);
    boolean isNoImplementation=ExtractInfo.isNoImplementation(currentIntCb);
    boolean headerOk=currentIntCb.isInterface()==currentExtCb.isInterface();
    ClassKind kindSrc= ExtractInfo.classKind(cbTop,cs,currentIntCb, null, isPrivateState, isNoImplementation);
    if(!headerOk && !currentIntCb.isInterface()){
      if(kindSrc==ClassKind.FreeTemplate){headerOk=true;}
    }
    ClassKind kindDest = ExtractInfo.classKind(null,null,currentExtCb,null,null,null);
    if(isPrivateState || !isNoImplementation){//unexpectedMembers stay empty if there is implementation
      assert kindSrc!=ClassKind.FreeTemplate 
          || kindSrc!=ClassKind.Template
          || kindSrc!=ClassKind.Interface:
            kindSrc;
      throw Errors42.errorSourceUnfit(current.getPath().getCBar(),path,  
        kindSrc,kindDest,Collections.emptyList(), headerOk, Collections.emptyList());
    }
    redirectOkImpl(kindSrc,kindDest,ambiguities,current,currentIntCb,currentExtCb);
    List<Member> unexpectedMembers=new ArrayList<>();
    for(Member mi:currentIntCb.getMs()){
      Optional<Member> miPrime = Program.getIfInDom(currentExtCb.getMs(),mi);
      if(miPrime.isPresent() && miPrime.get().getClass().equals(mi.getClass())){
        Member miGet=miPrime.get();
        redirectOkMember(ambiguities,exceptions, mi,miGet,current);
      }
      else{unexpectedMembers.add(mi);}
    }
    if(unexpectedMembers.isEmpty() && headerOk){return;}
    if(kindSrc==null){kindSrc = ExtractInfo.classKind(cbTop,cs,currentIntCb, null, isPrivateState, isNoImplementation);}
    if(kindDest==null){kindDest = ExtractInfo.classKind(null,null,currentExtCb,null,null,null);}
    throw Errors42.errorSourceUnfit(cs,path,
        kindSrc,kindDest,unexpectedMembers, headerOk, Collections.emptyList());
    
  }
  private static void redirectOkMember(List<PathSPath> ambiguities,List<SPathSPath>exceptions, Member mi, Member miGet, PathSPath current) {
    if(mi instanceof NestedClass){
      assert miGet instanceof NestedClass;
      assert ((NestedClass)mi).getName().equals(((NestedClass)miGet).getName());
      Path src=current.getPath().pushC(((NestedClass)mi).getName());
      Path dest=current.getPaths().get(0).pushC(((NestedClass)mi).getName());
      plusEqual(ambiguities,src,Arrays.asList(dest));
      return;
    }    
    assert mi.getClass().equals( miGet.getClass());
    assert mi instanceof MethodWithType:mi;
    MethodWithType mwtSrc=(MethodWithType)mi;
    MethodWithType mwtDest=(MethodWithType)miGet;
    mwtSrc=From.from(mwtSrc, current.getPath());//this is what happens in p.method
    mwtDest=From.from(mwtDest, current.getPaths().get(0));
    assert mwtSrc.getMs().equals(mwtDest.getMs());
    boolean thisMdfOk=mwtSrc.getMt().getMdf().equals(mwtDest.getMt().getMdf());
    boolean retOk=redirectOkT(ambiguities,mwtSrc.getMt().getReturnType(),mwtDest.getMt().getReturnType());
    List<Integer>  parWrong=new ArrayList<Integer>();
    {int i=-1;for(Type tSrc:mwtSrc.getMt().getTs()){i+=1;Type tDest=mwtDest.getMt().getTs().get(i);
      if(!redirectOkT(ambiguities,tSrc,tDest)){parWrong.add(i);};
    }}
    boolean excOk=plusEqualAndExc(ambiguities,exceptions,current.getPath(),mwtSrc, mwtDest);
    if(thisMdfOk && retOk && excOk && parWrong.isEmpty()){return;}
    throw Errors42.errorMethodClash(current.getPath().getCBar(),mwtSrc,mwtDest,excOk,parWrong,retOk, thisMdfOk,false);
    }
  private static boolean plusEqualAndExc(List<PathSPath> ambiguities, List<SPathSPath> exceptions, Path src,MethodWithType mwtSrc, MethodWithType mwtDest) {
    int countExternal=0;
    int countExternalSatisfied=0;
    exceptions.add(new SPathSPath(src,mwtSrc,mwtDest));
    for(Path pi:mwtSrc.getMt().getExceptions()){
      if(pi.isPrimitive() || pi.outerNumber()>0){
        countExternal+=1;
        if(mwtDest.getMt().getExceptions().contains(pi)){countExternalSatisfied+=1;}
        continue;}
      plusEqual(ambiguities,pi,mwtDest.getMt().getExceptions());
    }
    int countInternal=mwtSrc.getMt().getExceptions().size()-countExternal;
    return countInternal+countExternalSatisfied>=mwtDest.getMt().getExceptions().size();
  }
  private static boolean redirectOkT(List<PathSPath> ambiguities, Type tSrc, Type tDest) {
    if(!tSrc.getClass().equals(tDest.getClass())){
      return false;
      }//incompatible internal/external types t1 t2
    //Boolean[] pathOk={true};
    return tSrc.match(
      normType->{
        NormType ntP=(NormType)tDest;
        if(!normType.getMdf().equals(ntP.getMdf())){return false;}//incompatible internal/external types t1 t2
        if(!normType.getPh().equals(ntP.getPh())){return false;}//incompatible internal/external types t1 t2
         return plusEqualCheckExt(ambiguities,normType.getPath(),Arrays.asList(ntP.getPath()));
      },
      hType->{
        HistoricType htP=(HistoricType)tDest;
        if(!hType.getSelectors().equals(htP.getSelectors())){return false;}//incompatible internal/external types t1 t2
        if(hType.isForcePlaceholder()!=htP.isForcePlaceholder()){return false;}//incompatible internal/external types t1 t2
        return plusEqualCheckExt(ambiguities,hType.getPath(),Arrays.asList(htP.getPath()));
      });
  }
  private static boolean plusEqualCheckExt(List<PathSPath> ambiguities, Path path, List<Path> paths) {
    if(!path.isPrimitive() && path.outerNumber()==0){
      plusEqual(ambiguities,path,paths);
      assert ambiguitiesOk(ambiguities);
      return true;
      }
    assert paths.size()==1;
    return path.equals(paths.get(0));
  }
  private static void redirectOkImpl(ClassKind kindSrc,ClassKind kindDest,List<PathSPath> ambiguities, PathSPath current, ClassB currentIntCb, ClassB currentExtCb) {
   // List<Path>unexpectedInterfaces=new ArrayList<>(unexpectedI);
   // Collections.sort(unexpectedInterfaces,(pa,pb)->pa.toString().compareTo(pb.toString()));
    List<Path>extPs=currentExtCb.getSupertypes();
    Path destP=current.getPaths().get(0);
    extPs=Map.of(pi->From.fromP(pi,destP), extPs);
    List<Path> unexpectedInterfaces=new ArrayList<>();
    for(Path pi:currentIntCb.getSupertypes()){
      Path pif=From.fromP(pi, current.getPath());
      if(extPs.isEmpty()){unexpectedInterfaces.add(pif);}
      else if(pif.isPrimitive() || pif.outerNumber()>0){
        if(!extPs.contains(pif)){
          unexpectedInterfaces.add(pif);
          }
      }
      else{
        plusEqual(ambiguities,pif,extPs);
      }
    }
    if(unexpectedInterfaces.isEmpty()){return;}
    throw Errors42.errorSourceUnfit(current.getPath().getCBar(),current.getPaths().get(0),
          kindSrc,kindDest,Collections.emptyList(), true, unexpectedInterfaces);
  }
  private static void plusEqual(List<PathSPath> ambiguities, Path pif, List<Path> extPs) {
    assert ambiguitiesOk(ambiguities);
    try{assert !extPs.isEmpty();
    assert !extPs.contains(null);
    assert !pif.isPrimitive() && pif.outerNumber()==0;
    for(PathSPath psp:ambiguities){
      if(psp.getPath().equals(pif)){
        psp.setPaths(new ArrayList<>(psp.getPaths()));
        assert !psp.getPaths().isEmpty();
        Path forErr=psp.getPaths().get(0);
        psp.getPaths().retainAll(extPs);
        if(psp.getPaths().isEmpty()){
          List<Path>psErr=Arrays.asList(forErr,extPs.get(0));
          throw Errors42.errorIncoherentRedirectMapping(Redirect.verifiedForErrorMsg,ambiguities,psp.getPath(),psErr);
          }
        return;
      }}
    ambiguities.add(new PathSPath(pif,extPs));
  }finally{assert ambiguitiesOk(ambiguities);}}
  static PathSPath choseUnabigus(List<PathSPath> ambiguities){
    for(PathSPath psp:ambiguities){if (psp.getPaths().size()==1){return psp;}}
    return null;
  }
  /*
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
*/
}