package is.L42.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class LoopCache {
  
  public static boolean USE_DFA_MINIMIZATION = true;
  public static boolean NO_MINIMIZE_ON_READ = true;
  
  private static final Map<KeyNorm2D, Object> circularIndex = L42CacheMap.newNormMap();
  
  @SuppressWarnings({  "unchecked" }) 
  public static <T> T normalizeCircle(T desired, Set<Object> circle) 
  {
    Map<Object, Set<Object>> equivClasses = new IdentityHashMap<>();
    Map<Object, List<Object>> circleFields = new IdentityHashMap<>();
    Map<Object, Object[]> fields = new IdentityHashMap<>();
    Map<Object, L42Cache<Object>> types = new IdentityHashMap<>();
    for(Object o : circle) { 
      equivClasses.put(o,L42CacheMap.identityHashSet()); 
      circleFields.put(o, new ArrayList<>()); 
      types.put(o, L42CacheMap.getCacheObject(o));
      }
    for(Object o : circle) {
      L42Cache<Object> myType = types.get(o);
      for(Object o2 : circle) {
        if(myType.equals(types.get(o2))) {
          equivClasses.get(o).add(o2);
          }
        }
      Object[] f = myType.f(o);
      fields.put(o, f);
      for(Object field : f) {
        if(circle.contains(field)) {
          circleFields.get(o).add(field);
          }
        }
      }
    Set<Object> toRemove =L42CacheMap.identityHashSet();
    int repl = 0;
    do {
      repl = 0;
      for(Object o : circle) {
        toRemove.clear();
        object2:
        for(Object o2 : equivClasses.get(o)) {
          List<Object> f1 = circleFields.get(o);
          List<Object> f2 = circleFields.get(o2);
          for(int i = 0; i < f1.size(); i++) {
            if(i >= f2.size() || Collections.disjoint(equivClasses.get(f1.get(i)), equivClasses.get(f2.get(i)))) {
              toRemove.add(o2);
              equivClasses.get(o2).remove(o);
              repl++;
              break object2;
              }
            }
          }
        equivClasses.get(o).removeAll(toRemove);
        }
      } while(repl > 0);
    Map<Object, Object> replacements = new IdentityHashMap<>();
    Set<Object> used = L42CacheMap.identityHashSet();
    Set<Object> nircle = L42CacheMap.identityHashSet();
    for(Set<Object> equiv : equivClasses.values()) {
      if(!used.containsAll(equiv)) {
        used.addAll(equiv);
        Object to  = equiv.contains(desired) ? desired : equiv.iterator().next();
        nircle.add(to);
        for(Object from : equiv) {
          replacements.put(from, to);
          }
        }
      }
    for(Object o : circle) {
      L42Cache<Object> cache = types.get(o);
      Object[] f = fields.get(o);
      for(int i = 0; i < f.length; i++) {
        if(replacements.containsKey(f[i])) {
          cache.f(o, replacements.get(f[i]), i);
          }
        }
      }
    Map<Object, CircleObject> circleobjects = new IdentityHashMap<>();
    for(Object circleobj : nircle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
    for(Object o : circle) { if(!types.get(o).isValueType()) { types.get(o).setMyNorm(o, replacements.get(o)); } }
    KeyNorm2D key = keyFromCircleObject(desired, circleobjects);
    if(circularIndex.containsKey(key)) {
      replacements.clear();
      for(Object o : nircle) {
        KeyNorm2D chonker = keyFromCircleObject(o, circleobjects);
        replacements.put(o, circularIndex.get(chonker));
        }
      for(Object o : circle) {
        if(!types.get(o).isValueType()) { 
          L42Cache<Object> cache = types.get(o);
          cache.setMyNorm(o, replacements.get(cache.getMyNorm(o)));
          }
        L42Cache<Object> cache = types.get(o);
        Object[] f = fields.get(o);
        for(int i = 0; i < f.length; i++) {
          if(replacements.containsKey(f[i])) {
            cache.f(o, replacements.get(f[i]), i);
            }
          }
        }
      return (T) circularIndex.get(key);
      } else {
      for(Object o : nircle) {
        KeyNorm2D chonker = keyFromCircleObject(o, circleobjects);
        KeyNorm2D simplified = simpleKeyFromChonker(o, chonker);
        circularIndex.put(chonker, o);
        types.get(o).addObjectOverride(simplified, o);
        }
      return (T) desired;
      }
    }
  
  public static KeyNorm2D getKeyCircleNN(Object desired, Set<Object> circle) {    
    Map<Object, CircleObject> circleobjects = new IdentityHashMap<>();
    for(Object circleobj : circle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
    return keyFromCircleObject(desired, circleobjects);
  }
  
  private static KeyNorm2D keyFromCircleObject(Object circleobj, Map<Object, CircleObject> circleobjects)
  {
    Map<Object, Integer> varnames = new IdentityHashMap<>();
    List<Object> order = new ArrayList<>();
    circleobjects.get(circleobj).constructVarMap(varnames, order, circleobjects, new MyInteger(0));
    Object[][] lines = new Object[order.size()][];
    for(int i = 0; i < lines.length; i++) { lines[i] = (circleobjects.get(order.get(i))).toKey(varnames); }
    return new KeyNorm2D(lines);
  }
  
  private static <T> KeyNorm2D simpleKeyFromChonker(T obj, KeyNorm2D key) {
    L42Cache<T> cache = L42CacheMap.getCacheObject(obj);
    Object[] ln1 = key.lines()[0];
    Object[] ln1cpy = new Object[ln1.length];
    System.arraycopy(ln1, 0, ln1cpy, 0, ln1.length);
    for(int i = 1; i < ln1.length; i++) {
      if(ln1[i] instanceof KeyVarID) {
        ln1cpy[i] = cache.f(obj, i - 1);
        }
      }
    return new KeyNorm2D(new Object[][] { ln1cpy });
    }
  
  protected static class Field {  
    boolean isNorm;
    Object value;
    
    public Field(Object o, boolean isnorm) {
      this.value = o;
      this.isNorm = isnorm;
      }
    
    public String toString(Map<Object, Integer> varmap) {
      if(isNorm) { return Integer.toHexString(System.identityHashCode(value)); }
      return "" + varmap.get(this.value);
      }
    
    public Object toKeyObject(Map<Object, Integer> varmap) {
      if(!varmap.containsKey(value)) { return value; }
      else { return new KeyVarID(varmap.get(this.value)); }
      }
    
    public void replaceNN(Map<Object, Object> replacements) {
      if(replacements.containsKey(value)) {
        value = replacements.get(value);
        isNorm = L42CacheMap.isNorm(value);
        }
      }
    }
  
  protected static class CircleObject  {
    private final Object obj;
    private final Field[] params;
    
    private final L42Cache<Object> cache;
    
    public CircleObject(Object obj) {
      cache = L42CacheMap.getCacheObject(obj);
      Object[] rawfields = cache.f(obj);
      Field[] fields = new Field[rawfields.length];
      for(int i = 0; i < rawfields.length; i++) {
        boolean isNorm=L42CacheMap.isNorm(rawfields[i],i,cache);
        fields[i] = new Field(rawfields[i], isNorm);
        }
      this.obj = obj;
      this.params = fields;
      }
    
    public Object getUnderlyingObject() { return this.obj; }
    
    public CircleObject replace(Map<Object, Object> replacements) {
      //TODO: Optimize this
      Object[] fields = cache.f(obj);
      for(int i = 0; i < fields.length; i++)
        if(replacements.containsKey(fields[i]))
          cache.f(obj, replacements.get(fields[i]), i);
      return new CircleObject(this.obj);
      }
    
    public CircleObject replaceNN(Map<Object, Object> replacements) {
      for(Field field : params) {field.replaceNN(replacements); }
      return this;
      }
    
    public void constructVarMap(Map<Object, Integer> varmap, List<Object> order, Map<Object, CircleObject> amap, MyInteger i) {
      varmap.put(this.obj, i.getAndIncrement());
      order.add(this.obj);
      for(Field f : params)
        if(!varmap.containsKey(f.value) && amap.containsKey(f.value))
          amap.get(f.value).constructVarMap(varmap, order, amap, i);
      }
    
    public String toString(Map<Object, Integer> map) {
      StringBuilder builder = new StringBuilder();
      builder.append(map.get(this.obj));
      builder.append("=n ");
      builder.append(cache.typename());
      builder.append("(");
      for(int i = 0; i < params.length; i++) {
        builder.append(params[i].toString(map));
        if(i < params.length - 1) { builder.append(","); }
        }
      builder.append(");");
      return builder.toString();
      }
    
    public Object[] toKey(Map<Object, Integer> map) {
      Object[] line = new Object[this.params.length + 1];
      line[0] = this.cache;
      for(int i = 0; i < this.params.length; i++) {
        line[i + 1] = this.params[i].toKeyObject(map);
        }
      return line;
      }
    }
  
  protected static class MyInteger {
    
    private int value;
    
    public MyInteger(int value) {
      this.value = value;
      }
    
    public int getAndIncrement() {
      return value++;
      }
    
    }

  }
