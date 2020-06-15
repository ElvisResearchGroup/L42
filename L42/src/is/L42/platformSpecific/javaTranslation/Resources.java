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
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.translationToJava.Loader;
import safeNativeCode.slave.Slave;

public class Resources {
  public static Loader loader=new Loader();
  private static HashMap<String,String> logs=new HashMap<>();
  public static HashMap<String,String> logs(){return logs;}
  private static StringBuffer compiledNesteds=new StringBuffer();//StringBuffer is the synchronized StringBuilder
  public static void notifyCompiledNC(String s){
    compiledNesteds.append(s);}
  public static String notifiedCompiledNC(){return compiledNesteds.toString();}
  private static StringBuffer out=new StringBuffer();
  public static String out(){return out.toString();}
  public static void out(String s){
    s+="\n";
    out.append(s);
    System.out.print(s);
    }
  private static StringBuffer tests=new StringBuffer();
  public static String tests(){return tests.toString();}
  public static void tests(String s){
    tests.append(s);
    System.out.print(s);
    }
  public static <K> K throwE(L42Throwable e){throw e;}
  public static<K> L42£Void toVoid(K k){return L42£Void.instance;}
  public static HashSet<Integer> usedUniqueNs=new HashSet<>();
  public static int allBusyUpTo=0;
  public static Program currentP;
  public static C currentC;
  public static void setLibsCached(Program p,C c,ArrayList<L42£Library> libs){
    currentP=p;
    currentC=c;
    libsCached=libs;
    }
  public static L42£Library ofLib(int id){
    assert libsCached.size()>id: id+" "+libsCached.size();
    L42£Library l=libsCached.get(id);
    l.currentProgram(currentP);
    return l;
    }
  public static L42ClassAny ofPath(int id){
    assert libsCached.size()>id;
    assert id>=0;
    L42£Library l=libsCached.get(id);
    l.currentProgram(currentP);
    return new L42ClassAny(l.localPath());
    }
  private static ArrayList<L42£Library>libsCached;
  public static final HashMap<String,Slave>slaves=new HashMap<>();
  public static void clearRes() {
    libsCached=null;
    slaves.clear();
    usedUniqueNs.clear();
    allBusyUpTo=0;
    out=new StringBuffer();
    tests=new StringBuffer();
    compiledNesteds=new StringBuffer();
    logs.clear();
    }
  }