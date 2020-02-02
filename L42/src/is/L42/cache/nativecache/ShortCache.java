package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class ShortCache extends ValueCache<Short> {

  @Override 
  public String typename() { 
    return "£nativeshort"; 
    }

  @Override 
  protected boolean valueCompare(Short t1, Short t2) { 
    return t1.shortValue() == t2.shortValue(); 
    }

  }
