package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Full;
import is.L42.generated.Full.D;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Op.OpKind;
import is.L42.generated.HasVisitable;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;

class ContainsFullL extends Contains.SkipL<Full.L>{
  @Override public void visitL(Full.L l){setResult(l);}
  }
class ContainsSlashXOut extends Contains.SkipL<Full.SlashX>{
  @Override public void visitSlashX(Full.SlashX sx){setResult(sx);}
  @Override public void visitPar(Full.Par par){}
  }
class ContainsIllegalVarUpdate extends Contains.SkipL<Core.EX>{
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
class AccumulateUnique extends Accumulate<Map<Integer,List<LL>>>{
  @Override public void visitL(Full.L l){
    super.visitL(l);
    accLL(l);
    }
  @Override public void visitL(Core.L l){
    super.visitL(l);
    accLL(l);
    }
  private void acc(LL l,LDom key){
    if (!key.hasUniqueNum()){return;}
    int u=key.uniqueNum();
    var val=acc().getOrDefault(u,new ArrayList<>());
    acc().put(u, val);//in case it was new
    val.add(l);
    }
  private void accLL(LL ll) { 
    if(ll.isFullL()){
      Full.L l=(Full.L)ll;
      for(var m:l.ms()){acc(ll,m.key());}
      }
    else{
      Core.L l=(Core.L)ll;
      for(var m:l.mwts()){acc(ll,m.key());}
      for(var m:l.ncs()){acc(ll,m.key());}
      }
    }
  @Override public Map<Integer, List<LL>> empty() {return new HashMap<>();}
  }
  
public class WellFormedness extends PropagatorCollectorVisitor{
  public static class NotWellFormed extends EndError{
    public NotWellFormed(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static boolean of(Visitable<?> v){
    var tos=new WellFormedness();
    v.accept(tos);
    return true;
    }

  List<Pos> lastPos;
  private final Error err(String msg){
    throw new NotWellFormed(lastPos,msg);
    }
  @Override public void visitE(Full.E e){lastPos=e.poss();super.visitE(e);}

  @Override public void visitVarTx(Full.VarTx d){
    assert lastPos!=null;
    super.visitVarTx(d);
    if(!d.isVar()){return;}
    Mdf _mdf=d._mdf();
    if(d._t()!=null){_mdf=d._t()._mdf();}
    if(_mdf==null){return;}
    if(!_mdf.isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.varBindingCanNotBe(_mdf.inner));
    }
  @Override public void visitD(Full.D d){
    if(d._e()!=null){lastPos=d._e().poss();}
    super.visitD(d);
    if(d._e()==null){return;}
    if(d._varTx()!=null || !d.varTxs().isEmpty()){return;}
    boolean degenerate=
         d._e() instanceof Core.EX 
      || d._e() instanceof Full.CsP
      || d._e() instanceof Core.EVoid
      || d._e() instanceof LL
      || d._e() instanceof Full.Slash
      || d._e() instanceof Full.SlashX
      || d._e() instanceof Full.EPathSel
      || d._e() instanceof Full.Cast;
    if(!degenerate){return;}
    err(Err.degenerateStatement(d._e()));
    }
  @Override public void visitD(Core.D d){
    lastPos=d.e().poss();
    super.visitD(d);
    if(!d.isVar()){return;}
    if(!d.t().mdf().isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.varBindingCanNotBe(d.t().mdf().inner));    
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
  @Override public void visitThrow(Full.Throw t){
    lastPos=t.poss();
    if(!Returning.of(t.e())){super.visitThrow(t);return;}
    err(Err.deadThrow(t.thr()));
    }
  @Override public void visitBlock(Full.Block b){
    lastPos=b.poss();
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
      lastPos=b.ds().get(i)._e().poss();
      err(Err.deadCodeAfter(i));
      }
    for(int i:range(1,b.ds().size())){
      var di=b.ds().get(i);
      var dj=b.ds().get(i-1);
      if(di._varTx()!=null || !di.varTxs().isEmpty()){continue;}
      if(b.dsAfter()==i){continue;}
      if(!CheckBlockNeeded.of(dj._e(),true)){continue;}
      lastPos=dj._e().poss();
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
    lastPos=kLast.e().poss();
    err(Err.needBlock(kLast.e()));
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
    var redefined=L(Stream.concat(kxs,s
       .filter(e->e!=null)
       .flatMap(e->Bindings.of(e.visitable()).stream())
       )
     .filter(x->domDs.contains(x)));
    if(redefined.isEmpty()){return;}
    err(Err.redefinedName(redefined));
    }

  private void declaredVariableNotUsedInCatch(List<? extends Visitable<?>> ks, List<X> domDs) {
    for(var ki:ks){
      var inside=FV.of(ki).stream().filter(x->domDs.contains(x)).findFirst();
      if(inside.isEmpty()){continue;}
      err(Err.nameUsedInCatch(inside.get()));
      }
    }
  @Override public void visitBlock(Core.Block b){
    lastPos=b.poss();
    super.visitBlock(b);
    var domDs=FV.domDs(b.ds());
    okXs(domDs);
    declaredVariableNotUsedInCatch(b.ks(), domDs);
    declaredVariableNotRedeclared(b, domDs);
    }    
  @Override public void visitK(Core.K k){
    lastPos=k.e().poss();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k.x());  
    }
  @Override public void visitK(Full.K k){
    lastPos=k.e().poss();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k._x());
    }
  private void wfKCommon(Visitable<?> v, X x) {
    var bindings=Bindings.of(v);
    if(x==null){return;}
    if(bindings.contains(x)){err(Err.redefinedName(L(x)));}
    if(x.inner().equals("this")){err(Err.duplicatedNameThis());}
    }
    
