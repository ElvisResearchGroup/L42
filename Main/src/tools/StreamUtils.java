package tools;

import java.util.*;
import java.util.Map;
import java.util.function.*;
import java.util.stream.*;

import ast.Util;
import tools.LambdaExceptionUtil.*;

// Some collection utilities
public class StreamUtils<T> implements Iterable<T> {
  public static <K, V> void replaceAllNull(Map<K, V> m, Function<K, V> f) { m.replaceAll((k, v) -> v == null ? f.apply(k) : v); }

  private Stream<T> s; private StreamUtils(Stream<T> s) { this.s = s; }
  private static<T> StreamUtils<T> from (Stream<T> s) { return new StreamUtils<>(s); }
  public static <R> StreamUtils<R> stream(Collection<R> c) { return StreamUtils.from(c.stream()); }
  public static <R> StreamUtils<R> stream(R[] a) { return StreamUtils.from(Arrays.stream(a)); }
  
  public static <T, R, E extends Throwable> List<R> stream(List<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toList(); }
  public static <T, R, E extends Throwable> Set<R> stream(Set<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toSet(); }
  public static <T, R, E extends Throwable> Collection<R> stream(Collection<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toCollection(); }

  public static StreamUtils<Integer> range(int inclusive, int exclusive) { return from(IntStream.range(inclusive, exclusive).boxed()); }
  public static<T> Set<T> interesect(Collection<Collection<T>> s) { return interesect(stream(s)); }
  public static<T> Set<T> interesect(StreamUtils<Collection<T>> s) {
    Set<T> res = null;
    for (var set : s) {
      if (res == null) { res = new HashSet<>(set); }
      else { res.retainAll(set); }}
    return res; }

  @Override public Iterator<T> iterator() { return this.s.iterator(); }

  public static<T> StreamUtils<T> of(T... args) { return from(Stream.of(args)); }
  public StreamUtils<T> concat(T... args) {return from(Stream.concat(this.s, Stream.of(args)));}

  public static<T> List<T> concat(List<T> l, T... args) {
    var res = new ArrayList<>(l);
    Collections.addAll(res, args);
    return res; }

  public static<T> List<T> concat(List<T> l, Iterable<T>... args) {
    var res = new ArrayList<>(l);
    for (var x : args) { for (var y : x) { res.add(y); } }
    return res; }
  public static <T> Iterable<T> concat(Iterable<T> it, Iterable<T>... args) {
    return () -> new Iterator<>() {
      Iterator<T> current = it.iterator();
      int i = 0;

      private boolean advance() {
        if (i < args.length) { current = args[i++].iterator(); return true; }
        else { return false; }}

      @Override public boolean hasNext() {
        while (!current.hasNext()) { if (!advance()) { return false; }}
        return true; }

      @Override public T next() {
        while (true) {
          try { return current.next();}
          catch (NoSuchElementException e) { if(!advance()) { throw e; } }
        }}};}

  public StreamUtils<T> concat(StreamUtils<T>... args) {
    var res = this.s;
    for (var x : args) { res = Stream.concat(res, x.s); }
    return from(res);}

  public List<T> toList() { return this.s.collect(Collectors.toCollection(ArrayList::new)); }
  public Set<T> toSet() { return this.s.collect(Collectors.toCollection(HashSet::new)); }
  public Collection<T> toCollection() { return this.s.collect(Collectors.toList()); }

  public String toString(String sep) { return this.s.map(Objects::toString).collect(Collectors.joining(sep)); }

  // dont override Object.ToString, as this can't be called more than once
  public String toString_() { return this.toString(""); }

  public Collection<? super T> addTo(Collection<? super T> c) { c.addAll(this.toCollection()); return c; }

