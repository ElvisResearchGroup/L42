package newTypeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
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
    List<String> xs=new ArrayList<>();
    while(true){
      xs.addAll(coreVisitors.FreeVariables.of(ds.get(i).getInner()));//xs U  FV(ei)
      if (xsNotInDomi(xs,ds,i+1)){return i;}
      //cut will be from 0 to i included
      i+=1;
      if (i==n){return i;} //ds.size-1      
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
     //here we have the info to capture a failure about ds and discover if    
     //extant name (fwd[%]* x) was hidden by error safety or  modifiable name (capsule/mut/lent x)
     //was locked by error safety[cite the line number of the catch]
     throw Assertions.codeNotReachable();
     }
   TOkDs dsOk=_dsOut.toOkDs();
   //Phase| p| G'| Tr' |- ks~> ks' : Ts <= T' | Tr
   TOutKs _ksOut=ksType(in1,dsOk.trAcc,ks);
   if(!_ksOut.isOk()){throw Assertions.codeNotReachable();}
   TOkKs ksOk=_ksOut.toOkKs();
   //Phase| p| G'[G0\dom(G')] |- e0~>e'0:T0 <=T' | Tr0
   TIn G0LessG1=dsOk.g.removeGXs(in1.g.keySet());
   TOut _e0Out=type(G0LessG1.withE(s.getE(), in.expected));
   if(!_e0Out.isOk()){throw Assertions.codeNotReachable();}
   TOk e0Ok=_e0Out.toOk();
   //T= mostGeneralMdf({T0.mdf,Ts.mdfs}) T'.P //set of Mdfs admits no single most general mdf
   Set<Mdf>mdfs=new HashSet<>();
   for(NormType nt:ksOk.ts){
     mdfs.add(nt.getMdf());
     }
   mdfs.add(e0Ok.computed.getMdf());
   Mdf tMdf=TypeManipulation._mostGeneralMdf(mdfs);
   if(tMdf==null){throw Assertions.codeNotReachable();}
   NormType t=new NormType(tMdf,in.expected.getPath(),Doc.empty());
   assert TypeSystem.subtype(in.p,t, in.expected);
   Block annotated=new Block(s.getDoc(),dsOk.ds,e0Ok.annotated,ksOk.ks,s.getP());
   TOk res=new TOk(in,annotated,t);
   // result Tr: Tr'.capture(p,ks') U Tr U Tr0
   res=res.tsCapture(ksOk.ks).tsUnion(ksOk.trAcc).tsUnion(e0Ok);
   return res;
  }
  
  
  default TOutDs dsType(TIn in,List<Dec> _ds){
  //Phase| p| G |- empty ~> empty| empty;empty | G
    if(_ds.isEmpty()){return new TOkDs(null,_ds,in);}
  //Phase| p| G |- T0  x0=e0 ..Tn  xn=en, ds ~>
  //     T'1  x1=e'1 ..T'n  xn=e'n, ds'|Tr U Tr' | G2
    int i=splitDs(in,_ds);
    List<Dec> ds=_ds.subList(i+1,_ds.size());
    List<Dec> ds0n=_ds.subList(0,i);
    assert ds0n.stream()//fuck streams, love math..
      .flatMap(d->FreeVariables.of(d.getInner()).stream())
      .filter(x->ds.stream().anyMatch(d->d.getX().equals(x)))
      .count()==0;    
  //assert dom(ds) disjoint FV(e0..en)
    
  //  for i in 0..n T'i=resolve(p,Ti)
  //  G'=x0:T'0..xn:T'n //is just ds for the way I handle TIn
  //  G1= G[fwd(onlyMutOrImm(G'))] //capturing error for next line if not onlyMutOrImm(G') is used and is errored by next line
  List<Dec> dsFiltered = ds.stream().filter(
          d->{Mdf m=d.getT().getNT().getMdf(); return m==Mdf.Immutable||m==Mdf.Mutable;})
          .map(d->d.withT(TypeManipulation.fwd(d.getT().getNT())))
          .collect(Collectors.toList());
  TIn in1=in.addGds(dsFiltered); 
  //  for i in 0..n Phase| p| G1|-ei~>e'i: _ <= fwd% T'i | Tri
  //  Tr=Tr0 U .. U Trn
  TOk trAcc=new TOk(null,null,null);
  List<Dec>ds1=new ArrayList<>();
  for(Dec di:ds0n){
    NormType nt=Norm.resolve(in.p,di.getT());
    TOut _out=type(in.withE(di.getInner(),TypeManipulation.fwdP(nt)));
    if(!_out.isOk()){throw Assertions.codeNotReachable();}
    TOk ok=_out.toOk();
    trAcc=trAcc.tsUnion(ok);
    ds1.add(di.withInner(ok.annotated).withT(nt));
    }
  //  if fwd_or_fwd%_in Tr.Ts
  //    then x1..xn disjoint FV(e0..en)//returning unresolved items from cycles is prohibited
  if(TypeManipulation.fwd_or_fwdP_in(trAcc.returns)){
    if(!)
    }
  //  if fwd_or_fwd%_in { G(x) | x in FV(e0..en) } // x0..xn already excluded
  //    then G0=G[fwd%(G')]
  //    otherwise G0=G[G']//capturing error for next line, see if the difference between fwd%(G') ad G' would fix it. Still, then we need to check for the fwd x in FV(e0..en)..
  //  Phase| p| G0|- ds ~> ds'|Tr' | G2
  }
   /*
      
   //TODO: check that this kind of things work {Bar:{}   method m (foo catch exception Bar x e1 e0)  } redirect Bar->Any
   //Note: the new idea is that catch throw Any will catch all that can be thrown,
   // if not of form catchRethrow(k); in that case *only* the catchRethrow rule will apply
   (catchMany)
   D| Tr |-k1..kn ~> k'1..k'n:T1..Tn <= T | Tr1 U .. U Trn
     where
     forall i in 1..n D| Tr.capture(D.p,k1..ki-1)|-ki ~> k'i:Ti <= T |Tri
     
   (catch)
      // T0 is the declared caught type, which contributes only a path
      // T1 is the actual caught type, based on the types which can be thrown in context
      // T2 is the type of the expression, based on x being bound T1

   Phase| p| G| Tr' |- catch throw T0 x e ~> catch throw resolve(p,T0).P x e' :T2 <= T | Tr
     where
     mdf1 = mostGeneralMdf(throw,Tr') //set of Mdfs admits no single most general mdf, or mdfs is empty
     //inconsistent set of thrown things, which do not share a most
     //general modifier [list of line numbers of the throws]
     T1 = mdf1 resolve (p, T0).P //resolve can fail
     not catchRethrow(catch throw T1 x e)
     Phase| p| G[x:T1]|- e ~> e' : T2 <= T | Tr

   (catch and rethrow any)// could be sugared as "on throw doAndPropagate e"  
   Phase |p |G |Tr|-catch throw Any x (e0 throw x) ~> catch throw Any x (e0' throw x): T<=T | Tr
     where //Note: e0, e, e0',e' are using the sugar imm Void x=e == e
     e0=(e catch error Any z void void)
     e0'=(e' catch error Any z void void)
     Phase |p |G\x |- e ~> e':_ <=imm Void | empty
     catchRethrow(catch throw Any x(e0 throw x)) 
*/
  

default TOutKs ksType(TIn in1,TOut tsAcc,List<On> ks);

default TOut tsBlockPromotion(TIn in,Block s){throw Assertions.codeNotReachable();};
/*
(capsule promotion)//we are discussing if some blocks may not be promotable:
//for example blocks with empty ds and ks.
Phase |p |G |- (ds ks e)~>(ds' ks' e'):capsule P <=capsule P | Tr
  where
  Phase |p |toLent(G) |-(ds ks e)~>(ds' ks' e'):mut P <=mut P   | Tr
//this rule is now "deterministic" in the sense that if typing the block give us a capsule directly,
//this rule can not be applied, since we require mut P <=mut P in the premise.
*/


default TOut combine(TErr res,TErr promFail){throw Assertions.codeNotReachable();}

default boolean promotionMakesSense(TErr tErr){
    NormType expected=tErr.in.expected;
    NormType obtained=tErr._computed;
    if (!TypeSystem.subtype(tErr.in.p,obtained.getPath(), expected.getPath())){return false;}
    Mdf eM=expected.getMdf();
    Mdf oM=obtained.getMdf();
    return (eM==Mdf.Capsule || eM==Mdf.Immutable) && oM==Mdf.Mutable;
  }



}
