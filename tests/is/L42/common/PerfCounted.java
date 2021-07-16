package is.L42.common;

import org.junit.jupiter.api.AfterAll;

import is.L42.perftests.PerfCounters;

public class PerfCounted {
  
  static {
    PerfCounters.setEnabled(false);
    }
  @AfterAll
  public static void resetAndPrintCounters() {
    if(PerfCounters.isEnabled()) {
      PerfCounters.printAll();
      PerfCounters.resetAll();
      }
    }

}
