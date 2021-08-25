package is.L42.cache.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;

import is.L42.platformSpecific.javaTranslation.L42Throwable;

//NOTE: a #$ method will only be called from main.
//in that moment, the main is also not blocked on any join
//Thus Cache.killAll() it is a #$ System method
public abstract class CachedRes<T>{
  public static void killIfBusy(){
    if(!pool.isQuiescent()){killAll();}
  }
  @SuppressWarnings("deprecation")
  public static void killAll() {
    List<ForkJoinWorkerThread> threadsOld=new ArrayList<>(threads);
    //System.err.println(threads.size());
    threads.clear();
    pool.shutdown();
    threadsOld.forEach(t->t.stop());
    pool.awaitQuiescence(10,TimeUnit.SECONDS);
    pool=newPool();
    }
  private static List<ForkJoinWorkerThread> threads=new ArrayList<>();
  private static volatile ForkJoinPool pool=newPool();
  private ForkJoinPool localPool=null;
  private static ForkJoinPool newPool(){return new ForkJoinPool(50,
    a->{
      ForkJoinWorkerThread t=ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(a);
      threads.add(t);
      return t;
      },
    null, true);}
  public abstract T op();
  private CompletableFuture<T> val=null;
  public void clear() {val=null;localPool=null;}//terminating the computation would be hard
  public synchronized void startNow(){
    if(val!=null){return;}
    localPool=pool;
    val=new CompletableFuture<>();
    try {val.complete(op());}
    catch(L42Throwable t) {val.completeExceptionally(t);throw t;}
    }
  public synchronized void startEager(){
    if(val!=null){
      if(localPool==pool){return;}
      clear();//reset since the pool was killed
      startEager();
      return;
      }
    localPool=pool;
    val=CompletableFuture.supplyAsync(this::op,pool);
    }
  public synchronized T join(){
    if(val==null || localPool!=pool){
      val=null;
      localPool=null;
      startNow();
      }
    try{return val.join();}
    catch(CompletionException ce){
      if(ce.getCause() instanceof RuntimeException){throw (RuntimeException)ce.getCause();}
      if(ce.getCause() instanceof Error){throw (Error)ce.getCause();}
      throw ce;
      }
    }
  }