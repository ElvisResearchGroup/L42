package is.L42.common;

import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import is.L42.generated.Core;
import is.L42.generated.Core.D;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.X;
import lombok.NonNull;

public class G {
  final private Map<X,T> xInT;
  final private Set<X> vars;

  public static G empty() {
    return new G(Collections.emptyMap(),Collections.emptySet());
    }
  public static G of(Core.MH mh){
    Map<X, T> xInT=new HashMap<>();
    xInT.put(X.thisX,P.coreThis0.withMdf(mh.mdf()));
    for(int i:range(mh.s().xs())){
      X x=mh.s().xs().get(i);
      T t=mh.pars().get(i);
      xInT.put(x,t);
      }
    return new G(Collections.unmodifiableMap(xInT),Collections.emptySet());
    }
  public G(Map<X, T> xInT, Set<X> vars) {
    this.xInT=xInT;
    this.vars=vars;
    assert xInT.keySet().containsAll(vars);
    assert vars.stream().noneMatch(x->xInT.get(x).mdf().isIn(
      Mdf.Capsule,
      Mdf.ImmutableFwd,Mdf.ImmutablePFwd,
      Mdf.MutableFwd,Mdf.ImmutablePFwd)     
      );
    }

  public T of(X x) {
    T res= xInT.get(x);
    assert res!=null: x;
    return res;
    }
  public T _of(X x) {
    T res= xInT.get(x);
    return res;
    }


  public G plusEq(List<D> ds) {
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
    }
  public G plusEq(X x, T t) {
    Map<X,T> xInT=new HashMap<>(this.xInT);
    assert !xInT.containsKey(x);
    xInT.put(x,t);
    return new G(Collections.unmodifiableMap(xInT),vars); 
    }
  }
