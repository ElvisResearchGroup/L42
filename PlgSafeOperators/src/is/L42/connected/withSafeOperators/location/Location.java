package is.L42.connected.withSafeOperators.location;

import java.util.List;

import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public interface Location {
  static <T> T listAccess(List<T> list, int that) throws NotAvailable{
    try{return list.get(that);}
    catch(IndexOutOfBoundsException e){throw new RefactorErrors.NotAvailable();}
    }
  Location location();
  int originsSize();//many since metaprogramming
  Origin origin(int that) throws NotAvailable;
  //boolean equalsequals(This that)
  String toS();
 


}
