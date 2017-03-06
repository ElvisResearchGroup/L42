package newTypeSystem;

import ast.ExpCore.MCall;
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
    List<MethodType> mTypes = AlternativeMethodTypes.types(in.p,rec,s.getS());
    MethodType mType=AlternativeMethodTypes._firstMatchReturn(in.p,in.expected,mTypes);
    if (mType==null){throw Assertions.codeNotReachable();}
//unachievable return type (T) for method (P.ms) [line numbers of expression and declaration]
//2 type all the parameters with mutOnlyToLent(Ts) //we may include mutOnlyToLent in the computation of the MTypes, instead of in the loop below
    List<TOk> resp=new ArrayList<>();
    List<Type> computed=new ArrayList<>();
    List<ExpCore> annotated=new ArrayList<>();
    ExpCore e0=s.getInner();
    NormType t0=new NormType(mType.getMdf(),rec,Doc.empty());
    TOut _res0=type(in.withE(e0,TypeManipulation.mutOnlyToLent(t0)));
    if(!_res0.isOk()){throw Assertions.codeNotReachable();}
    TOk res0=_res0.toOk();
    Mdf recMdf=_res0.toOk().computed.getMdf();
    {int i=-1;for(  ExpCore ei:s.getEs()){i+=1;
      NormType ti=mType.getTs().get(i).getNT();
      TOut _resi=type(in.withE(ei,TypeManipulation.mutOnlyToLent(ti)));
      if(!_resi.isOk()){throw Assertions.codeNotReachable();}
      resp.add(_resi.toOk());
      computed.add(_resi.toOk().computed);
      annotated.add(_resi.toOk().annotated);
    }}
  MethodType mTypeRev=AlternativeMethodTypes._bestMatchMtype(in.p,
          new MethodType(false,recMdf,computed,in.expected,Collections.emptyList()),mTypes);
  if (mTypeRev!=null){
    MCall resM=new MCall(res0.annotated,s.getS(),s.getDoc(),annotated,s.getP());
    TOk res=new TOk(in,resM,mTypeRev.getReturnType().getNT());
    // Trs[with r in resp (use[r.Tr])].collapse())
    for(TOk oki:resp){res=res.tsUnion(oki);}
    return res;
    }
/*
//3 if there is no matching method, we may need to retype some mut
//in capsule caused by mvp:
//it is  not over if there is a mathing method type with mutToCapsule(result param types)
tsToCaps=Ts[with r in resp (use[mutToCapsule(r.T)])]
mTypeMVP=_bestMatchMtype(tsToCaps,TSIn.T,mTypes)
------------------
if mTypeMVP==null Error????
//this assert could not work: assert firstMatchParameters(tsToCaps,mTypesRes-mTypeMVP) fails
//To be happy, we can retype the obtained mut parameters into expected capsule
respMVP=TSOut[with ri in resp, T ti in mTypeMVP.TS (
  if (ri.mdf <= ti.mfd) (
    assert ri.mdf != Mdf.mut //James think this assertion may fail for ri/ti= read 
    use [ri]
    )
  else(
    assert ri.mdf == Mdf.mut
    assert ti.mdf == Mdf.capsule
    resi=ts(ri.in.with(expectedT:ti))
    if !resi.isOK()  return Error???
    use [resi]
    )
  )]
assert forall r in respMVP, r.isOk()
return res=makeMCallOK(TSIn,respMVP,mTypeMVP)
*/
throw Assertions.codeNotReachable();
}

}
