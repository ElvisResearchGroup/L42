package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;

public class R1 implements ForeignObject<R1> 
{  
  private static final Cache<R1> myCache;
  
  static
  {
    RootCache.addCacheableType(R1.class, (Cache<R1>) (myCache = new ForeignObjectCache<R1>()));
  }
  
  public Object referenced;
  
  public R1(Object ref)
  {
    this.referenced = ref;
  }

  @Override
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
  public Cache<R1> myCache() {
    return myCache;
  }
  
  @Override
  public String typename()
  {
    return "R1";
  }
  
  private boolean norm = false;

  @Override
  public boolean norm(boolean set) {
    return norm |= set;
  }
  
}
