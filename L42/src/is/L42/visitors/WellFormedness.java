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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.Block;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.E;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Core.EX;
import is.L42.generated.Core.K;
import is.L42.generated.Core.L;
import is.L42.generated.Core.Loop;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.OpUpdate;
import is.L42.generated.Core.PCastT;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.Throw;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.Full.Call;
import is.L42.generated.Full.Cast;
import is.L42.generated.Full.CsP;
import is.L42.generated.Full.D;
import is.L42.generated.Full.EPathSel;
import is.L42.generated.Full.EString;
import is.L42.generated.Full.For;
import is.L42.generated.Full.If;
import is.L42.generated.Full.Par;
import is.L42.generated.Full.Slash;
import is.L42.generated.Full.SlashX;
import is.L42.generated.Full.UOp;
import is.L42.generated.Full.VarTx;
import is.L42.generated.Full.While;
import is.L42.generated.Full.L.F;
import is.L42.generated.Full.L.MI;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Half.BinOp;
import is.L42.generated.Half.SlashCastT;
import is.L42.generated.Op.OpKind;
import is.L42.generated.ST.STMeth;
import is.L42.generated.ST.STOp;
import is.L42.generated.HasVisitable;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.typeSystem.Coherence;

class ContainsFullL extends Contains.SkipL<Full.L>{
  @Override public void visitL(Full.L l){setResult(l);}
  }
