// Generated from SettingsFile.g4 by ANTLR 4.7.2
package is.L42.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SettingsFileParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MSS=1, MMS=2, IMS=3, Num=4, Eq=5, ClassSep=6, CsP=7, URL=8, BlockComment=9, 
		LineComment=10, Whitespace=11;
	public static final int
		RULE_memOpt = 0, RULE_secOpt = 1, RULE_setting = 2, RULE_nudeSettings = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"memOpt", "secOpt", "setting", "nudeSettings"
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

	@Override
	public String getGrammarFileName() { return "SettingsFile.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SettingsFileParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MemOptContext extends ParserRuleContext {
		public TerminalNode Eq() { return getToken(SettingsFileParser.Eq, 0); }
		public TerminalNode Num() { return getToken(SettingsFileParser.Num, 0); }
		public TerminalNode MSS() { return getToken(SettingsFileParser.MSS, 0); }
		public TerminalNode MMS() { return getToken(SettingsFileParser.MMS, 0); }
		public TerminalNode IMS() { return getToken(SettingsFileParser.IMS, 0); }
		public MemOptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memOpt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).enterMemOpt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).exitMemOpt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SettingsFileVisitor ) return ((SettingsFileVisitor<? extends T>)visitor).visitMemOpt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemOptContext memOpt() throws RecognitionException {
		MemOptContext _localctx = new MemOptContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_memOpt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MSS) | (1L << MMS) | (1L << IMS))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(9);
			match(Eq);
			setState(10);
			match(Num);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SecOptContext extends ParserRuleContext {
		public TerminalNode CsP() { return getToken(SettingsFileParser.CsP, 0); }
		public TerminalNode Eq() { return getToken(SettingsFileParser.Eq, 0); }
		public List<TerminalNode> URL() { return getTokens(SettingsFileParser.URL); }
		public TerminalNode URL(int i) {
			return getToken(SettingsFileParser.URL, i);
		}
		public SecOptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_secOpt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).enterSecOpt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).exitSecOpt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SettingsFileVisitor ) return ((SettingsFileVisitor<? extends T>)visitor).visitSecOpt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SecOptContext secOpt() throws RecognitionException {
		SecOptContext _localctx = new SecOptContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_secOpt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			match(CsP);
			setState(13);
			match(Eq);
			setState(15); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(14);
				match(URL);
				}
				}
				setState(17); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==URL );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SettingContext extends ParserRuleContext {
		public MemOptContext memOpt() {
			return getRuleContext(MemOptContext.class,0);
		}
		public SecOptContext secOpt() {
			return getRuleContext(SecOptContext.class,0);
		}
		public SettingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).enterSetting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).exitSetting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SettingsFileVisitor ) return ((SettingsFileVisitor<? extends T>)visitor).visitSetting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SettingContext setting() throws RecognitionException {
		SettingContext _localctx = new SettingContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_setting);
		try {
			setState(21);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MSS:
			case MMS:
			case IMS:
				enterOuterAlt(_localctx, 1);
				{
				setState(19);
				memOpt();
				}
				break;
			case CsP:
				enterOuterAlt(_localctx, 2);
				{
				setState(20);
				secOpt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NudeSettingsContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SettingsFileParser.EOF, 0); }
		public List<SettingContext> setting() {
			return getRuleContexts(SettingContext.class);
		}
		public SettingContext setting(int i) {
			return getRuleContext(SettingContext.class,i);
		}
		public NudeSettingsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nudeSettings; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).enterNudeSettings(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SettingsFileListener ) ((SettingsFileListener)listener).exitNudeSettings(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SettingsFileVisitor ) return ((SettingsFileVisitor<? extends T>)visitor).visitNudeSettings(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NudeSettingsContext nudeSettings() throws RecognitionException {
		NudeSettingsContext _localctx = new NudeSettingsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_nudeSettings);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MSS) | (1L << MMS) | (1L << IMS) | (1L << CsP))) != 0)) {
				{
				{
				setState(23);
				setting();
				}
				}
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(29);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\r\"\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\3\3\3\3\3\6\3\22\n\3\r\3\16\3\23"+
		"\3\4\3\4\5\4\30\n\4\3\5\7\5\33\n\5\f\5\16\5\36\13\5\3\5\3\5\3\5\2\2\6"+
		"\2\4\6\b\2\3\3\2\3\5\2 \2\n\3\2\2\2\4\16\3\2\2\2\6\27\3\2\2\2\b\34\3\2"+
		"\2\2\n\13\t\2\2\2\13\f\7\7\2\2\f\r\7\6\2\2\r\3\3\2\2\2\16\17\7\t\2\2\17"+
		"\21\7\7\2\2\20\22\7\n\2\2\21\20\3\2\2\2\22\23\3\2\2\2\23\21\3\2\2\2\23"+
		"\24\3\2\2\2\24\5\3\2\2\2\25\30\5\2\2\2\26\30\5\4\3\2\27\25\3\2\2\2\27"+
		"\26\3\2\2\2\30\7\3\2\2\2\31\33\5\6\4\2\32\31\3\2\2\2\33\36\3\2\2\2\34"+
		"\32\3\2\2\2\34\35\3\2\2\2\35\37\3\2\2\2\36\34\3\2\2\2\37 \7\2\2\3 \t\3"+
		"\2\2\2\5\23\27\34";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}