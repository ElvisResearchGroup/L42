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
import ast.Util.Locator;
import ast.Util.NestedLocator;
import ast.Util.PathPath;
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
    NestedLocator nl = pathPathToLocator(src,dest);
    CollectedPrivates maps=new CollectedPrivates();
    maps.privatePaths.add(nl);
    return of(maps,cb);
  }
  public static  ClassB of(List<PathPath> pp,ClassB cb){
    CollectedPrivates maps=new CollectedPrivates();
    for(PathPath ppi:pp){
      NestedLocator nl = pathPathToLocator(ppi.getPath1(),ppi.getPath2());
      maps.privatePaths.add(nl);
    }
    return of(maps,cb);
  }
  
  public static Locator adaptLocator(Locator current,Path path){
    List<Member>currentMTail=new ArrayList<>(current.getMTail());
    List<Integer>currentMPos=new ArrayList<>(current.getMPos());
    List<ClassB>currentMOuters=new ArrayList<>(current.getMOuters());
    cutUpTo(path.outerNumber(),currentMTail,currentMPos,currentMOuters);
    addCs(path.getCBar(), currentMTail, currentMPos, currentMOuters);
    return new Locator.ImplLocator(currentMTail,currentMPos,currentMOuters);
  }
  private static NestedLocator pathPathToLocator(Path src, Path dest) {
    List<String> cs = src.getCBar();
    List<Member> mTail=new ArrayList<>();
    List<Integer> mPos=new ArrayList<>();
    List<ClassB> mOuters=new ArrayList<>();
    addCs(cs.subList(0, cs.size()-1), mTail, mPos,mOuters);
    NestedLocator nl=new NestedLocator(mTail, mPos,mOuters, cs.get(cs.size()-1));
    nl.setNewPath(dest);
    return nl;
  }
  private static void addCs(List<String> cs, List<Member> mTail, List<Integer> mPos,List<ClassB> mOuters) {
    for(String s:cs){
      mTail.add(new ClassB.NestedClass(ast.Ast.Doc.empty(),s,new ExpCore.WalkBy(), null));
      mPos.add(1);
      mOuters.add(dumbCb);
      }
  }  
  private static final ClassB dumbCb=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),Collections.emptyList());
  
  
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String>cs=s.getCBar();
        if(cs.isEmpty()){return s;}//no need to rename outers
        List<Member> current = new ArrayList<>(this.getAstNodesPath());
        List<Integer> currentIndexes = new ArrayList<>(this.getAstIndexesPath());
        List<ClassB> currentCb = new ArrayList<>(this.getAstCbPath());
        boolean canCut=cutUpTo(s.outerNumber(),current,currentIndexes,currentCb);
        if(!canCut){return s;}
        int whereImSize=current.size();
        addCs(s.getCBar(),current,currentIndexes,currentCb);
        for(NestedLocator nl:maps.privatePaths){
          if(whereImSize>nl.getMPos().size()){continue;}
          //situation: rename: s1 c1->path   current path locator is:  whereIm c cs
          //check #whereIm<=#s1 and  whereIm c cs =s1 _ 
          //assert isFullyPositive(nl.getMPos()):nl.getMPos();
          boolean compatible= compatible(current, currentIndexes,currentCb, nl);
          if(!compatible){continue;}
          int extraCs=(current.size()-nl.getMPos().size())-1;//the class name in nl.that
          Path pi=getDestPath(this.getClassNamesPath().size(),nl,s,extraCs);//TODO:can be made more efficient without creating the listPaths
          return pi;
          }
        return s;
        }
      private static boolean cutUpTo(int outerNumber, List<Member> current, List<Integer> currentIndexes,List<ClassB>currentCb) {
        int size=current.size();
        assert size==currentIndexes.size();
        if(size==0 && outerNumber>0){return false;}
        if(size==0){return true;}        
        if(currentCb.get(size-1)==null){
          current.remove(size-1);
          currentIndexes.remove(size-1);
          return cutUpTo(outerNumber,current,currentIndexes,currentCb);
        }
        if(outerNumber==0){return true;}
        current.remove(size-1);
        currentIndexes.remove(size-1);
        return cutUpTo(outerNumber-1,current,currentIndexes,currentCb);
        }

      private Path getDestPath(int myDept,NestedLocator nl, Path s, int extraCs) {
        assert extraCs>=0:extraCs;
        Path result=nl.getNewPath();
        List<String>cs=s.getCBar();
        if(result==null){
          assert nl.getNewName()!=null;
          List<String>newCs=new ArrayList<>(cs);
          newCs.set(cs.size()-1-extraCs, nl.getNewName());
          return Path.outer(s.outerNumber(),newCs);
          }
        List<String> path =cs.subList(cs.size()-extraCs,cs.size());
        for(String si:path){
          result=result.pushC(si);
        }
        return result.setNewOuter(result.outerNumber()+myDept);
      }
      public static boolean compatible(List<Member> current,  List<Integer> currentIndexes,List<ClassB>currentCb, NestedLocator nl) {
        int size=nl.getMPos().size();
        assert currentIndexes.size()==current.size();
        assert size==nl.getMTail().size();
        if(current.size()<size+1){return false;}//the extra name in nl.that
        for(int i=0;i<size;i++){
          int indexC=currentIndexes.get(i);
          int indexPos=nl.getMPos().get(i);
          if(currentCb.get(i)!=null && nl.getMOuters().get(i)!=null && indexC!=indexPos){return false;}
          Member ci = current.get(i);
          Member nli = nl.getMTail().get(i);
          if(ci==nli){continue;}
          if(ci.getClass()!=nli.getClass()){return false;}
          if(!(ci instanceof ClassB.NestedClass)){return false;}
          String nci=((ClassB.NestedClass)ci).getName();
          String nnli=((ClassB.NestedClass)nli).getName();
          if(!nci.equals(nnli)){return false;}
        }
        return ((ClassB.NestedClass)current.get(size)).getName().equals(nl.getThat());
      }
    }



