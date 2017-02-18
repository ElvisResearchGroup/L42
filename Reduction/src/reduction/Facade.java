package reduction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ast.Ast.Path;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;
import facade.Configuration;
import facade.L42;
import facade.PData;
import facade.Reduction;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import profiling.Timer;

public class Facade implements Reduction{
  private MapClassLoader lastLoader=null;//by being not static is resetted to null every time configuration is restored.
  public void setLastLoader(MapClassLoader loader){lastLoader=loader;}
  public MapClassLoader getLastLoader(){return lastLoader;}
  @Override public Object convertPath(ast.Ast.Path p){
    if (myExecutor instanceof CompiledStep){
    //we need to use this kind of stuff.
      if(p.equals(Path.Any())){return Resources.Any.type;}
      if(p.equals(Path.Void())){return Resources.Void.type;}
      if(p.equals(Path.Library())){return Resources.Library.type;}
      String s=Resources.nameOf(p);
      if(s.equals("Object")){
        return new Resources.Revertable(){public ast.ExpCore revert() {
           return p;
           }};
        }
      try {
        Class<?> c=lastLoader.loadClass("generated."+s);
        return c.getField("type").get(null);
      } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException  | SecurityException | NoSuchFieldException e) {
        throw new Error(e);   }
      //test returning paths and then throw catch them,
      //test especially difference between Library and Any.
    }
    return p;
  }


  //private static final Executor myExecutor=getExecutor();
  private static final Executor myExecutor=new CompiledStep();
  //private static final Executor myExecutor=new BigStep();
  //private static final Executor myExecutor=new SmallStep();

  @SuppressWarnings("unused") private static Executor getExecutor(){
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
@Override
public ExpCore metaExp(PData p,ExpCore e,Ast.C nameDebug) {
  return  myExecutor.executeAtomicStep(p, e,nameDebug);
 }
}
