package is.L42.cache.exampleobjs;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.L42SingletonCache;

public class Singleton implements L42Cachable<Singleton>, Serializable {

  public static final Class<Singleton> _class = Singleton.class;
  public static final L42SingletonCache<Singleton> myCache = L42CacheMap.newSingletonCache("Singleton", Singleton._class);
  private static Singleton singleton = new Singleton();
  
  private Singleton() { assert singleton == null; }
  
  public static Singleton construct() { return singleton; }
  
  public Object[] allFields() { return new Object[0]; }

  @Override 
  public void setField(int i, Object o) {
    throw new IndexOutOfBoundsException();
    }

  @Override 
  public Object getField(int i) { 
    throw new IndexOutOfBoundsException();
    }

  @Override 
  public int numFields() {
    return 0; 
    }

  @Override 
  public L42Cache<Singleton> myCache() {
    return myCache; 
    }

  @Override 
  public void setNorm(Singleton t) { 
    assert false;
    }

  @Override 
  public Singleton myNorm() { 
    return this; 
    }

  @Override 
  public Singleton newInstance() { 
    throw new NullPointerException(); 
    }
  }
