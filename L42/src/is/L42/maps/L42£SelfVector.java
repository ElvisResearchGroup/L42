package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
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
public class L42£SelfVector
    extends AbstractStructuredCache<L42£SelfVector,L42£SelfVector>
    implements L42Cachable<L42£SelfVector>{
  public L42£SelfVector(){}
  Supplier<L42Cache<L42£SelfVector,?>> kCache=()->this;
  public final ArrayList<Object> inner=new ArrayList<>();
  {inner.add(null);inner.add(null);}
  public int size(){return inner.size();}
  public L42£SelfVector get(int i){return (L42£SelfVector)inner.get(i);}
  public is.L42.nativeCode.Flags getFlag(int i){return (is.L42.nativeCode.Flags)inner.get(i);}  
  public void set(int i,Object that){inner.set(i,that);}
  public void add(int i,Object that){inner.add(i,that);}
  public void remove(int i){inner.remove(i);}
  @Override public int fn(L42£SelfVector t){return inner.size();}
  @Override public L42Cache<?,?> rawFieldCache(int i){return kCache.get();}
  @Override public L42Cache<L42£SelfVector,?> myCache(){return this;}
  @Override protected L42£SelfVector newInstance(L42£SelfVector t){return new L42£SelfVector();}
  @SuppressWarnings("unchecked")
  public static final Class<L42£SelfVector> _class=L42£SelfVector.class;
  @Override public Object typename(){return TrustedKind.SelfVector;}
  L42£SelfVector myNorm=null;
  @Override public boolean isNorm(L42£SelfVector t){return myNorm!=null;}
  @Override public L42£SelfVector getMyNorm(L42£SelfVector me) {return myNorm;}
  @Override public void setMyNorm(L42£SelfVector me, L42£SelfVector norm){myNorm=norm;}
  @Override public void setField(int i, Object o){throw unreachable();}
  @Override public Object getField(int i){throw unreachable();}
  @Override public int numFields(){throw unreachable();}
  @Override public void setNorm(L42£SelfVector norm){this.myNorm=norm;}
  @Override public L42£SelfVector myNorm(){return myNorm;}
  @Override public L42£SelfVector newInstance(){return new L42£SelfVector();}
  @Override public L42£SelfVector _fields(L42£SelfVector t){return t;}
  @Override public Object f(L42£SelfVector t, int i, L42£SelfVector _fields){throw unreachable();}
  @Override public void setF(L42£SelfVector t, int i, Object o, L42£SelfVector _fields){throw unreachable();}
  }