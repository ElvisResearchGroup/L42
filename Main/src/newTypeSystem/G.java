package newTypeSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ExpCore;
import ast.Ast.Type;
import ast.ExpCore.Block.Dec;
import programReduction.Program;

public interface G{
  G add(Program p,List<ExpCore.Block.Dec> ds);
  G addTx(String x,Type t);
  Type _g(String x);
  Set<String>dom();
  
  public static G of(Map<String, Type> varEnv){return new G(){
     public Set<String> dom(){return varEnv.keySet();}
     public Type _g(String x){return varEnv.get(x);}
     
     public G add(Program p,List<ExpCore.Block.Dec> ds){
       Map<String, Type> varEnv2=new HashMap<>(varEnv);
       for(Dec d:ds){
         if( d.get_t()!=null){varEnv2.put(d.getX(),d.get_t());}
         }
       return of(varEnv2);
       }

     public G addTx(String x,Type t){
       Map<String, Type> varEnv2=new HashMap<>(varEnv);
       varEnv2.put(x,t);
       return of(varEnv2);
       }
     };
   }
  }