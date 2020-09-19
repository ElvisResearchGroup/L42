package is.L42.cache;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.unreachable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import is.L42.cache.nativecache.BoolCache;
import is.L42.nativeCode.Flags;
import is.L42.nativeCode.TrustedKind;
import is.L42.tools.General;

public class ArrayListCache extends AbstractStructuredCache<ArrayList<?>>{
  @Override public Object f(ArrayList<?> t, int i){return t.get(i + 2);}
  @Override public void setF(ArrayList<?> t,int i,Object o){
    if(i+2>=t.size()){t.add(null);t.add(null);}//so that set is "also" an add
    @SuppressWarnings("unchecked")
    var tt=(ArrayList<Object>)t;
    tt.set(i + 2, o);    
    }      
  @Override protected ArrayList<?> newInstance(ArrayList<?> t){
    var res=new ArrayList<>(t.size());
    res.add(t.get(0));
    res.add(null);
    return res;
    }
  public ArrayListCache(){super();}
  private ArrayListCache(ArrayListCache o){super(o);}
  @Override protected void add(KeyNorm2D key, ArrayList<?> t) {
    super.add(key,t);
    this.setMyNorm(t, t);
    }  
  @Override public boolean isNorm(ArrayList<?> t){return t==null ||t.get(1)!=null;}
  //above, if t==null, this cache is used for an Optional ArrayList, and the empty optional is
  //the normalized verion of itself
  @Override public int fn(ArrayList<?> t){return t.size()-2;}
  @Override public Object typename(){return TrustedKind.Vector;}
  @Override public L42Cache<?> rawFieldCache(int i){throw unreachable();}
  @Override public ArrayList<?> getMyNorm(ArrayList<?> me){
      if(me==null) {return null;}///the normalized version of iself
      return (ArrayList<?>) ((Object[])me.get(1))[0];
      }
  @SuppressWarnings("unchecked") @Override 
  public void setMyNorm(ArrayList<?> me, ArrayList<?> norm){
    ((ArrayList<Object>)me).set(1, new Object[]{norm});
    }
  @Override public L42Cache<ArrayList<?>> refine(ArrayList<?> t) {
    var c=(L42Cache<?>)(t==null?L42CacheMap.boolCache:t.get(0));//Tricky: if t is null then this is
    //a 'composite' cache also for Optional List and this is the empty optional.
    //we reuse the same cache of T also for OptT, and for lists, we reuse the same map for all
    //the kinds of T, so 'boolCache' is good as any other for a cache for the empty optional
    return new ArrayListCache(this){
      @Override public L42Cache<?> rawFieldCache(int i){
        if(i%2!=0){return Flags.cache;}
        return c;
        }
      @Override public int hashCode(){return Objects.hashCode(c);}
      @Override public boolean equals(Object o){
        return General.eq(this,o,(o1,o2)->
          Objects.equals(o1.rawFieldCache(0),o2.rawFieldCache(0)));
        }
      };
    }
  }