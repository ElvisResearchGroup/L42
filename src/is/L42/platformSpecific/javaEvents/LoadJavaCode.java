package is.L42.platformSpecific.javaEvents;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import safeNativeCode.utils.Utils;

public class LoadJavaCode {
  static{
    var sup=Utils.getIsJavaClass();
    Utils.setIsJavaClass((loc,name)->
      name.equals("is.L42.platformSpecific.javaEvents.Event") || sup.test(loc, name)
      );
    }
  private static HashSet<String> loaded=new HashSet<>();
  private static void auxLoadJavaCode(String fullName,String code) throws CompilationError, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
    //full name in form "math.Calculator"
    if(loaded.contains(fullName)){return;}
    loaded.add(fullName);
    SourceFile file = new SourceFile(fullName,code);
    List<SourceFile> files = List.of(file);
    Event.initialize();
    Event e=Event.instance();//Thus, the class is loaded here, before calling the new code
    ClassLoader classes = InMemoryJavaCompiler.compile(
        //ClassLoader.getSystemClassLoader()
        LoadJavaCode.class.getClassLoader()
        ,files,new ArrayList<>());
    var k=classes.loadClass(fullName).getConstructor(Event.class);
    k.newInstance(e);
    }
  public static String loadJavaCode(String fullName,String code){
    try{auxLoadJavaCode(fullName, code);}
    catch(
        CompilationError|InstantiationException|
        IllegalAccessException|IllegalArgumentException|
        InvocationTargetException|NoSuchMethodException|
        SecurityException|ClassNotFoundException e){
      var sw=new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      return e.getClass().getName()+"\n"+e.getMessage()
        +"\n"+sw;
      }
    return "";
    }
  }
