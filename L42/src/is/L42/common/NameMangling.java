package is.L42.common;

import is.L42.generated.Op;
import is.L42.generated.S;

public class NameMangling {
  public static S keyOf(Op _op,int n,S s){
    if(_op==null){return s;}
    return s.withM(methName(_op,n));
    }
  public static String methName(Op op,int n){
    return "#"+op.name()+"n";
    }
}
