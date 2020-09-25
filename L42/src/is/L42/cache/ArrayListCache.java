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
  @Override protected void add(KeyNorm2D key, ArrayList<?> t) {
    super.add(key,t);
    this.setMyNorm(t, t);
    }  
  @Override public boolean isNorm(ArrayList<?> t){return t==null ||t.get(1)!=null;}
  //above, if t==null, this cache is used for an Optional ArrayList, and the empty optional is
  //the normalized verion of itself
  @Override public int fn(ArrayList<?> t){return t.size()-2;}
  @Override public Object typename(){return TrustedKind.Vector;}
  @Override public L42Cache<?> rawFieldCache(Object o,int i){
      if(i%2!=0){return Flags.cache;}
      if(o!=null) {return L42CacheMap.getCacheObject(o);}
      return this;
      }
  @Override public ArrayList<?> getMyNorm(ArrayList<?> me){
      if(me==null) {return null;}///the normalized version of iself
      return (ArrayList<?>) ((Object[])me.get(1))[0];
      }
  @SuppressWarnings("unchecked") @Override 
  public void setMyNorm(ArrayList<?> me, ArrayList<?> norm){
    ((ArrayList<Object>)me).set(1, new Object[]{norm});
    //Norm wrapped up as array, otherwise when placed as hashmap key it would loop 
    }
  }