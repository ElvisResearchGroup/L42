package is.L42.connected.withSafeOperators.refactor;

/*

 { T1 f1 ..Tn fn }

 ksignature

 add K,

 make state delegates   g()->f()  if setter/constr add invariant check

 for all the method calling this.exposer()
   check method is ok = ...old... restrictions
   call invariant end

 check validate method follow restrictions= ...old...

 make state private

 TODO: understand behaviour of old, what to change to make the new restrictions
 Validate: can use private state (instead of directly fields), can use methods only using private state
   //hard: ( make a private copy of all public methods used inside validate and make validate use such private version )





 * */


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

import javax.swing.*;

public class InvariantClose {
  static class FieldInfo { FieldInfo(Type t, String n) {this.type = t; this.name = n;} Type type; String name; }
  static MethodSelector invName = MethodSelector.of("#invariant", Collections.emptyList());
  public static ClassB closeJ(PData p, List<Ast.C> path, ClassB top, MethodSelector mutKName, MethodSelector immKName)
    throws PathUnfit, ClassUnfit {
    return close(p.p, path, top, mutKName, immKName);
  }

  // path is a path inside top
  public static ClassB close(Program p, List<Ast.C> path, ClassB top, MethodSelector mutKName, MethodSelector immKName)
      throws PathUnfit, ClassUnfit {

    if (!MembersUtils.isPathDefined(top, path)) throw new RefactorErrors.PathUnfit(path);
    if (MembersUtils.isPrivate(path)) throw new RefactorErrors.PathUnfit(path);

    Program pPath = p.evilPush(top).navigate(path);
    ClassB lPath = pPath.top();

    // Check for 'refine? read method imm Void #invariant() throws() e'
    {
      MethodWithType inv = getMwt(lPath, invName);

      MethodType invMt = new MethodType(inv.getMt().isRefine(), Mdf.Readable, Collections.emptyList(),
              Type.immVoid, Collections.emptyList());

      if (!inv.getMt().equals(invMt))
        throw new ClassUnfit().msg("selector #invariant() into " + PathAux.as42Path(path) + " has invalid type " + inv.getMt());

      if (inv.getInner() == null)
        throw new ClassUnfit().msg("selector #invariant() into " + PathAux.as42Path(path) + " has no body");
    }

    // The unique number to use for generate private things
    long uniqueNum = L42.freshPrivate();
    // Get constructor info
    MethodWithType mutK = getMwt(lPath, mutKName);
    MethodWithType immK = getMwt(lPath, immKName);

    List<FieldInfo> fields = new ArrayList<>(); // get field info
    for (int i = 0; i < mutK.getMt().getTs().size(); i ++)
      fields.add(new FieldInfo(mutK.getMt().getTs().get(i), mutK.getMs().getNames().get(i)));

    // Collect all the fields that can, and cannot be referenced in validate
    Set<String> validatableFields = new HashSet<>();
    Set<String> otherFields = new HashSet<>();
    for (FieldInfo f : fields) {
      if (TypeManipulation.noFwd(f.type).getMdf().isIn(Mdf.Immutable, Mdf.Capsule, Mdf.Class)) {
        validatableFields.add(f.name);
        validatableFields.add("#" + f.name);
      } else {
        otherFields.add(f.name);
        otherFields.add("#" + f.name);
      }
    }

    List<ClassB.Member> newMembers = new ArrayList<>();
    {
      List<MethodSelector> todo = new LinkedList<>();
      Set<MethodSelector> invMethods = new HashSet<>(); // The methods called by #invariant
      todo.add(invName);
      while (!todo.isEmpty()) {
        MethodSelector m = todo.remove(0);

        if (invMethods.contains(m)) // We've already processed this
          continue;

        MethodWithType mwt = (MethodWithType)lPath._getMember(m);
        if (mwt == null) {
          throw new ClassUnfit().msg("Error method " + m + " of path " + PathAux.as42Path(path) + " does not exist, but it is (transitiviley) called by #invariant");
        }

        // Collect all other-called methods, and check that this is only used to call methods or validatable fields
        todo.addAll(InvariantChecker.check(mwt, validatableFields, otherFields));

        invMethods.add(m);
      }

      // Create a 'template' class where the body of each method is renamed
      ClassB renamedTemplate = (ClassB)top.accept(new RenameMethodsAux(p, WrapAux.makeRenames(path, invMethods, uniqueNum), top));

      // Now make new private copies of each method
      for (MethodSelector ms : invMethods) {
        // Get a renamed version of the method
        MethodWithType newMwt = ((MethodWithType)renamedTemplate._getMember(ms))
            .withMs(ms.withUniqueNum(uniqueNum));

        addMember(newMembers, newMwt);
      }
    }

    Set<MethodSelector> exposers = new HashSet<>();
    Set<MethodSelector> state = new HashSet<>();
    for (MethodWithType mwt : lPath.mwts()) {
      try { if (!TsLibrary.coherentF(pPath, mutK, mwt)) continue; }
      catch (PathMetaOrNonExistant e) { continue; }

      assert mwt.get_inner() == null;

      state.add(mwt.getMs());

      // with non read result, assert is lent
      Mdf mdf = mwt.getMt().getReturnType().getMdf();

      // Only care about exposers for validatable (i.e. capsule) fields
      if (mdf == Mdf.Lent && validatableFields.contains(mwt.getMs().getName()))
        exposers.add(mwt.getMs());
    }

    // Wrap all calls to exposers (into calls to a unique-numbered one)
    ClassB newTop = (ClassB)top.accept(new WrapAux(p, path, exposers, top, uniqueNum));

    ClassB res = delegateState(uniqueNum, mutK, immK, state, validatableFields, newMembers, newTop.getClassB(path));
    if (path.isEmpty()) {
      // Trivial...
      return res;
    } else {
      // Navigate to the appropriate place, and put res there
      return newTop.onClassNavigateToPathAndDo(path, l -> res);
    }
  }

