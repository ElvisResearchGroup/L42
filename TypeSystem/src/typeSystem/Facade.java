package typeSystem;

import java.util.HashMap;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.L42.ExecutionStage;
import profiling.Timer;

public class Facade implements facade.TypeSystem{
  public @Override void computeInherited(Program p,ClassB cb){
    Timer.record("TypeSystem.computeInherited",()->{
    FillCache.computeInherited(p, cb);
    return null;});
  }
  public @Override void computeStage(Program p,ClassB cb) {//requires inherited
    Timer.record("TypeSystem.computeStage",()->{
    FillCache.computeStage(p, cb);
    return null;});
  }
  @Override
  public void checkCt(Program p, ClassB ct) {//is this ever called?
    Timer.record("TypeSystem.checkCt",()->{
    TypeSystemOK.checkCt(p, ct);
    return null;});
    }

  @Override
  public void checkAll(Program p) {
    String label="TypeSystem.checkAll in ";
    label+=L42.getStage();
    Timer.record(label,()->{
    TypeSystemOK.checkAll(p);   
    return null;});
    }

  @Override
  public void checkMetaExpr(Program p,ExpCore e) {
    Timer.record("TypeSystem.checkMetaExpr",()->{
    ThrowEnv env=new ThrowEnv();
    env.exceptions.add(Path.Any());
    TypeSystem.typecheckSure(false,p,new HashMap<>(),SealEnv.empty(),env,new Ast.NormType(Ast.Mdf.Immutable,Ast.Path.Library(),Ast.Ph.None),e);
    return null;});
  }
/*
  private static HashMap<Par,ClassB>cache=new HashMap<>();
  private static class Par{@Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((cb == null) ? 0 : System.identityHashCode(cb));
      result = prime * result + ((p == null) ? 0 : System.identityHashCode(p));
      return result;
    }
    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Par other = (Par) obj;
      return other.p==this.p && other.cb == this.cb;
    }
  public Par(Program p, ClassB cb) {this.p = p;this.cb = cb;    }
    Program p; ClassB cb;
    }
  */
 
  /*
  @Override public ClassB typeExtraction(Program p, ClassB cb) {
    Par par=new Par(p,cb);
    ClassB old=cache.get(par);
    if(old!=null){
      //System.out.println("cached result returned");
      return old;
      }
    old=TypeExtraction.etFull(p, cb);
    cache.put(par, old);
    return old;
  }*/
  
}
