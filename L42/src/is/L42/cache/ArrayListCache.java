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

public class ArrayListCache implements L42Cache<ArrayList<?>> {

  protected final Map<KeyNorm2D, ArrayList<?>> normMap;
  
  protected ArrayListCache(Map<KeyNorm2D, ArrayList<?>> normMap){this.normMap = normMap;}
   
  public ArrayListCache(){normMap =L42CacheMap.newNormMap();}
  
  private void add(KeyNorm2D key, ArrayList<?> t) {
    normMap.put(key, t);
    this.setMyNorm(t, t);
    }
  
  @Override 
  public void addObjectOverride(KeyNorm2D key, ArrayList<?> value) {
    normMap.put(key, value);
    }
  
  @Override
  public ArrayList<?> normalize(ArrayList<?> t) {
    NormResult<ArrayList<?>> res = normalizeInner(t, new ArrayList<>());
    if(res.hasResult()) { return res.result(); }
    else { return LoopCache.normalizeCircle(t, res.circle()); }
    }

  @Override
  public NormResult<ArrayList<?>> normalizeInner(ArrayList<?> list, List<Object> prevs) {
    if(isNorm(list)){return new NormResult<>(list);}
    prevs.add(list);
    Set<Object> circle = _computeCircle(list,prevs,true);
    if(circle!=null){return new NormResult<>(circle);}
    KeyNorm2D key = this.simpleKey(list);
    if(normMap.containsKey(key)) { 
      ArrayList<?> t2 = normMap.get(key);
      this.setMyNorm(list, t2);
      return new NormResult<>(t2); 
      }
    this.add(key, list);
    return new NormResult<>(list);
    }
  @Override
  public KeyNorm2D computeKeyNN(ArrayList<?> t) {
    NormResult<ArrayList<?>> res = computeKeyNNInner(t, new ArrayList<>());
    if(res.hasResult()) { return this.simpleKey(res.result()); }
    else { return LoopCache.getKeyCircleNN(t, res.circle()); }
    }

  private Set<Object> _computeCircle(ArrayList<?> list, List<Object> prevs,boolean norm){
    Set<Object> circle = null;
    int size=fn(list);
    for(int i = 0; i < size; i++) {
      var vali=f(list, i);
      if(vali==null){assert this.rawFieldCache(i)!=null; continue;}
      if(prevs.stream().anyMatch(o->o==vali)){
        List<Object> sl = prevs.subList(indexOf(prevs,vali), prevs.size());
        circle=addCircle(circle,sl);
        continue;
        }
      L42Cache<Object> cache =i%2==0?this.fieldCache(vali,i):Flags.cache;
      NormResult<Object> res=norm?
        cache.normalizeInner(vali, new ArrayList<>(prevs)):
        cache.computeKeyNNInner(vali,new ArrayList<>(prevs));
      if(norm && res.hasResult()){f(list, res.result(), i);continue;}
      if(!res.hasResult() && res.circle().contains(list)) {
        circle = circle==null ? res.circle() : addCircle(circle, res.circle());
        continue;
        }
      if(norm){f(list, LoopCache.normalizeCircle(vali, res.circle()), i);}
      }
    return circle;
    }
  @Override
  public NormResult<ArrayList<?>> computeKeyNNInner(ArrayList<?> list, List<Object> prevs) {
    prevs.add(list);
    Set<Object> circle = _computeCircle(list,prevs,false);
    if(circle!=null){return new NormResult<>(circle);}
    KeyNorm2D key = this.simpleKey(list);
    if(!normMap.containsKey(key)){return new NormResult<>(list);}
    var t2 = normMap.get(key);
    return new NormResult<>(t2); 
    }
      
  @Override
  public boolean isNorm(ArrayList<?> t) { 
    return t.get(1) != null;
    }

  @Override
  public boolean structurallyEquals(ArrayList<?> t1, ArrayList<?> t2) {
    t1 = normalize(t1);
    t2 = normalize(t2);
    return t1 == t2;
    }
  
  @Override 
  public boolean identityEquals(ArrayList<?> t1, ArrayList<?> t2) {
    return t1 == t2; 
    }
  
  @Override
  public Object[] f(ArrayList<?> t) {
    final int len = fn(t);
    Object[] arr = new Object[len];
    for(int i = 0; i < len; i++)
      arr[i] = t.get(i + 2);
    return arr;
    }
  
  @Override
  public Object f(ArrayList<?> t, int i) {
    return t.get(i + 2);
    }
  
  @SuppressWarnings("unchecked") @Override
  public void f(ArrayList<?> t, Object o, int i) {
    if((i & 1) == 1) {
      assert o instanceof Flags;
      assert t.get(i + 2).equals(o);
      return;
      }
    ((ArrayList<Object>)t).set(i + 2, o);
    }
  
  @Override 
  public int fn(ArrayList<?> t) {
    return t.size() - 2;
    }
  
  @Override
  public Object typename(){return TrustedKind.Vector;}
  
  @Override 
  public L42Cache<?> rawFieldCache(int i) {
    if(i%2!=0){return Flags.cache;}
    return null;
    }
  
  @Override 
  public ArrayList<?> getMyNorm(ArrayList<?> me) { 
    return (ArrayList<?>) me.get(1);
    }

  @SuppressWarnings("unchecked") @Override 
  public void setMyNorm(ArrayList<?> me, ArrayList<?> norm) { 
    ((ArrayList<Object>)me).set(1, norm);
    }
  
  @Override
  public L42Cache<ArrayList<?>> refine(ArrayList<?> t) {
    return new ArrayListCacheForType(this, (L42Cache<?>) t.get(0));
    }
  
  @Override public void clear(){normMap.clear();}
  
  @Override
  public ArrayList<?> dup(ArrayList<?> obj, Map<Object, Object> map){throw unreachable();}
  
  private static <T> Set<T> addCircle(Set<T> _circle, Collection<T> more){
    Set<T> res=L42CacheMap.identityHashSet();
    if(_circle != null){res.addAll(_circle);}
    res.addAll(more);
    return res;
    }
  
  public static <T> int indexOf(List<T> list, T t) {
    for(int i = 0; i < list.size(); i++) { if(list.get(i) == t) { return i; } }
    return -1;
    }
  
  public static class ArrayListCacheForType extends ArrayListCache {
    L42Cache<?> type;    
    public ArrayListCacheForType(ArrayListCache owner, L42Cache<?> type) {
      super(owner.normMap);
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
    @Override
    public ArrayList<?> dup(ArrayList<?> that, Map<Object,Object> map){ 
      if(that==null || isNorm(that)){return that;}
      ArrayList<Object> nObj = new ArrayList<Object>(that.size());
      nObj.add(that.get(0));
      nObj.add(null);
      map.put(that, nObj);
      for(int i = 0; i < this.fn(that); i++) {
        Object field = this.f(that, i);
        L42Cache<Object> fieldcache = this.fieldCache(field, i);
        if(!map.containsKey(field)){map.put(field, fieldcache.dup(field, map));}
        nObj.add(i + 2, map.get(field));
        }
      return nObj;
      }
    }
  }