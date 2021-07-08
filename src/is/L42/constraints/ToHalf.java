package is.L42.constraints;

import static is.L42.tools.General.L;
import static is.L42.tools.General.LL;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unique;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.CTz;
import is.L42.common.NameMangling;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Full.D;
import is.L42.generated.Full.E;
import is.L42.generated.Full.Par;
import is.L42.generated.Full.VarTx;
import is.L42.generated.Half;
import is.L42.generated.Half.K;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.Op.OpKind;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.Returning;
import is.L42.visitors.UndefinedCollectorVisitor;

public class ToHalf extends UndefinedCollectorVisitor{
  public static class Res<T>{
    public final T e;
    public final List<ST> resSTz;
    public final List<ST> retSTz;
    Res(T e, List<ST> resSTz, List<ST> retSTz){
      this.e=e;this.resSTz=resSTz;this.retSTz=retSTz;
      }
    }
  public ToHalf(Y y,CTz ctz,FreshNames fresh){
    this.y=y;
    this.ctz=ctz;
    this.fresh=fresh;
    }
  Y y;
  CTz ctz;
  FreshNames fresh;
  Res<Half.E> res;
  public final Res<Half.E> compute(Full.E e){
    assert ctz.coherent(y.p());
    assert res==null;
    e.visitable().accept(this);
    assert res!=null;
    var aux=res;
    res=null;
    return aux;
    }
  public final void commit(Half.E e, List<ST> resSTz, List<ST> retSTz){
    res=new Res<Half.E>(e,resSTz,retSTz);
    }
  private boolean expectedAny(){
    var et=y._expectedT();
    if(et==null || et.isEmpty()){return false;}
    et=y.p().solve(et);
    return et.stream().allMatch(e->e.equals(P.coreClassAny));
    }
  @Override public void visitL(Core.L l){commit(l,L(P.coreLibrary),L());}
  @Override public void visitL(Full.L l){commit(l,L(P.coreLibrary),L());}

  @Override public void visitEX(Core.EX x){
    var t=y.g()._of(x.x());
    if (t==null){commit(x,L(),L());}
    else{commit(x,t,L());}
    }
  @Override public void visitEVoid(Core.EVoid eVoid){
    commit(eVoid,L(P.coreVoid),L());
    }
  @Override public void visitSlash(Full.Slash slash){
    var res=new Half.SlashCastT(slash.pos(), y.onSlash(),y.onSlash());
    commit(res,y.onSlash(),L());
    }  
  private void commitMCall(Y oldY,Pos pos,S s,List<Full.E>es){
    Half.XP xp=this.y._onSlashX();
    assert xp!=null;
    List<ST> stz=expectedRes(xp);
    ArrayList<Half.E> esRes=new ArrayList<>();
    ArrayList<ST> retST=new ArrayList<>();
    for(int i:range(s.xs())){//i in 1..n
      List<ST> stz1i=L(stz,sti->new ST.STMeth(sti, s, i+1));
      y=y.withOnSlash(stz1i).with_expectedT(stz1i);
      var resi=compute(es.get(i));
      ctz.plusAcc(y.p(), resi.resSTz, stz1i); 
      esRes.add(resi.e);
      retST.addAll(resi.retSTz);    
      }    
    y=oldY;
    List<ST> stzRes=L(stz,sti->new ST.STMeth(sti, s, -1));
    commit(new Half.MCall(pos, xp, s,LL(esRes)),stzRes,unique(retST));
    }
  List<ST> expectedRes(Half.XP xp){
    if(xp instanceof Core.EX){return y.g()._of(((Core.EX)xp).x());}
    if(xp instanceof Half.PCastT){return ((Half.PCastT)xp).stz();}
    assert xp instanceof Half.SlashCastT;
    return ((Half.SlashCastT)xp).stz1();
    //Note: should it be stz1() or stz()? it was stz, but looked wrong
    }
  @Override public void visitSlashX(Full.SlashX slashX){
    var call=new Full.Call(slashX.pos(),slashX,null,false,Full.Par.emptys);
    visitCall(call);
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
    if(!(cast.e() instanceof Full.CsP)){
      X x=freshX("casted");
      Full.D d=makeDec(cast.t(),x,cast.e());
      var e=new Core.EX(cast.pos(),x);
      visitBlock(makeBlock(cast.pos(),L(d),e));
      return;
      } 
    var csp=(Full.CsP)cast.e();
    var p=csp._p();
    var t=TypeManipulation.toCore(cast.t());
    commit(new Half.PCastT(cast.pos(),p,L(t)),L(t),L());
    }
  private Full.D makeDec(Full.T _t,X x, Full.E e) {
    return new Full.D(new Full.VarTx(false,_t,null,x),L(), e);
    }
  private Full.D makeDec(Full.E e) {
    return new Full.D(null,L(), e);
    }

