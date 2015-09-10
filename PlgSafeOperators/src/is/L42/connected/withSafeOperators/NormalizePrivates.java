package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.Collections;
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
import ast.Util.*;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Program;
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
    if(!name.contains(doubleUnderscoreReplacement)){return;}
    doubleUnderscoreReplacement+="$%";
    updateDoubleUnderscoreReplacement(name);
  }
  public static String freshName(String name){
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
          if(name.contains("__")){result.normalized=false;}
          return super.visit(nc);
          }
        if(validUniquePexed==null){result.normalized=false;}
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
          if(name.contains("__")){result.normalized=false;}
          return super.visit(mwt);
          }
        if(validUniquePexed==null){result.normalized=false;}
        Locator ml=this.getLocator().copy();
        ml.pushMember(mwt);
        result.selectors.add(ml);
        return super.visit(mwt);
        }
      });
    return result;
    }
  
  static Util.PrivatePedex isValidPedex(String uniquePexed) {
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
    if (result.normalized ){return cb;}//put && result.pedexes.isEmpty() for renormalization
    cb=replace__ifPresent(cb, result);
    if(!result.pedexes.isEmpty()){
      result=NormalizePrivates.collectPrivates(cb);//could be made faster, but not important here
      }
    result.computeNewNames();
    cb=NormalizePrivates.normalize(p,result, cb);
    //cb.getStage().setPrivateNormalized(true);
    cb.accept(new CloneVisitor(){public ExpCore visit(ClassB cb){
      cb.getStage().setPrivateNormalized(true);
      return super.visit(cb);}});
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
        public ast.Ast.MethodSelector liftMs(ast.Ast.MethodSelector ms){
          return super.liftMs(ms.withName(replace__(ms.getName())).withNames(
              Map.of(ni->replace__(ni),ms.getNames())));
        }
        public ClassB.NestedClass visit(ClassB.NestedClass nc){
          return super.visit(nc.withName(replace__(nc.getName())));
        }
      }) ;
  }
   
  public static ClassB normalize(Program p,CollectedLocatorsMap privates,ClassB cb){
    cb=(ClassB)new RenameAlsoDefinition(cb,privates,p).visit(cb);
    return cb;
    //renameMethod still use old introspection
    //write a rename usage from data of collected privates for both paths and methods.
    //then write a simple rename declarations.
    //the rename declarations for methods, for renameMethod have to keep method sum in account.
    
    }
  public static ClassB importWithReuseNormalizedClass(int round,ClassB cb){return cb;}
}
