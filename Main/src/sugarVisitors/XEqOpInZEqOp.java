package sugarVisitors;
import ast.Ast;
import ast.Expression;
import ast.Expression.*;
public class XEqOpInZEqOp extends CloneVisitor{
  public XEqOpInZEqOp(X x, X z) {
    this.x = x; this.z = z;
  }
  X x;
  X z;
  public static Expression of(X x,X z, Expression e){
    return e.accept(new XEqOpInZEqOp(x, z));
  }
  public Expression visit(BinOp s) {
    if(s.getOp().kind!=Ast.OpKind.EqOp){return super.visit(s);}
    if(!s.getLeft().equals(x)){return super.visit(s);}
    return super.visit(s.withLeft(z));
  }
  
}
