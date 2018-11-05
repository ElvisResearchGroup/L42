package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ast.Ast;
import ast.ErrorMessage;
import ast.L42F;
import ast.Util.CsSPath;
import ast.Ast.MethodSelector;
import ast.Util.CsPath;
import ast.Ast.Path;
import ast.Ast.C;

import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.PathAux;
import com.sun.jdi.Value;
import coreVisitors.From;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.RedirectError;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import newTypeSystem.TypeSystem;
import programReduction.Program;
import tools.Assertions;

import javax.management.RuntimeErrorException;

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
    //return redirect(pData.p, that, src, dest);
    }

  Program p;
  static public ClassB applyRedirect(Program p, PathMap map) {
    //TODO: use the new renaming?
    ClassB L = map.apply(p.top());
    L = PathMap.remove(L, map.dom());
    return L;
  }

  enum ClassKind { Final, Interface, Class }

  Redirect(Program p) { this.p = p; }
  static ClassB redirect(Program p, ClassB L, List<C> src, Path dest) throws RedirectError {
    return redirect(p, L, new PathMap(List.of(new CsPath(src, dest)))); }

  static ClassB redirect(Program p_, ClassB L, PathMap R0) throws RedirectError {
    var p = p_.evilPush(L);
    // TODO: Check that empty not in Cs1 ... Csn // We can't redirect This0, as that does not make sense
    var redirecter = new Redirect(p);
    //p' = p.evilPush(L) // to simplify everything, just make L the new top

    // TODO: forall Cs in RedirectSet(L, Cs1, ..., Csn):
    //      Redirectable(L[Cs]) // Check that the source is valid

    // Chose a mapping, simplifying the input, and update the paths to be relative to our evil-pushed program
    //    ChooseRedirect(p'; Cs1, p'.minimize(P1[from This1]), ..., Csn, p'.minimize(Pn[from This1])) = R
    var R = redirecter.chooseRedirect(new PathMap(R0.stream().map(
        CsP -> new CsPath(CsP.getCs(), p.minimize(From.fromP(CsP.getPath(), Path.outer(1))))
    ).collect(Collectors.toList())));

    // R(p'.top()[remove Csz])
    return PathMap.remove(redirecter.p.top(), R.dom());
  }

  CsPzMap subtypeConstraints, supertypeConstraints;
  Map<List<C>, ClassKind> redirectSet = new HashMap<List<C>, ClassKind>();
  // ChooseRedirect(p; Cs1->P1, ..., Csn->Pn) = R'
  PathMap chooseRedirect(PathMap R) throws RedirectError {
    // Check that each Cs in the input actually exists!
    var EB = new ErrorBuilder("Input to redirect is invalid:\n", "  ", "\n", "", "");
    for (var CsP : R) {
      var Cs = CsP.getCs();
      try { p.top().getClassB(Cs); }
      catch (ErrorMessage.PathMetaOrNonExistant __) {
        EB.record("Cannot redirected non-existent nested class " + PathAux.as42Path(Cs) + "."); }}
    var errors = EB.toString();
    if (!errors.isEmpty()) {
      // TODO: Pass the list of valid Cs's
      throw new RefactorErrors.RedirectError(new ArrayList<>(), new ArrayList<>(), errors); }

    //CCz0 = {Cs1 <= P1, P1 <= Cs1, ..., Csn <= Pn, Pn <= Csn}
    this.subtypeConstraints = new CsPzMap(R.toList()) {
      @Override String format(String Cs, String Pz) {return Cs + " <= " + Pz; }};
    this.supertypeConstraints = new CsPzMap(this.subtypeConstraints) {
      @Override String format(String Cs, String Pz) {return Pz + " <= " + Cs; }};

    // Pre-compute the redirect set, mustClass and mustInterface
    computeRedirectSet(p.top(), R.dom());
    errors = verifyRedirectable();
    if (!errors.isEmpty()) {
      throw new RefactorErrors.RedirectError(new ArrayList<>(redirectSet.keySet()), new ArrayList<>(), errors); }

    // CCz = CollectAll(p; CCz0)
    collect();

    // R' = ChooseR(p; CCz)
    var R1 = chooseR();
    //completeR(R1);
    System.out.println("Chosen R was: " + R1);


    this.p = p.updateTop(R1.apply(p.top()));
    errors = validRedirect(R, R1);
    if (!errors.isEmpty()) {
      var Csz1 = new ArrayList<List<C>>();
      var Csz2 = new ArrayList<List<C>>();
      var Pz = new ArrayList<Path>();
      for (var Cs : redirectSet.keySet()) {
       var P = R1._get(Cs);
       if (P == null) {
         Csz2.add(Cs); }
       else {
        Csz1.add(Cs);
        Pz.add(P); }}
      Csz1.addAll(Csz2);
      throw new RefactorErrors.RedirectError(Csz1, Pz, errors); }

    return R1;
  }

  String verifyRedirectable() {
    var result = new ErrorBuilder("Requested redirect is invalid:\n", "  ", "\n", "", "");
    /*forall Cs in dom(R'):
      ...
      Redirectable(Cs, p.top()) */

    // Since we require that dom(R') = RedirectSet(...)
    // We will just compute this on the latter

    for (var Cs : this.redirectSet.keySet()) {
      var L = this.p.top().getClassB(Cs);
      var errors = new ErrorBuilder("Cannot redirect " + PathAux.as42Path(Cs) + ":\n", "    ", "\n", "", "");
      if (Cs.isEmpty()) { // Cs not empty
        errors.record("You cant redirect everything!"); }

      if (Cs.stream().anyMatch(C::isUnique)) { // forall C in Cs: not Private(Cs)
        errors.record("Cannot redirect a private nested class"); }

      for (var mwt : L.mwts()) { // forall mwt in L[Cs].mwtz:
        var ms = mwt.getMs();
        var mwtErrors = new ErrorBuilder("Cannot redirect " + ms + ": it is ", "", "", " and ", ".");
        if (ms.isUnique()) { // not Private(mwt.m)
          mwtErrors.record("private"); }
        if (mwt.get_inner() != null) { // mwt.e = empty
          mwtErrors.record("implemented"); }
        errors.record(mwtErrors.toString()); }
      result.record(errors.toString()); }
    return result.toString(); }

  String validRedirect(PathMap R0, PathMap R) {
    var result = new ErrorBuilder("Choosen redirect is invalid:\n", "  ", "\n", "", "");
    for (var Cs : redirectSet.keySet()) { //dom(R) = RedirectSet(p.top(); dom(R0))
      if (!R.dom().contains(Cs)) {
        result.record("No mapping for " + PathAux.as42Path(Cs) + " found");  }}

    // Check structural subtyping (including NC consistency)
    for (var CsP : R) {
      var P = CsP.getPath();
      var Cs = CsP.getCs();
      ClassKind ck = redirectSet.get(Cs);

      var errors = new ErrorBuilder("Cannot redirect " + PathAux.as42Path(Cs) + " to " + P + ":\n", "    ", "", "\n", "");
      var P2 = R0._get(Cs);

      if (P2 != null && !P2.equals(P)) {//R0 subseteq R
        errors.record("Mapping is inconsistent with input target: " + P2 + ".");}

      if (!p.minimize(P).equals(P)) { // p.minimize(P)=P
        errors.record("The target is not minimized, expected " + p.minimize(P) + "."); }
      if (P.tryOuterNumber() == 0) { // P not of form This0._
        errors.record("The target is internal!"); }
      var L1 = p.top().getClassB(Cs);
      var L2 = p.extractClassB(P);

      // No need to check for well-typedness of L2, as 42 gurantees this for any 'class Any' given as input is (transitivley) well-typed

      /*for (var C : L1.cDom()) { // forall C in dom(p(Cs)): R(Cs.C) = R(Cs).C
        if (!L2.cDom().contains(C)) {
          errors.record("Target does not contain nested class " + C + ".");
          continue; }

        var CsC = withAdd(Cs, C);
        var PC2 = R._get(CsC);
        var PC = P.pushC(C);
        if (PC2 != null && !PC.equals(PC2)) { // TODO: Do a p.equiv instead?
          errors.record("Inconsistent mapping for nested class " + C + ", " + "expected target " + PC + ", but got " + PC2 + ".");} }
        */
      //    p|- P; L2 <= Cs; L1
      var Pz2 = superClasses(P);
      var Pz1 = p.minimizeSet(fromPz(L2, toP(Cs)));
      if (!Pz2.containsAll(Pz1)) { //Pz subseteq_p SuperClasses(p; P)
        errors.record("Target is a subclass of " + PathAux.asSet(Pz2) + ", and not all of " + PathAux.asSet(Pz1)); }

      // Check class compatability
      if (ck.equals(ClassKind.Final) && L2.isInterface()) {
        errors.record("Cannot redirect a final class, with class methods, to an interface"); }
      if (ck.equals(ClassKind.Interface) && !L2.isInterface()) {
        errors.record("Cannot redirect an interface to a final class"); }

      //forall MS in dom(mwtz): p |- mwtz'(MS).mt <= mwt(MS).mt
      for (var mwt1 : iterate(fromMwtz(L1, toP(Cs)))) {
        var mwt2 = L2._getMwt(mwt1.getMs());
        if (mwt2 == null) {
          errors.record("Target does not contain method " + mwt1.getMs());
          continue; }
        mwt2 = fromMwt(mwt2, P);
        if (!TypeSystem.methTSubtype(p, mwt2.getMt(), mwt1.getMt())) {
          errors.record("The method type for " + mwt1.getMs() + " of the target (" + mwt2.getMt() +
          ") is not a subtype of the source (" + mwt1.getMt() + ")."); }}

      // mwtz[with refine?s=empty] = mwtz'[with refine?s=empty]
      // (implied by the above check together with this one)
      if (ck.equals(ClassKind.Interface)) {
        for (var mwt2 : iterate(fromMwtz(L2, P))) {
          var mwt1 = L1._getMwt(mwt2.getMs());
          if (mwt1 == null) {
            errors.record("Source does not contain method " + mwt2.getMs());
            continue; }
          mwt1 = fromMwt(mwt1, toP(Cs));
          if (!TypeSystem.methTSubtype(p, mwt1.getMt(), mwt2.getMt())) {
            errors.record("The method type for " + mwt2.getMs() + " of the target (" + mwt2.getMt() +
            ") is no // TODO: OK, due to F-bounded polymorphismt a supertype of the source (" + mwt1.getMt() + ")."); }}}

      result.record(errors.toString()); }

    // Note: the redirectable check will be done imediatley after we compute the redirectset
    return result.toString();}

  PathMap chooseR() {
    PathMap res = new PathMap();
    //  ChooseR(p; Cs <= P0, ..., Cs <= Pn, CCz) := Cs -> P, ChooseR(p; CCz)
    //    P = MostSpecific(p; P0,...,Pn)
    // Marco Says to ignore this case
    for (var CsPz : this.supertypeConstraints.mappings()) {
      var Cs = CsPz.getCs();
      var Ps1 = CsPz.getPathsSet();
      assert !Ps1.isEmpty();
      var Ps2 = this.subtypeConstraints.get(Cs);
      //ChooseR(p; P1 <= Cs, ..., Pn <= Cs, Cs <= P'1, ..., Cs <= P'k, CCz)

      // Pz = SuperClasses(p; P1, ..., Pn)
      var Pz = superClasses(Ps1);

      //Pz' = { P in Pz | {P'1, ..., P'k} subseteq SuperClasses(p; P)}
      var Pz2 = Pz.stream().filter(P -> superClasses(P).containsAll(Ps2)).collect(Collectors.toSet());

      // Pz'' = {P in Pz' | PossibleRedirect(p; Cs; P)}
      var Pz3 = Pz2.stream().filter(P -> possibleRedirect(Cs, P)).collect(Collectors.toSet());

      // P = MostSpecific(p; Pz'')
      var P = _mostSpecific(Pz3);
      if (P == null) {
        // If there was an error, lets still continue, so we can see what mappings could be worked out
        System.out.println("Most Specific for " + PathAux.as42Path(Cs) + " failed on: " + PathAux.asSet(Pz3) + " "); }
      else {
        res.add(Cs, P); }}
    return res; }

  boolean possibleRedirect(List<C> Cs, Path P) {
    /*PossibleRedirect(p; Cs; P) iff
        if MustInterface(p; Cs)
            p[P].interface? = interface
            dom(p[P].mwtz) = dom(p[Cs].mwtz) // No extra methods
        if MustClass(p; Cs)
            p[P].interface? = empty
        dom(p[Cs]) subseteq dom(p[P])
        // TODO: Refine this more?*/
    var kind = redirectSet.get(Cs);
    assert kind != null;

    // TODO: Recursivley check nested classes?
    // TODO: Check type modifiers?

    var L1 = p.top().getClassB(Cs);
    var L2 = p.extractClassB(P);
    if (kind == ClassKind.Interface) {
      if (!L2.isInterface()) { return false; }
      if (!L2.msDom().equals(L1.msDom())) { return false; }
    } else {
      if (kind == ClassKind.Final && L2.isInterface()) { return false; }
      if (!L2.msDom().containsAll(L1.msDom())) { return false; }}

    return true;}
    // Recurislvey check nested classes!
    //return L1.cDom().stream().allMatch(C -> L2.cDom().contains(C) && possibleRedirect(withAdd(Cs, C), P.pushC(C)));}

  /*void completeR(PathMap R) {
    for (var CsP : R) {
      for (var C : p.top().getClassB(CsP.getCs()).cDom()) {
        var P = CsP.getPath();
        var CsC = withAdd(CsP.getCs(), C);
        //var P2 = CsP.getPath().pushC(Cs)
        if (!R.contains(CsC) && p.extractClassB(P).cDom().contains(C)) {
          R.add(CsC, P.pushC(C));
          completeR(R);
          return; }}}}*/

  Path _mostSpecific(Set<Path> Pz) {
    // TODO: This is horribly inefficient...
    for (Path P : Pz) {
      if (superClasses(P).containsAll(Pz)) { return P; }}

    return null;
  }
  Path _mostGeneral(Set<Path> Pz) {
      // TODO: This is horribly inefficient...
      if (!Pz.isEmpty()) {
        var res = superClasses(Pz);
        res.retainAll(Pz);
        if (res.size() == 1) { return res.iterator().next(); } }

      return null;
    }

  void computeRedirectSet(ClassB L, Collection<List<C>> Csz) {
    var todo = new ArrayList<List<C>>(Csz);
    Consumer<Path> accumulate = P -> {
      if (P.tryOuterNumber() == 0 && !redirectSet.containsKey(P.getCBar())) {
        todo.add(P.getCBar()); }};

    // TODO: Do p.minimizes?
    while (!todo.isEmpty()) {
      var Cs = todo.get(0);
      todo.remove(0);
      ClassB L2 = L.getClassB(Cs);

      ClassKind kind = L2.isInterface() ? ClassKind.Interface :
        L2.mwts().stream().anyMatch(mwt -> mwt.getMdf().isClass()) ? ClassKind.Final :
        ClassKind.Class;
      redirectSet.put(Cs, kind);

      for (var mwt : iterate(fromMwtz(L2, toP(Cs)))) {
        accumulate.accept(mwt.getReturnPath());
        mwt.getPaths().forEach(accumulate);
        mwt.getExceptions().forEach(accumulate); }

      L2.ns().forEach(nc -> accumulate.accept(Path.outer(0, Cs).pushC(nc.getName())));
      fromPz(L2, toP(Cs)).forEach(accumulate); }}

  boolean mustClass(List<Ast.C> Cs) { return this.redirectSet.get(Cs) == ClassKind.Final; }
  boolean mustInterface(List<Ast.C> Cs) { return this.redirectSet.get(Cs) == ClassKind.Interface; }

  void collect() {
    System.out.println("\nStart: " + this.constraints());
    boolean progress; // Have we done something?
    do {
      progress = false;
      //1: Collect(p; Cs <= P) = P <= Cs
      //   p[P].interface? = empty
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (!this.p.extractClassB(CsP.getPath()).isInterface()) { // p[P].interface? = empty
          progress |= supertypeConstraints.add(CsP.getCs(), CsP.getPath(), "Rule 1: " + asSubtype(CsP)); }} // P <= Cs

      //3: Collect(p; P <= Cs) = MostSpecific(p; Pz) <= Cs', Cs' <= MostGeneral(p; Pz)
      //   This0.Cs' in p[Cs].Pz
      //   Pz = {P' in SuperClasses(p; P) | msdom(p[P') = msdom(p[Cs'])}
      for (var CsP : supertypeConstraints) { // P <= Cs
        for (var PCs2 : iterate(this.fromPz(toP(CsP.getCs())))) { // P in p[Cs].Pz
          if (PCs2.tryOuterNumber() == 0) { // P = This0.Cs'
            var msz = p.extractClassB(PCs2).msDom(); // msz = msdom(p[Cs'])
            var Pz = superClasses(CsP.getPath()).stream().filter(Pi -> p.extractClassB(Pi).msDom().equals(msz))
              .collect(Collectors.toSet()); // px = {P' in SuperClasses(p; P) | msdom(p[P']) = msz}
            var P1 = _mostSpecific(Pz); // P1 = MostSpecific(p; Pz)
            var P2 = _mostGeneral(Pz); // P2 = MostGeneral(p; Pz)
            if (P1 != null && P2 != null) { // P1 <= Cs', Cs' <= P2
              var message = "Rule 3: " + asSupertype(CsP);
              progress |= supertypeConstraints.add(PCs2.getCBar(), P1, message) | subtypeConstraints.add(PCs2.getCBar(), P2, message); }}}}

      //4: Collect(p; P <= Cs) = p[P.ms].P <= Cs'
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : supertypeConstraints) { // P <= Cs
        progress |= collectReturns(CsP, (Cs, P, ms) -> supertypeConstraints.add(Cs, P,
          "Rule 4: " + asSupertype(CsP) + " [" + ms + "]"));} // p[P.ms].P <= p[Cs.ms].P.Cs

      //5: Collect(p; CC) = Cs' <= p[P.ms].Pi
      //   CC = P <= Cs or  CC = Cs <= P
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : seqIterate(supertypeConstraints, subtypeConstraints)) { // P <= Cs or Cs <= P
        // p[Cs.ms].Pi.Cs <= p[P.ms].Pi
        progress |= collectParams(CsP, (Cs, P, ms, i) -> subtypeConstraints.add(Cs, P,
            "Rule 5: " + asReltype(CsP) + " [" + ms + "." + i + "]"));}

      //6: Collect(p; Cs <= P) = Cs' <= p[P.ms].P:
      //   MustInterface(p; Cs)
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          // p[Cs.ms].P.Cs <= p[P.ms].P
          progress |= collectReturns(CsP, (Cs, P, ms) -> subtypeConstraints.add(Cs, P,
            "Rule 6: " + asSubtype(CsP) + " [" + ms + "]"));}}

      //7: Collect(p; Cs <= P) = p[P.ms].Pi <= Cs':
      //   MustInterface(p; Cs)
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          progress |= collectParams(CsP, (Cs, P, ms, i) -> supertypeConstraints.add(Cs, P,
              "Rule 7: " + asSubtype(CsP) + " [" + ms + "." + i + "]"));}} // p[P.ms].Pi <= p[Cs.ms].Pi.Cs

      //8/8': Collect(p; Cs <= P, CCz) = CCz'
      for (var CsP : subtypeConstraints) { // Cs <= P
        TriPredicate<List<C>, Path, String> body = (Cs2, P2, message) -> {
          boolean progress2 = false;

          // 8a/8'a
          if (!p.extractClassB(P2).isInterface()) { // p[P'].interface?=empty and
            progress2 |= supertypeConstraints.add(Cs2, P2, message); } // CC = P' <= Cs'

          // 8d/8'd
          for (var ms : p.top().getClassB(Cs2).msDom()) { // or ms' in dom(p[Cs'])
            var P3 = _origin(ms, P2); // P'' = Origin(p; sel'; P')
            if (P3 != null) {
              progress2 |= subtypeConstraints.add(Cs2, P3, message + " [" + ms + "]");}}  // CC = Cs' <= P''

          return progress2;};

        // 8: This0.Cs' = p[Cs.sel].P
        //    P' = p[P.sel].P
        progress |= collectReturns(CsP, (Cs, P, ms) -> body.test(Cs, P, "Rule 8: " + asSubtype(CsP) + " [" + ms + "]"));

        // 8': This0.Cs' in p[Cs].Pz
        //     P' in SuperClass(p; P)
        /*for (var P2 : iterate(this.fromPz(toP(CsP.getCs())))) { // P' in p[Cs].Pz
          if (P2.tryOuterNumber() == 0) { // P' = This0.Cs'
            for (var P3 : superClasses(CsP.getPath())) { // P'' in SuperClass(p; P)
              progress |= body.test(P2.getCBar(), P3, "Rule 8': " + asSubtype(CsP)); }}}*/}

      //9: Collect(p; Cs <= P, P <= Cs) = Cs.C <= P.C, P.C <= Cs.C
      //   C in dom(p[Cs])
      /*for (var CsP : subtypeConstraints) { // Cs <= P
        if (supertypeConstraints.contains(CsP.getCs(), CsP.getPath())) { // P <= Cs
          for (var C : this.p.top().getClassB(CsP.getCs()).cDom()) {// C in dom(p[Cs])
            var CsC = withAdd(CsP.getCs(), C);
            var PC = CsP.getPath().pushC(C);
            // Path dosn't exist (ValidRedirect will produce an appropriate error message)
            if (!p.extractClassB(CsP.getPath()).cDom().contains(C)) { continue; }

            var message = "Rule 9: " + asSametype(CsP);
            // Cs.C <= P.C and P.C <= Cs.C
            progress |= subtypeConstraints.add(CsC, PC, message) | supertypeConstraints.add(CsC, PC, message); }}}
         */
      //10: Collect(p; Cs,Csz; Cs.C <= P.C, P.C <= Cs.C) = Cs <= P, P <= Cs
      
      /*//find all the Cs.C in both      
      for (var CsCPC : subtypeConstraints) { // CsC <= PC
        var CsC = CsCPC.getCs();
        var PC  = CsCPC.getPath();
        if (!PC.isCore() || !supertypeConstraints.contains(CsC, PC)) { continue; } 
        var n1 = CsC.size();
        var n2 = PC.getCBar().size();
        if (n1 == 0 || n2 == 0) { continue; }
        var C  = CsC.get(n1 - 1);
        var Cs = CsC.subList(0, n1 - 1);
        var P = PC.popC();
        if (!PC.getCBar().get(n2 - 1).equals(C) || !redirectSet.containsKey(Cs)) { continue; }
        var message = "Rule 10: " + asSametype(CsCPC);
        progress |= subtypeConstraints.add(Cs, P, message) | supertypeConstraints.add(Cs, P, message); }}
       */ }
    // Keep going untill we stop doing anything
    while (progress);
    System.out.println("End: " + this.constraints() + "\n"); }

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
          return inner.next(); }
    };}

  static<T> boolean intersects(Set<T> a, Set<T> b) { return a.stream().anyMatch(b::contains); }

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
  String constraints() {
    assert this.redirectSet.keySet().containsAll(subtypeConstraints.dom());
    assert this.redirectSet.keySet().containsAll(supertypeConstraints.dom());
    return this.redirectSet.keySet().stream().map(Cs -> {
      if (subtypeConstraints.contains(Cs) && supertypeConstraints.contains(Cs)) {
        return PathAux.asSet(supertypeConstraints.get(Cs)) + " <= " + PathAux.as42Path(Cs) +
            " <= " + PathAux.asSet(subtypeConstraints.get(Cs));
      } else if (subtypeConstraints.contains(Cs)) {
        return PathAux.as42Path(Cs) + " <= " + PathAux.asSet(subtypeConstraints.get(Cs));
      } else if (supertypeConstraints.contains(Cs)) {
        return PathAux.asSet(supertypeConstraints.get(Cs)) + " <= " + PathAux.as42Path(Cs);
      } else {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.joining(", "));
  }
}

class ErrorBuilder {
  private StringBuilder result = new StringBuilder();
  private String header, prefix, suffix, seperator, footer;
  boolean empty = true;
  ErrorBuilder(String header, String prefix, String suffix, String seperator, String footer) {
    this.header = header;
    this.prefix = prefix;
    this.suffix = suffix;
    this.seperator = seperator;
    this.footer = footer; }

  void record(String message) {
    if (message.isEmpty()) { return; }
    if (this.empty) { this.result.append(header); this.empty = false; }
    else { this.result.append(seperator); }
    this.result.append(prefix).append(message).append(suffix); }

  // returns an empty string if 'record' was never called
  // Otherwise, returns:
  //    header + (prefix + msg1 + suffix) + seperator + ... + seperator + (prefix + msgn + suffix) + footer
  // where msg1 ... msgn where the non-empty arguments passed to record (in order)
  @Override public String toString() {
    var r = this.result.toString();
    if (!r.isEmpty()) { r = r + footer; }
    return r; }}

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

  boolean add(List<C> key, Path value, String message) {
    var res = this.add(key, value);
    if (res) System.out.println(message + " ==> " + this.format(PathAux.as42Path(key), value.toString()));
    return res; }

  private boolean add(List<C> key, Path value) {
    assert value.tryOuterNumber() != 0;
    Set<Path> orig = this.map.get(key);
    if (orig != null) {
      if (orig.contains(value))
        return false;
      else {
        try { orig.add(value); }
        // In case the set happens to be unmodifiable...
        catch (UnsupportedOperationException __) {
          var res = new HashSet<Path>(orig);
          res.add(value);
          this.map.put(key, res); } }
    } else {
      this.map.put(key, Collections.singleton(value)); }

    return true; }

  public boolean contains(List<C> key, Path value) { return this.get(key).contains(value); }
  public boolean contains(List<C> key) { return !this.get(key).isEmpty(); }

  public Set<CsSPath> mappings() {
    return this.map.entrySet().stream().map(e -> {
      assert !e.getValue().isEmpty();
      return new CsSPath(e.getKey(), e.getValue());}
    ).collect(Collectors.toSet()); }

  public Set<Path> get(List<C> key) { return this.map.getOrDefault(key, Collections.emptySet()); }
  public Set<List<C>> dom() { return this.mappings().stream().map(CsSPath::getCs).collect(Collectors.toSet()); }
  public Stream<CsPath> stream() {
    return this.mappings().stream().flatMap(e -> e.getPathsSet().stream().map(P -> new CsPath(e.getCs(), P))); }

  public Set<CsPath> values() { return this.stream().collect(Collectors.toSet()); }
  @Override public Iterator<CsPath> iterator() {
    // Who cares about performance anyway? What's more important is that I want to be able to iterate over something while modifying it!
    return this.values().iterator(); } }

@FunctionalInterface interface TriPredicate<T, U, V> { boolean test(T t, U u, V V); }
@FunctionalInterface interface QuadPredicate<T, U, V, W> { boolean test(T t, U u, V v, W w); }