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
import newTypeSystem.GuessTypeCore;
import newTypeSystem.StaticDispatch;
import newTypeSystem.TsLibrary;
import newTypeSystem.TypeManipulation;
import programReduction.Methods;
import programReduction.Program;
import tools.LambdaExceptionUtil;


/**

 validatable(p, x) iff
    FieldsMdf(p.top(), x, read) subseteq {imm, capsule, class}
                    // TODO: and x is transitively referenced in #invariant

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
  static MethodSelector invName = MethodSelector.of("#invariant", Collections.emptyList());
  public static ClassB closeJ(PData p, List<Ast.C> path, ClassB top, MethodSelector mutKName, MethodSelector immKName, boolean stupid)
    throws PathUnfit, ClassUnfit {
    return close(p.p, path, top, mutKName, immKName, stupid);
  }

  // path is a path inside top
  public static ClassB close(Program p, List<Ast.C> path, ClassB top, MethodSelector mutKName, MethodSelector immKName, boolean stupid)
      throws PathUnfit, ClassUnfit {

    if (!MembersUtils.isPathDefined(top, path)) throw new RefactorErrors.PathUnfit(path);
    if (MembersUtils.isPrivate(path)) throw new RefactorErrors.PathUnfit(path);

    InvariantClose c = new InvariantClose();
    c.stupid = stupid;
    c.pInner = p.evilPush(top).navigate(path);
    c.inner = c.pInner.top();
    c.p = p;
    c.path = path;
    c.top = top;
    c.mutK = c.getMwt(mutKName);
    c.immK = c.getMwt(immKName);
    return c.run();
  }

  List<Ast.C> path;
  MethodWithType mutK;
  MethodWithType immK;
  Program p;
  ClassB top; // The class that we were given
  boolean stupid = false;

  Program pInner;
  ClassB inner; // The class were closing
  long uniqueNum;
  Set<String> validatableFields = new HashSet<>();
  Set<String> otherFields = new HashSet<>();
  Set<MethodSelector> exposers = new HashSet<>();
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

    // Collect all the fields that can, and cannot be referenced in validate
    for (int i = 0; i < this.mutK.getMt().getTs().size(); i ++) {
      Type type = this.mutK.getMt().getTs().get(i);
      String name = this.mutK.getMs().getNames().get(i);
      if (TypeManipulation.noFwd(type).getMdf().isIn(Mdf.Immutable, Mdf.Capsule, Mdf.Class)) {
        this.validatableFields.add(name);
        this.validatableFields.add("#" + name);
      } else {
        this.otherFields.add(name);
        this.otherFields.add("#" + name);
      }
    }

    {
      List<MethodSelector> todo = new LinkedList<>();
      Set<MethodSelector>  done = new HashSet<>(); // The methods called by #invariant
      todo.add(invName);
      while (!todo.isEmpty()) {
        MethodSelector m = todo.remove(0);

        if (done.contains(m)) // We've already processed this
          continue;

        // Collect all other-called methods
        // check that 'this' is only used to call methods or validatable fields
        // and finally, create a private-version, where all non-field accessers are replaced with private numbered
        // versions
        this.addMember(new InvariantChecker(done, todo).visit(this.getMwt(m)));
        done.add(m);
      }
    }

    for (MethodWithType mwt : this.inner.mwts()) {
      // TODO: Find a better way of determining if 'mwt' is an abstract-state operation
      if (mwt.getMdf() == Mdf.Class || mwt.get_inner() != null)
        continue;

      this.state.add(mwt.getMs());

      Mdf mdf = mwt.getMt().getReturnType().getMdf();

      // Only care about exposers for validatable (i.e. capsule) fields
      if (mdf == Mdf.Lent && this.validatableFields.contains(mwt.getMs().getName()))
        this.exposers.add(mwt.getMs());
    }


    if (!this.stupid)
      // Play with capsule mutators...
      this.top = (ClassB)this.top.accept(new WrapAux(this.p, this.makeRenames(this.exposers), this.top));
    else
      // Redirect everything to use private geters/setters (so we don't call the public version, which will check the invariant)
      this.top = (ClassB)this.top.accept(new RenameMethodsAux(this.p, this.makeRenames(this.state), this.top) {
        // Don't touch method headers
        @Override protected MethodSelector liftMsInMetDec(MethodSelector ms) { return ms; }
      });

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
  private void delegateState() throws ClassUnfit {
    // Make the constructor
    MethodWithType fwdK = this.mutK.withMs(this.mutK.getMs().withUniqueNum(uniqueNum))
        .withMt(this.mutK.getMt().withTs(tools.Map.of(TypeManipulation::fieldToFwd, this.mutK.getMt().getTs())));

    this.addMember(fwdK);

    for (MethodWithType mwt : inner.mwts()) {
      MethodType mt = mwt.getMt();
      if (Arrays.asList(this.mutK.getMs(), this.immK.getMs()).contains(mwt.getMs())) {
        // Delegate mutK and immK to the real constructor
        this.addMember(delegate(true, mwt, fwdK));
        continue;
      }

      boolean setter = !mwt.getMs().getNames().isEmpty();
      Mdf fieldMdf = setter ? mt.getTs().get(0).getMdf() :
        mt.getReturnType().getMdf();

      // Not a state method
      if (!this.state.contains(mwt.getMs()) || mt.getMdf() == Mdf.Class) {
        if (this.stupid && !mwt.getMs().isUnique() && mt.getMdf() != Mdf.Class) // Wrap the body up, but only if a public method
          mwt = mwt.withInner(makeWrapper(InvariantClose.thisStupidWrapper, mwt.getInner(), mt.getReturnType()));

        this.addMember(mwt);
        continue;
      }

      // The new (real) accessor will have a unique number
      MethodWithType newMwt = mwt.withMs(mwt.getMs().withUniqueNum(this.uniqueNum));

      if (this.stupid){
        this.addMember(delegate(true, mwt, newMwt));
        }// Exposer, which should have already been dealt with
      else if (fieldMdf.equals(Mdf.Lent) && this.validatableFields.contains(mwt.getMs().getName())) {
        continue;
        }
      // Call the invariant in a setter, but only if it's for a field that could be validated against
      // Note: 'class' references are really immutable
      else if (setter && this.validatableFields.contains(mwt.getMs().getName())) {
        this.addMember(delegate(true, mwt, newMwt));
      } else {
        this.addMember(delegate(false, mwt, newMwt));
      }

      this.addMember(newMwt); // Add the real thing...
    }
  }

  void addMember(Member member) {
    assert !Functions.getIfInDom(this.newMembers, member).isPresent() : member;
    this.newMembers.add(member);
  }

  MethodWithType delegate(boolean callInvariant, MethodWithType original, MethodWithType delegate) throws ClassUnfit {
    assert original.get_inner() == null;
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
    if (!callInvariant) return original.withInner(delegateMCall);
    else{
      ExpCore.Block wrapTemplate;
      if (original.getMt().getMdf()==Mdf.Class) {
        // Imm constructor
        if(original.getMt().getReturnType().getMdf() == Mdf.Immutable)
            wrapTemplate = InvariantClose.thisResultWrapper;

        // Mut constructor
        else wrapTemplate = InvariantClose.resultWrapper;
      } else if (this.stupid) {
          if (original.getMs().isUnique()) // Not a public method, so just delegate
            return original.withInner(delegateMCall);
          else wrapTemplate = InvariantClose.thisStupidWrapper;
      } else {
          wrapTemplate = InvariantClose.thisWrapper;
      }

      // Wrap up the call
      return original.withInner(makeWrapper(wrapTemplate, delegateMCall, null));
    }
  }

  static ExpCore.Block thisStupidWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisStupidWrapper",
      "{method m() (this.#invariant() r=void this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block thisWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisWrapper",
    "{method m() (r=void this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block resultWrapper=(ExpCore.Block)Functions.parseAndDesugar("ResultWrapper",
    "{method m() (r=void r.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block thisResultWrapper = (ExpCore.Block)Functions.parseAndDesugar("ThisResultWrapper",
    "{method m() (This r=void r.#invariant() r)}").getMs().get(0).getInner();

  ExpCore.Block makeWrapper(ExpCore.Block template, ExpCore body, Type type) {
    String newR = Functions.freshName("r", L42.usedNames);

    // Rename to our newR's and newU's, probably slow, but I'm too lazy to expand this out
    return (Block)template.accept(new CloneVisitor() {
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
        } else if (x.startsWith("unused")) {
           f = f.withX(Functions.freshName("unused", L42.usedNames));
        }
        return super.liftDec(f);
    }
    });
  }

  static X thisX = new X(Position.noInfo, "this");

  class WrapAux extends RenameMethodsAux {
    WrapAux(Program p, List<CsMxMx> renames, ClassB top) {
      super(p, renames, top);
    }

    // Are we calling the capsule exposer?
    boolean capsuleAccesser = false;
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

      // Ok, we've found a call to a lent exposer

      // Exposers are supposed to be instance private...
      if (!s.getInner().equals(thisX))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "Exposer '" + s.getS() + "' called on non 'this' receiver in "+s.getP()));

      this.capsuleAccesser = true;
      return super.visit(s);
    }

    @Override public
    ClassB.MethodWithType visit(ClassB.MethodWithType mwt) {
      this.capsuleAccesser = false;
      this.thisUses = 0;

      ClassB.MethodWithType res = super.visit(mwt);
      if (!this.capsuleAccesser) return res;

      MethodType mt = res.getMt();

      // Can we mutate this?
      // NOTE: Even though a capsule method can mutate this, if it accesses a field
      // that must be the only thing done with 'this', and 'this' will never be reached
      // So there's no need to put restrictions on what we can and can't do with it beyond
      // the fact that a capsule-this can only be used once
      if (!mt.getMdf().isIn(Mdf.Lent, Mdf.Mutable)) return res;

      assert this.thisUses >= 1;
      if (this.thisUses > 1)
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator can only use 'this' once '" + res.getMs() + "' in " + res.getP()));


      // NOTE: A 'mut' return can't alias a capsule field, since capsule fields are seen as lent
      if (TypeManipulation.noFwd(mt.getReturnType()).getMdf() == Mdf.Lent)
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot return lent or mut '" + res.getMs() + "' in "+res.getP()));

      if (mt.getTs().stream().anyMatch(t -> t.getMdf().isIn(Mdf.Mutable, Mdf.Readable, Mdf.Lent)))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot take lent mut or read paramaters '" + res.getMs() + "' in "+res.getP()));

      return res.withInner(makeWrapper(InvariantClose.thisWrapper, res.getInner(), mt.getReturnType()));
    }
  }
  class InvariantChecker extends CloneVisitor {
    private InvariantChecker(Set<MethodSelector> excludedMethods, List<MethodSelector> methodCalls) {
      super();
      this.excludedMethods = excludedMethods;
      this.methodCalls = methodCalls;
    }

    List<MethodSelector> methodCalls;
    Set<MethodSelector> excludedMethods;
    @Override
    public ExpCore visit(X s) {
        if (s.equals(thisX))
          LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
            "Can only use this to call getters for imm and capsule fields within #invariant!"));

        return super.visit(s);
    }

    @Override
    public MethodWithType visit(MethodWithType mwt) {
      if (mwt.get_inner() == null)
        LambdaExceptionUtil.throwAsUnchecked(
            new ClassUnfit().msg("The method " + mwt.getMs() + " is abstract"));

      ExpCore newInner = this.lift(mwt.getInner());
      MethodSelector newMs = mwt.getMs().withUniqueNum(uniqueNum);
      return mwt.withInner(newInner).withMs(newMs);
    }

    @Override
    public ExpCore visit(MCall s) {
      // Is this a method call on this?
      if (s.getInner().equals(thisX)) {
        // possibly a field access ?
        if (s.getEs().size() == 0) {
          if (validatableFields.contains(s.getS().getName()))
            return thisCall(s); // Ok

          else if (otherFields.contains(s.getS().getName()))
            LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
              "Can only use this to access imm and capsule fields within #invariant! '" + s + "'"));
        }

        if (this.excludedMethods.contains(s.getS()))
          LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
              "Cannot call (indirectly) recursive methods in #invariant!'" + s + "'"));

        this.methodCalls.add(s.getS());
        return thisCall(s);
      } else {
        return super.visit(s);
      }
    }

    MCall thisCall(MCall s) {
      ExpCore receiver = s.getInner();
      s = s.withInner(new ExpCore._void()); // So super.visit(s) dosn't call this.visit(X)

      return ((MCall)super.visit(s)).withS(s.getS().withUniqueNum(uniqueNum)).withInner(receiver);
    }

    @Override
    public ExpCore visit(ClassB l) { return l; }
  }
}
