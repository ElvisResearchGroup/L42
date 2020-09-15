package is.L42.maps;
import java.util.function.Supplier;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;
public class L42£ImmMap<K,T> extends L42£AbsMap<K,T,L42£ImmMap<K,T>>{
  public <KK,TT>L42£ImmMap(Supplier<L42Cache<KK,?>> kCache,Supplier<L42Cache<TT,?>> vCache){super(kCache,vCache);}
  public T processVal(T val){return vCache.get().normalize(val);}
  @SuppressWarnings("unchecked")
  public static final Class<L42£ImmMap<?,?>> _class=(Class<L42£ImmMap<?,?>>)(Object)L42£ImmMap.class;
  @Override public Object typename(){return TrustedKind.HIMap;}
  @Override public L42£ImmMap<K,T> myCache(){return this;}
  @Override protected L42£ImmMap<K,T> newInstance(L42£ImmMap<K,T> t){return new L42£ImmMap<K,T>(kCache,vCache);}
  }