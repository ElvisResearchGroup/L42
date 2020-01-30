package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class StringCache extends ValueCache<String> {

  @Override 
  public String typename() { 
    return "£nativestring"; 
    }

  @Override 
  protected boolean valueCompare(String t1, String t2) { 
    if(t1 == null) { return t2 == null; }
    return t1.equals(t2);
    }

  }
