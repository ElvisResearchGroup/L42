package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;

public class I implements ForeignObject<I> {
	
	private static final Cache<I> myCache;
	
	static
	{
		RootCache.addCacheableType(I.class, (Cache<I>) (myCache = new ForeignObjectCache<I>()));
	}
	

	public Object[] allFields() 
	{
		return new Object[0];
	}

	public void setField(int i, Object o) 
	{
		throw new ArrayIndexOutOfBoundsException();	
	}

	@Override
	public Cache<I> myCache() {
		return myCache;
	}

	@Override
	public String typename() {
		return "I";
	}
	
	private boolean norm = false;

	@Override
	public boolean norm(boolean set) {
		return norm |= set;
	}

}
