package is.L42.perftests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Claire
 * 
 * For rudimentary profiling tests... monitors heap size.
 */
public class MemoryMonitor {
  public final List<Long> observations = new ArrayList<>();
  public final long interval;
  public final long max;
  private volatile boolean monitoring = false;
  private MonitoringThread thread;
  public MemoryMonitor(long intervalMillis) {
    this.interval = intervalMillis;
    this.max = Runtime.getRuntime().maxMemory();
    }
  public void startMonitoring() {
    this.observations.clear();
    this.thread = new MonitoringThread();
    this.monitoring = true;
    this.thread.start();
    }
  public void stopMonitoring() {
    this.monitoring = false;
    //Assuming we start waiting at the worst possible time, as in, just after the while loop condition runs,
    //we should have to wait at least 2 intervals for the thread to stop.
    int waits = 0;
    while(!this.thread.shutdown && waits < 2) {
      this.waitForInterval();
      }  
    if(!this.thread.shutdown) {
      System.err.println("Monitoring thread " + thread.toString() + " did not gracefully shutdown.");
      thread.interrupt();
      }
    }
  private synchronized void waitForInterval() {
    try {
      this.wait(this.interval);
      } 
    catch (InterruptedException e) {}
    }
  public long getPeak() {
    return observations.stream().reduce(Long.MIN_VALUE, (l1, l2)->Math.max(l1, l2));
    }
  public long getAverage() {
    return observations.stream().reduce(0L, (l1, l2)->l1 + l2) / observations.size();
    }
  protected class MonitoringThread extends Thread {
    protected volatile boolean shutdown = false;
    @Override
    public void run() {
      while(MemoryMonitor.this.monitoring) {
        MemoryMonitor.this.observations.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        this.callThisWait(MemoryMonitor.this.interval);
        }
      this.shutdown = true;
      }
    private synchronized void callThisWait(long interval) {
      try {
        this.wait(MemoryMonitor.this.interval);
        } 
      catch (InterruptedException e) {}
      }
    }
}
