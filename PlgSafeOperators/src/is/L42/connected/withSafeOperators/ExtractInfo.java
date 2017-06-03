package is.L42.connected.withSafeOperators;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import programReduction.Program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Path;
import ast.Ast.NormType;
import ast.Ast.NormType;
import ast.ErrorMessage.TwoDifferentImplementedInterfacesDeclareMethod;
import ast.ExpCore.*;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import platformSpecific.javaTranslation.Resources;
import tools.Map;

public class ExtractInfo {
  static class IsUsed extends CloneWithPath{
    Path target;IsUsed(Path target){
      assert target.outerNumber()==0:"only for used internal paths";
      this.target=target;
      }
    Set<Path> whereUsed=new HashSet<>();
    @Override protected Path liftP(Path s) {
      List<Ast.C> path = this.getLocator().getClassNamesPath();
      if(s.isPrimitive()){return s;}
      if(path.size()<s.outerNumber()){return s;}
      //List<String> unexploredPath=path.subList(0,path.size()-s.outerNumber());
      //if(unexploredPath.contains(null)){return super.visit(s);}
      if(path.contains(null)){return super.liftP(s);}
      Path localP=Path.outer(0,path);
      boolean isSame=From.fromP(s, localP).equals(target);
      if(isSame){
        whereUsed.add(localP);
        }
      return super.liftP(s);
      }
  public static Set<Path> of(ClassB cb,Path path){
    IsUsed iu=new IsUsed(path);
    cb.accept(iu);
    return iu.whereUsed;
    }  }
  static class IsUsedAsPath extends IsUsed{
    IsUsedAsPath(Path target){super(target);}
    protected List<NormType> liftSup(List<NormType> supertypes) {return supertypes;}
    protected NormType liftT(NormType t){return t;}
  public static Set<Path> of(ClassB cb,Path path){
    IsUsedAsPath iu=new IsUsedAsPath(path);
    cb.accept(iu);
    return iu.whereUsed;
    }
  }
  static class IsImplemented extends CloneWithPath{
    Path target;IsImplemented(Path target){this.target=target;}
    Set<Path> whereUsed=new HashSet<>();
    public ExpCore visit(ClassB s) {
      Path localP=Path.outer(0,this.getLocator().getClassNamesPath());
      for(Path ip:s.getSuperPaths()){
      if(From.fromP(ip, localP).equals(target)){
        whereUsed.add(localP);
        }
      }
      return super.visit(s);
      }
  public static Set<Path> of(ClassB cb,Path path){
    IsImplemented iu=new IsImplemented(path);
    cb.accept(iu);
    return iu.whereUsed;
    }
  }
  //path member is not a nestedclass
  //path is used
  public static boolean checkBox(ClassB top,ClassB cb,List<Ast.C> path,boolean justFalse) throws Resources.Error/*NotBox*/{
    List<MethodSelector> meth = collectDeclaredMethods(cb);
    Set<Path> used = ExtractInfo.IsUsed.of(top,Path.outer(0,path));
    if(meth.isEmpty()&& used.isEmpty() && !cb.isInterface() && cb.getSupertypes().isEmpty()){return true;}
    if(justFalse){return false;}
    throw Errors42.errorNotBox(cb, meth, used,classKind(cb,Collections.emptyList(),cb,false,null,null));
  }

  private static List<MethodSelector> collectDeclaredMethods(ClassB cb) {
    List<MethodSelector> meth=new ArrayList<>();
    for(ClassB.Member m:cb.getMs()){
      m.match(nc->false, mi->meth.add(mi.getS()), mt->meth.add(mt.getMs()));
      }
    return meth;
  }

  public static void checkBox(ClassB top,ClassB cb,List<Ast.C> path) throws Resources.Error/*NotBox*/{ checkBox(top,cb, path,false);}
  public static boolean isBox(ClassB top,ClassB cb,List<Ast.C> path){return checkBox(top, cb,path,true);}
  public static boolean isNeverImplemented(ClassB top,List<Ast.C> path){
    Set<Path> used = ExtractInfo.IsImplemented.of(top,Path.outer(0,path));
    if(used.isEmpty()){ return true;}
    return false;
  }

  public static String memberKind(Member m){
    return m.match(
      nc->"NestedClass",
      mi->"InterfaceImplementedMethod",
      mt->(mt.get_inner().isPresent())?"ImplementedMethod":"AbstractMethod");
  }

