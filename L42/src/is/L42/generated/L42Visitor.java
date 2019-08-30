// Generated from L42.g4 by ANTLR 4.7.2
package is.L42.generated;
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
	 * Visit a parse tree produced by {@link L42Parser#slash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlash(L42Parser.SlashContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#pathSel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathSel(L42Parser.PathSelContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(L42Parser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#x}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitX(L42Parser.XContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#slashX}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlashX(L42Parser.SlashXContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#m}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitM(L42Parser.MContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#doc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoc(L42Parser.DocContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#csP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCsP(L42Parser.CsPContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#t}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitT(L42Parser.TContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#tLocal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTLocal(L42Parser.TLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#eAtomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEAtomic(L42Parser.EAtomicContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullL}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullL(L42Parser.FullLContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullM}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullM(L42Parser.FullMContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullF}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullF(L42Parser.FullFContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullMi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMi(L42Parser.FullMiContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullMWT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMWT(L42Parser.FullMWTContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullNC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullNC(L42Parser.FullNCContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(L42Parser.HeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#info}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfo(L42Parser.InfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fullMH}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMH(L42Parser.FullMHContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#mOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMOp(L42Parser.MOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#voidE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidE(L42Parser.VoidEContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#ePostfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEPostfix(L42Parser.EPostfixContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#fCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFCall(L42Parser.FCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#squareCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSquareCall(L42Parser.SquareCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#cast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast(L42Parser.CastContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#oR}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOR(L42Parser.ORContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#par}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar(L42Parser.ParContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(L42Parser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#d}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitD(L42Parser.DContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#dX}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDX(L42Parser.DXContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#k}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitK(L42Parser.KContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#whoops}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhoops(L42Parser.WhoopsContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#eBinary0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEBinary0(L42Parser.EBinary0Context ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#eBinary1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEBinary1(L42Parser.EBinary1Context ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#eBinary2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEBinary2(L42Parser.EBinary2Context ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#eBinary3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEBinary3(L42Parser.EBinary3Context ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSIf(L42Parser.SIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(L42Parser.MatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sWhile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSWhile(L42Parser.SWhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSFor(L42Parser.SForContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSLoop(L42Parser.SLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sThrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSThrow(L42Parser.SThrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#sUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSUpdate(L42Parser.SUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitE(L42Parser.EContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42Parser#nudeE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudeE(L42Parser.NudeEContext ctx);
}