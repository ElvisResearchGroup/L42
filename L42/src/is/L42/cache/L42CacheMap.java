package is.L42.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.common.cache.CacheBuilder;

import is.L42.cache.nativecache.BoolCache;
import is.L42.cache.nativecache.ByteCache;
import is.L42.cache.nativecache.CharCache;
import is.L42.cache.nativecache.DoubleCache;
import is.L42.cache.nativecache.FlagsCache;
import is.L42.cache.nativecache.FloatCache;
import is.L42.cache.nativecache.IntCache;
import is.L42.cache.nativecache.LongCache;
import is.L42.cache.nativecache.ShortCache;
import is.L42.cache.nativecache.StringCache;
import is.L42.nativeCode.Flags;

public class L42CacheMap {
  
  //TODO: Change to string to get rid of reflection maybe?
  private static final Map<Class<?>, L42Cache<?>> commander;
  private static final L42Cache<?> arrayListCache;  
  private static final CacheBuilder<Object,Object> builder = CacheBuilder.newBuilder().softValues(); 
  
  static {    
    commander = new HashMap<>();
    commander.put(int.class, new IntCache());
    commander.put(Integer.class, commander.get(int.class));
    commander.put(boolean.class, new BoolCache());
    commander.put(Boolean.class, commander.get(boolean.class));
    commander.put(float.class, new FloatCache());
    commander.put(Float.class, commander.get(float.class));
    commander.put(double.class, new DoubleCache());
    commander.put(Double.class, commander.get(double.class));
    commander.put(long.class, new LongCache());
    commander.put(Long.class, commander.get(long.class));
    commander.put(short.class, new ShortCache());
    commander.put(Short.class, commander.get(short.class));
    commander.put(byte.class, new ByteCache());
    commander.put(Byte.class, commander.get(byte.class));
    commander.put(char.class, new CharCache());
    commander.put(Character.class, commander.get(char.class));
    commander.put(String.class, new StringCache());
    arrayListCache=new ArrayListCache();
    commander.put(Flags.class, Flags.cache);
    commander.put(ArrayList.class,arrayListCache);
    }
  
  /**
   * Adds a cacheable type using the given class and cache object.
   * Once added, the specified cache will be returned by further
   * calls to <code>getCacheObject(...)</code> and indirectly
   * by calls to <code>normalize(...)</code> and
   * <code>isNorm()</code>
   * 
   * @param <T> The type of the cache
   * @param class_ The class object representing the type
   * @param cache The relevant cache object
   */
  static <T,C extends L42Cache<T>> C addCacheableType(Class<? extends T> class_, C cache) {
    commander.put(class_, cache);
    return cache;
    }
  
  /**
   * Given a class object, retrieves the 
   * @param <T>
   * @param class_
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> L42Cache<T> getCacheObject(Class<T> class_) {//NOTE: public only for testing
    if(class_==ArrayList.class){return (L42Cache<T>) arrayListCache;}
    assert cacheUnderControl();
    return (L42Cache<T>) commander.get(class_);
    }
  
  private static boolean cacheUnderControl(){
    String s=List.of(Thread.currentThread().getStackTrace()).toString();
    assert s.contains(".lateInitialize(") 
      //|| s.contains("ArrayListCacheForType")
      || s.contains(".NormalizationTests."):
      s;
    return true;    
    }
    
  static L42Cache<?>[] getCacheArray(Class<?> ... classes) {
    L42Cache<?>[] caches = new L42Cache<?>[classes.length];
    for(int i = 0; i < classes.length; i++) {
      caches[i] = getCacheObject(classes[i]);
      }
    return caches;
    }
  
  @SuppressWarnings("unchecked") //NOTE: public only for testing
  public static <T> L42Cache<T> getCacheObject(T o) {
    if(o instanceof L42Cachable){return ((L42Cachable<T>) o).myCache();}
    return getCacheObject((Class<T>) o.getClass()).refine(o);
    }
  public static <T> T normalize_internal(T t) {//NOTE: public only for testing
    L42Cache<T> cache = getCacheObject(t);
    return cache.normalize(t);
    }
  
  @SuppressWarnings("unchecked") 
  static <T> boolean isNorm(T t) {
    if(t == null) { return true; }
    if(t instanceof L42Cachable) { return ((L42Cachable<T>) t).isNorm(); }
    L42Cache<T> cache = getCacheObject(t);
    return cache.isNorm(t);
    }
  
  static <T> boolean identityEquals(T t1, T t2) {
    if(t1 instanceof L42Cachable) { return t1 == t2; }
    L42Cache<T> cache = getCacheObject(t1);
    return cache.identityEquals(t1, t2);
    }
  
  /**
   * Gets the key for an object, optionally normalizing it first
   * 
   * @param <T> type of object
   * @param t The object
   * @param norm Whether to normalize the object before getting
   * the key
   * 
   * @return The object's key
   */
  public static <T> KeyNorm2D getKey(T t, boolean norm) {//NOTE: public only for testing
    L42Cache<T> cache = getCacheObject(t);
    t = norm ? cache.normalize(t) : t;
    return cache.computeKeyNN(t);
  }
  
