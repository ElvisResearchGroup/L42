package typeSystem;

import java.util.HashMap;

import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

public class Facade implements facade.TypeSystem{

  @Override
  public void checkCt(Program p, ClassB ct) {
    TypeSystemOK.checkCt(p, ct);    
  }

  @Override
  public void checkAll(Program p) {
    TypeSystemOK.checkAll(p);    
    }

  @Override
  public void checkMetaExpr(Program p,ExpCore e) {
    TypeSystem.typecheckSure(false,p,new HashMap<>(),SealEnv.empty(),new ThrowEnv(),new Ast.NormType(Ast.Mdf.Immutable,Ast.Path.Library(),Ast.Ph.None),e);
  }

  @Override public ClassB typeExtraction(Program p, ClassB cb) {
    return TypeExtraction.etFull(p, cb);
  }
  
}
