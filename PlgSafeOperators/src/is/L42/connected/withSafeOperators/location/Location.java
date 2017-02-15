package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.ExpCore;
import ast.Expression;
import ast.Ast.Position;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public interface Location {
  static <T> T listAccess(List<T> list, int that) throws NotAvailable{
    try{return list.get(that);}
    catch(IndexOutOfBoundsException e){throw new RefactorErrors.NotAvailable();}
    }
  Location location();
  Doc doc();
  //boolean equalsequals(This that)
  String toS();
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
      protectedOrigins=new Cacher<List<Origin>>(){
      public List<Origin> cache(){
      List<Origin> origins=new ArrayList<>();
      Position pi=inner.getP();
      while(true){
        if (pi==null || pi==Position.noInfo){return origins;}
        origins.add(new Origin(pi.getFile(),pi.getLine1(),pi.getLine2(),pi.getPos1(),pi.getPos2()));
        pi=pi.get_next();
      }}};}
    @Override public Cacher<List<Origin>> protectedOrigins(){return protectedOrigins;}
    }
  default int originsSize() {
    return protectedOrigins().get().size();
    }
  default Origin origin(int that) throws NotAvailable{
    return Location.listAccess(protectedOrigins().get(), that);
    }
  }
