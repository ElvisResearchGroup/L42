// Generated from L42.g4 by ANTLR 4.2.2
package is.L42.generated;
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
	 * Visit a parse tree produced by {@link L42Parser#par}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar(@NotNull L42Parser.ParContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#oR}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOR(@NotNull L42Parser.ORContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(@NotNull L42Parser.StringContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#d}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitD(@NotNull L42Parser.DContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitE(@NotNull L42Parser.EContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#eAtomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEAtomic(@NotNull L42Parser.EAtomicContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#x}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitX(@NotNull L42Parser.XContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#fCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFCall(@NotNull L42Parser.FCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#nudeE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudeE(@NotNull L42Parser.NudeEContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull L42Parser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link L42Parser#m}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitM(@NotNull L42Parser.MContext ctx);
}