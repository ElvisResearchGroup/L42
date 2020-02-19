package is.L42.cache.nativecache;

import java.util.List;

import is.L42.nativeCode.TrustedKind;
import is.L42.tools.General;

public class IntCache extends ValueCache<Integer> {

  @Override 
  public Object typename() { 
    return TrustedKind.Int; 
    }

  @Override 
  protected boolean valueCompare(Integer t1, Integer t2) { 
    return t1.intValue() == t2.intValue(); 
    }

  }
