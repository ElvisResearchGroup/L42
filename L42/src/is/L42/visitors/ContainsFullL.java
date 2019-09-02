package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;

public class ContainsFullL extends PropagatorCollectorVisitor{
  public static Full.L _of(Full.E e){
    ContainsFullL fv=new ContainsFullL();
    fv.visitE(e);
    return fv.result;
    }
  Full.L result=null;  
  @Override public void visitL(Full.L l){result=l;}
  @Override public void visitL(Core.L l){}
  }
