package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.*;
import ast.ErrorMessage;
import ast.Util.CsPz;
import ast.Util.CsPath;

import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.PathAux;
import auxiliaryGrammar.*;
import coreVisitors.From;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.RedirectError;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import programReduction.Program;
import tools.Assertions;
import tools.ListFormatter;

/*Note the type system prevents to return a class T with T without methods.
This is needed to avoid the redirect from violating metalevel soundness:

 TT:Resource<><{T:{interface} method class T id(class T that){return that}}

 if we was to allow TT to be well typed, the result of

 C:Redirect[\"T" into Any]<><TT()

 would not be well typed: we can not return class Any (is ill typed). Otherwise we may cast
  a class Any obtained thanks to the distinction between tryTyped and tryCohereny
  back to its original Path (that may not be coherent) and call class methods on it.

 */
public class Redirect {
  public static ClassB redirectS(PData pData,ClassB that,String src,ast.Ast.Path dest) throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit{
    return redirectJ(pData,that,PathAux.parseValidCs(src),dest);
    }
  public static ClassB redirectJ(PData pData,ClassB that,List<C> src,ast.Ast.Path dest) throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit{
    assert dest.isCore() || dest.isPrimitive():
      dest;
    return new is.L42.connected.withSafeOperators.refactor.RedirectObj(that).redirect(pData.p,src, dest);
    //return redirect(pData.p, that, new PathMap(List.of(new CsPath(src, dest))));
    }


  static public ClassB applyRedirect(Program p, PathMap map) {
    //TODO: use the new renaming?
    ClassB L = map.apply(p.top());
    L = PathMap.remove(L, map.dom());
    return L;
  }

  enum ClassKind { Final, Interface, Class }

  Program p; // The current program we are look at, this may be different from problem.p

  public final Problem problem; // Everything you need to know about what we are trying to do
  public static class Problem {
    final Program p; // The program we are redirecting in terms of, with the argument to the redirect at the top
    final PathMap R; // The user-provided map, in terms of the above p
    Problem(Program p, ClassB L, PathMap R) {
      this.p = p.evilPush(L);
      this.R = new PathMap(R.stream().map(
        CsP -> new CsPath(CsP.getCs(), this.p.minimize(From.fromP(CsP.getPath(), Path.outer(1))))
      ).collect(Collectors.toList())); }}

  final boolean detailed;
  <T> T detailed(Supplier<T> f) { return this.detailed ? f.get() : null; }
  void debugPrint(String m) { if (!this.detailed) System.out.println(m); }

  public Redirect(Problem problem, boolean detailed) { this.problem = problem; this.p = problem.p; this.detailed = detailed;}

  static ClassB redirect(Program p, ClassB L, PathMap R0) throws RedirectError {
    return new Redirect(new Problem(p, L, R0), false).solve(); }

  CsPzMap subtypeConstraints, supertypeConstraints;
  Map<List<C>, ClassKind> redirectSet = new HashMap<List<C>, ClassKind>();
  // ChooseRedirect(p; Cs1->P1, ..., Csn->Pn) = R'

  /*
   * CollectError(ListFormat ls, string message) { 
   * }
   * checkCsExistsWork(Collection<List<C>> Css) { ... }
   *checkCsExists(Collection<List<C>> Css) { checkCsExistsWork(Cs -> thorw new exception }
   *checkCsExists(Collection<List<C>> Css) { ... checkCsExistsWork(Cs -> thorw new exception .. }
   * */

  void pathUnfit(ListFormatter formatter, List<C> Cs, String reason) throws RedirectError.PathUnfit {
    if (this.detailed) { formatter.append("Cannot redirect " + PathAux.as42Path(Cs) + ": " + reason); }
    else { throw new RedirectError.PathUnfit(Cs, reason, this); }}
  void checkCsValid(Collection<List<C>> Css) throws RedirectError.DetailedError, RedirectError.PathUnfit {
    var errors = detailed(() -> new ListFormatter().header("Input to redirect is invalid:\n").prefix("  ").suffix("\n"));
    for (var Cs : Css) {
      if (Cs.isEmpty()) {
        pathUnfit(errors, Cs, "it is empty.");
        continue; }
      if (Cs.stream().anyMatch(C::isUnique)) {
        pathUnfit(errors, Cs, "it is private.");
        continue; }
      try { p.top().getClassB(Cs); }
      catch (ErrorMessage.PathMetaOrNonExistant __) { pathUnfit(errors, Cs, "it does not exist."); }}

    if (this.detailed && errors.count() > 0) {
      throw new RedirectError.DetailedError(errors.toString()); }}

