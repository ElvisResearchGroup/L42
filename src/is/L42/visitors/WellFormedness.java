package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.*;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Full.D;
import is.L42.generated.Op.OpKind;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.top.Deps;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.ProgramTypeSystem;

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
  static <T>void checkMissing(List<T> setAll,List<T> setSome,List<Pos> pos,Function<Object,String>err){
    var allGood=setSome.containsAll(setAll);
    if(allGood){return;}
    setAll=new ArrayList<T>(setAll);
    setAll.removeAll(setSome);
    throw new EndError.NotWellFormed(pos,err.apply(unique(setAll)));
    }
  public static boolean coherentInfo(Program p,Info i){
    i.accept(new PropagatorCollectorVisitor(){
      @Override public void visitP(P path){
        assert p.minimize(path)==path:
          path +" "+p.minimize(path);        
        }
      });
    assert i.usedMethods().stream().noneMatch(u->i.watched().contains(u.p()));
    return true;
    }
  public static boolean checkInfo(Program p,Core.L l){
    Deps deps=new Deps().collectDeps(p,l.mwts());
    deps.collectDepsNCs(p,l.ncs());    
    deps.collectDocs(p,l.docs());
    deps.collectTs(p,l.ts());
    ArrayList<S> refined=new ArrayList<>();
    Deps.collectRefined(p,refined);
    Info i=deps.toInfo(false);
    //l can be different from p().top because all nested stuff has been inited in l and not in p().top
    checkMissing(i.typeDep(),l.info().typeDep(),l.poss(),ErrMsg::missedTypeDep);
    checkMissing(i.coherentDep(),l.info().coherentDep(),l.poss(),ErrMsg::missedCoheDep);
    checkMissing(i.metaCoherentDep(),l.info().metaCoherentDep(),l.poss(),ErrMsg::missedMetaCoheDep);
    checkMissing(i.watched(),l.info().watched(),l.poss(),ErrMsg::missedWatched);
    checkMissing(i.hiddenSupertypes(),l.info().hiddenSupertypes(),l.poss(),ErrMsg::missedHiddenSupertypes);
    List<PathSel> usedNotWatched=L(i.usedMethods().stream().filter(ps->!l.info().watched().contains(ps.p())));
    checkMissing(usedNotWatched,l.info().usedMethods(),l.poss(),ErrMsg::missedUsedMethods);
    checkMissing(refined,L(l.mwts().stream().map(m->m.key())),l.poss(),ErrMsg::missedRefined);
    checkMissing(
      L(l.info().nativePar().stream().filter(pi->pi.isNCs()).map(pi->pi.toNCs())),
      l.info().coherentDep(),l.poss(),ErrMsg::missedCoheDep);
    boolean refinedExact=l.info().refined().containsAll(refined) && refined.containsAll(l.info().refined());
    if(!refinedExact){
      throw new EndError.NotWellFormed(l.poss(),ErrMsg.mismatchRefine(
        L(refined.stream().distinct()),l.info().refined()));
      }
    if(l.info().isTyped()){
      for(var pi:l.info().typeDep()){
        var li=p.of(pi,l.poss());//throw the right error if path not exists
        if(li.isFullL()){
          throw new EndError.NotWellFormed(l.poss(),ErrMsg.typeDependencyNotCore(p));
          }
        }
      ProgramTypeSystem.type(false,p.update(l,false));
      }
    checkInfoMeth(p,l);  
    return true;
    }
  public static boolean checkInfoMeth(Program p,Core.L l){
    for (var m:l.mwts()){
      if(m._e()==null){continue;}
      m._e().visitable().accept(new PropagatorCollectorVisitor(){
        @Override public void visitL(Core.L l){
          checkInfo(p.push(l),l);
          }
        });
      }
    return true;
    }
  public static boolean of(Visitable<?> v){
    var tos=new WellFormedness();
    v.accept(tos);
    return true;
    }
  public static boolean allMinimized(Program p,LDom _last,Core.L l){
    if (_last==null || _last instanceof S){p=p.push(l);}
    else{ p=p.push((C)_last,l);}
    new CloneVisitorWithProgram(p){
      @Override public P visitP(P path){
        assert p().minimize(path)==path:
        path+" "+p().minimize(path);
        return path;
        }
      }.visitL(p.topCore());
    return true;
    }
  HashSet<X> declared=new HashSet<>();
  HashSet<X> declaredHidden=new HashSet<>();
  HashSet<X> declaredVarFwd=new HashSet<>();
  HashSet<X> declaredVarError=new HashSet<>();
  HashSet<X> declaredVar=new HashSet<>();
  List<Pos> lastPos;
  boolean lastIsInterface;
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
    err(ErrMsg.varBindingCanNotBe(_mdf.inner));
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
  @Override public void visitC(C c){
    if(c.hasUniqueNum() && c.uniqueNum()==0){
      err(ErrMsg.zeroNumberForC(c));
      }
    }
  @Override public void visitD(Full.D d){
    if(d._e()!=null){lastPos=d._e().poss();}
    super.visitD(d);
    }
  void degenerated(Full.D d,Full.Block b,int next){
    if(d._e()==null){return;}
    if(d._varTx()!=null || !d.varTxs().isEmpty()){return;}
    if(!degenerate(d._e())){return;}
    assert d._e()!=null;
    lastPos=d._e().poss();
    if(b.ds().size()<=next){
      boolean noE=!b.ks().isEmpty() || !b.whoopsed().isEmpty() || !similarToPars(b._e());
      if(noE){err(ErrMsg.degenerateStatement(d._e()));}
      err(ErrMsg.degenerateStatement(d._e(),b._e()));
      }
    var dNext=b.ds().get(next);
    if(similarToPars(dNext)){err(ErrMsg.degenerateStatement(d._e(),dNext._e()));}
    err(ErrMsg.degenerateStatement(d._e()));
    //degenerate statements often happens for forgetting par names in meth calls.
    //for example in Debug(Point(3\,5\).xy()).
    //checking degenerate late gives better error msg (nested first)
    }
  boolean similarToPars(Full.E _e){
    if(_e==null){return false;}
    if(!(_e instanceof Full.Block)){return false;}
    var b=(Full.Block)_e;
    if(b.isCurly()){return false;}
    if(!b.ks().isEmpty() ||!b.whoopsed().isEmpty()){return false;}
    return !b.ds().isEmpty();
    }
  boolean similarToPars(Full.D d){
    if(d._varTx()!=null){return false;}
    return similarToPars(d._e());    
    }
  @Override public void visitD(Core.D d){
    lastPos=d.e().poss();
    super.visitD(d);
    if(!d.isVar()){return;}
    if(!d.t().mdf().isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(ErrMsg.varBindingCanNotBe(d.t().mdf().inner));    
    }    
  @Override public void visitS(S s){
    super.visitS(s);
    okXs(s.xs());
    }
  private void okXs(List<X>xs) { 
    for(X x:xs){
      if(x.inner().equals("this")){
       err(ErrMsg.duplicatedNameThis());}
      }
    long size=xs.stream().map(X::inner).distinct().count();
    if(size!=xs.size()){
      List<X> dups=dups(xs);
      err(ErrMsg.duplicatedName(dups));
      } 
    }
  public static <T> List<T> dups(List<T> es) {
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
    err(ErrMsg.duplicatedNameThat());
    }
  @Override public void visitThrow(Full.Throw t){
    lastPos=t.poss();
    if(!Returning.of(t.e())){super.visitThrow(t);return;}
    err(ErrMsg.deadThrow(t.thr()));
    }
  @Override public void visitBlock(Full.Block b){
    visitBlockDeclared(b);
    if(b.isCurly()){Returning.ofBlock(b);}
    else{checkDeadCode(b);checkNeedBlock(b);}
    for(var i:range(b.ds())){degenerated(b.ds().get(i),b,i+1);}
    }      
  void visitBlockDeclared(Full.Block b){    
    lastPos=b.poss();
    var domDs=FV.domFullDs(b.ds());
    var domMatches=FV.domFullDsOnlyMatchs(b.ds());
    var allVar=FV.domVarFullDs(b.ds());
    okXs(domDs);
    for(var x: domDs){if(declared.contains(x)){err(ErrMsg.redefinedName(x));}}
    var oldDeclared=new ArrayList<>(declared);
    declared.addAll(domDs);
    declaredHidden.addAll(domMatches);
    declaredVar.addAll(allVar);
    for(var v:allVar){if(!domMatches.contains(v)){declaredVarFwd.addAll(allVar);}}
    boolean isErr=b.ks().stream().anyMatch(k->k._thr()==ThrowKind.Error);
    if(isErr){declaredVarError.addAll(oldDeclared);}
    for(var d:b.ds()){
      visitD(d);
      assert d.varTxs().stream().allMatch(vtx->vtx._x()!=null);
      for(var vtx:d.varTxs()){declaredHidden.remove(vtx._x());}
      if(d._varTx()==null || !d._varTx().isVar()){continue;}
      assert d._varTx()._x()!=null;
      declaredVarFwd.remove(d._varTx()._x());
      }
    declaredHidden.addAll(domDs);
    visitFullKs(b.ks());
    visitFullTs(b.whoopsed());
    declaredHidden.removeAll(domDs);
    if(isErr){declaredVarError.removeAll(oldDeclared);}
    if(b._e()!=null){visitE(b._e());}
    declared.removeAll(domDs);
    declaredVar.removeAll(allVar);
    }
  void checkDeadCode(Full.Block b){
    int minus=0;
    if(b._e()==null){
      var last=b.ds().get(b.ds().size()-1);
      assert last._varTx()==null && last.varTxs().isEmpty();
      minus=1;
      }
    for(int i:range(b.ds().size()-minus)){
      if(!Returning.of(b.ds().get(i)._e())){continue;}
      lastPos=b.ds().get(i)._e().poss();
      err(ErrMsg.deadCodeAfter(i));
      }
    }
  void checkNeedBlock(Full.Block b){
    for(int i:range(1,b.ds().size())){
      var di=b.ds().get(i);
      var dj=b.ds().get(i-1);
      if(di._varTx()!=null || !di.varTxs().isEmpty()){continue;}
      if(b.dsAfter()==i){continue;}
      if(!CheckBlockNeeded.of(dj._e(),true)){continue;}
      lastPos=dj._e().poss();
      err(ErrMsg.needBlock(dj._e()));
      }
    if(!b.ds().isEmpty() && b.ks().isEmpty() && b._e()!=null){
      var lastD=b.ds().get(b.ds().size()-1);
      if(!CheckBlockNeeded.of(lastD._e(),true)){return;}
      lastPos=lastD._e().poss();
      err(ErrMsg.needBlock(lastD._e()));
      }
    if(b.ks().isEmpty()){return;}
    var hasAfter=b.ds().size()>b.dsAfter();
    if(!hasAfter && b._e()==null){return;}
    if(hasAfter){
      Full.D firstAfter=b.ds().get(b.dsAfter());
      if(firstAfter._varTx()!=null || !firstAfter.varTxs().isEmpty()){return;}
      }
    if(!b.whoopsed().isEmpty()){return;}
    Full.K kLast=b.ks().get(b.ks().size()-1);
    if(degenerate(kLast.e())){return;}
    if(!CheckBlockNeeded.of(kLast.e(),false)){return;}
    lastPos=kLast.e().poss();
    err(ErrMsg.needBlock(kLast.e()));    
    }
  @Override public void visitBlock(Core.Block b){
    lastPos=b.poss();
    var domDs=FV.domDs(b.ds());
    var allVar=FV.domVarDs(b.ds());
    okXs(domDs);
    for(var x: domDs){if(declared.contains(x)){
      err(ErrMsg.redefinedName(x));
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
    if(declared.contains(x)){err(ErrMsg.redefinedName(x));}
    if(x.inner().equals("this")){err(ErrMsg.duplicatedNameThis());}
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
    if(d._e()!=null && v!=null && v._x()!=null){preDeclare(v.isVar(),v._x());}
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
    if(!declared.contains(x)){err(ErrMsg.nameUsedNotInScope(x));}
    if(declaredHidden.contains(x)){err(ErrMsg.nameUsedInCatchOrMatch(x));}
    }    
  @Override public void visitF(Full.L.F f){
    super.visitF(f);
    if(f._e()!=null){visitE(f._e());}
    if(f.t()._mdf()==null){return;}
    if(!f.t()._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err(ErrMsg.invalidFieldType(f));
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
    err(ErrMsg.noFullL(l));
    }
  @Override public void visitNC(Full.L.NC nc){
    lastPos=nc.poss();
    super.visitNC(nc);
    checkTopE(nc.e().visitable(),L());
    FV.of(nc.e().visitable());
    }
  @Override public void visitMWT(Full.L.MWT mwt){
    lastPos=mwt.poss();
    if(mwt.key().hasUniqueNum() && mwt.key().uniqueNum()!=0 && mwt._e()==null){
      err(ErrMsg.zeroPrivateState(mwt.key().uniqueNum()));
      }
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
    err(ErrMsg.noFullL(l));
    }
  @Override public void visitMWT(Core.L.MWT mwt){
    lastPos=mwt.poss();
    boolean privateState=mwt.key().hasUniqueNum() && mwt._e()==null;
    if(privateState && lastIsInterface){
      if(mwt.key().uniqueNum()==0){err(ErrMsg.zeroInterfaceMethod(mwt.key()));}
      }
    if(privateState && !lastIsInterface){
      if(mwt.key().uniqueNum()!=0){err(ErrMsg.zeroPrivateState(mwt.key()));}
      }
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
      err(ErrMsg.capsuleBindingUsedOnce(xi));
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
    throw err(ErrMsg.slashOut(sx));
    }
  @Override public void visitMH(Full.MH mh){
    super.visitMH(mh);
    checkAllEmptyMdfFull(mh.exceptions());
    if(mh._mdf()!=null && mh._mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err(ErrMsg.methodTypeMdfNoFwd());} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t()._mdf();
    if(tmdf!=null && ps.stream().anyMatch(ti->ti._mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err(ErrMsg.methodTypeNoFwdPar(mh.t()._mdf().inner));}
      }
    if(ps.stream().anyMatch(ti->ti._mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err(ErrMsg.methodTypeNoFwdPar(tmdf==null?"imm":tmdf.inner));}
      }
    if(tmdf!=null && tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti._mdf()!=null && ti._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err(ErrMsg.methodTypeNoFwdReturn());}
      }   
    }
  @Override public void visitMH(Core.MH mh){
    super.visitMH(mh);
    checkAllImmMdf(mh.exceptions());
    if(mh.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd,Mdf.MutablePFwd)){
      err(ErrMsg.methodTypeMdfNoFwd());} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t().mdf();
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.MutablePFwd)
        ||tmdf.isIn(Mdf.Immutable,Mdf.ImmutableFwd,Mdf.ImmutablePFwd);
      if(!res){err(ErrMsg.methodTypeNoFwdPar(mh.t().mdf().inner));}
      }
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.MutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err(ErrMsg.methodTypeNoFwdPar(tmdf.inner));}
      }
    if(tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err(ErrMsg.methodTypeNoFwdReturn());}
      }   
    }
  private void checkAllEmptyMdfFull(List<Full.T> ts){
    for(var ti:ts){if(ti._mdf()!=Mdf.Immutable){
        err(ErrMsg.tsMustBeImm());
    }}}
  private void checkAllImmMdf(List<Core.T> ts){
    for(var ti:ts){if(ti.mdf()!=Mdf.Immutable){
        err(ErrMsg.tsMustBeImm());
    }}}

  @Override public void visitMCall(Core.MCall m){
    super.visitMCall(m);
    var rec=m.xP();
    boolean wrong=m.s().hasUniqueNum() && m.s().uniqueNum()==0
      && rec instanceof Core.EX && !((Core.EX)rec).x().equals(X.thisX); 
    if(wrong){err(ErrMsg.zeroNumberForNonThis(m));}
    }
  @Override public void visitIf(Full.If i){
    lastPos=i.poss();
    var _c0=i._condition();
    if(_c0!=null){visitE(_c0);}
    visitFullDs(i.matches());
    for(var di:i.matches()){
      validMatch(di);
      if(di._e()!=null){preDeclare(di);}
      //else it is of form x<:T
      }
    visitE(i.then());
    for(var di:i.matches()){
      if(di._e()!=null){postDeclare(di);}
      }
    var _else0=i._else();    
    if(_else0!=null){visitE(_else0);}

    }
  private void validMatch(D d) {
    long ts=FV.allVarTx(L(d))
    .filter(v->v._t()!=null)
    .count();
    if(ts>0){return;}
    err(ErrMsg.ifMatchNoT(d));
    }
  private boolean isAnyVoidLibrary(Full.E e){
    if(!(e instanceof Full.CsP)){return false;}
    var csp=(Full.CsP)e;
    var p=csp._p();
    return p!=null && !p.isNCs();
    }
  @Override public void visitOpUpdate(Full.OpUpdate op){
    super.visitOpUpdate(op);
    var x=op.x();
    if(!declaredVar.contains(x)){err(ErrMsg.nonVarBindingOpUpdate(x,op.op().inner));}
    if(declaredVarFwd.contains(x)){err(ErrMsg.fwdVarBindingOpUpdate(x,op.op().inner));}
    if(declaredVarError.contains(x)){err(ErrMsg.errorVarBindingOpUpdate(x,op.op().inner));}
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
    err(ErrMsg.noOperatorOnPrimitive(errs.get(0),binOp.op()));    
    }
  @Override public void visitFor(Full.For f){
    lastPos=f.poss();
    visitFullDs(f.ds());
    for(var d:f.ds()){
      preDeclare(d);
      for(var vtx:d.varTxs()){
        if(vtx.isVar()){err(ErrMsg.forMatchNoVar(vtx._x()));}
        }
      }
    visitE(f.body());
    for(var di:f.ds()){postDeclare(di);}
    }
  public void superVisitL(Full.L l){
    lastPos=l.poss();
    lastIsInterface=l.isInterface();
    super.visitL(l);
    }
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
        if(!l.docs().isEmpty()){err(ErrMsg.noDocWithReuseOrDots(kind,l.docs()));}
        var invalids=L(l.ms().stream().filter(m->m instanceof Full.L.F || m instanceof Full.L.MI));
        if(!invalids.isEmpty()){err(ErrMsg.invalidMemberWithReuseOrDots(kind,invalids));}
        }
      }
    checkAllEmptyMdfFull(l.ts());
    for(var m:l.ms()){
      if(!m.key().hasUniqueNum()){continue;}
      if(!(m instanceof Full.L.NC)){continue;}
      validPrivateNested(m.poss(),(C)m.key(),m._e());
      }
    var bridges=bridgeFull(l.ms());
    if(!bridges.isEmpty()){wellFormednessFullBridges(l,bridges);}
    List<LDom> dom=L(l.ms().stream().map(m->m.key()));
    List<LDom> impl=L(l.ms().stream()
      .filter(m->!(m instanceof Full.L.NC))
      .filter(m->m._e()!=null).map(m->m.key()));
    List<Visitable<?>> ts=L(l.ts().stream()
      .map(t->(Visitable<?>)t.with_mdf(null).withDocs(L())));
    common(l.isInterface(), ts, dom, impl);
    }
  private void wellFormednessFullBridges(Full.L l,List<Full.L.M> bridges) {
    var anyMi=l.ms().stream().anyMatch(mi->mi instanceof Full.L.MI);
    if(anyMi){err(ErrMsg.bridgeMethodsInFullL(L(bridges.stream().map(m->m.key()))));}
    List<Full.MH> classMhs=L(l.ms(),(c,m)->{
      if(!(m instanceof Full.L.MWT)){return;}
      var mwt=(Full.L.MWT)m;
      if(mwt.mh()._mdf()==null || !mwt.mh()._mdf().isClass() || mwt._e()!=null){return;}
      if(mwt.key().hasUniqueNum()){c.add(mwt.mh());}
      });    
    if(classMhs.isEmpty()){
      err(ErrMsg.mustHaveCloseStateBridge(
        L(classMhs.stream().map(m->m.key())),
        L(bridges.stream().map(m->m.key()))));      
      }    
    for(var m:bridges){
      var mwt=(Full.L.MWT)m;
      if(mwt.mh()._mdf()==null){err(ErrMsg.bridgeNotMutable(mwt.key(),"imm"));}
      else if(!mwt.mh()._mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Capsule)){err(ErrMsg.bridgeNotMutable(mwt.key(),mwt.mh()._mdf()));}
      }
    if(!l.isInterface()){
      if(!classMhs.isEmpty()){
        var xzs=L(classMhs.stream().map(m->new HashSet<>(m.key().xs())).distinct());
        if(xzs.size()>1){throw new EndError.CoherentError(l.poss(),ErrMsg.nonCoherentNoSetOfFields(xzs));}
        }
      for(var mbi:bridges){for(var mhj:classMhs){
        var bi=(Full.L.MWT)mbi;
        if(!Coherence.canAlsoBe(mhj.t()._mdf(),bi.mh()._mdf())){continue;}
        if(mhj.key().m().startsWith("#$")){continue;}
        err(ErrMsg.bridgeViolatedByFactory(bi.key(),mhj.key()));
        }}
      }    
    }
  private <PP extends P> void typedContains(Core.L.Info info,Stream<PP> others,String name){
    var extra=L(others.filter(o->o.isNCs() && !info.typeDep().contains(o)));
    if(!extra.isEmpty()){
      throw new EndError.NotWellFormed(lastPos,ErrMsg.infoPathNotInTyped(name,extra));
      }
    }
  @Override public void visitInfo(Core.L.Info info){
    typedContains(info,info.coherentDep().stream(),"coherentDep");
    typedContains(info,info.metaCoherentDep().stream(),"metaCoherentDep");
    typedContains(info,info.hiddenSupertypes().stream(),"hiddenSupertypes");
    typedContains(info,info.nativePar().stream(),"nativePar");
    typedContains(info,info.usedMethods().stream().map(ps->ps.p()),"usedMethods");
    typedContains(info,info.watched().stream(),"watched");
    for(var p:info.watched()){
      if(p.hasUniqueNum()){throw new EndError.NotWellFormed(lastPos,ErrMsg.noUniqueWatch(p));}
      if(p.equals(P.pThis0)){throw new EndError.NotWellFormed(lastPos,ErrMsg.noSelfWatch());}
      for(var ps:info.usedMethods()){
        if(!p.equals(ps.p())){continue;}
        throw new EndError.NotWellFormed(lastPos,ErrMsg.infoWatchedUsedDisjoint(p));
        }
      }
    }
  public void superVisitL(Core.L l){
    lastPos=l.poss();
    lastIsInterface=l.isInterface();
    super.visitL(l);
    }
  @Override public void visitL(Core.L l){
    lastPos=l.poss();
    new WellFormedness().superVisitL(l);
    checkAllImmMdf(l.ts());
    for(var m:l.ncs()){
      if(!m.key().hasUniqueNum()){continue;}
      validPrivateNested(m.poss(),m.key(),m.l());
      }
    var bridges=bridge(l.mwts());
    boolean mustClose=!hasOpenState(l,bridges);
    var classMhs=L(l.mwts().stream().filter(m->
        m.mh().mdf().isClass() && m._e()==null && m.key().hasUniqueNum()
        ).map(m->m.mh()));
    for(var mwt:bridges){
      if(!mwt.mh().mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Capsule)){
        err(ErrMsg.bridgeNotMutable(mwt.key(),mwt.mh().mdf()));
        }
      }
    if(!l.isInterface()){
      if(!classMhs.isEmpty()){
        var xzs=L(classMhs.stream().map(m->new HashSet<>(m.key().xs())).distinct());
        if(xzs.size()>1){throw new EndError.CoherentError(l.poss(),
          ErrMsg.nonCoherentNoSetOfFields(xzs));
          }
        }
      for(var bi:bridges){for(var mhj:classMhs){ 
        if(!Coherence.canAlsoBe(mhj.t().mdf(),bi.mh().mdf())){continue;}
        if(mhj.key().m().startsWith("#$")){continue;}
        err(ErrMsg.bridgeViolatedByFactory(bi.key(),mhj.key()));
        }}
      }
    if(!mustClose && l.isInterface()){
       mustClose= l.mwts().stream().anyMatch(m->m.key().hasUniqueNum())
        || l.ts().stream().anyMatch(t->t.p().hasUniqueNum());
      }
    if(mustClose){
      if(!l.info().close()){err(ErrMsg.mustHaveCloseStateBridge(
        L(classMhs.stream().map(m->m.key())),
        L(bridges.stream().map(m->m.key()))
        ));}
      }
    if(!l.info().nativeKind().isEmpty()){
      var t=TrustedKind._rawFromString(l.info().nativeKind());
      if(t==null){throw new EndError.NotWellFormed(l.poss(),ErrMsg.nativeKindInvalid(l.info().nativeKind()));}
      if(t.genericNumber()+t.genExceptionNumber()!=l.info().nativePar().size()){
        throw new EndError.NotWellFormed(l.poss(),ErrMsg.nativeKindParCountInvalid(t,t.genericNumber()+t.genExceptionNumber(),l.info().nativePar().size()));
        }
      }
    for(MWT mwt:l.mwts()){
      String nativeUrl=mwt.nativeUrl();
      if(!nativeUrl.startsWith("trusted:")){continue;}
      String nativeOp=nativeUrl.substring("trusted:".length());
      var op=TrustedOp._fromString(nativeOp);
      if(op==null){throw new EndError.NotWellFormed(l.poss(),
        ErrMsg.nativeTrustedOpInvalid(nativeOp));}
      var mw=op.nativeMustWatch(mwt,l.info());
      checkMissing(mw,merge(l.info().watched(),L(P.pThis0)),l.poss(),ErrMsg::missedWatchedNative);
      }
    List<LDom> dom=L(Stream.concat(l.mwts().stream()
      .map(m->m.key()),l.ncs().stream().map(m->m.key())));
    List<LDom> impl=L(l.mwts().stream()
      .filter(m->m._e()!=null).map(m->m.key()));
    List<Visitable<?>> ts=L(l.ts().stream().map(t->(Visitable<?>)t.p()));
    common(l.isInterface(), ts, dom, impl);
    }
  private static boolean isThis0(String s){return s.equals("This") || s.equals("This0");}
  void common(boolean isInterface,List<Visitable<?>> ts,List<LDom> dom, List<LDom> impl){
    if(ts.stream().anyMatch(t->isThis0(t.toString()))){err(ErrMsg.interfaceImplementsItself(ts));}
    long countM=dom.stream().distinct().count();
    if(countM<dom.size()) {
      var dups=dups(dom);
      err(ErrMsg.duplicatedName(dups));
      }
    long countI=ts.stream().distinct().count();
    if(countI<ts.size()){err(ErrMsg.duplicatedName(dups(ts)));}
    for(var t:ts){
      boolean isAny=P.pAny.toString().equals(t.toString());
      if(isAny){err(ErrMsg.duplicatedNameAny());}
      };      
    if(isInterface){
      if(!impl.isEmpty()){err(ErrMsg.methodImplementedInInterface(impl));}
      }
    }
  private void validPrivateNested(List<Pos> pos,C key, Full.E e) {
    lastPos=pos;
    if(!(e instanceof Core.L)){err(ErrMsg.privateNestedNotCore(key));}
    var l=(Core.L)e;
    for(var m:l.ncs()){
      if(m.key().hasUniqueNum()){continue;}
      lastPos=m.poss();
      err(ErrMsg.privateNestedPrivateMember(m.key()));      
      }   
    for(var m:l.mwts()){
      if(m.key().hasUniqueNum()){continue;}
      if(l.info().refined().contains(m.key())){continue;}
      lastPos=m.poss();
      err(ErrMsg.privateNestedPrivateMember(m.key()));
      }       
    }
  private static boolean hasOpenState(Core.L l,List<Core.L.MWT>bridges){
    return hasOpenState(l.isInterface(),l.mwts(),l.ncs(),bridges);
    }
  public static boolean hasOpenState(boolean isInterface,List<Core.L.MWT>mwts,List<Core.L.NC>ncs, List<Core.L.MWT>bridges){
    if(mwts.stream().anyMatch(m->m.key().uniqueNum()==0)){return false;}
    if(ncs.stream().anyMatch(n->n.key().uniqueNum()==0)){return false;}
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
    e.accept(new Accumulate.SkipL<Void>(){
      @Override public void visitS(S s){
        if(s.m().startsWith("#$")){res[0]=true;}
        }});
    return res[0];
    }
  public static List<Core.L.MWT> bridge(List<Core.L.MWT> mwts){
    return L(mwts.stream().filter(m->m._e()!=null &&
      isBridgeMeth(m.key().m(),m.nativeUrl(),m._e().visitable())));
    }
  private List<Full.L.M> bridgeFull(List<Full.L.M> ms){
    return L(ms,(c,m)->{
      if(m instanceof Full.L.NC){return;}
      if(m._e()==null || m instanceof Full.L.F){return;}
      if(m instanceof Full.L.MI){
        var mi=(Full.L.MI)m;
        if(isBridgeMeth(mi.key().m(),"",m._e().visitable())){c.add(m);}
        return;
        }
      var mwt=(Full.L.MWT)m;
      if(isBridgeMeth(mwt.key().m(),mwt.nativeUrl(),m._e().visitable())){c.add(m);}
      });
    }
  }
