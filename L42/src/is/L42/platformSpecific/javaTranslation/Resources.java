package is.L42.platformSpecific.javaTranslation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import is.L42.generated.Core;

public class Resources {
  static <K> K throwE(Error e){throw e;}
  static<K> L42Void toVoid(K k){return L42Void.instance;}
  static interface L42Any{}
  static class L42Void implements L42Any{
    private L42Void(){}
    public static final L42Void instance=new L42Void();
    }
  static class L42Library implements L42Any{}
  public interface Fwd{
    static L42Void £cAddIfFwd(Object x1, Object res, BiConsumer<Object,Object> action){
      ((Fwd)x1).rememberAssign(res,action);
      return L42Void.instance;
      }
    static L42Void £cFix(Object a,Object b) { //hoping 'a' is a Fwd
      Fwd _a=(Fwd)a;
      _a.fix(b);
      return L42Void.instance;
      }
    List<Object> os();
    List<BiConsumer<Object,Object>> fs();
    default void rememberAssign(Object f, BiConsumer<Object,Object> fo){
      os().add(f);
      fs().add(fo);
      }
    default void fix(Object b){
      List<Object> os=os();
      List<BiConsumer<Object,Object>> fs=fs();
      assert os.size()==fs.size();
      for(int i=0;i<os.size();i++){fs.get(i).accept(b,os.get(i));}
      }
    }
  private static final HashMap<Integer,Core.L>libs=new HashMap<>();
  private static final HashMap<Integer,L42Library>libsCached=new HashMap<>();
  public static void clearRes() {
    libs.clear();
    libsCached.clear();
    }

  @SuppressWarnings("serial")
  public static class L42Throwable extends RuntimeException{
    public final Object unbox; public L42Throwable(Object u){unbox=u;}
    public final Object inner(){return unbox;}
    }
  @SuppressWarnings("serial")
  public static class Error extends L42Throwable{
    public String toString() {return "Error["+ unbox +"]";}
    public Error(Object u){super(u);}
    }
  @SuppressWarnings("serial")
  public static class Exception extends L42Throwable{
    public String toString() {return "Exception["+ unbox +"]";}
    public Exception(Object u){super(u);}
    }
  @SuppressWarnings("serial")
  public static class Return extends L42Throwable{
    public String toString() {return "Return["+ unbox +"]";}
    public Return(Object u){super(u);}
    }
  }