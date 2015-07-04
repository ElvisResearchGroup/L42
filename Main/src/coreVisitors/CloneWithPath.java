package coreVisitors;

import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class CloneWithPath extends CloneVisitor{
  Path path=Path.outer(0);
  public Path getPath(){return path;}
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    Path old=path;
    path=path.pushC(nc.getName());
    try{return super.visit(nc);}
    finally{path=old;}
    }  
}
