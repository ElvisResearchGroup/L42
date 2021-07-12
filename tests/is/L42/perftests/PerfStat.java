package is.L42.perftests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class PerfStat implements Serializable, Comparable<PerfStat> {
	public static double MARGIN_OF_ERROR = 0.02;
	public final long peakMemory;
	public final long averageMemory;
	public final long timeMilis;
	public PerfStat(long peakMemory, long averageMemory, long timeMilis) {
		this.peakMemory = peakMemory;
		this.averageMemory = averageMemory;
		this.timeMilis = timeMilis;
		}
	public long getPeakMemory() { return peakMemory; }
	public long getAverageMemory() { return averageMemory; }
	public long getTimeMilis() { return timeMilis; }
	@Override
	public String toString() {
		return "Peak Memory: " + this.peakMemory + " bytes, Average Memory: " + this.averageMemory + " bytes, time: " + (((double) this.timeMilis) / (1000 * 1000 * 1000)) + "s";
		}
	public static PerfStat average(Collection<PerfStat> collection) {
		long pm = 0;
		long am = 0;
		long t = 0;
		for(PerfStat pr : collection) {
			pm = Math.max(pm, pr.peakMemory);
			am += pr.averageMemory;
			t += pr.timeMilis;
			}
		return new PerfStat(pm, am / collection.size(), t / collection.size());
		}
	/**
	 * If <code>this</code> is better then <code>o</code>, return 1. If this is within the margin of error
	 * of o, return 0. If this is worse than 0, return -1
	 */
	public int compareTo(PerfStat o) {
		double diffTime = ((double) (o.timeMilis - this.timeMilis)) / this.timeMilis;
		return Math.abs(diffTime) > MARGIN_OF_ERROR ? (diffTime > 0 ? 1 : -1) : 0;
		}
	public static void saveResult(String name, PerfStat res) throws IOException {
		if(!new File("perfresults/").exists()) { new File("perfresults/").mkdirs(); }
		File file = new File("perfresults/" + name);
		file.createNewFile();
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(res);
			}
		}
	/**
	 * Returns a saved PerfResult for this name, or <code>null</code> if no perfresult of that
	 * name has been saved 
	 * 
	 * @param name
	 * @return The saved result, or <code>null</code> if none exists
	 * @throws IOException If reading the result results in an IO Exception
	 * @throws ClassNotFoundException 
	 */
	public static PerfStat getResult(String name) throws IOException, ClassNotFoundException {
		File file = new File("perfresults/" + name);
		if(!file.exists() || file.isDirectory()) { 
			System.err.println("No previous results for: " + name);
			return null; 
			}
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (PerfStat) ois.readObject();
			}
		}
	}
