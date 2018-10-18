package newTypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ast.ErrorMessage;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import programReduction.Program;
import tools.Assertions;

public class Coherence
{
  public static String removeHash(String s)
  {
    if (s.startsWith("#")) return s.substring(1);
    else return s;
  }

  public ClassB top;
  public boolean check;
  public Program p;
  public Set<String> fieldNames;
  public long uniqueNum;
  public BinaryMultiMap<String, Mdf> fieldModifiers = new BinaryMultiMap<>();
  public BinaryMultiMap<String, Path> fieldPaths = new BinaryMultiMap<>();
  public BinaryMultiMap<String, Mdf> fieldAccessModifiers = new BinaryMultiMap<>();
  public boolean mutOrLCFactories = false;

  public List<MethodWithType> getters = new ArrayList<>();
  public List<MethodWithType> factories = new ArrayList<>();
  public List<MethodWithType> setters = new ArrayList<>();
  public List<MethodWithType> others = new ArrayList<>();

  public static Coherence coherence(Program p, boolean check) {
    Coherence c = new Coherence();
    c.p = p;
    c.top = p.top();
    c.check = check;
    c.run();
    return c;
  }

  void error(MethodWithType mwt, String message)
  {
      throw new ErrorMessage.NotOkToStar(this.top, mwt, message, mwt.getP());
  }

  void processFactory(MethodWithType ck)
  {
    boolean immOrC = ck.getReturnMdf().isIn(Mdf.Immutable, Mdf.Capsule);
    boolean lentOrR = ck.getReturnMdf().isIn(Mdf.Lent, Mdf.Readable);
    boolean mutOrLC = ck.getReturnMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule);
    this.mutOrLCFactories |= mutOrLC;

    for (int i = 0; i < ck.getSize(); i++)
    {
      String x = ck.getXs().get(i);
      Mdf mdfi = ck.getMdfs().get(i);
      Path Pi = ck.getPaths().get(i);

      this.fieldModifiers.add(mutOrLC, x, mdfi);
      this.fieldPaths.add(mutOrLC, x, Pi);

      if (!this.check) continue;

      //  lent not in mdf1..mdfn
      if (mdfi == Mdf.Lent)
          error(ck, "Factory cannot have a lent argument.");

      //  if read in {mdf1..mdfn) then mdf in {read, lent}
      if(mdfi == Mdf.Readable && !lentOrR)
          error(ck,"Only lent and read returning factories can have read paramaters.");

      //  if mdf in {imm, capsule} then {mdf1..mdfn} disjoint {mut, fwd mut}
      if (immOrC & mdfi.isIn(Mdf.Mutable, Mdf.MutableFwd))
          error(ck, "Imm and capsule factories cannot have mut or fwd mut parameters.");
    }

    if (!this.check) return;

    //  M=refine? class method mdf P m__n?(mdf1 P1 x1, ..., mdfn Pn xn) exception _
    if (ck.getMs().getUniqueNum() != this.uniqueNum)
        error(ck, "All abstract state operations must have the same unique number.");

    //  {x1, ..., xn} = xz
    if (!fieldNames.containsAll(ck.getMs().getNames()))
        error(ck, "Factories have different sets of paramater names.");

    // if untrustedClass(p.top()) then m is of form #$m
    if (p.top().isUntrusted() && !ck.getMs().isUntrusted())
        error(ck, "Untrusted classes can only have #$ constructors.");

    // p|-This <= P
    if (null != TypeSystem.subtype(p, Path.outer(0), ck.getReturnPath()))
        error(ck, "Factory must return a super-type of 'This'.");

