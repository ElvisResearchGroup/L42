package is.L42.common;

import static is.L42.tools.General.unreachable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.opentest4j.AssertionFailedError;


public class Err {
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
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
