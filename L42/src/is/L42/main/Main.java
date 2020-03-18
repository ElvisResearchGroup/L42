package is.L42.main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42Exception;
import is.L42.platformSpecific.javaTranslation.L42Return;
import is.L42.platformSpecific.javaTranslation.L42Throwable;
import is.L42.top.Init;

public class Main {
  public static void main(String...arg) throws IOException {
    String name = arg[0];
    assert name != null;
    Path path=Paths.get(name);
    if(Files.exists(path) && Files.isDirectory(path)){
      path=path.resolve("This.L42");
      }
    else if(!Files.exists(path)){path=Paths.get(name+".L42");}
    try{
      var code=Parse.fromPath(path);
      Init init=new Init((Full.L)code);    
      init.top.top(init.p);
      }
    catch(L42Throwable ee){printError(ee.unbox); throw ee;}
    }
  private static void printError(Object o){
    System.out.println("L42 Error message:");
    Class<?> c=o.getClass(); 
    System.out.println(c.getCanonicalName());
    try{System.out.println(c.getMethod("£mtoS").invoke(o).toString());return;}
    catch(Throwable t){}
    try{
      Field of=c.getField("unwrap");
      of.setAccessible(true);
      Object oo=of.get(o);
      Class<?>cc=oo.getClass();
      try{System.out.println(cc.getMethod("£mtoS").invoke(oo).toString());return;}
      catch(Throwable t){}
      try{System.out.println(cc.getMethod("getMsg").invoke(oo).toString());return;}
      catch(Throwable t){}
      System.out.println("Methods of the unwrapped object");
      for(var m:cc.getMethods()){System.out.println(m.getName());}
      }
    catch(Throwable t){}
    System.out.println("The error type can not be displayed");
    for(var m:c.getMethods()){System.out.println(m.getName());}
    for(var m:c.getFields()){System.out.println(m.getName());}
    }
  }