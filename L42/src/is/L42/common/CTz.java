package is.L42.common;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static is.L42.tools.General.L;
import static is.L42.tools.General.popL;

import java.util.ArrayList;
import java.util.HashMap;
import is.L42.generated.ST;


public class CTz {
  private final Map<ST,ArrayList<ST>> inner=new HashMap<>();
  boolean coherent(){
    for(var e:inner.entrySet()){
      ST st=e.getKey();
      List<ST> stz=e.getValue();
      assert stz.contains(st);
      for(ST st1:stz){
        assert stz.containsAll(inner.get(st1));
        }
      }
      return true;
    }
  void minimize(Program p,ArrayList<ST>stz){

    }
    
  void plusAcc(Program p,ArrayList<ST> stz,ArrayList<ST>stz1){
    assert coherent();
    while(!stz.isEmpty()){
      var st=stz.get(0);
      stz.remove(0);
      plusAcc(p,st,stz1);
      minimize(p,stz);
      minimize(p,stz1);
      }
    assert coherent();
    }
  void plusAcc(Program p,ST st,List<ST>stz){
    for(var stzi:inner.values()){
       if(!stzi.contains(st)){continue;}
       for(ST stj: stz){if(!stzi.contains(stj)){stzi.add(stj);}}
       minimize(p,stzi);
       }
    }
  Set<ST> dom(){return inner.keySet();}
  List<ST> of(ST st){return L(inner.get(st).stream());}
  List<ST> of(List<ST>stz){return L(stz,(c,sti)->c.addAll(of(sti)));}
  }
