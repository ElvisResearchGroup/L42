package auxiliaryGrammar;

import ast.ExpCore;
import ast.Redex;

public class Ctx<T> {
  public ExpCore ctx;
  public T hole;
  public Ctx(ExpCore ctx, T hole){
    this.ctx=ctx;this.hole=hole;}
}
