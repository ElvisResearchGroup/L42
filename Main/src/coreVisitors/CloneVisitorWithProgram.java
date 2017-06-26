package coreVisitors;

import facade.Configuration;

import java.util.ArrayList;
import java.util.List;

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
  private List<Ast.C> whereFromTop=new ArrayList<>();
  protected List<Ast.C> whereFromTop(){
    return this.whereFromTop.subList(1,this.whereFromTop.size());
    //to remove a dumb null at the start :(
    }
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
      whereFromTop.add((Ast.C)lastCMs);
      }
    else{
      p=p.evilPush(s);
      whereFromTop.add(null);
      }
    levels+=1;
    Object auxO=lastCMs;
    lastCMs=null;
    try{return super.visit(s);}
    finally{
      p=aux;
      lastCMs=auxO;
      levels-=1;
      whereFromTop.remove(whereFromTop.size()-1);
      }
  }
}
