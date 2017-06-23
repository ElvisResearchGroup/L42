// Generated from L42.g4 by ANTLR 4.2.2
package antlrGenerated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class L42Parser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SRE=1, SEX=2, SER=3, Mdf=4, ORoundNoSpace=5, ORoundSpace=6, CRound=7, 
		OSquare=8, CSquare=9, OCurly=10, DotDotDot=11, EndType=12, CCurly=13, 
		Colon=14, Semicolon=15, Dot=16, Equal=17, Ph=18, Implements=19, Case=20, 
		If=21, Else=22, While=23, Loop=24, With=25, On=26, In=27, Catch=28, Var=29, 
		Default=30, Interface=31, Method=32, Using=33, Check=34, Refine=35, FieldSpecial=36, 
		WalkBy=37, Stage=38, Path=39, ClassSep=40, MX=41, X=42, HashX=43, ContextId=44, 
		StringQuote=45, UrlNL=46, Url=47, Doc=48, WS=49, UnOp=50, EqOp=51, BoolOp=52, 
		RelOp=53, DataOp=54, NumParse=55;
	public static final String[] tokenNames = {
		"<INVALID>", "'return'", "'exception'", "'error'", "Mdf", "'('", "'\t'", 
		"')'", "'['", "']'", "'{'", "'...'", "'^##'", "'}'", "':'", "';'", "Dot", 
		"'='", "'fwd'", "'implements'", "'case'", "'if'", "'else'", "'while'", 
		"'loop'", "'with'", "'on'", "'in'", "'catch'", "'var'", "'default'", "'interface'", 
		"'method'", "'use'", "'check'", "'refine'", "'##field'", "'##walkBy'", 
		"Stage", "Path", "ClassSep", "MX", "X", "HashX", "ContextId", "StringQuote", 
		"UrlNL", "Url", "Doc", "WS", "UnOp", "EqOp", "BoolOp", "RelOp", "DataOp", 
		"NumParse"
	};
	public static final int
		RULE_m = 0, RULE_mDec = 1, RULE_path = 2, RULE_docs = 3, RULE_docsOpt = 4, 
		RULE_t = 5, RULE_methSelector = 6, RULE_x = 7, RULE_sS = 8, RULE_sEx = 9, 
		RULE_xOp = 10, RULE_eTopForMethod = 11, RULE_eTop = 12, RULE_eL3 = 13, 
		RULE_eL2 = 14, RULE_eL1 = 15, RULE_numParse = 16, RULE_eUnOp = 17, RULE_ePost = 18, 
		RULE_eAtom = 19, RULE_mxRound = 20, RULE_contextId = 21, RULE_useSquare = 22, 
		RULE_ifExpr = 23, RULE_using = 24, RULE_whileExpr = 25, RULE_signalExpr = 26, 
		RULE_loopExpr = 27, RULE_block = 28, RULE_roundBlockForMethod = 29, RULE_roundBlock = 30, 
		RULE_bb = 31, RULE_curlyBlock = 32, RULE_varDec = 33, RULE_d = 34, RULE_stringParse = 35, 
		RULE_square = 36, RULE_squareW = 37, RULE_mCall = 38, RULE_round = 39, 
		RULE_ps = 40, RULE_k1 = 41, RULE_kMany = 42, RULE_kProp = 43, RULE_k = 44, 
		RULE_ks = 45, RULE_on = 46, RULE_onPlus = 47, RULE_nudeE = 48, RULE_classBExtra = 49, 
		RULE_classBReuse = 50, RULE_classB = 51, RULE_impls = 52, RULE_mhs = 53, 
		RULE_mht = 54, RULE_member = 55, RULE_methodWithType = 56, RULE_methodImplemented = 57, 
		RULE_nestedClass = 58, RULE_header = 59, RULE_fieldDec = 60, RULE_w = 61, 
		RULE_wSwitch = 62, RULE_i = 63, RULE_wSimple = 64;
	public static final String[] ruleNames = {
		"m", "mDec", "path", "docs", "docsOpt", "t", "methSelector", "x", "sS", 
		"sEx", "xOp", "eTopForMethod", "eTop", "eL3", "eL2", "eL1", "numParse", 
		"eUnOp", "ePost", "eAtom", "mxRound", "contextId", "useSquare", "ifExpr", 
		"using", "whileExpr", "signalExpr", "loopExpr", "block", "roundBlockForMethod", 
		"roundBlock", "bb", "curlyBlock", "varDec", "d", "stringParse", "square", 
		"squareW", "mCall", "round", "ps", "k1", "kMany", "kProp", "k", "ks", 
		"on", "onPlus", "nudeE", "classBExtra", "classBReuse", "classB", "impls", 
		"mhs", "mht", "member", "methodWithType", "methodImplemented", "nestedClass", 
		"header", "fieldDec", "w", "wSwitch", "i", "wSimple"
	};

	@Override
	public String getGrammarFileName() { return "L42.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public L42Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class MContext extends ParserRuleContext {
		public TerminalNode MX() { return getToken(L42Parser.MX, 0); }
		public TerminalNode HashX() { return getToken(L42Parser.HashX, 0); }
		public MContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_m; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterM(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitM(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitM(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MContext m() throws RecognitionException {
		MContext _localctx = new MContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_m);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			_la = _input.LA(1);
			if ( !(_la==MX || _la==HashX) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class MDecContext extends ParserRuleContext {
		public TerminalNode RelOp() { return getToken(L42Parser.RelOp, 0); }
		public TerminalNode BoolOp() { return getToken(L42Parser.BoolOp, 0); }
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public TerminalNode UnOp() { return getToken(L42Parser.UnOp, 0); }
		public TerminalNode DataOp() { return getToken(L42Parser.DataOp, 0); }
		public MContext m() {
			return getRuleContext(MContext.class,0);
		}
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public TerminalNode EqOp() { return getToken(L42Parser.EqOp, 0); }
		public MDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MDecContext mDec() throws RecognitionException {
		MDecContext _localctx = new MDecContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mDec);
		try {
			setState(153);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(132); m();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(133); match(UnOp);
				setState(134); match(ORoundNoSpace);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(135); match(EqOp);
				setState(136); match(ORoundNoSpace);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(137); match(BoolOp);
				setState(138); match(ORoundNoSpace);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(139); match(RelOp);
				setState(140); match(ORoundNoSpace);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(141); match(DataOp);
				setState(142); match(ORoundNoSpace);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(143); match(UnOp);
				setState(144); match(ORoundSpace);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(145); match(EqOp);
				setState(146); match(ORoundSpace);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(147); match(BoolOp);
				setState(148); match(ORoundSpace);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(149); match(RelOp);
				setState(150); match(ORoundSpace);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(151); match(DataOp);
				setState(152); match(ORoundSpace);
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

	public static class PathContext extends ParserRuleContext {
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_path);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155); match(Path);
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

	public static class DocsContext extends ParserRuleContext {
		public TerminalNode Doc() { return getToken(L42Parser.Doc, 0); }
		public DocsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_docs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterDocs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitDocs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitDocs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocsContext docs() throws RecognitionException {
		DocsContext _localctx = new DocsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_docs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157); match(Doc);
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

	public static class DocsOptContext extends ParserRuleContext {
		public TerminalNode Doc() { return getToken(L42Parser.Doc, 0); }
		public DocsOptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_docsOpt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterDocsOpt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitDocsOpt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitDocsOpt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocsOptContext docsOpt() throws RecognitionException {
		DocsOptContext _localctx = new DocsOptContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_docsOpt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(159); match(Doc);
				}
				break;
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

	public static class TContext extends ParserRuleContext {
		public TerminalNode Ph() { return getToken(L42Parser.Ph, 0); }
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode Mdf() { return getToken(L42Parser.Mdf, 0); }
		public TContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TContext t() throws RecognitionException {
		TContext _localctx = new TContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_t);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(162); match(Ph);
				}
			}

			setState(166);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(165); match(Mdf);
				}
			}

			setState(168); match(Path);
			setState(169); docsOpt();
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

	public static class MethSelectorContext extends ParserRuleContext {
		public MDecContext mDec() {
			return getRuleContext(MDecContext.class,0);
		}
		public List<XContext> x() {
			return getRuleContexts(XContext.class);
		}
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public XContext x(int i) {
			return getRuleContext(XContext.class,i);
		}
		public MethSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMethSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMethSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMethSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethSelectorContext methSelector() throws RecognitionException {
		MethSelectorContext _localctx = new MethSelectorContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_methSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(171); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(172); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(173); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(176); x();
				}
				}
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(182); match(CRound);
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
		public TerminalNode X() { return getToken(L42Parser.X, 0); }
		public XContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_x; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterX(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitX(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitX(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XContext x() throws RecognitionException {
		XContext _localctx = new XContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_x);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); match(X);
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

	public static class SSContext extends ParserRuleContext {
		public TerminalNode SER() { return getToken(L42Parser.SER, 0); }
		public TerminalNode SRE() { return getToken(L42Parser.SRE, 0); }
		public TerminalNode SEX() { return getToken(L42Parser.SEX, 0); }
		public SSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sS; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterSS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitSS(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitSS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SSContext sS() throws RecognitionException {
		SSContext _localctx = new SSContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_sS);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SRE) | (1L << SEX) | (1L << SER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class SExContext extends ParserRuleContext {
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public TerminalNode SEX() { return getToken(L42Parser.SEX, 0); }
		public SExContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sEx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterSEx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitSEx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitSEx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SExContext sEx() throws RecognitionException {
		SExContext _localctx = new SExContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_sEx);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(188); match(CRound);
			setState(189); match(SEX);
			setState(191); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(190); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(193); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
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

	public static class XOpContext extends ParserRuleContext {
		public TerminalNode X() { return getToken(L42Parser.X, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode EqOp() { return getToken(L42Parser.EqOp, 0); }
		public XOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterXOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitXOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitXOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XOpContext xOp() throws RecognitionException {
		XOpContext _localctx = new XOpContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_xOp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195); match(X);
			setState(196); match(EqOp);
			setState(197); docsOpt();
			setState(198); eTop();
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

	public static class ETopForMethodContext extends ParserRuleContext {
		public List<SquareContext> square() {
			return getRuleContexts(SquareContext.class);
		}
		public StringParseContext stringParse(int i) {
			return getRuleContext(StringParseContext.class,i);
		}
		public DocsContext docs(int i) {
			return getRuleContext(DocsContext.class,i);
		}
		public RoundBlockForMethodContext roundBlockForMethod() {
			return getRuleContext(RoundBlockForMethodContext.class,0);
		}
		public List<TerminalNode> ORoundNoSpace() { return getTokens(L42Parser.ORoundNoSpace); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode Dot(int i) {
			return getToken(L42Parser.Dot, i);
		}
		public List<TerminalNode> Dot() { return getTokens(L42Parser.Dot); }
		public RoundContext round(int i) {
			return getRuleContext(RoundContext.class,i);
		}
		public List<RoundContext> round() {
			return getRuleContexts(RoundContext.class);
		}
		public SquareWContext squareW(int i) {
			return getRuleContext(SquareWContext.class,i);
		}
		public List<DocsContext> docs() {
			return getRuleContexts(DocsContext.class);
		}
		public TerminalNode ORoundNoSpace(int i) {
			return getToken(L42Parser.ORoundNoSpace, i);
		}
		public List<StringParseContext> stringParse() {
			return getRuleContexts(StringParseContext.class);
		}
		public List<MCallContext> mCall() {
			return getRuleContexts(MCallContext.class);
		}
		public MCallContext mCall(int i) {
			return getRuleContext(MCallContext.class,i);
		}
		public SquareContext square(int i) {
			return getRuleContext(SquareContext.class,i);
		}
		public List<SquareWContext> squareW() {
			return getRuleContexts(SquareWContext.class);
		}
		public ETopForMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eTopForMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterETopForMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitETopForMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitETopForMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ETopForMethodContext eTopForMethod() throws RecognitionException {
		ETopForMethodContext _localctx = new ETopForMethodContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_eTopForMethod);
		int _la;
		try {
			setState(215);
			switch (_input.LA(1)) {
			case SRE:
			case SEX:
			case SER:
			case ORoundSpace:
			case OCurly:
			case DotDotDot:
			case If:
			case While:
			case Loop:
			case With:
			case Using:
			case WalkBy:
			case Path:
			case MX:
			case X:
			case ContextId:
			case UnOp:
			case NumParse:
				enterOuterAlt(_localctx, 1);
				{
				setState(200); eTop();
				}
				break;
			case ORoundNoSpace:
				enterOuterAlt(_localctx, 2);
				{
				setState(201); roundBlockForMethod();
				setState(212);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ORoundNoSpace) | (1L << OSquare) | (1L << Dot) | (1L << StringQuote) | (1L << Doc))) != 0)) {
					{
					setState(210);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						setState(202); squareW();
						}
						break;

					case 2:
						{
						setState(203); square();
						}
						break;

					case 3:
						{
						setState(204); match(Dot);
						setState(205); mCall();
						}
						break;

					case 4:
						{
						setState(206); match(ORoundNoSpace);
						setState(207); round();
						}
						break;

					case 5:
						{
						setState(208); docs();
						}
						break;

					case 6:
						{
						setState(209); stringParse();
						}
						break;
					}
					}
					setState(214);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class ETopContext extends ParserRuleContext {
		public List<EL3Context> eL3() {
			return getRuleContexts(EL3Context.class);
		}
		public TerminalNode BoolOp(int i) {
			return getToken(L42Parser.BoolOp, i);
		}
		public EL3Context eL3(int i) {
			return getRuleContext(EL3Context.class,i);
		}
		public List<TerminalNode> BoolOp() { return getTokens(L42Parser.BoolOp); }
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public ETopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eTop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterETop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitETop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitETop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ETopContext eTop() throws RecognitionException {
		ETopContext _localctx = new ETopContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_eTop);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(217); eL3();
			setState(224);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(218); match(BoolOp);
					setState(219); docsOpt();
					setState(220); eL3();
					}
					} 
				}
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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

	public static class EL3Context extends ParserRuleContext {
		public List<TerminalNode> RelOp() { return getTokens(L42Parser.RelOp); }
		public EL2Context eL2(int i) {
			return getRuleContext(EL2Context.class,i);
		}
		public List<EL2Context> eL2() {
			return getRuleContexts(EL2Context.class);
		}
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public TerminalNode RelOp(int i) {
			return getToken(L42Parser.RelOp, i);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public EL3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eL3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEL3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEL3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEL3(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EL3Context eL3() throws RecognitionException {
		EL3Context _localctx = new EL3Context(_ctx, getState());
		enterRule(_localctx, 26, RULE_eL3);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(227); eL2();
			setState(234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(228); match(RelOp);
					setState(229); docsOpt();
					setState(230); eL2();
					}
					} 
				}
				setState(236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
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

	public static class EL2Context extends ParserRuleContext {
		public List<TerminalNode> DataOp() { return getTokens(L42Parser.DataOp); }
		public TerminalNode DataOp(int i) {
			return getToken(L42Parser.DataOp, i);
		}
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public EL1Context eL1(int i) {
			return getRuleContext(EL1Context.class,i);
		}
		public List<EL1Context> eL1() {
			return getRuleContexts(EL1Context.class);
		}
		public EL2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eL2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEL2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEL2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEL2(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EL2Context eL2() throws RecognitionException {
		EL2Context _localctx = new EL2Context(_ctx, getState());
		enterRule(_localctx, 28, RULE_eL2);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(237); eL1();
			setState(244);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(238); match(DataOp);
					setState(239); docsOpt();
					setState(240); eL1();
					}
					} 
				}
				setState(246);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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

	public static class EL1Context extends ParserRuleContext {
		public EUnOpContext eUnOp() {
			return getRuleContext(EUnOpContext.class,0);
		}
		public EL1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eL1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEL1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEL1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEL1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EL1Context eL1() throws RecognitionException {
		EL1Context _localctx = new EL1Context(_ctx, getState());
		enterRule(_localctx, 30, RULE_eL1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247); eUnOp();
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

	public static class NumParseContext extends ParserRuleContext {
		public TerminalNode NumParse() { return getToken(L42Parser.NumParse, 0); }
		public NumParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numParse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterNumParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitNumParse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitNumParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumParseContext numParse() throws RecognitionException {
		NumParseContext _localctx = new NumParseContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_numParse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249); match(NumParse);
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

	public static class EUnOpContext extends ParserRuleContext {
		public EPostContext ePost() {
			return getRuleContext(EPostContext.class,0);
		}
		public TerminalNode UnOp() { return getToken(L42Parser.UnOp, 0); }
		public EUnOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eUnOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEUnOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEUnOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEUnOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EUnOpContext eUnOp() throws RecognitionException {
		EUnOpContext _localctx = new EUnOpContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_eUnOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_la = _input.LA(1);
			if (_la==UnOp) {
				{
				setState(251); match(UnOp);
				}
			}

			setState(254); ePost();
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

	public static class EPostContext extends ParserRuleContext {
		public List<SquareContext> square() {
			return getRuleContexts(SquareContext.class);
		}
		public StringParseContext stringParse(int i) {
			return getRuleContext(StringParseContext.class,i);
		}
		public DocsContext docs(int i) {
			return getRuleContext(DocsContext.class,i);
		}
		public List<TerminalNode> ORoundNoSpace() { return getTokens(L42Parser.ORoundNoSpace); }
		public TerminalNode Dot(int i) {
			return getToken(L42Parser.Dot, i);
		}
		public List<TerminalNode> Dot() { return getTokens(L42Parser.Dot); }
		public RoundContext round(int i) {
			return getRuleContext(RoundContext.class,i);
		}
		public List<RoundContext> round() {
			return getRuleContexts(RoundContext.class);
		}
		public SquareWContext squareW(int i) {
			return getRuleContext(SquareWContext.class,i);
		}
		public List<DocsContext> docs() {
			return getRuleContexts(DocsContext.class);
		}
		public EAtomContext eAtom() {
			return getRuleContext(EAtomContext.class,0);
		}
		public TerminalNode ORoundNoSpace(int i) {
			return getToken(L42Parser.ORoundNoSpace, i);
		}
		public List<StringParseContext> stringParse() {
			return getRuleContexts(StringParseContext.class);
		}
		public List<MCallContext> mCall() {
			return getRuleContexts(MCallContext.class);
		}
		public MCallContext mCall(int i) {
			return getRuleContext(MCallContext.class,i);
		}
		public SquareContext square(int i) {
			return getRuleContext(SquareContext.class,i);
		}
		public List<SquareWContext> squareW() {
			return getRuleContexts(SquareWContext.class);
		}
		public EPostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ePost; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEPost(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEPost(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEPost(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EPostContext ePost() throws RecognitionException {
		EPostContext _localctx = new EPostContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_ePost);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(256); eAtom();
			setState(267);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(265);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						setState(257); squareW();
						}
						break;

					case 2:
						{
						setState(258); square();
						}
						break;

					case 3:
						{
						setState(259); match(Dot);
						setState(260); mCall();
						}
						break;

					case 4:
						{
						setState(261); match(ORoundNoSpace);
						setState(262); round();
						}
						break;

					case 5:
						{
						setState(263); docs();
						}
						break;

					case 6:
						{
						setState(264); stringParse();
						}
						break;
					}
					} 
				}
				setState(269);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
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

	public static class EAtomContext extends ParserRuleContext {
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public NumParseContext numParse() {
			return getRuleContext(NumParseContext.class,0);
		}
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public XOpContext xOp() {
			return getRuleContext(XOpContext.class,0);
		}
		public WContext w() {
			return getRuleContext(WContext.class,0);
		}
		public LoopExprContext loopExpr() {
			return getRuleContext(LoopExprContext.class,0);
		}
		public ClassBReuseContext classBReuse() {
			return getRuleContext(ClassBReuseContext.class,0);
		}
		public ClassBContext classB() {
			return getRuleContext(ClassBContext.class,0);
		}
		public TerminalNode WalkBy() { return getToken(L42Parser.WalkBy, 0); }
		public ContextIdContext contextId() {
			return getRuleContext(ContextIdContext.class,0);
		}
		public WhileExprContext whileExpr() {
			return getRuleContext(WhileExprContext.class,0);
		}
		public SignalExprContext signalExpr() {
			return getRuleContext(SignalExprContext.class,0);
		}
		public MxRoundContext mxRound() {
			return getRuleContext(MxRoundContext.class,0);
		}
		public UseSquareContext useSquare() {
			return getRuleContext(UseSquareContext.class,0);
		}
		public IfExprContext ifExpr() {
			return getRuleContext(IfExprContext.class,0);
		}
		public UsingContext using() {
			return getRuleContext(UsingContext.class,0);
		}
		public TerminalNode DotDotDot() { return getToken(L42Parser.DotDotDot, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public EAtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eAtom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterEAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitEAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitEAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EAtomContext eAtom() throws RecognitionException {
		EAtomContext _localctx = new EAtomContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_eAtom);
		int _la;
		try {
			setState(299);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(270); classBReuse();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(271); classB();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(273);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(272); numParse();
					}
				}

				setState(275); x();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(276); xOp();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(278);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(277); numParse();
					}
				}

				setState(280); match(Path);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(282);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(281); numParse();
					}
				}

				setState(284); block();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(285); ifExpr();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(286); whileExpr();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(287); signalExpr();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(288); loopExpr();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(289); match(WalkBy);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(290); w();
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(291); using();
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(292); match(DotDotDot);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(293); mxRound();
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(294); useSquare();
				}
				break;

			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(296);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(295); numParse();
					}
				}

				setState(298); contextId();
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

	public static class MxRoundContext extends ParserRuleContext {
		public TerminalNode MX() { return getToken(L42Parser.MX, 0); }
		public RoundContext round() {
			return getRuleContext(RoundContext.class,0);
		}
		public MxRoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mxRound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMxRound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMxRound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMxRound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MxRoundContext mxRound() throws RecognitionException {
		MxRoundContext _localctx = new MxRoundContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_mxRound);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301); match(MX);
			setState(302); round();
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

	public static class ContextIdContext extends ParserRuleContext {
		public TerminalNode ContextId() { return getToken(L42Parser.ContextId, 0); }
		public ContextIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterContextId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitContextId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitContextId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextIdContext contextId() throws RecognitionException {
		ContextIdContext _localctx = new ContextIdContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_contextId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304); match(ContextId);
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

	public static class UseSquareContext extends ParserRuleContext {
		public SquareContext square() {
			return getRuleContext(SquareContext.class,0);
		}
		public TerminalNode Using() { return getToken(L42Parser.Using, 0); }
		public SquareWContext squareW() {
			return getRuleContext(SquareWContext.class,0);
		}
		public UseSquareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_useSquare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterUseSquare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitUseSquare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitUseSquare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseSquareContext useSquare() throws RecognitionException {
		UseSquareContext _localctx = new UseSquareContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_useSquare);
		try {
			setState(310);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(306); match(Using);
				setState(307); square();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(308); match(Using);
				setState(309); squareW();
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

	public static class IfExprContext extends ParserRuleContext {
		public ETopContext eTop(int i) {
			return getRuleContext(ETopContext.class,i);
		}
		public List<ETopContext> eTop() {
			return getRuleContexts(ETopContext.class);
		}
		public TerminalNode Else() { return getToken(L42Parser.Else, 0); }
		public TerminalNode If() { return getToken(L42Parser.If, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterIfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitIfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_ifExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312); match(If);
			setState(313); eTop();
			setState(314); block();
			setState(317);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(315); match(Else);
				setState(316); eTop();
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

	public static class UsingContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(L42Parser.Using, 0); }
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public MCallContext mCall() {
			return getRuleContext(MCallContext.class,0);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode Check() { return getToken(L42Parser.Check, 0); }
		public UsingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_using; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterUsing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitUsing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitUsing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UsingContext using() throws RecognitionException {
		UsingContext _localctx = new UsingContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_using);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319); match(Using);
			setState(320); match(Path);
			setState(321); match(Check);
			setState(322); mCall();
			setState(323); eTop();
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

	public static class WhileExprContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(L42Parser.While, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterWhileExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitWhileExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitWhileExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileExprContext whileExpr() throws RecognitionException {
		WhileExprContext _localctx = new WhileExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_whileExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325); match(While);
			setState(326); eTop();
			setState(327); block();
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

	public static class SignalExprContext extends ParserRuleContext {
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public SSContext sS() {
			return getRuleContext(SSContext.class,0);
		}
		public SignalExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signalExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterSignalExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitSignalExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitSignalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignalExprContext signalExpr() throws RecognitionException {
		SignalExprContext _localctx = new SignalExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_signalExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329); sS();
			setState(330); eTop();
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

	public static class LoopExprContext extends ParserRuleContext {
		public TerminalNode Loop() { return getToken(L42Parser.Loop, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public LoopExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterLoopExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitLoopExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitLoopExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopExprContext loopExpr() throws RecognitionException {
		LoopExprContext _localctx = new LoopExprContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_loopExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332); match(Loop);
			setState(333); eTop();
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

	public static class BlockContext extends ParserRuleContext {
		public RoundBlockContext roundBlock() {
			return getRuleContext(RoundBlockContext.class,0);
		}
		public CurlyBlockContext curlyBlock() {
			return getRuleContext(CurlyBlockContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_block);
		try {
			setState(337);
			switch (_input.LA(1)) {
			case ORoundSpace:
				enterOuterAlt(_localctx, 1);
				{
				setState(335); roundBlock();
				}
				break;
			case OCurly:
				enterOuterAlt(_localctx, 2);
				{
				setState(336); curlyBlock();
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

	public static class RoundBlockForMethodContext extends ParserRuleContext {
		public BbContext bb(int i) {
			return getRuleContext(BbContext.class,i);
		}
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public List<BbContext> bb() {
			return getRuleContexts(BbContext.class);
		}
		public RoundBlockForMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_roundBlockForMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterRoundBlockForMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitRoundBlockForMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitRoundBlockForMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoundBlockForMethodContext roundBlockForMethod() throws RecognitionException {
		RoundBlockForMethodContext _localctx = new RoundBlockForMethodContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_roundBlockForMethod);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(339); match(ORoundNoSpace);
			setState(340); docsOpt();
			setState(344);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(341); bb();
					}
					} 
				}
				setState(346);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			setState(347); eTop();
			setState(348); match(CRound);
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

	public static class RoundBlockContext extends ParserRuleContext {
		public BbContext bb(int i) {
			return getRuleContext(BbContext.class,i);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public List<BbContext> bb() {
			return getRuleContexts(BbContext.class);
		}
		public RoundBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_roundBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterRoundBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitRoundBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitRoundBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoundBlockContext roundBlock() throws RecognitionException {
		RoundBlockContext _localctx = new RoundBlockContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_roundBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(350); match(ORoundSpace);
			setState(351); docsOpt();
			setState(355);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(352); bb();
					}
					} 
				}
				setState(357);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			setState(358); eTop();
			setState(359); match(CRound);
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

	public static class BbContext extends ParserRuleContext {
		public KsContext ks() {
			return getRuleContext(KsContext.class,0);
		}
		public List<DContext> d() {
			return getRuleContexts(DContext.class);
		}
		public DContext d(int i) {
			return getRuleContext(DContext.class,i);
		}
		public BbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bb; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterBb(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitBb(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitBb(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BbContext bb() throws RecognitionException {
		BbContext _localctx = new BbContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_bb);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(362); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(361); d();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(364); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(366); ks();
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

	public static class CurlyBlockContext extends ParserRuleContext {
		public TerminalNode CCurly() { return getToken(L42Parser.CCurly, 0); }
		public BbContext bb(int i) {
			return getRuleContext(BbContext.class,i);
		}
		public TerminalNode OCurly() { return getToken(L42Parser.OCurly, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public List<BbContext> bb() {
			return getRuleContexts(BbContext.class);
		}
		public CurlyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_curlyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterCurlyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitCurlyBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitCurlyBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CurlyBlockContext curlyBlock() throws RecognitionException {
		CurlyBlockContext _localctx = new CurlyBlockContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_curlyBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368); match(OCurly);
			setState(369); docsOpt();
			setState(371); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(370); bb();
				}
				}
				setState(373); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SRE) | (1L << SEX) | (1L << SER) | (1L << Mdf) | (1L << ORoundSpace) | (1L << OCurly) | (1L << DotDotDot) | (1L << Ph) | (1L << If) | (1L << While) | (1L << Loop) | (1L << With) | (1L << Var) | (1L << Using) | (1L << WalkBy) | (1L << Path) | (1L << MX) | (1L << X) | (1L << ContextId) | (1L << UnOp) | (1L << NumParse))) != 0) );
			setState(375); match(CCurly);
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

	public static class VarDecContext extends ParserRuleContext {
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode Equal() { return getToken(L42Parser.Equal, 0); }
		public TerminalNode Var() { return getToken(L42Parser.Var, 0); }
		public VarDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterVarDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitVarDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitVarDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDecContext varDec() throws RecognitionException {
		VarDecContext _localctx = new VarDecContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_varDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(377); match(Var);
				}
			}

			setState(381);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(380); t();
				}
			}

			setState(383); x();
			setState(384); match(Equal);
			setState(385); eTop();
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

	public static class DContext extends ParserRuleContext {
		public NestedClassContext nestedClass() {
			return getRuleContext(NestedClassContext.class,0);
		}
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public DContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_d; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterD(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitD(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitD(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DContext d() throws RecognitionException {
		DContext _localctx = new DContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_d);
		try {
			setState(390);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(387); varDec();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(388); eTop();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(389); nestedClass();
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

	public static class StringParseContext extends ParserRuleContext {
		public TerminalNode StringQuote() { return getToken(L42Parser.StringQuote, 0); }
		public StringParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringParse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterStringParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitStringParse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitStringParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringParseContext stringParse() throws RecognitionException {
		StringParseContext _localctx = new StringParseContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_stringParse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392); match(StringQuote);
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

	public static class SquareContext extends ParserRuleContext {
		public TerminalNode CSquare() { return getToken(L42Parser.CSquare, 0); }
		public PsContext ps(int i) {
			return getRuleContext(PsContext.class,i);
		}
		public List<TerminalNode> Semicolon() { return getTokens(L42Parser.Semicolon); }
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public TerminalNode Semicolon(int i) {
			return getToken(L42Parser.Semicolon, i);
		}
		public List<PsContext> ps() {
			return getRuleContexts(PsContext.class);
		}
		public TerminalNode OSquare() { return getToken(L42Parser.OSquare, 0); }
		public SquareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_square; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterSquare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitSquare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitSquare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SquareContext square() throws RecognitionException {
		SquareContext _localctx = new SquareContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_square);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(394); match(OSquare);
			setState(395); docsOpt();
			setState(402);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(396); ps();
					setState(397); match(Semicolon);
					setState(398); docsOpt();
					}
					} 
				}
				setState(404);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			}
			setState(405); ps();
			setState(406); match(CSquare);
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

	public static class SquareWContext extends ParserRuleContext {
		public TerminalNode CSquare() { return getToken(L42Parser.CSquare, 0); }
		public WContext w() {
			return getRuleContext(WContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode OSquare() { return getToken(L42Parser.OSquare, 0); }
		public SquareWContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_squareW; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterSquareW(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitSquareW(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitSquareW(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SquareWContext squareW() throws RecognitionException {
		SquareWContext _localctx = new SquareWContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_squareW);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408); match(OSquare);
			setState(409); docsOpt();
			setState(410); w();
			setState(411); match(CSquare);
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

	public static class MCallContext extends ParserRuleContext {
		public MContext m() {
			return getRuleContext(MContext.class,0);
		}
		public RoundContext round() {
			return getRuleContext(RoundContext.class,0);
		}
		public MCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MCallContext mCall() throws RecognitionException {
		MCallContext _localctx = new MCallContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_mCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413); m();
			setState(414); round();
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

	public static class RoundContext extends ParserRuleContext {
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public PsContext ps() {
			return getRuleContext(PsContext.class,0);
		}
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public RoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_round; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterRound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitRound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitRound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoundContext round() throws RecognitionException {
		RoundContext _localctx = new RoundContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_round);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416); docsOpt();
			setState(417); ps();
			setState(418); match(CRound);
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

	public static class PsContext extends ParserRuleContext {
		public ETopContext eTop(int i) {
			return getRuleContext(ETopContext.class,i);
		}
		public TerminalNode X(int i) {
			return getToken(L42Parser.X, i);
		}
		public List<TerminalNode> X() { return getTokens(L42Parser.X); }
		public List<ETopContext> eTop() {
			return getRuleContexts(ETopContext.class);
		}
		public List<TerminalNode> Colon() { return getTokens(L42Parser.Colon); }
		public TerminalNode Colon(int i) {
			return getToken(L42Parser.Colon, i);
		}
		public PsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ps; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterPs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitPs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitPs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PsContext ps() throws RecognitionException {
		PsContext _localctx = new PsContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_ps);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(420); eTop();
				}
				break;
			}
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(423); match(X);
				setState(424); match(Colon);
				setState(425); eTop();
				}
				}
				setState(430);
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

	public static class K1Context extends ParserRuleContext {
		public TerminalNode Catch() { return getToken(L42Parser.Catch, 0); }
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public TerminalNode X() { return getToken(L42Parser.X, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public SSContext sS() {
			return getRuleContext(SSContext.class,0);
		}
		public K1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_k1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterK1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitK1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitK1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final K1Context k1() throws RecognitionException {
		K1Context _localctx = new K1Context(_ctx, getState());
		enterRule(_localctx, 82, RULE_k1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431); match(Catch);
			setState(432); sS();
			setState(433); t();
			setState(434); match(X);
			setState(435); eTop();
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

	public static class KManyContext extends ParserRuleContext {
		public TerminalNode Catch() { return getToken(L42Parser.Catch, 0); }
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public SSContext sS() {
			return getRuleContext(SSContext.class,0);
		}
		public KManyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kMany; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterKMany(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitKMany(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitKMany(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KManyContext kMany() throws RecognitionException {
		KManyContext _localctx = new KManyContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_kMany);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(437); match(Catch);
			setState(438); sS();
			setState(440); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(439); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(442); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(444); eTop();
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

	public static class KPropContext extends ParserRuleContext {
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode On() { return getToken(L42Parser.On, 0); }
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public SSContext sS() {
			return getRuleContext(SSContext.class,0);
		}
		public KPropContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kProp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterKProp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitKProp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitKProp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KPropContext kProp() throws RecognitionException {
		KPropContext _localctx = new KPropContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_kProp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(446); sS();
			setState(447); match(On);
			setState(449); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(448); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(451); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(453); eTop();
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

	public static class KContext extends ParserRuleContext {
		public KPropContext kProp() {
			return getRuleContext(KPropContext.class,0);
		}
		public K1Context k1() {
			return getRuleContext(K1Context.class,0);
		}
		public KManyContext kMany() {
			return getRuleContext(KManyContext.class,0);
		}
		public KContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_k; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterK(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitK(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitK(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KContext k() throws RecognitionException {
		KContext _localctx = new KContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_k);
		try {
			setState(458);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(455); k1();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(456); kMany();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(457); kProp();
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

	public static class KsContext extends ParserRuleContext {
		public List<KContext> k() {
			return getRuleContexts(KContext.class);
		}
		public KContext k(int i) {
			return getRuleContext(KContext.class,i);
		}
		public KsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterKs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitKs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitKs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KsContext ks() throws RecognitionException {
		KsContext _localctx = new KsContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_ks);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(460); k();
					}
					} 
				}
				setState(465);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
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

	public static class OnContext extends ParserRuleContext {
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public ETopContext eTop(int i) {
			return getRuleContext(ETopContext.class,i);
		}
		public List<ETopContext> eTop() {
			return getRuleContexts(ETopContext.class);
		}
		public TerminalNode On() { return getToken(L42Parser.On, 0); }
		public TerminalNode Case() { return getToken(L42Parser.Case, 0); }
		public OnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_on; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterOn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitOn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitOn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnContext on() throws RecognitionException {
		OnContext _localctx = new OnContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_on);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466); match(On);
			setState(467); t();
			setState(470);
			_la = _input.LA(1);
			if (_la==Case) {
				{
				setState(468); match(Case);
				setState(469); eTop();
				}
			}

			setState(472); eTop();
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

	public static class OnPlusContext extends ParserRuleContext {
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public ETopContext eTop(int i) {
			return getRuleContext(ETopContext.class,i);
		}
		public List<ETopContext> eTop() {
			return getRuleContexts(ETopContext.class);
		}
		public TerminalNode On() { return getToken(L42Parser.On, 0); }
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public TerminalNode Case() { return getToken(L42Parser.Case, 0); }
		public OnPlusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_onPlus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterOnPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitOnPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitOnPlus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnPlusContext onPlus() throws RecognitionException {
		OnPlusContext _localctx = new OnPlusContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_onPlus);
		int _la;
		try {
			int _alt;
			setState(490);
			switch (_input.LA(1)) {
			case On:
				enterOuterAlt(_localctx, 1);
				{
				setState(474); match(On);
				setState(476); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(475); t();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(478); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
				setState(482);
				_la = _input.LA(1);
				if (_la==Case) {
					{
					setState(480); match(Case);
					setState(481); eTop();
					}
				}

				setState(484); eTop();
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(486); match(Case);
				setState(487); eTop();
				setState(488); eTop();
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

	public static class NudeEContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(L42Parser.EOF, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public NudeEContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nudeE; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterNudeE(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitNudeE(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitNudeE(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NudeEContext nudeE() throws RecognitionException {
		NudeEContext _localctx = new NudeEContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_nudeE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492); eTop();
			setState(493); match(EOF);
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

	public static class ClassBExtraContext extends ParserRuleContext {
		public List<TerminalNode> Path() { return getTokens(L42Parser.Path); }
		public TerminalNode Stage() { return getToken(L42Parser.Stage, 0); }
		public TerminalNode EndType() { return getToken(L42Parser.EndType, 0); }
		public TerminalNode Path(int i) {
			return getToken(L42Parser.Path, i);
		}
		public ClassBExtraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBExtra; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterClassBExtra(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitClassBExtra(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitClassBExtra(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBExtraContext classBExtra() throws RecognitionException {
		ClassBExtraContext _localctx = new ClassBExtraContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_classBExtra);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			_la = _input.LA(1);
			if (_la==Stage) {
				{
				setState(495); match(Stage);
				}
			}

			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Path) {
				{
				{
				setState(498); match(Path);
				}
				}
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(504); match(EndType);
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

	public static class ClassBReuseContext extends ParserRuleContext {
		public TerminalNode CCurly() { return getToken(L42Parser.CCurly, 0); }
		public TerminalNode OCurly() { return getToken(L42Parser.OCurly, 0); }
		public MemberContext member(int i) {
			return getRuleContext(MemberContext.class,i);
		}
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public TerminalNode UrlNL() { return getToken(L42Parser.UrlNL, 0); }
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public TerminalNode Url() { return getToken(L42Parser.Url, 0); }
		public List<MemberContext> member() {
			return getRuleContexts(MemberContext.class);
		}
		public ClassBReuseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBReuse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterClassBReuse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitClassBReuse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitClassBReuse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBReuseContext classBReuse() throws RecognitionException {
		ClassBReuseContext _localctx = new ClassBReuseContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_classBReuse);
		int _la;
		try {
			setState(522);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(506); match(OCurly);
				setState(507); docsOpt();
				setState(508); match(Url);
				setState(509); match(CCurly);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(511); match(OCurly);
				setState(512); docsOpt();
				setState(513); match(UrlNL);
				setState(514); docsOpt();
				setState(516); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(515); member();
					}
					}
					setState(518); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Refine) | (1L << Path))) != 0) );
				setState(520); match(CCurly);
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

	public static class ClassBContext extends ParserRuleContext {
		public TerminalNode CCurly() { return getToken(L42Parser.CCurly, 0); }
		public ImplsContext impls() {
			return getRuleContext(ImplsContext.class,0);
		}
		public FieldDecContext fieldDec(int i) {
			return getRuleContext(FieldDecContext.class,i);
		}
		public TerminalNode OCurly() { return getToken(L42Parser.OCurly, 0); }
		public MemberContext member(int i) {
			return getRuleContext(MemberContext.class,i);
		}
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public List<FieldDecContext> fieldDec() {
			return getRuleContexts(FieldDecContext.class);
		}
		public List<MemberContext> member() {
			return getRuleContexts(MemberContext.class);
		}
		public ClassBExtraContext classBExtra() {
			return getRuleContext(ClassBExtraContext.class,0);
		}
		public ClassBContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classB; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterClassB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitClassB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitClassB(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBContext classB() throws RecognitionException {
		ClassBContext _localctx = new ClassBContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_classB);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(524); match(OCurly);
			setState(525); docsOpt();
			setState(526); header();
			setState(528);
			_la = _input.LA(1);
			if (_la==Implements) {
				{
				setState(527); impls();
				}
			}

			setState(533);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(530); fieldDec();
					}
					} 
				}
				setState(535);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			setState(536); docsOpt();
			setState(540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Refine) | (1L << Path))) != 0)) {
				{
				{
				setState(537); member();
				}
				}
				setState(542);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(543); match(CCurly);
			setState(545);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(544); classBExtra();
				}
				break;
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

	public static class ImplsContext extends ParserRuleContext {
		public TerminalNode Implements() { return getToken(L42Parser.Implements, 0); }
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public ImplsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_impls; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterImpls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitImpls(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitImpls(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImplsContext impls() throws RecognitionException {
		ImplsContext _localctx = new ImplsContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_impls);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(547); match(Implements);
			setState(549); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(548); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(551); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
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

	public static class MhsContext extends ParserRuleContext {
		public TerminalNode Method() { return getToken(L42Parser.Method, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public MethSelectorContext methSelector() {
			return getRuleContext(MethSelectorContext.class,0);
		}
		public MhsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mhs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMhs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMhs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMhs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MhsContext mhs() throws RecognitionException {
		MhsContext _localctx = new MhsContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_mhs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553); match(Method);
			setState(554); docsOpt();
			setState(555); methSelector();
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

	public static class MhtContext extends ParserRuleContext {
		public SExContext sEx() {
			return getRuleContext(SExContext.class,0);
		}
		public List<XContext> x() {
			return getRuleContexts(XContext.class);
		}
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
		}
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public TerminalNode Method() { return getToken(L42Parser.Method, 0); }
		public List<TContext> t() {
			return getRuleContexts(TContext.class);
		}
		public MDecContext mDec() {
			return getRuleContext(MDecContext.class,0);
		}
		public TerminalNode Refine() { return getToken(L42Parser.Refine, 0); }
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public TerminalNode Mdf() { return getToken(L42Parser.Mdf, 0); }
		public XContext x(int i) {
			return getRuleContext(XContext.class,i);
		}
		public MhtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mht; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMht(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMht(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMht(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MhtContext mht() throws RecognitionException {
		MhtContext _localctx = new MhtContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_mht);
		int _la;
		try {
			setState(608);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(558);
				_la = _input.LA(1);
				if (_la==Refine) {
					{
					setState(557); match(Refine);
					}
				}

				setState(561);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(560); match(Mdf);
					}
				}

				setState(563); match(Method);
				setState(564); docsOpt();
				setState(565); t();
				setState(569);
				switch (_input.LA(1)) {
				case MX:
				case HashX:
				case UnOp:
				case EqOp:
				case BoolOp:
				case RelOp:
				case DataOp:
					{
					setState(566); mDec();
					}
					break;
				case ORoundNoSpace:
					{
					setState(567); match(ORoundNoSpace);
					}
					break;
				case ORoundSpace:
					{
					setState(568); match(ORoundSpace);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
					{
					{
					setState(571); t();
					setState(572); x();
					setState(573); docsOpt();
					}
					}
					setState(579);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(580); sEx();
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(583);
				_la = _input.LA(1);
				if (_la==Refine) {
					{
					setState(582); match(Refine);
					}
				}

				setState(586);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(585); match(Mdf);
					}
				}

				setState(588); match(Method);
				setState(589); docsOpt();
				setState(590); t();
				setState(594);
				switch (_input.LA(1)) {
				case MX:
				case HashX:
				case UnOp:
				case EqOp:
				case BoolOp:
				case RelOp:
				case DataOp:
					{
					setState(591); mDec();
					}
					break;
				case ORoundNoSpace:
					{
					setState(592); match(ORoundNoSpace);
					}
					break;
				case ORoundSpace:
					{
					setState(593); match(ORoundSpace);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(602);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
					{
					{
					setState(596); t();
					setState(597); x();
					setState(598); docsOpt();
					}
					}
					setState(604);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(605); match(CRound);
				setState(606); docsOpt();
				}
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

	public static class MemberContext extends ParserRuleContext {
		public NestedClassContext nestedClass() {
			return getRuleContext(NestedClassContext.class,0);
		}
		public MethodWithTypeContext methodWithType() {
			return getRuleContext(MethodWithTypeContext.class,0);
		}
		public MethodImplementedContext methodImplemented() {
			return getRuleContext(MethodImplementedContext.class,0);
		}
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_member);
		try {
			setState(613);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(610); methodWithType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(611); methodImplemented();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(612); nestedClass();
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

	public static class MethodWithTypeContext extends ParserRuleContext {
		public ETopForMethodContext eTopForMethod() {
			return getRuleContext(ETopForMethodContext.class,0);
		}
		public MhtContext mht() {
			return getRuleContext(MhtContext.class,0);
		}
		public MethodWithTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodWithType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMethodWithType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMethodWithType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMethodWithType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodWithTypeContext methodWithType() throws RecognitionException {
		MethodWithTypeContext _localctx = new MethodWithTypeContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_methodWithType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(615); mht();
			setState(617);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(616); eTopForMethod();
				}
				break;
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

	public static class MethodImplementedContext extends ParserRuleContext {
		public ETopForMethodContext eTopForMethod() {
			return getRuleContext(ETopForMethodContext.class,0);
		}
		public MhsContext mhs() {
			return getRuleContext(MhsContext.class,0);
		}
		public MethodImplementedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodImplemented; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterMethodImplemented(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitMethodImplemented(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitMethodImplemented(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodImplementedContext methodImplemented() throws RecognitionException {
		MethodImplementedContext _localctx = new MethodImplementedContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_methodImplemented);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(619); mhs();
			setState(620); eTopForMethod();
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

	public static class NestedClassContext extends ParserRuleContext {
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public TerminalNode Colon() { return getToken(L42Parser.Colon, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public NestedClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedClass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterNestedClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitNestedClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitNestedClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedClassContext nestedClass() throws RecognitionException {
		NestedClassContext _localctx = new NestedClassContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_nestedClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622); match(Path);
			setState(623); match(Colon);
			setState(624); docsOpt();
			setState(625); eTop();
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

	public static class HeaderContext extends ParserRuleContext {
		public TerminalNode Interface() { return getToken(L42Parser.Interface, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitHeader(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_header);
		try {
			setState(629);
			switch (_input.LA(1)) {
			case Mdf:
			case CCurly:
			case Ph:
			case Implements:
			case Var:
			case Method:
			case Refine:
			case Path:
			case Doc:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case Interface:
				enterOuterAlt(_localctx, 2);
				{
				setState(628); match(Interface);
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

	public static class FieldDecContext extends ParserRuleContext {
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode Var() { return getToken(L42Parser.Var, 0); }
		public FieldDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterFieldDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitFieldDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitFieldDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDecContext fieldDec() throws RecognitionException {
		FieldDecContext _localctx = new FieldDecContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_fieldDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(632);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(631); match(Var);
				}
			}

			setState(634); t();
			setState(635); x();
			setState(636); docsOpt();
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

	public static class WContext extends ParserRuleContext {
		public WSwitchContext wSwitch() {
			return getRuleContext(WSwitchContext.class,0);
		}
		public WSimpleContext wSimple() {
			return getRuleContext(WSimpleContext.class,0);
		}
		public WContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_w; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterW(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitW(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitW(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WContext w() throws RecognitionException {
		WContext _localctx = new WContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_w);
		try {
			setState(640);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(638); wSwitch();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(639); wSimple();
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

	public static class WSwitchContext extends ParserRuleContext {
		public List<XContext> x() {
			return getRuleContexts(XContext.class);
		}
		public OnPlusContext onPlus(int i) {
			return getRuleContext(OnPlusContext.class,i);
		}
		public TerminalNode Default() { return getToken(L42Parser.Default, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public List<OnPlusContext> onPlus() {
			return getRuleContexts(OnPlusContext.class);
		}
		public IContext i(int i) {
			return getRuleContext(IContext.class,i);
		}
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public List<IContext> i() {
			return getRuleContexts(IContext.class);
		}
		public TerminalNode With() { return getToken(L42Parser.With, 0); }
		public List<VarDecContext> varDec() {
			return getRuleContexts(VarDecContext.class);
		}
		public XContext x(int i) {
			return getRuleContext(XContext.class,i);
		}
		public VarDecContext varDec(int i) {
			return getRuleContext(VarDecContext.class,i);
		}
		public WSwitchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wSwitch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterWSwitch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitWSwitch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitWSwitch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WSwitchContext wSwitch() throws RecognitionException {
		WSwitchContext _localctx = new WSwitchContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_wSwitch);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(642); match(With);
			setState(646);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(643); x();
					}
					} 
				}
				setState(648);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			setState(652);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(649); i();
					}
					} 
				}
				setState(654);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			}
			setState(658);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0)) {
				{
				{
				setState(655); varDec();
				}
				}
				setState(660);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(661); match(ORoundSpace);
			setState(663); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(662); onPlus();
				}
				}
				setState(665); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Case || _la==On );
			setState(669);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(667); match(Default);
				setState(668); eTop();
				}
			}

			setState(671); match(CRound);
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

	public static class IContext extends ParserRuleContext {
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode In() { return getToken(L42Parser.In, 0); }
		public TerminalNode Var() { return getToken(L42Parser.Var, 0); }
		public IContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_i; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IContext i() throws RecognitionException {
		IContext _localctx = new IContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_i);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(673); match(Var);
				}
			}

			setState(677);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(676); t();
				}
			}

			setState(679); x();
			setState(680); match(In);
			setState(681); eTop();
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

	public static class WSimpleContext extends ParserRuleContext {
		public IContext i(int i) {
			return getRuleContext(IContext.class,i);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<IContext> i() {
			return getRuleContexts(IContext.class);
		}
		public TerminalNode With() { return getToken(L42Parser.With, 0); }
		public WSimpleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wSimple; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterWSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitWSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitWSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WSimpleContext wSimple() throws RecognitionException {
		WSimpleContext _localctx = new WSimpleContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_wSimple);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683); match(With);
			setState(685); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(684); i();
				}
				}
				setState(687); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0) );
			setState(689); block();
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\39\u02b6\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u009c\n\3\3\4"+
		"\3\4\3\5\3\5\3\6\5\6\u00a3\n\6\3\7\5\7\u00a6\n\7\3\7\5\7\u00a9\n\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\5\b\u00b1\n\b\3\b\7\b\u00b4\n\b\f\b\16\b\u00b7\13"+
		"\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\6\13\u00c2\n\13\r\13\16\13\u00c3"+
		"\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u00d5"+
		"\n\r\f\r\16\r\u00d8\13\r\5\r\u00da\n\r\3\16\3\16\3\16\3\16\3\16\7\16\u00e1"+
		"\n\16\f\16\16\16\u00e4\13\16\3\17\3\17\3\17\3\17\3\17\7\17\u00eb\n\17"+
		"\f\17\16\17\u00ee\13\17\3\20\3\20\3\20\3\20\3\20\7\20\u00f5\n\20\f\20"+
		"\16\20\u00f8\13\20\3\21\3\21\3\22\3\22\3\23\5\23\u00ff\n\23\3\23\3\23"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u010c\n\24\f\24\16"+
		"\24\u010f\13\24\3\25\3\25\3\25\5\25\u0114\n\25\3\25\3\25\3\25\5\25\u0119"+
		"\n\25\3\25\3\25\5\25\u011d\n\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\5\25\u012b\n\25\3\25\5\25\u012e\n\25\3\26\3\26\3"+
		"\26\3\27\3\27\3\30\3\30\3\30\3\30\5\30\u0139\n\30\3\31\3\31\3\31\3\31"+
		"\3\31\5\31\u0140\n\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\5\36\u0154\n\36\3\37\3\37\3\37"+
		"\7\37\u0159\n\37\f\37\16\37\u015c\13\37\3\37\3\37\3\37\3 \3 \3 \7 \u0164"+
		"\n \f \16 \u0167\13 \3 \3 \3 \3!\6!\u016d\n!\r!\16!\u016e\3!\3!\3\"\3"+
		"\"\3\"\6\"\u0176\n\"\r\"\16\"\u0177\3\"\3\"\3#\5#\u017d\n#\3#\5#\u0180"+
		"\n#\3#\3#\3#\3#\3$\3$\3$\5$\u0189\n$\3%\3%\3&\3&\3&\3&\3&\3&\7&\u0193"+
		"\n&\f&\16&\u0196\13&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3"+
		")\3*\5*\u01a8\n*\3*\3*\3*\7*\u01ad\n*\f*\16*\u01b0\13*\3+\3+\3+\3+\3+"+
		"\3+\3,\3,\3,\6,\u01bb\n,\r,\16,\u01bc\3,\3,\3-\3-\3-\6-\u01c4\n-\r-\16"+
		"-\u01c5\3-\3-\3.\3.\3.\5.\u01cd\n.\3/\7/\u01d0\n/\f/\16/\u01d3\13/\3\60"+
		"\3\60\3\60\3\60\5\60\u01d9\n\60\3\60\3\60\3\61\3\61\6\61\u01df\n\61\r"+
		"\61\16\61\u01e0\3\61\3\61\5\61\u01e5\n\61\3\61\3\61\3\61\3\61\3\61\3\61"+
		"\5\61\u01ed\n\61\3\62\3\62\3\62\3\63\5\63\u01f3\n\63\3\63\7\63\u01f6\n"+
		"\63\f\63\16\63\u01f9\13\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\6\64\u0207\n\64\r\64\16\64\u0208\3\64\3\64\5\64\u020d"+
		"\n\64\3\65\3\65\3\65\3\65\5\65\u0213\n\65\3\65\7\65\u0216\n\65\f\65\16"+
		"\65\u0219\13\65\3\65\3\65\7\65\u021d\n\65\f\65\16\65\u0220\13\65\3\65"+
		"\3\65\5\65\u0224\n\65\3\66\3\66\6\66\u0228\n\66\r\66\16\66\u0229\3\67"+
		"\3\67\3\67\3\67\38\58\u0231\n8\38\58\u0234\n8\38\38\38\38\38\38\58\u023c"+
		"\n8\38\38\38\38\78\u0242\n8\f8\168\u0245\138\38\38\38\58\u024a\n8\38\5"+
		"8\u024d\n8\38\38\38\38\38\38\58\u0255\n8\38\38\38\38\78\u025b\n8\f8\16"+
		"8\u025e\138\38\38\38\58\u0263\n8\39\39\39\59\u0268\n9\3:\3:\5:\u026c\n"+
		":\3;\3;\3;\3<\3<\3<\3<\3<\3=\3=\5=\u0278\n=\3>\5>\u027b\n>\3>\3>\3>\3"+
		">\3?\3?\5?\u0283\n?\3@\3@\7@\u0287\n@\f@\16@\u028a\13@\3@\7@\u028d\n@"+
		"\f@\16@\u0290\13@\3@\7@\u0293\n@\f@\16@\u0296\13@\3@\3@\6@\u029a\n@\r"+
		"@\16@\u029b\3@\3@\5@\u02a0\n@\3@\3@\3A\5A\u02a5\nA\3A\5A\u02a8\nA\3A\3"+
		"A\3A\3A\3B\3B\6B\u02b0\nB\rB\16B\u02b1\3B\3B\3B\2\2C\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhj"+
		"lnprtvxz|~\u0080\u0082\2\4\4\2++--\3\2\3\5\u02e3\2\u0084\3\2\2\2\4\u009b"+
		"\3\2\2\2\6\u009d\3\2\2\2\b\u009f\3\2\2\2\n\u00a2\3\2\2\2\f\u00a5\3\2\2"+
		"\2\16\u00b0\3\2\2\2\20\u00ba\3\2\2\2\22\u00bc\3\2\2\2\24\u00be\3\2\2\2"+
		"\26\u00c5\3\2\2\2\30\u00d9\3\2\2\2\32\u00db\3\2\2\2\34\u00e5\3\2\2\2\36"+
		"\u00ef\3\2\2\2 \u00f9\3\2\2\2\"\u00fb\3\2\2\2$\u00fe\3\2\2\2&\u0102\3"+
		"\2\2\2(\u012d\3\2\2\2*\u012f\3\2\2\2,\u0132\3\2\2\2.\u0138\3\2\2\2\60"+
		"\u013a\3\2\2\2\62\u0141\3\2\2\2\64\u0147\3\2\2\2\66\u014b\3\2\2\28\u014e"+
		"\3\2\2\2:\u0153\3\2\2\2<\u0155\3\2\2\2>\u0160\3\2\2\2@\u016c\3\2\2\2B"+
		"\u0172\3\2\2\2D\u017c\3\2\2\2F\u0188\3\2\2\2H\u018a\3\2\2\2J\u018c\3\2"+
		"\2\2L\u019a\3\2\2\2N\u019f\3\2\2\2P\u01a2\3\2\2\2R\u01a7\3\2\2\2T\u01b1"+
		"\3\2\2\2V\u01b7\3\2\2\2X\u01c0\3\2\2\2Z\u01cc\3\2\2\2\\\u01d1\3\2\2\2"+
		"^\u01d4\3\2\2\2`\u01ec\3\2\2\2b\u01ee\3\2\2\2d\u01f2\3\2\2\2f\u020c\3"+
		"\2\2\2h\u020e\3\2\2\2j\u0225\3\2\2\2l\u022b\3\2\2\2n\u0262\3\2\2\2p\u0267"+
		"\3\2\2\2r\u0269\3\2\2\2t\u026d\3\2\2\2v\u0270\3\2\2\2x\u0277\3\2\2\2z"+
		"\u027a\3\2\2\2|\u0282\3\2\2\2~\u0284\3\2\2\2\u0080\u02a4\3\2\2\2\u0082"+
		"\u02ad\3\2\2\2\u0084\u0085\t\2\2\2\u0085\3\3\2\2\2\u0086\u009c\5\2\2\2"+
		"\u0087\u0088\7\64\2\2\u0088\u009c\7\7\2\2\u0089\u008a\7\65\2\2\u008a\u009c"+
		"\7\7\2\2\u008b\u008c\7\66\2\2\u008c\u009c\7\7\2\2\u008d\u008e\7\67\2\2"+
		"\u008e\u009c\7\7\2\2\u008f\u0090\78\2\2\u0090\u009c\7\7\2\2\u0091\u0092"+
		"\7\64\2\2\u0092\u009c\7\b\2\2\u0093\u0094\7\65\2\2\u0094\u009c\7\b\2\2"+
		"\u0095\u0096\7\66\2\2\u0096\u009c\7\b\2\2\u0097\u0098\7\67\2\2\u0098\u009c"+
		"\7\b\2\2\u0099\u009a\78\2\2\u009a\u009c\7\b\2\2\u009b\u0086\3\2\2\2\u009b"+
		"\u0087\3\2\2\2\u009b\u0089\3\2\2\2\u009b\u008b\3\2\2\2\u009b\u008d\3\2"+
		"\2\2\u009b\u008f\3\2\2\2\u009b\u0091\3\2\2\2\u009b\u0093\3\2\2\2\u009b"+
		"\u0095\3\2\2\2\u009b\u0097\3\2\2\2\u009b\u0099\3\2\2\2\u009c\5\3\2\2\2"+
		"\u009d\u009e\7)\2\2\u009e\7\3\2\2\2\u009f\u00a0\7\62\2\2\u00a0\t\3\2\2"+
		"\2\u00a1\u00a3\7\62\2\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3"+
		"\13\3\2\2\2\u00a4\u00a6\7\24\2\2\u00a5\u00a4\3\2\2\2\u00a5\u00a6\3\2\2"+
		"\2\u00a6\u00a8\3\2\2\2\u00a7\u00a9\7\6\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9"+
		"\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\7)\2\2\u00ab\u00ac\5\n\6\2\u00ac"+
		"\r\3\2\2\2\u00ad\u00b1\5\4\3\2\u00ae\u00b1\7\7\2\2\u00af\u00b1\7\b\2\2"+
		"\u00b0\u00ad\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00af\3\2\2\2\u00b1\u00b5"+
		"\3\2\2\2\u00b2\u00b4\5\20\t\2\u00b3\u00b2\3\2\2\2\u00b4\u00b7\3\2\2\2"+
		"\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00b5"+
		"\3\2\2\2\u00b8\u00b9\7\t\2\2\u00b9\17\3\2\2\2\u00ba\u00bb\7,\2\2\u00bb"+
		"\21\3\2\2\2\u00bc\u00bd\t\3\2\2\u00bd\23\3\2\2\2\u00be\u00bf\7\t\2\2\u00bf"+
		"\u00c1\7\4\2\2\u00c0\u00c2\5\f\7\2\u00c1\u00c0\3\2\2\2\u00c2\u00c3\3\2"+
		"\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\25\3\2\2\2\u00c5\u00c6"+
		"\7,\2\2\u00c6\u00c7\7\65\2\2\u00c7\u00c8\5\n\6\2\u00c8\u00c9\5\32\16\2"+
		"\u00c9\27\3\2\2\2\u00ca\u00da\5\32\16\2\u00cb\u00d6\5<\37\2\u00cc\u00d5"+
		"\5L\'\2\u00cd\u00d5\5J&\2\u00ce\u00cf\7\22\2\2\u00cf\u00d5\5N(\2\u00d0"+
		"\u00d1\7\7\2\2\u00d1\u00d5\5P)\2\u00d2\u00d5\5\b\5\2\u00d3\u00d5\5H%\2"+
		"\u00d4\u00cc\3\2\2\2\u00d4\u00cd\3\2\2\2\u00d4\u00ce\3\2\2\2\u00d4\u00d0"+
		"\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d3\3\2\2\2\u00d5\u00d8\3\2\2\2\u00d6"+
		"\u00d4\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2"+
		"\2\2\u00d9\u00ca\3\2\2\2\u00d9\u00cb\3\2\2\2\u00da\31\3\2\2\2\u00db\u00e2"+
		"\5\34\17\2\u00dc\u00dd\7\66\2\2\u00dd\u00de\5\n\6\2\u00de\u00df\5\34\17"+
		"\2\u00df\u00e1\3\2\2\2\u00e0\u00dc\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0"+
		"\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\33\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5"+
		"\u00ec\5\36\20\2\u00e6\u00e7\7\67\2\2\u00e7\u00e8\5\n\6\2\u00e8\u00e9"+
		"\5\36\20\2\u00e9\u00eb\3\2\2\2\u00ea\u00e6\3\2\2\2\u00eb\u00ee\3\2\2\2"+
		"\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\35\3\2\2\2\u00ee\u00ec"+
		"\3\2\2\2\u00ef\u00f6\5 \21\2\u00f0\u00f1\78\2\2\u00f1\u00f2\5\n\6\2\u00f2"+
		"\u00f3\5 \21\2\u00f3\u00f5\3\2\2\2\u00f4\u00f0\3\2\2\2\u00f5\u00f8\3\2"+
		"\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\37\3\2\2\2\u00f8\u00f6"+
		"\3\2\2\2\u00f9\u00fa\5$\23\2\u00fa!\3\2\2\2\u00fb\u00fc\79\2\2\u00fc#"+
		"\3\2\2\2\u00fd\u00ff\7\64\2\2\u00fe\u00fd\3\2\2\2\u00fe\u00ff\3\2\2\2"+
		"\u00ff\u0100\3\2\2\2\u0100\u0101\5&\24\2\u0101%\3\2\2\2\u0102\u010d\5"+
		"(\25\2\u0103\u010c\5L\'\2\u0104\u010c\5J&\2\u0105\u0106\7\22\2\2\u0106"+
		"\u010c\5N(\2\u0107\u0108\7\7\2\2\u0108\u010c\5P)\2\u0109\u010c\5\b\5\2"+
		"\u010a\u010c\5H%\2\u010b\u0103\3\2\2\2\u010b\u0104\3\2\2\2\u010b\u0105"+
		"\3\2\2\2\u010b\u0107\3\2\2\2\u010b\u0109\3\2\2\2\u010b\u010a\3\2\2\2\u010c"+
		"\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\'\3\2\2\2"+
		"\u010f\u010d\3\2\2\2\u0110\u012e\5f\64\2\u0111\u012e\5h\65\2\u0112\u0114"+
		"\5\"\22\2\u0113\u0112\3\2\2\2\u0113\u0114\3\2\2\2\u0114\u0115\3\2\2\2"+
		"\u0115\u012e\5\20\t\2\u0116\u012e\5\26\f\2\u0117\u0119\5\"\22\2\u0118"+
		"\u0117\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u012e\7)"+
		"\2\2\u011b\u011d\5\"\22\2\u011c\u011b\3\2\2\2\u011c\u011d\3\2\2\2\u011d"+
		"\u011e\3\2\2\2\u011e\u012e\5:\36\2\u011f\u012e\5\60\31\2\u0120\u012e\5"+
		"\64\33\2\u0121\u012e\5\66\34\2\u0122\u012e\58\35\2\u0123\u012e\7\'\2\2"+
		"\u0124\u012e\5|?\2\u0125\u012e\5\62\32\2\u0126\u012e\7\r\2\2\u0127\u012e"+
		"\5*\26\2\u0128\u012e\5.\30\2\u0129\u012b\5\"\22\2\u012a\u0129\3\2\2\2"+
		"\u012a\u012b\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012e\5,\27\2\u012d\u0110"+
		"\3\2\2\2\u012d\u0111\3\2\2\2\u012d\u0113\3\2\2\2\u012d\u0116\3\2\2\2\u012d"+
		"\u0118\3\2\2\2\u012d\u011c\3\2\2\2\u012d\u011f\3\2\2\2\u012d\u0120\3\2"+
		"\2\2\u012d\u0121\3\2\2\2\u012d\u0122\3\2\2\2\u012d\u0123\3\2\2\2\u012d"+
		"\u0124\3\2\2\2\u012d\u0125\3\2\2\2\u012d\u0126\3\2\2\2\u012d\u0127\3\2"+
		"\2\2\u012d\u0128\3\2\2\2\u012d\u012a\3\2\2\2\u012e)\3\2\2\2\u012f\u0130"+
		"\7+\2\2\u0130\u0131\5P)\2\u0131+\3\2\2\2\u0132\u0133\7.\2\2\u0133-\3\2"+
		"\2\2\u0134\u0135\7#\2\2\u0135\u0139\5J&\2\u0136\u0137\7#\2\2\u0137\u0139"+
		"\5L\'\2\u0138\u0134\3\2\2\2\u0138\u0136\3\2\2\2\u0139/\3\2\2\2\u013a\u013b"+
		"\7\27\2\2\u013b\u013c\5\32\16\2\u013c\u013f\5:\36\2\u013d\u013e\7\30\2"+
		"\2\u013e\u0140\5\32\16\2\u013f\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140"+
		"\61\3\2\2\2\u0141\u0142\7#\2\2\u0142\u0143\7)\2\2\u0143\u0144\7$\2\2\u0144"+
		"\u0145\5N(\2\u0145\u0146\5\32\16\2\u0146\63\3\2\2\2\u0147\u0148\7\31\2"+
		"\2\u0148\u0149\5\32\16\2\u0149\u014a\5:\36\2\u014a\65\3\2\2\2\u014b\u014c"+
		"\5\22\n\2\u014c\u014d\5\32\16\2\u014d\67\3\2\2\2\u014e\u014f\7\32\2\2"+
		"\u014f\u0150\5\32\16\2\u01509\3\2\2\2\u0151\u0154\5> \2\u0152\u0154\5"+
		"B\"\2\u0153\u0151\3\2\2\2\u0153\u0152\3\2\2\2\u0154;\3\2\2\2\u0155\u0156"+
		"\7\7\2\2\u0156\u015a\5\n\6\2\u0157\u0159\5@!\2\u0158\u0157\3\2\2\2\u0159"+
		"\u015c\3\2\2\2\u015a\u0158\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015d\3\2"+
		"\2\2\u015c\u015a\3\2\2\2\u015d\u015e\5\32\16\2\u015e\u015f\7\t\2\2\u015f"+
		"=\3\2\2\2\u0160\u0161\7\b\2\2\u0161\u0165\5\n\6\2\u0162\u0164\5@!\2\u0163"+
		"\u0162\3\2\2\2\u0164\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2"+
		"\2\2\u0166\u0168\3\2\2\2\u0167\u0165\3\2\2\2\u0168\u0169\5\32\16\2\u0169"+
		"\u016a\7\t\2\2\u016a?\3\2\2\2\u016b\u016d\5F$\2\u016c\u016b\3\2\2\2\u016d"+
		"\u016e\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0170\3\2"+
		"\2\2\u0170\u0171\5\\/\2\u0171A\3\2\2\2\u0172\u0173\7\f\2\2\u0173\u0175"+
		"\5\n\6\2\u0174\u0176\5@!\2\u0175\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u017a\7\17"+
		"\2\2\u017aC\3\2\2\2\u017b\u017d\7\37\2\2\u017c\u017b\3\2\2\2\u017c\u017d"+
		"\3\2\2\2\u017d\u017f\3\2\2\2\u017e\u0180\5\f\7\2\u017f\u017e\3\2\2\2\u017f"+
		"\u0180\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0182\5\20\t\2\u0182\u0183\7"+
		"\23\2\2\u0183\u0184\5\32\16\2\u0184E\3\2\2\2\u0185\u0189\5D#\2\u0186\u0189"+
		"\5\32\16\2\u0187\u0189\5v<\2\u0188\u0185\3\2\2\2\u0188\u0186\3\2\2\2\u0188"+
		"\u0187\3\2\2\2\u0189G\3\2\2\2\u018a\u018b\7/\2\2\u018bI\3\2\2\2\u018c"+
		"\u018d\7\n\2\2\u018d\u0194\5\n\6\2\u018e\u018f\5R*\2\u018f\u0190\7\21"+
		"\2\2\u0190\u0191\5\n\6\2\u0191\u0193\3\2\2\2\u0192\u018e\3\2\2\2\u0193"+
		"\u0196\3\2\2\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0197\3\2"+
		"\2\2\u0196\u0194\3\2\2\2\u0197\u0198\5R*\2\u0198\u0199\7\13\2\2\u0199"+
		"K\3\2\2\2\u019a\u019b\7\n\2\2\u019b\u019c\5\n\6\2\u019c\u019d\5|?\2\u019d"+
		"\u019e\7\13\2\2\u019eM\3\2\2\2\u019f\u01a0\5\2\2\2\u01a0\u01a1\5P)\2\u01a1"+
		"O\3\2\2\2\u01a2\u01a3\5\n\6\2\u01a3\u01a4\5R*\2\u01a4\u01a5\7\t\2\2\u01a5"+
		"Q\3\2\2\2\u01a6\u01a8\5\32\16\2\u01a7\u01a6\3\2\2\2\u01a7\u01a8\3\2\2"+
		"\2\u01a8\u01ae\3\2\2\2\u01a9\u01aa\7,\2\2\u01aa\u01ab\7\20\2\2\u01ab\u01ad"+
		"\5\32\16\2\u01ac\u01a9\3\2\2\2\u01ad\u01b0\3\2\2\2\u01ae\u01ac\3\2\2\2"+
		"\u01ae\u01af\3\2\2\2\u01afS\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b1\u01b2\7"+
		"\36\2\2\u01b2\u01b3\5\22\n\2\u01b3\u01b4\5\f\7\2\u01b4\u01b5\7,\2\2\u01b5"+
		"\u01b6\5\32\16\2\u01b6U\3\2\2\2\u01b7\u01b8\7\36\2\2\u01b8\u01ba\5\22"+
		"\n\2\u01b9\u01bb\5\f\7\2\u01ba\u01b9\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc"+
		"\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\u01bf\5\32"+
		"\16\2\u01bfW\3\2\2\2\u01c0\u01c1\5\22\n\2\u01c1\u01c3\7\34\2\2\u01c2\u01c4"+
		"\5\f\7\2\u01c3\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c5"+
		"\u01c6\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\5\32\16\2\u01c8Y\3\2\2"+
		"\2\u01c9\u01cd\5T+\2\u01ca\u01cd\5V,\2\u01cb\u01cd\5X-\2\u01cc\u01c9\3"+
		"\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cb\3\2\2\2\u01cd[\3\2\2\2\u01ce\u01d0"+
		"\5Z.\2\u01cf\u01ce\3\2\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d1"+
		"\u01d2\3\2\2\2\u01d2]\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d4\u01d5\7\34\2\2"+
		"\u01d5\u01d8\5\f\7\2\u01d6\u01d7\7\26\2\2\u01d7\u01d9\5\32\16\2\u01d8"+
		"\u01d6\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01db\5\32"+
		"\16\2\u01db_\3\2\2\2\u01dc\u01de\7\34\2\2\u01dd\u01df\5\f\7\2\u01de\u01dd"+
		"\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1"+
		"\u01e4\3\2\2\2\u01e2\u01e3\7\26\2\2\u01e3\u01e5\5\32\16\2\u01e4\u01e2"+
		"\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e7\5\32\16\2"+
		"\u01e7\u01ed\3\2\2\2\u01e8\u01e9\7\26\2\2\u01e9\u01ea\5\32\16\2\u01ea"+
		"\u01eb\5\32\16\2\u01eb\u01ed\3\2\2\2\u01ec\u01dc\3\2\2\2\u01ec\u01e8\3"+
		"\2\2\2\u01eda\3\2\2\2\u01ee\u01ef\5\32\16\2\u01ef\u01f0\7\2\2\3\u01f0"+
		"c\3\2\2\2\u01f1\u01f3\7(\2\2\u01f2\u01f1\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3"+
		"\u01f7\3\2\2\2\u01f4\u01f6\7)\2\2\u01f5\u01f4\3\2\2\2\u01f6\u01f9\3\2"+
		"\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa\3\2\2\2\u01f9"+
		"\u01f7\3\2\2\2\u01fa\u01fb\7\16\2\2\u01fbe\3\2\2\2\u01fc\u01fd\7\f\2\2"+
		"\u01fd\u01fe\5\n\6\2\u01fe\u01ff\7\61\2\2\u01ff\u0200\7\17\2\2\u0200\u020d"+
		"\3\2\2\2\u0201\u0202\7\f\2\2\u0202\u0203\5\n\6\2\u0203\u0204\7\60\2\2"+
		"\u0204\u0206\5\n\6\2\u0205\u0207\5p9\2\u0206\u0205\3\2\2\2\u0207\u0208"+
		"\3\2\2\2\u0208\u0206\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u020a\3\2\2\2\u020a"+
		"\u020b\7\17\2\2\u020b\u020d\3\2\2\2\u020c\u01fc\3\2\2\2\u020c\u0201\3"+
		"\2\2\2\u020dg\3\2\2\2\u020e\u020f\7\f\2\2\u020f\u0210\5\n\6\2\u0210\u0212"+
		"\5x=\2\u0211\u0213\5j\66\2\u0212\u0211\3\2\2\2\u0212\u0213\3\2\2\2\u0213"+
		"\u0217\3\2\2\2\u0214\u0216\5z>\2\u0215\u0214\3\2\2\2\u0216\u0219\3\2\2"+
		"\2\u0217\u0215\3\2\2\2\u0217\u0218\3\2\2\2\u0218\u021a\3\2\2\2\u0219\u0217"+
		"\3\2\2\2\u021a\u021e\5\n\6\2\u021b\u021d\5p9\2\u021c\u021b\3\2\2\2\u021d"+
		"\u0220\3\2\2\2\u021e\u021c\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0221\3\2"+
		"\2\2\u0220\u021e\3\2\2\2\u0221\u0223\7\17\2\2\u0222\u0224\5d\63\2\u0223"+
		"\u0222\3\2\2\2\u0223\u0224\3\2\2\2\u0224i\3\2\2\2\u0225\u0227\7\25\2\2"+
		"\u0226\u0228\5\f\7\2\u0227\u0226\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u0227"+
		"\3\2\2\2\u0229\u022a\3\2\2\2\u022ak\3\2\2\2\u022b\u022c\7\"\2\2\u022c"+
		"\u022d\5\n\6\2\u022d\u022e\5\16\b\2\u022em\3\2\2\2\u022f\u0231\7%\2\2"+
		"\u0230\u022f\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u0233\3\2\2\2\u0232\u0234"+
		"\7\6\2\2\u0233\u0232\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0235\3\2\2\2\u0235"+
		"\u0236\7\"\2\2\u0236\u0237\5\n\6\2\u0237\u023b\5\f\7\2\u0238\u023c\5\4"+
		"\3\2\u0239\u023c\7\7\2\2\u023a\u023c\7\b\2\2\u023b\u0238\3\2\2\2\u023b"+
		"\u0239\3\2\2\2\u023b\u023a\3\2\2\2\u023c\u0243\3\2\2\2\u023d\u023e\5\f"+
		"\7\2\u023e\u023f\5\20\t\2\u023f\u0240\5\n\6\2\u0240\u0242\3\2\2\2\u0241"+
		"\u023d\3\2\2\2\u0242\u0245\3\2\2\2\u0243\u0241\3\2\2\2\u0243\u0244\3\2"+
		"\2\2\u0244\u0246\3\2\2\2\u0245\u0243\3\2\2\2\u0246\u0247\5\24\13\2\u0247"+
		"\u0263\3\2\2\2\u0248\u024a\7%\2\2\u0249\u0248\3\2\2\2\u0249\u024a\3\2"+
		"\2\2\u024a\u024c\3\2\2\2\u024b\u024d\7\6\2\2\u024c\u024b\3\2\2\2\u024c"+
		"\u024d\3\2\2\2\u024d\u024e\3\2\2\2\u024e\u024f\7\"\2\2\u024f\u0250\5\n"+
		"\6\2\u0250\u0254\5\f\7\2\u0251\u0255\5\4\3\2\u0252\u0255\7\7\2\2\u0253"+
		"\u0255\7\b\2\2\u0254\u0251\3\2\2\2\u0254\u0252\3\2\2\2\u0254\u0253\3\2"+
		"\2\2\u0255\u025c\3\2\2\2\u0256\u0257\5\f\7\2\u0257\u0258\5\20\t\2\u0258"+
		"\u0259\5\n\6\2\u0259\u025b\3\2\2\2\u025a\u0256\3\2\2\2\u025b\u025e\3\2"+
		"\2\2\u025c\u025a\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u025f\3\2\2\2\u025e"+
		"\u025c\3\2\2\2\u025f\u0260\7\t\2\2\u0260\u0261\5\n\6\2\u0261\u0263\3\2"+
		"\2\2\u0262\u0230\3\2\2\2\u0262\u0249\3\2\2\2\u0263o\3\2\2\2\u0264\u0268"+
		"\5r:\2\u0265\u0268\5t;\2\u0266\u0268\5v<\2\u0267\u0264\3\2\2\2\u0267\u0265"+
		"\3\2\2\2\u0267\u0266\3\2\2\2\u0268q\3\2\2\2\u0269\u026b\5n8\2\u026a\u026c"+
		"\5\30\r\2\u026b\u026a\3\2\2\2\u026b\u026c\3\2\2\2\u026cs\3\2\2\2\u026d"+
		"\u026e\5l\67\2\u026e\u026f\5\30\r\2\u026fu\3\2\2\2\u0270\u0271\7)\2\2"+
		"\u0271\u0272\7\20\2\2\u0272\u0273\5\n\6\2\u0273\u0274\5\32\16\2\u0274"+
		"w\3\2\2\2\u0275\u0278\3\2\2\2\u0276\u0278\7!\2\2\u0277\u0275\3\2\2\2\u0277"+
		"\u0276\3\2\2\2\u0278y\3\2\2\2\u0279\u027b\7\37\2\2\u027a\u0279\3\2\2\2"+
		"\u027a\u027b\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u027d\5\f\7\2\u027d\u027e"+
		"\5\20\t\2\u027e\u027f\5\n\6\2\u027f{\3\2\2\2\u0280\u0283\5~@\2\u0281\u0283"+
		"\5\u0082B\2\u0282\u0280\3\2\2\2\u0282\u0281\3\2\2\2\u0283}\3\2\2\2\u0284"+
		"\u0288\7\33\2\2\u0285\u0287\5\20\t\2\u0286\u0285\3\2\2\2\u0287\u028a\3"+
		"\2\2\2\u0288\u0286\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u028e\3\2\2\2\u028a"+
		"\u0288\3\2\2\2\u028b\u028d\5\u0080A\2\u028c\u028b\3\2\2\2\u028d\u0290"+
		"\3\2\2\2\u028e\u028c\3\2\2\2\u028e\u028f\3\2\2\2\u028f\u0294\3\2\2\2\u0290"+
		"\u028e\3\2\2\2\u0291\u0293\5D#\2\u0292\u0291\3\2\2\2\u0293\u0296\3\2\2"+
		"\2\u0294\u0292\3\2\2\2\u0294\u0295\3\2\2\2\u0295\u0297\3\2\2\2\u0296\u0294"+
		"\3\2\2\2\u0297\u0299\7\b\2\2\u0298\u029a\5`\61\2\u0299\u0298\3\2\2\2\u029a"+
		"\u029b\3\2\2\2\u029b\u0299\3\2\2\2\u029b\u029c\3\2\2\2\u029c\u029f\3\2"+
		"\2\2\u029d\u029e\7 \2\2\u029e\u02a0\5\32\16\2\u029f\u029d\3\2\2\2\u029f"+
		"\u02a0\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1\u02a2\7\t\2\2\u02a2\177\3\2\2"+
		"\2\u02a3\u02a5\7\37\2\2\u02a4\u02a3\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5"+
		"\u02a7\3\2\2\2\u02a6\u02a8\5\f\7\2\u02a7\u02a6\3\2\2\2\u02a7\u02a8\3\2"+
		"\2\2\u02a8\u02a9\3\2\2\2\u02a9\u02aa\5\20\t\2\u02aa\u02ab\7\35\2\2\u02ab"+
		"\u02ac\5\32\16\2\u02ac\u0081\3\2\2\2\u02ad\u02af\7\33\2\2\u02ae\u02b0"+
		"\5\u0080A\2\u02af\u02ae\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02af\3\2\2"+
		"\2\u02b1\u02b2\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3\u02b4\5:\36\2\u02b4\u0083"+
		"\3\2\2\2K\u009b\u00a2\u00a5\u00a8\u00b0\u00b5\u00c3\u00d4\u00d6\u00d9"+
		"\u00e2\u00ec\u00f6\u00fe\u010b\u010d\u0113\u0118\u011c\u012a\u012d\u0138"+
		"\u013f\u0153\u015a\u0165\u016e\u0177\u017c\u017f\u0188\u0194\u01a7\u01ae"+
		"\u01bc\u01c5\u01cc\u01d1\u01d8\u01e0\u01e4\u01ec\u01f2\u01f7\u0208\u020c"+
		"\u0212\u0217\u021e\u0223\u0229\u0230\u0233\u023b\u0243\u0249\u024c\u0254"+
		"\u025c\u0262\u0267\u026b\u0277\u027a\u0282\u0288\u028e\u0294\u029b\u029f"+
		"\u02a4\u02a7\u02b1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}