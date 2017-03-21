package programReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
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
import tools.Map;

public interface CtxC {
  ExpCore fillHole(ExpCore hole);
  ExpCore originalHole();
  CtxC divide(ExpCore all);
  static CtxC split (ExpCore e){return e.accept(new CtxSplitter());}
  static CtxC hole(ExpCore original){return new CtxSplitter.Hole(original);} 
  default String _toString() {return "CtxC["+sugarVisitors.ToFormattedText.of(this.fillHole(new ExpCore.X(Position.noInfo,"_HOLE_")))+",originalHole:"+sugarVisitors.ToFormattedText.of(this.originalHole())+"]";}
  default int _hashCode() {return this.fillHole(new ExpCore.WalkBy()).hashCode();}
  default boolean _equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CtxC other = (CtxC) obj;
    ExpCore oE=other.fillHole(new ExpCore.WalkBy());
    ExpCore thisE=this.fillHole(new ExpCore.WalkBy());
    return thisE.equals(oE);
    }
  }

//utility class to create CtxCs that point somewhere in a list
abstract class CtxCAbsPos<T> implements CtxC{
  @Override public String toString() {return _toString();}//limitation of Java8 requires methods of object
  @Override public int hashCode() {return this._hashCode();} //to be reimplemented :(
  @Override public boolean equals(Object obj) {return this._equals(obj);}
  T origin;int pos;CtxC ctx;
  CtxCAbsPos(T origin,int pos, CtxC ctx){
    this.origin=origin;this.pos=pos;this.ctx=ctx;
    }
  public ExpCore originalHole() {return ctx.originalHole();}
  }

//generic class allowing to contexts in a subexpression "main expression" (like the method receiver)
class CtxCInner<T extends ExpCore.WithInner<T> & ExpCore> implements CtxC{
  @Override public String toString() {return _toString();}//limitation of Java8 requires methods of object
  @Override public int hashCode() {return this._hashCode();} //to be reimplemented :(
  @Override public boolean equals(Object obj) {return this._equals(obj);}
  T origin;CtxC ctx;
  CtxCInner(T origin,CtxC ctx) {this.origin=origin;this.ctx=ctx;}
  public ExpCore originalHole() {return ctx.originalHole();}
  public T fillHole(ExpCore hole) {return origin.withInner(ctx.fillHole(hole));}
  public CtxC divide(ExpCore all) {
    assert origin.getClass().equals(all.getClass());
    @SuppressWarnings("unchecked") //safe here
    T _all=(T)all;
    CtxC ctxInner=ctx.divide(_all.getInner());
    return new CtxCInner<T>(_all,ctxInner);
    }
  }


class CtxSplitter implements coreVisitors.Visitor<CtxC>{
  //those are never reached by the visit
  public CtxC visit(Path s) {throw Assertions.codeNotReachable();}
  public CtxC visit(X s) {throw Assertions.codeNotReachable();}
  public CtxC visit(_void s) {throw Assertions.codeNotReachable();}
  //this case should eventually disappear
  public CtxC visit(WalkBy s) {throw new AssertionError();}
  
  //expressions that just wrap another
  public CtxC visit(Signal s) {return new CtxCInner<Signal>(s, s.getInner().accept(this));}
  public CtxC visit(Loop s) { return new CtxCInner<Loop>(s, s.getInner().accept(this));}

