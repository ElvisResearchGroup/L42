package coreVisitors;

import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Loop;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;

public class Dec implements Visitor<ExpCore> {
  String x;
  boolean ensureImmutable;
  private Dec(String x,boolean ensureImmutable){
    this.x=x;this.ensureImmutable=ensureImmutable;
  }
  public static ExpCore of(ExpCore ctxVal,String x,boolean ensureImmutable){
    Dec dec=new Dec(x,ensureImmutable);
    return ctxVal.accept(dec);
  }
  
  //return null if not walkBy,
  //return walkby on walkby but not dec for x,
  //return dec for x otherwise
  
  public ExpCore visit(Signal s) {
    return s.getInner().accept(this);
  }
  public ExpCore visit(Loop s) {
    return s.getInner().accept(this);
  }
  @Override
  public ExpCore visit(Using s) {
    ExpCore result=null;
    for(ExpCore ei:s.getEs()){
      result=acc(result,ei.accept(this));
    }
    return acc(result,s.getInner().accept(this));
  }

  @Override
  public ExpCore visit(MCall s) {
    ExpCore result=s.getReceiver().accept(this);
    for(ExpCore ei:s.getEs()){
      result=acc(result,ei.accept(this));
    }
    return result;
  }

  @Override
  public ExpCore visit(Block s) {
    ExpCore result=null;
    for(Block.Dec ei:s.getDecs()){
      result=acc(result,ei.getE().accept(this));
    }
    result=acc(result,s.getInner().accept(this));
    if(result==null){return null;}
    if(!(result instanceof WalkBy)){return result;}
    int pos=-1;
    for(int i=0;i<s.getDecs().size();i++){
      if(!s.getDecs().get(i).getX().equals(this.x)){continue;}
      pos=i;break;
    }
    if(pos==-1){return result;}
    if(ensureImmutable){
      ast.Ast.NormType ti=s.getDecs().get(pos).getNT();
      if(ti.getMdf()!=Mdf.Immutable){throw new ErrorMessage.ExpectedImmutableVar(s,x);}
    }
    return s.getDecs().get(pos).getE();
  }

  public ExpCore visit(Path s) {return null;}
  public ExpCore visit(X s) {return null;}
  public ExpCore visit(_void s) {return null;}
  public ExpCore visit(WalkBy s) {return s;}
  public ExpCore visit(ClassB s) {return null;}  
  
  private ExpCore acc(ExpCore c1,ExpCore c2){
    if(c2==null){return c1;}
    return c2;
    }
}
