package newTypeSystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BinaryMultiMap<K, V> 
{
  private Map<K, Set<V>> true_ = new HashMap<>();
  private Map<K, Set<V>> false_ = new HashMap<>();

  void add(boolean b, K key, V value) {
    this.addOnce(false, key, value);
    if (b) this.addOnce(b, key, value);
  }

  void addOnce(boolean b, K key, V value) {
    this.get(b).merge(key, Collections.singleton(value), (x, y) -> {
        Set<V> s = new HashSet<V>(x);
        s.addAll(y);
        return s;
    });
  }

  public Map<K, Set<V>> get(boolean b)
  {
    if (b) return this.true_;
    else return this.false_;
  }
  public Set<V> get(boolean b, K key) { return this.get(b).get(key); }
}