  public ClassB solve() throws RedirectError {
    checkCsValid(this.problem.R.dom());

    this.subtypeConstraints = new CsPzMap(this.problem.R.toList()) {
      @Override String format(String Cs, String Pz) {return Cs + " <= " + Pz; }};
    this.supertypeConstraints = new CsPzMap(this.subtypeConstraints) {
      @Override String format(String Cs, String Pz) {return Pz + " <= " + Cs; }};

    // Pre-compute the redirect set, mustClass and mustInterface
    computeRedirectSet();
    validRedirect(null); // Check user input!, only needed to get a more 'acurate' error message

    collect();

    var R1 = chooseR();
    debugPrint("Chosen R was: " + R1);

    this.p = p.updateTop(R1.apply(p.top()));
    //validRedirect(R1);

    return PathMap.remove(this.p.top(), R1.dom()); }

  void unredirectable(ListFormatter formatter, List<C> Cs, String reason) throws RedirectError.ClassUnfit {
    if (this.detailed) { formatter.append(reason); }
    else { throw new RedirectError.ClassUnfit(Cs, reason, this); }}
  String redirectable(List<C> Cs) throws RedirectError.ClassUnfit {
    var L = this.p.top().getClassB(Cs);
    var errors = detailed(() ->
      new ListFormatter().header("Cannot redirect " + PathAux.as42Path(Cs) + ":\n").prefix("    ").suffix("\n"));

    if (Cs.isEmpty()) { // Cs not empty
      unredirectable(errors, Cs, "It refers to the whole library literal!"); }

    if (Cs.stream().anyMatch(C::isUnique)) { // forall C in Cs: not Private(Cs)
      unredirectable(errors, Cs, "It is private."); }

    for (var mwt : L.mwts()) { // forall mwt in L[Cs].mwtz:
      var ms = mwt.getMs();
      if (ms.isUnique()) { // not Private(mwt.m)
        unredirectable(errors, Cs, "It contains a private method: " + ms + "."); }
      if (mwt.get_inner() != null) { // mwt.e = empty
        unredirectable(errors, Cs, "It contains a non abstract method: " + ms + "."); }}

    return Objects.toString(errors); }
  void computeRedirectSet() throws RedirectError.DetailedError, RedirectError.ClassUnfit {
    var L = this.p.top();
    var Csz = this.problem.R.dom();

    var message = detailed(() -> new ListFormatter().header("Requested redirect is invalid:\n").prefix("  ").suffix("\n"));
    var todo = new ArrayList<List<C>>(Csz);
    var done = new ArrayList<List<C>>(); // Just to ensure determinism of error messages
    Consumer<Path> accumulate = P -> {
      if (P.tryOuterNumber() == 0 && !redirectSet.containsKey(P.getCBar())) {
        todo.add(P.getCBar()); }};

    // TODO: Do p.minimizes?
    while (!todo.isEmpty()) {
      var Cs = todo.get(0);
      todo.remove(0);
      done.add(Cs);
      ClassB L2 = L.getClassB(Cs);

      ClassKind kind = L2.isInterface() ? ClassKind.Interface :
        L2.mwts().stream().anyMatch(mwt -> mwt.getMdf().isClass()) ? ClassKind.Final :
        ClassKind.Class;
      redirectSet.put(Cs, kind);

      var e = redirectable(Cs);
      detailed(() -> message.append(e));

      for (var mwt : iterate(fromMwtz(L2, toP(Cs)))) {
        accumulate.accept(mwt.getReturnPath());
        mwt.getPaths().forEach(accumulate);
        mwt.getExceptions().forEach(accumulate); }

      fromPz(L2, toP(Cs)).forEach(accumulate); }

    for (var Cs : done) {
      var errors = detailed(() ->
        new ListFormatter().header("Cannot completely redirect " + PathAux.as42Path(Cs) + ":\n").prefix("    ").suffix("\n"));
      for (var C : L.getClassB(Cs).cDom()) {
        if (!this.redirectSet.containsKey(withAdd(Cs, C))) {
          unredirectable(errors, Cs, "It contains a nested class " + C + ", which is not in the redirect set."); }}
      detailed(() -> message.append(errors.toString()));}

    if (this.detailed && message.count() > 0) {
      throw new RedirectError.DetailedError(message.toString()); }}

