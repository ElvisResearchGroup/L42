package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import coreVisitors.CloneWithPath;
import coreVisitors.Exists;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.*;
import ast.Util.*;
import ast.Ast.Path;
//this file may be moved in L42_Main
public class NormalizePrivates {
  public static class CollectedPrivates{
    Set<String> pedexes=new HashSet<>();
    List<CsMx>privateSelectors=new ArrayList<>();
    List<List<String>>privatePaths=new ArrayList<>();
    boolean normalized=true;
    public String toString(){
      return""+pedexes+"\n"+privateSelectors+"\n"+privatePaths+"\n"+normalized;}
    }
  public static CollectedPrivates collectPrivates(ClassB cb){
    CollectedPrivates result=new CollectedPrivates();
    cb.accept(new CloneWithPath(){
      public ExpCore visit(ClassB cb){
        result.normalized=result.normalized&& allMembersNormalized(result,this.getPath(),cb);
        return super.visit(cb);
        }});
    return result;
    }
  public static boolean allMembersNormalized(CollectedPrivates result,List<String> path,ClassB cb){
    boolean res=true;
    for(Member m:cb.getMs()){
      boolean isNormal=m.match(
        nc->{
          if(!nc.getDoc().isPrivate()){return true;}
          String name=nc.getName();
          List<String>appended=new ArrayList<>(path);
          appended.add(name);
          result.privatePaths.add(appended);
          int index=name.indexOf("__");
          if(index==-1){return false;}
          String pedex=name.substring(index+2,name.length());
          boolean wasAdded=result.pedexes.add(pedex);
          return wasAdded;
        },
        mi->true,
        mt->{
          if(!mt.getDoc().isPrivate()){return true;}
          String name=mt.getMs().getName();
          result.privateSelectors.add(new CsMx(path,mt.getMs()));
          int index=name.indexOf("__");
          if(index==-1){return false;}
          String pedex=name.substring(index+2,name.length());
          boolean wasAdded=result.pedexes.add(pedex);
          return wasAdded;
        });
      res=res && isNormal;
    }
    return res;
  }
  public static ClassB normalize(ClassB cb){return cb;}
  public static ClassB importWithReuseNormalizedClass(int round,ClassB cb){return cb;}
}
