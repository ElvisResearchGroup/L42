package platformSpecific.inMemoryCompiler;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

import facade.L42;

public class InMemoryJavaCompiler {
  public static class SourceFile extends SimpleJavaFileObject {
    private String contents;
    private static URI methForSuper(String s){
    try {
    return java.net.URI.create(URLEncoder.encode(s,"UTF-8"));
    } catch (UnsupportedEncodingException e) { throw new Error(e);}
    }
    public SourceFile(String className, String contents) {
      super(methForSuper(
        "string:///" + className.replace('.', '/')+ Kind.SOURCE.extension
        ), Kind.SOURCE);
      this.contents = contents;}
    public CharSequence getCharContent(boolean b){return contents;}
  }
  @SuppressWarnings("serial")
  public static class CompilationError extends Exception{
    public final MyDiagnosticListener diagnostic;
    CompilationError(MyDiagnosticListener diagnostic){
      super(diagnostic.toString()); this.diagnostic=diagnostic;}
  }
  public static class ClassFile extends SimpleJavaFileObject{
    @Override
    public int hashCode() {
      cacheBytes();
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(bytes);
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ClassFile other = (ClassFile) obj;
      cacheBytes();
      other.cacheBytes();
      if (!Arrays.equals(bytes, other.bytes))
        return false;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      return true;
    }
    private final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();
    public final String name;
    private byte[] bytes=null;
    public ClassFile(String name, Kind kind) {
      super(java.net.URI.create("string:///" + name.replace('.', '/')+kind.extension),kind);
      this.name=name;
      }
    private void cacheBytes() {
      if(bytes!=null) {return;}
      bytes=byteCode.toByteArray();
      try {byteCode.close();}
      catch (IOException e) {throw new Error(e);}
      }
    public byte[] getBytes() {if(bytes==null) {cacheBytes();} return bytes;}
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
      public void readAllStreams() {
        for(String s: map.keySet()) {
          map.get(s).cacheBytes();
          }
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
          OutputStream os = Files.newOutputStream(file);
          ObjectOutputStream out = new ObjectOutputStream(os);
        ){
          out.writeObject(smap);
        }catch(IOException i) {throw new Error(i);}
      }

      public HashMap<String, SClassFile> exportMap() {
        HashMap<String, SClassFile> smap =new HashMap<>();
        for(Entry<String, ClassFile> e:map().entrySet()){
          smap.put(e.getKey(),SClassFile.fromCF(e.getValue()));
          }
        return smap;
      }

      public static MapClassLoader readFromFile(Path file,ClassLoader env){
        try (
          InputStream is = Files.newInputStream(file);
          ObjectInputStream in = new ObjectInputStream(is);
        ){
          Object res = in.readObject();
          @SuppressWarnings("unchecked")
          HashMap<String,SClassFile> smap=(HashMap<String,SClassFile>)res;
          HashMap<String, ClassFile> map =new HashMap<>();
          for(Entry<String, SClassFile> e:smap.entrySet()){
            map.put(e.getKey(),e.getValue().toCF());
            }
          return new MapClassLoader(map,env);
          }
        catch(IOException i) {throw new Error(i);}
        catch (ClassNotFoundException e) {throw new Error(e);}
        catch (ClassCastException e) {throw new Error(e);}//means file corrupted?
        }
      public void updateFromMap(HashMap<String,SClassFile> smap){
        map.clear();
        for(Entry<String, SClassFile> e:smap.entrySet()){
          map.put(e.getKey(),e.getValue().toCF());
          }
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
        if(kinds.contains(Kind.CLASS)) {
          for (ClassFile c : map.values())
            if (c.name.startsWith(packageName + "."))
              list.add(c);
        }
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
    CompilationTask compilerTask = compiler.getTask(/*out:*/null,classFileManager,diagnisticListenerForErrors,
            /*compilerOptions:*/Arrays.asList("-Xlint:unchecked","-encoding","\"UTF-8\"","-classpath",plugins()),/*StringsClasses??:*/null,files
            );
    boolean compilationRes=compilerTask.call();
    if(!compilationRes){
        throw new CompilationError(diagnisticListenerForErrors);
        }
    return classLoader;
    }
  }