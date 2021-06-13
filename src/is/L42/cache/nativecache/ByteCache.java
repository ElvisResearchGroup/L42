package is.L42.cache.nativecache;

public class ByteCache extends L42ValueCache<Byte> {

  @Override 
  public String typename() { 
    return "£nativebyte"; 
    }

  @Override 
  protected boolean valueCompare(Byte t1, Byte t2) { 
    return t1.byteValue() == t2.byteValue(); 
    }

  }
