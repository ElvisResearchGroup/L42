package is.L42.connected.withSafeOperators;

import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import introspection.FindUsage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ast.Ast.Path;
import ast.ErrorMessage.TwoDifferentImplementedInterfacesDeclareMethod;
import ast.ExpCore.*;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Error;
import tools.Map;

public class ExtractInfo {
  static class IsUsed extends CloneWithPath{
    Path target;IsUsed(Path target){this.target=target;}
    Set<Path> whereUsed=new HashSet<>();
    public ExpCore visit(Path s) {
      Path localP=Path.outer(0,getPath());
      if(From.fromP(s, localP).equals(target)){
        whereUsed.add(localP);
        }
      return super.visit(s);
      }
  public static Set<Path> of(ClassB cb,Path path){
    IsUsed iu=new IsUsed(path);
    cb.accept(iu);
    return iu.whereUsed;
    }
  }
  static class IsImplemented extends CloneWithPath{
    Path target;IsImplemented(Path target){this.target=target;}
    Set<Path> whereUsed=new HashSet<>();
    public ExpCore visit(ClassB s) {
      Path localP=Path.outer(0,getPath());
      for(Path ip:s.getSupertypes()){
      if(From.fromP(ip, localP).equals(target)){
        whereUsed.add(localP);
        }
      }
      return super.visit(s);
      }
  public static Set<Path> of(ClassB cb,Path path){
    IsUsed iu=new IsUsed(path);
    cb.accept(iu);
    return iu.whereUsed;
    }
  }
  //path member is not a nestedclass
  //path is used
  public static boolean checkBox(ClassB topCb,List<String> path,boolean justFalse) throws Resources.Error/*NotBox*/{
    ClassB cb=Program.extractCBar(path, topCb);
    List<String> meth=new ArrayList<>();
    for(ClassB.Member m:cb.getMs()){
      m.match(nc->false, mi->meth.add(mi.getS().toString()), mt->meth.add(mt.getMs().toString()));
      }
    Set<Path> used = ExtractInfo.IsUsed.of(cb,Path.outer(0,path));
    if(meth.isEmpty()&& used.isEmpty() && !cb.isInterface()){return true;}
    if(justFalse){return false;}
    throw Resources.Error.multiPartStringError("NotBox",
        "UsedBy",""+used,
        "ContainsMethods",""+meth,
        "IsInterface",""+cb.isInterface());
  }
  public static void checkBox(ClassB cb,List<String> path) throws Resources.Error/*NotBox*/{ checkBox(cb, path,false);}
  public static boolean isBox(ClassB cb,List<String> path){return checkBox(cb, path,true);}
  public static boolean isFreeInterface(ClassB topCb,List<String> path){
    ClassB cb=Program.extractCBar(path, topCb);
    if(!cb.isInterface()){return false;}
    Set<Path> used = ExtractInfo.IsImplemented.of(cb,Path.outer(0,path));
    if(used.isEmpty()){ return true;}
    return false;
  }

  public static String memberKind(Member m){
    return m.match(
      nc->"NestedClass",
      mi->"InterfaceImplementedMethod",
      mt->(mt.getInner().isPresent())?"ImplementedMethod":"AbstractMethod");
  }

