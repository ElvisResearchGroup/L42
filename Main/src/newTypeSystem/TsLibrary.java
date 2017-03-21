package newTypeSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;
import coreVisitors.From;
import programReduction.Norm;
import programReduction.Program;
import tools.Assertions;
import ast.Ast.Mdf;

public interface TsLibrary extends TypeSystem{
  
  @Override default TOut typeLib(TIn in) {
    assert in.phase!=Phase.None;
    if(in.phase==Phase.Norm){return libraryShallowNorm(in);}
    return libraryWellTyped(in);
    }
    
  default TOut libraryShallowNorm(TIn in) {
    //(library shallow norm)
    //Norm  |- p ~> norm(p)  //remember: norm ignores meth bodies
    ////assert forall P in norm(p).Ps p(P).Phase>=Norm
    ClassB normP = normTopL(in);
    return new TOk(in,normP,Path.Library().toImmNT());
    }

  default ClassB normTopL(TIn in) throws Error {
    ClassB normP;
    try{normP=new Norm().norm(in.p);}
    catch(RuntimeException exc){
      throw exc;
      //exceptions from normalization all represents unsolvable
      //type errors. Same for cyclic resolve
    }
    assert normP.getSupertypes().stream().allMatch(
      t->{
        Phase phase=in.p.extractClassB(t.getNT().getPath()).getPhase();
        assert phase!=Phase.None:
          t.getNT().getPath();
        return phase!=Phase.None;}):
      "";
    return normP;
    }
    
  default TOut libraryWellTyped(TIn in) {
//   (library well typed)
//   Phase |- p ~> L' //In implementation, if p.top().Phase>=Phase, L'=p.Top()
     ClassB top=in.p.top();
     assert in.phase.subtypeEq(Phase.Typed)://   Phase in {Typed,Coherent}
       "";
     if(top.getPhase().subtypeEq(in.phase)){ 
       return new TOk(in,top,Path.Library().toImmNT());
       }
//   L0={interface? implements Ps M1..Mn Phase'}=norm(p)
//   L'={interface? implements Ps M1'..Mn' max(Phase',Phase)}
     ClassB L0 = normTopL(in);
     List<MethodWithType> mwts = L0.mwts();
     List<MethodWithType> newMwts = new ArrayList<>();
     List<NestedClass> ns = L0.ns();
     List<NestedClass> newNs = new ArrayList<>();
     //   forall i in 1..n
//     Phase| p| Ps |- Mi ~> Mi'
     TIn inNested=in.withP(in.p.updateTop(L0));
     for(MethodWithType mwt:mwts){
       TOutM out=memberMethod(inNested,L0.getSupertypes(),mwt);
       if(!out.isOk()){return out.toError();}
       newMwts.add((MethodWithType)out.toOkM().inner);
       }
     for(NestedClass nt:ns){
       TOutM out=memberNested(inNested,nt);
       if(!out.isOk()){return out.toError();}
       newNs.add((NestedClass)out.toOkM().inner);
     }
     Phase maxPhase=L0.getPhase();
     if(in.phase.subtypeEq(maxPhase)){maxPhase=in.phase;}
     ClassB L1=new ClassB(L0.getDoc1(),L0.isInterface(),L0.getSupertypes(),newMwts,newNs,L0.getP(),maxPhase,L0.getUniqueId());
     if(in.phase==Phase.Coherent){
       boolean isCoh=coherent(in.p.updateTop(L1));
       if(!isCoh){
         return new TErr(in,"",Path.Library().toImmNT(),ErrorKind.LibraryNotCoherent);
         }
       }
//   if Phase=Coherent then coherent(p.pop().evilPush(L'))
//   //or error not coherent set of abstr. methods:list
    return new TOk(in,L1,Path.Library().toImmNT());
    }

  default TOutM memberNested(newTypeSystem.TIn in, NestedClass nc) {
    //(member nested)
    //Phase| p| Ps |-C:L ~>  C:L'
    //   where
    //   Phase |-p.push(C) ~> L'    return null;
    Program p1=in.p.push(nc.getName());
    TOut res=typeLib(in.withP(p1));
    if(!res.isOk()){return res.toError();}
    return new TOkM(nc.withInner(res.toOk().annotated));
    }

