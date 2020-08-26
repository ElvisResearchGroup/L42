package is.L42.main;

import static is.L42.tools.General.L;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42Exception;
import is.L42.platformSpecific.javaTranslation.L42Return;
import is.L42.platformSpecific.javaTranslation.L42Throwable;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;

public class Main {
  public static void main(String...arg) throws IOException {
    String name = arg[0];
    assert name != null;
    Path path=Paths.get(name);
    boolean isDir=Files.exists(path) && Files.isDirectory(path);
    if(!Files.exists(path)){path=Paths.get(name+".L42");}
    run(path,isDir,false);      
    }
  public static void run(Path path,boolean isDir,boolean caching) throws IOException {
    CachedTop c=new CachedTop(L(),L());
    if(isDir && caching){c=CachedTop.loadCache(path);}
    run(path.resolve("This.L42"),c);
    if(caching){c.toNextCache().saveCache(path);}
    }
  public static Core.L run(Path path,CachedTop c) throws IOException {
    try{
      var code=Parse.codeFromPath(path);
      return Init.topCache(c,path,code);    
      }
    catch(L42Throwable ee){
      var pTails=Resources.currentP.pTails;
      var location=pTails.printCs();
      if(!location.isEmpty()){location+=".";}
      location+=Resources.currentC;
      Resources.err("While performing reduction on "+location);
      if(ee.getClass()==L42Exception.class){Resources.err("L42 terminated with exception");}
      else{Resources.err("L42 terminated with error");}
      printError(ee.unbox);
      printStackTrace(ee);
      throw ee;
      }
    catch(EndError ee) {
      Resources.err("-------------------------");
      Resources.err("Compile time error:");
      Resources.err("-------------------------");
      Resources.err(ee.getMessage());
      throw ee;
      }
    }
  public static Object _unwrap(Object o){
    Class<?> c=o.getClass(); 
    try{
      Field of=c.getField("unwrap");
      of.setAccessible(true);
      return of.get(o);
      }
    catch(ReflectiveOperationException roe){return null;}
    }
  private static String unMark(String s){
    s=s.replace("£h","#");
    int i0=s.indexOf("£n");
    if(i0!=-1) {s=s.substring(0,i0);}
    if(s.contains("£x")){s=s.replaceFirst(Pattern.quote("£x"),"(")+")";}
    s=s.replaceAll(Pattern.quote("£x"),", ");
    return s;//x h c n m
    }
  private static void printStackTrace(Throwable t){
    Resources.err("Reconstructed stack trace");
    String init="is.L42.metaGenerated.£c";
    for(var se:t.getStackTrace()){
      var cn=se.getClassName();
      var mn=se.getMethodName();
      if(mn.equals("execute")){return;}
      //is.L42.metaGenerated.£cIntrospection£n0£_£cNested
      //Introspection.Nested £n0£_£c
      cn=cn.replace(init,"");
      cn=Arrays.stream(cn.split(Pattern.quote("£_£c")))
        .map(s->unMark(s.trim())).collect(Collectors.joining("."));
      mn=unMark(mn.replace("£m",""));
      if(!mn.contains("(")){mn+="()";}
      if(mn.startsWith("#apply(")){
        Resources.err(cn+mn.substring("#apply".length()));
        }
      else{
        Resources.err(cn+"."+mn);
        }
      }
    }
  private static void printError(Object o){
    Class<?> c=o.getClass();
    try{Resources.err(c.getMethod("£mtoS").invoke(o).toString());return;}
    catch(Throwable t){
      t.printStackTrace();
      }
    try{
      Object oo=_unwrap(o);
      Class<?>cc=oo.getClass();
      try{Resources.err(cc.getMethod("£mtoS").invoke(oo).toString());return;}
      catch(Throwable t){}
      try{Resources.err(cc.getMethod("getMsg").invoke(oo).toString());return;}
      catch(Throwable t){}
      Resources.err("Methods of the unwrapped object");
      for(var m:cc.getMethods()){Resources.err(m.getName());}
      }
    catch(Throwable t){}
    Resources.err("The error type can not be displayed");
    for(var m:c.getMethods()){Resources.err(m.getName()+" "+m.toString());}
    for(var m:c.getFields()){Resources.err(m.getName());}
    }
  }