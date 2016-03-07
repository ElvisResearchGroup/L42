package sugarVisitors;
import tools.Assertions;
import ast.Ast;
import ast.Expression;
import ast.Expression.*;
public class XInE extends CloneVisitor{
  public XInE(X x, Expression eAlt) {
    this.x = x; this.eAlt = eAlt;
  }
  X x;
  Expression eAlt;
  public static Expression of(X x,Expression eAlt, Expression e){
    return e.accept(new XInE(x, eAlt));
  }

  public Expression visit(X s) {
    if(!s.equals(x)){return super.visit(s);}
    return eAlt;
  }
  public Expression visit(Expression.BinOp s) {
    if(s.getOp().kind!=Ast.OpKind.EqOp){return super.visit(s);}
    return s.withRight(s.getRight().accept(this));
  }

}