  private Full.Block makeBlock(Pos pos,List<Full.D>ds,Full.E e){
    return new Full.Block(pos,false, ds,ds.size(), L(),L(),e);
    }
  @Override public void visitEPathSel(Full.EPathSel p){
    var receiver=new Full.Slash(p.pos());
    var content=p.pathSel().toString();
    var s=new Full.EString(p.pos(),0,L(receiver),L(content));
    visitEString(s);
    }
  @Override public void visitUOp(Full.UOp u){
    if(u._op()==null){
      assert u._num()!=null;
      var s=new Full.EString(u.pos(),0,L(u.e()),L(u._num()));
      visitEString(s);
      return;
      }
    String name=NameMangling.methName(u._op(),0);
    S s=new S(name,L(),-1);
    var m=new Full.Call(u.pos(),u.e(),s,false,Full.Par.emptys);
    visitCall(m);
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
    ctz.plusAcc(y.p(), res.resSTz,P.stzCoreVoid);
    commit(new Half.Loop(loop.pos(),res.e),P.stzCoreVoid,res.retSTz);
    }
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
    if(opUpdate.op()!=Op.ColonEqual){opUpdate=doUpdate(opUpdate);}
    Y oldY=y;
    y=y.with_expectedT(y.g().of(opUpdate.x()));
    var res=compute(opUpdate.e());
    y=oldY;
    ctz.plusAcc(y.p(), res.resSTz, y.g().of(opUpdate.x()));
    commit(new Half.OpUpdate(opUpdate.pos(),opUpdate.x(),res.e),P.stzCoreVoid,res.retSTz);        
    }  
  private Full.OpUpdate doUpdate(Full.OpUpdate ou) {
    if(ou.op()==Op.ColonEqual){return ou;}
    Pos p=ou.pos();
    Op op=ou.op().nonEqOpVersion();
    List<Full.E> es=List.of(new Core.EX(p,ou.x()),ou.e());
    Full.E e=new Full.BinOp(p,op,es);
    return new Full.OpUpdate(p,ou.x(),Op.ColonEqual,e);
    }
  boolean isFullXP(Full.E e){//Can not be a marker interface since it depends on the values
    if(e instanceof Full.CsP || e instanceof Full.Slash ||e instanceof Core.EX){return true;}
    if(!(e instanceof Full.Cast)){return false;}
    var c=(Full.Cast)e;
    return c.e() instanceof Full.CsP || c.e() instanceof Full.Slash;
    }
    
  private List<Full.Par> addThats(List<Full.Par> pars){
    return L(pars,par->{
      if(par._that()==null){return par;}
      par=par.withEs(pushL(par._that(),par.es()));
      par=par.withXs(pushL(X.thatX,par.xs()));
      return par.with_that(null);
      });
    }
  @Override public void visitCall(Full.Call call){
    boolean slashXRec=call.e() instanceof Full.SlashX;
    if(!slashXRec && call._s()==null){call=call.with_s(NameMangling.hashApply());}
    if(call.isSquare()){call=expandSquare(call);}
    var pars=addThats(call.pars());
    assert !pars.isEmpty();
    Full.Par par=pars.get(0);
    if(!isFullXP(call.e())&& !(slashXRec && call._s()==null)){
      X rec=freshX("receiver");
      Full.D d=makeDec(null,rec,call.e());
      Full.E c=call.withE(new Core.EX(call.pos(),rec));
      visitBlock(makeBlock(call.pos(),L(d),c));
      return;
      }
    Y oldY=y;
    if(call._s()==null){//we are on a \foo(par)
      var slashX=(Full.SlashX)call.e();
      S s=new S(slashX.x().inner(),L(),-1);
      s=s.withXs(par.xs());
      commitMCall(oldY,call.pos(),s,par.es());
      return;}
    S s=call._s().withXs(par.xs());
    var recXP=compute(call.e());
    y=y.with_onSlashX((Half.XP)recXP.e);
    commitMCall(oldY,call.pos(),s,par.es());
    }
  @Override public void visitBlock(Full.Block block){
    if(block.isCurly()){curlyBlock(block);return;}
    if(block._e()==null){
      assert block.dsAfter()==block.ds().size();
      var lastD= block.ds().get(block.ds().size()-1);
      assert lastD._e()!=null && lastD._varTx()==null && lastD.varTxs().isEmpty();
      Full.E end=new Core.EVoid(block.pos());
      if(Returning.of(lastD._e())){
        end=new Full.Throw(block.pos(), ThrowKind.Error, end);
        }
      block=block.with_e(end);
      }
    if(block.dsAfter()!=block.ds().size()){block=splitBlock(block);}
    if(!block.whoopsed().isEmpty()){block=expandWhoopses(block);}
    Y oldY=y;
    y=y.withG(y.g().plusEq(block.ds()));
    ArrayList<Half.D> ds=new ArrayList<>();
    ArrayList<Half.K> ks=new ArrayList<>();
    ArrayList<ST> resST=new ArrayList<>();
    ArrayList<ST> retSTDs=new ArrayList<>();
    ArrayList<ST> retSTKs=new ArrayList<>();
    for(Full.D d:block.ds()){
      var res=auxD(d);
      ds.addAll(res.e);
      retSTDs.addAll(res.retSTz); //resST is empty
      for(var dRes:res.e){
        y=y.withG(y.g().plusEqOver(dRes.x(),dRes.stz()));
        }
      }
    for(Full.K k:block.ks()){
      var res=auxK(k);
      ks.add(res.e);
      resST.addAll(res.resSTz);
      retSTKs.addAll(res.retSTz);
      }
    var res=compute(block._e());
    resST.addAll(res.resSTz);
    retSTKs.addAll(res.retSTz);
    removeRets(ks,retSTDs,retSTKs);
    commit(new Half.Block(block.pos(),LL(ds),LL(ks),res.e),resST,retSTKs);
    y=oldY;
    }
  private void removeRets(ArrayList<K> ks, ArrayList<ST> all,ArrayList<ST> dest){
    for(var k:ks){
      if(k.thr()!=ThrowKind.Return){continue;}
      if(k.stz().stream().anyMatch(this::isAny)){
        all.clear();break;}
      all.removeAll(k.stz());
      }
    dest.addAll(all);
    }
  private boolean isAny(ST st){
    if(st instanceof Core.T t){return t.p().equals(P.pAny);}
    return false;
    }
  private Res<List<Half.D>> auxD(Full.D d){
    assert d._e()!=null;
    if(d._varTx()==null){d=d.with_varTx(immVoid_);}
    if( !d.varTxs().isEmpty()){ return auxMultiD(d);}
    if(d._varTx()._x()==null){d=d.with_varTx(d._varTx().with_x(freshX("underscore")));}
    Y oldY=y;
    List<ST> t=null;
    if(d._varTx()._t()!=null){
      t=L(TypeManipulation.toCore(d._varTx()._t()));
      }
    y=y.with_expectedT(t);
    var res=compute(d._e());
    if(t==null){t=res.resSTz;}
    else{ctz.plusAcc(y.p(),res.resSTz,t);}
    Mdf mdf=d._varTx()._mdf();
    if(mdf==null && d._varTx()._t()!=null){mdf=d._varTx()._t()._mdf();}
    var hd=new Half.D(d._varTx().isVar(),mdf, t, d._varTx()._x(),res.e);
    y=oldY;
    return new Res<>(L(hd),L(),res.retSTz);
    }
  private boolean hasMdf(VarTx v,Mdf m){
    if(v._mdf()==m){return true;}
    return v._t()!=null && v._t()._mdf()==m;
    }
  private Mdf propagateSubMdf(Full.D d){
    var m=d._varTx()._mdf();
    if(m!=null) {return m;}
    if(d._varTx()._t()!=null){return null;}
    boolean hasCap=d.varTxs().stream().anyMatch(v->hasMdf(v,Mdf.Capsule));
    if(hasCap){return Mdf.Mutable;}
    boolean hasMut=d.varTxs().stream().anyMatch(v->hasMdf(v,Mdf.Mutable));
    if(hasMut){return Mdf.Mutable;}
    boolean hasLent=d.varTxs().stream().anyMatch(v->hasMdf(v,Mdf.Lent));
    if(hasLent){return Mdf.Lent;}
    return null;
    }
  private Res<List<Half.D>> auxMultiD(Full.D d){
    X x=freshX("DecMatch");
    Core.EX ex=new Core.EX(d._e().pos(),x);
    Full.D step0=d.withVarTxs(L()).with_varTx(d._varTx().with_x(x).with_mdf(propagateSubMdf(d)));    
    List<ST>retST=new ArrayList<>();
    List<Half.D> ds=new ArrayList<>();
    var res=auxD(step0);
    ds.addAll(res.e);
    retST.addAll(res.retSTz);
    Y oldY=y;
    y=y.withG(y.g().plusEq(x,res.e.get(0).stz()));
    for(var vtx:d.varTxs()){
      S s=NameMangling.methNameTrim(vtx._x());
      var addHash=vtx._mdf()!=null && vtx._mdf().isIn(Mdf.Capsule,Mdf.Mutable,Mdf.Lent);
      addHash|=vtx._t()!=null && vtx._t()._mdf()!=null && vtx._t()._mdf().isIn(Mdf.Capsule,Mdf.Mutable,Mdf.Lent);
      if(addHash){s=s.withM("#"+s.m());}
      var mCall=new Full.Call(ex.pos(),ex,s,false,Par.emptys);
      Full.D di=new Full.D(vtx,L(),mCall);
      var resi=auxD(di);
      ds.addAll(resi.e);
      retST.addAll(resi.retSTz);
      };
    y=oldY;
    return new Res<>(ds,L(),retST);
    }
    
  private Full.Block splitBlock(Full.Block b){
    List<Full.D> ds0=b.ds().subList(0,b.dsAfter());
    List<Full.D> ds1=b.ds().subList(b.dsAfter(),b.ds().size());
    assert !b.isCurly();
    assert !ds0.isEmpty();
    assert !ds1.isEmpty();
    var inner=new Full.Block(b.pos(),false,ds1,ds1.size(),L(),L(),b._e());
    return new Full.Block(b.pos(), false, ds0, ds0.size(), b.ks(),b.whoopsed(), inner);
    }
  private void curlyBlock(Full.Block b){
    Pos p=b.pos();
    X x=freshX("curlyX");
    X x1=freshX("curlyX1");
    Core.EX ex1=new Core.EX(p,x1);
    var innerBlock=b.withCurly(false).with_e(new Core.EVoid(p));
    Y oldY=y;
    y=y.with_expectedT(P.stzCoreVoid);
    var res=compute(innerBlock);
    y=oldY;
    var kStz=res.retSTz;
    if(y._expectedT()!=null){kStz=mergeU(y._expectedT(),kStz);}
    Half.K k=new Half.K(ThrowKind.Return,kStz,x1,ex1);
    Half.D d=new Half.D(false,null,L(P.coreVoid), x,res.e);
    Half.E end=new Half.Throw(p,ThrowKind.Error,new Core.EVoid(p));
    commit(new Half.Block(b.pos(),L(d),L(k),end),res.resSTz,L());
    }
  private Full.Call expandSquare(Full.Call s){
    Pos p=s.pos();
    assert s.isSquare();
    X x=freshX("builder");
    Core.EX ex=new Core.EX(p,x);
    boolean okSlashX=s.e() instanceof Full.SlashX && s._s()==null;
    var sel=okSlashX?
      new S(((Full.SlashX)s.e()).x().inner(),L(),-1)
      :s._s();
    var squareB=new Full.Call(p,new Full.Slash(p),squareBuilder(sel),false,Par.emptys);
    var decX=makeDec(null,x,squareB);
    List<Par> pars=s.pars();
    if(pars.size()>=2){
      var last=pars.get(pars.size()-1);
      if(last._that()==null && last.es().isEmpty()){
        pars=pars.subList(0, pars.size()-1);
        }
      }
    List<Full.D> e1n=L(pars,(c,pi)->
      c.add(makeDec(new Full.Call(p,ex,squareAddS,false,L(pi))))
      );
    Full.E block=makeBlock(p,e1n,new Core.EVoid(p));
    var eIf=new Full.If(p,squareB.with_s(shortCircutSquare),L(),block,null);
    var builderBlock=makeBlock(p,List.of(decX,makeDec(eIf)),ex);
    var mCallPar=new Full.Par(null, squareBuilderX, L(builderBlock));
    var mCall=new Full.Call(p,s.e(),s._s(),false,L(mCallPar));
    return mCall;
    }
  private Full.Block expandWhoopses(Full.Block b){
    Pos p=b.pos();
    List<Full.K> ks=L(c->{
      c.addAll(b.ks());
      for(Full.T w:b.whoopsed()){
        X xi=freshX("whoops");
        var exi=new Core.EX(p,xi);
        var s=new S("#whoopsed",L(),-1);
        X atPos=new X("atPos");
        Par par=new Par(null,L(atPos),L(Program.emptyL.withPoss(L(p))));
        //Ki = catch exception Ti xi error 
        Full.E errE=new Full.Call(p,exi,s,false,L(par));
        errE=new Full.Throw(p,ThrowKind.Error,errE);
        c.add(new Full.K(ThrowKind.Exception,w, xi,errE));
        }
      });
      return b.withKs(ks);
    }
  private Res<Half.K> auxK(Full.K k){
    if(k._x()==null){k=k.with_x(freshX("underscore"));}
    if(k._thr()==null){k=k.with_thr(ThrowKind.Exception);}
    Y oldY=y;
    List<ST> t=L(TypeManipulation.toCore(k.t()));
    y=y.withG(y.g().plusEq(k._x(),t));
    var res=compute(k.e());
    Half.K kr=new Half.K(k._thr(),t,k._x(),res.e);
    y=oldY;
    return new Res<>(kr,res.resSTz,res.retSTz);
    }
  private X freshX(String s){return new X(fresh.fresh(s));}
  @Override public void visitBinOp(Full.BinOp binOp){
    assert binOp.es().size()>=2:binOp.es();
    while(binOp.es().size()!=2 && binOp.op().kind!=OpKind.RelationalOp){
      binOp=applyAssociativity(binOp);;
      }
    if(binOp.op().kind==OpKind.BoolOp){visitBinOp3(binOp);return;}
    if(binOp.es().get(0) instanceof Full.CsP){binOp=visitBinOpCsp(binOp);}
    List<Full.E> xps=new ArrayList<>();
    List<Full.D> ds=L(binOp.es(),(c,ei)->{
      if(isFullXP(ei)){xps.add(ei);return;}
      X xi=freshX("op");
      xps.add(new Core.EX(ei.pos(),xi));
      c.add(makeDec(null, xi, ei));
      });
    if(!ds.isEmpty()){
      visitBlock(makeBlock(binOp.pos(), ds, binOp.withEs(LL(xps))));
      return;
      }
    ArrayList<List<ST>> opArgs=new ArrayList<>();
    ArrayList<ST> retSTz=new ArrayList<>();
    Y oldY=y;
    if(binOp.op().kind!=OpKind.RelationalOp){this.y=this.y.with_expectedT(L());}
    else {this.y=this.y.with_expectedT(L(P.coreClassAny));}
    List<Half.XP> es=L(binOp.es(),(c,ei)->{
      var ri=compute(ei);
      c.add((Half.XP)ri.e);
      opArgs.add(ri.resSTz);
      retSTz.addAll(ri.retSTz);
      });
    this.y=oldY;
    var resST=new ST.STOp(binOp.op(),LL(opArgs));
    commit(new Half.BinOp(binOp.pos(),binOp.op(), es), L(resST), retSTz);
    }
  private Full.BinOp applyAssociativity(Full.BinOp b){
    int s=b.es().size();
    assert s>2:s;
    if(b.op().kind==OpKind.DataLeftOp){
      var left=new Full.BinOp(b.pos(),b.op(),b.es().subList(0, 2));
      return b.withEs(pushL(left,b.es().subList(2,s)));
      }
    var right=new Full.BinOp(b.pos(),b.op(),b.es().subList(s-2, s));
    return b.withEs(pushL(b.es().subList(0,s-2),right)); 
    }// a : b : (c : d)    // (a + b) + c + d
  private void visitBinOp3(Full.BinOp b){
    Pos p=b.pos();
    var e0=b.es().get(0);
    var e1=b.es().get(1);
    assert b.es().size()==2;
    X x=freshX("op3");
    var ex=new Core.EX(p,x);
    if(!isFullXP(e0)){
      var d=makeDec(null,x,e0);
      visitBlock(makeBlock(p,L(d),b.withEs(List.of(ex,e1))));
      return;
      }
    //from here, e0 is xP, and x is x
    S sc=NameMangling.shortCircuit(b.op());
    S sr=NameMangling.shortResult(b.op());
    S sp=NameMangling.shortProcess(b.op());
    X other=new X("other");
    Par par1=new Par(ex,L(),L());
    Par par2=new Par(ex,L(other),L(e1));
    var scCall=new Full.Call(p,e0,sc,false,Par.emptys);
    var srCall=new Full.Call(p,e0,sr,false,L(par1));
    var spCall=new Full.Call(p,e0,sp,false,L(par2));
    var dx=makeDec(null,x,scCall);
    var ifElse=new Full.If(p, ex,L(), srCall,spCall);
    visitBlock(makeBlock(p, L(dx),ifElse));
    }
  private Full.BinOp visitBinOpCsp(Full.BinOp b){
    assert b.es().get(0) instanceof Full.CsP;
    var csp=(Full.CsP)b.es().get(0);
    var e=b.es().get(1);
    assert b.es().size()==2:
      "";
    assert csp.cs().isEmpty();
    assert csp._p().isNCs();//should be a well formedness error
    var p=csp._p().toNCs();
    p=p.withCs(pushL(p.cs(),classOperators));
    var pos=b.pos();
    var methC=new Full.Call(pos,new Full.CsP(pos,L(),p),applyZero,false,Par.emptys);
    return b.withEs(List.of(methC,e));
    }
  @Override public void visitEString(Full.EString s){
    Pos p=s.pos();
    assert !s.es().isEmpty();
    assert s.es().size()==s.strings().size();
    Full.E block=strLitPar(p,s);
    var mCallPar=new Full.Par(null, stringLiteralX, L(block));
    var mCall=new Full.Call(p,s.es().get(0),fromS,false,L(mCallPar));
    visitCall(mCall);
    }
  private Full.E strLitPar(Pos p,String content){
    var strB=new Full.Call(p,new Full.Slash(p), stringLiteralBuilder,false,Par.emptys);
    X x=freshX("builder");
    Core.EX ex=new Core.EX(p,x);
    var decX=makeDec(null,x,strB);
    var e1nStream=content.chars().boxed().map(c->{
      S sChar=NameMangling.charName(c);
      var call=new Full.Call(p,ex,sChar,false,Par.emptys);
      return makeDec(call);
      });
    List<Full.D> e0n=L(Stream.concat(Stream.of(decX),e1nStream));
    return makeBlock(p,e0n,ex);
    }
  private Full.E strLitPar(Pos p,Full.EString s){
    if(s.es().size()==1){return strLitPar(p,s.strings().get(0));}
    var strB=new Full.Call(p,new Full.Slash(p), stringLiteralBuilder,false,Par.emptys);
    X x=freshX("builder");
    Core.EX ex=new Core.EX(p,x);
    var decX=makeDec(null,x,strB);
    List<Full.D> e0n=L(c->{
      c.add(decX);
      for(int i:range(s.es().size()-1)){
        String si=s.strings().get(i);
        Full.E ei=s.es().get(i+1);
        var bi=strLitPar(p,si);
        var e1i=new Full.Call(p,ex,stringAddAllS,false,L(new Par(bi,L(),L())));
        var e2i=new Full.Call(p,ex,stringAddExprS,false,L(new Par(ei,L(),L())));
        c.add(makeDec(e1i));
        c.add(makeDec(e2i));
        }
      var bLast=strLitPar(p,s.strings().get(s.strings().size()-1));
      var e1Last=new Full.Call(p,ex,stringAddAllS,false,L(new Par(bLast,L(),L())));
      c.add(makeDec(e1Last));
      });
    return makeBlock(p,e0n,ex);
    }
  private void visitIfMatchTxy(Full.If o,D m){
    X x=freshX("typeMatch");
    var vtx0=m._varTx();
    if(vtx0==null){vtx0=new VarTx(false,null,null,x);}
    else{vtx0=vtx0.with_x(x);}
    var m0=m.withVarTxs(L()).with_varTx(vtx0);
    List<VarTx> withTs=L(m.varTxs().stream().filter(v->v._t()!=null));
    var d0 = dFromTxs(m,new Core.EX(o.pos(), x));
    var e2=o.then();
    if(!withTs.isEmpty()){
      List<D>ms=L(withTs.stream().map(v->new Full.D(v,L(),null)));
      e2=new Full.If(e2.pos(),null,ms,e2,null);
      }
    var e0=makeBlock(o.then().pos(),L(d0), e2);
    visitIf(o.withMatches(L(m0)).withThen(e0));
    }
  private D dFromTxs(D m,Full.E e) {
    List<VarTx> noTs=L(m.varTxs().stream().map(v->v.with_t(null)));
    var d0=new Full.D(new VarTx(false, null, null, null),noTs,e);
    return d0;
  }
  private void visitIfMatch(Full.If sIf){
    assert sIf._condition()==null;
    assert sIf._else()==null;
    assert !sIf.matches().isEmpty();
    if(sIf.matches().size()>1){visitIfMatchPlus(sIf);return;}
    var m=sIf.matches().get(0);
    //x<:T case
    if(m._e()==null && m.varTxs().isEmpty()){visitIfMatchXCastT(sIf, m);return;}
    assert m._e()!=null;
    if(!m.varTxs().isEmpty()){
      //case if T (x,y)=e 
      if(m._varTx()!=null){visitIfMatchTxy(sIf,m);return;}
      //case if (x,y)=e 
      visitIfMatchXs(sIf,m);return;
      }
    //case T x=xP
    assert m._varTx()._t()!=null;
    if(!isFullXP(m._e())){visitBlock(ifMatchBlock(sIf.pos(),sIf,m));return;}
    //case T x=e
    visitIfMatchTxe(sIf,m);
    }
  private void visitIfMatchXCastT(Full.If sIf, D m) {
    assert m._varTx()!=null;
    assert sIf._else()==null && sIf._condition()==null;
    var x0=new Core.EX(sIf.pos(),m._varTx()._x());
    X freshX=freshX("typeCase");
    D m0=m.with_e(x0).with_varTx(m._varTx().with_x(freshX));
    var e0=sIf.then().visitable().accept(new CloneVisitor(){
      @Override public Core.EX visitEX(Core.EX ex){
        if(!ex.x().equals(x0.x())){return ex;}
        return ex.withX(freshX);
      }});
    visitIfMatch(sIf.withMatches(L(m0)).withThen(e0));
  }
  void visitIfMatchXs(Full.If sIf, D m){
    List<VarTx> txs=L(m.varTxs().stream().filter(v->v._t()!=null));
    assert !txs.isEmpty():"";
    assert m._e()!=null;
    var d0 = dFromTxs(m,m._e());
    List<D>ms=L(txs.stream().map(v->new Full.D(v,L(),null)));
    Full.If sIf0=sIf.withMatches(ms);
    var b=makeBlock(sIf.pos(),L(d0),sIf0);
    visitBlock(b);
    }
  
  void visitIfMatchTxe(Full.If sIf, D m){
    Pos p=sIf.pos();
    var t=m._varTx()._t();
    assert m._varTx()._x()!=null;
    X x1=freshX("cast");
    Core.EX ex1=new Core.EX(p,x1);
    var returnXP=makeDec(new Full.Throw(p,ThrowKind.Return,m._e()));
    var returnK=new Full.K(ThrowKind.Return,t,x1,ex1);
    var returnKAny=new Full.K(ThrowKind.Return,P.fullClassAny.with_mdf(t._mdf()),x1,new Core.EVoid(p));
    var innerB=new Full.Block(p,false,L(returnXP),1,L(returnK),L(),null);
    var decX=makeDec(t,m._varTx()._x(),innerB);
    var outerB=new Full.Block(p,false,L(decX),1,L(returnKAny),L(),sIf.then());
    visitBlock(outerB);    
    }
  private Full.Block ifMatchBlock(Pos p,Full.If sIf,D match){
    X x=freshX("cond");
    Core.EX ex=new Core.EX(p,x);
    var decX=makeDec(null,x,match._e());
    sIf=sIf.withMatches(L(match.with_e(ex)));
    return makeBlock(p,L(decX),sIf);
    }
  private void visitIfMatchPlus(Full.If sIf){
    var m=sIf.matches().get(0);
    sIf=sIf.withMatches(popL(sIf.matches()));
    sIf=sIf.withMatches(L(m)).withThen(sIf);
    visitIfMatch(sIf);
    }
  @Override public void visitIf(Full.If sIf){
    if(!sIf.matches().isEmpty()){visitIfMatch(sIf);return;}
    Pos p=sIf.pos();
    if(sIf._else()==null){sIf=sIf.with_else(new Core.EVoid(p));}
    if(sIf._condition()==null){uc();}
    if(!isFullXP(sIf._condition())){visitBlock(ifBlock(p,sIf));return;}
    Full.E test=new Full.Call(p,sIf._condition(),ifS,false,Par.emptys);
    test=new Full.Call(p,test,checkTrueS,false,Par.emptys);
    var k=new Full.K(ThrowKind.Exception,immVoid_._t(),null,sIf._else());
    var block=new Full.Block(p,false,L(makeDec(test)),1,L(k),L(),sIf.then());
    visitBlock(block);
    }
  private Full.Block ifBlock(Pos p,Full.If sIf){
    X x=freshX("cond");
    Core.EX ex=new Core.EX(p,x);
    var decX=makeDec(null,x,sIf._condition());
    sIf=sIf.with_condition(ex);
    return makeBlock(p,L(decX),sIf);
    }
  @Override public void visitWhile(Full.While sWhile){
    Pos p=sWhile.pos();
    Full.E e0=sWhile.condition();
    e0=new Full.Call(p,e0,checkTrueS,false,Par.emptys);
    var k=new Full.K(ThrowKind.Exception,immVoid_._t(),null,new Core.EVoid(p)); 
    var l=new Full.Loop(p,makeBlock(p,L(makeDec(e0)),sWhile.body()));
    visitBlock(new Full.Block(p,false,L(makeDec(l)),1,L(k),L(),null));
    }
  @Override public void visitFor(Full.For sFor){
    boolean allXPs=sFor.ds().stream().allMatch(di->isFullXP(di._e()));
    if(allXPs){visitBlock(forMain(sFor));}
    else{visitBlock(forXPs(sFor));}
    }
  private Full.Block forXPs(Full.For sFor){
    List<Full.D> newDs=new ArrayList<>();
    List<Full.D> ds=L(sFor.ds(),(c,di)->{
      if(isFullXP(di._e())){newDs.add(di);return;}
      X xi=freshX("forIt");
      newDs.add(di.with_e(new Core.EX(di._e().pos(),xi)));
      c.add(makeDec(null, xi, di._e()));
      });
    var sForSolved=sFor.withDs(newDs);
    return makeBlock(sFor.pos(), ds, forMain(sForSolved));
    }
  private Full.D dsElem(Pos p,String hint,Full.E e,boolean isVar,S sel,ArrayList<X> acc){
    X x=freshX(hint);
    acc.add(x);
    var call=new Full.Call(p,e,sel,false,Par.emptys);
    return new Full.D(new VarTx(isVar,null,null,x),L(),call);
  }
  private Full.E binMeth(Pos p,X xi,X x1i,S s){
    var xie=new Core.EX(p,xi);
    var x1ie=new Core.EX(p,x1i);
    return new Full.Call(p,xie,s,false,L(new Par(x1ie,L(),L())));    
    }
  private boolean useVarIterator(D d){
    if(d._varTx()!=null && useVarIterator(d._varTx())){return true;}
    for(var v:d.varTxs()){if(useVarIterator(v)){return true;}}
    return false;
    }
  private boolean useVarIterator(VarTx v){
    if(v.isVar()){return true;}
    if(v._mdf()!=null && v._mdf().isIn(Mdf.Mutable, Mdf.Lent,Mdf.Capsule)){return true;}
    return v._t()!=null && v._t()._mdf()!=null && v._t()._mdf().isIn(Mdf.Mutable, Mdf.Lent,Mdf.Capsule);
    }
  private Mdf _computeMdfi(VarTx v){
    if(v==null){return null;}
    if(v._mdf()!=null){return v._mdf();}
    if(v._t()==null){return null;}
    var m=v._t()._mdf();
    return m==null?Mdf.Immutable:m;
    }
  private Mdf _computeMdfi(D d){
    var v=d._varTx();
    Mdf res=_computeMdfi(v);
    if(res!=null){return res;}
    var mdfs=new ArrayList<Mdf>();
    for(var vi:d.varTxs()){
      var mi=_computeMdfi(vi);
      if(mi!=null){mdfs.add(mi);}
      }
    if(mdfs.isEmpty()){return null;}
    //order: capsule,mut,lent,read,imm
    if(mdfs.contains(Mdf.Capsule)) {return Mdf.Capsule;}
    if(mdfs.contains(Mdf.Mutable)) {return Mdf.Mutable;}
    if(mdfs.contains(Mdf.Lent)) {return Mdf.Lent;}
    if(mdfs.contains(Mdf.Readable)) {return Mdf.Readable;}
    if(mdfs.contains(Mdf.Immutable)) {return Mdf.Immutable;}
    return null;
    }
  private Full.Block forMain(Full.For sFor){
    Pos p=sFor.pos();
    Full.E ev=new Core.EVoid(p);
    var xIts=new ArrayList<X>();
    var xIndexs=new ArrayList<X>();
    List<Full.D> dsIts=L(sFor.ds().stream().map(d->//x1=xP1.#iterator()..xn=xPn.#iterator()
      dsElem(p,"xIt",d._e(),false,useVarIterator(d)?varIteratorS:iteratorS,xIts)));//need to turn to list so that xIts is filled, same below
    List<Full.D> dsStartIndexs=L(sFor.ds().stream().map(d->//var x'1 = xP1.#startIndex() .. var x'n = xPn.#startIndex()
      dsElem(p,"xIndex",d._e(),true,startIndexS,xIndexs)));
    List<Full.D> dsCloses=L(xIts,xIndexs,(c,xi,x1i)->//x1.#close(x'1) .. xn.#close(x'n)
      c.add(makeDec(binMeth(p,xi,x1i,closeS))));        
    List<Full.E> orsEs=L(xIts,xIndexs,(c,xi,x1i)->//( x1.#incomplete(x'1) || .. || xn.#incomplete(x'n)
      c.add(binMeth(p,xi,x1i,incompleteS)));
    Full.E ors=orsEs.get(0);
    if(orsEs.size()>1){ors=new Full.BinOp(p,Op.OrOr,orsEs);}
    List<Full.E> andsEs=pushL(L(xIts,xIndexs,(c,xi,x1i)->//x1.#hasElem(x'1) && .. && xn.#hasElem(x'n) && ors
      c.add(binMeth(p,xi,x1i,hasElemS))),ors);
    var cond=new Full.BinOp(p,Op.AndAnd,andsEs);
    Full.E[] e={sFor.body()};
    List<Full.D> dsElems=L(sFor.ds(),xIts,xIndexs,(c,di,xi,x1i)->{//DX1 = x1.methName('elem',mdf?1)(x'1) .. DXn = xn.methName('elem',mdf?n)(x'n)
      var v=di._varTx();
      X _x=null;
      Mdf _mdfi=_computeMdfi(di);
      if(v!=null){_x=v._x();}
      S s=NameMangling.methName("elem",_mdfi);
      c.add(di.with_e(binMeth(p,xi,x1i,s)));
      if(_x!=null && v.isVar()){e[0]=replaceOnUpdate(e[0],_mdfi,_x,xi,x1i);}
      });
    List<Full.D> dsSuccs=L(xIndexs,(c,xi)->{//x'1 := x'1.succ() .. xn := x'n.succ())
      var xie=new Core.EX(p,xi);
      var call=new Full.Call(p,xie,succS,false,Par.emptys);
      var op=new Full.OpUpdate(p,xi,Op.ColonEqual,call);
      c.add(makeDec(op));
      });
    Stream<Full.D> allWhileDecs=Stream.concat(
      dsElems.stream(),Stream.concat(Stream.of(makeDec(e[0])),
      dsSuccs.stream()));
    Full.E ifThen=makeBlock(p,L(allWhileDecs),ev);
    Full.E ifElse=makeBlock(p,dsCloses,new Full.Throw(p, ThrowKind.Exception,ev));
    Full.If ifThenElse=new Full.If(p, cond,L(),ifThen,ifElse);
    Full.K  k=new Full.K(ThrowKind.Exception,P.fullVoid,null, ev);
    Full.D  loopDec=makeDec(new Full.Loop(p, ifThenElse));
    Full.Block loopBlock=new Full.Block(p,false,L(loopDec),1,L(k),L(),null);
    Stream<Full.D> allTopDecs=Stream.concat(dsIts.stream(),dsStartIndexs.stream());
    var res= makeBlock(p,L(allTopDecs),loopBlock);
    return res;
    }
  private E replaceOnUpdate(Full.E e,Mdf _mdf, X x2, X x, X x1) {
    S s=NameMangling.methName("update",_mdf);
    return new CloneVisitor(){
      public @Override Full.OpUpdate visitOpUpdate(Full.OpUpdate o){
        o=super.visitOpUpdate(o);
        if(!o.x().equals(x2)){return o;}
        Full.Par pars=new Full.Par(new Core.EX(o.pos(), x1), valX,L(o.e()));
        Full.E e2=new Full.Call(o.pos(),new Core.EX(o.pos(), x),s,false,L(pars));
        return new Full.OpUpdate(e2.pos(),x2, o.op(), e2);
        }
      }.visitE(e);
    }
  private static final S ifS=S.parse("#if()");
  private static final S checkTrueS=S.parse("#checkTrue()");
  private static final S iteratorS=S.parse("#iterator()");
  private static final S varIteratorS=S.parse("#varIterator()");
  private static final S closeS=S.parse("#close()");
  private static final S succS=S.parse("#succ()");
  private static final S incompleteS=S.parse("#incomplete()");
  private static final S hasElemS=S.parse("#hasElem()");
  private static final S startIndexS=S.parse("#startIndex()");
  //private static final S applyThat=S.parse("#apply(that)");
  private static final S applyZero=S.parse("#apply()");
  private static final S stringLiteralBuilder=S.parse("#stringLiteralBuilder()");
  private static final S baseSquareBuilder=S.parse("#squareBuilder()");
  private static final S squareBuilder(S m){
    var n="";
    if(!m.m().equals("#apply")){n="#"+m.m();}
    return baseSquareBuilder.withM(baseSquareBuilder.m()+n);
    }
  private static final S shortCircutSquare=S.parse("#shortCircutSquare()");
  private static final S squareAddS=S.parse("#squareAdd()");
  private static final S stringAddAllS=S.parse("#stringAddAll()");
  private static final S stringAddExprS=S.parse("#stringAddExpr()");
  private static final S fromS=S.parse("#from()");
  private static final C classOperators=new C("ClassOperators",-1);
  private static final List<X> squareBuilderX=L(new X("squareBuilder"));
  private static final List<X> stringLiteralX=L(new X("stringLiteral"));
  private static final List<X> valX=L(new X("val"));
  private static final Full.VarTx immVoid_=new Full.VarTx(false,new Full.T(Mdf.Immutable,L(),L(),P.pVoid),null,null);
 
}
