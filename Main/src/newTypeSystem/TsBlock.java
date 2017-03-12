package newTypeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import coreVisitors.FreeVariables;
import programReduction.Norm;
import tools.Assertions;
import tools.Map;

public interface TsBlock extends TypeSystem{
  default TOut tsBlock(TIn in, Block s) {
    TOut res1=tsBlockBase(in,s);
    if (res1.isOk()){return res1;}
    if (!promotionMakesSense(res1.toError())){return res1;}//promotionMakesSense: mut that need capsule
    TOut res2=tsBlockPromotion(in,s);//calls tsBlock internally,
    //thanks to promotionMakesSense does not goes in loop
    if (res2.isOk()){return res2;}
    //if we are here, both res1,res2 not ok
    return combine(res1.toError(),res2.toError());
    }
  
  default int splitDs(TIn in,List<Block.Dec> ds){
    int i=0;
    final int n=ds.size()-1;
    if (ds.isEmpty()){return 0;}
    List<String> xs=new ArrayList<>();
    while(true){
      xs.addAll(coreVisitors.FreeVariables.of(ds.get(i).getInner()));//xs U  FV(ei)
      if (xsNotInDomi(xs,ds,i+1)){return i;}
      //cut will be from 0 to i included
      if (i==n){return i;} //ds.size-1  
      i+=1;
      }
    }  
default boolean xsNotInDomi(List<String> xs,List<Dec> ds,int ip1){
  //ds=ds0..dsn;
  //domi=dom(dsi+1..dsn)
  //domi\xs=domi == no x in xs in dom(dsi+1..dsn)
  if(ip1>=ds.size()){return false;}
  String xDsip1=ds.get(ip1).getX();
  if(xs.contains(xDsip1)){return true;}
  return xsNotInDomi(xs,ds,ip1+1);
  }

