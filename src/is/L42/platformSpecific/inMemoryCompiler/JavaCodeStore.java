package is.L42.platformSpecific.inMemoryCompiler;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassRefs;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ResolvedClassRefs;

public class JavaCodeStore implements Serializable {
  private final Map<String, SourceFile> sources = new HashMap<>();
  private final Map<String, ClassFile> compiled = new HashMap<>();
  private final Map<String, ResolvedClassRefs> depMap;
  private final Set<String> packages;
  private final JavaCodeStore prev;
  public JavaCodeStore() {
    this.depMap = new HashMap<>();
    this.packages = new HashSet<>();
    this.prev = null;
    }
  private JavaCodeStore(JavaCodeStore prev) {
    this.depMap = new HashMap<>(prev.depMap); 
    this.packages = new HashSet<>(prev.packages);
    this.prev = prev;
    }
  public JavaCodeStore next() { return new JavaCodeStore(this); }
  public Optional<ClassFile> getClassFile(String name) {
    ClassFile cf = this.getAllCompiled().get(name);
    if(cf == null) { return Optional.empty(); }
    return Optional.of(cf);
    }
  public SourceFile getSourceFile(String name) {
    SourceFile sf = this.getAllSources().get(name);
    if(sf == null) { throw new Error("Tried to get non-existent file"); }
    return sf;
    }
  public ResolvedClassRefs getDependencies(String name) { return this.depMap.get(name); }
  public boolean hasFile(String name) {
    if(this.hasFileInner(name)) { return true; }
    else if(name.contains("$")) {  return this.hasFile(name.substring(0, name.lastIndexOf('$'))); }
    return false;
  }
  public boolean hasFileInner(String name) {
    if(this.getAllCompiled().containsKey(name)) { return true; }
    if(this.getAllSources().containsKey(name)) { return true; }
    return false;
    }
  public void putCompiled(String name, ClassFile cf) { 
    this.getAllCompiled().put(name, cf);
    compiled.put(name, cf); 
    }
  public void putSources(Collection<SourceFile> sfs) { this.putSources(sfs, null); }
  public void putSources(Collection<SourceFile> sfs, Collection<SourceFile> noDepsAnalysis) {
    this.getAllSourcesCache = null;
    Map<String, ClassRefs> classMap = new HashMap<>();
    for(SourceFile file : sfs) {
      String pname = file.className.substring(0, file.className.lastIndexOf("."));
      this.getAllSources().put(file.className, file);
      sources.put(file.className, file);
      packages.add(pname);
      if(pname.equals("is.L42.metaGenerated")) {
        putSourcesOnGeneratedSource(file, classMap);
        }
      else {
        if(noDepsAnalysis != null) noDepsAnalysis.add(file);
        }
      }
    classMap.values().forEach(val->depMap.put(val.name, new ResolvedClassRefs(val.name)));
    classMap.forEach((name, unresolvedClass) -> {
      ResolvedClassRefs resolvedClass = depMap.get(unresolvedClass.name);
      unresolvedClass.refs.forEach(v->{
        if(depMap.containsKey(v)) {
          resolvedClass.refs.add(depMap.get(v));
          }
        });
      });
    }
  private void putSourcesOnGeneratedSource(SourceFile file, Map<String, ClassRefs> classMap) {
    boolean executable = file.contents.contains("public static L42£Library execute()");
    ClassRefs dc = new ClassRefs(file.className);
    Pattern.compile("£[A-Za-z0-9£\\$_]*").matcher(file.contents).results().forEach((r)->dc.refs.add("is.L42.metaGenerated." + r.group()));
    classMap.put(file.className, dc);
    if(!executable) {
      ClassRefs dc2 = new ClassRefs(file.className + "$_Fwd");
      dc2.refs.add(file.className);
      classMap.put(dc2.name, dc2);
      }
    }
  //Should only ever be read by getAllCompiled() (can be written to by many)
  private transient Map<String, ClassFile> getAllCompiledCache = null; 
  public Map<String, ClassFile> getAllCompiled() {
    if(getAllCompiledCache == null) {
      this.getAllCompiledCache = this.prev == null ? new HashMap<>() : new HashMap<>(this.prev.getAllCompiled());
      this.getAllCompiledCache.putAll(this.compiled);
      }
    return getAllCompiledCache;
    }
  private transient Map<String, SourceFile> getAllSourcesCache = null; 
  public Map<String, SourceFile> getAllSources() {
    if(getAllSourcesCache == null) {
      this.getAllSourcesCache = this.prev == null ? new HashMap<>() : new HashMap<>(this.prev.getAllSources());
      this.getAllSourcesCache.putAll(this.sources);
      }
    return getAllSourcesCache;
    }
  public Collection<ClassFile> compiledByPackage(String packagename) {
    //This method shouldn't be called for scanning, that's what hasPackage() and hasFile() are for
    assert packages.contains(packagename);
    //This method is called quite often by the file manager, and in almost all cases we only have one package (is.L42.metaGenerated) to worry about
    if(packages.size() == 1) { return this.getAllCompiled().values(); }
    return this.getAllCompiled().entrySet().stream()
                                .filter(e->e.getKey().startsWith(packagename+"."))
                                .map(Map.Entry::getValue).toList();
    }
  public boolean hasPackage(String packagename) { return this.packages.contains(packagename); }
  public int size() {
    return this.sources.size() + (this.prev != null ? this.prev.size() : 0);
    }
  public ClassFile computeIfAbsentClass(String name, Function<String, ClassFile> sup) {
    ClassFile ret;
    if(prev != null && prev.hasFile(name)) { ret = prev.computeIfAbsentClass(name, sup); } 
    else { ret = this.compiled.computeIfAbsent(name, sup); }
    this.getAllCompiled().put(name, ret);
    return ret;
    }
  }