  boolean addConstraint(CsPzMap map, List<C> key, Path value, String message) {
    var res = map.add(key, value);
    if (res) this.debugPrint(message + " ==> " + map.format(PathAux.as42Path(key), value.toString()));
    return res; }
  boolean addSupertype(List<C> key, Path value, String message) { return addConstraint(this.supertypeConstraints, key, value, message); }
  boolean addSubtype(List<C> key, Path value, String message) { return addConstraint(this.subtypeConstraints, key, value, message); }
  void collect() {
    debugPrint("\nStart: " + this.printConstraints());
    boolean progress; // Have we done something?
    do {
      progress = false;
      //1: Collect(p; Cs <= P) = P <= Cs
      //   p[P].interface? = empty
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (!this.p.extractClassB(CsP.getPath()).isInterface()) { // p[P].interface? = empty
          progress |= addSupertype(CsP.getCs(), CsP.getPath(), "Rule 1: " + asSubtype(CsP)); }} // P <= Cs

      //3: Collect(p; P <= Cs) = MostSpecific(p; Pz) <= Cs', Cs' <= MostGeneral(p; Pz)
      //   This0.Cs' in p[Cs].Pz
      //   Pz = {P' in SuperClasses(p; P) | msdom(p[P') = msdom(p[Cs'])}
      for (var CsP : supertypeConstraints) { // P <= Cs
        for (var PCs2 : iterate(this.fromPz(toP(CsP.getCs())))) { // P in p[Cs].Pz
          if (PCs2.tryOuterNumber() == 0) { // P = This0.Cs'
            var Cs2 = PCs2.getCBar();
            var Pz = superClasses(CsP.getPath()).stream().filter(Pi -> possibleTarget(Cs2, Pi))
              .collect(Collectors.toSet()); // px = {P' in SuperClasses(p; P) | msdom(p[P']) = msz}
            var P1 = _mostSpecific(Pz); // P1 = MostSpecific(p; Pz)
            var P2 = _mostGeneral(Pz); // P2 = MostGeneral(p; Pz)

            if (P1 != null) { // P1 <= Cs'
              progress |= addSupertype(Cs2, P1, "Rule 3a: " + asSupertype(CsP)); } // TODO?
            if (P2 != null) { // Cs' <= P2
              progress |= addSubtype(Cs2, P2, "Rule 3b: " + asSupertype(CsP)); }

            progress |= addSupertype(Cs2, CsP.getPath(), "Rule 3d: " + asSupertype(CsP));

            /*for (var P3 : subtypeConstraints.get(Cs2)) { // Cs' <= P" // TODO: Proove this is neccessary
              var CsP3 = new CsPath(Cs2, P3); //Cs <= P", P <= Cs'
              progress |= addSubtype(CsP.getCs(), P3, "Rule 3c: " + asSupertype(CsP) + ", " + asSubtype(CsP3)); }*/}}}

      //4: Collect(p; P <= Cs) = p[P.ms].P <= Cs'
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : supertypeConstraints) { // P <= Cs
        progress |= collectReturns(CsP, (Cs, P, ms) -> addSupertype(Cs, P,
          "Rule 4: " + asSupertype(CsP) + " [" + ms + "]"));} // p[P.ms].P <= p[Cs.ms].P.Cs

