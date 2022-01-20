// Generated from L42Aux.g4 by ANTLR 4.7.2
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
public class L42AuxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Dot=1, TDStart=2, InfoNorm=3, InfoTyped=4, TypeDep=5, CoherentDep=6, MetaCoherentDep=7, 
		Watched=8, UsedMethods=9, HiddenSupertypes=10, Refined=11, Close=12, NativeKind=13, 
		NativePar=14, UniqueId=15, ThisKw=16, AnyKw=17, VoidKw=18, LibraryKw=19, 
		C=20, MUniqueNum=21, MHash=22, X=23, ORound=24, CRound=25, OCurly=26, 
		CCurly=27, W=28, Doc=29, CHARInDoc=30;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Dot", "TDStart", "InfoNorm", "InfoTyped", "TypeDep", "CoherentDep", 
			"MetaCoherentDep", "Watched", "UsedMethods", "HiddenSupertypes", "Refined", 
			"Close", "NativeKind", "NativePar", "UniqueId", "ThisKw", "AnyKw", "VoidKw", 
			"LibraryKw", "C", "IdUp", "IdLow", "IdChar", "CHAR", "CHARInStringSingle", 
			"CharsUrl", "CHARDocText", "URL", "Fn", "Fx", "Number", "MUniqueNum", 
			"MHash", "X", "ORound", "CRound", "OCurly", "CCurly", "W", "Doc", "FS", 
			"FParXs", "FPathSel", "DocText", "CHARInDoc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'@@'", "'#norm{'", "'#typed{'", "'typeDep='", "'coherentDep='", 
			"'metaCoherentDep='", "'watched='", "'usedMethods='", "'hiddenSupertypes='", 
			"'refined='", "'close'", "'nativeKind='", "'nativePar='", "'uniqueId='", 
			null, "'Any'", "'Void'", "'Library'", null, null, null, null, "'('", 
			"')'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Dot", "TDStart", "InfoNorm", "InfoTyped", "TypeDep", "CoherentDep", 
			"MetaCoherentDep", "Watched", "UsedMethods", "HiddenSupertypes", "Refined", 
			"Close", "NativeKind", "NativePar", "UniqueId", "ThisKw", "AnyKw", "VoidKw", 
			"LibraryKw", "C", "MUniqueNum", "MHash", "X", "ORound", "CRound", "OCurly", 
			"CCurly", "W", "Doc", "CHARInDoc"
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


	public L42AuxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "L42Aux.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2 \u01cf\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00f7\n\21"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\25\3\25\7\25\u010c\n\25\f\25\16\25\u010f\13\25\3\25"+
		"\3\25\3\25\3\25\5\25\u0115\n\25\3\26\7\26\u0118\n\26\f\26\16\26\u011b"+
		"\13\26\3\26\3\26\3\27\7\27\u0120\n\27\f\27\16\27\u0123\13\27\3\27\3\27"+
		"\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\6\35\u0132\n\35"+
		"\r\35\16\35\u0133\3\36\3\36\3\36\7\36\u0139\n\36\f\36\16\36\u013c\13\36"+
		"\5\36\u013e\n\36\3\37\3\37\7\37\u0142\n\37\f\37\16\37\u0145\13\37\3 \3"+
		" \7 \u0149\n \f \16 \u014c\13 \3!\3!\3!\7!\u0151\n!\f!\16!\u0154\13!\3"+
		"!\3!\3!\3!\3!\3\"\3\"\3\"\6\"\u015e\n\"\r\"\16\"\u015f\5\"\u0162\n\"\3"+
		"\"\3\"\3\"\7\"\u0167\n\"\f\"\16\"\u016a\13\"\3\"\3\"\3\"\3\"\5\"\u0170"+
		"\n\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3)\5)\u0182\n)\3)"+
		"\3)\3)\3)\5)\u0188\n)\3*\3*\3*\5*\u018d\n*\3*\3*\3+\3+\3+\3+\3+\3+\7+"+
		"\u0197\n+\f+\16+\u019a\13+\3+\3+\5+\u019e\n+\3,\3,\3,\5,\u01a3\n,\3,\3"+
		",\3,\5,\u01a8\n,\3,\3,\3,\3,\7,\u01ae\n,\f,\16,\u01b1\13,\3,\3,\3,\5,"+
		"\u01b6\n,\3,\3,\5,\u01ba\n,\5,\u01bc\n,\5,\u01be\n,\3-\3-\3-\3-\3-\3-"+
		"\3-\3-\3-\3-\3-\3-\5-\u01cc\n-\3.\3.\2\2/\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\2-\2/\2\61\2\63\2\65\2\67\29\2;\2=\2?\2A\27C\30E\31G\32I\33K\34M\35O"+
		"\36Q\37S\2U\2W\2Y\2[ \3\2\f\4\2&&C\\\7\2&&\62;C\\aac|\4\2\f\f\"\u0080"+
		"\4\2\"#%\u0080\t\2\"#%(*\\^^`|~~\u0080\u0080\7\2\f\f\"AC|~~\u0080\u0080"+
		"\5\2/\60\62;aa\5\2\f\f\"\"..\4\2\"\"..\t\2#),-//\61AC|~~\u0080\u0080\2"+
		"\u01df\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2"+
		"E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3"+
		"\2\2\2\2[\3\2\2\2\3]\3\2\2\2\5_\3\2\2\2\7b\3\2\2\2\ti\3\2\2\2\13q\3\2"+
		"\2\2\rz\3\2\2\2\17\u0087\3\2\2\2\21\u0098\3\2\2\2\23\u00a1\3\2\2\2\25"+
		"\u00ae\3\2\2\2\27\u00c0\3\2\2\2\31\u00c9\3\2\2\2\33\u00cf\3\2\2\2\35\u00db"+
		"\3\2\2\2\37\u00e6\3\2\2\2!\u00f0\3\2\2\2#\u00f8\3\2\2\2%\u00fc\3\2\2\2"+
		"\'\u0101\3\2\2\2)\u0109\3\2\2\2+\u0119\3\2\2\2-\u0121\3\2\2\2/\u0126\3"+
		"\2\2\2\61\u0128\3\2\2\2\63\u012a\3\2\2\2\65\u012c\3\2\2\2\67\u012e\3\2"+
		"\2\29\u0131\3\2\2\2;\u013d\3\2\2\2=\u013f\3\2\2\2?\u0146\3\2\2\2A\u014d"+
		"\3\2\2\2C\u0161\3\2\2\2E\u0171\3\2\2\2G\u0173\3\2\2\2I\u0175\3\2\2\2K"+
		"\u0177\3\2\2\2M\u0179\3\2\2\2O\u017b\3\2\2\2Q\u0187\3\2\2\2S\u018c\3\2"+
		"\2\2U\u019d\3\2\2\2W\u01bd\3\2\2\2Y\u01cb\3\2\2\2[\u01cd\3\2\2\2]^\7\60"+
		"\2\2^\4\3\2\2\2_`\7B\2\2`a\7B\2\2a\6\3\2\2\2bc\7%\2\2cd\7p\2\2de\7q\2"+
		"\2ef\7t\2\2fg\7o\2\2gh\7}\2\2h\b\3\2\2\2ij\7%\2\2jk\7v\2\2kl\7{\2\2lm"+
		"\7r\2\2mn\7g\2\2no\7f\2\2op\7}\2\2p\n\3\2\2\2qr\7v\2\2rs\7{\2\2st\7r\2"+
		"\2tu\7g\2\2uv\7F\2\2vw\7g\2\2wx\7r\2\2xy\7?\2\2y\f\3\2\2\2z{\7e\2\2{|"+
		"\7q\2\2|}\7j\2\2}~\7g\2\2~\177\7t\2\2\177\u0080\7g\2\2\u0080\u0081\7p"+
		"\2\2\u0081\u0082\7v\2\2\u0082\u0083\7F\2\2\u0083\u0084\7g\2\2\u0084\u0085"+
		"\7r\2\2\u0085\u0086\7?\2\2\u0086\16\3\2\2\2\u0087\u0088\7o\2\2\u0088\u0089"+
		"\7g\2\2\u0089\u008a\7v\2\2\u008a\u008b\7c\2\2\u008b\u008c\7E\2\2\u008c"+
		"\u008d\7q\2\2\u008d\u008e\7j\2\2\u008e\u008f\7g\2\2\u008f\u0090\7t\2\2"+
		"\u0090\u0091\7g\2\2\u0091\u0092\7p\2\2\u0092\u0093\7v\2\2\u0093\u0094"+
		"\7F\2\2\u0094\u0095\7g\2\2\u0095\u0096\7r\2\2\u0096\u0097\7?\2\2\u0097"+
		"\20\3\2\2\2\u0098\u0099\7y\2\2\u0099\u009a\7c\2\2\u009a\u009b\7v\2\2\u009b"+
		"\u009c\7e\2\2\u009c\u009d\7j\2\2\u009d\u009e\7g\2\2\u009e\u009f\7f\2\2"+
		"\u009f\u00a0\7?\2\2\u00a0\22\3\2\2\2\u00a1\u00a2\7w\2\2\u00a2\u00a3\7"+
		"u\2\2\u00a3\u00a4\7g\2\2\u00a4\u00a5\7f\2\2\u00a5\u00a6\7O\2\2\u00a6\u00a7"+
		"\7g\2\2\u00a7\u00a8\7v\2\2\u00a8\u00a9\7j\2\2\u00a9\u00aa\7q\2\2\u00aa"+
		"\u00ab\7f\2\2\u00ab\u00ac\7u\2\2\u00ac\u00ad\7?\2\2\u00ad\24\3\2\2\2\u00ae"+
		"\u00af\7j\2\2\u00af\u00b0\7k\2\2\u00b0\u00b1\7f\2\2\u00b1\u00b2\7f\2\2"+
		"\u00b2\u00b3\7g\2\2\u00b3\u00b4\7p\2\2\u00b4\u00b5\7U\2\2\u00b5\u00b6"+
		"\7w\2\2\u00b6\u00b7\7r\2\2\u00b7\u00b8\7g\2\2\u00b8\u00b9\7t\2\2\u00b9"+
		"\u00ba\7v\2\2\u00ba\u00bb\7{\2\2\u00bb\u00bc\7r\2\2\u00bc\u00bd\7g\2\2"+
		"\u00bd\u00be\7u\2\2\u00be\u00bf\7?\2\2\u00bf\26\3\2\2\2\u00c0\u00c1\7"+
		"t\2\2\u00c1\u00c2\7g\2\2\u00c2\u00c3\7h\2\2\u00c3\u00c4\7k\2\2\u00c4\u00c5"+
		"\7p\2\2\u00c5\u00c6\7g\2\2\u00c6\u00c7\7f\2\2\u00c7\u00c8\7?\2\2\u00c8"+
		"\30\3\2\2\2\u00c9\u00ca\7e\2\2\u00ca\u00cb\7n\2\2\u00cb\u00cc\7q\2\2\u00cc"+
		"\u00cd\7u\2\2\u00cd\u00ce\7g\2\2\u00ce\32\3\2\2\2\u00cf\u00d0\7p\2\2\u00d0"+
		"\u00d1\7c\2\2\u00d1\u00d2\7v\2\2\u00d2\u00d3\7k\2\2\u00d3\u00d4\7x\2\2"+
		"\u00d4\u00d5\7g\2\2\u00d5\u00d6\7M\2\2\u00d6\u00d7\7k\2\2\u00d7\u00d8"+
		"\7p\2\2\u00d8\u00d9\7f\2\2\u00d9\u00da\7?\2\2\u00da\34\3\2\2\2\u00db\u00dc"+
		"\7p\2\2\u00dc\u00dd\7c\2\2\u00dd\u00de\7v\2\2\u00de\u00df\7k\2\2\u00df"+
		"\u00e0\7x\2\2\u00e0\u00e1\7g\2\2\u00e1\u00e2\7R\2\2\u00e2\u00e3\7c\2\2"+
		"\u00e3\u00e4\7t\2\2\u00e4\u00e5\7?\2\2\u00e5\36\3\2\2\2\u00e6\u00e7\7"+
		"w\2\2\u00e7\u00e8\7p\2\2\u00e8\u00e9\7k\2\2\u00e9\u00ea\7s\2\2\u00ea\u00eb"+
		"\7w\2\2\u00eb\u00ec\7g\2\2\u00ec\u00ed\7K\2\2\u00ed\u00ee\7f\2\2\u00ee"+
		"\u00ef\7?\2\2\u00ef \3\2\2\2\u00f0\u00f1\7V\2\2\u00f1\u00f2\7j\2\2\u00f2"+
		"\u00f3\7k\2\2\u00f3\u00f4\7u\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f7\5;\36"+
		"\2\u00f6\u00f5\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\"\3\2\2\2\u00f8\u00f9"+
		"\7C\2\2\u00f9\u00fa\7p\2\2\u00fa\u00fb\7{\2\2\u00fb$\3\2\2\2\u00fc\u00fd"+
		"\7X\2\2\u00fd\u00fe\7q\2\2\u00fe\u00ff\7k\2\2\u00ff\u0100\7f\2\2\u0100"+
		"&\3\2\2\2\u0101\u0102\7N\2\2\u0102\u0103\7k\2\2\u0103\u0104\7d\2\2\u0104"+
		"\u0105\7t\2\2\u0105\u0106\7c\2\2\u0106\u0107\7t\2\2\u0107\u0108\7{\2\2"+
		"\u0108(\3\2\2\2\u0109\u010d\5+\26\2\u010a\u010c\5/\30\2\u010b\u010a\3"+
		"\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e"+
		"\u0114\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0111\7<\2\2\u0111\u0112\7<\2"+
		"\2\u0112\u0113\3\2\2\2\u0113\u0115\5;\36\2\u0114\u0110\3\2\2\2\u0114\u0115"+
		"\3\2\2\2\u0115*\3\2\2\2\u0116\u0118\7a\2\2\u0117\u0116\3\2\2\2\u0118\u011b"+
		"\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011c\3\2\2\2\u011b"+
		"\u0119\3\2\2\2\u011c\u011d\t\2\2\2\u011d,\3\2\2\2\u011e\u0120\7a\2\2\u011f"+
		"\u011e\3\2\2\2\u0120\u0123\3\2\2\2\u0121\u011f\3\2\2\2\u0121\u0122\3\2"+
		"\2\2\u0122\u0124\3\2\2\2\u0123\u0121\3\2\2\2\u0124\u0125\4c|\2\u0125."+
		"\3\2\2\2\u0126\u0127\t\3\2\2\u0127\60\3\2\2\2\u0128\u0129\t\4\2\2\u0129"+
		"\62\3\2\2\2\u012a\u012b\t\5\2\2\u012b\64\3\2\2\2\u012c\u012d\t\6\2\2\u012d"+
		"\66\3\2\2\2\u012e\u012f\t\7\2\2\u012f8\3\2\2\2\u0130\u0132\5\65\33\2\u0131"+
		"\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2"+
		"\2\2\u0134:\3\2\2\2\u0135\u013e\7\62\2\2\u0136\u013a\4\63;\2\u0137\u0139"+
		"\4\62;\2\u0138\u0137\3\2\2\2\u0139\u013c\3\2\2\2\u013a\u0138\3\2\2\2\u013a"+
		"\u013b\3\2\2\2\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013d\u0135\3\2"+
		"\2\2\u013d\u0136\3\2\2\2\u013e<\3\2\2\2\u013f\u0143\5-\27\2\u0140\u0142"+
		"\5/\30\2\u0141\u0140\3\2\2\2\u0142\u0145\3\2\2\2\u0143\u0141\3\2\2\2\u0143"+
		"\u0144\3\2\2\2\u0144>\3\2\2\2\u0145\u0143\3\2\2\2\u0146\u014a\4\62;\2"+
		"\u0147\u0149\t\b\2\2\u0148\u0147\3\2\2\2\u0149\u014c\3\2\2\2\u014a\u0148"+
		"\3\2\2\2\u014a\u014b\3\2\2\2\u014b@\3\2\2\2\u014c\u014a\3\2\2\2\u014d"+
		"\u0152\5=\37\2\u014e\u014f\7%\2\2\u014f\u0151\5=\37\2\u0150\u014e\3\2"+
		"\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0156\7<\2\2\u0156\u0157\7<\2"+
		"\2\u0157\u0158\3\2\2\2\u0158\u0159\5;\36\2\u0159B\3\2\2\2\u015a\u015b"+
		"\7%\2\2\u015b\u0162\7&\2\2\u015c\u015e\7%\2\2\u015d\u015c\3\2\2\2\u015e"+
		"\u015f\3\2\2\2\u015f\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0162\3\2"+
		"\2\2\u0161\u015a\3\2\2\2\u0161\u015d\3\2\2\2\u0162\u0163\3\2\2\2\u0163"+
		"\u0168\5=\37\2\u0164\u0165\7%\2\2\u0165\u0167\5=\37\2\u0166\u0164\3\2"+
		"\2\2\u0167\u016a\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169"+
		"\u016f\3\2\2\2\u016a\u0168\3\2\2\2\u016b\u016c\7<\2\2\u016c\u016d\7<\2"+
		"\2\u016d\u016e\3\2\2\2\u016e\u0170\5;\36\2\u016f\u016b\3\2\2\2\u016f\u0170"+
		"\3\2\2\2\u0170D\3\2\2\2\u0171\u0172\5=\37\2\u0172F\3\2\2\2\u0173\u0174"+
		"\7*\2\2\u0174H\3\2\2\2\u0175\u0176\7+\2\2\u0176J\3\2\2\2\u0177\u0178\7"+
		"}\2\2\u0178L\3\2\2\2\u0179\u017a\7\177\2\2\u017aN\3\2\2\2\u017b\u017c"+
		"\t\t\2\2\u017cP\3\2\2\2\u017d\u017e\7B\2\2\u017e\u0188\5W,\2\u017f\u0181"+
		"\7B\2\2\u0180\u0182\5W,\2\u0181\u0180\3\2\2\2\u0181\u0182\3\2\2\2\u0182"+
		"\u0183\3\2\2\2\u0183\u0184\5K&\2\u0184\u0185\5Y-\2\u0185\u0186\5M\'\2"+
		"\u0186\u0188\3\2\2\2\u0187\u017d\3\2\2\2\u0187\u017f\3\2\2\2\u0188R\3"+
		"\2\2\2\u0189\u018d\5A!\2\u018a\u018d\5C\"\2\u018b\u018d\5E#\2\u018c\u0189"+
		"\3\2\2\2\u018c\u018a\3\2\2\2\u018c\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u018f\5U+\2\u018fT\3\2\2\2\u0190\u0191\7*\2\2\u0191\u019e\7+\2\2\u0192"+
		"\u0193\7*\2\2\u0193\u0198\5E#\2\u0194\u0195\t\n\2\2\u0195\u0197\5E#\2"+
		"\u0196\u0194\3\2\2\2\u0197\u019a\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199"+
		"\3\2\2\2\u0199\u019b\3\2\2\2\u019a\u0198\3\2\2\2\u019b\u019c\7+\2\2\u019c"+
		"\u019e\3\2\2\2\u019d\u0190\3\2\2\2\u019d\u0192\3\2\2\2\u019eV\3\2\2\2"+
		"\u019f\u01a2\5S*\2\u01a0\u01a1\7\60\2\2\u01a1\u01a3\5E#\2\u01a2\u01a0"+
		"\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01be\3\2\2\2\u01a4\u01a7\5U+\2\u01a5"+
		"\u01a6\7\60\2\2\u01a6\u01a8\5E#\2\u01a7\u01a5\3\2\2\2\u01a7\u01a8\3\2"+
		"\2\2\u01a8\u01be\3\2\2\2\u01a9\u01af\5)\25\2\u01aa\u01ab\5\3\2\2\u01ab"+
		"\u01ac\5)\25\2\u01ac\u01ae\3\2\2\2\u01ad\u01aa\3\2\2\2\u01ae\u01b1\3\2"+
		"\2\2\u01af\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01bb\3\2\2\2\u01b1"+
		"\u01af\3\2\2\2\u01b2\u01b3\7\60\2\2\u01b3\u01b6\5S*\2\u01b4\u01b6\5U+"+
		"\2\u01b5\u01b2\3\2\2\2\u01b5\u01b4\3\2\2\2\u01b6\u01b9\3\2\2\2\u01b7\u01b8"+
		"\7\60\2\2\u01b8\u01ba\5E#\2\u01b9\u01b7\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba"+
		"\u01bc\3\2\2\2\u01bb\u01b5\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01be\3\2"+
		"\2\2\u01bd\u019f\3\2\2\2\u01bd\u01a4\3\2\2\2\u01bd\u01a9\3\2\2\2\u01be"+
		"X\3\2\2\2\u01bf\u01cc\3\2\2\2\u01c0\u01c1\5\67\34\2\u01c1\u01c2\5Y-\2"+
		"\u01c2\u01cc\3\2\2\2\u01c3\u01c4\5Q)\2\u01c4\u01c5\5Y-\2\u01c5\u01cc\3"+
		"\2\2\2\u01c6\u01c7\7}\2\2\u01c7\u01c8\5Y-\2\u01c8\u01c9\7\177\2\2\u01c9"+
		"\u01ca\5Y-\2\u01ca\u01cc\3\2\2\2\u01cb\u01bf\3\2\2\2\u01cb\u01c0\3\2\2"+
		"\2\u01cb\u01c3\3\2\2\2\u01cb\u01c6\3\2\2\2\u01ccZ\3\2\2\2\u01cd\u01ce"+
		"\t\13\2\2\u01ce\\\3\2\2\2\37\2\u00f6\u010d\u0114\u0119\u0121\u0133\u013a"+
		"\u013d\u0143\u014a\u0152\u015f\u0161\u0168\u016f\u0181\u0187\u018c\u0198"+
		"\u019d\u01a2\u01a7\u01af\u01b5\u01b9\u01bb\u01bd\u01cb\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}