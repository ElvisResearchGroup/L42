package is.L42.common;

import static is.L42.tools.General.L;

import is.L42.generated.Op;
import is.L42.generated.S;

public class NameMangling {
  private static final S hashApply=new S("#apply",L(),-1);
  public static S hashApply(){return hashApply;}
  public static S keyOf(Op _op,int n,S s){
    if(_op==null && !s.m().isEmpty()){return s;}
    if(_op==null && s.m().isEmpty()){return hashApply.withXs(s.xs());}
    return s.withM(methName(_op,n));
    }
  public static String methName(Op op,int n){
    if(n==-1){n=0;}
    return "#"+op.name().toLowerCase()+n;
    }
  public static S shortCircuit(Op op){
    return new S("#shortCircut"+op.name().toLowerCase(),L(),-1);
    }
  public static S shortResult(Op op){
    return new S("#shortResult"+op.name().toLowerCase(),L(),-1);
    }
  public static S shortProcess(Op op){
    return new S("#shortProcess"+op.name().toLowerCase(),L(),-1);
    }

}
