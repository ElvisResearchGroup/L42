package typeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import ast.Ast;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.Ast.C;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import coreVisitors.PropagatorVisitor;
import coreVisitors.Visitor;
import programReduction.Program;
import tools.Assertions;

public class TypeSystem {
  HashMap<TIn,TOut>map=new HashMap<>();//memoized map
  class TIn{
    Phase phase;
    Program p;
    HashMap<String,NormType>g=new HashMap<>();//could be two arrays for efficiency
    ExpCore e;
    NormType expected;
    TIn(Phase phase,Program p,ExpCore e,NormType expected){
      this.phase=phase;this.p=p;
      this.e=e;this.expected=expected;
      }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + e.hashCode();
      result = prime * result + expected.hashCode();
      result = prime * result + g.hashCode();
      result = prime * result + p.hashCode();
      result = prime * result + phase.hashCode();
      return result;
      }
    @Override
    public boolean equals(Object obj) {
      if (this == obj){ return true;}
      assert obj!=null;
      assert getClass() == obj.getClass();
      TIn other = (TIn) obj;
      if (phase != other.phase){ return false;}
      if (!e.equals(other.e)){ return false; }
      if (!expected.equals(other.expected)){ return false;}
      if (!g.equals(other.g)){return false;}
      if (p != other.p){return false;}//simplifing comparing ps
      return true;
      }
    public TIn addG(String x, NormType t){
      TIn res=new TIn(phase,p,e,expected);
      res.g.putAll(this.g);
      res.g.put(x,t);
      return res;
      }
    public TIn withE(ExpCore newE,NormType newExpected){
      return new TIn(phase,p,newE,newExpected);
      }
    boolean isCoherent(){
      return true;
      }
    }
  class TOk implements TOut{
    public boolean isOk() { return true;}
    public TOk toOk() {return this;}
    TIn in;
    ExpCore annotated;
    NormType computed;
    List<NormType>returns=new ArrayList<>();
    List<Path>exceptions=new ArrayList<>();
    boolean isCoherent(){
      assert in!=null;
      assert annotated!=null;
      assert computed!=null;
      return true;
      }
    }
  class TErr implements TOut {
    public boolean isOk() { return false;}
    public TErr toError() {return this;}
    public TErr(TIn in, String msg, NormType _computed) {
      this.in = in; this.msg = msg; this._computed = _computed;
      }
    TIn in;
    String msg;
    NormType _computed;  
    }
  interface TOut extends OkOr<TOk,TErr>{}
  interface OkOr<T extends OkOr<T,E>, E extends OkOr<T,E> >{
    boolean isOk();
    default T toOk() {throw new java.lang.Error();}
    default E toError() {throw new java.lang.Error();}
    }
  public TOut type(TIn in){
    TOut res=_memoizedTSRes(in);
    if (res!=null){return res;}
    try{
      res=in.e.accept(new Visitor<TOut>(){
        public TOut visit(Path s) {return TsOperations.tsPath(in,s);}
        public TOut visit(X s) {return TsOperations.tsX(in,s);}
        public TOut visit(_void s) {return TsOperations.tsVoid(in,s);}
        public TOut visit(Using s) {return TsOperations.tsUsing(in,s);}
        public TOut visit(Signal s) {return TsOperations.tsSignal(in,s);}
        public TOut visit(MCall s) {return TsOperations.tsMCall(in,s);}
        public TOut visit(Block s) {return TsOperations.tsBlock(in,s);}
        public TOut visit(ClassB s) {return TsOperations.tsClassB(in,s);}
        public TOut visit(Loop s) {return TsOperations.tsLoop(in,s);}
        public TOut visit(WalkBy s) {throw Assertions.codeNotReachable();}
        });
      }
    finally{memoizeTSRes(in,res);}
    assert res!=null;
    return res;
    }
  private void memoizeTSRes(TIn in, TOut out) {
  //memoizeTSRes need to not memoize null res
    assert out!=null;
    assert !map.containsKey(in);
    map.put(in, out);
    }
  private TOut _memoizedTSRes(TIn in) {
    TOut out=map.get(in);
    return out;
  }
}
