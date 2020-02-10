package is.L42.cache.exampleobjs;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;

public class S1 implements L42Cachable<S1>, Serializable {
  
  public static final Class<S1> _class = S1.class;
  public static final L42StandardCache<S1> myCache;
  
  static
  {
    myCache = new L42StandardCache<S1>("S1", S1.class);
    myCache.lateInitialize(String.class);
  }
  
  String myString;

  private S1() {}
  
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
  public int numFields() { return 1; }
  
  @Override 
  public L42Cache<S1> myCache() { 
    return myCache;
    }

  private S1 myNorm = null;
  
  public S1 myNorm() {
    return myNorm;
  }
  
  public void setNorm(S1 norm) {
    this.myNorm = norm;
  }

  @Override 
  public S1 newInstance() {
    return new S1(); 
    }

}
