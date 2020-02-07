package is.L42.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class L42StandardCache<T extends L42Cachable<T>> implements L42Cache<T> {
  
  @SuppressWarnings("unchecked") 
  private final Map<KeyNorm2D, T> normMap = (Map<KeyNorm2D, T>) L42CacheMap.newNormMap();
  
  private final String typename;
  private L42Cache<?>[] caches;
  
  public L42StandardCache(String typename, Class<T> myClass) {
    this.typename = typename;   
    L42CacheMap.addCacheableType(myClass, this);
    }
  
  public void lateInitialize(Class<?> ... classes) {
    caches = L42CacheMap.getCacheArray(classes);
  }
  
  private void add(KeyNorm2D key, T t) {
    normMap.put(key, t);
    t.setNorm(t);
    }
  
   @Override 
   public void addObjectOverride(KeyNorm2D key, T value) {
     normMap.put(key, value);
     }
  
  @Override
  public T normalize(T t) {
    NormResult<T> res = normalizeInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return res.result(); }
    else { return LoopCache.normalizeCircle(t, res.circle()); }
    }
  
  public NormResult<T> normalizeInner(T t, List<Object> prevs) {
    if(!this.isNorm(t)) {
      prevs.add(t);
      boolean inCircle = false;
      Object[] fields = t.allFields();
      Set<Object> circle = null;   
      for(int i = 0; i < fields.length; i++) {
        if(fields[i] == null) { 
          if(this.rawFieldCache(i) == null) { throw new NullPointerException("Cannot have null field with an interface type."); } 
          continue; 
          }
        if(prevs.contains(fields[i])) {
          List<Object> sl = prevs.subList(prevs.indexOf(fields[i]), prevs.size());
          if(circle == null) { circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
          else { circle = union(circle, sl); }
          inCircle = true;
          continue;
          }
        L42Cache<Object> cache = this.fieldCache(fields[i], i);
        NormResult<Object> res = cache.normalizeInner(fields[i], new ArrayList<Object>(prevs));
        if(res.hasResult()) {
          t.setField(i, fields[i] = res.result());
          } else if(!res.circle().contains(t)) {
          t.setField(i, fields[i] = LoopCache.normalizeCircle(fields[i], res.circle())); 
          } else {
          inCircle = true;
          circle = circle == null ? res.circle() : union(circle, res.circle());
          }
        }
      if(inCircle) { return new NormResult<T>(circle); }
      KeyNorm2D key = this.simpleKey(t);
      if(normMap.containsKey(key)) { 
        T t2 = normMap.get(key);
        t.setNorm(t2);
        return new NormResult<T>(t2); 
        }
      this.add(key, t);
      return new NormResult<T>(t);      
      } else { return new NormResult<T>(t.myNorm()); }
    }
  
  @Override
  public KeyNorm2D computeKeyNN(T t) {
    NormResult<T> res = computeKeyNNInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return this.simpleKey(res.result()); }
    else { return LoopCache.getKeyCircleNN(t, res.circle()); }
    }
  
  @Override
  public NormResult<T> computeKeyNNInner(T t, List<Object> prevs) {
    prevs.add(t);
    boolean inCircle = false;
    Object[] fields = t.allFields();
    Set<Object> circle = null;   
    for(int i = 0; i < fields.length; i++) {
      if(fields[i] == null) { 
        if(this.rawFieldCache(i) == null) { throw new NullPointerException("Cannot have null field with an interface type."); } 
        continue; 
        }
      if(prevs.contains(fields[i])) {
        List<Object> sl = prevs.subList(prevs.indexOf(fields[i]), prevs.size());
        if(circle == null) {  circle = L42CacheMap.identityHashSet(); circle.addAll(sl); }
        else { circle = union(circle, sl); }
        inCircle = true;
        continue;
        }
      L42Cache<Object> cache = this.fieldCache(fields[i], i);
      NormResult<Object> res = cache.computeKeyNNInner(fields[i], new ArrayList<Object>(prevs));
      if(!res.hasResult() && res.circle().contains(t)) {
        inCircle = true;
        circle = circle == null ? res.circle() : union(circle, res.circle());
        }
      }
    if(inCircle) { return new NormResult<T>(circle); }
    KeyNorm2D key = this.simpleKey(t);
    if(normMap.containsKey(key)) { return new NormResult<T>(normMap.get(key)); }
    else { return new NormResult<T>(t);  }    
    }

  @Override
  public boolean isNorm(T t) { return t.isNorm(); }

  @Override
  public boolean structurallyEquals(T t1, T t2) {
    t1 = normalize(t1);
    t2 = normalize(t2);
    return t1 == t2;
    }
  
  @Override 
  public boolean identityEquals(T t1, T t2) {
    return t1 == t2; 
    }
  
  @Override
  public Object[] f(T t) { return t.allFields(); }
  
  @Override
  public Object f(T t, int i) { return t.getField(i); }
  
  @Override
  public void f(T t, Object o, int i) { t.setField(i, o); }
  
  @Override 
  public int fn(T t) { return t.numFields(); }
  
  @Override
  public void clear() {
    normMap.clear();
    }
  
  public static <T> Set<T> union(Collection<T> l1, Collection<T> l2) {
    Set<T> set = new HashSet<T>();
    set.addAll(l1);
    set.addAll(l2);
    return set;
    }
  
  @Override
  public String typename() {
    return typename;
    }
  
  @Override 
  public L42Cache<?> rawFieldCache(int i) { 
    return caches[i]; 
    }

  @Override
  public T getMyNorm(T me) {
    return me.myNorm(); 
    }

  @Override 
  public void setMyNorm(T me, T norm) { 
    me.setNorm(norm);
     }
  
}
