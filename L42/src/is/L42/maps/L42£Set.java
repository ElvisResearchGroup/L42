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
/*
re designing arraylist caching and map caching
right now the code to take fetch the cache object for the gen parameter of 
an arraylist, when the type is native is:
return wrapperT+".myCache.rawFieldCache(0)";
This may or may not work if the type is an arrayList,
and I doubt it working for an arraylist of "This0", that is possible in L42
Should we have a "function" that give you the cache in the first element of the array,
instead of the cache object iself?



*/
/*
can have both imm and mut vals, but the keys are all normalized imms.
three generics: key T and OptT, and OptT need to be generic on T
In the future can be optimized; may be a array could be used for small maps.
*/
//both cachable and its own cache
public class L42£Set<K,T> extends AbstractStructuredCache<L42£Set<K,T>,L42£Set<K,T>>
implements L42Cachable<L42£Set<K,T>>{
  LinkedHashMap<K,T> mapImms=null;
  LinkedHashMap<K,T> mapMuts=null;
  K[] keys=null;
  T[] vals=null;
  L42£Set<K,T> norm=null;
  Supplier<L42Cache<K>> kCache;
  Supplier<L42Cache<T>> vCache;
  public boolean isEmpty(){
    return (mapImms==null || mapImms.isEmpty()) && (mapMuts==null || mapMuts.isEmpty());
    }
  L42£Set(Supplier<L42Cache<K>> kCache,Supplier<L42Cache<T>> vCache){this.kCache=kCache;this.vCache=vCache;}
  private int sizeImm(){return mapImms==null?0:mapImms.size();}
  private int sizeMut(){return mapMuts==null?0:mapMuts.size();}
  static private final Object[] emptyArr=new Object[]{};
  static private final Object unnull=new Object();
  private void clearIteration(){keys=null;vals=null;}
  @SuppressWarnings("unchecked")
  void loadIteration(){
    if(keys!=null){return;}
    if(mapImms==null){
      if(mapMuts==null){
        keys=(K[])emptyArr;
        vals=(T[])emptyArr;
        return;
        }
      keys=(K[])mapMuts.keySet().toArray();
      vals=(T[])mapMuts.values().toArray();
      return;
      }
    if(mapMuts==null){
      keys=(K[])mapImms.keySet().toArray();
      vals=(T[])mapImms.values().toArray();
      return;
      }
    keys=(K[])Stream.concat(mapImms.keySet().stream(),mapMuts.keySet().stream()).toArray();
    vals=(T[])Stream.concat(mapImms.values().stream(),mapMuts.values().stream()).toArray();        
    }
  public int size() {return sizeImm()+sizeMut();}
  public K keyIndex(int index){loadIteration();return keys[index];}
  public T valIndex(int index){loadIteration();return vals[index];}

  public /*Opt<T>*/T val(K key){//can never be null, we will insert unnull on null (for opts and opts supertypes)!
    key=kCache.get().normalize(key);
    T valImm=mapImms==null?null:mapImms.get(key);
    T val=valImm!=null?valImm:mapMuts==null?null:mapMuts.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  public /*Opt<T>*/T immVal(K key){
    key=kCache.get().normalize(key);
    return mapImms==null?null:mapImms.get(key);
    }
  public /*Opt<T>*/T mutVal(K key){
    key=kCache.get().normalize(key);
    return mapMuts==null?null:mapMuts.get(key);
    }
  @SuppressWarnings("unchecked")
  public void addImm(K key,T val){
    key=kCache.get().normalize(key);
    val=vCache.get().normalize(val);
    clearIteration();
    if(val==null){val=(T)unnull;}
    if(mapImms==null){mapImms=new LinkedHashMap<>();}
    if(mapMuts!=null){mapMuts.remove(key);}
    mapImms.put(key, val);
    }
  @SuppressWarnings("unchecked")
  public void addMut(K key,T val){
    key=kCache.get().normalize(key);
    clearIteration();
    if(val==null){val=(T)unnull;}
    if(mapMuts==null){mapMuts=new LinkedHashMap<>();}
    if(mapImms!=null){mapImms.remove(key);}
    mapMuts.put(key, val);
    }
  public void remove(K key){
    clearIteration();
    if(mapImms!=null){mapImms.remove(key);}
    if(mapMuts!=null){mapMuts.remove(key);}
    }
/*  @Override public boolean eq(L42£Map<?,?> other){
    loadIteration();
    int size=keys.length;
    if(size!=other.keys.length){return false;}
    for(int i=0;i<=size;i+=1){
      if(keys[i]!=other.keys[i] || vals[i]!=other.vals[i]){return false;}
      }//not yet... 
    //TODO:
    // MUT VALS need to be normalized before eq? should we support fields(..) and the others?
    return true;
    }*/
  @SuppressWarnings("unchecked")
  public static final Class<L42£Set<?,?>> _class=(Class<L42£Set<?,?>>)(Object)L42£Set.class;
  @Override public L42£Set<K,T> myCache(){return this;}
  //static{L42CacheMap.addCachableType_synchronized(_class,myCache);}
  @Override public L42£Set<K,T> newInstance(){throw unreachable();}

  @Override public boolean isNorm(L42£Set<K, T> t) {return norm!=null;}
  @Override public L42£Set<K, T> getMyNorm(L42£Set<K, T> t) {return norm;}
  @Override public void setMyNorm(L42£Set<K, T> t, L42£Set<K, T> norm){this.norm=norm;}
  
  @Override public void setNorm(L42£Set<K, T> norm){this.norm=norm;}//from cachable
  @Override public L42£Set<K, T> myNorm(){return norm;}//from cachable
  
  @Override public Object[] f(L42£Set<K, T> t){
    loadIteration();
    return vals;
    }

  @Override public Object f(L42£Set<K, T> t, int i) {
    loadIteration();
    if(i%2==0){return keys[i/2];}
    return vals[i/2];
    }

  @Override public void f(L42£Set<K, T> t, Object o, int i) {
    @SuppressWarnings("unchecked")
    T v=(T)o;
    loadIteration();
    assert i%2!=0;
    i=i/2;
    vals[i]=v;//TODO: would need to be the mutvals only?
    mapMuts.put(t.keys[i],v);
    }
  @Override public int fn(L42£Set<K, T> t){return mapMuts.size()*2;}

  @Override public L42Cache<?> rawFieldCache(int i){
    if(i%2==0){kCache.get();}
    return vCache.get();
    }

  @Override public Object typename(){throw unreachable();}//return TrustedKind.HMap;}return TrustedKind.HMap;}

  @Override protected L42£Set<K, T> _fields(L42£Set<K, T> t){return this;}

  @Override protected Object f(L42£Set<K, T> t, int i, L42£Set<K, T> _fields){return f(t,i);}
  
  @Override protected void setF(L42£Set<K, T> t, int i, Object o, L42£Set<K, T> _fields){f(t,o,i);}

  @Override protected L42£Set<K, T> newInstance(L42£Set<K, T> t) {
    return new L42£Set<K, T>(kCache,vCache);
    }
  @Override public Object[] allFields(){throw unreachable();}
  @Override
  public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  }