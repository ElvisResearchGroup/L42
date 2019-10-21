// Generated from L42Aux.g4 by ANTLR 4.7.2
package is.L42.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link L42AuxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface L42AuxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#anyKw}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyKw(L42AuxParser.AnyKwContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#voidKw}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidKw(L42AuxParser.VoidKwContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#libraryKw}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryKw(L42AuxParser.LibraryKwContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#thisKw}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisKw(L42AuxParser.ThisKwContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC(L42AuxParser.CContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#csP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCsP(L42AuxParser.CsPContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#nudeCsP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudeCsP(L42AuxParser.NudeCsPContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(L42AuxParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#cs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCs(L42AuxParser.CsContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(L42AuxParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#pathSel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathSel(L42AuxParser.PathSelContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#pathSelX}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathSelX(L42AuxParser.PathSelXContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#nudePathSelX}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudePathSelX(L42AuxParser.NudePathSelXContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#infoNorm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfoNorm(L42AuxParser.InfoNormContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#infoTyped}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfoTyped(L42AuxParser.InfoTypedContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#info}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfo(L42AuxParser.InfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#infoBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfoBody(L42AuxParser.InfoBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#typeDep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDep(L42AuxParser.TypeDepContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#coherentDep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoherentDep(L42AuxParser.CoherentDepContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#watched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWatched(L42AuxParser.WatchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#usedMethods}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsedMethods(L42AuxParser.UsedMethodsContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#hiddenSupertypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHiddenSupertypes(L42AuxParser.HiddenSupertypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#refined}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefined(L42AuxParser.RefinedContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#declaresClassMethods}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaresClassMethods(L42AuxParser.DeclaresClassMethodsContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#nativeKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNativeKind(L42AuxParser.NativeKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#nativePar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNativePar(L42AuxParser.NativeParContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#uniqueId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniqueId(L42AuxParser.UniqueIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#x}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitX(L42AuxParser.XContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#m}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitM(L42AuxParser.MContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#charInDoc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharInDoc(L42AuxParser.CharInDocContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#topDoc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopDoc(L42AuxParser.TopDocContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#topDocText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopDocText(L42AuxParser.TopDocTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link L42AuxParser#doc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoc(L42AuxParser.DocContext ctx);
}