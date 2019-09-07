package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;

public abstract class Accumulate<T> extends PropagatorCollectorVisitor{
  public T of(Visitable<?> e){
    e.accept(this);
    return result;
    }
  private T result=empty();
  public T acc(){return this.result;}
  public abstract T empty();  
  public static abstract class SkipL<T> extends Accumulate<T>{
    @Override public void visitL(Full.L l){}
    @Override public void visitL(Core.L l){}
    }
  }