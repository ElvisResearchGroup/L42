package is.L42.maps;

import static is.L42.tools.General.unreachable;

import java.util.function.Supplier;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;

public abstract class L42£AbsSet<K,Inner,Self> 
    extends AbstractStructuredCache<Self,Self>
    implements L42Cachable<Self>{
  Inner inner=null;
  K[] keys=null;
  Self norm=null;
  Supplier<L42Cache<K>> kCache;
  @SuppressWarnings("unchecked")
  public L42£AbsSet(Object kCache){this.kCache=(Supplier<L42Cache<K>>)kCache;}
  public abstract int size();//{return inner==null?0:inner.size();}
  static protected final Object[] emptyArr=new Object[]{};
  protected void clearIteration(){keys=null;}//vals=null;
  protected abstract void loadIteration();
  public K keyIndex(int index){loadIteration();return keys[index];}
  public abstract void remove(K key);
  @Override public Self newInstance(){throw unreachable();}
  @Override public boolean isNorm(Self t) {return norm!=null;}
  @Override public Self getMyNorm(Self t) {return norm;}
  @Override public void setMyNorm(Self t, Self norm){this.norm=norm;}
  @Override public void setNorm(Self norm){this.norm=norm;}//from cachable
  @Override public Self myNorm(){return norm;}//from cachable
  @Override public Object[] f(Self t){throw unreachable();}
  @Override public void f(Self t, Object o, int i){throw unreachable();}
  @Override protected Self _fields(Self t){return t;}
  @Override protected Object f(Self t, int i, Self _fields){return f(t,i);}
  @Override protected void setF(Self t, int i, Object o, Self _fields){throw unreachable();}
  @Override public Object[] allFields(){throw unreachable();}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }