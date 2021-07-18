package is.L42.platformSpecific.javaTranslation;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.nativeCode.TrustedKind;

public class L42£NonDeterministicError extends L42NoFields.Eq<L42£NonDeterministicError>{
  public String getMsg(){return msg;}
  public void setMsg(String msg){
    assert msg!=null;
    this.msg=msg;
    }
  private String msg;
  public L42£NonDeterministicError(){this.msg="";}
  private static String throwableToMsg(Throwable t){
    var res=t.getClass().getSimpleName();
    if(t.getMessage()!=null){res+="\n"+t.getMessage();}
    return res;
    }
  public L42£NonDeterministicError(Throwable t){this(throwableToMsg(t));}
  public L42£NonDeterministicError(String msg){
    assert msg!=null:
      "";
    this.msg=msg;
    }
  @Override public String toString(){return getMsg();}
  @Override public boolean eq(L42£NonDeterministicError other){
    return msg.equals(other.msg);
    }
  @Override public int hashCode(){return msg.hashCode();}      
  public static final Class<L42£NonDeterministicError> _class = L42£NonDeterministicError.class;
  public static final L42Cache<L42£NonDeterministicError> myCache=new EqCache<>(TrustedKind.NonDeterministicError);
  @Override public L42Cache<L42£NonDeterministicError> myCache(){return myCache;}
  }