package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import tools.Assertions;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.ExpCore.Block.Dec;
import ast.Redex;
import auxiliaryGrammar.Ctx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class ExtractCtxVal implements Visitor<Ctx<Redex>>{
  Program p;
  private ExtractCtxVal(Program p){this.p=p;}
  
  public Ctx<Redex> visit(Path s) {return null;}
  public Ctx<Redex> visit(X s) {return null;}
  public Ctx<Redex> visit(_void s) { return null; }
  public Ctx<Redex> visit(WalkBy s) {throw Assertions.codeNotReachable();}
  
  public Ctx<Redex> visit(ClassB s) {
    return checkRedex(s);
    }

  public Ctx<Redex> visit(Signal s) {
    return lift(s.getInner().accept(this),
        ctx->s.withInner(ctx));
    }
  public Ctx<Redex> visit(Loop s) {
    return checkRedex(s);
    }
    
  public Ctx<Redex> visit(Using s) {
    Ctx<Redex> res=checkRedex(s);
    if(res!=null){return res;}
    for(ExpCore ei:s.getEs()){
      if(IsValue.of(p,ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    return lift(s.getInner().accept(this),
        ctx->s.withInner(ctx));
  }
  public Ctx<Redex> visit(MCall s) {
    Ctx<Redex> res=checkRedex(s);
    if(res!=null){return res;}
    
    if(!IsValue.of(p, s.getReceiver())){
      return lift(s.getReceiver().accept(this),
        ctx->s.withReceiver(ctx));}
    for(ExpCore ei:s.getEs()){
      if(IsValue.of(p,ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    return null;  
  }
  public Ctx<Redex> visit(Block s) {
    Ctx<Redex> res=checkRedex(s);
    if(res!=null){return res;}
    
    for(int i=0;i<s.getDecs().size();i++){
      int ii=i;//final restrictions
      Dec di=s.getDecs().get(i);
      boolean isDv=new IsValue(p).validDvs(di);
      Redex isGarbage=null;
      if(isDv && di.getE() instanceof Block){
        Block diB=(Block)di.getE();
        isGarbage=IsValue.nestedGarbage(diB);
        }
      if(isGarbage!=null){
        List<Block.Dec> ds=new ArrayList<Block.Dec>(s.getDecs());
        ds.set(ii,di.withE(new WalkBy()));
        return new Ctx<Redex>(s.withDecs(ds),isGarbage);
        }
      if(isDv){continue;}
      //otherwise, i is the first non dv
      return lift(di.getE().accept(this),ctx->{
        List<Block.Dec> es=new ArrayList<Block.Dec>(s.getDecs());
        es.set(ii,es.get(ii).withE(ctx));
        return s.withDecs(es);
        });
      }
    if(s.get_catch().isPresent()){return null;}
    return lift(s.getInner().accept(this),
        ctx->s.withInner(ctx));
    }
  
  /*public static Redex.Garbage nestedGarbage(Block b) {
    //b is a right value!
    Block b2=Functions.garbage(b,b.getDecs().size());
    if(!b2.equals(b)){return new Redex.Garbage(b,b2);}
    //else, try nested!
    {int i=-1;for(Dec di:b.getDecs()){i+=1;
      if (!(di.getE() instanceof Block)){continue;}
      Block bi=(Block) di.getE();
      Redex.Garbage ngi=nestedGarbage(bi);
      if(ngi==null){continue;}
      assert ngi.getThat().equals(bi);
      List<Block.Dec> ds=new ArrayList<Block.Dec>(b.getDecs());
      ds.set(i,di.withE(ngi.getThatLessGarbage()));
      return new Redex.Garbage(b,b.withDecs(ds));
    }}
    if(!(b.getInner() instanceof Block)){return null;}
    Block bIn=(Block)b.getInner();
    Redex.Garbage ngIn=nestedGarbage(bIn);
    if(ngIn==null){return null;}
    assert ngIn.getThat().equals(bIn);
    return new Redex.Garbage(b,b.withInner(ngIn.getThatLessGarbage()));    
    
  }*/

  private Ctx<Redex> lift(Ctx<Redex> res,Function<ExpCore,ExpCore> f){
    if(res!=null){res.ctx=f.apply(res.ctx);}
    return res;
  }
  private Ctx<Redex> checkRedex(ExpCore s) {
    Redex r=IsRedex.of(p, s);
    if(!(r instanceof Redex.NoRedex)){
      return new Ctx<Redex>(new WalkBy(),r);}
    return null;
  }
  public static Ctx<Redex> of(Program p,ExpCore e){
    //what if check garbage first?
    Ctx<Redex> result=e.accept(new ExtractCtxVal(p));
    if(result!=null){return result;}
    throw new ErrorMessage.CtxExtractImpossible(e,p.getInnerData());
  }
}