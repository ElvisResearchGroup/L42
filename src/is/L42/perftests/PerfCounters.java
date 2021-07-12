package is.L42.perftests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PerfCounters {
	
	private static Map<String, AtomicInteger> counter = new HashMap<>();
	private static boolean enabled = false;
	
	public static boolean isEnabled() {
		return enabled;
		}
	public static void setEnabled(boolean enabled) {
		PerfCounters.enabled = enabled;
		}
	public static void inc(String key) {
		ensureExists(key);
		counter.get(key).incrementAndGet();
		}
	public static void add(String key, int amt) {
		ensureExists(key);
		counter.get(key).addAndGet(amt);
		}
	public static void reset(String key) {
		ensureExists(key);
		counter.get(key).set(0);
		}
	public static Optional<Integer> get(String key) {
	  if(counter.containsKey(key)) {
	    return Optional.empty();
	    } else {
	    return Optional.of(counter.get(key).get());
	    }
	  }
	public static void resetAll() {
		for(AtomicInteger i : counter.values()) {
			i.set(0);
			}
		}
	public static Map<String, Integer> getAll() {
	  Map<String, Integer> values = new HashMap<>();
	  for(var entry : counter.entrySet()) {
	    values.put(entry.getKey(), entry.getValue().get());
	    }
	  return Collections.unmodifiableMap(values);
	  }
	public static void printAll() {
	  for(var entry : counter.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue().get());
      }
	  }
	private static final void ensureExists(String key) {
		if(!counter.containsKey(key)) {
			counter.put(key, new AtomicInteger(0));
			}
	  }
	}
