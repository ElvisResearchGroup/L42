package is.L42.platformSpecific.inMemoryCompiler;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;

public class ResourceSerialized implements Serializable{
  public static final String fileName = 
    "is/L42/metaGenerated/ResourcesSerialized.L42Bytes";
  //Note: inside Jar File.pathSeparator is WRONG, needs the '/'
  Program p;
  ArrayList<L42£Library> libs;
  ResourceSerialized(Program p,ArrayList<L42£Library> libs){this.p=p;this.libs=libs;}
  public static void loadResource(InputStream in){
    ResourceSerialized res=load(in);
    Resources.setLibsCached(res.p,new C("Top",-1),res.libs);
    }
  public void save(JarOutputStream target,long time) throws IOException{
    JarEntry entry = new JarEntry(fileName);
    entry.setTime(time);
    target.putNextEntry(entry);
    var out=new ObjectOutputStream(target);
    out.writeObject(this);
    out.flush();
    target.closeEntry();
    }
  public static ResourceSerialized load(InputStream readerIs){
    try(var reader=new ObjectInputStream(readerIs)){
      return (ResourceSerialized)reader.readObject();
      }
    catch(FileNotFoundException e){throw new Error(e);}
    catch(ClassNotFoundException e){throw new Error(e);}
    catch(java.io.InvalidClassException e){throw new Error(e);}
    catch(java.io.EOFException e){throw new Error(e);}
    catch(IOException e){throw new Error(e);}
    }
  }