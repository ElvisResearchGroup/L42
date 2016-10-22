package platformSpecific.inMemoryCompiler;

import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class RunningUtils {
  private static Logger log = Logger.getLogger(RunningUtils.class.getName());
  public static Object runExecute(ClassLoader cl,String className)throws InvocationTargetException{
  try {
     return cl.loadClass(className).getDeclaredMethod("execute").invoke(null);
    }
  catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
    throw new Error(e);
    }
  }

  public static void runMain(ClassLoader cl,String className)throws InvocationTargetException{
    try {
       cl.loadClass(className).getDeclaredMethod("main",String[].class).invoke(null,(Object)new String[]{});
    } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
      throw new Error(e);
    //} catch (InvocationTargetException ex) {
    //  throw ex.getCause();
      //if (ex.getCause() instanceof RuntimeException) throw (RuntimeException)ex.getCause();
      //if (ex.getCause() instanceof Error) throw (Error)ex.getCause();
      //throw new Error(ex.getCause());
    }
  }
  public static void runMainStrictSecurity(final ClassLoader cl,final String className,int millisecondsTimeOut)throws InvocationTargetException{
    final InvocationTargetException[]mainException={null};
    RunningUtils.runInSecurity(
    		RunningUtils.strictSecurityReadClassAccessDeclaredMembers,new Runnable(){
    			@Override public void run() {
    				try {RunningUtils.runMain(cl,className);
    				} catch (InvocationTargetException e) {mainException[0]=e;}
    			}}, millisecondsTimeOut
      );
    if(mainException[0]!=null)throw mainException[0];//Correct with no finally
  }

  public static final SecurityManager strictSecurity=new SecurityManager() {
    public void checkPermission(Permission p) {throw new SecurityException(p.toString());}
  };

  public static final SecurityManager strictSecurityReadClassAccessDeclaredMembers=new SecurityManager(){
      public void checkPermission(Permission p) {
        if(p instanceof RuntimePermission && "accessDeclaredMembers".equals(p.getName())){return;}
        if (p instanceof java.io.FilePermission){
          java.io.FilePermission pp=(java.io.FilePermission)p;
          if("read".equals(p.getActions()) && pp.getName().endsWith(".class"))return;
          }
        throw new SecurityException(p.toString());}
  };
  public static synchronized void runInSecurity(SecurityManager m,final Runnable task,int millisecondsTimeOut){
    final SecurityManager old=System.getSecurityManager();
    final SecurityManager delegate=m;
    final boolean[]allows={false};
    System.setSecurityManager(new SecurityManager() {
    	public void checkPermission(Permission p) {
    		if (p instanceof RuntimePermission && "setSecurityManager".equals(p.getName()) && allows[0]){
    			return;
    		}
    		else delegate.checkPermission(p);
    	}
    });
    FutureTask<Object> control=new FutureTask<Object>(new Callable<Object>(){
      public Object call() throws Exception {task.run();return null;}}
    );
    Thread t=new Thread(control);
    log.finer(String.format("Starting thread %s",t.getName()));
    t.start();
    try { control.get(millisecondsTimeOut, TimeUnit.MILLISECONDS);}
    catch (TimeoutException ex) {
      throw new Error("The run time of your code was over the fixed time limit");}
    catch (InterruptedException ex) {
      throw new Error("We experienced some tecnical difficulties, try to submit your code again.");}
    catch (ExecutionException ex) {
      throw new Error(ex.getCause());}
    finally{
      allows[0]=true;
      System.setSecurityManager(old);
      control.cancel(true);
      log.finer(String.format("Stopping thread %s",t.getName()));
      t.stop();
      }
    }
}