package is.L42.maps;
import java.util.function.Supplier;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;
public class L42£ImmMap<K,T> extends L42£AbsMap<K,T,L42£ImmMap<K,T>>{
  public <KK,TT>L42£ImmMap(L42Cache<KK> kCache,L42Cache<TT> vCache){super(kCache,vCache);}
  
  //damn generics
  private <X extends L42Cachable<T>>T processOptVal(X val,T valT){
    return val.myCache().refine(valT).normalize(valT);
    }
  @SuppressWarnings("unchecked")
  public T processVal(T val){//If is optopt, then extract first field from val
    if(val instanceof L42Cachable<?>){
      return processOptVal((L42Cachable<T>)val,val);
      }
    return vCache.refine(val).normalize(val);}
  @SuppressWarnings("unchecked")
  public static final Class<L42£ImmMap<?,?>> _class=(Class<L42£ImmMap<?,?>>)(Object)L42£ImmMap.class;
  @SuppressWarnings("unchecked")
  @Override public MapCache<K,T,L42£ImmMap<K,T>> myCache(){return ((MapCache<K,T,L42£ImmMap<K,T>>)myCache).refine(this);}
  public static final MapCache<?,?,?> myCache=new MapCache<Object,Object,L42£ImmMap<Object,Object>>(TrustedKind.HIMap);
  }