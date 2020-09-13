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
public class L42£SelfSet extends L42£AbsSet<L42£SelfSet,LinkedHashSet<L42£SelfSet>,L42£SelfSet>{
  public L42£SelfSet(Supplier<L42Cache<L42£SelfSet>> kCache){super(kCache);}
  //public L42£SelfSet(){super(null);kCache=()->this;}//NOTE: this can not be used in super :-(
  public int size(){return inner==null?0:inner.size();}
  protected void loadIteration(){
    if(keys!=null){return;}
    if(inner==null){keys=(L42£SelfSet[])emptyArr;return;}
    keys=(L42£SelfSet[])inner.toArray();
    }
  public void remove(L42£SelfSet key){
    if(inner==null){return;}
    inner.remove(key);
    clearIteration();
    }
  public void add(L42£SelfSet key){
    key=kCache.get().normalize(key);
    clearIteration();
    if(inner==null){inner=new LinkedHashSet<>();}
    inner.add(key);
    }
  @Override protected Object f(L42£SelfSet t, int i, L42£SelfSet _fields){
    loadIteration();
    return keys[i];
    }
  @Override protected void setF(L42£SelfSet t, int i, Object o, L42£SelfSet _fields){
    loadIteration();
    assert keys[i]==o;
    }
  @Override public int fn(L42£SelfSet t){return inner==null?0:inner.size();}
  @Override public L42Cache<?> rawFieldCache(int i){return kCache.get();}
  @Override public L42Cache<L42£SelfSet> myCache(){return this;}
  @Override protected L42£SelfSet newInstance(L42£SelfSet t){return new L42£SelfSet(kCache);}
  public static final Class<L42£SelfSet> _class=L42£SelfSet.class;
  @Override public Object typename(){return TrustedKind.HSet;}
  }