package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import tools.Assertions;
import ast.Ast.*;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.Redex;
import auxiliaryGrammar.Ctx;

public class ExtractCtxUpToX implements Visitor<Ctx<Block>>{
  String x;
  private ExtractCtxUpToX(String x){this.x=x;}
  
  public Ctx<Block> visit(ExpCore.EPath s) {throw Assertions.codeNotReachable();}
  public Ctx<Block> visit(X s) {throw Assertions.codeNotReachable();}
  public Ctx<Block> visit(_void s) {throw Assertions.codeNotReachable();}
  public Ctx<Block> visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Ctx<Block> visit(ClassB s) {throw Assertions.codeNotReachable();}

  public Ctx<Block> visit(Signal s) {
    return lift(s.getInner().accept(this),s::withInner);
    }
  public Ctx<Block> visit(Loop s) {
    return lift(s.getInner().accept(this),s::withInner);
    }
  public Ctx<Block> visit(UpdateVar s) {
    return lift(s.getInner().accept(this),s::withInner);
    }


  public Ctx<Block> visit(Using s) {
    for(ExpCore ei:s.getEs()){
      if(!IsCtx.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    throw Assertions.codeNotReachable();
  }
  public Ctx<Block> visit(MCall s) {
    if(IsCtx.of(s.getInner())){
      return lift(s.getInner().accept(this),
        ctx->s.withInner(ctx));}
    for(ExpCore ei:s.getEs()){
      if(!IsCtx.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    throw Assertions.codeNotReachable();
  }
  public Ctx<Block> visit(Block s) {
   int xPos=s.domDecs().indexOf(x);
   if(xPos!=-1 && IsCtx.of(s)){return new Ctx<Block>(new WalkBy(),s);}
   List<Block.Dec> newDecs=new ArrayList<Block.Dec>();
   Ctx<Block>res=null;
   for(Block.Dec dec:s.getDecs()){
     if(!IsCtx.of(dec.getInner())){
       newDecs.add(dec);
       continue;}
     res=dec.getInner().accept(this);
     newDecs.add(dec.withInner(res.ctx));
   }
   if(res!=null){
     res.ctx=s.withDecs(newDecs);
     return res;
   }
   assert IsCtx.of(s.getInner());
   assert s.getOns().isEmpty();
   res=s.getInner().accept(this);
   res.ctx=s.withInner(res.ctx);
   return res;
 }
  
  private Ctx<Block> lift(Ctx<Block> res,Function<ExpCore,ExpCore> f){
    assert res!=null;
    res.ctx=f.apply(res.ctx);
    return res;
  }
  public static Ctx<Block> of(String x,ExpCore e){
    Ctx<Block> result=e.accept(new ExtractCtxUpToX(x));
    if(result!=null){
      //NO assert result.ctx instanceof Block: result.ctx;
      return result;
      }
    throw Assertions.codeNotReachable();
  }

}