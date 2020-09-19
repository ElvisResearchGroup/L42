package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.maps.L42£Set.SetCache;
import is.L42.nativeCode.TrustedKind;
import is.L42.numbers.L42£BigRational;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42NoFields.EqCache;
import is.L42.tools.General;
public abstract class L42£AbsMap<K,T,Self> extends L42£AbsSet<K,LinkedHashMap<K,T>,Self>{
  T[] vals=null;
  L42Cache<T> vCache;
  @SuppressWarnings("unchecked")
  public L42£AbsMap(Object kCache,Object vCache){
    super(kCache);
    this.vCache=(L42Cache<T>)vCache;
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
    key=kCache.refine(key).normalize(key);
    T val=inner==null?null:inner.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  abstract public T processVal(T val);//vCache.get().normalize(val);
  public void put(K key,T val){
    key=kCache.refine(key).normalize(key);
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
  public static class MapCache<K,T,M extends L42£AbsMap<K,T,M>> extends AbsSetCache<M>{
    public MapCache(Object typeName){super();this.typeName=typeName;}
    protected MapCache(Object typeName,MapCache<K,T,M> o){super(o);this.typeName=typeName;}
    Object typeName;
    @Override public Object f(M t, int i){
      t.loadIteration();
      if(i%2==0){return t.keys[i/2];}
      return t.vals[i/2];
      }
    @SuppressWarnings("unchecked")
    @Override public void setF(M t, int i, Object o){
      t.loadIteration();
      if(i%2==0){assert t.keys[i/2]==o;}
      else{t.inner.put(t.keys[i/2],(T)o); t.vals[i/2]=(T)o;}
      }
    @Override public int fn(M t){return t.inner==null?0:t.inner.size()*2;}
    @Override public L42Cache<?> rawFieldCache(int i){throw unreachable();}
    @Override protected M newInstance(M t){return t.newInstance();}
    @Override public Object typename(){return typeName;}
    @Override public MapCache<K,T,M> refine(M t){
      var k=t.kCache;
      var v=t.vCache;
      return new MapCache<K,T,M>(typeName,this){
        @Override public L42Cache<?> rawFieldCache(int i){
          if(i%2==0){return k;}
          return v;
          }
        @Override public int hashCode(){return Objects.hash(k,v);}
        @Override public boolean equals(Object o){
          return General.eq(this,o,(o1,o2)->
            Objects.equals(o1.rawFieldCache(0),o2.rawFieldCache(0))
            && Objects.equals(o1.rawFieldCache(1),o2.rawFieldCache(1))
            && o1.typeName==o2.typeName
            );
          }
        };
      }
    }
  }