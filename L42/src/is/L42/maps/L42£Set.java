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
  public <KK>L42£Set(Supplier<L42Cache<KK,?>> kCache){super(kCache);}
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
  @Override public Object f(L42£Set<K> t, int i, L42£Set<K> _fields){
    loadIteration();
    return keys[i];
    }
  @Override public void setF(L42£Set<K> t, int i, Object o, L42£Set<K> _fields){
    loadIteration();
    assert keys[i]==o;
    }

  @Override public int fn(L42£Set<K> t){return inner==null?0:inner.size();}
  @Override public L42Cache<?,?> rawFieldCache(int i){return kCache.get();}
  @Override public L42Cache<L42£Set<K>,?> myCache(){return this;}
  @Override protected L42£Set<K> newInstance(L42£Set<K> t){return new L42£Set<K>(kCache);}
  @SuppressWarnings("unchecked")
  public static final Class<L42£Set<?>> _class=(Class<L42£Set<?>>)(Object)L42£Set.class;
  @Override public Object typename(){return TrustedKind.HSet;}
  }