package is.L42.maps;
import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import is.L42.cache.AbstractStructuredCache;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.maps.L42£Set.SetCache;
import is.L42.nativeCode.TrustedKind;
import is.L42.numbers.L42£BigRational;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42NoFields.EqCache;
import is.L42.tools.General;
public class L42£SelfVector implements L42Cachable<L42£SelfVector>{
  public L42£SelfVector(){}
  public final ArrayList<Object> inner=new ArrayList<>();
  {inner.add(null);inner.add(null);}
  public int size(){return inner.size();}
  public L42£SelfVector get(int i){return (L42£SelfVector)inner.get(i);}
  public is.L42.nativeCode.Flags getFlag(int i){return (is.L42.nativeCode.Flags)inner.get(i);}  
  public void set(int i,Object that){inner.set(i,that);}
  public void add(int i,Object that){inner.add(i,that);}
  public void remove(int i){inner.remove(i);}
  public static final Class<L42£SelfVector> _class=L42£SelfVector.class;
  L42£SelfVector myNorm=null;
  @Override public void setNorm(L42£SelfVector norm){this.myNorm=norm;}
  @Override public L42£SelfVector myNorm(){return myNorm;}
  @Override public void setField(int i, Object o){inner.set(i+2,o);}
  @Override public Object getField(int i){return inner.get(i+2);}
  @Override public int numFields(){return inner.size()-2;}
  @Override public L42£SelfVector newInstance(){return new L42£SelfVector();}
  @Override public L42Cache<L42£SelfVector> myCache(){return myCache;}
  public static final SelfVectorCache myCache=new SelfVectorCache();
  public static class SelfVectorCache extends AbsSetCache<L42£SelfVector>{
    public SelfVectorCache(){super();}
    @Override public Object f(L42£SelfVector t, int i){return t.getField(i);}
    @Override public void setF(L42£SelfVector t, int i, Object o){t.setField(i,o);}
    @Override public L42Cache<?> rawFieldCache(Object t,int i){return this;}
    @Override public int fn(L42£SelfVector t){return t.size();}
    @Override public Object typename(){return TrustedKind.SelfVector;}
    @Override protected L42£SelfVector newInstance(L42£SelfVector t){return t.newInstance();}
    }
  }