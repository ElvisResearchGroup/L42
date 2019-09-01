package is.L42.visitors;
import java.util.List;
import is.L42.generated.*;

public class UndefinedCollectorVisitor implements CollectorVisitor{
  @SuppressWarnings("serial") public static class UndefinedCase extends RuntimeException{}
  private static final UndefinedCase uc=new UndefinedCase();
  public void visitC(C c){throw uc;}
  public void visitP(P p){throw uc;}
  public void visitS(S s){throw uc;}
  public void visitX(X x){throw uc;}
  public void visitSTMeth(ST.STMeth stMeth){throw uc;}
  public void visitSTOp(ST.STOp stOp){throw uc;}
  public void visitEX(Core.EX x){throw uc;}
  public void visitPCastT(Core.PCastT pCastT){throw uc;}
  public void visitEVoid(Core.EVoid eVoid){throw uc;}
  public void visitL(Core.L l){throw uc;}
  public void visitInfo(Core.L.Info info){throw uc;}
  public void visitMWT(Core.L.MWT mwt){throw uc;}
  public void visitNC(Core.L.NC nc){throw uc;}
  public void visitMCall(Core.MCall mCall){throw uc;}
  public void visitBlock(Core.Block block){throw uc;}
  public void visitLoop(Core.Loop loop){throw uc;}
  public void visitThrow(Core.Throw thr){throw uc;}
  public void visitOpUpdate(Core.OpUpdate opUpdate){throw uc;}
  public void visitD(Core.D d){throw uc;}
  public void visitK(Core.K k){throw uc;}
  public void visitT(Core.T t){throw uc;}
  public void visitDoc(Core.Doc doc){throw uc;}
  public void visitPathSel(Core.PathSel pathSel){throw uc;}
  public void visitMH(Core.MH mh){throw uc;}
  public void visitPCastT(Half.PCastT pCastT){throw uc;}
  public void visitSlash(Half.Slash slash){throw uc;}
  public void visitBinOp(Half.BinOp binOp){throw uc;}
  public void visitMCall(Half.MCall mCall){throw uc;}
  public void visitBlock(Half.Block block){throw uc;}
  public void visitLoop(Half.Loop loop){throw uc;}
  public void visitThrow(Half.Throw thr){throw uc;}
  public void visitOpUpdate(Half.OpUpdate opUpdate){throw uc;}
  public void visitD(Half.D d){throw uc;}
  public void visitK(Half.K k){throw uc;}
  public void visitT(Half.T t){throw uc;}
  public void visitCsP(Full.CsP csP){throw uc;}
  public void visitL(Full.L l){throw uc;}
  public void visitF(Full.L.F f){throw uc;}
  public void visitMI(Full.L.MI mi){throw uc;}
  public void visitMWT(Full.L.MWT mwt){throw uc;}
  public void visitNC(Full.L.NC nc){throw uc;}
  public void visitSlash(Full.Slash slash){throw uc;}
  public void visitSlashX(Full.SlashX slashX){throw uc;}
  public void visitEString(Full.EString eString){throw uc;}
  public void visitEPathSel(Full.EPathSel ePathSel){throw uc;}
  public void visitUOp(Full.UOp uOp){throw uc;}
  public void visitBinOp(Full.BinOp binOp){throw uc;}
  public void visitCast(Full.Cast cast){throw uc;}
  public void visitCall(Full.Call call){throw uc;}
  public void visitBlock(Full.Block block){throw uc;}
  public void visitLoop(Full.Loop loop){throw uc;}
  public void visitWhile(Full.While sWhile){throw uc;}
  public void visitFor(Full.For sFor){throw uc;}
  public void visitThrow(Full.Throw thr){throw uc;}
  public void visitOpUpdate(Full.OpUpdate opUpdate){throw uc;}
  public void visitIf(Full.If sIf){throw uc;}
  public void visitD(Full.D d){throw uc;}
  public void visitVarTx(Full.VarTx varTx){throw uc;}
  public void visitK(Full.K k){throw uc;}
  public void visitPar(Full.Par par){throw uc;}
  public void visitT(Full.T t){throw uc;}
  public void visitDoc(Full.Doc doc){throw uc;}
  public void visitPathSel(Full.PathSel pathSel){throw uc;}
  public void visitMH(Full.MH mh){throw uc;}
  }