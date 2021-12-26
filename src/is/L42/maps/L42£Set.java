package is.L42.maps;
import java.util.LinkedHashSet;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.TrustedKind;
public class L42£Set<K> extends L42£AbsSet<K,LinkedHashSet<K>,L42£Set<K>>{
  public int size(){return inner==null?0:inner.size();}
  @Override public L42£Set<K> newInstance(){
    var res=new L42£Set<K>();
    if(this.inner==null){ return res; }
    res.inner=new LinkedHashSet<K>(this.inner);
    return res; 
    }
  @SuppressWarnings("unchecked")
  protected void loadIteration(){
    if(keys!=null){return;}
    if(inner==null){keys=(K[])emptyArr;return;}
    keys=(K[])inner.toArray();
    }
  public void remove(K key){
    if(inner==null){return;}
    if(key!=null){key=L42CacheMap.normalize(key);}
    inner.remove(key);
    clearIteration();
    }
  public boolean contains(K key){
    if(inner==null){return false;}
    if(key!=null){key=L42CacheMap.normalize(key);}
    return inner.contains(key);
    }
  public void add(K key){
    if(key!=null){key=L42CacheMap.normalize(key);}
    clearIteration();
    if(inner==null){inner=new LinkedHashSet<>();}
    inner.add(key);
    }
  @SuppressWarnings("unchecked")
  public static final Class<L42£Set<?>> _class=(Class<L42£Set<?>>)(Object)L42£Set.class;
  @SuppressWarnings("unchecked")
  @Override public L42Cache<L42£Set<K>> myCache(){return ((SetCache<K>)myCache);}
  public static final L42Cache<?> myCache=new SetCache<Object>();
  public static class SetCache<K> extends AbsSetCache<L42£Set<K>>{
    @Override public Object f(L42£Set<K> t, int i){
      t.loadIteration();
      return t.keys[i];
      }
    @Override public void setF(L42£Set<K> t, int i, Object o){
      //ok doing nothing: keys are pre normalized,
      //thus when the outer set is normalized,
      //the keys are already normalized
      t.loadIteration();
      assert i>=0 && i<t.keys.length;
      assert t.keys[i]==o;
      }
    @Override public int fn(L42£Set<K> t){return t.inner==null?0:t.inner.size();}
    @Override public Object typename(){return TrustedKind.HSet;}
    @Override protected L42£Set<K> newInstance(L42£Set<K> t){return t.newInstance();}
    @Override public L42Cache<?> rawFieldCache(Object o,int i){
      if(o==null){return this;}
      return L42CacheMap.getCacheObject(o);
      }
    }
  }