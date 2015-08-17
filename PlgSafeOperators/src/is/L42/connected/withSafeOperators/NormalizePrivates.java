package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import coreVisitors.CloneWithPath;
import coreVisitors.Exists;
import tools.Map;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.Util;
import ast.ExpCore.*;
import ast.Util.*;
import ast.Ast.Mdf;
import ast.Ast.Path;
//this file may be moved in L42_Main
public class NormalizePrivates {
  public static int countPrivates;
  public static int countFamilies;
  public static String doubleUnderscoreReplacement;
  static{reset();}
  public static void reset(){
    countPrivates=0;
    countFamilies=0;
    doubleUnderscoreReplacement="$%";
    }
  public static void updateDoubleUnderscoreReplacement(String name){
    if(!name.contains(doubleUnderscoreReplacement)){return;}
    doubleUnderscoreReplacement+="$%";
    updateDoubleUnderscoreReplacement(name);
  }
  public static String freshName(String name){
   return freshName(name,"__"+countPrivates++ +"_"+countFamilies);
  }
  public static String freshName(String name,String newPedex){
    //assert !name.contains("__");//TODO:we can relax this somehow?
    if(name.contains("__")){
      name=name.replace("__", "_"+NormalizePrivates.doubleUnderscoreReplacement);      }
    //just removing __ would be wrong, multiple methods would get different names, but multiple getters would be merged together
    
    return name+newPedex;
  }
  public static class CollectedPrivates{
    final Set<String> pedexes=new HashSet<>();
    final List<MethodLocator>privateSelectors=new ArrayList<>();
    final List<NestedLocator>privatePaths=new ArrayList<>();
    boolean normalized=true;
    public String toString(){
      return""+pedexes+"\n"+privateSelectors+"\n"+privatePaths+"\n"+normalized;
      }
    public void computeNewNames(){
      HashMap<MethodLocator,String> map=new HashMap<>();
      for(MethodLocator mL:privateSelectors){computeNewName(map,mL);}
      for(NestedLocator nL:privatePaths){nL.setNewName(freshName(nL.getThat()));}
    }
    private void computeNewName(HashMap<MethodLocator, String> map, MethodLocator mL) {
      if(mL.getThat().getInner().isPresent()){
        mL.setNewName(mL.getThat().getMs().withName(freshName(mL.getThat().getMs().getName())));
        return;
      }
      //it must be state!
      MethodLocator stardizedMethodLocator=new MethodLocator(mL.getMTail(),mL.getMPos(),null,null);
      String s=map.get(stardizedMethodLocator);
      if(s==null){
        s="__"+countPrivates++ +"_"+countFamilies;//may be turn in method?
        map.put(stardizedMethodLocator,s);
      }
      String newPedex=s;
      List<String> names = (mL.getThat().getMt().getMdf()!=Mdf.Type)?mL.getThat().getMs().getNames():Map.of(si->freshName(si,newPedex),mL.getThat().getMs().getNames());
      ast.Ast.MethodSelector ms=new ast.Ast.MethodSelector(
          freshName(mL.getThat().getMs().getName(),newPedex),names);
      mL.setNewName(ms);
      }
    }
  public static CollectedPrivates collectPrivates(ClassB cb){
    CollectedPrivates result=new CollectedPrivates();
    cb.accept(new CloneWithPath(){
      @Override public ClassB.NestedClass visit(ClassB.NestedClass nc){
        String name=nc.getName();
        boolean hasValidUniquePexed=processNameAndReturnHasUnseenPedex(result.pedexes,name);
        if(!nc.getDoc().isPrivate()){return super.visit(nc);}
        if(!hasValidUniquePexed){result.normalized=false;}
        result.privatePaths.add(new NestedLocator(new ArrayList<>(this.getAstNodesPath()),new ArrayList<>(this.getAstIndexesPath()) ,name));
        return super.visit(nc);
        }
      @Override public ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
        String name=mwt.getMs().getName();
        boolean hasValidUniquePexed=processNameAndReturnHasUnseenPedex(result.pedexes,name);
        if(!mwt.getDoc().isPrivate()){return super.visit(mwt);}
        if(!hasValidUniquePexed){result.normalized=false;}
        result.privateSelectors.add(new MethodLocator(
            new ArrayList<>(this.getAstNodesPath()),
            new ArrayList<>(this.getAstIndexesPath()) ,
            mwt,null));
        return super.visit(mwt);
        }
      });
    return result;
    }
  
  public static boolean processNameAndReturnHasUnseenPedex(Set<String> collected,String name){
    int index=name.indexOf("__");
    if(index==-1){return false;}
    updateDoubleUnderscoreReplacement(name);
    
    String pedex=name.substring(index+2,name.length());
    boolean wasAdded=collected.add(pedex);
    return wasAdded;
  }
  
  public static ClassB normalize(CollectedPrivates privates,ClassB cb){
    return cb;
    //renameMethod still use old introspection
    //write a rename usage from data of collected privates for both paths and methods.
    //then write a simple rename declarations.
    //the rename declarations for methods, for renameMethod have to keep method sum in account.
    
    }
  public static ClassB importWithReuseNormalizedClass(int round,ClassB cb){return cb;}
}
