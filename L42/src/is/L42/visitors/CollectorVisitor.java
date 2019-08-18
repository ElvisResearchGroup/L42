package is.L42.visitors;
import java.util.List;
import is.L42.generated.*;

public class CollectorVisitor {
  public final void visitST(ST st){st.visitable().accept(this);}

  public final void visitE(Core.E e){e.visitable().accept(this);}
  
  public final void visitEs(List<Core.E> es){es.forEach(this::visitE);}

  public final void visitXP(Core.XP xP){xP.visitable().accept(this);}

  public final void visitPs(List<P> ps){ps.forEach(this::visitP);}

  public final void visitSs(List<S> ss){ss.forEach(this::visitS);}

  public final void visitPathSels(List<Core.PathSel> pathSels){pathSels.forEach(this::visitPathSel);}

  public final void visitXPs(List<Core.XP> xPs){xPs.forEach(this::visitXP);}

  public final void visitE(Full.E e){e.visitable().accept(this);}

  public final void visitFullEs(List<Full.E> es){es.forEach(this::visitE);}

  public final void visitM(Full.L.M m){m.visitable().accept(this);}

  public final void visitE(Half.E e){e.visitable().accept(this);}

  public final void visitHalfEs(List<Half.E> es){es.forEach(this::visitE);}

  public final void visitXP(Half.XP xP){xP.visitable().accept(this);}

  public final void visitHalfXPs(List<Half.XP> xPs){xPs.forEach(this::visitXP);}

  public final void visitCs(List<C> cs){cs.forEach(this::visitC);}

  public final void visitXs(List<X> xs){xs.forEach(this::visitX);}
  
  public final void visitSTz(List<ST> stz){stz.forEach(this::visitST);}

  public final void visitMWTs(List<Core.L.MWT> mwts){mwts.forEach(this::visitMWT);}
  
  public final void visitNCs(List<Core.L.NC> ncs){ncs.forEach(this::visitNC);}
  
  public final void visitDs(List<Core.D> ds){ds.forEach(this::visitD);}
    
  public final void visitKs(List<Core.K> ks){ks.forEach(this::visitK);}

  public final void visitTs(List<Core.T> ts){ts.forEach(this::visitT);}

  public final void visitDocs(List<Core.Doc> docs){docs.forEach(this::visitDoc);}

  public final void visitHalfDs(List<Half.D> ds){ds.forEach(this::visitD);}

  public final void visitHalfKs(List<Half.K> ks){ks.forEach(this::visitK);}
    
  public final void visitHalfTs(List<Half.T> ts){ts.forEach(this::visitT);}

  public final void visitFullMs(List<Full.L.M> ms){ms.forEach(this::visitM);}

  public final void visitFullDs(List<Full.D> ds){ds.forEach(this::visitD);}  

  public final void visitFullVarTxs(List<Full.VarTx> varTxs){varTxs.forEach(this::visitVarTx);}

  public final void visitFullKs(List<Full.K> ks){ks.forEach(this::visitK);}

  public final void visitFullPars(List<Full.Par> pars){pars.forEach(this::visitPar);}

  public final void visitFullTs(List<Full.T> ts){ts.forEach(this::visitT);}

  public final void visitFullDocs(List<Full.Doc> docs){docs.forEach(this::visitDoc);}

  
  //------------
  
  public void visitC(C c){}

  public void visitP(P p){}

  public void visitS(S s){
    visitXs(s.xs());
    }

  public void visitX(X x){}

  public void visitSTMeth(ST.STMeth stMeth){
    visitST(stMeth.st());
    visitS(stMeth.s());
    }

  public void visitSTOp(ST.STOp stOp){
    stOp.stzs().forEach(this::visitSTz);
    }

  public void visitEX(Core.EX x){visitX(x.x());}

  public void visitPCastT(Core.PCastT pCastT){
    visitP(pCastT.p());
    visitT(pCastT.t());
    }
    
  public void visitEVoid(Core.EVoid eVoid){}
  
  public void visitL(Core.L l){
    visitTs(l.ts());
    visitMWTs(l.mwts());
    visitNCs(l.ncs());
    visitInfo(l.info());
    visitDocs(l.docs());
    }
    
