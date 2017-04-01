package is.L42.connected.withSafeOperators;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Locator;
import programReduction.Program;
import ast.ExpCore;
import coreVisitors.CloneWithPath;
import facade.Configuration;
public class IsValidCompleteCode {
  boolean isComplete(ClassB cb){
    boolean[]found={false};
    cb.accept(new CloneWithPath(){
      public ExpCore visit(ExpCore.EPath p){
        Locator l=this.getLocator().copy();
        if(p.getInner().outerNumber()>l.size()){found[0]=true;}
        return super.visit(p);
      }
    });
    return found[0];
  }
  void ensureWellTyped(ClassB cb){//In case of error, should be false or error?
    newTypeSystem.TypeSystem.instance().topTypeLib(Phase.Typed, Program.emptyLibraryProgram().updateTop(cb));
  }
}
