package is.L42.platformSpecific.inMemoryCompiler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.SecureClassLoader;
import java.util.List;

import is.L42.perftests.PerfCounters;

public abstract class MapClassLoader extends SecureClassLoader {
  protected JavaCodeStore java = new JavaCodeStore();
  protected MapClassLoader(ClassLoader env) {
    super(env);
    }
  public void updateMap(JavaCodeStore map) {
    this.java = map;
    }
  public abstract void addSources(List<SourceFile> files, JavaCodeStore newBytecode);
  // Called by both findClass and loadClass
  protected abstract Class<?> getMappedClass(String name) throws ClassFormatError;
  protected abstract boolean hasMappedClass(String name);
  @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
    name = name.replaceAll("/", ".");
    if(this.hasMappedClass(name)) {
      if(PerfCounters.isEnabled()) {
        PerfCounters.inc("mapclassloader.loadclass");
        }
      return this.getMappedClass(name);
      }
    return super.findClass(name);
    }
  @Override public Class<?> loadClass(String name) throws ClassNotFoundException {
    if(this.hasMappedClass(name)) {
      if(PerfCounters.isEnabled()) {
        PerfCounters.inc("mapclassloader.loadclass");
        }
      return this.getMappedClass(name);
      }
    return super.loadClass(name);
    }
  public void readAllStreams() {
    for(ClassFile file : this.java.getAllCompiled().values()) { file.cacheBytes(); } //TODO: Can be made even faster by restricting to only this layer
    }
  public JavaCodeStore map(){return this.java;}
  private URLStreamHandler forcedUrlResolution(){
    return new URLStreamHandler(){
      @Override protected URLConnection openConnection(URL u){
        return new URLConnection(u){
          @Override public void connect(){}
          @Override public InputStream getInputStream(){
            return new ByteArrayInputStream(java.getClassFile(url.getPath()).get().getBytes());
            }
          };
        }
      };
    }
  @Override
  public URL getResource(String name){
    String javaName = name.replace(".class","").replace(File.separatorChar/*'/'*/, '.').replace('/', '.');
    //NOTE: probably there is a bug in safeNativeCode, where '/' is used instead of File.separatorChar
    //If this isn't a class from this compiler, hand off to the parent
    if (!this.java.hasFile(javaName)){return super.getResource(name);}
    //Return a new url, that returns our file for its input stream.
    try {return new URL(null, "string:" + javaName, forcedUrlResolution());}
    catch(MalformedURLException e){throw new Error(e);}
    }
  }
