package is.L42.platformSpecific.javaEvents;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import is.L42.tests.RunningUtils;

public class LoadJavaCode {
  private static HashSet<String> loaded=new HashSet<>();
  private static void auxLoadJavaCode(String fullName,String code) throws CompilationError, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
    //full name in form "math.Calculator"
    if(loaded.contains(fullName)){return;}
    loaded.add(fullName);
    SourceFile file = new SourceFile(fullName,code);
    List<SourceFile> files = List.of(file);
    ClassLoader classes = InMemoryJavaCompiler.compile(Event.class.getClassLoader(),files,new ArrayList<>());
    classes.loadClass(fullName).getConstructor().newInstance();
    }
  public static String loadJavaCode(String fullName,String code){
    Event.preLoad();
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
