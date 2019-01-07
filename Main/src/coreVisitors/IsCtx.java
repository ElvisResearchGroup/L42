package coreVisitors;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;

public class IsCtx extends CloneVisitor{
  @SuppressWarnings("serial")
  private static class Found extends RuntimeException{}
  private IsCtx(){}
  public ExpCore visit(ClassB s) {return s;}
  public ExpCore visit(ExpCore.WalkBy s) {throw new Found();}
  public static boolean of(ExpCore e){
    try{e.accept(new IsCtx());return false;}
    catch(Found f){return true;}
  }
}
