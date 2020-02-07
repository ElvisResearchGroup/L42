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
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Full.L.NC;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;
public class CacheEntry implements Serializable{
  public CacheEntry(NC _key, 
    CTz frommedCTz,Program p,
    List<L42£Library> _mLibs,List<L42£Library> cLibs,int sizeWithMLibs,int sizeWithCLibs,
    HashMap<String,SClassFile> _mByteCode,HashMap<String,SClassFile> cByteCode
    ){
    this._key=_key;
    this.frommedCTz=frommedCTz;this.p=p;
    this._mLibs=_mLibs;this.cLibs=cLibs;this.sizeWithMLibs=sizeWithMLibs;this.sizeWithCLibs=sizeWithCLibs;
    this._mByteCode=_mByteCode;this.cByteCode=cByteCode;}
  Full.L.NC _key;
  CTz frommedCTz;
  //TODO:Can not cache ctz: it is very big AND mutable.
  //This would make it quadratic
  Program p;
  HashMap<String,SClassFile> _mByteCode;
  HashMap<String,SClassFile> cByteCode;
  List<L42£Library> _mLibs;
  List<L42£Library> cLibs;
  int sizeWithMLibs;
  int sizeWithCLibs;  
  @SuppressWarnings("unchecked")
  public static ArrayList<CacheEntry> loadCache(Path path){
    try(
      var file=new FileInputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectInputStream(file);
      ){return (ArrayList<CacheEntry>)out.readObject();}
    catch(FileNotFoundException e){return new ArrayList<>();}
    catch(ClassNotFoundException e){throw unreachable();}
    catch(IOException e){
      throw new Error(e);
      }
    }
  public static void saveCache(Path path,ArrayList<CacheEntry> cache){
    try(
      var file=new FileOutputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectOutputStream(file);
      ){out.writeObject(cache);}
    catch (FileNotFoundException e) {throw new Error(e);}
    catch (IOException e) {
      e.printStackTrace();
      throw todo();
      }
    }
  }
