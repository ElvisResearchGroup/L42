package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class IntCache extends ValueCache<Integer> {

  @Override 
  public String typename() { 
    return "£nativeint"; 
    }

  @Override 
  protected boolean valueCompare(Integer t1, Integer t2) { 
    return t1.intValue() == t2.intValue(); 
    }

  }
