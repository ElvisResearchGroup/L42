package is.L42.perftests;

import java.util.ArrayList;
import java.util.List;

import is.L42.top.CachedTop;

public class PerfMonitor {
  private final MemoryMonitor mem;
  private long starttime;
  public PerfMonitor(boolean longTime) {
    this.mem = new MemoryMonitor(longTime ? 50 : 5);
    }
  public void startMonitoring() {
    mem.startMonitoring();
    this.starttime = System.nanoTime();
    }
  public PerfStat stopMonitoring() {
    long totalTime = System.nanoTime() - this.starttime;
    mem.stopMonitoring();
    return new PerfStat(mem.getPeak(), mem.getAverage(), totalTime);
    }
  public static PerfResult test(NoExecSupplier<CachedTop> sup, int times, boolean longTime) {
    PerfMonitor mon = new PerfMonitor(longTime);
    List<PerfStat> res = new ArrayList<>();
    //Save the CachedTop only once as these are large and consume much memory
    CachedTop top = null;
    for(int i = 0; i < times; i++) {
      mon.startMonitoring();
      top = sup.get();
      res.add(mon.stopMonitoring());
      }
    return new PerfResult(PerfStat.average(res),top);
    }
  public static record PerfResult(PerfStat stat, CachedTop cache) {}
  }