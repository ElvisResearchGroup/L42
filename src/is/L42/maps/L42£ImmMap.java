package is.L42.maps;
import java.util.LinkedHashMap;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.TrustedKind;
public class L42£ImmMap<K,T> extends L42£AbsMap<K,T,L42£ImmMap<K,T>>{
  public T processVal(T val){
    assert val!=null;
    return L42CacheMap.normalize(val);
    }
  @Override public L42£ImmMap<K,T> newInstance(){
     var res=new L42£ImmMap<K,T>();
     if(this.inner==null){ return res; }
     res.inner=new LinkedHashMap<>(this.inner);
     return res;
     }
  @SuppressWarnings("unchecked")
  public static final Class<L42£ImmMap<?,?>> _class=(Class<L42£ImmMap<?,?>>)(Object)L42£ImmMap.class;
  @SuppressWarnings("unchecked")
  @Override public MapCache<K,T,L42£ImmMap<K,T>> myCache(){return ((MapCache<K,T,L42£ImmMap<K,T>>)myCache);}
  public static final MapCache<?,?,?> myCacheAlt=new MapCache<Object,Object,L42£ImmMap<Object,Object>>(TrustedKind.HIMap);
  public static final L42Cache<?> myCache=myCacheAlt;
  }