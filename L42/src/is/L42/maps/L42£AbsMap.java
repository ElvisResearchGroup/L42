package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
public abstract class L42£AbsMap<K,T,Self> extends L42£AbsSet<K,LinkedHashMap<K,T>,Self>{
  T[] vals=null;
  Supplier<L42Cache<T,?>> vCache;
  @SuppressWarnings("unchecked")
  public L42£AbsMap(Object kCache,Object vCache){
    super(kCache);
    this.vCache=(Supplier<L42Cache<T,?>>)vCache;
    }
  public int size(){return inner==null?0:inner.size();}
  @Override protected void clearIteration(){keys=null;vals=null;}
  @SuppressWarnings("unchecked")
  protected void loadIteration(){
    if(keys!=null){return;}
    if(inner==null){
      keys=(K[])emptyArr;
      vals=(T[])emptyArr;
      return;
      }
    keys=(K[])inner.keySet().toArray();
    vals=(T[])inner.values().toArray();        
    }
  public T valIndex(int index){loadIteration();return vals[index];}
  public /*Opt<T>*/T val(K key){//can never be null, TODO: we need to sort Opt to use flag values?
    key=kCache.get().normalize(key);
    T val=inner==null?null:inner.get(key);
    return val;//this, in 42 must be an Opt<T> native
    //TODO: still broken if T is an optional
    //in the Java generation, if T is an optional, we need to make a map of wrapped<T>
    //and is ok to return the wrapped T here.
    //in put, we would need to "wrap" the T before calling put
    //should be ok to put Opt keys, but we should test it
    }
  abstract public T processVal(T val);//vCache.get().normalize(val);
  public void put(K key,T val){
    key=kCache.get().normalize(key);
    val=processVal(val);
    clearIteration();
    assert val!=null;
    if(inner==null){inner=new LinkedHashMap<>();}
    inner.put(key, val);
    }
  public void remove(K key){
    if(inner==null){return;}
    inner.remove(key);
    clearIteration();
    }
  @Override public Object f(Self t, int i, Self _fields){
    loadIteration();
    if(i%2==0){return keys[i/2];}
    return vals[i/2];
    }
  @SuppressWarnings("unchecked")
  @Override public void setF(Self t, int i, Object o, Self _fields){
    loadIteration();
    if(i%2==0){assert keys[i/2]==o;}
    else{inner.put(keys[i/2],(T)o); vals[i/2]=(T)o;}
    }

  @Override public int fn(Self t){return inner==null?0:inner.size()*2;}
  @Override public L42Cache<?,?> rawFieldCache(int i){
    if(i%2==0){return kCache.get();}
    return vCache.get();
    }
  }