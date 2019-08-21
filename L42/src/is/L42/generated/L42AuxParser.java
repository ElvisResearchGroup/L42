// Generated from L42Aux.g4 by ANTLR 4.7.2
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
public class L42AuxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, Dot=14, TDStart=15, ThisKw=16, 
		AnyKw=17, VoidKw=18, LibraryKw=19, C=20, MUniqueNum=21, MHash=22, X=23, 
		Doc=24, W=25, CHARInDoc=26;
	public static final int
		RULE_anyKw = 0, RULE_voidKw = 1, RULE_libraryKw = 2, RULE_thisKw = 3, 
		RULE_c = 4, RULE_csP = 5, RULE_path = 6, RULE_cs = 7, RULE_selector = 8, 
		RULE_pathSel = 9, RULE_pathSelX = 10, RULE_infoNorm = 11, RULE_infoTyped = 12, 
		RULE_info = 13, RULE_infoBody = 14, RULE_typeDep = 15, RULE_coherentDep = 16, 
		RULE_friends = 17, RULE_usedMethods = 18, RULE_privateSubtypes = 19, RULE_refined = 20, 
		RULE_canBeClassAny = 21, RULE_x = 22, RULE_m = 23, RULE_charInDoc = 24, 
		RULE_topDoc = 25, RULE_topDocText = 26, RULE_doc = 27;
	private static String[] makeRuleNames() {
		return new String[] {
			"anyKw", "voidKw", "libraryKw", "thisKw", "c", "csP", "path", "cs", "selector", 
			"pathSel", "pathSelX", "infoNorm", "infoTyped", "info", "infoBody", "typeDep", 
			"coherentDep", "friends", "usedMethods", "privateSubtypes", "refined", 
			"canBeClassAny", "x", "m", "charInDoc", "topDoc", "topDocText", "doc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'#norm{'", "'#typed{'", "'}'", "'typeDep='", "'coherentDep='", 
			"'friends='", "'usedMethods='", "'privateSubtypes='", "'refined='", "'canBeClassAny'", 
			"'{'", "'.'", "'@@'", null, "'Any'", "'Void'", "'Library'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "Dot", "TDStart", "ThisKw", "AnyKw", "VoidKw", "LibraryKw", 
			"C", "MUniqueNum", "MHash", "X", "Doc", "W", "CHARInDoc"
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
	public String getGrammarFileName() { return "L42Aux.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public L42AuxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AnyKwContext extends ParserRuleContext {
		public TerminalNode AnyKw() { return getToken(L42AuxParser.AnyKw, 0); }
		public AnyKwContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyKw; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterAnyKw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitAnyKw(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitAnyKw(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyKwContext anyKw() throws RecognitionException {
		AnyKwContext _localctx = new AnyKwContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_anyKw);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(AnyKw);
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

	public static class VoidKwContext extends ParserRuleContext {
		public TerminalNode VoidKw() { return getToken(L42AuxParser.VoidKw, 0); }
		public VoidKwContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_voidKw; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterVoidKw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitVoidKw(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitVoidKw(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VoidKwContext voidKw() throws RecognitionException {
		VoidKwContext _localctx = new VoidKwContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_voidKw);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(VoidKw);
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

	public static class LibraryKwContext extends ParserRuleContext {
		public TerminalNode LibraryKw() { return getToken(L42AuxParser.LibraryKw, 0); }
		public LibraryKwContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryKw; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterLibraryKw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitLibraryKw(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitLibraryKw(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryKwContext libraryKw() throws RecognitionException {
		LibraryKwContext _localctx = new LibraryKwContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_libraryKw);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(LibraryKw);
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

	public static class ThisKwContext extends ParserRuleContext {
		public TerminalNode ThisKw() { return getToken(L42AuxParser.ThisKw, 0); }
		public ThisKwContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_thisKw; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterThisKw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitThisKw(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitThisKw(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThisKwContext thisKw() throws RecognitionException {
		ThisKwContext _localctx = new ThisKwContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_thisKw);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(ThisKw);
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

	public static class CContext extends ParserRuleContext {
		public TerminalNode C() { return getToken(L42AuxParser.C, 0); }
		public CContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_c; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitC(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitC(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CContext c() throws RecognitionException {
		CContext _localctx = new CContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_c);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(C);
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

	public static class CsPContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(L42AuxParser.EOF, 0); }
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public CsContext cs() {
			return getRuleContext(CsContext.class,0);
		}
		public AnyKwContext anyKw() {
			return getRuleContext(AnyKwContext.class,0);
		}
		public VoidKwContext voidKw() {
			return getRuleContext(VoidKwContext.class,0);
		}
		public LibraryKwContext libraryKw() {
			return getRuleContext(LibraryKwContext.class,0);
		}
		public CsPContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_csP; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterCsP(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitCsP(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitCsP(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CsPContext csP() throws RecognitionException {
		CsPContext _localctx = new CsPContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_csP);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ThisKw:
				{
				setState(66);
				path();
				}
				break;
			case C:
				{
				setState(67);
				cs();
				}
				break;
			case AnyKw:
				{
				setState(68);
				anyKw();
				}
				break;
			case VoidKw:
				{
				setState(69);
				voidKw();
				}
				break;
			case LibraryKw:
				{
				setState(70);
				libraryKw();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(73);
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

	public static class PathContext extends ParserRuleContext {
		public ThisKwContext thisKw() {
			return getRuleContext(ThisKwContext.class,0);
		}
		public List<TerminalNode> Dot() { return getTokens(L42AuxParser.Dot); }
		public TerminalNode Dot(int i) {
			return getToken(L42AuxParser.Dot, i);
		}
		public List<CContext> c() {
			return getRuleContexts(CContext.class);
		}
		public CContext c(int i) {
			return getRuleContext(CContext.class,i);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_path);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			thisKw();
			setState(80);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(76);
					match(Dot);
					setState(77);
					c();
					}
					} 
				}
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
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

	public static class CsContext extends ParserRuleContext {
		public List<CContext> c() {
			return getRuleContexts(CContext.class);
		}
		public CContext c(int i) {
			return getRuleContext(CContext.class,i);
		}
		public List<TerminalNode> Dot() { return getTokens(L42AuxParser.Dot); }
		public TerminalNode Dot(int i) {
			return getToken(L42AuxParser.Dot, i);
		}
		public CsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterCs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitCs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitCs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CsContext cs() throws RecognitionException {
		CsContext _localctx = new CsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			c();
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Dot) {
				{
				{
				setState(84);
				match(Dot);
				setState(85);
				c();
				}
				}
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class SelectorContext extends ParserRuleContext {
		public MContext m() {
			return getRuleContext(MContext.class,0);
		}
		public List<XContext> x() {
			return getRuleContexts(XContext.class);
		}
		public XContext x(int i) {
			return getRuleContext(XContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public SelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorContext selector() throws RecognitionException {
		SelectorContext _localctx = new SelectorContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_selector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			m();
			setState(92);
			match(T__0);
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(93);
				x();
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==W) {
					{
					{
					setState(94);
					match(W);
					}
					}
					setState(99);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(105);
			match(T__1);
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

	public static class PathSelContext extends ParserRuleContext {
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TerminalNode Dot() { return getToken(L42AuxParser.Dot, 0); }
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public PathSelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterPathSel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitPathSel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitPathSel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathSelContext pathSel() throws RecognitionException {
		PathSelContext _localctx = new PathSelContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pathSel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			path();
			setState(108);
			match(Dot);
			setState(109);
			selector();
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

	public static class PathSelXContext extends ParserRuleContext {
		public PathSelContext pathSel() {
			return getRuleContext(PathSelContext.class,0);
		}
		public TerminalNode Dot() { return getToken(L42AuxParser.Dot, 0); }
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public PathSelXContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSelX; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterPathSelX(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitPathSelX(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitPathSelX(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathSelXContext pathSelX() throws RecognitionException {
		PathSelXContext _localctx = new PathSelXContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pathSelX);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			pathSel();
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Dot) {
				{
				setState(112);
				match(Dot);
				setState(113);
				x();
				}
			}

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

	public static class InfoNormContext extends ParserRuleContext {
		public InfoNormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infoNorm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterInfoNorm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitInfoNorm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitInfoNorm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfoNormContext infoNorm() throws RecognitionException {
		InfoNormContext _localctx = new InfoNormContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_infoNorm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(T__2);
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

	public static class InfoTypedContext extends ParserRuleContext {
		public InfoTypedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infoTyped; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterInfoTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitInfoTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitInfoTyped(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfoTypedContext infoTyped() throws RecognitionException {
		InfoTypedContext _localctx = new InfoTypedContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_infoTyped);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(T__3);
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

	public static class InfoContext extends ParserRuleContext {
		public InfoNormContext infoNorm() {
			return getRuleContext(InfoNormContext.class,0);
		}
		public InfoTypedContext infoTyped() {
			return getRuleContext(InfoTypedContext.class,0);
		}
		public List<InfoBodyContext> infoBody() {
			return getRuleContexts(InfoBodyContext.class);
		}
		public InfoBodyContext infoBody(int i) {
			return getRuleContext(InfoBodyContext.class,i);
		}
		public InfoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_info; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterInfo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitInfo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitInfo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfoContext info() throws RecognitionException {
		InfoContext _localctx = new InfoContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_info);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(120);
				infoNorm();
				}
				break;
			case T__3:
				{
				setState(121);
				infoTyped();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) {
				{
				{
				setState(124);
				infoBody();
				}
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(130);
			match(T__4);
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

	public static class InfoBodyContext extends ParserRuleContext {
		public TypeDepContext typeDep() {
			return getRuleContext(TypeDepContext.class,0);
		}
		public CoherentDepContext coherentDep() {
			return getRuleContext(CoherentDepContext.class,0);
		}
		public FriendsContext friends() {
			return getRuleContext(FriendsContext.class,0);
		}
		public UsedMethodsContext usedMethods() {
			return getRuleContext(UsedMethodsContext.class,0);
		}
		public PrivateSubtypesContext privateSubtypes() {
			return getRuleContext(PrivateSubtypesContext.class,0);
		}
		public RefinedContext refined() {
			return getRuleContext(RefinedContext.class,0);
		}
		public CanBeClassAnyContext canBeClassAny() {
			return getRuleContext(CanBeClassAnyContext.class,0);
		}
		public InfoBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infoBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterInfoBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitInfoBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitInfoBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfoBodyContext infoBody() throws RecognitionException {
		InfoBodyContext _localctx = new InfoBodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_infoBody);
		try {
			setState(139);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				typeDep();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				coherentDep();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(134);
				friends();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(135);
				usedMethods();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(136);
				privateSubtypes();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(137);
				refined();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 7);
				{
				setState(138);
				canBeClassAny();
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

	public static class TypeDepContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public TypeDepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterTypeDep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitTypeDep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitTypeDep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDepContext typeDep() throws RecognitionException {
		TypeDepContext _localctx = new TypeDepContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__5);
			setState(149); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(145);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(142);
						match(W);
						}
						}
						setState(147);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(148);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(151); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(153);
				match(W);
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class CoherentDepContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public CoherentDepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coherentDep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterCoherentDep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitCoherentDep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitCoherentDep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CoherentDepContext coherentDep() throws RecognitionException {
		CoherentDepContext _localctx = new CoherentDepContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_coherentDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(T__6);
			setState(167); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(163);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(160);
						match(W);
						}
						}
						setState(165);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(166);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(169); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(171);
				match(W);
				}
				}
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class FriendsContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public FriendsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_friends; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterFriends(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitFriends(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitFriends(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FriendsContext friends() throws RecognitionException {
		FriendsContext _localctx = new FriendsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_friends);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(T__7);
			setState(185); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(181);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(178);
						match(W);
						}
						}
						setState(183);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(184);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(187); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(189);
				match(W);
				}
				}
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class UsedMethodsContext extends ParserRuleContext {
		public List<PathSelContext> pathSel() {
			return getRuleContexts(PathSelContext.class);
		}
		public PathSelContext pathSel(int i) {
			return getRuleContext(PathSelContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public UsedMethodsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usedMethods; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterUsedMethods(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitUsedMethods(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitUsedMethods(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UsedMethodsContext usedMethods() throws RecognitionException {
		UsedMethodsContext _localctx = new UsedMethodsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_usedMethods);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(T__8);
			setState(203); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(199);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(196);
						match(W);
						}
						}
						setState(201);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(202);
					pathSel();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(205); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(207);
				match(W);
				}
				}
				setState(212);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class PrivateSubtypesContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public PrivateSubtypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privateSubtypes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterPrivateSubtypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitPrivateSubtypes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitPrivateSubtypes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivateSubtypesContext privateSubtypes() throws RecognitionException {
		PrivateSubtypesContext _localctx = new PrivateSubtypesContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_privateSubtypes);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(T__9);
			setState(221); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(217);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(214);
						match(W);
						}
						}
						setState(219);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(220);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(223); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(225);
				match(W);
				}
				}
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class RefinedContext extends ParserRuleContext {
		public List<SelectorContext> selector() {
			return getRuleContexts(SelectorContext.class);
		}
		public SelectorContext selector(int i) {
			return getRuleContext(SelectorContext.class,i);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
		}
		public RefinedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refined; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterRefined(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitRefined(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitRefined(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefinedContext refined() throws RecognitionException {
		RefinedContext _localctx = new RefinedContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_refined);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(T__10);
			setState(239); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(235);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(232);
						match(W);
						}
						}
						setState(237);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(238);
					selector();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(241); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(243);
				match(W);
				}
				}
				setState(248);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class CanBeClassAnyContext extends ParserRuleContext {
		public CanBeClassAnyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_canBeClassAny; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterCanBeClassAny(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitCanBeClassAny(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitCanBeClassAny(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CanBeClassAnyContext canBeClassAny() throws RecognitionException {
		CanBeClassAnyContext _localctx = new CanBeClassAnyContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_canBeClassAny);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(T__11);
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

	public static class XContext extends ParserRuleContext {
		public TerminalNode X() { return getToken(L42AuxParser.X, 0); }
		public XContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_x; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterX(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitX(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitX(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XContext x() throws RecognitionException {
		XContext _localctx = new XContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_x);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(X);
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

	public static class MContext extends ParserRuleContext {
		public TerminalNode MUniqueNum() { return getToken(L42AuxParser.MUniqueNum, 0); }
		public TerminalNode MHash() { return getToken(L42AuxParser.MHash, 0); }
		public TerminalNode X() { return getToken(L42AuxParser.X, 0); }
		public MContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_m; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterM(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitM(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitM(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MContext m() throws RecognitionException {
		MContext _localctx = new MContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_m);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUniqueNum) | (1L << MHash) | (1L << X))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class CharInDocContext extends ParserRuleContext {
		public TerminalNode CHARInDoc() { return getToken(L42AuxParser.CHARInDoc, 0); }
		public TerminalNode MUniqueNum() { return getToken(L42AuxParser.MUniqueNum, 0); }
		public TerminalNode MHash() { return getToken(L42AuxParser.MHash, 0); }
		public TerminalNode X() { return getToken(L42AuxParser.X, 0); }
		public TerminalNode Dot() { return getToken(L42AuxParser.Dot, 0); }
		public TerminalNode ThisKw() { return getToken(L42AuxParser.ThisKw, 0); }
		public TerminalNode C() { return getToken(L42AuxParser.C, 0); }
		public TerminalNode AnyKw() { return getToken(L42AuxParser.AnyKw, 0); }
		public TerminalNode VoidKw() { return getToken(L42AuxParser.VoidKw, 0); }
		public TerminalNode LibraryKw() { return getToken(L42AuxParser.LibraryKw, 0); }
		public TerminalNode W() { return getToken(L42AuxParser.W, 0); }
		public CharInDocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charInDoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterCharInDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitCharInDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitCharInDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharInDocContext charInDoc() throws RecognitionException {
		CharInDocContext _localctx = new CharInDocContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_charInDoc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class TopDocContext extends ParserRuleContext {
		public TerminalNode TDStart() { return getToken(L42AuxParser.TDStart, 0); }
		public PathSelXContext pathSelX() {
			return getRuleContext(PathSelXContext.class,0);
		}
		public TerminalNode EOF() { return getToken(L42AuxParser.EOF, 0); }
		public TopDocTextContext topDocText() {
			return getRuleContext(TopDocTextContext.class,0);
		}
		public TopDocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_topDoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterTopDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitTopDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitTopDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TopDocContext topDoc() throws RecognitionException {
		TopDocContext _localctx = new TopDocContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_topDoc);
		int _la;
		try {
			setState(270);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				match(TDStart);
				setState(258);
				pathSelX();
				setState(259);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(261);
				match(TDStart);
				setState(263);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ThisKw) {
					{
					setState(262);
					pathSelX();
					}
				}

				setState(265);
				match(T__12);
				setState(266);
				topDocText();
				setState(267);
				match(T__4);
				setState(268);
				match(EOF);
				}
				break;
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

	public static class TopDocTextContext extends ParserRuleContext {
		public List<CharInDocContext> charInDoc() {
			return getRuleContexts(CharInDocContext.class);
		}
		public CharInDocContext charInDoc(int i) {
			return getRuleContext(CharInDocContext.class,i);
		}
		public DocContext doc() {
			return getRuleContext(DocContext.class,0);
		}
		public List<TopDocTextContext> topDocText() {
			return getRuleContexts(TopDocTextContext.class);
		}
		public TopDocTextContext topDocText(int i) {
			return getRuleContext(TopDocTextContext.class,i);
		}
		public TopDocTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_topDocText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterTopDocText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitTopDocText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitTopDocText(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TopDocTextContext topDocText() throws RecognitionException {
		TopDocTextContext _localctx = new TopDocTextContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_topDocText);
		int _la;
		try {
			setState(298);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(275);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(272);
					charInDoc();
					}
					}
					setState(277);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(278);
					charInDoc();
					}
					}
					setState(283);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(284);
				doc();
				setState(285);
				topDocText();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(290);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(287);
					charInDoc();
					}
					}
					setState(292);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(293);
				match(T__12);
				setState(294);
				topDocText();
				setState(295);
				match(T__4);
				setState(296);
				topDocText();
				}
				break;
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

	public static class DocContext extends ParserRuleContext {
		public TerminalNode Doc() { return getToken(L42AuxParser.Doc, 0); }
		public DocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocContext doc() throws RecognitionException {
		DocContext _localctx = new DocContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_doc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			match(Doc);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34\u0131\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7J\n\7\3\7\3\7\3\b\3\b\3\b\7\bQ\n\b\f"+
		"\b\16\bT\13\b\3\t\3\t\3\t\7\tY\n\t\f\t\16\t\\\13\t\3\n\3\n\3\n\3\n\7\n"+
		"b\n\n\f\n\16\ne\13\n\7\ng\n\n\f\n\16\nj\13\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\5\fu\n\f\3\r\3\r\3\16\3\16\3\17\3\17\5\17}\n\17\3\17\7"+
		"\17\u0080\n\17\f\17\16\17\u0083\13\17\3\17\3\17\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\5\20\u008e\n\20\3\21\3\21\7\21\u0092\n\21\f\21\16\21\u0095"+
		"\13\21\3\21\6\21\u0098\n\21\r\21\16\21\u0099\3\21\7\21\u009d\n\21\f\21"+
		"\16\21\u00a0\13\21\3\22\3\22\7\22\u00a4\n\22\f\22\16\22\u00a7\13\22\3"+
		"\22\6\22\u00aa\n\22\r\22\16\22\u00ab\3\22\7\22\u00af\n\22\f\22\16\22\u00b2"+
		"\13\22\3\23\3\23\7\23\u00b6\n\23\f\23\16\23\u00b9\13\23\3\23\6\23\u00bc"+
		"\n\23\r\23\16\23\u00bd\3\23\7\23\u00c1\n\23\f\23\16\23\u00c4\13\23\3\24"+
		"\3\24\7\24\u00c8\n\24\f\24\16\24\u00cb\13\24\3\24\6\24\u00ce\n\24\r\24"+
		"\16\24\u00cf\3\24\7\24\u00d3\n\24\f\24\16\24\u00d6\13\24\3\25\3\25\7\25"+
		"\u00da\n\25\f\25\16\25\u00dd\13\25\3\25\6\25\u00e0\n\25\r\25\16\25\u00e1"+
		"\3\25\7\25\u00e5\n\25\f\25\16\25\u00e8\13\25\3\26\3\26\7\26\u00ec\n\26"+
		"\f\26\16\26\u00ef\13\26\3\26\6\26\u00f2\n\26\r\26\16\26\u00f3\3\26\7\26"+
		"\u00f7\n\26\f\26\16\26\u00fa\13\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u010a\n\33\3\33\3\33\3\33\3\33"+
		"\3\33\5\33\u0111\n\33\3\34\7\34\u0114\n\34\f\34\16\34\u0117\13\34\3\34"+
		"\7\34\u011a\n\34\f\34\16\34\u011d\13\34\3\34\3\34\3\34\3\34\7\34\u0123"+
		"\n\34\f\34\16\34\u0126\13\34\3\34\3\34\3\34\3\34\3\34\5\34\u012d\n\34"+
		"\3\35\3\35\3\35\2\2\36\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,"+
		".\60\62\64\668\2\4\3\2\27\31\5\2\20\20\22\31\33\34\2\u013e\2:\3\2\2\2"+
		"\4<\3\2\2\2\6>\3\2\2\2\b@\3\2\2\2\nB\3\2\2\2\fI\3\2\2\2\16M\3\2\2\2\20"+
		"U\3\2\2\2\22]\3\2\2\2\24m\3\2\2\2\26q\3\2\2\2\30v\3\2\2\2\32x\3\2\2\2"+
		"\34|\3\2\2\2\36\u008d\3\2\2\2 \u008f\3\2\2\2\"\u00a1\3\2\2\2$\u00b3\3"+
		"\2\2\2&\u00c5\3\2\2\2(\u00d7\3\2\2\2*\u00e9\3\2\2\2,\u00fb\3\2\2\2.\u00fd"+
		"\3\2\2\2\60\u00ff\3\2\2\2\62\u0101\3\2\2\2\64\u0110\3\2\2\2\66\u012c\3"+
		"\2\2\28\u012e\3\2\2\2:;\7\23\2\2;\3\3\2\2\2<=\7\24\2\2=\5\3\2\2\2>?\7"+
		"\25\2\2?\7\3\2\2\2@A\7\22\2\2A\t\3\2\2\2BC\7\26\2\2C\13\3\2\2\2DJ\5\16"+
		"\b\2EJ\5\20\t\2FJ\5\2\2\2GJ\5\4\3\2HJ\5\6\4\2ID\3\2\2\2IE\3\2\2\2IF\3"+
		"\2\2\2IG\3\2\2\2IH\3\2\2\2JK\3\2\2\2KL\7\2\2\3L\r\3\2\2\2MR\5\b\5\2NO"+
		"\7\20\2\2OQ\5\n\6\2PN\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2S\17\3\2\2"+
		"\2TR\3\2\2\2UZ\5\n\6\2VW\7\20\2\2WY\5\n\6\2XV\3\2\2\2Y\\\3\2\2\2ZX\3\2"+
		"\2\2Z[\3\2\2\2[\21\3\2\2\2\\Z\3\2\2\2]^\5\60\31\2^h\7\3\2\2_c\5.\30\2"+
		"`b\7\33\2\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2dg\3\2\2\2ec\3\2\2"+
		"\2f_\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2ik\3\2\2\2jh\3\2\2\2kl\7\4\2"+
		"\2l\23\3\2\2\2mn\5\16\b\2no\7\20\2\2op\5\22\n\2p\25\3\2\2\2qt\5\24\13"+
		"\2rs\7\20\2\2su\5.\30\2tr\3\2\2\2tu\3\2\2\2u\27\3\2\2\2vw\7\5\2\2w\31"+
		"\3\2\2\2xy\7\6\2\2y\33\3\2\2\2z}\5\30\r\2{}\5\32\16\2|z\3\2\2\2|{\3\2"+
		"\2\2}\u0081\3\2\2\2~\u0080\5\36\20\2\177~\3\2\2\2\u0080\u0083\3\2\2\2"+
		"\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0084\3\2\2\2\u0083\u0081"+
		"\3\2\2\2\u0084\u0085\7\7\2\2\u0085\35\3\2\2\2\u0086\u008e\5 \21\2\u0087"+
		"\u008e\5\"\22\2\u0088\u008e\5$\23\2\u0089\u008e\5&\24\2\u008a\u008e\5"+
		"(\25\2\u008b\u008e\5*\26\2\u008c\u008e\5,\27\2\u008d\u0086\3\2\2\2\u008d"+
		"\u0087\3\2\2\2\u008d\u0088\3\2\2\2\u008d\u0089\3\2\2\2\u008d\u008a\3\2"+
		"\2\2\u008d\u008b\3\2\2\2\u008d\u008c\3\2\2\2\u008e\37\3\2\2\2\u008f\u0097"+
		"\7\b\2\2\u0090\u0092\7\33\2\2\u0091\u0090\3\2\2\2\u0092\u0095\3\2\2\2"+
		"\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0096\3\2\2\2\u0095\u0093"+
		"\3\2\2\2\u0096\u0098\5\16\b\2\u0097\u0093\3\2\2\2\u0098\u0099\3\2\2\2"+
		"\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009e\3\2\2\2\u009b\u009d"+
		"\7\33\2\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3\2\2\2"+
		"\u009e\u009f\3\2\2\2\u009f!\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a9\7"+
		"\t\2\2\u00a2\u00a4\7\33\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5"+
		"\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a5\3\2"+
		"\2\2\u00a8\u00aa\5\16\b\2\u00a9\u00a5\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00b0\3\2\2\2\u00ad\u00af\7\33"+
		"\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b2\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1#\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00bb\7\n\2\2"+
		"\u00b4\u00b6\7\33\2\2\u00b5\u00b4\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5"+
		"\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00ba\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba"+
		"\u00bc\5\16\b\2\u00bb\u00b7\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bb\3"+
		"\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c2\3\2\2\2\u00bf\u00c1\7\33\2\2\u00c0"+
		"\u00bf\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2"+
		"\2\2\u00c3%\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00cd\7\13\2\2\u00c6\u00c8"+
		"\7\33\2\2\u00c7\u00c6\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9\u00c7\3\2\2\2"+
		"\u00c9\u00ca\3\2\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00ce"+
		"\5\24\13\2\u00cd\u00c9\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00cd\3\2\2\2"+
		"\u00cf\u00d0\3\2\2\2\u00d0\u00d4\3\2\2\2\u00d1\u00d3\7\33\2\2\u00d2\u00d1"+
		"\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"+
		"\'\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00df\7\f\2\2\u00d8\u00da\7\33\2"+
		"\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc"+
		"\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00e0\5\16\b\2"+
		"\u00df\u00db\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1\u00e2"+
		"\3\2\2\2\u00e2\u00e6\3\2\2\2\u00e3\u00e5\7\33\2\2\u00e4\u00e3\3\2\2\2"+
		"\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7)\3"+
		"\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00f1\7\r\2\2\u00ea\u00ec\7\33\2\2\u00eb"+
		"\u00ea\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2"+
		"\2\2\u00ee\u00f0\3\2\2\2\u00ef\u00ed\3\2\2\2\u00f0\u00f2\5\22\n\2\u00f1"+
		"\u00ed\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2"+
		"\2\2\u00f4\u00f8\3\2\2\2\u00f5\u00f7\7\33\2\2\u00f6\u00f5\3\2\2\2\u00f7"+
		"\u00fa\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9+\3\2\2\2"+
		"\u00fa\u00f8\3\2\2\2\u00fb\u00fc\7\16\2\2\u00fc-\3\2\2\2\u00fd\u00fe\7"+
		"\31\2\2\u00fe/\3\2\2\2\u00ff\u0100\t\2\2\2\u0100\61\3\2\2\2\u0101\u0102"+
		"\t\3\2\2\u0102\63\3\2\2\2\u0103\u0104\7\21\2\2\u0104\u0105\5\26\f\2\u0105"+
		"\u0106\7\2\2\3\u0106\u0111\3\2\2\2\u0107\u0109\7\21\2\2\u0108\u010a\5"+
		"\26\f\2\u0109\u0108\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u010b\3\2\2\2\u010b"+
		"\u010c\7\17\2\2\u010c\u010d\5\66\34\2\u010d\u010e\7\7\2\2\u010e\u010f"+
		"\7\2\2\3\u010f\u0111\3\2\2\2\u0110\u0103\3\2\2\2\u0110\u0107\3\2\2\2\u0111"+
		"\65\3\2\2\2\u0112\u0114\5\62\32\2\u0113\u0112\3\2\2\2\u0114\u0117\3\2"+
		"\2\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u012d\3\2\2\2\u0117"+
		"\u0115\3\2\2\2\u0118\u011a\5\62\32\2\u0119\u0118\3\2\2\2\u011a\u011d\3"+
		"\2\2\2\u011b\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011e\3\2\2\2\u011d"+
		"\u011b\3\2\2\2\u011e\u011f\58\35\2\u011f\u0120\5\66\34\2\u0120\u012d\3"+
		"\2\2\2\u0121\u0123\5\62\32\2\u0122\u0121\3\2\2\2\u0123\u0126\3\2\2\2\u0124"+
		"\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0127\3\2\2\2\u0126\u0124\3\2"+
		"\2\2\u0127\u0128\7\17\2\2\u0128\u0129\5\66\34\2\u0129\u012a\7\7\2\2\u012a"+
		"\u012b\5\66\34\2\u012b\u012d\3\2\2\2\u012c\u0115\3\2\2\2\u012c\u011b\3"+
		"\2\2\2\u012c\u0124\3\2\2\2\u012d\67\3\2\2\2\u012e\u012f\7\32\2\2\u012f"+
		"9\3\2\2\2#IRZcht|\u0081\u008d\u0093\u0099\u009e\u00a5\u00ab\u00b0\u00b7"+
		"\u00bd\u00c2\u00c9\u00cf\u00d4\u00db\u00e1\u00e6\u00ed\u00f3\u00f8\u0109"+
		"\u0110\u0115\u011b\u0124\u012c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}