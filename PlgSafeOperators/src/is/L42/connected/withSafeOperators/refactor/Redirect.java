package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ast.Ast;
import ast.Util;
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
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import programReduction.Program;
import tools.Assertions;

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
  static ClassB redirect(Program p, ClassB L, List<C> src, Path dest) { return redirect(p, L, new PathMap(List.of(new CsPath(src, dest)))); }
  static ClassB redirect(Program p_, ClassB L, PathMap R0) {
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
    return applyRedirect(p, R);
  }

  CsPzMap subtypeConstraints, supertypeConstraints;
  Map<List<C>, ClassKind> redirectSet = new HashMap<List<C>, ClassKind>();
  // ChooseRedirect(p; Cs1->P1, ..., Csn->Pn) = R'
  PathMap chooseRedirect(PathMap R) {
    //CCz0 = {Cs1 <= P1, P1 <= Cs1, ..., Csn <= Pn, Pn <= Csn}
    this.subtypeConstraints = new CsPzMap(R.toList());
    this.supertypeConstraints = new CsPzMap(this.subtypeConstraints);

    // Pre-compute the redirect set, mustClass and mustInterface
    computeRedirectSet(p.top(), R.dom());

    // CCz = CollectAll(p; CCz0)
    collect();

    // R' = ChooseR(p; CCz)
    return ChooseR();
    // TODO: ValidRedirect(p; R; R')
  }

  PathMap ChooseR() {
    PathMap res = new PathMap();
    //  ChooseR(p; Cs <= P0, ..., Cs <= Pn, CCz) := Cs -> P, ChooseR(p; CCz)
    //    P = MostSpecific(p; P0,...,Pn)
    // Marco Says to ignore this case

    for (var CsPz : this.supertypeConstraints.map.entrySet()) {
      var Cs = CsPz.getKey();
      var Ps1 = CsPz.getValue();
      assert !Ps1.isEmpty();
      var Ps2 = this.subtypeConstraints.get(CsPz.getKey());
      //ChooseR(p; P1 <= Cs, ..., Pn <= Cs, Cs <= P'1, ..., Cs <= P'k, CCz)

      // Pz = SuperClasses(p; P1, ..., Pn)
      var Pz = superClasses(Ps1);

      //Pz' = { P in Pz | {P'1, ..., P'k} subseteq SuperClasses(p; P)}
      var Pz2 = Pz.stream().filter(P -> superClasses(P).containsAll(Ps2)).collect(Collectors.toSet());

      // Pz'' = {P in Pz' | PossibleRedirect(p; Cs; P)}
      var Pz3 = Pz2.stream().filter(P -> possibleRedirect(Cs, P)).collect(Collectors.toSet());

      // P = MostSpecific(p; Pz'')
      var P = _mostSpecific(Pz3);
      if (P == null)
        throw new RuntimeException("Most specific failed!");
      res.map.put(Cs, P); }

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
    // TODO: Check mappings of external paths?

    var L1 = p.top().getClassB(Cs);
    var L2 = p.extractClassB(P);
    if (kind == ClassKind.Interface) {
      if (!L2.isInterface()) { return false; }
      if (!L2.msDom().equals(L1.msDom())) { return false; } }
    else {
      if (kind == ClassKind.Final && L2.isInterface()) { return false; }
      if (!L2.msDom().containsAll(L1.msDom())) { return false; } }

    return L2.cDom().containsAll(L1.cDom()); }

  Path _mostSpecific(Set<Path> Pz) {
    // TODO: This is horribly inefficient...
    for (Path P : Pz) {
      if (superClasses(P).containsAll(Pz)) { return P; }}

    return null;
  }
  Path _mostGeneral(Set<Path> Pz) {
      // TODO: This is horribly inefficient...
      var res = superClasses(Pz);
      res.retainAll(Pz);
      if (res.size() == 1) { return res.iterator().next(); }
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
        L2.mwts().stream().anyMatch(mwt -> mwt.getReturnMdf().isClass()) ? ClassKind.Final :
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
    boolean progress; // Have we done something?
    do {
      progress = false;
      //1: Collect(p; Cs <= P) = P <= Cs
      //   p[P].interface? = empty
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (!this.p.extractClassB(CsP.getPath()).isInterface()) { // p[P].interface? = empty
          progress |= supertypeConstraints.add(CsP.getCs(), CsP.getPath()); }} // P <= Cs

      //2: Collect(p; P <= Cs) = Cs <= P TODO: REMOVE
      //   MustClass(p; Cs)
      /*for (var CsP : supertypeConstraints) { // P <= Cs
        if (mustClass(CsP.getCs())) { // MustClass(p; Cs)
          progress |= subtypeConstraints.add(CsP.getCs(), CsP.getPath()); }}*/ // Cs <= P

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
            if (P1 != null && P2 != null) // P1 <= Cs', Cs' <= P2
              progress |= supertypeConstraints.add(PCs2.getCBar(), P1) | subtypeConstraints.add(PCs2.getCBar(), P2); }}}

      //4: Collect(p; P <= Cs) = p[P.ms].P <= Cs'
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : supertypeConstraints) { // P <= Cs
        progress |= collectReturns(CsP, supertypeConstraints::add);} // p[P.ms].P <= p[Cs.ms].P.Cs

      //5: Collect(p; CC) = Cs' <= p[P.ms].Pi
      //   CC = P <= Cs or  CC = Cs <= P
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : seqIterate(supertypeConstraints, subtypeConstraints)) { // P <= Cs or Cs <= P
        // p[Cs.ms].Pi.Cs <= p[P.ms].Pi
        progress |= collectParams(CsP, subtypeConstraints::add);}

      //6: Collect(p; Cs <= P) = Cs' <= p[P.ms].P:
      //   MustInterface(p; Cs)
      //   p[Cs.ms].P = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          // p[Cs.ms].P.Cs <= p[P.ms].P
          progress |= this.collectReturns(CsP, subtypeConstraints::add);}}

      //7: Collect(p; Cs <= P) = p[P.ms].Pi <= Cs':
      //   MustInterface(p; Cs)
      //   p[Cs.ms].Pi = This0.Cs'
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface(CsP.getCs())) { // MustInterface(CsP.Cs)
          progress |= this.collectParams(CsP, supertypeConstraints::add);}} // p[P.ms].Pi <= p[Cs.ms].Pi.Cs

      //8/8': Collect(p; Cs <= P, CCz) = CCz'
      for (var CsP : subtypeConstraints) { // Cs <= P
        BiPredicate<List<C>, Path> body = (Cs2, P2) -> {
          boolean progress2 = false;

          // 8a/8'a
          if (!p.extractClassB(P2).isInterface()) { // p[P'].interface?=empty and
            progress2 |= supertypeConstraints.add(Cs2, P2); } // CC = P' <= Cs'

          /*// 8b/8'b // TODO: Remove
          for (var P3 : subtypeConstraints.get(Cs2)) { // or Cs' <= P''  in CCz and
            if (!p.extractClassB(P3).isInterface()) { // p[P''].interface?=empty and
              progress2 |= subtypeConstraints.add(Cs2, P2); // CC = Cs'<=P'
              break; }}*/

          // 8c/8'c // TODO: Remove
          /*if (mustClass(Cs2)) { // or MustClass(Cs')
            progress2 |= subtypeConstraints.add(Cs2, P2); } */// CC = Cs' <= P'

          // 8d/8'd
          for (var ms : p.top().getClassB(Cs2).msDom()) { // or ms' in dom(p[Cs'])
            if (p.extractClassB(P2).msDom().contains(ms)) { // and ms' in dom(p[P'])
              //       CC = Cs' <= Origin(p; sel'; P')
              progress2 |= subtypeConstraints.add(Cs2, origin(ms, P2));}}

          return progress2;};

        // 8: This0.Cs' = p[Cs.sel].P
        //    P' = p[P.sel].P
        progress |= collectReturns(CsP, body);

        // 8': This0.Cs' in p[Cs].Pz
        //     P' in SuperClass(p; P)
        /*for (var P2 : iterate(this.fromPz(toP(CsP.getCs())))) { // P' in p[Cs].Pz
          if (P2.tryOuterNumber() == 0) { // P' = This0.Cs'
            for (var P3 : superClasses(CsP.getPath())) { // P'' in SuperClass(p; P)
              progress |= body.test(P2.getCBar(), P3); }}}*/}

      //9: Collect(p; Cs <= P, P <= Cs) = Cs.C <= P.C, P.C <= Cs.C
      //   C in dom(p[Cs])
      for (var CsP : subtypeConstraints) { // Cs <= P
        if (supertypeConstraints.contains(CsP.getCs(), CsP.getPath())) { // P <= Cs
          for (var C : this.p.top().getClassB(CsP.getCs()).cDom()) {// C in dom(p[Cs])
            var CsC = withAdd(CsP.getCs(), C);
            var PC = CsP.getPath().pushC(C);
            if (!p.extractClassB(CsP.getPath()).cDom().contains(C))
              throw new RuntimeException("Bad redirect: Nested class dosn't exist");

            // Cs.C <= P.C and P.C <= Cs.C
            progress |= subtypeConstraints.add(CsC, PC) || supertypeConstraints.add(CsC, PC); }}}

      //10: Collect(p; Cs,Csz; Cs.C <= P.C, P.C <= Cs.C) = Cs <= P, P <= Cs
      for (var CsCPC : subtypeConstraints) { // CsC <= PC
        var CsC = CsCPC.getCs();
        var PC  = CsCPC.getPath();
        if (PC.isCore() && supertypeConstraints.contains(CsC, PC)) { // PC = Thisk.Cs' and PC <= CsC
          var n1 = CsC.size();
          var n2 = PC.getCBar().size();
          if (n1 > 0 && n2 > 0) {
            var C  = CsC.get(n1 - 1);
            var Cs = CsC.subList(0, n1 - 1); // CsC = Cs.C
             // PC = P.C'
            var P = PC.popC();
            if (PC.getCBar().get(n2 - 1).equals(C) && redirectSet.containsKey(Cs)) { // C' = C and Cs in CsZ
              // Cs <= P and P <= Cs
              progress |= subtypeConstraints.add(Cs, P) || supertypeConstraints.add(Cs, P); }}}}}

    // Keep going untill we stop doing anything
    while (progress); }

  // collectReturns(Cs P, f) computes f(Cs', P'), such that
  //    This0.Cs' = p[Cs](ms).P
  //    P' = p[P](ms).P
  // and returns true if any of the calls to f did
  boolean collectReturns(CsPath CsP, BiPredicate<List<C>, Path> f) {
    boolean progress = false;
    for (var mwt : iterate(this.fromMwtz(toP(CsP.getCs())))) {
      var P2 = mwt.getReturnPath(); // P2 = mwt.P
      var mwt2 = this._fromPms(CsP.getPath(), mwt.getMs()); // mwt2? = p[P](mwt.ms)
      if (P2.tryOuterNumber() == 0 && mwt2 != null) { // P2 = This0.Cs', mwt2? != empty
        progress |= f.test(P2.getCBar(), mwt2.getReturnPath()); }} //f(P2.Cs, mwt2.P)
    return progress; }

  // collectParams(Cs P, f) same as collectReturns, but for each paramater type instead
  boolean collectParams(CsPath CsP, BiPredicate<List<C>, Path> f) {
    boolean progress = false;
    for (var mwt : iterate(this.fromMwtz(toP(CsP.getCs())))) {
      for (int i = 0; i < mwt.getSize(); i++) { // i in 0..#(mwt.ms.xs)
        var P2 = mwt.getPaths().get(i); // P2 = mwt.Pi
        var mwt2 = this._fromPms(CsP.getPath(), mwt.getMs()); // mwt2? = p[P](mwt.ms)
        if (P2.tryOuterNumber() == 0 && mwt2 != null) { // P2 = This0.Cs', mwt2? != empty
          progress |= f.test(P2.getCBar(), mwt2.getPaths().get(i)); }}} //f(P2.Cs, mwt2.Pi)
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
  Path origin(MethodSelector ms, Path P) { return Objects.requireNonNull(this._origin(ms, P));}

  Path _origin(MethodSelector ms, Path P) {
    // TODO: Ask marco if this is correct
    var L = p.extractClassB(P);
    var mwt = L._getMwt(ms);
    if (mwt == null) { return null; }

    if (!mwt.getMt().isRefine()) { return P; }
    for (var P2 : L.getSuperPaths()) {
      var P3 = this._origin(ms, From.fromP(P2, P));
      if (P3 != null) return P3; }
    throw Assertions.codeNotReachable(); };
}

