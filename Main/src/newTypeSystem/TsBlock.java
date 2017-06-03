package newTypeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    if (!promotionMakesSense(in,res1.toError())){return res1;}//promotionMakesSense: mut that need capsule
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
  if(ip1>=ds.size()){return true;}
  String xDsip1=ds.get(ip1).getX();
  if(xs.contains(xDsip1)){return false;}
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
     //TODO: here we have the info to capture a failure about ds and discover if    
     //extant name (fwd[%]* x) was hidden by error safety or  modifiable name (capsule/mut/lent x)
     //was locked by error safety[cite the line number of the catch]
     return err;
     }
   TOkDs dsOk=_dsOut.toOkDs();
   //Phase| p| G'| Tr' |- ks~> ks' : Ts <= T' | Tr
   TOutKs _ksOut=ksType(in1,dsOk.trAcc,ks);
   if(!_ksOut.isOk()){return _ksOut.toError();}
   TOkKs ksOk=_ksOut.toOkKs();
   ks=ksOk.ks;//now resolved
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
   if(tMdf==null){
     return new TErr(in,"",e0Ok.computed,ErrorKind.NoMostGeneralMdf);
     }
   NormType t=new NormType(tMdf,in.expected.getPath(),Doc.empty());
   assert null==TypeSystem.subtype(in.p,t, in.expected);
   Block annotated=new Block(s.getDoc(),dsOk.ds,e0Ok.annotated,ksOk.ks,s.getP());
   TOk res=new TOk(in,annotated,t);
   // result Tr: Tr'.capture(p,ks') U Tr U Tr0
   Tr trCaptured=dsOk.trAcc;
   for(On k : ks){
     trCaptured=trCaptured.trCapture(in.p,k);
     }
   Tr trUnion = trCaptured.trUnion(ksOk.trAcc).trUnion(e0Ok);
   res=res.trUnion(trUnion);
   return res;
  }  
  
  default TOutDs dsType(TIn in,List<Dec> _ds){
    if(_ds.isEmpty()){return new TOkDs(Tr.instance,_ds,G.instance.addGG(in));}
    int i=splitDs(in,_ds);
    assert i+1<=_ds.size();
    List<Dec> ds=_ds.subList(i+1,_ds.size());
    List<Dec> ds0n=GuessTypeCore.guessedDs(in,_ds.subList(0,i+1));//G'
    List<String> fve0n=new ArrayList<>();
    for(Dec di:_ds.subList(0,i+1)){
      fve0n.addAll(FreeVariables.of(di.getInner()));
      }
    assert !fve0n.stream()
      .anyMatch(x->ds.stream()
        .anyMatch(d->d.getX().equals(x)));    
    List<Dec> dsFiltered = ds0n.stream().filter(
          d->{Mdf m=d.getT().get().getMdf(); return m==Mdf.Immutable||m==Mdf.Mutable;})
          .map(d->d.withVar(false).withT(Optional.of(TypeManipulation.fwd(d.getT().get()))))
          .collect(Collectors.toList());
    TIn in1=in.addGds(in.p,dsFiltered); //G1
    Tr trAcc=Tr.instance;
    List<Dec>ds1=new ArrayList<>();
    List<Dec>ds1FwdP=new ArrayList<>();
    for(Dec di:ds0n){
      NormType nt=di.getT().get();
      NormType ntFwdP=TypeManipulation.fwdP(nt);
      TOut _out=type(in1.withE(di.getInner(),ntFwdP));
      if(!_out.isOk()){return _out.toError();}
      TOk ok=_out.toOk();
      trAcc=trAcc.trUnion(ok);
      Dec di1=di.withInner(ok.annotated);
      if(TypeManipulation.fwd_or_fwdP_in(nt.getMdf())){//building G2
        ds1.add(di1.withT(Optional.of(ok.computed)));
        }
      else{ds1.add(di1.withT(Optional.of(TypeManipulation.noFwd(ok.computed))));}
      ds1FwdP.add(di1.withVar(false).withT(Optional.of(TypeManipulation.fwdP(ok.computed))));
      }
    if(TypeManipulation.fwd_or_fwdP_in(trAcc.returns)){
      boolean xInCommon=fve0n.stream().anyMatch(x->ds0n.stream().anyMatch(d->d.getX().equals(x)));
      if(xInCommon){return new TErr(in,"",null,ErrorKind.AttemptReturnFwd);}
      }
    List<NormType> _nts=new ArrayList<>();
    for(String x: fve0n){
      NormType t=in._g(x);
      if(t!=null){_nts.add(t);}
      }
    TIn inG0;
    if(TypeManipulation.fwd_or_fwdP_in(_nts)){inG0=in.addGds(in.p,ds1FwdP);}
    else{inG0=in.addGds(in.p,ds1);}
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
      }

    TOkKs res=new TOkKs(newTrAcc,ks1,ts);
    return res;
    }    
  
  default TOutK kType(TIn in,Tr tr,On k){
    if(TypeManipulation.catchRethrow(k)){return kTypeCatchAny(in,tr,k);}
    boolean preciseApplicable=
      k.getKind()==SignalKind.Return &&
      k.getT().equals(Path.Any().toImmNT()) &&
      tr.returns.stream().allMatch(t->
        TypeSystem.subtype(in.p, t,in.expected)==null
        );
    if (preciseApplicable){return kTypeCatchPreciseAny(in, tr, k);}
    return kTypeCatch(in,tr,k);
    }
      // T0 is the declared caught type, which contributes only a path
      // T1 is the actual caught type, based on the types which can be thrown in context
      // T2 is the type of the expression, based on x being bound T1
  default TOutK kTypeCatch(TIn in,Tr tr1,On k){
    if(k.getKind()==SignalKind.Return && tr1.returns.isEmpty()){
      return new TErr(in,"No returns in scope",null,ErrorKind.NoMostGeneralMdf);
      }
    Mdf mdf1=TypeManipulation._mostGeneralMdf(k.getKind(),tr1);
    if(mdf1==null){
    return new TErr(in,"Contrasting mdf expected for return",null,ErrorKind.NoMostGeneralMdf);
    }
    NormType T1 = k.getT().withMdf(mdf1);
    TOut _out=type(in.addG(k.getX(),false,T1).withE(k.getE(), in.expected));
    if(!_out.isOk()){return _out.toError();}
    TOk out=_out.toOk();
    TOkK res=new TOkK(Tr.instance.trUnion(out),k.withE(out.annotated).withT(T1),out.computed);
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
  default TOutK kTypeCatchPreciseAny(TIn in,Tr tr,On k){
    assert k.getKind()==SignalKind.Return;
    assert k.getT().equals(Path.Any().toImmNT());
    assert !TypeManipulation.catchRethrow(k);
    TOut _out=type(in.addG(k.getX(),false,in.expected).withE(k.getE(), in.expected));
    if(!_out.isOk()){return _out.toError();}
    TOk out=_out.toOk();
    TOkK res=new TOkK(Tr.instance.trUnion(out),k.withE(out.annotated).withT(in.expected),out.computed);
    return res;
    }
  default TOutK kTypeCatchAny(TIn in,Tr tr,On k){
    Block e=(Block) k.getE();
    ExpCore e0=e.getDecs().get(0).getInner();
    TIn in1=in.removeG(k.getX());
    TOut _out=type(in1.withE(e0,Path.Void().toImmNT()));
    if(!_out.isOk()){return _out.toError();}
    TOk ok=_out.toOk();
    if(!ok.exceptions.isEmpty() ||!ok.returns.isEmpty()){return new TErr(in,"",null,ErrorKind.UnsafeCatchAny);}
    ExpCore newE=e.withDeci(0,e.getDecs().get(0).withInner(ok.annotated));
    return new TOkK(tr,k.withE(newE),in.expected);
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
  assert eM==Mdf.Capsule || eM==Mdf.Immutable ||eM==Mdf.ImmutableFwd || eM==Mdf.ImmutablePFwd:
    eM;
  
  TIn in2=in.toLent();
  TOut out=type(in2.withE(in.e,in.expected.withMdf(Mdf.Mutable)));
  if(!out.isOk()){return out;}
  TOk ok=out.toOk();
  return new TOk(in,ok.annotated,ok.computed.withMdf(Mdf.Capsule));
//this rule is now "deterministic" in the sense that if typing the block give us a capsule directly,
//this rule can not be applied, since we require mut P <=mut P in the premise.
  }


default TErr combine(TErr res,TErr promFail){
  return new TErr(res.in,res.msg
  +"\n(Block promotion attempted but\n"+promFail.in+"\n failed)",res._computed,res.kind);
  }

default boolean promotionMakesSense(TIn in,TErr tErr){
    NormType expected=in.expected;
    NormType obtained=tErr._computed;
    if(expected==null || obtained==null){return false;}
    if (null!=TypeSystem.subtype(in.p,obtained.getPath(), expected.getPath())){return false;}
    Mdf eM=expected.getMdf();
    Mdf oM=obtained.getMdf();
    boolean acceptableEM=eM==Mdf.Capsule || eM==Mdf.Immutable ||eM==Mdf.ImmutableFwd || eM==Mdf.ImmutablePFwd;
    return acceptableEM && oM==Mdf.Mutable;
  }

}
