package is.L42.connected.withSafeOperators;
import ast.ExpCore.*;
import auxiliaryGrammar.Locator;
import auxiliaryGrammar.Program;
import ast.ExpCore;
import coreVisitors.CloneWithPath;
import facade.Configuration;
public class IsValidCompleteCode {
  boolean isComplete(ClassB cb){
    boolean[]found={false};
    cb.accept(new CloneWithPath(){
      public ExpCore visit(ast.Ast.Path p){
        Locator l=this.getLocator().copy();
        if(p.outerNumber()>l.size()){found[0]=true;}
        return super.visit(p);
      }
    });
    return found[0];
  }
  void ensureWellTyped(ClassB cb){//In case of error, should be false or error?
    Program p=Program.empty();
    Configuration.typeSystem.computeStage(p, cb);
    Configuration.typeSystem.checkCt(p, cb);
  }
}
