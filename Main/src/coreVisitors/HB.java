package coreVisitors;

import java.util.HashSet;

import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;

public class HB implements Visitor<ExpCore> {
  HashSet<String> xs=new HashSet<String>();
  HashSet<String> xsRes=new HashSet<String>();
  boolean onlyAroundHole;
  HB(boolean onlyAroundHole){this.onlyAroundHole=onlyAroundHole;}
  public static HashSet<String> of(ExpCore ctxVal, boolean onlyAroundHole){
    HB hb=new HB(onlyAroundHole);
    ctxVal.accept(hb);
    return hb.xsRes;
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
    for(Block.Dec di:s.getDecs()){
      result=acc(result,di.getE().accept(this));
    }
    result=acc(result,s.getInner().accept(this));
    if(result==null &&onlyAroundHole){return null;}
    assert !onlyAroundHole || result instanceof WalkBy:onlyAroundHole+" "+result;
    for(Block.Dec dec:s.getDecs()){
      if(xs.contains(dec.getX())){throw new ErrorMessage.VariableDeclaredMultipleTimes(dec.getX(),s.getP());}
      this.xs.add(dec.getX());
      this.xsRes.add(dec.getX());
      }
    try{
      if(!onlyAroundHole && s.get_catch().isPresent()){
        Catch k=s.get_catch().get();          
        if(xs.contains(k.getX())){throw new ErrorMessage.VariableDeclaredMultipleTimes(k.getX(),s.getP());}
        xs.add(k.getX());
        xsRes.add(k.getX());
        for(On on:k.getOns()){
          on.getInner().accept(this);
          }
        }
      return result;
    }finally{
      for(Block.Dec dec:s.getDecs()){
        this.xs.remove(dec.getX());
        }
    }
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
