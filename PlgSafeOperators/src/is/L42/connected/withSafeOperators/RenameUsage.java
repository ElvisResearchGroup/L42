package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.MethodSelectorX;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.MCall;
import ast.Util;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.InfoAboutMs;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Locator.Kind;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;

public class RenameUsage extends MethodPathCloneVisitor {
  RenameUsage(ClassB visitStart,CollectedLocatorsMap maps,Program p) {
    super(visitStart,maps,p);
  }
  
  @Override public MethodSelector visitMS(MethodSelector original,Path src){
      assert src!=null;
      List<Locator> filtered=new ArrayList<>();
      for(Locator pMx:maps.selectors){
        MethodSelector mii=pMx.getLastMember().match(
            nc->{throw Assertions.codeNotReachable();},
            mi->mi.getS(),
            mt->mt.getMs());
        if(original.equals(mii)){filtered.add(pMx);}
      }
      if(filtered.isEmpty()){return original;}
      Locator pathOriginal=this.getLocator().copy();
      boolean isOut=pathOriginal.moveInPath(src);
      if(isOut){return original;}
      for(Locator pMx:filtered){
          Locator pathDef=pMx;
          pathDef.toFormerNodeLocator();
          if(!pathDef.equals(pathOriginal)){continue;}
          return (MethodSelector)pMx.getAnnotation();
          }
        return original;
    }
}