  public static enum ClassKind{
    //Box,
    Interface("interface"),
    //FreeInterface,
    ClosedClass("closedClass"),
    OpenClass("openClass"),
    Template("template")/*,PureRecord*/,
    FreeTemplate("freeTemplate");
    //Module,
    //TemplateModule,
    //Interface_FreeInterface,
    //Box_TemplateModule
    public final String name42;
    ClassKind(String name42){this.name42=name42;}
    }
  //top can be null, in this case we can return the mixed kinds
  public static ClassKind classKind(ClassB top, List<Ast.C> current,ClassB cb,Boolean isFree,Boolean isPrivateState,Boolean isNoImplementation){//9 options
   assert (top==null)==(current==null);
    if(cb.isInterface()){  return ClassKind.Interface; }
    if(isPrivateState==null){isPrivateState=hasPrivateState(cb);}
    if(isPrivateState){return ClassKind.ClosedClass;}
    if(isNoImplementation==null){isNoImplementation=isNoImplementation(cb);}
    if(!isNoImplementation){return ClassKind.OpenClass;}
    if(isFree==null && current!=null ){isFree=ExtractInfo.isFree(top,current);}
    if(isFree!=null &&isFree){return ClassKind.FreeTemplate;}
    return ClassKind.Template;
  }

  public static boolean isFree(ClassB top, List<Ast.C> current) {
    assert current!=null;
    Set<Path> used = ExtractInfo.IsUsedAsPath.of(top,Path.outer(0,current));
    if(used.isEmpty()){ return true;}
    return false;
  }

  static List<String> showMembers(List<Member> ms){
    List<String>result=new ArrayList<>();
    for(Member m:ms){
      result.add(""+m.match(nc->nc.getName(), mi->mi.getS(), mt->mt.getMs()));
    }
    return result;
  }
  static Ast.Doc showPaths(List<Path> ps){
    Ast.Doc result=Doc.empty();
    for(Path pi:ps){
      result=result.sum(Errors42.formatPath(pi));
    }
    return result.formatNewLinesAsList();
  }
   public static boolean hasPrivateState(ClassB cb) {
     for(Member m:cb.getMs()){
       if(!(m instanceof MethodWithType)){continue;}
       MethodWithType mwt=(MethodWithType)m;
       if(mwt.get_inner().isPresent()){continue;}
       if(mwt.getMs().isUnique()){return true;}
     }
    return false;
   }
   public static boolean isNoImplementation(ClassB cb){
     for(Member m:cb.getMs()){
       boolean isImpl=m.match(
         nc->false,
         mi->{
         return true
         ;},
         mt->mt.get_inner().isPresent()
         );
       if(isImpl){return false;}
     }
     return true;
   }
   public static boolean isModule(ClassB cb) {
     for(Member m:cb.getMs()){
       if(!(m instanceof MethodWithType)){continue;}
       MethodWithType mwt=(MethodWithType)m;
       if(mwt.getMt().getMdf()!=Mdf.Class){return false;}
       NormType rt = mwt.getMt().getReturnType();
       if(!(rt instanceof NormType)){continue;}
       NormType nt=(NormType)rt;
       if(nt.getPath().equals(Path.outer(0))){return false;}
     }
    return true;
   }

