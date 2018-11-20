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
  
  public static <T, R, E extends Throwable> List<R> stream(List<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toList(); }
  public static <T, R, E extends Throwable> Set<R> stream(Set<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toSet(); }
  public static <T, R, E extends Throwable> Collection<R> stream(Collection<T> c, CheckedFunction<StreamUtils<T>, StreamUtils<R>, E> f) throws E { return f.apply(stream(c)).toCollection(); }

  public static <T> Iterable<T> iterate(Iterable<T>... args) {
    if (args.length == 0) { return List.of(); }
    return () -> new Iterator<T>() {
      Iterator<T> current = args[0].iterator();
      int i = 1;

      boolean advance() {
        if (i < args.length) { current = args[i++].iterator(); return true; }
        else { return false; }
      }

      @Override public boolean hasNext() {
        while (!current.hasNext()) { if (!advance()) { return false; }}
        return true; }

      @Override public T next() {
        while (true) {
          try { return current.next();}
          catch (NoSuchElementException e) { if(!advance()) { throw e; } }
        }}};}

  public static StreamUtils<Integer> range(int inclusive, int exclusive) { return from(IntStream.range(inclusive, exclusive).boxed()); }
  public static<T> Set<T> interesect(Collection<Collection<T>> s) { return interesect(stream(s)); }
  public static<T> Set<T> interesect(StreamUtils<Collection<T>> s) {
    Set<T> res = null;
    for (var set : s) {
      if (res == null) { res = new HashSet<>(set); }
      else { res.retainAll(set); }}
    return res; }

  // iterates over the first, and then the second collection
  /*static <T> Iterable<T> seqIterate(Iterable<T> first, Iterable<T> second) {
    return () -> new Iterator<T>() {
        Iterator<T> inner = first.iterator();
        boolean firstHalf = true;

        void update() { inner = second.iterator(); firstHalf = false; }
        @Override public boolean hasNext() {
          var res = this.inner.hasNext();
          if (!res && firstHalf) {
            this.update();
            res = this.inner.hasNext();
          }
          return res; }
        @Override public T next() {
          if (firstHalf) {
            try { return inner.next(); }
            catch (NoSuchElementException e) { update(); } }
          return inner.next(); }};}

  static <T> Set<T> intersect(Stream<Collection<T>> s) {
      Set<T> res = null;
      for (var set : iterate(s)) {
        if (res == null) { res = new HashSet<>(set); }
        else { res.retainAll(set); }}
      return res; }
*/

  @Override public Iterator<T> iterator() { return this.s.iterator(); }

  public static<T> StreamUtils<T> of(T... args) { return from(Stream.of(args)); }
  public StreamUtils<T> concat(T... args) {return from(Stream.concat(this.s, Stream.of(args)));}

  public static<T> List<T> concat(List<T> l, T... args) {
    var res = new ArrayList<>(l);
    Collections.addAll(res, args);
    return res; }

  public static<T> List<T> concat(List<T> l, Collection<T>... args) {
    var res = new ArrayList<>(l);
    for (var x : args) { res.addAll(x); }
    return res; }

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

  public <E extends Throwable> StreamUtils<T> ifilter(CheckedPredicate<T, E> f) throws E { return from(this.s.filter(f.negate().uncheck())); }
  public static <T,E extends Throwable> List<T> ifilter(List<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).ifilter(f).toList(); }
  public static <T,E extends Throwable> Set<T> ifilter(Set<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).ifilter(f).toSet(); }
  public static <T,E extends Throwable> Collection<T> ifilter(Collection<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).ifilter(f).toCollection(); }

  public <E extends Throwable> StreamUtils<T> filter(CheckedPredicate<T, E> f) throws E { return from(s.filter(f.uncheck())); }
  public static <T,E extends Throwable> List<T> filter(List<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toList(); }
  public static <T,E extends Throwable> Set<T> filter(Set<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toSet(); }
  public static <T,E extends Throwable> Collection<T> filter(Collection<T> c, CheckedPredicate<T, E> f) throws E { return stream(c).filter(f).toCollection(); }

  public <E extends Throwable> boolean all(CheckedPredicate<T, E> p) throws E { return this.s.allMatch(p.uncheck()); }
  public static <T, E extends Throwable> boolean all(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).all(p); }

  public boolean allIn(Collection<? super T> c) { return this.all(c::contains); }

  public <E extends Throwable> T first(CheckedPredicate<T, E> p) throws E { return  this.filter(p).s.findFirst().orElse(null); }
  public static<T, E extends Throwable> T first(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).first(p); }

  public <E extends Throwable> boolean any(CheckedPredicate<T, E> p) throws E { return this.s.anyMatch(p.uncheck()); }
  public static <T, E extends Throwable> boolean any(Collection<T> c, CheckedPredicate<T, E> p) throws E { return stream(c).any(p); }}
