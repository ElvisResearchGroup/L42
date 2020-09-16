package is.L42.cache;

import java.util.Arrays;
import java.util.HashSet;

public class L42StandardCache<T extends L42Cachable<T>> extends AbstractStructuredCache<T,T>{
  @Override public T _fields(T t){return t;}
  @Override public Object f(T t,int i,T _fields){assert t==_fields;return _fields.getField(i);}
  @Override public void setF(T t,int i,Object o,T _fields){assert t==_fields;_fields.setField(i,o);}
  @Override protected T newInstance(T t){return t.newInstance();}
  private final Object typename;
  private L42Cache<?,?>[] caches;
    L42StandardCache(Object typename, Class<? extends T> myClass) {
    this.typename = typename;   
    L42CacheMap.addCacheableType(myClass, this);
    }  
  public void lateInitialize(Class<?>...classes){caches=L42CacheMap.getCacheArray(classes);}
  @Override protected void add(KeyNorm2D key, T t) {
    super.add(key,t);
    t.setNorm(t);
    }          
  @Override public boolean isNorm(T t){return t.isNorm();}     
  @Override public int fn(T t){return t.numFields();}
  @Override public Object typename(){return typename;}  
  @Override public L42Cache<?,?> rawFieldCache(int i){return caches[i];}
  @Override public T getMyNorm(T me){return me.myNorm();}
  @Override public void setMyNorm(T me, T norm){me.setNorm(norm);}  
  }