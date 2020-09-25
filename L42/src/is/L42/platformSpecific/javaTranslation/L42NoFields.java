package is.L42.platformSpecific.javaTranslation;

import is.L42.cache.L42Cachable;
import is.L42.cache.nativecache.L42ValueCache;
import is.L42.generated.P;
import is.L42.tools.General;

public abstract class L42NoFields<T> implements L42Cachable<T>{
  @Override public void setField(int i,Object o){throw new IndexOutOfBoundsException();}
  @Override public Object getField(int i){throw new IndexOutOfBoundsException();}
  @Override public int numFields(){return 0;}
  @Override public void setNorm(T t){assert false;}
  @SuppressWarnings("unchecked")
  @Override public T myNorm(){return (T)this;} 
  public T newInstance(){throw General.unreachable();}
  public static abstract class Eq<T> extends L42NoFields<T>{
    @Override public int hashCode(){return toString().hashCode();}
    @Override public boolean equals(Object obj) {
      if(this == obj){return true;}
      if(obj == null){return false;}
      if(getClass() != obj.getClass()){return false;}
      @SuppressWarnings("unchecked") T that =(T)obj;
      return eq(that);
      }
    public abstract boolean eq(T that);
    }
  public static class EqCache<T extends Eq<T>> extends L42ValueCache<T>{
    Object typeName; public EqCache(Object typeName){this.typeName=typeName;}
    @Override public Object typename(){return typeName;}
    @Override protected boolean valueCompare(T t1,T t2) {return t1.eq(t2);}
    }
  }