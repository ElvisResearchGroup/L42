package tools;

import java.util.Objects;
import java.util.function.*;

/**
 * Utility class to handle checked exceptions in lambdas.
 * <p>
 * From <a href="http://stackoverflow.com/questions/27644361/how-can-i-throw-checked-exceptions-from-inside-java-8-streams">How can I throw CHECKED exceptions from inside Java 8 streams?</a>.
 *   This class helps to handle checked exceptions with lambdas. Example, with Class.forName method, which throws checked exceptions:
 * </p>
 * <pre>
 *   Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
 *       .map(rethrowFunction(Class::forName))
 *       .collect(Collectors.toList());
 * </pre>
 * 
 * @author http://stackoverflow.com/users/3411681/marcg
 */
public final class LambdaExceptionUtil {
    public static<T> T uncheck(CheckedSupplier<T, ? extends Throwable> s) { return s.uncheck().get(); }
    public static Throwable catch_(CheckedRunnable<? extends Throwable> r) {
        try { r.run(); }
        catch (Throwable t) { return t; }
        return null; }

    @FunctionalInterface
    public interface CheckedConsumer<T, E extends Throwable>
    {
        void accept(T t) throws E;
        default void acceptUnchecked(T t) { this.uncheck().accept(t); }
        default Throwable acceptCatch(T t) { return catch_(() -> this.accept(t)); }

        @SuppressWarnings("unchecked")
        default Consumer<T> uncheck() { return ((CheckedConsumer<T, RuntimeException>)this)::accept; }
        static<T> Consumer<T> uncheck( CheckedConsumer<T, ? extends Throwable> f) { return f.uncheck(); }
        static<T> CheckedConsumer<T, ? extends RuntimeException> check(Consumer<T> p) { return p::accept; }
        default CheckedConsumer<T, E> andThen(CheckedConsumer<? super T, ? extends E> after) { Objects.requireNonNull(after); return (T t) -> { accept(t); after.accept(t); }; }}

    @FunctionalInterface
    public interface CheckedBiConsumer<T, U, E extends Throwable> {
        void accept(T t, U u) throws E;
        default void acceptUnchecked(T t, U u) { this.uncheck().accept(t, u); }
        default Throwable acceptCatch(T t, U u) { return catch_(() -> this.accept(t, u)); }

        @SuppressWarnings("unchecked")
        default BiConsumer<T, U> uncheck() { return ((CheckedBiConsumer<T, U, RuntimeException>)this)::accept; }
        static<T, U> BiConsumer<T, U> uncheck(CheckedBiConsumer<T, U, ? extends Throwable> f) { return f.uncheck(); }
        static<T, R> CheckedBiConsumer<T, R, ? extends RuntimeException> check(BiConsumer<T, R> f) { return f::accept; }

        default CheckedBiConsumer<T, U, E> andThen(CheckedBiConsumer<? super T, ? super U, ? extends E> after) { Objects.requireNonNull(after); return (l, r) -> {accept(l, r); after.accept(l, r);};}}

    @FunctionalInterface
    public interface CheckedFunction<T, R, E extends Throwable> {
        R apply(T t) throws E;
        default R applyUnchecked(T t) { return this.uncheck().apply(t); }
        default Throwable applyCatch(T t) { return catch_(() -> this.apply(t)); }

        @SuppressWarnings("unchecked")
        default Function<T, R> uncheck() { return ((CheckedFunction<T, R, RuntimeException>)this)::apply; }
        static<T, R> Function<T, R> uncheck(CheckedFunction<T, R, ? extends Throwable> f) { return f.uncheck(); }
        static<T, R> CheckedFunction<T, R, ? extends RuntimeException> check(Function<T, R> f) { return f::apply; }

        @SuppressWarnings("unchecked")
        default <R2> CheckedFunction<T, R2, E> cast() { return (t) -> (R2)this.apply(t); }

        default <V> CheckedFunction<V, R, E> compose(CheckedFunction<? super V, ? extends T, ? extends E> before) { Objects.requireNonNull(before); return (V v) -> apply(before.apply(v)); }
        default <V> CheckedFunction<T, V, E> andThen(CheckedFunction<? super R, ? extends V, ? extends E> after) { Objects.requireNonNull(after); return (T t) -> after.apply(apply(t)); }}

