package coreVisitors;

import tools.Assertions;
import ast.ExpCore;
import ast.ExpCore.*;

public class ReplaceX extends CloneVisitor{
  ExpCore toReplace;
  String toBeReplaced;
  //boolean done=false;
  ReplaceX(ExpCore toReplace,String toBeReplaced){this.toReplace=toReplace;this.toBeReplaced=toBeReplaced;}
  
  public ExpCore visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public ExpCore visit(ClassB s) {return s;}
  public ExpCore visit(X s) {
    if(s.getInner().equals(toBeReplaced)){
  //    if(done){throw Assertions.codeNotReachable();}
  //    this.done=true;
      return toReplace;
      }
    return super.visit(s);
    }
  
  public static ExpCore of(ExpCore ctx,ExpCore toReplace,String toBeReplaced){
    ReplaceX v=new ReplaceX(toReplace,toBeReplaced);
//    if(!v.done){throw Assertions.codeNotReachable();}
    return ctx.accept(v);
  }
  
}