class CsPzMap implements Iterable<CsPath>
{
  // Cs |-> Pz
  // (Cs, P)z
  Map<List<C>, Set<Path>> map = new HashMap<>();

  @Override public String toString() {
    return this.map.entrySet().stream().map(CsPz -> PathAux.as42Path(CsPz.getKey()) + "->" +
      "{" + CsPz.getValue().stream().map(Object::toString).collect(Collectors.joining(",")) + "}"
    ).collect(Collectors.joining(",")); }

  CsPzMap() { }
  CsPzMap(CsPzMap other) {this.map = new HashMap<>(other.map); }
  CsPzMap(List<CsPath> CsPz) { this(); for (var CsP : CsPz) { this.add(CsP); } }
  // Returns true iff the key, value pair wasn't allready present
  boolean add(CsPath keyValue) { return this.add(keyValue.getCs(), keyValue.getPath()); }
  boolean add(List<C> key, Path value)
  {
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

    return true;
  }

  public boolean contains(List<C> key, Path value) {
    Set<Path> Pz = this.map.get(key);
    return Pz != null && Pz.contains(value);
  }
  public Set<Path> get(List<C> key) { return this.map.getOrDefault(key, Collections.emptySet()); }
  public Stream<CsPath> stream() {
    return this.map.entrySet().stream()
      .flatMap(e -> e.getValue().stream().map(P -> new CsPath(e.getKey(), P))); }
  public Set<CsPath> values() { return this.stream().collect(Collectors.toSet()); }
  @Override public Iterator<CsPath> iterator() {
    // Who cares about performance anyway? What's more important is that I want to be able to iterate over something while modifying it!
    return this.values().iterator(); }
}