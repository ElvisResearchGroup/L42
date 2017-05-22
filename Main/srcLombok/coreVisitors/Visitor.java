package coreVisitors;

import ast.ExpCore.*;

public interface Visitor<T> {
  T visit(EPath s);
  T visit(X s);
  T visit(_void s);
  T visit(WalkBy s);
  T visit(Using s);
  T visit(Signal s);
  T visit(MCall s);
  T visit(Block s);
  T visit(ClassB s);
  T visit(Loop s);
  T visit(UpdateVar s);

}
