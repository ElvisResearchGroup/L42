package is.L42.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

class CircleEntry<T,F>{
  Set<Object>equivClasses;
  List<Object>circleFields;
  List<Object>nonCircleFields;
  F fields;
  L42Cache<T,F> cache;
  T replacement;
  void applyReplacements(Object oo,Map<Object,CircleEntry<?,?>> map){
    @SuppressWarnings("unchecked")
    T o=(T)oo;
    int size=cache.fn(o);
    for(int i = 0; i < size; i++) {
      var r=map.get(cache.f(o, i, fields));
      if(r!=null){cache.setF(o,i,r.replacement,fields);}
      }
    assert replacement!=null;
    //cache.setMyNorm(o, replacement);//TODO:
    assert replacement==map.get(replacement).replacement;
    fields=cache._fields(o);
    }
  @SuppressWarnings("unchecked")
  void setReplacement(Object o){
    this.replacement=(T)o;
    }
  @SuppressWarnings("unchecked")
  void setNorm(Object o,Object norm){
    cache.setMyNorm((T)o,(T)norm);
    }
  @SuppressWarnings("unchecked")
  void addObjectOverride(KeyNorm2D key,Object obj){
    cache.addObjectOverride(key,(T)obj);
    }
  CircleEntry(Set<Object> circle,T o,L42Cache<T,F> myCache){
    this.cache=myCache;
    equivClasses=L42CacheMap.identityHashSet();
    for(Object o2 : circle) {
      var c2=L42CacheMap.getCacheObject(o2);
      if(myCache.equals(c2)){equivClasses.add(o2);}
      }
    circleFields=new ArrayList<>();
    nonCircleFields=new ArrayList<>();
    fields = myCache._fields(o);
    int size=myCache.fn(o);
    for(int i=0;i<size;i++){
      Object f=myCache.f(o, i,fields);
      if(circle.contains(f)){circleFields.add(f);}
      else{nonCircleFields.add(f);}
      }
    }
  static <T,F> CircleEntry<T,F> of(Set<Object> circle,T o,L42Cache<T,F> myCache){return new CircleEntry<T, F>(circle, o, myCache);}
  }
public class LoopCache<T> {
  //TODO: how can we have a global circularIndex map???
  private static final Map<KeyNorm2D, Object> circularIndex = L42CacheMap.newNormMap();
  
  public LoopCache(T desired, Set<Object> circle){
    initMap(circle);
    remove(circle);
    List<Object> forCircleObjects=fillReplacements(desired);
    for(Object o : circle){map.get(o).applyReplacements(o,map);}
    Map<Object, CircleObject> circleObjects=new IdentityHashMap<>();
    //circle objects created after applyReplacements
    for(var o:forCircleObjects) {circleObjects.put(o,CircleObject.of(o));}
    KeyNorm2D key = keyFromCircleObject(desired, circleObjects);
    if(circularIndex.containsKey(key)){
      result=inCircularIndex(key, circle, circleObjects);
      return;
      }
    for(Object o : circleObjects.keySet()) {
      KeyNorm2D chonker = keyFromCircleObject(o, circleObjects);//TODO: assert chonker.equals(key)?
      KeyNorm2D simplified = simpleKeyFromChonker(o, chonker,L42CacheMap.getCacheObject(o));
      circularIndex.put(chonker, o);
      var m=map.get(o);
      m.addObjectOverride(simplified, o);
      m.setNorm(o,m.replacement);
      }
    result=desired;
    }
  Map<Object,CircleEntry<?,?>> map=new IdentityHashMap<>();
  final T result;
  void initMap(Set<Object> circle){
    for(Object o : circle){
      map.put(o,CircleEntry.of(circle,o,L42CacheMap.getCacheObject(o)));
      }
    }    
  boolean shouldBeRemovedCirc(int i,List<Object>f1,List<Object>f2){
    return i >= f2.size() || Collections.disjoint(map.get(f1.get(i)).equivClasses, map.get(f2.get(i)).equivClasses);
    }
  boolean shouldBeRemovedNoCirc(int i,List<Object>f1,List<Object>f2){
    return i >= f2.size() || f1.get(i) != f2.get(i);
    }
  boolean shouldBeRemoved(Object o,Object o2,Set<Object>equivClassesO,Set<Object>equivClassesO2){
    List<Object> nf1 = map.get(o).nonCircleFields;
    List<Object> nf2 = map.get(o2).nonCircleFields;
    for(int i = 0; i < nf1.size(); i++){
      if(shouldBeRemovedNoCirc(i,nf1,nf2)){return true;}
      }
    List<Object> cf1 = map.get(o).circleFields;
    List<Object> cf2 = map.get(o2).circleFields;
    for(int i = 0; i < cf1.size(); i++) {
      if(shouldBeRemovedCirc(i,cf1,cf2)){return true;}
      }
    return false;
    }
  void remove(Set<Object> circle){
    while(true){    
      boolean acted=false;
      for(Object o:circle){
        Set<Object> equivClassesO=map.get(o).equivClasses;
        while(remove1Round(o,equivClassesO)){acted=true;}
        }
      if(!acted){return;}
      }
    }
  boolean remove1Round(Object o,Set<Object> equivClassesO){
    boolean acted=false;
    for(Object o2 : equivClassesO.toArray()){
      if(o==o2){continue;}
      var equivClassesO2 = map.get(o2).equivClasses;
      if(!shouldBeRemoved(o,o2,equivClassesO,equivClassesO2)){continue;}
      equivClassesO.remove(o2);
      equivClassesO2.remove(o);
      assert !equivClassesO.isEmpty();
      assert !equivClassesO2.isEmpty();
      acted=true;
      }
    return acted;
    }
  List<Object> fillReplacements(T desired){
    List<Object>res=new ArrayList<>();
    Set<Object> used = L42CacheMap.identityHashSet();
    for(var m:map.values()){
      Set<Object> equiv=m.equivClasses;
      if(used.containsAll(equiv)){continue;}
      used.addAll(equiv);
      Object to  = equiv.contains(desired) ? desired : equiv.iterator().next();
      res.add(to);
      for(Object from : equiv){map.get(from).setReplacement(to);}
      }
    return res;
    }
  T inCircularIndex(KeyNorm2D key,Set<Object>circle,Map<Object, CircleObject> circleObjects){
    for(Object o : circleObjects.keySet()) {
      KeyNorm2D chonker = keyFromCircleObject(o, circleObjects);
      var m=map.get(o);
      var ch=circularIndex.get(chonker);
      m.setReplacement(ch);//why? TODO: test if we remove it
      m.setNorm(o,ch);
      }
    @SuppressWarnings("unchecked")
    var res=(T)circularIndex.get(key);
    return res;
    }  
  public static KeyNorm2D getKeyCircleNN(Object desired, Set<Object> circle) {    
    Map<Object, CircleObject> circleobjects = new IdentityHashMap<>();
    for(Object circleobj : circle){
      circleobjects.put(circleobj, CircleObject.of(circleobj));
      }
    return keyFromCircleObject(desired, circleobjects);
  }
  
