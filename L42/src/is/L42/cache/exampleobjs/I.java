package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;

public class I implements ForeignObject<I> {
	
  public static final Class<I> _class = I.class;
	private static final Cache<I> myCache;
	
	static
	{
		RootCache.addCacheableType(I.class, (Cache<I>) (myCache = new ForeignObjectCache<I>("I")));
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
	public Cache<I> myCache() {
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
