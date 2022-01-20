// Generated from SettingsFile.g4 by ANTLR 4.7.2
package is.L42.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SettingsFileParser}.
 */
public interface SettingsFileListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SettingsFileParser#memOpt}.
	 * @param ctx the parse tree
	 */
	void enterMemOpt(SettingsFileParser.MemOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link SettingsFileParser#memOpt}.
	 * @param ctx the parse tree
	 */
	void exitMemOpt(SettingsFileParser.MemOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link SettingsFileParser#secOpt}.
	 * @param ctx the parse tree
	 */
	void enterSecOpt(SettingsFileParser.SecOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link SettingsFileParser#secOpt}.
	 * @param ctx the parse tree
	 */
	void exitSecOpt(SettingsFileParser.SecOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link SettingsFileParser#setting}.
	 * @param ctx the parse tree
	 */
	void enterSetting(SettingsFileParser.SettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SettingsFileParser#setting}.
	 * @param ctx the parse tree
	 */
	void exitSetting(SettingsFileParser.SettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SettingsFileParser#nudeSettings}.
	 * @param ctx the parse tree
	 */
	void enterNudeSettings(SettingsFileParser.NudeSettingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SettingsFileParser#nudeSettings}.
	 * @param ctx the parse tree
	 */
	void exitNudeSettings(SettingsFileParser.NudeSettingsContext ctx);
}