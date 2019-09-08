package is.L42.visitors;
import java.util.List;

import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.*;
import static is.L42.tools.General.*;

public class CloneVisitor {
  public final ST visitST(ST st){return st.visitable().accept(this);}

  public final Core.E visitE(Core.E e){return e.visitable().accept(this);}
  
  public final List<Core.E> visitEs(List<Core.E> es){return L(es,this::visitE);}

  public final Core.XP visitXP(Core.XP xP){return xP.visitable().accept(this);}

  public final List<P> visitPs(List<P> ps){return L(ps,this::visitP);}

  public final List<S> visitSs(List<S> ss){return L(ss,this::visitS);}

  public final List<Core.PathSel> visitPathSels(List<Core.PathSel> pathSels){return L(pathSels,this::visitPathSel);}

  public final List<Core.XP> visitXPs(List<Core.XP> xPs){return L(xPs,this::visitXP);}

  public final Full.E visitE(Full.E e){return e.visitable().accept(this);}

  public final List<Full.E> visitFullEs(List<Full.E> es){return L(es,this::visitE);}

  public final Full.L.M visitM(Full.L.M m){return m.visitable().accept(this);}

  public final Half.E visitE(Half.E e){return e.visitable().accept(this);}

  public final List<Half.E> visitHalfEs(List<Half.E> es){return L(es,this::visitE);}

  public final Half.XP visitXP(Half.XP xP){return xP.visitable().accept(this);}

  public final List<Half.XP> visitHalfXPs(List<Half.XP> xPs){return L(xPs,this::visitXP);}

  public final List<C> visitCs(List<C> cs){return L(cs,this::visitC);}

  public final List<X> visitXs(List<X> xs){return L(xs,this::visitX);}
  
  public final List<ST> visitSTz(List<ST> stz){return L(stz,this::visitST);}

  public final List<Core.L.MWT> visitMWTs(List<Core.L.MWT> mwts){return L(mwts,this::visitMWT);}
  
  public final List<Core.L.NC> visitNCs(List<Core.L.NC> ncs){return L(ncs,this::visitNC);}
  
  public final List<Core.D> visitDs(List<Core.D> ds){return L(ds,this::visitD);}
    
  public final List<Core.K> visitKs(List<Core.K> ks){return L(ks,this::visitK);}

  public final List<Core.T> visitTs(List<Core.T> ts){return L(ts,this::visitT);}

  public final List<Core.Doc> visitDocs(List<Core.Doc> docs){return L(docs,this::visitDoc);}

  public final List<Half.D> visitHalfDs(List<Half.D> ds){return L(ds,this::visitD);}

  public final List<Half.K> visitHalfKs(List<Half.K> ks){return L(ks,this::visitK);}
    
  public final List<Half.T> visitHalfTs(List<Half.T> ts){return L(ts,this::visitT);}

  public final List<Full.L.M> visitFullMs(List<Full.L.M> ms){return L(ms,this::visitM);}

  public final List<Full.D> visitFullDs(List<Full.D> ds){return L(ds,this::visitD);}  

  public final List<Full.VarTx> visitFullVarTxs(List<Full.VarTx> varTxs){return L(varTxs,this::visitVarTx);}

  public final List<Full.K> visitFullKs(List<Full.K> ks){return L(ks,this::visitK);}

  public final List<Full.Par> visitFullPars(List<Full.Par> pars){return L(pars,this::visitPar);}

  public final List<Full.T> visitFullTs(List<Full.T> ts){return L(ts,this::visitT);}

  public final List<Full.Doc> visitFullDocs(List<Full.Doc> docs){return L(docs,this::visitDoc);}

  
  //------------
  
  public C visitC(C c){return c;}

  public P visitP(P p){return p;}

  public S visitS(S s){
    return s.withXs(visitXs(s.xs()));
    }

  public X visitX(X x){return x;}

