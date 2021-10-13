package is.L42.perftests;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PerfCounters {
	private static Map<String, AtomicInteger> counter = new HashMap<>();
	private static Map<String, Set<Object>> cacheCandidateMap = new HashMap<>();
	private static boolean enabled = false;
	public static boolean isEnabled() { return enabled; }
	public static void setEnabled(boolean enabled) {
		PerfCounters.enabled = enabled;
		}
	private static AtomicInteger iGet(String key) {
	  return counter.computeIfAbsent(key, unused->new AtomicInteger(0));
	  }
	public static void inc(String key) { iGet(key).incrementAndGet(); }
	public static void add(String key, int amt) { iGet(key).addAndGet(amt); }
	public static void mcall(String name) { iGet("invoke. " + name).incrementAndGet(); }
	public static void cacheMcall(String name, Object ... args) {
	  iGet("invoke." + name + ".total").incrementAndGet();
	  Set<Object> usedBefore = PerfCounters.cacheCandidateMap.computeIfAbsent(name, (s)->new HashSet<Object>());
	  DoneBeforeArgs dba = new DoneBeforeArgs(args);
	  if(!usedBefore.contains(dba)) { iGet("invoke." + name + ".unique").incrementAndGet(); }
	  usedBefore.add(dba);
	  }
	public static void reset(String key) { iGet(key).set(0); }
	public static Optional<Integer> get(String key) {
	  AtomicInteger res = counter.get(key);
	  return res == null ? Optional.empty() : Optional.of(res.get());
	  }
	public static void resetAll() {	counter.values().forEach(ctr->ctr.set(0)); }
	public static Map<String, Integer> getAll() {
	  return counter.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().get()));
	  }
	public static void printAll() {
	  List<Map.Entry<String,AtomicInteger>> list = new ArrayList<>(counter.entrySet().stream().toList());
	  list.sort((e1,e2)->e1.getKey().compareTo(e2.getKey()));
	  list.stream().forEach(e->{
	    System.out.println(e.getKey() + ": " + e.getValue().get());
	    });
	  }
	private static record DoneBeforeArgs(Object[] args) {
    @Override public int hashCode() {
      return Arrays.deepHashCode(args);
      }
    @Override public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      DoneBeforeArgs other = (DoneBeforeArgs) obj;
      return Arrays.deepEquals(args, other.args);
      }
	  }
	}
