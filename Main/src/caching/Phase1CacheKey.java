package caching;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import facade.L42;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;

/**
 
 file ff.P42
containes
cacheKey:
  srcFileString,  //for example This.L42 if is a folder
  map<url,srcFileString>//loaded while caching
  map<deepFileName,srcFileString>//readed while caching
cacheData:
  serialized 42core ast+L42.usedNames and similar
1-read entry file
2-read entry file cache
3-load maps with mentioned files in cache
4-make a new cache key
5-if identical to cacheKey, use cacheData
6-if different, 
compute up to top execute()
make new cache with the typed program there.
 
 * */

public class Phase1CacheKey implements Serializable{
  @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + ((fileNameToLib == null) ? 0 : fileNameToLib.hashCode());
    result = prime * result + ((urlToLib == null) ? 0 : urlToLib.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Phase1CacheKey other = (Phase1CacheKey) obj;
    if (fileName == null) {
    if (other.fileName != null)
        return false;
    } else if (!fileName.equals(other.fileName))
        return false;
    if (fileNameToLib == null) {
    if (other.fileNameToLib != null)
        return false;
    } else if (!fileNameToLib.equals(other.fileNameToLib))
        return false;
    if (urlToLib == null) {
    if (other.urlToLib != null)
        return false;
    } else if (!urlToLib.equals(other.urlToLib))
        return false;
    return true;
    }
private static final long serialVersionUID = 1L;
  String fileName;//the starting point
  Map<String,String> urlToLib=new HashMap<>();
  Map<Path,String>fileNameToLib=new HashMap<>();//contains fileName->??
  public static String _contentOrNull(Path fn){
    try{return L42._pathToString(fn);}
    catch (IOException e) {return null;}
  }
  public Phase1CacheKey current(){
    Phase1CacheKey res=new Phase1CacheKey();
    res.fileName=this.fileName;
    for(String url:urlToLib.keySet()){
      Path fn = L42.path.resolve(fileName);
      String fnContent=_contentOrNull(fn);
      res.urlToLib.put(url,fnContent);
      }
    for(Path path:fileNameToLib.keySet()){
      Path fn = L42.path.resolve(path);
      String fnContent=_contentOrNull(fn);
      res.fileNameToLib.put(path,fnContent);
      }   
    return res;
    }
  public void saveOnFile(Path file){
    try (
      OutputStream os = Files.newOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(os);
      )
      {out.writeObject(this);}
    catch(IOException i) {throw new Error(i);}
    }
  public static Phase1CacheKey readFromFile(Path file){
    try (
      InputStream is = Files.newInputStream(file);
      ObjectInputStream in = new ObjectInputStream(is);
      ){
      Object res = in.readObject();
      Phase1CacheKey cache=(Phase1CacheKey)res;
      return cache;
     }
    catch(IOException i) {throw new Error(i);}
    catch (ClassNotFoundException e) {throw new Error(e);}
    catch (ClassCastException e) {throw new Error(e);}//means file corrupted?
    }
  }