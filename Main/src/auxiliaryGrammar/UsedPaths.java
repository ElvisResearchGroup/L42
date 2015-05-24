package auxiliaryGrammar;

import java.util.List;

import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import coreVisitors.IsCompiled;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;

public class UsedPaths extends UsedPathsPlus{
  Void collectPaths(List<Path> ps,MethodWithType mwt){
    super.collectPaths(ps, mwt);
    if(mwt.getInner().isPresent()){
      List<ClassB> cbs = CollectClassBs0.of(mwt.getInner().get());
      for(ClassB cb:cbs){ ps.addAll(Functions.remove1OuterAndPrimitives(of(cb))); }
      }
    return null;
  }
  Void collectPaths(List<Path> ps,MethodImplemented mi){
    ps.addAll(CollectPaths0.of(mi.getInner()));
    List<ClassB> cbs = CollectClassBs0.of(mi.getInner());
    for(ClassB cb:cbs){ ps.addAll(Functions.remove1OuterAndPrimitives(of(cb)));}
    return null;
    }
  }
