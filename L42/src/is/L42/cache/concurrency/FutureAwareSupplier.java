package is.L42.cache.concurrency;

import java.util.function.Supplier;

class FutureAwareSupplier<T> implements Supplier<T> {
  
  private final Supplier<T> delegate;
  private LockResolvingFuture<T> fut;
  
  public FutureAwareSupplier(Supplier<T> delegate) {
    this.delegate = delegate;
  }

  public void setFut(LockResolvingFuture<T> fut) {
    assert this.fut == null;
    this.fut = fut;
    }
  
  @Override 
  public T get() { 
    synchronized(Thread.currentThread()) { while(fut == null) try {
      Thread.currentThread().wait(1);
      } catch (InterruptedException e) {} }
    fut.setThread(Thread.currentThread());
    T t = delegate.get(); 
    fut.done();
    return t;
    }
  
  }