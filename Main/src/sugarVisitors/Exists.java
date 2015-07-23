package sugarVisitors;

import java.util.function.Predicate;

import ast.Expression;

public class Exists extends CloneVisitor{
  Predicate<Expression> pred;
  @SuppressWarnings("serial")
  private static class Found extends RuntimeException{}
  private Exists(Predicate<Expression> pred){this.pred=pred;}
  public static boolean of(Expression e,Predicate<Expression>pred){
    try{new Exists(pred).lift(e);}
    catch(Found f){return true;}
    return false;
  }
  @Override
  protected <T extends Expression>T lift(T e){
    if(pred.test(e)){throw new Found();}
    return super.lift(e);
    }
}
