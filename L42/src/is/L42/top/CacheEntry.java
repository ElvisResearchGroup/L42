package is.L42.top;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Full.L.NC;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
public class CacheEntry implements Serializable{
  public CacheEntry(
    NC _key,CTz ctz,Program p,HashMap<String,ClassFile> _mByteCode,HashMap<String,ClassFile> cByteCode
    ){this._key=_key;this.ctz=ctz;this.p=p;this._mByteCode=_mByteCode;this.cByteCode=cByteCode;}
  Full.L.NC _key;
  CTz ctz;
  Program p;
  HashMap<String,ClassFile> _mByteCode;
  HashMap<String,ClassFile> cByteCode;
  
  @SuppressWarnings("unchecked")
  public static ArrayList<CacheEntry> loadCache(Path path){
    try(
      var file=new FileInputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectInputStream(file);
      ){return (ArrayList<CacheEntry>)out.readObject();}
    catch(FileNotFoundException e){return new ArrayList<>();}
    catch(ClassNotFoundException e){throw unreachable();}
    catch(IOException e){
      e.printStackTrace();
      throw todo();
      }
    }
  public static void saveCache(Path path,ArrayList<CacheEntry> cache){
    try(
      var file=new FileOutputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectOutputStream(file);
      ){out.writeObject(cache);}
    catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {
      e.printStackTrace();
      throw todo();
      }
    }
  }