  public ST visitSTMeth(ST.STMeth stMeth){//note: different ret type
    var s0=stMeth.s();
    var st0=stMeth.st();
    var s=visitS(s0);
    var st=visitST(st0);
    if(s==s0 && st==st0){return stMeth;}
    return new ST.STMeth(st, s, stMeth.i());
    }

  public ST visitSTOp(ST.STOp stOp){//note: different ret type
    var zs=L(stOp.stzs(),this::visitSTz);
    return stOp.withStzs(zs);
    }

  public Core.EX visitEX(Core.EX x){return x.withX(visitX(x.x()));}

  public Core.PCastT visitPCastT(Core.PCastT pCastT){
    var p0=pCastT.p();
    var t0=pCastT.t();
    var p=visitP(p0);
    var t=visitT(t0);
    if(p==p0&&t==t0){return pCastT;}
    return new Core.PCastT(pCastT.pos(), p, t);
    }
    
  public Core.EVoid visitEVoid(Core.EVoid eVoid){return eVoid;}
  
  public Core.L visitL(Core.L l){
    var ts0=l.ts();
    var mwts0=l.mwts();
    var ncs0=l.ncs();
    var info0=l.info();
    var docs0=l.docs();
    var ts=visitTs(ts0);
    var mwts=visitMWTs(mwts0);
    var ncs=visitNCs(ncs0);
    var info=visitInfo(info0);
    var docs=visitDocs(docs0);
    if(ts0==ts && mwts==mwts0 && ncs==ncs0 && info==info0 && docs==docs0){return l;}
    return new Core.L(l.poss(),l.isInterface(),ts,mwts,ncs,info,docs);
    }
    
  public Core.L.Info visitInfo(Core.L.Info info){
    var typeDep0=info.typeDep();
    var coherentDep0=info.coherentDep();
    var friends0=info.friends();
    var usedMethods0=info.usedMethods();
    var privateSupertypes0=info.privateSupertypes();
    var refined0=info.refined();
    var typeDep=visitPs(typeDep0);
    var coherentDep=visitPs(coherentDep0);
    var friends=visitPs(friends0);
    var usedMethods=visitPathSels(usedMethods0);
    var privateSupertypes=visitPs(privateSupertypes0);
    var refined=visitSs(refined0);
    if(typeDep==typeDep0 && coherentDep==coherentDep0 && friends==friends0 
      && usedMethods==usedMethods0 && privateSupertypes==privateSupertypes0 && refined==refined0){return info;}
    return new Core.L.Info(info.isTyped(),typeDep,coherentDep,friends,usedMethods,privateSupertypes,refined,info.declaresClassMethods());
    }
    
  public Core.L.MWT visitMWT(Core.L.MWT mwt){
    var docs0=mwt.docs();
    var mh0=mwt.mh();
    var _e0=mwt._e();
    var docs=visitDocs(docs0);
    var mh=visitMH(mh0);
    var _e=_e0==null?null:visitE(_e0);
    if(docs==docs0 && mh==mh0 && _e==_e0){return mwt;}
    return new Core.L.MWT(mwt.poss(),docs, mh, mwt.nativeUrl(), _e);
    }
  
  public Core.L.NC visitNC(Core.L.NC nc){
    var docs0=nc.docs();
    var c0=nc.key();
    var l0=nc.l();
    var docs=visitDocs(docs0);
    var c=visitC(c0);
    var l=visitL(l0);
    if(docs==docs0 && c==c0 && l==l0){return nc;}
    return new Core.L.NC(nc.poss(), docs, c, l);
    }

  public Core.MCall visitMCall(Core.MCall mCall){
    var xP0=mCall.xP();
    var s0=mCall.s();
    var es0=mCall.es();
    var xP=visitXP(xP0);
    var s=visitS(s0);
    var es=visitEs(es0);
    if(xP==xP0 && s==s0 && es==es0){return mCall;}
    return new Core.MCall(mCall.pos(),xP, s, es);
    }
    
