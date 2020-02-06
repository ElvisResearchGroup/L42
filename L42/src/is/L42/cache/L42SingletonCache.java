package is.L42.cache;

import java.util.List;

public class L42SingletonCache<T extends L42Cachable<T>> implements L42Cache<T> {
  
  private final String typename;
  private final KeyNorm2D key;
  
  public L42SingletonCache(String typename, Class<T> myClass) {
    this.typename = typename;
    this.key = new KeyNorm2D(new Object[][] { { this } });
    L42CacheMap.addCacheableType(myClass, this);
    }
  
  @Override 
  public T normalize(T t) {
    return t;
    }

  @Override 
  public NormResult<T> normalizeInner(T t, List<Object> chain) { 
    return new NormResult<T>(t);
    }

  @Override 
  public KeyNorm2D computeKeyNN(T t) { 
    return key;
    }

  @Override 
  public NormResult<T> computeKeyNNInner(T t, List<Object> chain) {
    return new NormResult<T>(t); 
    }

  @Override 
  public void addObjectOverride(KeyNorm2D key, T obj) { 
   assert false;
   }

  @Override 
  public boolean isNorm(T t) { 
    return true; 
    }

  @Override 
  public boolean structurallyEquals(T t1, T t2) {
    return true; 
    }

  @Override
  public boolean identityEquals(T t1, T t2) { 
    return true; 
    }

  @Override 
  public Object[] f(T t) { 
    return t.allFields();
    }

  @Override 
  public Object f(T t, int i) { 
    throw new IndexOutOfBoundsException();  
    }

  @Override 
  public void f(T t, Object o, int i) { 
    throw new IndexOutOfBoundsException(); 
    }

  @Override 
  public int fn(T t) { 
    return 0; 
    }
  
  @Override 
  public L42Cache<?> rawFieldCache(int i) {
    throw new IndexOutOfBoundsException(); 
    }

  @Override 
  public T getMyNorm(T me) {
    return me; 
    }

  @Override
  public void setMyNorm(T me, T norm) {}

  @Override 
  public String typename() {
    return this.typename; 
    }

  @Override
  public void clear() {}
  
  }
