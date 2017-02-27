package newTypeSystem;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import coreVisitors.From;
import programReduction.Program;
import tools.Map;

import java.util.List;

import ast.Ast.Mdf;
import ast.Ast.MethodSelector;

import static newTypeSystem.TypeManipulation.*;

public class AlternativeMethodTypes {

  MethodType mBase(Program p, Path P, MethodSelector ms){
//p(P)(ms).mh[from P]=refine? mdf0 method T m(T1 x1,..Tn xn) exception Ps
//(mBase)-------------------------------------------------------------------
//mdf0 P T1..Tn-> fwd% T;Ps in methTypes(p,P,ms)
    MethodWithType mwt = (MethodWithType) p.extractClassB(P)._getMember(ms);
    assert mwt!=null;
    MethodType mt=From.from(mwt.getMt(),P);
    NormType retT=fwdP(mt.getReturnType().getNT());
    return mt.withReturnType(retT);
    }

  MethodType mNoFwd(MethodType mt){
//  Ts->T;Ps in methTypes(p,P,ms) 
//(mNoFwd)-------------------------------------------------------------------
//  noFwd Ts-> noFwd T;Ps in methTypes(p,P,ms)
    List<Type> ts = Map.of(t->noFwd(t.getNT()),mt.getTs());
    Type retT=noFwd(mt.getReturnType().getNT());
    return mt.withReturnType(retT).withTs(ts);
    }

  MethodType _mC(MethodType mt){
//Ts->mut P0;Ps in methTypes(p,P,ms)
//(mC)-------------------------------------------------------------------
//mutToCapsule(Ts)->capsule P0;Ps in methTypes(p,P,ms)
    NormType retT=mt.getReturnType().getNT();
    if(retT.getMdf()!=Mdf.Mutable){return null;}
    retT=retT.withMdf(Mdf.Capsule);
    List<Type> ts = Map.of(t->mutToCapsule(t.getNT()),mt.getTs());
    return mt.withReturnType(retT).withTs(ts);
    }
  MethodType _mI(MethodType mt){
//Ts->read P0;Ps in methTypes(p,P,ms) //by well formedness if return type is read, not fwd_or_fwd%_in Ts
//(mI)-------------------------------------------------------------------
//toImmOrCapsule(Ts)->imm P0;Ps in methTypes(p,P,ms) 
////the behaviour of immorcapsule on fwd is not relevant since the method
//// returns a read and will be not well formed if it had fwd parameters
    NormType retT=mt.getReturnType().getNT();
    if(retT.getMdf()!=Mdf.Readable){return null;}
    retT=retT.withMdf(Mdf.Immutable);
    List<Type> ts = Map.of(t->toImmOrCapsule(t.getNT()),mt.getTs());
    return mt.withReturnType(retT).withTs(ts);
    }
  MethodType _mVp(MethodType mt, int parNum){
//Ts0 mut P Ts2->T;Ps in methTypes(p,P,ms)
//Ts'=mutToCapsule(Ts0) lent P mutToCapsule(Ts2) //this implies not fwd_or_fwd%_in Ts0,Ts2
//(mVp)-------------------------------------------------------------------
//Ts'->toLent(T);Ps in methTypes(p,P,ms)
    NormType pN=mt.getTs().get(parNum).getNT();
    if (pN.getMdf()!=Mdf.Mutable){return null;}
    NormType retT=mt.getReturnType().getNT();
    retT=_toLent(retT);
    if(retT==null){return null;}
    List<Type> ts = Map.of(t->mutToCapsule(t.getNT()),mt.getTs());
    ts.set(parNum, pN.withMdf(Mdf.Lent));
    return mt.withReturnType(retT).withTs(ts);
    }
  MethodType _mFwd(MethodType mt){
//Ts->fwd%Mut P0;Ps in methTypes(p,P,ms)
//(mFwd)-------------------------------------------------------------------
//mutToCapsuleAndFwdMutToFwdImm(Ts)->fwd%Imm P0;Ps in methTypes(p,P,ms)   
    NormType retT=mt.getReturnType().getNT();
    if(retT.getMdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.ImmutablePFwd);
    List<Type> ts = Map.of(t->mutToCapsuleAndFwdMutToFwdImm(t.getNT()),mt.getTs());
    return mt.withReturnType(retT).withTs(ts);
    }
  MethodType mRead(MethodType mt){
//Ts->fwd%Mut P0;Ps in methTypes(p,P,ms)
//(mRead)-------------------------------------------------------------------
//mutToCapsuleAndFwdMutToRead(Ts)->read P0;Ps in methTypes(p,P,ms)
    NormType retT=mt.getReturnType().getNT();
    if(retT.getMdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.Readable);
    List<Type> ts = Map.of(t->mutToCapsuleAndFwdMutToRead(t.getNT()),mt.getTs());
    return mt.withReturnType(retT).withTs(ts);
    }
  
  
  }
