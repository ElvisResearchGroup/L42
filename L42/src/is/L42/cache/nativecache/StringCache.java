package is.L42.cache.nativecache;

import java.util.List;

import is.L42.nativeCode.TrustedKind;
import is.L42.tools.General;

public class StringCache extends L42ValueCache<String> {

  @Override 
  public Object typename(){return TrustedKind.String;}

  @Override 
  protected boolean valueCompare(String t1, String t2) { 
    if(t1 == null) { return t2 == null; }
    return t1.equals(t2);
    }

  }
