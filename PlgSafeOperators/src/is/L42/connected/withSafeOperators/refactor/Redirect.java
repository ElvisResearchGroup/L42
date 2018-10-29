package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast;
import ast.Util;
import ast.Util.CsPath;
import ast.Ast.Path;
import ast.Ast.C;

import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import programReduction.Program;

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
    if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}
    return new is.L42.connected.withSafeOperators.refactor.RedirectObj(that).redirect(pData.p,src, dest);
    }

  Program p;
  static public ClassB applyRedirect(Program p, PathMap map) {
    //TODO: use the new renaming?
    ClassB L = map.apply(p.top());
    L = PathMap.only(L, map.dom());
    return L;
  }

  enum ClassKind { Final, Interface, Class }


  // ChooseRedirect(p; Cs1->P1, ..., Csn->Pn) = V'
  PathMap chooseRedirect(PathMap V) {
    //CCz0 = {Cs1 <= P1, P1 <= Cs1, ..., Csn <= Pn, Pn <= Csn}
    CsPzMap map = new CsPzMap();
    for (CsPath CsP : V) { map.add(CsP); }

    // Pre-compute the redirect set, mustClass and mustInterface
    Map<List<C>, ClassKind> redirectSet = null;

    // CCz = CollectAll(p; CCz0)
    this.collect(map, new CsPzMap(map), redirectSet);

    // V' = ChooseV(p; CCz)
    // ValidRedirect(p; V; V')
    return null;
  }

  // Returns true if it did something

  void collect(CsPzMap subtypeConstraints, CsPzMap supertypeConstraints, Map<List<C>, ClassKind> redirectSet) {
    Predicate<List<Ast.C>> mustClass = Cs -> redirectSet.get(Cs) == ClassKind.Final;
    Predicate<List<Ast.C>> mustInterface = Cs -> redirectSet.get(Cs) == ClassKind.Interface;

    boolean progress = false; // Have we done something?
    do {
      // Collect(p; Cs <= P) = P <= Cs
      //   p[P].interface? = empty
      for (CsPath CsP : subtypeConstraints) { // Cs <= P
        if (!this.p.extractClassB(CsP.getPath()).isInterface()) { // p[P].interface? = empty
          progress |= supertypeConstraints.add(CsP.getCs(), CsP.getPath()); }} // P <= Cs

      // Collect(p; P <= Cs) = Cs <= P
      //   MustClass(p; Cs)
      for (CsPath CsP : supertypeConstraints) { // P <= Cs
        if (mustClass.test(CsP.getCs())) { // MustClass(p; Cs)
          progress |= subtypeConstraints.add(CsP.getCs(), CsP.getPath()); }} // Cs <= P

      // Collect(p; P <= Cs) = Cs <= P'
      // Collect(p; P <= Cs) = Cs <= P'
      //   This0.Cs' in p[Cs].Pz
      //   P' in SuperClasses(p; P)
      //   dom(p[Cs'].mwtz) intersects dom(p[P'].mwtz)
      for (CsPath CsP : supertypeConstraints) { // P <= Cs
        for (Path P : p.top().getClassB(CsP.getCs()).getSuperPaths()) { // P in p[Cs].Pz
          if (P.tryOuterNumber() == 0) { // P = This0.Cs'
            for (Path P2 : this.superClasses(P)) // P2 in SuperClasses(p; P)
              if (intersects(p.extractClassB(P).msDom(), p.extractClassB(P2).msDom())) // mdom(p[P]) intersects mdom(p[P2])
                progress |= subtypeConstraints.add(CsP.getCs(), P2); }}}


      BiPredicate<CsPath, CsPzMap> addReturns = (CsP, map) -> {
        boolean res = false;
        for (MethodWithType mwt : this.p.extractClassB(CsP.getPath()).mwts()) { // mwt in p[Cs]
         Path P2 = mwt.getReturnPath(); // P2 = mwt.P
         MethodWithType mwt2 = p.extractClassB(CsP.getPath())._getMwt(mwt.getMs()); // mwt2? = p[P](mwt.ms)
         if (P2.tryOuterNumber() == 0 && mwt2 != null) { // P2 = This0.Cs', mwt2? != empty
           res |= map.add(P2.getCBar(), mwt2.getReturnPath()); }} // map.add(P2.Cs -> mwt2.P)
        return res; };

      BiPredicate<CsPath, CsPzMap> addArgs = (CsP, map) -> {
        boolean res = false;
        for (MethodWithType mwt : this.p.fromClassB(CsP.getPath()).mwts()) { // mwt in p[Cs]
         for (int i = 0; i < mwt.getSize(); i++) { // i in 0..#(mwt.ms.xs)
           Path P2 = mwt.getPaths().get(i); // P2 = mwt.Pi
           MethodWithType mwt2 = p.fromClassB(CsP.getPath())._getMwt(mwt.getMs()); // mwt2? = p[P](mwt.ms)
           if (P2.tryOuterNumber() == 0 && mwt2 != null) {
             res |= map.add(P2.getCBar(), mwt2.getPaths().get(i)); }}} // map.add(P2.Cs -> mwt2.Pi)
        return res; };

      // Collect(p; P <= Cs) = p[P.ms].P <= Cs'
      //   p[Cs.ms].P = This0.Cs'
      for (CsPath CsP : supertypeConstraints) { // P <= Cs
        progress |= addReturns.test(CsP, supertypeConstraints); } // p[P.ms].P <= p[Cs.ms].P.Cs

      // Collect(p; CC) = Cs' <= p[P.ms].Pi
      //   CC = P <= Cs or  CC = Cs <= P
      //   p[Cs.ms].Pi = This0.Cs'
      for (CsPath CsP : iterate(Stream.concat(supertypeConstraints.stream(), subtypeConstraints.stream()))) {
        // P <= Cs or Cs <= P
        progress |= addArgs.test(CsP, subtypeConstraints); } // p[Cs.ms].Pi.Cs <= p[P.ms].Pi

      // Collect(p; Cs <= P) = Cs' <= p[P.ms].P:
      //   MustInterface(p; Cs)
      //   p[Cs.ms].P = This0.Cs'
      for (CsPath CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface.test(CsP.getCs())) { // MustInterface(CsP.Cs)
          progress |= addReturns.test(CsP, subtypeConstraints); }} // p[Cs.ms].P.Cs <= p[P.ms].P

      // Collect(p; Cs <= P) = p[P.ms].Pi <= Cs':
      //   MustInterface(p; Cs)
      //   p[Cs.ms].Pi = This0.Cs'
      for (CsPath CsP : subtypeConstraints) { // Cs <= P
        if (mustInterface.test(CsP.getCs())) { // MustInterface(CsP.Cs)
          progress |= addReturns.test(CsP, subtypeConstraints); }} // p[P.ms].Pi <= p[Cs.ms].Pi.Cs

      // TODO: Collect(p; Cs <= P, CCz) = CCz'
      //   This0.Cs' = p[Cs.sel].P
      //   P' = p[P.sel].P
      //   CC in CCz' iff
      //     p[P'].interface?=empty and
      //       CC = P' <= Cs'
      //     or P'' <= Cs' in CCz and p[P''].interface?=empty and
      //       CC = Cs'<=P' in CCz'
      //     or MustClass(Cs')
      //       CC = Cs' <= P'
      //     or sel' in dom(p[Cs']) and sel' in dom(p[P'])
      //       CC = Cs' <= Origin(p; sel'; P')

      // Collect(p; Cs <= P, P <= Cs) = Cs.C <= P.C, P.C <= Cs.C
      //   C in dom(p[Cs])
      for (CsPath CsP : subtypeConstraints) { // Cs <= P
        if (supertypeConstraints.contains(CsP.getCs(), CsP.getPath())) { // P <= Cs
          for (C C : this.p.top().getClassB(CsP.getCs()).cDom()) {// C in dom(p[Cs])
            List<C> CsC = withAdd(CsP.getCs(), C);
            Path PC = CsP.getPath().pushC(C);

            // Cs.C <= P.C and P.C <= Cs.C
            progress |= subtypeConstraints.add(CsC, PC) || supertypeConstraints.add(CsC, PC); }}}}

    // Keep going untill we stop doing anything
    while (progress); }

  // Utilities, not directly related to redirect

  //SuperClasses(p; Pz) = intersect {p.minimize(p[P].Pz U {P, Any}) | P in Pz}
  Set<Path> superClasses(Path... Pz) {
    return this.superClasses(Arrays.asList(Pz)); }

  Set<Path> superClasses(Collection<Path> Pz) {
    assert !Pz.isEmpty();
    return intersect(Pz.stream().map(P -> {
      List<Path> r = p.fromClassB(P).getSuperPaths();
      r.add(P);
      r.add(Path.Any());
      return this.p.minimizeSet(r); })); }

  // Because Java's collection API sucks
  static <T> List<T> withAdd(List<T> s, T... extras) {
    List<T> res = new ArrayList<>(s);
    res.addAll(Arrays.asList(extras));
    return res;
  }

  static <T> Iterable<T> iterate(Stream<T> s) { return s::iterator; }
  static <T> Set<T> intersect(Stream<Collection<T>> s) {
    Set<T> res = null;
    for (Collection<T> set : iterate(s)) {
      if (res == null) { res = new HashSet<>(set); }
      else { res.retainAll(set); } }
    return res; }
  static <T> boolean disjoint(Stream<Collection<T>> s) {
    // TODO: do this more efficiently?
    return intersect(s).isEmpty(); }
  static <T> boolean intersects(Stream<Collection<T>> s) { return !disjoint(s); }
  static <T> boolean intersects(Collection<T>... s) { return intersects(Arrays.stream(s)); }
}

