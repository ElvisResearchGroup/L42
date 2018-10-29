package is.L42.connected.withSafeOperators.refactor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Util.CsMx;
import ast.Util.CsPath;
import coreVisitors.CloneVisitorWithProgram;
import programReduction.Program;

public class PathMap implements Iterable<CsPath> {
    Map<List<C>, Path> map;
    public PathMap() { this.map = new HashMap<>(); }
    public PathMap(Map<List<C>, Path> map) { this.map = new HashMap<>(map); }

    // returns V such that
    // Cs-> This0.Dest in V
    //  If Cs->This0.Cs' in V
    //     C in dom(L[Cs])
    //     Cs.C -> This0.Cs'.C in V
    // I.e. it causes all (indirect) nested classes of Cs to be consistantly mapped
    public PathMap(ClassB L, List<Ast.C> Cs, List<Ast.C> dest) {
      this();
      this.makeMap(L, Cs, Path.outer(0, dest));
    }
    private void makeMap(ClassB L, List<Ast.C> Cs, Path P) {
      this.map.put(Cs, P);
      for (Member m : L.getClassB(Cs).getMs()) {
          if (m instanceof NestedClass) {
              Ast.C C = ((NestedClass)m).getName();
              List<Ast.C> Cs2 = new ArrayList<>(Cs); Cs2.add(C);
              Path P2 = P.pushC(C);
              this.makeMap(L, Cs2, P2);
          }
      }
    }

    // Compatability functions
    public PathMap(Collection<CsPath> list) {
      this.map = list.stream().collect(Collectors.toMap(CsPath::getCs, CsPath::getPath));
    }
    public List<CsPath> toList() { return stream().collect(Collectors.toList()); }

    public Stream<CsPath> stream() {
      return this.map.entrySet().stream().map(e -> new CsPath(e.getKey(), e.getValue()));
    }
    public Iterator<CsPath> iterator() { return this.stream().iterator(); }

    // dom(V)
    public Set<List<Ast.C>> dom() { return this.map.keySet(); }

    //V(L)
    ClassB apply(ClassB L) { return (ClassB)(new Visitor(Program.emptyLibraryProgram().evilPush(L)).visit(L)); }
    private class Visitor extends CloneVisitorWithProgram{
      public Visitor(Program p) { super(p); }

      private <T> List<T> concat(List<T> a, List<T> b) {
          ArrayList<T> res = new ArrayList<>(a);
          res.addAll(b);
          return Collections.unmodifiableList(res);
      }

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
                  if (P2.isPrimitive()) {
                      return P2;
                  } else {
                      return P2.setNewOuter(P2.outerNumber() + n);
                  }
              }
          }
        }
        // No change found
        return P;
    }
    /*
    @Override public Path liftP(Path that){
      if(that.isPrimitive()){return that;}
      if(that.getCBar().isEmpty()){return that;}
      for(CsPath cp: map){
        Path newP=_processCsPath(cp,that);
        if(newP!=null){return newP;}
        }
      return that;
      }
    protected Path _processCsPath(CsPath cp,Path that){
      Path srcHere=Path.outer(levels,cp.getCs());
      List<Ast.C> tail=p._equivSubPath(srcHere,that);
      if(tail==null){return null;}
      return computeNonNullRes(cp, tail);
      }
    protected Path computeNonNullRes(CsPath cp, List<Ast.C> tail) {
      if(cp.getPath().isPrimitive()){return cp.getPath();}
      int newOuter=cp.getPath().outerNumber()+levels;
      if(tail.isEmpty()){return cp.getPath().setNewOuter(newOuter);}
      List<Ast.C> newCs=new ArrayList<>(cp.getPath().getCBar());
      newCs.addAll(tail);
      Path destHere=Path.outer(newOuter,newCs);
      return destHere;
      }*/
  }

    // L[only Csz]
    public static ClassB only(ClassB L, Collection<List<Ast.C>> Csz) {
      for(List<Ast.C> cs : Csz) {
        if(cs.isEmpty()){
          throw new RuntimeException("L[only Cs] is undefined when Cs = empty");
          //L = ClassB.membersClass(Collections.emptyList(), Ast.Position.noInfo, L.getPhase());
        }
        L = (ClassB)L.accept(new Remover(cs));
      }
      return L;
    }
    private static class Remover extends coreVisitors.CloneVisitor {
    List<Ast.C> cs;
    Remover(List<Ast.C> cs){this.cs=cs;}
    public List<Member> liftMembers(List<Member> s) {
        List<Member> result=new ArrayList<Member>();
        for(Member m:s){m.match(
          nc->manageNC(nc,result),
          mi->result.add(liftM(m)),
          mt->result.add(liftM(m))
          );}
        return result;
        }
    private boolean manageNC(NestedClass nc, List<Member> result) {
        assert !cs.isEmpty();
        Ast.C top=cs.get(0);
        if(!top.equals(nc.getName())){return result.add(nc);}//out of path
        if(cs.size()==1){return true;}
        List<Ast.C> csLocal=cs;
        cs=cs.subList(1,cs.size());
        try{return result.add(this.visit(nc));}
        finally{cs=csLocal;}
      }
    }
  }