  public static synchronized String readObjToString(Object o) {
    if(isNorm(o)) { return objToString_internal(o); }
    return objToString_internal(normalize_internal(getCacheObject(o).dup(o)));
    }
  public static synchronized String objToString(Object obj) {
    return objToString_internal(obj);
    }
  static String objToString_internal(Object obj) {
    return KeyFormatter.start(new KeyExpander(obj,true,false).expandedKey());
    }
  //public just for testing
  static public class KeyExpander {
    Object obj;
    boolean entireROG;
    boolean norm;
    public KeyExpander(Object obj, boolean entireROG, boolean norm){this.obj=obj;this.entireROG=entireROG;this.norm=norm;}
    final Map<Object, Integer> done = new IdentityHashMap<>();
    final ArrayList<Object[]> nkeylist = new ArrayList<>();
    <T> KeyVarID apply(int offset, L42Cache<T> cache, T toAdd, int toAddIndex, Object[][] subkey){
      KeyVarID res = new KeyVarID(offset + toAddIndex);
      done.put(toAdd, res.value());
      var key=subkey[toAddIndex];
      for(int i = 1; i < key.length; i++){
        if(!(key[i] instanceof KeyVarID)){continue;}
        var oldId = (KeyVarID)key[i];
        Object field = cache.f(toAdd, i - 1);
        if(done.containsKey(field)){key[i]=new KeyVarID(done.get(field));continue;}
        var fCache=cache.fieldCache(field, i - 1); 
        key[i]=apply(offset, fCache, field, oldId.value(), subkey);
        }
      return res;
      }
    <T>Object addNewObject(L42Cache<T> theCache, T theObj){
      if(theCache==null){theCache=getCacheObject(theObj);}
      if(!entireROG && theCache.isNorm(theObj) ) { return theObj; }
      theObj = norm ? theCache.normalize(theObj) : theObj;
      KeyNorm2D subkey = theCache.refine(theObj).computeKeyNN(theObj);
      Object[][] subkeylines = subkey.lines();
      int offset = nkeylist.size();
      KeyVarID nid = apply(offset, theCache, theObj, 0, subkeylines);
      for(Object[] o : subkeylines){nkeylist.add(o);}
      return nid;
      }
    public KeyNorm2D expandedKey(){
      addNewObject(null, obj);
      for(int i = 0; i < nkeylist.size(); i++){addI(i);}
      //Note: the above add lines while working, thus size changes
      Object[][] narr = new Object[nkeylist.size()][];
      for(int i = 0; i < narr.length; i++) { narr[i] = nkeylist.get(i);}
      return new KeyNorm2D(narr);
      }
    void addI(int i){
      Object[] line=nkeylist.get(i);
      L42Cache<?> c = (L42Cache<?>) line[0];
      if(c.isValueType()){return;}
      for(int j = 1; j < line.length; j++) {forBody(c,j,line);}
      }
    <T>void forBody(L42Cache<?> lineCache,int j,Object[] line){
      if(line[j] instanceof KeyVarID){return;}
      if(done.containsKey(line[j])){
        line[j] = new KeyVarID(done.get(line[j]));
        return;
        }
      if(line[j]==null){return;}
      @SuppressWarnings("unchecked") var cache=(L42Cache<T>)lineCache.rawFieldCache(j - 1);
      @SuppressWarnings("unchecked") var value=(T)line[j];
      line[j] = addNewObject(cache,value);
      }
    }
 //---------------
  /*
  public static KeyNorm2D expandedKey(final Object obj, final boolean entireROG, final boolean norm) {
    final Map<Object, Integer> done = new IdentityHashMap<>();
    final ArrayList<Object[]> nkeylist = new ArrayList<>();
    class A { <T> KeyVarID apply(int offset, L42Cache<T> cache, T toAdd, int toAddIndex, Object[][] subkey) {
      KeyVarID ourId = new KeyVarID(offset + toAddIndex);
        done.put(toAdd, ourId.value());
        for(int i = 1; i < subkey[toAddIndex].length; i++) {
          if(subkey[toAddIndex][i] instanceof KeyVarID) {
            KeyVarID oldId = (KeyVarID) subkey[toAddIndex][i];
            Object field = cache.f(toAdd, i - 1);
            if(!done.containsKey(field)) {
              KeyVarID newId = this.apply(offset, cache.fieldCache(field, i - 1), field, oldId.value(), subkey);
              subkey[toAddIndex][i] = newId;
              } else {
              KeyVarID newId = new KeyVarID(done.get(field)); 
              subkey[toAddIndex][i] = newId;
              }
            }
          }
        return ourId;
        }}
    A subkeyProcessor = new A();
    BiFunction<L42Cache<Object>, Object, Object> addNewObject = (theCache, theObj) -> {
      if(theCache == null) { theCache = getCacheObject(theObj); }
      if(!entireROG && theCache.isNorm(theObj) ) { return theObj; }
      theObj = norm ? theCache.normalize(theObj) : theObj;
      KeyNorm2D subkey = theCache.computeKeyNN(theObj);
      Object[][] subkeylines = subkey.lines();
      int offset = nkeylist.size();
      KeyVarID nid = subkeyProcessor.apply(offset, theCache, theObj, 0, subkeylines);
      for(Object[] o : subkeylines) { nkeylist.add(o); }
      return nid;
      };
    addNewObject.apply(null, obj);
    for(int i = 0; i < nkeylist.size(); i++) {
      Object[] line = nkeylist.get(i);
      L42Cache<Object> lineCache = (L42Cache<Object>) line[0];
      if(lineCache.isValueType()) { continue; }
      for(int j = 1; j < line.length; j++) {
        if(!(line[j] instanceof KeyVarID)) {
          if(done.containsKey(line[j])) {
            line[j] = new KeyVarID(done.get(line[j]));
            } else if(line[j] != null) {
            line[j] = addNewObject.apply((L42Cache<Object>) lineCache.rawFieldCache(j - 1), line[j]);
            }
          }
        }
      }
    
    //toArray doesn't work for whatever reason
    Object[][] narr = new Object[nkeylist.size()][];
    for(int i = 0; i < narr.length; i++) { narr[i] = nkeylist.get(i); }
    return new KeyNorm2D(narr);
  }*/  
  //---------------
  public static <T>Set<T> identityHashSet() {
    return Collections.newSetFromMap(new IdentityHashMap<T, Boolean>());
    }
  
