package is.L42.cache.nativecache;

import is.L42.nativeCode.TrustedKind;

public class DoubleCache extends L42ValueCache<Double> {

  @Override 
  public Object typename() { 
    return TrustedKind.Double; 
    }

  @Override 
  protected boolean valueCompare(Double t1, Double t2) { 
    return t1.doubleValue() == t2.doubleValue(); 
    }

  }
