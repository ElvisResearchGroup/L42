package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import coreVisitors.CloneVisitor;
import coreVisitors.CloneWithPath;
import coreVisitors.Exists;
import tools.Map;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.Util;
import ast.ExpCore.*;
import ast.Util.*;
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
    assert !name.contains("__");//should have been removed before
    //if(name.contains("__")){
    //  name=name.replace("__", "_"+NormalizePrivates.doubleUnderscoreReplacement);      }
    //just removing __ would be wrong, multiple methods would get different names, but multiple getters would be merged together
    
    return name+newPedex;
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
  private static String replace__(String s){
    String ss=s.replace("__", "_"+NormalizePrivates.doubleUnderscoreReplacement);
    if(s.equals(ss)){return ss;}
    return replace__(ss);
  }
  public static ClassB normalize(ClassB cb){
    CollectedPrivates result = NormalizePrivates.collectPrivates(cb);
    if (result.normalized && result.pedexes.isEmpty()){return cb;}
    cb=replace__ifPresent(cb, result);
    if(!result.pedexes.isEmpty()){
      result=NormalizePrivates.collectPrivates(cb);//could be made faster, but not important here
      }
    result.computeNewNames();
    cb=NormalizePrivates.normalize(result, cb);
    return cb;
  
    }
  private static ClassB replace__ifPresent(ClassB cb, CollectedPrivates result) {
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
        public ast.Ast.MethodSelector liftMs(ast.Ast.MethodSelector ms){
          return super.liftMs(ms.withName(replace__(ms.getName())).withNames(
              Map.of(ni->replace__(ni),ms.getNames())));
        }
        public ClassB.NestedClass visit(ClassB.NestedClass nc){
          return super.visit(nc.withName(replace__(nc.getName())));
        }
      }) ;
  }
   
  static class RenameAlsoDefinition extends RenameMembers{
    public RenameAlsoDefinition(CollectedPrivates maps) { super(maps);}
    public static  ClassB of(CollectedPrivates maps,ClassB cb){
      return (ClassB)cb.accept(new RenameAlsoDefinition(maps));
    }
    public ClassB.NestedClass visit(ClassB.NestedClass nc){
      for(NestedLocator nl:maps.privatePaths){
        if(!nl.getMPos().equals(this.getAstIndexesPath())){continue;}
        if(!nl.getMTail().equals(this.getAstNodesPath())){continue;}
        if(!nc.getName().equals(nl.getThat())){continue;}
        assert nl.getNewName()!=null;
        return super.visit(nc.withName(nl.getNewName()));
      }
      return super.visit(nc);
    }
  }
  public static ClassB normalize(CollectedPrivates privates,ClassB cb){
    cb=RenameAlsoDefinition.of(privates, cb);
    return cb;
    //renameMethod still use old introspection
    //write a rename usage from data of collected privates for both paths and methods.
    //then write a simple rename declarations.
    //the rename declarations for methods, for renameMethod have to keep method sum in account.
    
    }
  public static ClassB importWithReuseNormalizedClass(int round,ClassB cb){return cb;}
}
