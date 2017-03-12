package coreVisitors;

import facade.Configuration;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.Member;
import programReduction.Program;

public class CloneVisitorWithProgram extends CloneVisitor{
  public CloneVisitorWithProgram(Program p) {this.p = p;}
  protected Program p;
 
  public ExpCore visit(ClassB s) {
    Program aux=p;
    p=p.evilPush(s);
    try{return super.visit(s);}
    finally{p=aux;}
  }
  public ClassB startVisit(ClassB s) {
    //so that visitClassB can be overridden independently
    //Configuration.typeSystem.computeStage(p,s);//TODO: is it needed?
    Program aux=p;
    p=p.evilPush(s);
    try{return (ClassB)super.visit(s);}
    finally{p=aux;}
  }
}
