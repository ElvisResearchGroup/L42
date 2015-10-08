package platformSpecific.inMemoryCompiler;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.tools.*;

import facade.L42;

public class InMemoryJavaCompiler {
  public static class SourceFile extends SimpleJavaFileObject {
    private String contents;
    public SourceFile(String className, String contents) {
      super(java.net.URI.create("string:///" + className.replace('.', '/')+ Kind.SOURCE.extension), Kind.SOURCE);
      this.contents = contents;}
    public CharSequence getCharContent(boolean b){return contents;}
  }
  @SuppressWarnings("serial")
  public static class CompilationError extends Exception{
    public final Diagnostic<? extends JavaFileObject> diagnostic;
    CompilationError(Diagnostic<? extends JavaFileObject> diagnostic){
      super(diagnostic.toString()); this.diagnostic=diagnostic;}
  }
  private static class ClassFile extends SimpleJavaFileObject {
    private final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();
    public ClassFile(String name, Kind kind) {
        super(java.net.URI.create("string:///" + name.replace('.', '/')+kind.extension),kind);}
    public byte[] getBytes() { return byteCode.toByteArray(); }
    @Override
    public OutputStream openOutputStream() throws IOException { return byteCode; }
  }
  private static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
    public Diagnostic<? extends JavaFileObject> diagnostic=null;
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
      this.diagnostic=diagnostic;
      }
    }
  private static String plugins(){
    String result="";
    if(L42.pluginPaths==null){
      return System.getProperty("java.class.path");
      }
    for (URL url:L42.pluginPaths){result+=url.toString().substring(6)+System.getProperty("path.separator");}
    //TODO: substring 6 is a hack to remove "file:/", do it better.
    result+=System.getProperty("java.class.path");
    return result;
  }
  public static ClassLoader compile(List<SourceFile> files) throws CompilationError {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) throw new Error("Only JDK contains the JavaCompiler, you are running a Java JRE");
    MyDiagnosticListener diagnisticListenerForErrors=new MyDiagnosticListener();
    final HashMap<String,ClassFile> map=new HashMap<>();
    final ClassLoader classLoader=new java.security.SecureClassLoader(ClassLoader.getSystemClassLoader()) {
      @Override
      protected Class<?> findClass(String name)throws ClassNotFoundException {
        if(!map.containsKey(name))return super.findClass(name);
        ClassFile jclassObject = map.get(name);
        return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
        }
      };
    final ForwardingJavaFileManager<StandardJavaFileManager>  classFileManager=
        new ForwardingJavaFileManager<StandardJavaFileManager>(compiler.getStandardFileManager(diagnisticListenerForErrors,Locale.ENGLISH, null)){
      @Override
      public ClassLoader getClassLoader(Location location) {return classLoader;}
      @Override
      public JavaFileObject getJavaFileForOutput(Location location,String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException{
        ClassFile classFile=new ClassFile(className, kind);
        map.put(className, classFile);
        return classFile;
        }
      };
    if(!compiler.getTask(/*out:*/null,classFileManager,diagnisticListenerForErrors,
        /*compilerOptions:*/Arrays.asList("-Xlint:unchecked","-classpath",plugins()),/*StringsClasses??:*/null,files
      ).call()
        )throw new CompilationError(diagnisticListenerForErrors.diagnostic);
    return classLoader;
    }
  }