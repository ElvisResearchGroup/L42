package is.L42.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoopCache {
  
  public static boolean USE_DFA_MINIMIZATION = true;
  public static boolean NO_MINIMIZE_ON_READ = true;
  
  private static final Map<KeyNorm2D, Object> circularIndex = L42CacheMap.newNormMap();
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  public static <T> T normalizeCircle2(Object desired, Set<Object> circle) 
  {
    Map<Object, Set<Object>> equivClasses = new IdentityHashMap<>();
    Map<Object, List<Object>> circleFields = new IdentityHashMap<>();
    Map<Object, Object[]> fields = new IdentityHashMap<>();
    Map<Object, L42Cache> types = new IdentityHashMap<>();
    for(Object o : circle) { 
      equivClasses.put(o, Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>())); 
      circleFields.put(o, new ArrayList<>()); 
      types.put(o, L42CacheMap.getCacheObject(o));
      }
    for(Object o : circle) {
      L42Cache myType = types.get(o);
      for(Object o2 : circle) {
        if(myType == types.get(o2)) {
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
    Set<Object> toRemove = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
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
            if(Collections.disjoint(equivClasses.get(f1.get(i)), equivClasses.get(f2.get(i)))) {
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
    Set<Object> used = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
    Set<Object> nircle = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
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
      L42Cache cache = types.get(o);
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
          L42Cache cache = types.get(o);
          cache.setMyNorm(o, replacements.get(cache.getMyNorm(o)));
          }
        L42Cache cache = types.get(o);
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
  
  /**
   * Normalizes a circle via progressive approximation, first creating keys for each object, then phasing
   * out duplicates. Ones no more approximations can be done, the keys are entered into the lookup table
   * to discover if the circle already exists in memory. If it does, the version in memory is returned.
   * Otherwise, the given circle is entered into memory and returned.
   * 
   * @param desired The object the caller wants returned as a normalized variant
   * @param circle Set of all objects in the circle
   * @return the normalized variant of o
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> T normalizeCircle(Object desired, Set<Object> circle) {
    if(USE_DFA_MINIMIZATION)
      return normalizeCircle2(desired, circle);
    Map<Object, CircleObject> circleobjects = new IdentityHashMap<>();
    for(Object circleobj : circle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
    ReplacementMap replacements = new ReplacementMap();
    do {  
      KeyNorm2D retKey = null;
      Map<KeyNorm2D, Object> tempKeyMap = new HashMap<>();
      int nreplacements = 0;
      for(Object circleobj : circle) {
        KeyNorm2D key = keyFromCircleObject(circleobj, circleobjects);
        if(circleobj == desired) { retKey = key; }
        if(tempKeyMap.containsKey(key)) {
          if(circleobj != desired) {
            replacements.add(circleobj, tempKeyMap.get(key)); nreplacements++;
            } else {
            replacements.add(tempKeyMap.get(key), circleobj); nreplacements++;
            tempKeyMap.put(key, circleobj);
            }
          } else {
          tempKeyMap.put(key, circleobj);
          }
        }
      if(nreplacements == 0) {
        if(!circularIndex.containsKey(retKey)) {
          for(Map.Entry<KeyNorm2D, Object> entry : tempKeyMap.entrySet()) {
            if(entry.getValue() instanceof L42Cachable) ((L42Cachable) entry.getValue()).setNorm(entry.getValue());
            L42Cache cache = L42CacheMap.getCacheObject(entry.getValue());
            circularIndex.put(entry.getKey(), entry.getValue());
            cache.addObjectOverride(simpleKeyFromChonker(entry.getValue(), entry.getKey()), entry.getValue());
            }
          for(Map.Entry<Object, Object> entry : replacements.entrySet()) {
            if(entry.getKey() instanceof L42Cachable) ((L42Cachable) entry.getKey()).setNorm(entry.getValue());
            }
          } else {
            for(Map.Entry<KeyNorm2D, Object> entry : tempKeyMap.entrySet()) {
              Object repl = circularIndex.get(entry.getKey());
              if(entry.getValue() instanceof L42Cachable) ((L42Cachable) entry.getValue()).setNorm(repl);
              replacements.add(entry.getValue(), repl);
              }
            for(Map.Entry<Object, Object> entry : replacements.entrySet()) {
              if(entry.getKey() instanceof L42Cachable) ((L42Cachable) entry.getKey()).setNorm(entry.getValue());
              }
          }
        return (T) circularIndex.get(retKey);
        } else {
        for(Object key : replacements.underlyingMap().keySet()) {
          circleobjects.remove(key);
          circle.remove(key);
          }
        for(Map.Entry<Object, CircleObject> entry : circleobjects.entrySet()) {
          circleobjects.put(entry.getKey(), entry.getValue().replace(replacements.underlyingMap()));
          }
        }
      } while(true);
    }
  
  public static KeyNorm2D getKeyCircleNN(Object desired, Set<Object> circle) {    
    Map<Object, CircleObject> circleobjects = new IdentityHashMap<>();
    for(Object circleobj : circle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
    if(NO_MINIMIZE_ON_READ) {
      return keyFromCircleObject(desired, circleobjects);
      }
    do {  
      KeyNorm2D retKey = null;
      Map<KeyNorm2D, Object> tempKeyMap = new HashMap<>();
      ReplacementMap replacements = new ReplacementMap();
      for(Object circleobj : circle) {
        KeyNorm2D key = keyFromCircleObject(circleobj, circleobjects);
        if(circleobj == desired) { retKey = key; }
        if(tempKeyMap.containsKey(key)) {
          if(circleobj != desired) {
            replacements.add(circleobj, tempKeyMap.get(key));
            } else {
            replacements.add(tempKeyMap.get(key), circleobj);
            tempKeyMap.put(key, circleobj);
            }
          } else {
          tempKeyMap.put(key, circleobj);
          }
        }
      if(replacements.size() == 0) {
        return retKey;
        } else {
        for(Object key : replacements.underlyingMap().keySet()) {
          circleobjects.remove(key);
          circle.remove(key);
          }
        for(Map.Entry<Object, CircleObject> entry : circleobjects.entrySet()) {
          circleobjects.put(entry.getKey(), entry.getValue().replaceNN(replacements.underlyingMap()));
          }
        }
    } while(true);
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
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  private static KeyNorm2D simpleKeyFromChonker(Object obj, KeyNorm2D key) {
    L42Cache cache = L42CacheMap.getCacheObject(obj);
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
    
    @SuppressWarnings("rawtypes")
    private final L42Cache cache;
    
    @SuppressWarnings({ "unchecked" })
    public CircleObject(Object obj) {
      cache = L42CacheMap.getCacheObject(obj);
      Object[] rawfields = cache.f(obj);
      Field[] fields = new Field[rawfields.length];
      for(int i = 0; i < rawfields.length; i++) {
        fields[i] = new Field(rawfields[i], L42CacheMap.isNorm(rawfields[i]));
        }
      this.obj = obj;
      this.params = fields;
      }
    
    public Object getUnderlyingObject() { return this.obj; }
    
    @SuppressWarnings("unchecked")
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
  
  protected static class ReplacementMap
  {
    Map<Object, Object> replacements = new IdentityHashMap<>();
    Map<Object, Set<Object>> invReplacements = new IdentityHashMap<>();
    
    private void addToInv(Object to, Object from) {
      if(!invReplacements.containsKey(to)) {
        invReplacements.put(to, L42CacheMap.identityHashSet()); 
        }   
      invReplacements.get(to).add(from);
      }
    
    public void add(Object from, Object to) {
      replacements.put(from, to);                     //Add relation f->t
      if(invReplacements.containsKey(from)) {         //Check if any relation exists such that x->f
        Set<Object> list = invReplacements.get(from);
        for(Object o : list) {                        //For each x->f, replace f with t
          replacements.put(o, to);
          addToInv(to, o);
          }
        invReplacements.remove(from);
        }
      addToInv(to, from);                             //Add inverse relation
      }
    
    public int size() {
      return replacements.size();
      }
    
    public Set<Map.Entry<Object, Object>> entrySet() {
      return replacements.entrySet();
      }
    
    public Map<Object, Object> underlyingMap() {
      return replacements;
      }
  }

  }