      //5: Collect(p; CC) = Cs' <= p[P.ms].Pi
      //   CC = P <= Cs or  CC = Cs <= P
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : seqIterate(supertypeConstraints, subtypeConstraints)) { // P <= Cs or Cs <= P
        // p[Cs.ms].Pi.Cs <= p[P.ms].Pi
        progress |= collectParams(CsP, (Cs, P, ms, i) -> addSubtype(Cs, P,
            "Rule 5: " + asReltype(CsP) + " [" + ms + "." + i + "]"));}

      //6: Collect(p; Cs <= P) = Cs' <= p[P.ms].P:
      //   MustInterface(p; Cs)
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          // p[Cs.ms].P.Cs <= p[P.ms].P
          progress |= collectReturns(CsP, (Cs, P, ms) -> addSubtype(Cs, P,
            "Rule 6: " + asSubtype(CsP) + " [" + ms + "]"));}}

      //7: Collect(p; Cs <= P) = p[P.ms].Pi <= Cs':
      //   MustInterface(p; Cs)
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          progress |= collectParams(CsP, (Cs, P, ms, i) -> addSupertype(Cs, P,
              "Rule 7: " + asSubtype(CsP) + " [" + ms + "." + i + "]"));}} // p[P.ms].Pi <= p[Cs.ms].Pi.Cs

      //8/8': Collect(p; Cs <= P, CCz) = CCz'
      for (var CsP : subtypeConstraints) { // Cs <= P
        TriPredicate<List<C>, Path, String> body = (Cs2, P2, message) -> {
          boolean progress2 = false;

          // 8a/8'a
          if (!p.extractClassB(P2).isInterface()) { // p[P'].interface?=empty and
            progress2 |= addSupertype(Cs2, P2, message); } // CC = P' <= Cs'

          // 8d/8'd
          for (var ms : p.top().getClassB(Cs2).msDom()) { // or ms' in dom(p[Cs'])
            var P3 = _origin(ms, P2); // P'' = Origin(p; sel'; P')
            if (P3 != null) {
              progress2 |= addSubtype(Cs2, P3, message + " [" + ms + "]");}}  // CC = Cs' <= P''

          return progress2;};

        // 8: This0.Cs' = p[Cs.sel].P
        //    P' = p[P.sel].P
        progress |= collectReturns(CsP, (Cs, P, ms) -> body.test(Cs, P, "Rule 8: " + asSubtype(CsP) + " [" + ms + "]"));

        // TODO: Proove this is neccessary?
        // 8': This0.Cs' in p[Cs].Pz
        //     P' in SuperClass(p; P)
        /*for (var P2 : iterate(this.fromPz(toP(CsP.getCs())))) { // P' in p[Cs].Pz
          if (P2.tryOuterNumber() == 0) { // P' = This0.Cs'
            for (var P3 : superClasses(CsP.getPath())) { // P'' in SuperClass(p; P)
              progress |= body.test(P2.getCBar(), P3, "Rule 8': " + asSubtype(CsP)); }}}*/}

      //11a and 11b
      // TODO: Proove this is neccessary?
      for (var Cs : supertypeConstraints.dom()) { // RChoices is only defined for things in superTypeConstraints
        var Pz = _RChoices(Cs);
        // TODO: If Pz is empty, we can produce an error now!
        if (Pz.size() == 1) { // 11a: RChoices(CCz) = Cs -> {P} ==> Cs <= P
          progress |= addSubtype(Cs, Pz.iterator().next(), "Rule 11a: " + printConstraint(Cs)); }
        // 11b: RChoices(CCz) = Cs -> Pz ==> MostSpecific(p; Pz) <= Cs
        var P = _mostSpecific(Pz);
        if (P != null) {
          progress |= addSupertype(Cs, P, "Rule 11b: " + printConstraint(Cs)); }}}

    // Keep going until we stop doing anything
    while (progress);
    debugPrint("End: " + this.printConstraints() + "\n"); }

  Set<Path> _RChoices(List<C> Cs) {
    var Ps1 = this.supertypeConstraints.get(Cs);
    if (Ps1.isEmpty()) { return null; }

    var Ps2 = this.subtypeConstraints.get(Cs);
    var Pz = superClasses(Ps1);
    var Pz2 = Pz.stream().filter(P -> superClasses(P).containsAll(Ps2)).collect(Collectors.toSet());

    return Pz2.stream().filter(P -> possibleTarget(Cs, P)).collect(Collectors.toSet()); }

  static Random rand = new Random();
  PathMap chooseRandomR() throws RedirectError.DeductionFailure {
    PathMap res = new PathMap();
    for (var Cs : this.redirectSet.keySet()) {
      var Pz = _RChoices(Cs);
      if (Pz == null || Pz.isEmpty()) { throw new RedirectError.DeductionFailure(Cs, "", this); }

      var Ps = Pz.stream().sorted(Comparator.comparing(Path::toString)).collect(Collectors.toList());
      var P = Ps.get(rand.nextInt(Ps.size()));
      res.add(Cs, P); }

    return res; }

  PathMap chooseR() throws RedirectError.DetailedError, RedirectError.DeductionFailure {
    var message = detailed(() -> new ListFormatter().header("Failed to choose mapping:\n").prefix("  ").suffix("\n"));
    PathMap res = new PathMap(/*this.problem.R*/);
    //  ChooseR(p; Cs <= P0, ..., Cs <= Pn, CCz) := Cs -> P, ChooseR(p; CCz)
    //    P = MostSpecific(p; P0,...,Pn)
    // Marco Says to ignore this case
    assert this.redirectSet.keySet().containsAll(subtypeConstraints.dom());
    assert this.redirectSet.keySet().containsAll(supertypeConstraints.dom());

    for (var Cs : this.redirectSet.keySet()) {
      var Pz = _RChoices(Cs);//this.supertypeConstraints.get(Cs);
      if (Pz == null) {
        if (!this.subtypeConstraints.contains(Cs)) {
          if (detailed) { message.append("Found no constraints for " + PathAux.as42Path(Cs) + "."); }
          else { throw new RedirectError.DeductionFailure(Cs, "We were unable to collect any constraints on it.", this); }
        } else {
          var con = this.printConstraint(Cs);
          if (detailed) { message.append("The only constraints we found for " + PathAux.as42Path(Cs) + ", were subtype constraints: " + con + "."); }
          else { throw new RedirectError.DeductionFailure(Cs, "We only found the subtype constraints: " + con + ".", this); }}
        
        continue; }

      var P = _mostSpecific(Pz);
      if (P == null) {
        var cons = this.printConstraint(Cs);
        if (detailed) { message.append("Cannot find a most specific solution for " + cons + "."); }
        else {
          var P2 = res._get(Cs);

          if (P2 != null) { // The mapping was given by the user
            // Remove!
            this.subtypeConstraints.remove(Cs, P2); // Remove the users constraints
            this.supertypeConstraints.remove(Cs, P2); // Remove the users constraints
            cons = this.printConstraint(Cs);
            throw new RedirectError.DeductionFailure(Cs, "The given target, " + P2 +", does not satisfy " + cons + ".", this); }
          else {
            throw new RedirectError.DeductionFailure(Cs, "Cannot find a most-specific solution to the constraint "
              + cons + ".", this); }}}
      else { res.add(Cs, P); }}
    if (detailed && message.count() > 0) { throw new RedirectError.DetailedError(message.toString()); }
    return res; }

  void invalidRedirect(boolean inputCheck, ListFormatter formatter, List<C> Cs, Path P, String reason)
      throws RedirectError.DeductionFailure, RedirectError.InvalidMapping {
    if (this.detailed) { formatter.append(reason); }
    else {
      if (inputCheck) { // Was the users fault!
        throw new RedirectError.InvalidMapping(Cs, P, reason, this); }
      else {throw new RedirectError.DeductionFailure(Cs, "our best guess, " + P + ", failed: " + reason, this); }}}
  // Otherwise, throw an AlgorithmError
  void validRedirect(PathMap R) throws RedirectError.DetailedError, RedirectError.DeductionFailure, RedirectError.InvalidMapping {
    var message = detailed(() -> new ListFormatter().header("Chosen redirect is invalid:\n").prefix("  ").suffix("\n"));
    // Check structural subtyping (including NC consistency)
    var inputCheck = R == null;
    if (R == null) { R = this.problem.R; }

    for (var CsP : R) {
      var P = CsP.getPath();
      var Cs = CsP.getCs();
      ClassKind ck = redirectSet.get(Cs);

      var errors = detailed(() -> new ListFormatter().header("Cannot redirect " + PathAux.as42Path(Cs) + " to " + P + ":\n")
          .prefix("    ").seperator("\n"));

      assert p.minimize(P).equals(P);
      assert P.tryOuterNumber() != 0;

      var L1 = p.top().getClassB(Cs);
      var L2 = p.extractClassB(P);

      // No need to check for well-typedness of L2, as 42 gurantees this for any 'class Any' given as input is (transitivley) well-typed

      // p|- P; L2 <= Cs; L1
      var Pz2 = superClasses(P);
      var Pz1 = p.minimizeSet(fromPz(L1, toP(Cs)).filter(x -> x.tryOuterNumber() != 0));
      if (!Pz2.containsAll(Pz1)) { //Pz subseteq_p SuperClasses(p; P)
        var Pz11 = new HashSet<>(Pz1);
        Pz11.removeAll(Pz2);
        invalidRedirect(inputCheck, errors, Cs, P, "Target is not a subclass of " + PathAux.asSet(Pz11)); }

      // Check class compatability
      if (ck.equals(ClassKind.Final) && L2.isInterface()) {
        invalidRedirect(inputCheck, errors, Cs, P, "Cannot redirect a final class, with class methods, to an interface."); }
      if (ck.equals(ClassKind.Interface) && !L2.isInterface()) {
        invalidRedirect(inputCheck, errors, Cs, P, "Cannot redirect an interface to a final class"); }

      //forall MS in dom(mwtz): p |- mwtz'(MS).mt <= mwt(MS).mt
      for (var mwt1 : iterate(fromMwtz(L1, toP(Cs)))) {
        var mwt2 = L2._getMwt(mwt1.getMs());
        if (mwt2 == null) {
          invalidRedirect(inputCheck, errors, Cs, P, "Target does not contain method " + mwt1.getMs() + ".");
          continue; }
        mwt2 = fromMwt(mwt2, P);
        if (!partialMethSubType(mwt2.getMt(), mwt1.getMt())) {
          invalidRedirect(inputCheck, errors, Cs, P, "The method type for " + mwt1.getMs() + " of the target (" + mwt2.getMt() +
          ") is not a subtype of the source (" + mwt1.getMt() + ")."); }}

      // mwtz[with refine?s=empty] = mwtz'[with refine?s=empty]
      // (implied by the above check together with this one)
      if (ck.equals(ClassKind.Interface)) {
        for (var mwt2 : iterate(fromMwtz(L2, P))) {
          var mwt1 = L1._getMwt(mwt2.getMs());
          if (mwt1 == null) {
            invalidRedirect(inputCheck, errors, Cs, P, "Source does not contain method " + mwt2.getMs());
            continue; }
          mwt1 = fromMwt(mwt1, toP(Cs));
          if (!partialMethSubType(mwt1.getMt(), mwt2.getMt())) {
            invalidRedirect(inputCheck, errors, Cs, P, "The method type for " + mwt2.getMs() + " of the target (" + mwt2.getMt() +
            ") isn't a supertype of the source (" + mwt1.getMt() + ")."); }}}

      detailed(() -> message.append(errors.toString())); }

    if (this.detailed && message.count() > 0) {
      throw new RedirectError.DetailedError(message.toString()); }}

  boolean possibleInterfaceTarget(List<C> Cs, Path P) {
    var kind = redirectSet.get(Cs);
    assert kind != null;

    var L1 = p.top().getClassB(Cs);
    var L2 = p.extractClassB(P);

    if (!L2.isInterface()) { return false; }
    if (!L2.msDom().equals(L1.msDom())) { return false; }

    for (var mwt : iterate(fromMwtz(L1, toP(Cs)))) { //forall sel in dom(p[P].mwtz)
      var T = mwt.getReturnType(); // T = p[Cs.sel].P
      // We know this exists, thanks to the above code
      var T2 = From.fromT(L2._getMwt(mwt.getMs()).getReturnType(), P); // T' = p[P.sel].mt.T

      if (!T.getMdf().equals(T2.getMdf())) { return false; } // T.mdf =T'.mdf
      if (T.getPath().tryOuterNumber() != 0) { // if T.P not of the form This0.Cs' // TODO: Implement in code
        if (!p.equiv(T2.getPath(), T.getPath())) { return false; }}} // p.equiv(T.P, T'.P)

    return true; }

  boolean possibleTarget(List<C> Cs, Path P) {
    var kind = redirectSet.get(Cs);
    assert kind != null;
    assert p.minimize(P).equals(p.minimize(P));

    var L1 = p.top().getClassB(Cs);
    var L2 = p.extractClassB(P);
    var ck = this.redirectSet.get(Cs);

    if (ck.equals(ClassKind.Final) && L2.isInterface()) { return false; }
    if (ck.equals(ClassKind.Interface) && !L2.isInterface()) { return false; }

    if (!L2.msDom().containsAll(L1.msDom())) { return false; }
    if (ck.equals(ClassKind.Interface) && !L1.msDom().containsAll(L2.msDom())) { return false; }

    for (var mwt : iterate(fromMwtz(L1, toP(Cs)))) {
      // We know this exists, thanks to the above code
      var mwt2 = fromMwt(L2._getMwt(mwt.getMs()), P);
      if (!partialMethSubType(mwt2.getMt(), mwt.getMt())) { return false; }
      if (ck.equals(ClassKind.Interface) && !partialMethSubType(mwt.getMt(), mwt2.getMt())) { return false; }}
    return superClasses(P).containsAll(p.minimizeSet(fromPz(L1, toP(Cs)).filter(x -> x.tryOuterNumber() != 0))); }


  Path _mostSpecific(Set<Path> Pz) {
    Pz = p.minimizeSet(Pz.stream());
    // TODO: This is horribly inefficient...
    for (Path P : Pz) {
      if (superClasses(P).containsAll(Pz)) { return P; }}

    return null;}

  Path _mostGeneral(Set<Path> Pz) {
    Pz = p.minimizeSet(Pz.stream());
    // TODO: This is horribly inefficient...
    if (!Pz.isEmpty()) {
      var res = superClasses(Pz);
      res.retainAll(Pz);
      if (res.size() == 1) { return res.iterator().next(); } }

    return null;}


  boolean partialSubtype(Type sub, Type sup) {
    if (sub.getPath().tryOuterNumber() != 0 && sup.getPath().tryOuterNumber() != 0) { return p.subtypeEq(sub, sup); }
    else { return Functions.isSubtype(sup.getMdf(), sub.getMdf()); }}

  boolean partialMethSubType(MethodType sub, MethodType sup) {
    assert sub.getTs().size() == sup.getTs().size();

    return Functions.isSubtype(sup.getMdf(), sub.getMdf())
      && partialSubtype(sub.getReturnType(), sup.getReturnType())
      && IntStream.range(0, sub.getTs().size()).allMatch(i -> partialSubtype(sup.getTs().get(i), sub.getTs().get(i)))
      && sub.getExceptions().stream().allMatch(T1 -> sup.getExceptions().stream().anyMatch(T2 -> partialSubtype(T1, T2)));}

  boolean mustInterface(List<Ast.C> Cs) { return this.redirectSet.get(Cs) == ClassKind.Interface; }

  // collectReturns(Cs P, f) computes f(Cs', P'), such that
  //    This0.Cs' = p[Cs](ms).P
  //    P' = p[P](ms).P
  // and returns true if any of the calls to f did
  boolean collectReturns(CsPath CsP, TriPredicate<List<C>, Path, MethodSelector> f) {
    boolean progress = false;
    for (var mwt : iterate(this.fromMwtz(toP(CsP.getCs())))) {
      var P2 = mwt.getReturnPath(); // P2 = mwt.P
      var mwt2 = this._fromPms(CsP.getPath(), mwt.getMs()); // mwt2? = p[P](mwt.ms)
      if (P2.tryOuterNumber() == 0 && mwt2 != null) { // P2 = This0.Cs', mwt2? != empty
        progress |= f.test(P2.getCBar(), mwt2.getReturnPath(), mwt.getMs()); }} //f(P2.Cs, mwt2.P)
    return progress; }

  // collectParams(Cs P, f) same as collectReturns, but for each paramater type instead
  boolean collectParams(CsPath CsP, QuadPredicate<List<C>, Path, MethodSelector, Integer> f) {
    boolean progress = false;
    for (var mwt : iterate(this.fromMwtz(toP(CsP.getCs())))) {
      for (int i = 0; i < mwt.getSize(); i++) { // i in 0..#(mwt.ms.xs)
        var P2 = mwt.getPaths().get(i); // P2 = mwt.Pi
        var mwt2 = this._fromPms(CsP.getPath(), mwt.getMs()); // mwt2? = p[P](mwt.ms)
        if (P2.tryOuterNumber() == 0 && mwt2 != null) { // P2 = This0.Cs', mwt2? != empty
          progress |= f.test(P2.getCBar(), mwt2.getPaths().get(i), mwt.getMs(), i); }}} //f(P2.Cs, mwt2.Pi)
    return progress; }

  // Utilities, not directly related to redirect

  //SuperClasses(p; Pz) = intersect {p.minimize(p[P].Pz U {P, Any}) | P in Pz}
  Set<Path> superClasses(Path P) { return this.p.minimizeSet(Stream.concat(this.fromPz(P), Stream.of(P, Path.Any()))); }

  Set<Path> superClasses(Collection<Path> Pz) {
    assert !Pz.isEmpty();
    return intersect(Pz.stream().map(this::superClasses)); }

  // Because Java's collection API sucks
  static <T> List<T> withAdd(List<T> s, T... extras) {
    var res = new ArrayList<T>(s);
    res.addAll(Arrays.asList(extras));
    return res; }

  // iterates over the first, and then the second collection
  static <T> Iterable<T> seqIterate(Iterable<T> first, Iterable<T> second) {
    return () -> new Iterator<T>() {
        Iterator<T> inner = first.iterator();
        boolean firstHalf = true;

        void update() { inner = second.iterator(); firstHalf = false; }
        @Override public boolean hasNext() {
          var res = this.inner.hasNext();
          if (!res && firstHalf) {
            this.update();
            res = this.inner.hasNext();
          }
          return res; }
        @Override public T next() {
          if (firstHalf) {
            try { return inner.next(); }
            catch (NoSuchElementException e) { update(); } }
          return inner.next(); }};}

  static <T> Set<T> intersect(Stream<Collection<T>> s) {
      Set<T> res = null;
      for (var set : iterate(s)) {
        if (res == null) { res = new HashSet<>(set); }
        else { res.retainAll(set); }}
      return res; }

  static <T> Iterable<T> iterate(Stream<T> s) { return s::iterator; }

  // Utility functions to do fromming...

  // p[P].Pz
  Stream<Path> fromPz(Path P) { return fromPz(this.p.extractClassB(P), P); }
  // L.Pz[from P]
  Stream<Path> fromPz(ClassB L, Path P) { return From.fromPs(this.p.extractClassB(P).getSuperPaths(), P); }

  // p[P].mwtz
  Stream<MethodWithType> fromMwtz(Path P) { return fromMwtz(p.extractClassB(P), P); }
  // (L.mwtz)[from P]
  Stream<MethodWithType> fromMwtz(ClassB L, Path P) { return L.mwts().stream().map(mwt -> fromMwt(mwt, P)); }

  // p[P](ms)
  MethodWithType _fromPms(Path P, MethodSelector ms) {
    MethodWithType res = p.extractClassB(P)._getMwt(ms);
    return res != null ? fromMwt(res, P) : null; }

  // mwt[from P][with e?=empty]
  static MethodWithType fromMwt(MethodWithType mwt, Path P) { return From.from(mwt.with_inner(null), P); }

  // Makes a Path from a Cs
  static Path toP(List<Ast.C> Cs) { return Path.outer(0, Cs); }

  /*Origin(p; sel; P) = P'
    forall P'' in Supertypes(p; P) where sel in dom [P'']
       p |- P'' <= P'*/
  Path _origin(MethodSelector ms, Path P) {
    var L = p.extractClassB(P);

    var mwt = L._getMwt(ms);
    if (mwt == null) { return null; }
    if (!mwt.getMt().isRefine()) { return P; }

    for (var P2 : iterate(fromPz(L, P))) {
      var mwt2 = p.extractClassB(P2)._getMwt(ms);
      if (mwt2 != null && !mwt2.getMt().isRefine()) {
        return P2; }}

    throw Assertions.codeNotReachable(); }

  // For debuging
  String asSubtype(CsPath csp) { return PathAux.as42Path(csp.getCs()) + " <= " + csp.getPath(); }
  String asSupertype(CsPath csp) { return csp.getPath() + " <= " + PathAux.as42Path(csp.getCs()); }
  String asReltype(CsPath csp)  { return PathAux.as42Path(csp.getCs()) + " <=> " + csp.getPath(); }
  String asSametype(CsPath csp)  { return PathAux.as42Path(csp.getCs()) + " == " + csp.getPath(); }
  String printConstraints() {
    return new ListFormatter().seperator(", ").append(this.redirectSet.keySet().stream().map(this::printConstraint))
        .toString();
  }
  String printConstraint(List<C> Cs) {
    var sub = supertypeConstraints.get(Cs);
    var sup = subtypeConstraints.get(Cs);
    if (sub.isEmpty() && sup.isEmpty()) {
      return ""; }
    else {
      if (sub.isEmpty()) {
        return PathAux.as42Path(Cs) + " <= " + PathAux.asSet(sup); }
      else {
        if (sup.isEmpty()) {
          return PathAux.asSet(sub) + " <= " + PathAux.as42Path(Cs); }
        else {
          return PathAux.asSet(sub) + " <= " + PathAux.as42Path(Cs) + " <= " + PathAux.asSet(sup);}}}}}

