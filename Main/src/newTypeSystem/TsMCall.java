package newTypeSystem;

import ast.ExpCore.MCall;
import auxiliaryGrammar.Functions;
import tools.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.Ast.Type;
public interface TsMCall extends TypeSystem{

  default TOut tsMCall(TIn in, MCall s) {
    NormType _rec=GuessTypeCore.of(in, s.getInner());
    Path rec=_rec.getPath();
    MethodType mDec=AlternativeMethodTypes.mtDeclared(in.p,rec,s.getS());
    NormType ret=mDec.getReturnType().getNT();
    ErrorKind kind = TypeSystem.subtype(in.p, ret.getPath(),in.expected.getPath());
    if(kind!=null){return new TErr(in,"",ret,kind);}
    List<MethodType> mTypes = AlternativeMethodTypes.types(mDec);
    MethodType mType=AlternativeMethodTypes._firstMatchReturn(in.p,in.expected,mTypes);
    if (mType==null){
      return new TErr(in,"",ret,ErrorKind.NotSubtypeMdf);
      }
//unachievable return type (T) for method (P.ms) [line numbers of expression and declaration]
//2 type all the parameters with mutOnlyToLent(Ts) //we may include mutOnlyToLent in the computation of the MTypes, instead of in the loop below
    List<TOk> resp=new ArrayList<>();
    List<Type> computed=new ArrayList<>();
    List<ExpCore> annotated=new ArrayList<>();
    ExpCore e0=s.getInner();
    NormType t0=new NormType(mType.getMdf(),rec,Doc.empty());
    TOut _res0=type(in.withE(e0,TypeManipulation.mutOnlyToLent(t0)));
    if(!_res0.isOk()){return _res0.toError();}
    TOk res0=_res0.toOk();
    Mdf recMdf=_res0.toOk().computed.getMdf();
    {int i=-1;for(  ExpCore ei:s.getEs()){i+=1;
      NormType ti=mType.getTs().get(i).getNT();
      TOut _resi=type(in.withE(ei,TypeManipulation.mutOnlyToLent(ti)));
      if(!_resi.isOk()){return _resi.toError();}
      resp.add(_resi.toOk());
      computed.add(_resi.toOk().computed);
      annotated.add(_resi.toOk().annotated);
    }}
  MethodType computedMt=new MethodType(false,recMdf,computed,in.expected,Collections.emptyList());
  MethodType mTypeRev=AlternativeMethodTypes._bestMatchMtype(in.p,computedMt,mTypes);
  if (mTypeRev!=null){
    MCall resM=new MCall(res0.annotated,s.getS(),s.getDoc(),annotated,s.getP());
    TOk res=new TOk(in,resM,mTypeRev.getReturnType().getNT());
    // Trs[with r in resp (use[r.Tr])].collapse())
    for(TOk oki:resp){res=res.tsUnion(oki);}
    return res;
    }
//3 if there is no matching method, we may need to retype some mut
//in capsule caused by mvp:
//it is  not over if there is a mathing method type with mutToCapsule(result param types)
//tsToCaps=Ts[with r in resp (use[mutToCapsule(r.T)])]
//mTypeMVP=_bestMatchMtype(tsToCaps,TSIn.T,mTypes)
  List<Type>tsToCaps=new ArrayList<>();
  for(TOk r: resp){
    Mdf m=r.computed.getMdf();
    if(m==Mdf.MutableFwd || m==Mdf.MutablePFwd){
      return new TErr(in,"impossible to search for mvp since mdf "+m,mTypes.get(0).getReturnType().getNT(),ErrorKind.NotSubtypeClass);
      }
    NormType nt=TypeManipulation.mutToCapsule(r.computed);
    tsToCaps.add(nt);
    }
  computedMt=computedMt.withTs(tsToCaps).withMdf(TypeManipulation.mutToCapsule(computedMt.getMdf()));
  MethodType mTypeMVP=AlternativeMethodTypes._bestMatchMtype(in.p, computedMt, mTypes);
  if (mTypeMVP==null){
    return new TErr(in,"mvp candidate notfound",mTypes.get(0).getReturnType().getNT(),ErrorKind.NotSubtypeClass);
    }
//To be happy, we can retype the obtained mut parameters into expected capsule
  List<TOk> respMVP=new ArrayList<>();
  {int i=-1;for(TOk ri :resp){i+=1;NormType ti=mTypeMVP.getTs().get(i).getNT();
//respMVP=TSOut[with ri in resp, T ti in mTypeMVP.TS (
    if(Functions.isSubtype(ri.computed.getMdf(),ti.getMdf())){
//  if (ri.mdf <= ti.mfd) (
    assert ri.computed.getMdf()!=Mdf.Mutable; 
    respMVP.add(ri);
//    assert ri.mdf != Mdf.mut //James think this assertion may fail for ri/ti= read 
//    use [ri]
//    )
//  else(
    }else{
    assert ri.computed.getMdf() == Mdf.Mutable;
    assert ti.getMdf() == Mdf.Capsule;
    TIn ini=ri.in.withE(ri.in.e,ti);
    TOut resi=type(ini);
    if(!resi.isOk()){return resi.toError();}
//    resi=ts(ri.in.with(expectedT:ti))
//    if !resi.isOK()  return Error???
//    use [resi]
    respMVP.add(resi.toOk());
    }}}
//    )
//  )]
//return res=makeMCallOK(TSIn,respMVP,mTypeMVP)
NO NO, I have forgot about the receiver not in the ts!!!
MCall resM=new MCall(res0.annotated,s.getS(),s.getDoc(),annotated,s.getP());
TOk res=new TOk(in,resM,mTypeRev.getReturnType().getNT());
// Trs[with r in resp (use[r.Tr])].collapse())
for(TOk oki:resp){res=res.tsUnion(oki);}
return res;
}

}
