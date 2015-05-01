package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
  public Set<Path> of(ClassB cb){
    List<Path> result=new ArrayList<Path>();        
    result.addAll(cb.getSupertypes());
    result.addAll(cb.getDoc1().getPaths());
    result.addAll(cb.getDoc2().getPaths());
    for(Member m:cb.getMs()){m.match(
      nc->collectPaths(result,nc),
      mi->collectPaths(result,mi),
      mt->collectPaths(result,mt)
      );}
    Set<Path> result2=Functions.remove1OuterAndPrimitives(result);
    return result2;
  }
  Void collectPaths(List<Path> ps,ExpCore e){
    class CollectReceiversPaths extends CloneVisitor{
      public ExpCore visit(ClassB s) {return s;}   
      public ExpCore visit(ExpCore.MCall s) {//otherwise, we could not use paths+ as Any in star classes.
        if(!(s.getReceiver() instanceof Path)){return super.visit(s);}
        Path p=(Path)s.getReceiver();
        if(!p.isPrimitive()){ps.add(p);}
        return super.visit(s);
      }    }
    e.accept(new CollectReceiversPaths());
    return null;
  }
  
  Void collectPaths(List<Path> ps,NestedClass nc){
    assert nc.getInner() instanceof ClassB;
    ClassB cb=(ClassB)nc.getInner();
    assert IsCompiled.of(cb);
    //assert Configuration.typeExtraction.isCt(cb);
    ps.addAll(nc.getDoc().getPaths());
    ps.addAll(of(cb));
    return null;
  }
  /*Void collectPaths(List<Path> ps,Ast.Type t){
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
    if(mwt.getInner().isPresent()){
      collectPaths(ps,mwt.getInner().get());
      ps.addAll(CollectPaths0.of(mwt.withInner(Optional.empty())));
    }
    else ps.addAll(CollectPaths0.of(mwt));
    return null;
  }
  /*Void collectPaths(List<Path> ps, MethodType mt) {
    collectPaths(ps,mt.getReturnType());
    for(Ast.Type t:mt.getTs()){
      collectPaths(ps,t);
      }
    ps.addAll(mt.getExceptions());
    return null;
  }*/
  Void collectPaths(List<Path> ps,MethodImplemented mi){
    throw new ast.InternalError.InterfaceNotFullyProcessed();
    //never invoked in base class? - invoked in extended version?
}
}