class ContainsSlashXOut extends Contains.SkipL<Full.SlashX>{
  @Override public void visitSlashX(Full.SlashX sx){setResult(sx);}
  @Override public void visitPar(Full.Par par){}
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
  public static boolean of(Visitable<?> v){
    var tos=new WellFormedness();
    v.accept(tos);
    return true;
    }
  HashSet<X> declared=new HashSet<>();
  HashSet<X> declaredHidden=new HashSet<>();
  HashSet<X> declaredVarFwd=new HashSet<>();
  HashSet<X> declaredVarError=new HashSet<>();
  HashSet<X> declaredVar=new HashSet<>();
  List<Pos> lastPos;
  private final Error err(String msg){
    throw new EndError.NotWellFormed(lastPos,msg);
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
  private static boolean degenerate(Full.E e){
    return
         e instanceof Core.EX 
      || e instanceof Full.CsP
      || e instanceof Core.EVoid
      || e instanceof LL
      || e instanceof Full.Slash
      || e instanceof Full.SlashX
      || e instanceof Full.EPathSel
      || e instanceof Full.Cast;
    }
  @Override public void visitD(Full.D d){
    if(d._e()!=null){lastPos=d._e().poss();}
    super.visitD(d);
    if(d._e()==null){return;}
    if(d._varTx()!=null || !d.varTxs().isEmpty()){return;}
    if(!degenerate(d._e())){return;}
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
    var domDs=FV.domFullDs(b.ds());
    var domMatches=FV.domFullDsOnlyMatchs(b.ds());
    var allVar=FV.domVarFullDs(b.ds());
    okXs(domDs);
    for(var x: domDs){if(declared.contains(x)){
      err(Err.redefinedName(x));
      }}
    var oldDeclared=new ArrayList<>(declared);
    declared.addAll(domDs);
    declaredHidden.addAll(domMatches);
    declaredVar.addAll(allVar);
    for(var v:allVar){if(!domMatches.contains(v)){declaredVarFwd.addAll(allVar);}}
    boolean isErr=b.ks().stream().anyMatch(k->k._thr()==ThrowKind.Error);
    if(isErr){declaredVarError.addAll(oldDeclared);}
    for(var d:b.ds()){
      visitD(d);
      if(d._varTx()!=null && d._varTx().isVar()){
        assert d._varTx()._x()!=null;
        declaredVarFwd.remove(d._varTx()._x());
        for(var vtx:d.varTxs()){
          assert vtx._x()!=null;
          declaredHidden.remove(vtx._x());
          }
        }
      }
    declaredHidden.addAll(domDs);
    visitFullKs(b.ks());
    visitFullTs(b.whoopsed());
    declaredHidden.removeAll(domDs);
    if(isErr){declaredVarError.removeAll(oldDeclared);}
    if(b._e()!=null){visitE(b._e());}
    declared.removeAll(domDs);
    declaredVar.removeAll(allVar);
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
    if(!b.whoopsed().isEmpty()){return;}
    Full.K kLast=b.ks().get(b.ks().size()-1);
    if(degenerate(kLast.e())){return;}
    if(!CheckBlockNeeded.of(kLast.e(),false)){return;}
    lastPos=kLast.e().poss();
    err(Err.needBlock(kLast.e()));
    }
  @Override public void visitBlock(Core.Block b){
    lastPos=b.poss();
    var domDs=FV.domDs(b.ds());
    var allVar=FV.domVarDs(b.ds());
    okXs(domDs);
    for(var x: domDs){if(declared.contains(x)){
      err(Err.redefinedName(x));
      }}
    declared.addAll(domDs);
    declaredVar.addAll(allVar);
    declaredVarFwd.addAll(allVar);
    boolean isErr=b.ks().stream().anyMatch(k->k.thr()==ThrowKind.Error);
    if(isErr){declaredVarError.addAll(allVar);}
    for(var d:b.ds()){
      visitD(d);
      if(d.isVar()){declaredVarFwd.remove(d.x());}
      }
    declaredHidden.addAll(domDs);
    visitKs(b.ks());
    declaredHidden.removeAll(domDs);
    if(isErr){declaredVarError.removeAll(allVar);}
    visitE(b.e());
    declared.removeAll(domDs);
    declaredVar.removeAll(allVar);
    }
  @Override public void visitK(Core.K k){
    lastPos=k.e().poss();
    preDeclare(false, k.x());
    super.visitK(k);
    postDeclare(false, k.x());  
    }
  @Override public void visitK(Full.K k){
    lastPos=k.e().poss();
    preDeclare(false, k._x());
    super.visitK(k);
    postDeclare(false, k._x());
    }
  private void preDeclare(boolean var,X x) {
    if(x==null){return;}
    if(declared.contains(x)){err(Err.redefinedName(x));}
    if(x.inner().equals("this")){err(Err.duplicatedNameThis());}
    declared.add(x);
    if(var){declaredVar.add(x);}
    }
  private void postDeclare(boolean var,X x) {
    if(x==null){return;}
    declared.remove(x);
    if(var){declaredVar.remove(x);}
    }
  private void preDeclare(Full.D d) {
    var v=d._varTx();
    var vs=d.varTxs();
    if(v!=null && v._x()!=null){preDeclare(v.isVar(),v._x());}
    for(var vi:vs){
      assert vi._x()!=null;
      preDeclare(vi.isVar(),vi._x());
      }
    }
  private void postDeclare(Full.D d) {
    var v=d._varTx();
    var vs=d.varTxs();
    if(v!=null && v._x()!=null){postDeclare(v.isVar(),v._x());}
    for(var vi:vs){
      assert vi._x()!=null;
      postDeclare(vi.isVar(),vi._x());
      }
    }
  @Override public void visitEX(Core.EX ex){//is also Full
    X x=ex.x();
    lastPos=ex.poss();
    if(!declared.contains(x)){err(Err.nameUsedNotInScope(x));}
    if(declaredHidden.contains(x)){err(Err.nameUsedInCatchOrMatch(x));}
    }    
  @Override public void visitF(Full.L.F f){
    super.visitF(f);
    if(f.t()._mdf()==null){return;}
    if(!f.t()._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(Err.invalidFieldType(f));
    }
  @Override public void visitMI(Full.L.MI mi){
    lastPos=mi.poss();
    var pars=pushL(X.thisX,mi.s().xs());
    declared.addAll(pars);    
    super.visitMI(mi);
    declared.removeAll(pars);
    checkTopE(mi.e().visitable(),pars);
    FV.of(mi.e().visitable());
    var l=new ContainsFullL()._of(mi.e().visitable());
    if(l==null){return;}
    lastPos=l.poss();
    err(Err.noFullL(l));
    }
  @Override public void visitNC(Full.L.NC nc){
    lastPos=nc.poss();
    super.visitNC(nc);
    checkTopE(nc.e().visitable(),L());
    FV.of(nc.e().visitable());
    }
  @Override public void visitMWT(Full.L.MWT mwt){
    lastPos=mwt.poss();
    var pars=pushL(X.thisX,mwt.mh().s().xs());
    declared.addAll(pars);    
    super.visitMWT(mwt);
    declared.removeAll(pars);
    if(mwt._e()==null){return;}
    checkTopE(mwt._e().visitable(),pars);
    checkCapsulesUsedOnlyOnce(mwt._e().visitable(),pars,
      mwt.mh().parsWithThis().stream().map(t->t._mdf()));
    var l=new ContainsFullL()._of(mwt._e().visitable());
    if(l==null){return;}
    lastPos=l.poss();
    err(Err.noFullL(l));
    }
  @Override public void visitMWT(Core.L.MWT mwt){
    lastPos=mwt.poss();
    var pars=pushL(X.thisX,mwt.mh().s().xs());
    declared.addAll(pars);
    super.visitMWT(mwt);
    declared.removeAll(pars);
    if(mwt._e()==null){return;}
    checkTopE(mwt._e().visitable(),pars);
    checkCapsulesUsedOnlyOnce(mwt._e().visitable(),pars,
      mwt.mh().parsWithThis().stream().map(t->t.mdf()));
    }
  private void checkCapsulesUsedOnlyOnce(Visitable<?> v,List<X> pars,Stream<Mdf> parMdfs){
    var fv = FV.of(v);
    int[]i={-1};
    parMdfs.forEach(mi->{
      i[0]+=1;
      if(mi!=Mdf.Capsule){return;} //mi may be null
      var xi=pars.get(i[0]);
      long count=fv.stream().filter(x->x.equals(xi)).count();
      if(count<=1){return;}
      err(Err.capsuleBindingUsedOnce(xi));
      });
    }
  private void checkTopE(Visitable<?> v,List<X>pars) {
    assert declared.isEmpty():
      declared;
    assert declaredHidden.isEmpty();
    assert declaredVar.isEmpty();
    assert declaredVarError.isEmpty();
    assert declaredVarFwd.isEmpty();
    declared.addAll(pars);
    v.accept(this);
    declared.removeAll(pars);
    var sx=new ContainsSlashXOut()._of(v);
    if(sx==null){return;}
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
    checkAllImmMdf(mh.exceptions());
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
  private void checkAllImmMdf(List<Core.T> ts){
    for(var ti:ts){if(ti.mdf()!=Mdf.Immutable){
        err(Err.tsMustBeImm());
    }}}

  @Override public void visitIf(Full.If i){
    lastPos=i.poss();
    var _c0=i._condition();
    if(_c0!=null){visitE(_c0);}
    visitFullDs(i.matches());
    for(var di:i.matches()){validMatch(di);preDeclare(di);}
    visitE(i.then());
    for(var di:i.matches()){postDeclare(di);}
    var _else0=i._else();    
    if(_else0!=null){visitE(_else0);}

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
  @Override public void visitOpUpdate(Full.OpUpdate op){
    var x=op.x();
    if(!declaredVar.contains(x)){err(Err.nonVarBindingOpUpdate(x,op.op().inner));}
    if(declaredVarFwd.contains(x)){err(Err.fwdVarBindingOpUpdate(x,op.op().inner));}
    if(declaredVarError.contains(x)){err(Err.errorVarBindingOpUpdate(x,op.op().inner));}
    }
  @Override public void visitBinOp(Full.BinOp binOp){
    lastPos=binOp.poss();
    super.visitBinOp(binOp);
    var es=binOp.es();
    Stream<Full.E> risks=es.stream();
    assert binOp.op().kind!=OpKind.OpUpdate;
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
    visitFullDs(f.ds());
    for(var d:f.ds()){
      preDeclare(d);
      for(var vtx:d.varTxs()){
        if(vtx.isVar()){err(Err.forMatchNoVar(vtx._x()));}
        }
      }
    visitE(f.body());
    for(var di:f.ds()){postDeclare(di);}
    }
  public void superVisitL(Full.L l){lastPos=l.poss();super.visitL(l);}  
  @Override public void visitL(Full.L l){
    lastPos=l.poss();
    new WellFormedness().superVisitL(l);
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
    var bridges=bridgeFull(l.ms());
    if(!bridges.isEmpty()){
      err(Err.bridgeMethodsInFullL(L(bridges.stream().map(m->m.key()))));
      }
    //if _.#$_(_) inside LL.MI.e then LL.MI.s of form #$_
    var notHDCallsHD=L(l.ms().stream()
      .filter(Full.L.MI.class::isInstance)
      .map(Full.L.MI.class::cast)
      .filter(m->isBridgeMeth(m.key().m(),"",m.e().visitable()))
      .map(m->m.key()));
    if(!notHDCallsHD.isEmpty()){err(Err.bridgeMethodsInFullL(notHDCallsHD));}
    List<LDom> dom=L(l.ms().stream().map(m->m.key()));
    List<LDom> impl=L(l.ms().stream()
      .filter(m->!(m instanceof Full.L.NC))
      .filter(m->m._e()!=null).map(m->m.key()));
    List<Integer> privateAbstract=L(l.ms().stream()
      .filter(m->m._e()==null && m.key().hasUniqueNum())
      .map(m->m.key().uniqueNum())
      .distinct());
    List<Visitable<?>> ts=L(l.ts().stream()
      .map(t->(Visitable<?>)t.with_mdf(null).withDocs(L())));
    common(l.isInterface(), ts, dom, impl, privateAbstract);
    }
  private <PP extends P> void typedContains(Core.L.Info info,Stream<PP> others,String name){
    var extra=L(others.filter(o->o.isNCs() && !info.typeDep().contains(o)));
    if(!extra.isEmpty()){
      throw new EndError.NotWellFormed(lastPos,Err.infoPathNotInTyped(name,extra));
      }
    }
  @Override public void visitInfo(Core.L.Info info){
    typedContains(info,info.coherentDep().stream(),"coherentDep");
    typedContains(info,info.hiddenSupertypes().stream(),"hiddenSupertypes");
    typedContains(info,info.nativePar().stream(),"nativePar");
    typedContains(info,info.usedMethods().stream().map(ps->ps.p()),"usedMethods");
    typedContains(info,info.watched().stream(),"watched");
    }
  public void superVisitL(Core.L l){lastPos=l.poss();super.visitL(l);}
  @Override public void visitL(Core.L l){
    lastPos=l.poss();
    new WellFormedness().superVisitL(l);
    checkAllImmMdf(l.ts());
    for(var m:l.ncs()){
      if(!m.key().hasUniqueNum()){continue;}
      validPrivateNested(m.poss(),m.key(),m.l());
      }

    var bridges=bridge(l.mwts());
    var classMhs=L(l.mwts().stream().filter(m->
      m.mh().mdf().isClass() && m._e()==null && m.key().hasUniqueNum()
      ).map(m->m.mh()));
    if(!classMhs.isEmpty()){
      var xzs=L(classMhs.stream().map(m->new HashSet<>(m.key().xs())).distinct());
      if(xzs.size()>1){throw new EndError.CoherentError(l.poss(),
        Err.nonCoherentNoSetOfFields(xzs));
        }
      }
    for(var mwt:bridges){
      if(!mwt.mh().mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Capsule)){
        err(Err.bridgeNotMutable(mwt.key(),mwt.mh().mdf()));
        }
      }
    for(var bi:bridges){for(var mhj:classMhs){ 
      if(!Coherence.canAlsoBe(mhj.t().mdf(),bi.mh().mdf())){continue;}
      if(mhj.key().m().startsWith("#$")){continue;}
      err(Err.bridgeViolatedByFactory(bi.key(),mhj.key()));
      }}
    boolean mustClose=!hasOpenState(l,bridges)
      || (l.isInterface() && l.mwts().stream().anyMatch(m->m.key().hasUniqueNum()))
      || (l.isInterface() && l.ts().stream().anyMatch(t->t.p().toNCs().hasUniqueNum()));
    if(mustClose){
      if(!l.info().close()){err(Err.mustHaveCloseStateBridge(
        L(classMhs.stream().map(m->m.key())),
        L(bridges.stream().map(m->m.key()))
        ));}
      }
    if(!l.info().nativeKind().isEmpty()){
      var t=TrustedKind._fromString(l.info().nativeKind());
      if(t==null){throw new EndError.NotWellFormed(l.poss(),Err.nativeKindInvalid(l.info().nativeKind()));}
      if(t.genericNumber()+t.genExceptionNumber()!=l.info().nativePar().size()){
        throw new EndError.NotWellFormed(l.poss(),Err.nativeKindParCountInvalid(t,t.genericNumber()+t.genExceptionNumber(),l.info().nativePar().size()));
        }
      }
    List<LDom> dom=L(Stream.concat(l.mwts().stream()
      .map(m->m.key()),l.ncs().stream().map(m->m.key())));
    List<LDom> impl=L(l.mwts().stream()
      .filter(m->m._e()!=null).map(m->m.key()));
    List<Integer> privateAbstract=L(l.mwts().stream()
      .filter(m->m._e()==null && m.key().hasUniqueNum())
      .map(m->m.key().uniqueNum())
      .distinct());
    List<Visitable<?>> ts=L(l.ts().stream().map(t->(Visitable<?>)t.p()));
    common(l.isInterface(), ts, dom, impl, privateAbstract);
    }
  void common(boolean isInterface,List<Visitable<?>> ts,List<LDom> dom, List<LDom> impl,List<Integer> privateAbstract){
    long countM=dom.stream().distinct().count();
    if(countM<dom.size()) {
      var dups=dups(dom);
      err(Err.duplicatedName(dups));
      }
    long countI=ts.stream().distinct().count();
    if(countI<ts.size()){err(Err.duplicatedName(dups(ts)));}
    for(var t:ts){
      boolean isAny=P.pAny.toString().equals(t.toString());
      if(isAny){err(Err.duplicatedNameAny());}
      };      
    if(privateAbstract.size()>1) {err(Err.singlePrivateState(privateAbstract));}
    if(isInterface){
      if(!impl.isEmpty()){err(Err.methodImplementedInInterface(impl));}
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
  private static boolean hasOpenState(Core.L l,List<Core.L.MWT>bridges){
    return hasOpenState(l.isInterface(),l.mwts(),bridges);
    }
  public static boolean hasOpenState(boolean isInterface,List<Core.L.MWT>mwts, List<Core.L.MWT>bridges){
    if(isInterface){return true;}
    if(!bridges.isEmpty()){return false;}
    for(var mwt:mwts){
      if(mwt._e()==null && mwt.key().hasUniqueNum()){return false;}
      }
    return true;
    }
  private static boolean isBridgeMeth(String m,String nativeUrl,Visitable<?>e){
    if(m.startsWith("#$")){return false;}
    boolean trustedHD=nativeUrl.startsWith("trusted:#$");
    boolean trusted=!trustedHD && nativeUrl.startsWith("trusted:");
    if(!nativeUrl.isEmpty() && !trusted){return true;}
    boolean[]res={false};
    e.accept(new PropagatorCollectorVisitor() {
      @Override public void visitS(S s){
        if(s.m().startsWith("#$")){res[0]=true;}
        }});
    return res[0];
    }
  public static List<Core.L.MWT> bridge(List<Core.L.MWT> mwts){
    return L(mwts.stream().filter(m->m._e()!=null &&
      isBridgeMeth(m.key().m(),m.nativeUrl(),m._e().visitable())));
    }
  private List<Full.L.MWT> bridgeFull(List<Full.L.M> ms){
    return L(ms.stream()
      .filter(Full.L.MWT.class::isInstance)
      .map(Full.L.MWT.class::cast)
      .filter(m-> m._e()!=null &&
        isBridgeMeth(m.key().m(),m.nativeUrl(),m._e().visitable())));
    }
  }
