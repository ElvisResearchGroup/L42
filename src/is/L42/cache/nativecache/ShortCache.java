package is.L42.cache.nativecache;

public class ShortCache extends L42ValueCache<Short> {

  @Override 
  public String typename() { 
    return "£nativeshort"; 
    }

  @Override 
  protected boolean valueCompare(Short t1, Short t2) { 
    return t1.shortValue() == t2.shortValue(); 
    }

  }
