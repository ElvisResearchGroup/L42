package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.*;
import ast.ErrorMessage.*;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import coreVisitors.*;
import programReduction.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// A caching wrapper over an L, which does froming only when required?
public class FromedL {
  static <T> List<T> withAdd(Collection<T> s, Collection<T>... extras) {
    var res = new ArrayList<>(s);
    for (var e : extras) { res.addAll(e); }
    return res; }

  // Because Java's collection API sucks
  static <T> List<T> withAdd(Collection<T> s, T... extras) {
    return withAdd(s, Arrays.asList(extras)); }

  static <K, V> void replaceAllNull(Map<K, V> m, Function<K, V> f) { m.replaceAll((k, v) -> v == null ? f.apply(k) : v); }

  static <I, O> Stream<O> optMap(Stream<I> s, Function<I, O> m) { return s.map(m).filter(Objects::nonNull); }
  public final ClassB L; public final Path P;

  //private List<FromedL> Pz = null;
  static class Nested {
    final C C;final FromedL FL; final NestedClass nc;
    Nested(NestedClass nc, Path P) { this.nc = nc; this.C = nc.getName(); this.FL = new FromedL((ClassB)nc.getInner(), P.pushC(C)); }}

  FromedL(Program p, Path P) { this(p.extractClassB(P), P); }
  FromedL(ClassB L, Path P) { this.L = L; this.P = P; }

  private List<Path> _Pz = null;
  Collection<Path> Pz() {
    if (this._Pz == null) {
      this._Pz = this.L.getSuperPaths().stream().map(this::from).collect(Collectors.toList()); }
    return this._Pz; }

  private Map<MethodSelector, MethodWithType> _mwtz = null; private Map<C, Nested> _ncz = null;
  private boolean mwtz_computed = false; private boolean ncz_completed = false;

  private void prepareMwtz() {
    if (this._mwtz == null) {
      this._mwtz = new HashMap<>();
      this.L.msDom().forEach(ms -> this._mwtz.put(ms, null)); }}
  private void prepareNcz() {
    if (this._ncz == null) {
      this._ncz = new HashMap<>();
      this.L.cDom().forEach(C -> this._ncz.put(C, null)); }}

  // Note: the from will throw a NullPointerException if the method dosn't exist
  private MethodWithType computeMwt(MethodSelector ms) { return from(this.L._getMwt(ms)); }
  private Nested computeNested(C C) { return from(this.L.getNested(List.of(C))); }

  Collection<MethodWithType> mwtz() {
    prepareMwtz();
    if (!mwtz_computed) { replaceAllNull(this._mwtz, this::computeMwt); mwtz_computed = true; }
    return this._mwtz.values(); }
  Collection<Nested> ncz() {
    prepareNcz();
    if (!ncz_completed) { replaceAllNull(this._ncz, this::computeNested); ncz_completed = true; }
    return this._ncz.values(); }

  MethodWithType get(MethodSelector ms) {
    prepareMwtz();
    return this._mwtz.computeIfAbsent(ms, this::computeMwt); }
  FromedL get(C C) {
    prepareNcz();
    return this._ncz.computeIfAbsent(C, this::computeNested).FL; }
  FromedL get(List<C> Cs) {
    return Cs.isEmpty() ? this : this.get(Cs.get(0)).get(Cs.subList(1, Cs.size())); }

  // TODO: Is this dangerouse? Could I be catching more NPE's then the one I expect?
  MethodWithType _get(MethodSelector ms) { try { return this.get(ms); } catch (NullPointerException __) { return null; } }
  FromedL _get(C C) {try { return this.get(C); } catch (PathMetaOrNonExistant __) { return null; }}
  FromedL _get(List<C> Cs) {try { return this.get(Cs); } catch (PathMetaOrNonExistant __) { return null; }}

  boolean contains(MethodSelector ms) { return this.msDom().contains(ms); }
  boolean contains(C C) { return this.cDom().contains(C); }
  boolean contains(List<C> Cs) { return _get(Cs) != null; }

  Set<MethodSelector> msDom() { prepareMwtz(); return this._mwtz.keySet(); }
    Set<C> cDom() { prepareNcz(); return this._ncz.keySet(); }

  Path from(Path P1) { return From.fromP(P1, P); }
  MethodWithType from(MethodWithType mwt) { return From.from(mwt.with_inner(null), P).with_inner(mwt.get_inner()); }
  Nested from(NestedClass nc) { return new Nested(nc, this.P); }

  boolean isInterface() { return this.L.isInterface(); }
  // Gets the 'top' L, ignores nested classes, and does not from method bodies
  ClassB topL() {
    var Tz = this.Pz().stream().map(Type::of).collect(Collectors.toList());
    return this.L.withSupertypes(Tz).withMs(List.copyOf(this.mwtz())); }

  static Set<Path> reachables(MethodWithType mwt) {
    var res = new HashSet<>(mwt.getPaths());
    res.add(mwt.getReturnPath());
    res.addAll(mwt.getExceptions());
    return res; }

  Set<Path> reachables() {
    var res = new HashSet<>(this.Pz());
    this.ncz().forEach(N -> res.addAll(N.FL.reachables()));
    for (var mwt : mwtz()) { res.addAll(reachables(mwt)); }
    return res; }

  static List<List<C>> internals(Collection<Path> Ps) {
    return optMap(Ps.stream(), P -> P.tryOuterNumber() == 0 ? P.getCBar() : null).collect(Collectors.toList()); }
  static List<C> internal(Path P) { return isInternal(P) ? P.getCBar() : null; }
  static boolean isInternal(Path P) { return P.tryOuterNumber() == 0; }
}