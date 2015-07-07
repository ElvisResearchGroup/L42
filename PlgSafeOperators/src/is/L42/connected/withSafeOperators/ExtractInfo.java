package is.L42.connected.withSafeOperators;

import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

import java.util.ArrayList;
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
  public static boolean isVirginInterface(ClassB topCb,List<String> path){
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
  public static boolean checkClassClashAndReturnIsInterface(
      Program p,List<String>current,
      ClassB topA,ClassB topB,
      ClassB typeA,ClassB typeB,
      ClassB currentA,ClassB currentB){
   List<Path> confl=conflictingImplementedInterfaces(p, current, typeA, typeB);
   //*sum of two classes with private state
   //*sum class/interface invalid
   boolean twoPrivateState=hasPrivateState(currentA) &&hasPrivateState(currentB);
   boolean isAllOk=confl.isEmpty() && !twoPrivateState && currentA.isInterface()==currentB.isInterface();
   if (isAllOk){return currentA.isInterface();}//code under is slow
   boolean aBox=ExtractInfo.isBox(topA,current);
   boolean bBox=ExtractInfo.isBox(topB,current);
   boolean aVirginI=ExtractInfo.isVirginInterface(topA,current);
   boolean bVirginI=ExtractInfo.isVirginInterface(topB,current);
   boolean isClassInterfaceSumOk=aBox||bBox ||aVirginI ||bVirginI;
   isAllOk=confl.isEmpty() && !twoPrivateState && isClassInterfaceSumOk;
   if (isAllOk){return aBox  || bBox;}
   throw Resources.Error.multiPartStringError("ClassClash",
//     "left",sugarVisitors.ToFormattedText.of(mta),//boh
//     "right",sugarVisitors.ToFormattedText.of(mtb),
     "ConflictingImplementedInterfaces",""+confl,
     "TwoPrivateState",""+twoPrivateState,
     "IsClassInterfaceSum",""+(currentA.isInterface()==currentB.isInterface()),
     "IsClassInterfaceSumOk",""+isClassInterfaceSumOk,
     "leftIsBox",""+ aBox,
     "RightIsBox",""+ bBox,
     "LeftIsVirginInterface",""+ aVirginI,
     "RightIsVirginInterface",""+ bVirginI
      );
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
  public static void checkMethodClash(MethodWithType mta, MethodWithType mtb){
    boolean implClash=mta.getInner().isPresent() && mtb.getInner().isPresent();
    boolean exc=isExceptionOk(mta,mtb);
    List<Integer> pars=isParTypeOk(mta,mtb);
    boolean retType=mta.getMt().getReturnType().equals(mtb.getMt().getReturnType());
    boolean thisMdf=mta.getMt().getMdf().equals(mtb.getMt().getMdf());
    if(!implClash && exc && pars.isEmpty() && retType && thisMdf){return;}
    if(mta.getInner().isPresent()){mta=mta.withInner(Optional.of(new ExpCore.X("implementation")));}
    if(mtb.getInner().isPresent()){mtb=mtb.withInner(Optional.of(new ExpCore.X("implementation")));}
    throw Resources.Error.multiPartStringError("MethodClash",
    "Left",sugarVisitors.ToFormattedText.of(mta),
    "Right",sugarVisitors.ToFormattedText.of(mtb),
    "LeftKind",memberKind(mta),
    "RightKind",memberKind(mtb),
    "IncompatibleHeader",""+(!exc||!pars.isEmpty() || !retType || !thisMdf),
    "DifferentParameters",""+ pars,
    "DifferentReturnType",""+ !retType,
    "DifferentThisMdf",""+ !thisMdf,
    "IncompatibleException",""+!exc);
  }
  static Resources.Error clashImpl(Member ma, Member mb) {
    throw Resources.Error.multiPartStringError("MethodClash",
        "left",sugarVisitors.ToFormattedText.of(ma),
        "right",sugarVisitors.ToFormattedText.of(mb),
        "leftKind",memberKind(ma),
        "rightKind",memberKind(mb),
        "incompatibleHeader",""+false,//ok to have all at false to keep a single kind of error?
        "differentParameters",""+ false,
        "differentReturnType",""+ false,
        "differentThisMdf",""+ false,
        "incompatibleException",""+false);
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
}
