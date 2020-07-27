package is.L42.maps;
import java.util.function.Supplier;
import is.L42.cache.L42Cache;
public class L42£MutMap<K,T,Extra> extends L42£AbsMap<K,T,L42£MutMap<K,T,Extra>>{
  public <KK,TT>L42£MutMap(Supplier<L42Cache<KK>> kCache,Supplier<L42Cache<TT>> vCache){super(kCache,vCache);}
  public T processVal(T val){return val;}
  @SuppressWarnings("unchecked")
  public static final Class<L42£MutMap<?,?,?>> _class=(Class<L42£MutMap<?,?,?>>)(Object)L42£MutMap.class;
  @Override public L42£MutMap<K,T,Extra> myCache(){return this;}
  @Override protected L42£MutMap<K,T,Extra> newInstance(L42£MutMap<K,T,Extra> t){return new L42£MutMap<K,T,Extra>(kCache,vCache);}
  }
/*
re designing arraylist caching and map caching
right now the code to take fetch the cache object for the gen parameter of 
an arraylist, when the type is native is:
return wrapperT+".myCache.rawFieldCache(0)";
This may or may not work if the type is an arrayList,
and I doubt it working for an arraylist of "This0", that is possible in L42
Should we have a "function" that give you the cache in the first element of the array,
instead of the cache object iself?



*/
/*
can have both imm and mut vals, but the keys are all normalized imms.
three generics: key T and OptT, and OptT need to be generic on T
In the future can be optimized; may be a array could be used for small maps.
*/
//both cachable and its own cache