  public Core.Block visitBlock(Core.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var e0=block.e();
    var ds=visitDs(ds0);
    var ks=visitKs(ks0);
    var e=visitE(e0);
    if(ds==ds0 && ks==ks0 && e==e0){return block;}
    return new Core.Block(block.pos(), ds, ks, e);
    }
    
  public Core.Loop visitLoop(Core.Loop loop){
    return loop.withE(visitE(loop.e()));
    }
    
  public Core.Throw visitThrow(Core.Throw thr){
    return thr.withE(visitE(thr.e()));
    }
    
  public Core.OpUpdate visitOpUpdate(Core.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    var x=visitX(x0);
    var e=visitE(e0);
    if(x==x0 && e==e0){return opUpdate;}
    return new Core.OpUpdate(opUpdate.pos(),x,e);
    }
    
  public Core.D visitD(Core.D d){
    var t0=d.t();
    var x0=d.x();
    var e0=d.e();
    var t=visitT(t0);
    var x=visitX(x0);
    var e=visitE(e0);
    if(t==t0 && x==x0 && e==e0){return d;}
    return new Core.D(d.isVar(),t, x, e);
    }

  public Core.K visitK(Core.K k){
    var t0=k.t();
    var x0=k.x();
    var e0=k.e();
    var t=visitT(t0);
    var x=visitX(x0);
    var e=visitE(e0);
    if(t==t0 && e==e0){return k;}
    return new Core.K(k.thr(),t,x,e);
    }

  public Core.T visitT(Core.T t){
    var docs0=t.docs();
    var p0=t.p();
    var docs=visitDocs(docs0);
    var p=visitP(p0);
    return new Core.T(t.mdf(), docs, p);
    }
      
  public Core.Doc visitDoc(Core.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    var pathSel=pathSel0==null?null:visitPathSel(pathSel0);
    var docs=visitDocs(docs0);
    if(docs==docs0 && pathSel==pathSel0){return doc;}
    return new Core.Doc(pathSel, doc.texts(), docs);
    }
     
  public Core.PathSel visitPathSel(Core.PathSel pathSel){
    var p0=pathSel.p();
    var s0=pathSel._s();
    var x0=pathSel._x();
    var p=visitP(p0);
    var s=s0==null?null:visitS(s0);
    var x=x0==null?null:visitX(x0);
    if( p==p0 && s==s0 && x==x0){return pathSel;}
    return new Core.PathSel(p, s, x);
    }
    
  public Core.MH visitMH(Core.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    var docs=visitDocs(docs0);
    var t=visitT(t0);
    var s=visitS(s0);
    var pars=visitTs(pars0);
    var exceptions=visitTs(exceptions0);
    if(docs==docs0 && t==t0 && s==s0 && pars==pars0 && exceptions==exceptions0){return mh;}
    return new Core.MH(mh.mdf(),docs,t,s,pars,exceptions);
    }

  public Half.PCastT visitPCastT(Half.PCastT pCastT){
    var p0=pCastT.p();
    var t0=pCastT.t();
    var p=visitP(p0);
    var t=visitT(t0);
    if(p==p0 && t==t0){return pCastT;}
    return new Half.PCastT(pCastT.pos(), p, t);
    }
    
  public Half.Slash visitSlash(Half.Slash slash){
    return slash.withStz(visitSTz(slash.stz()));
    }
    
  public Half.BinOp visitBinOp(Half.BinOp binOp){
    return binOp.withEs(visitHalfXPs(binOp.es()));
    }
    
  public Half.MCall visitMCall(Half.MCall mCall){
    var xP0=mCall.xP();
    var s0=mCall.s();
    var es0=mCall.es();
    var xP=visitXP(xP0);
    var s=visitS(s0);
    var es=visitHalfEs(es0);
    if(xP==xP0 && s==s0 && es==es0){return mCall;}
    return new Half.MCall(mCall.pos(),xP,s,es);
    }
    
