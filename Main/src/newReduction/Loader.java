package newReduction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ast.MiniJ;
import facade.Configuration;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import platformSpecific.javaTranslation.Resources;

public class Loader {
  ClassTable ct;
  HashMap<String, ClassFile> map=new HashMap<>();
  MapClassLoader cl=new MapClassLoader(map, ClassLoader.getSystemClassLoader());
  Object runAndTrash(MiniJ.CD j) throws CompilationError, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
  //classLoader=Loader.load(toBytecode(J))
  //return classLoader.usualReflectionToCall("CN.execute()")
  List<SourceFile> files =Collections.singletonList(new SourceFile(
    j.getCn(),"package generated;\n"+MiniJToJava.of(j)
    ));
  System.out.println(MiniJToJava.of(j));
  //try{Files.write(Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+k+".java"), map.get(k).getBytes());}catch (IOException _e) {throw new Error(_e);}
  //try{Files.write(Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+j.getCn()+".java"), files.get(j.getCn()).getBytes());}catch (IOException _e) {throw new Error(_e);}
  MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
  Class<?> cl0 = clX.loadClass("generated."+j.getCn());
  Method m0 = cl0.getDeclaredMethod("execute0");
  Object result = m0.invoke(null);
  return result;
  }
}
