package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Path;
import ast.Util.CsMx;
import ast.Util.CsPath;
import coreVisitors.CloneVisitorWithProgram;
import programReduction.Program;

class PathRename extends CloneVisitorWithProgram{
    List<CsPath> map;//map since for redirect is a whole bunch
    public PathRename(Program p,List<Ast.C> src,List<Ast.C> dest) {
      super(p);
      map=new ArrayList<>();
      map.add(new CsPath(src,Path.outer(0,dest)));
      }
    public PathRename(Program p,List<CsPath> map) {
      super(p);
      this.map=map;
    }

  @Override public Path liftP(Path that){
    /*if(that.isPrimitive()){return that;}
    if(that.getCBar().isEmpty()){return that;}
    for(CsPath cp: map){
      Path newP=_processCsPath(cp,that);
      if(newP!=null){return newP;}
      }
    return that;*/
    return rename_path(that);
    }

  Path rename_path(Path P) {
      List<Ast.C> Cs = new ArrayList<>(this.whereFromTop());
      int n = Cs.size();
      if (!P.isPrimitive() && Cs.size() >= P.outerNumber()) {
        int k = P.outerNumber();
        // This is alegedly the best way to remove the last 'k' elements from Cs
        Cs.subList(Cs.size() - k, Cs.size()).clear();
        Cs.addAll(P.getCBar());

        return lookup_path(Cs, n, P);
      } else {
        return P;
      }
  }
  Path lookup_path(List<Ast.C> Cs, int n, Path P) {
    // Look Cs in map, if we find it, add 'n' to the other number
    // otherwise, just return p
    for (CsPath CsP : map) {
      List<Ast.C> Cs2 =  CsP.getCs();
      Path P2 =  CsP.getPath();
      // Cs2 is a prefix of Cs
      if (Collections.indexOfSubList(Cs, Cs2) == 0) {
        if (P2.isPrimitive()) {
          // Can't have sub-paths of a primitive
          assert Cs.size() == Cs2.size();
          return P2;
        } else {
          // Get the rest
          List<Ast.C> rest = Cs.subList(Cs2.size(), Cs.size());
          List<Ast.C> result = new ArrayList<>(P2.getCBar());
          result.addAll(rest);
          return Path.outer(P2.outerNumber() + n, result);
        }
      }
    }
    return P;
  }

  protected Path _processCsPath(CsPath cp,Path that){
    Path srcHere=Path.outer(levels,cp.getCs());
    List<Ast.C> tail=p._equivSubPath(srcHere,that);
    if(tail==null){return null;}
    return computeNonNullRes(cp, tail);
    }
  protected Path computeNonNullRes(CsPath cp, List<Ast.C> tail) {
    if(cp.getPath().isPrimitive()){ assert tail.isEmpty(); return cp.getPath();}
    int newOuter=cp.getPath().outerNumber()+levels;
    if(tail.isEmpty()){return cp.getPath().setNewOuter(newOuter);}
    List<Ast.C> newCs=new ArrayList<>(cp.getPath().getCBar());
    newCs.addAll(tail);
    Path destHere=Path.outer(newOuter,newCs);
    return destHere;
    }
  }