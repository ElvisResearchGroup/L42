package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import coreVisitors.CloneVisitor;
import coreVisitors.CloneWithPath;
import coreVisitors.Exists;
import privateMangling.PrivateHelper;
import tools.Map;
import ast.ExpCore;
import ast.Util;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.*;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Program;
import ast.Ast;
import ast.Ast.Path;
//this file may be moved in L42_Main
public class NormalizePrivates {
  public static int countPrivates;
  public static String doubleUnderscoreReplacement;
  static{reset();}
  public static void reset(){
    countPrivates=0;
    PrivateHelper.countFamilies=0;
    doubleUnderscoreReplacement="$%";
    }
  public static void updateDoubleUnderscoreReplacement(String name){
     //TODO: do something about it, now  it grow like creazy
    if(!name.contains(doubleUnderscoreReplacement)){return;}
    doubleUnderscoreReplacement+="$%";
    updateDoubleUnderscoreReplacement(name);
  }
  public static String freshName(String name){
   //int index=name.indexOf('_');
   //if(index>0){name=name.substring(0,index);}
   return freshName(name,"__"+countPrivates++ +"_"+PrivateHelper.countFamilies);
  }
  public static String freshName(String name,String newPedex){
    assert !name.contains("__");//should have been removed before
    return name+newPedex;
  }
  public static CollectedLocatorsMap collectPrivates(ClassB cb){
    CollectedLocatorsMap result=new CollectedLocatorsMap();
    cb.accept(new CloneWithPath(){
      @Override public ClassB.NestedClass visit(ClassB.NestedClass nc){
        String name=nc.getName();
        String uniquePexed=processNameAndReturnUnseenPedex(result.pedexes,name);
        PrivatePedex validUniquePexed = isValidPedex(uniquePexed);
        if(!nc.getDoc().isPrivate()){
          if(name.contains("__")){result.notNormalized();}
          return super.visit(nc);
          }
        if(validUniquePexed==null){result.notNormalized();}
        else{result.families.add(validUniquePexed.getFamily());}
        Locator nl=this.getLocator().copy();
        nl.pushMember(nc);
        result.nesteds.add(nl);
        return super.visit(nc);
        }
      @Override public ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
        String name=mwt.getMs().getName();
        String uniquePexed=processNameAndReturnUnseenPedex(result.pedexes,name);
        PrivatePedex validUniquePexed = isValidPedex(uniquePexed);
        if(!mwt.getDoc().isPrivate()){
          if(name.contains("__")){result.notNormalized();}
          return super.visit(mwt);
          }
        if(validUniquePexed==null){result.notNormalized();}
        Locator ml=this.getLocator().copy();
        ml.pushMember(mwt);
        result.selectors.add(ml);
        return super.visit(mwt);
        }
      });
    if(result.normalized){
      cb.getStage().getFamilies().clear();
      cb.getStage().getFamilies().addAll(result.families);
    }
    return result;
    }

  static Util.PrivatePedex isValidPedex(String uniquePexed) {
    if(uniquePexed==null){return null;}
    int pos_=uniquePexed.indexOf("_");
    if(pos_==-1){return null;}
    String n1=uniquePexed.substring(0,pos_);
    String n2=uniquePexed.substring(pos_+1);
    try{
      int i1=Integer.parseInt(n1);
      int i2=Integer.parseInt(n2);
      return new Util.PrivatePedex(i2,i1);
    }
    catch (NumberFormatException nfe){return null;}
  }
  public static String processNameAndReturnUnseenPedex(Set<String> collected,String name){
    int index=name.indexOf("__");
    if(index==-1){return null;}
    updateDoubleUnderscoreReplacement(name);

    String pedex=name.substring(index+2,name.length());
    boolean wasAdded=collected.add(pedex);
    if(wasAdded){return pedex;}
    return null;
  }
  private static String replace__(String s){
    String ss=s.replace("__", "_"+NormalizePrivates.doubleUnderscoreReplacement);
    if(s.equals(ss)){return ss;}
    return replace__(ss);
  }
  public static ClassB normalize(Program p,ClassB cb){
    if(cb.getStage().isPrivateNormalized()){return cb;}
    CollectedLocatorsMap result = NormalizePrivates.collectPrivates(cb);
    cb = auxNormalize(p, cb, result);
    cb.accept(new CloneVisitor(){public ExpCore visit(ClassB cb){
      cb.getStage().setPrivateNormalized(true);
      return super.visit(cb);}});
    return cb;

    }
  private static ClassB auxNormalize(Program p, ClassB cb, CollectedLocatorsMap result) {
    if (result.normalized ){return cb;}//put && result.pedexes.isEmpty() for renormalization
    cb=replace__ifPresent(cb, result);
    if(!result.pedexes.isEmpty()){
      result=NormalizePrivates.collectPrivates(cb);//could be made faster, but not important here
      }
    result.computeNewNames();
    cb=NormalizePrivates.normalize(p,result, cb);
    return cb;
  }
  private static ClassB replace__ifPresent(ClassB cb, CollectedLocatorsMap result) {
    if(result.pedexes.isEmpty()){return cb;}
    return (ClassB)cb.accept(new CloneVisitor(){
        public ExpCore visit(Path s){
          if(s.isPrimitive()){return s;}
          List<String> cs=s.getCBar();
          List<String>newCs=new ArrayList<>();
          for(String si:cs){newCs.add(replace__(si));}
          if(newCs.equals(cs)){return s;}
          return Path.outer(s.outerNumber(),newCs);
        }
        @Override public ast.Ast.MethodSelector liftMs(ast.Ast.MethodSelector ms){
          return super.liftMs(ms.withName(replace__(ms.getName())).withNames(
              Map.of(ni->replace__(ni),ms.getNames())));
        }
        @Override public ast.Ast.MethodSelector liftMsInMetDec(ast.Ast.MethodSelector ms){
          return liftMs(ms);
        }
        public ClassB.NestedClass visit(ClassB.NestedClass nc){
          return super.visit(nc.withName(replace__(nc.getName())));
        }
      }) ;
  }
  public static ClassB refreshFamilies(List<Integer>forbidden,ClassB cb){
    CachedStage stg = cb.getStage();
    assert stg.isPrivateNormalized():
      stg;
    List<Integer>needRename=new ArrayList<>(stg.getFamilies());
    needRename.retainAll(forbidden);
    if(needRename.isEmpty()){return cb;}
    PrivateHelper.countFamilies+=1;
    int newFamily=PrivateHelper.countFamilies;
    HashMap<String,String>renaming=new HashMap<>();
    return (ClassB) cb.accept(new CloneVisitor(){
      private int currentNum=0;
      private String visitName(String name) {
        String mapped=renaming.get(name);
        if(mapped!=null){return mapped;}
        int index__=name.indexOf("__");
        if(index__==-1){return name;}
        int index_=name.lastIndexOf("_");
        assert index_>index__+2;
        int family=Integer.parseInt(name.substring(index_+1));
        //if(!needRename.contains(family)){return name;}//this check is wrong, indeed we want to rename all in a single family
        String newName=name.substring(0,index__)+"__"+(currentNum++)+"_"+newFamily;
        renaming.put(name,newName);
        return newName;
      }
      //nc, ms,path
      public NestedClass visit(NestedClass nc){
        String newName=visitName(nc.getName());
        return super.visit(nc.withName(newName));
        }

      public ExpCore visit(Path path){
        List<String>cs=path.getCBar();
        cs=Map.of(c->visitName(c),cs);
        return super.visit(Path.outer(path.outerNumber(),cs));
        }
      public Ast.MethodSelector liftMs(Ast.MethodSelector ms){
        return super.liftMs(ms.withName(visitName(ms.getName())).withNames(
            Map.of(n->visitName(n), ms.getNames())
            ));
        }
    });
  }
  public static ClassB normalize(Program p,CollectedLocatorsMap privates,ClassB cb){
    cb=(ClassB)new RenameAlsoDefinition(cb,privates,p).visit(cb);
    return cb;
    }
}
