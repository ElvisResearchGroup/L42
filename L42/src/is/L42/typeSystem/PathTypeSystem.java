package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.List;

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
    if(e.thr()==ThrowKind.Error){return;}
    boolean find=false;
    if(e.thr()==ThrowKind.Exception){
      for(P pi:ps){
        try{visitExpecting(e.e(),pi);find=true;}
        catch(EndError.TypeError te){}
        }
      }
    else{assert e.thr()==ThrowKind.Return;
      for(T ti:ts){
        try{visitExpecting(e.e(),ti.p());find=true;}
        catch(EndError.TypeError te){}
        }
      }
    errIf(!find,e,Err.leakedThrow(e.thr().inner));
    //TODO: we may add a flag to the TS so that we skip rechecking all the expressions that does not
    // contribute to the result (that is, meth parameters, D.es and crucially throw.e)
    }
  @Override public void visitMCall(MCall e){
    P p0=guess(g,e.xP());
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
  public static P guess(G g, XP xp){
    if(xp instanceof EX){return g.of(((EX)xp).x()).p();}
    return ((PCastT)xp).t().p();
    }
  @Override public void visitOpUpdate(OpUpdate e){
    mustSubPath(P.pVoid,expected,e.poss());
    T t=g.of(e.x());
    visitExpecting(e.e(),t.p());
    assert !TypeManipulation.fwd_or_fwdP_in(t.mdf());
    }
  @Override public void visitBlock(Block e){
    }
  @Override public void visitK(K e){
    }




  }
