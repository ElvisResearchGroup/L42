package reduction;

import java.lang.reflect.InvocationTargetException;

import ast.ExpCore.ClassB;
import facade.Configuration;
import facade.L42;
import facade.Reduction;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import profiling.Timer;

public class Facade implements Reduction{
  private static ClassLoader lastLoader=null;
  public static void setLastLoader(ClassLoader loader){lastLoader=loader;}
  @Override public Object convertPath(ast.Ast.Path p){
    if (myExecutor instanceof CompiledStep){
    //we need to use this kind of stuff.
      String s=Resources.nameOf(p);
      try {
        Class<?> c=lastLoader.loadClass("generated.Program42$"+s);
        return c.getField("type").get(null);
      } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException  | SecurityException | NoSuchFieldException e) {throw new Error(e);   }
      //test returning paths and then throw catch them,
      //test especially difference between Library and Any.
    }
    return p;
  } 
  //private static final Executor myExecutor=getExecutor();
  private static final Executor myExecutor=new CompiledStep();
  //private static final Executor myExecutor=new BigStep();
  //private static final Executor myExecutor=new SmallStep();

  private static Executor getExecutor(){
    if(L42.programArgs!=null && L42.programArgs.length==2 && L42.programArgs[0].equals("-step")){
      return new SmallStep();
      //return new BigStep();
      }
    else{
      return new CompiledStep();
      }
    }
  @Override
  public ClassB of(ClassB topLevel) {
    return Timer.record("Reduction.execute",()->{    
    return (ClassB)Executor.stepStar(myExecutor, topLevel);
  });}
}
