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
import java.util.function.Supplier;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
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
  public static Program currentP;
  public static void setLibsCached(Program p,ArrayList<L42Library> libs){
    currentP=p;
    libsCached=libs;
    }
  public static L42Library ofLib(int id){
    assert libsCached.size()>id;
    L42Library l=libsCached.get(id);
    l.currentProgram(currentP);
    return l;
    }
  public static L42ClassAny ofPath(int id){
    assert libsCached.size()>id;
    if(id>=0){
      L42Library l=libsCached.get(id);
      l.currentProgram(currentP);
      return new L42ClassAny(l.localPath);
      }
    if(id==-1){return new L42ClassAny(P.pAny);}
    if(id==-2){return new L42ClassAny(P.pVoid);}
    assert id==-3;
    return new L42ClassAny(P.pLibrary);
    }
  private static ArrayList<L42Library>libsCached;
  public static final HashMap<String,Slave>slaves=new HashMap<>();
  public static void clearRes() {
    libsCached=null;
    slaves.clear();
    out=new StringBuffer();
    }
  }