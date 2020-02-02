package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class ByteCache extends ValueCache<Byte> {

  @Override 
  public String typename() { 
    return "£nativebyte"; 
    }

  @Override 
  protected boolean valueCompare(Byte t1, Byte t2) { 
    return t1.byteValue() == t2.byteValue(); 
    }

  }
