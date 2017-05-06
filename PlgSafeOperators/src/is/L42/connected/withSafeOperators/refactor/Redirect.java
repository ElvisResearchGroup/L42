package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.PathAux;
import facade.PData;
import programReduction.Program;

public class Redirect {
  public static ClassB redirect(PData pData,ClassB that,String src,ast.Ast.Path dest){
    assert dest.isCore() || dest.isPrimitive():
      dest;
    if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}
    return is.L42.connected.withSafeOperators.Redirect.redirect(pData.p, that, Path.outer(0,PathAux.parseValidCs(src)), dest);
    }
}
