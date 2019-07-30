// Generated from L42.g4 by ANTLR 4.2.2
package is.L42.generated;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link L42Parser}.
 */
public interface L42Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link L42Parser#par}.
	 * @param ctx the parse tree
	 */
	void enterPar(@NotNull L42Parser.ParContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#par}.
	 * @param ctx the parse tree
	 */
	void exitPar(@NotNull L42Parser.ParContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#oR}.
	 * @param ctx the parse tree
	 */
	void enterOR(@NotNull L42Parser.ORContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#oR}.
	 * @param ctx the parse tree
	 */
	void exitOR(@NotNull L42Parser.ORContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(@NotNull L42Parser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(@NotNull L42Parser.StringContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#d}.
	 * @param ctx the parse tree
	 */
	void enterD(@NotNull L42Parser.DContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#d}.
	 * @param ctx the parse tree
	 */
	void exitD(@NotNull L42Parser.DContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#e}.
	 * @param ctx the parse tree
	 */
	void enterE(@NotNull L42Parser.EContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#e}.
	 * @param ctx the parse tree
	 */
	void exitE(@NotNull L42Parser.EContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#eAtomic}.
	 * @param ctx the parse tree
	 */
	void enterEAtomic(@NotNull L42Parser.EAtomicContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#eAtomic}.
	 * @param ctx the parse tree
	 */
	void exitEAtomic(@NotNull L42Parser.EAtomicContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#x}.
	 * @param ctx the parse tree
	 */
	void enterX(@NotNull L42Parser.XContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#x}.
	 * @param ctx the parse tree
	 */
	void exitX(@NotNull L42Parser.XContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#fCall}.
	 * @param ctx the parse tree
	 */
	void enterFCall(@NotNull L42Parser.FCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#fCall}.
	 * @param ctx the parse tree
	 */
	void exitFCall(@NotNull L42Parser.FCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#nudeE}.
	 * @param ctx the parse tree
	 */
	void enterNudeE(@NotNull L42Parser.NudeEContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#nudeE}.
	 * @param ctx the parse tree
	 */
	void exitNudeE(@NotNull L42Parser.NudeEContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(@NotNull L42Parser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(@NotNull L42Parser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link L42Parser#m}.
	 * @param ctx the parse tree
	 */
	void enterM(@NotNull L42Parser.MContext ctx);
	/**
	 * Exit a parse tree produced by {@link L42Parser#m}.
	 * @param ctx the parse tree
	 */
	void exitM(@NotNull L42Parser.MContext ctx);
}