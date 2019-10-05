package is.L42.visitors;
import java.util.List;

import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.*;

public class UndefinedCollectorVisitor implements CollectorVisitor{
  @SuppressWarnings("serial") public static class UndefinedCase extends RuntimeException{}
  protected static final UndefinedCase uc=new UndefinedCase();
  @Override public void visitC(C c){throw uc;}
  @Override public void visitP(P p){throw uc;}
  @Override public void visitS(S s){throw uc;}
  @Override public void visitX(X x){throw uc;}
  @Override public void visitSTMeth(ST.STMeth stMeth){throw uc;}
  @Override public void visitSTOp(ST.STOp stOp){throw uc;}
  @Override public void visitEX(Core.EX x){throw uc;}
  @Override public void visitPCastT(Core.PCastT pCastT){throw uc;}
  @Override public void visitEVoid(Core.EVoid eVoid){throw uc;}
  @Override public void visitL(Core.L l){throw uc;}
  @Override public void visitInfo(Core.L.Info info){throw uc;}
  @Override public void visitMWT(Core.L.MWT mwt){throw uc;}
  @Override public void visitNC(Core.L.NC nc){throw uc;}
  @Override public void visitMCall(Core.MCall mCall){throw uc;}
  @Override public void visitBlock(Core.Block block){throw uc;}
  @Override public void visitLoop(Core.Loop loop){throw uc;}
  @Override public void visitThrow(Core.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Core.OpUpdate opUpdate){throw uc;}
  @Override public void visitD(Core.D d){throw uc;}
  @Override public void visitK(Core.K k){throw uc;}
  @Override public void visitT(Core.T t){throw uc;}
  @Override public void visitDoc(Core.Doc doc){throw uc;}
  @Override public void visitPathSel(Core.PathSel pathSel){throw uc;}
  @Override public void visitMH(Core.MH mh){throw uc;}
  @Override public void visitPCastT(Half.PCastT pCastT){throw uc;}
  @Override public void visitSlashCastT(Half.SlashCastT slash){throw uc;}
  @Override public void visitBinOp(Half.BinOp binOp){throw uc;}
  @Override public void visitMCall(Half.MCall mCall){throw uc;}
  @Override public void visitBlock(Half.Block block){throw uc;}
  @Override public void visitLoop(Half.Loop loop){throw uc;}
  @Override public void visitThrow(Half.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Half.OpUpdate opUpdate){throw uc;}
  @Override public void visitD(Half.D d){throw uc;}
  @Override public void visitK(Half.K k){throw uc;}
  @Override public void visitCsP(Full.CsP csP){throw uc;}
  @Override public void visitL(Full.L l){throw uc;}
  @Override public void visitF(Full.L.F f){throw uc;}
  @Override public void visitMI(Full.L.MI mi){throw uc;}
  @Override public void visitMWT(Full.L.MWT mwt){throw uc;}
  @Override public void visitNC(Full.L.NC nc){throw uc;}
  @Override public void visitSlash(Full.Slash slash){throw uc;}
  @Override public void visitSlashX(Full.SlashX slashX){throw uc;}
  @Override public void visitEString(Full.EString eString){throw uc;}
  @Override public void visitEPathSel(Full.EPathSel ePathSel){throw uc;}
  @Override public void visitUOp(Full.UOp uOp){throw uc;}
  @Override public void visitBinOp(Full.BinOp binOp){throw uc;}
  @Override public void visitCast(Full.Cast cast){throw uc;}
  @Override public void visitCall(Full.Call call){throw uc;}
  @Override public void visitBlock(Full.Block block){throw uc;}
  @Override public void visitLoop(Full.Loop loop){throw uc;}
  @Override public void visitWhile(Full.While sWhile){throw uc;}
  @Override public void visitFor(Full.For sFor){throw uc;}
  @Override public void visitThrow(Full.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Full.OpUpdate opUpdate){throw uc;}
  @Override public void visitIf(Full.If sIf){throw uc;}
  @Override public void visitD(Full.D d){throw uc;}
  @Override public void visitVarTx(Full.VarTx varTx){throw uc;}
  @Override public void visitK(Full.K k){throw uc;}
  @Override public void visitPar(Full.Par par){throw uc;}
  @Override public void visitT(Full.T t){throw uc;}
  @Override public void visitDoc(Full.Doc doc){throw uc;}
  @Override public void visitPathSel(Full.PathSel pathSel){throw uc;}
  @Override public void visitMH(Full.MH mh){throw uc;}
  @Override public void visitProgram(Program program){throw uc;}
  @Override public void visitPTails(PTails pTails){throw uc;}
  }