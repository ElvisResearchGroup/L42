package newTypeSystem;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.WellFormednessCore;
import coreVisitors.From;
import programReduction.Program;
import tools.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.Ast.Mdf;
import ast.Ast.MethodSelector;

import static newTypeSystem.TypeManipulation.*;

public class AlternativeMethodTypes {

  static MethodType mBase(Program p, Path P, MethodSelector ms){
//p(P)(ms).mh[from P]=refine? mdf0 method T m(T1 x1,..Tn xn) exception Ps
//T'=fwd% T if fwd_or_fwd%_in(Ts)
//otherwise T'=T       
//(mBase)-------------------------------------------------------------------
//mdf0 P T1..Tn-> T';Ps in methTypes(p,P,ms)    MethodWithType mwt = (MethodWithType) p.extractClassB(P)._getMember(ms);
    MethodWithType mwt = (MethodWithType) p.extractClassB(P)._getMember(ms);
    assert mwt!=null;
    MethodType mt=From.from(mwt.getMt(),P);
    return mBase(mt);
    }
  static MethodType mBase(MethodType mt){
  NormType retT=mt.getReturnType();    
    if (fwd_or_fwdP_in(mt.getTs())){
      retT=fwdP(mt.getReturnType());
    }
    return mt.withReturnType(retT);
    }
  static MethodType mNoFwd(MethodType mt){
//  Ts->T;Ps in methTypes(p,P,ms) 
//(mNoFwd)-------------------------------------------------------------------
//  noFwd Ts-> noFwd T;Ps in methTypes(p,P,ms)
    List<NormType> ts = Map.of(t->noFwd(t),mt.getTs());
    NormType retT=noFwd(mt.getReturnType());
    return mt.withReturnType(retT).withTs(ts);
    }

  static MethodType _mC(MethodType mt){
//Ts->mut P0;Ps in methTypes(p,P,ms)
//(mC)-------------------------------------------------------------------
//mutToCapsule(Ts)->capsule P0;Ps in methTypes(p,P,ms)
    NormType retT=mt.getReturnType();
    if(retT.getMdf()!=Mdf.Mutable){return null;}
    retT=retT.withMdf(Mdf.Capsule);
    List<NormType> ts = Map.of(t->mutToCapsule(t),mt.getTs());
    return mt.withReturnType(retT).withTs(ts).withMdf(mutToCapsule(mt.getMdf()));
    }
  static MethodType _mI(MethodType mt){
//Ts->read P0;Ps in methTypes(p,P,ms) //by well formedness if return type is read, not fwd_or_fwd%_in Ts
//(mI)-------------------------------------------------------------------
//toImmOrCapsule(Ts)->imm P0;Ps in methTypes(p,P,ms) 
////the behaviour of immorcapsule on fwd is not relevant since the method
//// returns a read and will be not well formed if it had fwd parameters
    NormType retT=mt.getReturnType();
    if(retT.getMdf()!=Mdf.Readable && retT.getMdf()!=Mdf.Lent){return null;}
    retT=retT.withMdf(Mdf.Immutable);
    List<NormType> ts = Map.of(t->toImmOrCapsule(t),mt.getTs());
    return mt.withReturnType(retT).withTs(ts).withMdf(toImmOrCapsule(mt.getMdf()));
    }

