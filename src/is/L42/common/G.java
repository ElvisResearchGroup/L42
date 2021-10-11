package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Core.D;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.typeSystem.TypeManipulation;

public class G {
  private boolean active = true;
  private G child;
  private Consumer<G> undo;
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
  /**
   * Ensures that this G has no active children, and is active itself.
   * If this G has been inactivated, this method will throw error.
   */
  private void ensureFresh() {
    if(!this.active) { throw new Error("Attempt to use inactivated G. Consider using copy() if you need two G simultaneously"); }
    if(this.child == null) { return; }
    this.child.inactivate();
    this.undo.accept(this);
    this.child = null;
    this.undo = null;
    }
  /**
   * Undoes any mutations to the underlying map, then marks this G
   * as unusable for future operations
   */
  private void inactivate() {
    assert this.active; //References to inactive G should be purged
    if(this.child != null) { 
      this.child.inactivate(); 
      this.undo.accept(this);
      }
    this.active = false;
    }
  private G newChild(Consumer<G> undo) {
    assert this.child == null;
    this.undo = undo;
    return this.child = new G(this.xInT, this.vars);
    }
  public G copy() {
    this.ensureFresh();
    return new G(new HashMap<>(xInT), new HashSet<>(vars));
    }
  public Set<X> dom(){
    this.ensureFresh();
    return Collections.unmodifiableSet(xInT.keySet()); 
    }
  public static G empty() {
    return new G(new HashMap<>(),new HashSet<>());
    }
  public static G of(Core.MH mh){return of(mh,L());}
  public static G of(Core.MH mh,List<Doc>thisDocs){    
    Map<X, T> xInT=new HashMap<>();
    xInT.put(X.thisX,P.coreThis0.withMdf(mh.mdf()).withDocs(thisDocs));
    for(int i:range(mh.s().xs())){
      X x=mh.s().xs().get(i);
      T t=mh.pars().get(i);
      xInT.put(x,t);
      }
    return new G(xInT,new HashSet<>());
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
  public boolean isVar(X x){
    this.ensureFresh();
    return vars.contains(x);
    }
  public G keepVars(Collection<X> vars){
    this.ensureFresh();
    Set<X> vs=new HashSet<>(this.vars);
    vs.retainAll(vars);
    return new G(new HashMap<>(xInT),vs);
    }
  public T of(X x) {
    this.ensureFresh();
    T res= xInT.get(x);
    assert res!=null:
      x;
    return res;
    }
  public T _of(X x) {
    this.ensureFresh();
    T res= xInT.get(x);
    return res;
    }
  public T _of(Core.XP xp){
    this.ensureFresh();
    if(xp instanceof Core.EX){return _of(((Core.EX)xp).x());}
    return ((Core.PCastT)xp).t();
    }
  public T of(Core.XP xp){
    this.ensureFresh();
    if(xp instanceof Core.EX){return of(((Core.EX)xp).x());}
    return ((Core.PCastT)xp).t();
    }
  private static record ToRemove(Set<X> keys, Set<X> vars) implements Consumer<G> {
    @Override public void accept(G t) {
      keys.forEach(k->t.xInT.remove(k));
      vars.forEach(v->t.vars.remove(v));
      } 
    }
  public G plusEq(List<D> ds) {
    this.ensureFresh();
    if(ds.isEmpty()){return this;}
    Set<X> keysTR = new HashSet<>();
    Set<X> varsTR = new HashSet<>();
    for(D di:ds){
      assert !xInT.containsKey(di.x()):di.x()+"; "+xInT;
      assert !vars.contains(di.x());
      keysTR.add(di.x());
      if(di.isVar()) varsTR.add(di.x());
      xInT.put(di.x(),di.t());
      if(di.isVar()){vars.add(di.x());}
      }
    return this.newChild(new ToRemove(keysTR, varsTR));
    }
  public G plusEq(X x, T t) {
    this.ensureFresh();
    assert !xInT.containsKey(x):x+" in "+xInT;
    xInT.put(x,t);
    return this.newChild(g->g.xInT.remove(x));
    }
  public G update(X x, T t) {
    this.ensureFresh();
    T old = this.xInT.get(x);
    this.xInT.put(x, t);
    return this.newChild(g->g.xInT.put(x, old));
    }

  public G toRead(){
    this.ensureFresh();
    var map=new HashMap<X,T>();
    for(var e:this.xInT.entrySet()){
      map.put(e.getKey(),TypeManipulation.toRead(e.getValue()));
      }
    return new G(map,new HashSet<>());
    }
  public G toLent(){
    this.ensureFresh();
    var newVars=new HashSet<X>();
    var map=new HashMap<X,T>();
    for(var e:this.xInT.entrySet()){
      var x=e.getKey();
      var t=e.getValue();
      if(t.mdf().isImm() && vars.contains(x)){newVars.add(x);}
      var mdf=TypeManipulation._toLent(t.mdf());
      if(mdf!=null){map.put(x,t.withMdf(mdf));}
      }
    return new G(map,newVars);
    }
  public G plusEqMdf(G g0) {
    this.ensureFresh();
    Set<X> keysTR = new HashSet<>();
    for(var e:g0.xInT.entrySet()){
      T ret=xInT.putIfAbsent(e.getKey(),e.getValue());
      if(ret == null) keysTR.add(e.getKey());
      }
    Set<X> varsTR = new HashSet<>();
    for(X x : g0.vars) {
      if(vars.add(x)) { varsTR.add(x); }
      }
    return this.newChild(new ToRemove(keysTR, varsTR));
    }
  public G plusEqFwdOnlyMutOrImm(List<D> ds) {
    this.ensureFresh();
    Set<X> keysTR = new HashSet<>();
    for(var d:ds){
      assert !xInT.containsKey(d.x()):
        d.x();
      Mdf m=d.t().mdf();
      if(!m.isIn(Mdf.Mutable, Mdf.Immutable)){continue;}
      m=TypeManipulation.fwdOf(d.t().mdf());
      keysTR.add(d.x());
      xInT.put(d.x(),d.t().withMdf(m));
      }
    return this.newChild(new ToRemove(keysTR, Set.of()));
    }
  public G plusEqFwdP(List<D> ds) {
    this.ensureFresh();
    Set<X> keysTR = new HashSet<>();
    for(var d:ds){
      assert !xInT.containsKey(d.x());
      Mdf m=d.t().mdf();
      m=TypeManipulation.fwdPOf(d.t().mdf());
      keysTR.add(d.x());
      xInT.put(d.x(),d.t().withMdf(m));
      }
    return this.newChild(new ToRemove(keysTR, Set.of()));
    }
  }
