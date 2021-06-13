package is.L42.visitors;
import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.*;

public class UndefinedCollectorVisitor implements CollectorVisitor{
  public static class UndefinedCase extends RuntimeException{}
  protected static final void uc(){throw new UndefinedCase();}
  @Override public void visitC(C c){uc();}
  @Override public void visitP(P p){uc();}
  @Override public void visitS(S s){uc();}
  @Override public void visitX(X x){uc();}
  @Override public void visitSTMeth(ST.STMeth stMeth){uc();}
  @Override public void visitSTOp(ST.STOp stOp){uc();}
  @Override public void visitEX(Core.EX x){uc();}
  @Override public void visitPCastT(Core.PCastT pCastT){uc();}
  @Override public void visitEVoid(Core.EVoid eVoid){uc();}
  @Override public void visitL(Core.L l){uc();}
  @Override public void visitInfo(Core.L.Info info){uc();}
  @Override public void visitMWT(Core.L.MWT mwt){uc();}
  @Override public void visitNC(Core.L.NC nc){uc();}
  @Override public void visitMCall(Core.MCall mCall){uc();}
  @Override public void visitBlock(Core.Block block){uc();}
  @Override public void visitLoop(Core.Loop loop){uc();}
  @Override public void visitThrow(Core.Throw thr){uc();}
  @Override public void visitOpUpdate(Core.OpUpdate opUpdate){uc();}
  @Override public void visitD(Core.D d){uc();}
  @Override public void visitK(Core.K k){uc();}
  @Override public void visitT(Core.T t){uc();}
  @Override public void visitDoc(Core.Doc doc){uc();}
  @Override public void visitPathSel(Core.PathSel pathSel){uc();}
  @Override public void visitMH(Core.MH mh){uc();}
  @Override public void visitPCastT(Half.PCastT pCastT){uc();}
  @Override public void visitSlashCastT(Half.SlashCastT slash){uc();}
  @Override public void visitBinOp(Half.BinOp binOp){uc();}
  @Override public void visitMCall(Half.MCall mCall){uc();}
  @Override public void visitBlock(Half.Block block){uc();}
  @Override public void visitLoop(Half.Loop loop){uc();}
  @Override public void visitThrow(Half.Throw thr){uc();}
  @Override public void visitOpUpdate(Half.OpUpdate opUpdate){uc();}
  @Override public void visitD(Half.D d){uc();}
  @Override public void visitK(Half.K k){uc();}
  @Override public void visitCsP(Full.CsP csP){uc();}
  @Override public void visitL(Full.L l){uc();}
  @Override public void visitF(Full.L.F f){uc();}
  @Override public void visitMI(Full.L.MI mi){uc();}
  @Override public void visitMWT(Full.L.MWT mwt){uc();}
  @Override public void visitNC(Full.L.NC nc){uc();}
  @Override public void visitSlash(Full.Slash slash){uc();}
  @Override public void visitSlashX(Full.SlashX slashX){uc();}
  @Override public void visitEString(Full.EString eString){uc();}
  @Override public void visitEPathSel(Full.EPathSel ePathSel){uc();}
  @Override public void visitUOp(Full.UOp uOp){uc();}
  @Override public void visitBinOp(Full.BinOp binOp){uc();}
  @Override public void visitCast(Full.Cast cast){uc();}
  @Override public void visitCall(Full.Call call){uc();}
  @Override public void visitBlock(Full.Block block){uc();}
  @Override public void visitLoop(Full.Loop loop){uc();}
  @Override public void visitWhile(Full.While sWhile){uc();}
  @Override public void visitFor(Full.For sFor){uc();}
  @Override public void visitThrow(Full.Throw thr){uc();}
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){uc();}
  @Override public void visitIf(Full.If sIf){uc();}
  @Override public void visitD(Full.D d){uc();}
  @Override public void visitVarTx(Full.VarTx varTx){uc();}
  @Override public void visitK(Full.K k){uc();}
  @Override public void visitPar(Full.Par par){uc();}
  @Override public void visitT(Full.T t){uc();}
  @Override public void visitDoc(Full.Doc doc){uc();}
  @Override public void visitPathSel(Full.PathSel pathSel){uc();}
  @Override public void visitMH(Full.MH mh){uc();}
  @Override public void visitProgram(Program program){uc();}
  @Override public void visitPTails(PTails pTails){uc();}
  }