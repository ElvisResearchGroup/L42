package platformSpecific.inMemoryCompiler;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javax.tools.*;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

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
    public final MyDiagnosticListener diagnostic;
    CompilationError(MyDiagnosticListener diagnostic){
      super(diagnostic.toString()); this.diagnostic=diagnostic;}
  }
  public static class ClassFile extends SimpleJavaFileObject implements Serializable {
    private static final long serialVersionUID = 1L;
    transient private final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();
    public final String name;
    private byte[] bytes=null;
    public ClassFile(String name, Kind kind) {
        super(java.net.URI.create("string:///" + name.replace('.', '/')+kind.extension),kind);
        this.name=name;}
    private void cacheBytes() {bytes=byteCode.toByteArray(); }
    private byte[] getBytes() {if(bytes==null) {cacheBytes();} return bytes;}
    @Override
    public InputStream openInputStream() {
      return new ByteArrayInputStream(getBytes());
      }
    @Override
    public OutputStream openOutputStream() throws IOException {
      return byteCode; 
      }
    }
  
  
  private static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
    public List<Diagnostic<? extends JavaFileObject>> diagnostic=new ArrayList<>();
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
      this.diagnostic.add(diagnostic);
      }
    public String toString(){
      StringBuilder res=new StringBuilder();
      for(Diagnostic<? extends JavaFileObject> d: diagnostic){
        res.append(d.toString());
        res.append("\n\n");
        }
      return res.toString();
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
  public static class MapClassLoader extends java.security.SecureClassLoader{
      public MapClassLoader(HashMap<String,ClassFile> map,ClassLoader env){
        super(env);
        this.map=map;
        }
      private final HashMap<String,ClassFile> map;
      public HashMap<String,ClassFile> map(){return map;}
      
      public void saveOnFile(Path file){
        try (
          OutputStream os = Files.newOutputStream(file);
          ObjectOutputStream out = new ObjectOutputStream(os);
        ){
          out.writeObject(map());
        }catch(IOException i) {throw new Error(i);}
      }
      
      public static MapClassLoader readFromFile(Path file,ClassLoader env){
        try (
          InputStream is = Files.newInputStream(file);
          ObjectInputStream in = new ObjectInputStream(is);
        ){
          Object res = in.readObject();
          @SuppressWarnings("unchecked")
          HashMap<String,ClassFile> map=(HashMap<String,ClassFile>)res;
          return new MapClassLoader(map,env);
        }
        catch(IOException i) {throw new Error(i);}
        catch (ClassNotFoundException e) {throw new Error(e);}
        catch (ClassCastException e) {throw new Error(e);}//means file corrupted?
      }
      
      @Override
      protected Class<?> findClass(String name)throws ClassNotFoundException {
        if(!map.containsKey(name)){
          return super.findClass(name);
          }
        ClassFile jclassObject = map.get(name);
        return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
        }
      };

  public static MapClassLoader compile(ClassLoader env,List<SourceFile> files) throws CompilationError {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) throw new Error("Only JDK contains the JavaCompiler, you are running a Java JRE");
    MyDiagnosticListener diagnisticListenerForErrors=new MyDiagnosticListener();
    final HashMap<String,ClassFile> map;
    final MapClassLoader classLoader;
    if(!(env instanceof MapClassLoader)){
      map= new HashMap<>();
      classLoader=new MapClassLoader(map,env);
      }
    else{
      classLoader=(MapClassLoader)env;
      map=classLoader.map();
      }
    final ForwardingJavaFileManager<StandardJavaFileManager>  classFileManager=
        new ForwardingJavaFileManager<StandardJavaFileManager>(compiler.getStandardFileManager(diagnisticListenerForErrors,Locale.ENGLISH, null)){
      @Override
      public ClassLoader getClassLoader(Location location) {return classLoader;}

      @Override
      public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
        ClassFile f = map.get(className);
        if (f!=null){return f;}
        return super.getJavaFileForInput(location, className, kind);
    }
    @Override   public Iterable<JavaFileObject> list(Location location,
                                         String packageName,
                                         Set<Kind> kinds,
                                         boolean recurse)
        throws IOException  {
        ArrayList<JavaFileObject> list=new ArrayList<>();
        if(kinds.contains(Kind.CLASS)){list.addAll(map.values());}
        for(JavaFileObject jfo:fileManager.list(location, packageName, kinds, recurse)){
          list.add(jfo);
          }
        return list;
    }
    @Override public String inferBinaryName(Location location, JavaFileObject file) {
        try{
        String res;
        if(file instanceof ClassFile){res=((ClassFile)file).name;}
        else{ res=super.inferBinaryName(location,file);}
        return res;
        }catch(IllegalArgumentException ill){
        throw ill;}
    }

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
        ){
        throw new CompilationError(diagnisticListenerForErrors);
        }
    return classLoader;
    }
  }