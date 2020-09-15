package is.L42.sifo;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;
import static is.L42.generated.Mdf.*;
import static is.L42.generated.ThrowKind.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.L42.generated.Core.*;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.typeSystem.AlternativeMethodTypes;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.FV;
import is.L42.visitors.PropagatorCollectorVisitor;
import is.L42.visitors.UndefinedCollectorVisitor;

public class SifoTypeSystem extends UndefinedCollectorVisitor{
  public SifoTypeSystem(Program p, G g,List<T> exceptions, Set<Mdf> mdfs, T expected,Lattice42 lattice) {
    this.p = p;
    this.g = g;
    this.mdfs=mdfs;
    this.expected=expected;
    this.lattice=lattice;
    this.sifoExceptions=L(exceptions,(c,t)->{
      var sifoP=getSifoAnn(t.docs());
      if(sifoP.isNCs()){c.add(sifoP.toNCs());}
      });
    }
  boolean isDeep;
  int dept=0;
  Program p;
  G g;
  List<P.NCs> sifoExceptions;
  List<P> sifoReturns=L();
  Set<Mdf> mdfs;
  T expected;
  Lattice42 lattice;
  void visitExpecting(E e,T newExpected){
    T oldE=expected;
    expected=newExpected;
    visitE(e);
    expected=oldE;
    }
  void errIf(boolean cond,E e,String msg){
    if(cond){
    throw new EndError.TypeError(e.poss(),msg);}
    }
  void mustSubMdf(Mdf m1,Mdf m2,List<Pos> poss){//TODO: check if it was needed
    if(!Program.isSubtype(m1, m2)){
      throw new EndError.TypeError(poss,Err.subTypeExpected(m1,m2));
      }
    }  
  @Override public void visitEVoid(EVoid e){}
  @Override public void visitPCastT(PCastT e){}
  @Override public void visitL(L e){}
  private P getSifoAnn(List<Doc>docs){
    List<P.NCs> paths=sifos(expected.docs());
    if(paths.isEmpty()) {return lattice.getBottom();}
    if(paths.size()!=1){throw bug();}//TODO: good error
    return paths.get(0);
    }
  private List<P.NCs> sifos(List<Doc> docs){
    return L(docs,(c,d)->{
      PathSel ps=d._pathSel();
      if(ps==null){return;}
      var p=_asSifo(ps.p());
      if(p!=null){c.add(p);}
      });    
    }
  private P.NCs _asSifo(P path){
    if(!path.isNCs()){return null;}
    P.NCs ncs=path.toNCs();
    if(ncs.n()!=dept){return null;}
    ncs=ncs.withN(0);
    if(!lattice.getAllLevels().contains(ncs)){return null;}
    return ncs;
    }
  @Override public void visitEX(EX e){
    T t=this.g._of(e.x());
    assert t!=null;
    P actualPath= getSifoAnn(t.docs());
    P expectedPath= getSifoAnn(expected.docs());
    mustSubSecurity(t.mdf(),actualPath, expected.mdf(),expectedPath,e.poss());
    }
  private T tWithSec(P sec){
    if(!sec.isNCs()){return P.coreVoid;}
    var s=sec.toNCs().withN(dept);
    var d=new Doc(new PathSel(s,null,null),L(),L());
    return P.coreVoid.withDocs(L(d));
    }
  private void mustSubSecurity(Mdf subMdf,P sub,Mdf supMdf,P sup,List<Pos>pos){
    if(sub.equals(sup)){return;}
    var mdfOk=subMdf.isIn(Mdf.Immutable, Mdf.Capsule);
    if(mdfOk && lattice.secondHigherThanFirst(sub,sup)){return;}
    throw bug();//TODO: good error
    }  
  @Override public void visitLoop(Loop e){
    visitExpecting(e.e(),P.coreVoid);
    }
  @Override public void visitThrow(Throw e){
    if(e.thr()==Error){
      visitExpecting(e.e(),P.coreVoid);return;
      }
    if(e.thr()==Exception){
      var lub=lattice.leastUpperBoundOrLow(this.sifoExceptions);
      visitExpecting(e.e(),tWithSec(lub));
      return;
      }
    assert e.thr()==Return;
    //Ts2.mdfs subsetEq {imm,capsule}
    boolean ret1=List.of(Mdf.Capsule,Mdf.Immutable).containsAll(mdfs); 
    if(ret1){
      var lub=lattice.leastUpperBoundOrLow(this.sifoReturns);
      visitExpecting(e.e(),tWithSec(lub));
      return;
      }
    if(sifoReturns.stream().distinct().count()!=1) {throw todo();}//TODO:good error here
    var s=sifoReturns.get(0);
    var general=TypeManipulation._mostGeneralMdf(mdfs);
    assert general!=null;
    visitExpecting(e.e(),tWithSec(s).withMdf(general));
    }
  @Override public void visitMCall(MCall e){
    var s=getSifoAnn(expected.docs());
    var selectedS=lattice.getBottom();
    if (!s.equals(lattice.getBottom())){
      boolean promotable=expected.mdf().isIn(Mdf.Immutable,Mdf.Capsule);
      if(promotable) {throw todo();}//TODO: Tobias
      //implements a latitce.between(s,s1)=ss so that s,s1 in ss and all in between
      selectedS=s;
      }
    P.NCs p0=TypeManipulation.guess(g,e.xP()).toNCs();
    var mh=_elem(p._ofCore(p0).mwts(),e.s()).mh();
    List<Core.T>parTypes=p.from(mh.parsWithThis(), p0);
    //Core.T retType=p.from(mh.t(),p0);
    //var retSec=getSifoAnn(retType.docs());
    //retSec=lattice.leastUpperBound(List.of(retSec,selectedS));
    var meths=AlternativeMethodTypes.types(p,p0.toNCs(),e.s());
    meths=L(meths.stream().filter(m->Program.isSubtype(m.mdf(),expected.mdf())));
    assert !meths.isEmpty();
    List<E> es=L(c->{c.add(e.xP());c.addAll(e.es());});//the receiver and the arguments
    assert es.size()==parTypes.size();
    var oldExpected=expected;
    var oldG=g;
    var oldMdfs=mdfs;
    var oldSifoExceptions=sifoExceptions;
    var oldSifoReturns=sifoReturns;
    EndError.TypeError lastErr=null;
    for(var m:meths){
      try{
        g=oldG;
        mdfs=oldMdfs;
        sifoExceptions=oldSifoExceptions;
        sifoReturns=oldSifoReturns;
        for(int i:range(es)){
          var pi=parTypes.get(i);
          var sec=lattice.leastUpperBound(List.of(getSifoAnn(pi.docs()),selectedS));//TODO: may want to write the meth taking 2 only          
          expected=tWithSec(sec).withMdf(m.mdfs().get(i)).withP(pi.p());
          visitE(es.get(i));
          }
        expected=oldExpected;
        return;     
        }
      catch(EndError.TypeError toSave){lastErr=toSave;}
      }
    throw lastErr;//TODO: better error?
    }
  @Override public void visitOpUpdate(OpUpdate e){
    //mustSubMdf(Immutable,expected,e.poss());
    T t=g.of(e.x());
    //visitExpecting(e.e(),t.mdf());
    assert g.isVar(e.x());//TODO: where is varriness of G used?
    }
  @Override public void visitBlock(Block e){
    //var hope=expected.isIn(Capsule,Immutable,ImmutableFwd,ImmutablePFwd);
    //if(e.ds().isEmpty()){hope=false;}
    //if(!hope){visitBlockDirect(e);return;}
    var oldG=g;
    var oldExpected=expected;
    var oldMdfs=mdfs;
    try{visitBlockDirect(e);}
    catch(EndError.TypeError te){
      var lentG=oldG.toLent();
      e.accept(new PropagatorCollectorVisitor(){
        @Override public void visitL(Core.L l){}
        @Override public void visitEX(EX x){
          if(oldG._of(x.x())==null){return;}
          if(lentG._of(x.x())==null){throw te;}
          }
        @Override public void visitOpUpdate(Core.OpUpdate u){
          if(oldG._of(u.x())==null){return;}
          if(!lentG.isVar(u.x())){throw te;}
          super.visitOpUpdate(u);
          }
        });
      g=lentG;
      //expected=Mutable;
      mdfs=oldMdfs;
      try{visitBlockDirect(e);}
      catch(EndError.TypeError te2){throw te;}
      finally{g=oldG;expected=oldExpected;}      
      }
    }
  private void visitBlockDirect(Block e){
    var mdfs2=new HashSet<Mdf>(mdfs);
    for(K k:e.ks()){typeK(k,mdfs2);}
    G g1=g;
    if(e.ks().stream().anyMatch(k->k.thr()==Error)){g1=g.toRead();}
    var oldMdfs=mdfs;
    mdfs=mdfs2;
    G oldG=g;
    G g0=typeDs(g1,e.ds(),mdfs2);
    g=oldG.plusEqMdf(g0);
    mdfs=oldMdfs;
    visitE(e.e());
    g=oldG;
    }
  private G typeDs(G g0,List<D>allDs, HashSet<Mdf> mdfs) {
    if(allDs.isEmpty()){return g0;}
    var fvs=new ArrayList<X>();
    int split=splitDs(allDs,fvs);
    var txe=allDs.subList(0, split+1);
    var restDs=allDs.subList(split+1,allDs.size());
    G g1=g0.plusEqFwdOnlyMutOrImm(txe);
    for(var d:txe){
      g=g1;
      var mdf=TypeManipulation.fwdPOf(d.t().mdf());
      //visitExpecting(d.e(),mdf);
      }
    if(TypeManipulation.fwd_or_fwdP_inMdfs(mdfs)){
      List<D> errs=L(txe.stream().filter(di->fvs.contains(di.x())));
      if(!errs.isEmpty()){//TODO: errIf may not be the best abstraction...
        errIf(true,errs.get(0).e(),Err.mayLeakUnresolvedFwd(errs.get(0).x()));
        }
      assert txe.size()==1;//TODO: if this is true we can optimize above, avoiding streams
      }
    G g2;
    boolean fwdInFreeMdfs=fvs.stream().anyMatch(xi->{
      var ti=g0._of(xi);
      if(ti==null){return false;}
      return TypeManipulation.fwd_or_fwdP_in(ti.mdf());
      });
    if(fwdInFreeMdfs){g2=g0.plusEqFwdP(txe);}
    else{g2=g0.plusEq(txe);}
    return typeDs(g2,restDs,mdfs); 
    }
  private int splitDs(List<D> ds,ArrayList<X> xs){//cut will be from 0 to i included
    if (ds.isEmpty()){return 0;}
    int n=ds.size()-1;
    for(int i=0;i<n;i+=1){//i+1 always available in ds
      xs.addAll(FV.of(ds.get(i).e().visitable()));
      if(xsNotInDomi(xs,ds,i+1)){return i;}
      }
    return n;
    }
  private boolean xsNotInDomi(List<X> xs,List<D> ds,int ip1){
    for(int i=ip1;i<ds.size();i+=1){
      if(xs.contains(ds.get(i).x())){return false;}
      }
    return true;
    }
  private void typeK(K k, HashSet<Mdf> mdfs1) {
    var t=k.t();
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;
    if(k.thr()==ThrowKind.Return){mdfs1.add(t.mdf());}
    }
  }