  private static enum ClassKinds{Box,Interface,FreeInterface,CloseClass,OpenClass,Template,PureRecord,Module,TemplateModule;}
  public static String classKind(ClassB top, List<String> current,ClassB cb,Boolean isBox,Boolean isPrivateState,
      Boolean isFreeI){//9 options
    if(cb.isInterface()){
      if(isFreeI==null){isFreeI=ExtractInfo.isFreeInterface(top,current);}
      if(isFreeI){return ClassKinds.FreeInterface.name();}
      return ClassKinds.Interface.name();
    }//not interface, 7 options left
    if(isBox!=null &&isBox){return ClassKinds.Box.name();}//fast exit

    if(isPrivateState==null){isPrivateState=hasPrivateState(cb);}
    if(isPrivateState){return ClassKinds.CloseClass.name();}//6 options left

    if(isBox){return "BoxClass";}
    if(isFreeI){return "FreeInterface";}
    if(isInterface){return "Interface";}
    return "Class";
  }
  public static String oldClassKind(boolean isBox,boolean isPrivateState,boolean isInterface,boolean isFreeI){
    if(isBox){return "BoxClass";}
    if(isPrivateState){return"PrivateStateClass";}
    if(isFreeI){return "FreeInterface";}
    if(isInterface){return "Interface";}
    return "Class";
  }
  public static boolean checkClassClashAndReturnIsInterface(
      Program p,List<String>current,
      ClassB topA,ClassB topB,
      ClassB typeA,ClassB typeB,
      ClassB currentA,ClassB currentB){
   List<Path> confl=conflictingImplementedInterfaces(p, current, typeA, typeB);
   //*sum of two classes with private state
   //*sum class/interface invalid
   boolean privateA=hasPrivateState(currentA);
   boolean privateB=hasPrivateState(currentB);
   boolean twoPrivateState=privateA &&privateB;
   boolean isAllOk=confl.isEmpty() && !twoPrivateState && currentA.isInterface()==currentB.isInterface();
   if (isAllOk){return currentA.isInterface();}//code under is slow
   boolean aBox=ExtractInfo.isBox(topA,current);//!privateA && would make it faster, but harder to test
   boolean bBox=ExtractInfo.isBox(topB,current);//same for the rest
   boolean aFreeI=ExtractInfo.isFreeInterface(topA,current);
   boolean bFreeI=ExtractInfo.isFreeInterface(topB,current);
   boolean isClassInterfaceSumOk=aBox||bBox ||aFreeI ||bFreeI;
   isAllOk=confl.isEmpty() && !twoPrivateState && isClassInterfaceSumOk;
   if (isAllOk){return aBox  || bBox;}
   throw errorClassClash(current, currentA, currentB, confl, privateA, privateB, aBox, bBox, aFreeI, bFreeI);
  }
  private static Error errorClassClash(List<String> current, ClassB currentA, ClassB currentB, List<Path> confl, boolean privateA, boolean privateB, boolean aBox, boolean bBox, boolean aFreeI, boolean bFreeI) {
    return Resources.Error.multiPartStringError("ClassClash",
       "Path",""+Path.outer(0,current),
       "ConflictingImplementedInterfaces",""+confl,
       "LeftKind",classKind(aBox,privateA,currentA.isInterface(),aFreeI),
       "RightKind",classKind(bBox,privateB,currentB.isInterface(),bFreeI)
        );
  }
  public static Error errorSourceUnfit(List<String> current,List<Member>unexpected,boolean headerOk,List<Path>unexpectedIntefaces,boolean isPrivate,boolean isAbstract){
      return Resources.Error.multiPartStringError("SourceUnfit",
          "Path",""+Path.outer(0,current),
          "UnexpectedMethods",""+showMembers(unexpected),
          "IncompatibleHeaders",""+!headerOk,
          "UnexpectedImplementednterfaces",""+unexpectedIntefaces,
          "PrivatePath",""+isPrivate,
          "FullyAbstractPath",""+isAbstract
           );
  }
  private static List<String> showMembers(List<Member> ms){
    List<String>result=new ArrayList<>();
    for(Member m:ms){
      result.add(""+m.match(nc->nc.getName(), mi->mi.getS(), mt->mt.getMs()));
    }
    return result;
  }
   public static boolean hasPrivateState(ClassB cb) {
     for(Member m:cb.getMs()){
       if(!(m instanceof MethodWithType)){continue;}
       MethodWithType mwt=(MethodWithType)m;
       if(mwt.getInner().isPresent()){continue;}
       if(mwt.getDoc().isPrivate()){return true;}
     }
    return false;
   }
   public static boolean isModule(ClassB cb) {
     for(Member m:cb.getMs()){
       if(!(m instanceof MethodWithType)){continue;}
       MethodWithType mwt=(MethodWithType)m;
       if(mwt.getMt().getMdf()!=Mdf.Type){continue;}
       if(mwt.getMt().getReturnType().equals(new Norm)){return true;}
     }
    return false;
   }

