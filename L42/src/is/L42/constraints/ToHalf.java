package is.L42.constraints;

import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.NameMangling;
import is.L42.common.TypeManipulation;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Full.Par;
import is.L42.generated.Half;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.visitors.UndefinedCollectorVisitor;
import is.L42.visitors.Visitable;

public class ToHalf extends UndefinedCollectorVisitor{
  static class Res<T>{
    public final T e;
    public final List<ST> resSTz;
    public final List<ST> retSTz;
    Res(T e, List<ST> resSTz, List<ST> retSTz){
      this.e=e;this.resSTz=resSTz;this.retSTz=retSTz;
      }
    }
  Y y;
  CTz ctz;
  Res<?> res;
  public final Res<Half.E> compute(Full.E e){
    assert res==null;
    e.visitable().accept(this);
    assert res!=null;
    @SuppressWarnings("unchecked")
    Res<Half.E> aux=(Res<Half.E>)res;
    res=null;
    return aux;
    }
  public final void commit(Half.E e, List<ST> resSTz, List<ST> retSTz){
    res=new Res<Half.E>(e,resSTz,retSTz);
    }    
  public final Res<Half.D> computeD(Full.D d){
    assert res==null;
    d.accept(this);
    assert res!=null;
    @SuppressWarnings("unchecked")
    Res<Half.D> aux=(Res<Half.D>)res;
    res=null;
    return aux;
    }
  public final void commitD(Half.D d, List<ST> retSTz){
    res=new Res<Half.D>(d,L(),retSTz);
    }    
  public final Res<Half.K> computeK(Full.K k){
    assert res==null;
    k.accept(this);
    assert res!=null;
    @SuppressWarnings("unchecked")
    Res<Half.K> aux=(Res<Half.K>)res;
    res=null;
    return aux;
    }
  public final void commitK(Half.K k, List<ST> resSTz, List<ST> retSTz){
    res=new Res<Half.K>(k,resSTz,retSTz);
    }    


  private boolean expectedAny(){
    var et=y._expectedT();
    if(et==null || et.isEmpty()){return false;}
    return et.stream().allMatch(e->e.equals(P.coreAny));
    }
  @Override public void visitL(Core.L l){commit(l,L(P.coreLibrary),L());}
  @Override public void visitL(Full.L l){commit(l,L(P.coreLibrary),L());}

