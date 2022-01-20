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
public class L42Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, MSS=3, MMS=4, IMS=5, Num=6, Eq=7, ClassSep=8, CsP=9, URL=10, 
		Whitespace=11;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "MSS", "MMS", "IMS", "Num", "Eq", "IdUp", "IdChar", "Fn", 
			"ClassSep", "C", "CsP", "URL", "CharsUrl", "Whitespace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "']'", "'maxStackSize'", "'maxMemorySize'", "'initialMemorySize'", 
			null, "'='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "MSS", "MMS", "IMS", "Num", "Eq", "ClassSep", "CsP", 
			"URL", "Whitespace"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\r\u0096\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\7\7W\n\7\f\7"+
		"\16\7Z\13\7\3\7\3\7\3\b\3\b\3\t\7\ta\n\t\f\t\16\td\13\t\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\13\7\13m\n\13\f\13\16\13p\13\13\5\13r\n\13\3\f\3\f\3\r"+
		"\3\r\7\rx\n\r\f\r\16\r{\13\r\3\r\3\r\3\r\3\r\5\r\u0081\n\r\3\16\3\16\3"+
		"\16\3\16\7\16\u0087\n\16\f\16\16\16\u008a\13\16\3\17\6\17\u008d\n\17\r"+
		"\17\16\17\u008e\3\20\3\20\3\21\3\21\3\21\3\21\2\2\22\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\2\23\2\25\2\27\n\31\2\33\13\35\f\37\2!\r\3\2\7\5\2IIMM"+
		"OO\4\2&&C\\\7\2&&\62;C\\aac|\t\2\"#%(*\\^^`|~~\u0080\u0080\5\2\f\f\"\""+
		"..\2\u0098\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\27\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2!"+
		"\3\2\2\2\3#\3\2\2\2\5%\3\2\2\2\7\'\3\2\2\2\t\64\3\2\2\2\13B\3\2\2\2\r"+
		"T\3\2\2\2\17]\3\2\2\2\21b\3\2\2\2\23g\3\2\2\2\25q\3\2\2\2\27s\3\2\2\2"+
		"\31u\3\2\2\2\33\u0082\3\2\2\2\35\u008c\3\2\2\2\37\u0090\3\2\2\2!\u0092"+
		"\3\2\2\2#$\7]\2\2$\4\3\2\2\2%&\7_\2\2&\6\3\2\2\2\'(\7o\2\2()\7c\2\2)*"+
		"\7z\2\2*+\7U\2\2+,\7v\2\2,-\7c\2\2-.\7e\2\2./\7m\2\2/\60\7U\2\2\60\61"+
		"\7k\2\2\61\62\7|\2\2\62\63\7g\2\2\63\b\3\2\2\2\64\65\7o\2\2\65\66\7c\2"+
		"\2\66\67\7z\2\2\678\7O\2\289\7g\2\29:\7o\2\2:;\7q\2\2;<\7t\2\2<=\7{\2"+
		"\2=>\7U\2\2>?\7k\2\2?@\7|\2\2@A\7g\2\2A\n\3\2\2\2BC\7k\2\2CD\7p\2\2DE"+
		"\7k\2\2EF\7v\2\2FG\7k\2\2GH\7c\2\2HI\7n\2\2IJ\7O\2\2JK\7g\2\2KL\7o\2\2"+
		"LM\7q\2\2MN\7t\2\2NO\7{\2\2OP\7U\2\2PQ\7k\2\2QR\7|\2\2RS\7g\2\2S\f\3\2"+
		"\2\2TX\4\63;\2UW\4\62;\2VU\3\2\2\2WZ\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y[\3\2"+
		"\2\2ZX\3\2\2\2[\\\t\2\2\2\\\16\3\2\2\2]^\7?\2\2^\20\3\2\2\2_a\7a\2\2`"+
		"_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2ef\t\3\2\2"+
		"f\22\3\2\2\2gh\t\4\2\2h\24\3\2\2\2ir\7\62\2\2jn\4\63;\2km\4\62;\2lk\3"+
		"\2\2\2mp\3\2\2\2nl\3\2\2\2no\3\2\2\2or\3\2\2\2pn\3\2\2\2qi\3\2\2\2qj\3"+
		"\2\2\2r\26\3\2\2\2st\7\60\2\2t\30\3\2\2\2uy\5\21\t\2vx\5\23\n\2wv\3\2"+
		"\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\u0080\3\2\2\2{y\3\2\2\2|}\7<\2\2}"+
		"~\7<\2\2~\177\3\2\2\2\177\u0081\5\25\13\2\u0080|\3\2\2\2\u0080\u0081\3"+
		"\2\2\2\u0081\32\3\2\2\2\u0082\u0088\5\31\r\2\u0083\u0084\5\27\f\2\u0084"+
		"\u0085\5\31\r\2\u0085\u0087\3\2\2\2\u0086\u0083\3\2\2\2\u0087\u008a\3"+
		"\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\34\3\2\2\2\u008a"+
		"\u0088\3\2\2\2\u008b\u008d\5\37\20\2\u008c\u008b\3\2\2\2\u008d\u008e\3"+
		"\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\36\3\2\2\2\u0090"+
		"\u0091\t\5\2\2\u0091 \3\2\2\2\u0092\u0093\t\6\2\2\u0093\u0094\3\2\2\2"+
		"\u0094\u0095\b\21\2\2\u0095\"\3\2\2\2\13\2Xbnqy\u0080\u0088\u008e\3\2"+
		"\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}