  /**
   * Produces a map that has the desired behavior of entries
   * being garbage collectable with the relevant object is 
   * no longer in use
   * 
   * @return A map from object to object that can be used
   * as a norm map, such that the references to the keys
   * are strong references and the references to the values
   * are weak references.
   */
  public static <T>Map<KeyNorm2D, T> newNormMap() {
    return builder.<KeyNorm2D,T>build().asMap();
    }
  
  public static void clearAllCaches() {
    for(L42Cache<?> cache : commander.values()) {
      cache.clear();
      }
    }
  
  public static synchronized <T> L42SingletonCache<T> newSingletonCache(Object name, Class<? extends T> class_) {
    return new L42SingletonCache<T>(name, class_);
    }
  
  public static synchronized <T extends L42Cachable<T>> L42StandardCache<T> newStandardCache(Object name, Class<? extends T> class_) {
    return new L42StandardCache<T>(name, class_);
    }
  
  public static synchronized <T extends L42Cachable<T>> void lateInitialize(L42StandardCache<T> cache, Class<?> ... classes) {
    cache.lateInitialize(classes);
    }
  
  public static synchronized <T, C extends L42Cache<T>> void addCachableType_synchronized(Class<? extends T> class_, C cache) {
    addCacheableType(class_, cache);
    }
  
  public static synchronized <T> T normalizeAndDup(T t) {
    t = normalize_internal(t);
    return dup(t);
    }
  
  public static synchronized <T extends L42Cachable<T>> T normalizeCachable(T t) {
    return ((L42Cachable<T>) t).myCache().normalize(t);
  }
  
  public static synchronized <T> T normalize(T t) {
    return normalize_internal(t);
    }
  
  public synchronized static <T> T dup(T t) {
    L42Cache<T> cache = getCacheObject(t);
    return cache.dup(t);
    }
  
  public static synchronized <T> boolean structurallyEqualNoNorm(T obj1, T obj2) {
    if(obj1 == null) { return obj2 == null; }
    var k1=new KeyExpander(obj1,true,false).expandedKey();
    var k2=new KeyExpander(obj2,true,false).expandedKey();
    return k1.equals(k2);
    }
  
  public static synchronized <T> boolean structurallyEqualNorm(T obj1, T obj2) {
    obj1 = normalize_internal(obj1);
    obj2 = normalize_internal(obj2);
    return L42CacheMap.identityEquals(obj1, obj2);
    }

}
