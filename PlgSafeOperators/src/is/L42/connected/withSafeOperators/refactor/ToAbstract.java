package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.PathAux;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import facade.PData;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.location.Lib;
import is.L42.connected.withSafeOperators.location.Location;
import is.L42.connected.withSafeOperators.location.Method;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import programReduction.Program;
import tools.LambdaExceptionUtil.*;

public class ToAbstract {
public static ClassB toAbstractPathJ(PData pData,ClassB cb, List<Ast.C> path,MethodSelector sel) throws SelectorUnfit, PathUnfit, MethodClash{
  return toAbstractAux(pData.p,cb,path,sel,null);
  }
public static ClassB toAbstractPathDestJ(PData pData,ClassB cb, List<Ast.C> path,MethodSelector sel,MethodSelector newSel) throws SelectorUnfit, PathUnfit, MethodClash{
  return toAbstractAux(pData.p,cb,path,sel,newSel);
  }
public static ClassB toAbstractJ(PData pData,ClassB cb,MethodSelector sel) throws SelectorUnfit, PathUnfit, MethodClash{
  return toAbstractAux(pData.p,cb,Collections.emptyList(),sel,null);
  }
public static ClassB toAbstractDestJ(PData pData,ClassB cb, MethodSelector sel,MethodSelector newSel) throws SelectorUnfit, PathUnfit, MethodClash{
  return toAbstractAux(pData.p,cb,Collections.emptyList(),sel,newSel);
  }

public static ClassB toAbstractAux(Program p,ClassB cb, List<Ast.C> path,MethodSelector sel,MethodSelector newSel) throws SelectorUnfit, PathUnfit, MethodClash{
  if(MembersUtils.isPrivate(path)){
    throw new RefactorErrors.PathUnfit(path).msg("Private path");
    }
  if(MembersUtils.isPrivate(sel)){
    throw new RefactorErrors.SelectorUnfit(path,sel).msg("Private selector");
    }
  if(newSel!=null && MembersUtils.isPrivate(newSel)){
    throw new RefactorErrors.SelectorUnfit(path,newSel).msg("Private selector");
    }
  if(newSel!=null && sel.getNames().size()!=newSel.getNames().size()){
    throw new RefactorErrors.SelectorUnfit(path,newSel).msg("Selector has wrong number of parameters");
    }
  try{
    ClassB nested=cb.getClassB(path);
    //interfaces are ok if(nested.isInterface()){throw new RefactorErrors.PathUnfit(Location.as42Path(path));}
    if(nested._getMember(sel)==null){throw new RefactorErrors.SelectorUnfit(path,sel);}
    }
  catch(ast.ErrorMessage.PathMetaOrNonExistant unused){
    throw new RefactorErrors.PathUnfit(path);
    }
  if(path.isEmpty()){return auxToAbstract(p,cb,path,sel,newSel,cb);}
  return cb.onClassNavigateToPathAndDo(path, CheckedFunction.uncheck(cbi->auxToAbstract(p,cbi,path,sel,newSel,cb)));
  }
  private static ClassB auxToAbstract(Program p,ClassB cb,List<Ast.C> path,MethodSelector sel,MethodSelector newSel,ClassB top) throws MethodClash {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    MethodWithType _mwt = (MethodWithType) cb._getMember(sel);
    boolean isSrcAbstract=_mwt.get_inner()==null;
    MethodWithType mwt1=_mwt.with_inner(null);
    Functions.replaceIfInDom(newMs,mwt1);
    if(isSrcAbstract){ return cb;}
    if(newSel==null){ return cb.withMs(newMs);  }
    MethodWithType mwt2 = (MethodWithType) cb._getMember(newSel);
    if(mwt2==null){
      mwt2=_mwt.withMs(newSel).withMt(_mwt.getMt().withRefine(false));
      if(!newSel.getNames().equals(_mwt.getMs().getNames())){
        mwt2=mwt2.withInner(MembersUtils.renameParNames(
          _mwt.getMs().getNames(), newSel.getNames(),
          mwt2.getInner()));
        }
      newMs.add(0,mwt2);
      return cb.withMs(newMs);
      }
    boolean ok=Compose.mtGT(p, mwt1.getMt(), mwt2.getMt());
    boolean noTwoImpl=isSrcAbstract ||mwt2.get_inner()==null;
    if(!ok || !noTwoImpl){
      Lib root=Lib.newFromLibrary(top);
      Lib nested=root.navigateCs(path);
      Method m1=new Method(_mwt,nested);
      Method m2=new Method(mwt2,nested);
      throw new RefactorErrors.MethodClash(m1,m2);
      }
    if(isSrcAbstract){return cb;}
    mwt2=mwt2.with_inner(_mwt.get_inner());
    Functions.replaceIfInDom(newMs,mwt2);
    return cb.withMs(newMs);
    }
  }

interface ToAbstractSpec{
/**<pre>
If sel is abstract, always return l unmodified. 
check for subtying only if sel is not abstract and newSel exists.
If a new method is introduced, it is not refine.
//Note: I believed it was better to always test for subtyping.
 * But this more relaxed operator can be used in a simpler way.
</pre>*/
void toAbstract();
}
