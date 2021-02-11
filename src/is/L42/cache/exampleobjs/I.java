package is.L42.cache.exampleobjs;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class I implements L42Cachable<I>, Serializable {
  
  public static final Class<I> _class = I.class;
  public static final L42Cache<I> myCache;
  
  static{
    myCache = L42CacheMap.newStandardCache("I", I.class);
  }
  

  public Object[] allFields() 
  {
    return new Object[0];
  }

  public void setField(int i, Object o) {
    throw new ArrayIndexOutOfBoundsException();  
    }
  
  @Override
  public Object getField(int i) {
    throw new ArrayIndexOutOfBoundsException();
    }
  
  @Override
  public int numFields() { return 0; }

  @Override
  public L42Cache<I> myCache() {
    return myCache;
  }

  private volatile I myNorm = null;
  
  public I myNorm() {
    return myNorm;
  }
  
  public void setNorm(I norm) {
    this.myNorm = norm;
  }

  @Override 
  public I newInstance() { 
    return new I(); 
    }

}
