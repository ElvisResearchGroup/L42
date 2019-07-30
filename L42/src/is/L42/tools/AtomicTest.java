package is.L42.tools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AtomicTest{
public static class Tester {
  @MethodSource
  @ParameterizedTest(name = "{index}: {0}")
  public void test(AtomicTest input) {input.run();}
  }
int lineNumber=lineNumber();
Runnable r; 
public AtomicTest(Runnable r){this.r=r;}
public String toString() {return "line "+lineNumber;}
public void run() {r.run();}
public static int lineNumber() {
  return Thread.currentThread().getStackTrace()[4].getLineNumber();
  }
}