package is.L42.platformSpecific.javaTranslation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.io.Files;

import is.L42.common.Program;
import is.L42.common.ReadURL;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.translationToJava.Loader;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;
import safeNativeCode.utils.Utils;

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
  private static StringBuffer err=new StringBuffer();
  public static String err(){return err.toString();}
  private static Consumer<String> outHandler=s->{};
  public static void setOutHandler(Consumer<String> c){outHandler=c;}
  private static Consumer<String> errHandler=s->{};
  public static void setErrHandler(Consumer<String> c){errHandler=c;}
  public static void out(String s){
    s+="\n";
    out.append(s);
    outHandler.accept(s);
    System.out.print(s);
    }
  public static void err(String s){
    s+="\n";
    err.append(s);
    errHandler.accept(s);
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
  private static final LinkedHashMap<String,Slave>slaves=new LinkedHashMap<>();
  public static void killAllSlaves(){
    for(Slave s:slaves.values()){s.terminate();}
    try{for(Slave s:slaves.values()){s.waitForExit();}}
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
      throw new Error(e);
      }
    catch(IOException e){throw new Error(e);}
    finally{slaves.clear();}
    }
  public static void clearRes() {
    clearResKeepReuse();
    ReadURL.resetCache();
    }
  public static void clearResKeepReuse() {
    libsCached=null;
    killAllSlaves();
    usedUniqueNs.clear();
    allBusyUpTo=0;
    out=new StringBuffer();
    err=new StringBuffer();
    tests=new StringBuffer();
    compiledNesteds=new StringBuffer();
    logs.clear();
    }
  public static Program top(){
    Program p=currentP;
    while(!p.pTails.isEmpty()){p=p.pop();}
    return p;
    }
  public static Path initialPath(){
    var t=top();
    return Paths.get(t.top.pos().fileName()).getParent();
    }
  /*static{//Not needed here, but in the slave code!
    var sup=Utils.getIsJavaClass();
    Utils.setIsJavaClass((loc,name)->
      name.equals("is.L42.platformSpecific.javaEvents.Event") || sup.test(loc, name)
      );
    }*/
  public static Slave loadSlave(int memoryLimit,int timeLimit,String slaveName,Object o){
    return Resources.slaves.computeIfAbsent(slaveName, sn->{
      String[] args = new String[]{"--enable-preview"};
      if(memoryLimit>0){args=new String[]{"-Xmx"+memoryLimit+"M","--enable-preview"};}
      var workingDir=initialPath().toFile();//the nio/Path API is not great for the needed task
      Stream<String> sysPaths=Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator));
      Stream<String> workingPaths=
        Arrays.stream(workingDir.listFiles((dir,name)->name.endsWith(".jar")))
        .map(f->f.getAbsolutePath().toString());
      String[] localPaths=Stream.concat(sysPaths, workingPaths)
        .map(path -> Paths.get(path).toAbsolutePath().toString())
        .toArray(String[]::new);
      //System.out.println("######################");
      //System.out.println(Arrays.asList(workingDir.list()));
      //System.out.println(Arrays.asList(localPaths));
      Slave s=new ProcessSlave(timeLimit, args, ClassLoader.getPlatformClassLoader()){
        @Override protected ProcessBuilder makeProcessBuilder() throws IOException {
          var pb=super.makeProcessBuilder();
          return pb.directory(workingDir);
          }
       @Override protected String[] getClassPath(){return localPaths;}
        };
      s.addClassLoader(o.getClass().getEnclosingClass().getClassLoader());
      return s;
      });
    }
  public static void breakHere(){//poor man attempt to add breakpoints to generated java
    System.out.println("java debugger Breakpoint");
    String s=Arrays.asList(Thread.currentThread().getStackTrace()).toString();
    if(s.contains("TestCircularObjects£n0£_£cBase£n71")) {
      System.out.println();
      }
    }
  }