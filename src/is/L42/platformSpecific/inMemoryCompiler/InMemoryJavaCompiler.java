package is.L42.platformSpecific.inMemoryCompiler;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

import is.L42.perftests.PerfCounters;
import is.L42.tools.General;

public class InMemoryJavaCompiler {
  static URI strToUrl(String s,Kind kind){
    s="string:///" + s.replace('.', '/')+ kind.extension;
    try {return java.net.URI.create(URLEncoder.encode(s,"UTF-8"));}
    catch (UnsupportedEncodingException e) { throw new Error(e);}
    }
  public static class CompilationError extends Exception{
    public final MyDiagnosticListener diagnostic;
    CompilationError(MyDiagnosticListener diagnostic){
      super(diagnostic.toString());
      this.diagnostic=diagnostic;
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
  public static class ClassRefs {
    final String name;
    final Set<String> refs = new HashSet<>();
    public ClassRefs(String name) { this.name = name; }
    }
  public static class ResolvedClassRefs implements Serializable {
    final String name;
    final Set<ResolvedClassRefs> refs = new HashSet<>();
    public ResolvedClassRefs(String name) { this.name = name; }
    public Set<ResolvedClassRefs> allTransitiveDependencies() {
      return this.allTransitiveDependencies(new HashSet<>());
      }
    public Set<ResolvedClassRefs> allTransitiveDependencies(Set<ResolvedClassRefs> soFar) {
      soFar.add(this);
      refs.forEach(dep->{ if(!soFar.contains(dep)) { dep.allTransitiveDependencies(soFar); }});
      return soFar;
      }
    }
  public static MapClassLoader newClassLoader(ClassLoader env) {
    if(JITIsEnabled) { return new JITClassLoader(env); } 
    return new PreCompileClassLoader(env);
    }
  private static void perfCountClassContents(Collection<SourceFile> files, String prefix, boolean stdout) {
    for(SourceFile file : files) {
  	  int classes = (int) Pattern.compile("(public class)|(public static class)|(public interface)").matcher(file.contents).results().count();
  	  int chars = file.contents.length();
  	  int sloc = (int) Pattern.compile("\n").matcher(file.contents).results().count();
  	  int methods = (int) Pattern.compile("((public|protected|private|static|final|synchronized) )+[A-Za-z£][A-Za-z0-9£_]*(\\<[^ ]*\\>)? [A-Za-z£][A-Za-z0-9£_]*\\(").matcher(file.contents).results().count();
  	  int trycatch = (int) Pattern.compile("try\\{").matcher(file.contents).results().count();
  	  int switchzero = (int) Pattern.compile("switch\\(0\\)").matcher(file.contents).results().count();
  	  boolean executable = file.contents.contains("public static L42£Library execute()");
  	  PerfCounters.inc(prefix+".sourcefiles");
  	  PerfCounters.add(prefix+".classes", classes);
  	  PerfCounters.add(prefix+".chars", chars);
  	  PerfCounters.add(prefix+".sloc", sloc);
  	  PerfCounters.add(prefix+".methods", methods);
  	  PerfCounters.add(prefix+".trycatch", trycatch);
  	  PerfCounters.add(prefix+".switchzero", switchzero);
  	  if(methods == 6) {
  		if(stdout) { System.out.println("Zero method class!"); }
  	    PerfCounters.inc(prefix+".zeromethodclasses");
  	    PerfCounters.inc(prefix+".zerooronemethodclasses");
  	    }
  	  if(methods == 7) {
  	    if(stdout) { System.out.println("One method class!"); }
  	    PerfCounters.inc(prefix+".onemethodclasses");
  	    PerfCounters.inc(prefix+".zerooronemethodclasses");
  	    }
  	  if(executable) {
  	    PerfCounters.inc(prefix+".executableclasses");
  	    }
  	  if(!file.className.startsWith("is.L42.metaGenerated.")) { continue; }
  	  Matcher cname_m = Pattern.compile("public (class|interface) (£[A-Za-z0-9£\\$_]*)( |\\{)").matcher(file.contents);
  	  if(!cname_m.find()) { General.bug(); }
  	  String cname = cname_m.group(2);
  	  List<String> cnameList = Pattern.compile("£c([A-Za-z0-9$_]+)(£|$)").matcher(cname).results().map(m->m.group(1)).toList();
  	  String t = "";
  	  for(String name : cnameList) {
  	    PerfCounters.inc("javac.classes." + t + name);
  	    t = t + name + ".";
  	    }
  	  if(stdout) {
  	    System.out.println("classes: " + classes + " chars: " + chars + " sloc: " + sloc + " methods: " + methods + " trycatch: " + trycatch + " switchzero: " + switchzero);
  	    System.out.println(file.contents);
  	    }
      }
  	}
  public static final boolean JITIsEnabled = true;
  public static ClassLoader compile(ClassLoader env,List<SourceFile> files, JavaCodeStore newBytecode) throws CompilationError {
    if(PerfCounters.isEnabled()) {
      perfCountClassContents(files, "compile", false);
      }
    MapClassLoader classLoader;
    if(env instanceof MapClassLoader envMCL){
      classLoader=envMCL;
      }
    else {
      classLoader=newClassLoader(env);
      }
    
    classLoader.addSources(files, newBytecode);
    return classLoader;
    }
  private static final record ListArg(Location location, String string, Set<Kind> kinds, boolean recurse) {}
  private static final Set<ListArg> doneBefore = new HashSet<>();
  private static final MyJavaFileManager man;
  static { 
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) throw new Error("Only JDK contains the JavaCompiler, you are running a Java JRE");
    MyDiagnosticListener diagnisticListenerForErrors=new MyDiagnosticListener();
    StandardJavaFileManager sfm=compiler.getStandardFileManager(diagnisticListenerForErrors,Locale.ENGLISH, null);
    man = new MyJavaFileManager(sfm);
    }
  private static final class MyJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    protected MyJavaFileManager(StandardJavaFileManager fileManager) { super(fileManager); }
    private JavaCodeStore map;
    private ClassLoader classLoader;
    public void setMap(JavaCodeStore map) { this.map = map; }
    public void setClassLoader(ClassLoader classLoader) { this.classLoader = classLoader; }
    @Override public ClassLoader getClassLoader(Location location) {return classLoader;}
    @Override public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
      //CANNOT be made into a one-liner because of differing return types!
      //getClassFile returns an Optional<ClassFile> while superclass method returns a JavaFileObject
      ClassFile f = map.getClassFile(className).orElse(null);
      if (f!=null){return f;}
      return super.getJavaFileForInput(location, className, kind);
      }
    private void perfCountList(Location location,String packageName,Set<Kind> kinds,boolean recurse) {
      PerfCounters.inc("invoke.classFileManager.list");
      ListArg args = new ListArg(location, packageName, kinds, recurse);
      if(!doneBefore.contains(args)) {
        PerfCounters.inc("invoke.classFileManager.list.unique");
        System.out.println(args);
        }
      doneBefore.add(args);
      }
    @Override public Iterable<JavaFileObject> list(
        Location location,String packageName,Set<Kind> kinds,boolean recurse)throws IOException{
      if(PerfCounters.isEnabled()) {
        perfCountList(location,packageName,kinds,recurse);
        }
      Collection<JavaFileObject> delegated = this.delegateList(location, packageName, kinds, recurse);
      boolean needMap = kinds.contains(Kind.CLASS) && this.map.hasPackage(packageName);
      List<JavaFileObject> list = new ArrayList<JavaFileObject>(delegated.size() + (needMap ? this.map.size() : 0));
      if(needMap) {
        list.addAll(this.map.compiledByPackage(packageName));
        }
      list.addAll(delegated);
      return list;
      }
    Map<ListArg, Collection<JavaFileObject>> delegateListCache = new HashMap<>();
    Collection<JavaFileObject> delegateList(Location location,String packageName,Set<Kind> kinds,boolean recurse) {
      return delegateListCache.computeIfAbsent(new ListArg(location, packageName, kinds, recurse), la->{
        List<JavaFileObject> list = new ArrayList<>();
        try {
          for(JavaFileObject jfo:fileManager.list(la.location, la.string, la.kinds, la.recurse)){
            list.add(jfo);
            }
          } 
        catch (IOException e) { throw new Error(e); }
        return Collections.unmodifiableList(list);
        });
      }
    @Override public String inferBinaryName(Location location, JavaFileObject file){
      if(file instanceof ClassFile) { return ((ClassFile)file).name;}
      else { return super.inferBinaryName(location,file);}
      }
    @Override public JavaFileObject getJavaFileForOutput(
        Location location,String className, JavaFileObject.Kind kind, FileObject sibling)throws IOException{
      //ClassFile classFile=new ClassFile(className, kind);//maybe is easier to just be allowed to readd stuff
      //var added=map.put(className, classFile);
      //assert added==null:className;
      //outNewBytecode.add(classFile);
      //return classFile;
      return map.computeIfAbsentClass(className,k->{
        ClassFile classFile=new ClassFile(className, kind);
        //outNewBytecode.add(classFile);  
        return classFile;
        });
      }
    }
  static MapClassLoader auxCompile(MapClassLoader classLoader,List<SourceFile> files) throws CompilationError {
    if(PerfCounters.isEnabled()) {
      perfCountClassContents(files, "auxCompile", false);
      }
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    var diagnisticListenerForErrors=new MyDiagnosticListener();
    final var classFileManager=man;
    man.setMap(classLoader.java);
    man.setClassLoader(classLoader);
    CompilationTask compilerTask = compiler.getTask(
      /*out:*/null,classFileManager,diagnisticListenerForErrors,
      /*compilerOptions:*/Arrays.asList("-Xlint:-unchecked","-encoding","\"UTF-8\"","--enable-preview","--release",javaVersion(),"-classpath",System.getProperty("java.class.path")),
      //-Xlint:all-Xlint:unchecked
      /*StringsClasses??:*/null,files
      );
    boolean compilationRes=compilerTask.call();
    if(!compilationRes){
      System.err.println(files);
      throw new CompilationError(diagnisticListenerForErrors);
      }
    return classLoader;
    }
  static private String javaVersion(){
    var res=System.getProperty("java.version");
    if(res.contains(".")){res=res.substring(0,res.indexOf("."));}
    return res;
    }
  }