    //  mdf not in {class, fwd mut, fwd imm}
    if (ck.getReturnMdf().isIn(Mdf.Class, Mdf.MutableFwd, Mdf.ImmutableFwd))
        error(ck, "Factory cannot return '" + ck.getReturnMdf() + "'");
  }

  void processSetter(MethodWithType mwt)
  {
    this.setters.add(mwt);

    String x = removeHash(mwt.getMs().getName());
    // M=refine? mdf method T #?x__n?(mdf' P' that) exception _
    fieldModifiers.add(true, x, mwt.getMdfs().get(0));

    if (!check) return;

    if (mwt.getMs().getUniqueNum() != uniqueNum)
      error(mwt, "All abstract state operations must have the same unique number.");
    if (!fieldNames.contains(x))
      error(mwt, "Setter for unknown field.");

    //p |- imm Void <= T
    if (null != TypeSystem._subtype(p, Type.immVoid, mwt.getReturnType()))
      error(mwt, "Setters must return a supertype of imm Void.");

    //mdf' in {imm, mut, capsule, class}//that is not in {read, lent, fwd mut, fwd imm}
    if (!mwt.getMdfs().get(0).isIn(Mdf.Immutable, Mdf.Mutable, Mdf.Capsule, Mdf.Class))
      error(mwt, "Setters cannot take a read, lent, fwd mut, or fwd imm.");

    //mdf in {lent, mut, capsule}
    if (!mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule))
      error(mwt, "Setters must be lent, mut, or capsule methods.");

    // if mdf = lent then mdf' != mut
    if (mwt.getMdf().equals(Mdf.Lent) && mwt.getMdfs().get(0).equals(Mdf.Mutable))
      error(mwt, "A lent setter cannot take a mut.");
  }
  void processGetter(MethodWithType mwt)
  {
    getters.add(mwt);

    // M=refine? mdf method T #?x__n?(mdf' P' that) exception _
    String x = removeHash(mwt.getMs().getName());

    if (!mwt.getMdf().equals(Mdf.Capsule))
      fieldAccessModifiers.add(mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Readable), x, mwt.getReturnMdf());

    if (!this.check) return;

    if (mwt.getMs().getUniqueNum() != uniqueNum)
      error(mwt, "All abstract state operations must have the same unique number.");
    if (!fieldNames.contains(x))
      error(mwt, "Getter for unknown field (" + x + ")");

    // mdf != class;
    assert !(mwt.getMdf().equals(Mdf.Class));
  }
  void checkGetter(MethodWithType mwt)
  {
    String x = removeHash(mwt.getMs().getName());

    Mdf mdf = mwt.getMdf();
    Mdf mdfR = mwt.getReturnMdf();
    boolean isLCM = mdf.isIn(Mdf.Lent, Mdf.Capsule, Mdf.Mutable);

    // forall P in FieldPath(p.top(), x, mdf), p |- P <= P'
    if (!fieldPaths.get(isLCM, x).stream().allMatch(P ->
        null == TypeSystem._subtype(p, mwt.getReturnType().withPath(P), mwt.getReturnType())))
      error(mwt, "Return class of getter must be a subclass of all possible field values' classes.");

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
          error(mwt, "Cannot get a possibly class field as imm.");
      }
      ////coherentGetMdf(imm,mdf,mdfs0,mdfs1)
      else
      {
        // mdfs0 subseteq {imm, fwd imm,capsule}
        if (!mdfs0.stream().allMatch(m -> m.isIn(Mdf.Immutable, Mdf.ImmutableFwd, Mdf.Capsule)))
          error(mwt, "Can only get imm if the value is guaranteed to be an imm, fwd imm, or capsule");
        // {mut, lent} disjoint mdfs1
        if (mdfs1.stream().anyMatch(m -> m.isIn(Mdf.Mutable, Mdf.Lent)))
          error(mwt, "Cannot return imm when field could have been leaked as mut or lent");
      }
    }
    //coherentGetMdf(read, _, mdfs0,_)
    else if (mdfR.equals(Mdf.Readable))
    {
      //  class not in mdfs0
      if (mdfs0.contains(Mdf.Class))
        error(mwt, "Cannot get a possibly class field as read.");
    }
    //coherentGetMdf(class,_,{class},_)
    else if (mdfR.equals(Mdf.Class))
    {
      if (!mdfs0.equals(Collections.singleton(Mdf.Class)))
        error(mwt, "Cannot get a possibly non-class field as class.");
    }
    //coherentGetMdf(capsule,capsule,mdfs0,mdfs1)
    else if (mdfR.equals(Mdf.Capsule))
    {
      if (!mdf.equals(Mdf.Capsule))
        error(mwt, "Can only return a field as capsule from a capsule method.");

      // mdfs0 subseteq {mut, fwd mut, capsule}
      if (!mutOrC)
        error(mwt, "Cannot get a field as capsule when it might not be mut, fwd mut, or capsule.");

      // imm not in mdfs1
      if (mdfs1.contains(Mdf.Immutable))
        error(mwt, "Cannot get a field as capsule when it may have been leaked as imm.");
    }
    //coherentGetMdf(capsule,capsule,mdfs0,mdfs1)
    else if (mdfR.equals(Mdf.Lent))
    {
      // mdfs0 subseteq {mut, fwd mut, capsule}
      if (!mutOrC)
        error(mwt, "Cannot get a field as capsule when it might not be mut, fwd mut, or capsule.");

      // mdf in {lent, mut, capsule}
      if (!mdf.isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule))
        error(mwt, "Can only expose a field as lent from a mut, lent or capsule method.");
    }
    //coherentGetMdf(mut,mdf,mdfs0,_)
    else if (mdfR.equals(Mdf.Mutable))
    {
      // mdfs0 subseteq {mut, fwd mut, capsule}
      if (!mutOrC)
        error(mwt, "Cannot get a field as mut when it might not be mut, fwd mut, or capsule.");

      // mdf in {mut, capsule}
      if (!mdf.isIn(Mdf.Mutable, Mdf.Capsule))
        error(mwt, "Can only expose a field as mut from a mut or capsule method.");
    }
    else Assertions.codeNotReachable();
  }

  void run()
  {
    if (top.isInterface()) return;

    Map<Boolean, List<MethodWithType>> abstracts=top.mwts().stream()
      .filter(m->m.get_inner()==null)
      .collect(Collectors.partitioningBy(m -> m.getMt().getMdf() == Mdf.Class));

    this.factories = abstracts.get(Boolean.TRUE);
    List<MethodWithType> instanceMethods = abstracts.get(Boolean.FALSE);

    if (this.factories.isEmpty()) return;

    // All factories need to have the same field names
    this.fieldNames = new HashSet<>(this.factories.get(0).getMs().getNames());
    this.uniqueNum = this.factories.get(0).getMs().getUniqueNum();

    // Check each factory
    for (MethodWithType ck : this.factories)
      processFactory(ck);

    for (MethodWithType mwt : instanceMethods)
    {
      /* coherent(p;M;_;_)
        M=refine? mdf method _
        mdf in {lent, mut, capsule}
        forall refine? class method mdf' _ in p.top(), mdf' not in {lent, mut, capsule}
      */
      if (!mutOrLCFactories && mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule))
        this.others.add(mwt);

      // A setter
      else if (mwt.getMs().getNames().equals(Collections.singletonList("that")))
        this.processSetter(mwt);

      // A getter, don't check it yet, wait until we've collected the fieldModifiers etc.
      else if (mwt.getMs().getNames().isEmpty())
        this.processGetter(mwt);

      else if (this.check)
        error(mwt, "Abstract method is neither a factory, getter, nor a setter.");
    }

    // Now we can check the getters, as we have collected enough information
    if (this.check) for (MethodWithType mwt : getters)
      this.checkGetter(mwt);
  }
}