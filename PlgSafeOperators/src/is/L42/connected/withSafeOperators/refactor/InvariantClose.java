package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.stream.Collectors;

import ast.ExpCore;
import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.MCall;
import ast.ExpCore.X;
import ast.PathAux;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import auxiliaryGrammar.Functions;
import coreVisitors.CloneVisitor;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.PropagatorVisitor;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.Coherence;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.StaticDispatch;
import newTypeSystem.TsLibrary;
import newTypeSystem.TypeManipulation;
import programReduction.Methods;
import programReduction.Program;
import tools.LambdaExceptionUtil;


/**
ignore(p, mdf) iff
    mdf in {lent, mut, capsule}
    for all refine? class method mdf' _ in p.top()
        mdf' is not in {lent, mut, capsule}

exposer(p, M)
    M=refine? mdf method  mdf' P' #?x() exception Ps
    validatable(p, x)
    mdf' in {mut, lent}
    !ignore(p, mdf)

IC(p) = {interface? implements Ps Mz'}
                    coherent(p)
    p.top() = {interface? implements Ps M1..Mn}
    read method Bool #invariant() e in M1..Mn
    Mz' = IC(p; M1; k),..,IC(p; Mn; k) //k is a fresh private number
                    exists class method _ in M1...Nn

    // TODO: copy #invariant and all methods it transativley calls on this to private version
    // TODO: check the above methods only use this to call the getter of a validatable field
    // TODO: redirect calls to exposers to the private version
    // TODO: check methods that call exposers are proper 'capsule mutators'

IC(p; M; n) = {M[with e?=e], M[with m=m__n]}
  M =refine? class method mdf P m(mdf1 P1 x1, ..., mdfn Pn xn) exception Ps
  e = (x' = This.m__n(x1:x1, ..., xn:xn) x'.#invariant__n() x') //x' is fresh
  {mdf1, ..., mdfn} disjoint {fwd mut, fwd imm}

IC(p; M; n) = {M[with e?=e], M[with m=#?x__n]} // A setter
    M =refine? mdf method T #?x(mdf' P' that) exception Ps
    e = (x'=this.#?x__n(that:that) this.#invariant() x') //x' is fresh
    validatable(p, x)
    !ignore(p, mdf)

IC(p; M; n) = {M[with e?=e], M[with m=#?x__n]} // A getter, that is not an exposer
    M=refine? mdf method mdf' P' #?x() exception Ps
    e = this.#?x__n()
    !exposer(p, M)
    !ignore(p, mdf)

IC(p; M; n) = {M[with m=#?x__n]} // An exposer, make it private
    M=refine? mdf method  mdf' P' #?x() exception Ps
    exposer(p, M)
                    !ignore(p, mdf)

IC(p; M; n) = {M}
                    M.e? != empty or ignore(p, mdf)


  */

public class InvariantClose {
  static final int MODE_L42 = 0;
  static final int MODE_D = 1;
  static final int MODE_EIFFEL = 2;

  static MethodSelector invName = MethodSelector.of("#invariant", Collections.emptyList());
  public static ClassB closeJ(PData p, List<Ast.C> path, ClassB top, int mode)
    throws PathUnfit, ClassUnfit {
    return close(p.p, path, top, mode);
  }

  // path is a path inside top
  public static ClassB close(Program p, List<Ast.C> path, ClassB top, int mode)
      throws PathUnfit, ClassUnfit {

    if (!MembersUtils.isPathDefined(top, path)) throw new RefactorErrors.PathUnfit(path);
    if (MembersUtils.isPrivate(path)) throw new RefactorErrors.PathUnfit(path);

    InvariantClose c = new InvariantClose();
    c.mode = mode;
    c.pInner = p.evilPush(top).navigate(path);
    c.coherence = Coherence.coherence(c.pInner, true);
    c.inner = c.pInner.top();
    c.p = p;
    c.path = path;
    c.top = top;
    return c.run();
  }

