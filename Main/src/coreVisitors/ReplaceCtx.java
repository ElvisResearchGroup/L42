package coreVisitors;

import ast.ExpCore;
import ast.ExpCore.WalkBy;

public class ReplaceCtx extends CloneVisitor{
  ExpCore toReplace;
  ReplaceCtx(ExpCore toReplace){this.toReplace=toReplace;}
  
  public ExpCore visit(WalkBy s) {return toReplace;}
  
  public static ExpCore of(ExpCore ctx,ExpCore toReplace){
    return ctx.accept(new ReplaceCtx(toReplace));
  }
  
}