    public static List<Path> conflictingImplementedInterfaces(
        Program p,List<String>current,ClassB typeA,ClassB typeB){
      //*sum of class with non compatible interfaces (same method, different signature)
      //the implemented interfaces in typeA,typeB (transitive already done!)
      //implA=typeA.impl()//with normalization
      //implB=typeB.impl()//with normalization
      ClassB typeACurrent=Program.extractCBar(current,typeA);
      ClassB typeBCurrent=Program.extractCBar(current,typeA);
      List<Path> implA = Map.of(pi->Norm.of(p,pi), typeACurrent.getSupertypes());
      List<Path> implB = Map.of(pi->Norm.of(p,pi), typeBCurrent.getSupertypes());
      //implA0=implA-implB
      //implB0=implB-implA
      Set<Path> implA0=new HashSet<Path>(implA);
      implA0.removeAll(implB);
      Set<Path> implB0=new HashSet<Path>(implB);
      List<Path> conflicts=new ArrayList<>();
      implB0.removeAll(implA);
    //for all implA api, for all the originalMethods amij in pi Functions.originalMethOf(p, a)
    //for all implB bpi, for all the originalMethods bmij in pi
      for(Path api:implA0)for(Path bpi:implB0){
        ClassB aci=p.extract(api);
        ClassB bci=p.extract(bpi);
        Set<MethodSelector> amis = Functions.originalMethOf(p, aci);
        Set<MethodSelector> bmis = Functions.originalMethOf(p, bci);
        //amij!=bmij as selectors
        amis.retainAll(bmis);//intersection
        if(!amis.isEmpty()){conflicts.add(api);conflicts.add(bpi);}
      }
     return conflicts;
  }
  public static void checkMethodClash(List<String>pathForError,MethodWithType mta, MethodWithType mtb){
    boolean implClash=mta.getInner().isPresent() && mtb.getInner().isPresent();
    boolean exc=isExceptionOk(mta,mtb);
    List<Integer> pars=isParTypeOk(mta,mtb);
    boolean retType=mta.getMt().getReturnType().equals(mtb.getMt().getReturnType());
    boolean thisMdf=mta.getMt().getMdf().equals(mtb.getMt().getMdf());
    if(!implClash && exc && pars.isEmpty() && retType && thisMdf){return;}
    if(mta.getInner().isPresent()){mta=mta.withInner(Optional.of(new ExpCore.X("implementation")));}
    if(mtb.getInner().isPresent()){mtb=mtb.withInner(Optional.of(new ExpCore.X("implementation")));}
    throw errorMehtodClash(pathForError, mta, mtb, exc, pars, retType, thisMdf);
  }
  static Error errorMehtodClash(List<String> pathForError, Member mta, Member mtb, boolean exc, List<Integer> pars, boolean retType, boolean thisMdf) {
    return Resources.Error.multiPartStringError("MethodClash",
     "Path",""+Path.outer(0,pathForError),
    "Left",sugarVisitors.ToFormattedText.of(mta),
    "Right",sugarVisitors.ToFormattedText.of(mtb),
    "LeftKind",memberKind(mta),
    "RightKind",memberKind(mtb),
//deducible    "IncompatibleHeader",""+(!exc||!pars.isEmpty() || !retType || !thisMdf),
    "DifferentParameters",""+ pars,
    "DifferentReturnType",""+ !retType,
    "DifferentThisMdf",""+ !thisMdf,
    "IncompatibleException",""+!exc);
  }
  static void checkExistsPathMethod(ClassB cb, List<String> path,Optional<MethodSelector>ms){
    try{
      ClassB cbi=Program.extractCBar(path, cb);
      if(!ms.isPresent()){return;}
      Optional<Member> meth=Program.getIfInDom(cbi.getMs(),ms.get());
      if(meth.isPresent()){return;}
      throw Resources.Error.multiPartStringError("InexistentMethod",
          "Path",""+Path.outer(0,path),
          "Selector",""+ms);
      }
    catch(ast.ErrorMessage.PathNonExistant e){
      throw Resources.Error.multiPartStringError("InexistentPath",
          "Path",""+Path.outer(0,path));
    }
  }
  private static List<Integer> isParTypeOk(MethodWithType mta, MethodWithType mtb) {
    List<Integer>res=new ArrayList<>();
    for(int i=0;i<mta.getMt().getTs().size();i++){
      if(!mta.getMt().getTs().get(i).equals(mtb.getMt().getTs().get(i))){res.add(i);}
    }
    return res;
  }
  private static boolean isExceptionOk(MethodWithType mta, MethodWithType mtb) {
    Set<Path> pa=new HashSet<>(mta.getMt().getExceptions());
    Set<Path> pb=new HashSet<>(mtb.getMt().getExceptions());
    Set<Path> pc=new HashSet<>(pa);
    pc.retainAll(pb);
    if(mta.getInner().isPresent() && !pc.containsAll(pa)){return false;}
    if(mtb.getInner().isPresent() && !pc.containsAll(pb)){return false;}
    return true;
  }
  public static void checkPrivacyCoupuled(ClassB cbFull,ClassB cbClear, List<String> path) {
    //start from a already cleared out of private states
    //check if all private nested classes are USED using IsUsed on cbClear
    //this also verify that no private nested classes are used as
    //type in public methods of public classes.
    //collect all PublicPath.privateMethod
    //use main->introspection.FindUsage
    List<Path>prPath=collectPrivatePathsAndSubpaths(cbFull,path);
    List<PathMx>prMeth=collectPrivateMethodsOfPublicPaths(cbFull,path);
    List<Path>coupuledPaths=new ArrayList<>();
    for(Path pi:prPath){
      Set<Path> used = IsUsed.of(cbClear,pi);
      if(used.isEmpty()){continue;}
      coupuledPaths.add(pi);
    }
    Set<PathMx> usedPrMeth = FindUsage.of(Program.empty(),prMeth, cbClear);
    if(coupuledPaths.isEmpty() && usedPrMeth.isEmpty()){return;}
    List<PathMx> ordered=new ArrayList<>(usedPrMeth);
    Collections.sort(ordered,(px1,px2)->px1.toString().compareTo(px2.toString()));
    throw Resources.Error.multiPartStringError("PrivacyCoupuled",
       "CoupuledPath",""+coupuledPaths,
      "CoupuledMethods",""+ordered);
    }
  private static List<PathMx> collectPrivateMethodsOfPublicPaths(ClassB cb, List<String> path) {
    List<PathMx> result=new ArrayList<>();
    cb=Program.extractCBar(path, cb);
    auxCollectPrivateMethodsOfPublicPaths(cb,result,path);
    return result;
  }
  private static void auxCollectPrivateMethodsOfPublicPaths(ClassB cb,List<PathMx> accumulator, List<String> prefix) {
    for(Member m:cb.getMs()){m.match(
      nc->{
        if(nc.getDoc().isPrivate()){return null;}
        List<String> newPrefix=new ArrayList<>(prefix);
        newPrefix.add(nc.getName());
        auxCollectPrivateMethodsOfPublicPaths(cb,accumulator,newPrefix);
        return null;
      },mi->null,
      mt->{
        if (!mt.getDoc().isPrivate()){return null;}
        accumulator.add(new PathMx(Path.outer(0,prefix),mt.getMs()));
        return null;
      });}
  }
  private static void auxCollectPrivatePathsAndSubpaths(ClassB cb,List<Path> accumulator, List<String> prefix, boolean collectAll) {
    for(Member m:cb.getMs()){m.match(
      nc->{
        List<String> newPrefix=new ArrayList<>(prefix);
        newPrefix.add(nc.getName());
        auxCollectPrivatePathsAndSubpaths(cb,accumulator,newPrefix,collectAll || nc.getDoc().isPrivate());
        if(collectAll || nc.getDoc().isPrivate()){accumulator.add(Path.outer(0,newPrefix));}
        return null;
      },mi->null, mt->null);}
  }
  private static List<Path> collectPrivatePathsAndSubpaths(ClassB cb, List<String> path) {
    List<Path> result=new ArrayList<>();
    cb=Program.extractCBar(path, cb);
    auxCollectPrivatePathsAndSubpaths(cb,result,path ,false);
    return result;
  }
  public static void checkPrefix(List<String> a, List<String> b) {
    List<String> la=a;
    List<String> lb=b;
    while (!la.isEmpty() && !lb.isEmpty()){
      String ai=la.get(0);
      String bi=lb.get(0);
      if(!ai.equals(bi)){return;}
      la=la.subList(1, la.size());
      lb=lb.subList(1, lb.size());
    }
    if(la.isEmpty() || lb.isEmpty()){
      List<String> shorter=la.isEmpty()?a:b;
      List<String> longer=la.isEmpty()?b:a;
      throw Resources.Error.multiPartStringError("PathClash",
         "Prefix",""+shorter,
         "Clashing",""+longer);}
  }

}