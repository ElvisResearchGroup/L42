package is.L42.cache;

import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.platformSpecific.javaTranslation.L42Fwd;
abstract class AbstractStructuredCache<T,F> implements L42Cache<T>{
  abstract F _fields(T t);//{return t.allFields();}//null for arraylist
  abstract Object f(T t,int i,F _fields);//{return fields[i];}//override for arraylist
  abstract void setF(T t,int i,Object o,F _fields);
  abstract T newInstance(T t);
  private <K> Set<K> addCircle(Set<K> _circle, Collection<K> more){
    if(more==null){return _circle;}
    Set<K> res=L42CacheMap.identityHashSet();
    if(_circle != null){res.addAll(_circle);}
    res.addAll(more);
    return res;
    }  
  private  List<Object> cutTo(List<Object> list, Object t) {
    for(int i = 0; i < list.size(); i++){
      if(list.get(i)!=t){continue;}
      return list.subList(i,list.size());
      }
    throw unreachable();
    }
  private boolean checkFwd(Object o){
    if (o==null) {return false;}
    if (!(o instanceof L42Fwd)){return false;}
    try{
      var f=o.getClass().getField("pathInstance");
      Object oo=f.get(o);
      return oo!=o;
      }//relax, is an assertion method
    catch(Exception e){throw new Error(e);}
    }
  public Set<Object> _computeCircle(T t, List<Object> prevs,boolean norm){
    F _fields = _fields(t);
    int size=fn(t);
    Set<Object> circle = null;   
    for(int i = 0; i < size; i++){
      var vali=f(t,i,_fields);
      assert !checkFwd(vali):
        "";
      if(vali==null){assert this.rawFieldCache(i)!=null;continue;} 
      if(prevs.stream().anyMatch(o->o==vali)){circle=addCircle(circle,cutTo(prevs,vali));continue;}
      L42Cache<Object> cache = this.fieldCache(vali,i);
      NormResult<Object> res=norm?
        cache.normalizeInner(vali, new ArrayList<>(prevs)):
        cache.computeKeyNNInner(vali,new ArrayList<>(prevs));
      if(norm && res.hasResult()){setF(t,i,res.result(),_fields);continue;}
      if(!res.hasResult() && res.circle().contains(t)){circle=addCircle(res.circle(),circle);continue;}
      if(norm){setF(t,i,LoopCache.normalizeCircle(vali, res.circle()),_fields);}
      }
    return circle;
    }
  private final Map<KeyNorm2D, T> normMap;
  AbstractStructuredCache(AbstractStructuredCache<T,F> o){this.normMap=o.normMap;}
  public AbstractStructuredCache(){this.normMap=L42CacheMap.newNormMap();}
  @Override public void addObjectOverride(KeyNorm2D key,T value){normMap.put(key, value);}
  T _get(KeyNorm2D key) {return normMap.get(key);}
  void add(KeyNorm2D key, T t) {
    normMap.put(key, t);
    publish(t);
    }
  @Override public T normalize(T t) {
    assert !checkFwd(t):"";
    NormResult<T> res = normalizeInner(t, new ArrayList<Object>());
    if(res.hasResult()) { return res.result(); }
    return LoopCache.normalizeCircle(t, res.circle());    
    }
  public NormResult<T> normalizeInner(T t, List<Object> prevs) {
    if(this.isNorm(t)){return new NormResult<T>(this.getMyNorm(t));}
    prevs.add(t);
    Set<Object> circle = _computeCircle(t, prevs, true);   
    if(circle!=null){return new NormResult<T>(circle);}
    KeyNorm2D key = this.simpleKey(t);
    T t2=_get(key);
    if(t2!=null) { 
      setMyNorm(t,t2);
      return new NormResult<T>(t2); 
      }
    this.add(key, t);
    return new NormResult<T>(t);
    }
  @Override public KeyNorm2D computeKeyNN(T t) {
    NormResult<T> res = computeKeyNNInner(t, new ArrayList<>());
    if(res.hasResult()){return this.simpleKey(res.result());}
    return LoopCache.getKeyCircleNN(t, res.circle());
    }
  @Override public NormResult<T> computeKeyNNInner(T t, List<Object> prevs) {
    prevs.add(t);
    Set<Object> circle = _computeCircle(t, prevs, false);   
    if(circle!=null){return new NormResult<T>(circle);}
    KeyNorm2D key = this.simpleKey(t);
    T res=_get(key);
    if(res!=null){return new NormResult<T>(res);}
    return new NormResult<T>(t);    
    }
  @Override public boolean structurallyEquals(T t1, T t2) {
    t1 = normalize(t1);
    t2 = normalize(t2);
    return t1 == t2;
    }
  @Override public T dup(T that, Map<Object, Object> map) {
    if(that == null || isNorm(that)){return that;}
    T nObj = newInstance(that);
    map.put(that, nObj);
    for(int i = 0; i < this.fn(that); i++){
      Object field = this.f(that, i);
      L42Cache<Object> fieldcache = this.fieldCache(field, i);
      var mapped=map.get(field);
      if(mapped==null){map.put(field,mapped=fieldcache.dup(field, map));}
      this.f(nObj,mapped, i);
      }
    return nObj;
    }
  @Override public boolean identityEquals(T t1, T t2){return t1 == t2;}
  @Override public void clear(){normMap.clear();}
  private static void publish(Object o){new Publisher(o);}
  private static final class Publisher{
    @SuppressWarnings("unused") final Object o; Publisher(Object o){this.o=o;}
    //See https://stackoverflow.com/questions/6457109/java-concurrency-is-final-field-initialized-in-constructor-thread-safe
    //--Storing a reference to it into a final field of a properly constructed object;
    //TODO: discuss if we can use publishing INSTEAD of volatile fields for caches
    }
  }