  public Half.Block visitBlock(Half.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var e0=block.e();
    var ds=visitHalfDs(ds0);
    var ks=visitHalfKs(ks0);
    var e=visitE(e0);
    if(ds==ds0 && ks==ks0 && e==e0){return block;}
    return new Half.Block(block.pos(),ds,ks,e);
    }
    
  public Half.Loop visitLoop(Half.Loop loop){
    return loop.withE(visitE(loop.e()));
    }
  public Half.Throw visitThrow(Half.Throw thr){
    return thr.withE(visitE(thr.e()));
    }
  public Half.OpUpdate visitOpUpdate(Half.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    var x=visitX(x0);
    var e=visitE(e0);
    if(x==x0 && e==e0){return opUpdate;}
    return new Half.OpUpdate(opUpdate.pos(),x,e);
    }
  public Half.D visitD(Half.D d){
    var t0=d.t();
    var x0=d.x();
    var e0=d.e();
    var t=visitT(t0);
    var x=visitX(x0);
    var e=visitE(e0);
    if(t==t0 && x==x0 && e==e0){return d;}
    return new Half.D(t, x, e);
    }
  
  public Half.K visitK(Half.K k){
    var t0=k.t();
    var x0=k.x();
    var e0=k.e();
    var t=visitT(t0);
    var x=visitX(x0);
    var e=visitE(e0);
    if(t==t0 && e==e0){return k;}
    return new Half.K(k.thr(),t,x,e);
    }

  public Half.T visitT(Half.T t){
    return t.withStz(visitSTz(t.stz()));
    }

  public Full.CsP visitCsP(Full.CsP csP){
    if(csP.cs().isEmpty()){return csP.with_p(visitP(csP._p()));}
    assert csP._p()==null;
    return csP.withCs(visitCs(csP.cs()));
    }
    
  public Full.L visitL(Full.L l){
    var ts0=l.ts();
    var ms0=l.ms();
    var docs0=l.docs();
    var ts=visitFullTs(ts0);
    var ms=visitFullMs(ms0);
    var docs=visitFullDocs(docs0);
    if(ts==ts0 && ms==ms0 && docs==docs0){return l;}
    return new Full.L(l.pos(),l.isDots(),l.reuseUrl(),l.isInterface(),ts,ms,docs);
    }
    
  public Full.L.F visitF(Full.L.F f){
    var docs0=f.docs();
    var t0=f.t();
    var s0=f.key();
    var docs=visitFullDocs(docs0);
    var t=visitT(t0);
    var s=visitS(s0);
    if(docs==docs0 && t==t0 && s==s0){return f;}
    return new Full.L.F(f.pos(),docs,f.isVar(),t,s);
    }
    
  public Full.L.MI visitMI(Full.L.MI mi){
    var docs0=mi.docs();
    var s0=mi.key();
    var e0=mi.e();
    var docs=visitFullDocs(docs0);
    var s=visitS(s0);
    var e=visitE(e0);
    if(docs==docs0 && s==s0 && e==e0){return mi;}
    return new Full.L.MI(mi.pos(),docs,mi._op(),mi.n(),s,e);
    }
    
  public Full.L.MWT visitMWT(Full.L.MWT mwt){
    var docs0=mwt.docs();
    var mh0=mwt.mh();
    var _e0=mwt._e();
    var docs=visitFullDocs(docs0);
    var mh=visitMH(mh0);
    var _e=_e0==null?null:visitE(_e0);
    if(docs==docs0 && mh==mh0 && _e==_e0){return mwt;}
    return new Full.L.MWT(mwt.pos(),docs, mh, mwt.nativeUrl(), _e);
    }
  
