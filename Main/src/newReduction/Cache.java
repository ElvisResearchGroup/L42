package newReduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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
    public Element(List<CD> cds, Set<String> byteCodeNames) {
      this.cds = cds;
      this.byteCodeNames = byteCodeNames;
      }
    List<L42F.CD>cds;
    Set<String> byteCodeNames;
    }
  HashMap<String, ClassFile> fullMap=new HashMap<>();
  HashMap<Set<String>,Element>inner=new HashMap<>();
  HashMap<String,SClassFile> smap=null;//contains values when saving/loading
  public Element get(Set<String> key){
    Element res = inner.get(key);
    return res;
    }
  public void add(Set<String> dep,List<L42F.CD>cds,HashMap<String, ClassFile> clMap){
    //if cache(dep0).clMap(byteName)=byteCode
    //cache(dep1).clMap(byteName)=byteCode'
    Element old = inner.get(dep);
    if(old!=null){
      assert old.clMap.equals(clMap);
      return;
      }
    HashMap<String, ClassFile> clMapNoRep=new HashMap<>(clMap);
    for(String s:clMap.keySet()){
      ClassFile cf=fullMap.get(s);
      if(cf!=null){//already there
        clMapNoRep.put(s, cf);
        }
      else{//new classfile
        fullMap.put(s, clMap.get(s));
        }
      }
    inner.put(dep, new Element(cds,clMapNoRep));
    }

  /**
   NO, 
   class loader is NOT saved. class loader is "loaded"
   from cache if is required:
   -at all times full map >=cl.map
   -at all times smap= translation of map
   -elements just store set of strings.(not maps)
   -loading require using fullMap+element
   
   * */
  public void saveOnFile(Path file,MapClassLoader cl){
    this.smap = cl.exportMap();
    try (
      OutputStream os = Files.newOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(os);
      )
      {out.writeObject(this);}
      catch(IOException i) {throw new Error(i);}
    }
  public static Cache readFromFile(Path file){
    try (
      InputStream is = Files.newInputStream(file);
      ObjectInputStream in = new ObjectInputStream(is);
    ){
      Object res = in.readObject();
      return (Cache)res;
      }
    catch(IOException i) {throw new Error(i);}
    catch (ClassNotFoundException e) {throw new Error(e);}
    catch (ClassCastException e) {throw new Error(e);}//means file corrupted?
    }

  }
