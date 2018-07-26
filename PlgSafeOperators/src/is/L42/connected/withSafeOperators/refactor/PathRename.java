package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
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
    if(that.isPrimitive()){return that;}
    if(that.getCBar().isEmpty()){return that;}
    for(CsPath cp: map){
      Path newP=_processCsPath(cp,that);
      if(newP!=null){return newP;}
      }
    return that;
    }

  Path rename_path(Path p) {
      List<Ast.C> Cs = new ArrayList<>(this.whereFromTop());
      int n = Cs.size();
      if (!p.isPrimitive() && Cs.size() >= p.outerNumber()) {
        int k = p.outerNumber();
        // This is alegedly the best way to remove the last 'k' elements from Cs
        Cs.subList(Cs.size() - k, Cs.size()).clear();
        Cs.addAll(p.getCBar());

        return lookup_path(Cs, n, p);
      } else {
        return p;
      }
  }
  Path lookup_path(List<Ast.C> Cs, int n, Path p) {
    // Look Cs in map, if we find it, add 'n' to the other number
    // otherwise, just return p
    return null;
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