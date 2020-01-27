package is.L42.cache.exampleobjs;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.RootCache;
import is.L42.tools.General;

public class AnnotatedExample implements ForeignObject<AnnotatedExample> {
  
  /*
   * Create and add the cache to the index in a static initializer,
   * such that the cache is created before any objects of this type
   * are used.
   * 
   * Store the cache statically for later retrieval by myCache() 
   */
  
  private static final Cache<AnnotatedExample> myCache;
  
  static
  {
    //The call to .addCacheableType is necessary, as the cache
    //map is sometimes used as a fallback mechanism
    RootCache.addCacheableType(AnnotatedExample.class, (Cache<AnnotatedExample>) (myCache = new ForeignObjectCache<AnnotatedExample>()));
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
  public Cache<AnnotatedExample> myCache() { 
    return myCache; 
    }

  @Override 
  public String typename() {
    /*
     * The typename should be unchanging, and unique. Colliding typenames
     * will result in key collisions.
     */
    return "AnnotatedExample";  
    }
  
  /*
   * Whether this object is the canonical version of itself,
   * both a setter and a getter
   * TODO: Discuss interactions with proposed norm field
   */
  
  private boolean norm = false;

  @Override 
  public boolean norm(boolean set) { 
    return norm |= set;
    }

  }
