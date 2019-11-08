package is.L42.constraints;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.NameMangling;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.Op.OpKind;
import is.L42.typeSystem.TypeManipulation;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.generated.Full.Call;
import is.L42.generated.Full.Cast;
import is.L42.generated.Full.If;
import is.L42.generated.Full.Par;
import is.L42.visitors.UndefinedCollectorVisitor;
import is.L42.visitors.Visitable;

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
  @Override public void visitSlashX(Full.SlashX slashX){
    var xp=y._onSlashX();
    assert xp!=null;
    var s=new S(slashX.x().inner(),L(),-1);
    var h=new Half.MCall(slashX.pos(),xp,s,L());
    visitMCall(h);
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
    var s=new Full.EString(p.pos(),L(receiver),L(content));
    visitEString(s);
    }
  @Override public void visitUOp(Full.UOp u){
    if(u._op()==null){
      assert u._num()!=null;
      var s=new Full.EString(u.pos(),L(u.e()),L(u._num()));
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
      return par;
      });
    }
  @Override public void visitCall(Full.Call call){
    if(call._s()==null){call=call.with_s(NameMangling.hashApply());}
    if(call.isSquare()){call=expandSquare(call);}
    var pars=addThats(call.pars());
    assert !pars.isEmpty();
    Full.Par par=pars.get(0);
    if(!isFullXP(call.e())){
      X rec=freshX("receiver");
      Full.D d=makeDec(null,rec,call.e());
      Full.E c=call.withE(new Core.EX(call.pos(),rec));
      visitBlock(makeBlock(call.pos(),L(d),c));
      return;
      }
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
      ctz.plusAcc(y.p(), resi.resSTz, stz1i); 
      es.add(resi.e);
      retST.addAll(resi.retSTz);    
      }    
    y=oldY;
    commit(new Half.MCall(call.pos(), (Half.XP)resXP.e, s,L(es.stream())),stz1,L(retST.stream().distinct()));
    }
  @Override public void visitBlock(Full.Block block){
    if(block.isCurly()){curlyBlock(block);return;}
    if(block._e()==null){block=block.with_e(new Core.EVoid(block.pos()));}
    if(block.dsAfter()!=block.ds().size()){block=splitBlock(block);}
    if(!block.whoopsed().isEmpty()){block=expandWhoopses(block);}
    Y oldY=y;
    y=y.withG(y.g().plusEq(block.ds()));
    ArrayList<Half.D> ds=new ArrayList<>();
    ArrayList<Half.K> ks=new ArrayList<>();
    ArrayList<ST> resST=new ArrayList<>();
    ArrayList<ST> retST=new ArrayList<>();
    for(Full.D d:block.ds()){
      var res=auxD(d);
      ds.addAll(res.e);
      retST.addAll(res.retSTz); //resST is empty
      for(var dRes:res.e){
        y=y.withG(y.g().plusEqOver(dRes.x(),dRes.stz()));
        }
      }
    for(Full.K k:block.ks()){
      var res=auxK(k);
      ks.add(res.e);
      resST.addAll(res.resSTz);
      retST.addAll(res.retSTz);
      }
    var res=compute(block._e());
    resST.addAll(res.resSTz);
    retST.addAll(res.retSTz);
    commit(new Half.Block(block.pos(),L(ds.stream()),L(ks.stream()),res.e),resST,retST);
    y=oldY;
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
    var hd=new Half.D(d._varTx().isVar(),d._varTx()._mdf(), t, d._varTx()._x(),res.e);
    y=oldY;
    return new Res<>(L(hd),L(),res.retSTz);
    }
  private Res<List<Half.D>> auxMultiD(Full.D d){
    X x=freshX("DecMatch");
    Core.EX ex=new Core.EX(d._e().pos(),x);
    d.varTxs();
    Full.D step0=d.withVarTxs(L()).with_varTx(d._varTx().with_x(x));
    List<ST>retST=new ArrayList<>();
    List<Half.D> ds=new ArrayList<>();
    var res=auxD(step0);
    ds.addAll(res.e);
    retST.addAll(res.retSTz);
    Y oldY=y;
    y=y.withG(y.g().plusEq(x,res.e.get(0).stz()));
    for(var vtx:d.varTxs()){
      S s=NameMangling.methNameTrim(vtx._x());
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
    var squareB=new Full.Call(p,new Full.Slash(p), squareBuilder,false,Par.emptys);
    var decX=makeDec(null,x,squareB);
    List<Full.D> e1n=L(s.pars(),(c,pi)->{
      if(pi.es().isEmpty() && pi._that()!=null){
        var e1i=pi._that();
        if(e1i instanceof Full.If 
         ||e1i instanceof Full.While
         ||e1i instanceof Full.For
         ||e1i instanceof Full.Loop
         ||e1i instanceof Full.Block){
          c.add(makeDec(new Full.Call(p,ex,yieldS,false,L(new Par(e1i,L(),L())))));
          return;
          }
        }
      c.add(makeDec(new Full.Call(p,ex,addS,false,L(pi))));
      });
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
    if(binOp.es().size()!=2){binOp=applyAssociativity(binOp);}
    if(binOp.op().kind==OpKind.BoolOp){visitBinOp3(binOp);return;}
    if(binOp.es().get(0) instanceof Full.CsP){
      visitBinOpCsp(binOp);return;}
    List<Full.E> xps=new ArrayList<>();
    List<Full.D> ds=L(binOp.es(),(c,ei)->{
      if(isFullXP(ei)){xps.add(ei);return;}
      X xi=freshX("op");
      xps.add(new Core.EX(ei.pos(),xi));
      c.add(makeDec(null, xi, ei));
      });
    if(!ds.isEmpty()){
      visitBlock(makeBlock(binOp.pos(), ds, binOp.withEs(L(xps.stream()))));
      return;
      }
    ArrayList<ST> resSTz=new ArrayList<>();
    ArrayList<ST> retSTz=new ArrayList<>();
    List<Half.XP> es=L(binOp.es(),(c,ei)->{
      var ri=compute(ei);
      c.add((Half.XP)ri.e);
      resSTz.addAll(ri.resSTz);
      retSTz.addAll(ri.retSTz);
      });
    commit(new Half.BinOp(binOp.pos(),binOp.op(), es), resSTz, retSTz);
    }
  private Full.BinOp applyAssociativity(Full.BinOp b){
    int s=b.es().size();
    assert s>2;
    if(b.op().kind==OpKind.RelationalOp){return b;}
    if(b.op().kind==OpKind.DataLeftOp){
      var left=new Full.BinOp(b.pos(),b.op(),b.es().subList(0, 2));
      return b.withEs(pushL(left,b.es().subList(2,s)));
      }
    var right=new Full.BinOp(b.pos(),b.op(),b.es().subList(s-2, s));
    return b.withEs(pushL(b.es().subList(0,s-2),right)); 
    }
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
    S sc=NameMangling.shortCircuit(b.op());
    S sr=NameMangling.shortResult(b.op());
    S sp=NameMangling.shortProcess(b.op());
    var scCall=new Full.Call(p,e0,sc,false,Par.emptys);
    var srCall=new Full.Call(p,e0,sr,false,Par.emptys);
    X other=new X("other");
    Par par=new Par(ex,L(other),L(e1));
    var spCall=new Full.Call(p,e0,sp,false,L(par));
    var dx=makeDec(null,x,scCall);
    var ifElse=new Full.If(p, ex,L(), srCall,spCall);
    visitBlock(makeBlock(p, L(dx),ifElse));
    }
  private void visitBinOpCsp(Full.BinOp b){
    assert b.es().get(0) instanceof Full.CsP;
    var csp=(Full.CsP)b.es().get(0);
    var e=b.es().get(1);
    assert b.es().size()==2;
    assert csp.cs().isEmpty();
    assert csp._p().isNCs();//should be a well formedness error
    C c=new C("$"+NameMangling.methName(b.op(),0).substring(1),-1);
    var p=csp._p().toNCs();
    p=p.withCs(pushL(p.cs(),c));
    var pct=new Half.PCastT(b.pos(), p, L(new Core.T(Mdf.Class,L(),p)));
    List<ST> stz0= L(new ST.STMeth(pct.stz().get(0),applyThat,-1));
    List<ST> stz1= L(new ST.STMeth(pct.stz().get(0),applyThat,1));
    var oldExpectedT=y._expectedT();
    y=y.with_expectedT(stz1);
    var res=compute(e);
    y=y.with_expectedT(oldExpectedT);
    var e1=res.e;
    var stz2=res.resSTz;
    var stz=res.retSTz;
    ctz.plusAcc(y.p(), stz2, stz1);
    Half.MCall half=new Half.MCall(b.pos(),pct,applyThat,L(e1));
    commit(half,stz0,stz);
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
        var e1i=new Full.Call(p,ex,addAllS,false,L(new Par(bi,L(),L())));
        var e2i=new Full.Call(p,ex,spliceS,false,L(new Par(ei,L(),L())));
        c.add(makeDec(e1i));
        c.add(makeDec(e2i));
        }
      var bLast=strLitPar(p,s.strings().get(s.strings().size()-1));
      var e1Last=new Full.Call(p,ex,addAllS,false,L(new Par(bLast,L(),L())));
      c.add(makeDec(e1Last));
      });
    return makeBlock(p,e0n,ex);
    }
  @Override public void visitIf(Full.If sIf){
    Pos p=sIf.pos();
    if(sIf._else()==null){sIf=sIf.with_else(new Core.EVoid(p));}
    if(sIf._condition()==null){uc();}
    if(!isFullXP(sIf._condition())){visitBlock(ifBlock(p,sIf));return;}
    Full.E test=new Full.Call(p,sIf._condition(),ifS,false,Par.emptys);
    test=new Full.Call(p,test,checkTrueS,false,Par.emptys);
    var k=new Full.K(null,immVoid_._t(),null,sIf._else());
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
  private static final S ifS=S.parse("#if()");
  private static final S checkTrueS=S.parse("#checkTrue()");
  private static final S applyThat=S.parse("#apply(that)");
  private static final S stringLiteralBuilder=S.parse("#stringLiteralBuilder()");
  private static final S squareBuilder=S.parse("#squareBuilder()");
  private static final S shortCircutSquare=S.parse("#shortCircutSquare()");
  private static final S yieldS=S.parse("#yield()");
  private static final S addS=S.parse("#add()");
  private static final S addAllS=S.parse("#addAll()");
  private static final S spliceS=S.parse("#splice()");
  private static final S fromS=S.parse("#from()");
  private static final List<X> squareBuilderX=L(new X("squareBuilder"));
  private static final List<X> stringLiteralX=L(new X("stringLiteral"));
  private static final Full.VarTx immVoid_=new Full.VarTx(false,new Full.T(Mdf.Immutable,L(),L(),P.pVoid),null,null);

  @Override public void visitWhile(Full.While sWhile){uc();}
  @Override public void visitFor(Full.For sFor){uc();}
 
}
