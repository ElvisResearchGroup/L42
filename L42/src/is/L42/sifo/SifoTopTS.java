package is.L42.sifo;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;
import static is.L42.generated.Mdf.*;
import static is.L42.generated.ThrowKind.*;
import static is.L42.sifo.SifoTopTS.*;
import static is.L42.sifo.SifoTypeSystem.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core.*;
import is.L42.generated.Core.L.MWT;
import is.L42.tests.TestDecoratorsErrors;
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
public class SifoTopTS extends is.L42.visitors.PropagatorCollectorVisitor{
  public static String exceptionsErrString = " Errors/Exceptions have different security levels, only the same is allowed. Found: ";
  public static String varErrString = " Variables have different security levels, only the same is allowed. Found: ";
  public static String topErrString = "Errors/Exceptions have different security levels, all have to be top of lattice: ";
  public static String allMustTopErr(String t0n, Object top) {
    return topErrString + top + ". Found: " + t0n;
    }
  public static String differentSecurityLevelsExceptionsErr(String sifoExceptions){
    return exceptionsErrString + sifoExceptions;}
  public static String differentSecurityLevelsVariablesErr(String sifoExceptions){
    return varErrString + sifoExceptions;}
  public static String moreThanOneAnnotationErr(String annotations){
    return "More than one level is annotated, only one is allowed: " +  annotations;}
  static String listPToString(List<P> ps) {
    return ps.stream()
      .map(p -> p+"")
      .collect(Collectors.joining(", "));
    }
  static String listPNCsToString(List<P.NCs> ncs) {
    return ncs.stream().map(p -> p+"").collect(Collectors.joining(", "));
  }
  public static String noSubErr(Object p1, Object p2){
    return "Level " + p1 + " is not a sublevel of " + p2;}
  public static String notEqualErr(Object p1, Object p2){
    return "Level " + p1 + " is not equal to " + p2;}
  public static String isNotTopErr(Object p, Object top){
    return "Level " + p + " is not the top of the lattice. Should be " + top;}

  Program p;
  Lattice42 lattice;
  int startDept;
  public SifoTopTS(Program p,P.NCs top){
    this.p=p;
    var s=top.cs().size();
    var ttop=new P.NCs(0,L(top.cs().get(s-1)));
    startDept=-1;//TODO:
    var pp=p.navigate(popLRight(top.cs()));
    this.lattice=new Lattice42(pp,ttop);
    }  
  @Override public void visitNC(Core.L.NC nc){
     Program oldP=p;
     p=p.push(nc.key(),nc.l());
     startDept+=1;
     try{visitL(nc.l());}
     finally{p=oldP;startDept-=1;}
     }
  @Override public void visitMWT(Core.L.MWT m){
    var mh=m.mh();
    var g=G.of(mh,mh.docs());
    var vis=new SifoTypeSystem(startDept,p,g,mh.exceptions(),Set.of(),mh.t(),this.lattice);
    vis.checkMH(m.mh());
    if(m._e()!=null){
      m._e().visitable().accept(vis);
      }
    }
  }
