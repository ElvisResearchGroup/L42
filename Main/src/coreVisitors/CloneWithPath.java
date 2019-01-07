package coreVisitors;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Locator;

public class CloneWithPath extends CloneVisitor{
  private final Locator location=new Locator();
  public Locator getLocator(){return location;}
  
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    location.pushMember(nc);
    try{return super.visit(nc);}
    finally{
      location.toFormerNodeLocator();
      }
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    location.pushMember(mt);
    try{return super.visit(mt);}
    finally{
      location.toFormerNodeLocator();
      }
  }

  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    location.pushMember(mi);
    try{return super.visit(mi);}
    finally{
      location.toFormerNodeLocator();
      }
  }
  public ExpCore visit(ClassB cb){
    location.enterClassB(cb);
    try{return super.visit(cb);}
//    catch(NullPointerException npe){throw npe;}
    finally{ location.exitClassB(); }
  }
    
  //protected List<Path> liftSup(List<Path> supertypes) {//SOB... to synchronise the last null
  
}
