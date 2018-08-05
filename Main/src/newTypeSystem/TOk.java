package newTypeSystem;

import ast.ExpCore;
import ast.Ast.Type;

class TOk extends ATr<TOk> implements TOut{
  @Override TOk trClean() {return new TOk(in,annotated,computed);}
  @Override TOk self() {return this;}
  @Override public boolean isOk() { return true;}
  @Override public TOk toOk() {return this;}
  public TIn in;
  public ExpCore annotated;
  public Type computed;
  public TOk(TIn in, ExpCore annotated, Type computed){
    this.in=in;this.annotated=annotated;this.computed=computed;
    }
  public TOk withAC(ExpCore annotated,Type computed){
    TOk res=new TOk(this.in,annotated,computed);
    res.returns=this.returns;
    res.exceptions=this.exceptions;
    return res;
    }

  boolean isCoherent(){
    assert in!=null;
    assert annotated!=null;
    assert computed!=null;
    return true;
    }
  }