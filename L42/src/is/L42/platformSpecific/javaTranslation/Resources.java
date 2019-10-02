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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import is.L42.generated.Core;
import safeNativeCode.slave.Slave;

public class Resources {
  private static StringBuffer out=new StringBuffer();//StringBuffer is the synchronized StringBuilder
  public static String out(){return out.toString();}
  public static void out(String s){
    out.append(s);
    System.out.println(s);
    }
  public static <K> K throwE(L42Error e){throw e;}
  public static<K> L42Void toVoid(K k){return L42Void.instance;}
  public static L42Library ofLib(String id){return new L42Library();}
  public static L42Any ofPath(String id){return new L42Any(){};}
  private static final HashMap<Integer,Core.L>libs=new HashMap<>();
  private static final HashMap<Integer,L42Library>libsCached=new HashMap<>();
  public static final HashMap<String,Slave>slaves=new HashMap<>();
  public static void clearRes() {
    libs.clear();
    libsCached.clear();
    out=new StringBuffer();
    }
  }