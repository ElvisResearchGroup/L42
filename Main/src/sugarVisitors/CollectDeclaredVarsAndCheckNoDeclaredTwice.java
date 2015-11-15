package sugarVisitors;

import java.util.HashSet;

import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.*;

public class CollectDeclaredVarsAndCheckNoDeclaredTwice extends CloneVisitor {
  HashSet<String> xs=new HashSet<String>();
  public static HashSet<String> of(Expression e){
    CollectDeclaredVarsAndCheckNoDeclaredTwice cdv=new CollectDeclaredVarsAndCheckNoDeclaredTwice();
    e.accept(cdv);
    return cdv.xs;
  }
  @Override
  protected <T extends Expression>T lift(T e){
    try{  return super.lift(e); }
    catch(ErrorMessage.VariableDeclaredMultipleTimes err){
      if(e instanceof ast.Ast.HasPos){
        throw err.withPos(((ast.Ast.HasPos)e).getP());
      } else throw err;
    }
    
  }
  //Note: this also work for with is and es!   
  @Override
  protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
    if(xs.contains(d.getX())){
      throw new ErrorMessage.VariableDeclaredMultipleTimes(d.getX(),null);
      }
    assert d.getX().length()>0;
    //if(d.getX().length()>0){xs.add(d.getX());}
    xs.add(d.getX());
    return super.liftVarDecXE(d);
  }
  @Override 
  protected ast.Ast.Catch liftK(ast.Ast.Catch k){
    if(xs.contains(k.getX())){
      throw new ErrorMessage.VariableDeclaredMultipleTimes(k.getX(),null);
      }
    if(k.getX().length()>0){xs.add(k.getX());}
    return super.liftK(k);
    } 
  public Expression visit(ClassB s) {
    return s;
  }
  
}