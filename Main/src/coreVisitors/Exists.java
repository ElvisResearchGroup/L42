package coreVisitors;

import java.util.function.Predicate;

import ast.ExpCore;


public class Exists extends CloneVisitor{
  Predicate<ExpCore> pred;
  private static class Found extends RuntimeException{}
  private Exists(Predicate<ExpCore> pred){this.pred=pred;}
  public static boolean of(ExpCore e,Predicate<ExpCore>pred){
    try{new Exists(pred).lift(e);}
    catch(Found f){return true;}
    return false;
  }
  @Override
  protected <T extends ExpCore>T lift(T e){
    if(pred.test(e)){throw new Found();}
    return super.lift(e);
    }
}
