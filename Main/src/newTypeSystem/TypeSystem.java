package newTypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ast.Ast.Doc;
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
import programReduction.Program;
import tools.Assertions;

public interface TypeSystem{
  static TypeSystem instance(){return new Impl();}

  default ExpCore topTypeExpVoid(Program p,ExpCore e){
    TIn in=TIn.top(Phase.Norm,p, e);
    TOut out=type(in.withE(e,Path.Void().toImmNT()));
    if(out.isOk()){return out.toOk().annotated;}
    TErr err=out.toError();
    throw new FormattedError(err);
    }
  default ExpCore topTypeExp(Program p,ExpCore e){
    TIn in=TIn.top(Phase.Norm,p, e);
    TOut out=type(in);
    if(out.isOk()){return out.toOk().annotated;}
    TErr err=out.toError();
    throw new FormattedError(err);
    }
  default ClassB topTypeLib(Phase phase,Program p){
    TIn in=TIn.top(phase,p, p.top());
    TOut out=typeLib(in);//this already check if the phase is already good enough!
    if(out.isOk()){return (ClassB) out.toOk().annotated;}
    TErr err=out.toError();
    throw new FormattedError(err);
    //errors from norm still leak out, are they good looking?
  }
  TOut type(TIn in);
  TOut typeLib(TIn in);
  public static ErrorKind subtype(Program p,Path pSub,Path pSuper){
    if (!p.subtypeEq(pSub, pSuper)){return ErrorKind.NotSubtypeClass;}
    return null;
    }
  public static void checkExists(Program p, Path pi){
    if (pi.isCore()){p.extractClassB(pi);}
    }
  public static ErrorKind subtype(Program p, NormType tSub, NormType tSuper) {
    checkExists(p,tSub.getPath());
    checkExists(p,tSuper.getPath());
    if (!p.subtypeEq(tSub.getPath(),tSuper.getPath())){return ErrorKind.NotSubtypeClass;}
    if(!Functions.isSubtype(tSub.getMdf(),tSuper.getMdf())){return ErrorKind.NotSubtypeMdf;}
    return null;
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
    if (null!=subtype(p,mSub.getReturnType().getNT(),mSuper.getReturnType().getNT())){
      return false;
    }
    if(mSub.getTs().size()!=mSuper.getTs().size()){return false;}
    {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
      if (null!=subtype(p,tSuper.getNT(),tSub.getNT())){
        return false;
        }    
      }}
    for(Type ti:mSub.getExceptions()){
      if(!exceptionSubtype(p,ti.getNT(), mSuper)){return false;}
      }
    return true;
    }
  public static boolean exceptionSubtype(Program p,NormType ti, MethodType mSuper) {
    for(Type tj:mSuper.getExceptions()){
      if(p.subtypeEq(ti.getPath(),tj.getNT().getPath())){
        return true;
        }
      }
      return false;
    }
  }
class Impl implements TypeSystem,TsOperations,TsBlock,TsMCall,TsLibrary{
  HashMap<TIn,TOut>map=new HashMap<>();//memoized map
  @Override public TOut type(TIn in){
    TOut res=_memoizedTSRes(in);
    if (res!=null){return res;}
    try{
      res=in.e.accept(new Visitor<TOut>(){
        public TOut visit(ExpCore.EPath s) {return tsPath(in,s);}
        public TOut visit(X s) {return tsX(in,s);}
        public TOut visit(_void s) {return tsVoid(in,s);}
        public TOut visit(Using s) {return tsUsing(in,s);}
        public TOut visit(Signal s) {return tsSignal(in,s);}
        public TOut visit(MCall s) {return tsMCall(in,s);}
        public TOut visit(Block s) {return tsBlock(in,s);}
        public TOut visit(ClassB s) {return tsClassB(in,s);}
        public TOut visit(Loop s) {return tsLoop(in,s);}
        public TOut visit(ExpCore.UpdateVar s) {return tsUpdateVar(in,s);}
        public TOut visit(WalkBy s) {throw Assertions.codeNotReachable();}
        });
      }
    finally{if(res!=null){memoizeTSRes(in,res);}}
    assert res!=null:
      "";
    return res;
    }
  private void memoizeTSRes(TIn in, TOut out) {
  //memoizeTSRes need to not memoize null res
    assert out!=null:
      "";
    assert !map.containsKey(in);
    map.put(in, out);
    }
  private TOut _memoizedTSRes(TIn in) {
    TOut out=map.get(in);
    return out;
    }
  }