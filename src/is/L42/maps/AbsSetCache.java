package is.L42.maps;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.KeyNorm2D;
import is.L42.cache.L42Cachable;

public abstract class AbsSetCache<T extends L42Cachable<T>> extends AbstractStructuredCache<T>{
  @Override protected void add(KeyNorm2D key, T t) {
    super.add(key,t);
    this.setMyNorm(t, t);
    }  
  @Override public boolean isNorm(T t){return t.isNorm();}
  @Override public int fn(T t){return t.numFields();}
  @Override public T getMyNorm(T t){return t.myNorm();}
  @Override public void setMyNorm(T t, T norm){t.setNorm(t);}
  }