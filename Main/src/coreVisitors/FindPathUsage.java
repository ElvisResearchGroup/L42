package coreVisitors;

import ast.Ast;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;

public class FindPathUsage extends PropagatorVisitor{
  Ast.Position located=null;
  Path searchingFor;
  public static Ast.Position _locate(ClassB cb, Path p){
    FindPathUsage fpu=new FindPathUsage();
    fpu.searchingFor=p;
    cb.accept(fpu);
    return fpu.located;
  } 
  public Void visit(ClassB s) {
    Path oldP=searchingFor;
    searchingFor=searchingFor.setNewOuter(searchingFor.outerNumber()+1);
    try{return super.visit(s);}
    finally{searchingFor=oldP;}
    }
  public Void visit(ExpCore.EPath s) {
    return null;//TODO: need to do the injections
    //add using, type... type has no pos..
    }
  }
