package is.L42.cache.concurrency;

public class ExampleWrapper {
  public static String a() {return "a";}
  static CachedRes<String> cB=new CachedRes<>(){public String op(){
    if(stuck)return cA.join();return "b";}};
  static CachedRes<String> cA=new CachedRes<>(){public String op(){
    while(stuck){};return "b";}};
  volatile static boolean stuck=true;
  public static void main(String[]arg) throws InterruptedException{
    cA.startEager();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"phase1");
    CachedRes.killAll();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"phase2");
    stuck=false;
    cA.startEager();
    Thread.sleep(20);
    System.err.println(Thread.currentThread()+"hi");
    System.err.println(cA.join());
    CachedRes.killAll();
    stuck=true;
    cA.startEager();
    Thread.sleep(20);
    CachedRes.killAll();
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