  static MethodWithType getMwt(ClassB l, MethodSelector ms) throws ClassUnfit {
    MethodWithType res = (MethodWithType)l._getMember(ms);
    if (res == null)
      throw new ClassUnfit().msg("Error method " + ms + " of th eclass at '" + l.getP() + "' does not exist");
    else return res;
  }
  private static ClassB delegateState(long uniqueNum, MethodWithType mutK, MethodWithType immK, Set<MethodSelector> state, Set<String> validatableFields, List<Member> newMembers, ClassB lPath) {
    // Make the constructor
    MethodWithType fwdK = mutK.withMs(mutK.getMs().withUniqueNum(uniqueNum))
        .withMt(mutK.getMt().withTs(tools.Map.of(TypeManipulation::fieldToFwd, mutK.getMt().getTs())));

    addMember(newMembers, fwdK);

    for (MethodWithType mwt : lPath.mwts()) {
      if (Arrays.asList(mutK.getMs(), immK.getMs()).contains(mwt.getMs())) {
        // Delegate mutK and immK to the real constructor
        addMember(newMembers, delegate(true, mutK, fwdK, uniqueNum));
        addMember(newMembers, delegate(true, immK, fwdK, uniqueNum));
        continue;
      }

      Mdf mdf = mwt.getMt().getMdf();
      boolean setter = !mwt.getMs().getNames().isEmpty();
      Mdf fieldMdf = setter ? mwt.getMt().getTs().get(0).getMdf() :
        mwt.getMt().getReturnType().getMdf();

      // Not a state method
      if (!state.contains(mwt.getMs()) || mwt.getMt().getMdf() == Mdf.Class) {
        addMember(newMembers, mwt);
        continue;
      }

      // The new (real) accessor will have a unique number
      MethodWithType newMwt = mwt.withMs(mwt.getMs().withUniqueNum(uniqueNum));

      // Exposer, which should have already been dealt with
      if (fieldMdf.equals(Mdf.Lent) && validatableFields.contains(mwt.getMs().getName()))
        continue;

      // Call the invariant in a setter, but only if it's for a field that could be validated against
      // Note: 'class' references are really immutable
      if (setter && validatableFields.contains(mwt.getMs().getName())) {
        addMember(newMembers, delegate(true, mwt, newMwt, uniqueNum));
      } else {
        addMember(newMembers, delegate(false, mwt, newMwt, uniqueNum));
      }

      addMember(newMembers, newMwt); // Add the real thing...
    }

    // Don't play with the nested classes
    newMembers.addAll(lPath.ns());
    return lPath.withMs(newMembers);
  }

  static void addMember(List<Member> members, Member member) {
    assert !Functions.getIfInDom(members, member).isPresent() : member;
    members.add(member);
  }
  static MethodWithType delegate(boolean callInvariant, MethodWithType original, MethodWithType delegate, long uniqueNum) {

    assert original.get_inner() == null : original;
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

    if (!callInvariant) // Just do the call
        original = original.withInner(delegateMCall);
    else{
      ExpCore.Block wrapTemplate;
      if (original.getMt().getMdf()==Mdf.Class) {
        // Imm constructor
        if(original.getMt().getReturnType().getMdf() == Mdf.Immutable)
            wrapTemplate = InvariantClose.thisResultWrapper;

        // Mut constructor
        else wrapTemplate = InvariantClose.resultWrapper;
      } else {
          wrapTemplate = InvariantClose.thisWrapper;
      }

      // Wrap up the call
      original = original.withInner(makeWrapper(uniqueNum, wrapTemplate, delegateMCall, null));
    }

    return original;
  }

