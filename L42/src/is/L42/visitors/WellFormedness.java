package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Full;
import is.L42.generated.Full.D;
import is.L42.generated.HasVisitable;
import is.L42.generated.LDom;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;

class ContainsFullL extends Contains<Full.L>{
  @Override public void visitL(Full.L l){setResult(l);}
  }
class ContainsSlashXOut extends Contains<Full.SlashX>{
  @Override public void visitSlashX(Full.SlashX sx){setResult(sx);}
  @Override public void visitPar(Full.Par par){}
  }
class ContainsIllegalVarUpdate extends Contains<Core.EX>{
  Set<X> updatableXs=Collections.emptySet();
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
    super.visitOpUpdate(opUpdate);
    if(updatableXs.contains(opUpdate.x())){return;}
    setResult(new Core.EX(opUpdate.pos(),opUpdate.x()));
    }
  @Override public void visitBlock(Full.Block b){
    var old=updatableXs;
    updatableXs=Stream.concat(old.stream(),
      FV.allVarTx(b.ds()).filter(v->v.isVar()).map(v->v._x()).filter(x->x!=null))
      .collect(Collectors.toSet());
    super.visitBlock(b);
    updatableXs=old;
    }
  }
  
  
public class WellFormedness extends PropagatorCollectorVisitor{
  @SuppressWarnings("serial")
  public static class NotWellFormed extends EndError{
    public NotWellFormed(Pos pos, String msg) { super(pos, msg);}
    }
  public static boolean of(Visitable<?> v){
    var tos=new WellFormedness();
    v.accept(tos);
    return true;
    }

  Pos lastPos;
  private final Error err(String msg){
    throw new NotWellFormed(lastPos,msg);
    }
  @Override public void visitE(Full.E e){lastPos=e.pos();super.visitE(e);}

  @Override public void visitVarTx(Full.VarTx d){
    assert lastPos!=null;
    super.visitVarTx(d);
    if(!d.isVar()){return;}
    Mdf _mdf=d._mdf();
    if(d._t()!=null){_mdf=d._t()._mdf();}
    if(_mdf==null){return;}
    if(!_mdf.isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.varBindingCanNotBe(_mdf));
    }
  
  @Override public void visitD(Core.D d){
    lastPos=d.e().pos();
    super.visitD(d);
    if(!d.isVar()){return;}
    if(!d.t().mdf().isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.varBindingCanNotBe(d.t().mdf()));    
    }    
  @Override public void visitS(S s){
    super.visitS(s);
    okXs(s.xs());
    }
  private void okXs(List<X>xs) { 
    for(X x:xs){
      if(x.inner().equals("this")){
       err(Err.duplicatedNameThis());}
      }
    long size=xs.stream().map(X::inner).distinct().count();
    if(size!=xs.size()){
      List<X> dups=dups(xs);
      err(Err.duplicatedName(dups));
      } 
    }
  private<T> List<T> dups(List<T> es) {
    Set<T> all=new HashSet<>(es);
    List<T> res=new ArrayList<>(es);
    for(T t:all) {res.remove(t);}
    return res;
  }
  public void visitPar(Full.Par par){
    super.visitPar(par);
    okXs(par.xs());
    if(par._that()==null){return;}
    if(par.xs().stream().map(X::inner).noneMatch(x->x.equals("that"))){return;}
    err(Err.duplicatedNameThat());
    }
  @Override public void visitBlock(Full.Block b){
    lastPos=b.pos();
    super.visitBlock(b);
    var domDs=FV.domFullDs(b.ds());
    okXs(domDs);
    declaredVariableNotUsedInCatch(b.ks(), domDs);
    declaredVariableNotRedeclared(b, domDs);
    if(b.isCurly()){Returning.ofBlock(b);return;}
    int minus=0;
    if(b._e()==null){
      var last=b.ds().get(b.ds().size()-1);
      assert last._varTx()==null && last.varTxs().isEmpty();
      minus=1;
      }
    for(int i:range(b.ds().size()-minus)){
      if(!Returning.of(b.ds().get(i)._e())){continue;}
      lastPos=b.ds().get(i)._e().pos();
      err(Err.deadCodeAfter(i));
      }
    for(int i:range(1,b.ds().size())){
      var di=b.ds().get(i);
      var dj=b.ds().get(i-1);
      if(di._varTx()!=null || !di.varTxs().isEmpty()){continue;}
      if(b.dsAfter()==i){continue;}
      if(!CheckBlockNeeded.of(dj._e(),true)){continue;}
      lastPos=dj._e().pos();
      err(Err.needBlock(dj._e()));
      }
    if(b.ks().isEmpty()){return;}
    if(b.ds().size()<=b.dsAfter()){
      if(b._e()==null){return;}
      }
    else{  
      Full.D firstAfter=b.ds().get(b.dsAfter());
      if(firstAfter._varTx()!=null || !firstAfter.varTxs().isEmpty()){return;}
      }
    Full.K kLast=b.ks().get(b.ks().size()-1);
    if(!CheckBlockNeeded.of(kLast.e(),false)){return;}
    lastPos=kLast.e().pos();
    err("expression need to be enclose in block to avoid ambiguities");
    }
  private void declaredVariableNotRedeclared(Full.Block b, List<X> domDs) {
    declaredVariableNotRedeclared(Stream.concat(Stream.concat(
      b.ds().stream().map(d->d._e()),//ds
      b.ks().stream().map(k->k.e())//ks
      ),Stream.of(b._e())),//e
      b.ks().stream().map(k->k._x()),
      domDs
      );
   }
  private void declaredVariableNotRedeclared(Core.Block b, List<X> domDs) {
    declaredVariableNotRedeclared(Stream.concat(Stream.concat(
      b.ds().stream().map(d->d.e()),//ds
      b.ks().stream().map(k->k.e())//ks
      ),Stream.of(b.e())),//e
      b.ks().stream().map(k->k.x()),
      domDs
      );
    }
  private void declaredVariableNotRedeclared(Stream<HasVisitable>s,Stream<X>kxs, List<X> domDs) {
    var redefined=Stream.concat(kxs,s
       .filter(e->e!=null)
       .flatMap(e->Bindings.of(e.visitable()).stream())
       )
     .filter(x->domDs.contains(x))
     .findFirst();
    if(redefined.isEmpty()){return;}
    err("binding "+redefined.get()+" is internally redefined");
    }

