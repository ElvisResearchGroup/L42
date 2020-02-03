package is.L42.cache.exampleobjs;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class R2 implements L42Cachable<R2> {
  public static final Class<R2> _class = R2.class;
  private static final L42StandardCache<R2> myCache;
  
  static
  {
    myCache = new L42StandardCache<R2>("R2", R2.class);
    myCache.lateInitialize(Object.class, Object.class);
  }
  
  
  public Object referenced;
  public Object referenced2;
  
  public R2(Object ref, Object ref2)
  {
    this.referenced = ref;
    this.referenced2 = ref2;
  }

  @Override
  public Object[] allFields() {
    return new Object[] { referenced, referenced2 };
  }

  @Override
  public void setField(int i, Object o) {
    if(i == 0)
      referenced = o;
    else if(i == 1)
      referenced2 = o;
    else
      throw new ArrayIndexOutOfBoundsException();
  }
  
  @Override
  public Object getField(int i) {
    switch(i) {
      case 0:
        return referenced;
      case 1:
        return referenced2;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }
  
  @Override
  public int numFields() { return 2; }

  @Override
  public L42Cache<R2> myCache() 
  {
    return myCache;
  }
  
  private R2 myNorm = null;
  
  public R2 myNorm() {
    return myNorm;
  }
  
  public void setNorm(R2 norm) {
    this.myNorm = norm;
  }
  
}
