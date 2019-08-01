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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, Mdf=6, VoidKW=7, VarKw=8, CatchKw=9, 
		Throw=10, WhoopsKw=11, StringSingle=12, Number=13, MUniqueNum=14, MHash=15, 
		X=16, CsP=17, ClassSep=18, UnderScore=19, OR=20, ORNS=21, Doc=22, BlockComment=23, 
		LineComment=24, Whitespace=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "Mdf", "VoidKW", "VarKw", "CatchKw", 
			"Throw", "WhoopsKw", "IdUp", "IdLow", "IdChar", "CHAR", "CHARInStringSingle", 
			"CharsUrl", "CHARDocText", "URL", "Fn", "Fx", "StringSingle", "Number", 
			"MUniqueNum", "MHash", "X", "C", "CsP", "ClassSep", "UnderScore", "OR", 
			"ORNS", "Doc", "FS", "FParXs", "FPathLit", "DocText", "BlockComment", 
			"LineComment", "Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\'", "')'", "'='", "'{'", "'}'", null, "'void'", "'var'", "'catch'", 
			null, "'whoops'", null, null, null, null, null, null, "'.'", "'_'", null, 
			"'('"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "Mdf", "VoidKW", "VarKw", "CatchKw", 
			"Throw", "WhoopsKw", "StringSingle", "Number", "MUniqueNum", "MHash", 
			"X", "CsP", "ClassSep", "UnderScore", "OR", "ORNS", "Doc", "BlockComment", 
			"LineComment", "Whitespace"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\33\u0199\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u0086\n\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\5\13\u00ab\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\7\r\u00b5"+
		"\n\r\f\r\16\r\u00b8\13\r\3\r\3\r\3\16\7\16\u00bd\n\16\f\16\16\16\u00c0"+
		"\13\16\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24"+
		"\6\24\u00cf\n\24\r\24\16\24\u00d0\3\25\3\25\3\25\7\25\u00d6\n\25\f\25"+
		"\16\25\u00d9\13\25\5\25\u00db\n\25\3\26\3\26\7\26\u00df\n\26\f\26\16\26"+
		"\u00e2\13\26\3\27\3\27\3\27\3\27\3\30\3\30\7\30\u00ea\n\30\f\30\16\30"+
		"\u00ed\13\30\3\31\3\31\3\31\7\31\u00f2\n\31\f\31\16\31\u00f5\13\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\5\32\u00fe\n\32\3\32\3\32\3\32\7\32\u0103"+
		"\n\32\f\32\16\32\u0106\13\32\3\32\3\32\3\32\3\32\5\32\u010c\n\32\3\33"+
		"\3\33\3\34\3\34\7\34\u0112\n\34\f\34\16\34\u0115\13\34\3\35\3\35\3\35"+
		"\3\35\7\35\u011b\n\35\f\35\16\35\u011e\13\35\3\36\3\36\3\37\3\37\3 \3"+
		" \3 \3 \3 \3 \5 \u012a\n \3!\3!\3\"\3\"\3\"\3\"\5\"\u0132\n\"\3\"\3\""+
		"\3\"\3\"\5\"\u0138\n\"\3#\3#\3#\5#\u013d\n#\3#\3#\3$\3$\3$\3$\3$\3$\7"+
		"$\u0147\n$\f$\16$\u014a\13$\3$\3$\5$\u014e\n$\3%\3%\3%\5%\u0153\n%\3%"+
		"\3%\3%\5%\u0158\n%\3%\3%\3%\3%\5%\u015e\n%\3%\3%\5%\u0162\n%\5%\u0164"+
		"\n%\5%\u0166\n%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u0174\n&\3\'\3"+
		"\'\3\'\3\'\3\'\7\'\u017b\n\'\f\'\16\'\u017e\13\'\3\'\3\'\3\'\3\'\3\'\3"+
		"(\3(\3(\3(\7(\u0189\n(\f(\16(\u018c\13(\3(\5(\u018f\n(\3(\3(\3)\6)\u0194"+
		"\n)\r)\16)\u0195\3)\3)\4\u017c\u018a\2*\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\16/\17\61"+
		"\20\63\21\65\22\67\29\23;\24=\25?\26A\27C\30E\2G\2I\2K\2M\31O\32Q\33\3"+
		"\2\r\4\2&&C\\\7\2&&\62;C\\aac|\4\2\f\f\"\u0080\4\2\"#%\u0080\7\2\"#%("+
		"*|~~\u0080\u0080\7\2\f\f\"AC|~~\u0080\u0080\5\2/\60\62;aa\6\2&&\62;C\\"+
		"c|\4\2\"\"..\3\3\f\f\5\2\f\f\"\"..\2\u01b5\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\3S\3\2\2"+
		"\2\5U\3\2\2\2\7W\3\2\2\2\tY\3\2\2\2\13[\3\2\2\2\r\u0085\3\2\2\2\17\u0087"+
		"\3\2\2\2\21\u008c\3\2\2\2\23\u0090\3\2\2\2\25\u00aa\3\2\2\2\27\u00ac\3"+
		"\2\2\2\31\u00b6\3\2\2\2\33\u00be\3\2\2\2\35\u00c3\3\2\2\2\37\u00c5\3\2"+
		"\2\2!\u00c7\3\2\2\2#\u00c9\3\2\2\2%\u00cb\3\2\2\2\'\u00ce\3\2\2\2)\u00da"+
		"\3\2\2\2+\u00dc\3\2\2\2-\u00e3\3\2\2\2/\u00e7\3\2\2\2\61\u00ee\3\2\2\2"+
		"\63\u00fb\3\2\2\2\65\u010d\3\2\2\2\67\u010f\3\2\2\29\u0116\3\2\2\2;\u011f"+
		"\3\2\2\2=\u0121\3\2\2\2?\u0129\3\2\2\2A\u012b\3\2\2\2C\u0137\3\2\2\2E"+
		"\u013c\3\2\2\2G\u014d\3\2\2\2I\u0165\3\2\2\2K\u0173\3\2\2\2M\u0175\3\2"+
		"\2\2O\u0184\3\2\2\2Q\u0193\3\2\2\2ST\7^\2\2T\4\3\2\2\2UV\7+\2\2V\6\3\2"+
		"\2\2WX\7?\2\2X\b\3\2\2\2YZ\7}\2\2Z\n\3\2\2\2[\\\7\177\2\2\\\f\3\2\2\2"+
		"]^\7h\2\2^_\7y\2\2_`\7f\2\2`a\7\"\2\2ab\7o\2\2bc\7w\2\2c\u0086\7v\2\2"+
		"de\7h\2\2ef\7y\2\2fg\7f\2\2gh\7\"\2\2hi\7k\2\2ij\7o\2\2j\u0086\7o\2\2"+
		"kl\7k\2\2lm\7o\2\2m\u0086\7o\2\2no\7o\2\2op\7w\2\2p\u0086\7v\2\2qr\7n"+
		"\2\2rs\7g\2\2st\7p\2\2t\u0086\7v\2\2uv\7t\2\2vw\7g\2\2wx\7c\2\2x\u0086"+
		"\7f\2\2yz\7e\2\2z{\7c\2\2{|\7r\2\2|}\7u\2\2}~\7w\2\2~\177\7n\2\2\177\u0086"+
		"\7g\2\2\u0080\u0081\7e\2\2\u0081\u0082\7n\2\2\u0082\u0083\7c\2\2\u0083"+
		"\u0084\7u\2\2\u0084\u0086\7u\2\2\u0085]\3\2\2\2\u0085d\3\2\2\2\u0085k"+
		"\3\2\2\2\u0085n\3\2\2\2\u0085q\3\2\2\2\u0085u\3\2\2\2\u0085y\3\2\2\2\u0085"+
		"\u0080\3\2\2\2\u0086\16\3\2\2\2\u0087\u0088\7x\2\2\u0088\u0089\7q\2\2"+
		"\u0089\u008a\7k\2\2\u008a\u008b\7f\2\2\u008b\20\3\2\2\2\u008c\u008d\7"+
		"x\2\2\u008d\u008e\7c\2\2\u008e\u008f\7t\2\2\u008f\22\3\2\2\2\u0090\u0091"+
		"\7e\2\2\u0091\u0092\7c\2\2\u0092\u0093\7v\2\2\u0093\u0094\7e\2\2\u0094"+
		"\u0095\7j\2\2\u0095\24\3\2\2\2\u0096\u0097\7t\2\2\u0097\u0098\7g\2\2\u0098"+
		"\u0099\7v\2\2\u0099\u009a\7w\2\2\u009a\u009b\7t\2\2\u009b\u00ab\7p\2\2"+
		"\u009c\u009d\7g\2\2\u009d\u009e\7t\2\2\u009e\u009f\7t\2\2\u009f\u00a0"+
		"\7q\2\2\u00a0\u00ab\7t\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7z\2\2\u00a3"+
		"\u00a4\7e\2\2\u00a4\u00a5\7g\2\2\u00a5\u00a6\7r\2\2\u00a6\u00a7\7v\2\2"+
		"\u00a7\u00a8\7k\2\2\u00a8\u00a9\7q\2\2\u00a9\u00ab\7p\2\2\u00aa\u0096"+
		"\3\2\2\2\u00aa\u009c\3\2\2\2\u00aa\u00a1\3\2\2\2\u00ab\26\3\2\2\2\u00ac"+
		"\u00ad\7y\2\2\u00ad\u00ae\7j\2\2\u00ae\u00af\7q\2\2\u00af\u00b0\7q\2\2"+
		"\u00b0\u00b1\7r\2\2\u00b1\u00b2\7u\2\2\u00b2\30\3\2\2\2\u00b3\u00b5\7"+
		"a\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6"+
		"\u00b7\3\2\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00ba\t\2"+
		"\2\2\u00ba\32\3\2\2\2\u00bb\u00bd\7a\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0"+
		"\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0"+
		"\u00be\3\2\2\2\u00c1\u00c2\4c|\2\u00c2\34\3\2\2\2\u00c3\u00c4\t\3\2\2"+
		"\u00c4\36\3\2\2\2\u00c5\u00c6\t\4\2\2\u00c6 \3\2\2\2\u00c7\u00c8\t\5\2"+
		"\2\u00c8\"\3\2\2\2\u00c9\u00ca\t\6\2\2\u00ca$\3\2\2\2\u00cb\u00cc\t\7"+
		"\2\2\u00cc&\3\2\2\2\u00cd\u00cf\5#\22\2\u00ce\u00cd\3\2\2\2\u00cf\u00d0"+
		"\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1(\3\2\2\2\u00d2"+
		"\u00db\7\62\2\2\u00d3\u00d7\4\63;\2\u00d4\u00d6\4\62;\2\u00d5\u00d4\3"+
		"\2\2\2\u00d6\u00d9\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"+
		"\u00db\3\2\2\2\u00d9\u00d7\3\2\2\2\u00da\u00d2\3\2\2\2\u00da\u00d3\3\2"+
		"\2\2\u00db*\3\2\2\2\u00dc\u00e0\5\33\16\2\u00dd\u00df\5\35\17\2\u00de"+
		"\u00dd\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2"+
		"\2\2\u00e1,\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e3\u00e4\7$\2\2\u00e4\u00e5"+
		"\5!\21\2\u00e5\u00e6\7$\2\2\u00e6.\3\2\2\2\u00e7\u00eb\4\62;\2\u00e8\u00ea"+
		"\t\b\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb"+
		"\u00ec\3\2\2\2\u00ec\60\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00f3\5+\26"+
		"\2\u00ef\u00f0\7%\2\2\u00f0\u00f2\5+\26\2\u00f1\u00ef\3\2\2\2\u00f2\u00f5"+
		"\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5"+
		"\u00f3\3\2\2\2\u00f6\u00f7\7<\2\2\u00f7\u00f8\7<\2\2\u00f8\u00f9\3\2\2"+
		"\2\u00f9\u00fa\5)\25\2\u00fa\62\3\2\2\2\u00fb\u00fd\7%\2\2\u00fc\u00fe"+
		"\7&\2\2\u00fd\u00fc\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff"+
		"\u0104\5+\26\2\u0100\u0101\7%\2\2\u0101\u0103\5+\26\2\u0102\u0100\3\2"+
		"\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105"+
		"\u010b\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u0108\7<\2\2\u0108\u0109\7<\2"+
		"\2\u0109\u010a\3\2\2\2\u010a\u010c\5)\25\2\u010b\u0107\3\2\2\2\u010b\u010c"+
		"\3\2\2\2\u010c\64\3\2\2\2\u010d\u010e\5+\26\2\u010e\66\3\2\2\2\u010f\u0113"+
		"\5\31\r\2\u0110\u0112\t\t\2\2\u0111\u0110\3\2\2\2\u0112\u0115\3\2\2\2"+
		"\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u01148\3\2\2\2\u0115\u0113\3"+
		"\2\2\2\u0116\u011c\5\67\34\2\u0117\u0118\5;\36\2\u0118\u0119\5\67\34\2"+
		"\u0119\u011b\3\2\2\2\u011a\u0117\3\2\2\2\u011b\u011e\3\2\2\2\u011c\u011a"+
		"\3\2\2\2\u011c\u011d\3\2\2\2\u011d:\3\2\2\2\u011e\u011c\3\2\2\2\u011f"+
		"\u0120\7\60\2\2\u0120<\3\2\2\2\u0121\u0122\7a\2\2\u0122>\3\2\2\2\u0123"+
		"\u0124\7\"\2\2\u0124\u012a\7*\2\2\u0125\u0126\7.\2\2\u0126\u012a\7*\2"+
		"\2\u0127\u0128\7\f\2\2\u0128\u012a\7*\2\2\u0129\u0123\3\2\2\2\u0129\u0125"+
		"\3\2\2\2\u0129\u0127\3\2\2\2\u012a@\3\2\2\2\u012b\u012c\7*\2\2\u012cB"+
		"\3\2\2\2\u012d\u012e\7B\2\2\u012e\u0138\5I%\2\u012f\u0131\7B\2\2\u0130"+
		"\u0132\5I%\2\u0131\u0130\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\3\2\2"+
		"\2\u0133\u0134\7}\2\2\u0134\u0135\5K&\2\u0135\u0136\7\177\2\2\u0136\u0138"+
		"\3\2\2\2\u0137\u012d\3\2\2\2\u0137\u012f\3\2\2\2\u0138D\3\2\2\2\u0139"+
		"\u013d\5\61\31\2\u013a\u013d\5\63\32\2\u013b\u013d\5\65\33\2\u013c\u0139"+
		"\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013b\3\2\2\2\u013d\u013e\3\2\2\2\u013e"+
		"\u013f\5G$\2\u013fF\3\2\2\2\u0140\u0141\7*\2\2\u0141\u014e\7+\2\2\u0142"+
		"\u0143\7*\2\2\u0143\u0148\5\65\33\2\u0144\u0145\t\n\2\2\u0145\u0147\5"+
		"\65\33\2\u0146\u0144\3\2\2\2\u0147\u014a\3\2\2\2\u0148\u0146\3\2\2\2\u0148"+
		"\u0149\3\2\2\2\u0149\u014b\3\2\2\2\u014a\u0148\3\2\2\2\u014b\u014c\7+"+
		"\2\2\u014c\u014e\3\2\2\2\u014d\u0140\3\2\2\2\u014d\u0142\3\2\2\2\u014e"+
		"H\3\2\2\2\u014f\u0152\5E#\2\u0150\u0151\7\60\2\2\u0151\u0153\5\65\33\2"+
		"\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0166\3\2\2\2\u0154\u0157"+
		"\5G$\2\u0155\u0156\7\60\2\2\u0156\u0158\5\65\33\2\u0157\u0155\3\2\2\2"+
		"\u0157\u0158\3\2\2\2\u0158\u0166\3\2\2\2\u0159\u0163\59\35\2\u015a\u015b"+
		"\7\60\2\2\u015b\u015e\5E#\2\u015c\u015e\5G$\2\u015d\u015a\3\2\2\2\u015d"+
		"\u015c\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u0160\7\60\2\2\u0160\u0162\5"+
		"\65\33\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0164\3\2\2\2\u0163"+
		"\u015d\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0166\3\2\2\2\u0165\u014f\3\2"+
		"\2\2\u0165\u0154\3\2\2\2\u0165\u0159\3\2\2\2\u0166J\3\2\2\2\u0167\u0174"+
		"\3\2\2\2\u0168\u0169\5%\23\2\u0169\u016a\5K&\2\u016a\u0174\3\2\2\2\u016b"+
		"\u016c\5C\"\2\u016c\u016d\5K&\2\u016d\u0174\3\2\2\2\u016e\u016f\7}\2\2"+
		"\u016f\u0170\5K&\2\u0170\u0171\7\177\2\2\u0171\u0172\5K&\2\u0172\u0174"+
		"\3\2\2\2\u0173\u0167\3\2\2\2\u0173\u0168\3\2\2\2\u0173\u016b\3\2\2\2\u0173"+
		"\u016e\3\2\2\2\u0174L\3\2\2\2\u0175\u0176\7\61\2\2\u0176\u0177\7,\2\2"+
		"\u0177\u017c\3\2\2\2\u0178\u017b\5M\'\2\u0179\u017b\13\2\2\2\u017a\u0178"+
		"\3\2\2\2\u017a\u0179\3\2\2\2\u017b\u017e\3\2\2\2\u017c\u017d\3\2\2\2\u017c"+
		"\u017a\3\2\2\2\u017d\u017f\3\2\2\2\u017e\u017c\3\2\2\2\u017f\u0180\7,"+
		"\2\2\u0180\u0181\7\61\2\2\u0181\u0182\3\2\2\2\u0182\u0183\b\'\2\2\u0183"+
		"N\3\2\2\2\u0184\u0185\7\61\2\2\u0185\u0186\7\61\2\2\u0186\u018a\3\2\2"+
		"\2\u0187\u0189\13\2\2\2\u0188\u0187\3\2\2\2\u0189\u018c\3\2\2\2\u018a"+
		"\u018b\3\2\2\2\u018a\u0188\3\2\2\2\u018b\u018e\3\2\2\2\u018c\u018a\3\2"+
		"\2\2\u018d\u018f\t\13\2\2\u018e\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\u0191\b(\2\2\u0191P\3\2\2\2\u0192\u0194\t\f\2\2\u0193\u0192\3\2\2\2\u0194"+
		"\u0195\3\2\2\2\u0195\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\3\2"+
		"\2\2\u0197\u0198\b)\2\2\u0198R\3\2\2\2$\2\u0085\u00aa\u00b6\u00be\u00d0"+
		"\u00d7\u00da\u00e0\u00eb\u00f3\u00fd\u0104\u010b\u0113\u011c\u0129\u0131"+
		"\u0137\u013c\u0148\u014d\u0152\u0157\u015d\u0161\u0163\u0165\u0173\u017a"+
		"\u017c\u018a\u018e\u0195\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}