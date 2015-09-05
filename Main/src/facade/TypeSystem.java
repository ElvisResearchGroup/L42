package facade;



import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

public interface TypeSystem {
  
  //ClassB typeExtraction(Program p, ClassB cb);
  
  void checkCt(Program p, ClassB ct);

  void checkAll(Program p);//TODO: check who use it and why

  void checkMetaExpr(Program p, ExpCore e);

  void computeInherited(Program p, ClassB cb);

  void computeStage(Program p, ClassB cb);
  
}
