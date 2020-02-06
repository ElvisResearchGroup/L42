package is.L42.cache.nativecache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.cache.KeyNorm2D;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.LoopCache;
import is.L42.cache.NormResult;
import is.L42.nativeCode.Flags;

public class ArrayListCache implements L42Cache<ArrayList<Object>> {

  protected final Map<KeyNorm2D, ArrayList<Object>> normMap;
  protected final Map<Object, L42Cache<ArrayList<Object>>> types;
  
  protected ArrayListCache(Map<KeyNorm2D, ArrayList<Object>> normMap,  
                           Map<Object, L42Cache<ArrayList<Object>>> types) {
    this.normMap = normMap;
    this.types = types;
    }
  
  @SuppressWarnings("unchecked") 
  public ArrayListCache() {
    normMap = (Map<KeyNorm2D, ArrayList<Object>>) ((Map<?, ?>) L42CacheMap.newNormMap());
    types = new IdentityHashMap<>();
    }
  
  private void add(KeyNorm2D key, ArrayList<Object> t) {
    normMap.put(key, t);
    this.setMyNorm(t, t);
    }
  
  @Override 
  public void addObjectOverride(KeyNorm2D key, ArrayList<Object> value) {
    normMap.put(key, value);
    }
  
  @Override
  public ArrayList<Object> normalize(ArrayList<Object> t) {
    NormResult<ArrayList<Object>> res = normalizeInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return res.result(); }
    else { return LoopCache.normalizeCircle(t, res.circle()); }
    }

  @Override
  public NormResult<ArrayList<Object>> normalizeInner(ArrayList<Object> list, List<Object> prevs) {
    if(!isNorm(list)) {
      prevs.add(list);
      boolean inCircle = false;
      Set<Object> circle = null;   
      for(int i = 0; i < fn(list); i++) {
        if(f(list, i) == null) { assert this.rawFieldCache(i) != null; continue; }
        final int j = i;
        if(prevs.stream().anyMatch((o) -> { return o == f(list, j); })) { 
          List<Object> sl = prevs.subList(indexOf(prevs, f(list, i)), prevs.size());
          if(circle == null) { circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
          else { circle = union(circle, sl); }
          inCircle = true;
          continue;
          }
        L42Cache<Object> cache = L42CacheMap.getCacheObject(f(list, i));
        NormResult<Object> res = cache.normalizeInner(f(list, i), new ArrayList<Object>(prevs));
        if(res.hasResult()) { f(list, res.result(), i); }
        else if(!res.circle().contains(list)) {  f(list, LoopCache.normalizeCircle(f(list, i), res.circle()), i); }
        else {
          inCircle = true;
          circle = circle == null ? res.circle() : union(circle, res.circle());
          }
        }
      if(inCircle) { return new NormResult<ArrayList<Object>>(circle); }
      KeyNorm2D key = this.simpleKey(list);
      if(normMap.containsKey(key)) { 
        ArrayList<Object> t2 = normMap.get(key);
        this.setMyNorm(list, t2);
        return new NormResult<ArrayList<Object>>(t2); 
        }
      this.add(key, list);
      return new NormResult<ArrayList<Object>>(list);      
    } else {
      return new NormResult<ArrayList<Object>>(list);
      }
    }

  @Override
  public KeyNorm2D computeKeyNN(ArrayList<Object> t) {
    NormResult<ArrayList<Object>> res = computeKeyNNInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return this.simpleKey(res.result()); }
    else { return LoopCache.getKeyCircleNN(t, res.circle()); }
    }
  
  @Override
  public NormResult<ArrayList<Object>> computeKeyNNInner(ArrayList<Object> list, List<Object> prevs) {
    prevs.add(list);
    boolean inCircle = false;
    Set<Object> circle = null;   
    for(int i = 0; i < fn(list); i++) {
      if(f(list, i) == null) { assert this.rawFieldCache(i) != null; continue; }
      final int j = i;
      if(prevs.stream().anyMatch((o) -> { return o == f(list, j); })) {
        List<Object> sl = prevs.subList(indexOf(prevs, f(list, i)), prevs.size());
        if(circle == null) { circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
        else { circle = union(circle, sl); }
        inCircle = true;
        continue;
        }
      L42Cache<Object> cache = L42CacheMap.getCacheObject(f(list, i));
      NormResult<Object> res = cache.computeKeyNNInner(f(list, i), new ArrayList<Object>(prevs));
      if(!res.hasResult() && res.circle().contains(list)) {
        inCircle = true;
        circle = circle == null ? res.circle() : union(circle, res.circle());
        }
      }
    if(inCircle) { return new NormResult<ArrayList<Object>>(circle); }
    KeyNorm2D key = this.simpleKey(list);
    if(normMap.containsKey(key)) { 
      ArrayList<Object> t2 = (ArrayList<Object>) normMap.get(key);
      return new NormResult<ArrayList<Object>>(t2); 
       } else { return new NormResult<ArrayList<Object>>(list);  }  
    }
  
  @Override
  public boolean isNorm(ArrayList<Object> t) { 
    return t.get(1) != null;
    }

  @Override
  public boolean structurallyEquals(ArrayList<Object> t1, ArrayList<Object> t2) {
    t1 = normalize(t1);
    t2 = normalize(t2);
    return t1 == t2;
    }
  
  @Override 
  public boolean identityEquals(ArrayList<Object> t1, ArrayList<Object> t2) {
    return t1 == t2; 
    }
  
  @Override
  public Object[] f(ArrayList<Object> t) {
    final int len = fn(t);
    Object[] arr = new Object[len];
    for(int i = 0; i < len; i++)
      arr[i] = t.get(i + 2);
    return arr;
    }
  
  @Override
  public Object f(ArrayList<Object> t, int i) {
    return t.get(i + 2);
    }
  
  @Override
  public void f(ArrayList<Object> t, Object o, int i) {
    if((i & 1) == 1) {
      assert o instanceof Flags;
      assert t.get(i + 2).equals(o);
      return;
      }
    t.set(i + 2, o);
    }
  
  @Override 
  public int fn(ArrayList<Object> t) {
    return t.size() - 2;
    }
  
  @Override
  public String typename() {
    return "£ArrayList";
    }
  
  @Override 
  public L42Cache<?> rawFieldCache(int i) {
    return null; 
    }
  
  @SuppressWarnings("unchecked")
  @Override 
  public ArrayList<Object> getMyNorm(ArrayList<Object> me) { 
    return (ArrayList<Object>) me.get(1);
    }

  @Override 
  public void setMyNorm(ArrayList<Object> me, ArrayList<Object> norm) { 
    me.set(1, norm);
    }
  
  @Override
  public L42Cache<ArrayList<Object>> refine(ArrayList<Object> t) {
    if(!types.containsKey((L42Cache<?>) t.get(0)))
      types.put((L42Cache<?>) t.get(0), new ArrayListCacheForType(this, (L42Cache<?>) t.get(0)));
    return types.get((L42Cache<?>) t.get(0));
    }
  
  @Override
  public void clear() {
    normMap.clear();
    types.clear();
    }
  
  @SuppressWarnings("unchecked") 
  public static <T> Set<T> union(Collection<T> l1, Collection<T> l2)
  {
    Set<T> set = (Set<T>) L42CacheMap.identityHashSet();
    set.addAll(l1);
    set.addAll(l2);
    return set;
  }
  
  public static <T> int indexOf(List<T> list, T t) {
    for(int i = 0; i < list.size(); i++) { if(list.get(i) == t) { return i; } }
    return -1;
    }

  public static  class ArrayListCacheForType extends ArrayListCache {
    
    static L42Cache<?> flag = L42CacheMap.getCacheObject(Flags.class);
    L42Cache<?> type;
    
    public ArrayListCacheForType(ArrayListCache owner, L42Cache<?> type) {
      super(owner.normMap, owner.types);
      this.type = type;
      }
    
    @Override 
    public L42Cache<?> rawFieldCache(int i) {
      return ((i & 1) == 0) ? type : flag; 
      }
    }
  }
