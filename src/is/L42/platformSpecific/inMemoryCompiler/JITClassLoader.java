package is.L42.platformSpecific.inMemoryCompiler;

import java.util.ArrayList;
import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;

public class JITClassLoader extends MapClassLoader{
  public JITClassLoader(ClassLoader env){
    super(env);
    }
  @Override
  protected Class<?> getMappedClass(String name) throws ClassFormatError {
    if(!this.map().getAllCompiled().containsKey(name)) {
      List<String> deps = this.map().getDependencies(name).allTransitiveDependencies().stream().map(r->r.name).toList();
      List<SourceFile> toCompile = deps.stream().filter(dep->!this.map().getAllCompiled().containsKey(dep)).map(dep->this.map().getSourceFile(dep)).toList();
      try {
        InMemoryJavaCompiler.auxCompile(this, toCompile);
        } 
      catch (CompilationError e) { throw new Error(e); }
      }
    ClassFile jclassObject = this.map().getClassFile(name).get();
    return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
    }
  @Override protected boolean hasMappedClass(String name) {
    return this.map().hasFile(name);
    }
  @Override
  public void addSources(List<SourceFile> files, JavaCodeStore newBytecode) {
    List<SourceFile> noDeps = new ArrayList<>();
    newBytecode.putSources(files, noDeps);
    this.updateMap(newBytecode);
    //Pre-compile classes that aren't auto-generated, as we cannot create reliable dependency graphs for them
    if(noDeps.size() > 0) {
      try {
        InMemoryJavaCompiler.auxCompile(this, noDeps);
        } 
      catch (CompilationError e) { throw new Error(e); }
      }
    }
}