  static MethodType _mVp(MethodType mt, int parNum){
    if(parNum>0){return _mVpNoRec(mt,parNum-1);}
    assert parNum==0;
    if(mt.getMdf()!=Mdf.Mutable){return null;}
    NormType retT=mt.getReturnType();
    retT=_toLent(retT);
    if(retT==null){return null;}
    List<NormType> ts = Map.of(t->mutToCapsule(t),mt.getTs());
    MethodType res= mt.withReturnType(retT).withTs(ts).withMdf(Mdf.Lent);
    if(WellFormednessCore.methodTypeWellFormed(res)){return res;}
    return null;
    }
  static MethodType _mVpNoRec(MethodType mt, int parNum){
//Ts0 mut P Ts2->T;Ps in methTypes(p,P,ms)
//Ts'=mutToCapsule(Ts0) lent P mutToCapsule(Ts2) //this implies not fwd_or_fwd%_in Ts0,Ts2
//(mVp)-------------------------------------------------------------------
//Ts'->toLent(T);Ps in methTypes(p,P,ms)
    NormType pN=mt.getTs().get(parNum);
    if (pN.getMdf()!=Mdf.Mutable){return null;}
    NormType retT=mt.getReturnType();
    retT=_toLent(retT);
    if(retT==null){return null;}
    List<NormType> ts = Map.of(t->mutToCapsule(t),mt.getTs());
    ts.set(parNum, pN.withMdf(Mdf.Lent));
    MethodType res= mt.withReturnType(retT).withTs(ts).withMdf(mutToCapsule(mt.getMdf()));
    if(WellFormednessCore.methodTypeWellFormed(res)){return res;}
    return null;
    }
  static MethodType _mImmFwd(MethodType mt){
//Ts->fwd%Mut P0;Ps in methTypes(p,P,ms)
//fwd_or_fwd%_in(Ts)
//(mImmFwd)-------------------------------------------------------------------
//mutToCapsuleAndFwdMutToFwdImm(Ts)->fwd%Imm P0;Ps in methTypes(p,P,ms)
    if(!TypeManipulation.fwd_or_fwdP_in(mt.getTs())){return null;}
    NormType retT=mt.getReturnType();
    if(retT.getMdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.ImmutablePFwd);
    List<NormType> ts = Map.of(t->mutToCapsuleAndFwdMutToFwdImm(t),mt.getTs());
    MethodType res= mt.withReturnType(retT).withTs(ts).withMdf(mutToCapsuleAndFwdMutToFwdImm(mt.getMdf()));
    if(WellFormednessCore.methodTypeWellFormed(res)){return res;}
    return null;
    }
  static MethodType _mRead(MethodType mt){
//Ts->fwd%Mut P0;Ps in methTypes(p,P,ms)
//fwd_or_fwd%_in(Ts)
//(mRead)-------------------------------------------------------------------
//mutToCapsuleAndFwdToRead(Ts)->read P0;Ps in methTypes(p,P,ms)
    if(!TypeManipulation.fwd_or_fwdP_in(mt.getTs())){return null;}
    NormType retT=mt.getReturnType();
    if(retT.getMdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.Readable);
    List<NormType> ts = Map.of(t->mutToCapsuleAndFwdToRead(t),mt.getTs());
    MethodType res=mt.withReturnType(retT).withTs(ts).withMdf(mutToCapsuleAndFwdToRead(mt.getMdf()));
    if(WellFormednessCore.methodTypeWellFormed(res)){return res;}
    return null;
    }
  
  static void add(List<MethodType>l,MethodType t){
    if(t==null){return;}
    l.add(t);
    }
    static MethodType _mtDeclared(Program p, Path P, MethodSelector ms){
      MethodWithType mwt = (MethodWithType) p.extractClassB(P)._getMember(ms);
      if(mwt==null){return null;}
      MethodType mt=From.from(mwt.getMt(),P);
      return mt;
      }

  static List<MethodType> types(MethodType mt){
    List<MethodType>res=new ArrayList<>();
    MethodType base=mBase(mt);
    add(res,base);
    MethodType mNoFwd=mNoFwd(base);
    add(res,mNoFwd);
    MethodType mImmFwd=_mImmFwd(base);
    add(res,mImmFwd);
    MethodType mRead=_mRead(base);
    add(res, mRead);
    add(res,_mC(base));
    add(res,_mC(mNoFwd));
    add(res,_mI(base));
    if(mRead!=null){add(res,_mI(mRead));}
    if(mImmFwd!=null){add(res,mNoFwd(mImmFwd));}  
    if(mt.getMdf()==Mdf.Mutable){add(res,_mVp(base,0));}
    //later, 0 for mvp is the receiver so is ok to start from 1
    {int i=0;for(NormType ti:base.getTs()){i+=1;
      if(ti.getMdf()!=Mdf.Mutable){continue;}
      add(res,_mVp(base,i)); //1 mType for each mut parameter
      }}
    if(mt.getMdf()==Mdf.Mutable){add(res,_mVp(mNoFwd,0));}
    {int i=0;for(NormType ti:mNoFwd.getTs()){i+=1;
    if(ti.getMdf()!=Mdf.Mutable){continue;}
    add(res,_mVp(mNoFwd,i)); //1 mType for each mut parameter
    }}
    return res;
  }
  public static MethodType _firstMatchReturn(Program p,NormType nt,List<MethodType> mts){
  for(MethodType mt:mts){
    if(null==TypeSystem.subtype(p, mt.getReturnType(), nt)){return mt;}
    }
  return null;
  }

public static MethodType _bestMatchMtype(Program p,MethodType superMt,List<MethodType> mts){
  List<MethodType> res=new ArrayList<>();
  for(MethodType mt:mts){
    if(TypeSystem._methMdfTSubtype(mt, superMt)){
      if(!res.stream().anyMatch(mti->TypeSystem._methMdfTSubtype(mti, mt))){
        res=res.stream().filter(mti->!TypeSystem._methMdfTSubtype(mt, mti)).collect(Collectors.toList());
        res.add(mt);//if there is no method that is even better, add
        }
      }
  }
  //assert res.size()==1: res.size(); sometime is false, for example capsule->capsule and mut->mut
  if(res.isEmpty()){return null;}
  if(res.size()==1){
    return res.get(0);
    }
  List<MethodType> _res=res;//for final limitations
  Optional<MethodType> res1 = res.stream().filter(
    mt1->_res.stream().allMatch(
      mt2->Functions.isSubtype(
        mt1.getReturnType().getMdf(),mt2.getReturnType().getMdf()
        ))).findAny();
    if(res1.isPresent()){return res1.get();}
    assert false:
      "";
    return res.get(0);
  }
}