class CsPzMap implements Iterable<CsPath> {
  // Cs |-> Pz
  // (Cs, P)z
  private Map<List<C>, Set<Path>> map = new HashMap<>();

  @Override public String toString() {
    return this.mappings().stream().map(
        CsPz -> this.format(PathAux.as42Path(CsPz.getCs()), PathAux.asSet(CsPz.getPathsSet()))

    ).collect(Collectors.joining(", ")); }

  String format(String Cs, String Pz) { return Cs + "->" + Pz; }
  CsPzMap() { }
  CsPzMap(CsPzMap other) {this.map = new HashMap<>(other.map); }
  CsPzMap(List<CsPath> CsPz) { this(); for (var CsP : CsPz) { this.add(CsP); } }
  // Returns true iff the key, value pair wasn't allready present
  boolean add(CsPath keyValue) { return this.add(keyValue.getCs(), keyValue.getPath()); }

  boolean remove(List<C> key, Path value) {
    return this.update(key, () -> null, Pz -> Pz.contains(value), Pz -> Pz.remove(value)); }

  // Tries to update the value corresponding to key:
  //    If their was no value, and def() returns one, add that to the map
  //    Otherwise if condition holds on the current value, perform op
  //    If op failed, perform op on a copy of the original value
  // Returns true if the map was modified (under the assumption that 'op' will modify it's argument)
  private boolean update(List<C> key, Supplier<Set<Path>> def, Predicate<Set<Path>> condition, Consumer<Set<Path>> op) {
    var set = this.map.get(key);
    if (set == null) {
      var res = def.get();
      if (res == null || res.isEmpty()) { return false; }
      this.map.put(key, res);
      return true; }
    if (!condition.test(set)) { return false; }
    try { op.accept(set); }
    catch (UnsupportedOperationException __) {
      var res = new HashSet<>(set); op.accept(res); this.map.put(key, res); }
    return true; }

