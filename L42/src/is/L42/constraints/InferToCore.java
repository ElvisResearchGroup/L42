package is.L42.constraints;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.typeFilter;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.PCastT;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.I;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.Psi;
import is.L42.generated.ST;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.tools.InductiveSet;
import is.L42.top.Top;
import is.L42.visitors.FV;
import is.L42.visitors.UndefinedCollectorVisitor;
import lombok.NonNull;
import lombok.var;

public class InferToCore extends UndefinedCollectorVisitor{
  I i;
  final InductiveSet<ST, ST> sets;
  Core.E res; 
  final CTz ctzFrom;
  final Top top;
  public InferToCore(I i,CTz ctz,Top top){
    this.i=i; this.top=top;
    this.sets = ctz.allSTz(i.p());
    this.ctzFrom=ctz;//frommed=CTz[from This1;p]TODO:
    } 
  public final Core.E compute(Half.E e){
    assert res==null;
    e.visitable().accept(this);
    assert res!=null;
    Core.E aux=res;
    res=null;
    return aux;
    }
  public final void commit(Core.E e){res=e;}
  private T infer(Mdf _mdf, List<ST> stz,List<Pos> poss) {
    T res=infer(stz,poss);
    if(_mdf!=null){res=res.withMdf(_mdf);}
    return res;
    }
  private T infer(List<ST> stz, List<Pos> poss) {
    if(stz.size()==1 && stz.get(0) instanceof T){return (T)stz.get(0);}
    List<T> tzErr=new ArrayList<>();
    List<T> ts=L(stz,(c,sti)->{
      var stzi=sets.compute(sti);
      var tz=typeFilter(stzi.stream(),T.class);
      var speci=i.p()._chooseSpecificT(tz, poss);
      if(speci!=null){c.add(speci);}
      else{tzErr.addAll(tz);}
      });
    T res=i.p()._chooseGeneralT(ts,poss);
    if(res!=null){return res;}
    if(!ts.isEmpty()){
      throw new EndError.InferenceFailure(poss, Err.noCommonSupertypeAmong(stz,ts));
      }
    if(tzErr.isEmpty()){
      throw new EndError.InferenceFailure(poss, Err.inferenceFailNoInfoAbout(stz));
      }
    throw new EndError.InferenceFailure(poss, Err.contraddictoryInfoAbout(stz,tzErr));
    }  
  @Override public void visitEX(Core.EX x){commit(x);}
  @Override public void visitEVoid(Core.EVoid eVoid){commit(eVoid);}
  @Override public void visitL(Full.L l){
    Program p=i.p().push(i._c(),l);
    //CTz ctzFrom=ctz;//TODO: from!!!
    Program pr=top.top(ctzFrom,p);//propagate errors
    //ctz=ctzFrom;//TODO: from!!
    commit(pr.topCore());
    }
  @Override public void visitL(Core.L l){commit(l);}
  @Override public void visitPCastT(Half.PCastT pCastT){
    commit(new Core.PCastT(pCastT.pos(), pCastT.p(),infer(Mdf.Class,pCastT.stz(),pCastT.poss())));
    }
  @Override public void visitSlashCastT(Half.SlashCastT slash){
    commit(new Core.PCastT(slash.pos(),infer(slash.stz(),slash.poss()).p(),infer(Mdf.Class,slash.stz1(),slash.poss())));
    }
  @Override public void visitMCall(Half.MCall mCall){
    Core.E e0=compute(mCall.xP());
    List<Core.E> es=L(mCall.es(), (c,ei)->c.add(compute(ei)));
    commit(new Core.MCall(mCall.pos(), (Core.XP)e0, mCall.s(), es));
    }

