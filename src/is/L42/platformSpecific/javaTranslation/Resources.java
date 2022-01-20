package is.L42.platformSpecific.javaTranslation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import is.L42.cache.concurrency.CachedRes;
import is.L42.common.Program;
import is.L42.common.ReadURL;
import is.L42.flyweight.C;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.main.Main;
import is.L42.main.Settings;
import is.L42.translationToJava.Loader;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;

public class Resources {
  public static Encoder encoder = Base64.getMimeEncoder();
  public static Decoder decoder = Base64.getMimeDecoder();
  public static Loader loader;//=new Loader(); created during topCache
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
  private static Consumer<String> testHandler=s->{};
  public static void setTestHandler(Consumer<String> c){testHandler=c;}
  private static Settings settings=null;
  public static void setSettings(Settings s){settings=s;}
  public static Settings settings(){return settings;}
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
    testHandler.accept(s);
    System.out.print(s);
    }
  public static <K> K throwE(L42Throwable e){throw e;}
  public static<K> L42£Void toVoid(K k){return L42£Void.instance;}
  public static HashSet<Integer> usedUniqueNs=new HashSet<>();
  public static int allBusyUpTo=0;
  public static Program currentP;
  public static C currentC;
  public static Pos currentPos;
  public static void setLibsCached(Program p,C c,Pos pos,ArrayList<L42£Library> libs){
    currentP=p;
    currentC=c;
    currentPos=pos;
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
    CachedRes.killIfBusy();
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
  @SuppressWarnings("unchecked")
  public static <T> T sanitizeJavaRes(T t) {
    if(t==null){return t;}
    if(t instanceof String s){return (T)sanitizeString(s);}
    assert t instanceof Integer || t instanceof Float || t instanceof Long;//Will need to add a few other cases
    return t;
    }
  private static String sanitizeString(String s){
    return s.chars()
      .filter(Resources::charOk)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
    }
  static final private Set<Integer> allowed=IntStream.of(
    '(',')','[',']','<','>',
    '&','|','*','+','-','=',
    '/','!','?',';',':',',',
    '.',' ','~','@','#','$',
    '%','`','^','_','\\','{',
    '}','"','\'','\n','\t'    
    ).boxed().collect(Collectors.toUnmodifiableSet());
  private static boolean charOk(int i){
    var low= i>='a' & i<='z';
    var upper= i>='A' & i<='Z';
    var num=i>='0' & i<='9';
    return low || upper || num || allowed.contains(i);
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
      Slave s=new ProcessSlave(timeLimit, args, ClassLoader.getPlatformClassLoader()){
        @Override protected List<String> getJavaArgs(String libLocation){
          var res=super.getJavaArgs(libLocation);
          if(Main.isTesting()){ res.add(0,"-ea"); }
          return res;
          }
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
  public interface InferenceHandler{
    default void ex(Core.EX ex, Program p) {}
    default void mwt(Core.MWT mwt, Program p) {}
    default void nc(Core.E e, Program p) {}
    }
  private static InferenceHandler inferenceHandler=new InferenceHandler(){};
  public static void inferenceHandler(InferenceHandler i){inferenceHandler = i;}
  public static InferenceHandler inferenceHandler(){return inferenceHandler;}

  public static void breakHere(){//poor man attempt to add breakpoints to generated java
    System.out.println("java debugger Breakpoint");
    String s=Arrays.asList(Thread.currentThread().getStackTrace()).toString();
    if(s.contains("My_TestCircularObjects£i0£_£cBase£i71")) {
      System.out.println();//example conditional breakpoint
      }
    }
  }