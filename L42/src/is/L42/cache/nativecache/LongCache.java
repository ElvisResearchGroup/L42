package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class LongCache extends L42ValueCache<Long> {

  @Override 
  public String typename() { 
    return "£nativelong"; 
    }

  @Override 
  protected boolean valueCompare(Long t1, Long t2) { 
    return t1.longValue() == t2.longValue(); 
    }

  }
