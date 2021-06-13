package is.L42.tests;

import org.junit.jupiter.api.Test;

import is.L42.common.Balance;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;

public class TestBalance{
  public static void pass(String input){
    Balance.checkForBalancedParenthesis(Constants.dummy.toUri(),input);
    }
  public static void fail(String input,String output){
    try{Balance.checkForBalancedParenthesis(Constants.dummy.toUri(),input);}
    catch(EndError.NotWellFormed nwf){
      Err.strCmp(nwf.getMessage(),output);
      }
    }
  @Test void bal1(){pass("foo");}
  @Test void bal2(){pass("(foo)");}
  @Test void bal3(){pass("{foo S\"dd((dd\"ff}f");}
  @Test void bal4(){pass("{foo S\"dd)]}dd\"ff}f");}
  @Test void bal4a(){pass("{foo S\"\"ff}f");}
  @Test void bal5(){pass("""
    {foo S\"""
      |dd)]}dd
      \"""ff}f
    """);}
  @Test void bal6(){fail("""
      {foo S\"""
        dd)]}dd
        \"""ff}f
      ""","[###]line 2[###]indentation[###]");}
  @Test void bal7(){fail("""
      {foo S\"""
        |ffdf
        dd)]}dd
        \"""ff}f
      ""","[###]line 3[###]indentation[###]");}
  @Test void bal8(){pass("""
      foo (a,b S"" c){
       ee[]
        d
         }      
      """);}
  @Test void bal9(){fail("""
      foo a,b S"" c){
       ee[]
        d
         }      
      ""","[###]line 1[###]Unopened[###])[###]");}
  @Test void bal10(){fail("""
      foo (a,b S"" c{
       ee[]
        d
         }      
      ""","[###]line 1[###]Unclosed[###]([###]");}
  @Test void bal11(){fail("""
      foo (a,b S"" c){
       ee)[]
        d
         }      
      ""","[###]line 1[###]line 2[###]mismatch[###]{[###])[###]");}
  @Test void bal13(){fail("""
      foo (a,b S"" c){
       ee[]
        d
         }}      
      ""","[###]line 4[###]Unopened[###]}[###]");}
  }