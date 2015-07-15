package coreVisitors;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class CloneWithPath extends CloneVisitor{
  public CloneWithPath(){path=new ArrayList<>();}
  public CloneWithPath(List<String>path){this.path=path;}
  List<String> path=new ArrayList<>();
  public List<String> getPath(){return path;}
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    List<String> old=path;
    path=new ArrayList<>(path);
    path.add(nc.getName());
    try{return super.visit(nc);}
    finally{path=old;}
    }
}
