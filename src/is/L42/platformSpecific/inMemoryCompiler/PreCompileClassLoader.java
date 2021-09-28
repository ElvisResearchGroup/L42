package is.L42.platformSpecific.inMemoryCompiler;

import java.util.List;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;

public class PreCompileClassLoader extends MapClassLoader {
  public PreCompileClassLoader(ClassLoader env){
    super(env);
    }
  @Override protected Class<?> getMappedClass(String name) throws ClassFormatError {
    ClassFile jclassObject = this.java.getClassFile(name).get();
    return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
    }
  @Override
  protected boolean hasMappedClass(String name) {
    return this.java.hasFile(name);
    }
  @Override
  public void addSources(List<SourceFile> files, JavaCodeStore newBytecode) {
    this.updateMap(newBytecode);
    this.map().putSources(files);
    try {
      InMemoryJavaCompiler.auxCompile(this, files);
      } 
    catch (CompilationError e) { throw new Error(e); }
    }
}