class CsPzMap implements Iterable<CsPath>
{
  // Cs |-> Pz
  // (Cs, P)z
  private Map<List<C>, Set<Path>> map = new HashMap<>();
  CsPzMap() { }
  CsPzMap(CsPzMap other) {this.map = new HashMap<>(other.map); }
  // Returns true iff the key, value pair wasn't allready present
  boolean add(CsPath keyValue) { return this.add(keyValue.getCs(), keyValue.getPath()); }
  boolean add(List<C> key, Path value)
  {
    Set<Path> orig = this.map.get(key);
    if (orig != null) {
      if (orig.contains(value))
        return false;
      else {
        orig.add(value);
      }
    } else {
      this.map.put(key, Collections.singleton(value));
    }

    return true;
  }

  public boolean contains(List<C> key, Path value) {
    Set<Path> Pz = this.map.get(key);
    return Pz != null && Pz.contains(value);
  }
  public Set<Path> get(List<C> key) { return this.map.getOrDefault(key, Collections.emptySet()); }
  public Stream<CsPath> stream() {
    return this.map.entrySet().stream()
      .flatMap(e -> e.getValue().stream().map(P -> new CsPath(e.getKey(), P)));
    }
  public Set<CsPath> values() { return this.stream().collect(Collectors.toSet()); }
  @Override public Iterator<CsPath> iterator() {return this.stream().iterator(); }
}
