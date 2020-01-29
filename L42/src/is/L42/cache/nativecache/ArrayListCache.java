package is.L42.cache.nativecache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.cache.Cache;
import is.L42.cache.KeyNorm2D;
import is.L42.cache.LoopCache;
import is.L42.cache.NormResult;
import is.L42.cache.RootCache;

@SuppressWarnings("rawtypes")
public class ArrayListCache implements Cache<ArrayList> {

  private final Map<KeyNorm2D, Object> normMap = RootCache.newNormMap();
  //TODO: This doesn't seem like a good way to do this.
  private final Set<WeakReference<Object>> normSet = new HashSet<WeakReference<Object>>();
  
  private void add(KeyNorm2D key, ArrayList t) {
    normMap.put(key, t);
    normSet.add(new WeakReference<>(t));
    }
  
  @Override 
  public void addObjectOverride(KeyNorm2D key, ArrayList value) {
    normMap.put(key, value);
    normSet.add(new WeakReference<>(value));
    }
  

  @Override
  public ArrayList normalize(ArrayList t) {
    NormResult<ArrayList> res = normalizeInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return res.result(); }
    else { return LoopCache.normalizeCircle(t, res.circle()); }
    }

  @SuppressWarnings("unchecked")
  public NormResult<ArrayList> normalizeInner(ArrayList list, List<Object> prevs) {
    if(!isNorm(list)) {
      prevs.add(list);
      boolean inCircle = false;
      Set<Object> circle = null;   
      for(int i = 0; i < list.size(); i++) {
        if(prevs.contains(list.get(i))) {
          List<Object> sl = prevs.subList(prevs.indexOf(list.get(i)), prevs.size());
          if(circle == null) { circle = new HashSet<Object>(sl); }
          else { circle = union(circle, sl); }
          inCircle = true;
          continue;
          }
        Cache cache = RootCache.getCacheObject(list.get(i));
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
      return new NormResult<ArrayList>(list);      
    } else {
      return new NormResult<ArrayList>(list);
      }
    }

  @Override
  public KeyNorm2D computeKeyNN(ArrayList t) {
    NormResult<ArrayList> res = computeKeyNNInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return this.simpleKey(res.result()); }
    else { return LoopCache.getKeyCircleNN(t, res.circle()); }
    }

  @SuppressWarnings("unchecked")
  @Override
  public NormResult<ArrayList> computeKeyNNInner(ArrayList list, List<Object> prevs) {
    prevs.add(list);
    boolean inCircle = false;
    Set<Object> circle = null;   
    for(int i = 0; i < list.size(); i++) {
      if(prevs.contains(list.get(i))) {
        List<Object> sl = prevs.subList(prevs.indexOf(list.get(i)), prevs.size());
        if(circle == null) { circle = new HashSet<Object>(sl); }
        else { circle = union(circle, sl); }
        inCircle = true;
        continue;
        }
      Cache cache = RootCache.getCacheObject(list.get(i));
      NormResult res = cache.computeKeyNNInner(list.get(i), new ArrayList<Object>(prevs));
      if(!res.hasResult() && res.circle().contains(list)) {
        inCircle = true;
        circle = circle == null ? res.circle() : union(circle, res.circle());
        }
      }
    if(inCircle) { return new NormResult(circle); }
    KeyNorm2D key = this.simpleKey(list);
    if(normMap.containsKey(key)) { return new NormResult(normMap.get(key)); }
    else { return new NormResult<ArrayList>(list);  }  
    }
  
  @Override
  public boolean isNorm(ArrayList t) { 
    return normSet.contains(new WeakReference<>(t));
    }

  @Override
  public boolean structurallyEquals(ArrayList t1, ArrayList t2) {
    t1 = normalize(t1);
    t2 = normalize(t2);
    return t1 == t2;
    }
  
  @Override 
  public boolean identityEquals(ArrayList t1, ArrayList t2) {
    return t1 == t2; 
    }
  
  @Override
  public Object[] f(ArrayList t) {
    return t.toArray();
    }
  
  @Override
  public Object f(ArrayList t, int i) {
    return t.get(i);
    }
  
  @SuppressWarnings("unchecked")
  @Override
  public void f(ArrayList t, Object o, int i) {
    t.set(i, o);
    }
  
  @Override
  public String typename(ArrayList t) {
    return "£ArrayList";
    }
  
  @Override 
  public Cache rawFieldCache(int i) {
    return null; 
    }
  
  public static <T> Set<T> union(Collection<T> l1, Collection<T> l2)
  {
    Set<T> set = new HashSet<T>();
    set.addAll(l1);
    set.addAll(l2);
    return set;
  }

}
