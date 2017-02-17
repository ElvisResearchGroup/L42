package coreVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import tools.Map;
import ast.Ast.*;
import ast.ExpCore.ClassB;
import ast.ExpCore;

public class FromInClass extends CloneVisitor {
  private int j;
  private Path path;
  public FromInClass(int j,Path path){
    this.j=j;this.path=path;
    }
  @Override public ExpCore visit(Path p){
    assert j>=1;
    if(p.isPrimitive()){return p;}
    int n=p.outerNumber();
    if(n<j){return p;}
    //Path result=From.fromP(p, addOuter(j-1,path));
    Path nLessJ=addOuter(-j,p);
    nLessJ=From.fromP(nLessJ,path);
    Path result= addOuter(j,nLessJ);
    return result;
    }
  public ExpCore visit(ClassB s) {
    int oldJ=this.j;
    this.j+=1;
    try{return super.visit(s);}
    finally{this.j=oldJ;}
  }
  private static Path addOuter(int k,Path p){
    return Path.outer(p.outerNumber()+k,p.getCBar());
  }
  public static ExpCore of(ClassB cb, Path source){
    return cb.accept(new FromInClass(0,source));
  }
}
