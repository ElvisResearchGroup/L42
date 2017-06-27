package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import programReduction.Program;

public class Redirect {
  public static ClassB redirect(PData pData,ClassB that,String src,ast.Ast.Path dest) throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit{
    assert dest.isCore() || dest.isPrimitive():
      dest;
    if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}
    return new is.L42.connected.withSafeOperators.refactor.RedirectObj(that).redirect(pData.p,PathAux.parseValidCs(src), dest);
    }
}