  public <R,E extends Throwable> StreamUtils<R> map(CheckedFunction<T, R, E> f) throws E { return from(s.map(f.uncheck())); }
  public static <T,R,E extends Throwable> List<R> map(List<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).map(f).toList(); }
  public static <T,R,E extends Throwable> Set<R> map(Set<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).map(f).toSet(); }
  public static <T,R,E extends Throwable> Collection<R> map(Collection<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).map(f).toCollection(); }

  public <R,E extends Throwable> StreamUtils<R> filterMap(CheckedFunction<T, R, E> f) throws E { return from(s.map(f.uncheck()).filter(Objects::nonNull)); }
  public static <T,R,E extends Throwable> List<R> filterMap(List<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).filterMap(f).toList(); }
  public static <T,R,E extends Throwable> Set<R> filterMap(Set<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).filterMap(f).toSet(); }
  public static <T,R,E extends Throwable> Collection<R> filterMap(Collection<T> c, CheckedFunction<T, R, E> f) throws E { return stream(c).filterMap(f).toCollection(); }

  public <R,E extends Throwable> StreamUtils<R> flatMap(CheckedFunction<T, Collection<R>, E> f) throws E { return from(s.flatMap(x -> f.applyUnchecked(x).stream())); }
  public static <T,R,E extends Throwable> List<R> flatMap(List<T> c, CheckedFunction<T, Collection<R>, E> f) throws E { return stream(c).flatMap(f).toList(); }
  public static <T,R,E extends Throwable> Set<R> flatMap(Set<T> c, CheckedFunction<T, Collection<R>, E> f) throws E { return stream(c).flatMap(f).toSet(); }
  public static <T,R,E extends Throwable> Collection<R> flatMap(Collection<T> c, CheckedFunction<T, Collection<R>, E> f) throws E { return stream(c).flatMap(f).toCollection(); }

  public <R,E extends Throwable> StreamUtils<R> flatMapS(CheckedFunction<T, StreamUtils<R>, E> f) throws E { return from(s.flatMap(x -> f.applyUnchecked(x).s)); }
  public static <T,R,E extends Throwable> List<R> flatMapS(List<T> c, CheckedFunction<T, StreamUtils<R>, E> f) throws E { return stream(c).flatMapS(f).toList(); }
  public static <T,R,E extends Throwable> Set<R> flatMapS(Set<T> c, CheckedFunction<T, StreamUtils<R>, E> f) throws E { return stream(c).flatMapS(f).toSet(); }
  public static <T,R,E extends Throwable> Collection<R> flatMapS(Collection<T> c, CheckedFunction<T, StreamUtils<R>, E> f) throws E { return stream(c).flatMapS(f).toCollection(); }

  public <R> StreamUtils<R> mapCast() { return this.map(Utils::cast); }
  public static <R> List<R> mapCast(List<? super R> c) { return stream(c).<R>mapCast().toList(); }
  public static <R> Set<R> mapCast(Set<? super R> c) { return stream(c).<R>mapCast().toSet(); }
  public static <R> Collection<R> mapCast(Collection<? super R> c) { return stream(c).<R>mapCast().toCollection(); }

  public <E extends Throwable> StreamUtils<T> filter(CheckedPredicate<T, E> f) throws E { return from(s.filter(f.uncheck())); }
  public static <T,E extends Throwable> List<T> filter(List<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toList(); }
  public static <T,E extends Throwable> Set<T> filter(Set<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toSet(); }
  public static <T,E extends Throwable> Collection<T> filter(Collection<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toCollection(); }

  public StreamUtils<T> distinct() { return from(s.distinct()); }
  public static <T> List<T> distinct(List<T> c) { return stream(c).distinct().toList(); }
  public static <T> Set<T> distinct(Set<T> c) { return stream(c).distinct().toSet(); } // This function is pointless
  public static <T> Collection<T> distinct(Collection<T> c) { return stream(c).distinct().toSet(); }


  public <E extends Throwable> StreamUtils<T> distinct(CheckedBiPredicate<T, T, E> eq) throws E {
    // this is likley to be horrible inefficeint
    var matched = new ArrayList<T>();
    return this.filter(x -> {
      if (matched.stream().anyMatch(y -> eq.testUnchecked(x, y))) { return false; }
      matched.add(x); return true; });}

  public static <T,E extends Throwable> List<T> distinct(List<T> c, CheckedBiPredicate<T, T, E> f) throws E { return stream(c).distinct(f).toList(); }
  public static <T,E extends Throwable> Set<T> distinct(Set<T> c, CheckedBiPredicate<T, T, E> f) throws E { return stream(c).distinct(f).toSet(); }
  public static <T,E extends Throwable> Collection<T> distinct(Collection<T> c, CheckedBiPredicate<T, T, E> f) throws E { return stream(c).distinct(f).toSet(); }

  public <E extends Throwable> boolean all(CheckedPredicate<T, E> p) throws E { return this.s.allMatch(p.uncheck()); }
  public static <T, E extends Throwable> boolean all(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).all(p); }

  public boolean allIn(Collection<? super T> c) { return this.all(c::contains); }

  public <E extends Throwable> T first(CheckedPredicate<T, E> p) throws E { return  this.filter(p).s.findFirst().orElse(null); }
  public static<T, E extends Throwable> T first(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).first(p); }

  public <E extends Throwable> boolean any(CheckedPredicate<T, E> p) throws E { return this.s.anyMatch(p.uncheck()); }
  public static <T, E extends Throwable> boolean any(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).any(p); }}
