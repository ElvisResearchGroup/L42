package l42FVisitors;

import java.util.List;

import ast.L42F.*;

public interface BodyVisitor<V> {
V visitEmpty(SimpleBody s);
V visitSetter(SimpleBody s);
V visitGetter(SimpleBody s);
V visitNew(SimpleBody s);
V visitNewWithFwd(SimpleBody s);
V visitNewFwd(SimpleBody s);
V visitNativeIntSum(SimpleBody s);
V visitE(E s);
}
