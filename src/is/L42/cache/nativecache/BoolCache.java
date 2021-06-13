package is.L42.cache.nativecache;

import is.L42.nativeCode.TrustedKind;

public class BoolCache extends L42ValueCache<Boolean> {

  @Override 
  public Object typename() {return TrustedKind.Bool;}

  @Override 
  protected boolean valueCompare(Boolean t1, Boolean t2) { 
    return t1.booleanValue() == t2.booleanValue();
    }
  }
