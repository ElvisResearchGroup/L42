package reduction;

import ast.ExpCore.ClassB;
import facade.Configuration;
import facade.L42;
import facade.Reduction;
import profiling.Timer;

public class Facade implements Reduction{

  private Executor getExecutor(){
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
    return (ClassB)Executor.stepStar(
        //getExecutor(),
        new CompiledStep(),
        //new BigStep(),
        //new SmallStep(),
        topLevel);
  });}
}
