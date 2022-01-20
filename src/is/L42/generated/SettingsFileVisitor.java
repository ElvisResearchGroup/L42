// Generated from SettingsFile.g4 by ANTLR 4.7.2
package is.L42.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SettingsFileParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SettingsFileVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SettingsFileParser#memOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemOpt(SettingsFileParser.MemOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link SettingsFileParser#secOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecOpt(SettingsFileParser.SecOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link SettingsFileParser#setting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetting(SettingsFileParser.SettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SettingsFileParser#nudeSettings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNudeSettings(SettingsFileParser.NudeSettingsContext ctx);
}