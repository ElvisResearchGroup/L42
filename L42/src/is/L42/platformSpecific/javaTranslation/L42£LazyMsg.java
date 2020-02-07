package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

public class L42£LazyMsg extends L42Singleton<L42£LazyMsg>{
  private static Supplier<String> base=()->"";
  public String getMsg(){
    if(msg!=null){return msg;}
    assert lazy!=null;
    msg=lazy.get();
    return msg;
    }
  public void setMsg(String msg){this.msg=msg;this.lazy=null;}
  private String msg;
  private Supplier<String> lazy;
  public L42£LazyMsg(){this.lazy=base;}
  public L42£LazyMsg(Supplier<String>lazy){this.msg=null;this.lazy=lazy;}
  public L42£LazyMsg(String msg){this.msg=msg;this.lazy=null;}
  public boolean eq(L42£LazyMsg other){
    if(lazy==null && other.lazy==null){
      assert msg!=null;
      assert other.msg!=null;
      return msg.equals(other.msg);
      }      
    if(lazy==other.lazy){
      assert msg==null || other.msg==null || msg.equals(other.msg);
      return true;
      }
    return false;
    }
  public static final Class<L42£LazyMsg> _class = L42£LazyMsg.class;
  public static final LazyMsgCache myCache = new LazyMsgCache();
  static{L42CacheMap.addCacheableType(L42£LazyMsg.class, myCache);}
  @Override public L42Cache<L42£LazyMsg> myCache(){return myCache;}
  }
class LazyMsgCache extends ValueCache<L42£LazyMsg>{
  @Override public String typename() {return "L42£LazyMsg";}
  @Override protected boolean valueCompare(L42£LazyMsg t1, L42£LazyMsg t2) {
    return t1.eq(t2);
    }
  }