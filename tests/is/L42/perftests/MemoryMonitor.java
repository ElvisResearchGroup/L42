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
			synchronized(this) {
				try {
					this.wait(this.interval);
					} catch (InterruptedException e) {}
				}
			}	
		if(!this.thread.shutdown) {
			System.err.println("Monitoring thread " + thread.toString() + " did not gracefully shutdown.");
			thread.interrupt();
			}
		}
	public long getPeak() {
		long peak = Long.MIN_VALUE;
		for(long l : observations) {
			peak = Math.max(l, peak);
			}
		return peak;
		}
	public long getAverage() {
		long total = 0L;
		for(long l : observations) {
			total += l;
			}
		return total / observations.size();
		}
	protected class MonitoringThread extends Thread {
		protected volatile boolean shutdown = false;
		@Override
		public void run() {
			while(MemoryMonitor.this.monitoring) {
				MemoryMonitor.this.observations.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
				synchronized(this) {
					try {
						this.wait(MemoryMonitor.this.interval);
						} catch (InterruptedException e) {}
					}
				}
			this.shutdown = true;
			}
		}
}
