package newTypeSystem;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;
import coreVisitors.From;
import facade.L42;
import org.antlr.v4.runtime.misc.MultiMap;
import programReduction.Norm;
import programReduction.Program;
import tools.Assertions;
import ast.Ast.Mdf;
import tools.LambdaExceptionUtil;

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
    return normP;
    }

  default TOut libraryWellTyped(TIn in) {
//   (library well typed)
//   Phase |- p ~> L' //In implementation, if p.top().Phase>=Phase, L'=p.Top()
     ClassB top=in.p.top();
     assert in.phase.subtypeEq(Phase.Typed)://   Phase in {Typed,Coherent}
       "";
     if(L42.trustLibraryTypedStage && top.getPhase().subtypeEq(in.phase)){
       return new TOk(in,top,Path.Library().toImmNT());
       }
//   L0={interface? implements Ps M1..Mn Phase'}=norm(p)
//   L'={interface? implements Ps M1'..Mn' max(Phase',Phase)}
     ClassB L0 = normTopL(in);
     List<MethodWithType> mwts = L0.mwts();
     List<NestedClass> ns = L0.ns();
     List<NestedClass> newNs = new ArrayList<>();
     //   forall i in 1..n
//     Phase| p| Ps |- Mi ~> Mi'
     TIn inNested=in.withP(in.p.updateTop(L0));

     List<MethodWithType> newMwts;
     if(in.phase==Phase.Coherent && top.getPhase()==Phase.Typed){
       newMwts=new ArrayList<>(mwts);
       }
     else {
       newMwts = new ArrayList<>();
       for(MethodWithType mwt:mwts){
         TOutM out=memberMethod(inNested,L0.getSupertypes(),mwt);
         if(!out.isOk()){return out.toError();}
         newMwts.add((MethodWithType)out.toOkM().inner);
         }
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
       boolean isCoh=coherent(in.p.updateTop(L1),true);
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
    assert in.isTrusted; // The containing expression should be a meta-expression, which is always trusted
    MethodWithType mwt1;
    if(mwt.get_inner()==null){mwt1=mwt;}
    else{
      TIn newIn=TIn.freshGFromMt(in.p,mwt);
      TOut out=type(newIn);
      if(!out.isOk()){return out.toError();}
      for(Path P1: out.toOk().exceptions){
        //exists P0 in Ps0 such that p|-P1<=P0
        boolean ok=mwt.getMt().getExceptions().stream().anyMatch(
          P0->null==TypeSystem.subtype(newIn.p, P1, P0.getPath()));
        if(!ok){
          return new TErr(newIn,"In method "+mwt.getMs()+", "+P1.toImmNT()+" not subtype of any of "+mwt.getMt().getExceptions(),P1.toImmNT(),ErrorKind.MethodLeaksExceptions);
          }
        }
      if(!out.toOk().returns.isEmpty()){
        return new TErr(newIn,"",out.toOk().returns.get(0),ErrorKind.MethodLeaksReturns);
        }
      mwt1=mwt.with_inner(out.toOk().annotated);
      }
    if(!mwt.getMt().isRefine()){
      for(Type pi:supertypes) {
        ClassB li=in.p.extractClassB(pi.getPath());
        if(li._getMember(mwt.getMs())!=null) {
          return new TErr(in,"Method "+mwt.getMs()+" do not refine method from "+pi.getPath(),pi.getPath().toImmNT(),ErrorKind.InvalidImplements);
          }
        }
      }
    else{
      for(Type t :supertypes){
        Path P=t.getPath();
        ClassB cbP=in.p.extractClassB(P);
        MethodWithType mwtP=(MethodWithType) cbP._getMember(mwt.getMs());
        if(mwtP==null){continue;}
        MethodType M0=From.from(mwtP.getMt(),P);
        ErrorKind kind=TypeSystem._subtype(in.p, mwt.getMt().getReturnType(),M0.getReturnType());
        if(kind!=null){
          return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
          }
        {int i=-1;for(Type Ti:mwt.getMt().getTs()){i+=1; Type T1i=M0.getTs().get(i);
          if(!in.p.equiv(Ti,T1i)){
          return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
          }
        }}
        for(Type Pi: mwt.getMt().getExceptions()){
          //exists Pj in Ps' such that p |- Pi<=Pj
          boolean ok=M0.getExceptions().stream().anyMatch(
                    Pj->null==TypeSystem.subtype(in.p, Pi.getPath(), Pj.getPath()));
          if(!ok){
            return new TErr(in,"",P.toImmNT(),ErrorKind.InvalidImplements);
            }
          }
        }
      }
    return new TOkM(mwt1);
    }

  static boolean coherent(Program p, boolean force) {
    try
    {
      Coherence.coherence(p, true);
    }
    catch (ErrorMessage.NotOkToStar e)
    {
      if (force) throw e; // Don't ignore it
      return false;
    }
    return true;
  }
}