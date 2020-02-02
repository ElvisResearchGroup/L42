package is.L42.cache.exampleobjs;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class I implements L42Cachable<I> {
	
  public static final Class<I> _class = I.class;
	private static final L42Cache<I> myCache;
	
	static
	{
	  L42CacheMap.addCacheableType(I.class, (L42Cache<I>) (myCache = new L42StandardCache<I>("I")));
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
	public L42Cache<I> myCache() {
		return myCache;
	}

	private I myNorm = null;
  
  public I myNorm() {
    return myNorm;
  }
  
  public void setNorm(I norm) {
    this.myNorm = norm;
  }

}
