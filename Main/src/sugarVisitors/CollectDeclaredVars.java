package sugarVisitors;

import java.util.HashSet;

import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.*;

public class CollectDeclaredVars extends CloneVisitor {
  HashSet<String> xs=new HashSet<String>();
  public static HashSet<String> of(Expression e){
    CollectDeclaredVars cdv=new CollectDeclaredVars();
    e.accept(cdv);
    return cdv.xs;
  }
  

  //Note: this also work for with is and es!   
  @Override
  protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
    assert d.getX().length()>0;
    //if(d.getX().length()>0){xs.add(d.getX());}
    xs.add(d.getX());
    return super.liftVarDecXE(d);
  }
  @Override 
  protected Expression.Catch liftK(Expression.Catch k){
    if(k.getX().length()>0){xs.add(k.getX());}
    return super.liftK(k);
    } 
  public Expression visit(ClassB s) {
    return s;
  }
  
}