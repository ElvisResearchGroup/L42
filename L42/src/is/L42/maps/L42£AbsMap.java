package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.TrustedKind;
import is.L42.numbers.L42£BigRational;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42NoFields.EqCache;
public abstract class L42£AbsMap<K,T,Self> extends AbstractStructuredCache<Self,Self>
implements L42Cachable<Self>{
  LinkedHashMap<K,T> map=null;
  K[] keys=null;
  T[] vals=null;
  Self norm=null;
  Supplier<L42Cache<K>> kCache;
  Supplier<L42Cache<T>> vCache;
  @SuppressWarnings("unchecked")
  public L42£AbsMap(Object kCache,Object vCache){
    this.kCache=(Supplier<L42Cache<K>>)kCache;
    this.vCache=(Supplier<L42Cache<T>>)vCache;
    }
  public int size(){return map==null?0:map.size();}
  static private final Object[] emptyArr=new Object[]{};
  private void clearIteration(){keys=null;vals=null;}
  @SuppressWarnings("unchecked")
  void loadIteration(){
    if(keys!=null){return;}
    if(map==null){
      keys=(K[])emptyArr;
      vals=(T[])emptyArr;
      return;
      }
    keys=(K[])map.keySet().toArray();
    vals=(T[])map.values().toArray();        
    }
  public K keyIndex(int index){loadIteration();return keys[index];}
  public T valIndex(int index){loadIteration();return vals[index];}

  public /*Opt<T>*/T val(K key){//can never be null, TODO: we need to sort Opt to use flag values?
    key=kCache.get().normalize(key);
    T val=map==null?null:map.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  abstract public T processVal(T val);//vCache.get().normalize(val);
  public void put(K key,T val){
    key=kCache.get().normalize(key);
    val=processVal(val);
    clearIteration();
    assert val!=null;
    if(map==null){map=new LinkedHashMap<>();}
    map.put(key, val);
    }
  public void remove(K key){
    if(map==null){return;}
    map.remove(key);
    clearIteration();
    }
  //@SuppressWarnings("unchecked")
  //public static final Class<L42£AbsMap<?,?,?>> _class=(Class<L42£AbsMap<?,?,?>>)(Object)L42£AbsMap.class;
  //@Override abstract public Self myCache();//{return this;}
  @Override public Self newInstance(){throw unreachable();}
  @Override public boolean isNorm(Self t) {return norm!=null;}
  @Override public Self getMyNorm(Self t) {return norm;}
  @Override public void setMyNorm(Self t, Self norm){this.norm=norm;}
  @Override public void setNorm(Self norm){this.norm=norm;}//from cachable
  @Override public Self myNorm(){return norm;}//from cachable
  @Override public Object[] f(Self t){throw unreachable();}
  @Override public Object f(Self t, int i){
    loadIteration();
    if(i%2==0){return keys[i/2];}
    return vals[i/2];
    }
  @Override public void f(Self t, Object o, int i){throw unreachable();}
  @Override public int fn(Self t){return map.size()*2;}
  @Override public L42Cache<?> rawFieldCache(int i){
    if(i%2==0){kCache.get();}
    return vCache.get();
    }
  @Override public Object typename(){return TrustedKind.HIMap;}
  @Override protected Self _fields(Self t){return t;}
  @Override protected Object f(Self t, int i, Self _fields){return f(t,i);}
  @Override protected void setF(Self t, int i, Object o, Self _fields){f(t,o,i);}
  //@Override abstract protected Self newInstance(Self t);
    //{return new L42£AbsMap<K, T,Extra>(kCache,vCache);}
  @Override public Object[] allFields(){throw unreachable();}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }