package is.L42.platformSpecific.javaTranslation;

import is.L42.cache.L42Cachable;
import is.L42.generated.P;

public abstract class L42NoFields<T> implements L42Cachable<T>{
  @Override public Object[] allFields(){return new Object[0];}
  @Override public void setField(int i,Object o){throw new IndexOutOfBoundsException();}
  @Override public Object getField(int i){throw new IndexOutOfBoundsException();}
  @Override public int numFields(){return 0;}
  @Override public void setNorm(T t){assert false;}
  @SuppressWarnings("unchecked")
  @Override public T myNorm(){return (T)this;} 
  }