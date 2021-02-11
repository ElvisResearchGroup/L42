package is.L42.cache;

import java.util.List;
import java.util.Map;

public class L42SingletonCache<T> implements L42Cache<T> {
  
  private final Object typename;
  private final KeyNorm2D key;
  
  L42SingletonCache(Object typename, Class<? extends T> myClass) {
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

  @Override public int fn(T t){return 0;}
  
  @Override 
  public L42Cache<?> rawFieldCache(Object t,int i) {
    throw new IndexOutOfBoundsException(); 
    }

  @Override 
  public T getMyNorm(T me) {
    return me; 
    }

  @Override
  public void setMyNorm(T me, T norm) {}

  @Override 
  public Object typename() {
    return this.typename; 
    }

  @Override
  public void clear() {}

  @Override 
  public T dup(T that, Map<Object, Object> map) { 
    return that;
    }

  @Override public Object f(T t, int i){throw new IndexOutOfBoundsException();}
  @Override public void setF(T t, int i, Object o){throw new IndexOutOfBoundsException();}
  }