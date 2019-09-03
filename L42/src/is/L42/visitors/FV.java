package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;

public class FV extends PropagatorCollectorVisitor{
  public static List<X> of(Visitable<?> v){
    FV fv=new FV();
    v.accept(fv);
    return fv.result;
    }
  
  List<X> result=new ArrayList<>();
  
  public void visitEX(Core.EX x){result.add(x.x());}
  //full part
  @Override public void visitK(Full.K k){
    var acc=store();
    super.visitK(k);
    result.removeAll(L(k._x()));
    acc(acc);
    }
  @Override public void visitFor(Full.For f){
    var acc=store();
    super.visitFor(f);
    result.removeAll(domFullDs(f.ds()));
    acc(acc);
    }
  @Override public void visitIf(Full.If i){
    var acc=store();
    super.visitIf(i);
    result.removeAll(domFullDs(i.matches()));
    acc(acc);
    }
  @Override public void visitBlock(Full.Block b){
    var acc=store();
    super.visitBlock(b);
    result.removeAll(domFullDs(b.ds()));
    acc(acc);
    }
  @Override public void visitL(Full.L L){}
  private List<X> store(){
    var acc=result;
    result=new ArrayList<>();
    return acc;
    }
  private void acc(List<X> acc){
    acc.addAll(result);
    result=acc;
    }
  public static List<X> domFullDs(List<Full.D>ds){
    return allVarTx(ds)
      .map(vtx->vtx._x())
      .filter(x->x!=null)
      .collect(Collectors.toList());
    }
  public static Stream<Full.VarTx> allVarTx(List<Full.D>ds){
    return ds.stream().flatMap(d->Stream.concat(Stream.of(
      d._varTx()),d.varTxs().stream()))
     .filter(vtx->vtx!=null);
     }
  //core part
  @Override public void visitL(Core.L L){}
  @Override public void visitBlock(Core.Block b){
    var acc=store();
    var domDs=domDs(b.ds());
    visitDs(b.ds());
    var ys=store();
    ys.removeAll(domDs);
    List<List<X>> ks=new ArrayList<>();
    for(var k: b.ks()){
      visitK(k);
      ks.add(store());
      }
    visitE(b.e());
    var e=result;
    e.removeAll(domDs);
    ks.add(e);
    var max=max(ks);
    acc.addAll(max);
    acc.addAll(ys);
    result=acc;
    for(var d:b.ds()){
      if(!d.t().mdf().isCapsule()){continue;}
      long count=Stream.concat(ys.stream(),e.stream())
        .filter(x->x.equals(d.x())).count();
      if (count<=1){continue;}
      throw new WellFormedness.NotWellFormed(b.pos(),"capsule binding "+d.x()+" used more then once");
      }
    }
  public static List<X> max(List<List<X>> xss){
    return xss.stream().reduce(FV::max).get();
    }
  public static List<X> max(List<X> l1,List<X>l2){
    if(l1.isEmpty()){return l2;}
    if(l2.isEmpty()){return l1;}
    List<X>res=new ArrayList<>(l2);
    for(X x:l1){res.remove(x);}//removes the leftmost if any
    res.addAll(l1);
    return res;
    }
  @Override public void visitK(Core.K k){
    var acc=store();
    super.visitK(k);
    result.removeAll(L(k.x()));
    acc(acc);
    }
  public static List<X> domDs(List<Core.D>ds){
    return ds.stream().map(d->d.x()).collect(Collectors.toList());
    }
}
