package coreVisitors;

import tools.Map;
import ast.ExpCore;
import ast.Ast.HistoricType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class NormE extends CloneVisitor{
  private Program p;
  private NormE(Program p){this.p=p;}
  public static ExpCore of(Program p,ExpCore e){
    return e.accept(new NormE(p));
  }  
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    return Norm.of(p,nc);
  }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    return Norm.of(p, mi);
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    return Norm.of(p, mt,false);
  }
  public ExpCore visit(Path s) {return Norm.of(p,s);}
  
  protected Type liftT(Type t){ return Norm.of(p,t); }
  
  public ExpCore visit(ClassB s) {
    return s;
    //TODO: suspicios, why methods visitNestedClass and so on are implemented?
    /*ClassB cb=null;
    p=p.addAtTop(cb);
    ExpCore result=super.visit(s);
    p=p.pop();
    return result;*/
  }
  
}
