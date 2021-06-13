package is.L42.visitors;

import is.L42.generated.Core;
import is.L42.generated.Full;

public class Contains<T> extends PropagatorCollectorVisitor{
  public T _of(Visitable<?> e){
    e.accept(this);
    return result;
    }
  private T result=null;
  public void setResult(T result){this.result=result;}
  public static class SkipL<T> extends Contains<T>{
    @Override public void visitL(Full.L l){}
    @Override public void visitL(Core.L l){}
    }
  }