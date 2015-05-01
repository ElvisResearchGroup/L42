package coreVisitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import ast.Ast.*;
import ast.ErrorMessage.*;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Loop;
import ast.Redex;
import ast.Redex.FUpdateExtended;
import auxiliaryGrammar.Ctx;
import auxiliaryGrammar.Program;

public class Move implements Visitor<Ctx<List<Block.Dec>>>{
  
  final String x;
  private Move(String x){this.x=x;}
  
  public Ctx<List<Block.Dec>> visit(Path s) {throw Assertions.codeNotReachable();}
  public Ctx<List<Block.Dec>> visit(X s) {throw Assertions.codeNotReachable();}
  public Ctx<List<Block.Dec>> visit(_void s) {throw Assertions.codeNotReachable();}
  public Ctx<List<Block.Dec>> visit(WalkBy s) {
    return new Ctx<List<Block.Dec>>(s, Collections.emptyList());
    }  
  public Ctx<List<Block.Dec>> visit(ClassB s) {throw Assertions.codeNotReachable();}

  public Ctx<List<Block.Dec>> visit(Signal s) {
    return lift(s.getInner().accept(this),
        ctx->s.withInner(ctx));
    }
  public Ctx<List<Block.Dec>> visit(Using s) {
    for(ExpCore ei:s.getEs()){
      if(!IsCtx.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    return null;
  }
  public Ctx<List<Block.Dec>> visit(MCall s) {
    if(IsCtx.of(s.getReceiver())){
      return lift(s.getReceiver().accept(this),
        ctx->s.withReceiver(ctx));}
    for(ExpCore ei:s.getEs()){
      if(!IsCtx.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    return null;  
  }
  private void split( List<Block.Dec> dvs,List<Block.Dec> dvs1,List<Block.Dec> dvs2,HashSet<String> xs){
    for(Block.Dec dec:dvs){
      if(xs.contains(dec.getX())){dvs1.add(dec);}
      else{dvs2.add(dec);}
    }
    iterate:while(true){
      for(Block.Dec decX:dvs2){
        for(Block.Dec decE:dvs1){
        Set<String> fv = FreeVariables.of(decE.getE());
        if(!fv.contains(decX.getX())){continue;}
        dvs1.add(decX);
        dvs2.remove(decX);
        continue iterate;
      }}break;};
    }
  public Ctx<List<Block.Dec>> visit(Block s) {
    List<Block.Dec> dvs=new ArrayList<Block.Dec>();
    List<Block.Dec> ds=new ArrayList<Block.Dec>();
    List<Block.Dec> dvs1=new ArrayList<Block.Dec>();
    List<Block.Dec> dvs2=new ArrayList<Block.Dec>();
    for(Block.Dec d:s.getDecs()){
      if(ds.isEmpty() && !IsCtx.of(d.getE())){
        dvs.add(d);
        }
      else{
        assert ds.isEmpty() || !IsCtx.of(d.getE());
        ds.add(d);
        }
     }
    Ctx<List<Block.Dec>> nestedCtx=null;
    if(ds.isEmpty()){
      nestedCtx= s.getInner().accept(this);
    }else{
      nestedCtx=ds.get(0).getE().accept(this);
      }
    HashSet<String> richXs=new HashSet<String>();
    richXs.add(x);
    for(Block.Dec dv:nestedCtx.hole){
      richXs.addAll(FreeVariables.of(dv.getE()));
    }
    split(dvs,dvs1,dvs2,richXs);
    Block s2=null;
    if(ds.isEmpty()){
      s2=s.withInner(nestedCtx.ctx);
    }else{
      ds.set(0,ds.get(0).withE(nestedCtx.ctx));
      dvs2.addAll(ds);
      s2=s;
      }
    s2=s2.withDecs(dvs2);
    dvs1.addAll(nestedCtx.hole);
    return new Ctx<List<Block.Dec>>(s2,dvs1);
    }
  
  private Ctx<List<Block.Dec>> lift(Ctx<List<Block.Dec>> res,Function<ExpCore,ExpCore> f){
    res.ctx=f.apply(res.ctx);
    return res;
    }
  //solved on call side if atom!=x
  public static Block of(Block ctx,String a){
    int ctxPosition=-1;
    //ExpCore ex=ctx.getDecs().get(ctx.domDecs().indexOf(x)).getE();
    List<Block.Dec> newDecs=new ArrayList<>();
    Ctx<List<Block.Dec>> res=null;
    int i=-1;
    for(Block.Dec dec:ctx.getDecs()){
      i+=1;
      if(!IsCtx.of(dec.getE())){
        newDecs.add(dec);
        continue;}
      ctxPosition=i;
      res=dec.getE().accept(new Move(a));
      newDecs.add(dec.withE(res.ctx));
    }
    if(res!=null){
      //ex fieldU1,
      newDecs.addAll(ctxPosition-1,res.hole);
      return ctx.withDecs(newDecs);
    }
    assert IsCtx.of(ctx.getInner());
    assert !ctx.get_catch().isPresent();
    res=ctx.getInner().accept(new Move(a));
    newDecs.addAll(res.hole);
    return ctx.withDecs(newDecs).withInner(res.ctx);
  }

  public Ctx<List<Dec>> visit(Loop s) {throw Assertions.codeNotReachable();}
}