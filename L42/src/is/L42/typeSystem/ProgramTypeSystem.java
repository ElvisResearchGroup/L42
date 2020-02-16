package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.todo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.translationToJava.J;
import is.L42.generated.Core.L.NC;
import is.L42.visitors.UndefinedCollectorVisitor;

public class ProgramTypeSystem {
  Program p;
  static void errIf(boolean cond,List<Pos> poss,String msg){
    if(cond){
      throw new EndError.TypeError(poss,msg);
      }
    }
  public static void type(boolean typed,Program p){
    L l=p.topCore();
    J j=new J(p,null,false,null,true);
    assert l.ts().stream().allMatch(t->p._ofCore(t.p()).isInterface());
    for(MWT mwt:l.mwts()){
      assert !l.info().isTyped() || switch(0){default: typeMWT(p,mwt,j); yield true;};
      if(!l.info().isTyped()){typeMWT(p,mwt,j);}
      }
    for(NC nc:l.ncs()){
      var pushed=p.push(nc.key(),nc.l());
      if(typed||nc.key().hasUniqueNum()){type(typed,pushed);}
      if(nc.key().hasUniqueNum()){new Coherence(pushed,false).isCoherent(false);}
      }
    if(l.info().close()){
      new Coherence(p,true).isCoherent(false);}
    List<S> estimatedRefined=L(l.ts(),(c,ti)->{
      var pi=ti.p().toNCs();
      var li=p._ofCore(pi);
      for(var m:li.mwts()){c.add(m.key());}
      for(var tj:li.ts()){
        var pj=p.from(tj.p(),pi);
        var extraTj=l.ts().stream().noneMatch(tk->pj.equals(tk.p()));
        errIf(extraTj,l.poss(),Err.missingImplementedInterface(pj));
        }
      });
    var ok=new HashSet<>(estimatedRefined).equals(new HashSet<>(l.info().refined()));
    errIf(!ok,l.poss(),Err.mismatchRefine(estimatedRefined,l.info().refined()));
    }
  public static void typeMWT(Program p,MWT mwt,J j){
    if(mwt._e()!=null){typeMethE(p,mwt.mh(),mwt._e());}
    if(!mwt.nativeUrl().isEmpty()){typePlugin(p,mwt,j);}
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
  private static void typePlugin(Program p, MWT mwt,J j) {
    String nativeUrl=mwt.nativeUrl();
    String nativeKind=p.topCore().info().nativeKind();
    if(!nativeUrl.startsWith("trusted:")){
      var mh=mwt.mh();
      var ok=mh.mdf().isIn(Mdf.Class,Mdf.Immutable);
      errIf(!ok,mwt.poss(),Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),"imm or class",mh.mdf(),"imm or class"));
      var errs=L(mh.pars().stream().filter(t->!t.mdf().isImm()));
      errIf(!errs.isEmpty(),mwt.poss(),Err.nativeParameterInvalidKind(mwt.nativeUrl(),"imm",mwt.key(),errs,"imm"));
      return;
      }
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind._fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    var g=op._of(k);
    errIf(g==null,mwt._e().poss(),
      Err.nativeReceiverInvalid(mwt.nativeUrl(),nativeKind));
    g.of(true,mwt,j);
    }
  private static void typeMethE(Program p,MH mh, E e){
    var g=G.of(mh);
    List<P> ps=L(mh.exceptions().stream().map(t->t.p()));
    e.visitable().accept(new PathTypeSystem(true,p,g,L(),ps,mh.t().p()));
    var mdf=TypeManipulation.fwdPOf(mh.t().mdf());
    e.visitable().accept(new MdfTypeSystem(p,g,Collections.emptySet(),mdf));
    }

}
