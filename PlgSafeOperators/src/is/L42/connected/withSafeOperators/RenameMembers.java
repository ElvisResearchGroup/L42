package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.List;


import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.Util.NestedLocator;
import ast.Util.PathPath;

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
  private static NestedLocator pathPathToLocator(Path src, Path dest) {
    List<String> cs = src.getCBar();
    List<Member> mTail=new ArrayList<>();
    List<Integer> mPos=new ArrayList<>();
    addCs(cs.subList(0, cs.size()-1), mTail, mPos);
    NestedLocator nl=new NestedLocator(mTail, mPos, cs.get(cs.size()-1));
    nl.setNewPath(dest);
    return nl;
  }
  private static void addCs(List<String> cs, List<Member> mTail, List<Integer> mPos) {
    for(String s:cs){
      mTail.add(new ClassB.NestedClass(ast.Ast.Doc.empty(),s,new ExpCore.WalkBy(), null));
      mPos.add(1);
      }
  }  
  
  
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String>cs=s.getCBar();
        if(cs.isEmpty()){return s;}//no need to rename outers
        List<Member> current = new ArrayList<>(this.getAstNodesPath());
        List<Integer> currentIndexes = new ArrayList<>(this.getAstIndexesPath());
        boolean canCut=cutUpTo(s.outerNumber(),current,currentIndexes);
        if(!canCut){return s;}
        int whereImSize=current.size();
        addCs(s.getCBar(),current,currentIndexes);
        for(NestedLocator nl:maps.privatePaths){
          if(whereImSize>nl.getMPos().size()){continue;}
          //situation: rename: s1 c1->path   current path locator is:  whereIm c cs
          //check #whereIm<=#s1 and  whereIm c cs =s1 _ 
          assert isFullyPositive(nl.getMPos()):nl.getMPos();
          boolean compatible= compatible(current, currentIndexes, nl);
          if(!compatible){continue;}
          int extraCs=(current.size()-nl.getMPos().size())-1;//the class name in nl.that
          Path pi=getDestPath(this.getClassNamesPath().size(),nl,s,extraCs);//TODO:can be made more efficient without creating the listPaths
          return pi;
          }
        return s;
        }
      private boolean cutUpTo(int outerNumber, List<Member> current, List<Integer> currentIndexes) {
        int size=current.size();
        assert size==currentIndexes.size();
        if(size==0 && outerNumber>0){return false;}
        if(size==0){return true;}        
        if(currentIndexes.get(size-1)<=0){
          current.remove(size-1);
          currentIndexes.remove(size-1);
          return cutUpTo(outerNumber,current,currentIndexes);
        }
        if(outerNumber==0){return true;}
        current.remove(size-1);
        currentIndexes.remove(size-1);
        return cutUpTo(outerNumber-1,current,currentIndexes);
        }
      private boolean isFullyPositive(List<Integer>indexes){
        for(int i:indexes){if(i<=0)return false;}
        return true;
      }
      private Path getDestPath(int myDept,NestedLocator nl, Path s, int extraCs) {
        assert extraCs>=0:extraCs;
        Path result=nl.getNewPath();
        List<String>cs=s.getCBar();
        if(result==null){
          assert false: "test it in rename privates";
          List<String>newCs=new ArrayList<>(cs);
          //newCs.set(nl.getMTail().size()-cutSize,nl.getNewName());//+1?-1?
          result= Path.outer(s.outerNumber()+myDept,newCs);
          }
        List<String> path =cs.subList(cs.size()-extraCs,cs.size());
        for(String si:path){
          result=result.pushC(si);
        }
        /*if(result.outerNumber()==0){
          List<String> path = this.getClassNamesPath();
          path.addAll(cs.subList(cs.size()-extraCs,cs.size()));
          return ClassOperations.normalizePath(path,path.size(),result.getCBar());
          }*/
        return result.setNewOuter(result.outerNumber()+myDept);
      }
      private static boolean compatible(List<Member> current,  List<Integer> currentIndexes, NestedLocator nl) {
        int size=nl.getMPos().size();
        assert currentIndexes.size()==current.size();
        assert size==nl.getMTail().size();
        if(current.size()<size+1){return false;}//the extra name in nl.that
        for(int i=0;i<size;i++){
          int indexC=currentIndexes.get(i);
          int indexPos=nl.getMPos().get(i);
          if(indexC>0 && indexPos>0 && indexC!=indexPos){return false;}
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