  public void visitInfo(Core.L.Info info){
    visitPs(info.typeDep());
    visitPs(info.coherentDep());
    visitPs(info.friendsDep());
    visitPathSels(info.usedMethDep());
    visitPs(info.privateImpl());
    visitSs(info.refined());
    }
    
  public void visitMWT(Core.L.MWT mwt){
    visitDocs(mwt.docs());
    visitMH(mwt.mh());
    var _e0=mwt._e();
    if(_e0!=null){visitE(_e0);}
    }
  
  public void visitNC(Core.L.NC nc){
    visitDocs(nc.docs());
    visitC(nc.key());
    visitL(nc.l());
    }

  public void visitMCall(Core.MCall mCall){
    visitXP(mCall.xP());
    visitS(mCall.s());
    visitEs(mCall.es());
    }
    
  public void visitBlock(Core.Block block){
    visitDs(block.ds());
    visitKs(block.ks());
    visitE(block.e());
    }
    
  public void visitLoop(Core.Loop loop){
    visitE(loop.e());
    }
    
  public void visitThrow(Core.Throw thr){
    visitE(thr.e());
    }
    
  public void visitOpUpdate(Core.OpUpdate opUpdate){
    visitX(opUpdate.x());
    visitE(opUpdate.e());
    }
    
  public void visitD(Core.D d){
    visitT(d.t());
    visitX(d.x());
    visitE(d.e());
    }

  public void visitK(Core.K k){
    visitT(k.t());
    visitX(k.x());
    visitE(k.e());
    }

  public void visitT(Core.T t){
    visitDocs(t.docs());
    visitP(t.p());
    }
      
  public void visitDoc(Core.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    if(pathSel0!=null){visitPathSel(pathSel0);}
    visitDocs(docs0);
    }
     
