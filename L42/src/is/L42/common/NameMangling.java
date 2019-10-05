package is.L42.common;

import static is.L42.tools.General.L;

import is.L42.generated.Op;
import is.L42.generated.S;

public class NameMangling {
  private static final S hashApply=new S("#apply",L(),-1);
  public static S hashApply(){return hashApply;}
  public static S keyOf(Op _op,int n,S s){
    if(_op==null){return s;}
    return s.withM(methName(_op,n));
    }
  public static String methName(Op op,int n){
    if(n==-1){n=0;}
    return "#"+op.name().toLowerCase()+n;
    }
}
