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
		RULE_selector = 9, RULE_pathSel = 10, RULE_pathSelX = 11, RULE_nudePathSelX = 12, 
		RULE_infoNorm = 13, RULE_infoTyped = 14, RULE_info = 15, RULE_infoBody = 16, 
		RULE_typeDep = 17, RULE_coherentDep = 18, RULE_friends = 19, RULE_usedMethods = 20, 
		RULE_privateSupertypes = 21, RULE_refined = 22, RULE_declaresClassMethods = 23, 
		RULE_x = 24, RULE_m = 25, RULE_charInDoc = 26, RULE_topDoc = 27, RULE_topDocText = 28, 
		RULE_doc = 29;
	private static String[] makeRuleNames() {
		return new String[] {
			"anyKw", "voidKw", "libraryKw", "thisKw", "c", "csP", "nudeCsP", "path", 
			"cs", "selector", "pathSel", "pathSelX", "nudePathSelX", "infoNorm", 
			"infoTyped", "info", "infoBody", "typeDep", "coherentDep", "friends", 
			"usedMethods", "privateSupertypes", "refined", "declaresClassMethods", 
			"x", "m", "charInDoc", "topDoc", "topDocText", "doc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'#norm{'", "'#typed{'", "'}'", "'typeDep='", "'coherentDep='", 
			"'friends='", "'usedMethods='", "'privateSupertypes='", "'refined='", 
			"'declaresClassMethods'", "'{'", "'.'", "'@@'", null, "'Any'", "'Void'", 
			"'Library'"
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
			setState(60);
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
			setState(62);
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
			setState(64);
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
			setState(66);
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
			setState(68);
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
			setState(75);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ThisKw:
				{
				setState(70);
				path();
				}
				break;
			case C:
				{
				setState(71);
				cs();
				}
				break;
			case AnyKw:
				{
				setState(72);
				anyKw();
				}
				break;
			case VoidKw:
				{
				setState(73);
				voidKw();
				}
				break;
			case LibraryKw:
				{
				setState(74);
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
			setState(77);
			csP();
			setState(78);
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
			setState(80);
			thisKw();
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(81);
					match(Dot);
					setState(82);
					c();
					}
					} 
				}
				setState(87);
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
			setState(88);
			c();
			setState(93);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(89);
					match(Dot);
					setState(90);
					c();
					}
					} 
				}
				setState(95);
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
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUniqueNum) | (1L << MHash) | (1L << X))) != 0)) {
				{
				setState(96);
				m();
				}
			}

			setState(99);
			match(T__0);
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(100);
				x();
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==W) {
					{
					{
					setState(101);
					match(W);
					}
					}
					setState(106);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(112);
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
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ThisKw:
			case AnyKw:
			case VoidKw:
			case LibraryKw:
			case C:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				csP();
				setState(119);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(116);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Dot) {
						{
						setState(115);
						match(Dot);
						}
					}

					setState(118);
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
				setState(121);
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
			setState(124);
			pathSel();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Dot) {
				{
				setState(125);
				match(Dot);
				setState(126);
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

	public static class NudePathSelXContext extends ParserRuleContext {
		public PathSelXContext pathSelX() {
			return getRuleContext(PathSelXContext.class,0);
		}
		public TerminalNode EOF() { return getToken(L42AuxParser.EOF, 0); }
		public NudePathSelXContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nudePathSelX; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterNudePathSelX(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitNudePathSelX(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitNudePathSelX(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NudePathSelXContext nudePathSelX() throws RecognitionException {
		NudePathSelXContext _localctx = new NudePathSelXContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_nudePathSelX);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			pathSelX();
			setState(130);
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
		enterRule(_localctx, 26, RULE_infoNorm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
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
		enterRule(_localctx, 28, RULE_infoTyped);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
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
		public TerminalNode EOF() { return getToken(L42AuxParser.EOF, 0); }
		public InfoNormContext infoNorm() {
			return getRuleContext(InfoNormContext.class,0);
		}
		public InfoTypedContext infoTyped() {
			return getRuleContext(InfoTypedContext.class,0);
		}
		public List<TerminalNode> W() { return getTokens(L42AuxParser.W); }
		public TerminalNode W(int i) {
			return getToken(L42AuxParser.W, i);
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
		enterRule(_localctx, 30, RULE_info);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(136);
				match(W);
				}
				}
				setState(141);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(142);
				infoNorm();
				}
				break;
			case T__3:
				{
				setState(143);
				infoTyped();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) {
				{
				{
				setState(146);
				infoBody();
				}
				}
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(152);
			match(T__4);
			setState(153);
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
		public PrivateSupertypesContext privateSupertypes() {
			return getRuleContext(PrivateSupertypesContext.class,0);
		}
		public RefinedContext refined() {
			return getRuleContext(RefinedContext.class,0);
		}
		public DeclaresClassMethodsContext declaresClassMethods() {
			return getRuleContext(DeclaresClassMethodsContext.class,0);
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
		enterRule(_localctx, 32, RULE_infoBody);
		try {
			setState(162);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(155);
				typeDep();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(156);
				coherentDep();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(157);
				friends();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(158);
				usedMethods();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(159);
				privateSupertypes();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(160);
				refined();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 7);
				{
				setState(161);
				declaresClassMethods();
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
		enterRule(_localctx, 34, RULE_typeDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(T__5);
			setState(172); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(168);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(165);
						match(W);
						}
						}
						setState(170);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(171);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(174); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(176);
				match(W);
				}
				}
				setState(181);
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
		enterRule(_localctx, 36, RULE_coherentDep);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(T__6);
			setState(190); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(186);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(183);
						match(W);
						}
						}
						setState(188);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(189);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(192); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(197);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(194);
				match(W);
				}
				}
				setState(199);
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
		enterRule(_localctx, 38, RULE_friends);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			match(T__7);
			setState(208); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(204);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(201);
						match(W);
						}
						}
						setState(206);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(207);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(210); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(212);
				match(W);
				}
				}
				setState(217);
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
		enterRule(_localctx, 40, RULE_usedMethods);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(T__8);
			setState(226); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(222);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(219);
						match(W);
						}
						}
						setState(224);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(225);
					pathSel();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(228); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(230);
				match(W);
				}
				}
				setState(235);
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

	public static class PrivateSupertypesContext extends ParserRuleContext {
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
		public PrivateSupertypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_privateSupertypes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterPrivateSupertypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitPrivateSupertypes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitPrivateSupertypes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrivateSupertypesContext privateSupertypes() throws RecognitionException {
		PrivateSupertypesContext _localctx = new PrivateSupertypesContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_privateSupertypes);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(T__9);
			setState(244); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(240);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(237);
						match(W);
						}
						}
						setState(242);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(243);
					path();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(246); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(248);
				match(W);
				}
				}
				setState(253);
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
		enterRule(_localctx, 44, RULE_refined);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(T__10);
			setState(262); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(258);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==W) {
						{
						{
						setState(255);
						match(W);
						}
						}
						setState(260);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(261);
					selector();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(264); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==W) {
				{
				{
				setState(266);
				match(W);
				}
				}
				setState(271);
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

	public static class DeclaresClassMethodsContext extends ParserRuleContext {
		public DeclaresClassMethodsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaresClassMethods; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).enterDeclaresClassMethods(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42AuxListener ) ((L42AuxListener)listener).exitDeclaresClassMethods(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42AuxVisitor ) return ((L42AuxVisitor<? extends T>)visitor).visitDeclaresClassMethods(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaresClassMethodsContext declaresClassMethods() throws RecognitionException {
		DeclaresClassMethodsContext _localctx = new DeclaresClassMethodsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_declaresClassMethods);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
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
		enterRule(_localctx, 48, RULE_x);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
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
		enterRule(_localctx, 50, RULE_m);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
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
		enterRule(_localctx, 52, RULE_charInDoc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
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
		enterRule(_localctx, 54, RULE_topDoc);
		int _la;
		try {
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(280);
				match(TDStart);
				setState(281);
				pathSelX();
				setState(282);
				match(EOF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(284);
				match(TDStart);
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X))) != 0)) {
					{
					setState(285);
					pathSelX();
					}
				}

				setState(288);
				match(T__12);
				setState(289);
				topDocText();
				setState(290);
				match(T__4);
				setState(291);
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
		enterRule(_localctx, 56, RULE_topDocText);
		int _la;
		try {
			setState(321);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(295);
					charInDoc();
					}
					}
					setState(300);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(301);
					charInDoc();
					}
					}
					setState(306);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(307);
				doc();
				setState(308);
				topDocText();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(313);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Dot) | (1L << ThisKw) | (1L << AnyKw) | (1L << VoidKw) | (1L << LibraryKw) | (1L << C) | (1L << MUniqueNum) | (1L << MHash) | (1L << X) | (1L << W) | (1L << CHARInDoc))) != 0)) {
					{
					{
					setState(310);
					charInDoc();
					}
					}
					setState(315);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(316);
				match(T__12);
				setState(317);
				topDocText();
				setState(318);
				match(T__4);
				setState(319);
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
		enterRule(_localctx, 58, RULE_doc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34\u0148\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7N\n\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\7\tV\n\t\f\t\16\tY\13\t\3\n\3\n\3\n\7\n^\n\n\f\n\16\na"+
		"\13\n\3\13\5\13d\n\13\3\13\3\13\3\13\7\13i\n\13\f\13\16\13l\13\13\7\13"+
		"n\n\13\f\13\16\13q\13\13\3\13\3\13\3\f\3\f\5\fw\n\f\3\f\5\fz\n\f\3\f\5"+
		"\f}\n\f\3\r\3\r\3\r\5\r\u0082\n\r\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3"+
		"\21\7\21\u008c\n\21\f\21\16\21\u008f\13\21\3\21\3\21\5\21\u0093\n\21\3"+
		"\21\7\21\u0096\n\21\f\21\16\21\u0099\13\21\3\21\3\21\3\21\3\22\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\5\22\u00a5\n\22\3\23\3\23\7\23\u00a9\n\23\f\23"+
		"\16\23\u00ac\13\23\3\23\6\23\u00af\n\23\r\23\16\23\u00b0\3\23\7\23\u00b4"+
		"\n\23\f\23\16\23\u00b7\13\23\3\24\3\24\7\24\u00bb\n\24\f\24\16\24\u00be"+
		"\13\24\3\24\6\24\u00c1\n\24\r\24\16\24\u00c2\3\24\7\24\u00c6\n\24\f\24"+
		"\16\24\u00c9\13\24\3\25\3\25\7\25\u00cd\n\25\f\25\16\25\u00d0\13\25\3"+
		"\25\6\25\u00d3\n\25\r\25\16\25\u00d4\3\25\7\25\u00d8\n\25\f\25\16\25\u00db"+
		"\13\25\3\26\3\26\7\26\u00df\n\26\f\26\16\26\u00e2\13\26\3\26\6\26\u00e5"+
		"\n\26\r\26\16\26\u00e6\3\26\7\26\u00ea\n\26\f\26\16\26\u00ed\13\26\3\27"+
		"\3\27\7\27\u00f1\n\27\f\27\16\27\u00f4\13\27\3\27\6\27\u00f7\n\27\r\27"+
		"\16\27\u00f8\3\27\7\27\u00fc\n\27\f\27\16\27\u00ff\13\27\3\30\3\30\7\30"+
		"\u0103\n\30\f\30\16\30\u0106\13\30\3\30\6\30\u0109\n\30\r\30\16\30\u010a"+
		"\3\30\7\30\u010e\n\30\f\30\16\30\u0111\13\30\3\31\3\31\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0121\n\35\3\35\3\35"+
		"\3\35\3\35\3\35\5\35\u0128\n\35\3\36\7\36\u012b\n\36\f\36\16\36\u012e"+
		"\13\36\3\36\7\36\u0131\n\36\f\36\16\36\u0134\13\36\3\36\3\36\3\36\3\36"+
		"\7\36\u013a\n\36\f\36\16\36\u013d\13\36\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u0144\n\36\3\37\3\37\3\37\2\2 \2\4\6\b\n\f\16\20\22\24\26\30\32\34\36"+
		" \"$&(*,.\60\62\64\668:<\2\4\3\2\27\31\5\2\20\20\22\31\33\34\2\u0158\2"+
		">\3\2\2\2\4@\3\2\2\2\6B\3\2\2\2\bD\3\2\2\2\nF\3\2\2\2\fM\3\2\2\2\16O\3"+
		"\2\2\2\20R\3\2\2\2\22Z\3\2\2\2\24c\3\2\2\2\26|\3\2\2\2\30~\3\2\2\2\32"+
		"\u0083\3\2\2\2\34\u0086\3\2\2\2\36\u0088\3\2\2\2 \u008d\3\2\2\2\"\u00a4"+
		"\3\2\2\2$\u00a6\3\2\2\2&\u00b8\3\2\2\2(\u00ca\3\2\2\2*\u00dc\3\2\2\2,"+
		"\u00ee\3\2\2\2.\u0100\3\2\2\2\60\u0112\3\2\2\2\62\u0114\3\2\2\2\64\u0116"+
		"\3\2\2\2\66\u0118\3\2\2\28\u0127\3\2\2\2:\u0143\3\2\2\2<\u0145\3\2\2\2"+
		">?\7\23\2\2?\3\3\2\2\2@A\7\24\2\2A\5\3\2\2\2BC\7\25\2\2C\7\3\2\2\2DE\7"+
		"\22\2\2E\t\3\2\2\2FG\7\26\2\2G\13\3\2\2\2HN\5\20\t\2IN\5\22\n\2JN\5\2"+
		"\2\2KN\5\4\3\2LN\5\6\4\2MH\3\2\2\2MI\3\2\2\2MJ\3\2\2\2MK\3\2\2\2ML\3\2"+
		"\2\2N\r\3\2\2\2OP\5\f\7\2PQ\7\2\2\3Q\17\3\2\2\2RW\5\b\5\2ST\7\20\2\2T"+
		"V\5\n\6\2US\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2X\21\3\2\2\2YW\3\2\2"+
		"\2Z_\5\n\6\2[\\\7\20\2\2\\^\5\n\6\2][\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3"+
		"\2\2\2`\23\3\2\2\2a_\3\2\2\2bd\5\64\33\2cb\3\2\2\2cd\3\2\2\2de\3\2\2\2"+
		"eo\7\3\2\2fj\5\62\32\2gi\7\33\2\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2"+
		"\2\2kn\3\2\2\2lj\3\2\2\2mf\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3\2"+
		"\2\2qo\3\2\2\2rs\7\4\2\2s\25\3\2\2\2ty\5\f\7\2uw\7\20\2\2vu\3\2\2\2vw"+
		"\3\2\2\2wx\3\2\2\2xz\5\24\13\2yv\3\2\2\2yz\3\2\2\2z}\3\2\2\2{}\5\24\13"+
		"\2|t\3\2\2\2|{\3\2\2\2}\27\3\2\2\2~\u0081\5\26\f\2\177\u0080\7\20\2\2"+
		"\u0080\u0082\5\62\32\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\31"+
		"\3\2\2\2\u0083\u0084\5\30\r\2\u0084\u0085\7\2\2\3\u0085\33\3\2\2\2\u0086"+
		"\u0087\7\5\2\2\u0087\35\3\2\2\2\u0088\u0089\7\6\2\2\u0089\37\3\2\2\2\u008a"+
		"\u008c\7\33\2\2\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3"+
		"\2\2\2\u008d\u008e\3\2\2\2\u008e\u0092\3\2\2\2\u008f\u008d\3\2\2\2\u0090"+
		"\u0093\5\34\17\2\u0091\u0093\5\36\20\2\u0092\u0090\3\2\2\2\u0092\u0091"+
		"\3\2\2\2\u0093\u0097\3\2\2\2\u0094\u0096\5\"\22\2\u0095\u0094\3\2\2\2"+
		"\u0096\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009a"+
		"\3\2\2\2\u0099\u0097\3\2\2\2\u009a\u009b\7\7\2\2\u009b\u009c\7\2\2\3\u009c"+
		"!\3\2\2\2\u009d\u00a5\5$\23\2\u009e\u00a5\5&\24\2\u009f\u00a5\5(\25\2"+
		"\u00a0\u00a5\5*\26\2\u00a1\u00a5\5,\27\2\u00a2\u00a5\5.\30\2\u00a3\u00a5"+
		"\5\60\31\2\u00a4\u009d\3\2\2\2\u00a4\u009e\3\2\2\2\u00a4\u009f\3\2\2\2"+
		"\u00a4\u00a0\3\2\2\2\u00a4\u00a1\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a3"+
		"\3\2\2\2\u00a5#\3\2\2\2\u00a6\u00ae\7\b\2\2\u00a7\u00a9\7\33\2\2\u00a8"+
		"\u00a7\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2"+
		"\2\2\u00ab\u00ad\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00af\5\20\t\2\u00ae"+
		"\u00aa\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2"+
		"\2\2\u00b1\u00b5\3\2\2\2\u00b2\u00b4\7\33\2\2\u00b3\u00b2\3\2\2\2\u00b4"+
		"\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6%\3\2\2\2"+
		"\u00b7\u00b5\3\2\2\2\u00b8\u00c0\7\t\2\2\u00b9\u00bb\7\33\2\2\u00ba\u00b9"+
		"\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd"+
		"\u00bf\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c1\5\20\t\2\u00c0\u00bc\3"+
		"\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"\u00c7\3\2\2\2\u00c4\u00c6\7\33\2\2\u00c5\u00c4\3\2\2\2\u00c6\u00c9\3"+
		"\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\'\3\2\2\2\u00c9\u00c7"+
		"\3\2\2\2\u00ca\u00d2\7\n\2\2\u00cb\u00cd\7\33\2\2\u00cc\u00cb\3\2\2\2"+
		"\u00cd\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d1"+
		"\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d3\5\20\t\2\u00d2\u00ce\3\2\2\2"+
		"\u00d3\u00d4\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d9"+
		"\3\2\2\2\u00d6\u00d8\7\33\2\2\u00d7\u00d6\3\2\2\2\u00d8\u00db\3\2\2\2"+
		"\u00d9\u00d7\3\2\2\2\u00d9\u00da\3\2\2\2\u00da)\3\2\2\2\u00db\u00d9\3"+
		"\2\2\2\u00dc\u00e4\7\13\2\2\u00dd\u00df\7\33\2\2\u00de\u00dd\3\2\2\2\u00df"+
		"\u00e2\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\3\2"+
		"\2\2\u00e2\u00e0\3\2\2\2\u00e3\u00e5\5\26\f\2\u00e4\u00e0\3\2\2\2\u00e5"+
		"\u00e6\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00eb\3\2"+
		"\2\2\u00e8\u00ea\7\33\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb"+
		"\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec+\3\2\2\2\u00ed\u00eb\3\2\2\2"+
		"\u00ee\u00f6\7\f\2\2\u00ef\u00f1\7\33\2\2\u00f0\u00ef\3\2\2\2\u00f1\u00f4"+
		"\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f5\3\2\2\2\u00f4"+
		"\u00f2\3\2\2\2\u00f5\u00f7\5\20\t\2\u00f6\u00f2\3\2\2\2\u00f7\u00f8\3"+
		"\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fd\3\2\2\2\u00fa"+
		"\u00fc\7\33\2\2\u00fb\u00fa\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3"+
		"\2\2\2\u00fd\u00fe\3\2\2\2\u00fe-\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0108"+
		"\7\r\2\2\u0101\u0103\7\33\2\2\u0102\u0101\3\2\2\2\u0103\u0106\3\2\2\2"+
		"\u0104\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0107\3\2\2\2\u0106\u0104"+
		"\3\2\2\2\u0107\u0109\5\24\13\2\u0108\u0104\3\2\2\2\u0109\u010a\3\2\2\2"+
		"\u010a\u0108\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010f\3\2\2\2\u010c\u010e"+
		"\7\33\2\2\u010d\u010c\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u010d\3\2\2\2"+
		"\u010f\u0110\3\2\2\2\u0110/\3\2\2\2\u0111\u010f\3\2\2\2\u0112\u0113\7"+
		"\16\2\2\u0113\61\3\2\2\2\u0114\u0115\7\31\2\2\u0115\63\3\2\2\2\u0116\u0117"+
		"\t\2\2\2\u0117\65\3\2\2\2\u0118\u0119\t\3\2\2\u0119\67\3\2\2\2\u011a\u011b"+
		"\7\21\2\2\u011b\u011c\5\30\r\2\u011c\u011d\7\2\2\3\u011d\u0128\3\2\2\2"+
		"\u011e\u0120\7\21\2\2\u011f\u0121\5\30\r\2\u0120\u011f\3\2\2\2\u0120\u0121"+
		"\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0123\7\17\2\2\u0123\u0124\5:\36\2"+
		"\u0124\u0125\7\7\2\2\u0125\u0126\7\2\2\3\u0126\u0128\3\2\2\2\u0127\u011a"+
		"\3\2\2\2\u0127\u011e\3\2\2\2\u01289\3\2\2\2\u0129\u012b\5\66\34\2\u012a"+
		"\u0129\3\2\2\2\u012b\u012e\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2"+
		"\2\2\u012d\u0144\3\2\2\2\u012e\u012c\3\2\2\2\u012f\u0131\5\66\34\2\u0130"+
		"\u012f\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2"+
		"\2\2\u0133\u0135\3\2\2\2\u0134\u0132\3\2\2\2\u0135\u0136\5<\37\2\u0136"+
		"\u0137\5:\36\2\u0137\u0144\3\2\2\2\u0138\u013a\5\66\34\2\u0139\u0138\3"+
		"\2\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c"+
		"\u013e\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u013f\7\17\2\2\u013f\u0140\5"+
		":\36\2\u0140\u0141\7\7\2\2\u0141\u0142\5:\36\2\u0142\u0144\3\2\2\2\u0143"+
		"\u012c\3\2\2\2\u0143\u0132\3\2\2\2\u0143\u013b\3\2\2\2\u0144;\3\2\2\2"+
		"\u0145\u0146\7\32\2\2\u0146=\3\2\2\2(MW_cjovy|\u0081\u008d\u0092\u0097"+
		"\u00a4\u00aa\u00b0\u00b5\u00bc\u00c2\u00c7\u00ce\u00d4\u00d9\u00e0\u00e6"+
		"\u00eb\u00f2\u00f8\u00fd\u0104\u010a\u010f\u0120\u0127\u012c\u0132\u013b"+
		"\u0143";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}