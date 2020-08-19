package is.L42.platformSpecific.javaEvents;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import is.L42.tests.RunningUtils;

public class LoadJavaCode {
  /*private static String className(String code){
    int pkgStart=code.indexOf("package");
    if(pkgStart==-1){return null;}
    int pkgEnd=code.indexOf(";",pkgStart);
    if(pkgEnd==-1){return null;}
    String pkg=code.substring(pkgStart,pkgEnd).trim();
    pkg=pkg.replaceAll("[\\n\\t ]","");
    int classStart=code.indexOf("public class",pkgEnd);
    if(classStart==-1){return null;}
    int classEnd=code.indexOf(";",i);
    if(classEnd==-1){return null;}
    }*/
  private static void auxLoadJavaCode(String fullName,String code) throws CompilationError, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
    //"math.Calculator"
    SourceFile file = new SourceFile(fullName,code);
    List<SourceFile> files = List.of(file);
    ClassLoader classes = InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files,new ArrayList<>());
    classes.loadClass(fullName).getConstructor().newInstance();
    }
  public static String loadJavaCode(String fullName,String code){
    try{auxLoadJavaCode(fullName, code);}
    catch(
        CompilationError|InstantiationException|
        IllegalAccessException|IllegalArgumentException|
        InvocationTargetException|NoSuchMethodException|
        SecurityException|ClassNotFoundException e){
      return e.getClass().getName()+"\n"+e.getMessage();
      }
    return "";
    }
  }
