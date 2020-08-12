package is.L42.maps;
import java.util.function.Supplier;
import is.L42.cache.L42Cache;
public class L42£MutMap<K,T> extends L42£AbsMap<K,T,L42£MutMap<K,T>>{
  public <KK,TT>L42£MutMap(Supplier<L42Cache<KK>> kCache,Supplier<L42Cache<TT>> vCache){super(kCache,vCache);}
  public T processVal(T val){return val;}
  @SuppressWarnings("unchecked")
  public static final Class<L42£MutMap<?,?>> _class=(Class<L42£MutMap<?,?>>)(Object)L42£MutMap.class;
  @Override public L42£MutMap<K,T> myCache(){return this;}
  @Override protected L42£MutMap<K,T> newInstance(L42£MutMap<K,T> t){return new L42£MutMap<K,T>(kCache,vCache);}
  }