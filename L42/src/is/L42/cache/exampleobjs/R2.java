package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;

public class R2 implements ForeignObject<R2> {
  public static final Class<R2> _class = R2.class;
  private static final ForeignObjectCache<R2> myCache;
  
  static
  {
    RootCache.addCacheableType(R2.class, (Cache<R2>) (myCache = new ForeignObjectCache<R2>("R2")));
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
  public Cache<R2> myCache() 
  {
    return myCache;
  }
  
  @Override
  public String typename()
  {
    return "R2";
  }
  
  
  private boolean norm = false;

  @Override
  public boolean norm(boolean set) {
    return norm |= set;
  }
  
}
