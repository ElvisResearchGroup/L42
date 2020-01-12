package is.L42.generated;

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
import java.util.Set;
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42Library;
import lombok.Data;
import lombok.AllArgsConstructor;
@Data @AllArgsConstructor public class Cache implements Serializable{
  ArrayList<L42Library> allLibs;
  ArrayList<SClassFile> allByteCode;
  CTop _top;
  @Data @AllArgsConstructor public static class CTop implements Serializable{
    InOut in;
    InOut out;
    ArrayList<CTopNC1>ncs;
    boolean hasHDDeep;
    Core.L sortedHeader;
    int nHByteCode;
    int nHlibs;
    }
  @Data @AllArgsConstructor public static class CTopNC1 implements Serializable{
    ArrayList<CTop> tops;
    InOut in;
    InOut out;
    boolean hasHDE;
    boolean hasHDL;
    Full.L.NC ncIn;
    Core.E coreE;
    Core.L lOut;
    }
  @Data @AllArgsConstructor public static class InOut implements Serializable{
    int nByteCode;
    int nLibs;
    CTz ctz;
    List<Set<List<C>>> coherentList;
    Program p;
    }
  public static Cache loadCache(Path path){
    try(
      var file=new FileInputStream(path.resolve("cache.L42Bytes").toFile()); 
      var out=new ObjectInputStream(file);
      ){return (Cache)out.readObject();}
    catch(FileNotFoundException e){return new Cache(new ArrayList<>(),new ArrayList<>(),null);}
    catch(ClassNotFoundException e){throw unreachable();}
    catch(IOException e){throw new Error(e);}
    }
  public static void saveCache(Path path,Cache cache){
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