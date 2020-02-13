package is.L42.cache.tests;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import is.L42.cache.L42CacheMap;

public class MyTestMain {
  
  public static void main(String[] arg) {
    int ctr = 0;
   try { 
     System.out.println(func1()); 
   } catch(StackOverflowError e) {
     ctr++;
   }
   try { 
     System.out.println(func2()); 
   } catch(StackOverflowError e1) {
     ctr++;
   }
   assert ctr == 2;
  }
  
  private static FutureDecorator<String> future1 = supplyAsync(MyTestMain::auxFunc1);
  private static FutureDecorator<String> future2 = supplyAsync(MyTestMain::auxFunc2);
  
  public static String func1()
  {
    return future1.join();
  }
  
  public static String func2()
  {
    return future2.join();
  }
  
  public static String auxFunc1()
  {
    return func2();
  }
  
  public static String auxFunc2() 
  {
    return func1();
  }
  
  static class FutureDecorator<T> {
    
    static Map<Thread, FutureDecorator<?>> futureMap = new IdentityHashMap<>();
    
    static FutureDecorator<?> currentFuture() {
      return futureMap.get(Thread.currentThread());
      }
    
    static boolean isInWorker() {
      return Thread.currentThread().getName().contains("ForkJoinPool");
      }
    
    boolean amIStuck = false;
    Set<Thread> peopleWhoAreWaitingOnMe = Collections.newSetFromMap(new IdentityHashMap<Thread, Boolean>());
    CompletableFuture<T> fut;
    
    public FutureDecorator(CompletableFuture<T> fut) {
      this.fut = fut;
      }
    
    public void setThread(Thread thread) {
      futureMap.put(thread, this);
      }
    
    public void done() {
      futureMap.remove(Thread.currentThread());
      peopleWhoAreWaitingOnMe.clear();
      }
    
    public boolean amIBeingWaitedOnBy(Set<FutureDecorator<?>> prev, Thread thread) {
      if(prev.contains(this)) { return false; }
      if(peopleWhoAreWaitingOnMe.contains(thread)) { return true; }
      prev.add(this);
      for(Thread t : peopleWhoAreWaitingOnMe) {
        if(futureMap.containsKey(t) && futureMap.get(t).amIBeingWaitedOnBy(prev, thread)) { return true; }
        }
      return false;
      }
    
    @SuppressWarnings("unchecked") 
    public T join() 
    {
      this.peopleWhoAreWaitingOnMe.add(Thread.currentThread());
      if(isInWorker() && futureMap.get(Thread.currentThread()).amIBeingWaitedOnBy((Set<FutureDecorator<?>>) (Set<?>) L42CacheMap.identityHashSet(), Thread.currentThread())) {
        amIStuck = true;
        for(Thread t : peopleWhoAreWaitingOnMe) { t.interrupt(); }
        } else {
        if(amIStuck) { throw new StackOverflowError(); }
        }
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
    }
    
  static class SupplierDecorator<T> implements Supplier<T> {
    
    private final Supplier<T> delegate;
    private FutureDecorator<T> fut;
    
    public SupplierDecorator(Supplier<T> delegate) {
      this.delegate = delegate;
    }

    public void setFut(FutureDecorator<T> fut) {
      assert this.fut == null;
      this.fut = fut;
      }
    
    @Override 
    public T get() { 
      assert fut != null;
      fut.setThread(Thread.currentThread());
      T t = delegate.get(); 
      fut.done();
      return t;
      }
    
  }
  
  public static <T> FutureDecorator<T> supplyAsync(Supplier<T> sup) {
    FutureDecorator<T> futDec = null;
    SupplierDecorator<T> dec = new SupplierDecorator<T>(sup);
    dec.setFut(futDec = new FutureDecorator<T>(CompletableFuture.supplyAsync(dec)));
    return futDec;
  }
  

}
