package is.L42.cache;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import is.L42.nativeCode.TrustedKind;

public abstract class KeyFormatter<T> {
  KeyNorm2D k;
  abstract T cacheKind(L42Cache<?> c);
  abstract String specialS(T t,int lineN);
  abstract String[] stringParts(boolean isInterface,T t);
  abstract boolean[] interfaceFields(T t);
  abstract String varName(T t);
  String format(KeyNorm2D k){
    this.k=k;
    var res=new HashMap<Integer,String>();
    T topT=cacheKind((L42Cache<?>)k.lines()[0][0]);
    String special=specialS(topT, 0);
    if(special!=null){return special;}
    String first=format(topT,0,true,0,new HashSet<>(),res);
    if(res.isEmpty()){return first;}
    first+="\n"+res.entrySet().stream()
      .sorted((e1,e2)->e1.getKey()-e2.getKey())
      .map(e->e.getValue())
      .collect(Collectors.joining("\n"));
    if(res.keySet().contains(0)){
      T t=cacheKind((L42Cache<?>)k.lines()[0][0]);
      first=varName(t)+"0="+first;
      }
    return first;
    }
  String formatDispatch(Object o, boolean flag,int size,HashSet<Integer>added,HashMap<Integer,String>expanded){
    if(o instanceof KeyVarID){
      int id=((KeyVarID)o).value();
      T topT=cacheKind((L42Cache<?>)k.lines()[id][0]);
      String special=specialS(topT, id);
      if(special!=null){return special;}
      String name=varName(topT)+id;
      if(added.contains(id)){return name;}
      if(size<50){return format(topT,id,flag,size,added,expanded);}
      added.add(id);
      expanded.put(id,name+"="+format(topT,id,flag,size,added,expanded));
      return name;
      }
    if(o instanceof ArrayList){throw todo();}
    return o.toString();
    }
  String format(T topT,int lineN, boolean flag,int size,HashSet<Integer>added,HashMap<Integer,String>expanded){
    assert k.lines().length>lineN;
    //added can be bigger then expanded.dom(), since we first notify add, then we compute, then we put in expanded
    boolean[] flags=interfaceFields(topT);
    String[] splits=stringParts(flag,topT);
    assert flags!=null;
    assert splits!=null;
    for(var s:splits){size+=s.length();}
    int newSize=splits.length*5+size;
    assert flags.length+1==splits.length;
    List<String> parts=L(range(flags.length), (c,i)->
      c.add(formatDispatch(k.lines()[lineN][i+1],flags[i],newSize,added,expanded)));
    String res=splits[0];
    for(int i:range(1,splits.length)){res+=parts.get(i-1)+splits[i];}
    return res;
    }
}
