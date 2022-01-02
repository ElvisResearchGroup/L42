package is.L42.constraints;

import static is.L42.tools.General.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.NameMangling;
import is.L42.flyweight.*;
import is.L42.generated.*;
import is.L42.generated.Core.T;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.InductiveSet;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.FV;
import is.L42.visitors.UndefinedCollectorVisitor;


public class InferToCore extends UndefinedCollectorVisitor{
  I i;
  final InductiveSet<ST, ST> sets;
  Core.E res; 
  final CTz ctzFrom;
  public InferToCore(I i,CTz ctz){
    this.i=i;
    this.sets = ctz.allSTz(i.p());
    this.ctzFrom=ctz;
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
  private void mapMulti(ST st,Consumer<T> c){
    if(st instanceof T t) { c.accept(t); }
    if(st instanceof ST.STHalfT ht) {mapMulti(ht,c);}
    }
  private void mapMulti(ST.STHalfT ht,Consumer<T> c){
    if (ht.stz().size()!=1){ return; }
    var left=ht.stz().get(0);
    if(!(left instanceof T t)){ return; }
    if(ht._mdf()!=null) { t=t.withMdf(ht._mdf()); }
    c.accept(t);
    }
  private T infer(List<ST> stz, List<Pos> poss) {
    if(stz.size()==1 && stz.get(0) instanceof T){return (T)stz.get(0);}
    List<T> tzErr=new ArrayList<>();
    List<T> ts=L(stz,(c,sti)->{
      var stzi=sets.compute(sti);
      var tz=stzi.stream().<T>mapMulti(this::mapMulti).toList();
      var speci=TypeManipulation._chooseSpecificT(i.p(),tz, poss);
      if(speci!=null){c.add(speci);}
      else{tzErr.addAll(tz);}
      });
    T res=i.p()._chooseGeneralT(ts);
    if(res!=null){return res;}
    if(!ts.isEmpty()){
      throw new EndError.InferenceFailure(poss, ErrMsg.noCommonSupertypeAmong(i.p().solve(stz),ts));
      }
    if(tzErr.isEmpty()){
      var st=i.p().solve(stz);
      String hints="";
      for(var sti:st){
        if(!(sti instanceof ST.STMeth meth)){continue;}
        if(!(meth.st() instanceof Core.T)){continue;}
        var t=(Core.T)meth.st();
        var l=i.p()._ofCore(t.p());
        if(l==null){
          hints+="\nThe class "+t.p()+" does not exists";          
          continue;
          }
        hints+="\nThe available methods for "+t.p()+" are \n"
          +ErrMsg.options(meth.s(), l.mwts())+"\n";
        }
      throw new EndError.InferenceFailure(poss, ErrMsg.inferenceFailNoInfoAbout(st,hints));
      }
    throw new EndError.InferenceFailure(poss, ErrMsg.contraddictoryInfoAbout(i.p().solve(stz),tzErr));
    }  
  @Override public void visitEX(Core.EX x){commit(x);}
  @Override public void visitEVoid(Core.EVoid eVoid){commit(eVoid);}
  @Override public void visitL(Full.L l){throw bug();}
  @Override public void visitL(CoreL l){commit(l);}
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
    List<T> ts=L(xps,(c,xpi)->c.add(i.g().of(xpi)));
    for(var t:ts){
      if(i.p()._ofCore(t.p())!=null){continue;}
      throw new EndError.PathNotExistent(binOp.poss(),i.p(), t.p().toNCs());
      }
    var res=i.p()._opOptions(binOp.op(), ts);
    assert res!=null;//null if some involved path does not exists
    if(res.size()!=1){
      var list=L(res,(c,psi)->c.add(psi.p()+"."+psi.s()));
      String err=ErrMsg.operatorNotFound(binOp.op().inner+" ("+NameMangling.methName(binOp.op(),0)+")",list);
      throw new EndError.InferenceFailure(binOp.poss(),err);
      }    
    Psi psi=res.get(0);
    var receiver=xps.get(psi.i());
    List<Core.E> args=new ArrayList<>(xps);
    args.remove(psi.i());
    args=LL(args);
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
    I oldI=i;
    List<X> fv = L(c->{
      for(var d:block.ds()){c.addAll(FV.of(d.e().visitable()));}
      for(var k:block.ks()){c.addAll(FV.of(k.e().visitable()));}
      c.addAll(FV.of(block.e().visitable()));
      });
    List<Core.K> ks=L(block.ks(),(c,ki)->c.add(auxK(ki)));//before ds, so they see the smaller I
    List<Core.D> ds=auxDs(fv,block.ds(),block.poss());
    //i=i.withG(i.g().plusEq(ds)); unnneded, it already happens in ds
    Core.E e=compute(block.e());
    i=oldI;
    commit(new Core.Block(block.pos(), ds, ks, e));
    }
  private Core.K auxK(Half.K k) {
    I oldI=i;
    Core.T t=infer(k.stz(),k.e().poss());
    if(t.p().isNCs()) {
      var ex=new Core.EX(k.e().pos(),k.x());
      var pt=i.p()._navigate(t.p().toNCs());
      if(pt!=null){Resources.inferenceHandler().ex(ex,pt);}
      }
    i=i.withG(i.g().plusEq(k.x(),t));
    Core.E e=compute(k.e());
    i=oldI;
    assert t.mdf().isImm() || k.thr()==ThrowKind.Return;
    return new Core.K(k.thr(),t,k.x(),e);
    }
  private boolean xIsRead(X x,Core.D d){
    return d.x().equals(x) && d.t().mdf().isRead();
    }
  private List<Core.D> auxDs(List<X> fv, List<Half.D> ds0,List<Pos>poss) {
    //was recursive. For very ds, coming from long strings, could go in stack overflow.
    //Refactored to be iterative
    return L(ds0.stream().map(di->ds1(fv,di,poss)));
  }
  private Core.D ds1(List<X> fv, Half.D d,List<Pos>poss) {
    Core.T t=infer(d._mdf(),d.stz(),d.e().poss());//not the final t
    Core.E e1=compute(d.e());
    var fvE=FV.of(e1.visitable());
    Core.T t1=t;
    boolean noDMdf=d._mdf()==null;
    boolean readBlock=false;
    if (e1 instanceof Core.Block b1){
      if (b1.e() instanceof Core.EX x1) {
        readBlock=b1.ds().stream().anyMatch(di->xIsRead(x1.x(),di));
        }
      }
    boolean toImm=noDMdf && t.mdf().isRead() && !readBlock && 
      fvE.stream().noneMatch(xi->{
        var ti=i.g()._of(xi);
        return ti!=null && ti.mdf().isIn(Mdf.Readable,Mdf.Lent,Mdf.Mutable);
        });
    boolean toMut=!toImm && noDMdf && t.mdf().isCapsule() && 
      fv.stream().filter(xi->xi.equals(d.x())).count()>=2;
    if(toImm){t1=t.withMdf(Mdf.Immutable);}
    else if(toMut){t1=t.withMdf(Mdf.Mutable);}
    i=i.withG(i.g().plusEq(d.x(), t1));
    if(t1.p().isNCs()) {
      var ex=new Core.EX(d.e().pos(),d.x());
      var pt1=i.p()._navigate(t1.p().toNCs());
      if(pt1!=null){Resources.inferenceHandler().ex(ex, pt1);}
      }
    return new Core.D(d.isVar(),t1,d.x(),e1); 
    }
  @Override public void visitD(Half.D d){uc();}
  @Override public void visitK(Half.K k){uc();}

  @Override public void visitSTMeth(ST.STMeth stMeth){uc();}
  @Override public void visitSTOp(ST.STOp stOp){uc();}


}
