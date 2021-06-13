package is.L42.maps;
import java.util.LinkedHashMap;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
public abstract class L42£AbsMap<K,T,Self> extends L42£AbsSet<K,LinkedHashMap<K,T>,Self>{
  T[] vals=null;
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
  public T val(K key){
    if(key!=null){key=L42CacheMap.normalize(key);}
    T val=inner==null?null:inner.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  abstract public T processVal(T val);//vCache.get().normalize(val);
  public void put(K key,T val){
    if(key!=null){key=L42CacheMap.normalize(key);}
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
    public MapCache(Object typeName){this.typeName=typeName;}
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
    @Override public L42Cache<?> rawFieldCache(Object o,int i){
      if(o==null){return null;}
      return L42CacheMap.getCacheObject(o);
      }
    @Override protected M newInstance(M t){return t.newInstance();}
    @Override public Object typename(){return typeName;}
    }
  }