  public void visitPathSel(Core.PathSel pathSel){
    var s0=pathSel._s();
    var x0=pathSel._x();
    visitP(pathSel.p());
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  public void visitMH(Core.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    visitDocs(docs0);
    visitT(t0);
    visitS(s0);
    visitTs(pars0);
    visitTs(exceptions0);
    }

  public void visitPCastT(Half.PCastT pCastT){
    var p0=pCastT.p();
    var t0=pCastT.t();
    visitP(p0);
    visitT(t0);
    }
    
  public void visitSlash(Half.Slash slash){
    visitSTz(slash.stz());
    }
    
  public void visitBinOp(Half.BinOp binOp){
    visitHalfXPs(binOp.es());
    }
    
  public void visitMCall(Half.MCall mCall){
    var xP0=mCall.xP();
    var s0=mCall.s();
    var es0=mCall.es();
    visitXP(xP0);
    visitS(s0);
    visitHalfEs(es0);
    }
    
  public void visitBlock(Half.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var e0=block.e();
    visitHalfDs(ds0);
    visitHalfKs(ks0);
    visitE(e0);
    }
    
  public void visitLoop(Half.Loop loop){
    visitE(loop.e());
    }
  public void visitThrow(Half.Throw thr){
    visitE(thr.e());
    }
  public void visitOpUpdate(Half.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
  public void visitD(Half.D d){
    var t0=d.t();
    var x0=d.x();
    var e0=d.e();
    visitT(t0);
    visitX(x0);
    visitE(e0);
    }
  
  public void visitK(Half.K k){
    var t0=k.t();
    var x0=k.x();
    var e0=k.e();
    visitT(t0);
    visitX(x0);
    visitE(e0);
    }

  public void visitT(Half.T t){
    visitSTz(t.stz());
    }

  public void visitCsP(Full.CsP csP){
    if(csP.cs().isEmpty()){visitP(csP._p());return;}
    assert csP._p()==null;
    visitCs(csP.cs());
    }
    
  public void visitL(Full.L l){
    var ts0=l.ts();
    var ms0=l.ms();
    var docs0=l.docs();
    visitFullTs(ts0);
    visitFullMs(ms0);
    visitFullDocs(docs0);
    }
    
  public void visitF(Full.L.F f){
    var docs0=f.docs();
    var t0=f.t();
    var s0=f.key();
    visitFullDocs(docs0);
    visitT(t0);
    visitS(s0);
    }
    
  public void visitMI(Full.L.MI mi){
    var docs0=mi.docs();
    var s0=mi.key();
    var e0=mi.e();
    visitFullDocs(docs0);
    visitS(s0);
    visitE(e0);
    }
    
  public void visitMWT(Full.L.MWT mwt){
    var docs0=mwt.docs();
    var mh0=mwt.mh();
    var _e0=mwt._e();
    visitFullDocs(docs0);
    visitMH(mh0);
    if(_e0!=null){visitE(_e0);}
    }
  
  public void visitNC(Full.L.NC nc){
    var docs0=nc.docs();
    var c0=nc.key();
    var e0=nc.e();
    visitFullDocs(docs0);
    visitC(c0);
    visitE(e0);
    }
    
  public void visitSlash(Full.Slash slash){}

  public void visitSlashX(Full.SlashX slashX){}//note, is right to not propagate on the x
  
  public void visitEString(Full.EString eString){
    visitFullEs(eString.es());
    }
  
  public void visitEPathSel(Full.EPathSel ePathSel){
    visitPathSel(ePathSel.pathSel());
    }

  public void visitUOp(Full.UOp uOp){
    visitE(uOp.e());
    }

  public void visitBinOp(Full.BinOp binOp){
    visitFullEs(binOp.es());
    }
    
  public void visitCast(Full.Cast cast){
    var e0=cast.e();
    var t0=cast.t();
    visitE(e0);
    visitT(t0);
    }
  
  public void visitCall(Full.Call call){
    var e0=call.e();
    var s0=call._s();
    var pars0=call.pars();
    visitE(e0);
    if(s0!=null){visitS(s0);}
    visitFullPars(pars0);
    }
  
  public void visitBlock(Full.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var ts0=block.whoopsed();
    var e0=block._e();
    visitFullDs(ds0);
    visitFullKs(ks0);
    visitFullTs(ts0);
    if(e0!=null){visitE(e0);}
    }
    
  public void visitLoop(Full.Loop loop){
    visitE(loop.e());
    }
  public void visitWhile(Full.While sWhile){
    var c0=sWhile.condition();
    var b0=sWhile.body();
    visitE(c0);
    visitE(b0);
    }
  public void visitFor(Full.For sFor){
    var ds0=sFor.ds();
    var b0=sFor.body();
    visitFullDs(ds0);
    visitE(b0);
    }
    
  public void visitThrow(Full.Throw thr){
    visitE(thr.e());
    }
    
  public void visitOpUpdate(Full.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
    
  public void visitIf(Full.If sIf){
    var _c0=sIf._condition();
    var ds0=sIf.matches();
    var then0=sIf.then();
    var _else0=sIf._else();
    if(_c0!=null){visitE(_c0);}
    visitFullDs(ds0);
    visitE(then0);
    if(_else0!=null){visitE(_else0);}
    }
  public void visitD(Full.D d){
    var tx0=d._varTx();
    var txs0=d.varTxs();
    var e0=d._e();
    if(tx0!=null){visitVarTx(tx0);}
    visitFullVarTxs(txs0);
    if(e0!=null){visitE(e0);}
    }
 
  public void visitVarTx(Full.VarTx varTx){
    var t0=varTx._t();
    var x0=varTx._x();
    if(t0!=null){visitT(t0);}
    if(x0!=null){visitX(x0);}
    }
  
  public void visitK(Full.K k){
    var t0=k.t();
    var x0=k._x();
    var e0=k.e();
    visitT(t0);
    if(x0!=null){visitX(x0);}
    visitE(e0);
    }
    
  public void visitPar(Full.Par par){
    var e0=par._that();
    var xs0=par.xs();
    var es0=par.es();
    if(e0!=null){visitE(e0);}
    visitXs(xs0);
    visitFullEs(es0);
    }
      
  public void visitT(Full.T t){
    var docs0=t.docs();
    var csP0=t.csP();
    visitFullDocs(docs0);
    visitCsP(csP0);
    }
    
  public void visitDoc(Full.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    if(pathSel0!=null){visitPathSel(pathSel0);}
    visitFullDocs(docs0);
    }
      
  public void visitPathSel(Full.PathSel pathSel){
    var csP0=pathSel._csP();
    var s0=pathSel._s();
    var x0=pathSel._x();
    if(csP0!=null){visitCsP(csP0);}
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  public void visitMH(Full.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    visitFullDocs(docs0);
    visitT(t0);
    visitS(s0);
    visitFullTs(pars0);
    visitFullTs(exceptions0);
    }
  }