  Coherence coherence;
  List<Ast.C> path;
  Program p;
  ClassB top; // The class that we were given
  int mode = MODE_L42;

  Program pInner;
  ClassB inner; // The class were closing
  long uniqueNum;
  Set<String> validatableFields = new HashSet<>();
  Set<String> otherFields = new HashSet<>();
  Set<MethodSelector> exposers = new HashSet<>();
  Set<MethodSelector> mutExposers = new HashSet<>();
  List<Member> newMembers = new ArrayList<>();
  Set<MethodSelector> state = new HashSet<>();

  public ClassB run() throws ClassUnfit {
    // Check for 'refine? read method imm Void #invariant() throws() e'
    {
      MethodWithType inv = this.getMwt(invName);

      MethodType invMt = new MethodType(inv.getMt().isRefine(), Mdf.Readable, Collections.emptyList(),
              Type.immVoid, Collections.emptyList());

      if (!inv.getMt().equals(invMt))
        throw new ClassUnfit().msg("selector #invariant() into " + PathAux.as42Path(path) + " has invalid type " + inv.getMt());

      if (inv.getInner() == null)
        throw new ClassUnfit().msg("selector #invariant() into " + PathAux.as42Path(path) + " has no body");
    }

    // The unique number to use to generate private things
    this.uniqueNum = L42.freshPrivate();

    // Collect all the fields that can, and cannot be referenced in invariant
    for (String name : this.coherence.fieldNames)
      if (this.coherence.fieldModifiers.get(false, name).stream()
              .allMatch(m -> m.isIn(Mdf.Immutable, Mdf.Capsule, Mdf.Class)))
          this.validatableFields.add(name);
      else
        this.otherFields.add(name);

    {
      List<MethodSelector> todo = new LinkedList<>();
      Set<String> actuallyValidatedFields = new HashSet<>();
      Set<MethodSelector>  done = new HashSet<>(); // The methods called by #invariant

      todo.add(invName);
      while (!todo.isEmpty()) {
        MethodSelector m = todo.remove(0);

        if (done.contains(m)) // We've already processed this
          continue;

        done.add(m);
        // Collect all other-called methods
        // check that 'this' is only used to call methods or validatable fields
        // and finally, create a private-version, where all non-field accessers are replaced with private numbered
        // versions

        MethodWithType newMwt = new InvariantChecker(done, todo, actuallyValidatedFields).visit(this.getMwt(m));

        if (this.mode == MODE_EIFFEL) {
          // Don't do anything for eiffel, the private versions will be made by delegateState
          continue;
        }

        this.addMember(newMwt);
      }
      this.validatableFields = actuallyValidatedFields;
    }

    for (MethodWithType mwt : this.inner.mwts()) {
      if (mwt.get_inner() != null || (!this.coherence.mutOrLCFactories && mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule)))
        continue;

      if (mwt.getMdfs().stream().anyMatch(m -> m.isFwdImm() || m.isFwdMut())) {
        assert mwt.getMdf().isClass();
        throw new ClassUnfit().msg("The factory " + mwt.getMs() + " at " + mwt.getP() + " takes fwd paramaters");
      }

      this.state.add(mwt.getMs());

      if (mwt.getMs().getNames().isEmpty() && this.validatableFields.contains(Coherence.removeHash(mwt.getMs().getName()))
            && mwt.getReturnMdf().isIn(Mdf.Mutable, Mdf.Lent)) {
        if (mwt.getReturnMdf().equals(Mdf.Mutable)) {
          this.mutExposers.add(mwt.getMs());
        }
        this.exposers.add(mwt.getMs());
      }
    }

    if (this.mode == MODE_L42)
      // Play with capsule mutators...
      this.top = (ClassB)this.top.accept(new WrapAux(this.p, this.makeRenames(this.exposers), this.top));
    else if (this.mode == MODE_D)
      // Redirect everything to use private getters/setters (so we don't call the public version, which will check the invariant)
      this.top = (ClassB)this.top.accept(new RenameMethodsAux(this.p, this.makeRenames(this.state), this.top) {
        // Don't touch method headers
        @Override protected MethodSelector liftMsInMetDec(MethodSelector ms) { return ms; }
      });
    else {
      assert this.mode == MODE_EIFFEL;
      // Redirect all calls on 'this' to private versions (delegateState will handle actually making the private versions)
      this.top = new Privatiser().of(this.top);
    }

