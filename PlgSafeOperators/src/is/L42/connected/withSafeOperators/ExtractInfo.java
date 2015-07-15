package is.L42.connected.withSafeOperators;

import ast.Ast;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Path;
import ast.Ast.Type;
import ast.Ast.NormType;
import ast.ErrorMessage.TwoDifferentImplementedInterfacesDeclareMethod;
import ast.ExpCore.*;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import platformSpecific.javaTranslation.Resources;
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

  public static enum ClassKind{Box,Interface,FreeInterface,CloseClass,OpenClass,Template/*,PureRecord*/,Module,TemplateModule;}
  public static ClassKind classKind(ClassB top, List<String> current,ClassB cb,Boolean isBox,Boolean isFreeI,Boolean isPrivateState,Boolean isNoImplementation){//9 options
    if(cb.isInterface()){
      if(isFreeI==null){isFreeI=ExtractInfo.isFreeInterface(top,current);}
      if(isFreeI){return ClassKind.FreeInterface;}
      return ClassKind.Interface;
    }//not interface, 7 options left
    //if(isBox!=null &&isBox){return ClassKinds.Box.name();}//fast exit
    if(isPrivateState==null){isPrivateState=hasPrivateState(cb);}
    if(isPrivateState){return ClassKind.CloseClass;}//6 options left
    if(isNoImplementation==null){isNoImplementation=isNoImplementation(cb);}
    if(isBox==null){isBox=isBox(top,current);}
    if(isBox){return ClassKind.Box;}//5 options left
    if(isModule(cb)){
      if(isNoImplementation){return ClassKind.TemplateModule;}
      return ClassKind.Module;
      }//3 options left template, openclass and pure record left
    if(!isNoImplementation){return ClassKind.OpenClass;}
    //if noImplementation and consistent is pure record (or box, sob)//pure reocord merged with template now :(
    return ClassKind.Template;
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
   boolean boxA=ExtractInfo.isBox(topA,current);//!privateA && would make it faster, but harder to test
   boolean boxB=ExtractInfo.isBox(topB,current);//same for the rest
   boolean freeInterfA=ExtractInfo.isFreeInterface(topA,current);
   boolean freeInterfB=ExtractInfo.isFreeInterface(topB,current);
   boolean isClassInterfaceSumOk=boxA||boxB ||freeInterfA ||freeInterfB;
   isAllOk=confl.isEmpty() && !twoPrivateState && isClassInterfaceSumOk;
   if (isAllOk){return boxA  || boxB;}
   ClassKind kindA=classKind(topA,current,currentA,boxA,freeInterfA,privateA,null);
   ClassKind kindB=classKind(topB,current,currentB,boxB,freeInterfB,privateB,null);
   throw Errors42.errorClassClash(current, confl,kindA,kindB);
  }
  static List<String> showMembers(List<Member> ms){
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
   public static boolean isNoImplementation(ClassB cb){
     for(Member m:cb.getMs()){
       boolean isImpl=m.match(
         nc->false,
         mi->true,
         mt->mt.getInner().isPresent()
         );
       if(isImpl){return false;}
     }
     return true;
   }
   public static boolean isModule(ClassB cb) {
     for(Member m:cb.getMs()){
       if(!(m instanceof MethodWithType)){continue;}
       MethodWithType mwt=(MethodWithType)m;
       if(mwt.getMt().getMdf()!=Mdf.Type){return false;}
       Type rt = mwt.getMt().getReturnType();
       if(!(rt instanceof NormType)){continue;}
       NormType nt=(NormType)rt;
       if(nt.getPath().equals(Path.outer(0))){return false;}
     }
    return true;
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
  static List<Integer> isParTypeOk(MethodWithType mta, MethodWithType mtb) {
    List<Integer>res=new ArrayList<>();
    for(int i=0;i<mta.getMt().getTs().size();i++){
      if(!mta.getMt().getTs().get(i).equals(mtb.getMt().getTs().get(i))){res.add(i);}
    }
    return res;
  }
  static boolean isExceptionOk(MethodWithType mta, MethodWithType mtb) {
    Set<Path> pa=new HashSet<>(mta.getMt().getExceptions());
    Set<Path> pb=new HashSet<>(mtb.getMt().getExceptions());
    Set<Path> pc=new HashSet<>(pa);
    pc.retainAll(pb);
    if(mta.getInner().isPresent() && !pc.containsAll(pa)){return false;}
    if(mtb.getInner().isPresent() && !pc.containsAll(pb)){return false;}
    return true;
  }
  static List<PathMx> collectPrivateMethodsOfPublicPaths(ClassB cb, List<String> path) {
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
  static List<Path> collectPrivatePathsAndSubpaths(ClassB cb, List<String> path) {
    List<Path> result=new ArrayList<>();
    cb=Program.extractCBar(path, cb);
    auxCollectPrivatePathsAndSubpaths(cb,result,path ,false);
    return result;
  }

  public static boolean isPrefix(List<String> a, List<String> b) {
    List<String> la=a;
    List<String> lb=b;
    while (!la.isEmpty() && !lb.isEmpty()){
      String ai=la.get(0);
      String bi=lb.get(0);
      if(!ai.equals(bi)){return false;}
      la=la.subList(1, la.size());
      lb=lb.subList(1, lb.size());
    }
    assert la.isEmpty() || lb.isEmpty();
    return true;
    }
  }

