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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
  public static ClassB closeJ(PData p, List<Ast.C>path, ClassB top, String mutK, String immK)
    throws PathUnfit, ClassUnfit, ParseFail {
    return close(p.p, path, top, mutK, immK);
  }

  // path is a path inside top
  public static ClassB close(Program p, List<Ast.C> path, ClassB top, String mutK, String immK)
    throws PathUnfit, ClassUnfit, ParseFail {
    if (!MembersUtils.isPathDefined(top, path)) throw new RefactorErrors.PathUnfit(path);
    if (MembersUtils.isPrivate(path)) throw new RefactorErrors.PathUnfit(path);

    Program pPath = p.evilPush(top).navigate(path);
    ClassB lPath = pPath.top();

    // Check for 'refine? read method imm Void #invariant() throws()'
    {
      MethodWithType inv = (MethodWithType) lPath._getMember(MethodSelector.of("#invariant", Collections.emptyList()));
      if (inv == null)
        throw new ClassUnfit().msg("selector #invariant() missing into " + PathAux.as42Path(path));


      MethodType invMt = new MethodType(inv.getMt().isRefine(), Mdf.Readable, Collections.emptyList(),
              Type.immVoid, Collections.emptyList());

      if (!inv.getMt().equals(invMt))
        throw new ClassUnfit().msg("selector #invariant() into " + PathAux.as42Path(path) + " has invalid type " + inv.getMt());

      // TODO: Check the body of validate for sanity...
    }

    // Ge constructor info
    Ks ks = new Ks(lPath, mutK, immK);

    List<CsMxMx> sel = new ArrayList<>();
    Set<MethodSelector> state = new HashSet<>();

    for (MethodWithType mwt : lPath.mwts()) {
      try { if (!TsLibrary.coherentF(pPath, ks.candidateK, mwt))
          continue; }
      catch (PathMetaOrNonExistant e) { continue; }

      assert mwt.get_inner() == null;

      state.add(mwt.getMs());

      // with non read result, assert is lent
      Mdf mdf = mwt.getMt().getReturnType().getMdf();

      // TODO: Previously: any fields whose return type was not read imm or class were required to return lent
      // I have relaxed that, those that return lent are considered to be a 'capsule field' getters
      // mut or other such fields can't be used by validate anyway, so we don't care about them
      if (mdf == Mdf.Lent) {
        sel.add(new CsMxMx(path, false, mwt.getMs(), mwt.getMs().withUniqueNum(ks.uniqueNum)));
      }
    }

    // Wrap all calls to exposers (into calls to a unique-numbered one)
    ClassB newTop = (ClassB)top.accept(new WrapAux(p, sel, top));

    ClassB res = delegateState(ks, state, newTop.getClassB(path));
    if (path.isEmpty()) {
      // Trivial...
      return res;
    } else {
      // Navigate to the appropriate place, and put res there
      return newTop.onClassNavigateToPathAndDo(path, l -> res);
    }
  }

  private static ClassB delegateState(Ks ks, Set<MethodSelector> state, ClassB lPath)
      throws ClassUnfit {
    List<ClassB.Member> newMembers = new ArrayList<>();
    for (MethodWithType mwt : lPath.mwts()) {
      System.out.println("> " + mwt);
      // Not a state method, so don't touch it
      if (!state.contains(mwt.getMs())) {
        addMember(newMembers, mwt);
        continue;
      }

      Mdf mdf = mwt.getMt().getMdf();
      boolean setter = !mwt.getMs().getNames().isEmpty();
      Mdf fieldMdf = setter ? mwt.getMt().getTs().get(0).getMdf() :
        mwt.getMt().getReturnType().getMdf();

      // Class methods can't be state methods...
      if (mdf == Mdf.Class) {
        addMember(newMembers, mwt);
        continue;
      }

      // The new (real) accessor will have a unique number
      MethodWithType newMwt = mwt.withMs(mwt.getMs().withUniqueNum(ks.uniqueNum));

      // Call the invariant in a setter, but only if it's for a field that could be validated against
      // Note: 'class' references are really immutable
      if (setter && fieldMdf.isIn(Mdf.Immutable, Mdf.Capsule, Mdf.Class)) {
        addMember(newMembers, delegate(true, mwt, newMwt));
        addMember(newMembers, newMwt); // Add the real thing...
      } else if (!fieldMdf.equals(Mdf.Lent)) {
        // Note: Capsule getters (i.e. exposers) should've already been dealt with
        // Obviously don't check the invariant for a getter
        addMember(newMembers, delegate(false, mwt, newMwt));
        addMember(newMembers, newMwt); // Add the real thing...
      }
    }

    // Delegate mutK and immK to the real constructor
    addMember(newMembers, delegate(true, ks.mutK, ks.fwdK));
    addMember(newMembers, ks.fwdK);
    addMember(newMembers, delegate(true, ks.immK, ks.fwdK));

    // Don't play with the nested classes
    newMembers.addAll(lPath.ns());
    return lPath.withMs(newMembers);
  }

  static void addMember(List<Member> members, Member member) {
    System.out.println(member);
    assert !Functions.getIfInDom(members, member).isPresent();
    members.add(member);
  }
  static MethodWithType delegate(boolean callInvariant, MethodWithType original, MethodWithType delegate) {

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
      original = original.withInner(makeWrapper(wrapTemplate, delegateMCall, null));
    }

    return original;
  }

  static ExpCore.Block thisWrapper=(ExpCore.Block)Functions.parseAndDesugar("ThisWrapper",
    "{method m() (r=void Void unusedInv=this.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block resultWrapper=(ExpCore.Block)Functions.parseAndDesugar("ResultWrapper",
    "{method m() (r=void Void unusedInv=r.#invariant() r)}").getMs().get(0).getInner();

  static ExpCore.Block thisResultWrapper = (ExpCore.Block)Functions.parseAndDesugar("ThisResultWrapper",
    "{method m() (This r=void Void unusedInv=r.#invariant() r)}").getMs().get(0).getInner();


  static ExpCore.Block makeWrapper(ExpCore.Block template, ExpCore body, Type type) {
    String newR = Functions.freshName("r", L42.usedNames);
    String newU = Functions.freshName("unusedInv", L42.usedNames);

    // Rename to our newR's and newU's, probably slow, but I'm too lazy to expand this out
    template = (Block)template.accept(new CloneVisitor() {
    public ExpCore visit(X s) {
        if(s.getInner().equals("r"))
            return s.withInner(newR);
        else return s;
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

  static class Ks {
    MethodWithType candidateK;
    MethodWithType fwdK;
    MethodWithType mutK;
    MethodWithType immK;
    long uniqueNum;

    Ks(ClassB l, String mutKName, String immKName) throws ParseFail, ClassUnfit {
      List<String> fields = MakeK.collectFieldNames(l);

      MethodWithType k = MakeK.candidateK("k", l, fields, true);
      MethodSelector ms = k.getMs();
      MethodType mt = k.getMt();

      this.candidateK = k;

      // use mutKName & remove fwd's
      this.mutK = k.withMs(ms.withName(mutKName))
        .withMt(mt.withTs(tools.Map.of(TypeManipulation::noFwd, mt.getTs()))); // make fwd

      // use immKName and convert types (except class) to imm
      this.immK = k.withMs(ms.withName(immKName))
        .withMt(mt.withTs(tools.Map.of(TypeManipulation::toImm, mt.getTs()))
          .withReturnType(TypeManipulation.toImm(mt.getReturnType())));

      this.uniqueNum = L42.freshPrivate();
      // use k__n, and convert capsules to fwdmut
      this.fwdK = k.withMs(ms.withUniqueNum(this.uniqueNum))
        .withMt(mt.withTs(tools.Map.of(TypeManipulation::capsuleToFwdMut, mt.getTs())));
    }
  }

  static class WrapAux extends RenameMethodsAux {
    WrapAux(Program p, List<CsMxMx> renames, ClassB top) { super(p, renames, top); }

    static X thisX = new X(Position.noInfo,"this");

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
      if (!mt.getMdf().isIn(Mdf.Lent, Mdf.Mutable, Mdf.Capsule)) return res;

      assert this.thisUses >= 1;
      if (this.thisUses > 1)
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator can only use 'this' once '" + res.getMs() + "' in " + res.getP()));

      // TODO: Don't care about? Mdf = capsule (the paper says we have to...)

      // TODO: Is it ok to return a fwd mut?
      if (TypeManipulation.noFwd(mt.getReturnType()).getMdf().isIn(Mdf.Lent, Mdf.Mutable))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot return lent or mut '" + res.getMs() + "' in "+res.getP()));

      // TODO: Why were we not allowed to declare exceptions?
      // TODO: Could a 'lent' parameter alias 'this'?
      if (!mt.getTs().stream().allMatch(t -> t.getMdf().isIn(Mdf.Mutable, Mdf.Readable, Mdf.Lent)))
        LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
          "A capsule mutator cannot take lent mut or read paramaters '" + res.getMs() + "' in "+res.getP()));

      return res.withInner(
        InvariantClose.makeWrapper(InvariantClose.thisWrapper, res.getInner(), mt.getReturnType()));
    }
  }
}
