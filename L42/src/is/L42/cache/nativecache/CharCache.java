package is.L42.cache.nativecache;

public class CharCache extends L42ValueCache<Character> {

  @Override 
  public String typename() { 
    return "£nativechar"; 
    }

  @Override 
  protected boolean valueCompare(Character t1, Character t2) { 
    return t1.charValue() == t2.charValue(); 
    }

  }