    @FunctionalInterface
    public interface CheckedSupplier<T, E extends Throwable> {
        T get() throws E;

        default T getUnchecked() { return this.uncheck().get(); }
        default Throwable getCatch() { return catch_(this::get); }

        @SuppressWarnings("unchecked")
        default Supplier<T> uncheck() { return ((CheckedSupplier<T, RuntimeException>)this)::get; }
        static<T> Supplier<T> uncheck(CheckedSupplier<T, ? extends Throwable> f) { return f.uncheck(); }}

    @FunctionalInterface
    public interface CheckedPredicate<T, E extends Throwable> {
        boolean test(T t) throws E;

        default boolean testUnchecked(T t) { return this.uncheck().test(t); }
        default Throwable testCatch(T t) { return catch_(() -> this.test(t)); }

        @SuppressWarnings("unchecked")
        default Predicate<T> uncheck() { return ((CheckedPredicate<T, RuntimeException>)this)::test; }
        static<T> Predicate<T> uncheck(CheckedPredicate<T, ? extends Throwable> f) { return f.uncheck(); }
        static<T> CheckedPredicate<T, ? extends RuntimeException> check(Predicate<T> p) { return p::test; }

        default CheckedPredicate<T, E> and(CheckedPredicate<? super T, ? extends E> that) { Objects.requireNonNull(that); return (t) -> this.test(t) && that.test(t); }
        default CheckedPredicate<T, E> negate() { return (t) -> !this.test(t); }
        default CheckedPredicate<T, E> or(CheckedPredicate<? super T, ? extends E> that) { Objects.requireNonNull(that); return (t) -> this.test(t) || that.test(t); }

        static <T, E extends Throwable> CheckedPredicate<T, E> not(CheckedPredicate<T, E> target) { Objects.requireNonNull(target); return target.negate(); }}
    public interface CheckedBiPredicate<T, Y, E extends Throwable> {
        boolean test(T t, Y y) throws E;

        default boolean testUnchecked(T t, Y y) { return this.uncheck().test(t, y); }
        default Throwable testCatch(T t, Y y) { return catch_(() -> this.test(t, y)); }

        @SuppressWarnings("unchecked")
        default BiPredicate<T, Y> uncheck() { return ((CheckedBiPredicate<T, Y, RuntimeException>)this)::test; }
        static<T, Y> BiPredicate<T, Y> uncheck(CheckedBiPredicate<T, Y, ? extends Throwable> f) { return f.uncheck(); }
        static<T, Y> CheckedBiPredicate<T, Y, ? extends RuntimeException> check(BiPredicate<T, Y> p) { return p::test; }

        default CheckedBiPredicate<T, Y, E> and(CheckedBiPredicate<? super T, ? super Y, ? extends E> that) { Objects.requireNonNull(that); return (t, y) -> this.test(t, y) && that.test(t, y); }
        default CheckedBiPredicate<T, Y, E> negate() { return (t, y) -> !this.test(t, y); }
        default CheckedBiPredicate<T, Y, E> or(CheckedBiPredicate<? super T, ? super Y, ? extends E> that) { Objects.requireNonNull(that); return (t, y) -> this.test(t, y) || that.test(t, y); }

        static <T, Y, E extends Throwable> CheckedBiPredicate<T, Y, E> not(CheckedBiPredicate<T, Y, E> target) { Objects.requireNonNull(target); return target.negate(); }}
    @FunctionalInterface
    public interface CheckedRunnable<E extends Throwable> {
        void run() throws E;

        default void runUnchecked() { this.uncheck().run(); }
        default Throwable runCatch() { return catch_(this); }

        @SuppressWarnings("unchecked")
        default Runnable uncheck() { return ((CheckedRunnable<RuntimeException>)this)::run; }
        static Runnable uncheck(CheckedRunnable<? extends Throwable> f) { return f.uncheck(); }
        static CheckedRunnable<? extends RuntimeException> check(Runnable r) { return r::run; }}

    @SuppressWarnings ("unchecked")
    public static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E { 
        throw (E)exception; }
}