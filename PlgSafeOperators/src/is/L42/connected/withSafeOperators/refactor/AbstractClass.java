package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import coreVisitors.CloneVisitorWithProgram;
import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.PathAux;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore.*;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import ast.Util.CsPath;
import auxiliaryGrammar.Functions;
import facade.PData;
import programReduction.Program;
import tools.Assertions;
import tools.Map;
import is.L42.connected.withSafeOperators.CollectedLocatorsMap;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.MethodPathCloneVisitor;
import is.L42.connected.withSafeOperators.RenameUsage;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PrivacyCoupuled;

class CollectUsages{
  List<List<Ast.C>> coupuledPaths=new ArrayList<>();
  List<CsMx> coupuledMethods=new ArrayList<>();

  public static void checkPrivacyCoupuled(Program p,List<Ast.C>path,ClassB l,ClassB clear) throws PrivacyCoupuled{
    CollectUsages us=new CollectUsages();
    List<List<Ast.C>>prPath=ExtractInfo.collectPrivatePathsAndSubpaths(l,path);
    List<CsMxMx>prMeth=ExtractInfo.collectPrivateMethodsOfPublicPaths(l,path);
    us.process(p.evilPush(l),clear,prPath,prMeth);
    if(us.coupuledMethods.isEmpty() && us.coupuledPaths.isEmpty()){return;}
    throw new RefactorErrors.PrivacyCoupuled(us.coupuledPaths, us.coupuledMethods);
    }
  private void process(Program p,ClassB l,List<List<Ast.C>>paths,List<CsMxMx> renames) {
    l.accept(new CloneVisitorWithProgram(p) {
      @Override
      public Path liftP(Path that) {
        // Just for optimisation
        if (that.isPrimitive() || that.getCBar().isEmpty())
          return that;


        for (List<Ast.C> cs : paths) {
          Path srcHere = Path.outer(levels, cs);
          if (p._equivSubPath(srcHere, that) != null)
            coupuledPaths.add(cs);
        }

        return that;
      }
    });
  l.accept(new RenameMethodsAux(p,renames,l){
  public MethodSelector mSToReplaceOrNull(MethodSelector original,Path src){
    MethodSelector sup=super.mSToReplaceOrNull(original, src);
    if(sup!=null){
      List<C> wft = whereFromTop();
      wft=new ArrayList<>(wft.subList(0, wft.size()-src.outerNumber()));
      wft.addAll(src.getCBar());
      coupuledMethods.add(new CsMx(wft,original));
      }
    return null;
    }
  });
  }
}

public class AbstractClass {
  public static ClassB toAbstractJ(PData p,ClassB cb, List<Ast.C> path) throws PathUnfit, PrivacyCoupuled{
    return toAbstract(p.p,cb,path);
    }
  public static ClassB toAbstractS(PData p,ClassB cb, String path) throws PathUnfit, PrivacyCoupuled{
    return toAbstract(p.p,cb,PathAux.parseValidCs(path));
    }
  public static ClassB toAbstract(Program p,ClassB cb, List<Ast.C> path) throws PathUnfit, PrivacyCoupuled{
  if(!MembersUtils.isPathDefined(cb, path)){throw new RefactorErrors.PathUnfit(path);}
  if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
    //check privacy coupled
    ClassB cbClear=cb.onClassNavigateToPathAndDo( path, cbi->clear(cbi));
    CollectUsages.checkPrivacyCoupuled(p,path,cb,cbClear);
    return cbClear;
  }

  private static ClassB clear(ClassB cb) {
    //note: it would be wrong to remove implementation of private interfaces:
    //otherwise a use could rely on methods (the interface-implemented ones) that do not
    //exists any more.
    //we choose to not remove private trown exceptions just for coherence.
    //it is empty, cut it down!
    List<Member> newMs=new ArrayList<>();
    for(Member m:cb.getMs()){
      m.match(
          nc->{
            if(nc.getName().isUnique()){return null;}
            newMs.add(nc.withInner(clear((ClassB)nc.getInner())));return null;
            },
          mi->{throw Assertions.codeNotReachable();},
          mt->{
            if(mt.getMs().isUnique()){return null;}
            newMs.add(mt.with_inner(null));return null;}
      );}
    //create new class
    return cb.withMs(newMs);
  }
}