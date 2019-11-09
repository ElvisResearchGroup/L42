package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.todo;

import java.util.Collections;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.P;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.generated.Core.L.NC;
import is.L42.visitors.UndefinedCollectorVisitor;

public class ProgramTypeSystem {
  Program p;
  static void errIf(boolean cond,E e,String msg){
    if(cond){throw new EndError.TypeError(e.poss(),msg);}
    }
  public static void type(boolean typed,Program p){
    L l=p.topCore();
    assert !l.info().isTyped();
    assert l.ts().stream().allMatch(t->p._ofCore(t.p()).isInterface());
    for(MWT mwt:l.mwts()){typeMWT(p,mwt);}
    for(NC nc:l.ncs()){
      var pushed=p.push(nc.key(),nc.l());
      if(typed||nc.key().hasUniqueNum()){type(typed,pushed);}
      if(nc.key().hasUniqueNum()){coherent(pushed);}
      }
    }
  public static void coherent(Program p){
    //TODO:
    }
  public static void typeMWT(Program p,MWT mwt){
    if(mwt._e()!=null){visitMethE(p,mwt.mh(),mwt._e());}
    if(!mwt.nativeUrl().isEmpty()){visitNative(p,mwt);}
    //TODO: more stuff here
    }
  private static void visitNative(Program p, MWT mwt) {
    String nativeUrl=mwt.nativeUrl();
    String nativeKind=p.topCore().info().nativeKind();
    if(!nativeUrl.startsWith("trusted:")){throw todo();}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    var g=op._of(k);
    errIf(g==null,mwt._e(),
      Err.nativeReceiverInvalid(mwt.nativeUrl(),nativeKind));
    g.of(true, p, mwt);
    }
  private static void visitMethE(Program p,MH mh, E e){
    var g=G.of(mh);
    List<P> ps=L(mh.exceptions().stream().map(t->t.p()));
    e.visitable().accept(new PathTypeSystem(true,p,g,L(),ps,mh.t().p()));
    var mdf=TypeManipulation.fwdPOf(mh.t().mdf());
    e.visitable().accept(new MdfTypeSystem(p,g,Collections.emptySet(),mdf));
    }

}
