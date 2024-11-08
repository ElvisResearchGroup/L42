package is.L42.platformSpecific.inMemoryCompiler;

import static is.L42.tools.General.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.generated.Core;
import is.L42.generated.Core.Info;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.translationToJava.Loader;

public abstract class ToJar {
  public static void ofPath(Path jarName,CoreL lib) throws IOException, CompilationError{
    try(var stream=Files.newOutputStream(jarName)){
      new ToJar()
        {OutputStream output(){return stream;}}
        .of(lib);
      stream.flush();
    }
  }
  public static String ofBase64(CoreL lib) throws IOException, CompilationError{
    try(var stream=new ByteArrayOutputStream()){
      new ToJar()
        {OutputStream output(){return stream;}}
        .of(lib);
      stream.flush();
      return Resources.encoder.encodeToString(stream.toByteArray())
        .replace("\r\n","");
    }
  }
  long timeNow=System.currentTimeMillis();
  abstract OutputStream output();
  public void of(CoreL lib) throws IOException, CompilationError {
    var nc=new Core.NC(lib.poss(),L(),C.of("Top",-1), lib);
    var info=Info.empty.withTyped(true);
    lib=new CoreL(lib.poss(),false,L(),L(),L(nc), info, L());
    Program p=Program.flat(lib);
    var files=new JavaCodeStore();
    var libs= new ArrayList<L42£Library>();
    var loader=new Loader();
    loader.loadNow(p, files, libs);
    String name="ExportedMain"; 
    String code="package is.L42.metaGenerated;"
      +"\npublic class "+name
      +"{public static void main(String[]arg){\n"
      +"var stream="+name+".class.getResourceAsStream(\"ResourcesSerialized.L42Bytes\");"
      +"is.L42.platformSpecific.inMemoryCompiler.ResourceSerialized.loadResource(stream);\n"
      +"£cTop.£m£h$main(£cTop.pathInstance);"
      +"\n}}";
    var fileMain=List.of(new SourceFile(Loader.metaPackage+name,code));
    InMemoryJavaCompiler.compile(loader.classLoader,fileMain,files);
    of(files,new ResourceSerialized(p,libs));
    }
  public void of(JavaCodeStore files,ResourceSerialized res) throws IOException {
    var manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    try(
      var target = new JarOutputStream(output(), manifest);
      ){
      res.save(target,timeNow);
      for(var f : files.getAllCompiled().values()) {
        addSingle(target,f.name.replace(".",File.separator)+".class",f.getBytes());
        }
      }
    }
  private void addSingle(JarOutputStream target,String name,byte[] in) throws IOException{
    JarEntry entry = new JarEntry(name.replace("\\", "/"));
    entry.setTime(timeNow);
    target.putNextEntry(entry);
    target.write(in);
    target.closeEntry();
    }
  }