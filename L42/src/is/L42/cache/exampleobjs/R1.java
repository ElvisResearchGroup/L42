package is.L42.cache.exampleobjs;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class R1 implements L42Cachable<R1>, Serializable
{  
  public static final Class<R1> _class = R1.class;
  public static final L42StandardCache<R1> myCache;
  
  static
  {
    myCache = L42CacheMap.newStandardCache("R1", R1.class);
    myCache.lateInitialize(Object.class);
  }
  
  public Object referenced;
  
  private R1() {}
  
  public R1(Object ref)
  {
    this.referenced = ref;
  }

  public Object[] allFields() {
    return new Object[] { referenced };
  }

  @Override
  public void setField(int i, Object o) {
    if(i == 0)
      referenced = o;
    else
      throw new ArrayIndexOutOfBoundsException();
  }
  
  @Override
  public Object getField(int i) {
    switch(i) {
      case 0:
        return referenced;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }
  
  @Override
  public int numFields() { return 1; }

  @Override
  public L42Cache<R1,?> myCache() {
    return myCache;
  }
  
  private volatile R1 myNorm = null;
  
  public R1 myNorm() {
    return myNorm;
  }
  
  public void setNorm(R1 norm) {
    this.myNorm = norm;
  }

  @Override 
  public R1 newInstance() {
    return new R1();
    }
  
}
