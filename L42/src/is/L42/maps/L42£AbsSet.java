package is.L42.maps;

import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;

import java.util.function.Supplier;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;

public abstract class L42£AbsSet<K,Inner,Self>implements L42Cachable<Self>{
  Inner inner=null;
  K[] keys=null;
  Self norm=null;
  L42Cache<K,?> kCache;
  @SuppressWarnings("unchecked")
  public L42£AbsSet(Object kCache){this.kCache=(L42Cache<K,?>)kCache;}
  public abstract int size();//{return inner==null?0:inner.size();}
  static protected final Object[] emptyArr=new Object[]{};
  protected void clearIteration(){keys=null;}//vals=null;
  protected abstract void loadIteration();
  public K keyIndex(int index){loadIteration();return keys[index];}
  public abstract void remove(K key);
  @Override public Self newInstance(){throw unreachable();}
  @Override public void setNorm(Self norm){this.norm=norm;}
  @Override public Self myNorm(){return norm;}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }