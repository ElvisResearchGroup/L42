package sugarVisitors;

import ast.Ast;
import ast.Expression.*;

public interface Visitor<T> {
  T visit(Signal s);
  T visit(If s);
  T visit(While s);
  T visit(With s);
  T visit(X s);
  T visit(ContextId s);
  T visit(BinOp s);
  T visit(DocE s);
  T visit(UnOp s);
  T visit(MCall s);
  T visit(FCall s);
  T visit(SquareCall s);
  T visit(SquareWithCall s);
  T visit(RoundBlock s);
  T visit(CurlyBlock s);
  T visit(Using s);
  T visit(ClassB s);
  T visit(DotDotDot s);
  T visit(WalkBy s);
  T visit(_void s);
  T visit(Literal s);
  T visit(Ast.Path s);
  T visit(Loop s);
  T visit(ClassReuse s);
  T visit(UseSquare s);
}
