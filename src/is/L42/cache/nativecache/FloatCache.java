package is.L42.cache.nativecache;

public class FloatCache extends L42ValueCache<Float> {

  @Override 
  public String typename() { 
    return "£nativefloat"; 
    }

  @Override 
  protected boolean valueCompare(Float t1, Float t2) { 
    return t1.floatValue() == t2.floatValue(); 
    }

  }
