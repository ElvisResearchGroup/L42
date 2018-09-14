package newTypeSystem;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.Type;
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
import facade.L42;
import programReduction.Program;
import tools.Assertions;

public interface TypeSystem {
  public static TOut typeCheck(ExpCore.ClassB lib, Phase phase) {
    return TypeSystem.instance().type(TIn.top(phase, Program.emptyLibraryProgram(), lib, true, Ast.Type.immLibrary));
  }

  static TypeSystem instance(){return new Impl();}


  default ExpCore _topTypeExp(Program p,ExpCore e,Type t,boolean formatError){
    TIn in=TIn.top(Phase.Norm,p, e,true,t);
    TOut out=type(in);
    if(!out.isOk()){
      if(!formatError){return null;}
      throw new FormattedError(out.toError());
      }
    TOk res=out.toOk();
    if(!res.returns.isEmpty()){
      if(!formatError){return null;}
      throw new FormattedError(new TErr(in,"",
        res.returns.get(0),
        ErrorKind.MethodLeaksReturns));
      }
    return res.annotated;
  }
  default ClassB topTypeLib(Phase phase,Program p){
    TIn in=TIn.top(phase,p, p.top(),true,Type.immLibrary);
    TOut out=typeLib(in);//this already check if the phase is already good enough!
    if(out.isOk()){return (ClassB) out.toOk().annotated;}
    TErr err=out.toError();
    throw new FormattedError(err);
    //errors from norm still leak out, are they good looking?
  }
  TOut type(TIn in);
  TOut typeLib(TIn in);
  public static ErrorKind subtype(Program p,Path pSub,Path pSuper){
    if (!p.subtypeEq(pSub, pSuper)){
      return ErrorKind.NotSubtypeClass;
      }
    return null;
    }
  public static void checkExists(Program p, Path pi){
    if (pi.isCore()){p.extractClassB(pi);}
    }
  public static ErrorKind _subtype(Program p, Type tSub, Type tSuper) {
    checkExists(p,tSub.getPath());
    checkExists(p,tSuper.getPath());
    if (!p.subtypeEq(tSub.getPath(),tSuper.getPath())){
      return ErrorKind.NotSubtypeClass;
      }
    if(!Functions.isSubtype(tSub.getMdf(),tSuper.getMdf())){
      return ErrorKind.NotSubtypeMdf;
      }
    return null;
    }
  //only check mdf subtyping
  public static boolean _methMdfTSubtype(MethodType mSub,MethodType mSuper){
    if (!Functions.isSubtype(mSuper.getMdf(),mSub.getMdf())){return false;}
    if (!Functions.isSubtype(mSub.getReturnType().getMdf(),mSuper.getReturnType().getMdf())){return false;}
    {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
      if (!Functions.isSubtype(tSuper.getMdf(),tSub.getMdf())){
        return false;
        }
      }}
    return true;
    }
  public static boolean methTSubtype(Program p,MethodType mSub,MethodType mSuper){
    if (!Functions.isSubtype(mSuper.getMdf(),mSub.getMdf())){return false;}
    if (null!=_subtype(p,mSub.getReturnType(),mSuper.getReturnType())){
      return false;
    }
    if(mSub.getTs().size()!=mSuper.getTs().size()){return false;}
    {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
      if (null!=_subtype(p,tSuper,tSub)){
        return false;
        }
      }}
    for(Type ti:mSub.getExceptions()){
      if(!exceptionSubtype(p,ti, mSuper)){return false;}
      }
    return true;
    }
  public static boolean exceptionSubtype(Program p,Type ti, MethodType mSuper) {
    for(Type tj:mSuper.getExceptions()){
      if(p.subtypeEq(ti.getPath(),tj.getPath())){
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
        public TOut visit(ExpCore.OperationDispatch s) {return StaticDispatch.of(in.p,in,s,true).accept(this);}
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
    if(L42.memoizeExpressionTyping) {map.put(in, out);}
    }
  private TOut _memoizedTSRes(TIn in) {
    TOut out=map.get(in);
    return out;
    }
  }