    this.inner = this.top.getClassB(this.path);
    this.delegateState();

    // Don't play with the nested classes
    this.newMembers.addAll(this.inner.ns());
    this.inner = this.inner.withMs(this.newMembers);

    if (this.path.isEmpty()) {
      // Trivial...
      return this.inner;
    } else {
      // Navigate to the appropriate place, and put res there
      return this.top.onClassNavigateToPathAndDo(this.path, l -> this.inner);
    }
  }

  List<CsMxMx> makeRenames(Set<MethodSelector> mss) {
    List<CsMxMx> renames = new ArrayList<>();
    for (MethodSelector ms : mss )
      renames.add(new CsMxMx(this.path, false, ms, ms.withUniqueNum(this.uniqueNum)));

    return renames;
  }

  MethodWithType getMwt(MethodSelector ms) throws ClassUnfit {
    MethodWithType res = (MethodWithType)this.inner._getMember(ms);
    if (res == null)
      throw new ClassUnfit().msg("Error method " + ms + " of the class at '" + this.inner.getP() + "' does not exist");
    else return res;
  }
  private void delegateState() {
    for (MethodWithType mwt : inner.mwts()) {
      MethodType mt = mwt.getMt();
      // The mwt we (might) delegate to
      MethodWithType newMwt = mwt.withMs(mwt.getMs().withUniqueNum(this.uniqueNum)).withMt(mwt.getMt().withRefine(false));
      // Not a state method

      if (this.mode == MODE_D && !mwt.getMs().isUnique() && mt.getMdf() != Mdf.Class) {
        // Wrap the body up, but only if a public instance method
        this.delegate(true, mwt, newMwt);
      } else if (this.mode == MODE_EIFFEL && !mt.getMdf().isClass()) {
        // For all instance methods (even private ones) do an invariant check
        this.delegate(true, mwt, newMwt);
      } else if (this.mode == MODE_L42 && this.exposers.contains(mwt.getMs())) {
        // Do nothing, we've already handled it
      } else if (this.state.contains(mwt.getMs())) {
        assert this.mode == MODE_L42;

        // Call the invariant for factories, and setters of validatable fields
        if (mwt.getMdf().equals(Mdf.Class) || (!mwt.getMs().getNames().isEmpty()
              && this.validatableFields.contains(Coherence.removeHash(mwt.getMs().getName()))))
          this.delegate(true, mwt, newMwt);

        else this.delegate(false, mwt, newMwt);
      } else {
        this.addMember(mwt); // keep the method as is
      }
    }
  }

  void addMember(Member member) {
    boolean res = this.tryAddMember(member);
    assert res : member;
  }
  boolean tryAddMember(Member member) {
      if (Functions.getIfInDom(this.newMembers, member).isPresent())
        return false;

      this.newMembers.add(member);
      return true;
  }

  void delegate(boolean callInvariant, MethodWithType original, MethodWithType delegate) {
    assert original.getMs().nameSize() == delegate.getMs().nameSize();
    Position p = original.getP();

    // delegateMCall = 'this.delagate(x1, ..., xn)
    // where x1, ..., xn are the paramater names of 'original'
    ExpCore.MCall delegateMCall = new ExpCore.MCall(
      new ExpCore.X(p, "this"), // receiver
      delegate.getMs(), // callee
      Doc.empty(),
      tools.Map.of(s -> new ExpCore.X(p, s), original.getMs().getNames()), // arguments
      p, // position
      Type.readThis0, // receiver type
      original.getMt().getReturnType() // return type
    );

    // Just do the call
    if (!callInvariant || this.mode == MODE_D && original.getMs().isUnique())
      original = original.withInner(delegateMCall);
    else{
      ExpCore.Block wrapTemplate;
      if (original.getMt().getMdf()==Mdf.Class) {
        // Factory
        if (original.getReturnMdf().isImm()) { // TODO: is this stupid?
          wrapTemplate = InvariantClose.thisResultWrapper;
        } else { wrapTemplate = InvariantClose.resultWrapper; }
      } else if (this.mode != MODE_L42) {
          wrapTemplate = InvariantClose.thisVisibleStateWrapper;
      } else {
          wrapTemplate = InvariantClose.thisWrapper;
      }

      // Wrap up the call
      original = original.withInner(makeWrapper(wrapTemplate, delegateMCall, null));
    }

    this.addMember(original);
    this.tryAddMember(delegate); // Who cares if it allregy exists, since we didn't touch it
  }

  static ExpCore.Block thisVisibleStateWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisVisibleStateWrapper",
    "{method m() (this.#invariant() r=void this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block thisWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisWrapper",
    "{method m() (r=void this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block resultWrapper=(ExpCore.Block)Functions.parseAndDesugar("ResultWrapper",
    "{method m() (r=void r.#invariant() r)}").getMs().get(0).getInner();
  static ExpCore.Block thisResultWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThissResultWrapper",
    "{method m() (This r=void r.#invariant() r)}").getMs().get(0).getInner();

  ExpCore.Block makeWrapper(ExpCore.Block template, ExpCore body, Type type) {
    String newR = Functions.freshName("r", L42.usedNames);

    // Rename to our newR's and newU's, probably slow, but I'm too lazy to expand this out
    Block res = (Block)template.accept(new CloneVisitor() {
    public ExpCore visit(X s) {
        if(s.getInner().equals("r"))
            return s.withInner(newR);
        else return s;
    }
    //#invariant
    public ExpCore visit(MCall m) {
      m = (MCall) super.visit(m);
      if (m.getS().getName().equals("#invariant")) {
         return m.withS(m.getS().withUniqueNum(uniqueNum));
      } else {
        return m;
      }
    }
    protected Block.Dec liftDec(Block.Dec f) {
        String x = f.getX();
        if (x.equals("r")) {
          f = f.withX(newR).withInner(body);
          if (type != null) f = f.with_t(type);
          return f; // Don't rename occurrences of 'r' inside body!
        } else if (x.startsWith("unused")) {
           f = f.withX(Functions.freshName("unused", L42.usedNames));
        }
        return super.liftDec(f);
    }
    });

    return res;
  }

  static X thisX = new X(Position.noInfo, "this");

  class WrapAux extends RenameMethodsAux {
    WrapAux(Program p, List<CsMxMx> renames, ClassB top) {
      super(p, renames, top);
    }

    // What exposer (if any) are we calling?
    MethodSelector exposer = null;
    int thisUses = 0;

    @Override
    public ExpCore visit(X s) {
        if (s.equals(thisX)) this.thisUses++;
        return super.visit(s);
    }

    @Override
    public ExpCore visit(MCall s) {
      ExpCore e = StaticDispatch.of(this.p, this.g, s.getInner(), false);
      if (e == null) return super.visit(s);

      Type guessed = GuessTypeCore._of(this.p, this.g, e, false);
      if (guessed == null) return super.visit(s);

      MethodSelector ms2 = mSToReplaceOrNull(s.getS(), guessed.getPath());
      if (ms2==null) return super.visit(s);

      // Ok, we've found a call an exposer

      // Exposers are supposed to be instance private...
      if (!s.getInner().equals(thisX))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "Exposer '" + s.getS() + "' called on non 'this' receiver in "+s.getP()));

      this.exposer = ms2;
      return super.visit(s);
    }

    @Override public
    ClassB.MethodWithType visit(ClassB.MethodWithType mwt) {
      this.exposer = null;
      this.thisUses = 0;

      mwt = super.visit(mwt);
      if (this.exposer == null) return mwt;

      if (!mwt.getMdf().isIn(Mdf.Lent, Mdf.Mutable)) return mwt;

      assert this.thisUses >= 1;
      if (this.thisUses > 1)
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator can only use 'this' once '" + mwt.getMs() + "' in " + mwt.getP()));

      //assert !TypeManipulation.fwd_or_fwdP_in(res.getReturnMdf());

      if (mwt.getReturnMdf().equals(Mdf.Lent))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot return lent '" + mwt.getMs() + "' in "+mwt.getP()));

      if (mutExposers.contains(this.exposer) && mwt.getReturnMdf().isIn(Mdf.Mutable,Mdf.MutableFwd))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator that calls a mut exposer cannot also return mut '" + mwt.getMs() + "' in "+mwt.getP()));

      if (mwt.getMdfs().stream().anyMatch(m -> m.isIn(Mdf.Mutable, Mdf.Readable, Mdf.Lent)))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot take lent, mut, or read paramaters '" + mwt.getMs() + "' in "+mwt.getP()));

      return mwt.withInner(makeWrapper(InvariantClose.thisWrapper, mwt.getInner(), mwt.getReturnType()));
    }
  }

  class Privatiser extends CloneVisitor {
    @Override
    public MethodWithType visit(MethodWithType mwt) {
      if (mwt.get_inner() == null || mwt.getMdf().isClass()) // Nothing interesting to do, ignore
        return mwt;

      return super.visit(mwt);
    }

    @Override
    public ExpCore visit(MCall s) {
      // Is this a method call on this?
      if (s.getInner().equals(thisX)) {
        return this.visitThisCall(s);
      } else {
        return super.visit(s);
      }
    }
    public MCall visitThisCall(MCall s) {
      return ((MCall)super.visit(s)).withS(s.getS().withUniqueNum(uniqueNum));
    }

    public ClassB of(ClassB b) { return (ClassB)super.visit(b); }

    @Override
    public ExpCore visit(ClassB l) { return l; }
  }
  class InvariantChecker extends Privatiser {
    private InvariantChecker(Set<MethodSelector> excludedMethods, List<MethodSelector> methodCalls, Set<String> actuallyValidatedFields) {
      super();
      this.excludedMethods = excludedMethods;
      this.methodCalls = methodCalls;
      this.actuallyValidatedFields = actuallyValidatedFields;
    }

    List<MethodSelector> methodCalls;
    Set<MethodSelector> excludedMethods;
    Set<String> actuallyValidatedFields;

    @Override
    public ExpCore visit(X s) {
        if (s.equals(thisX))
          LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
            "Can only use this to call getters for imm and capsule fields within #invariant!"));

        return super.visit(s);
    }

    @Override
    public MethodWithType visit(MethodWithType mwt) {
      return super.visit(mwt).withMs(mwt.getMs().withUniqueNum(uniqueNum)).withMt(mwt.getMt().withRefine(false));
    }

    @Override
    public ExpCore visit(MCall s) {
      // Is this a method call on this?
      if (s.getInner().equals(thisX)) {
        // possibly a field access ?
        if (s.getEs().size() == 0) {
          String name = Coherence.removeHash(s.getS().getName());
          if (validatableFields.contains(name)) {
            this.actuallyValidatedFields.add(name);
            // Don't continue (and hence don't add to this.methodCalls)
            return super.visit(s);
          }
          else if (otherFields.contains(name))
            LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
              "Can only use this to access imm and capsule fields within #invariant! '" + s + "'"));
        }

        if (this.excludedMethods.contains(s.getS()))
          LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
              "Cannot call (indirectly) recursive methods in #invariant!'" + s + "'"));

        this.methodCalls.add(s.getS());
      }
      return super.visit(s);
    }

    @Override
    public MCall visitThisCall(MCall s) {
      // So super.visit(s) dosn't call this.visit(X)
      ExpCore receiver = s.getInner();
      return super.visitThisCall(s.withInner(new ExpCore._void())).withInner(receiver);
    }
  }
}
