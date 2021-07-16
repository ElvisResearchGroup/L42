package is.L42.platformSpecific.javaTranslation;

import java.util.function.Supplier;

import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.TrustedKind;

public class L42£LazyMsg extends L42NoFields.Eq<L42£LazyMsg>{
  private static Supplier<String> base=()->"";
  public String getMsg(){
    if(msg!=null){return msg;}
    assert lazy!=null;
    msg=lazy.get();
    return msg;
    }
  public void setMsg(String msg){
    assert msg!=null;
    this.msg=msg;
    this.lazy=null;
    }
  private String msg;
  private Supplier<String> lazy;
  public L42£LazyMsg(){this.lazy=base;}
  public L42£LazyMsg(Supplier<String>lazy){
    assert lazy!=null;
    this.msg=null;
    this.lazy=lazy;
    }
  public L42£LazyMsg(String msg){
    assert msg!=null:
      "";
    this.msg=msg;
    this.lazy=null;
    }
  @Override public String toString(){return getMsg();}
  @Override public boolean eq(L42£LazyMsg other){
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
  @Override public int hashCode(){
    if(lazy==null){return msg.hashCode();}      
    return lazy.hashCode();
    }
  public static final Class<L42£LazyMsg> _class = L42£LazyMsg.class;
  public static final EqCache<L42£LazyMsg> myCache=new EqCache<>(TrustedKind.LazyMessage);
  @Override public EqCache<L42£LazyMsg> myCache(){return myCache;}
  }