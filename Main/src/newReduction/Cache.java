package newReduction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.L42F;
import ast.L42F.CD;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import tools.Assertions;


public class Cache {
  public static class Element{
    public Element(List<CD> cds, HashMap<String, ClassFile> clMap) {
      this.cds = cds;
      this.clMap = clMap;
      }
    List<L42F.CD>cds;
    HashMap<String, ClassFile> clMap;
    }
  HashMap<String, ClassFile> fullMap=new HashMap<>();
  HashMap<Set<String>,Element>inner=new HashMap<>();
  public static Cache loadFromFile(){throw Assertions.codeNotReachable();}
  public Element get(Set<String> key){
    Element res = inner.get(key);
    assert res!=null;
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
  }
