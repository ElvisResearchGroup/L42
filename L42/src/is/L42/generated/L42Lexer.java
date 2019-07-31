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
		T__0=1, T__1=2, T__2=3, Mdf=4, VoidKW=5, VarKw=6, StringSingle=7, Number=8, 
		MUniqueNum=9, MHash=10, X=11, CsP=12, ClassSep=13, UnderScore=14, OR=15, 
		ORNS=16, Doc=17, BlockComment=18, LineComment=19, Whitespace=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "Mdf", "VoidKW", "VarKw", "IdUp", "IdLow", "IdChar", 
			"CHAR", "CHARInStringSingle", "CharsUrl", "CHARDocText", "URL", "Fn", 
			"Fx", "StringSingle", "Number", "MUniqueNum", "MHash", "X", "C", "CsP", 
			"ClassSep", "UnderScore", "OR", "ORNS", "Doc", "FS", "FParXs", "FPathLit", 
			"DocText", "BlockComment", "LineComment", "Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\'", "')'", "'='", null, "'void'", "'var'", null, null, null, 
			null, null, null, "'.'", "'_'", null, "'('"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "Mdf", "VoidKW", "VarKw", "StringSingle", "Number", 
			"MUniqueNum", "MHash", "X", "CsP", "ClassSep", "UnderScore", "OR", "ORNS", 
			"Doc", "BlockComment", "LineComment", "Whitespace"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\26\u0168\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\5\5x\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\7\b\u0084\n\b\f\b"+
		"\16\b\u0087\13\b\3\b\3\b\3\t\7\t\u008c\n\t\f\t\16\t\u008f\13\t\3\t\3\t"+
		"\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\6\17\u009e\n\17\r\17"+
		"\16\17\u009f\3\20\3\20\3\20\7\20\u00a5\n\20\f\20\16\20\u00a8\13\20\5\20"+
		"\u00aa\n\20\3\21\3\21\7\21\u00ae\n\21\f\21\16\21\u00b1\13\21\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\7\23\u00b9\n\23\f\23\16\23\u00bc\13\23\3\24\3\24"+
		"\3\24\7\24\u00c1\n\24\f\24\16\24\u00c4\13\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\5\25\u00cd\n\25\3\25\3\25\3\25\7\25\u00d2\n\25\f\25\16\25\u00d5"+
		"\13\25\3\25\3\25\3\25\3\25\5\25\u00db\n\25\3\26\3\26\3\27\3\27\7\27\u00e1"+
		"\n\27\f\27\16\27\u00e4\13\27\3\30\3\30\3\30\3\30\7\30\u00ea\n\30\f\30"+
		"\16\30\u00ed\13\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\5"+
		"\33\u00f9\n\33\3\34\3\34\3\35\3\35\3\35\3\35\5\35\u0101\n\35\3\35\3\35"+
		"\3\35\3\35\5\35\u0107\n\35\3\36\3\36\3\36\5\36\u010c\n\36\3\36\3\36\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\7\37\u0116\n\37\f\37\16\37\u0119\13\37\3"+
		"\37\3\37\5\37\u011d\n\37\3 \3 \3 \5 \u0122\n \3 \3 \3 \5 \u0127\n \3 "+
		"\3 \3 \3 \5 \u012d\n \3 \3 \5 \u0131\n \5 \u0133\n \5 \u0135\n \3!\3!"+
		"\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\5!\u0143\n!\3\"\3\"\3\"\3\"\3\"\7\"\u014a"+
		"\n\"\f\"\16\"\u014d\13\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\7#\u0158\n#\f"+
		"#\16#\u015b\13#\3#\5#\u015e\n#\3#\3#\3$\6$\u0163\n$\r$\16$\u0164\3$\3"+
		"$\4\u014b\u0159\2%\3\3\5\4\7\5\t\6\13\7\r\b\17\2\21\2\23\2\25\2\27\2\31"+
		"\2\33\2\35\2\37\2!\2#\t%\n\'\13)\f+\r-\2/\16\61\17\63\20\65\21\67\229"+
		"\23;\2=\2?\2A\2C\24E\25G\26\3\2\r\4\2&&C\\\7\2&&\62;C\\aac|\4\2\f\f\""+
		"\u0080\4\2\"#%\u0080\7\2\"#%(*|~~\u0080\u0080\7\2\f\f\"AC|~~\u0080\u0080"+
		"\5\2/\60\62;aa\6\2&&\62;C\\c|\4\2\"\"..\3\3\f\f\5\2\f\f\"\"..\2\u0182"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2C\3"+
		"\2\2\2\2E\3\2\2\2\2G\3\2\2\2\3I\3\2\2\2\5K\3\2\2\2\7M\3\2\2\2\tw\3\2\2"+
		"\2\13y\3\2\2\2\r~\3\2\2\2\17\u0085\3\2\2\2\21\u008d\3\2\2\2\23\u0092\3"+
		"\2\2\2\25\u0094\3\2\2\2\27\u0096\3\2\2\2\31\u0098\3\2\2\2\33\u009a\3\2"+
		"\2\2\35\u009d\3\2\2\2\37\u00a9\3\2\2\2!\u00ab\3\2\2\2#\u00b2\3\2\2\2%"+
		"\u00b6\3\2\2\2\'\u00bd\3\2\2\2)\u00ca\3\2\2\2+\u00dc\3\2\2\2-\u00de\3"+
		"\2\2\2/\u00e5\3\2\2\2\61\u00ee\3\2\2\2\63\u00f0\3\2\2\2\65\u00f8\3\2\2"+
		"\2\67\u00fa\3\2\2\29\u0106\3\2\2\2;\u010b\3\2\2\2=\u011c\3\2\2\2?\u0134"+
		"\3\2\2\2A\u0142\3\2\2\2C\u0144\3\2\2\2E\u0153\3\2\2\2G\u0162\3\2\2\2I"+
		"J\7^\2\2J\4\3\2\2\2KL\7+\2\2L\6\3\2\2\2MN\7?\2\2N\b\3\2\2\2OP\7h\2\2P"+
		"Q\7y\2\2QR\7f\2\2RS\7\"\2\2ST\7o\2\2TU\7w\2\2Ux\7v\2\2VW\7h\2\2WX\7y\2"+
		"\2XY\7f\2\2YZ\7\"\2\2Z[\7k\2\2[\\\7o\2\2\\x\7o\2\2]^\7k\2\2^_\7o\2\2_"+
		"x\7o\2\2`a\7o\2\2ab\7w\2\2bx\7v\2\2cd\7n\2\2de\7g\2\2ef\7p\2\2fx\7v\2"+
		"\2gh\7t\2\2hi\7g\2\2ij\7c\2\2jx\7f\2\2kl\7e\2\2lm\7c\2\2mn\7r\2\2no\7"+
		"u\2\2op\7w\2\2pq\7n\2\2qx\7g\2\2rs\7e\2\2st\7n\2\2tu\7c\2\2uv\7u\2\2v"+
		"x\7u\2\2wO\3\2\2\2wV\3\2\2\2w]\3\2\2\2w`\3\2\2\2wc\3\2\2\2wg\3\2\2\2w"+
		"k\3\2\2\2wr\3\2\2\2x\n\3\2\2\2yz\7x\2\2z{\7q\2\2{|\7k\2\2|}\7f\2\2}\f"+
		"\3\2\2\2~\177\7x\2\2\177\u0080\7c\2\2\u0080\u0081\7t\2\2\u0081\16\3\2"+
		"\2\2\u0082\u0084\7a\2\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2\2\2\u0085"+
		"\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0085\3\2"+
		"\2\2\u0088\u0089\t\2\2\2\u0089\20\3\2\2\2\u008a\u008c\7a\2\2\u008b\u008a"+
		"\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e"+
		"\u0090\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091\4c|\2\u0091\22\3\2\2\2"+
		"\u0092\u0093\t\3\2\2\u0093\24\3\2\2\2\u0094\u0095\t\4\2\2\u0095\26\3\2"+
		"\2\2\u0096\u0097\t\5\2\2\u0097\30\3\2\2\2\u0098\u0099\t\6\2\2\u0099\32"+
		"\3\2\2\2\u009a\u009b\t\7\2\2\u009b\34\3\2\2\2\u009c\u009e\5\31\r\2\u009d"+
		"\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2"+
		"\2\2\u00a0\36\3\2\2\2\u00a1\u00aa\7\62\2\2\u00a2\u00a6\4\63;\2\u00a3\u00a5"+
		"\4\62;\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6"+
		"\u00a7\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00a1\3\2"+
		"\2\2\u00a9\u00a2\3\2\2\2\u00aa \3\2\2\2\u00ab\u00af\5\21\t\2\u00ac\u00ae"+
		"\5\23\n\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2"+
		"\u00af\u00b0\3\2\2\2\u00b0\"\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b3\7"+
		"$\2\2\u00b3\u00b4\5\27\f\2\u00b4\u00b5\7$\2\2\u00b5$\3\2\2\2\u00b6\u00ba"+
		"\4\62;\2\u00b7\u00b9\t\b\2\2\u00b8\u00b7\3\2\2\2\u00b9\u00bc\3\2\2\2\u00ba"+
		"\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb&\3\2\2\2\u00bc\u00ba\3\2\2\2"+
		"\u00bd\u00c2\5!\21\2\u00be\u00bf\7%\2\2\u00bf\u00c1\5!\21\2\u00c0\u00be"+
		"\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c6\7<\2\2\u00c6\u00c7\7<\2"+
		"\2\u00c7\u00c8\3\2\2\2\u00c8\u00c9\5\37\20\2\u00c9(\3\2\2\2\u00ca\u00cc"+
		"\7%\2\2\u00cb\u00cd\7&\2\2\u00cc\u00cb\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd"+
		"\u00ce\3\2\2\2\u00ce\u00d3\5!\21\2\u00cf\u00d0\7%\2\2\u00d0\u00d2\5!\21"+
		"\2\u00d1\u00cf\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4"+
		"\3\2\2\2\u00d4\u00da\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00d7\7<\2\2\u00d7"+
		"\u00d8\7<\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00db\5\37\20\2\u00da\u00d6\3"+
		"\2\2\2\u00da\u00db\3\2\2\2\u00db*\3\2\2\2\u00dc\u00dd\5!\21\2\u00dd,\3"+
		"\2\2\2\u00de\u00e2\5\17\b\2\u00df\u00e1\t\t\2\2\u00e0\u00df\3\2\2\2\u00e1"+
		"\u00e4\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3.\3\2\2\2"+
		"\u00e4\u00e2\3\2\2\2\u00e5\u00eb\5-\27\2\u00e6\u00e7\5\61\31\2\u00e7\u00e8"+
		"\5-\27\2\u00e8\u00ea\3\2\2\2\u00e9\u00e6\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb"+
		"\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\60\3\2\2\2\u00ed\u00eb\3\2\2"+
		"\2\u00ee\u00ef\7\60\2\2\u00ef\62\3\2\2\2\u00f0\u00f1\7a\2\2\u00f1\64\3"+
		"\2\2\2\u00f2\u00f3\7\"\2\2\u00f3\u00f9\7*\2\2\u00f4\u00f5\7.\2\2\u00f5"+
		"\u00f9\7*\2\2\u00f6\u00f7\7\f\2\2\u00f7\u00f9\7*\2\2\u00f8\u00f2\3\2\2"+
		"\2\u00f8\u00f4\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\66\3\2\2\2\u00fa\u00fb"+
		"\7*\2\2\u00fb8\3\2\2\2\u00fc\u00fd\7B\2\2\u00fd\u0107\5? \2\u00fe\u0100"+
		"\7B\2\2\u00ff\u0101\5? \2\u0100\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101"+
		"\u0102\3\2\2\2\u0102\u0103\7}\2\2\u0103\u0104\5A!\2\u0104\u0105\7\177"+
		"\2\2\u0105\u0107\3\2\2\2\u0106\u00fc\3\2\2\2\u0106\u00fe\3\2\2\2\u0107"+
		":\3\2\2\2\u0108\u010c\5\'\24\2\u0109\u010c\5)\25\2\u010a\u010c\5+\26\2"+
		"\u010b\u0108\3\2\2\2\u010b\u0109\3\2\2\2\u010b\u010a\3\2\2\2\u010c\u010d"+
		"\3\2\2\2\u010d\u010e\5=\37\2\u010e<\3\2\2\2\u010f\u0110\7*\2\2\u0110\u011d"+
		"\7+\2\2\u0111\u0112\7*\2\2\u0112\u0117\5+\26\2\u0113\u0114\t\n\2\2\u0114"+
		"\u0116\5+\26\2\u0115\u0113\3\2\2\2\u0116\u0119\3\2\2\2\u0117\u0115\3\2"+
		"\2\2\u0117\u0118\3\2\2\2\u0118\u011a\3\2\2\2\u0119\u0117\3\2\2\2\u011a"+
		"\u011b\7+\2\2\u011b\u011d\3\2\2\2\u011c\u010f\3\2\2\2\u011c\u0111\3\2"+
		"\2\2\u011d>\3\2\2\2\u011e\u0121\5;\36\2\u011f\u0120\7\60\2\2\u0120\u0122"+
		"\5+\26\2\u0121\u011f\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0135\3\2\2\2\u0123"+
		"\u0126\5=\37\2\u0124\u0125\7\60\2\2\u0125\u0127\5+\26\2\u0126\u0124\3"+
		"\2\2\2\u0126\u0127\3\2\2\2\u0127\u0135\3\2\2\2\u0128\u0132\5/\30\2\u0129"+
		"\u012a\7\60\2\2\u012a\u012d\5;\36\2\u012b\u012d\5=\37\2\u012c\u0129\3"+
		"\2\2\2\u012c\u012b\3\2\2\2\u012d\u0130\3\2\2\2\u012e\u012f\7\60\2\2\u012f"+
		"\u0131\5+\26\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0133\3\2"+
		"\2\2\u0132\u012c\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0135\3\2\2\2\u0134"+
		"\u011e\3\2\2\2\u0134\u0123\3\2\2\2\u0134\u0128\3\2\2\2\u0135@\3\2\2\2"+
		"\u0136\u0143\3\2\2\2\u0137\u0138\5\33\16\2\u0138\u0139\5A!\2\u0139\u0143"+
		"\3\2\2\2\u013a\u013b\59\35\2\u013b\u013c\5A!\2\u013c\u0143\3\2\2\2\u013d"+
		"\u013e\7}\2\2\u013e\u013f\5A!\2\u013f\u0140\7\177\2\2\u0140\u0141\5A!"+
		"\2\u0141\u0143\3\2\2\2\u0142\u0136\3\2\2\2\u0142\u0137\3\2\2\2\u0142\u013a"+
		"\3\2\2\2\u0142\u013d\3\2\2\2\u0143B\3\2\2\2\u0144\u0145\7\61\2\2\u0145"+
		"\u0146\7,\2\2\u0146\u014b\3\2\2\2\u0147\u014a\5C\"\2\u0148\u014a\13\2"+
		"\2\2\u0149\u0147\3\2\2\2\u0149\u0148\3\2\2\2\u014a\u014d\3\2\2\2\u014b"+
		"\u014c\3\2\2\2\u014b\u0149\3\2\2\2\u014c\u014e\3\2\2\2\u014d\u014b\3\2"+
		"\2\2\u014e\u014f\7,\2\2\u014f\u0150\7\61\2\2\u0150\u0151\3\2\2\2\u0151"+
		"\u0152\b\"\2\2\u0152D\3\2\2\2\u0153\u0154\7\61\2\2\u0154\u0155\7\61\2"+
		"\2\u0155\u0159\3\2\2\2\u0156\u0158\13\2\2\2\u0157\u0156\3\2\2\2\u0158"+
		"\u015b\3\2\2\2\u0159\u015a\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015d\3\2"+
		"\2\2\u015b\u0159\3\2\2\2\u015c\u015e\t\13\2\2\u015d\u015c\3\2\2\2\u015e"+
		"\u015f\3\2\2\2\u015f\u0160\b#\2\2\u0160F\3\2\2\2\u0161\u0163\t\f\2\2\u0162"+
		"\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2"+
		"\2\2\u0165\u0166\3\2\2\2\u0166\u0167\b$\2\2\u0167H\3\2\2\2#\2w\u0085\u008d"+
		"\u009f\u00a6\u00a9\u00af\u00ba\u00c2\u00cc\u00d3\u00da\u00e2\u00eb\u00f8"+
		"\u0100\u0106\u010b\u0117\u011c\u0121\u0126\u012c\u0130\u0132\u0134\u0142"+
		"\u0149\u014b\u0159\u015d\u0164\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}