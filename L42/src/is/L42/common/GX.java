package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.visitors.FV;
import is.L42.generated.Half;

public class GX {
  final private Map<X,List<ST>> xInT;
  public static GX empty() {
    return new GX(Collections.emptyMap());
    }
  public static GX of(Core.MH mh){
    Map<X, List<ST>> xInT=new HashMap<>();
    xInT.put(X.thisX,L(P.coreThis0.withMdf(mh.mdf())));
    for(int i:range(mh.s().xs())){
      X x=mh.s().xs().get(i);
      Core.T t=mh.pars().get(i);
      xInT.put(x,L(t));
      }
    return new GX(Collections.unmodifiableMap(xInT));
    }
  public GX(Map<X,List<ST>> xInT) {this.xInT=xInT;}

  public List<ST> of(X x) {
    List<ST> res= xInT.get(x);
    assert res!=null: x;
    return res;
    }
  public List<ST> _of(X x) {
    List<ST> res= xInT.get(x);
    return res;//can be null
    }

/*
  public GX plusEq(List<Full.D> ds) {
    if(ds.isEmpty()){return this;}
    Map<X,T> xInT=new HashMap<>(this.xInT);
    Set<X> vars=new HashSet<>(this.vars);
    for(D di:ds){
      assert !xInT.containsKey(di.x());
      assert !vars.contains(di.x());
      xInT.put(di.x(),di.t());
      if(di.isVar()){vars.add(di.x());}
      }
    return new G(Collections.unmodifiableMap(xInT),Collections.unmodifiableSet(vars)); 
    }*/
    
  public GX plusEq(X x, List<ST> stz) {
    Map<X,List<ST>> xInT=new HashMap<>(this.xInT);
    assert !xInT.containsKey(x): x+"; "+xInT;
    xInT.put(x,stz);
    return new GX(Collections.unmodifiableMap(xInT)); 
    }
  public GX plusEqOver(X x, List<ST> stz) {
    Map<X,List<ST>> xInT=new HashMap<>(this.xInT);
    assert !xInT.containsKey(x) || xInT.get(x).equals(stz): x+"; "+xInT;
    xInT.putIfAbsent(x,stz);
    return new GX(Collections.unmodifiableMap(xInT)); 
    }

  public GX plusEq(List<Full.D> ds) {
    Map<X,List<ST>> xInT=new HashMap<>(this.xInT);
    FV.allVarTx(ds).filter(vx->vx._x()!=null && vx._t()!=null).forEach(vx->{
      assert !xInT.containsKey(vx._x());
      xInT.put(vx._x(),L(TypeManipulation.toCore(vx._t())));
      });
    return new GX(Collections.unmodifiableMap(xInT)); 
    }
  }
