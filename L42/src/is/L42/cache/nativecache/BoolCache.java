package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class BoolCache extends ValueCache<Boolean> {

  @Override 
  public String typename(Boolean t) { 
    return "£nativeboolean"; 
    }

  @Override 
  protected boolean valueCompare(Boolean t1, Boolean t2) { 
    return t1.booleanValue() == t2.booleanValue();
    }

  }
