package l42FVisitors;

import java.util.List;

import ast.MiniJ.*;

public interface JVisitor<V> {
V visit(B s);
V visit(Break s);
V visit(If s);
V visit(IfTypeCase s);
V visit(MCall s);
V visit(Return s);
V visit(Throw s);
V visit(Try s);
V visit(UseCall s);
V visit(VarAss s);
V visit(VarDec s);
V visit(WhileTrue s);
V visit(X s);
}
