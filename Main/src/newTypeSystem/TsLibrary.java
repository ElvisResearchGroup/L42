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
      ClassB top = p.top();
      if (top.isInterface()) return true;

      Map<Boolean, List<MethodWithType>> stateC=top.mwts().stream()
      .map(m->(MethodWithType)m)
      .filter(m->m.get_inner()==null)
      .collect(Collectors.partitioningBy(m -> m.getMt().getMdf() == Mdf.Class));

      List<MethodWithType> stateK = stateC.get(Boolean.TRUE);
      List<MethodWithType> stateF = stateC.get(Boolean.FALSE);

      if(stateK.isEmpty()) return true;

      // All factories need to have the same field names
      Set<String> fieldNames = new HashSet<>(stateK.get(0).getMs().getNames());
      long uniqueNum = stateK.get(0).getMs().getUniqueNum();

      BinaryMultiMap<String, Mdf> fieldModifiers = new BinaryMultiMap<>();
      BinaryMultiMap<String, Path> fieldPaths = new BinaryMultiMap<>();
      BinaryMultiMap<String, Mdf> fieldAccessModifiers = new BinaryMultiMap<>();

      boolean mutOrLCFactories = false;

      BiPredicate<MethodWithType, String> error = (mwt, message) -> {
        if (force) throw new ErrorMessage.NotOkToStar(top, mwt, message, mwt.getP());
        else return false;
      };

      // Check each factory
      for (MethodWithType ck : stateK)
      {
        //  M=refine? class method mdf P m__n?(mdf1 P1 x1, ..., mdfn Pn xn) exception _
        if (ck.getMs().getUniqueNum() != uniqueNum)
            return error.test(ck, "All abstract state operations must have the same unique number.");

        //  {x1, ..., xn} = xz
        if (!fieldNames.containsAll(ck.getMs().getNames()))
            return error.test(ck, "Factories have different sets of paramater names.");

        // if untrustedClass(p.top()) then m is of form #$m
        if (p.top().isUntrusted() && !ck.getMs().isUntrusted())
            return error.test(ck, "Untrusted classes can only have #$ constructors.");

        // p|-This <= P
        if (null != TypeSystem.subtype(p, Path.outer(0), ck.getReturnPath()))
            return error.test(ck, "Factory must return a super-type of 'This'.");

        //  mdf not in {class, fwd mut, fwd imm}
        if (ck.getReturnMdf().isIn(Mdf.Class, Mdf.MutableFwd, Mdf.ImmutableFwd))
            return error.test(ck, "Factory cannot return '" + ck.getReturnMdf() + "'");

        boolean immOrC = ck.getReturnMdf().isIn(Mdf.Immutable, Mdf.Capsule);
        boolean lentOrR = ck.getReturnMdf().isIn(Mdf.Lent, Mdf.Readable);
        boolean mutOrLC = ck.getReturnMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule);
        mutOrLCFactories |= mutOrLC;

        for (int i = 0; i < ck.getSize(); i++)
        {
          String x = ck.getXs().get(i);
          Mdf mdfi = ck.getMdfs().get(i);
          Path Pi = ck.getPaths().get(i);

          //  lent not in mdf1..mdfn
          if (mdfi == Mdf.Lent)
              return error.test(ck, "Factory cannot have a lent argument.");

          //  if read in {mdf1..mdfn) then mdf in {read, lent}
          if(mdfi == Mdf.Readable && !lentOrR)
              return error.test(ck,"Only lent and read returning factories can have read paramaters.");

          //  if mdf in {imm, capsule} then {mdf1..mdfn} disjoint {mut, fwd mut}
          if (immOrC & mdfi.isIn(Mdf.Mutable, Mdf.MutableFwd))
              return error.test(ck, "Imm and capsule factories cannot have mut or fwd mut parameters.");

          fieldModifiers.add(mutOrLC, x, mdfi);
          fieldPaths.add(mutOrLC, x, Pi);
        }
      }

      List<MethodWithType> getters = new ArrayList<>();

      for (MethodWithType mwt : stateF)
      {
        /* coherent(p;M;_;_)
          M=refine? mdf method _
          mdf in {lent, mut, capsule}
          forall refine? class method mdf' _ in p.top(), mdf' not in {lent, mut, capsule}
        */
        if (!mutOrLCFactories && mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule));

        // A setter
        else if (mwt.getMs().getNames().equals(Collections.singletonList("that")))
        {
          // M=refine? mdf method T #?x__n?(mdf' P' that) exception _
          String x = _removeHash(mwt.getMs().getName());
          if (mwt.getMs().getUniqueNum() != uniqueNum)
            return error.test(mwt, "All abstract state operations must have the same unique number.");
          if (!fieldNames.contains(x))
            return error.test(mwt, "Setter for unknown field.");

          //p |- imm Void <= T
          if (null != TypeSystem._subtype(p, mwt.getReturnType(), Type.immVoid))
            return error.test(mwt, "Setters must return a supertype of read Void.");

          //mdf' in {imm, mut, capsule, class}//that is not in {read, lent, fwd mut, fwd imm}
          if (!mwt.getMdfs().get(0).isIn(Mdf.Immutable, Mdf.Mutable, Mdf.Capsule, Mdf.Class))
            return error.test(mwt, "Setters cannot take a read, lent, fwd mut, or fwd imm.");

          //mdf in {lent, mut, capsule}
          if (!mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule))
            return error.test(mwt, "Setters must be lent, mut, or capsule methods.");

          // if mdf = lent then mdf' != mut
          if (mwt.getMdf().equals(Mdf.Lent) && mwt.getMdfs().get(0).equals(Mdf.Mutable))
            return error.test(mwt, "A lent setter cannot take a mut.");

          fieldModifiers.add(true, x, mwt.getMdfs().get(0));
        }

        // A getter, don't fully check it yet, wait until we've collected the fieldModifiers etc.
        else if (mwt.getMs().getNames().isEmpty())
        {
          // M=refine? mdf method T #?x__n?(mdf' P' that) exception _
          String x = _removeHash(mwt.getMs().getName());
          if (mwt.getMs().getUniqueNum() != uniqueNum)
            return error.test(mwt, "All abstract state operations must have the same unique number.");
          if (!fieldNames.contains(x))
            return error.test(mwt, "Getter for unknown field.");

          // mdf != class;
          assert !(mwt.getMdf().equals(Mdf.Class));

          getters.add(mwt);

          if (!mwt.getMdf().equals(Mdf.Capsule))
            fieldAccessModifiers.add(mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Readable), x, mwt.getReturnMdf());
        }

        else return error.test(mwt, "Abstract method is neither a factory, getter, nor a setter.");
      }


      // Now we can check the getters, as we have collected enough information
      for (MethodWithType mwt : getters)
      {
        String x = _removeHash(mwt.getMs().getName());
        Mdf mdf = mwt.getMdf();
        Mdf mdfR = mwt.getReturnMdf();
        boolean isLCM = mdf.isIn(Mdf.Lent, Mdf.Capsule, Mdf.Mutable);

        // forall P in FieldPath(p.top(), x, mdf), p |- P <= P'
        if (!fieldPaths.get(isLCM, x).stream().allMatch(P ->
            null == TypeSystem._subtype(p, mwt.getReturnType().withPath(P), mwt.getReturnType())))
          return error.test(mwt, "Return class of getter must be a subclass of all possible field values' classes.");

        // coherentGetMdf(mdf',mdf,FieldMdf(p.top(),x, mdf),FieldAccessMdf(p.top(),x, mdf))

        Set<Mdf> mdfs0 = fieldModifiers.get(isLCM, x);
        Set<Mdf> mdfs1 = fieldAccessModifiers.get(isLCM, x);

        //mdfs0 subseteq {mut, fwd mut, capsule}
        boolean mutOrC = mdfs0.stream().allMatch(m -> m.isIn(Mdf.Mutable, Mdf.MutableFwd, Mdf.Capsule));

        // coherentGetMdf(imm, ...)
        if (mdfR.equals(Mdf.Immutable))
        {
          // coherentGetMdf(imm,imm, mdfs0,_)
          if (mdf.equals(Mdf.Immutable))
          {
            //  class not in mdfs0
            if (mdfs0.contains(Mdf.Class))
              return error.test(mwt, "Cannot get a possibly class field as imm.");
          }
          ////coherentGetMdf(imm,mdf,mdfs0,mdfs1)
          else
          {
            // mdfs0 subseteq {imm, fwd imm,capsule}
            if (!mdfs0.stream().allMatch(m -> m.isIn(Mdf.Immutable, Mdf.ImmutableFwd, Mdf.Capsule)))
              return error.test(mwt, "Can only get imm if the value is guaranteed to be an imm, fwd imm, or capsule");
            // {mut, lent} disjoint mdfs1
            if (mdfs1.stream().anyMatch(m -> m.isIn(Mdf.Mutable, Mdf.Lent)))
              return error.test(mwt, "Cannot return imm when field could have been leaked as mut or lent");
          }
        }
        //coherentGetMdf(read, _, mdfs0,_)
        else if (mdfR.equals(Mdf.Readable))
        {
          //  class not in mdfs0
          if (mdfs0.contains(Mdf.Class))
            return error.test(mwt, "Cannot get a possibly class field as read.");
        }
        //coherentGetMdf(class,_,{class},_)
        else if (mdfR.equals(Mdf.Class))
        {
          if (!mdfs0.equals(Collections.singleton(Mdf.Class)))
            return error.test(mwt, "Cannot get a possibly non-class field as class.");
        }
        //coherentGetMdf(capsule,capsule,mdfs0,mdfs1)
        else if (mdfR.equals(Mdf.Capsule))
        {
          if (!mdf.equals(Mdf.Capsule))
            return error.test(mwt, "Can only return a field as capsule from a capsule method.");

          // mdfs0 subseteq {mut, fwd mut, capsule}
          if (!mutOrC)
            return error.test(mwt, "Cannot get a field as capsule when it might not be mut, fwd mut, or capsule.");

          // imm not in mdfs1
          if (mdfs1.contains(Mdf.Immutable))
            return error.test(mwt, "Cannot get a field as capsule when it may have been leaked as imm.");
        }
        //coherentGetMdf(capsule,capsule,mdfs0,mdfs1)
        else if (mdfR.equals(Mdf.Lent))
        {
          // mdfs0 subseteq {mut, fwd mut, capsule}
          if (!mutOrC)
            return error.test(mwt, "Cannot get a field as capsule when it might not be mut, fwd mut, or capsule.");

          // mdf in {lent, mut, capsule}
          if (!mdf.isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule))
            return error.test(mwt, "Can only expose a field as lent from a mut, lent or capsule method.");
        }
        //coherentGetMdf(mut,mdf,mdfs0,_)
        else if (mdfR.equals(Mdf.Mutable))
        {
          // mdfs0 subseteq {mut, fwd mut, capsule}
          if (!mutOrC)
            return error.test(mwt, "Cannot get a field as mut when it might not be mut, fwd mut, or capsule.");

          // mdf in {mut, capsule}
          if (!mdf.isIn(Mdf.Mutable, Mdf.Capsule))
            return error.test(mwt, "Can only expose a field as mut from a mut or capsule method.");
        }
        else Assertions.codeNotReachable();
      }
      return true;
    }

    static String _removeHash(String s) {
      if (s.startsWith("#")) return s.substring(1);
      else return s;
    }
}

class BinaryMultiMap<K, V> {
  private Map<K, Set<V>> true_ = new HashMap<>();
  private Map<K, Set<V>> false_ = new HashMap<>();

  void add(boolean b, K key, V value) {
    this.addOnce(false, key, value);
    if (b) this.addOnce(b, key, value);
  }

  void addOnce(boolean b, K key, V value) {
    this.get(b).merge(key, Collections.singleton(value), (x, y) -> {
        Set<V> s = new HashSet<V>(x);
        s.addAll(y);
        return s;
    });
  }

  Map<K, Set<V>> get(boolean b)
  {
    if (b) return this.true_;
    else return this.false_;
  }
  Set<V> get(boolean b, K key) { return this.get(b).get(key); }
}