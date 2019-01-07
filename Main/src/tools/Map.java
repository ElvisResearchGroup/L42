package tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Map {
  public static <E1,E2, T extends List<E2>>
  List<E1> of(Function<E2,E1>f,T seq){  return seq.stream().map(f).collect(Collectors.toList());   }

  public static <E1,E2, T extends Set<E2>>
  Set<E1> of(Function<E2,E1>f,T seq){  return seq.stream().map(f).collect(Collectors.toSet());   }


  public static <E1,E2>
  Optional<E1> of(Function<E2,E1>f,Optional<E2> el){
      if(!el.isPresent()){return Optional.empty();}
      return Optional.of(f.apply(el.get()));
    }
  
  /**turn two (immutable) lists into a map*/
  static public <K,V> java.util.Map<K,V> list2map(List<K>ks,List<V>vs){
    return new MapList<K,V>(ks,vs);
    }
  static class MapList<K,V> implements java.util.Map<K,V>{
    List<K> ks;List<V>vs;
    MapList(List<K> ks,List<V>vs){
      this.ks=ks;this.vs=vs;
      assert ks.size()==vs.size();
      }
    @Override public int size() {return ks.size();}

    @Override public boolean isEmpty() {return ks.isEmpty();}

    @Override public boolean containsKey(Object key) {
      return ks.contains(key);
      }

    @Override public boolean containsValue(Object value) {
      return vs.contains(value);
      }

    @Override public V get(Object key) {
      for(int i=0;i<ks.size();i++){
        if(ks.get(i).equals(key)){return vs.get(i);}
        }
      return null;
      }

    @Override public V put(K key, V value) {
      throw Assertions.codeNotReachable("Immutable map");
      }

    @Override public V remove(Object key) {
      throw Assertions.codeNotReachable("Immutable map");
      }

    @Override public void putAll(java.util.Map<? extends K, ? extends V> m) {
      throw Assertions.codeNotReachable("Immutable map");
      }

    @Override public void clear() {
      throw Assertions.codeNotReachable("Immutable map");
      }

    @Override public Set<K> keySet() {
      throw Assertions.codeNotReachable("Would be very slow");
      }

    @Override public Collection<V> values() {
      return this.vs;
      }

    @Override public Set<java.util.Map.Entry<K, V>> entrySet() {
      throw Assertions.codeNotReachable("Would be very slow");
      }
    }
  }

