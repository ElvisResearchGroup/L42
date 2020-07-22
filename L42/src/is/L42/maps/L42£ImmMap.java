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
public class L42£ImmMap<K,T,Extra> extends AbstractStructuredCache<L42£ImmMap<K,T,Extra>,L42£ImmMap<K,T,Extra>>
implements L42Cachable<L42£ImmMap<K,T,Extra>>{
  LinkedHashMap<K,T> map=null;
  K[] keys=null;
  T[] vals=null;
  L42£ImmMap<K,T,Extra> norm=null;
  Supplier<L42Cache<K>> kCache;
  Supplier<L42Cache<T>> vCache;
  public boolean isEmpty(){return map==null || map.isEmpty();}
  @SuppressWarnings("unchecked")
  public <KK,TT>L42£ImmMap(Supplier<L42Cache<KK>> kCache,Supplier<L42Cache<TT>> vCache){
    this.kCache=(Supplier<L42Cache<K>>)(Object)kCache;
    this.vCache=(Supplier<L42Cache<T>>)(Object)vCache;
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

  //TODO: unnull does not work or does not make sense.
  //if the map is of Opt<T>, we can have a "de factor" map of Any and cast the results?
  public /*Opt<T>*/T val(K key){//can never be null, we will insert unnull on null (for opts and opts supertypes)!
    key=kCache.get().normalize(key);
    T val=map==null?null:map.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  public void put(K key,T val){
    key=kCache.get().normalize(key);
    val=vCache.get().normalize(val);
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
  @SuppressWarnings("unchecked")
  public static final Class<L42£ImmMap<?,?,?>> _class=(Class<L42£ImmMap<?,?,?>>)(Object)L42£ImmMap.class;
  @Override public L42£ImmMap<K,T,Extra> myCache(){return this;}
  @Override public L42£ImmMap<K,T,Extra> newInstance(){throw unreachable();}
  @Override public boolean isNorm(L42£ImmMap<K, T,Extra> t) {return norm!=null;}
  @Override public L42£ImmMap<K, T,Extra> getMyNorm(L42£ImmMap<K, T,Extra> t) {return norm;}
  @Override public void setMyNorm(L42£ImmMap<K, T,Extra> t, L42£ImmMap<K, T,Extra> norm){this.norm=norm;}
  @Override public void setNorm(L42£ImmMap<K, T,Extra> norm){this.norm=norm;}//from cachable
  @Override public L42£ImmMap<K, T,Extra> myNorm(){return norm;}//from cachable
  @Override public Object[] f(L42£ImmMap<K, T,Extra> t){throw unreachable();}
  @Override public Object f(L42£ImmMap<K, T,Extra> t, int i){
    loadIteration();
    if(i%2==0){return keys[i/2];}
    return vals[i/2];
    }
  @Override public void f(L42£ImmMap<K, T,Extra> t, Object o, int i){throw unreachable();}
  @Override public int fn(L42£ImmMap<K, T,Extra> t){return map.size()*2;}
  @Override public L42Cache<?> rawFieldCache(int i){
    if(i%2==0){kCache.get();}
    return vCache.get();
    }
  @Override public Object typename(){return TrustedKind.HIMap;}
  @Override protected L42£ImmMap<K, T,Extra> _fields(L42£ImmMap<K, T,Extra> t){return this;}
  @Override protected Object f(L42£ImmMap<K, T,Extra> t, int i, L42£ImmMap<K, T,Extra> _fields){return f(t,i);}
  @Override protected void setF(L42£ImmMap<K, T,Extra> t, int i, Object o, L42£ImmMap<K, T,Extra> _fields){f(t,o,i);}
  @Override protected L42£ImmMap<K, T,Extra> newInstance(L42£ImmMap<K, T,Extra> t) {
    return new L42£ImmMap<K, T,Extra>(kCache,vCache);
    }
  @Override public Object[] allFields(){throw unreachable();}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }