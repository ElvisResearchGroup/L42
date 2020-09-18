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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public static String err1(String par1,String par2){return "";}  
  public SifoTypeSystem(Program p, G g,List<T> exceptions, Set<Mdf> mdfs, T expected,Lattice42 lattice) {
    this.p = p;
    this.g = g;
    this.mdfs=mdfs;
    this.expected=expected;
    this.lattice=lattice;
    if(exceptions.isEmpty()){return;}
    List<P> sifoExceptions=L(exceptions,(c,t)->
      c.add(getSifoAnn(t.docs())));
    if(sifoExceptions.stream().distinct().count()==1){
      this._sifoExceptions=sifoExceptions.get(0);
      return;
      }
    throw bug();//TODO: error: multiple types of sifo security exceptions
    }
  boolean isDeep;
  int dept=0;
  Program p;
  G g;
  P _sifoExceptions=null;
  P _sifoReturns=null;
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
      P.NCs top=lattice.getTop().toNCs();
      assert top.n()==0;
      top=top.withN(dept);
      visitExpecting(e.e(),this.tWithSec(top));return;
      }
    if(e.thr()==Exception){
      T expected=P.coreVoid;
      if(this._sifoExceptions!=null){expected=tWithSec(this._sifoExceptions);};
      visitExpecting(e.e(),expected);
      return;
      }
    assert e.thr()==Return;
    T expected=P.coreVoid;
    var general=TypeManipulation._mostGeneralMdf(mdfs);
    if(this._sifoReturns!=null){expected=tWithSec(this._sifoReturns);}
    visitExpecting(e.e(),expected.withMdf(general));
    }
  public void visitMCall(MCall e,P.NCs p0,List<T> parTypes,List<T>excs, P selectedS){
    var excsSifo=L(excs.stream().map(t->getSifoAnn(t.docs())).distinct());
    //if excsSifo.size()>1 it will be an error when the header is typed
    P excSifo=excsSifo.isEmpty()?null:excsSifo.get(0);
    if(excSifo!=null && !excSifo.equals(this._sifoExceptions)){
      throw bug();//can not throw excSifo
      }
    //will go in block
    //if(this._sifoExceptions!=null && excSifo!=null){
    //  if(!this._sifoExceptions.equals(excSifo)){throw bug();}//no more then one kind of sifo exception in scope
    //  }
    var meths=AlternativeMethodTypes.types(p,p0,e.s());
    meths=L(meths.stream().filter(m->Program.isSubtype(m.mdf(),expected.mdf())));
    assert !meths.isEmpty();
    List<E> es=L(c->{c.add(e.xP());c.addAll(e.es());});//the receiver and the arguments
    assert es.size()==parTypes.size();
    var oldExpected=expected;
    var oldG=g;
    var oldMdfs=mdfs;
    var oldSifoExceptions=_sifoExceptions;
    var oldSifoReturns=_sifoReturns;
    EndError.TypeError lastErr=null;
    for(var m:meths){
      try{
        g=oldG;
        mdfs=oldMdfs;
        _sifoExceptions=oldSifoExceptions;
        _sifoReturns=oldSifoReturns;
        for(int i:range(es)){
          var pi=parTypes.get(i);
          var sec=lattice.leastUpperBound(getSifoAnn(pi.docs()),selectedS);          
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
  
  @Override public void visitMCall(MCall e){
    var s=getSifoAnn(expected.docs());
    var selectedS=lattice.getBottom();
    P.NCs p0=TypeManipulation.guess(g,e.xP()).toNCs();
    var mh=_elem(p._ofCore(p0).mwts(),e.s()).mh();
    List<Core.T>parTypes=p.from(mh.parsWithThis(), p0);
    List<Core.T>excTypes=p.from(mh.exceptions(), p0);
    Core.T retType=p.from(mh.t(),p0);
    var retSec=getSifoAnn(retType.docs());
    if (!s.equals(lattice.getBottom())){
      boolean promotable=expected.mdf().isIn(Mdf.Immutable,Mdf.Capsule);
      if(promotable) {
        var s1n=lattice.levelsBetween(retSec,s);
        EndError firstErr=null;//TODO: what about order
        for(var si:s1n){
          try{visitMCall(e,p0,parTypes,excTypes,si);return;}
          catch(EndError err){if(firstErr!=null){firstErr=err;}}
          }
        assert firstErr!=null;
        throw firstErr;
        }
      selectedS=s;
      }
    visitMCall(e,p0,parTypes,excTypes,selectedS);
    }
  @Override public void visitOpUpdate(OpUpdate e){
    T t=g.of(e.x());
    visitExpecting(e.e(),t);
    }
  private boolean isTop(T t){return isTop(getSifoAnn(t.docs()));}
  private boolean isTop(P sifo){return lattice.getTop().equals(sifo);}
  @Override public void visitBlock(Block e){
    var newMdfs=new HashSet<>(mdfs);
    for(K k:e.ks()){typeK(k,newMdfs);}
    var oldMdfs=mdfs;
    mdfs=newMdfs;
    boolean hasKs=e.ks().isEmpty();
    boolean hasErr=e.ks().stream().anyMatch(k->k.thr()==ThrowKind.Error);
    if(hasKs){
      var sfv=e.ds().stream().flatMap(di->FV.of(di.e().visitable()).stream());
      List<X> fv=L(sfv.filter(x->e.ds().stream().noneMatch(d->d.x().equals(x))));
      var s=L(fv.stream()
        .filter(x->g._of(x).mdf().isIn(Mdf.Mutable, Mdf.Capsule) || g.isVar(x))
        .map(x->getSifoAnn(g._of(x).docs()))
        .distinct());
      if(!s.isEmpty()){
        if(s.size()>1){throw todo();}//more the one mut security in-out
        if(hasErr && !isTop(s.get(0))){throw todo();}//error only caught as high
        }
      var t0n=L(Stream.concat(Stream.of(expected),e.ks().stream().map(k->k.t())));
      if(hasErr){//T0..Tn: the result type+the types of catches
        if(t0n.stream().anyMatch(t->isTop(t))) {throw todo();}//error capturing errors must be high
        }
      else{
        for(T ti:t0n){
          ArrayList<P> ss=fv.stream().map(x->getSifoAnn(g._of(x).docs())).collect(Collectors.toCollection(ArrayList::new));
          var secTi=getSifoAnn(ti.docs());
          ss.add(secTi);
          var lub=lattice.leastUpperBound(ss);
          if(!lub.equals(secTi)){throw todo();}//error all catched/results need to be lub
          }
        }
      }
    G oldG=g;
    G g0=typeDs(e.ds());
    g=g.plusEqMdf(g0);
    mdfs=oldMdfs;
    visitE(e.e());
    g=oldG;
    }
  private G typeDs(List<D>allDs){
    G g1=g.plusEq(allDs);
    var oldG=g;
    g=g1;
    for(var d:allDs){visitExpecting(d.e(),d.t());}
    g=oldG;
    return g1;
    }
  private void typeK(K k, HashSet<Mdf> mdfs1) {
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;
    if(k.thr()==ThrowKind.Return){mdfs1.add(k.t().mdf());}
    }
  }