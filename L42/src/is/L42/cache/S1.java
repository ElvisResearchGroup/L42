package is.L42.cache;

import is.L42.cache.exampleobjs.R1;

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

  @Override 
  public String typename() {
    return "S1";
    }
  
  private boolean norm;

  @Override 
  public boolean norm(boolean set) {
    return norm |= set;
    }

}
