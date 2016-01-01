// Generated from L42.g4 by ANTLR 4.2.2
package antlrGenerated;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link L42Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface L42Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link L42Parser#concreteT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcreteT(@NotNull L42Parser.ConcreteTContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#ps}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPs(@NotNull L42Parser.PsContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#squareW}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSquareW(@NotNull L42Parser.SquareWContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#onPlus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnPlus(@NotNull L42Parser.OnPlusContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#fieldDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDec(@NotNull L42Parser.FieldDecContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eL2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEL2(@NotNull L42Parser.EL2Context ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eL1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEL1(@NotNull L42Parser.EL1Context ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#classBReuse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBReuse(@NotNull L42Parser.ClassBReuseContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eL3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEL3(@NotNull L42Parser.EL3Context ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#ePost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEPost(@NotNull L42Parser.EPostContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(@NotNull L42Parser.PathContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#signalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalExpr(@NotNull L42Parser.SignalExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#docs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocs(@NotNull L42Parser.DocsContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull L42Parser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eTop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitETop(@NotNull L42Parser.ETopContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#nestedClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedClass(@NotNull L42Parser.NestedClassContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#using}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing(@NotNull L42Parser.UsingContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#curlyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurlyBlock(@NotNull L42Parser.CurlyBlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#mxRound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMxRound(@NotNull L42Parser.MxRoundContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#wSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWSimple(@NotNull L42Parser.WSimpleContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#roundBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoundBlock(@NotNull L42Parser.RoundBlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#loopExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopExpr(@NotNull L42Parser.LoopExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#square}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSquare(@NotNull L42Parser.SquareContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#mht}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMht(@NotNull L42Parser.MhtContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#mhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMhs(@NotNull L42Parser.MhsContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(@NotNull L42Parser.HeaderContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#varDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDec(@NotNull L42Parser.VarDecContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#stringParse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringParse(@NotNull L42Parser.StringParseContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#bb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBb(@NotNull L42Parser.BbContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#historicalSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistoricalSeq(@NotNull L42Parser.HistoricalSeqContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#numParse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumParse(@NotNull L42Parser.NumParseContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#methodWithType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodWithType(@NotNull L42Parser.MethodWithTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#classB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassB(@NotNull L42Parser.ClassBContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(@NotNull L42Parser.MemberContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#methSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethSelector(@NotNull L42Parser.MethSelectorContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#ifExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(@NotNull L42Parser.IfExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#on}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn(@NotNull L42Parser.OnContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#d}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitD(@NotNull L42Parser.DContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#roundBlockForMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoundBlockForMethod(@NotNull L42Parser.RoundBlockForMethodContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#mDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMDec(@NotNull L42Parser.MDecContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eUnOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEUnOp(@NotNull L42Parser.EUnOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#i}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitI(@NotNull L42Parser.IContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#historicalT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistoricalT(@NotNull L42Parser.HistoricalTContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eTopForMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitETopForMethod(@NotNull L42Parser.ETopForMethodContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#k}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitK(@NotNull L42Parser.KContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#m}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitM(@NotNull L42Parser.MContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#wSwitch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWSwitch(@NotNull L42Parser.WSwitchContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#t}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitT(@NotNull L42Parser.TContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#mCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMCall(@NotNull L42Parser.MCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#round}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRound(@NotNull L42Parser.RoundContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#whileExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileExpr(@NotNull L42Parser.WhileExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#classBExtra}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBExtra(@NotNull L42Parser.ClassBExtraContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#w}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitW(@NotNull L42Parser.WContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#x}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitX(@NotNull L42Parser.XContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#xOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXOp(@NotNull L42Parser.XOpContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEAtom(@NotNull L42Parser.EAtomContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#docsOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocsOpt(@NotNull L42Parser.DocsOptContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#nudeE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudeE(@NotNull L42Parser.NudeEContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#methodImplemented}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodImplemented(@NotNull L42Parser.MethodImplementedContext ctx);
}