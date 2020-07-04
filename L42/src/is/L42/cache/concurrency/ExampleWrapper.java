package is.L42.cache.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;

import is.L42.platformSpecific.javaTranslation.L42Throwable;

//NOTE: a #$ method will only be called from main.
//in that moment, the main is also not blocked on any join
//Thus it may
//kill/resfresh the whole thread pool and grow the version number,
//so futures waiting on the old version can be restarted
abstract class Cache<T>{
  @SuppressWarnings("deprecation")
  static void killAll() {
    List<ForkJoinWorkerThread> threadsOld=new ArrayList<>(threads);
    System.err.println(threads.size());
    threads.clear();
    pool.shutdown();
    threadsOld.forEach(t->t.stop());
    pool.awaitQuiescence(10,TimeUnit.SECONDS);
    pool=newPool();
    }
  static List<ForkJoinWorkerThread> threads=new ArrayList<>();
  static volatile ForkJoinPool pool=newPool();
  private ForkJoinPool localPool=null;
  static ForkJoinPool newPool(){return new ForkJoinPool(50,
    a->{
      ForkJoinWorkerThread t=ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(a);
      threads.add(t);
      return t;
      },
    null, true);}
  abstract T op();
  CompletableFuture<T> val=null;
  void clear() {val=null;localPool=null;}//terminating the computation would be hard
  synchronized void startNow(){
    if(val!=null){return;}
    val=new CompletableFuture<>();
    localPool=pool;
    try {val.complete(op());}
    catch(L42Throwable t) {val.completeExceptionally(t);throw t;}
    }
  synchronized void startEager(){
    if(val!=null){
      if(localPool==pool){return;}
      clear();//reset since the pool was killed
      startEager();
      return;
      }
    val=CompletableFuture.supplyAsync(this::op,pool);
    }
  synchronized T join(){
    if(val==null || localPool!=pool){
      val=null;
      localPool=null;
      startNow();
      }
    try{return val.join();}
    catch(CompletionException ce){throw (L42Throwable)ce.getCause();}
    }
  }
public class ExampleWrapper {
  public static String a() {return "a";}
  static Cache<String> cB=new Cache<>(){String op(){
    if(stuck)return cA.join();return "b";}};
  static Cache<String> cA=new Cache<>(){String op(){
    while(stuck){};return "b";}};
  volatile static boolean stuck=true;
  public static void main(String[]arg) throws InterruptedException{
    cA.startEager();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"phase1");
    Cache.killAll();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"phase2");
    stuck=false;
    cA.startEager();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"hi");
    System.err.println(cA.join());
    Cache.killAll();
    stuck=true;
    cA.startEager();
    Thread.sleep(20);
    Cache.killAll();
    stuck=false;
    System.err.println("hi");
    System.err.println(cA.join());
    }
  }

/*
patterns:
  Cache.killAll() is a #$ System method
  
  for each cache:
    _meth(){e;}
    Cache<T> cacheMeth=new Cache<T>(){op=_meth();}
    meth(){return cacheMeth.join();}
    in the constructor:
      startEager all the Cache.Eager
      startNow all the Cache.Now, (the read version)
    in the normalization:
      startEager all the Cache.Eager
    in clear: clear() on Cache.Lazy, clear();startEager(); on Cache.Eager; clear();startNow() on Cache.Now;     
-There must be no Cache.Now on imm methods
-Cache.Now can only be on class methods retrofitted on read ones.
-Cache.Lazy can be on on class methods retrofitted on read ones or on imm methods
-Cache.Eager can be on on class methods retrofitted on read ones or on imm methods
   what happen on clear cache?
   
   
   clear happens on mutation
   a mutable object is only visible to 1 task
   -no other tasks are waiting on a lazy/eager since they can not see the object
   
   
   
*/