  public Full.L.NC visitNC(Full.L.NC nc){
    var docs0=nc.docs();
    var c0=nc.key();
    var e0=nc.e();
    var docs=visitFullDocs(docs0);
    var c=visitC(c0);
    var e=visitE(e0);
    if(docs==docs0 && c==c0 && e==e0){return nc;}
    return new Full.L.NC(nc.pos(), docs, c, e);
    }
    
  public Full.Slash visitSlash(Full.Slash slash){return slash;}

  public Full.SlashX visitSlashX(Full.SlashX slashX){return slashX;}//note, is right to not propagate on the x
  
  public Full.EString visitEString(Full.EString eString){
    return eString.withEs(visitFullEs(eString.es()));
    }
  
  public Full.EPathSel visitEPathSel(Full.EPathSel ePathSel){
    return ePathSel.withPathSel(ePathSel.pathSel());
    }

  public Full.UOp visitUOp(Full.UOp uOp){
    return uOp.withE(visitE(uOp.e()));
    }

  public Full.BinOp visitBinOp(Full.BinOp binOp){
    return binOp.withEs(visitFullEs(binOp.es()));
    }
    
  public Full.Cast visitCast(Full.Cast cast){
    var e0=cast.e();
    var t0=cast.t();
    var e=visitE(e0);
    var t=visitT(t0);
    if(e==e0 && t==t0){return cast;}
    return new Full.Cast(cast.pos(),e,t);
    }
  
  public Full.Call visitCall(Full.Call call){
    var e0=call.e();
    var s0=call._s();
    var pars0=call.pars();
    var e=visitE(e0);
    var s=s0==null?null:visitS(s0);
    var pars=visitFullPars(pars0);
    if(e==e0 && s==s0 && pars==pars0){return call;}
    return new Full.Call(call.pos(),e,s,call.isSquare(),pars);
    }
  
  public Full.Block visitBlock(Full.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var ts0=block.whoopsed();
    var e0=block._e();
    var ds=visitFullDs(ds0);
    var ks=visitFullKs(ks0);
    var ts=visitFullTs(ts0);
    var e=e0==null?null:visitE(e0);
    if(ds==ds0 && ks==ks0 && ts==ts0 && e==e0){return block;}
    return new Full.Block(block.pos(),block.isCurly(),ds,block.dsAfter(),ks,ts,e);
    }
    
  public Full.Loop visitLoop(Full.Loop loop){
    return loop.withE(visitE(loop.e()));
    }
  public Full.While visitWhile(Full.While sWhile){
    var c0=sWhile.condition();
    var b0=sWhile.body();
    var c=visitE(c0);
    var b=visitE(b0);
    if(c==c0 && b==b0){return sWhile;}
    return new Full.While(sWhile.pos(),c,b);
    }
  public Full.For visitFor(Full.For sFor){
    var ds0=sFor.ds();
    var b0=sFor.body();
    var ds=visitFullDs(ds0);
    var b=visitE(b0);
    if(ds==ds0 && b==b0){return sFor;}
    return new Full.For(sFor.pos(),ds,b);
    }
    
  public Full.Throw visitThrow(Full.Throw thr){
    return thr.withE(visitE(thr.e()));
    }
    
  public Full.OpUpdate visitOpUpdate(Full.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    var x=visitX(x0);
    var e=visitE(e0);
    if(x==x0 && e==e0){return opUpdate;}
    return new Full.OpUpdate(opUpdate.pos(),x,opUpdate.op(),e);
    }
    
  public Full.If visitIf(Full.If sIf){
    var c0=sIf._condition();
    var ds0=sIf.matches();
    var then0=sIf.then();
    var _else0=sIf._else();
    var c=c0==null?null:visitE(c0);
    var ds=visitFullDs(ds0);
    var then=visitE(then0);
    var _else=_else0==null?null:visitE(_else0);
    if(c==c0 && ds==ds0 && then==then0 && _else==_else0){return sIf;}
    return new Full.If(sIf.pos(),c,ds,then,_else);
    }
  public Full.D visitD(Full.D d){
    var tx0=d._varTx();
    var txs0=d.varTxs();
    var e0=d._e();
    var tx=tx0==null?null:visitVarTx(tx0);
    var txs=visitFullVarTxs(txs0);
    var e=e0==null?null:visitE(e0);
    if(tx==tx0 && txs==txs0 && e==e0){return d;}
    return new Full.D(tx,txs,e);
    }
 