  @Override public void visitEX(Core.EX x){
    commit(x,y.g().of(x.x()),L());
    }
  @Override public void visitEVoid(Core.EVoid eVoid){
    commit(eVoid,L(P.coreVoid),L());
    }
  @Override public void visitCsP(Full.CsP csP){
    assert csP._p()!=null;
    if(expectedAny()){
      visitCast(new Full.Cast(csP.pos(),csP,P.fullClassAny));
      return;
      }
    var t=P.fullClassAny.with_p(csP._p());
    visitCast(new Full.Cast(csP.pos(),csP,t));    
    }
  @Override public void visitCast(Full.Cast cast){
    if(!(cast.e() instanceof Full.CsP)){throw uc;} 
    var csp=(Full.CsP)cast.e();
    var p=csp._p();
    var t=TypeManipulation.toCore(cast.t());
    commit(new Core.PCastT(cast.pos(),p,t),L(t),L());
    }
  @Override public void visitThrow(Full.Throw thr){
    Y oldY=y;
    y=y.with_expectedT(null);
    var res=compute(thr.e());
    y=oldY;
    List<ST> stz2;
    if(thr.thr()!=ThrowKind.Return){stz2=res.retSTz;}
    else{stz2=mergeU(res.resSTz,res.retSTz);}
    commit(new Half.Throw(thr.pos(),thr.thr(),res.e),L(),stz2);
    }
  @Override public void visitLoop(Full.Loop loop){
    Y oldY=y;
    y=y.with_expectedT(P.stzCoreVoid);
    var res=compute(loop.e());
    y=oldY;
    ctz.plusAccCopy(y.p(), res.resSTz,P.stzCoreVoid);
    commit(new Half.Loop(loop.pos(),res.e),P.stzCoreVoid,res.retSTz);
    }
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
    if(opUpdate.op()!=Op.ColonEqual){throw uc;}
    Y oldY=y;
    y=y.with_expectedT(y.g().of(opUpdate.x()));
    var res=compute(opUpdate.e());
    y=oldY;
    ctz.plusAccCopy(y.p(), res.resSTz, y.g().of(opUpdate.x()));
    commit(new Half.Loop(opUpdate.pos(),res.e),P.stzCoreVoid,res.retSTz);        
    }  
  boolean isFullXP(Full.E e){//Can not be a marker interface since it depends on the values
    if(e instanceof Full.CsP || e instanceof Full.Slash ||e instanceof Core.EX){return true;}
    if(!(e instanceof Full.Cast)){return false;}
    var c=(Full.Cast)e;
    return c.e() instanceof Full.CsP || c.e() instanceof Full.Slash;
    }
    
  private List<Par> addThats(List<Par> pars){
    return L(pars,par->{
      if(par._that()==null){return par;}
      par=par.withEs(pushL(par._that(),par.es()));
      par=par.withXs(pushL(X.thatX,par.xs()));
      return par;
      });
    }
  
  @Override public void visitCall(Full.Call call){
    if(call._s()==null){visitCall(call.with_s(NameMangling.hashApply()));return;}
    var pars=addThats(call.pars());
    if(pars.size()!=1){throw uc;}
    Par par=pars.get(0);
    if(!isFullXP(call.e())){throw uc;}
    Y oldY=y;
    y=y.with_expectedT(P.stzCoreVoid);
    var resXP=compute(call.e());
    S s=call._s().withXs(par.xs());
    List<ST> stz1=L(resXP.resSTz,sti->new ST.STMeth(sti, s, -1));
    ArrayList<Half.E> es=new ArrayList<>();
    ArrayList<ST> retST=new ArrayList<>();
    for(int i:range(s.xs())){
      List<ST> stz1i=L(resXP.resSTz,sti->new ST.STMeth(sti, s, i+1));
      Y yi=y.withOnSlash(stz1i)
        .with_onSlashX((Half.XP)resXP.e)
        .with_expectedT(stz1i);
      y=yi;
      var resi=compute(par.es().get(i));
      ctz.plusAccCopy(y.p(), resi.resSTz, stz1i); 
      es.add(resi.e);
      retST.addAll(resi.retSTz);    
      }    
    y=oldY;
    commit(new Half.MCall(call.pos(), (Half.XP)resXP.e, s,L(es.stream())),stz1,L(retST.stream().distinct()));
    }
  @Override public void visitBlock(Full.Block block){
    if(block.isCurly()){throw uc;}
    if(block._e()==null){throw uc;}
    if(block.dsAfter()!=block.ds().size()){throw uc;}
    Y oldY=y;
    y=y.withG(y.g().plusEq(block.ds()));
    ArrayList<Half.D> ds=new ArrayList<>();
    ArrayList<Half.K> ks=new ArrayList<>();
    ArrayList<ST> resST=new ArrayList<>();
    ArrayList<ST> retST=new ArrayList<>();
    for(Full.D d:block.ds()){
      var res=computeD(d);
      ds.add(res.e);
      retST.addAll(res.retSTz); //resST is empty
      y=y.withG(y.g().plusEq(res.e.x(),res.e.stz()));
      }
    for(Full.K k:block.ks()){
      var res=computeK(k);
      ks.add(res.e);
      resST.addAll(res.resSTz);
      retST.addAll(res.retSTz);
      }
    var res=compute(block._e());
    commit(new Half.Block(block.pos(),L(ds.stream()),L(ks.stream()),res.e),resST,retST);
    y=oldY;
    }
  @Override public void visitD(Full.D d){
    assert d._e()!=null;
    if(d._varTx()==null || !d.varTxs().isEmpty()){throw uc;}
    if(d._varTx()._x()==null){throw uc;}
    Y oldY=y;
    List<ST> t=null;
    if(d._varTx()._t()!=null){
      t=L(TypeManipulation.toCore(d._varTx()._t()));
      }
    y=y.with_expectedT(t);
    var res=compute(d._e());
    if(t==null){t=res.resSTz;}
    else{ctz.plusAccCopy(y.p(),res.resSTz,t);}
    var hd=new Half.D(d._varTx()._mdf(), t, d._varTx()._x(),res.e);
    commitD(hd,res.retSTz);
    y=oldY;
    }
  @Override public void visitK(Full.K k){
    if(k._x()==null || k._thr()==null){throw uc;}
    Y oldY=y;
    List<ST> t=L(TypeManipulation.toCore(k.t()));
    y=y.withG(y.g().plusEq(k._x(),t));
    var res=compute(k.e());
    Half.K kr=new Half.K(k._thr(),t,k._x(),res.e);
    commitK(kr,res.resSTz,res.retSTz);
    y=oldY;
    }
  
  
  @Override public void visitIf(Full.If sIf){throw uc;}
  @Override public void visitWhile(Full.While sWhile){throw uc;}
  @Override public void visitFor(Full.For sFor){throw uc;}
  @Override public void visitSlash(Full.Slash slash){throw uc;}
  @Override public void visitSlashX(Full.SlashX slashX){throw uc;}
  @Override public void visitEString(Full.EString eString){throw uc;}
  @Override public void visitEPathSel(Full.EPathSel ePathSel){throw uc;}
  @Override public void visitUOp(Full.UOp uOp){throw uc;}
  @Override public void visitBinOp(Full.BinOp binOp){throw uc;}
}