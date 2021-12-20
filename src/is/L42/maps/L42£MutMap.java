package is.L42.maps;
import java.util.LinkedHashMap;

import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;
public class L42£MutMap<K,T> extends L42£AbsMap<K,T,L42£MutMap<K,T>>{
  public T processVal(T val){return val;}
  @Override public L42£MutMap<K,T> newInstance(){
    var res=new L42£MutMap<K,T>();
    if(this.inner==null){ return res; }
    res.inner=new LinkedHashMap<>(this.inner);
    return res;
    }
  @SuppressWarnings("unchecked")
  public static final Class<L42£MutMap<?,?>> _class=(Class<L42£MutMap<?,?>>)(Object)L42£MutMap.class;
  @SuppressWarnings("unchecked")
  @Override public MapCache<K,T,L42£MutMap<K,T>> myCache(){return ((MapCache<K,T,L42£MutMap<K,T>>)myCacheAlt);}
  public static final MapCache<?,?,?> myCacheAlt=new MapCache<Object,Object,L42£MutMap<Object,Object>>(TrustedKind.HMMap);
  public static final L42Cache<?> myCache=myCacheAlt;
  }