    private void declaredVariableNotUsedInCatch(List<? extends Visitable<?>> ks, List<X> domDs) {
      for(var ki:ks){
        var inside=FV.of(ki).stream().filter(x->domDs.contains(x)).findFirst();
        if(inside.isEmpty()){continue;}
        err("binding "+inside.get()+ " used in catch; it may not be initialized");
        }
      }
  @Override public void visitBlock(Core.Block b){
    lastPos=b.pos();
    super.visitBlock(b);
    var domDs=FV.domDs(b.ds());
    okXs(domDs);
    declaredVariableNotUsedInCatch(b.ks(), domDs);
    declaredVariableNotRedeclared(b, domDs);
    }    
  @Override public void visitK(Core.K k){
    lastPos=k.e().pos();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k.x());  
    }
  @Override public void visitK(Full.K k){
    lastPos=k.e().pos();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k._x());
    }
  private void wfKCommon(Visitable<?> v, X x) { var bindings=Bindings.of(v);
  if(x==null){return;}
  if(bindings.contains(x)){
    err("binding "+x+" is internally redefined");
    }
  if(x.inner().equals("this")){
    err("'this' can not be used as a name");
    } }

  @Override public void visitMI(Full.L.MI mi){
    lastPos=mi.pos();
    super.visitMI(mi);
    var pars=pushL(X.thisX,mi.s().xs());
    checkTopE(mi.e().visitable(),pars);
    var l=new ContainsFullL()._of(mi.e().visitable());
    if(l==null){return;}
    lastPos=l.pos();
    err("Method body can not contain a full library literal");
    }
  @Override public void visitNC(Full.L.NC nc){
    lastPos=nc.pos();
    super.visitNC(nc);
    checkTopE(nc.e().visitable(),L());
    }
  @Override public void visitMWT(Full.L.MWT mwt){
    lastPos=mwt.pos();
    super.visitMWT(mwt);
    if(mwt._e()==null){return;}
    var pars=pushL(X.thisX,mwt.mh().s().xs());
    checkTopE(mwt._e().visitable(),pars);
    var l=new ContainsFullL()._of(mwt._e().visitable());
    if(l==null){return;}
    lastPos=l.pos();
    err("Method body can not contain a full library literal");
    }
  @Override public void visitMWT(Core.L.MWT mwt){
    lastPos=mwt.pos();
    super.visitMWT(mwt);
    if(mwt._e()==null){return;}
    var pars=pushL(X.thisX,mwt.mh().s().xs());
    var fv=checkTopE(mwt._e().visitable(),pars);
    var parT=mwt.mh().parsWithThis();
    for(var i:range(pars)){
      if(!parT.get(i).mdf().isCapsule()){continue;}
      var xi=pars.get(i);
      long count=fv.stream().filter(x->x.equals(xi)).count();
      if(count<=1){continue;}
      err("capsule binding "+xi+" used more then once");
      }
    }
  private List<X> checkAllVariablesUsedInScope(Visitable<?> v, List<X> pars) {
    var fv=FV.of(v);
    var extra=fv.stream().filter(x->!pars.contains(x)).findFirst();
    if(extra.isPresent()){
      err("Used binding is not in scope: "+extra.get());
      }
    return fv;
    }
    private List<X> checkTopE(Visitable<?> v,List<X>pars) {
      var res=checkAllVariablesUsedInScope(v, pars);
      var x=new ContainsIllegalVarUpdate()._of(v);
      if(x!=null){
        lastPos=x.pos();
        err("name "+x+" is not declared as var, thus it can not be updated");
        }
      var sx=new ContainsSlashXOut()._of(v);
      if(sx==null){return res;}
      lastPos=sx.pos();
      throw err("term "+sx+" can only be used inside parameters");
      }
  @Override public void visitMH(Full.MH mh){
    super.visitMH(mh);
    checkAllEmptyMdfFull(mh.exceptions());
    if(mh._mdf()!=null && mh._mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err("method modifier can not be fwd imm or fwd mut");} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t()._mdf();
    if(tmdf!=null && ps.stream().anyMatch(ti->ti._mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err("unusable fwd parameter given return type "+mh.t()._mdf().inner);}
      }
    if(ps.stream().anyMatch(ti->ti._mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err("unusable fwd parameter given return type "+(tmdf==null?"imm":tmdf.inner));}
      }
    if(tmdf!=null && tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti._mdf()!=null && ti._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err("invalid fwd return type since there is no fwd parameter");}
      }   
    }
  @Override public void visitMH(Core.MH mh){
    super.visitMH(mh);
    checkAllEmptyMdf(mh.exceptions());
    if(mh.mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err("method modifier can not be fwd imm or fwd mut");} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t().mdf();
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err("unusable fwd parameter given return type "+mh.t().mdf().inner);}
      }
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err("unusable fwd parameter given return type "+tmdf.inner);}
      }
    if(tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err("invalid fwd return type since there is no fwd parameter");}
      }   
    }
  private void checkAllEmptyMdfFull(List<Full.T> ts){
    for(var ti:ts){if(ti._mdf()!=Mdf.Immutable){
        err("Error: implemented and exception types can not declare a modifier (and are implicitly imm)");
    }}}
  private void checkAllEmptyMdf(List<Core.T> ts){
    for(var ti:ts){if(ti.mdf()!=Mdf.Immutable){
        err("Error: implemented and exception types can not declare a modifier (and are implicitly imm)");
    }}}

  @Override public void visitIf(Full.If i){
    lastPos=i.pos();
    super.visitIf(i);
    for(var d:i.matches()){validMatch(d);}
    }
  private void validMatch(D d) {
    long ts=d.varTxs().stream()
    .filter(v->v!=null && v._t()!=null)
    .count();
    if(ts>0){return;}
    err("invalid 'if match': no type selected in "+d);
    }
    
  @Override public void visitFor(Full.For f){
    lastPos=f.pos();
    super.visitFor(f);    
    for(var d:f.ds()){
      for(var vtx:d.varTxs()){
        if(!vtx.isVar()){continue;}
        err("nested name "+vtx._x()+" is var; in a 'for' match only top level names can be var");
        }
      }
    }  
  @Override public void visitL(Full.L l){
    lastPos=l.pos();
    super.visitL(l);
    checkAllEmptyMdfFull(l.ts());
    long countM=l.ms().stream().map(m->m.key()).count();
    if(countM<l.ms().size()) {
      var dups=dups(l.ms().stream().map(m->m.key()).collect(Collectors.toList()));
      err("duplicated names: "+dups);
      }
    long countI=l.ts().stream().map(t->t.withDocs(L())).count();
    if(countI<l.ts().size()) {
      var dups=dups(l.ts().stream().map(t->t.withDocs(L())).collect(Collectors.toList()));
      err("duplicated names: "+dups);
      }
    for(var t:l.ts()){
      if(!P.pAny.equals(t._p())){continue;}
      err("duplicated names: [Any];  'Any' is implicitly present as an implemented interface");
      }
    //exists at most one n such that exists m::n(xs) where LL(m::n(xs))=MWT, and MWT.e? is empty
    Supplier<Stream<Integer>> privateAbstract=()->l.ms().stream()
      .filter(m->m instanceof Full.L.MWT)
      .map(m->(Full.L.MWT)m)
      .filter(m->m._e()==null && m.key().hasUniqueNum())
      .map(m->m.key().uniqueNum());
    if(privateAbstract.get().count()>1) {
      var dups=dups(privateAbstract.get().collect(Collectors.toList()));
      err("Only one private state number is allowed; but the following are used "+dups);
      }
    }
  @Override public void visitL(Core.L l){
    lastPos=l.pos();
    super.visitL(l);
    checkAllEmptyMdf(l.ts());
    }
  }