  default TOutM memberMethod(TIn in, List<Type> supertypes, MethodWithType mwt) {
//(member method)
//Phase| p| Ps |-M ~> M'
//  where
//  M =refine? mdf method T m(T1 x1 .. Tn xn)exceptions Ps0 e?
//  M'=refine? mdf method T m(T1 x1 .. Tn xn)exceptions Ps0 e?'
//  G=this:mdf This0,x1:T1,..,xn:Tn
//  if e?=e then
//  Typed| p| G |- e ~>  e?':_ <=fwd% T | empty;Ps1
//  forall P1 in Ps1 exists P0 in Ps0 such that p|-P1<=P0
//else
//  e?=e?'
    MethodWithType mwt1;
    if(!mwt.get_inner().isPresent()){
      mwt1=mwt;
    }
    else{
      TIn newIn=in.freshGFromMt(mwt);
      TOut out=type(newIn);
      if(!out.isOk()){return out.toError();}
      for(Path P1: out.toOk().exceptions){
        //exists P0 in Ps0 such that p|-P1<=P0
        boolean ok=mwt.getMt().getExceptions().stream().anyMatch(
          P0->null==TypeSystem.subtype(newIn.p, P1, P0.getNT().getPath()));
        if(!ok){return new TErr(newIn,"",P1.toImmNT(),ErrorKind.MethodLeaksExceptions);}
        }
      if(!out.toOk().returns.isEmpty()){
        return new TErr(newIn,"",out.toOk().returns.get(0),ErrorKind.MethodLeaksReturns);
        }
      mwt1=mwt.with_inner(Optional.of(out.toOk().annotated));
    }
    if(mwt.getMt().isRefine()){
//  refine? = refine <=> 
//    forall P in Ps such that p(P)(m(x1..xn))[from P]=M0 //that is, is defined
//      all of the following hold:
//      M0=refine?' mdf method T' m(T'1 x1..T'n xn)exceptions Ps'
//      p|-T<= T' //method returns a type which is not a sybtype of its ancestor "name" or worst, ancestor do not define method named m(xs)
//      p.equiv(T1,T'1)..p.equiv(Tn,T'n) //invalid type w.r.t. ancestor paramerer xi   
//      forall Pi in Ps0 exists Pj in Ps' such that p |- Pi<=Pj
//      //or error: leaked exception P is not the subtype of a declared exception
//      /or  method declares an exception (P) which is not a subtype of ancestor exceptions 
      for(Type t :supertypes){
        Path P=t.getNT().getPath();
        ClassB cbP=in.p.extractClassB(P);
        MethodWithType mwtP=(MethodWithType) cbP._getMember(mwt.getMs());
        if(mwtP==null){continue;}
        MethodType M0=From.from(mwtP.getMt(),P);
        ErrorKind kind=TypeSystem.subtype(in.p, mwt.getMt().getReturnType().getNT(),M0.getReturnType().getNT());
        if(kind!=null){
          return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
          }
        {int i=-1;for(Type Ti:mwt.getMt().getTs()){i+=1; Type T1i=M0.getTs().get(i);
          if(!in.p.equiv(Ti.getNT(),T1i.getNT())){
          return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
          }
        }}
        for(Type Pi: mwt.getMt().getExceptions()){
          //exists Pj in Ps' such that p |- Pi<=Pj
          boolean ok=M0.getExceptions().stream().anyMatch(
                    Pj->null==TypeSystem.subtype(in.p, Pi.getNT().getPath(), Pj.getNT().getPath()));
          if(!ok){
          return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
            }
          }
        }
      }
    return new TOkM(mwt1);
    }

    
  default boolean coherent(Program p) {
      ClassB top=p.top();
      if (top.isInterface()){return true;}
      List<MethodWithType> stateC=top.mwts().stream()
      .map(m->(MethodWithType)m)
      .filter(m->!m.get_inner().isPresent())
      .sorted((m1,m2)->m1.getMt().getMdf()==Mdf.Class?-1:1)
      .collect(Collectors.toList());
      if(stateC.isEmpty()){return true;}
      MethodWithType ck=stateC.get(0);
      if(!coherentK(p,ck)){return false;}
      for(MethodWithType mwt:stateC.subList(1,stateC.size())){
        if(!coherentF(p,ck,mwt)){return false;}
      }
      return true;
    }
//coherent(p) //interfaces are always coherent
//  where
//  p.top()={interface implements _ mwts ncs}
    
//coherent(p)  //classes are coherent if they have a coherent set of abstract methods
//  where
//  p.top()={implements _ mwts' ncs} //note, no interface
//  mwts={mwt in mwts'| mwt.e undefined } //collect the abstract methods
//  either mwts is empty or
//    there is exactly 1 class method, and (after removing fwds) have //may make more formal this line
//    (T x)s parameters and n? such that 
//      all T in (T x)s are mut, imm, class or capsule //thus, no read/lent
//      forall mwt in mwts coherent(n?,p,(T x)s, mwt) //all abstract methods are coherent according to those fields

//coherent(n?,p,T1 x1..Tn xn,
//    refine? mdf method T m[n?]() exception _)
//  where
//  m=#?xi
//  either
//    mdf=mut and p|-capsuleToLent(Ti)<=T //exposer
//  or 
//    mdf=read and p|-toRead(Ti)<=T //getter //note for James, toRead need to keep imm as imm, toRead code reverted again :)
  
//coherent(n?,p,T1 x1..Tn xn,
//    refine? mut method Void m[n?](T that) exception _)
//  where
//  m=#?xi
//  p|-T<=Ti//setter