  //method call: define class for pointing in parameters
  private static class CtxCMCallPos extends CtxCAbsPos<MCall>{
    CtxCMCallPos(MCall origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
    public ExpCore fillHole(ExpCore hole) {return origin.withEsi(pos,ctx.fillHole(hole));}
    public CtxC divide(ExpCore all) {
      MCall _all=(MCall)all;
      CtxC ctxInner=ctx.divide(_all.getEs().get(pos));
      return new CtxCMCallPos(_all,pos,ctxInner);
      }
    }
  //method call: if receiver, otherwise a point in parameters
  public CtxC visit(MCall s) {
      ExpCore r=s.getInner();
      if (!IsCompiled.of(r)){return new CtxCInner<MCall>(s,r.accept(this));}
      int pos=firstNotCompiled(s.getEs());
      return new CtxCMCallPos(s,pos,s.getEs().get(pos).accept(this));
    }
  //from now on we just repeat this pattern
  //using: define class for pointing in parameters 
  private static class CtxCUsingPos extends CtxCAbsPos<Using>{
    CtxCUsingPos(Using origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
    public ExpCore fillHole(ExpCore hole) {return origin.withEsi(pos,ctx.fillHole(hole));}
    public CtxC divide(ExpCore all) {
      Using _all=(Using)all;
      CtxC ctxInner=ctx.divide(_all.getEs().get(pos));
      return new CtxCUsingPos(_all,pos,ctxInner);
      }
    }
  //using: if parameters, otherwise the main expression
  public CtxC visit(Using s) {
    int pos=firstNotCompiled(s.getEs());
    if (pos<s.getEs().size()){
      return new CtxCUsingPos(s,pos,s.getEs().get(pos).accept(this));
    }
    assert !IsCompiled.of(s.getInner());
    return new CtxCInner<Using>(s,s.getInner().accept(this));
    }
  
  //block is more complex since it has two sets of es:
  //one nested in the decs, and one nested in the ons; so we define two classes
  private static class CtxCBlock1Pos extends CtxCAbsPos<Block>{
    CtxCBlock1Pos(Block origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
    public ExpCore fillHole(ExpCore hole) {
      List<Dec> ds=new ArrayList<>(origin.getDecs());
      ds.set(pos, ds.get(pos).withInner(ctx.fillHole(hole)));
      return origin.withDecs(ds);
      }
    public CtxC divide(ExpCore all) {
      Block _all=(Block)all;
      CtxC ctxInner=ctx.divide(_all.getDecs().get(pos).getInner());
      return new CtxCBlock1Pos(_all,pos,ctxInner);
      }
    }
  private static class CtxCBlock2Pos extends CtxCAbsPos<Block>{
    CtxCBlock2Pos(Block origin,int pos,CtxC ctx) {super(origin,pos,ctx);}
    public ExpCore fillHole(ExpCore hole) {
      List<On> os=new ArrayList<>(origin.getOns());
      os.set(pos, os.get(pos).withInner(ctx.fillHole(hole)));
      return origin.withOns(os);
      }
    public CtxC divide(ExpCore all) {
      Block _all=(Block)all;
      CtxC ctxInner=ctx.divide(_all.getDecs().get(pos).getInner());
      return new CtxCBlock1Pos(_all,pos,ctxInner);
      }
    }
  
  //block: first in the decs, then in the ons, than in the main
  public CtxC visit(Block s) {{//to scope variables
    List<ExpCore> es=Map.of(d->d.getInner(),s.getDecs());
    int pos=firstNotCompiled(es);
    if (pos<es.size()){
      return new CtxCBlock1Pos(s,pos,es.get(pos).accept(this));
      }
    }{
    List<ExpCore> es=Map.of(o->o.getInner(),s.getOns());
    int pos=firstNotCompiled(es);
    if (pos<es.size()){
      return new CtxCBlock2Pos(s,pos,es.get(pos).accept(this));
      }
    }{
    return new CtxCInner<Block>(s,s.getInner().accept(this));
    }}
  
  //finally: case for L, where the hole will be
  static class Hole implements CtxC{
    @Override public String toString() {return _toString();}
    @Override public int hashCode() {return 0;}
    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      return true;//all holes are equals, and ignore the original content
      }
    ExpCore original; Hole(ExpCore original){this.original=original;}
    public ExpCore fillHole(ExpCore hole) {return hole;}
    public ExpCore originalHole() {return original;}
    public CtxC divide(ExpCore all) {return new Hole(all);}
  }
  public CtxC visit(ClassB s) {
    assert !IsCompiled.of(s);
    return new Hole(s);
    }
  
  //utility method
  static int firstNotCompiled(List<ExpCore> es) {
    for(int i=0;i<es.size();i++){
      if(!IsCompiled.of(es.get(i))){return i;}
      }
    return es.size();
    }
  }