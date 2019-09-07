package is.L42.visitors;
import java.util.List;

import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.*;

public interface CollectorVisitor {
  default void visitE(Core.E e){e.visitable().accept(this);}
  default void visitM(Full.L.M m){m.visitable().accept(this);}
  default void visitE(Full.E e){e.visitable().accept(this);}
  default void visitE(Half.E e){e.visitable().accept(this);}
  default void visitST(ST st){st.visitable().accept(this);}
  default void visitXP(Core.XP xP){xP.visitable().accept(this);}
  default void visitXP(Half.XP xP){xP.visitable().accept(this);}

  default void visitEs(List<Core.E> es){es.forEach(this::visitE);}
  default void visitPs(List<P> ps){ps.forEach(this::visitP);}
  default void visitSs(List<S> ss){ss.forEach(this::visitS);}
  default void visitPathSels(List<Core.PathSel> pathSels){pathSels.forEach(this::visitPathSel);}
  default void visitXPs(List<Core.XP> xPs){xPs.forEach(this::visitXP);}
  default void visitFullEs(List<Full.E> es){es.forEach(this::visitE);}
  default void visitHalfEs(List<Half.E> es){es.forEach(this::visitE);}
  default void visitHalfXPs(List<Half.XP> xPs){xPs.forEach(this::visitXP);}
  default void visitCs(List<C> cs){cs.forEach(this::visitC);}
  default void visitXs(List<X> xs){xs.forEach(this::visitX);}
  default void visitSTz(List<ST> stz){stz.forEach(this::visitST);}
  default void visitMWTs(List<Core.L.MWT> mwts){mwts.forEach(this::visitMWT);}
  default void visitNCs(List<Core.L.NC> ncs){ncs.forEach(this::visitNC);}
  default void visitDs(List<Core.D> ds){ds.forEach(this::visitD);}
  default void visitKs(List<Core.K> ks){ks.forEach(this::visitK);}
  default void visitTs(List<Core.T> ts){ts.forEach(this::visitT);}
  default void visitDocs(List<Core.Doc> docs){docs.forEach(this::visitDoc);}
  default void visitHalfDs(List<Half.D> ds){ds.forEach(this::visitD);}
  default void visitHalfKs(List<Half.K> ks){ks.forEach(this::visitK);}
  default void visitHalfTs(List<Half.T> ts){ts.forEach(this::visitT);}
  default void visitFullMs(List<Full.L.M> ms){ms.forEach(this::visitM);}
  default void visitFullDs(List<Full.D> ds){ds.forEach(this::visitD);}  
  default void visitFullVarTxs(List<Full.VarTx> varTxs){varTxs.forEach(this::visitVarTx);}
  default void visitFullKs(List<Full.K> ks){ks.forEach(this::visitK);}
  default void visitFullPars(List<Full.Par> pars){pars.forEach(this::visitPar);}
  default void visitFullTs(List<Full.T> ts){ts.forEach(this::visitT);}
  default void visitFullDocs(List<Full.Doc> docs){docs.forEach(this::visitDoc);}
  
  void visitC(C c);
  void visitP(P p);
  void visitS(S s);
  void visitX(X x);
  void visitSTMeth(ST.STMeth stMeth);
  void visitSTOp(ST.STOp stOp);
  void visitEX(Core.EX x);
  void visitPCastT(Core.PCastT pCastT);
  void visitEVoid(Core.EVoid eVoid);
  void visitL(Core.L l);
  void visitInfo(Core.L.Info info);
  void visitMWT(Core.L.MWT mwt);
  void visitNC(Core.L.NC nc);
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
  void visitSlash(Half.Slash slash);
  void visitBinOp(Half.BinOp binOp);
  void visitMCall(Half.MCall mCall);
  void visitBlock(Half.Block block);
  void visitLoop(Half.Loop loop);
  void visitThrow(Half.Throw thr);
  void visitOpUpdate(Half.OpUpdate opUpdate);
  void visitD(Half.D d);
  void visitK(Half.K k);
  void visitT(Half.T t);
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