class SifoTypeSystem extends UndefinedCollectorVisitor{
  public SifoTypeSystem(int dept,Program p, G g,List<T> exceptions, Set<Mdf> mdfs, T expected,Lattice42 lattice) {
    this.dept=dept;
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
    throw new EndError.TypeError(p.topCore().poss(), differentSecurityLevelsExceptionsErr(listPToString(sifoExceptions)));//TODO:poss
    }
  boolean isDeep;
  int dept;
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
  void checkMHSub(Core.MH mh,T t){
    var l=p._ofCore(t.p());
    var elem=_elem(l.mwts(),mh.key());
    if(elem==null){return;}
    MH mh0=p.from(elem.mh(),t.p().toNCs());
    var retOk=getSifoAnn(mh.t().docs()).equals(getSifoAnn(mh0.t().docs()));
    if(!retOk) {throw new EndError.TypeError(this.p.topCore().poss(), notEqualErr(getSifoAnn(mh.t().docs()), getSifoAnn(mh0.t().docs())));}//TODO:poss
    var recOk=getSifoAnn(mh.docs()).equals(getSifoAnn(mh0.docs()));
    if(!recOk) {throw new EndError.TypeError(this.p.topCore().poss(), notEqualErr(getSifoAnn(mh.docs()), getSifoAnn(mh0.docs())));}//TODO:poss
    for(int i:range(mh.pars())){
      var ti=mh.pars().get(i);
      var t0i=mh0.pars().get(i);
      var tiOk=getSifoAnn(ti.docs()).equals(getSifoAnn(t0i.docs()));
      if(!tiOk) {throw new EndError.TypeError(this.p.topCore().poss(), notEqualErr(getSifoAnn(ti.docs()), getSifoAnn(t0i.docs())));}//TODO:poss
      }
    if(mh.exceptions().size()!=mh0.exceptions().size()){throw new Error("4");}//TODO: already caught by L42?
    for(int i:range(mh.exceptions())){
      var ti=mh.exceptions().get(i);
      var t0i=mh0.exceptions().get(i);
      if(!ti.p().equals(t0i.p())){throw new Error("5");}//TODO:
      var tiOk=getSifoAnn(ti.docs()).equals(getSifoAnn(t0i.docs()));
      if(!tiOk){throw new EndError.TypeError(this.p.topCore().poss(), notEqualErr(getSifoAnn(ti.docs()), getSifoAnn(t0i.docs())));}//TODO:poss
      }
    }
  void checkMH(Core.MH mh){
    for(var t:this.p.topCore().ts()){checkMHSub(mh,t);}
    var sRec=getSifoAnn(mh.docs());
    if(!mh.exceptions().isEmpty()){
      var allS=L(mh.exceptions().stream()
          .map(t->getSifoAnn(t.docs()))
          .distinct());
      if(allS.size()!=1){throw new Error("7");}//TODO: already thrown in line 104?
      var sExc=allS.get(0);
      if(!lattice.secondHigherThanFirst(sRec,sExc)){throw new EndError.TypeError(this.p.topCore().poss(), noSubErr(sRec, sExc));}//TODO:poss
      }
    var sRet=getSifoAnn(mh.t().docs());
    if(!lattice.secondHigherThanFirst(sRec,sRet)){throw new EndError.TypeError(this.p.topCore().poss(), noSubErr(sRec, sRet));}//TODO:poss
    for(T ti:mh.pars()){
      if(!ti.mdf().isIn(Mdf.Capsule, Mdf.Mutable, Mdf.Lent)){continue;}
      var si=getSifoAnn(ti.docs());
      if(!lattice.secondHigherThanFirst(sRec,si)){throw new EndError.TypeError(this.p.topCore().poss(), noSubErr(sRec, si));}//TODO:poss
      }
     }
  @Override public void visitEVoid(EVoid e){}
  @Override public void visitPCastT(PCastT e){}
  @Override public void visitL(L e){}
  P getSifoAnn(List<Doc>docs){
    List<P.NCs> paths=sifos(docs);
    if(paths.isEmpty()) {return lattice.getBottom();}
    if(paths.size()!=1){throw new EndError.TypeError(this.p.topCore().poss(), moreThanOneAnnotationErr(listPNCsToString(paths)));}//TODO:poss
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
    var mdfOk=subMdf.isIn(Mdf.Immutable, Mdf.Capsule,Mdf.Class);
    if(mdfOk && lattice.secondHigherThanFirst(sub,sup)){return;}
    throw new EndError.TypeError(pos, noSubErr(sub,sup));
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
  public void visitMCall(boolean promoted,MCall e,P.NCs p0,List<T> parTypes,List<T>excs, P selectedS){
    var excsSifo=L(excs.stream().map(t->getSifoAnn(t.docs())).distinct());
    P excSifo=excsSifo.isEmpty()?null:excsSifo.get(0);
    if(excSifo!=null && !excSifo.equals(this._sifoExceptions)){
      throw new EndError.TypeError(e.poss(), notEqualErr(excSifo, _sifoExceptions));//TODO: can not throw excSifo
      }
    var meths=AlternativeMethodTypes.types(p,p0,e.s());
    meths=L(meths.stream().filter(m->Program.isSubtype(m.mdf(),expected.mdf())));
    assert !meths.isEmpty():
      "";
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
          var sec=getSifoAnn(pi.docs());
          if(promoted){sec=lattice.leastUpperBound(sec,selectedS);}          
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
  boolean comparable(List<P> ss,P s){
    for(P si:ss){
      if(lattice.secondHigherThanFirst(si,s)){continue;}
      if(lattice.secondHigherThanFirst(s,si)){continue;}
      return false;
      }
    return true;
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
    List<P> allSec=L(c->{
      c.add(retSec);
      for(var pi:parTypes){c.add(getSifoAnn(pi.docs()));}
      });
    boolean promotable=expected.mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class);
    if (!s.equals(lattice.getBottom())){
      List<P>s1n=List.of();
      if(promotable){
        s1n=lattice.levelsBetween(retSec,s);
        s1n=L(s1n.stream().filter(si->comparable(allSec,si)));
        }
      if(!s1n.isEmpty()){
        for(var si:s1n){
          var oldExpected=expected;
          var oldG=g;
          var oldMdfs=mdfs;
          var oldSifoExceptions=_sifoExceptions;
          var oldSifoReturns=_sifoReturns;
          try{visitMCall(true,e,p0,parTypes,excTypes,si);return;}
          catch(EndError err){}
          expected=oldExpected;
          g=oldG;
          mdfs=oldMdfs;
          _sifoExceptions=oldSifoExceptions;
          _sifoReturns=oldSifoReturns;
          }
        }
      selectedS=s;
      }
    if(!promotable && !s.equals(retSec)){
      throw new EndError.TypeError(e.poss(),notEqualErr(s,retSec));
      }
    if(promotable && !lattice.secondHigherThanFirst(retSec,s)){
      throw new EndError.TypeError(e.poss(),noSubErr(retSec,s));
      }
    visitMCall(false,e,p0,parTypes,excTypes,selectedS);
    }
  @Override public void visitOpUpdate(OpUpdate e){
    T t=g.of(e.x());
    visitExpecting(e.e(),t);
    }
  private boolean isTop(T t){return isTop(getSifoAnn(t.docs()));}
  private boolean isTop(P sifo){return lattice.getTop().equals(sifo);}

  String listTToString(List<T> t0n) {
    return listPToString(t0n.stream().map(t -> getSifoAnn(t.docs())).collect(Collectors.toList()));
    }
  private void onKs(Block e){
    boolean hasErr=e.ks().stream().anyMatch(k->k.thr()==ThrowKind.Error);
    var sfv=e.ds().stream().flatMap(di->FV.of(di.e().visitable()).stream());
    List<X> fv=L(sfv.filter(x->e.ds().stream().noneMatch(d->d.x().equals(x))));
    var s=L(fv.stream()
      .filter(x->g._of(x).mdf().isIn(Mdf.Mutable, Mdf.Capsule) || g.isVar(x))
      .map(x->getSifoAnn(g._of(x).docs()))
      .distinct());
    if(!s.isEmpty()){
      if(s.size()>1){
        throw new EndError.TypeError(e.poss(), differentSecurityLevelsVariablesErr(listPToString(s)));
        }
      }
    var t0n=L(Stream.concat(Stream.of(expected),e.ks().stream().map(k->k.t())));
    if(hasErr){//T0..Tn: the result type+the types of catches
      if(t0n.stream().allMatch(t->isTop(t))){return;}
      throw new EndError.TypeError(e.poss(), allMustTopErr(listTToString(t0n), lattice.getTop()));
      }
    for(T ti:t0n){
      ArrayList<P> ss=fv.stream().map(x->getSifoAnn(g._of(x).docs())).collect(Collectors.toCollection(ArrayList::new));
      var secTi=getSifoAnn(ti.docs());
      ss.add(secTi);
      var lub=lattice.leastUpperBound(ss);
      if(!lub.equals(secTi)){
        throw new EndError.TypeError(e.poss(), notEqualErr(lub, secTi));}
      }
    }
  @Override public void visitBlock(Block e){
    var newMdfs=new HashSet<>(mdfs);
    for(K k:e.ks()){typeK(k,newMdfs);}
    var oldMdfs=mdfs;
    mdfs=newMdfs;
    boolean hasKs=!e.ks().isEmpty();
    if(hasKs){onKs(e);}
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