  default boolean coherentF(Program p,MethodWithType ck, MethodWithType mwt) {
      MethodType mt=mwt.getMt();
      Mdf m=mt.getMdf();
      if(mwt.getMs().getUniqueNum()!=ck.getMs().getUniqueNum()){return false;}
      NormType Ti=_extractTi(ck,mwt.getMs().getName());// internally do noFwd
      if (Ti==null){return false;}
      //if(m==Mdf.Class){return false;}
      if(m==Mdf.Readable || m==Mdf.Immutable){//getter
        if(!mt.getTs().isEmpty()){return false;}
        NormType Ti_=TypeManipulation._toRead(Ti);
        if (Ti_==null){return false;}//p|-toRead(Ti)<=T
        if(null!=TypeSystem.subtype(p, Ti_,mt.getReturnType().getNT())){return false;}
        return true;
        }
      if(m!=Mdf.Mutable){return false;}
      //exposer/setter
      if(mt.getTs().isEmpty()){//exposer
        NormType Ti_=TypeManipulation.capsuleToLent(Ti);
        if (Ti_==null){return false;}//p|-capsuleToLent(Ti)<=T
        if(null!=TypeSystem.subtype(p, Ti_,mt.getReturnType().getNT())){return false;}
        return true;
        }
      //setter refine? mut method Void m[n?](T that)
      if(!mt.getReturnType().equals(NormType.immVoid)){return false;}
      if(mt.getTs().size()!=1){return false;}
      if(!mwt.getMs().getNames().get(0).equals("that")){return false;}
      if(null!=TypeSystem.subtype(p, Ti,mt.getTs().get(0).getNT())){return false;}
      return true;
    }

  default NormType _extractTi(MethodWithType ck, String name) {
    if(name.startsWith("#")){name=name.substring(1);}
    int i=-1;for(String ni:ck.getMs().getNames()){i+=1;
      if (ni.equals(name)){return TypeManipulation.noFwd(ck.getMt().getTs().get(i).getNT());} 
      }
    return null;
    }

  
//coherent(n?,p,T1 x1..Tn xn,
//refine? class method T m[n?] (T1' x1..Tn' xn) exception _)
//where
//p|- This0 <=T.P and p|-Ti'<=fwd Ti
//T.mdf!=class and if T.mdf in {imm,capsule}, mut notin (T1..Tn).mdfs

    default boolean coherentK(Program p,MethodWithType ck) {
      MethodType mt=ck.getMt();
      if(mt.getMdf()!=Mdf.Class){return false;}
      NormType rt=mt.getReturnType().getNT();
      if(null!=TypeSystem.subtype(p, Path.outer(0),rt.getPath())){return false;}
      Mdf m=rt.getMdf();
      if (m==Mdf.Class){return false;}
      boolean immOrC=(m==Mdf.Immutable || m==Mdf.Capsule);
      for(Type ti:mt.getTs()){
        Mdf mi=ti.getNT().getMdf();
        if(mi==Mdf.Readable || mi==Mdf.Lent){return false;}
        if(immOrC & (mi==Mdf.Mutable || mi==Mdf.MutableFwd)){return false;}
        }
      return true;
    }
}