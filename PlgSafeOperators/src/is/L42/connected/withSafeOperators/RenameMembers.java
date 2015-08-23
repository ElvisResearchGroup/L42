package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathPath;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;
import tools.Map;

public class RenameMembers extends coreVisitors.CloneWithPath{
  CollectedPrivates maps;//ClassB start;
  public RenameMembers(CollectedPrivates maps) {
    super();
    this.maps = maps;
  }
  public static  ClassB of(CollectedPrivates maps,ClassB cb){
    return (ClassB)cb.accept(new RenameMembers(maps));
  }
  public static  ClassB of(Path src,Path dest,ClassB cb){
    Locator nl = pathPathToLocator(src,dest);
    CollectedPrivates maps=new CollectedPrivates();
    maps.privatePaths.add(nl);
    return of(maps,cb);
  }
  public static  ClassB of(List<PathPath> pp,ClassB cb){
    CollectedPrivates maps=new CollectedPrivates();
    for(PathPath ppi:pp){
      Locator nl = pathPathToLocator(ppi.getPath1(),ppi.getPath2());
      maps.privatePaths.add(nl);
    }
    return of(maps,cb);
  }
  
  private static Locator pathPathToLocator(Path src, Path dest) {
    Locator result=new Locator();
    result.addCs(src.getCBar());
    assert src.outerNumber()==0;
    result.setAnnotation(dest);
    return result;
  }

  
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String>cs=s.getCBar();
        if(cs.isEmpty()){return s;}//no need to rename outers
        Locator current=this.getLocator().copy();
        current.toFormerNodeLocator();
        boolean canCut=current.cutUpTo(s.outerNumber());
        if(!canCut){return s;}
        int whereImSize=current.size();
        current.addCs(s.getCBar());
        
        for(Locator nl:maps.privatePaths){
          if(whereImSize>nl.size()){continue;}
          //situation: rename: s1 c1->path   current path locator is:  whereIm c cs
          //check whereImSize<=s1Size and  whereIm c cs =s1 _ 
          boolean compatible= current.prefixOf(nl);
          if(!compatible){continue;}
          int extraCs=(current.size()-nl.size());//the class name in nl.that
          Path pi=getDestPath(this.getLocator().getClassNamesPath().size(),nl,s,extraCs);//TODO:can be made more efficient without creating the listPaths
          return pi;
          }
        return s;
        }
 

      private Path getDestPath(int myDept,Locator nl, Path s, int extraCs) {
        assert extraCs>=0:extraCs;
        Path result=null;
        if (nl.getAnnotation() instanceof Path){result=(Path)nl.getAnnotation();}
        List<String>cs=s.getCBar();
        if(result==null){
          assert nl.getAnnotation()!=null;
          assert nl.getAnnotation() instanceof String;
          List<String>newCs=new ArrayList<>(cs);
          newCs.set(cs.size()-1-extraCs,(String) nl.getAnnotation());
          return Path.outer(s.outerNumber(),newCs);
          }
        List<String> path =cs.subList(cs.size()-extraCs,cs.size());
        for(String si:path){
          result=result.pushC(si);
        }
        return result.setNewOuter(result.outerNumber()+myDept);
      }
      
    }



