package is.L42.platformSpecific.javaTranslation;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.nativecache.ValueCache;
import is.L42.generated.P;
import is.L42.tools.General;

public class L42ClassAny implements L42Any, L42Cachable<L42ClassAny> {
  final public P unwrap;  
  public L42ClassAny(P p) {
    assert p!=null;
    this.unwrap=p;
    }
  
  public static final ValueCache<L42ClassAny> myCache = new ValueCache<L42ClassAny>() {

    @Override 
    public String typename() { 
      return "L42ClassAny";
      }

    @Override 
    protected boolean valueCompare(L42ClassAny t1, L42ClassAny t2) { 
      return t1 == t2; 
      }
    
    };
  
  @Override 
  public void setField(int i, Object o) {}
  @Override 
  public Object getField(int i) { return null; }
  @Override 
  public int numFields() { return 0; }
  @Override 
  public L42Cache<L42ClassAny,?> myCache() { return myCache; }
  @Override 
  public void setNorm(L42ClassAny t) {}
  @Override public L42ClassAny myNorm() { return null; }
  @Override 
  public L42ClassAny newInstance() {
    throw General.unreachable();
    }
  
  }