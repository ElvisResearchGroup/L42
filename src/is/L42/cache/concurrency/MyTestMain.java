package is.L42.cache.concurrency;

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
  
  private static LockResolvingFuture<String> future1 = LockResolvingFuture.supplyAsync(MyTestMain::auxFunc1);
  private static LockResolvingFuture<String> future2 = LockResolvingFuture.supplyAsync(MyTestMain::auxFunc2);
  
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
  
  
  

}
