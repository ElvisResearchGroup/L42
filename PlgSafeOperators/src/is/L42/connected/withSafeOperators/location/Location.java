package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.ErrorMessage;
import ast.ExpCore;
import ast.Expression;
import ast.Ast;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.From;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;
import programReduction.Program;

public interface Location {
  static public String as42Path(List<ast.Ast.C>path){
    if(path.isEmpty()){return "This";}
    String res=path.get(0).toString();
    for(int i=1;i<path.size();i++){res+="."+path.get(i);}
    return res;
    }
  static TypeRefTo refTo(Program p,Path path,List<Ast.C> location,Lib root) {
  Path whereP=Path.outer(0,location);
  path=From.fromP(path,whereP);
  if(path.isPrimitive()){
    return new TypeRefTo.Binded(path);
    }
  if (path.outerNumber()==0){
    return new TypeRefTo.Lib(root,path);
    }
  path=path.setNewOuter(path.outerNumber()-1);
  try{
    ClassB cb=p.extractClassB(path);
    if(cb.getPhase().subtypeEq(Phase.Typed)){//typed,coherent
      return new TypeRefTo.Binded(path);
      }
    //else, phase is none but cb available and not typed yet
    return new TypeRefTo.Unavailable("Unavailable path: "+path+"; code not typed yet.");
    }
  catch(ErrorMessage.PathMetaOrNonExistant pne){
    if (pne.isMeta()){return new TypeRefTo.Unavailable("Unavailable path: "+path+"; code not generated yet.");}
    return new TypeRefTo.Missing(path.toString());
    }
  }

  
  static <T> T listAccess(List<T> list, int that) throws NotAvailable{
    try{return list.get(that);}
    catch(IndexOutOfBoundsException e){throw new RefactorErrors.NotAvailable();}
    }
  Location location();
  Doc doc();
  boolean equalequal(Object that);
  //String toS();//complicated relation with toS of Type
  Cacher<List<Origin>> protectedOrigins();
  static abstract class LocationImpl<T extends Expression.HasPos, L extends Location> implements Location{
    protected Cacher<List<Origin>> protectedOrigins;
    T inner;
    L location;
    @Override
    public L location() {return location;}
    public LocationImpl(T inner, L location){
      this.inner=inner;
      this.location=location;
      this.protectedOrigins=new Cacher<List<Origin>>(){
        public List<Origin> cache(){
          List<Origin> origins=new ArrayList<>();
          Position pi=inner.getP();
          while(true){
            if (pi==null || pi==Position.noInfo){return origins;}
            origins.add(new Origin(pi.getFile(),pi.getLine1(),pi.getLine2(),pi.getPos1(),pi.getPos2()));
            pi=pi.get_next();
            }}};}
    @Override public Cacher<List<Origin>> protectedOrigins(){return protectedOrigins;}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((inner == null) ? 0 : inner.hashCode());
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    return result;
    }
    @Override//Note, not just self generated
    public boolean equals(Object obj) {
    if (this == obj){return true;}
    if (obj == null){return false;}
    if (getClass() != obj.getClass()){return false;}
    LocationImpl<?, ?> other = (LocationImpl<?, ?>) obj;
    if (inner == null) {
      if (other.inner != null){return false;}
    }
    else if (!inner.equals(other.inner)){return false;}
    if (location == null) {
      if (other.location != null){return false;}
    }
    else if(location==this && other.location==other){return true;}
    else if (!location.equals(other.location)){return false;}
    return true;
    }
    
    }
  default int originSize() {
    return protectedOrigins().get().size();
    }
  default Origin origin(int that) throws NotAvailable{
    return Location.listAccess(protectedOrigins().get(), that);
    }
  }
