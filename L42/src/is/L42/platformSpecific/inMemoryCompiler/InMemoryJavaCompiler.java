package is.L42.platformSpecific.inMemoryCompiler;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

public class InMemoryJavaCompiler {
  private static URI strToUrl(String s,Kind kind){
    s="string:///" + s.replace('.', '/')+ kind.extension;
    try {return java.net.URI.create(URLEncoder.encode(s,"UTF-8"));}
    catch (UnsupportedEncodingException e) { throw new Error(e);}
    }
  public static class SourceFile extends SimpleJavaFileObject {
    private String contents;
    public SourceFile(String className, String contents) {
      super(strToUrl(className,Kind.SOURCE), Kind.SOURCE);
      this.contents = contents;
      }
    public CharSequence getCharContent(boolean b){return contents;}
    public String toString(){
      return super.toString()+"\n"+contents;
      }
    }
  @SuppressWarnings("serial")
  public static class CompilationError extends Exception{
    public final MyDiagnosticListener diagnostic;
    CompilationError(MyDiagnosticListener diagnostic){
      super(diagnostic.toString());
      this.diagnostic=diagnostic;
      }
    }
  public static class ClassFile extends SimpleJavaFileObject{
    @Override public int hashCode() {
      cacheBytes();
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(bytes);
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
      }
    @Override public boolean equals(Object obj) {
      if (this == obj){return true;}
      if (obj == null){return false;}
      if (getClass() != obj.getClass()){return false;}
      ClassFile other = (ClassFile) obj;
      cacheBytes();
      other.cacheBytes();
      if (!Arrays.equals(bytes, other.bytes)){return false;}
      if (name == null) {if (other.name != null){return false;}}
      else{if (!name.equals(other.name)){return false;}}
      return true;
      }
    private final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();
    public final String name;
    private byte[] bytes=null;
    public ClassFile(String name, Kind kind) {
      super(strToUrl(name,kind),kind);
      this.name=name;
      }
    private void cacheBytes() {
      if(bytes!=null) {return;}
      bytes=byteCode.toByteArray();
      try {byteCode.close();}
      catch (IOException e) {throw new Error(e);}
      }
    public byte[] getBytes() {
      if(bytes==null) {cacheBytes();}
      return bytes;
      }
    @Override public InputStream openInputStream() {
      return new ByteArrayInputStream(getBytes());
      }
    @Override public OutputStream openOutputStream() throws IOException {
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
  public static class MapClassLoader extends java.security.SecureClassLoader{
    public MapClassLoader(HashMap<String,ClassFile> map,ClassLoader env){
      super(env);
      this.map=map;
      }
    public void readAllStreams() {
      for(String s:map.keySet()){ map.get(s).cacheBytes();}
      }
    private final HashMap<String,ClassFile> map;
    public HashMap<String,ClassFile> map(){return map;}
    public static class SClassFile implements Serializable{
      private static final long serialVersionUID = 1L;
      public  String name;
      private byte[] bytes=null;
      private Kind kind;
      public static SClassFile fromCF(ClassFile from){
        SClassFile res=new SClassFile();
        res.name=from.name;
        assert from.bytes!=null;
        res.bytes=from.bytes;
        res.kind=from.getKind();
        return res;
        }
      public ClassFile toCF(){
        ClassFile res=new ClassFile(name,kind);
        res.bytes=this.bytes;
        return res;
        }
      }
    public void saveOnFile(Path file){
      HashMap<String, SClassFile> smap = exportMap();
      try (
        var os = Files.newOutputStream(file);
        var out = new ObjectOutputStream(os);
        ){out.writeObject(smap);}
      catch(IOException i) {throw new Error(i);}
      }
    public HashMap<String, SClassFile> exportMap() {
      var smap =new HashMap<String, SClassFile>();
      for(var e:map().entrySet()){
        smap.put(e.getKey(),SClassFile.fromCF(e.getValue()));
        }
      return smap;
      }
    public static MapClassLoader readFromFile(Path file,ClassLoader env){
      try (
        var is = Files.newInputStream(file);
        var in = new ObjectInputStream(is);
        ){
        Object res = in.readObject();
        @SuppressWarnings("unchecked")
        var smap=(HashMap<String,SClassFile>)res;
        var map =new HashMap<String, ClassFile>();
        for(var e:smap.entrySet()){
          map.put(e.getKey(),e.getValue().toCF());
          }
        return new MapClassLoader(map,env);
        }
      catch(IOException i) {throw new Error(i);}//keep them in 3 catches to put breakpoints
      catch (ClassNotFoundException e) {throw new Error(e);}
      catch (ClassCastException e) {throw new Error(e);}//means file corrupted?
      }
    public void updateFromMap(HashMap<String,SClassFile> smap){
      map.clear();
      for(var e:smap.entrySet()){
        map.put(e.getKey(),e.getValue().toCF());
        }
      }
    @Override protected Class<?> findClass(String name)throws ClassNotFoundException {
      if(!map.containsKey(name)){
        return super.findClass(name);
        }
      ClassFile jclassObject = map.get(name);
      return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
      }
    @Override 
    public Class<?> loadClass(String name)throws ClassNotFoundException {
      if(!map.containsKey(name)){
        return super.loadClass(name);
        }
      ClassFile jclassObject = map.get(name);
      return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
      }
    @Override
    public URL getResource(String name) {
        String javaName = name.replace(".class","").replace(File.separatorChar/*'/'*/, '.');
        //If this isn't a class from this compiler, hand off to the parent
        if(!map.containsKey(javaName)){javaName = name.replace(".class","").replace('/', '.');}
        //if(!map.containsKey(javaName)){javaName = javaName.replace('/', '.');}
        //NOTE: probably there is a bug in safeNativeCode, where '/' is used instead of File.separatorChar
        if (!map.containsKey(javaName)){return super.getResource(name);}
        //Return a new url, that returns our file for its input stream.
        try {
            return new URL(null, "string:" + javaName, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) {
                    return new URLConnection(u) {
                        public void connect() {
                        }

                        @Override
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream(map.get(url.getPath()).getBytes());
                        }
                    };
                }
            });
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
    }
  public static MapClassLoader compile(ClassLoader env,List<SourceFile> files, List<SClassFile> newBytecode) throws CompilationError {
    var out=new ArrayList<ClassFile>();
    try{return auxCompile(env,files,out);}
    finally{
      for(var o:out){
        o.cacheBytes();
        newBytecode.add(SClassFile.fromCF(o));
        }
      }
    }
  private static MapClassLoader auxCompile(ClassLoader env,List<SourceFile> files, List<ClassFile> outNewBytecode) throws CompilationError {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) throw new Error("Only JDK contains the JavaCompiler, you are running a Java JRE");
    var diagnisticListenerForErrors=new MyDiagnosticListener();
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
    var sfm=compiler.getStandardFileManager(diagnisticListenerForErrors,Locale.ENGLISH, null);
    final var classFileManager=new ForwardingJavaFileManager<StandardJavaFileManager>(sfm){
      @Override public ClassLoader getClassLoader(Location location) {return classLoader;}
      @Override public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
        ClassFile f = map.get(className);
        if (f!=null){return f;}
        return super.getJavaFileForInput(location, className, kind);
        }
      @Override public Iterable<JavaFileObject> list(
          Location location,String packageName,Set<Kind> kinds,boolean recurse)throws IOException{
        var list=new ArrayList<JavaFileObject>();
        if(kinds.contains(Kind.CLASS)) {
          for (var c:map.values()){
            if (c.name.startsWith(packageName + ".")){list.add(c);}
            }
          }
        for(JavaFileObject jfo:fileManager.list(location, packageName, kinds, recurse)){
          list.add(jfo);
          }
        return list;
        }
      @Override public String inferBinaryName(Location location, JavaFileObject file){
        try{
          String res;
          if(file instanceof ClassFile){res=((ClassFile)file).name;}
          else{ res=super.inferBinaryName(location,file);}
          return res;
          }
        catch(IllegalArgumentException ill){throw ill;}
        }
      @Override public JavaFileObject getJavaFileForOutput(
          Location location,String className, JavaFileObject.Kind kind, FileObject sibling)throws IOException{
        ClassFile classFile=new ClassFile(className, kind);
        var added=map.put(className, classFile);
        assert added==null:className;
        outNewBytecode.add(classFile);
        return classFile;
        }
      };
    CompilationTask compilerTask = compiler.getTask(
      /*out:*/null,classFileManager,diagnisticListenerForErrors,
      /*compilerOptions:*/Arrays.asList("-Xlint:unchecked","-encoding","\"UTF-8\"","--enable-preview","--release","13","-classpath",System.getProperty("java.class.path")),
      /*StringsClasses??:*/null,files
      );
    boolean compilationRes=compilerTask.call();
    if(!compilationRes){
      System.out.println(files);
      throw new CompilationError(diagnisticListenerForErrors);
      }
    return classLoader;
    }
  }