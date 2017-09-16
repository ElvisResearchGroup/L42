package caching;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.ExpCore;
import facade.L42;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import profiling.Timer;
import programReduction.Program;

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
2-oldK=read entry file cache
3-newK=load maps with mentioned files in cache
4-if newK==oldK then cacheKey=newK and p=flatProram.of(cacheVal)
6-if different, cacheKey=empty, normal execution..
compute up to top execute()
make new cacheVal /cacheKey with the typed program there.
 
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
  public String fileName;//the starting point
  public Map<String,String> urlToLib=new HashMap<>();
  public Map<List<String>,String>fileNameToLib=new HashMap<>();//contains fileName->??
  private static Path pathFromList(List<String> l){
    Path p=L42.root;
    for(String s:l){assert s!=null;
      p=p.resolve(s);
      }
    return p;
    }
  private static List<String> listFromPath(Path path){
    List<String> res=new ArrayList<>();
    for(Path si:path){res.add(si.toString());}
    return res;
    }
  public void fileNameToLibPut(Path path, String code){
    fileNameToLib.put(listFromPath(path),code);
    }
  public Path fileName(){
    assert fileName!=null;
    return pathFromList(Collections.singletonList(fileName));
    }
  public String firstSourceName(){
    Path fn=fileName();
    String name=fn.getName(fn.getNameCount()-1).toString();
    assert name.endsWith(".L42");
    return name.substring(0, name.length()-4);  
    }

  public static String _contentOrNull(Path fn){
    try{return L42._pathToString(fn);}
    catch (IOException e) {return null;}
  }
  public Phase1CacheKey current(){
    Phase1CacheKey res=new Phase1CacheKey();
    res.fileName=this.fileName;
    for(String url:urlToLib.keySet()){
      Path fn = Paths.get("localhost",url);
      String fnContent=_contentOrNull(fn);
      res.urlToLib.put(url,fnContent);
      }
    for(List<String> path:fileNameToLib.keySet()){
      Path fn = L42.root.resolve(pathFromList(path));
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
    Timer.activate("ReadCacheKey");
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
    finally{Timer.deactivate("ReadCacheKey");}
    }
  
  public static Program _handleCache(){
    if(L42.cacheK.fileName==null){return null;}
    Path vPath = L42.root.resolve(L42.cacheK.firstSourceName()+".V42");
    Path kPath = L42.root.resolve(L42.cacheK.firstSourceName()+".K42");
    try{
      Phase1CacheKey oldK=Phase1CacheKey.readFromFile(kPath);
      Phase1CacheKey newK=oldK.current();
      L42.newK=newK;
      assert oldK.fileName.equals(L42.cacheK.fileName);
      assert newK.fileName.equals(L42.cacheK.fileName);
      if(!newK.equals(oldK)){
        return null;}
      }
    catch(Error e){
      return null;}
    Phase1CacheValue val=Phase1CacheValue.readFromFile(vPath);
    L42.usedNames.clear();
    L42.usedNames.putAll(val.usedNames);
    Program pRes= Program.emptyLibraryProgram(val.uniqueIdStart);
    pRes=pRes.updateTop(val.top);
    return pRes;
    }
  }