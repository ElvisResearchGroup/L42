package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import is.L42.common.Err;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.Core.EVoid;

public class InjectionToCore extends UndefinedCollectorVisitor{
  public StringBuilder errors;
  public EVoid eVoid;
  Core.E result=null;
  public InjectionToCore(StringBuilder errors, EVoid eVoid) {
    this.errors = errors;
    this.eVoid = eVoid;
  }
  private Core.L makeErr(Pos pos,Core.L.Info info, String hint){
    if(info==null){return null;}
    String err=info.toString();
    var errRes=new Core.L(pos,false, L(),L(),L(),info,L());
    err=Err.trimExpression(err);
    err="line " + pos.line() + ":" + pos.column() 
      + " Error: Extraneus token "+err+"\n"+hint;
    errors.append(err);
    return errRes;
    }
  @Override public void visitEX(Core.EX x){result=x;}
  @Override public void visitEVoid(Core.EVoid eVoid){result=eVoid;}
  @Override public void visitL(Core.L l){result=l;}
  @Override public void visitL(Full.L l){result=_inject(l,null);}

  @Override public void visitCast(Full.Cast cast){
    if(!(cast.e() instanceof Full.CsP)){result=null;return;}
    P p=_inject((Full.CsP)cast.e());
    var t=_inject(cast.t());
    if(p==null || t==null){result=null;return;}
    result=new Core.PCastT(cast.pos(), p, t);
    }
        
  @Override public void visitCall(Full.Call call){
    if(call.isSquare() || call._s()==null ||call._s().m().isEmpty()) {result=null;return;}
    if(call.pars().get(0)._that()!=null){result=null;return;}
    var r=_inject(call.e());
    if(r==null || !(r instanceof Core.XP)){result=null;return;}
    var es=_injectL(call.pars().get(0).es(),this::_inject);
    if(es==null){result=null;return;}
    S s=call._s().withXs(call.pars().get(0).xs());
    result=new Core.MCall(call.pos(),(Core.XP)r, s, es); 
    }

