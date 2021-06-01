package is.L42.perftests;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
	public PerfResult stopMonitoring() {
		long totalTime = System.nanoTime() - this.starttime;
		mem.stopMonitoring();
		return new PerfResult(mem.getPeak(), mem.getAverage(), totalTime);
		}
	public static Pair<PerfResult,CachedTop> test(Supplier<CachedTop> sup, int times, boolean longTime) {
		PerfMonitor mon = new PerfMonitor(longTime);
		List<PerfResult> res = new ArrayList<>();
		CachedTop top = null;
		for(int i = 0; i < times; i++) {
			mon.startMonitoring();
			top = sup.get();
			res.add(mon.stopMonitoring());
			}
		return new Pair<>(PerfResult.average(res),top);
		}
	}