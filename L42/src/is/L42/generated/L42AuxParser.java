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
		RULE_c = 4, RULE_csP = 5, RULE_nudeCsP = 6, RULE_path = 7, RULE_cs = 8, 
		RULE_selector = 9, RULE_pathSel = 10, RULE_pathSelX = 11, RULE_infoNorm = 12, 
		RULE_infoTyped = 13, RULE_info = 14, RULE_infoBody = 15, RULE_typeDep = 16, 
		RULE_coherentDep = 17, RULE_friends = 18, RULE_usedMethods = 19, RULE_privateSubtypes = 20, 
		RULE_refined = 21, RULE_canBeClassAny = 22, RULE_x = 23, RULE_m = 24, 
		RULE_charInDoc = 25, RULE_topDoc = 26, RULE_topDocText = 27, RULE_doc = 28;
	private static String[] makeRuleNames() {
		return new String[] {
			"anyKw", "voidKw", "libraryKw", "thisKw", "c", "csP", "nudeCsP", "path", 
			"cs", "selector", "pathSel", "pathSelX", "infoNorm", "infoTyped", "info", 
			"infoBody", "typeDep", "coherentDep", "friends", "usedMethods", "privateSubtypes", 
			"refined", "canBeClassAny", "x", "m", "charInDoc", "topDoc", "topDocText", 
			"doc"
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
			setState(58);
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
			setState(60);
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
			setState(62);
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
			setState(64);
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
			setState(66);
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
			setState(73);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ThisKw:
				{
				setState(68);
				path();
				}
				break;
			case C:
				{
				setState(69);
				cs();
				}
				break;
			case AnyKw:
				{
				setState(70);
				anyKw();
				}
				break;
			case VoidKw:
				{
				setState(71);
				voidKw();
				}
				break;
			case LibraryKw:
				{
				setState(72);
				libraryKw();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class NudeCsPContext extends ParserRuleContext {
		public CsPContext csP() {
			return getRuleContext(CsPContext.class,0);
		}
		public TerminalNode EOF() { return getToken(L42AuxParser.EOF, 0); }
		public NudeCsPContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nudeCsP; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterNudeCsP(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitNudeCsP(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitNudeCsP(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NudeCsPContext nudeCsP() throws RecognitionException {
		NudeCsPContext _localctx = new NudeCsPContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_nudeCsP);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			csP();
			setState(76);
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
		enterRule(_localctx, 14, RULE_path);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			thisKw();
			setState(83);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(79);
					match(Dot);
					setState(80);
					c();
					}
					} 
				}
				setState(85);
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
		enterRule(_localctx, 16, RULE_cs);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			c();
			setState(91);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(87);
					match(Dot);
					setState(88);
					c();
					}
					} 
				}
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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
		enterRule(_localctx, 18, RULE_selector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUniqueNum) | (1L << MHash) | (1L << X))) != 0)) {
				{
				setState(94);
				m();
				}
			}

			setState(97);
			match(T__0);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(98);
				x();
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==W) {
					{
					{
					setState(99);
					match(W);
					}
					}
					setState(104);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
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
		public CsPContext csP() {
			return getRuleContext(CsPContext.class,0);
		}
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public TerminalNode Dot() { return getToken(L42AuxParser.Dot, 0); }
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
		enterRule(_localctx, 20, RULE_pathSel);
		int _la;
		try {
			setState(120);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ThisKw:
			case AnyKw:
			case VoidKw:
			case LibraryKw:
			case C:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				csP();
				setState(117);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(114);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Dot) {
						{
						setState(113);
						match(Dot);
						}
					}

					setState(116);
					selector();
					}
					break;
				}
				}
				break;
			case T__0:
			case MUniqueNum:
			case MHash:
			case X:
				enterOuterAlt(_localctx, 2);
				{
				setState(119);
				selector();
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
		enterRule(_localctx, 22, RULE_pathSelX);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			pathSel();
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Dot) {
				{
				setState(123);
				match(Dot);
				setState(124);
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
		enterRule(_localctx, 24, RULE_infoNorm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
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
		enterRule(_localctx, 26, RULE_infoTyped);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
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
		enterRule(_localctx, 28, RULE_info);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(131);
				infoNorm();
				}
				break;
			case T__3:
				{
				setState(132);
				infoTyped();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) {
				{
				{
				setState(135);
				infoBody();
				}
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(141);
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
		enterRule(_localctx, 30, RULE_infoBody);
		try {
			setState(150);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				typeDep();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(144);
				coherentDep();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(145);
				friends();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(146);
				usedMethods();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(147);
				privateSubtypes();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(148);
				refined();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 7);
				{
				setState(149);
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
		enterRule(_localctx, 32, RULE_typeDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__5);
			setState(160); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(159);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(162); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(164);
				match(W);
				}
				}
				setState(169);
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
		enterRule(_localctx, 34, RULE_coherentDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			match(T__6);
			setState(178); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(177);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(180); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(182);
				match(W);
				}
				}
				setState(187);
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
		enterRule(_localctx, 36, RULE_friends);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(T__7);
			setState(196); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(195);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(198); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(200);
				match(W);
				}
				}
				setState(205);
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
		enterRule(_localctx, 38, RULE_usedMethods);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(T__8);
			setState(214); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(213);
					pathSel();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(216); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(218);
				match(W);
				}
				}
				setState(223);
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
		enterRule(_localctx, 40, RULE_privateSubtypes);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(T__9);
			setState(232); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(231);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(234); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(236);
				match(W);
				}
				}
				setState(241);
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
		enterRule(_localctx, 42, RULE_refined);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(T__10);
			setState(250); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
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
					setState(249);
					selector();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(252); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(254);
				match(W);
				}
				}
				setState(259);
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
		enterRule(_localctx, 44, RULE_canBeClassAny);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
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
		enterRule(_localctx, 46, RULE_x);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
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
		enterRule(_localctx, 48, RULE_m);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
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
		enterRule(_localctx, 50, RULE_charInDoc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
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
		enterRule(_localctx, 52, RULE_topDoc);
		int _la;
		try {
			setState(281);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(268);
				match(TDStart);
				setState(269);
				pathSelX();
				setState(270);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(272);
				match(TDStart);
				setState(274);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X))) != 0)) {
					{
					setState(273);
					pathSelX();
					}
				}

				setState(276);
				match(T__12);
				setState(277);
				topDocText();
				setState(278);
				match(T__4);
				setState(279);
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
		enterRule(_localctx, 54, RULE_topDocText);
		int _la;
		try {
			setState(309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(283);
					charInDoc();
					}
					}
					setState(288);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(289);
					charInDoc();
					}
					}
					setState(294);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(295);
				doc();
				setState(296);
				topDocText();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(301);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(298);
					charInDoc();
					}
					}
					setState(303);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(304);
				match(T__12);
				setState(305);
				topDocText();
				setState(306);
				match(T__4);
				setState(307);
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
		enterRule(_localctx, 56, RULE_doc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34\u013c\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7L\n\7\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\7\tT\n\t\f\t\16\tW\13\t\3\n\3\n\3\n\7\n\\\n\n\f\n\16\n_\13\n\3\13"+
		"\5\13b\n\13\3\13\3\13\3\13\7\13g\n\13\f\13\16\13j\13\13\7\13l\n\13\f\13"+
		"\16\13o\13\13\3\13\3\13\3\f\3\f\5\fu\n\f\3\f\5\fx\n\f\3\f\5\f{\n\f\3\r"+
		"\3\r\3\r\5\r\u0080\n\r\3\16\3\16\3\17\3\17\3\20\3\20\5\20\u0088\n\20\3"+
		"\20\7\20\u008b\n\20\f\20\16\20\u008e\13\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\5\21\u0099\n\21\3\22\3\22\7\22\u009d\n\22\f\22\16\22"+
		"\u00a0\13\22\3\22\6\22\u00a3\n\22\r\22\16\22\u00a4\3\22\7\22\u00a8\n\22"+
		"\f\22\16\22\u00ab\13\22\3\23\3\23\7\23\u00af\n\23\f\23\16\23\u00b2\13"+
		"\23\3\23\6\23\u00b5\n\23\r\23\16\23\u00b6\3\23\7\23\u00ba\n\23\f\23\16"+
		"\23\u00bd\13\23\3\24\3\24\7\24\u00c1\n\24\f\24\16\24\u00c4\13\24\3\24"+
		"\6\24\u00c7\n\24\r\24\16\24\u00c8\3\24\7\24\u00cc\n\24\f\24\16\24\u00cf"+
		"\13\24\3\25\3\25\7\25\u00d3\n\25\f\25\16\25\u00d6\13\25\3\25\6\25\u00d9"+
		"\n\25\r\25\16\25\u00da\3\25\7\25\u00de\n\25\f\25\16\25\u00e1\13\25\3\26"+
		"\3\26\7\26\u00e5\n\26\f\26\16\26\u00e8\13\26\3\26\6\26\u00eb\n\26\r\26"+
		"\16\26\u00ec\3\26\7\26\u00f0\n\26\f\26\16\26\u00f3\13\26\3\27\3\27\7\27"+
		"\u00f7\n\27\f\27\16\27\u00fa\13\27\3\27\6\27\u00fd\n\27\r\27\16\27\u00fe"+
		"\3\27\7\27\u0102\n\27\f\27\16\27\u0105\13\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u0115\n\34\3\34\3\34"+
		"\3\34\3\34\3\34\5\34\u011c\n\34\3\35\7\35\u011f\n\35\f\35\16\35\u0122"+
		"\13\35\3\35\7\35\u0125\n\35\f\35\16\35\u0128\13\35\3\35\3\35\3\35\3\35"+
		"\7\35\u012e\n\35\f\35\16\35\u0131\13\35\3\35\3\35\3\35\3\35\3\35\5\35"+
		"\u0138\n\35\3\36\3\36\3\36\2\2\37\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:\2\4\3\2\27\31\5\2\20\20\22\31\33\34\2\u014c"+
		"\2<\3\2\2\2\4>\3\2\2\2\6@\3\2\2\2\bB\3\2\2\2\nD\3\2\2\2\fK\3\2\2\2\16"+
		"M\3\2\2\2\20P\3\2\2\2\22X\3\2\2\2\24a\3\2\2\2\26z\3\2\2\2\30|\3\2\2\2"+
		"\32\u0081\3\2\2\2\34\u0083\3\2\2\2\36\u0087\3\2\2\2 \u0098\3\2\2\2\"\u009a"+
		"\3\2\2\2$\u00ac\3\2\2\2&\u00be\3\2\2\2(\u00d0\3\2\2\2*\u00e2\3\2\2\2,"+
		"\u00f4\3\2\2\2.\u0106\3\2\2\2\60\u0108\3\2\2\2\62\u010a\3\2\2\2\64\u010c"+
		"\3\2\2\2\66\u011b\3\2\2\28\u0137\3\2\2\2:\u0139\3\2\2\2<=\7\23\2\2=\3"+
		"\3\2\2\2>?\7\24\2\2?\5\3\2\2\2@A\7\25\2\2A\7\3\2\2\2BC\7\22\2\2C\t\3\2"+
		"\2\2DE\7\26\2\2E\13\3\2\2\2FL\5\20\t\2GL\5\22\n\2HL\5\2\2\2IL\5\4\3\2"+
		"JL\5\6\4\2KF\3\2\2\2KG\3\2\2\2KH\3\2\2\2KI\3\2\2\2KJ\3\2\2\2L\r\3\2\2"+
		"\2MN\5\f\7\2NO\7\2\2\3O\17\3\2\2\2PU\5\b\5\2QR\7\20\2\2RT\5\n\6\2SQ\3"+
		"\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2V\21\3\2\2\2WU\3\2\2\2X]\5\n\6\2Y"+
		"Z\7\20\2\2Z\\\5\n\6\2[Y\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\23\3\2"+
		"\2\2_]\3\2\2\2`b\5\62\32\2a`\3\2\2\2ab\3\2\2\2bc\3\2\2\2cm\7\3\2\2dh\5"+
		"\60\31\2eg\7\33\2\2fe\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2il\3\2\2\2"+
		"jh\3\2\2\2kd\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2np\3\2\2\2om\3\2\2\2"+
		"pq\7\4\2\2q\25\3\2\2\2rw\5\f\7\2su\7\20\2\2ts\3\2\2\2tu\3\2\2\2uv\3\2"+
		"\2\2vx\5\24\13\2wt\3\2\2\2wx\3\2\2\2x{\3\2\2\2y{\5\24\13\2zr\3\2\2\2z"+
		"y\3\2\2\2{\27\3\2\2\2|\177\5\26\f\2}~\7\20\2\2~\u0080\5\60\31\2\177}\3"+
		"\2\2\2\177\u0080\3\2\2\2\u0080\31\3\2\2\2\u0081\u0082\7\5\2\2\u0082\33"+
		"\3\2\2\2\u0083\u0084\7\6\2\2\u0084\35\3\2\2\2\u0085\u0088\5\32\16\2\u0086"+
		"\u0088\5\34\17\2\u0087\u0085\3\2\2\2\u0087\u0086\3\2\2\2\u0088\u008c\3"+
		"\2\2\2\u0089\u008b\5 \21\2\u008a\u0089\3\2\2\2\u008b\u008e\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008f\3\2\2\2\u008e\u008c\3\2"+
		"\2\2\u008f\u0090\7\7\2\2\u0090\37\3\2\2\2\u0091\u0099\5\"\22\2\u0092\u0099"+
		"\5$\23\2\u0093\u0099\5&\24\2\u0094\u0099\5(\25\2\u0095\u0099\5*\26\2\u0096"+
		"\u0099\5,\27\2\u0097\u0099\5.\30\2\u0098\u0091\3\2\2\2\u0098\u0092\3\2"+
		"\2\2\u0098\u0093\3\2\2\2\u0098\u0094\3\2\2\2\u0098\u0095\3\2\2\2\u0098"+
		"\u0096\3\2\2\2\u0098\u0097\3\2\2\2\u0099!\3\2\2\2\u009a\u00a2\7\b\2\2"+
		"\u009b\u009d\7\33\2\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c"+
		"\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1"+
		"\u00a3\5\20\t\2\u00a2\u009e\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a2\3"+
		"\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a9\3\2\2\2\u00a6\u00a8\7\33\2\2\u00a7"+
		"\u00a6\3\2\2\2\u00a8\u00ab\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2"+
		"\2\2\u00aa#\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ac\u00b4\7\t\2\2\u00ad\u00af"+
		"\7\33\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b2\3\2\2\2\u00b0\u00ae\3\2\2\2"+
		"\u00b0\u00b1\3\2\2\2\u00b1\u00b3\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00b5"+
		"\5\20\t\2\u00b4\u00b0\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2"+
		"\u00b6\u00b7\3\2\2\2\u00b7\u00bb\3\2\2\2\u00b8\u00ba\7\33\2\2\u00b9\u00b8"+
		"\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc"+
		"%\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00c6\7\n\2\2\u00bf\u00c1\7\33\2\2"+
		"\u00c0\u00bf\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3"+
		"\3\2\2\2\u00c3\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c7\5\20\t\2"+
		"\u00c6\u00c2\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9"+
		"\3\2\2\2\u00c9\u00cd\3\2\2\2\u00ca\u00cc\7\33\2\2\u00cb\u00ca\3\2\2\2"+
		"\u00cc\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\'\3"+
		"\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d8\7\13\2\2\u00d1\u00d3\7\33\2\2\u00d2"+
		"\u00d1\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2"+
		"\2\2\u00d5\u00d7\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d9\5\26\f\2\u00d8"+
		"\u00d4\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2"+
		"\2\2\u00db\u00df\3\2\2\2\u00dc\u00de\7\33\2\2\u00dd\u00dc\3\2\2\2\u00de"+
		"\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0)\3\2\2\2"+
		"\u00e1\u00df\3\2\2\2\u00e2\u00ea\7\f\2\2\u00e3\u00e5\7\33\2\2\u00e4\u00e3"+
		"\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"\u00e9\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00eb\5\20\t\2\u00ea\u00e6\3"+
		"\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"\u00f1\3\2\2\2\u00ee\u00f0\7\33\2\2\u00ef\u00ee\3\2\2\2\u00f0\u00f3\3"+
		"\2\2\2\u00f1\u00ef\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2+\3\2\2\2\u00f3\u00f1"+
		"\3\2\2\2\u00f4\u00fc\7\r\2\2\u00f5\u00f7\7\33\2\2\u00f6\u00f5\3\2\2\2"+
		"\u00f7\u00fa\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fb"+
		"\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fb\u00fd\5\24\13\2\u00fc\u00f8\3\2\2\2"+
		"\u00fd\u00fe\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0103"+
		"\3\2\2\2\u0100\u0102\7\33\2\2\u0101\u0100\3\2\2\2\u0102\u0105\3\2\2\2"+
		"\u0103\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104-\3\2\2\2\u0105\u0103\3"+
		"\2\2\2\u0106\u0107\7\16\2\2\u0107/\3\2\2\2\u0108\u0109\7\31\2\2\u0109"+
		"\61\3\2\2\2\u010a\u010b\t\2\2\2\u010b\63\3\2\2\2\u010c\u010d\t\3\2\2\u010d"+
		"\65\3\2\2\2\u010e\u010f\7\21\2\2\u010f\u0110\5\30\r\2\u0110\u0111\7\2"+
		"\2\3\u0111\u011c\3\2\2\2\u0112\u0114\7\21\2\2\u0113\u0115\5\30\r\2\u0114"+
		"\u0113\3\2\2\2\u0114\u0115\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0117\7\17"+
		"\2\2\u0117\u0118\58\35\2\u0118\u0119\7\7\2\2\u0119\u011a\7\2\2\3\u011a"+
		"\u011c\3\2\2\2\u011b\u010e\3\2\2\2\u011b\u0112\3\2\2\2\u011c\67\3\2\2"+
		"\2\u011d\u011f\5\64\33\2\u011e\u011d\3\2\2\2\u011f\u0122\3\2\2\2\u0120"+
		"\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0138\3\2\2\2\u0122\u0120\3\2"+
		"\2\2\u0123\u0125\5\64\33\2\u0124\u0123\3\2\2\2\u0125\u0128\3\2\2\2\u0126"+
		"\u0124\3\2\2\2\u0126\u0127\3\2\2\2\u0127\u0129\3\2\2\2\u0128\u0126\3\2"+
		"\2\2\u0129\u012a\5:\36\2\u012a\u012b\58\35\2\u012b\u0138\3\2\2\2\u012c"+
		"\u012e\5\64\33\2\u012d\u012c\3\2\2\2\u012e\u0131\3\2\2\2\u012f\u012d\3"+
		"\2\2\2\u012f\u0130\3\2\2\2\u0130\u0132\3\2\2\2\u0131\u012f\3\2\2\2\u0132"+
		"\u0133\7\17\2\2\u0133\u0134\58\35\2\u0134\u0135\7\7\2\2\u0135\u0136\5"+
		"8\35\2\u0136\u0138\3\2\2\2\u0137\u0120\3\2\2\2\u0137\u0126\3\2\2\2\u0137"+
		"\u012f\3\2\2\2\u01389\3\2\2\2\u0139\u013a\7\32\2\2\u013a;\3\2\2\2\'KU"+
		"]ahmtwz\177\u0087\u008c\u0098\u009e\u00a4\u00a9\u00b0\u00b6\u00bb\u00c2"+
		"\u00c8\u00cd\u00d4\u00da\u00df\u00e6\u00ec\u00f1\u00f8\u00fe\u0103\u0114"+
		"\u011b\u0120\u0126\u012f\u0137";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}