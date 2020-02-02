package is.L42.cache.exampleobjs;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class A implements L42Cachable<A> {
  
  public static final Class<A> _class = A.class;
  private static final L42StandardCache<A> myCache;
  
  static
  {
    L42CacheMap.addCacheableType(A.class, (L42Cache<A>) (myCache = new L42StandardCache<A>("A")));
    myCache.lateInitialize(int.class, int.class);
  }
  
  int i1, i2;

  public A(int i1, int i2) {
    super();
    this.i1 = i1;
    this.i2 = i2;
  }

  public int getI1() {
    return i1;
  }

  public int getI2() {
    return i2;
  }

  @Override
  public Object[] allFields() 
  {
    return new Object[] { i1, i2 };
  }

  @Override
  public void setField(int i, Object o) {
    switch(i) {
      case 0:
        i1 = ((Integer) o);
        break;
      case 1:
        i2 = ((Integer) o);
        break;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }
  
  @Override
  public Object getField(int i) {
    switch(i) {
      case 0:
        return i1;
      case 1:
        return i2;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public String toString() {
    return "A [i1=" + i1 + ", i2=" + i2 + "]";
  }

  @Override
  public L42Cache<A> myCache() 
  {
    return myCache;
  }
  
  private A myNorm = null;
  
  public A myNorm() {
    return myNorm;
  }
  
  public void setNorm(A norm) {
    this.myNorm = norm;
  }

}
