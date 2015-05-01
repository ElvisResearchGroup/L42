package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import coreVisitors.IsCompiled;
import ast.Ast;
import ast.Ast.FieldDec;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;

public class UsedPaths extends UsedPathsPlus{
  Void collectPaths(List<Path> ps,NestedClass nc){
    assert nc.getInner() instanceof ClassB;
    ClassB cb=(ClassB)nc.getInner();
    assert IsCompiled.of(cb);
    //No, since it enters in classBs in expressions
    //assert ExtractTypeStep.isCt(cb);
    ps.addAll(of(cb));
    return null;
  }
  Void collectPaths(List<Path> ps,MethodWithType mwt){
    super.collectPaths(ps, mwt);
    if(mwt.getInner().isPresent()){
      List<ClassB> cbs = CollectClassBs0.of(mwt.getInner().get());
      for(ClassB cb:cbs){ ps.addAll(of(cb)); }
      }
    return null;
  }
  Void collectPaths(List<Path> ps,MethodImplemented mi){
    ps.addAll(CollectPaths0.of(mi.getInner()));
    List<ClassB> cbs = CollectClassBs0.of(mi.getInner());
    for(ClassB cb:cbs){ ps.addAll(of(cb));}
    return null;
    }
  }
