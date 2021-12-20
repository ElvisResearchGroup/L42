package is.L42.maps;

import static is.L42.tools.General.unreachable;

import is.L42.cache.L42Cachable;

public abstract class L42£AbsSet<K,Inner,Self>implements L42Cachable<Self>{
  Inner inner=null;
  K[] keys=null;
  Self norm=null;
  public abstract int size();
  static protected final Object[] emptyArr=new Object[]{};
  protected void clearIteration(){keys=null;}
  protected abstract void loadIteration();
  public K keyIndex(int index){loadIteration();return keys[index];}
  public abstract void remove(K key);
  @Override public void setNorm(Self norm){this.norm=norm;}
  @Override public Self myNorm(){return norm;}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }