package is.L42.constraints;

import java.util.HashSet;
import java.util.Set;

public class FreshNames {
  int current=0;
  Set<Integer> used=new HashSet<>();
  String fresh(String hint){
    while(used.contains(current)){current+=1;}
    String res="fresh"+current+"_"+hint;
    current+=1;
    return res;
    
    }
}
