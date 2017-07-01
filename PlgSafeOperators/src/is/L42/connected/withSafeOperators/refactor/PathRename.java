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
    } 
  }