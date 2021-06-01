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
import is.L42.cache.nativecache.L42ValueCache;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.nativeCode.TrustedKind;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

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
  public static final EqCache<L42£NonDeterministicError> myCache=new EqCache<>(TrustedKind.NonDeterministicError);
  @Override public EqCache<L42£NonDeterministicError> myCache(){return myCache;}
  }