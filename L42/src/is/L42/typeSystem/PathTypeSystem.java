package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.generated.Mdf.*;
import static is.L42.generated.ThrowKind.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import is.L42.generated.Core.*;
import is.L42.generated.Core.L.MWT;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.ThrowKind;
import is.L42.visitors.UndefinedCollectorVisitor;
import lombok.NonNull;

public class PathTypeSystem extends UndefinedCollectorVisitor{
  public PathTypeSystem(boolean isDeep, Program p, G g, List<T> ts, List<P> ps, P expected) {
    this.isDeep = isDeep;
    this.p = p;
    this.g = g;
    this.ts = ts;
    this.ps = ps;
    this.expected=expected;
  }
  boolean isDeep;
  Program p;
  G g;
  List<T> ts;
  List<P> ps;
  P expected;
  void visitExpecting(E e,P newExpected){
    P oldE=expected;
    expected=newExpected;
    visitE(e);
    expected=oldE;
    }
  void errIf(boolean cond,E e,String msg){
    if(cond){throw new EndError.TypeError(e.poss(),msg);}
    }
  void mustSubPath(P p1,P p2,List<Pos>poss){
    if(!p.isSubtype(p1, p2, poss)){
      throw new EndError.TypeError(poss,Err.subTypeExpected(p1,p2));
      }
    }  
  @Override public void visitEVoid(EVoid e){
    errIf(expected!=P.pAny && expected!=P.pVoid,e,
      Err.invalidExpectedTypeForVoidLiteral(expected));
    }
  @Override public void visitPCastT(PCastT e){
    errIf(!e.t().mdf().isClass(),e,Err.castOnPathMustBeClass(e.t()));
    mustSubPath(e.p(),e.t().p(),e.poss());
    mustSubPath(e.t().p(),expected,e.poss());
    if(e.t().p()==P.pAny){return;}
    L l=p._ofCore(e.p());
    L l1=p._ofCore(e.t().p());
    errIf(l.isInterface(),e,Err.castOnPathOnlyValidIfNotInterface(e.p()));
    errIf(!l1.info().declaresClassMethods(),e,
      Err.castOnPathOnlyValidIfDeclaredClassMethods(e.t().p()));
    }
  @Override public void visitL(L e){
    mustSubPath(P.pLibrary,expected,e.poss());
    if(isDeep){ProgramTypeSystem.type(true,p.push(e));}    
    }
  @Override public void visitEX(EX e){
    mustSubPath(g.of(e.x()).p(),expected,e.poss());
    }
  @Override public void visitLoop(Loop e){
    mustSubPath(P.pVoid,expected,e.poss());
    visitExpecting(e.e(),P.pVoid);
    }
  @Override public void visitThrow(Throw e){
    visitExpecting(e.e(),P.pAny);
    if(e.thr()==Error){return;}
    boolean find=false;
    if(e.thr()==Exception){find=tryAlternatives(ps.stream(),e.e());}
    else{find=tryAlternatives(ts.stream().map(t->t.p()),e.e());}
    errIf(!find,e,Err.leakedThrow(e.thr().inner));
    //TODO: we may add a flag to the TS so that we skip rechecking all the expressions that does not
    // contribute to the result (that is, meth parameters, D.es and crucially throw.e)
    }
  private boolean tryAlternatives(Stream<P> stream,E e){
    var oldTs=ts;
    var oldPs=ps;
    var oldG=g;
    var oldExpected=expected;
    var res=stream.anyMatch(p->{
      try{
        ts=oldTs;
        ps=oldPs;
        g=oldG;
        expected=p;
        visitE(e);
        expected=oldExpected;
        return true;
        }
      catch(EndError.TypeError te){return false;}    
      });
    ts=oldTs;
    ps=oldPs;
    g=oldG;
    expected=oldExpected;
    return res;
    }
  @Override public void visitMCall(MCall e){
    P p0=TypeManipulation.guess(g,e.xP());
    var l=p._ofCore(p0);
    assert l!=null;
    MWT mwt=_elem(l.mwts(),e.s());
    errIf(mwt==null,e,Err.methodDoesNotExists(e.s(),L(l.mwts().stream().map(m->m.key()))));
    MH mh=p.from(mwt.mh(),p0.toNCs());
    mustSubPath(mh.t().p(),expected,e.poss());
    visitExpecting(e.xP(),p0);
    for(int i:range(e.es())){
      visitExpecting(e.es().get(i),mh.pars().get(i).p());
      }
    for(T ti:mh.exceptions()){
      var err=ps.stream().noneMatch(pj->p.isSubtype(ti.p(),pj,e.poss()));
      errIf(err,e,Err.leakedExceptionFromMethCall(ti.p()));
      }
    }
  @Override public void visitOpUpdate(OpUpdate e){
    mustSubPath(P.pVoid,expected,e.poss());
    T t=g.of(e.x());
    visitExpecting(e.e(),t.p());
    assert !TypeManipulation.fwd_or_fwdP_in(t.mdf());
    }
  @Override public void visitBlock(Block e){
    var ts1=new ArrayList<T>(ts);
    var ps1=new ArrayList<P>(ps);
    for(K k:e.ks()){typeK(k,ts1,ps1);}
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
    }
  private void typeK(K k, ArrayList<T> ts1, ArrayList<P> ps1) {
    var t=k.t();
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;
    if(k.thr()==Return){ts1.add(t);}
    if(k.thr()==Exception){ps1.add(t.p());}
    if(!t.mdf().isClass()){return;}
    var l=p._ofCore(t.p());
    errIf(!l.isInterface(),k.e(),Err.castOnPathOnlyValidIfNotInterface(t.p()));
    errIf(!l.info().declaresClassMethods(),k.e(),
      Err.castOnPathOnlyValidIfDeclaredClassMethods(t.p()));
    }
  }
