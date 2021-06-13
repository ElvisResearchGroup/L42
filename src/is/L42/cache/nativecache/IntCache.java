package is.L42.cache.nativecache;

import is.L42.nativeCode.TrustedKind;

public class IntCache extends L42ValueCache<Integer> {

  @Override 
  public Object typename() { 
    return TrustedKind.Int; 
    }

  @Override 
  protected boolean valueCompare(Integer t1, Integer t2) { 
    return t1.intValue() == t2.intValue(); 
    }

  }
