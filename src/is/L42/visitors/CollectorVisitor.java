package is.L42.visitors;
import java.util.List;

import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.*;
import is.L42.generated.Core.Info;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.NC;

public interface CollectorVisitor {
  default void visitE(Core.E e){e.visitable().accept(this);}
  default void visitM(Full.L.M m){m.visitable().accept(this);}
  default void visitE(Full.E e){e.visitable().accept(this);}
  default void visitE(Half.E e){e.visitable().accept(this);}
  default void visitST(ST st){st.visitable().accept(this);}
  default void visitXP(Core.XP xP){xP.visitable().accept(this);}
  default void visitXP(Half.XP xP){xP.visitable().accept(this);}

  default void visitEs(List<Core.E> es){ for(var e:es){this.visitE(e);} }
  default void visitPs(List<? extends P> ps){ for(var p:ps){this.visitP(p);} }
  default void visitSs(List<S> ss){ for(var s:ss){this.visitS(s);} }
  default void visitPathSels(List<Core.PathSel> pathSels){ for(var s:pathSels){this.visitPathSel(s);} }
  default void visitXPs(List<Core.XP> xPs){ for(var x:xPs){this.visitXP(x);} }
  default void visitFullEs(List<Full.E> es){ for(var e:es){this.visitE(e);} }
  default void visitHalfEs(List<Half.E> es){ for(var e:es){this.visitE(e);} }
  default void visitHalfXPs(List<Half.XP> xPs){ for(var x:xPs){this.visitXP(x);} }
  default void visitCs(List<C> cs){ for(var c:cs){this.visitC(c);} }
  default void visitXs(List<X> xs){ for(var x:xs){this.visitX(x);} }
  default void visitSTz(List<ST> stz){ for(var s:stz){this.visitST(s);} }
  default void visitMWTs(List<MWT> mwts){ for(var mwt:mwts){this.visitMWT(mwt);} }
  default void visitNCs(List<NC> ncs){ for(var nc:ncs){this.visitNC(nc);} }
  default void visitDs(List<Core.D> ds){ for(var d:ds){this.visitD(d);} }
  default void visitKs(List<Core.K> ks){ for(var k:ks){this.visitK(k);} }
  default void visitTs(List<Core.T> ts){ for(var t:ts){this.visitT(t);} }
  default void visitDocs(List<Core.Doc> docs){ for(var doc:docs){this.visitDoc(doc);} }
  default void visitHalfDs(List<Half.D> ds){ for(var d:ds){this.visitD(d);} }
  default void visitHalfKs(List<Half.K> ks){ for(var k:ks){this.visitK(k);} }
  default void visitFullMs(List<Full.L.M> ms){ for(var m:ms){this.visitM(m);} }
  default void visitFullDs(List<Full.D> ds){ for(var d:ds){this.visitD(d);} }  
  default void visitFullVarTxs(List<Full.VarTx> varTxs){ for(var varTx:varTxs){this.visitVarTx(varTx);} }
  default void visitFullKs(List<Full.K> ks){ for(var k:ks){this.visitK(k);} }
  default void visitFullPars(List<Full.Par> pars){ for(var par:pars){this.visitPar(par);} }
  default void visitFullTs(List<Full.T> ts){ for(var t:ts){this.visitT(t);} }
  default void visitFullDocs(List<Full.Doc> docs){ for(var doc:docs){this.visitDoc(doc);} }
  
  void visitC(C c);
  void visitP(P p);
  void visitS(S s);
  void visitX(X x);
  void visitSTMeth(ST.STMeth stMeth);
  void visitSTOp(ST.STOp stOp);
  void visitEX(Core.EX x);
  void visitPCastT(Core.PCastT pCastT);
  void visitEVoid(Core.EVoid eVoid);
  void visitL(CoreL l);
  void visitInfo(Info info);
  void visitMWT(MWT mwt);
  void visitNC(NC nc);
  void visitMCall(Core.MCall mCall);
  void visitBlock(Core.Block block);
  void visitLoop(Core.Loop loop);
  void visitThrow(Core.Throw thr);
  void visitOpUpdate(Core.OpUpdate opUpdate);
  void visitD(Core.D d);
  void visitK(Core.K k);
  void visitT(Core.T t);
  void visitDoc(Core.Doc doc);
  void visitPathSel(Core.PathSel pathSel);
  void visitMH(Core.MH mh);
  void visitPCastT(Half.PCastT pCastT);
  void visitSlashCastT(Half.SlashCastT slash);
  void visitBinOp(Half.BinOp binOp);
  void visitMCall(Half.MCall mCall);
  void visitBlock(Half.Block block);
  void visitLoop(Half.Loop loop);
  void visitThrow(Half.Throw thr);
  void visitOpUpdate(Half.OpUpdate opUpdate);
  void visitD(Half.D d);
  void visitK(Half.K k);
  void visitCsP(Full.CsP csP);
  void visitL(Full.L l);
  void visitF(Full.L.F f);
  void visitMI(Full.L.MI mi);
  void visitMWT(Full.L.MWT mwt);
  void visitNC(Full.L.NC nc);
  void visitSlash(Full.Slash slash);
  void visitSlashX(Full.SlashX slashX);
  void visitEString(Full.EString eString);
  void visitEPathSel(Full.EPathSel ePathSel);
  void visitUOp(Full.UOp uOp);
  void visitBinOp(Full.BinOp binOp);
  void visitCast(Full.Cast cast);
  void visitCall(Full.Call call);
  void visitBlock(Full.Block block);
  void visitLoop(Full.Loop loop);
  void visitWhile(Full.While sWhile);
  void visitFor(Full.For sFor);
  void visitThrow(Full.Throw thr);
  void visitOpUpdate(Full.OpUpdate opUpdate);
  void visitIf(Full.If sIf);
  void visitD(Full.D d);
  void visitVarTx(Full.VarTx varTx);
  void visitK(Full.K k);
  void visitPar(Full.Par par);
  void visitT(Full.T t);
  void visitDoc(Full.Doc doc);
  void visitPathSel(Full.PathSel pathSel);
  void visitMH(Full.MH mh);
  void visitProgram(Program program);
  void visitPTails(PTails pTails);
  }