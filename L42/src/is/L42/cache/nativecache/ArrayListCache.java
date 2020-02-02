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

public class ArrayListCache implements L42Cache<ArrayList<Object>> {

  private final Map<KeyNorm2D, Object> normMap = L42CacheMap.newNormMap();
  private final Map<Object, L42Cache<ArrayList<Object>>> types = new IdentityHashMap<>();
  //TODO: This doesn't seem like a good way to do this.
  private final Set<WeakReference<Object>> normSet = new HashSet<WeakReference<Object>>();
  
  private void add(KeyNorm2D key, ArrayList<Object> t) {
    normMap.put(key, t);
    normSet.add(new WeakReference<>(t));
    }
  
  @Override 
  public void addObjectOverride(KeyNorm2D key, ArrayList<Object> value) {
    normMap.put(key, value);
    normSet.add(new WeakReference<>(value));
    }
  

  @Override
  public ArrayList<Object> normalize(ArrayList<Object> t) {
    NormResult<ArrayList<Object>> res = normalizeInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return res.result(); }
    else { return LoopCache.normalizeCircle(t, res.circle()); }
    }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public NormResult<ArrayList<Object>> normalizeInner(ArrayList<Object> list, List<Object> prevs) {
    if(!isNorm(list)) {
      prevs.add(list);
      boolean inCircle = false;
      Set<Object> circle = null;   
      for(int i = 0; i < list.size(); i++) {
        if(list.get(i) == null) { continue; }
        final int j = 0;
        if(prevs.stream().anyMatch((o) -> { return o == list.get(j); })) {
          List<Object> sl = prevs.subList(prevs.indexOf(list.get(i)), prevs.size());
          if(circle == null) { circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
          else { circle = union(circle, sl); }
          inCircle = true;
          continue;
          }
        L42Cache cache = L42CacheMap.getCacheObject(list.get(i));
        NormResult res = cache.normalizeInner(list.get(i), new ArrayList<Object>(prevs));
        if(res.hasResult()) { list.set(i, res.result()); }
        else if(!res.circle().contains(list)) {  list.set(i, LoopCache.normalizeCircle(list.get(i), res.circle())); }
        else {
          inCircle = true;
          circle = circle == null ? res.circle() : union(circle, res.circle());
          }
        }
      if(inCircle) { return new NormResult(circle); }
      KeyNorm2D key = this.simpleKey(list);
      if(normMap.containsKey(key)) { return new NormResult(normMap.get(key)); }
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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public NormResult<ArrayList<Object>> computeKeyNNInner(ArrayList<Object> list, List<Object> prevs) {
    prevs.add(list);
    boolean inCircle = false;
    Set<Object> circle = null;   
    for(int i = 0; i < list.size(); i++) {
      if(list.get(i) == null) { continue; }
      final int j = i;
      if(prevs.stream().anyMatch((o) -> { return o == list.get(j); })) {
        List<Object> sl = prevs.subList(prevs.indexOf(list.get(i)), prevs.size());
        if(circle == null) { circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
        else { circle = union(circle, sl); }
        inCircle = true;
        continue;
        }
      L42Cache cache = L42CacheMap.getCacheObject(list.get(i));
      NormResult res = cache.computeKeyNNInner(list.get(i), new ArrayList<Object>(prevs));
      if(!res.hasResult() && res.circle().contains(list)) {
        inCircle = true;
        circle = circle == null ? res.circle() : union(circle, res.circle());
        }
      }
    if(inCircle) { return new NormResult(circle); }
    KeyNorm2D key = this.simpleKey(list);
    if(normMap.containsKey(key)) { return new NormResult(normMap.get(key)); }
    else { return new NormResult<ArrayList<Object>>(list);  }  
    }
  
  @Override
  public boolean isNorm(ArrayList<Object> t) { 
    return normSet.contains(new WeakReference<>(t));
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
    return t.toArray();
    }
  
  @Override
  public Object f(ArrayList<Object> t, int i) {
    return t.get(i);
    }
  
  @Override
  public void f(ArrayList<Object> t, Object o, int i) {
    t.set(i, o);
    }
  
  @Override
  public String typename() {
    return "£ArrayList";
    }
  
  @SuppressWarnings("rawtypes") 
  @Override 
  public L42Cache rawFieldCache(int i) {
    return null; 
    //TODO: Fix this
    }
  
  @SuppressWarnings("unchecked")
  @Override 
  public ArrayList<Object> getMyNorm(ArrayList<Object> me) { 
    return (ArrayList<Object>) me.get(1);
    }

  @Override 
  public void setMyNorm(ArrayList<Object> me, ArrayList<Object> norm) { 
   me.add(norm);
   }
  
  @SuppressWarnings("unchecked") 
  public static <T> Set<T> union(Collection<T> l1, Collection<T> l2)
  {
    Set<T> set = (Set<T>) L42CacheMap.identityHashSet();
    set.addAll(l1);
    set.addAll(l2);
    return set;
  }

  public final class ArrayListCacheForType implements L42Cache<ArrayList<Object>> {
    
    L42Cache<?> type;
    
    public ArrayListCacheForType(L42Cache<?> type) {
      this.type = type;
      }
    
    @SuppressWarnings("rawtypes") 
    @Override 
    public L42Cache rawFieldCache(int i) {
      return type; 
      //TODO: Fix this
      }

    @Override 
    public ArrayList<Object> normalize(ArrayList<Object> t) { 
      return ArrayListCache.this.normalize(t);
      }

    @Override 
    public NormResult<ArrayList<Object>> normalizeInner(ArrayList<Object> t, List<Object> chain) {
      return ArrayListCache.this.normalizeInner(t, chain); 
      }

    @Override 
    public KeyNorm2D computeKeyNN(ArrayList<Object> t) { 
      return ArrayListCache.this.computeKeyNN(t);
      }

    @Override 
    public NormResult<ArrayList<Object>> computeKeyNNInner(ArrayList<Object> t, List<Object> chain) {
      return ArrayListCache.this.computeKeyNNInner(t, chain);
      }

    @Override
    public void addObjectOverride(KeyNorm2D key, ArrayList<Object> obj) { 
      ArrayListCache.this.addObjectOverride(key, obj);
      }

    @Override 
    public boolean isNorm(ArrayList<Object> t) { 
      return ArrayListCache.this.isNorm(t);
      }

    @Override 
    public boolean structurallyEquals(ArrayList<Object> t1, ArrayList<Object> t2) { 
      return ArrayListCache.this.structurallyEquals(t1, t2); 
      }

    @Override 
    public boolean identityEquals(ArrayList<Object> t1, ArrayList<Object> t2) { 
      return ArrayListCache.this.identityEquals(t1, t2); 
      }

    @Override 
    public Object[] f(ArrayList<Object> t) {
      return ArrayListCache.this.f(t); 
      }

    @Override 
    public Object f(ArrayList<Object> t, int i) {
      return ArrayListCache.this.f(t, i); 
      }

    @Override 
    public void f(ArrayList<Object> t, Object o, int i) { 
      ArrayListCache.this.f(t, o, i);
      }

    @Override 
    public ArrayList<Object> getMyNorm(ArrayList<Object> me) {
      return ArrayListCache.this.getMyNorm(me); 
      }

    @Override
    public void setMyNorm(ArrayList<Object> me, ArrayList<Object> norm) { 
      ArrayListCache.this.setMyNorm(me, norm);
      }

    @Override 
    public String typename() { 
      return ArrayListCache.this.typename();
      }
    }

}
