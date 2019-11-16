package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
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
import is.L42.generated.Pos;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.generated.Core.L.NC;
import is.L42.visitors.UndefinedCollectorVisitor;
import lombok.NonNull;

public class ProgramTypeSystem {
  Program p;
  static void errIf(boolean cond,List<Pos> poss,String msg){
    if(cond){throw new EndError.TypeError(poss,msg);}
    }
  public static void type(boolean okRetype,boolean typed,Program p){
    L l=p.topCore();
    assert okRetype || !l.info().isTyped();
    assert l.ts().stream().allMatch(t->p._ofCore(t.p()).isInterface());
    for(MWT mwt:l.mwts()){
      assert !l.info().isTyped() || switch(0){default: typeMWT(p,mwt); yield true;};
      if(!l.info().isTyped()){typeMWT(p,mwt);}
      }
    for(NC nc:l.ncs()){
      var pushed=p.push(nc.key(),nc.l());
      if(typed||nc.key().hasUniqueNum()){type(okRetype,typed,pushed);}
      if(nc.key().hasUniqueNum()){new Coherence(pushed,false).isCoherent();}
      }
    if(l.info().closeState()){new Coherence(p,true).isCoherent();}
    }
  public static void typeMWT(Program p,MWT mwt){
    if(mwt._e()!=null){typeMethE(p,mwt.mh(),mwt._e());}
    if(!mwt.nativeUrl().isEmpty()){typePlugin(p,mwt);}
    List<MH> mhs=L(p.topCore().ts(),(c,ti)->{
      var pi=ti.p().toNCs();
      var l=p._ofCore(pi);
      assert l.isInterface();
      var mwts=l.mwts();
      var mi=_elem(mwts,mwt.key());
      if(mi==null){return;}
      assert mi._e()==null;
      c.add(p.from(mi.mh(),pi));
      });
    for(MH mh:mhs){ typeMHSubtype(p,mh,mwt.mh(),mwt.poss());}
    }
  private static void typeMHSubtype(Program p,MH mhI, MH mhC,List<Pos>pos) {
    errIf(!p.isSubtype(mhC.t(), mhI.t(),pos),pos,
      Err.methSubTypeExpectedRet(mhC.key(),mhC.t(), mhI.t())); 
    errIf(mhC.mdf()!=mhI.mdf(),pos,Err.methSubTypeExpectedMdf(mhC.key(),mhC.mdf(),mhI.mdf()));
    errIf(!mhI.pars().equals(mhC.pars()),pos,
      Err.methSubTypeExpectedPars(mhC.key(),mhC.pars(),mhI.pars()));
    for( var eC:mhC.exceptions()){
      boolean cond=mhI.exceptions().stream().anyMatch(eI->p.isSubtype(eC,eI, pos));
      errIf(cond,pos,Err.methSubTypeExpectedExc(mhC.key(),eC, mhI.exceptions()));  
      }    
    }
  private static void typePlugin(Program p, MWT mwt) {
    String nativeUrl=mwt.nativeUrl();
    String nativeKind=p.topCore().info().nativeKind();
    if(!nativeUrl.startsWith("trusted:")){throw todo();}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    var g=op._of(k);
    errIf(g==null,mwt._e().poss(),
      Err.nativeReceiverInvalid(mwt.nativeUrl(),nativeKind));
    g.of(true, p, mwt);
    }
  private static void typeMethE(Program p,MH mh, E e){
    var g=G.of(mh);
    List<P> ps=L(mh.exceptions().stream().map(t->t.p()));
    e.visitable().accept(new PathTypeSystem(true,p,g,L(),ps,mh.t().p()));
    var mdf=TypeManipulation.fwdPOf(mh.t().mdf());
    e.visitable().accept(new MdfTypeSystem(p,g,Collections.emptySet(),mdf));
    }

}
