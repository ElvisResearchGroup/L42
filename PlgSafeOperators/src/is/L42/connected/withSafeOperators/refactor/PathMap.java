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

  //R(L)
  ClassB apply(Program p) {
    var visitor = new CloneVisitorWithProgram(p.pop()) {
      private <T> List<T> concat(List<T> a, List<T> b) {
        ArrayList<T> res = new ArrayList<>(a);
        res.addAll(b);
        return Collections.unmodifiableList(res); }

      @Override public Path liftP(Path P){
        List<Ast.C> Cs = Collections.unmodifiableList(this.whereFromTop());
        int n = Cs.size();
        if (!P.isPrimitive() && n >= P.outerNumber()) {
          int k = P.outerNumber();
          List<Ast.C> Cs1 = concat(Cs.subList(0, Cs.size() - k), P.getCBar());
          for (Map.Entry<List<C>, Path> CsP : map.entrySet()) {
            List<Ast.C> Cs2 =  CsP.getKey();
            Path P2 =  CsP.getValue();
            if (Cs1.equals(Cs2)) {
              if (P2.isPrimitive()) { return P2; }
              else { return P2.setNewOuter(P2.outerNumber() + n); }}}}
        // No change found
        return P; }
      
      @Override protected List<Type> liftSup(List<Type> supertypes) {
        // Marco is going to kill me for this, it destroys doc comments,
        // and I have no idea what order it will return things in,
        // it also destroys all your paths by minimizing them.
        return super.liftSup(supertypes);/*
        try {
          var supLiftSup=super.liftSup(supertypes);
          return StreamUtils.stream(supLiftSup)
            .flatMap(T -> StreamUtils.concat(new FromedL(p, T.getPath()).Tz(), T))
            .map(p::minimize).filter(T -> !T.getPath().equals(Path.Any()))
            .distinct().toList();
          }
        catch(PathMetaOrNonExistant pm) {
          throw pm;//breakpoint can be here
          }*/
        }
      
      /*@Override public Path liftP(Path that){
        if(that.isPrimitive()){return that;}
        if(that.getCBar().isEmpty()){return that;}
        for(CsPath cp: map){
          Path newP=_processCsPath(cp,that);
          if(newP!=null){return newP;}}
        return that;}
      protected Path _processCsPath(CsPath cp,Path that){
        Path srcHere=Path.outer(levels,cp.getCs());
        List<Ast.C> tail=p._equivSubPath(srcHere,that);
        if(tail==null){return null;}
        return computeNonNullRes(cp, tail); }
      protected Path computeNonNullRes(CsPath cp, List<Ast.C> tail) {
        if(cp.getPath().isPrimitive()){return cp.getPath();}
        int newOuter=cp.getPath().outerNumber()+levels;
        if(tail.isEmpty()){return cp.getPath().setNewOuter(newOuter);}
        List<Ast.C> newCs=new ArrayList<>(cp.getPath().getCBar());
        newCs.addAll(tail);
        Path destHere=Path.outer(newOuter,newCs);
        return destHere;}*/};

    return (ClassB)(visitor.visit(p.top())); }

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