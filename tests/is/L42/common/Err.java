package is.L42.common;

import static is.L42.tools.General.unreachable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.opentest4j.AssertionFailedError;

import is.L42.generated.C;



public class Err {
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
  public static String pathToS(Object o){
    if (!(o instanceof List<?>ls)){ return o.toString(); }
    if(ls.isEmpty()){ return o.toString(); }
    if(!(ls.get(0) instanceof C)){ return o.toString();}
    return ls.stream().map(c->c.toString()).collect(Collectors.joining("."));
    }
  public static boolean strCmp(String cmp1, String cmp2){
    cmp1 = cmp1.trim();
    cmp2 = cmp2.trim();
    try{assertTrue(ErrMsg.strCmpAux(cmp1, cmp2,Err.hole));}
    catch(AssertionFailedError e){
      assertEquals(cmp2,cmp1);
      throw unreachable();
      }
    return true;
    }  
 }
