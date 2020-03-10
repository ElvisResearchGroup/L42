package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Core.D;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.X;
import is.L42.typeSystem.TypeManipulation;

public class G {
  final private Map<X,T> xInT;
  final private Set<X> vars;
  @Override public String toString(){
    String res="";
    for(var x:L(xInT.keySet().stream().sorted((x1,x2)->x1.inner().compareTo(x2.inner())))){
      if(vars.contains(x)){res+="var ";}
      res+=x+":"+xInT.get(x)+"; ";
      
      }
    return res;
    } 
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
  public boolean isVar(X x){return vars.contains(x);}
  public T of(X x) {
    T res= xInT.get(x);
    assert res!=null: x;
    return res;
    }
  public T _of(X x) {
    T res= xInT.get(x);
    return res;
    }
  public T _of(Core.XP xp){
    if(xp instanceof Core.EX){return _of(((Core.EX)xp).x());}
    return ((Core.PCastT)xp).t();
    }
  public T of(Core.XP xp){
    if(xp instanceof Core.EX){return of(((Core.EX)xp).x());}
    return ((Core.PCastT)xp).t();
    }
  public G plusEq(List<D> ds) {
    if(ds.isEmpty()){return this;}
    Map<X,T> xInT=new HashMap<>(this.xInT);
    Set<X> vars=new HashSet<>(this.vars);
    for(D di:ds){
      assert !xInT.containsKey(di.x()):di.x()+"; "+xInT;
      assert !vars.contains(di.x());
      xInT.put(di.x(),di.t());
      if(di.isVar()){vars.add(di.x());}
      }
    return new G(Collections.unmodifiableMap(xInT),Collections.unmodifiableSet(vars)); 
    }
  public G plusEq(X x, T t) {
    Map<X,T> xInT=new HashMap<>(this.xInT);
    assert !xInT.containsKey(x):x+" not in "+xInT;
    xInT.put(x,t);
    return new G(Collections.unmodifiableMap(xInT),vars); 
    }
  public G toRead(){
    var map=new HashMap<X,T>();
    for(var e:this.xInT.entrySet()){
      map.put(e.getKey(),TypeManipulation.toRead(e.getValue()));
      }
    return new G(Collections.unmodifiableMap(map),Collections.emptySet());
    }
  public G toLent(){
    var newVars=vars;
    if(!vars.isEmpty()){newVars=new HashSet<X>();}
    var map=new HashMap<X,T>();
    for(var e:this.xInT.entrySet()){
      var x=e.getKey();
      var t=e.getValue();
      if(t.mdf().isImm() && vars.contains(x)){newVars.add(x);}
      var mdf=TypeManipulation._toLent(t.mdf());
      if(mdf!=null){map.put(x,t.withMdf(mdf));}
      }
    return new G(Collections.unmodifiableMap(map),newVars);
    }
  public G plusEqMdf(G g0) {
    var map=new HashMap<X,T>(this.xInT);
    for(var e:g0.xInT.entrySet()){
      map.putIfAbsent(e.getKey(),e.getValue());
      }
    var newVars=Stream.concat(this.vars.stream(),g0.vars.stream()).collect(Collectors.toSet());
    return new G(Collections.unmodifiableMap(map),newVars);    
    }
  public G plusEqFwdOnlyMutOrImm(List<D> ds) {
    var map=new HashMap<X,T>(this.xInT);
    for(var d:ds){
      assert !map.containsKey(d.x()):
        d.x();
      Mdf m=d.t().mdf();
      if(!m.isIn(Mdf.Mutable, Mdf.Immutable)){continue;}
      m=TypeManipulation.fwdOf(d.t().mdf());
      map.put(d.x(),d.t().withMdf(m));
      }
    return new G(Collections.unmodifiableMap(map),vars);    
    }
  public G plusEqFwdP(List<D> ds) {
    var map=new HashMap<X,T>(this.xInT);
    for(var d:ds){
      assert !map.containsKey(d.x());
      Mdf m=d.t().mdf();
      m=TypeManipulation.fwdPOf(d.t().mdf());
      map.put(d.x(),d.t().withMdf(m));
      }
    return new G(Collections.unmodifiableMap(map),vars);    
    }
  }
