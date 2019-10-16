package is.L42.constraints;

import java.util.HashSet;
import java.util.Set;

import is.L42.generated.X;

public class FreshNames {
  int current=0;
  Set<Integer> used=new HashSet<>();
  public void addToUsed(X x){
    String i=x.inner();
    if(!i.startsWith("fresh")){return;}
    int u=i.indexOf("_");
    if(u==-1){return;}
    i=i.substring(5,u);//"fresh".length()
    used.add(Integer.parseInt(i));
    }
  public String fresh(String hint){
    while(used.contains(current)){current+=1;}
    String res="fresh"+current+"_"+hint;
    current+=1;
    return res;
    
    }
}
