package is.L42.tests;

import java.lang.reflect.InvocationTargetException;


public class RunningUtils {
  public static Object runExecute(ClassLoader cl,String className)throws InvocationTargetException{
  try{return cl.loadClass(className).getDeclaredMethod("execute").invoke(null);}
  catch(ClassNotFoundException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException|SecurityException e){
    throw new Error(e);
    }
  }
  public static void runMain(ClassLoader cl,String className)throws InvocationTargetException{
    try{
       cl.loadClass(className)
         .getDeclaredMethod("main",String[].class)
         .invoke(null,(Object)new String[]{});
      }
    catch(ClassNotFoundException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException|SecurityException e){
      throw new Error(e);
      }
    }
  }