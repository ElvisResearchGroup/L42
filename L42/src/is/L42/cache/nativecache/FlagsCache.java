package is.L42.cache.nativecache;

import java.util.List;

import is.L42.nativeCode.TrustedOp;
import is.L42.tools.General;

public class FlagsCache extends ValueCache<TrustedOp.Flags> {

  @Override 
  public String typename() { 
    return "£nativeflags"; 
    }

  @Override 
  protected boolean valueCompare(TrustedOp.Flags t1, TrustedOp.Flags t2) { 
    return t1 == t2;
    }

  }
