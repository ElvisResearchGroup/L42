package l42FVisitors;

import java.util.List;

import ast.L42F.*;

public interface Visitor<V> {
V visit(Block s);
V visit(X s);
V visit(Cn s);
V visit(_void s);
V visit(Null s);
V visit(Unreachable s);
V visit(BreakLoop s);
V visit(Throw s);
V visit(Loop s);
V visit(Call s);
V visit(Use s);
V visit(If s);
V visit(Update s);
V visit(Cast s);
}
