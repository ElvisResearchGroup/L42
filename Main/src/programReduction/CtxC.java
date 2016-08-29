package programReduction;

import java.util.List;

import ast.Ast.Path;
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
import coreVisitors.IsCompiled;
import tools.Assertions;

public interface CtxC {
  ExpCore fillHole(ExpCore hole);
  ExpCore originalHole();
  CtxC divide(ExpCore all);
  static  CtxC split (ExpCore e){return e.accept(new CtxSplitter());}
  }

abstract class CtxCAbsPos<T> implements CtxC{
  T origin;int pos;CtxC ctx;
  CtxCAbsPos(T origin,int pos, CtxC ctx){
    this.origin=origin;this.pos=pos;this.ctx=ctx;
    }
  public ExpCore originalHole() {return ctx.originalHole();}
  }

class CtxCInner<T extends ExpCore.WithInner<T> & ExpCore> implements CtxC{
  T origin;CtxC ctx;
  CtxCInner(T origin,CtxC ctx) {this.origin=origin;this.ctx=ctx;}
  public ExpCore originalHole() {return ctx.originalHole();}
  public T fillHole(ExpCore hole) {return origin.withInner(ctx.fillHole(hole));}
  public CtxC divide(ExpCore all) {
    assert origin.getClass().equals(all.getClass());
    @SuppressWarnings("unchecked") //safe here
    T _all=(T)all;//TODO:
    CtxC ctxInner=ctx.divide(_all.getInner());
    return new CtxCInner<T>(_all,ctxInner);
    }
  }

class CtxSplitter implements coreVisitors.Visitor<CtxC>{
  public CtxC visit(Path s) {throw Assertions.codeNotReachable();}
  public CtxC visit(X s) {throw Assertions.codeNotReachable();}
  public CtxC visit(_void s) {throw Assertions.codeNotReachable();}
  public CtxC visit(WalkBy s) {throw new AssertionError();}
  
  public CtxC visit(Signal s) {return new CtxCInner<Signal>(s, s.getInner().accept(this));}
  public CtxC visit(Loop s) { return new CtxCInner<Loop>(s, s.getInner().accept(this));}

  private static class CtxCMCallPos extends CtxCAbsPos<MCall>{
    CtxCMCallPos(MCall origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
    public ExpCore fillHole(ExpCore hole) {return origin.withEsi(pos,ctx.fillHole(hole));}
    public CtxC divide(ExpCore all) {
      MCall _all=(MCall)all;
      CtxC ctxInner=ctx.divide(_all.getEs().get(pos));
      return new CtxCMCallPos(_all,pos,ctxInner);
      }
    }
  public CtxC visit(MCall s) {
      ExpCore r=s.getInner();
      if (!IsCompiled.of(r)){return new CtxCInner<MCall>(s,r.accept(this));}
      int pos=firstNotCompiled(s.getEs());
      return new CtxCMCallPos(s,pos,s.getEs().get(pos).accept(this));
    }
  public CtxC visit(Using s) {
    return null;
    }
  public CtxC visit(Block s) {
    return null;//need care, may be not compiled if there is skeletal type?
    }
  private static class Hole implements CtxC{
    ExpCore original; Hole(ExpCore original){this.original=original;}
    public ExpCore fillHole(ExpCore hole) {return hole;}
    public ExpCore originalHole() {return original;}
    public CtxC divide(ExpCore all) {return new Hole(all);}
  }
  public CtxC visit(ClassB s) {
    assert !IsCompiled.of(s);
    return new Hole(s);
    }
  private int firstNotCompiled(List<ExpCore> es) {
    for(int i=0;i<es.size();i++){
      if(!IsCompiled.of(es.get(i))){return i;}
      }
    return es.size();
    }
  }