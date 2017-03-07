package newTypeSystem;

import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.X;
import ast.ExpCore._void;
import tools.Assertions;

public interface TsOperations extends TypeSystem{
    
    default TOut tsPath(TIn in, Path s) {
    //D |- P~>P:class P <= T | emptyTr
    //D.p|-class P <= T
    NormType t=new NormType(Mdf.Class,s,Doc.empty());
    assert in.p.extractClassB(s)!=null;
    if(TypeSystem.subtype(in.p, t, in.expected)){
      return new TOk(in,s,t);
      }
    TErr out=new TErr(in,"----",t,ErrorKind.NotSybtype);
    return out;
    }

    default TOut tsX(TIn in, X s) {
    //D |-x ~> x :D.G(x) <= T | emptyTr
    //  D.p|- D.G(x) <= T
    NormType nt=in.g(s.getInner());
    if(TypeSystem.subtype(in.p,nt,in.expected)){
      return new TOk(in,s,nt);
      }
    throw Assertions.codeNotReachable();
    }

    default TOut tsVoid(TIn in, _void s) {
    //D |- void~> void:imm Void <= T | emptyTr
    //D.p|-imm Void <= T
    NormType t=Path.Void().toImmNT();
    if(TypeSystem.subtype(in.p, t, in.expected)){
      return new TOk(in,s,t);
      }
    TErr out=new TErr(in,"misplaced void constant",t,ErrorKind.NotSybtype);
    return out;
    }

    default TOut tsUsing(TIn in, Using s) {
    // TODO Auto-generated method stub
    return null;
    }

    default TOut tsSignal(TIn in, Signal s) {
      //D |- throw[_,_] e~> throw[T0,T3] e' : T0 <= T0 | Tr
      //  T1 = resolve(D.p,guessType(D.G,e))// Note, resolves and guessTypes can go in error, and need to become a type error here
      //  if throw=exception, T2= imm T1.P and Tr=Ts;Ps,P
      //  if throw=error,     T2= imm T1.P and Tr=Ts;Ps
      //  if throw=return,    T2= (fwd T1) and Tr=(Ts,T3);Ps 
      //  D|- e~>  e' :  T3 <=T2|Ts;Ps
      NormType T1=GuessTypeCore.of(in, s.getInner());
      NormType T2;      
      if(s.getKind()!=SignalKind.Return){T2=T1.getPath().toImmNT();}
      else{T2=TypeManipulation.fwd(T1);}
      TOut innerT=type(in.withE(s.getInner(), T2));
      if(!innerT.isOk()){throw Assertions.codeNotReachable();}
      TOk res=innerT.toOk();
      NormType T3=res.computed;
      if(s.getKind()==SignalKind.Return){res=res.returnsAdd(T3);}
      if(s.getKind()==SignalKind.Exception){res=res.exceptionsAdd(T3.getPath());}
      s=s.withInner(res.annotated).withTypeIn(T3).withTypeOut(in.expected);
      return res.withAC(s,in.expected);
    }

    default TOut tsClassB(TIn in, ClassB s) {
    //D |- L ~> L' : imm Library <= T | emptyTr
    //D.p|-imm Library <= T
    //D.Phase  |- D.p.evilPush(L) ~> L'
    NormType t=Path.Library().toImmNT();
    if(!TypeSystem.subtype(in.p, t, in.expected)){
      TErr out=new TErr(in,"-----------",t,ErrorKind.NotSybtype);
      return out;  
      }
    TOut out=typeLib(in.withP(in.p.evilPush(s)));
    if(out.isOk()){return new TOk(in,s,t);}
    return out.toError().enrich(in);
    }

    default TOut tsLoop(TIn in, Loop s) {
    //D |- loop e ~> loop e' : imm Void <= T | Tr
    //  D.p|-imm Void <= T
    //  D|- e ~> e' : _ <= imm Void | Tr
    TOut innerT=type(in.withE(s.getInner(), Path.Void().toImmNT()));
    if(!innerT.isOk()){throw Assertions.codeNotReachable();}
    if(TypeSystem.subtype(in.p, Path.Void().toImmNT(),in.expected)){
      TOk res= new TOk(in,s.withInner(innerT.toOk().annotated),innerT.toOk().computed);
      res=res.tsUnion(innerT.toOk());
      return res;
      }
    throw Assertions.codeNotReachable();
    }

}
