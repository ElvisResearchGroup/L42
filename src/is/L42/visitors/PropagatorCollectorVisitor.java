package is.L42.visitors;
import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.*;
import is.L42.generated.Core.Info;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.NC;

public class PropagatorCollectorVisitor implements CollectorVisitor{
  @Override public void visitC(C c){}

  @Override public void visitP(P p){}

  @Override public void visitS(S s){
    visitXs(s.xs());
    }

  @Override public void visitX(X x){}

  @Override public void visitSTMeth(ST.STMeth stMeth){
    visitST(stMeth.st());
    visitS(stMeth.s());
    }
  @Override public void visitSTHalfT(ST.STHalfT stHalfT){
    for(var st:stHalfT.stz()){ visitST(st); }
    }
  @Override public void visitSTOp(ST.STOp stOp){
    stOp.stzs().forEach(this::visitSTz);
    }

  @Override public void visitEX(Core.EX x){visitX(x.x());}

  @Override public void visitPCastT(Core.PCastT pCastT){
    visitP(pCastT.p());
    visitT(pCastT.t());
    }
    
  @Override public void visitEVoid(Core.EVoid eVoid){}
  
  @Override public void visitL(CoreL l){
    visitTs(l.ts());
    visitMWTs(l.mwts());
    visitNCs(l.ncs());
    visitInfo(l.info());
    visitDocs(l.docs());
    }
    
  @Override public void visitInfo(Info info){
    visitPs(info.typeDep());
    visitPs(info.coherentDep());
    visitPs(info.metaCoherentDep());
    visitPs(info.watched());
    visitPathSels(info.usedMethods());
    visitPs(info.hiddenSupertypes());
    visitSs(info.refined());
    }
    
  @Override public void visitMWT(MWT mwt){
    visitDocs(mwt.docs());
    visitMH(mwt.mh());
    var _e0=mwt._e();
    if(_e0!=null){visitE(_e0);}
    }
  
  @Override public void visitNC(NC nc){
    visitDocs(nc.docs());
    visitC(nc.key());
    visitL(nc.l());
    }

  @Override public void visitMCall(Core.MCall mCall){
    visitXP(mCall.xP());
    visitS(mCall.s());
    visitEs(mCall.es());
    }
    
  @Override public void visitBlock(Core.Block block){
    visitDs(block.ds());
    visitKs(block.ks());
    visitE(block.e());
    }
    
  @Override public void visitLoop(Core.Loop loop){
    visitE(loop.e());
    }
    
  @Override public void visitThrow(Core.Throw thr){
    visitE(thr.e());
    }
    
  @Override public void visitOpUpdate(Core.OpUpdate opUpdate){
    visitX(opUpdate.x());
    visitE(opUpdate.e());
    }
    
  @Override public void visitD(Core.D d){
    visitT(d.t());
    visitX(d.x());
    visitE(d.e());
    }

  @Override public void visitK(Core.K k){
    visitT(k.t());
    visitX(k.x());
    visitE(k.e());
    }

  @Override public void visitT(Core.T t){
    visitDocs(t.docs());
    visitP(t.p());
    }
      
  @Override public void visitDoc(Core.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    if(pathSel0!=null){visitPathSel(pathSel0);}
    visitDocs(docs0);
    }
     