  default TOut tsBlockBase(TIn in,Block s){
   //Phase| p| G |- (ds  ks  e0  [_]) ~>(ds' ks' e'0 [T]) 
   //     : T <= T' | Tr'.capture(p,ks') U Tr U Tr0
   List<Block.Dec> ds=s.getDecs();
   List<Block.On> ks=s.getOns();
   TIn in1=in.removeGDs(ds);//G'=G/dom(ds)
   TIn in2=in1.gKs(ks);//G'[ks]
   //Phase| p| G'[ks] |- ds ~> ds' |Tr' | G0
   TOutDs _dsOut=dsType(in2,ds);
   if(!_dsOut.isOk()){
     TErr err=_dsOut.toError();
     if(!err.kind.needContext){return err;}
     //here we have the info to capture a failure about ds and discover if    
     //extant name (fwd[%]* x) was hidden by error safety or  modifiable name (capsule/mut/lent x)
     //was locked by error safety[cite the line number of the catch]
     throw Assertions.codeNotReachable();
     }
   TOkDs dsOk=_dsOut.toOkDs();
   //Phase| p| G'| Tr' |- ks~> ks' : Ts <= T' | Tr
   TOutKs _ksOut=ksType(in1,dsOk.trAcc,ks);
   if(!_ksOut.isOk()){return _ksOut.toError();}
   TOkKs ksOk=_ksOut.toOkKs();
   //Phase| p| G'[G0\dom(G')] |- e0~>e'0:T0 <=T' | Tr0
   G G0LessG1=dsOk.g.removeGXs(in1.g.keySet());
   TOut _e0Out=type(in1.addGG(G0LessG1).withE(s.getE(), in.expected));
   if(!_e0Out.isOk()){return _e0Out.toError();}
   TOk e0Ok=_e0Out.toOk();
   //T= mostGeneralMdf({T0.mdf,Ts.mdfs}) T'.P //set of Mdfs admits no single most general mdf
   Set<Mdf>mdfs=new HashSet<>();
   for(NormType nt:ksOk.ts){
     mdfs.add(nt.getMdf());
     }
   mdfs.add(e0Ok.computed.getMdf());
   Mdf tMdf=TypeManipulation._mostGeneralMdf(mdfs);
   if(tMdf==null){return new TErr(in,"",e0Ok.computed,ErrorKind.NoMostGeneralMdf);}
   NormType t=new NormType(tMdf,in.expected.getPath(),Doc.empty());
   assert null==TypeSystem.subtype(in.p,t, in.expected);
   Block annotated=new Block(s.getDoc(),dsOk.ds,e0Ok.annotated,ksOk.ks,s.getP());
   TOk res=new TOk(in,annotated,t);
   // result Tr: Tr'.capture(p,ks') U Tr U Tr0
   assert ksOk.trCaptured!=null;
   Tr trUnion = ksOk.trCaptured.trUnion(ksOk.trAcc).trUnion(e0Ok);
   res=res.trUnion(trUnion);
   return res;
  }
  
  
  default TOutDs dsType(TIn in,List<Dec> _ds){
  //Phase| p| G |- empty ~> empty| empty;empty | G
    if(_ds.isEmpty()){return new TOkDs(Tr.instance,_ds,G.instance.addGG(in));}
  //Phase| p| G |- T0  x0=e0 ..Tn  xn=en, ds ~>
  //     T'0  x0=e'0 ..T'n  xn=e'n, ds'|Tr U Tr' | G2
    int i=splitDs(in,_ds);
    assert i+1<=_ds.size();
    List<Dec> ds=_ds.subList(i+1,_ds.size());
    List<Dec> ds0n=_ds.subList(0,i+1);
    List<String> fve0n=ds0n.stream().flatMap(d->FreeVariables.of(d.getInner()).stream()).collect(Collectors.toList());
    assert !fve0n.stream()
      .anyMatch(x->ds.stream()
        .anyMatch(d->d.getX().equals(x)));    
  //assert dom(ds) disjoint FV(e0..en)
    
  //  for i in 0..n T'i=resolve(p,Ti)
  //  G'=x0:T'0..xn:T'n //is just ds for the way I handle TIn
  //  G1= G[fwd(onlyMutOrImm(G'))] //capturing error for next line if not onlyMutOrImm(G') is used and is errored by next line
  List<Dec> dsFiltered = ds0n.stream().filter(
          d->{Mdf m=d.getT().getNT().getMdf(); return m==Mdf.Immutable||m==Mdf.Mutable;})
          .map(d->d.withT(TypeManipulation.fwd(d.getT().getNT())))
          .collect(Collectors.toList());
  TIn in1=in.addGds(in.p,dsFiltered); 
  //  for i in 0..n Phase| p| G1|-ei~>e'i: _ <= fwd% T'i | Tri
  //  Tr=Tr0 U .. U Trn
  Tr trAcc=Tr.instance;
  List<Dec>ds1=new ArrayList<>();
  List<Dec>ds1FwdP=new ArrayList<>();
  for(Dec di:ds0n){
    NormType nt=Norm.resolve(in.p,di.getT());
    NormType ntFwdP=TypeManipulation.fwdP(nt);
    TOut _out=type(in1.withE(di.getInner(),ntFwdP));
    if(!_out.isOk()){return _out.toError();}
    TOk ok=_out.toOk();
    trAcc=trAcc.trUnion(ok);
    Dec di1=di.withInner(ok.annotated);
    ds1.add(di1.withT(nt));
    ds1FwdP.add(di1.withT(ntFwdP));
    }
  //  if fwd_or_fwd%_in Tr.Ts
  //    then x0..xn disjoint FV(e0..en)//returning unresolved items from cycles is prohibited
  if(TypeManipulation.fwd_or_fwdP_in(trAcc.returns)){
    boolean xInCommon=fve0n.stream().anyMatch(x->ds0n.stream().anyMatch(d->d.getX().equals(x)));
    if(xInCommon){return new TErr(in,"",null,ErrorKind.AttemptReturnFwd);}
    }
  //  if fwd_or_fwd%_in { G(x) | x in FV(e0..en) } // x0..xn already excluded
  //    then G0=G[fwd%(G')]  
  //    otherwise G0=G[G']//capturing error for next line, see if the difference between fwd%(G') ad G' would fix it. Still, then we need to check for the fwd x in FV(e0..en)..
  List<NormType> _nts=new ArrayList<>();
  for(String x: fve0n){
    NormType t=in._g(x);
    if(t!=null){_nts.add(t);}
    }
  TIn inG0;
  if(TypeManipulation.fwd_or_fwdP_in(_nts)){inG0=in.addGds(in.p,ds1FwdP);}
  else{inG0=in.addGds(in.p,ds1);}
  //  Phase| p| G0|- ds ~> ds'|Tr' | G2
  TOutDs _res= dsType(inG0,ds);
  if(!_res.isOk()){return _res.toError();}
  TOkDs res=_res.toOkDs();
  ds1.addAll(res.ds);//safe? locally created, not leaked yet.
  if(res.trAcc!=null){trAcc=trAcc.trUnion(res.trAcc);}
  return new TOkDs(trAcc,ds1,res.g);
  }
  
  default TOutKs ksType(TIn in,Tr trAcc,List<On> ks){
//   D| Tr |-k1..kn ~> k'1..k'n:T1..Tn <= T | Tr1 U .. U Trn
//     forall i in 1..n D| Tr.capture(D.p,k1..ki-1)|-ki ~> k'i:Ti <= T |Tri
    assert trAcc!=null;
    Tr tr=trAcc;
    Tr newTrAcc=Tr.instance;
    List<On>ks1=new ArrayList<>();
    List<NormType>ts=new ArrayList<>();
    for(On k:ks){
      TOutK out=kType(in,tr,k);
      if(!out.isOk()){return out.toError();}
      TOkK ok=out.toOkK();
      ks1.add(ok.k);
      ts.add(ok.t);
      newTrAcc=newTrAcc.trUnion(ok.tr);
      tr=tr.trCapture(in.p,k);
      }

    TOkKs res=new TOkKs(newTrAcc,tr,ks,ts);
    return res;
    }    
  
