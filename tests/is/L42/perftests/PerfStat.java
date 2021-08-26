package is.L42.perftests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public record PerfStat(long peakMemory, long averageMemory, long timeMilis) implements Serializable, Comparable<PerfStat> {
	public static double MarginOfError = 0.02;
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
		return Math.abs(diffTime) > MarginOfError ? (diffTime > 0 ? 1 : -1) : 0;
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