  public boolean add(List<C> key, Path value) {
    assert value.tryOuterNumber() != 0;
    return this.update(key, () -> Collections.singleton(value), Pz -> !Pz.contains(value), Pz -> Pz.add(value)); }

  public boolean contains(List<C> key, Path value) { return this.get(key).contains(value); }
  public boolean contains(List<C> key) { return !this.get(key).isEmpty(); }

  public Set<CsPz> mappings() {
    return this.map.entrySet().stream().map(e -> {
      assert !e.getValue().isEmpty();
      return new CsPz(e.getKey(), e.getValue());}
    ).collect(Collectors.toSet()); }

  public Set<Path> get(List<C> key) { return this.map.getOrDefault(key, Collections.emptySet()); }
  public Set<List<C>> dom() { return this.mappings().stream().map(CsPz::getCs).collect(Collectors.toSet()); }
  public Stream<CsPath> stream() {
    return this.mappings().stream().flatMap(e -> e.getPathsSet().stream().map(P -> new CsPath(e.getCs(), P))); }

  public Set<CsPath> values() { return this.stream().collect(Collectors.toSet()); }
  @Override public Iterator<CsPath> iterator() {
    // Who cares about performance anyway? What's more important is that I want to be able to iterate over something while modifying it!
    return this.values().iterator(); } }

@FunctionalInterface interface TriPredicate<T, U, V> { boolean test(T t, U u, V V); }
@FunctionalInterface interface QuadPredicate<T, U, V, W> { boolean test(T t, U u, V v, W w); }