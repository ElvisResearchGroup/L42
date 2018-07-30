package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.*;
import ast.Util.CsMx;
import ast.Util.CsPath;
import coreVisitors.CloneVisitorWithProgram;
import programReduction.Program;

class PathRename extends CloneVisitorWithProgram{
    List<CsPath> map;//map since for redirect is a whole bunch

    public PathRename(Program p,List<Ast.C> src,List<Ast.C> dest) {
      super(p);

      this.map=new ArrayList<>();
      this.makeMap(src, Path.outer(0,dest));
      }
    public PathRename(Program p,List<CsPath> map) {
      super(p);
      this.map=map;
    }

    public ExpCore.ClassB apply() {
        return (ExpCore.ClassB)this.visit(this.p.top());
    }
    private void makeMap(List<Ast.C> Cs, Path P) {
        this.map.add(new CsPath(Cs, P));
        for (Member m : this.p.top().getClassB(Cs).getMs()) {
            if (m instanceof NestedClass) {
                Ast.C C = ((NestedClass)m).getName();
                List<Ast.C> Cs2 = new ArrayList<>(Cs); Cs2.add(C);
                Path P2 = P.pushC(C);
                this.makeMap(Cs2, P2);
            }
        }
    }
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

        for (CsPath CsP : map) {
            List<Ast.C> Cs2 =  CsP.getCs();
            Path P2 =  CsP.getPath();
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
