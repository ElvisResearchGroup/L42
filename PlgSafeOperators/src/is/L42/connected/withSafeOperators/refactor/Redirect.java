package is.L42.connected.withSafeOperators.refactor;

import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import programReduction.Program;

/*Note the type system prevents to return a class T with T without methods.
This is needed to avoid the redirect from violating metalevel soundness:
 
 TT:Resource<><{T:{interface} method class T id(class T that){return that}}
 
 if we was to allow TT to be well typed, the result of
 
 C:Redirect[\"T" into Any]<><TT()
 
 would not be well typed: we can not return class Any (is ill typed). Otherwise we may cast
  a class Any obtained thanks to the distinction between tryTyped and tryCohereny
  back to its original Path (that may not be coherent) and call class methods on it.
 
 */
public class Redirect {
  public static ClassB redirectS(PData pData,ClassB that,String src,ast.Ast.Path dest) throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit{
    return redirectJ(pData,that,PathAux.parseValidCs(src),dest);
    }
  public static ClassB redirectJ(PData pData,ClassB that,List<Ast.C> src,ast.Ast.Path dest) throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit{
    assert dest.isCore() || dest.isPrimitive():
      dest;
    if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}
    return new is.L42.connected.withSafeOperators.refactor.RedirectObj(that).redirect(pData.p,src, dest);
    }
}
