package is.L42.cache;

import java.util.Arrays;
import java.util.HashSet;

public class L42StandardCache<T extends L42Cachable<T>> extends AbstractStructuredCache<T,Object[]>{
  @Override Object[] _fields(T t){return t.allFields();}
  @Override Object f(T t,int i,Object[]_fields){return _fields[i];}
  @Override void setF(T t,int i,Object o,Object[]_fields){t.setField(i, _fields[i]=o);}
  @Override T newInstance(T t){return t.newInstance();}
  private final Object typename;
  private L42Cache<?>[] caches;
    L42StandardCache(Object typename, Class<? extends T> myClass) {
    this.typename = typename;   
    L42CacheMap.addCacheableType(myClass, this);
    }  
  public void lateInitialize(Class<?>...classes){caches=L42CacheMap.getCacheArray(classes);}
  @Override void add(KeyNorm2D key, T t) {
    super.add(key,t);
    t.setNorm(t);
    }          
  @Override public boolean isNorm(T t){return t.isNorm();} 
  @Override public Object[] f(T t){return t.allFields();}  
  @Override public Object f(T t, int i){return t.getField(i);}  
  @Override public void f(T t, Object o, int i){t.setField(i, o);}  
  @Override public int fn(T t){return t.numFields();}
  @Override public Object typename(){return typename;}  
  @Override public L42Cache<?> rawFieldCache(int i){return caches[i];}
  @Override public T getMyNorm(T me){return me.myNorm();}
  @Override public void setMyNorm(T me, T norm){me.setNorm(norm);}  
  }