  @Override public void visitBlock(Full.Block b){
    if(b.isCurly() || b.ds().size()!=b.dsAfter()){result=null;return;}
    if(b._e()==null || !b.whoopsed().isEmpty()){result=null;return;}
    var ds=_injectL(b.ds(),this::_inject);
    var ks=_injectL(b.ks(),this::_inject);
    var e=_inject(b._e());
    if(ds==null||ks==null ||e==null){result=null;return;}
    result=new Core.Block(b.pos(), ds, ks, e);
    }
  @Override public void visitLoop(Full.Loop loop){
    var r=_inject(loop.e());
    if(r==null){return;}
    result=new Core.Loop(loop.pos(),r);
    }
  @Override public void visitThrow(Full.Throw thr){
    var r=_inject(thr.e());
    if(r==null){return;}
    result=new Core.Throw(thr.pos(),thr.thr(),r);
    }
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
    if(opUpdate.op()!=Op.ColonEqual){result=null;return;}
    var r=_inject(opUpdate.e());
    if(r==null){return;}
    result=new Core.OpUpdate(opUpdate.pos(),opUpdate.x(),r);
    }
  public Core.L _inject(Full.L res,Core.L.Info info) {
    if(res.isDots() || !res.reuseUrl().isEmpty()){
      return makeErr(res.pos(),info,"no dots or reuse in core libraries");
      }
    long cut=res.ms().stream()
      .takeWhile(m->!(m instanceof Full.L.NC))
      .count();
    var mwts=res.ms().stream().limit(cut)
      .filter(m->m instanceof Full.L.MWT)
      .map(m->(Full.L.MWT)m)
      .collect(Collectors.toList());
    var ncs=res.ms().stream().skip(cut)
      .filter(m->m instanceof Full.L.NC)
      .map(m->(Full.L.NC)m)
      .collect(Collectors.toList());
    if(mwts.size()+ncs.size()!=res.ms().size()){
      return makeErr(res.pos(),info,"only methods with type and nested classes in core libraries");
      }
    List<Core.T>cts=_injectL(res.ts(),this::_inject);
    List<Core.L.MWT>cmwts=L(mwts,(c,mi)->c.add(_inject(mi)));
    List<Core.L.NC>cncs=L(ncs,(c,ni)->c.add(_inject(ni)));
    List<Core.Doc>cdocs=_injectL(res.docs(),this::_inject);
    if(cts==null){
      return makeErr(res.pos(),info,"invalid implemented type for core library");
      }      
    if(cdocs==null){
      return makeErr(res.pos(),info,"invalid docs for core library");
      }      
    if(cncs.contains(null)){
      return makeErr(res.pos(),info,"invalid nested class for core library");
      }      
    if(cmwts.contains(null)){
      return makeErr(res.pos(),info,"invalid method with type for core library");
      }      
    return new Core.L(res.pos(),res.isInterface(), cts, cmwts, cncs, info, cdocs); 
    }
  public Core.E _inject(Full.E e) {
    visitE(e);return this.result;
    }

  public Core.T _inject(Full.T t) {
    if(t==null){return null;}
    List<Core.Doc>docs=_injectL(t.docs(),this::_inject);
    if(docs==null){return null;}
    if(t._p()==null){return null;}
    assert t.cs().isEmpty(); 
    return new Core.T(_inject(t._mdf()), docs, t._p());    
    }
  public Core.L.MWT _inject(Full.L.MWT mwt) {
    try{
      var docs=_injectL(mwt.docs(),this::_inject);
      var mh=_inject(mwt.mh());
      if(docs==null || mh==null){return null;}
      if(mwt._e()==null){return new Core.L.MWT(mwt.pos(), docs, mh, mwt.nativeUrl(),null);}
      mwt._e().visitable().accept(this);
      if(result==null){return null;}
      return new Core.L.MWT(mwt.pos(), docs, mh, mwt.nativeUrl(),result);
      }
    catch(UndefinedCase uc){return null;}
    }
  public Mdf _inject(Mdf mdf) {return mdf==null?Mdf.Immutable:mdf;}
    
  public Core.MH _inject(Full.MH mh) {
    var docs=_injectL(mh.docs(),this::_inject);
    var pars=_injectL(mh.pars(),this::_inject);
    var exceptions=_injectL(mh.exceptions(),this::_inject);
    var t=_inject(mh.t());
    if(mh.s().m().isEmpty() || mh._op()!=null || docs==null
      || pars==null || exceptions==null){return null;}
    return new Core.MH(_inject(mh._mdf()), docs, t, mh.s(), pars, exceptions);
    }

  public Core.L.NC _inject(Full.L.NC nc) {
    var docs=_injectL(nc.docs(),this::_inject);
    Core.L l=null;
    if (nc.e() instanceof Core.L){l=(Core.L)nc.e();}
    if (nc.e() instanceof Full.L){l=_inject((Full.L)nc.e(),null);}
    if(docs==null || l==null){return null;}
    return new Core.L.NC(nc.pos(), docs, nc.key(),l);
    }
  public P _inject(Full.CsP csP) {return csP._p();}

  public Core.Doc _inject(Full.Doc d) {
    Core.PathSel ps=null;
    if(d._pathSel()!=null){
      ps=_inject(d._pathSel());
      if(ps==null){return null;}
      }
    var docs=_injectL(d.docs(),this::_inject);
    if(docs==null){return null;}
    return new Core.Doc(ps,d.texts(),docs);
    }
  public Core.PathSel _inject(Full.PathSel p) {
    if(p._p()==null){return null;}
    if(p._s()!=null && p._s().m().isEmpty()){return null;}
    return new Core.PathSel(p._p(), p._s(),p._x());
    }
  public Core.D _inject(Full.D d) {
    if(d._e()==null ||d._varTx()==null ||!d.varTxs().isEmpty()){return null;}
    var t=_inject(d._varTx()._t());
    var e=_inject(d._e());
    if(d._varTx()._x()==null || t==null || e==null){return null;}
    return new Core.D(d._varTx().isVar(),t, d._varTx()._x(), e);
    }
  public Core.K _inject(Full.K d) {
    if(d._thr()==null || d._x()==null){return null;}
    var t=_inject(d.t());
    var e=_inject(d.e());
    if(t==null || e==null){return null;}
    return new Core.K(d._thr(), t, d._x(), e);
    }

  public <A,B> List<B> _injectL(List<A> es,Function<A,B>f) {
    List<B>cts=L(es,(c,ei)->c.add(f.apply(ei)));
    if(cts.contains(null)){return null;}
    return cts;
    }
  }