  @Override public void visitBinOp(Half.BinOp binOp){
    List<Core.XP> xps=L(binOp.es(),(c,x)->c.add((Core.XP)compute(x)));
    List<T> ts=L(xps,(c,xpi)->{
      if (xpi instanceof Core.EX){
        var x=(Core.EX)xpi;
        c.add(i.g().of(x.x()));
        return;
        }
       var pct=(Core.PCastT) xpi;
       c.add(pct.t());
      });
    var res=CTz.opOptions(i.p(), binOp.op(), ts);
    //TODO: can we make a test where the former call throw some exception?
    if(res.size()!=1){
      var list=L(res,(c,psi)->c.add(psi.p()+"."+psi.s()));
      String err=Err.operatorNotFound(binOp.op(),list);
      throw new EndError.InferenceFailure(binOp.poss(),err);
      }    
    Psi psi=res.get(0);
    var receiver=xps.get(psi.i());
    List<Core.E> args=new ArrayList<>(xps);
    args.remove(psi.i());
    args=L(args.stream());
    commit(new Core.MCall(binOp.pos(),receiver,psi.s(),args));
    }
  @Override public void visitThrow(Half.Throw thr){
    commit(new Core.Throw(thr.pos(),thr.thr(),compute(thr.e())));
    }
  @Override public void visitLoop(Half.Loop loop){
    commit(new Core.Loop(loop.pos(),compute(loop.e())));
    }
  @Override public void visitOpUpdate(Half.OpUpdate op){
    commit(new Core.OpUpdate(op.pos(),op.x(),compute(op.e())));
    }


  @Override public void visitBlock(Half.Block block){
    List<X> fv = L(c->{
      for(var d:block.ds()){c.addAll(FV.of(d.e().visitable()));}
      for(var k:block.ks()){c.addAll(FV.of(k.e().visitable()));}
      c.addAll(FV.of(block.e().visitable()));
      });
    List<Core.K> ks=L(block.ks(),(c,ki)->c.add(auxK(ki)));//before ds, so they see the smaller I
    List<Core.D> ds=auxDs(fv,block.ds(),block.poss());
    //i=i.withG(i.g().plusEq(ds)); unnneded, it already happens in ds
    Core.E e=compute(block.e());
    commit(new Core.Block(block.pos(), ds, ks, e));
    }
  private Core.K auxK(Half.K k) {
    I oldI=i;
    Core.T t=infer(k.stz(),k.e().poss());
    i=i.withG(i.g().plusEq(k.x(),t));
    Core.E e=compute(k.e());
    i=oldI;
    assert t.mdf().isImm() || k.thr()==ThrowKind.Return;
    return new Core.K(k.thr(),t,k.x(),e);
    }
  private List<Core.D> auxDs(List<X> fv, List<Half.D> ds0,List<Pos>poss) {
    if(ds0.isEmpty()){return L();}
    Half.D d=ds0.get(0);
    List<Half.D> ds=popL(ds0);
    Core.T t=infer(d._mdf(),d.stz(),poss);//not the final t
    Core.E e1=compute(d.e());
    var fvE=FV.of(e1.visitable());
    Core.T t1=t;
    boolean toImm=d._mdf()==null && t.mdf().isRead() && 
      fvE.stream().noneMatch(xi->{
        var ti=i.g()._of(xi);
        return ti!=null && ti.mdf().isIn(Mdf.Readable,Mdf.Lent,Mdf.Mutable);
        });
    boolean toMut=!toImm && d._mdf()==null && t.mdf().isCapsule() && 
      fv.stream().filter(xi->xi.equals(d.x())).count()>=2;
    if(toImm){t1=t.withMdf(Mdf.Immutable);}
    else if(toMut){t1=t.withMdf(Mdf.Mutable);}
    i=i.withG(i.g().plusEq(d.x(), t1));
    var recursive=auxDs(fv,ds,poss);
    return pushL(new Core.D(d.isVar(),t1,d.x(),e1),recursive); 
    }
  @Override public void visitD(Half.D d){uc();}
  @Override public void visitK(Half.K k){uc();}

  @Override public void visitSTMeth(ST.STMeth stMeth){uc();}
  @Override public void visitSTOp(ST.STOp stOp){uc();}


}