  default TOutK kType(TIn in,Tr tr,On k){
    if(TypeManipulation.catchRethrow(k)){return kTypeCatchAny(in,tr,k);}
    return kTypeCatch(in,tr,k);
    }
      // T0 is the declared caught type, which contributes only a path
      // T1 is the actual caught type, based on the types which can be thrown in context
      // T2 is the type of the expression, based on x being bound T1
  default TOutK kTypeCatch(TIn in,Tr tr1,On k){
    if(k.getKind()==SignalKind.Return && tr1.returns.isEmpty()){return new TErr(in,"No returns in scope",null,ErrorKind.NoMostGeneralMdf);}
    Mdf mdf1=TypeManipulation._mostGeneralMdf(k.getKind(),tr1);
    if(mdf1==null){return new TErr(in,"Contrasting mdf expected for return",null,ErrorKind.NoMostGeneralMdf);}
    NormType T1 = Norm.resolve(in.p, k.getT()).withMdf(mdf1);
    TOut _out=type(in.addG(k.getX(),T1).withE(k.getE(), in.expected));
    if(!_out.isOk()){return _out.toError();}
    TOk out=_out.toOk();
    TOkK res=new TOkK(Tr.instance.trUnion(out),k.withE(out.annotated),out.computed);
    return res;
     /*   Phase| p| G| Tr' |- catch throw T0 x e ~> catch throw T1.P x e' :T2 <= T | Tr
     mdf1 = mostGeneralMdf(throw,Tr') //set of Mdfs admits no single most general mdf, or mdfs is empty
     //inconsistent set of thrown things, which do not share a most
     //general modifier [list of line numbers of the throws]
     T1 = mdf1 resolve (p, T0).P //resolve can fail
     not catchRethrow(catch throw T1 x e)
     Phase| p| G[x:T1]|- e ~> e' : T2 <= T | Tr
*/
    }
  default TOutK kTypeCatchAny(TIn in,Tr tr,On k){
    Block e=(Block) k.getE();
    ExpCore e0=e.getDecs().get(0).getInner();
    TIn in1=in.removeG(k.getX());
    TOut _out=type(in1.withE(e0,Path.Void().toImmNT()));
    if(!_out.isOk()){return _out.toError();}
    TOk ok=_out.toOk();
    if(!ok.exceptions.isEmpty() ||!ok.returns.isEmpty()){return new TErr(in,"",null,ErrorKind.UnsafeCatchAny);}
    return new TOkK(tr,k.withE(e.withDeci(0,e.getDecs().get(0).withInner(ok.annotated))),in.expected);
    /*
   (catch and rethrow any)// could be sugared as "on throw doAndPropagate e"  
   Phase |p |G |Tr|-catch throw Any x (e0 throw x) ~> catch throw Any x (e0' throw x): T<=T | Tr
     where //Note: e0, e, e0',e' are using the sugar imm Void x=e == e
     e0=(e catch error Any z void void)
     e0'=(e' catch error Any z void void)
     Phase |p |G\x |- e ~> e':_ <=imm Void | empty
     catchRethrow(catch throw Any x(e0 throw x)) 
*/
  }

//we are discussing if some blocks may not be promotable:
//for example blocks with empty ds and ks.

default TOut tsBlockPromotion(TIn in,Block s){
  //Phase |p |G |- (ds ks e)~>(ds' ks' e'):capsule P <=capsule P | Tr
  //  Phase |p |toLent(G) |-(ds ks e)~>(ds' ks' e'):mut P <=mut P   | Tr
  Mdf eM=in.expected.getMdf();
  assert eM==Mdf.Capsule || eM==Mdf.Immutable ||eM==Mdf.ImmutableFwd || eM==Mdf.ImmutablePFwd;
  
  TIn in2=in.toLent();
  TOut out=type(in2.withE(in.e,in.expected.withMdf(Mdf.Mutable)));
  if(!out.isOk()){return out;}
  TOk ok=out.toOk();
  return new TOk(in,ok.annotated,ok.computed.withMdf(Mdf.Capsule));
//this rule is now "deterministic" in the sense that if typing the block give us a capsule directly,
//this rule can not be applied, since we require mut P <=mut P in the premise.
  }


default TOut combine(TErr res,TErr promFail){throw Assertions.codeNotReachable();}

default boolean promotionMakesSense(TErr tErr){
    NormType expected=tErr.in.expected;
    NormType obtained=tErr._computed;
    if (null!=TypeSystem.subtype(tErr.in.p,obtained.getPath(), expected.getPath())){return false;}
    Mdf eM=expected.getMdf();
    Mdf oM=obtained.getMdf();
    boolean acceptableEM=eM==Mdf.Capsule || eM==Mdf.Immutable ||eM==Mdf.ImmutableFwd || eM==Mdf.ImmutablePFwd;
    return acceptableEM && oM==Mdf.Mutable;
  }

}
