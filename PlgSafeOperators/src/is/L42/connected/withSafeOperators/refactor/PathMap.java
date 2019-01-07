package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Util;
import ast.Util.CsMx;
import ast.Util.CsPath;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import programReduction.Program;
import tools.CollectionWrapper;
import tools.StreamUtils;
import tools.Utils;

public class PathMap extends CollectionWrapper<CsPath> {
  private Map<List<C>, Path> map;

  @Override public String toString() { return this.stream().map(Object::toString).collect(Collectors.joining(", ")); }

  public PathMap() { this(new HashMap<>()); }
  public PathMap(PathMap that) { this(that.map); }
  public PathMap(Map<List<C>, Path> map) { this.map = map; }

  public Path get(List<C> Cs) { return Objects.requireNonNull(this._get(Cs)); }
  public Path _get(List<C> Cs) { return this.map.get(Cs); }
  public boolean contains(List<C> Cs) { return this.map.containsKey(Cs); }

  public void add(List<C> Cs, Path P) { this.map.put(Objects.requireNonNull(Cs), Objects.requireNonNull(P)); }
  // returns R such that
  // Cs-> This0.Dest in R
  //  If Cs->This0.Cs' in R
  //     C in dom(L[Cs])
  //     Cs.C -> This0.Cs'.C in R
  // I.e. it causes all (indirect) nested classes of Cs to be consistantly mapped
  public PathMap(ClassB L, List<Ast.C> Cs, List<Ast.C> dest) {
    this();
    this.makeMap(L, Cs, Path.outer(0, dest)); }

  private void makeMap(ClassB L, List<Ast.C> Cs, Path P) {
    this.map.put(Cs, P);
    for (Member m : L.getClassB(Cs).getMs()) {
      if (m instanceof NestedClass) {
        Ast.C C = ((NestedClass)m).getName();
        List<Ast.C> Cs2 = new ArrayList<>(Cs); Cs2.add(C);
        Path P2 = P.pushC(C);
        this.makeMap(L, Cs2, P2); }}}

  // Compatability functions
  public PathMap(Collection<CsPath> list) {
    this.map = list.stream().collect(Collectors.toMap(CsPath::getCs, CsPath::getPath)); }

  public List<CsPath> toList() { return stream().collect(Collectors.toList()); }

  @Override public Stream<CsPath> stream() {
    return this.map.entrySet().stream().map(e -> new CsPath(e.getKey(), e.getValue())); }

  @Override public Iterator<CsPath> iterator() { return this.stream().iterator(); }

  // dom(R)
  public Set<List<Ast.C>> dom() { return this.map.keySet(); }

  public PathMap map(Function<Path, Path> f) {
    return new PathMap(StreamUtils.map(this, CsP -> CsP.withPath(f.apply(CsP.getPath()))));}

  private class Visitor extends CloneVisitorWithProgram {
    Visitor(Program p) { super(p); }
    @Override public ClassB visit(ClassB that) { return (ClassB)super.visit(that); }
    @Override public Path liftP(Path P){
      List<Ast.C> Cs = this.whereFromTop();
      int n = Cs.size();
      if (!P.isPrimitive() && n >= P.outerNumber()) {
        var Cs1 = StreamUtils.concat(Cs.subList(0, Cs.size() - P.outerNumber()), P.getCBar());
        for (var CsP : PathMap.this) {
          var Cs2 = CsP.getCs();
          var P2 = CsP.getPath();
          if (Cs1.equals(Cs2)) { return P2.isPrimitive() ? P2 : P2.setNewOuter(P2.outerNumber() + n); }}}
      return P;}}

  //R(L)
  ClassB apply(ClassB L) { return new Visitor(Program.emptyLibraryProgram().evilPush(L)).visit(L); }
  //R(p) like R(p.top()) except that it normalises implements lists
  ClassB apply(Program p) {
    var visitor = new Visitor(p.pop()) {
      @Override protected List<Type> liftSup(List<Type> supertypes) {
        // Marco is going to kill me for this, it destroys doc comments,
        // and I have no idea what order it will return things in,
        // it also destroys all your paths by minimizing them.
        return StreamUtils.stream(super.liftSup(supertypes))
          .flatMap(T -> StreamUtils.concat(new FromedL(p, T.getPath()).Tz(), T))
          .map(p::minimize).filter(T -> !T.getPath().equals(Path.Any()))
          .distinct().toList();}};
    return visitor.visit(p.top()); }

  // L[remove Csz]
  public static ClassB remove(ClassB L, Collection<List<Ast.C>> Csz) {
    for (var cs : Csz) {
      if(cs.isEmpty()) {
        throw new RuntimeException("L[remove Csz] is undefined when empty in Csz");
        /*L = ClassB.membersClass(Collections.emptyList(), Ast.Position.noInfo, L.getPhase());*/ }

      final var cs_ = cs;
      var remover = new coreVisitors.CloneVisitor() {
        List<C> cs = cs_;
        public List<Member> liftMembers(List<Member> s) {
          List<Member> result=new ArrayList<>();
          for(Member m:s) { m.match(nc->manageNC(nc,result), mi->result.add(liftM(m)), mt->result.add(liftM(m))); }
          return result; }
        private boolean manageNC(NestedClass nc, List<Member> result) {
          assert !cs.isEmpty();
          Ast.C top=cs.get(0);
          if (!top.equals(nc.getName())) { return result.add(nc); }//out of path
          if (cs.size() == 1) { return true; }
          List<Ast.C> csLocal=cs;
          cs=cs.subList(1,cs.size());
          try { return result.add(this.visit(nc)); }
          finally { cs=csLocal; } } };
      L = (ClassB)L.accept(remover); }
    return L; }

  @Override protected Collection collection() { return this.toList(); }
}