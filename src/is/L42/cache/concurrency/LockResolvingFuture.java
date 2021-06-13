package is.L42.cache.concurrency;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import is.L42.cache.L42CacheMap;

public class LockResolvingFuture<T> {

  private static final Map<Thread, LockResolvingFuture<?>> futureMap = new IdentityHashMap<>();
  
  private volatile boolean amIStuck = false;
  private final Set<Thread> peopleWhoAreWaitingOnMe = L42CacheMap.identityHashSet();
  private final CompletableFuture<T> fut;
  
  private LockResolvingFuture(CompletableFuture<T> fut) {
    this.fut = fut;
    }
  
  void setThread(Thread thread) {
    synchronized(futureMap) {
      futureMap.put(thread, this);
      }
    }
  
  void done() {
    synchronized(futureMap) {
      futureMap.remove(Thread.currentThread());
      }
    peopleWhoAreWaitingOnMe.clear();
    }
  
  public boolean isCmpletedExceptionally() {
    return fut.isCompletedExceptionally();
    }
  
  public boolean isDone() {
    return fut.isDone();
    }
  
  public T join() 
  {
  if(fut.isDone()) { return this.doGet(); }
  if(amIStuck) { throw new StackOverflowError(); }
  
  if(isInWorker()) {
    Thread cur = Thread.currentThread();
    boolean stuck = false;
    synchronized(futureMap) { 
      this.peopleWhoAreWaitingOnMe.add(cur);
      //Is the caller being waited on by itself, given the new relation cur->us?
      stuck = futureMap.get(cur).amIBeingWaitedOnBy(getNewIDSet(), cur); 
      }
    if(stuck) {
      amIStuck = true;
      fut.completeExceptionally(new StackOverflowError());
      }
    }
  
  return doGet();
  }

  private T doGet() {
    try {
      return fut.get();
      } catch(InterruptedException e) {
      throw new StackOverflowError();
      } catch (ExecutionException e) {
      Throwable t = e.getCause();
      if(t instanceof RuntimeException) { throw ((RuntimeException) t); }
      if(t instanceof Error) { throw ((Error) t); }
      throw new RuntimeException(t);
      }
    }
  
  private boolean amIBeingWaitedOnBy(Set<LockResolvingFuture<?>> prev, Thread thread) {
    if(prev.contains(this)) { return false; }
    if(peopleWhoAreWaitingOnMe.contains(thread)) { return true; }
    prev.add(this);
    for(Thread t : peopleWhoAreWaitingOnMe) {
      if(futureMap.containsKey(t) && futureMap.get(t).amIBeingWaitedOnBy(prev, thread)) { return true; }
      }
    return false;
    }
  
  private static boolean isInWorker() {
    return Thread.currentThread().getName().contains("ForkJoinPool");
    }
  
  private static Set<LockResolvingFuture<?>> getNewIDSet() {
    return L42CacheMap.identityHashSet();
    }
  
  public static <T> LockResolvingFuture<T> supplyAsync(Supplier<T> sup) {
    LockResolvingFuture<T> futDec = null;
    FutureAwareSupplier<T> dec = new FutureAwareSupplier<T>(sup);
    dec.setFut(futDec = new LockResolvingFuture<T>(CompletableFuture.supplyAsync(dec)));
    return futDec;
    }
  
  public static <T> LockResolvingFuture<T> supplyAsync(Supplier<T> sup, Executor exec) {
    LockResolvingFuture<T> futDec = null;
    FutureAwareSupplier<T> dec = new FutureAwareSupplier<T>(sup);
    dec.setFut(futDec = new LockResolvingFuture<T>(CompletableFuture.supplyAsync(dec, exec)));
    return futDec;
    }
  }