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
  FileName,  //for example This.L42 if is a folder
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
    result = prime * result + ((_fileName == null) ? 0 : _fileName.hashCode());
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
    if (_fileName == null) {
    if (other._fileName != null)
        return false;
    } else if (!_fileName.equals(other._fileName))
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
  private String _fileName;//the starting point
  public boolean hasFileName(){return _fileName!=null;}
  public Map<String,String> urlToLib=new HashMap<>();
  public Map<List<String>,String>fileNameToLib=new HashMap<>();//contains fileName->??
  private static Path pathFromList(List<String> l){
    assert !l.isEmpty();
    Path p=Paths.get(l.get(0));
    for(String s:l.subList(1, l.size())){assert s!=null;
      p=p.resolve(s);
      }
    return p;
    }
  public static List<String> listFromPath(Path path){
    List<String> res=new ArrayList<>();
    path=L42.root.relativize(path);
    for(Path si:path){res.add(si.toString());}
    return res;
    }
  public void fileNameToLibPut(Path path, String code){
    fileNameToLib.put(listFromPath(path),code);
    }
  public Path fileName(){
    assert _fileName!=null;
    return pathFromList(Collections.singletonList(_fileName));
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

  /**
   * The "this" cache is the old state of the files. The method current() return the new state of the files.
   *
   * @param _content Is null if when runned from command line, the IDE will force a content for the main file
   * @return
   */
  public Phase1CacheKey current(String _content){
    Phase1CacheKey res=new Phase1CacheKey();
    res._fileName=this._fileName;
    for(String url:urlToLib.keySet()){
      Path fn = Paths.get("localhost",url);
      String fnContent=_contentOrNull(fn);
      res.urlToLib.put(url,fnContent);
      }
    for(List<String> path:fileNameToLib.keySet()){
      if(_content!=null) {
        assert fileNameToLib.keySet().size()==1:
          fileNameToLib.keySet();
        res.fileNameToLib.put(path,_content);
        continue;
      }
      Path fn = L42.root.resolve(pathFromList(path));
      String fnContent=_contentOrNull(fn);
      if(!Files.exists(fn)){throw new Error();}
      res.fileNameToLib.put(path,fnContent);
      }
    return res;
    }
  public void saveOnFile(Path file){
    assert _fileName!=null;
    assert fileNameToLib.get(Collections.singletonList(_fileName))!=null;
    try (
      OutputStream os = Files.newOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(os);
      )
      {out.writeObject(this);}
    catch(IOException i) {throw new InvalidCacheFile(i);}
    }
  public static Phase1CacheKey readFromFile(Path file){
    Timer.activate("ReadCacheKey");
    try (
      InputStream is = Files.newInputStream(file);
      ObjectInputStream in = new ObjectInputStream(is);
      ){
      Object res = in.readObject();
      Phase1CacheKey cache=(Phase1CacheKey)res;
      assert cache._fileName!=null;
      assert cache.fileNameToLib.get(Collections.singletonList(cache._fileName))!=null;
      return cache;
     }
    catch(IOException i) {
      throw new InvalidCacheFile(i);}
    catch (ClassNotFoundException e) {throw new InvalidCacheFile(e);}
    catch (ClassCastException e) {throw new InvalidCacheFile(e);}//means file corrupted?
    finally{Timer.deactivate("ReadCacheKey");}
    }

  /**
   * @param _content Is null if when runned from command line, the IDE will force a content for the main file
   * @return
   */
  public static Program _handleCache(String _content){
    if(L42.cacheK._fileName==null){return null;}
    Path vPath = L42.root.resolve(L42.cacheK.firstSourceName()+".V42");
    Path kPath = L42.root.resolve(L42.cacheK.firstSourceName()+".K42");
    try{
      Phase1CacheKey oldK=Phase1CacheKey.readFromFile(kPath);
      L42.newK=oldK.current(_content);
      assert oldK._fileName.equals(L42.cacheK._fileName);
      assert L42.newK._fileName.equals(L42.cacheK._fileName);
      if(!L42.newK.equals(oldK)){
        return null;}
      }
    catch(InvalidCacheFile e){
      return null;}
    Phase1CacheValue val=Phase1CacheValue.readFromFile(vPath);
    L42.usedNames.clear();
    L42.usedNames.putAll(val.usedNames);
    Program pRes= Program.emptyLibraryProgram(val.uniqueIdStart);
    pRes=pRes.updateTop(val.top);
    return pRes;
    }
  public void setFileName(String fileName, String code) {
    this._fileName=fileName;
    this.fileNameToLib.put(Collections.singletonList(fileName),code);
  }
  }