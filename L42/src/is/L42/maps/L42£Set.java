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
public class L42£Set<K> extends L42£AbsSet<K,LinkedHashSet<K>,L42£Set<K>>{
  L42£Set(Supplier<L42Cache<K>> kCache){super(kCache);}
  public int size(){return inner==null?0:inner.size();}
  @SuppressWarnings("unchecked")
  protected void loadIteration(){
    if(keys!=null){return;}
    if(inner==null){keys=(K[])emptyArr;return;}
    keys=(K[])inner.toArray();
    }
  public void remove(K key){
    if(inner==null){return;}
    inner.remove(key);
    clearIteration();
    }
  public void add(K key){
    key=kCache.get().normalize(key);
    clearIteration();
    if(inner==null){inner=new LinkedHashSet<>();}
    inner.add(key);
    }
  @Override public Object f(L42£Set<K> t, int i){
    loadIteration();
    return keys[i];
    }
  @Override public int fn(L42£Set<K> t){return inner==null?0:inner.size();}
  @Override public L42Cache<?> rawFieldCache(int i){return kCache.get();}
  @Override public L42Cache<L42£Set<K>> myCache(){return this;}
  @Override protected L42£Set<K> newInstance(L42£Set<K> t){return new L42£Set<K>(kCache);}
  }