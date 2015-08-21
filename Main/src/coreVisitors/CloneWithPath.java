package coreVisitors;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;

public class CloneWithPath extends CloneVisitor{
  private final List<ClassB.Member> path=new ArrayList<>();
  private final List<Integer> pathNums=new ArrayList<>();
  private final List<ClassB> pathCb=new ArrayList<>();
  public List<ClassB.Member> getAstNodesPath(){return path;}
  public List<Integer> getAstIndexesPath(){return pathNums;}
  public List<ClassB> getAstCbPath(){return pathCb;}
  public List<String> getClassNamesPath(){
    //assert path.get(path.size()-1)==null;
    List<String> sPath=new ArrayList<>();
    for(Member m:this.path){
      m.match(nc->sPath.add(nc.getName()), mi->sPath.add(null), mt->sPath.add(null));
    }
    if(!pathNums.isEmpty() && pathCb.get(pathNums.size()-1)==null){
      sPath.remove(sPath.size()-1);
    }
    return sPath;
    }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    int pathSize=pushMember(nc);
    try{return super.visit(nc);}
    finally{
      popLast(pathSize);
      }
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    int pathSize = pushMember(mt);
    try{return super.visit(mt);}
    finally{
      popLast(pathSize);
      }
  }

  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    int pathSize=pushMember(mi);
    try{return super.visit(mi);}
    finally{
      popLast(pathSize);
      }
  }
  public ExpCore visit(ClassB cb){
    int last=pathNums.size()-1;
    if(last!=-1){
      pathNums.set(last,pathNums.get(last)+1);
      pathCb.set(last, cb);
      }
    try{return super.visit(cb);}
    finally{ 
      if(last!=-1){
        pathNums.set(last,pathNums.get(last));
        pathCb.set(last, null);
      }}
  }
    
  //protected List<Path> liftSup(List<Path> supertypes) {//SOB... to synchronise the last null
  private int pushMember(ClassB.Member m) {
    int pathSize=path.size();
    assert pathSize==pathNums.size();
    assert pathSize==pathCb.size();
    path.add(m);
    pathNums.add(0);
    pathCb.add(null);
    return pathSize;
  }
  private void popLast(int pathSize) {
    assert pathSize+1==path.size();
    assert pathSize+1==pathNums.size();
    assert pathSize+1==pathCb.size();
    path.remove(pathSize);
    pathNums.remove(pathSize);
    pathCb.remove(pathSize);
  }     
}