  @Override public void visitF(Full.L.F f){
    super.visitF(f);
    if(f.t()._mdf()==null){return;}
    if(!f.t()._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.invalidFieldType(f));
    }

  @Override public void visitMI(Full.L.MI mi){
    lastPos=mi.poss();
    super.visitMI(mi);
    var pars=pushL(X.thisX,mi.s().xs());
    checkTopE(mi.e().visitable(),pars);
    var l=new ContainsFullL()._of(mi.e().visitable());
    if(l==null){return;}
    lastPos=l.poss();
    err(Err.noFullL(l));
    }
  @Override public void visitNC(Full.L.NC nc){
    lastPos=nc.poss();
    super.visitNC(nc);
    checkTopE(nc.e().visitable(),L());
    }
  @Override public void visitMWT(Full.L.MWT mwt){
    lastPos=mwt.poss();
    super.visitMWT(mwt);
    if(mwt._e()==null){return;}
    var pars=pushL(X.thisX,mwt.mh().s().xs());
    checkTopE(mwt._e().visitable(),pars);
    var l=new ContainsFullL()._of(mwt._e().visitable());
    if(l==null){return;}
    lastPos=l.poss();
    err(Err.noFullL(l));
    }
  @Override public void visitMWT(Core.L.MWT mwt){
    lastPos=mwt.poss();
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
      err(Err.capsuleBindingUsedOnce(xi));
      }
    }
  private List<X> checkAllVariablesUsedInScope(Visitable<?> v, List<X> pars) {
    var fv=FV.of(v);
    var extra=fv.stream().filter(x->!pars.contains(x)).findFirst();
    if(extra.isPresent()){
      err(Err.nameUsedNotInScope(extra.get()));
      }
    return fv;
    }
    private List<X> checkTopE(Visitable<?> v,List<X>pars) {
      var res=checkAllVariablesUsedInScope(v, pars);
      var x=new ContainsIllegalVarUpdate()._of(v);
      if(x!=null){
        lastPos=x.poss();
        err(Err.unapdatable(x));
        }
      var sx=new ContainsSlashXOut()._of(v);
      if(sx==null){return res;}
      lastPos=sx.poss();
      throw err(Err.slashOut(sx));
      }
  @Override public void visitMH(Full.MH mh){
    super.visitMH(mh);
    checkAllEmptyMdfFull(mh.exceptions());
    if(mh._mdf()!=null && mh._mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err(Err.methodTypeMdfNoFwd());} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t()._mdf();
    if(tmdf!=null && ps.stream().anyMatch(ti->ti._mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err(Err.methodTypeNoFwdPar(mh.t()._mdf().inner));}
      }
    if(ps.stream().anyMatch(ti->ti._mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err(Err.methodTypeNoFwdPar(tmdf==null?"imm":tmdf.inner));}
      }
    if(tmdf!=null && tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti._mdf()!=null && ti._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err(Err.methodTypeNoFwdReturn());}
      }   
    }
  @Override public void visitMH(Core.MH mh){
    super.visitMH(mh);
    checkAllEmptyMdf(mh.exceptions());
    if(mh.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd,Mdf.MutablePFwd)){
      err(Err.methodTypeMdfNoFwd());} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t().mdf();
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.MutablePFwd)
        ||tmdf.isIn(Mdf.Immutable,Mdf.ImmutableFwd,Mdf.ImmutablePFwd);
      if(!res){err(Err.methodTypeNoFwdPar(mh.t().mdf().inner));}
      }
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.MutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err(Err.methodTypeNoFwdPar(tmdf.inner));}
      }
    if(tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err(Err.methodTypeNoFwdReturn());}
      }   
    }
  private void checkAllEmptyMdfFull(List<Full.T> ts){
    for(var ti:ts){if(ti._mdf()!=Mdf.Immutable){
        err(Err.tsMustBeImm());
    }}}
  private void checkAllEmptyMdf(List<Core.T> ts){
    for(var ti:ts){if(ti.mdf()!=Mdf.Immutable){
        err(Err.tsMustBeImm());
    }}}

  @Override public void visitIf(Full.If i){
    lastPos=i.poss();
    super.visitIf(i);
    for(var d:i.matches()){validMatch(d);}
    }
  private void validMatch(D d) {
    long ts=FV.allVarTx(L(d))
    .filter(v->v._t()!=null)
    .count();
    if(ts>0){return;}
    err(Err.ifMatchNoT(d));
    }
  private boolean isAnyVoidLibrary(Full.E e){
    if(!(e instanceof Full.CsP)){return false;}
    var csp=(Full.CsP)e;
    var p=csp._p();
    return p!=null && !p.isNCs();
    }
  @Override public void visitBinOp(Full.BinOp binOp){
    lastPos=binOp.poss();
    super.visitBinOp(binOp);
    var es=binOp.es();
    Stream<Full.E> risks=es.stream();
    // (a (b (c d)))//all but the last need to be checked for right associative ops
    // ((a b) c) d)//just the first one need to be checked for left associative ops
    if(binOp.op().kind==OpKind.DataRightOp){risks=risks.limit(es.size()-1);}
    else{risks=risks.limit(1);}
    var errs=L(risks.filter(this::isAnyVoidLibrary));
    if(errs.isEmpty()){return;}
    lastPos=binOp.poss();
    err(Err.noOperatorOnPrimitive(errs.get(0),binOp.op()));    
    }
  @Override public void visitFor(Full.For f){
    lastPos=f.poss();
    super.visitFor(f);    
    for(var d:f.ds()){
      for(var vtx:d.varTxs()){
        if(!vtx.isVar()){continue;}
        err(Err.forMatchNoVar(vtx._x()));
        }
      }
    }  
  @Override public void visitL(Full.L l){
    lastPos=l.poss();
    super.visitL(l);
    if(l.isDots() || !l.reuseUrl().isEmpty()){
      boolean broke=!l.docs().isEmpty() || l.ms().stream()
        .filter(m->m instanceof Full.L.F || m instanceof Full.L.MI)
        .count()!=0;
      if(broke){
        String kind="...";
        if(!l.isDots()){kind="reuse [ _ ]";}
        if(!l.docs().isEmpty()){err(Err.noDocWithReuseOrDots(kind,l.docs()));}
        var invalids=L(l.ms().stream().filter(m->m instanceof Full.L.F || m instanceof Full.L.MI));
        if(!invalids.isEmpty()){err(Err.invalidMemberWithReuseOrDots(kind,invalids));}
        }
      }
    checkAllEmptyMdfFull(l.ts());
    for(var m:l.ms()){
      if(!m.key().hasUniqueNum()){continue;}
      if(!(m instanceof Full.L.NC)){continue;}
      validPrivateNested(m.poss(),(C)m.key(),m._e());
      }   
    Supplier<Stream<LDom>> dom=()->l.ms().stream().map(m->m.key());
    Supplier<Stream<LDom>> impl=()->l.ms().stream()
      .filter(m->!(m instanceof Full.L.NC))
      .filter(m->m._e()!=null).map(m->m.key());
    Supplier<Stream<Integer>> privateAbstract=()->l.ms().stream()
      .filter(m->m._e()==null && m.key().hasUniqueNum())
      .map(m->m.key().uniqueNum())
      .distinct();
    Supplier<Stream<Visitable<?>>> ts=()->l.ts().stream()
      .map(t->(Visitable<?>)t.with_mdf(null).withDocs(L()));
    common(l.isInterface(), ts, dom, impl, privateAbstract);
    }
  @Override public void visitL(Core.L l){
    lastPos=l.poss();
    super.visitL(l);
    checkAllEmptyMdf(l.ts());
    for(var m:l.ncs()){
      if(!m.key().hasUniqueNum()){continue;}
      validPrivateNested(m.poss(),m.key(),m.l());
      }   
    Supplier<Stream<LDom>> dom=()->Stream.concat(l.mwts().stream()
      .map(m->m.key()),l.ncs().stream().map(m->m.key()));
    Supplier<Stream<LDom>> impl=()->l.mwts().stream()
      .filter(m->m._e()!=null).map(m->m.key());
    Supplier<Stream<Integer>> privateAbstract=()->l.mwts().stream()
      .filter(m->m._e()==null && m.key().hasUniqueNum())
      .map(m->m.key().uniqueNum())
      .distinct();
    Supplier<Stream<Visitable<?>>> ts=()->l.ts().stream().map(t->(Visitable<?>)t.p());
    common(l.isInterface(), ts, dom, impl, privateAbstract);
    }
    void common(
      boolean isInterface,
      Supplier<Stream<Visitable<?>>> ts,
      Supplier<Stream<LDom>> dom,
      Supplier<Stream<LDom>> impl,
      Supplier<Stream<Integer>> privateAbstract){
    long countM=dom.get().distinct().count();
    if(countM<dom.get().count()) {
      var dups=dups(L(dom.get()));
      err(Err.duplicatedName(dups));
      }
    long countI=ts.get().distinct().count();
    if(countI<ts.get().count()) {
      var dups=dups(L(ts.get()));
      err(Err.duplicatedName(dups));
      }
    ts.get().forEach(t->{
      boolean isAny=P.pAny.toString().equals(t.toString());
      if(isAny){err(Err.duplicatedNameAny());}
      });      
    if(privateAbstract.get().count()>1) {
      var uniqueNums=L(privateAbstract.get());
      err(Err.singlePrivateState(uniqueNums));
      }
    if(isInterface){
      if(impl.get().count()!=0){
        var mis=L(impl.get());
        err(Err.methodImplementedInInterface(mis));
        }
      }
    }
  private void validPrivateNested(List<Pos> pos,C key, Full.E e) {
    lastPos=pos;
    if(!(e instanceof Core.L)){err(Err.privateNestedNotCore(key));}
    var l=(Core.L)e;
    for(var m:l.ncs()){
      if(m.key().hasUniqueNum()){continue;}
      lastPos=m.poss();
      err(Err.privateNestedPrivateMember(m.key()));      
      }   
    for(var m:l.mwts()){
      if(m.key().hasUniqueNum()){continue;}
      if(l.info().refined().contains(m.key())){continue;}
      lastPos=m.poss();
      err(Err.privateNestedPrivateMember(m.key()));
      }       
    }
  @Override public void visitProgram(Program program) {
    //TODO: when typing is completed, there is more well formedness here
    if(!program.pTails.isEmpty()){
      program.pop().wf();
      return;
      }
    var map=new AccumulateUnique().of(program.top.visitable());
    for(var e:map.entrySet()){
      if(e.getValue().size()==1){continue;}
      lastPos=e.getValue().get(0).poss();
      List<Pos> morePos=L(e.getValue().subList(1, e.getValue().size()),(c,li)->{
        c.addAll(li.poss());});
      err(Err.nonUniqueNumber(e.getKey(),morePos));
      }
    } 
  }
