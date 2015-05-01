package typeSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ast.Ast.Mdf;
import ast.Ast.NormType;
import auxiliaryGrammar.Functions;

public class SealEnv {
  public HashSet<String> xs;
  public HashSet<HashSet<String>> xss;
  public HashSet<HashSet<String>> xssK;
  public static SealEnv empty(){return instance;}
  private static SealEnv instance=new SealEnv();
  static{
    instance.xs=new HashSet<String>();
    instance.xss=new HashSet<HashSet<String>>();
    instance.xssK=new HashSet<HashSet<String>>();
  }
  public SealEnv(SealEnv other){
    this();
    xs=new HashSet<String>(other.xs);
    xss=new HashSet<HashSet<String>>(other.xss);
    xssK=new HashSet<HashSet<String>>(other.xssK);
  }
  public static SealEnv addLentSingletons(HashMap<String, NormType> varEnv2,SealEnv sealEnv2) {
    SealEnv result=new SealEnv(sealEnv2);
    for(String s: varEnv2.keySet()){
      if (varEnv2.get(s).getMdf()!=Mdf.Lent){continue;}
      HashSet<String> newSingleton=new  HashSet<String>();
      newSingleton.add(s);
      result.xss.add(newSingleton);
    }
    return result;
  }

  private SealEnv(){};
  
  public boolean xInXs(String x){return xs.contains(x);}
  public boolean xInXss(String x){
    for(HashSet<String> xsi:xss){
      if(xsi.contains(x)){return true;}
      }
    return false;
    }
  public boolean xInXssK(String x){
    for(HashSet<String> xsi:xss){
      if(xsi.contains(x)){return true;}
      }
    return false;
    }

  public void addLayer(HashMap<String, NormType> varEnv) {
    HashSet<String> newLayer=new HashSet<String>();
    for(String s:varEnv.keySet()){
      if (varEnv.get(s).getMdf()!=Mdf.Mutable){continue;}
      if (xInXss(s)){continue;}
      newLayer.add(s);
    }
    this.xss.add(newLayer);
    
  }
  public void addStrongLock(HashMap<String, NormType> varEnv) {
    for(String s:varEnv.keySet()){
      Mdf mdf=varEnv.get(s).getMdf();
      if (Functions.isSuperTypeOfMut(mdf)){this.xs.add(s);}
    }    
  }
  public void swapLayer(String x,HashMap<String, NormType> varEnv) {
    addLayer(varEnv);
    for(HashSet<String> xsi:xss){
      if(xsi.contains(x)){
        xss.remove(xsi);break;
      }
    }
  }
  public void addToAllXxssK(Set<String> fvToAdd) {
    for(Set<String> xs :this.xssK){xs.addAll(fvToAdd);}    
  }
}
