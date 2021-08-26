package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.generated.ThrowKind.*;
import static is.L42.tools.General.*;
import static is.L42.typeSystem.ProgramTypeSystem.errIf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.*;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.nativeCode.TrustedKind;
import is.L42.visitors.UndefinedCollectorVisitor;

public class PathTypeSystem extends UndefinedCollectorVisitor{
  public PathTypeSystem(boolean isDeep, Program p, G g, List<T> ts, List<P> ps, P expected) {
    this.isDeep = isDeep;
    this.p = p;
    this.g = g;
    this.ts = ts;
    this.ps = ps;
    this.expected=expected;
    this._computed=null;
  }
  public List<Pos> positionOfNonDeterministicError=null;
  public P typeOfNonDetermisticError=null;
  boolean isDeep;
  Program p;
  G g;
  List<T> ts;
  List<P> ps;
  P expected;
  P _computed=null;
  public P _computed(){return _computed;}
  void visitExpecting(E e,P newExpected){
    P oldE=expected;
    expected=newExpected;
    visitE(e);
    expected=oldE;
    }
  void mustSubPath(P p1,P p2,List<Pos>poss){
    if(!p._isSubtype(p1, p2)){
      throw new EndError.TypeError(poss,ErrMsg.subTypeExpected(p1,p2));
      }
    }  
  @Override public void visitEVoid(EVoid e){
    errIf(expected!=P.pAny && expected!=P.pVoid,e.poss(),
      ErrMsg.invalidExpectedTypeForVoidLiteral(expected));
    _computed=P.pVoid;
    }
  @Override public void visitPCastT(PCastT e){
    errIf(!e.t().mdf().isClass(),e.poss(),ErrMsg.castOnPathMustBeClass(e.t()));
    mustSubPath(e.p(),e.t().p(),e.poss());
    mustSubPath(e.t().p(),expected,e.poss());
    if(e.t().p()==P.pAny){return;}
    L l=p._ofCore(e.p());
    boolean ok=e.t().p()==P.pAny || !l.isInterface();
    errIf(!ok,e.poss(),ErrMsg.castOnPathOnlyValidIfNotInterface(e.p()));
    _computed=e.t().p();
    }
  @Override public void visitL(L e){
    mustSubPath(P.pLibrary,expected,e.poss());
    if(isDeep){ProgramTypeSystem.type(true,p.push(e));}
    _computed=P.pLibrary;
    }
  @Override public void visitEX(EX e){
    mustSubPath(g.of(e.x()).p(),expected,e.poss());
    _computed=g.of(e.x()).p();
    }
  @Override public void visitLoop(Loop e){
    mustSubPath(P.pVoid,expected,e.poss());
    visitExpecting(e.e(),P.pVoid);
    _computed=P.pVoid;
    }
  @Override public void visitThrow(Throw e){
    visitExpecting(e.e(),P.pAny);
    var computed=_computed;
    assert computed!=null;
    if(e.thr()==Error){_computed=null;return;}
    boolean find=false;
    if(e.thr()==Exception){find=tryAlternatives(ps.stream(),computed);}
    else{find=tryAlternatives(ts.stream().map(t->t.p()),computed);}
    errIf(!find,e.poss(),ErrMsg.leakedThrow(e.thr().inner+" "+computed));
    _computed=null;
    }
  private boolean tryAlternatives(Stream<P> stream,P computed){
    return stream.anyMatch(pi->p._isSubtype(computed, pi));
    }
  @Override public void visitMCall(MCall e){
    P p0=TypeManipulation.guess(g,e.xP());
    Core.L l;try{l=p._ofCore(p0);}
    catch(AssertionError ae){throw new AssertionError(p0.toString(),ae);}
    assert l!=null:
    "";
    MWT mwt=_elem(l.mwts(),e.s());
    errIf(mwt==null,e.poss(),ErrMsg.methodDoesNotExists(e.s(),l.mwts()));
    MH mh=p.from(mwt.mh(),p0.toNCs());
    mustSubPath(mh.t().p(),expected,e.poss());
    visitExpecting(e.xP(),p0);
    for(int i:range(e.es())){
      visitExpecting(e.es().get(i),mh.pars().get(i).p());
      }
    for(T ti:mh.exceptions()){
      var err=ps.stream().noneMatch(pj->p._isSubtype(ti.p(),pj));
      errIf(err,e.poss(),ErrMsg.leakedExceptionFromMethCall(ti.p()));
      }
    _computed=mh.t().p();
    }
  @Override public void visitOpUpdate(OpUpdate e){
    mustSubPath(P.pVoid,expected,e.poss());
    T t=g.of(e.x());
    visitExpecting(e.e(),t.p());
    assert !TypeManipulation.fwd_or_fwdP_in(t.mdf());
    _computed=P.pVoid;
    }
  /*void checkCollectedTs(ArrayList<T> ts1){
    if(ts1.isEmpty()) {return;}
    if(ts1.size()>1){
      var count=ts1.stream().map(t->t.mdf()).distinct().count();
      errIf(count!=1,e,ErrMsg.returnMdfInconsistent(ts1));
      }
    T last=ts1.get(ts1.size()-1);
    if(last.p().equals(P.pAny)){return;}
    
    for(T1 in Q.Ts, forall T2 in Tsi T1.mdf=T2.mdf
    var allTs=pushL(this.ts,t);
    var count=allTs.stream().map(ti->ti.mdf()).distinct().count();
    errIf(count!=1,k.e(),ErrMsg.returnMdfInconsistent(allTs));

    
    }*/
  @Override public void visitBlock(Block e){
    var ts1=new ArrayList<T>(ts);
    var ps1=new ArrayList<P>(ps);
    var computeds=new ArrayList<P>();
    for(K k:e.ks()){computeds.add(typeK(k,ts1,ps1));}
    var g1=g.plusEq(e.ds());
    var oldTs=ts;
    var oldPs=ps;
    var oldG=g;
    ts=ts1;
    ps=ps1;
    g=g1;
    for(var di:e.ds()){visitExpecting(di.e(),di.t().p());}
    ts=oldTs;
    ps=oldPs;
    visitE(e.e());
    g=oldG;
    computeds.add(_computed);
    computeds.removeIf(c->c==null);
    var computed=new HashSet<P>();
    for(P c1:computeds){
      var superAll=computeds.stream().allMatch(c2->p._isSubtype(c2,c1));
      if(superAll){computed.add(c1);}
      }
    if(computed.size()!=1){_computed=expected;}
    else{_computed=computed.iterator().next();}
    }
  private void fillNonDeterministicError(List<Pos>pos,P path){
    if(positionOfNonDeterministicError!=null){return;}
    var lib=p._ofCore(path);
    if(lib==null){return;}
    var nk=lib.info().nativeKind();
    if(nk.isEmpty()){return;}
    if(TrustedKind._fromString(nk,p.navigate(path.toNCs()))!=TrustedKind.NonDeterministicError){return;}
    positionOfNonDeterministicError=pos;
    typeOfNonDetermisticError=path;
    }
  private P typeK(K k, ArrayList<T> ts1, ArrayList<P> ps1) {
    var t=k.t();
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;    
    fillNonDeterministicError(k.e().poss(),t.p());
    if(k.thr()==Return){ts1.add(t);}
    if(k.thr()==Exception){ps1.add(t.p());}
    if(!t.mdf().isClass()){return _computed;}
    var l=p._ofCore(t.p());
    boolean anyOrNotInterface=t.p()==P.pAny ||!l.isInterface();
    errIf(!anyOrNotInterface,k.e().poss(),ErrMsg.castOnPathOnlyValidIfNotInterface(t.p()));
    return _computed;
    }
  }