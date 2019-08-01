// Generated from L42.g4 by ANTLR 4.7.2
package is.L42.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class L42Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, Mdf=4, VoidKW=5, VarKw=6, CatchKw=7, Throw=8, 
		WhoopsKw=9, StringSingle=10, Number=11, MUniqueNum=12, MHash=13, X=14, 
		CsP=15, ClassSep=16, UnderScore=17, OR=18, ORNS=19, Doc=20, BlockComment=21, 
		LineComment=22, Whitespace=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "Mdf", "VoidKW", "VarKw", "CatchKw", "Throw", 
			"WhoopsKw", "IdUp", "IdLow", "IdChar", "CHAR", "CHARInStringSingle", 
			"CharsUrl", "CHARDocText", "URL", "Fn", "Fx", "StringSingle", "Number", 
			"MUniqueNum", "MHash", "X", "C", "CsP", "ClassSep", "UnderScore", "OR", 
			"ORNS", "Doc", "FS", "FParXs", "FPathLit", "DocText", "BlockComment", 
			"LineComment", "Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\'", "')'", "'='", null, "'void'", "'var'", "'catch'", null, 
			"'whoops'", null, null, null, null, null, null, "'.'", "'_'", null, "'('"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "Mdf", "VoidKW", "VarKw", "CatchKw", "Throw", 
			"WhoopsKw", "StringSingle", "Number", "MUniqueNum", "MHash", "X", "CsP", 
			"ClassSep", "UnderScore", "OR", "ORNS", "Doc", "BlockComment", "LineComment", 
			"Whitespace"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public L42Lexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "L42.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u0191\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\3\3\3\3\4\3"+
		"\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\5\5~\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u00a3\n\t\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\13\7\13\u00ad\n\13\f\13\16\13\u00b0\13\13\3\13\3\13\3\f\7\f\u00b5"+
		"\n\f\f\f\16\f\u00b8\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\6\22\u00c7\n\22\r\22\16\22\u00c8\3\23\3\23\3\23\7\23\u00ce"+
		"\n\23\f\23\16\23\u00d1\13\23\5\23\u00d3\n\23\3\24\3\24\7\24\u00d7\n\24"+
		"\f\24\16\24\u00da\13\24\3\25\3\25\3\25\3\25\3\26\3\26\7\26\u00e2\n\26"+
		"\f\26\16\26\u00e5\13\26\3\27\3\27\3\27\7\27\u00ea\n\27\f\27\16\27\u00ed"+
		"\13\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\5\30\u00f6\n\30\3\30\3\30\3"+
		"\30\7\30\u00fb\n\30\f\30\16\30\u00fe\13\30\3\30\3\30\3\30\3\30\5\30\u0104"+
		"\n\30\3\31\3\31\3\32\3\32\7\32\u010a\n\32\f\32\16\32\u010d\13\32\3\33"+
		"\3\33\3\33\3\33\7\33\u0113\n\33\f\33\16\33\u0116\13\33\3\34\3\34\3\35"+
		"\3\35\3\36\3\36\3\36\3\36\3\36\3\36\5\36\u0122\n\36\3\37\3\37\3 \3 \3"+
		" \3 \5 \u012a\n \3 \3 \3 \3 \5 \u0130\n \3!\3!\3!\5!\u0135\n!\3!\3!\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\7\"\u013f\n\"\f\"\16\"\u0142\13\"\3\"\3\"\5\"\u0146"+
		"\n\"\3#\3#\3#\5#\u014b\n#\3#\3#\3#\5#\u0150\n#\3#\3#\3#\3#\5#\u0156\n"+
		"#\3#\3#\5#\u015a\n#\5#\u015c\n#\5#\u015e\n#\3$\3$\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3$\5$\u016c\n$\3%\3%\3%\3%\3%\7%\u0173\n%\f%\16%\u0176\13%\3%"+
		"\3%\3%\3%\3%\3&\3&\3&\3&\7&\u0181\n&\f&\16&\u0184\13&\3&\5&\u0187\n&\3"+
		"&\3&\3\'\6\'\u018c\n\'\r\'\16\'\u018d\3\'\3\'\4\u0174\u0182\2(\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2"+
		"%\2\'\2)\f+\r-\16/\17\61\20\63\2\65\21\67\229\23;\24=\25?\26A\2C\2E\2"+
		"G\2I\27K\30M\31\3\2\r\4\2&&C\\\7\2&&\62;C\\aac|\4\2\f\f\"\u0080\4\2\""+
		"#%\u0080\7\2\"#%(*|~~\u0080\u0080\7\2\f\f\"AC|~~\u0080\u0080\5\2/\60\62"+
		";aa\6\2&&\62;C\\c|\4\2\"\"..\3\3\f\f\5\2\f\f\"\"..\2\u01ad\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2"+
		"\2\2\2\61\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5Q\3\2\2"+
		"\2\7S\3\2\2\2\t}\3\2\2\2\13\177\3\2\2\2\r\u0084\3\2\2\2\17\u0088\3\2\2"+
		"\2\21\u00a2\3\2\2\2\23\u00a4\3\2\2\2\25\u00ae\3\2\2\2\27\u00b6\3\2\2\2"+
		"\31\u00bb\3\2\2\2\33\u00bd\3\2\2\2\35\u00bf\3\2\2\2\37\u00c1\3\2\2\2!"+
		"\u00c3\3\2\2\2#\u00c6\3\2\2\2%\u00d2\3\2\2\2\'\u00d4\3\2\2\2)\u00db\3"+
		"\2\2\2+\u00df\3\2\2\2-\u00e6\3\2\2\2/\u00f3\3\2\2\2\61\u0105\3\2\2\2\63"+
		"\u0107\3\2\2\2\65\u010e\3\2\2\2\67\u0117\3\2\2\29\u0119\3\2\2\2;\u0121"+
		"\3\2\2\2=\u0123\3\2\2\2?\u012f\3\2\2\2A\u0134\3\2\2\2C\u0145\3\2\2\2E"+
		"\u015d\3\2\2\2G\u016b\3\2\2\2I\u016d\3\2\2\2K\u017c\3\2\2\2M\u018b\3\2"+
		"\2\2OP\7^\2\2P\4\3\2\2\2QR\7+\2\2R\6\3\2\2\2ST\7?\2\2T\b\3\2\2\2UV\7h"+
		"\2\2VW\7y\2\2WX\7f\2\2XY\7\"\2\2YZ\7o\2\2Z[\7w\2\2[~\7v\2\2\\]\7h\2\2"+
		"]^\7y\2\2^_\7f\2\2_`\7\"\2\2`a\7k\2\2ab\7o\2\2b~\7o\2\2cd\7k\2\2de\7o"+
		"\2\2e~\7o\2\2fg\7o\2\2gh\7w\2\2h~\7v\2\2ij\7n\2\2jk\7g\2\2kl\7p\2\2l~"+
		"\7v\2\2mn\7t\2\2no\7g\2\2op\7c\2\2p~\7f\2\2qr\7e\2\2rs\7c\2\2st\7r\2\2"+
		"tu\7u\2\2uv\7w\2\2vw\7n\2\2w~\7g\2\2xy\7e\2\2yz\7n\2\2z{\7c\2\2{|\7u\2"+
		"\2|~\7u\2\2}U\3\2\2\2}\\\3\2\2\2}c\3\2\2\2}f\3\2\2\2}i\3\2\2\2}m\3\2\2"+
		"\2}q\3\2\2\2}x\3\2\2\2~\n\3\2\2\2\177\u0080\7x\2\2\u0080\u0081\7q\2\2"+
		"\u0081\u0082\7k\2\2\u0082\u0083\7f\2\2\u0083\f\3\2\2\2\u0084\u0085\7x"+
		"\2\2\u0085\u0086\7c\2\2\u0086\u0087\7t\2\2\u0087\16\3\2\2\2\u0088\u0089"+
		"\7e\2\2\u0089\u008a\7c\2\2\u008a\u008b\7v\2\2\u008b\u008c\7e\2\2\u008c"+
		"\u008d\7j\2\2\u008d\20\3\2\2\2\u008e\u008f\7t\2\2\u008f\u0090\7g\2\2\u0090"+
		"\u0091\7v\2\2\u0091\u0092\7w\2\2\u0092\u0093\7t\2\2\u0093\u00a3\7p\2\2"+
		"\u0094\u0095\7g\2\2\u0095\u0096\7t\2\2\u0096\u0097\7t\2\2\u0097\u0098"+
		"\7q\2\2\u0098\u00a3\7t\2\2\u0099\u009a\7g\2\2\u009a\u009b\7z\2\2\u009b"+
		"\u009c\7e\2\2\u009c\u009d\7g\2\2\u009d\u009e\7r\2\2\u009e\u009f\7v\2\2"+
		"\u009f\u00a0\7k\2\2\u00a0\u00a1\7q\2\2\u00a1\u00a3\7p\2\2\u00a2\u008e"+
		"\3\2\2\2\u00a2\u0094\3\2\2\2\u00a2\u0099\3\2\2\2\u00a3\22\3\2\2\2\u00a4"+
		"\u00a5\7y\2\2\u00a5\u00a6\7j\2\2\u00a6\u00a7\7q\2\2\u00a7\u00a8\7q\2\2"+
		"\u00a8\u00a9\7r\2\2\u00a9\u00aa\7u\2\2\u00aa\24\3\2\2\2\u00ab\u00ad\7"+
		"a\2\2\u00ac\u00ab\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae"+
		"\u00af\3\2\2\2\u00af\u00b1\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b2\t\2"+
		"\2\2\u00b2\26\3\2\2\2\u00b3\u00b5\7a\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8"+
		"\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b9\3\2\2\2\u00b8"+
		"\u00b6\3\2\2\2\u00b9\u00ba\4c|\2\u00ba\30\3\2\2\2\u00bb\u00bc\t\3\2\2"+
		"\u00bc\32\3\2\2\2\u00bd\u00be\t\4\2\2\u00be\34\3\2\2\2\u00bf\u00c0\t\5"+
		"\2\2\u00c0\36\3\2\2\2\u00c1\u00c2\t\6\2\2\u00c2 \3\2\2\2\u00c3\u00c4\t"+
		"\7\2\2\u00c4\"\3\2\2\2\u00c5\u00c7\5\37\20\2\u00c6\u00c5\3\2\2\2\u00c7"+
		"\u00c8\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9$\3\2\2\2"+
		"\u00ca\u00d3\7\62\2\2\u00cb\u00cf\4\63;\2\u00cc\u00ce\4\62;\2\u00cd\u00cc"+
		"\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0"+
		"\u00d3\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d2\u00ca\3\2\2\2\u00d2\u00cb\3\2"+
		"\2\2\u00d3&\3\2\2\2\u00d4\u00d8\5\27\f\2\u00d5\u00d7\5\31\r\2\u00d6\u00d5"+
		"\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"(\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\7$\2\2\u00dc\u00dd\5\35\17\2"+
		"\u00dd\u00de\7$\2\2\u00de*\3\2\2\2\u00df\u00e3\4\62;\2\u00e0\u00e2\t\b"+
		"\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3"+
		"\u00e4\3\2\2\2\u00e4,\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00eb\5\'\24\2"+
		"\u00e7\u00e8\7%\2\2\u00e8\u00ea\5\'\24\2\u00e9\u00e7\3\2\2\2\u00ea\u00ed"+
		"\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ee\3\2\2\2\u00ed"+
		"\u00eb\3\2\2\2\u00ee\u00ef\7<\2\2\u00ef\u00f0\7<\2\2\u00f0\u00f1\3\2\2"+
		"\2\u00f1\u00f2\5%\23\2\u00f2.\3\2\2\2\u00f3\u00f5\7%\2\2\u00f4\u00f6\7"+
		"&\2\2\u00f5\u00f4\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7"+
		"\u00fc\5\'\24\2\u00f8\u00f9\7%\2\2\u00f9\u00fb\5\'\24\2\u00fa\u00f8\3"+
		"\2\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"\u0103\3\2\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0100\7<\2\2\u0100\u0101\7<\2"+
		"\2\u0101\u0102\3\2\2\2\u0102\u0104\5%\23\2\u0103\u00ff\3\2\2\2\u0103\u0104"+
		"\3\2\2\2\u0104\60\3\2\2\2\u0105\u0106\5\'\24\2\u0106\62\3\2\2\2\u0107"+
		"\u010b\5\25\13\2\u0108\u010a\t\t\2\2\u0109\u0108\3\2\2\2\u010a\u010d\3"+
		"\2\2\2\u010b\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\64\3\2\2\2\u010d"+
		"\u010b\3\2\2\2\u010e\u0114\5\63\32\2\u010f\u0110\5\67\34\2\u0110\u0111"+
		"\5\63\32\2\u0111\u0113\3\2\2\2\u0112\u010f\3\2\2\2\u0113\u0116\3\2\2\2"+
		"\u0114\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115\66\3\2\2\2\u0116\u0114"+
		"\3\2\2\2\u0117\u0118\7\60\2\2\u01188\3\2\2\2\u0119\u011a\7a\2\2\u011a"+
		":\3\2\2\2\u011b\u011c\7\"\2\2\u011c\u0122\7*\2\2\u011d\u011e\7.\2\2\u011e"+
		"\u0122\7*\2\2\u011f\u0120\7\f\2\2\u0120\u0122\7*\2\2\u0121\u011b\3\2\2"+
		"\2\u0121\u011d\3\2\2\2\u0121\u011f\3\2\2\2\u0122<\3\2\2\2\u0123\u0124"+
		"\7*\2\2\u0124>\3\2\2\2\u0125\u0126\7B\2\2\u0126\u0130\5E#\2\u0127\u0129"+
		"\7B\2\2\u0128\u012a\5E#\2\u0129\u0128\3\2\2\2\u0129\u012a\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012c\7}\2\2\u012c\u012d\5G$\2\u012d\u012e\7\177"+
		"\2\2\u012e\u0130\3\2\2\2\u012f\u0125\3\2\2\2\u012f\u0127\3\2\2\2\u0130"+
		"@\3\2\2\2\u0131\u0135\5-\27\2\u0132\u0135\5/\30\2\u0133\u0135\5\61\31"+
		"\2\u0134\u0131\3\2\2\2\u0134\u0132\3\2\2\2\u0134\u0133\3\2\2\2\u0135\u0136"+
		"\3\2\2\2\u0136\u0137\5C\"\2\u0137B\3\2\2\2\u0138\u0139\7*\2\2\u0139\u0146"+
		"\7+\2\2\u013a\u013b\7*\2\2\u013b\u0140\5\61\31\2\u013c\u013d\t\n\2\2\u013d"+
		"\u013f\5\61\31\2\u013e\u013c\3\2\2\2\u013f\u0142\3\2\2\2\u0140\u013e\3"+
		"\2\2\2\u0140\u0141\3\2\2\2\u0141\u0143\3\2\2\2\u0142\u0140\3\2\2\2\u0143"+
		"\u0144\7+\2\2\u0144\u0146\3\2\2\2\u0145\u0138\3\2\2\2\u0145\u013a\3\2"+
		"\2\2\u0146D\3\2\2\2\u0147\u014a\5A!\2\u0148\u0149\7\60\2\2\u0149\u014b"+
		"\5\61\31\2\u014a\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u015e\3\2\2\2"+
		"\u014c\u014f\5C\"\2\u014d\u014e\7\60\2\2\u014e\u0150\5\61\31\2\u014f\u014d"+
		"\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u015e\3\2\2\2\u0151\u015b\5\65\33\2"+
		"\u0152\u0153\7\60\2\2\u0153\u0156\5A!\2\u0154\u0156\5C\"\2\u0155\u0152"+
		"\3\2\2\2\u0155\u0154\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0158\7\60\2\2"+
		"\u0158\u015a\5\61\31\2\u0159\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015c"+
		"\3\2\2\2\u015b\u0155\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015e\3\2\2\2\u015d"+
		"\u0147\3\2\2\2\u015d\u014c\3\2\2\2\u015d\u0151\3\2\2\2\u015eF\3\2\2\2"+
		"\u015f\u016c\3\2\2\2\u0160\u0161\5!\21\2\u0161\u0162\5G$\2\u0162\u016c"+
		"\3\2\2\2\u0163\u0164\5? \2\u0164\u0165\5G$\2\u0165\u016c\3\2\2\2\u0166"+
		"\u0167\7}\2\2\u0167\u0168\5G$\2\u0168\u0169\7\177\2\2\u0169\u016a\5G$"+
		"\2\u016a\u016c\3\2\2\2\u016b\u015f\3\2\2\2\u016b\u0160\3\2\2\2\u016b\u0163"+
		"\3\2\2\2\u016b\u0166\3\2\2\2\u016cH\3\2\2\2\u016d\u016e\7\61\2\2\u016e"+
		"\u016f\7,\2\2\u016f\u0174\3\2\2\2\u0170\u0173\5I%\2\u0171\u0173\13\2\2"+
		"\2\u0172\u0170\3\2\2\2\u0172\u0171\3\2\2\2\u0173\u0176\3\2\2\2\u0174\u0175"+
		"\3\2\2\2\u0174\u0172\3\2\2\2\u0175\u0177\3\2\2\2\u0176\u0174\3\2\2\2\u0177"+
		"\u0178\7,\2\2\u0178\u0179\7\61\2\2\u0179\u017a\3\2\2\2\u017a\u017b\b%"+
		"\2\2\u017bJ\3\2\2\2\u017c\u017d\7\61\2\2\u017d\u017e\7\61\2\2\u017e\u0182"+
		"\3\2\2\2\u017f\u0181\13\2\2\2\u0180\u017f\3\2\2\2\u0181\u0184\3\2\2\2"+
		"\u0182\u0183\3\2\2\2\u0182\u0180\3\2\2\2\u0183\u0186\3\2\2\2\u0184\u0182"+
		"\3\2\2\2\u0185\u0187\t\13\2\2\u0186\u0185\3\2\2\2\u0187\u0188\3\2\2\2"+
		"\u0188\u0189\b&\2\2\u0189L\3\2\2\2\u018a\u018c\t\f\2\2\u018b\u018a\3\2"+
		"\2\2\u018c\u018d\3\2\2\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u018f\3\2\2\2\u018f\u0190\b\'\2\2\u0190N\3\2\2\2$\2}\u00a2\u00ae\u00b6"+
		"\u00c8\u00cf\u00d2\u00d8\u00e3\u00eb\u00f5\u00fc\u0103\u010b\u0114\u0121"+
		"\u0129\u012f\u0134\u0140\u0145\u014a\u014f\u0155\u0159\u015b\u015d\u016b"+
		"\u0172\u0174\u0182\u0186\u018d\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}