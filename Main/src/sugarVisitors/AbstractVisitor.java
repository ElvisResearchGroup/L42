package sugarVisitors;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import tools.Assertions;
import antlrGenerated.L42Parser.BbContext;
import antlrGenerated.L42Parser.BlockContext;
import antlrGenerated.L42Parser.ClassBContext;
import antlrGenerated.L42Parser.ClassBExtraContext;
import antlrGenerated.L42Parser.ClassBReuseContext;
import antlrGenerated.L42Parser.ConcreteTContext;
import antlrGenerated.L42Parser.CurlyBlockContext;
import antlrGenerated.L42Parser.DContext;
import antlrGenerated.L42Parser.DocsContext;
import antlrGenerated.L42Parser.DocsOptContext;
import antlrGenerated.L42Parser.EAtomContext;
import antlrGenerated.L42Parser.EL1Context;
import antlrGenerated.L42Parser.EL2Context;
import antlrGenerated.L42Parser.EL3Context;
import antlrGenerated.L42Parser.EPostContext;
import antlrGenerated.L42Parser.ETopContext;
import antlrGenerated.L42Parser.ETopForMethodContext;
import antlrGenerated.L42Parser.EUnOpContext;
import antlrGenerated.L42Parser.FieldDecContext;
import antlrGenerated.L42Parser.HeaderContext;
import antlrGenerated.L42Parser.HistoricalSeqContext;
import antlrGenerated.L42Parser.HistoricalTContext;
import antlrGenerated.L42Parser.IContext;
import antlrGenerated.L42Parser.IfExprContext;
import antlrGenerated.L42Parser.KContext;
import antlrGenerated.L42Parser.LoopExprContext;
import antlrGenerated.L42Parser.MCallContext;
import antlrGenerated.L42Parser.MContext;
import antlrGenerated.L42Parser.MDecContext;
import antlrGenerated.L42Parser.MemberContext;
import antlrGenerated.L42Parser.MethSelectorContext;
import antlrGenerated.L42Parser.MethodImplementedContext;
import antlrGenerated.L42Parser.MethodWithTypeContext;
import antlrGenerated.L42Parser.MhsContext;
import antlrGenerated.L42Parser.MhtContext;
import antlrGenerated.L42Parser.NestedClassContext;
import antlrGenerated.L42Parser.NudeEContext;
import antlrGenerated.L42Parser.NumParseContext;
import antlrGenerated.L42Parser.OnContext;
import antlrGenerated.L42Parser.OnPlusContext;
import antlrGenerated.L42Parser.PathContext;
import antlrGenerated.L42Parser.PsContext;
import antlrGenerated.L42Parser.RoundBlockContext;
import antlrGenerated.L42Parser.RoundBlockForMethodContext;
import antlrGenerated.L42Parser.RoundContext;
import antlrGenerated.L42Parser.SignalExprContext;
import antlrGenerated.L42Parser.SquareContext;
import antlrGenerated.L42Parser.SquareWContext;
import antlrGenerated.L42Parser.StringParseContext;
import antlrGenerated.L42Parser.TContext;
import antlrGenerated.L42Parser.UsingContext;
import antlrGenerated.L42Parser.VarDecContext;
import antlrGenerated.L42Parser.WContext;
import antlrGenerated.L42Parser.WSimpleContext;
import antlrGenerated.L42Parser.WSwitchContext;
import antlrGenerated.L42Parser.WhileExprContext;
import antlrGenerated.L42Parser.XContext;
import antlrGenerated.L42Parser.XOpContext;
import antlrGenerated.L42Visitor;

public  class AbstractVisitor<T>  implements L42Visitor<T>{

  @Override public T visit(ParseTree arg0) {throw Assertions.codeNotReachable();}

  @Override public T visitChildren(RuleNode arg0) {throw Assertions.codeNotReachable();}

  @Override public T visitErrorNode(ErrorNode arg0) {throw Assertions.codeNotReachable();}

  @Override public T visitTerminal(TerminalNode arg0) {throw Assertions.codeNotReachable();}

  @Override public T visitPs(PsContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEUnOp(EUnOpContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEL2(EL2Context ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEL1(EL1Context ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitM(MContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEL3(EL3Context ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEPost(EPostContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitPath(PathContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitSquare(SquareContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitDocs(DocsContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMCall(MCallContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitRound(RoundContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitX(XContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitEAtom(EAtomContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitDocsOpt(DocsOptContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitETop(ETopContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitNudeE(NudeEContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitD(DContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitT(TContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitIfExpr(IfExprContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitBlock(BlockContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitVarDec(VarDecContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitCurlyBlock(CurlyBlockContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitRoundBlock(RoundBlockContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitConcreteT(ConcreteTContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMethSelector(MethSelectorContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitHistoricalT(HistoricalTContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitSignalExpr(SignalExprContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitWhileExpr(WhileExprContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitBb(BbContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitOn(OnContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitK(KContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitClassB(ClassBContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitFieldDec(FieldDecContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitHeader(HeaderContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitSquareW(SquareWContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitOnPlus(OnPlusContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMember(MemberContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitI(IContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitWSimple(WSimpleContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitWSwitch(WSwitchContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitW(WContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMht(MhtContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMhs(MhsContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMethodWithType(MethodWithTypeContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitNestedClass(NestedClassContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitMethodImplemented(MethodImplementedContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitUsing(UsingContext ctx) {throw Assertions.codeNotReachable();}

  @Override public T visitStringParse(StringParseContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitClassBExtra(ClassBExtraContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitLoopExpr(LoopExprContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitClassBReuse(ClassBReuseContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitNumParse(NumParseContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitXOp(XOpContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitMDec(MDecContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitHistoricalSeq(HistoricalSeqContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitRoundBlockForMethod(RoundBlockForMethodContext ctx) {throw Assertions.codeNotReachable();}

  @Override
  public T visitETopForMethod(ETopForMethodContext ctx) {throw Assertions.codeNotReachable();}
  
}
