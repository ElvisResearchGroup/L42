package is.L42.maps;
import java.util.ArrayList;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.nativeCode.TrustedKind;
public class L42£SelfList implements L42Cachable<L42£SelfList>{
  public L42£SelfList(){}
  public final ArrayList<Object> inner=new ArrayList<>();
  {inner.add(null);inner.add(null);}
  public int size(){return inner.size();}
  public L42£SelfList get(int i){return (L42£SelfList)inner.get(i);}
  public is.L42.nativeCode.Flags getFlag(int i){return (is.L42.nativeCode.Flags)inner.get(i);}  
  public void set(int i,Object that){inner.set(i,that);}
  public void add(int i,Object that){inner.add(i,that);}
  public void remove(int i){inner.remove(i);}
  public static final Class<L42£SelfList> _class=L42£SelfList.class;
  L42£SelfList myNorm=null;
  @Override public void setNorm(L42£SelfList norm){this.myNorm=norm;}
  @Override public L42£SelfList myNorm(){return myNorm;}
  @Override public void setField(int i, Object o){inner.set(i+1,o);}
  @Override public Object getField(int i){return inner.get(i+1);}
  @Override public int numFields(){return inner.size()-1;}
  @Override public L42£SelfList newInstance(){return new L42£SelfList();}
  @Override public L42Cache<L42£SelfList> myCache(){return myCache;}
  public static final L42Cache<L42£SelfList> myCache=new SelfListCache();
  public static class SelfListCache extends AbsSetCache<L42£SelfList>{
    public SelfListCache(){super();}
    @Override public Object f(L42£SelfList t, int i){return t.getField(i);}
    @Override public void setF(L42£SelfList t, int i, Object o){t.setField(i,o);}
    @Override public L42Cache<?> rawFieldCache(Object t,int i){return this;}
    @Override public int fn(L42£SelfList t){return t.size();}
    @Override public Object typename(){return TrustedKind.SelfList;}
    @Override protected L42£SelfList newInstance(L42£SelfList t){return t.newInstance();}
    }
  }