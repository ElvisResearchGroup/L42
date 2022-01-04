package is.L42.platformSpecific.javaEvents;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.SourceFile;
import is.L42.platformSpecific.inMemoryCompiler.JavaCodeStore;
import safeNativeCode.utils.Utils;

public class LoadJavaCode {
  static final private List<String> extra=List.of(
    "is.L42.platformSpecific.javaEvents.Event",
    "is.L42.platformSpecific.javaEvents.ConcreteEvent"
    //"is.L42.platformSpecific.javaEvents.Event.Function3",
    //"is.L42.platformSpecific.javaEvents.Event.Consumer3"
    );
  static{
    var sup=Utils.getIsJavaClass();
    Utils.setIsJavaClass((loc,name)->
      extra.contains(name) || sup.test(loc, name)
      );
    }
  private static HashSet<String> loaded=new HashSet<>();
  private static void auxLoadJavaCode(String fullName,String code) throws CompilationError, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
    //full name in form "math.Calculator"
    if(loaded.contains(fullName)){return;}
    loaded.add(fullName);
    SourceFile file = new SourceFile(fullName,code);
    List<SourceFile> files = List.of(file);
    ConcreteEvent.initialize();
    Event e=ConcreteEvent.instance();//Thus, the class is loaded here, before calling the new code
    ClassLoader classes = InMemoryJavaCompiler.compile(
        //ClassLoader.getSystemClassLoader()
        LoadJavaCode.class.getClassLoader()
        ,files,new JavaCodeStore()); 
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