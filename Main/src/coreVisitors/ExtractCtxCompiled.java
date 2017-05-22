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
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.Redex;
import auxiliaryGrammar.Ctx;
public class ExtractCtxCompiled implements Visitor<Ctx<ClassB>>{
  //private ExtractCtxCompiled(Program p){this.p=p;}
  
  public Ctx<ClassB> visit(ExpCore.EPath s) {throw Assertions.codeNotReachable();}
  public Ctx<ClassB> visit(X s) {throw Assertions.codeNotReachable();}
  public Ctx<ClassB> visit(_void s) {throw Assertions.codeNotReachable();}
  public Ctx<ClassB> visit(WalkBy s) {throw Assertions.codeNotReachable();}
  
  public Ctx<ClassB> visit(ClassB s) {
    assert !IsCompiled.of(s);
    return new Ctx<ClassB>(new WalkBy(),s);
  }

  public Ctx<ClassB> visit(Signal s) {
    return lift(s.getInner().accept(this),s::withInner);
    }
  public Ctx<ClassB> visit(Loop s) {
    return lift(s.getInner().accept(this),s::withInner);
  }
  public Ctx<ClassB> visit(UpdateVar s) {
    return lift(s.getInner().accept(this),s::withInner);
  }


  public Ctx<ClassB> visit(Using s) {
    for(ExpCore ei:s.getEs()){
      if(IsCompiled.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    assert !IsCompiled.of(s.getInner());
    return lift(s.getInner().accept(this),s::withInner);
  }
  public Ctx<ClassB> visit(MCall s) {
    if(!IsCompiled.of(s.getInner())){
      return lift(s.getInner().accept(this),s::withInner);}
    for(ExpCore ei:s.getEs()){
      if(IsCompiled.of(ei)){continue;}
      return lift(ei.accept(this),ctx->{
        List<ExpCore> es=new ArrayList<ExpCore>(s.getEs());
        es.set(es.indexOf(ei), ctx);
        return s.withEs(es);
      });
    }
    throw Assertions.codeNotReachable();
  }
  public Ctx<ClassB> visit(Block s) {
    for(Block.Dec dec:s.getDecs()){
      if(IsCompiled.of(dec.getInner())){continue;}
      //otherwise, i is the first non dv
      return lift(dec.getInner().accept(this),ctx->{
        List<Block.Dec> es=new ArrayList<Block.Dec>(s.getDecs());
        int ii=es.indexOf(dec);
        es.set(ii,es.get(ii).withInner(ctx));
        return s.withDecs(es);
        });
      }
    return lift(s.getInner().accept(this),s::withInner);    }
  
  private Ctx<ClassB> lift(Ctx<ClassB> res,Function<ExpCore,ExpCore> f){
    if(res!=null){res.ctx=f.apply(res.ctx);}
    return res;
  }
  public static Ctx<ClassB> of(ExpCore e){
    Ctx<ClassB> result=e.accept(new ExtractCtxCompiled());
    assert result!=null;
    return result;
  }
}