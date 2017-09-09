package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tools.Assertions;
import coreVisitors.CloneVisitor;
import coreVisitors.CollectPaths0;
import coreVisitors.IsCompiled;
import facade.Configuration;
import ast.Ast;
import ast.Ast.FieldDec;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;

public class UsedPathsPlus {
  public List<Path> of(ClassB cb){
    List<Path> result=new ArrayList<Path>();
    result.addAll(cb.getSuperPaths());
    result.addAll(cb.getDoc1().getPaths());
    for(Member m:cb.getMs()){m.match(
      nc->collectPaths(result,nc),
      mi->collectPaths(result,mi),
      mt->collectPaths(result,mt)
      );}
    //Set<Path> result2=Functions.remove1OuterAndPrimitives(result);
    return result;
  }
  Void collectPaths(List<Path> ps,ExpCore e){
    //class CollectReceiversPaths extends CloneVisitor{
    class CollectPaths extends CloneVisitor{
      public ExpCore visit(ClassB s) {return s;}

      @Override public Path liftP(Path s){
        if(!s.isPrimitive()){ps.add(s);}
        return super.liftP(s);
        }
      }
    e.accept(new CollectPaths());
    return null;
  }

  Void collectPaths(List<Path> ps,NestedClass nc){
    if(! (nc.getInner() instanceof ClassB)){return null;}
    ClassB cb=(ClassB)nc.getInner();
    assert IsCompiled.of(cb);
    //assert Configuration.typeExtraction.isCt(cb);
    ps.addAll(nc.getDoc().getPaths());
    ps.addAll(Functions.remove1OuterAndPrimitives(of(cb)));
    return null;
  }
  /*Void collectPaths(List<Path> ps,Ast.NormType t){
    t.match(nt->ps.add(nt.getPath()), hType->ps.add(hType.getPath()));
    return null;
  }*/
  /*Void collectPaths(List<Path> ps,Ast.ConcreteHeader ch){
    for( FieldDec f:ch.getFs()){
      collectPaths(ps,f.getT());
      }
    return null;
  }*/
  Void collectPaths(List<Path> ps,MethodWithType mwt){
    if(mwt.get_inner()!=null){
      collectPaths(ps,mwt.getInner());
      ps.addAll(CollectPaths0.of(mwt.with_inner(null)));
    }
    else ps.addAll(CollectPaths0.of(mwt));
    return null;
  }
  /*Void collectPaths(List<Path> ps, MethodType mt) {
    collectPaths(ps,mt.getReturnType());
    for(Ast.NormType t:mt.getTs()){
      collectPaths(ps,t);
      }
    ps.addAll(mt.getExceptions());
    return null;
  }*/
  Void collectPaths(List<Path> ps,MethodImplemented mi){
    //not true any more throw new ast.InternalError.InterfaceNotFullyProcessed();
    //never invoked in base class? - invoked in extended version?
    collectPaths(ps,mi.getInner());
    return null;
    }
}
