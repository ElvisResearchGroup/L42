package coreVisitors;

import facade.Configuration;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Program;

public class CloneVisitorWithProgram extends CloneVisitor{
  public CloneVisitorWithProgram(Program p) {this.p = p;}
  protected Program p;
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    Program aux=p;
    p=p.pop().addAtTop(p.top().withMember(nc.withInner(new WalkBy())));
    try{return super.visit(nc);}
    finally{p=aux;}
    }
  public ExpCore visit(ClassB s) {
    ClassB ct=Configuration.typeSystem.typeExtraction(p,s);
    Program aux=p;
    p=p.addAtTop(ct);
    try{return super.visit(s);}
    finally{p=aux;}
  }  
  public ClassB startVisit(ClassB s) {
    //so that visitClassB can be overridden independently
    ClassB ct=Configuration.typeSystem.typeExtraction(p,s);
    Program aux=p;
    p=p.addAtTop(ct);
    try{return (ClassB)super.visit(s);}
    finally{p=aux;}
  }
}
