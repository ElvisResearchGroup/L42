package sugarVisitors;
import tools.Assertions;

import java.util.function.Function;

import ast.Ast;
import ast.Expression;
import ast.Expression.*;
public class XInE extends CloneVisitor{
  public XInE(X x, Expression eAlt,Function<Expression.BinOp,Expression> eEqOp) {
    this.x = x; this.eAlt = eAlt;this.eEqOp=eEqOp;
  }
  X x;
  Expression eAlt;
  Function<Expression.BinOp,Expression> eEqOp;
  public static Expression of(X x,Expression eAlt, Expression e){
    return of(x,eAlt,null,e);
    }
  public static Expression of(X x,Expression eAlt,Function<Expression.BinOp,Expression> eEqOp, Expression e){
    return e.accept(new XInE(x, eAlt,eEqOp));
  }

  public Expression visit(X s) {
    if(!s.equals(x)){return super.visit(s);}
    return eAlt;
  }
  public Expression visit(Expression.BinOp s) {
    if(s.getOp().kind!=Ast.OpKind.EqOp){return super.visit(s);}
    if(!s.getLeft().equals(x)){return super.visit(s);}
    s=s.withRight(s.getRight().accept(this));
    if(eEqOp==null){return s;}
    return eEqOp.apply(s);
  }

}
