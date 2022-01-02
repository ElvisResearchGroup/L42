package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.ST;
import is.L42.generated.ST.*;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.FV;
import is.L42.visitors.Visitable;

public class GX {
  final private Map<X,STHalfT> xInT;
  public static GX empty() {
    return new GX(Collections.emptyMap());
    }
  public static GX of(Core.MH mh){
    Map<X, STHalfT> xInT=new HashMap<>();
    xInT.put(X.thisX,STHalfT.of(P.coreThis0.withMdf(mh.mdf())));
    for(int i:range(mh.s().xs())){
      X x=mh.s().xs().get(i);
      Core.T t=mh.pars().get(i);
      xInT.put(x,STHalfT.of(t));
      }
    return new GX(Collections.unmodifiableMap(xInT));
    }
  public GX(Map<X,STHalfT> xInT) {this.xInT=xInT;}

  public STHalfT of(X x) {
    STHalfT res= xInT.get(x);
    assert res!=null: x;
    return res;
    }
  public STHalfT _of(X x) {
    STHalfT res= xInT.get(x);
    return res;//can be null
    }
    
  public GX plusEq(X x,Core.T t) { return plusEq(x,t.mdf(),L(t)); }
  public GX plusEq(X x, Mdf _mdf,List<ST> stz) {
    Map<X,STHalfT> xInT=new HashMap<>(this.xInT);
    assert !xInT.containsKey(x): x+"; "+xInT;
    xInT.put(x,new STHalfT(_mdf,stz));
    return new GX(Collections.unmodifiableMap(xInT)); 
    }
  private boolean updateOk(X x, STHalfT entry,Map<X,STHalfT> xInT){
    var curr = xInT.get(x);
    if(curr==null){ return true; } 
    if(curr.equals(entry)){ return true; }
    if(curr.stz().size()!=1 ||entry.stz().size()!=1){ return false; }
    var c = curr.stz().get(0);
    var e = entry.stz().get(0);
    if(!(c instanceof Core.T ct) || !(e instanceof Core.T et)){ return false; }
    if(entry._mdf()==null){ entry=entry.with_mdf(et.mdf()); }
    if(curr._mdf()==null){ curr=curr.with_mdf(ct.mdf()); }
    return curr.equals(entry);
    }
  public GX plusEqOver(X x, Mdf _mdf, List<ST> stz) {
    Map<X,STHalfT> xInT=new HashMap<>(this.xInT);
    var entry = new STHalfT(_mdf,stz);
    assert updateOk(x,entry,xInT);
    xInT.putIfAbsent(x,entry);
    return new GX(Collections.unmodifiableMap(xInT)); 
    }

  public GX plusEq(List<Full.D> ds) {
    Map<X,STHalfT> xInT=new HashMap<>(this.xInT);
    FV.allVarTx(ds).filter(vx->vx._x()!=null && vx._t()!=null).forEach(vx->{
      assert !xInT.containsKey(vx._x());
      xInT.put(vx._x(),STHalfT.of(TypeManipulation.toCore(vx._t())));
      });
    return new GX(Collections.unmodifiableMap(xInT)); 
    }
  }