 /*   private static Set<MethodSelector> intersection(Collection<MethodSelector>ams, Collection<MethodSelector>bms){
    if( ams==null || ams.isEmpty() || bms==null || bms.isEmpty()){return  Collections.emptySet();}
    Set<MethodSelector> result = new HashSet<>(ams);
    result.retainAll(bms);
    return result;
  }*/
  static void accumulateCb(java.util.Map<Path,List<MethodSelector>> accumulator,Path path,ClassB cb){
    assert cb.isInterface();
    if(accumulator.containsKey(path)){return;}
    List<MethodSelector> defined=new ArrayList<>();
    for(Member m:cb.getMs()){
      if(!(m instanceof MethodWithType)){continue;}
      defined.add(((MethodWithType)m).getMs());
      }
    accumulator.put(path, defined);
    }
/*
  static java.util.Map<Path,List<MethodSelector>> implementedInterfacesMethods(Program p,Path path){
    java.util.Map<Path,List<MethodSelector>> accumulator=new HashMap<>();
    fillImplementedInterfacesMethods(accumulator,p,path);
    return accumulator;
  }
  static void fillImplementedInterfacesMethods(java.util.Map<Path,List<MethodSelector>> accumulator,Program p,Path path){
    //assume p have all the cb present,we do not use ct here
    ClassB cb=p.extractCb(path);
    if(cb.isInterface()){accumulateCb(accumulator,path,cb);}
    for(Path pi:cb.getSupertypes()){
      pi=From.fromP(pi,path);
      fillImplementedInterfacesMethods(accumulator, p, pi);
    }
  }
*/
  static List<Integer> isParTypeOk(MethodWithType mta, MethodWithType mtb) {
    List<Integer>res=new ArrayList<>();
    int maxLen=Math.max(mta.getMt().getTs().size(),mtb.getMt().getTs().size());
    for(int i=0;i<maxLen;i++){
      NormType ta;
      NormType tb;
      try{
        ta = mta.getMt().getTs().get(i);
        tb=mtb.getMt().getTs().get(i);
      }catch(ArrayIndexOutOfBoundsException out){res.add(i);continue;}
      if(!ta.equals(tb)){res.add(i);}
    }
    return res;
  }
  static boolean isExceptionOk(MethodWithType mta, MethodWithType mtb) {
    Set<Path> pa=new HashSet<>(Map.of(t->t.getPath(), mta.getMt().getExceptions()));
    Set<Path> pb=new HashSet<>(Map.of(t->t.getPath(),mtb.getMt().getExceptions()));
    Set<Path> pc=new HashSet<>(pa);
    pc.retainAll(pb);
    if(mta.get_inner().isPresent() && !pc.containsAll(pa)){
      return false;}
    if(mtb.get_inner().isPresent() && !pc.containsAll(pb)){
      return false;}
    return true;
  }
  static List<PathMx> collectPrivateMethodsOfPublicPaths(ClassB cb, List<Ast.C> path) {
    List<PathMx> result=new ArrayList<>();
    cb=cb.getClassB(path);
    auxCollectPrivateMethodsOfPublicPaths(cb,result,path);
    return result;
  }
  private static void auxCollectPrivateMethodsOfPublicPaths(ClassB cb,List<PathMx> accumulator, List<Ast.C> prefix) {
    for(Member m:cb.getMs()){m.match(
      nc->{
        if(nc.getName().isUnique()){return null;}
        List<Ast.C> newPrefix=new ArrayList<>(prefix);
        newPrefix.add(nc.getName());
        auxCollectPrivateMethodsOfPublicPaths((ClassB)nc.getInner(),accumulator,newPrefix);
        return null;
      },mi->null,
      mt->{
        if (!mt.getMs().isUnique()){return null;}
        accumulator.add(new PathMx(Path.outer(0,prefix),mt.getMs()));
        return null;
      });}
  }
  private static void auxCollectPrivatePathsAndSubpaths(ClassB cb,List<Path> accumulator, List<Ast.C> prefix, boolean collectAll) {
    for(Member m:cb.getMs()){m.match(
      nc->{
        List<Ast.C> newPrefix=new ArrayList<>(prefix);
        newPrefix.add(nc.getName());
        boolean newCollectAll=collectAll || nc.getName().isUnique();
        auxCollectPrivatePathsAndSubpaths((ClassB)nc.getInner(),accumulator,newPrefix,newCollectAll);
        if(newCollectAll){accumulator.add(Path.outer(0,newPrefix));}
        return null;
      },mi->null, mt->null);}
  }
  static List<Path> collectPrivatePathsAndSubpaths(ClassB cb, List<Ast.C> path) {
    List<Path> result=new ArrayList<>();
    cb=cb.getClassB(path);
    auxCollectPrivatePathsAndSubpaths(cb,result,path ,false);
    return result;
  }

  public static boolean isPrefix(List<Ast.C> a, List<Ast.C> b) {
    List<Ast.C> la=a;
    List<Ast.C> lb=b;
    while (!la.isEmpty() && !lb.isEmpty()){
      Ast.C ai=la.get(0);
      Ast.C bi=lb.get(0);
      if(!ai.equals(bi)){return false;}
      la=la.subList(1, la.size());
      lb=lb.subList(1, lb.size());
    }
    assert la.isEmpty() || lb.isEmpty();
    return true;
    }
  }

