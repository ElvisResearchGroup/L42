package is.L42.cache;

import static is.L42.tools.General.unreachable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.nativeCode.Flags;
import is.L42.nativeCode.TrustedKind;
import is.L42.tools.General;

public class ArrayListCache extends AbstractStructuredCache<ArrayList<?>,ArrayList<?>>{
  @Override ArrayList<?> _fields(ArrayList<?> t){return t;}
  @Override Object f(ArrayList<?> t,int i,ArrayList<?> _fields){return f(t,i);}
  @Override void setF(ArrayList<?> t,int i,Object o,ArrayList<?> _fields){f(t,o,i);}
  @Override ArrayList<?> newInstance(ArrayList<?> t){
    var res=new ArrayList<>(t.size());
    res.add(t.get(0));
    res.add(null);
    return res;
    }
  public ArrayListCache(){super();}
  @Override void add(KeyNorm2D key, ArrayList<?> t) {
    super.add(key,t);
    this.setMyNorm(t, t);
    }  
  @Override public boolean isNorm(ArrayList<?> t){return t.get(1)!=null;}
  @Override public Object[] f(ArrayList<?> t) {
    //It is called in LoopCache... TODO: we could refactor this, but it also happen for normal objects anyway
    final int len = fn(t);
    Object[] arr = new Object[len];
    for(int i = 0; i < len; i++)
      arr[i] = t.get(i + 2);
    return arr;
    }  
  @Override public Object f(ArrayList<?> t, int i){return t.get(i + 2);}
  @SuppressWarnings("unchecked") @Override
  public void f(ArrayList<?> t, Object o, int i){
    if(i+2>=t.size()){t.add(null);t.add(null);}//so that set is "also" an add
    ((ArrayList<Object>)t).set(i + 2, o);
    }      
  @Override public int fn(ArrayList<?> t){return t.size()-2;}
  @Override public Object typename(){return TrustedKind.Vector;}
  @Override public L42Cache<?> rawFieldCache(int i) {
    if(i%2!=0){return Flags.cache;}
    return null;
    }
  @Override public ArrayList<?> getMyNorm(ArrayList<?> me){return (ArrayList<?>) me.get(1);}
  @SuppressWarnings("unchecked") @Override 
  public void setMyNorm(ArrayList<?> me, ArrayList<?> norm){
    ((ArrayList<Object>)me).set(1, norm);
    }
  @Override public L42Cache<ArrayList<?>> refine(ArrayList<?> t) {
    return new ArrayListCacheForType(this, (L42Cache<?>) t.get(0));
    }
  ArrayListCache(AbstractStructuredCache<ArrayList<?>,ArrayList<?>> o){super(o);}  
  public static class ArrayListCacheForType extends ArrayListCache {
    L42Cache<?> type;    
    public ArrayListCacheForType(ArrayListCache owner, L42Cache<?> type) {
      super(owner);
      //assert type!=null;//can be null if it is an interface
      this.type = type;
      }
    @Override public int hashCode(){
      if(type==null){return 0;}
      if(type==this){return 1;}
      return type.hashCode();
      }
    @Override public boolean equals(Object o){
      if(this==o){return true;}
      if(!(o instanceof ArrayListCacheForType)){return false;}
      var otype=((ArrayListCacheForType)o).type;
      if(type==null){return otype==null;}
      return type.equals(otype);
      }
    @Override 
    public L42Cache<?> rawFieldCache(int i) {
      if(i%2!=0){return Flags.cache;}
      return type; 
      }    
    }
  }