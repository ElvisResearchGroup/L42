// Generated from SettingsFile.g4 by ANTLR 4.7.2
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
public class SettingsFileLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MSS=1, MMS=2, IMS=3, Num=4, Eq=5, ClassSep=6, CsP=7, URL=8, BlockComment=9, 
		LineComment=10, Whitespace=11;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"MSS", "MMS", "IMS", "Num", "Eq", "IdUp", "IdChar", "Fn", "ClassSep", 
			"C", "CsP", "URL", "CharsUrl", "BlockComment", "LineComment", "Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'maxStackSize'", "'maxMemorySize'", "'initialMemorySize'", null, 
			"'='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "MSS", "MMS", "IMS", "Num", "Eq", "ClassSep", "CsP", "URL", "BlockComment", 
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


	public SettingsFileLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SettingsFile.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\r\u00b2\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\7\5S\n\5\f\5\16\5V\13\5\3\5\3"+
		"\5\3\6\3\6\3\7\7\7]\n\7\f\7\16\7`\13\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\7\t"+
		"i\n\t\f\t\16\tl\13\t\5\tn\n\t\3\n\3\n\3\13\3\13\7\13t\n\13\f\13\16\13"+
		"w\13\13\3\13\3\13\3\13\3\13\5\13}\n\13\3\f\3\f\3\f\3\f\7\f\u0083\n\f\f"+
		"\f\16\f\u0086\13\f\3\r\3\r\6\r\u008a\n\r\r\r\16\r\u008b\3\r\3\r\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\7\17\u0097\n\17\f\17\16\17\u009a\13\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\7\20\u00a5\n\20\f\20\16\20"+
		"\u00a8\13\20\3\20\5\20\u00ab\n\20\3\20\3\20\3\21\3\21\3\21\3\21\4\u0098"+
		"\u00a6\2\22\3\3\5\4\7\5\t\6\13\7\r\2\17\2\21\2\23\b\25\2\27\t\31\n\33"+
		"\2\35\13\37\f!\r\3\2\b\5\2IIMMOO\4\2&&C\\\7\2&&\62;C\\aac|\t\2\"#%(*\\"+
		"^^`|~~\u0080\u0080\3\3\f\f\5\2\f\f\"\"..\2\u00b7\2\3\3\2\2\2\2\5\3\2\2"+
		"\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\23\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3#\3\2\2\2\5\60\3\2\2\2"+
		"\7>\3\2\2\2\tP\3\2\2\2\13Y\3\2\2\2\r^\3\2\2\2\17c\3\2\2\2\21m\3\2\2\2"+
		"\23o\3\2\2\2\25q\3\2\2\2\27~\3\2\2\2\31\u0087\3\2\2\2\33\u008f\3\2\2\2"+
		"\35\u0091\3\2\2\2\37\u00a0\3\2\2\2!\u00ae\3\2\2\2#$\7o\2\2$%\7c\2\2%&"+
		"\7z\2\2&\'\7U\2\2\'(\7v\2\2()\7c\2\2)*\7e\2\2*+\7m\2\2+,\7U\2\2,-\7k\2"+
		"\2-.\7|\2\2./\7g\2\2/\4\3\2\2\2\60\61\7o\2\2\61\62\7c\2\2\62\63\7z\2\2"+
		"\63\64\7O\2\2\64\65\7g\2\2\65\66\7o\2\2\66\67\7q\2\2\678\7t\2\289\7{\2"+
		"\29:\7U\2\2:;\7k\2\2;<\7|\2\2<=\7g\2\2=\6\3\2\2\2>?\7k\2\2?@\7p\2\2@A"+
		"\7k\2\2AB\7v\2\2BC\7k\2\2CD\7c\2\2DE\7n\2\2EF\7O\2\2FG\7g\2\2GH\7o\2\2"+
		"HI\7q\2\2IJ\7t\2\2JK\7{\2\2KL\7U\2\2LM\7k\2\2MN\7|\2\2NO\7g\2\2O\b\3\2"+
		"\2\2PT\4\63;\2QS\4\62;\2RQ\3\2\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2\2\2UW\3\2"+
		"\2\2VT\3\2\2\2WX\t\2\2\2X\n\3\2\2\2YZ\7?\2\2Z\f\3\2\2\2[]\7a\2\2\\[\3"+
		"\2\2\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_a\3\2\2\2`^\3\2\2\2ab\t\3\2\2b\16"+
		"\3\2\2\2cd\t\4\2\2d\20\3\2\2\2en\7\62\2\2fj\4\63;\2gi\4\62;\2hg\3\2\2"+
		"\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2kn\3\2\2\2lj\3\2\2\2me\3\2\2\2mf\3\2\2"+
		"\2n\22\3\2\2\2op\7\60\2\2p\24\3\2\2\2qu\5\r\7\2rt\5\17\b\2sr\3\2\2\2t"+
		"w\3\2\2\2us\3\2\2\2uv\3\2\2\2v|\3\2\2\2wu\3\2\2\2xy\7<\2\2yz\7<\2\2z{"+
		"\3\2\2\2{}\5\21\t\2|x\3\2\2\2|}\3\2\2\2}\26\3\2\2\2~\u0084\5\25\13\2\177"+
		"\u0080\5\23\n\2\u0080\u0081\5\25\13\2\u0081\u0083\3\2\2\2\u0082\177\3"+
		"\2\2\2\u0083\u0086\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085"+
		"\30\3\2\2\2\u0086\u0084\3\2\2\2\u0087\u0089\7]\2\2\u0088\u008a\5\33\16"+
		"\2\u0089\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c"+
		"\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e\7_\2\2\u008e\32\3\2\2\2\u008f"+
		"\u0090\t\5\2\2\u0090\34\3\2\2\2\u0091\u0092\7\61\2\2\u0092\u0093\7,\2"+
		"\2\u0093\u0098\3\2\2\2\u0094\u0097\5\35\17\2\u0095\u0097\13\2\2\2\u0096"+
		"\u0094\3\2\2\2\u0096\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0099\3\2"+
		"\2\2\u0098\u0096\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u0098\3\2\2\2\u009b"+
		"\u009c\7,\2\2\u009c\u009d\7\61\2\2\u009d\u009e\3\2\2\2\u009e\u009f\b\17"+
		"\2\2\u009f\36\3\2\2\2\u00a0\u00a1\7\61\2\2\u00a1\u00a2\7\61\2\2\u00a2"+
		"\u00a6\3\2\2\2\u00a3\u00a5\13\2\2\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3"+
		"\2\2\2\u00a6\u00a7\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8"+
		"\u00a6\3\2\2\2\u00a9\u00ab\t\6\2\2\u00aa\u00a9\3\2\2\2\u00ab\u00ac\3\2"+
		"\2\2\u00ac\u00ad\b\20\2\2\u00ad \3\2\2\2\u00ae\u00af\t\7\2\2\u00af\u00b0"+
		"\3\2\2\2\u00b0\u00b1\b\21\2\2\u00b1\"\3\2\2\2\17\2T^jmu|\u0084\u008b\u0096"+
		"\u0098\u00a6\u00aa\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}