  @Override public void visitPathSel(Core.PathSel pathSel){
    var s0=pathSel._s();
    var x0=pathSel._x();
    visitP(pathSel.p());
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  @Override public void visitMH(Core.MH mh){
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

  @Override public void visitPCastT(Half.PCastT pCastT){
    var p0=pCastT.p();
    var stz0=pCastT.stz();
    visitP(p0);
    visitSTz(stz0);
    }
    
  @Override public void visitSlashCastT(Half.SlashCastT slash){
    visitSTz(slash.stz());
    visitSTz(slash.stz1());
    }
    
  @Override public void visitBinOp(Half.BinOp binOp){
    visitHalfXPs(binOp.es());
    }
    
  @Override public void visitMCall(Half.MCall mCall){
    var xP0=mCall.xP();
    var s0=mCall.s();
    var es0=mCall.es();
    visitXP(xP0);
    visitS(s0);
    visitHalfEs(es0);
    }
    
  @Override public void visitBlock(Half.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var e0=block.e();
    visitHalfDs(ds0);
    visitHalfKs(ks0);
    visitE(e0);
    }
    
  @Override public void visitLoop(Half.Loop loop){
    visitE(loop.e());
    }
  @Override public void visitThrow(Half.Throw thr){
    visitE(thr.e());
    }
  @Override public void visitOpUpdate(Half.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
  @Override public void visitD(Half.D d){
    var stz0=d.stz();
    var x0=d.x();
    var e0=d.e();
    visitSTz(stz0);
    visitX(x0);
    visitE(e0);
    }
  
  @Override public void visitK(Half.K k){
    var stz0=k.stz();
    var x0=k.x();
    var e0=k.e();
    visitSTz(stz0);
    visitX(x0);
    visitE(e0);
    }

  @Override public void visitCsP(Full.CsP csP){
    if(csP.cs().isEmpty()){visitP(csP._p());return;}
    assert csP._p()==null;
    visitCs(csP.cs());
    }
    
  @Override public void visitL(Full.L l){
    var ts0=l.ts();
    var ms0=l.ms();
    var docs0=l.docs();
    visitFullTs(ts0);
    visitFullMs(ms0);
    visitFullDocs(docs0);
    }
    
  @Override public void visitF(Full.L.F f){
    var docs0=f.docs();
    var t0=f.t();
    var s0=f.key();
    visitFullDocs(docs0);
    visitT(t0);
    visitS(s0);
    }
    
  @Override public void visitMI(Full.L.MI mi){
    var docs0=mi.docs();
    var s0=mi.key();
    var e0=mi.e();
    visitFullDocs(docs0);
    visitS(s0);
    visitE(e0);
    }
    
  @Override public void visitMWT(Full.L.MWT mwt){
    var docs0=mwt.docs();
    var mh0=mwt.mh();
    var _e0=mwt._e();
    visitFullDocs(docs0);
    visitMH(mh0);
    if(_e0!=null){visitE(_e0);}
    }
  
  @Override public void visitNC(Full.L.NC nc){
    var docs0=nc.docs();
    var c0=nc.key();
    var e0=nc.e();
    visitFullDocs(docs0);
    visitC(c0);
    visitE(e0);
    }
    
  @Override public void visitSlash(Full.Slash slash){}

  @Override public void visitSlashX(Full.SlashX slashX){}//note, is right to not propagate on the x
  
  @Override public void visitEString(Full.EString eString){
    visitFullEs(eString.es());
    }
  
  @Override public void visitEPathSel(Full.EPathSel ePathSel){
    visitPathSel(ePathSel.pathSel());
    }

  @Override public void visitUOp(Full.UOp uOp){
    visitE(uOp.e());
    }

  @Override public void visitBinOp(Full.BinOp binOp){
    visitFullEs(binOp.es());
    }
    
  @Override public void visitCast(Full.Cast cast){
    var e0=cast.e();
    var t0=cast.t();
    visitE(e0);
    visitT(t0);
    }
  
  @Override public void visitCall(Full.Call call){
    var e0=call.e();
    var s0=call._s();
    var pars0=call.pars();
    visitE(e0);
    if(s0!=null){visitS(s0);}
    visitFullPars(pars0);
    }
  
  @Override public void visitBlock(Full.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var ts0=block.whoopsed();
    var e0=block._e();
    visitFullDs(ds0);
    visitFullKs(ks0);
    visitFullTs(ts0);
    if(e0!=null){visitE(e0);}
    }
    
  @Override public void visitLoop(Full.Loop loop){
    visitE(loop.e());
    }
  @Override public void visitWhile(Full.While sWhile){
    var c0=sWhile.condition();
    var b0=sWhile.body();
    visitE(c0);
    visitE(b0);
    }
  @Override public void visitFor(Full.For sFor){
    var ds0=sFor.ds();
    var b0=sFor.body();
    visitFullDs(ds0);
    visitE(b0);
    }
    
  @Override public void visitThrow(Full.Throw thr){
    visitE(thr.e());
    }
    
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
    
  @Override public void visitIf(Full.If sIf){
    var _c0=sIf._condition();
    var ds0=sIf.matches();
    var then0=sIf.then();
    var _else0=sIf._else();
    if(_c0!=null){visitE(_c0);}
    visitFullDs(ds0);
    visitE(then0);
    if(_else0!=null){visitE(_else0);}
    }
  @Override public void visitD(Full.D d){
    var tx0=d._varTx();
    var txs0=d.varTxs();
    var e0=d._e();
    if(tx0!=null){visitVarTx(tx0);}
    visitFullVarTxs(txs0);
    if(e0!=null){visitE(e0);}
    }
 
  @Override public void visitVarTx(Full.VarTx varTx){
    var t0=varTx._t();
    var x0=varTx._x();
    if(t0!=null){visitT(t0);}
    if(x0!=null){visitX(x0);}
    }
  
  @Override public void visitK(Full.K k){
    var t0=k.t();
    var x0=k._x();
    var e0=k.e();
    visitT(t0);
    if(x0!=null){visitX(x0);}
    visitE(e0);
    }
    
  @Override public void visitPar(Full.Par par){
    var e0=par._that();
    var xs0=par.xs();
    var es0=par.es();
    if(e0!=null){visitE(e0);}
    visitXs(xs0);
    visitFullEs(es0);
    }
      
  @Override public void visitT(Full.T t){
    var docs0=t.docs();
    var cs0=t.cs();
    var _p0=t._p();
    visitFullDocs(docs0);
    visitCs(cs0);
    visitP(_p0);
    }
    
  @Override public void visitDoc(Full.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    if(pathSel0!=null){visitPathSel(pathSel0);}
    visitFullDocs(docs0);
    }
      
  @Override public void visitPathSel(Full.PathSel pathSel){
    var cs0=pathSel.cs();
    var _p0=pathSel._p();
    var s0=pathSel._s();
    var x0=pathSel._x();
    visitCs(cs0);
    if(_p0!=null){visitP(_p0);}
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  @Override public void visitMH(Full.MH mh){
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

  @Override public void visitProgram(Program program) {
    program.top.visitable().accept(this);
    program.pTails.accept(this);
    }

  @Override public void visitPTails(PTails pTails) {
    if(pTails.isEmpty()){return;}
    if(pTails.hasC()){visitC(pTails.c());}
    pTails.ll().visitable().accept(this);
    visitPTails(pTails.tail());
    }
  }