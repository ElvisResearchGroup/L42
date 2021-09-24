package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.translationToJava.J;
import is.L42.translationToJava.NativeDispatch;

public class ProgramTypeSystem {
  Program p;
  public static void errIf(boolean cond,List<Pos> poss,String msg){
    if(cond){
      throw new EndError.TypeError(poss,msg);
      }
    }
  public static void errIf(boolean cond,List<Pos> poss,Supplier<String> msg){
    if(cond){
      throw new EndError.TypeError(poss,msg);
      }
    }
  public static void type(boolean typed,Program p){
    L l=p.topCore();
    checkNativeConstraints(p);
    J j=new J(p,null,null,true);
    assert l.ts().stream().allMatch(t->p._ofCore(t.p()).isInterface());
    for(MWT mwt:l.mwts()){
      assert !l.info().isTyped() || switch(0){default: typeMWT(p,mwt,j); yield true;};
      if(!l.info().isTyped()){typeMWT(p,mwt,j);}
      }
    for(NC nc:l.ncs()){
      var pushed=p.push(nc.key(),nc.l());
      if(typed||nc.key().hasUniqueNum()){type(true,pushed);}
      if(nc.key().hasUniqueNum()){new Coherence(pushed,false){
        public boolean checkNativeKind(TrustedKind tK){return false;}
        }.isCoherent(false);}
      }
    if(l.info().close()){
      new Coherence(p,true){
        public boolean checkNativeKind(TrustedKind tK){return false;}
        }.isCoherent(false);
      }
    List<S> estimatedRefined=L(l.ts(),(c,ti)->{
      var pi=ti.p().toNCs();
      var li=p._ofCore(pi);
      for(var m:li.mwts()){c.add(m.key());}
      for(var tj:li.ts()){
        var pj=p.from(tj.p(),pi);
        var extraTj=l.ts().stream().noneMatch(tk->pj.equals(tk.p()));
        errIf(extraTj,l.poss(),ErrMsg.missingImplementedInterface(pj));
        }
      });
    assert new HashSet<>(estimatedRefined).equals(new HashSet<>(l.info().refined())):estimatedRefined+" "+l.info().refined()+" "+l.poss();
    //should hold for well formedness
    //var ok=new HashSet<>(estimatedRefined).equals(new HashSet<>(l.info().refined()));
    //errIf(!ok,l.poss(),ErrMsg.mismatchRefine(estimatedRefined,l.info().refined()));
    }
  public static void checkNativeConstraints(Program p){
    var l=p.topCore();
    var info=l.info();
    if(info.nativeKind().isEmpty()){return;}
    var nk=TrustedKind._fromString(info.nativeKind(),p);
    assert nk!=null;
    nk.checkNativePars(p,true);
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
  private static void typeMHSubtype(Program p,MH mhI, MH mhC,List<Pos>pos){
    String msg=_typeMHSubtypeErrMsg(p, mhI, mhC);
    errIf(msg!=null,pos,msg);
    }
  public static String _typeMHSubtypeErrMsg(Program p,MH mhI, MH mhC){
    if(!p._isSubtype(mhC.t(), mhI.t())){return ErrMsg.methSubTypeExpectedRet(mhC.key(),mhC.t(), mhI.t());} 
    if(mhC.mdf()!=mhI.mdf()){return ErrMsg.methSubTypeExpectedMdf(mhC.key(),mhC.mdf(),mhI.mdf());}
    var parsEq=IntStream.range(0,mhI.pars().size()).allMatch(i->{
      var tIi=mhI.pars().get(i);
      var tCi=mhC.pars().get(i);
      return tIi.mdf().equals(tCi.mdf()) && tIi.p().equals(tCi.p());
      });
    if(!parsEq){return ErrMsg.methSubTypeExpectedPars(mhC.key(),mhC.pars(),mhI.pars());}
    for( var eC:mhC.exceptions()){
      boolean cond=mhI.exceptions().stream().anyMatch(eI->p._isSubtype(eC,eI));
      if(!cond){return ErrMsg.methSubTypeExpectedExc(mhC.key(),eC, mhI.exceptions());}  
      }
    return null;    
    }
  private static void typePlugin(Program p, MWT mwt,J j) {
    //warningOnErr(()->auxTypePlugin(p,mwt,j));
    auxTypePlugin(p,mwt,j);
    }
  private static void auxTypePlugin(Program p, MWT mwt,J j) {
    String nativeUrl=mwt.nativeUrl();
    String nativeKind=p.topCore().info().nativeKind();
    if(!nativeUrl.startsWith("trusted:")){
      var info =new NativeDispatch.NativeUrlInfo(nativeUrl.trim());
      errIf(!info.errorMsg.isEmpty(),mwt.poss(),ErrMsg.nativeSlaveInvalid(info.errorMsg));
      var mh=mwt.mh();
      var ok=mh.mdf().isIn(Mdf.Class,Mdf.Immutable);
      errIf(!ok,mwt.poss(),ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.key(),"imm or class",mh.mdf(),"imm or class"));
      var errs=L(mh.pars().stream().filter(t->!t.mdf().isImm()));
      errIf(!errs.isEmpty(),mwt.poss(),ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),"imm",mwt.key(),errs,"imm"));
      return;
      }
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind._fromString(nativeKind,p);
    var op=TrustedOp._fromString(nativeOp);
    var g=op._of(k);
    errIf(g==null,mwt._e().poss(),
      ErrMsg.nativeReceiverInvalid(mwt.nativeUrl(),nativeKind));
    g.check(true,mwt,j);
    }
  private static void typeMethE(Program p,MH mh, E e){
    var g=G.of(mh);
    List<P> ps=L(mh.exceptions().stream().map(t->t.p()));
    var pts=new PathTypeSystem(true,p,g,L(),ps,mh.t().p());
    e.visitable().accept(pts);
    BodyTypes.checkBody(p, g, mh, e);
    var nde=pts.positionOfNonDeterministicError;
    errIf(nde!=null && !mh.key().m().startsWith("#$"),nde,
      ErrMsg.nonDetermisticErrorOnlyHD(mh,pts.typeOfNonDetermisticError));
    }
  }