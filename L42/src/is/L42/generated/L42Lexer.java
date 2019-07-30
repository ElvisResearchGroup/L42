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
		T__0=1, T__1=2, T__2=3, StringSingle=4, Number=5, MUniqueNum=6, MHash=7, 
		X=8, CsP=9, ClassSep=10, OR=11, ORNS=12, BlockComment=13, LineComment=14, 
		Whitespace=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "IdUp", "IdLow", "IdChar", "CHAR", "CHARInStringSingle", 
			"CharsUrl", "URL", "Fn", "Fx", "StringSingle", "Number", "MUniqueNum", 
			"MHash", "X", "C", "CsP", "ClassSep", "OR", "ORNS", "BlockComment", "LineComment", 
			"Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'void'", "')'", "'='", null, null, null, null, null, null, "'.'", 
			null, "'('"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "StringSingle", "Number", "MUniqueNum", "MHash", 
			"X", "CsP", "ClassSep", "OR", "ORNS", "BlockComment", "LineComment", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21\u00d8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\7\5@\n\5\f\5\16"+
		"\5C\13\5\3\5\3\5\3\6\7\6H\n\6\f\6\16\6K\13\6\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\13\6\13X\n\13\r\13\16\13Y\3\f\3\f\3\f\7\f_\n\f\f\f\16"+
		"\fb\13\f\5\fd\n\f\3\r\3\r\7\rh\n\r\f\r\16\rk\13\r\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\7\17s\n\17\f\17\16\17v\13\17\3\20\3\20\3\20\7\20{\n\20\f\20"+
		"\16\20~\13\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\5\21\u0087\n\21\3\21"+
		"\3\21\3\21\7\21\u008c\n\21\f\21\16\21\u008f\13\21\3\21\3\21\3\21\3\21"+
		"\5\21\u0095\n\21\3\22\3\22\3\23\3\23\7\23\u009b\n\23\f\23\16\23\u009e"+
		"\13\23\3\24\3\24\3\24\3\24\7\24\u00a4\n\24\f\24\16\24\u00a7\13\24\3\25"+
		"\3\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u00b1\n\26\3\27\3\27\3\30\3\30"+
		"\3\30\3\30\3\30\7\30\u00ba\n\30\f\30\16\30\u00bd\13\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\7\31\u00c8\n\31\f\31\16\31\u00cb\13\31"+
		"\3\31\5\31\u00ce\n\31\3\31\3\31\3\32\6\32\u00d3\n\32\r\32\16\32\u00d4"+
		"\3\32\3\32\4\u00bb\u00c9\2\33\3\3\5\4\7\5\t\2\13\2\r\2\17\2\21\2\23\2"+
		"\25\2\27\2\31\2\33\6\35\7\37\b!\t#\n%\2\'\13)\f+\r-\16/\17\61\20\63\21"+
		"\3\2\13\4\2&&C\\\7\2&&\62;C\\aac|\4\2\f\f\"\u0080\4\2\"#%\u0080\7\2\""+
		"#%(*|~~\u0080\u0080\5\2/\60\62;aa\6\2&&\62;C\\c|\3\3\f\f\5\2\f\f\"\"."+
		".\2\u00e0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\3\65\3\2\2\2\5:\3\2"+
		"\2\2\7<\3\2\2\2\tA\3\2\2\2\13I\3\2\2\2\rN\3\2\2\2\17P\3\2\2\2\21R\3\2"+
		"\2\2\23T\3\2\2\2\25W\3\2\2\2\27c\3\2\2\2\31e\3\2\2\2\33l\3\2\2\2\35p\3"+
		"\2\2\2\37w\3\2\2\2!\u0084\3\2\2\2#\u0096\3\2\2\2%\u0098\3\2\2\2\'\u009f"+
		"\3\2\2\2)\u00a8\3\2\2\2+\u00b0\3\2\2\2-\u00b2\3\2\2\2/\u00b4\3\2\2\2\61"+
		"\u00c3\3\2\2\2\63\u00d2\3\2\2\2\65\66\7x\2\2\66\67\7q\2\2\678\7k\2\28"+
		"9\7f\2\29\4\3\2\2\2:;\7+\2\2;\6\3\2\2\2<=\7?\2\2=\b\3\2\2\2>@\7a\2\2?"+
		">\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2BD\3\2\2\2CA\3\2\2\2DE\t\2\2\2"+
		"E\n\3\2\2\2FH\7a\2\2GF\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2"+
		"KI\3\2\2\2LM\4c|\2M\f\3\2\2\2NO\t\3\2\2O\16\3\2\2\2PQ\t\4\2\2Q\20\3\2"+
		"\2\2RS\t\5\2\2S\22\3\2\2\2TU\t\6\2\2U\24\3\2\2\2VX\5\23\n\2WV\3\2\2\2"+
		"XY\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\26\3\2\2\2[d\7\62\2\2\\`\4\63;\2]_\4\62"+
		";\2^]\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ad\3\2\2\2b`\3\2\2\2c[\3\2"+
		"\2\2c\\\3\2\2\2d\30\3\2\2\2ei\5\13\6\2fh\5\r\7\2gf\3\2\2\2hk\3\2\2\2i"+
		"g\3\2\2\2ij\3\2\2\2j\32\3\2\2\2ki\3\2\2\2lm\7$\2\2mn\5\21\t\2no\7$\2\2"+
		"o\34\3\2\2\2pt\4\62;\2qs\t\7\2\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2"+
		"\2u\36\3\2\2\2vt\3\2\2\2w|\5\31\r\2xy\7%\2\2y{\5\31\r\2zx\3\2\2\2{~\3"+
		"\2\2\2|z\3\2\2\2|}\3\2\2\2}\177\3\2\2\2~|\3\2\2\2\177\u0080\7<\2\2\u0080"+
		"\u0081\7<\2\2\u0081\u0082\3\2\2\2\u0082\u0083\5\27\f\2\u0083 \3\2\2\2"+
		"\u0084\u0086\7%\2\2\u0085\u0087\7&\2\2\u0086\u0085\3\2\2\2\u0086\u0087"+
		"\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008d\5\31\r\2\u0089\u008a\7%\2\2\u008a"+
		"\u008c\5\31\r\2\u008b\u0089\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3"+
		"\2\2\2\u008d\u008e\3\2\2\2\u008e\u0094\3\2\2\2\u008f\u008d\3\2\2\2\u0090"+
		"\u0091\7<\2\2\u0091\u0092\7<\2\2\u0092\u0093\3\2\2\2\u0093\u0095\5\27"+
		"\f\2\u0094\u0090\3\2\2\2\u0094\u0095\3\2\2\2\u0095\"\3\2\2\2\u0096\u0097"+
		"\5\31\r\2\u0097$\3\2\2\2\u0098\u009c\5\t\5\2\u0099\u009b\t\b\2\2\u009a"+
		"\u0099\3\2\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2"+
		"\2\2\u009d&\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a5\5%\23\2\u00a0\u00a1"+
		"\5)\25\2\u00a1\u00a2\5%\23\2\u00a2\u00a4\3\2\2\2\u00a3\u00a0\3\2\2\2\u00a4"+
		"\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6(\3\2\2\2"+
		"\u00a7\u00a5\3\2\2\2\u00a8\u00a9\7\60\2\2\u00a9*\3\2\2\2\u00aa\u00ab\7"+
		"\"\2\2\u00ab\u00b1\7*\2\2\u00ac\u00ad\7.\2\2\u00ad\u00b1\7*\2\2\u00ae"+
		"\u00af\7\f\2\2\u00af\u00b1\7*\2\2\u00b0\u00aa\3\2\2\2\u00b0\u00ac\3\2"+
		"\2\2\u00b0\u00ae\3\2\2\2\u00b1,\3\2\2\2\u00b2\u00b3\7*\2\2\u00b3.\3\2"+
		"\2\2\u00b4\u00b5\7\61\2\2\u00b5\u00b6\7,\2\2\u00b6\u00bb\3\2\2\2\u00b7"+
		"\u00ba\5/\30\2\u00b8\u00ba\13\2\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00b8\3"+
		"\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc"+
		"\u00be\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00bf\7,\2\2\u00bf\u00c0\7\61"+
		"\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\b\30\2\2\u00c2\60\3\2\2\2\u00c3\u00c4"+
		"\7\61\2\2\u00c4\u00c5\7\61\2\2\u00c5\u00c9\3\2\2\2\u00c6\u00c8\13\2\2"+
		"\2\u00c7\u00c6\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9\u00ca\3\2\2\2\u00c9\u00c7"+
		"\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00ce\t\t\2\2\u00cd"+
		"\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\b\31\2\2\u00d0\62\3\2\2"+
		"\2\u00d1\u00d3\t\n\2\2\u00d2\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d2"+
		"\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\b\32\2\2"+
		"\u00d7\64\3\2\2\2\26\2AIY`cit|\u0086\u008d\u0094\u009c\u00a5\u00b0\u00b9"+
		"\u00bb\u00c9\u00cd\u00d4\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}