package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Full;

public class Bindings extends PropagatorCollectorVisitor{
  public static List<X> of(Visitable<?> v){
    Bindings fv=new Bindings();
    v.accept(fv);
    return fv.result;
    }
  
  List<X> result=new ArrayList<>();
  
  //full part
  @Override public void visitK(Full.K k){
    super.visitK(k);
    if(k._x()!=null){result.add(k._x());}
    }
  @Override public void visitFor(Full.For f){
    super.visitFor(f);
    result.addAll(FV.domFullDs(f.ds()));
    }
  @Override public void visitIf(Full.If i){
    super.visitIf(i);
    var filtered=L(i.matches().stream().filter(m-> m._e()!=null));
    result.addAll(FV.domFullDs(filtered));
    }
  @Override public void visitBlock(Full.Block b){
    super.visitBlock(b);
    result.addAll(FV.domFullDs(b.ds()));
    }
  @Override public void visitL(Full.L L){}
  //core part
  @Override public void visitL(CoreL L){}
  @Override public void visitBlock(Core.Block b){
    super.visitBlock(b);
    result.addAll(FV.domDs(b.ds()));
    }
  @Override public void visitK(Core.K k){
    super.visitK(k);
    result.add(k.x());
    }
  }
