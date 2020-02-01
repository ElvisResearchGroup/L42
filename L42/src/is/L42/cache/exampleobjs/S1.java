package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;

public class S1 implements ForeignObject<S1> {
  
  public static final Class<S1> _class = S1.class;
  private static final ForeignObjectCache<S1> myCache;
  
  static
  {
    RootCache.addCacheableType(S1.class, (Cache<S1>) (myCache = new ForeignObjectCache<S1>("S1")));
    myCache.lateInitialize(String.class);
  }
  
  String myString;

  public S1(String s) { 
    this.myString = s;
    }

  @Override 
  public Object[] allFields() { 
    return new Object[] { myString };
    }

  @Override 
  public void setField(int i, Object o) {
    switch(i) {
      case 0:
        myString = (String) o;
        break;
      default:
        throw new ArrayIndexOutOfBoundsException();
      }
    }

  @Override 
  public Object getField(int i) { 
    switch(i) {
      case 0:
        return myString;
      default:
        throw new ArrayIndexOutOfBoundsException();
      }
    }

  @Override 
  public Cache<S1> myCache() { 
    return myCache;
    }

  private S1 myNorm = null;
  
  public S1 myNorm() {
    return myNorm;
  }
  
  public void setNorm(S1 norm) {
    this.myNorm = norm;
  }

}