  public Full.VarTx visitVarTx(Full.VarTx varTx){
    var t0=varTx._t();
    var x0=varTx._x();
    var t=t0==null?null:visitT(t0);
    var x=x0==null?null:visitX(x0);
    if(x==x0 && t==t0){return varTx;}
    return new Full.VarTx(varTx.isVar(),t,varTx._mdf(),x);
    }
  
  public Full.K visitK(Full.K k){
    var t0=k.t();
    var x0=k._x();
    var e0=k.e();
    var t=visitT(t0);
    var x=x0==null?null:visitX(x0);
    var e=visitE(e0);
    if(t==t0 && x==x0 && e==e0){return k;}
    return new Full.K(k._thr(),t,x,e);
    }
    
  public Full.Par visitPar(Full.Par par){
    var e0=par._that();
    var xs0=par.xs();
    var es0=par.es();
    var e=e0==null?null:visitE(e0);
    var xs=visitXs(xs0);
    var es=visitFullEs(es0);
    if(e==e0 && xs==xs0 && es==es0){return par;}
    return new Full.Par(e,xs,es);
    }
      
  public Full.T visitT(Full.T t){
    var docs0=t.docs();
    var cs0=t.cs();
    var _p0=t._p();
    var docs=visitFullDocs(docs0);
    var cs=visitCs(cs0);
    var _p=_p0==null?null:visitP(_p0);
    if(docs==docs0 && cs==cs0 && _p==_p0){return t;}
    return new Full.T(t._mdf(),docs,cs,_p);
    }
    
  public Full.Doc visitDoc(Full.Doc doc){
    var pathSel0=doc._pathSel();
    var docs0=doc.docs();
    var pathSel=pathSel0==null?null:visitPathSel(pathSel0);
    var docs=visitFullDocs(docs0);
    if(pathSel==pathSel0 && docs==docs0){return doc;}
    return new Full.Doc(pathSel,doc.texts(),docs);
    }
      
  public Full.PathSel visitPathSel(Full.PathSel pathSel){
    var cs0=pathSel.cs();
    var _p0=pathSel._p();
    var s0=pathSel._s();
    var x0=pathSel._x();
    var cs=cs0=visitCs(cs0);
    var _p=_p0==null?null:visitP(_p0);
    var s=s0==null?null:visitS(s0);
    var x=x0==null?null:visitX(x0);
    if(cs==cs0 && _p==_p0 && s==s0 && x==x0){return pathSel;}
    return new Full.PathSel(cs,_p,s,x);
    }
    
  public Full.MH visitMH(Full.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    var docs=visitFullDocs(docs0);
    var t=visitT(t0);
    var s=visitS(s0);
    var pars=visitFullTs(pars0);
    var exceptions=visitFullTs(exceptions0);
    if(docs==docs0 && t==t0 && s==s0 && pars==pars0 && exceptions==exceptions0){return mh;}
    return new Full.MH(mh._mdf(),docs,t,mh._op(),mh.n(),s,pars,exceptions);
    }
  public Program visitProgram(Program program) { 
    LL ll=program.top.visitable().accept(this);
    PTails pTails=program.pTails.accept(this);
    return new Program(ll,pTails);
    }
  public PTails visitPTails(PTails t) {
    if(t.isEmpty()){return t;}
    LL ll=t.ll().visitable().accept(this);
    PTails tail=t.tail().accept(this);
    if(!t.hasC()){return tail.pTailSingle((Core.L)ll);}
    return tail.pTailC(visitC(t.c()),ll);    
    }
  }
