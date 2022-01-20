package is.L42.common;

import static is.L42.tools.General.unreachable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.opentest4j.AssertionFailedError;


public class Err {
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
  public static boolean strCmp(String actual, String expected){
    actual = actual.trim();
    expected = expected.trim();
    try{assertTrue(ErrMsg.strCmpAux(actual, expected,Err.hole));}
    catch(AssertionFailedError e){
      assertEquals(expected,actual);
      throw unreachable();
      }
    return true;
    }  
 }
