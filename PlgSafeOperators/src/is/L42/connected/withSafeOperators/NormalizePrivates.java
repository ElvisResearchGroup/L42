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
  public static int countPrivates=0;
  public static int countFamilies=0;
  public static String freshName(String name){
    assert !name.contains("__");//we can relax this somehow?
    return name+"__"+countPrivates++ +"_"+countFamilies;
  }
  public static class CollectedPrivates{
    Set<String> pedexes=new HashSet<>();
    List<MethodLocator>privateSelectors=new ArrayList<>();
    List<NestedLocator>privatePaths=new ArrayList<>();
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
      }
      //it must be state!
      MethodLocator stardizedMethodLocator=new MethodLocator(mL.getMTail(),mL.getMPos(),null,null);
      String s=map.get(stardizedMethodLocator);
      if(s==null){
        s="__"+countPrivates++ +"_"+countFamilies;//may be turn in method?
        map.put(stardizedMethodLocator,s);
      }
      String newPedex=s;
      List<String> names = (mL.getThat().getMt().getMdf()==Mdf.Type)?mL.getThat().getMs().getNames():Map.of(si->si+newPedex,mL.getThat().getMs().getNames());
      ast.Ast.MethodSelector ms=new ast.Ast.MethodSelector(
          mL.getThat().getMs().getName()+newPedex,names);
      mL.setNewName(ms);
      }
    }
  public static CollectedPrivates collectPrivates(ClassB cb){
    CollectedPrivates result=new CollectedPrivates();
    cb.accept(new CloneWithPath(){
      @Override public ClassB.NestedClass visit(ClassB.NestedClass nc){
        String name=nc.getName();
        boolean hasValidUniquePexed=collectPedex(result.pedexes,name);
        if(!nc.getDoc().isPrivate()){return super.visit(nc);}
        if(!hasValidUniquePexed){result.normalized=false;}
        result.privatePaths.add(new NestedLocator(new ArrayList<>(this.getAstNodesPath()),new ArrayList<>(this.getAstIndexesPath()) ,name));
        return super.visit(nc);
        }
      @Override public ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
        String name=mwt.getMs().getName();
        boolean hasValidUniquePexed=collectPedex(result.pedexes,name);
        if(!mwt.getDoc().isPrivate()){return super.visit(mwt);}
        if(!hasValidUniquePexed){result.normalized=false;}
        result.privateSelectors.add(new MethodLocator(new ArrayList<>(this.getAstNodesPath()),new ArrayList<>(this.getAstIndexesPath()) ,mwt,null));
        return super.visit(mwt);
        }
      });
    return result;
    }
  public static boolean collectPedex(Set<String> collected,String name){
    int index=name.indexOf("__");
    if(index==-1){return false;}
    String pedex=name.substring(index+2,name.length());
    boolean wasAdded=collected.add(pedex);
    return wasAdded;
  }
  
  public static ClassB normalize(CollectedPrivates privates,ClassB cb){
    return cb;
    }
  public static ClassB importWithReuseNormalizedClass(int round,ClassB cb){return cb;}
}
