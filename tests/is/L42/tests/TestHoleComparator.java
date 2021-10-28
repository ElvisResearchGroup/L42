package is.L42.tests;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import is.L42.common.Err;
import is.L42.tools.AtomicTest;

public class TestHoleComparator extends AtomicTest.Tester
{  
  public static Stream<AtomicTest> test() {
    return Stream.of(
        cmp("teststring123", "", false),
        cmp("", "", true),
        cmp("A", "A", true),
        cmp("A", "", false),
        cmp("", "A", false),
        cmp("B", "A", false),
        cmp("", "  ", true),//because trimming
        cmp("teststring123", Err.hole, true),
        cmp("teststring123", "teststring123", true),
        cmp("teststring123", "test" + Err.hole + "123", true),
        cmp("teststring123", "teststring" + Err.hole , true),
        cmp("teststring123", Err.hole + "string123", true),
        cmp("teststring123", Err.hole + "string" + Err.hole, true),
        
        cmp("teststring123", Err.hole + "teststring1234" + Err.hole, false),
        cmp("basteststring123", "123stringtestbad", false),
        cmp("123stringtestbaddd", "123string" + Err.hole + "bad", false),
        
        cmp("1234QQABCD", "1234" + Err.hole + "AB" + Err.hole, true),
        cmp("1234QQABCDQQEFGH", "1234" + Err.hole + "ABCD" + Err.hole + "EFGH", true),
        cmp("1234QQABCDQQEFGH", Err.hole + "1234" + Err.hole + "ABCD" + Err.hole + "EFGH" + Err.hole, true),
        cmp("1234ABCDEFGH", "1234" + Err.hole + "ABCD" + Err.hole + "EFGH", true),
        cmp("1234ABCDEFGH", "1" + Err.hole + "D" + Err.hole + "H", true),
        
        cmp("1234ABCDEFGH", Err.hole + "1" + Err.hole + "3" + Err.hole + "C" + Err.hole + "H" + Err.hole, true),
        cmp("1234ABCDEFGH", Err.hole + "1" + Err.hole + "3" + Err.hole + "H" + Err.hole + "H" + Err.hole, false),
        cmp("1234ABCDEFGH", Err.hole + "1" + Err.hole + "3" + Err.hole + "N" + Err.hole + "H" + Err.hole, false),
        cmp("1234ABCDEFGH", Err.hole + "1" + Err.hole + "C" + Err.hole + "3" + Err.hole + "H" + Err.hole, false),
        
        cmp("1234QQABCDQQEFGH", Err.hole + "1234A" + Err.hole + "ABCD" + Err.hole + "EFGH" + Err.hole, false),
        cmp("1234QQABCDQQEFGH", Err.hole + "1234" + Err.hole + "ABCD" + Err.hole + "ENGH" + Err.hole, false),
        cmp("1234QQABCDQQEFGH", Err.hole + "1274" + Err.hole + "ABCD" + Err.hole + "EFGH" + Err.hole, false),
        cmp("1234QQABCDQQEFGH", Err.hole + "1234" + Err.hole + "ABFD" + Err.hole + "EFGH" + Err.hole, false),
        cmp("1234ABCDEFGH", Err.hole + "1234A" + Err.hole + "4ABCD" + Err.hole + "EFGH" + Err.hole, false),
        cmp("1234ABCD", "1234A"+Err.hole+"4ABCD", false)
    );
    }
  
  private static AtomicTest cmp(String cmp1, String cmp2, boolean success) {
    if(success) { return new AtomicTest(() -> { pass(cmp1, cmp2); }); }
    else { return new AtomicTest(() -> { fail(cmp1, cmp2); }); }
    }

  private static void pass(String cmp1, String cmp2) {
    assertTrue(Err.strCmp(cmp1, cmp2));
    }
  
  private static void fail(String cmp1, String cmp2) {
    assertThrows(AssertionError.class, () -> { Err.strCmp(cmp1, cmp2); });
    }
  
}