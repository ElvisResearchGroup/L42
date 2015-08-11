package coreVisitors;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class CloneWithPath extends CloneVisitor{
  public CloneWithPath(){path=new ArrayList<>();}
  public CloneWithPath(List<String>path){this.path=path;}
  List<String> path=new ArrayList<>();
  public List<String> getPath(){
    //assert path.get(path.size()-1)==null;
    if(path.isEmpty()){return path;}
    if(path.get(path.size()-1)==null){
      return path.subList(0, path.size()-1);
    }
    return path;
    }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    List<String> old=path;
    path=new ArrayList<>(path);
    path.add(nc.getName());
    //assert nc.getInner() instanceof ClassB:"CloneWithPath works only for compiled classes, to deal with paths + class literals in methods";
    if(!(nc.getInner() instanceof ClassB)){return nc;}
    try{return super.visit(nc);}
    finally{path=old;}
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    List<String> old=path;
    path=new ArrayList<>(path);
    path.add(null);
    try{return super.visit(mt);}
    finally{path=old;}
  }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    List<String> old=path;
    path=new ArrayList<>(path);
    path.add(null);
    try{return super.visit(mi);}
    finally{path=old;}
  }
  protected List<Path> liftSup(List<Path> supertypes) {//SOB... to sincronize the last null
    List<String> old=path;
    path=new ArrayList<>(path);
    path.add(null);
    try{return super.liftSup(supertypes);}
    finally{path=old;}
  }
}
