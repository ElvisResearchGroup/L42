package is.L42.cache.nativecache;

import is.L42.nativeCode.TrustedKind;

public class LongCache extends L42ValueCache<Long> {

  @Override 
  public Object typename() { 
    return TrustedKind.Long; 
    }

  @Override 
  protected boolean valueCompare(Long t1, Long t2) { 
    return t1.longValue() == t2.longValue(); 
    }

  }
