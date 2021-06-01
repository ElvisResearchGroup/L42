package is.L42.perftests;

public class TestMain {

	public static void main(String[] args) throws InterruptedException {
		MemoryMonitor mon = new MemoryMonitor(5);
		mon.startMonitoring();
		synchronized(mon) { mon.wait(500); }
		mon.stopMonitoring();
		System.out.println("Peak: " + mon.getPeak());
		System.out.println("Average: " + mon.getAverage());
	}

}
