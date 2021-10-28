package is.L42.common;

import static is.L42.tools.General.unreachable;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.flyweight.C;



public class Err {
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
  public static String pathToS(Object o){
    if (!(o instanceof List<?>ls)){ return o.toString(); }
    if(ls.isEmpty()){ return o.toString(); }
    if(!(ls.get(0) instanceof C)){ return o.toString();}
    return ls.stream().map(c->c.toString()).collect(Collectors.joining("."));
    }
  public static boolean strCmp(String cmp1, String cmp2){
    if (!ErrMsg.strCmpAux(cmp1.trim(), cmp2.trim(),Err.hole)){
      if (!cmp2.equals(cmp1)){ throw new AssertionError(String.format("\"%s\" does not equal \"%s\"", cmp2, cmp1)); }
      throw unreachable();
      }
    return true;
    }  
 }