  static ExpCore.Block thisWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisWrapper",
    "{method m() (r=void Void unusedInv=this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block resultWrapper=(ExpCore.Block)Functions.parseAndDesugar("ResultWrapper",
    "{method m() (r=void Void unusedInv=r.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block thisResultWrapper = (ExpCore.Block)Functions.parseAndDesugar("ThisResultWrapper",
    "{method m() (This r=void Void unusedInv=r.#invariant() r)}").getMs().get(0).getInner();


  static ExpCore.Block makeWrapper(long num, ExpCore.Block template, ExpCore body, Type type) {
    String newR = Functions.freshName("r", L42.usedNames);
    String newU = Functions.freshName("unusedInv", L42.usedNames);

    // Rename to our newR's and newU's, probably slow, but I'm too lazy to expand this out
    template = (Block)template.accept(new CloneVisitor() {
    public ExpCore visit(X s) {
        if(s.getInner().equals("r"))
            return s.withInner(newR);
        else return s;
    }
    //#invariant
    public ExpCore visit(MCall m) {
      m = (MCall) super.visit(m);
      if (m.getS().getName().equals("#invariant")) {
         return m.withS(m.getS().withUniqueNum(num));
      } else {
        return m;
      }
    }
    protected Block.Dec liftDec(Block.Dec f) {
        String x = f.getX();
        if (x.equals("r")) x = newR;
        else if (x.equals("unusedInv")) x = newU;

        return super.liftDec(f.withX(x));
    }
    });

    ExpCore.Block.Dec d0 = template.getDecs().get(0);
    d0 = d0.withInner(body);
    if (type != null)
    d0 = d0.with_t(type);

    return template.withDeci(0, d0);
  }

  static X thisX = new X(Position.noInfo, "this");
  static class WrapAux extends RenameMethodsAux {
    static List<CsMxMx> makeRenames(List<Ast.C> path, Set<MethodSelector> sources, long uniqueNum) {
      return sources.stream().map(
          m -> new CsMxMx(path, false, m, m.withUniqueNum(uniqueNum))).collect(Collectors.toList());
    }
    WrapAux(Program p, List<Ast.C> path, Set<MethodSelector> sources, ClassB top, long uniqueNum) {
      super(p, makeRenames(path, sources, uniqueNum), top);
      this.uniqueNum = uniqueNum;
    }

    // Are we calling the capsule exposer?
    boolean capsuleAccesser = false;
    int thisUses = 0;
    long uniqueNum;

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

      return res.withInner(
        InvariantClose.makeWrapper(this.uniqueNum, InvariantClose.thisWrapper, res.getInner(), mt.getReturnType()));
    }
  }

  static class InvariantChecker extends PropagatorVisitor {
    public static Set<MethodSelector> check(MethodWithType meth, Set<String> validatableFields, Set<String> otherFields) {
      InvariantChecker c = new InvariantChecker();
      c.validatableFields = validatableFields;
      c.otherFields = otherFields;
      c.visit(meth); // If the invariant is abstract this will do nothing, except waste memory and time...
      return c.methodCalls;
    }

    private InvariantChecker() { super(); }
    Set<String> validatableFields;
    Set<String> otherFields;
    Set<MethodSelector> methodCalls = new HashSet<>();
    @Override
    public Void visit(X s) {
        if (s.equals(thisX))
          LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
            "Can only use this to call getters for imm and capsule fields within #invariant!"));

        return super.visit(s);
    }

    @Override
    public Void visit(MCall s) {
      // Is this a method call on this?
      if (s.getInner().equals(thisX)) {
        s = s.withInner(new ExpCore._void()); // So super.visit(s) dosn't call this.visit(X)
        if (s.getEs().size() == 0) {
          // possibly a field access
          if (validatableFields.contains(s.getS().getName()))
            return super.visit(s); // Ok, skip
          else if (otherFields.contains(s.getS().getName()))
            LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
              "Can only use this to access imm and capsule fields within #invariant! '" + s + "'"));
        }

        this.methodCalls.add(s.getS());
      }
      return super.visit(s);
    }

    @Override
    public Void visit(ClassB l) {
      return null;
    }
  }
}
