package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.ArrayListCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.Flags;
import is.L42.nativeCode.TrustedKind;
import is.L42.numbers.L42£BigRational;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42NoFields.EqCache;
import is.L42.tools.General;
public class L42£Set<K> extends L42£AbsSet<K,LinkedHashSet<K>,L42£Set<K>>{
  public <KK>L42£Set(L42Cache<KK> kCache){super(kCache);}
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
    key=kCache.refine(key).normalize(key);
    clearIteration();
    if(inner==null){inner=new LinkedHashSet<>();}
    inner.add(key);
    }
  @SuppressWarnings("unchecked")
  public static final Class<L42£Set<?>> _class=(Class<L42£Set<?>>)(Object)L42£Set.class;
  @SuppressWarnings("unchecked")
  @Override public L42Cache<L42£Set<K>> myCache(){return ((SetCache<K>)myCache).refine(this);}
  public static final SetCache<?> myCache=new SetCache<Object>();
  public static class SetCache<K> extends AbsSetCache<L42£Set<K>>{
    public SetCache(){super();}
    protected SetCache(SetCache<K> o){super(o);}
    @Override public Object f(L42£Set<K> t, int i){
      t.loadIteration();
      return t.keys[i];
      }
    @Override public void setF(L42£Set<K> t, int i, Object o){
      t.loadIteration();
      assert t.keys[i]==o;
      }
    @Override public int fn(L42£Set<K> t){return t.inner==null?0:t.inner.size();}
    @Override public Object typename(){return TrustedKind.HSet;}
    @Override protected L42£Set<K> newInstance(L42£Set<K> t){return t.newInstance();}
    @Override public L42Cache<L42£Set<K>> refine(L42£Set<K> t){
      var c=t.kCache;
      return new SetCache<K>(this){
        @Override public L42Cache<?> rawFieldCache(int i){return c;}
        @Override public int hashCode(){return Objects.hashCode(c);}
        @Override public boolean equals(Object o){
          return General.eq(this,o,(o1,o2)->
            Objects.equals(o1.rawFieldCache(0),o2.rawFieldCache(0)));
          }
        };
      }
    }
  }