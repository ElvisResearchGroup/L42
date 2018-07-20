package repl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import facade.L42;
import helpers.TestHelper;
import profiling.Timer;

/*
Issues:
-caching seams to run old code something:
  pattern: write c1, run (c1), modify c1=c2, run (c2), run (c1)
-cut paste on win do nothing

Improvements:
-autosave when run
-doc dissapper if "." on invalid prefix
-more clear sign if "error happens"
-shorter visualization for method signatures
-sorting for method signatures
-clear console option
*/


public class ReplTest {
public static void main(String[]arg)throws Throwable{
  ClassLoader.getSystemClassLoader()
  .setDefaultAssertionStatus(true);
  Timer.activate("TOP");
  ReplMain.main(arg);
  }
/*@Test
public void test() throws Throwable {
  ReplGui.main(null);
  this.wait(10000);
  }*/

}
