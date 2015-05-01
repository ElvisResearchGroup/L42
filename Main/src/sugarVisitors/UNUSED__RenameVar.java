package sugarVisitors;

import tools.Assertions;
import ast.Ast;
import ast.Expression;
import ast.Expression.*;

public class UNUSED__RenameVar extends CloneVisitor{
  String src; String dest;
  UNUSED__RenameVar(String src, String dest){this.src=src;this.dest=dest;}
  
  public Expression visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Expression visit(ClassB s) {return s;}
  public static Expression of(Expression e,String src, String dest){
    return e.accept(new UNUSED__RenameVar(src,dest));
  }
  public Expression visit(X s) {
    if(!src.equals(s.getInner())){return s;}
    return new X(dest);
    }
  
  protected Ast.Catch liftK(Ast.Catch k) {
    if(!src.equals(k.getX())){return super.liftK(k);}
    return super.liftK(k.withX(dest));
  }
  protected Ast.VarDecXE liftVarDecXE(Ast.VarDecXE d) {
    if(!src.equals(d.getX())){return super.liftVarDecXE(d);}
    return super.liftVarDecXE(d.withX(dest));
  }
}
