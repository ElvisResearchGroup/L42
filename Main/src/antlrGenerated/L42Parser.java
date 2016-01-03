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
		S=1, Mdf=2, ORoundNoSpace=3, ORoundSpace=4, CRound=5, OSquare=6, CSquare=7, 
		OCurly=8, DotDotDot=9, EndType=10, CCurly=11, Colon=12, Semicolon=13, 
		Dot=14, Equal=15, Ph=16, Implements=17, Case=18, If=19, Else=20, While=21, 
		Loop=22, With=23, On=24, In=25, Catch=26, Var=27, Default=28, Interface=29, 
		Method=30, Using=31, Check=32, FieldSpecial=33, WalkBy=34, Stage=35, Path=36, 
		ClassSep=37, MX=38, X=39, HashX=40, StringQuote=41, UrlNL=42, Url=43, 
		Doc=44, WS=45, UnOp=46, EqOp=47, BoolOp=48, RelOp=49, DataOp=50, NumParse=51;
	public static final String[] tokenNames = {
		"<INVALID>", "S", "Mdf", "'('", "'\t'", "')'", "'['", "']'", "'{'", "'...'", 
		"'^##'", "'}'", "':'", "';'", "'.'", "'='", "'fwd'", "'<:'", "'case'", 
		"'if'", "'else'", "'while'", "'loop'", "'with'", "'on'", "'in'", "'catch'", 
		"'var'", "'default'", "'interface'", "'method'", "'use'", "'check'", "'##field'", 
		"'##walkBy'", "Stage", "Path", "'::'", "MX", "X", "HashX", "StringQuote", 
		"UrlNL", "Url", "Doc", "WS", "UnOp", "EqOp", "BoolOp", "RelOp", "DataOp", 
		"NumParse"
	};
	public static final int
		RULE_m = 0, RULE_mDec = 1, RULE_path = 2, RULE_docs = 3, RULE_docsOpt = 4, 
		RULE_t = 5, RULE_concreteT = 6, RULE_historicalSeq = 7, RULE_historicalT = 8, 
		RULE_methSelector = 9, RULE_x = 10, RULE_xOp = 11, RULE_eTopForMethod = 12, 
		RULE_eTop = 13, RULE_eL3 = 14, RULE_eL2 = 15, RULE_eL1 = 16, RULE_numParse = 17, 
		RULE_eUnOp = 18, RULE_ePost = 19, RULE_eAtom = 20, RULE_mxRound = 21, 
		RULE_useSquare = 22, RULE_ifExpr = 23, RULE_using = 24, RULE_whileExpr = 25, 
		RULE_signalExpr = 26, RULE_loopExpr = 27, RULE_block = 28, RULE_roundBlockForMethod = 29, 
		RULE_roundBlock = 30, RULE_bb = 31, RULE_curlyBlock = 32, RULE_varDec = 33, 
		RULE_d = 34, RULE_stringParse = 35, RULE_square = 36, RULE_squareW = 37, 
		RULE_mCall = 38, RULE_round = 39, RULE_ps = 40, RULE_k = 41, RULE_on = 42, 
		RULE_onPlus = 43, RULE_nudeE = 44, RULE_classBExtra = 45, RULE_classBReuse = 46, 
		RULE_classB = 47, RULE_mhs = 48, RULE_mht = 49, RULE_member = 50, RULE_methodWithType = 51, 
		RULE_methodImplemented = 52, RULE_nestedClass = 53, RULE_header = 54, 
		RULE_fieldDec = 55, RULE_w = 56, RULE_wSwitch = 57, RULE_i = 58, RULE_wSimple = 59;
	public static final String[] ruleNames = {
		"m", "mDec", "path", "docs", "docsOpt", "t", "concreteT", "historicalSeq", 
		"historicalT", "methSelector", "x", "xOp", "eTopForMethod", "eTop", "eL3", 
		"eL2", "eL1", "numParse", "eUnOp", "ePost", "eAtom", "mxRound", "useSquare", 
		"ifExpr", "using", "whileExpr", "signalExpr", "loopExpr", "block", "roundBlockForMethod", 
		"roundBlock", "bb", "curlyBlock", "varDec", "d", "stringParse", "square", 
		"squareW", "mCall", "round", "ps", "k", "on", "onPlus", "nudeE", "classBExtra", 
		"classBReuse", "classB", "mhs", "mht", "member", "methodWithType", "methodImplemented", 
		"nestedClass", "header", "fieldDec", "w", "wSwitch", "i", "wSimple"
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
			setState(120);
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
			setState(143);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(122); m();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(123); match(UnOp);
				setState(124); match(ORoundNoSpace);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(125); match(EqOp);
				setState(126); match(ORoundNoSpace);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(127); match(BoolOp);
				setState(128); match(ORoundNoSpace);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(129); match(RelOp);
				setState(130); match(ORoundNoSpace);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(131); match(DataOp);
				setState(132); match(ORoundNoSpace);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(133); match(UnOp);
				setState(134); match(ORoundSpace);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(135); match(EqOp);
				setState(136); match(ORoundSpace);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(137); match(BoolOp);
				setState(138); match(ORoundSpace);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(139); match(RelOp);
				setState(140); match(ORoundSpace);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(141); match(DataOp);
				setState(142); match(ORoundSpace);
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
			setState(145); match(Path);
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
			setState(147); match(Doc);
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
			setState(150);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(149); match(Doc);
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
		public ConcreteTContext concreteT() {
			return getRuleContext(ConcreteTContext.class,0);
		}
		public HistoricalTContext historicalT() {
			return getRuleContext(HistoricalTContext.class,0);
		}
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
		try {
			setState(154);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152); concreteT();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(153); historicalT();
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

	public static class ConcreteTContext extends ParserRuleContext {
		public TerminalNode Ph() { return getToken(L42Parser.Ph, 0); }
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public TerminalNode Mdf() { return getToken(L42Parser.Mdf, 0); }
		public ConcreteTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concreteT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterConcreteT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitConcreteT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitConcreteT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConcreteTContext concreteT() throws RecognitionException {
		ConcreteTContext _localctx = new ConcreteTContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_concreteT);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(156); match(Ph);
				}
			}

			setState(160);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(159); match(Mdf);
				}
			}

			setState(162); match(Path);
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

	public static class HistoricalSeqContext extends ParserRuleContext {
		public List<TerminalNode> ClassSep() { return getTokens(L42Parser.ClassSep); }
		public TerminalNode ClassSep(int i) {
			return getToken(L42Parser.ClassSep, i);
		}
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public MethSelectorContext methSelector() {
			return getRuleContext(MethSelectorContext.class,0);
		}
		public HistoricalSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historicalSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterHistoricalSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitHistoricalSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitHistoricalSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistoricalSeqContext historicalSeq() throws RecognitionException {
		HistoricalSeqContext _localctx = new HistoricalSeqContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_historicalSeq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164); match(ClassSep);
			setState(165); methSelector();
			setState(168);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(166); match(ClassSep);
				setState(167); x();
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

	public static class HistoricalTContext extends ParserRuleContext {
		public List<HistoricalSeqContext> historicalSeq() {
			return getRuleContexts(HistoricalSeqContext.class);
		}
		public HistoricalSeqContext historicalSeq(int i) {
			return getRuleContext(HistoricalSeqContext.class,i);
		}
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public HistoricalTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_historicalT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).enterHistoricalT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof L42Listener ) ((L42Listener)listener).exitHistoricalT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof L42Visitor ) return ((L42Visitor<? extends T>)visitor).visitHistoricalT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HistoricalTContext historicalT() throws RecognitionException {
		HistoricalTContext _localctx = new HistoricalTContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_historicalT);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170); match(Path);
			setState(172); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(171); historicalSeq();
				}
				}
				setState(174); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ClassSep );
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
		enterRule(_localctx, 18, RULE_methSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(176); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(177); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(178); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(184);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(181); x();
				}
				}
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(187); match(CRound);
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
		enterRule(_localctx, 20, RULE_x);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189); match(X);
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
		enterRule(_localctx, 22, RULE_xOp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191); match(X);
			setState(192); match(EqOp);
			setState(193); eTop();
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
		enterRule(_localctx, 24, RULE_eTopForMethod);
		int _la;
		try {
			setState(210);
			switch (_input.LA(1)) {
			case S:
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
			case UnOp:
			case DataOp:
			case NumParse:
				enterOuterAlt(_localctx, 1);
				{
				setState(195); eTop();
				}
				break;
			case ORoundNoSpace:
				enterOuterAlt(_localctx, 2);
				{
				setState(196); roundBlockForMethod();
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ORoundNoSpace) | (1L << OSquare) | (1L << Dot) | (1L << StringQuote) | (1L << Doc))) != 0)) {
					{
					setState(205);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(197); squareW();
						}
						break;

					case 2:
						{
						setState(198); square();
						}
						break;

					case 3:
						{
						setState(199); match(Dot);
						setState(200); mCall();
						}
						break;

					case 4:
						{
						setState(201); match(ORoundNoSpace);
						setState(202); round();
						}
						break;

					case 5:
						{
						setState(203); docs();
						}
						break;

					case 6:
						{
						setState(204); stringParse();
						}
						break;
					}
					}
					setState(209);
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
		enterRule(_localctx, 26, RULE_eTop);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(212); eL3();
			setState(217);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(213); match(BoolOp);
					setState(214); eL3();
					}
					} 
				}
				setState(219);
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

	public static class EL3Context extends ParserRuleContext {
		public List<TerminalNode> RelOp() { return getTokens(L42Parser.RelOp); }
		public EL2Context eL2(int i) {
			return getRuleContext(EL2Context.class,i);
		}
		public List<EL2Context> eL2() {
			return getRuleContexts(EL2Context.class);
		}
		public TerminalNode RelOp(int i) {
			return getToken(L42Parser.RelOp, i);
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
		enterRule(_localctx, 28, RULE_eL3);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(220); eL2();
			setState(225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(221); match(RelOp);
					setState(222); eL2();
					}
					} 
				}
				setState(227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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
		enterRule(_localctx, 30, RULE_eL2);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(228); eL1();
			setState(233);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(229); match(DataOp);
					setState(230); eL1();
					}
					} 
				}
				setState(235);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
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
		enterRule(_localctx, 32, RULE_eL1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236); eUnOp();
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
		public TerminalNode DataOp() { return getToken(L42Parser.DataOp, 0); }
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
		enterRule(_localctx, 34, RULE_numParse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			_la = _input.LA(1);
			if (_la==DataOp) {
				{
				setState(238); match(DataOp);
				}
			}

			setState(241); match(NumParse);
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
		enterRule(_localctx, 36, RULE_eUnOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_la = _input.LA(1);
			if (_la==UnOp) {
				{
				setState(243); match(UnOp);
				}
			}

			setState(246); ePost();
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
		enterRule(_localctx, 38, RULE_ePost);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(248); eAtom();
			setState(259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(257);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						setState(249); squareW();
						}
						break;

					case 2:
						{
						setState(250); square();
						}
						break;

					case 3:
						{
						setState(251); match(Dot);
						setState(252); mCall();
						}
						break;

					case 4:
						{
						setState(253); match(ORoundNoSpace);
						setState(254); round();
						}
						break;

					case 5:
						{
						setState(255); docs();
						}
						break;

					case 6:
						{
						setState(256); stringParse();
						}
						break;
					}
					} 
				}
				setState(261);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		enterRule(_localctx, 40, RULE_eAtom);
		int _la;
		try {
			setState(287);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(262); classBReuse();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(263); classB();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(265);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(264); numParse();
					}
				}

				setState(267); x();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(268); xOp();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(270);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(269); numParse();
					}
				}

				setState(272); match(Path);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(274);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(273); numParse();
					}
				}

				setState(276); block();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(277); ifExpr();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(278); whileExpr();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(279); signalExpr();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(280); loopExpr();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(281); match(WalkBy);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(282); w();
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(283); using();
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(284); match(DotDotDot);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(285); mxRound();
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(286); useSquare();
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
		enterRule(_localctx, 42, RULE_mxRound);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289); match(MX);
			setState(290); round();
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
			setState(296);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(292); match(Using);
				setState(293); square();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(294); match(Using);
				setState(295); squareW();
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
			setState(298); match(If);
			setState(299); eTop();
			setState(300); block();
			setState(303);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(301); match(Else);
				setState(302); eTop();
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
			setState(305); match(Using);
			setState(306); match(Path);
			setState(307); match(Check);
			setState(308); mCall();
			setState(309); eTop();
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
			setState(311); match(While);
			setState(312); eTop();
			setState(313); block();
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
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
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
			setState(315); match(S);
			setState(316); eTop();
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
			setState(318); match(Loop);
			setState(319); eTop();
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
			setState(323);
			switch (_input.LA(1)) {
			case ORoundSpace:
				enterOuterAlt(_localctx, 1);
				{
				setState(321); roundBlock();
				}
				break;
			case OCurly:
				enterOuterAlt(_localctx, 2);
				{
				setState(322); curlyBlock();
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
			setState(325); match(ORoundNoSpace);
			setState(326); docsOpt();
			setState(330);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(327); bb();
					}
					} 
				}
				setState(332);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(333); eTop();
			setState(334); match(CRound);
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
			setState(336); match(ORoundSpace);
			setState(337); docsOpt();
			setState(341);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(338); bb();
					}
					} 
				}
				setState(343);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(344); eTop();
			setState(345); match(CRound);
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
		public KContext k() {
			return getRuleContext(KContext.class,0);
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
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(348); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(347); d();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(350); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(353);
			_la = _input.LA(1);
			if (_la==Catch) {
				{
				setState(352); k();
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
			setState(355); match(OCurly);
			setState(356); docsOpt();
			setState(358); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(357); bb();
				}
				}
				setState(360); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << S) | (1L << Mdf) | (1L << ORoundSpace) | (1L << OCurly) | (1L << DotDotDot) | (1L << Ph) | (1L << If) | (1L << While) | (1L << Loop) | (1L << With) | (1L << Var) | (1L << Using) | (1L << WalkBy) | (1L << Path) | (1L << MX) | (1L << X) | (1L << UnOp) | (1L << DataOp) | (1L << NumParse))) != 0) );
			setState(362); match(CCurly);
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
			setState(365);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(364); match(Var);
				}
			}

			setState(368);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(367); t();
				}
			}

			setState(370); x();
			setState(371); match(Equal);
			setState(372); eTop();
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
			setState(377);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(374); varDec();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(375); eTop();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(376); nestedClass();
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
			setState(379); match(StringQuote);
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
			setState(381); match(OSquare);
			setState(382); docsOpt();
			setState(389);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(383); ps();
					setState(384); match(Semicolon);
					setState(385); docsOpt();
					}
					} 
				}
				setState(391);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(392); ps();
			setState(393); match(CSquare);
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
			setState(395); match(OSquare);
			setState(396); docsOpt();
			setState(397); w();
			setState(398); match(CSquare);
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
			setState(400); m();
			setState(401); round();
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
			setState(403); docsOpt();
			setState(404); ps();
			setState(405); match(CRound);
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
			setState(408);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(407); eTop();
				}
				break;
			}
			setState(415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(410); match(X);
				setState(411); match(Colon);
				setState(412); eTop();
				}
				}
				setState(417);
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

	public static class KContext extends ParserRuleContext {
		public TerminalNode Catch() { return getToken(L42Parser.Catch, 0); }
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public List<OnContext> on() {
			return getRuleContexts(OnContext.class);
		}
		public TerminalNode X() { return getToken(L42Parser.X, 0); }
		public TerminalNode Default() { return getToken(L42Parser.Default, 0); }
		public ETopContext eTop() {
			return getRuleContext(ETopContext.class,0);
		}
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public OnContext on(int i) {
			return getRuleContext(OnContext.class,i);
		}
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
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
		enterRule(_localctx, 82, RULE_k);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418); match(Catch);
			setState(419); match(S);
			setState(421);
			_la = _input.LA(1);
			if (_la==X) {
				{
				setState(420); match(X);
				}
			}

			setState(423);
			_la = _input.LA(1);
			if ( !(_la==ORoundNoSpace || _la==ORoundSpace) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==On) {
				{
				{
				setState(424); on();
				}
				}
				setState(429);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(432);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(430); match(Default);
				setState(431); eTop();
				}
			}

			setState(434); match(CRound);
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
		enterRule(_localctx, 84, RULE_on);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436); match(On);
			setState(437); t();
			setState(440);
			_la = _input.LA(1);
			if (_la==Case) {
				{
				setState(438); match(Case);
				setState(439); eTop();
				}
			}

			setState(442); eTop();
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
		enterRule(_localctx, 86, RULE_onPlus);
		int _la;
		try {
			int _alt;
			setState(460);
			switch (_input.LA(1)) {
			case On:
				enterOuterAlt(_localctx, 1);
				{
				setState(444); match(On);
				setState(446); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(445); t();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(448); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
				setState(452);
				_la = _input.LA(1);
				if (_la==Case) {
					{
					setState(450); match(Case);
					setState(451); eTop();
					}
				}

				setState(454); eTop();
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(456); match(Case);
				setState(457); eTop();
				setState(458); eTop();
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
		enterRule(_localctx, 88, RULE_nudeE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(462); eTop();
			setState(463); match(EOF);
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
		enterRule(_localctx, 90, RULE_classBExtra);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			_la = _input.LA(1);
			if (_la==Stage) {
				{
				setState(465); match(Stage);
				}
			}

			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Path) {
				{
				{
				setState(468); match(Path);
				}
				}
				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(474); match(EndType);
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
		enterRule(_localctx, 92, RULE_classBReuse);
		int _la;
		try {
			setState(492);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(476); match(OCurly);
				setState(477); docsOpt();
				setState(478); match(Url);
				setState(479); match(CCurly);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(481); match(OCurly);
				setState(482); docsOpt();
				setState(483); match(UrlNL);
				setState(484); docsOpt();
				setState(486); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(485); member();
					}
					}
					setState(488); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0) );
				setState(490); match(CCurly);
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
		public TerminalNode Implements() { return getToken(L42Parser.Implements, 0); }
		public TerminalNode OCurly() { return getToken(L42Parser.OCurly, 0); }
		public List<TerminalNode> Path() { return getTokens(L42Parser.Path); }
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
		public TerminalNode Path(int i) {
			return getToken(L42Parser.Path, i);
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
		enterRule(_localctx, 94, RULE_classB);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(494); match(OCurly);
			setState(495); docsOpt();
			setState(496); header();
			setState(498);
			_la = _input.LA(1);
			if (_la==Implements) {
				{
				setState(497); match(Implements);
				}
			}

			setState(503);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(500); match(Path);
					}
					} 
				}
				setState(505);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			}
			setState(506); docsOpt();
			setState(510);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0)) {
				{
				{
				setState(507); member();
				}
				}
				setState(512);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(513); match(CCurly);
			setState(515);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(514); classBExtra();
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

	public static class MhsContext extends ParserRuleContext {
		public TerminalNode Method() { return getToken(L42Parser.Method, 0); }
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode EndType() { return getToken(L42Parser.EndType, 0); }
		public MhtContext mht() {
			return getRuleContext(MhtContext.class,0);
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
		enterRule(_localctx, 96, RULE_mhs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517); match(Method);
			setState(518); docsOpt();
			setState(519); methSelector();
			setState(523);
			_la = _input.LA(1);
			if (_la==EndType) {
				{
				setState(520); match(EndType);
				setState(521); match(Path);
				setState(522); mht();
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

	public static class MhtContext extends ParserRuleContext {
		public List<XContext> x() {
			return getRuleContexts(XContext.class);
		}
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public List<TerminalNode> Path() { return getTokens(L42Parser.Path); }
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
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public TerminalNode Path(int i) {
			return getToken(L42Parser.Path, i);
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
		enterRule(_localctx, 98, RULE_mht);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(525); match(Mdf);
				}
			}

			setState(528); match(Method);
			setState(529); docsOpt();
			setState(530); t();
			setState(534);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(531); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(532); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(533); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(542);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				{
				setState(536); t();
				setState(537); x();
				setState(538); docsOpt();
				}
				}
				setState(544);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(545); match(CRound);
			setState(552);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(546); match(S);
				setState(548); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(547); match(Path);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(550); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
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
		enterRule(_localctx, 100, RULE_member);
		try {
			setState(557);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(554); methodWithType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(555); methodImplemented();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(556); nestedClass();
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
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
		public TerminalNode EndType() { return getToken(L42Parser.EndType, 0); }
		public MhtContext mht() {
			return getRuleContext(MhtContext.class,0);
		}
		public TerminalNode FieldSpecial() { return getToken(L42Parser.FieldSpecial, 0); }
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
		enterRule(_localctx, 102, RULE_methodWithType);
		int _la;
		try {
			setState(571);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(559); mht();
				setState(562);
				_la = _input.LA(1);
				if (_la==EndType) {
					{
					setState(560); match(EndType);
					setState(561); match(Path);
					}
				}

				setState(564); docsOpt();
				setState(566);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(565); eTopForMethod();
					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(568); mht();
				setState(569); match(FieldSpecial);
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
		enterRule(_localctx, 104, RULE_methodImplemented);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573); mhs();
			setState(574); eTopForMethod();
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
		enterRule(_localctx, 106, RULE_nestedClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(576); match(Path);
			setState(577); match(Colon);
			setState(578); docsOpt();
			setState(579); eTop();
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
		public FieldDecContext fieldDec(int i) {
			return getRuleContext(FieldDecContext.class,i);
		}
		public MDecContext mDec() {
			return getRuleContext(MDecContext.class,0);
		}
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
		public TerminalNode ORoundSpace() { return getToken(L42Parser.ORoundSpace, 0); }
		public List<FieldDecContext> fieldDec() {
			return getRuleContexts(FieldDecContext.class);
		}
		public TerminalNode CRound() { return getToken(L42Parser.CRound, 0); }
		public TerminalNode Mdf() { return getToken(L42Parser.Mdf, 0); }
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
		enterRule(_localctx, 108, RULE_header);
		int _la;
		try {
			setState(598);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(582); match(Interface);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(584);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(583); match(Mdf);
					}
				}

				setState(589);
				switch (_input.LA(1)) {
				case MX:
				case HashX:
				case UnOp:
				case EqOp:
				case BoolOp:
				case RelOp:
				case DataOp:
					{
					setState(586); mDec();
					}
					break;
				case ORoundSpace:
					{
					setState(587); match(ORoundSpace);
					}
					break;
				case ORoundNoSpace:
					{
					setState(588); match(ORoundNoSpace);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(594);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path))) != 0)) {
					{
					{
					setState(591); fieldDec();
					}
					}
					setState(596);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(597); match(CRound);
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
		enterRule(_localctx, 110, RULE_fieldDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(601);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(600); match(Var);
				}
			}

			setState(603); t();
			setState(604); x();
			setState(605); docsOpt();
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
		enterRule(_localctx, 112, RULE_w);
		try {
			setState(609);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(607); wSwitch();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(608); wSimple();
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
		enterRule(_localctx, 114, RULE_wSwitch);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(611); match(With);
			setState(615);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(612); x();
					}
					} 
				}
				setState(617);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			}
			setState(621);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(618); i();
					}
					} 
				}
				setState(623);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			}
			setState(627);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0)) {
				{
				{
				setState(624); varDec();
				}
				}
				setState(629);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(630); match(ORoundSpace);
			setState(632); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(631); onPlus();
				}
				}
				setState(634); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Case || _la==On );
			setState(638);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(636); match(Default);
				setState(637); eTop();
				}
			}

			setState(640); match(CRound);
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
		enterRule(_localctx, 116, RULE_i);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(642); match(Var);
				}
			}

			setState(646);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(645); t();
				}
			}

			setState(648); x();
			setState(649); match(In);
			setState(650); eTop();
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
		enterRule(_localctx, 118, RULE_wSimple);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(652); match(With);
			setState(654); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(653); i();
				}
				}
				setState(656); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0) );
			setState(658); block();
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\65\u0297\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\5\3\u0092\n\3\3\4\3\4\3\5\3\5\3\6\5\6\u0099\n\6"+
		"\3\7\3\7\5\7\u009d\n\7\3\b\5\b\u00a0\n\b\3\b\5\b\u00a3\n\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\5\t\u00ab\n\t\3\n\3\n\6\n\u00af\n\n\r\n\16\n\u00b0\3\13\3"+
		"\13\3\13\5\13\u00b6\n\13\3\13\7\13\u00b9\n\13\f\13\16\13\u00bc\13\13\3"+
		"\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\7\16\u00d0\n\16\f\16\16\16\u00d3\13\16\5\16\u00d5\n\16\3"+
		"\17\3\17\3\17\7\17\u00da\n\17\f\17\16\17\u00dd\13\17\3\20\3\20\3\20\7"+
		"\20\u00e2\n\20\f\20\16\20\u00e5\13\20\3\21\3\21\3\21\7\21\u00ea\n\21\f"+
		"\21\16\21\u00ed\13\21\3\22\3\22\3\23\5\23\u00f2\n\23\3\23\3\23\3\24\5"+
		"\24\u00f7\n\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\7\25\u0104\n\25\f\25\16\25\u0107\13\25\3\26\3\26\3\26\5\26\u010c\n\26"+
		"\3\26\3\26\3\26\5\26\u0111\n\26\3\26\3\26\5\26\u0115\n\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u0122\n\26\3\27\3\27"+
		"\3\27\3\30\3\30\3\30\3\30\5\30\u012b\n\30\3\31\3\31\3\31\3\31\3\31\5\31"+
		"\u0132\n\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34"+
		"\3\34\3\35\3\35\3\35\3\36\3\36\5\36\u0146\n\36\3\37\3\37\3\37\7\37\u014b"+
		"\n\37\f\37\16\37\u014e\13\37\3\37\3\37\3\37\3 \3 \3 \7 \u0156\n \f \16"+
		" \u0159\13 \3 \3 \3 \3!\6!\u015f\n!\r!\16!\u0160\3!\5!\u0164\n!\3\"\3"+
		"\"\3\"\6\"\u0169\n\"\r\"\16\"\u016a\3\"\3\"\3#\5#\u0170\n#\3#\5#\u0173"+
		"\n#\3#\3#\3#\3#\3$\3$\3$\5$\u017c\n$\3%\3%\3&\3&\3&\3&\3&\3&\7&\u0186"+
		"\n&\f&\16&\u0189\13&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3"+
		")\3*\5*\u019b\n*\3*\3*\3*\7*\u01a0\n*\f*\16*\u01a3\13*\3+\3+\3+\5+\u01a8"+
		"\n+\3+\3+\7+\u01ac\n+\f+\16+\u01af\13+\3+\3+\5+\u01b3\n+\3+\3+\3,\3,\3"+
		",\3,\5,\u01bb\n,\3,\3,\3-\3-\6-\u01c1\n-\r-\16-\u01c2\3-\3-\5-\u01c7\n"+
		"-\3-\3-\3-\3-\3-\3-\5-\u01cf\n-\3.\3.\3.\3/\5/\u01d5\n/\3/\7/\u01d8\n"+
		"/\f/\16/\u01db\13/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\6\60\u01e9\n\60\r\60\16\60\u01ea\3\60\3\60\5\60\u01ef\n\60\3\61"+
		"\3\61\3\61\3\61\5\61\u01f5\n\61\3\61\7\61\u01f8\n\61\f\61\16\61\u01fb"+
		"\13\61\3\61\3\61\7\61\u01ff\n\61\f\61\16\61\u0202\13\61\3\61\3\61\5\61"+
		"\u0206\n\61\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u020e\n\62\3\63\5\63\u0211"+
		"\n\63\3\63\3\63\3\63\3\63\3\63\3\63\5\63\u0219\n\63\3\63\3\63\3\63\3\63"+
		"\7\63\u021f\n\63\f\63\16\63\u0222\13\63\3\63\3\63\3\63\6\63\u0227\n\63"+
		"\r\63\16\63\u0228\5\63\u022b\n\63\3\64\3\64\3\64\5\64\u0230\n\64\3\65"+
		"\3\65\3\65\5\65\u0235\n\65\3\65\3\65\5\65\u0239\n\65\3\65\3\65\3\65\5"+
		"\65\u023e\n\65\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67\38\38\38\58\u024b"+
		"\n8\38\38\38\58\u0250\n8\38\78\u0253\n8\f8\168\u0256\138\38\58\u0259\n"+
		"8\39\59\u025c\n9\39\39\39\39\3:\3:\5:\u0264\n:\3;\3;\7;\u0268\n;\f;\16"+
		";\u026b\13;\3;\7;\u026e\n;\f;\16;\u0271\13;\3;\7;\u0274\n;\f;\16;\u0277"+
		"\13;\3;\3;\6;\u027b\n;\r;\16;\u027c\3;\3;\5;\u0281\n;\3;\3;\3<\5<\u0286"+
		"\n<\3<\5<\u0289\n<\3<\3<\3<\3<\3=\3=\6=\u0291\n=\r=\16=\u0292\3=\3=\3"+
		"=\2\2>\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<"+
		">@BDFHJLNPRTVXZ\\^`bdfhjlnprtvx\2\4\4\2((**\3\2\5\6\u02cb\2z\3\2\2\2\4"+
		"\u0091\3\2\2\2\6\u0093\3\2\2\2\b\u0095\3\2\2\2\n\u0098\3\2\2\2\f\u009c"+
		"\3\2\2\2\16\u009f\3\2\2\2\20\u00a6\3\2\2\2\22\u00ac\3\2\2\2\24\u00b5\3"+
		"\2\2\2\26\u00bf\3\2\2\2\30\u00c1\3\2\2\2\32\u00d4\3\2\2\2\34\u00d6\3\2"+
		"\2\2\36\u00de\3\2\2\2 \u00e6\3\2\2\2\"\u00ee\3\2\2\2$\u00f1\3\2\2\2&\u00f6"+
		"\3\2\2\2(\u00fa\3\2\2\2*\u0121\3\2\2\2,\u0123\3\2\2\2.\u012a\3\2\2\2\60"+
		"\u012c\3\2\2\2\62\u0133\3\2\2\2\64\u0139\3\2\2\2\66\u013d\3\2\2\28\u0140"+
		"\3\2\2\2:\u0145\3\2\2\2<\u0147\3\2\2\2>\u0152\3\2\2\2@\u015e\3\2\2\2B"+
		"\u0165\3\2\2\2D\u016f\3\2\2\2F\u017b\3\2\2\2H\u017d\3\2\2\2J\u017f\3\2"+
		"\2\2L\u018d\3\2\2\2N\u0192\3\2\2\2P\u0195\3\2\2\2R\u019a\3\2\2\2T\u01a4"+
		"\3\2\2\2V\u01b6\3\2\2\2X\u01ce\3\2\2\2Z\u01d0\3\2\2\2\\\u01d4\3\2\2\2"+
		"^\u01ee\3\2\2\2`\u01f0\3\2\2\2b\u0207\3\2\2\2d\u0210\3\2\2\2f\u022f\3"+
		"\2\2\2h\u023d\3\2\2\2j\u023f\3\2\2\2l\u0242\3\2\2\2n\u0258\3\2\2\2p\u025b"+
		"\3\2\2\2r\u0263\3\2\2\2t\u0265\3\2\2\2v\u0285\3\2\2\2x\u028e\3\2\2\2z"+
		"{\t\2\2\2{\3\3\2\2\2|\u0092\5\2\2\2}~\7\60\2\2~\u0092\7\5\2\2\177\u0080"+
		"\7\61\2\2\u0080\u0092\7\5\2\2\u0081\u0082\7\62\2\2\u0082\u0092\7\5\2\2"+
		"\u0083\u0084\7\63\2\2\u0084\u0092\7\5\2\2\u0085\u0086\7\64\2\2\u0086\u0092"+
		"\7\5\2\2\u0087\u0088\7\60\2\2\u0088\u0092\7\6\2\2\u0089\u008a\7\61\2\2"+
		"\u008a\u0092\7\6\2\2\u008b\u008c\7\62\2\2\u008c\u0092\7\6\2\2\u008d\u008e"+
		"\7\63\2\2\u008e\u0092\7\6\2\2\u008f\u0090\7\64\2\2\u0090\u0092\7\6\2\2"+
		"\u0091|\3\2\2\2\u0091}\3\2\2\2\u0091\177\3\2\2\2\u0091\u0081\3\2\2\2\u0091"+
		"\u0083\3\2\2\2\u0091\u0085\3\2\2\2\u0091\u0087\3\2\2\2\u0091\u0089\3\2"+
		"\2\2\u0091\u008b\3\2\2\2\u0091\u008d\3\2\2\2\u0091\u008f\3\2\2\2\u0092"+
		"\5\3\2\2\2\u0093\u0094\7&\2\2\u0094\7\3\2\2\2\u0095\u0096\7.\2\2\u0096"+
		"\t\3\2\2\2\u0097\u0099\7.\2\2\u0098\u0097\3\2\2\2\u0098\u0099\3\2\2\2"+
		"\u0099\13\3\2\2\2\u009a\u009d\5\16\b\2\u009b\u009d\5\22\n\2\u009c\u009a"+
		"\3\2\2\2\u009c\u009b\3\2\2\2\u009d\r\3\2\2\2\u009e\u00a0\7\22\2\2\u009f"+
		"\u009e\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u00a3\7\4"+
		"\2\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a5\7&\2\2\u00a5\17\3\2\2\2\u00a6\u00a7\7\'\2\2\u00a7\u00aa\5\24\13"+
		"\2\u00a8\u00a9\7\'\2\2\u00a9\u00ab\5\26\f\2\u00aa\u00a8\3\2\2\2\u00aa"+
		"\u00ab\3\2\2\2\u00ab\21\3\2\2\2\u00ac\u00ae\7&\2\2\u00ad\u00af\5\20\t"+
		"\2\u00ae\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1"+
		"\3\2\2\2\u00b1\23\3\2\2\2\u00b2\u00b6\5\4\3\2\u00b3\u00b6\7\5\2\2\u00b4"+
		"\u00b6\7\6\2\2\u00b5\u00b2\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b4\3\2"+
		"\2\2\u00b6\u00ba\3\2\2\2\u00b7\u00b9\5\26\f\2\u00b8\u00b7\3\2\2\2\u00b9"+
		"\u00bc\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bd\3\2"+
		"\2\2\u00bc\u00ba\3\2\2\2\u00bd\u00be\7\7\2\2\u00be\25\3\2\2\2\u00bf\u00c0"+
		"\7)\2\2\u00c0\27\3\2\2\2\u00c1\u00c2\7)\2\2\u00c2\u00c3\7\61\2\2\u00c3"+
		"\u00c4\5\34\17\2\u00c4\31\3\2\2\2\u00c5\u00d5\5\34\17\2\u00c6\u00d1\5"+
		"<\37\2\u00c7\u00d0\5L\'\2\u00c8\u00d0\5J&\2\u00c9\u00ca\7\20\2\2\u00ca"+
		"\u00d0\5N(\2\u00cb\u00cc\7\5\2\2\u00cc\u00d0\5P)\2\u00cd\u00d0\5\b\5\2"+
		"\u00ce\u00d0\5H%\2\u00cf\u00c7\3\2\2\2\u00cf\u00c8\3\2\2\2\u00cf\u00c9"+
		"\3\2\2\2\u00cf\u00cb\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00ce\3\2\2\2\u00d0"+
		"\u00d3\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d5\3\2"+
		"\2\2\u00d3\u00d1\3\2\2\2\u00d4\u00c5\3\2\2\2\u00d4\u00c6\3\2\2\2\u00d5"+
		"\33\3\2\2\2\u00d6\u00db\5\36\20\2\u00d7\u00d8\7\62\2\2\u00d8\u00da\5\36"+
		"\20\2\u00d9\u00d7\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db"+
		"\u00dc\3\2\2\2\u00dc\35\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00e3\5 \21"+
		"\2\u00df\u00e0\7\63\2\2\u00e0\u00e2\5 \21\2\u00e1\u00df\3\2\2\2\u00e2"+
		"\u00e5\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\37\3\2\2"+
		"\2\u00e5\u00e3\3\2\2\2\u00e6\u00eb\5\"\22\2\u00e7\u00e8\7\64\2\2\u00e8"+
		"\u00ea\5\"\22\2\u00e9\u00e7\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3"+
		"\2\2\2\u00eb\u00ec\3\2\2\2\u00ec!\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00ef"+
		"\5&\24\2\u00ef#\3\2\2\2\u00f0\u00f2\7\64\2\2\u00f1\u00f0\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4\7\65\2\2\u00f4%\3\2\2\2"+
		"\u00f5\u00f7\7\60\2\2\u00f6\u00f5\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8"+
		"\3\2\2\2\u00f8\u00f9\5(\25\2\u00f9\'\3\2\2\2\u00fa\u0105\5*\26\2\u00fb"+
		"\u0104\5L\'\2\u00fc\u0104\5J&\2\u00fd\u00fe\7\20\2\2\u00fe\u0104\5N(\2"+
		"\u00ff\u0100\7\5\2\2\u0100\u0104\5P)\2\u0101\u0104\5\b\5\2\u0102\u0104"+
		"\5H%\2\u0103\u00fb\3\2\2\2\u0103\u00fc\3\2\2\2\u0103\u00fd\3\2\2\2\u0103"+
		"\u00ff\3\2\2\2\u0103\u0101\3\2\2\2\u0103\u0102\3\2\2\2\u0104\u0107\3\2"+
		"\2\2\u0105\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106)\3\2\2\2\u0107\u0105"+
		"\3\2\2\2\u0108\u0122\5^\60\2\u0109\u0122\5`\61\2\u010a\u010c\5$\23\2\u010b"+
		"\u010a\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u0122\5\26"+
		"\f\2\u010e\u0122\5\30\r\2\u010f\u0111\5$\23\2\u0110\u010f\3\2\2\2\u0110"+
		"\u0111\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0122\7&\2\2\u0113\u0115\5$\23"+
		"\2\u0114\u0113\3\2\2\2\u0114\u0115\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0122"+
		"\5:\36\2\u0117\u0122\5\60\31\2\u0118\u0122\5\64\33\2\u0119\u0122\5\66"+
		"\34\2\u011a\u0122\58\35\2\u011b\u0122\7$\2\2\u011c\u0122\5r:\2\u011d\u0122"+
		"\5\62\32\2\u011e\u0122\7\13\2\2\u011f\u0122\5,\27\2\u0120\u0122\5.\30"+
		"\2\u0121\u0108\3\2\2\2\u0121\u0109\3\2\2\2\u0121\u010b\3\2\2\2\u0121\u010e"+
		"\3\2\2\2\u0121\u0110\3\2\2\2\u0121\u0114\3\2\2\2\u0121\u0117\3\2\2\2\u0121"+
		"\u0118\3\2\2\2\u0121\u0119\3\2\2\2\u0121\u011a\3\2\2\2\u0121\u011b\3\2"+
		"\2\2\u0121\u011c\3\2\2\2\u0121\u011d\3\2\2\2\u0121\u011e\3\2\2\2\u0121"+
		"\u011f\3\2\2\2\u0121\u0120\3\2\2\2\u0122+\3\2\2\2\u0123\u0124\7(\2\2\u0124"+
		"\u0125\5P)\2\u0125-\3\2\2\2\u0126\u0127\7!\2\2\u0127\u012b\5J&\2\u0128"+
		"\u0129\7!\2\2\u0129\u012b\5L\'\2\u012a\u0126\3\2\2\2\u012a\u0128\3\2\2"+
		"\2\u012b/\3\2\2\2\u012c\u012d\7\25\2\2\u012d\u012e\5\34\17\2\u012e\u0131"+
		"\5:\36\2\u012f\u0130\7\26\2\2\u0130\u0132\5\34\17\2\u0131\u012f\3\2\2"+
		"\2\u0131\u0132\3\2\2\2\u0132\61\3\2\2\2\u0133\u0134\7!\2\2\u0134\u0135"+
		"\7&\2\2\u0135\u0136\7\"\2\2\u0136\u0137\5N(\2\u0137\u0138\5\34\17\2\u0138"+
		"\63\3\2\2\2\u0139\u013a\7\27\2\2\u013a\u013b\5\34\17\2\u013b\u013c\5:"+
		"\36\2\u013c\65\3\2\2\2\u013d\u013e\7\3\2\2\u013e\u013f\5\34\17\2\u013f"+
		"\67\3\2\2\2\u0140\u0141\7\30\2\2\u0141\u0142\5\34\17\2\u01429\3\2\2\2"+
		"\u0143\u0146\5> \2\u0144\u0146\5B\"\2\u0145\u0143\3\2\2\2\u0145\u0144"+
		"\3\2\2\2\u0146;\3\2\2\2\u0147\u0148\7\5\2\2\u0148\u014c\5\n\6\2\u0149"+
		"\u014b\5@!\2\u014a\u0149\3\2\2\2\u014b\u014e\3\2\2\2\u014c\u014a\3\2\2"+
		"\2\u014c\u014d\3\2\2\2\u014d\u014f\3\2\2\2\u014e\u014c\3\2\2\2\u014f\u0150"+
		"\5\34\17\2\u0150\u0151\7\7\2\2\u0151=\3\2\2\2\u0152\u0153\7\6\2\2\u0153"+
		"\u0157\5\n\6\2\u0154\u0156\5@!\2\u0155\u0154\3\2\2\2\u0156\u0159\3\2\2"+
		"\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015a\3\2\2\2\u0159\u0157"+
		"\3\2\2\2\u015a\u015b\5\34\17\2\u015b\u015c\7\7\2\2\u015c?\3\2\2\2\u015d"+
		"\u015f\5F$\2\u015e\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u015e\3\2\2"+
		"\2\u0160\u0161\3\2\2\2\u0161\u0163\3\2\2\2\u0162\u0164\5T+\2\u0163\u0162"+
		"\3\2\2\2\u0163\u0164\3\2\2\2\u0164A\3\2\2\2\u0165\u0166\7\n\2\2\u0166"+
		"\u0168\5\n\6\2\u0167\u0169\5@!\2\u0168\u0167\3\2\2\2\u0169\u016a\3\2\2"+
		"\2\u016a\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016d"+
		"\7\r\2\2\u016dC\3\2\2\2\u016e\u0170\7\35\2\2\u016f\u016e\3\2\2\2\u016f"+
		"\u0170\3\2\2\2\u0170\u0172\3\2\2\2\u0171\u0173\5\f\7\2\u0172\u0171\3\2"+
		"\2\2\u0172\u0173\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0175\5\26\f\2\u0175"+
		"\u0176\7\21\2\2\u0176\u0177\5\34\17\2\u0177E\3\2\2\2\u0178\u017c\5D#\2"+
		"\u0179\u017c\5\34\17\2\u017a\u017c\5l\67\2\u017b\u0178\3\2\2\2\u017b\u0179"+
		"\3\2\2\2\u017b\u017a\3\2\2\2\u017cG\3\2\2\2\u017d\u017e\7+\2\2\u017eI"+
		"\3\2\2\2\u017f\u0180\7\b\2\2\u0180\u0187\5\n\6\2\u0181\u0182\5R*\2\u0182"+
		"\u0183\7\17\2\2\u0183\u0184\5\n\6\2\u0184\u0186\3\2\2\2\u0185\u0181\3"+
		"\2\2\2\u0186\u0189\3\2\2\2\u0187\u0185\3\2\2\2\u0187\u0188\3\2\2\2\u0188"+
		"\u018a\3\2\2\2\u0189\u0187\3\2\2\2\u018a\u018b\5R*\2\u018b\u018c\7\t\2"+
		"\2\u018cK\3\2\2\2\u018d\u018e\7\b\2\2\u018e\u018f\5\n\6\2\u018f\u0190"+
		"\5r:\2\u0190\u0191\7\t\2\2\u0191M\3\2\2\2\u0192\u0193\5\2\2\2\u0193\u0194"+
		"\5P)\2\u0194O\3\2\2\2\u0195\u0196\5\n\6\2\u0196\u0197\5R*\2\u0197\u0198"+
		"\7\7\2\2\u0198Q\3\2\2\2\u0199\u019b\5\34\17\2\u019a\u0199\3\2\2\2\u019a"+
		"\u019b\3\2\2\2\u019b\u01a1\3\2\2\2\u019c\u019d\7)\2\2\u019d\u019e\7\16"+
		"\2\2\u019e\u01a0\5\34\17\2\u019f\u019c\3\2\2\2\u01a0\u01a3\3\2\2\2\u01a1"+
		"\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2S\3\2\2\2\u01a3\u01a1\3\2\2\2"+
		"\u01a4\u01a5\7\34\2\2\u01a5\u01a7\7\3\2\2\u01a6\u01a8\7)\2\2\u01a7\u01a6"+
		"\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01ad\t\3\2\2\u01aa"+
		"\u01ac\5V,\2\u01ab\u01aa\3\2\2\2\u01ac\u01af\3\2\2\2\u01ad\u01ab\3\2\2"+
		"\2\u01ad\u01ae\3\2\2\2\u01ae\u01b2\3\2\2\2\u01af\u01ad\3\2\2\2\u01b0\u01b1"+
		"\7\36\2\2\u01b1\u01b3\5\34\17\2\u01b2\u01b0\3\2\2\2\u01b2\u01b3\3\2\2"+
		"\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\7\7\2\2\u01b5U\3\2\2\2\u01b6\u01b7"+
		"\7\32\2\2\u01b7\u01ba\5\f\7\2\u01b8\u01b9\7\24\2\2\u01b9\u01bb\5\34\17"+
		"\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd"+
		"\5\34\17\2\u01bdW\3\2\2\2\u01be\u01c0\7\32\2\2\u01bf\u01c1\5\f\7\2\u01c0"+
		"\u01bf\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c2\u01c3\3\2"+
		"\2\2\u01c3\u01c6\3\2\2\2\u01c4\u01c5\7\24\2\2\u01c5\u01c7\5\34\17\2\u01c6"+
		"\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01c9\5\34"+
		"\17\2\u01c9\u01cf\3\2\2\2\u01ca\u01cb\7\24\2\2\u01cb\u01cc\5\34\17\2\u01cc"+
		"\u01cd\5\34\17\2\u01cd\u01cf\3\2\2\2\u01ce\u01be\3\2\2\2\u01ce\u01ca\3"+
		"\2\2\2\u01cfY\3\2\2\2\u01d0\u01d1\5\34\17\2\u01d1\u01d2\7\2\2\3\u01d2"+
		"[\3\2\2\2\u01d3\u01d5\7%\2\2\u01d4\u01d3\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5"+
		"\u01d9\3\2\2\2\u01d6\u01d8\7&\2\2\u01d7\u01d6\3\2\2\2\u01d8\u01db\3\2"+
		"\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01dc\3\2\2\2\u01db"+
		"\u01d9\3\2\2\2\u01dc\u01dd\7\f\2\2\u01dd]\3\2\2\2\u01de\u01df\7\n\2\2"+
		"\u01df\u01e0\5\n\6\2\u01e0\u01e1\7-\2\2\u01e1\u01e2\7\r\2\2\u01e2\u01ef"+
		"\3\2\2\2\u01e3\u01e4\7\n\2\2\u01e4\u01e5\5\n\6\2\u01e5\u01e6\7,\2\2\u01e6"+
		"\u01e8\5\n\6\2\u01e7\u01e9\5f\64\2\u01e8\u01e7\3\2\2\2\u01e9\u01ea\3\2"+
		"\2\2\u01ea\u01e8\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec"+
		"\u01ed\7\r\2\2\u01ed\u01ef\3\2\2\2\u01ee\u01de\3\2\2\2\u01ee\u01e3\3\2"+
		"\2\2\u01ef_\3\2\2\2\u01f0\u01f1\7\n\2\2\u01f1\u01f2\5\n\6\2\u01f2\u01f4"+
		"\5n8\2\u01f3\u01f5\7\23\2\2\u01f4\u01f3\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5"+
		"\u01f9\3\2\2\2\u01f6\u01f8\7&\2\2\u01f7\u01f6\3\2\2\2\u01f8\u01fb\3\2"+
		"\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa\u01fc\3\2\2\2\u01fb"+
		"\u01f9\3\2\2\2\u01fc\u0200\5\n\6\2\u01fd\u01ff\5f\64\2\u01fe\u01fd\3\2"+
		"\2\2\u01ff\u0202\3\2\2\2\u0200\u01fe\3\2\2\2\u0200\u0201\3\2\2\2\u0201"+
		"\u0203\3\2\2\2\u0202\u0200\3\2\2\2\u0203\u0205\7\r\2\2\u0204\u0206\5\\"+
		"/\2\u0205\u0204\3\2\2\2\u0205\u0206\3\2\2\2\u0206a\3\2\2\2\u0207\u0208"+
		"\7 \2\2\u0208\u0209\5\n\6\2\u0209\u020d\5\24\13\2\u020a\u020b\7\f\2\2"+
		"\u020b\u020c\7&\2\2\u020c\u020e\5d\63\2\u020d\u020a\3\2\2\2\u020d\u020e"+
		"\3\2\2\2\u020ec\3\2\2\2\u020f\u0211\7\4\2\2\u0210\u020f\3\2\2\2\u0210"+
		"\u0211\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\7 \2\2\u0213\u0214\5\n"+
		"\6\2\u0214\u0218\5\f\7\2\u0215\u0219\5\4\3\2\u0216\u0219\7\5\2\2\u0217"+
		"\u0219\7\6\2\2\u0218\u0215\3\2\2\2\u0218\u0216\3\2\2\2\u0218\u0217\3\2"+
		"\2\2\u0219\u0220\3\2\2\2\u021a\u021b\5\f\7\2\u021b\u021c\5\26\f\2\u021c"+
		"\u021d\5\n\6\2\u021d\u021f\3\2\2\2\u021e\u021a\3\2\2\2\u021f\u0222\3\2"+
		"\2\2\u0220\u021e\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0223\3\2\2\2\u0222"+
		"\u0220\3\2\2\2\u0223\u022a\7\7\2\2\u0224\u0226\7\3\2\2\u0225\u0227\7&"+
		"\2\2\u0226\u0225\3\2\2\2\u0227\u0228\3\2\2\2\u0228\u0226\3\2\2\2\u0228"+
		"\u0229\3\2\2\2\u0229\u022b\3\2\2\2\u022a\u0224\3\2\2\2\u022a\u022b\3\2"+
		"\2\2\u022be\3\2\2\2\u022c\u0230\5h\65\2\u022d\u0230\5j\66\2\u022e\u0230"+
		"\5l\67\2\u022f\u022c\3\2\2\2\u022f\u022d\3\2\2\2\u022f\u022e\3\2\2\2\u0230"+
		"g\3\2\2\2\u0231\u0234\5d\63\2\u0232\u0233\7\f\2\2\u0233\u0235\7&\2\2\u0234"+
		"\u0232\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0236\3\2\2\2\u0236\u0238\5\n"+
		"\6\2\u0237\u0239\5\32\16\2\u0238\u0237\3\2\2\2\u0238\u0239\3\2\2\2\u0239"+
		"\u023e\3\2\2\2\u023a\u023b\5d\63\2\u023b\u023c\7#\2\2\u023c\u023e\3\2"+
		"\2\2\u023d\u0231\3\2\2\2\u023d\u023a\3\2\2\2\u023ei\3\2\2\2\u023f\u0240"+
		"\5b\62\2\u0240\u0241\5\32\16\2\u0241k\3\2\2\2\u0242\u0243\7&\2\2\u0243"+
		"\u0244\7\16\2\2\u0244\u0245\5\n\6\2\u0245\u0246\5\34\17\2\u0246m\3\2\2"+
		"\2\u0247\u0259\3\2\2\2\u0248\u0259\7\37\2\2\u0249\u024b\7\4\2\2\u024a"+
		"\u0249\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024f\3\2\2\2\u024c\u0250\5\4"+
		"\3\2\u024d\u0250\7\6\2\2\u024e\u0250\7\5\2\2\u024f\u024c\3\2\2\2\u024f"+
		"\u024d\3\2\2\2\u024f\u024e\3\2\2\2\u0250\u0254\3\2\2\2\u0251\u0253\5p"+
		"9\2\u0252\u0251\3\2\2\2\u0253\u0256\3\2\2\2\u0254\u0252\3\2\2\2\u0254"+
		"\u0255\3\2\2\2\u0255\u0257\3\2\2\2\u0256\u0254\3\2\2\2\u0257\u0259\7\7"+
		"\2\2\u0258\u0247\3\2\2\2\u0258\u0248\3\2\2\2\u0258\u024a\3\2\2\2\u0259"+
		"o\3\2\2\2\u025a\u025c\7\35\2\2\u025b\u025a\3\2\2\2\u025b\u025c\3\2\2\2"+
		"\u025c\u025d\3\2\2\2\u025d\u025e\5\f\7\2\u025e\u025f\5\26\f\2\u025f\u0260"+
		"\5\n\6\2\u0260q\3\2\2\2\u0261\u0264\5t;\2\u0262\u0264\5x=\2\u0263\u0261"+
		"\3\2\2\2\u0263\u0262\3\2\2\2\u0264s\3\2\2\2\u0265\u0269\7\31\2\2\u0266"+
		"\u0268\5\26\f\2\u0267\u0266\3\2\2\2\u0268\u026b\3\2\2\2\u0269\u0267\3"+
		"\2\2\2\u0269\u026a\3\2\2\2\u026a\u026f\3\2\2\2\u026b\u0269\3\2\2\2\u026c"+
		"\u026e\5v<\2\u026d\u026c\3\2\2\2\u026e\u0271\3\2\2\2\u026f\u026d\3\2\2"+
		"\2\u026f\u0270\3\2\2\2\u0270\u0275\3\2\2\2\u0271\u026f\3\2\2\2\u0272\u0274"+
		"\5D#\2\u0273\u0272\3\2\2\2\u0274\u0277\3\2\2\2\u0275\u0273\3\2\2\2\u0275"+
		"\u0276\3\2\2\2\u0276\u0278\3\2\2\2\u0277\u0275\3\2\2\2\u0278\u027a\7\6"+
		"\2\2\u0279\u027b\5X-\2\u027a\u0279\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u027a"+
		"\3\2\2\2\u027c\u027d\3\2\2\2\u027d\u0280\3\2\2\2\u027e\u027f\7\36\2\2"+
		"\u027f\u0281\5\34\17\2\u0280\u027e\3\2\2\2\u0280\u0281\3\2\2\2\u0281\u0282"+
		"\3\2\2\2\u0282\u0283\7\7\2\2\u0283u\3\2\2\2\u0284\u0286\7\35\2\2\u0285"+
		"\u0284\3\2\2\2\u0285\u0286\3\2\2\2\u0286\u0288\3\2\2\2\u0287\u0289\5\f"+
		"\7\2\u0288\u0287\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u028a\3\2\2\2\u028a"+
		"\u028b\5\26\f\2\u028b\u028c\7\33\2\2\u028c\u028d\5\34\17\2\u028dw\3\2"+
		"\2\2\u028e\u0290\7\31\2\2\u028f\u0291\5v<\2\u0290\u028f\3\2\2\2\u0291"+
		"\u0292\3\2\2\2\u0292\u0290\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u0294\3\2"+
		"\2\2\u0294\u0295\5:\36\2\u0295y\3\2\2\2N\u0091\u0098\u009c\u009f\u00a2"+
		"\u00aa\u00b0\u00b5\u00ba\u00cf\u00d1\u00d4\u00db\u00e3\u00eb\u00f1\u00f6"+
		"\u0103\u0105\u010b\u0110\u0114\u0121\u012a\u0131\u0145\u014c\u0157\u0160"+
		"\u0163\u016a\u016f\u0172\u017b\u0187\u019a\u01a1\u01a7\u01ad\u01b2\u01ba"+
		"\u01c2\u01c6\u01ce\u01d4\u01d9\u01ea\u01ee\u01f4\u01f9\u0200\u0205\u020d"+
		"\u0210\u0218\u0220\u0228\u022a\u022f\u0234\u0238\u023d\u024a\u024f\u0254"+
		"\u0258\u025b\u0263\u0269\u026f\u0275\u027c\u0280\u0285\u0288\u0292";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}