  private static KeyNorm2D keyFromCircleObject(Object circleobj, Map<Object, CircleObject> circleobjects){
    Map<Object, Integer> varnames = new IdentityHashMap<>();
    List<Object> order = new ArrayList<>();
    circleobjects.get(circleobj).constructVarMap(varnames, order, circleobjects, new MyInteger(0));
    Object[][] lines = new Object[order.size()][];
    for(int i = 0; i < lines.length; i++) { lines[i] = (circleobjects.get(order.get(i))).toKey(varnames); }
    return new KeyNorm2D(lines);
    }
  
  private static <T,F> KeyNorm2D simpleKeyFromChonker(T obj, KeyNorm2D key,L42Cache<T,F> cache) {
    Object[] ln1 = key.lines()[0];
    Object[] ln1cpy = new Object[ln1.length];
    System.arraycopy(ln1, 0, ln1cpy, 0, ln1.length);
    F fs=cache._fields(obj);
    for(int i = 1; i < ln1.length; i++) {
      if(ln1[i] instanceof KeyVarID) {
        ln1cpy[i] = cache.f(obj, i-1,fs);
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
      if(!varmap.containsKey(value)) { 
        return value; }
      else { return new KeyVarID(varmap.get(this.value)); }
      }    
    }
  
  protected static class CircleObject  {
    private final Object obj;
    private final Field[] params;
    
    private final L42Cache<?,?> cache;
    /*TODO:
    @SuppressWarnings("unchecked")
    public static<T,F> CircleObject of(Object obj,CircleEntry<T,F> m){
      return new CircleObject((T)obj,m.cache,m.fields);
      }*/
    @SuppressWarnings("unchecked")
    public static<T,F> CircleObject of(Object obj){
      var cache=(L42Cache<T,F>)L42CacheMap.getCacheObject(obj);
      var fields=cache._fields((T)obj);
      return new CircleObject((T)obj,cache,fields);
      }    
    public<T,F> CircleObject(T obj,L42Cache<T,F> cache,F fs){
      this.cache=cache;
      int size=cache.fn(obj);
      Field[] fields = new Field[size];
      for(int i = 0; i < size; i++) {
        Object fi=cache.f(obj,i,fs);
        boolean isNorm=L42CacheMap.isNorm(fi,i,cache);
        fields[i] = new Field(fi, isNorm);
        }
      this.obj = obj;
      this.params = fields;
      }
    
    public Object getUnderlyingObject() { return this.obj; }
    
    public CircleObject replace(Map<Object, Object> replacements) {
      return replace(replacements,cache);
      }
    private <T,F>CircleObject replace(Map<Object, Object> replacements,L42Cache<T,F>cache) {
      @SuppressWarnings("unchecked")
      T o=(T)obj;
      F fs=cache._fields(o);
      int size=cache.fn(o);
      for(int i = 0; i < size; i++){
        var fiR=replacements.get(cache.f(o,i,fs));
        if(fiR!=null){cache.setF(o,i,fiR,fs);}
        }
      return CircleObject.of(this.obj);
      }
    
    public void constructVarMap(Map<Object, Integer> varmap, List<Object> order, Map<Object, CircleObject> amap, MyInteger i) {
      varmap.put(this.obj, i.getAndIncrement());
      order.add(this.obj);
      for(Field f : params){
        if(!varmap.containsKey(f.value) && amap.containsKey(f.value)){
          amap.get(f.value).constructVarMap(varmap, order, amap, i);}
        }
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
