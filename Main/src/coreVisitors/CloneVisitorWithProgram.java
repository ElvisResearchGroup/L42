package coreVisitors;

import facade.Configuration;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Member;
import programReduction.Program;

public class CloneVisitorWithProgram extends CloneVisitor{
  public CloneVisitorWithProgram(Program p) {this.p = p;}
  protected Program p;
  protected int levels=-1;
  private Object lastCMs=null;
  
  public Object getLastCMs(){return lastCMs;}
  public NestedClass visit(NestedClass s) {
    Object aux=lastCMs;
    lastCMs=s.getName();
    try{return super.visit(s);}
    finally{lastCMs=aux;}
    }
  public MethodImplemented visit(MethodImplemented s) {
    Object aux=lastCMs;
    lastCMs=s.getS();
    try{return super.visit(s);}
    finally{lastCMs=aux;}
  }
  public MethodWithType visit(MethodWithType s) {
    Object aux=lastCMs;
    lastCMs=s.getMs();
    try{return super.visit(s);}
    finally{lastCMs=aux;}
  }  
  public ExpCore visit(ClassB s) {
    Program aux=p;
    if(lastCMs!=null && lastCMs instanceof Ast.C){
      p=p.push((Ast.C)lastCMs);
      }
    else{
      p=p.evilPush(s);
      }
    levels+=1;
    Object auxO=lastCMs;
    lastCMs=null;
    try{return super.visit(s);}
    finally{p=aux; lastCMs=auxO;levels-=1;}
  }
}
