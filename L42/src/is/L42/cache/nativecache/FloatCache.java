package is.L42.cache.nativecache;

import java.util.List;

import is.L42.tools.General;

public class FloatCache extends ValueCache<Float> {

  @Override 
  public String typename() { 
    return "£nativefloat"; 
    }

  @Override 
  protected boolean valueCompare(Float t1, Float t2) { 
    return t1.floatValue() == t2.floatValue(); 
    }

  }
