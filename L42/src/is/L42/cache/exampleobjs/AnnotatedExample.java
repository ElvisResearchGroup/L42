package is.L42.cache.exampleobjs;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42StandardCache;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.tools.General;

public class AnnotatedExample implements L42Cachable<AnnotatedExample>, Serializable {
  
  /*
   * Create and add the cache to the index in a static initializer,
   * such that the cache is created before any objects of this type
   * are used.
   * 
   * Store the cache statically for later retrieval by myCache() 
   */
  
  public static final Class<AnnotatedExample> _class = AnnotatedExample.class;
  public static final L42StandardCache<AnnotatedExample> myCache;
  
  static {
    //The call to .addCacheableType is necessary, as the cache
    //map is sometimes used as a fallback mechanism
    myCache = L42CacheMap.newStandardCache("AnnotatedExample", AnnotatedExample.class);
    myCache.lateInitialize(String.class, int.class, Object.class);
    }
  //Some field declarations
  String myString;
  int myInt;
  Object myReference;

  /*
   * The field mutator/access methods should all use
   * a consistent order. Violation of this order will
   * result in errors.
   */
  
  @Override 
  public Object[] allFields() { 
    return new Object[] { myString, myInt, myReference };
    }

  @Override 
  public void setField(int i, Object o) { 
    //Note no need to catch type errors, type errors would
    //indicate unsoundness in the normalization code. If
    //the normalization process works correctly, type errors
    //won't occur
    switch(i) {
      case 0:
        myString = (String) o;
        break;
      case 1:
        myInt = (Integer) o;
        break;
      case 2:
        myReference = o;
        break;
      default:
        //This should never occur, as it would indicate an
        //underlying problem in the normalization code
        throw General.unreachable();
      }
    }
  
  @Override
  public Object getField(int i) {
    switch(i) {
      case 0:
        return myString;
      case 1: 
        return myInt;
      case 2:
        return myReference;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }
  
  @Override
  public int numFields() { return 3; }

  @Override 
  public L42Cache<AnnotatedExample> myCache() { 
    return myCache; 
    }

  /*
   * Whether this object is the canonical version of itself,
   * both a setter and a getter
   */
  
  private volatile AnnotatedExample myNorm = null;
  
  public AnnotatedExample myNorm() {
    return myNorm;
  }
  
  public void setNorm(AnnotatedExample norm) {
    this.myNorm = norm;
  }

  @Override 
  public AnnotatedExample newInstance() { 
    return new AnnotatedExample(); 
    }
  }
