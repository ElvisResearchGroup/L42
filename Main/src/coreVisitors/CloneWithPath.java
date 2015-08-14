package coreVisitors;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;

public class CloneWithPath extends CloneVisitor{
  public CloneWithPath(){path=new ArrayList<>();}
  /*public CloneWithPath(List<String>path){
    this.path=path;
    }*/
  List<ClassB.Member> path=new ArrayList<>();
  List<Integer> pathNums=new ArrayList<>();
  public List<ClassB.Member> getAstNodesPath(){return null;}
  public List<Integer> getAstIndexesPath(){return null;}
  public List<String> getClassNamesPath(){
    //assert path.get(path.size()-1)==null;
    List<String> sPath=new ArrayList<>();
    for(Member m:this.path){
      m.match(nc->sPath.add(nc.getName()), mi->sPath.add(null), mt->sPath.add(null));
    }
    if(!pathNums.isEmpty() && pathNums.get(pathNums.size()-1)==0){
      sPath.remove(sPath.size()-1);
    }
    return sPath;
    }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    int pathSize=path.size();
    assert pathSize==pathNums.size();
    path.add(nc);
    pathNums.add(0);
    try{return super.visit(nc);}
    finally{
      assert pathSize+1==path.size();
      assert pathSize+1==pathNums.size();
      path.remove(pathSize);
      pathNums.remove(pathSize);
      }
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    int pathSize=path.size();
    assert pathSize==pathNums.size();
    path.add(mt);
    pathNums.add(0);
    try{return super.visit(mt);}
    finally{
      assert pathSize+1==path.size();
      assert pathSize+1==pathNums.size();
      path.remove(pathSize);
      pathNums.remove(pathSize);
      }
  }  
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    int pathSize=path.size();
    assert pathSize==pathNums.size();
    path.add(mi);
    pathNums.add(0);
    try{return super.visit(mi);}
    finally{
      assert pathSize+1==path.size();
      assert pathSize+1==pathNums.size();
      path.remove(pathSize);
      pathNums.remove(pathSize);
      }
  }
  public ExpCore visit(ClassB cb){
    int last=pathNums.size()-1;
    if(last!=-1){pathNums.set(last,pathNums.get(last)+1);}
    return super.visit(cb);
  }
    
  //protected List<Path> liftSup(List<Path> supertypes) {//SOB... to synchronise the last null
   
}
