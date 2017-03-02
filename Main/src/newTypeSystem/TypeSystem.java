package newTypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.ExpCore;
import ast.Ast.C;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.ExpCore.Block;
import ast.ExpCore.Block.On;
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
import auxiliaryGrammar.Functions;
import coreVisitors.PropagatorVisitor;
import coreVisitors.Visitor;
import newTypeSystem.TypeSystem.TIn;
import newTypeSystem.TypeSystem.TOut;
import programReduction.Program;
import tools.Assertions;

public class TypeSystem {
  HashMap<TIn,TOut>map=new HashMap<>();//memoized map
  public static boolean subtype(Program p,Path pSub,Path pSuper){
    return p.subtypeEq(pSub, pSuper);
    }
  public static boolean subtype(Program p, NormType tSub, NormType tSuper) {
    if(!Functions.isSubtype(tSub.getMdf(),tSuper.getMdf())){return false;}
    return subtype(p,tSub.getPath(),tSuper.getPath());
    }
//only check mdf subtyping
public static boolean _methMdfTSubtype(MethodType mSub,MethodType mSuper){
  if (!Functions.isSubtype(mSuper.getMdf(),mSub.getMdf())){return false;}
  if (!Functions.isSubtype(mSub.getReturnType().getNT().getMdf(),mSuper.getReturnType().getNT().getMdf())){return false;}
  {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
  if (!Functions.isSubtype(tSuper.getNT().getMdf(),tSub.getNT().getMdf())){
    return false;
    }    
  }}
  return true;
}
public static boolean methTSubtype(Program p,MethodType mSub,MethodType mSuper){
  if (!Functions.isSubtype(mSuper.getMdf(),mSub.getMdf())){return false;}
  if (!newTypeSystem.TypeSystem.subtype(p,mSub.getReturnType().getNT(),mSuper.getReturnType().getNT())){
    return false;
  }
  if(mSub.getTs().size()!=mSuper.getTs().size()){return false;}
  {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
    if (!newTypeSystem.TypeSystem.subtype(p,tSuper.getNT(),tSub.getNT())){
      return false;
      }    
  }}
  for(Type ti:mSub.getExceptions()){
    if(!exceptionSubtype(p,ti.getNT(), mSuper)){return false;}
    }
  return true;
  }
 private static boolean exceptionSubtype(Program p,NormType ti, MethodType mSuper) {
    for(Type tj:mSuper.getExceptions()){
    if(p.subtypeEq(ti.getPath(),tj.getNT().getPath())){
      return true;
      }
    }
    return false;
  }


  static class TIn{
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
      TIn res=gClean();
      res.g.putAll(this.g);
      assert !res.g.containsKey(x);
      res.g.put(x,t);
      return res;
      }
    public TIn addGds(List<ExpCore.Block.Dec> ds){
      TIn res=gClean();
      res.g.putAll(this.g);
      for(ExpCore.Block.Dec d : ds){
        assert !res.g.containsKey(d.getX());
        res.g.put(d.getX(),programReduction.Norm.resolve(this.p,d.getT()));
        }
      return res;
      }
    public NormType g(String x){
      NormType res=this.g.get(x);
      assert res!=null;
      return res;
      }
    public Set<String> gDom(){return g.keySet();}
    public TIn gClean(){return new TIn(phase,p,e,expected);}
    //onlyMutOrImm(G)={x:G(x) | G(x) only mut or imm}
    public TIn onlyMutOrImm(){
      TIn res=gClean();
      for(String xi:gDom()){
        NormType ti=g(xi);
        assert ti!=null;
        if (ti.getMdf()==Mdf.Mutable || ti.getMdf()==Mdf.Immutable){
          res.g.put(xi,ti);
          }
        }
      return res;
      }
    public TIn toRead(){//toRead(G)(x)=toRead(G(x)) //thus undefined where toRead undefined
      TIn res=gClean();
      for(String xi:gDom()){
        NormType ti=g(xi);
        assert ti!=null;
        ti=TypeManipulation._toRead(ti);
        if(ti==null){continue;}
        res.g.put(xi,ti);
        }
      return res;
      } 
    public TIn toLent(){//toLent(G)(x)=toLent(G(x)) //thus undefined where toLent undefined
      TIn res=gClean();
      for(String xi:gDom()){
        NormType ti=g(xi);
        assert ti!=null;
        ti=TypeManipulation._toRead(ti);
        if(ti==null){continue;}
        res.g.put(xi,ti);
        }
      return res;
      }
    public TIn gKs(List<ExpCore.Block.On>ks){     
    //G[ks]
    //  G[]=G
    //  G[k ks]=toRead(G) with k.throw=error and not catchRethrow(k)
    //  otherwise G[k ks] = G[ks]
    for( On k:ks){
      if(k.getKind()!=SignalKind.Error){continue;}
      if(TypeManipulation.catchRethrow(k)){continue;}
      return this.toRead();
      }
    return this;
    }
    public TIn withE(ExpCore newE,NormType newExpected){
      return new TIn(phase,p,newE,newExpected);
      }
    public TIn withP(Program newP){
      return new TIn(phase,newP,e,Path.Library().toImmNT());
      }

    boolean isCoherent(){
      return true;
      }
    }
  static class TOk implements TOut{
    public boolean isOk() { return true;}
    public TOk toOk() {return this;}
    TIn in;
    ExpCore annotated;
    NormType computed;
    List<NormType>returns=new ArrayList<>();
    List<Path>exceptions=new ArrayList<>();
    public TOk(TIn in, ExpCore annotated, NormType computed){
      this.in=in;this.annotated=annotated;this.computed=computed;
      }
    public TOk tsUnion(TOk that){
      //Tr1 U Tr2
      //  Ts1;Ps1 U Ts2;Ps2 =  Ts1,Ts2; Ps1,Ps2  
      TOk res=new TOk(this.in,this.annotated,this.computed);
      res.returns.addAll(this.returns);
      res.returns.addAll(that.returns);
      res.exceptions.addAll(this.exceptions);
      res.exceptions.addAll(that.exceptions);
      return res;
      }
    public TOk tsCapture(List<ExpCore.Block.On> ks){
      //Tr.capture(p,k1..kn)= Tr.capture(p,k1)...capture(p,kn)
      //Tr.capture(p,catch error P x e)=Tr
      //(Ts;Ps).capture(p,catch exception P x e)=Ts;{P'| P' in Ps, not p|-P'<=P}
      //(Ts;Ps).capture(p,catch return P x e)={T| T in Ts, not p|-T.P<=P};Ps
      Stream<NormType> ret = this.returns.stream();
      Stream<Path> exc = this.exceptions.stream();
      for(On k:ks){
        if(k.getKind()==SignalKind.Error){continue;}
        if(k.getKind()==SignalKind.Exception){
          exc=exc.filter(pi->!subtype(this.in.p,pi,k.getT().getNT().getPath()));
          }
        //otherwise, is return
        ret=ret.filter(ti->!subtype(this.in.p,ti.getPath(),k.getT().getNT().getPath()));
        }
      TOk result=new TOk(in,annotated,computed);
      exc.forEach(result.exceptions::add);
      ret.forEach(result.returns::add);
      return result;
      }
    boolean isCoherent(){
      assert in!=null;
      assert annotated!=null;
      assert computed!=null;
      return true;
      }
    }
  static class TErr implements TOut {
    public boolean isOk() { return false;}
    public TErr toError() {return this;}
    public TErr(TIn in, String msg, NormType _computed) {
      this.in = in; this.msg = msg; this._computed = _computed;
      }
    TIn in;
    String msg;
    NormType _computed;
    public TOut enrich(TIn in2) {
      return this;//TODO: design some general error context enreaching
      }  
    }
  static interface TOut extends OkOr<TOk,TErr>{}
  static interface OkOr<T extends OkOr<T,E>, E extends OkOr<T,E> >{
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
