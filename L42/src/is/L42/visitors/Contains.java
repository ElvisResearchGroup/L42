package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;

public class Contains<T> extends PropagatorCollectorVisitor{
  public T _of(Full.E e){
    this.visitE(e);
    return result;
    }
  private T result=null;
  public void setResult(T result){this.result=result;}  
  @Override public void visitL(Full.L l){}
  @Override public void visitL(Core.L l){}
  }
