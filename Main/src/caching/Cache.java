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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ast.L42F;
import ast.L42F.CD;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import tools.Assertions;


public class Cache implements Serializable{
  private static final long serialVersionUID = 1L;
  public static class Element implements Serializable{
    private static final long serialVersionUID = 1L;
    public Element(Map<Integer,L42F.CD> cds, Set<String> byteCodeNames) {
      this.cds = cds;
      this.byteCodeNames = byteCodeNames;
      }
    Map<Integer,L42F.CD>cds;
    Set<String> byteCodeNames;
    }
  transient HashMap<String, ClassFile> fullMap=new HashMap<>();
  HashMap<Set<String>,Element>inner=new HashMap<>();
  HashMap<String,SClassFile> smap=new HashMap<>();//contains values when saving/loading
  public Element get(Set<String> key){
    Element res = inner.get(key);
    return res;
    }
  public void add(Set<String> dep,Map<Integer,L42F.CD>cds,HashMap<String, ClassFile> clMap){
    //if cache(dep0).clMap(byteName)=byteCode
    //cache(dep1).clMap(byteName)=byteCode'
    HashSet<String> clSet=new HashSet<>(clMap.keySet());
    Element old = inner.get(dep);
    if(old!=null){
      System.out.println("Overriding cache for "+dep);
      HashMap<Set<String>,Element> oldCache=new HashMap<>(inner);
      inner.clear();
      HashMap<String, ClassFile> oldFullMap=new HashMap<>(fullMap);
      fullMap.clear();
      for(Entry<Set<String>, Element> e:oldCache.entrySet()){
        //if e.key superset of dep
        if(dep.containsAll(e.getKey())){continue;}
        inner.put(e.getKey(), e.getValue());
        for(String byteCodeName:e.getValue().byteCodeNames){
          fullMap.put(byteCodeName,oldFullMap.get(byteCodeName));
          }
        }
      }
    //was not already in cache
    assert !clSet.stream().anyMatch(s->fullMap.containsKey(s));
    fullMap.putAll(clMap);
    inner.put(dep, new Element(cds,clSet));
    }

  /*
   //TODO: have I handled this already? 
    NO,
   class loader is NOT saved. class loader is "loaded"
   from cache if is required:
   -at all times full map >=cl.map
   -at all times smap= translation of map
   -elements just store set of strings.(not maps)
   -loading require using fullMap+element

   * */
  public void saveOnFile(Path file){
    for(String s: fullMap.keySet()) {
      if(smap.containsKey(s)) {continue;}
      smap.put(s,SClassFile.fromCF(fullMap.get(s)));
      }
    try (
      OutputStream os = Files.newOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(os);
      )
      {out.writeObject(this);}
      catch(IOException i) {throw new InvalidCacheFile(i);}
    }
  public static Cache readFromFile(Path file){
    try (
      InputStream is = Files.newInputStream(file);
      ObjectInputStream in = new ObjectInputStream(is);
    ){
      Object res = in.readObject();
      Cache cache=(Cache)res;
      cache.fullMap=new HashMap<>();
      for(String s:cache.smap.keySet()) {
          cache.fullMap.put(s,cache.smap.get(s).toCF());
        }
      return cache;
      }
    catch(IOException i) {throw new InvalidCacheFile(i);}
    catch (ClassNotFoundException e) {throw new InvalidCacheFile(e);}
    catch (ClassCastException e) {throw new InvalidCacheFile(e);}//means file corrupted?
    }

  }
