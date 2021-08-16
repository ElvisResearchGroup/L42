package is.L42.sifo;

import static is.L42.generated.LDom._elem;
import static is.L42.generated.ThrowKind.Error;
import static is.L42.generated.ThrowKind.Exception;
import static is.L42.generated.ThrowKind.Return;
import static is.L42.sifo.SifoTopTS.allMustTopErr;
import static is.L42.sifo.SifoTopTS.differentSecurityLevelsExceptionsErr;
import static is.L42.sifo.SifoTopTS.differentSecurityLevelsVariablesErr;
import static is.L42.sifo.SifoTopTS.listPNCsToString;
import static is.L42.sifo.SifoTopTS.listPToString;
import static is.L42.sifo.SifoTopTS.moreThanOneAnnotationErr;
import static is.L42.sifo.SifoTopTS.notEqualErr;
import static is.L42.sifo.SifoTopTS.notSubErr;
import static is.L42.tools.General.L;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.Block;
import is.L42.generated.Core.D;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.E;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Core.EX;
import is.L42.generated.Core.K;
import is.L42.generated.Core.L;
import is.L42.generated.Core.Loop;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.OpUpdate;
import is.L42.generated.Core.PCastT;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.T;
import is.L42.generated.Core.Throw;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.typeSystem.AlternativeMethodTypes;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;
import is.L42.visitors.FV;
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
  public static String notSubErr(Object p1, Object p2){
    return "Level " + p1 + " is not a sublevel of " + p2;}
  public static String notEqualErr(Object p1, Object p2){
    return "Level " + p1 + " is not equal to " + p2;}
  public static String isNotTopErr(Object p, Object top){
    return "Level " + p + " is not the top of the lattice. Should be " + top;}

  Program p;
  Lattice42 lattice;
  int startDept;
  public SifoTopTS(Program p,Lattice42 lattice, int dept){
    this.p=p;
    this.lattice=lattice;
    this.startDept=dept;
    }
  public SifoTopTS(Program p,P.NCs top){
    this.p=p;
    var s=top.cs().size();
    var ttop=new P.NCs(0,L(top.cs().get(s-1)));
    startDept=-1;
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
  private void kParsOk(MH k,SifoTypeSystem vis,Coherence c){
    var s=vis.getSifoAnn(k.t().docs());
    for(var i:range(k.key().xs())){
      var xi=k.key().xs().get(i);
      var ti=k.pars().get(i);
      var si=vis.getSifoAnn(ti.docs());
      if(!lattice.secondHigherThanFirst(s, si)){throw new EndError.TypeError(p.topCore().poss(), notSubErr(s, si));}
      for(T tj:c.fieldTs(xi,Mdf.Readable)){//fieldTs does not return getter types
        var sj=vis.getSifoAnn(tj.docs());
        if(!si.equals(sj)){throw new Error("");}//TODO:
        }
      for(MH mhj:c.mhs){
        if(!mhj.key().xs().isEmpty()){continue;}
        if(mhj.mdf().isClass()){continue;}
        if(!Coherence.fieldName(mhj).equals(xi)){continue;}
        var sj=vis.getSifoAnn(mhj.t().docs());
        if(!si.equals(sj)){
          throw new EndError.TypeError(p.topCore().poss(), notEqualErr(si, sj));}
        }
      }
    }
  @Override public void visitL(L l){
    super.visitL(l);
    var vis=new SifoTypeSystem(startDept,p,G.empty(),L(),Set.of(),P.coreVoid,this.lattice);
    var c=new Coherence(p,false);    
    c.isCoherent(false);//check if is coherent ant throw errors otherwise
    for(MH k:c.classMhs){kParsOk(k,vis,c);}
    }
  @Override public void visitMWT(Core.L.MWT m){
    var mh=m.mh();
    var g=G.of(mh,mh.docs());
    var vis=new SifoTypeSystem(startDept,p,g,mh.exceptions(),Set.of(),mh.t(),this.lattice);
    vis.checkMH(m.mh(), m.poss());
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
  void mustSubMdf(Mdf m1,Mdf m2,List<Pos> poss){
    if(!Program.isSubtype(m1, m2)){
      throw new EndError.TypeError(poss,ErrMsg.subTypeExpected(m1,m2));
      }
    }  
  void checkMHSub(Core.MH mh,T t){
    var l=p._ofCore(t.p());
    var elem=_elem(l.mwts(),mh.key());
    if(elem==null){return;}
    MH mh0=p.from(elem.mh(),t.p().toNCs());
    var retOk=getSifoAnn(mh.t().docs()).equals(getSifoAnn(mh0.t().docs()));
    if(!retOk) {throw new EndError.TypeError(elem.poss(), notEqualErr(getSifoAnn(mh.t().docs()), getSifoAnn(mh0.t().docs())));}
    var recOk=getSifoAnn(mh.docs()).equals(getSifoAnn(mh0.docs()));
    if(!recOk) {throw new EndError.TypeError(elem.poss(), notEqualErr(getSifoAnn(mh.docs()), getSifoAnn(mh0.docs())));}
    for(int i:range(mh.pars())){
      var ti=mh.pars().get(i);
      var t0i=mh0.pars().get(i);
      var tiOk=getSifoAnn(ti.docs()).equals(getSifoAnn(t0i.docs()));
      if(!tiOk) {throw new EndError.TypeError(elem.poss(), notEqualErr(getSifoAnn(ti.docs()), getSifoAnn(t0i.docs())));}
      }
    if(mh.exceptions().size()!=mh0.exceptions().size()){throw new Error("4");}//TODO: already caught by L42?
    for(int i:range(mh.exceptions())){
      var ti=mh.exceptions().get(i);
      var t0i=mh0.exceptions().get(i);
      if(!ti.p().equals(t0i.p())){throw new Error("5");}//TODO: how to trigger this?
      var tiOk=getSifoAnn(ti.docs()).equals(getSifoAnn(t0i.docs()));
      if(!tiOk){throw new EndError.TypeError(elem.poss(), notEqualErr(getSifoAnn(ti.docs()), getSifoAnn(t0i.docs())));}
      }
    }
  void checkMH(Core.MH mh, List<Pos> poss){
    for(var t:this.p.topCore().ts()){checkMHSub(mh,t);}
    var sRec=getSifoAnn(mh.docs());
    if(!mh.exceptions().isEmpty()){
      var allS=L(mh.exceptions().stream()
          .map(t->getSifoAnn(t.docs()))
          .distinct());
      if(allS.size()!=1){throw new Error("7");}//TODO: already thrown in line 104?
      var sExc=allS.get(0);
      if(!lattice.secondHigherThanFirst(sRec,sExc)){throw new EndError.TypeError(poss, notSubErr(sRec, sExc));}
      }
    var sRet=getSifoAnn(mh.t().docs());
    boolean retIsVoid=mh.t().p().equals(P.pVoid);
    boolean retErr=!retIsVoid && !lattice.secondHigherThanFirst(sRec,sRet);
    if(retErr){throw new EndError.TypeError(poss, notSubErr(sRec, sRet));}
    for(T ti:mh.pars()){
      if(!ti.mdf().isIn(Mdf.Capsule, Mdf.Mutable, Mdf.Lent)){continue;}
      var si=getSifoAnn(ti.docs());
      if(!lattice.secondHigherThanFirst(sRec,si)){throw new EndError.TypeError(poss, notSubErr(sRec, si));}
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
    if(t.p().equals(P.pVoid)){return;}
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
    throw new EndError.TypeError(pos, notSubErr(sub,sup));
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
    var excsSifo=L(excs.stream()
      .filter(t->!t.p().equals(P.pVoid))
      .map(t->getSifoAnn(t.docs())).distinct());
    P excSifo=excsSifo.isEmpty()?null:excsSifo.get(0);
    if(promoted && excSifo!=null){excSifo=lattice.leastUpperBound(excSifo,selectedS);}
    if(excSifo!=null && !lattice.secondHigherThanFirst(excSifo, this._sifoExceptions)){
      throw new EndError.TypeError(e.poss(), notSubErr(excSifo, _sifoExceptions));
      }
    var meths0=AlternativeMethodTypes.types(p,p0,e.s());
    var meths=L(meths0.stream().filter(m->Program.isSubtype(m.mdf(),expected.mdf())));
    if(meths.isEmpty()){
      throw new EndError.TypeError(e.poss(), "IMPOSSIBLE");//TODO: is it impossible for real, but needed for backtracking?
      }
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
    throw lastErr;
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
    var coreP0=p._ofCore(p0);
    if(coreP0==null){return;}
    var mwt=_elem(coreP0.mwts(),e.s());
    if(mwt==null){return;}
    //assert mwt!=null:"";//can be null if the code is sifoed before the type checking happens
    var mh=mwt.mh();
    List<Core.T>parTypes=p.from(mh.parsWithThis(), p0);
    List<Core.T>excTypes=p.from(mh.exceptions(), p0);
    Core.T retType=p.from(mh.t(),p0);
    var retSec=getSifoAnn(retType.docs());
    List<P> allSec=L(c->{
      c.add(retSec);
      for(var pi:parTypes){c.add(getSifoAnn(pi.docs()));}
      });
    boolean promotable=expected.mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class);
    if(retType.p().equals(P.pVoid)){s=lattice.getTop();}
    if (!s.equals(lattice.getBottom())){
      List<P>s1n=List.of();
      if(promotable){
        s1n=lattice.levelsBetween(retSec,s);
        s1n=L(s1n.stream().filter(si->comparable(allSec,si)));
        }
      if(!s1n.isEmpty()){
        var oldExpected=expected;
        var oldG=g;
        var oldMdfs=mdfs;
        var oldSifoExceptions=_sifoExceptions;
        var oldSifoReturns=_sifoReturns;
        for(var si:s1n){
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
      throw new EndError.TypeError(e.poss(),notSubErr(retSec,s));
      }
    visitMCall(false,e,p0,parTypes,excTypes,selectedS);
    }
  @Override public void visitOpUpdate(OpUpdate e){
    T t=g.of(e.x());
    visitExpecting(e.e(),t);
    }
  private boolean isTop(T t){return isTop(getSifoAnn(t.docs()));}
  private boolean isTop(P sifo){return lattice.getTop().equals(sifo);}

  private boolean isBottom(T t){return isBottom(getSifoAnn(t.docs()));}
  private boolean isBottom(P sifo){return lattice.getBottom().equals(sifo);}

  String listTToString(List<T> t0n) {
    return listPToString(t0n.stream().map(t -> getSifoAnn(t.docs())).collect(Collectors.toList()));
    }
  private void onKs(Block e){
    boolean hasErr=e.ks().stream().anyMatch(k->k.thr()==ThrowKind.Error);
    List<X> fvDs=FV.ofBlockDs(e);
    List<X> fvBlock=FV.of(e);
    var s=L(fvBlock.stream()
      .filter(x->g._of(x).mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Capsule) || g.isVar(x))
      .map(x->getSifoAnn(g._of(x).docs()))
      .distinct());
    if(!s.isEmpty()){
      if(s.size()>1){
        throw new EndError.TypeError(e.poss(), differentSecurityLevelsVariablesErr(listPToString(s)));
        }
      for(X x:fvBlock){
        if(g._of(x).mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Capsule) || g.isVar(x)){continue;}
        var sifo=getSifoAnn(g._of(x).docs());
        if(lattice.secondHigherThanFirst(sifo, s.get(0))){continue;}
        throw new EndError.TypeError(e.poss(), notSubErr(sifo,  s.get(0)));
        }
      }
    var t0n=L(Stream.concat(Stream.of(expected),e.ks().stream().map(k->k.t())));
    if(hasErr){//T0..Tn: the result type+the types of catches
      if(t0n.stream().allMatch(t->isTop(t))){return;}
      var allBottom=g.dom().stream().allMatch(x->isBottom(g._of(x)));
      if(allBottom && t0n.stream().allMatch(t->isBottom(t))){return;}
      throw new EndError.TypeError(e.poss(), allMustTopErr(listTToString(t0n), lattice.getTop()));
      }
    for(T ti:t0n){
      if(ti.p().equals(P.pVoid)){continue;}
      ArrayList<P> ss=fvDs.stream().map(x->getSifoAnn(g._of(x).docs())).collect(Collectors.toCollection(ArrayList::new));
      var secTi=getSifoAnn(ti.docs());
      ss.add(secTi);
      var lub=lattice.leastUpperBound(ss);
      if(!lub.equals(secTi)){
        throw new EndError.TypeError(e.poss(), notEqualErr(lub, secTi));}
      }
    }
  void addThrow(Core.K k){
    if(k.thr().equals(ThrowKind.Return)){
      var sifo=getSifoAnn(k.t().docs());
      if(this._sifoReturns==null){this._sifoReturns=sifo;return;}
      if(sifo.equals(this._sifoReturns)){return;}
      var promotable=k.t().mdf().isIn(Mdf.Capsule,Mdf.Immutable);
      var sub=lattice.secondHigherThanFirst(this._sifoReturns,sifo);
      if(promotable && sub){return;}
      throw new Error("10");//TODO: how to trigger this
      }
    if(k.thr().equals(ThrowKind.Exception)){
      var sifo=getSifoAnn(k.t().docs());
      if(this._sifoExceptions==null){this._sifoExceptions=sifo;return;}
      if(sifo.equals(this._sifoExceptions)){return;}
      var sub=lattice.secondHigherThanFirst(this._sifoExceptions,sifo);
      if(sub){return;}
      throw new Error("11");//TODO: how to trigger this
      }
    }
  @Override public void visitBlock(Block e){
    G oldG=this.g;
    try{
      this.g=unVar(e);
      visitBlockUnVar(e);
      }
    finally{this.g=oldG;}
    }
  G unVar(Block e){
    List<X> varred=new ArrayList<>();
    e.accept(new Accumulate.SkipL<T>(){
      @Override public void visitOpUpdate(Core.OpUpdate opUpdate){
        varred.add(opUpdate.x());
        }
      });
    return this.g.keepVars(varred);
    }
  public void visitBlockUnVar(Block e){
    var newMdfs=new HashSet<>(mdfs);
    for(K k:e.ks()){typeK(k,newMdfs);}
    var oldMdfs=mdfs;
    mdfs=newMdfs;
    boolean hasKs=!e.ks().isEmpty();
    if(hasKs){onKs(e);}
    G oldG=g;
    var oldSifoExceptions=this._sifoExceptions;
    var oldSifoReturns=this._sifoReturns;
    for(var k:e.ks()){addThrow(k);}
    G g0;try{g0=typeDs(e.ds());}
    finally{
      this._sifoExceptions=oldSifoExceptions;
      this._sifoReturns=oldSifoReturns;
      }
    g=g.plusEqMdf(g0);
    mdfs=oldMdfs;
    visitE(e.e());
    g=oldG;
    }
  private G growG1(G acc,D d){
    var t=acc._of(d.x());
    if(t==null){return acc;}
    if(!t.docs().isEmpty()){return acc;}
    if(!(d.e() instanceof MCall)){return acc;}
    var e=(MCall)d.e();
    var receiver=acc._of(e.xP());
    if(receiver==null){return acc;}
    var rDocs=receiver.docs();
    if(!rDocs.isEmpty()){return acc.update(d.x(),t.withDocs(rDocs));}
    var l=p._ofCore(receiver.p());
    if(l==null){return acc;}
    var mwt=_elem(l.mwts(),e.s());
    if(mwt==null){return acc;}
    var docs=mwt.mh().t().docs();
    if(docs.isEmpty()){return acc;}
    var newDocs=p.fromDocs(docs,receiver.p().toNCs());
    return acc.update(d.x(),t.withDocs(newDocs));
    }
  private G growG(List<D>allDs){
    G res=g.plusEq(allDs);
    for(D d:allDs){res=growG1(res,d);}
    return res;
    }
  private G typeDs(List<D>allDs){
    G g1=growG(allDs);
    var oldG=g;
    g=g1;
    for(var d:allDs){visitExpecting(d.e(),g._of(d.x()));}//Note: d.t() is not more poor, we infer Sifos on the gamma
    g=oldG;
    return g1;
    }
  private void typeK(K k, HashSet<Mdf> mdfs1){
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;
    if(k.thr()==ThrowKind.Return){mdfs1.add(k.t().mdf());}
    }
  }