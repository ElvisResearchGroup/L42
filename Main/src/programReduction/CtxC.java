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

public interface CtxC {
  ExpCore fillHole(ExpCore hole);
  ExpCore originalHole();
  CtxC divide(ExpCore all);
  static  CtxC split (ExpCore e){
    return null;
  }
}
abstract class CtxCAbs<T> implements CtxC{
  T origin;CtxC ctx;
  CtxCAbs(T origin, CtxC ctx){
    this.origin=origin;this.ctx=ctx;
    }
  public ExpCore originalHole() {return ctx.originalHole();}
  }
abstract class CtxCAbsPos<T> implements CtxC{
T origin;int pos;CtxC ctx;
CtxCAbsPos(T origin,int pos, CtxC ctx){
  this.origin=origin;this.pos=pos;this.ctx=ctx;
  }
public ExpCore originalHole() {return ctx.originalHole();}
}

class CtxSplitter implements coreVisitors.Visitor<CtxC>{
  public CtxC visit(Path s) {return null;}
  public CtxC visit(X s) {return null;}
  public CtxC visit(_void s) {return null;}
  public CtxC visit(WalkBy s) {throw new AssertionError();}
  @Override
  public CtxC visit(Using s) {
  // TODO Auto-generated method stub
  return null;
  }
  private static class CtxCSignal extends CtxCAbs<Signal>{
  CtxCSignal(Signal origin,CtxC ctx) {super(origin,ctx);}
  public ExpCore fillHole(ExpCore hole) {return origin.withInner(ctx.fillHole(hole));}
  public CtxC divide(ExpCore all) {
    Signal _all=(Signal)all;
    CtxC ctxInner=ctx.divide(_all.getInner());
    return new CtxCSignal(_all,ctxInner);
  }}
  public CtxC visit(Signal s) {
    return null;
    }
  private static class CtxCMCall extends CtxCAbs<MCall>{
    CtxCMCall(MCall origin,CtxC ctx) {super(origin,ctx);}
    public ExpCore fillHole(ExpCore hole) {return origin.withInner(ctx.fillHole(hole));}
    public CtxC divide(ExpCore all) {
      MCall _all=(MCall)all;
      CtxC ctxInner=ctx.divide(_all.getInner());
      return new CtxCMCall(_all,ctxInner);
    }}
  private static class CtxCMCallPos extends CtxCAbsPos<MCall>{
  CtxCMCallPos(MCall origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
  public ExpCore fillHole(ExpCore hole) {return origin.withEsi(pos,ctx.fillHole(hole));}
  public CtxC divide(ExpCore all) {
    MCall _all=(MCall)all;
    CtxC ctxInner=ctx.divide(_all.getEs().get(pos));
    return new CtxCMCall(_all,ctxInner);
  }}
    public CtxC visit(MCall s) {
      ExpCore r=s.getInner();
      if (!IsCompiled.of(r)){return new CtxCMCall(s,r.accept(this));}
      int pos=firstNotCompiled(s.getEs());
      return new CtxCMCallPos(s,pos,s.getEs().get(pos).accept(this));
    }

    @Override
    public CtxC visit(Block s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public CtxC visit(ClassB s) {
    // TODO Auto-generated method stub
    return null;
    }

    @Override
    public CtxC visit(Loop s) {
    // TODO Auto-generated method stub
    return null;
    }
    private int firstNotCompiled(List<ExpCore> es) {
    for(int i=0;i<es.size();i++){
      if(!IsCompiled.of(es.get(i))){return i;}
    }
    return es.size();
    }

}