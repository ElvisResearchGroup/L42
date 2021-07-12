package is.L42.cache;

public class L42StandardCache<T extends L42Cachable<T>> extends AbstractStructuredCache<T>{
  @Override public Object f(T t,int i){return t.getField(i);}
  @Override public void setF(T t,int i,Object o){t.setField(i,o);}
  @Override protected T newInstance(T t){return t.newInstance();}
  private final Object typename;
    L42StandardCache(Object typename, Class<? extends T> myClass) {
    this.typename = typename;   
    }  
  public L42Cache<?>[] caches;
  public void lateInitialize(Class<?>...classes){
	caches = new L42Cache<?>[classes.length];
	for(int i = 0; i < classes.length; i++) {
	  caches[i] = L42CacheMap.getCacheObjectFromClass(classes[i]);
	  }
  	}
  @Override protected void add(KeyNorm2D key, T t) {
    super.add(key,t);
    t.setNorm(t);
    }          
  @Override public boolean isNorm(T t){return t==null ||t.isNorm();}     
  @Override public int fn(T t){return t.numFields();}
  @Override public Object typename(){return typename;}  
  @Override public L42Cache<?> rawFieldCache(Object t,int i){return caches[i];}
  @Override public T getMyNorm(T me){return me.myNorm();}
  @Override public void setMyNorm(T me, T norm){me.setNorm(norm);}  
  }