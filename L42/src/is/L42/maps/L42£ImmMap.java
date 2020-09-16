package is.L42.maps;
import java.util.function.Supplier;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;
public class L42£ImmMap<K,T> extends L42£AbsMap<K,T,L42£ImmMap<K,T>>{
  public <KK,TT>L42£ImmMap(L42Cache<KK,?> kCache,L42Cache<TT,?> vCache){super(kCache,vCache);}
  public T processVal(T val){return vCache.normalize(val);}
  @SuppressWarnings("unchecked")
  public static final Class<L42£ImmMap<?,?>> _class=(Class<L42£ImmMap<?,?>>)(Object)L42£ImmMap.class;
  @SuppressWarnings("unchecked")
  @Override public MapCache<K,T,L42£ImmMap<K,T>> myCache(){return (MapCache<K,T,L42£ImmMap<K,T>>)myCache;}
  public static final MapCache<?,?,?> myCache=new MapCache<Object,Object,L42£ImmMap<Object,Object>>(TrustedKind.HIMap);
  }