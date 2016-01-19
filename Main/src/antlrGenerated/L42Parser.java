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
		RULE_mCall = 38, RULE_round = 39, RULE_ps = 40, RULE_k1 = 41, RULE_kMany = 42, 
		RULE_k = 43, RULE_ks = 44, RULE_on = 45, RULE_onPlus = 46, RULE_nudeE = 47, 
		RULE_classBExtra = 48, RULE_classBReuse = 49, RULE_classB = 50, RULE_mhs = 51, 
		RULE_mht = 52, RULE_member = 53, RULE_methodWithType = 54, RULE_methodImplemented = 55, 
		RULE_nestedClass = 56, RULE_header = 57, RULE_fieldDec = 58, RULE_w = 59, 
		RULE_wSwitch = 60, RULE_i = 61, RULE_wSimple = 62;
	public static final String[] ruleNames = {
		"m", "mDec", "path", "docs", "docsOpt", "t", "concreteT", "historicalSeq", 
		"historicalT", "methSelector", "x", "xOp", "eTopForMethod", "eTop", "eL3", 
		"eL2", "eL1", "numParse", "eUnOp", "ePost", "eAtom", "mxRound", "useSquare", 
		"ifExpr", "using", "whileExpr", "signalExpr", "loopExpr", "block", "roundBlockForMethod", 
		"roundBlock", "bb", "curlyBlock", "varDec", "d", "stringParse", "square", 
		"squareW", "mCall", "round", "ps", "k1", "kMany", "k", "ks", "on", "onPlus", 
		"nudeE", "classBExtra", "classBReuse", "classB", "mhs", "mht", "member", 
		"methodWithType", "methodImplemented", "nestedClass", "header", "fieldDec", 
		"w", "wSwitch", "i", "wSimple"
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
			setState(126);
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
			setState(149);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(128); m();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(129); match(UnOp);
				setState(130); match(ORoundNoSpace);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(131); match(EqOp);
				setState(132); match(ORoundNoSpace);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(133); match(BoolOp);
				setState(134); match(ORoundNoSpace);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(135); match(RelOp);
				setState(136); match(ORoundNoSpace);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(137); match(DataOp);
				setState(138); match(ORoundNoSpace);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(139); match(UnOp);
				setState(140); match(ORoundSpace);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(141); match(EqOp);
				setState(142); match(ORoundSpace);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(143); match(BoolOp);
				setState(144); match(ORoundSpace);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(145); match(RelOp);
				setState(146); match(ORoundSpace);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(147); match(DataOp);
				setState(148); match(ORoundSpace);
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
			setState(151); match(Path);
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
			setState(153); match(Doc);
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
			setState(156);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(155); match(Doc);
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
			setState(160);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(158); concreteT();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159); historicalT();
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
			setState(170); match(ClassSep);
			setState(171); methSelector();
			setState(174);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(172); match(ClassSep);
				setState(173); x();
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
			setState(176); match(Path);
			setState(178); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(177); historicalSeq();
				}
				}
				setState(180); 
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
			setState(185);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(182); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(183); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(184); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(187); x();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193); match(CRound);
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
			setState(195); match(X);
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
			setState(197); match(X);
			setState(198); match(EqOp);
			setState(199); eTop();
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
			setState(216);
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
				setState(201); eTop();
				}
				break;
			case ORoundNoSpace:
				enterOuterAlt(_localctx, 2);
				{
				setState(202); roundBlockForMethod();
				setState(213);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ORoundNoSpace) | (1L << OSquare) | (1L << Dot) | (1L << StringQuote) | (1L << Doc))) != 0)) {
					{
					setState(211);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(203); squareW();
						}
						break;

					case 2:
						{
						setState(204); square();
						}
						break;

					case 3:
						{
						setState(205); match(Dot);
						setState(206); mCall();
						}
						break;

					case 4:
						{
						setState(207); match(ORoundNoSpace);
						setState(208); round();
						}
						break;

					case 5:
						{
						setState(209); docs();
						}
						break;

					case 6:
						{
						setState(210); stringParse();
						}
						break;
					}
					}
					setState(215);
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
			setState(218); eL3();
			setState(223);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(219); match(BoolOp);
					setState(220); eL3();
					}
					} 
				}
				setState(225);
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
			setState(226); eL2();
			setState(231);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(227); match(RelOp);
					setState(228); eL2();
					}
					} 
				}
				setState(233);
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
			setState(234); eL1();
			setState(239);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(235); match(DataOp);
					setState(236); eL1();
					}
					} 
				}
				setState(241);
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
			setState(242); eUnOp();
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
			setState(245);
			_la = _input.LA(1);
			if (_la==DataOp) {
				{
				setState(244); match(DataOp);
				}
			}

			setState(247); match(NumParse);
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
			setState(250);
			_la = _input.LA(1);
			if (_la==UnOp) {
				{
				setState(249); match(UnOp);
				}
			}

			setState(252); ePost();
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
			setState(254); eAtom();
			setState(265);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(263);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						setState(255); squareW();
						}
						break;

					case 2:
						{
						setState(256); square();
						}
						break;

					case 3:
						{
						setState(257); match(Dot);
						setState(258); mCall();
						}
						break;

					case 4:
						{
						setState(259); match(ORoundNoSpace);
						setState(260); round();
						}
						break;

					case 5:
						{
						setState(261); docs();
						}
						break;

					case 6:
						{
						setState(262); stringParse();
						}
						break;
					}
					} 
				}
				setState(267);
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
			setState(293);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(268); classBReuse();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(269); classB();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(271);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(270); numParse();
					}
				}

				setState(273); x();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(274); xOp();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(276);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(275); numParse();
					}
				}

				setState(278); match(Path);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(280);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(279); numParse();
					}
				}

				setState(282); block();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(283); ifExpr();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(284); whileExpr();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(285); signalExpr();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(286); loopExpr();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(287); match(WalkBy);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(288); w();
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(289); using();
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(290); match(DotDotDot);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(291); mxRound();
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(292); useSquare();
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
			setState(295); match(MX);
			setState(296); round();
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
			setState(302);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(298); match(Using);
				setState(299); square();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(300); match(Using);
				setState(301); squareW();
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
			setState(304); match(If);
			setState(305); eTop();
			setState(306); block();
			setState(309);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(307); match(Else);
				setState(308); eTop();
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
			setState(311); match(Using);
			setState(312); match(Path);
			setState(313); match(Check);
			setState(314); mCall();
			setState(315); eTop();
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
			setState(317); match(While);
			setState(318); eTop();
			setState(319); block();
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
			setState(321); match(S);
			setState(322); eTop();
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
			setState(324); match(Loop);
			setState(325); eTop();
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
			setState(329);
			switch (_input.LA(1)) {
			case ORoundSpace:
				enterOuterAlt(_localctx, 1);
				{
				setState(327); roundBlock();
				}
				break;
			case OCurly:
				enterOuterAlt(_localctx, 2);
				{
				setState(328); curlyBlock();
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
			setState(331); match(ORoundNoSpace);
			setState(332); docsOpt();
			setState(336);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(333); bb();
					}
					} 
				}
				setState(338);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(339); eTop();
			setState(340); match(CRound);
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
			setState(342); match(ORoundSpace);
			setState(343); docsOpt();
			setState(347);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(344); bb();
					}
					} 
				}
				setState(349);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(350); eTop();
			setState(351); match(CRound);
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
			setState(354); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(353); d();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(356); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(358); ks();
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
			setState(360); match(OCurly);
			setState(361); docsOpt();
			setState(363); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(362); bb();
				}
				}
				setState(365); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << S) | (1L << Mdf) | (1L << ORoundSpace) | (1L << OCurly) | (1L << DotDotDot) | (1L << Ph) | (1L << If) | (1L << While) | (1L << Loop) | (1L << With) | (1L << Var) | (1L << Using) | (1L << WalkBy) | (1L << Path) | (1L << MX) | (1L << X) | (1L << UnOp) | (1L << DataOp) | (1L << NumParse))) != 0) );
			setState(367); match(CCurly);
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
			setState(370);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(369); match(Var);
				}
			}

			setState(373);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(372); t();
				}
			}

			setState(375); x();
			setState(376); match(Equal);
			setState(377); eTop();
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
			setState(382);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(379); varDec();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(380); eTop();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(381); nestedClass();
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
			setState(384); match(StringQuote);
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
			setState(386); match(OSquare);
			setState(387); docsOpt();
			setState(394);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(388); ps();
					setState(389); match(Semicolon);
					setState(390); docsOpt();
					}
					} 
				}
				setState(396);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			setState(397); ps();
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
			setState(400); match(OSquare);
			setState(401); docsOpt();
			setState(402); w();
			setState(403); match(CSquare);
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
			setState(405); m();
			setState(406); round();
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
			setState(408); docsOpt();
			setState(409); ps();
			setState(410); match(CRound);
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
			setState(413);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(412); eTop();
				}
				break;
			}
			setState(420);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(415); match(X);
				setState(416); match(Colon);
				setState(417); eTop();
				}
				}
				setState(422);
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
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
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
			setState(423); match(Catch);
			setState(424); match(S);
			setState(425); t();
			setState(426); match(X);
			setState(427); eTop();
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
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
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
			setState(429); match(Catch);
			setState(430); match(S);
			setState(432); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(431); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(434); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(436); eTop();
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
		enterRule(_localctx, 86, RULE_k);
		try {
			setState(440);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(438); k1();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(439); kMany();
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
		enterRule(_localctx, 88, RULE_ks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Catch) {
				{
				{
				setState(442); k();
				}
				}
				setState(447);
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
		enterRule(_localctx, 90, RULE_on);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448); match(On);
			setState(449); t();
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
		enterRule(_localctx, 92, RULE_onPlus);
		int _la;
		try {
			int _alt;
			setState(472);
			switch (_input.LA(1)) {
			case On:
				enterOuterAlt(_localctx, 1);
				{
				setState(456); match(On);
				setState(458); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(457); t();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(460); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
				setState(464);
				_la = _input.LA(1);
				if (_la==Case) {
					{
					setState(462); match(Case);
					setState(463); eTop();
					}
				}

				setState(466); eTop();
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(468); match(Case);
				setState(469); eTop();
				setState(470); eTop();
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
		enterRule(_localctx, 94, RULE_nudeE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474); eTop();
			setState(475); match(EOF);
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
		enterRule(_localctx, 96, RULE_classBExtra);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			_la = _input.LA(1);
			if (_la==Stage) {
				{
				setState(477); match(Stage);
				}
			}

			setState(483);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Path) {
				{
				{
				setState(480); match(Path);
				}
				}
				setState(485);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(486); match(EndType);
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
		enterRule(_localctx, 98, RULE_classBReuse);
		int _la;
		try {
			setState(504);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(488); match(OCurly);
				setState(489); docsOpt();
				setState(490); match(Url);
				setState(491); match(CCurly);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(493); match(OCurly);
				setState(494); docsOpt();
				setState(495); match(UrlNL);
				setState(496); docsOpt();
				setState(498); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(497); member();
					}
					}
					setState(500); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0) );
				setState(502); match(CCurly);
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
		enterRule(_localctx, 100, RULE_classB);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(506); match(OCurly);
			setState(507); docsOpt();
			setState(508); header();
			setState(510);
			_la = _input.LA(1);
			if (_la==Implements) {
				{
				setState(509); match(Implements);
				}
			}

			setState(515);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(512); match(Path);
					}
					} 
				}
				setState(517);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			setState(518); docsOpt();
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0)) {
				{
				{
				setState(519); member();
				}
				}
				setState(524);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(525); match(CCurly);
			setState(527);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(526); classBExtra();
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
		enterRule(_localctx, 102, RULE_mhs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529); match(Method);
			setState(530); docsOpt();
			setState(531); methSelector();
			setState(535);
			_la = _input.LA(1);
			if (_la==EndType) {
				{
				setState(532); match(EndType);
				setState(533); match(Path);
				setState(534); mht();
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
		enterRule(_localctx, 104, RULE_mht);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(538);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(537); match(Mdf);
				}
			}

			setState(540); match(Method);
			setState(541); docsOpt();
			setState(542); t();
			setState(546);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(543); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(544); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(545); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(554);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				{
				setState(548); t();
				setState(549); x();
				setState(550); docsOpt();
				}
				}
				setState(556);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(557); match(CRound);
			setState(564);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(558); match(S);
				setState(560); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(559); match(Path);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(562); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
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
		enterRule(_localctx, 106, RULE_member);
		try {
			setState(569);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(566); methodWithType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(567); methodImplemented();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(568); nestedClass();
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
		enterRule(_localctx, 108, RULE_methodWithType);
		int _la;
		try {
			setState(583);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(571); mht();
				setState(574);
				_la = _input.LA(1);
				if (_la==EndType) {
					{
					setState(572); match(EndType);
					setState(573); match(Path);
					}
				}

				setState(576); docsOpt();
				setState(578);
				switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
				case 1:
					{
					setState(577); eTopForMethod();
					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(580); mht();
				setState(581); match(FieldSpecial);
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
		enterRule(_localctx, 110, RULE_methodImplemented);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585); mhs();
			setState(586); eTopForMethod();
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
		enterRule(_localctx, 112, RULE_nestedClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(588); match(Path);
			setState(589); match(Colon);
			setState(590); docsOpt();
			setState(591); eTop();
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
		enterRule(_localctx, 114, RULE_header);
		int _la;
		try {
			setState(610);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(594); match(Interface);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(596);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(595); match(Mdf);
					}
				}

				setState(601);
				switch (_input.LA(1)) {
				case MX:
				case HashX:
				case UnOp:
				case EqOp:
				case BoolOp:
				case RelOp:
				case DataOp:
					{
					setState(598); mDec();
					}
					break;
				case ORoundSpace:
					{
					setState(599); match(ORoundSpace);
					}
					break;
				case ORoundNoSpace:
					{
					setState(600); match(ORoundNoSpace);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(606);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path))) != 0)) {
					{
					{
					setState(603); fieldDec();
					}
					}
					setState(608);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(609); match(CRound);
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
		enterRule(_localctx, 116, RULE_fieldDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(613);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(612); match(Var);
				}
			}

			setState(615); t();
			setState(616); x();
			setState(617); docsOpt();
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
		enterRule(_localctx, 118, RULE_w);
		try {
			setState(621);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(619); wSwitch();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(620); wSimple();
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
		enterRule(_localctx, 120, RULE_wSwitch);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(623); match(With);
			setState(627);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(624); x();
					}
					} 
				}
				setState(629);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			}
			setState(633);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(630); i();
					}
					} 
				}
				setState(635);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			}
			setState(639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0)) {
				{
				{
				setState(636); varDec();
				}
				}
				setState(641);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(642); match(ORoundSpace);
			setState(644); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(643); onPlus();
				}
				}
				setState(646); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Case || _la==On );
			setState(650);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(648); match(Default);
				setState(649); eTop();
				}
			}

			setState(652); match(CRound);
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
		enterRule(_localctx, 122, RULE_i);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(654); match(Var);
				}
			}

			setState(658);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(657); t();
				}
			}

			setState(660); x();
			setState(661); match(In);
			setState(662); eTop();
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
		enterRule(_localctx, 124, RULE_wSimple);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(664); match(With);
			setState(666); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(665); i();
				}
				}
				setState(668); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0) );
			setState(670); block();
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\65\u02a3\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0098\n\3\3\4\3\4\3\5\3\5"+
		"\3\6\5\6\u009f\n\6\3\7\3\7\5\7\u00a3\n\7\3\b\5\b\u00a6\n\b\3\b\5\b\u00a9"+
		"\n\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00b1\n\t\3\n\3\n\6\n\u00b5\n\n\r\n\16"+
		"\n\u00b6\3\13\3\13\3\13\5\13\u00bc\n\13\3\13\7\13\u00bf\n\13\f\13\16\13"+
		"\u00c2\13\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\7\16\u00d6\n\16\f\16\16\16\u00d9\13\16\5\16"+
		"\u00db\n\16\3\17\3\17\3\17\7\17\u00e0\n\17\f\17\16\17\u00e3\13\17\3\20"+
		"\3\20\3\20\7\20\u00e8\n\20\f\20\16\20\u00eb\13\20\3\21\3\21\3\21\7\21"+
		"\u00f0\n\21\f\21\16\21\u00f3\13\21\3\22\3\22\3\23\5\23\u00f8\n\23\3\23"+
		"\3\23\3\24\5\24\u00fd\n\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\7\25\u010a\n\25\f\25\16\25\u010d\13\25\3\26\3\26\3\26\5\26"+
		"\u0112\n\26\3\26\3\26\3\26\5\26\u0117\n\26\3\26\3\26\5\26\u011b\n\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u0128\n\26"+
		"\3\27\3\27\3\27\3\30\3\30\3\30\3\30\5\30\u0131\n\30\3\31\3\31\3\31\3\31"+
		"\3\31\5\31\u0138\n\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\5\36\u014c\n\36\3\37\3\37\3\37"+
		"\7\37\u0151\n\37\f\37\16\37\u0154\13\37\3\37\3\37\3\37\3 \3 \3 \7 \u015c"+
		"\n \f \16 \u015f\13 \3 \3 \3 \3!\6!\u0165\n!\r!\16!\u0166\3!\3!\3\"\3"+
		"\"\3\"\6\"\u016e\n\"\r\"\16\"\u016f\3\"\3\"\3#\5#\u0175\n#\3#\5#\u0178"+
		"\n#\3#\3#\3#\3#\3$\3$\3$\5$\u0181\n$\3%\3%\3&\3&\3&\3&\3&\3&\7&\u018b"+
		"\n&\f&\16&\u018e\13&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3"+
		")\3*\5*\u01a0\n*\3*\3*\3*\7*\u01a5\n*\f*\16*\u01a8\13*\3+\3+\3+\3+\3+"+
		"\3+\3,\3,\3,\6,\u01b3\n,\r,\16,\u01b4\3,\3,\3-\3-\5-\u01bb\n-\3.\7.\u01be"+
		"\n.\f.\16.\u01c1\13.\3/\3/\3/\3/\5/\u01c7\n/\3/\3/\3\60\3\60\6\60\u01cd"+
		"\n\60\r\60\16\60\u01ce\3\60\3\60\5\60\u01d3\n\60\3\60\3\60\3\60\3\60\3"+
		"\60\3\60\5\60\u01db\n\60\3\61\3\61\3\61\3\62\5\62\u01e1\n\62\3\62\7\62"+
		"\u01e4\n\62\f\62\16\62\u01e7\13\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\6\63\u01f5\n\63\r\63\16\63\u01f6\3\63\3\63\5"+
		"\63\u01fb\n\63\3\64\3\64\3\64\3\64\5\64\u0201\n\64\3\64\7\64\u0204\n\64"+
		"\f\64\16\64\u0207\13\64\3\64\3\64\7\64\u020b\n\64\f\64\16\64\u020e\13"+
		"\64\3\64\3\64\5\64\u0212\n\64\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u021a"+
		"\n\65\3\66\5\66\u021d\n\66\3\66\3\66\3\66\3\66\3\66\3\66\5\66\u0225\n"+
		"\66\3\66\3\66\3\66\3\66\7\66\u022b\n\66\f\66\16\66\u022e\13\66\3\66\3"+
		"\66\3\66\6\66\u0233\n\66\r\66\16\66\u0234\5\66\u0237\n\66\3\67\3\67\3"+
		"\67\5\67\u023c\n\67\38\38\38\58\u0241\n8\38\38\58\u0245\n8\38\38\38\5"+
		"8\u024a\n8\39\39\39\3:\3:\3:\3:\3:\3;\3;\3;\5;\u0257\n;\3;\3;\3;\5;\u025c"+
		"\n;\3;\7;\u025f\n;\f;\16;\u0262\13;\3;\5;\u0265\n;\3<\5<\u0268\n<\3<\3"+
		"<\3<\3<\3=\3=\5=\u0270\n=\3>\3>\7>\u0274\n>\f>\16>\u0277\13>\3>\7>\u027a"+
		"\n>\f>\16>\u027d\13>\3>\7>\u0280\n>\f>\16>\u0283\13>\3>\3>\6>\u0287\n"+
		">\r>\16>\u0288\3>\3>\5>\u028d\n>\3>\3>\3?\5?\u0292\n?\3?\5?\u0295\n?\3"+
		"?\3?\3?\3?\3@\3@\6@\u029d\n@\r@\16@\u029e\3@\3@\3@\2\2A\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd"+
		"fhjlnprtvxz|~\2\3\4\2((**\u02d3\2\u0080\3\2\2\2\4\u0097\3\2\2\2\6\u0099"+
		"\3\2\2\2\b\u009b\3\2\2\2\n\u009e\3\2\2\2\f\u00a2\3\2\2\2\16\u00a5\3\2"+
		"\2\2\20\u00ac\3\2\2\2\22\u00b2\3\2\2\2\24\u00bb\3\2\2\2\26\u00c5\3\2\2"+
		"\2\30\u00c7\3\2\2\2\32\u00da\3\2\2\2\34\u00dc\3\2\2\2\36\u00e4\3\2\2\2"+
		" \u00ec\3\2\2\2\"\u00f4\3\2\2\2$\u00f7\3\2\2\2&\u00fc\3\2\2\2(\u0100\3"+
		"\2\2\2*\u0127\3\2\2\2,\u0129\3\2\2\2.\u0130\3\2\2\2\60\u0132\3\2\2\2\62"+
		"\u0139\3\2\2\2\64\u013f\3\2\2\2\66\u0143\3\2\2\28\u0146\3\2\2\2:\u014b"+
		"\3\2\2\2<\u014d\3\2\2\2>\u0158\3\2\2\2@\u0164\3\2\2\2B\u016a\3\2\2\2D"+
		"\u0174\3\2\2\2F\u0180\3\2\2\2H\u0182\3\2\2\2J\u0184\3\2\2\2L\u0192\3\2"+
		"\2\2N\u0197\3\2\2\2P\u019a\3\2\2\2R\u019f\3\2\2\2T\u01a9\3\2\2\2V\u01af"+
		"\3\2\2\2X\u01ba\3\2\2\2Z\u01bf\3\2\2\2\\\u01c2\3\2\2\2^\u01da\3\2\2\2"+
		"`\u01dc\3\2\2\2b\u01e0\3\2\2\2d\u01fa\3\2\2\2f\u01fc\3\2\2\2h\u0213\3"+
		"\2\2\2j\u021c\3\2\2\2l\u023b\3\2\2\2n\u0249\3\2\2\2p\u024b\3\2\2\2r\u024e"+
		"\3\2\2\2t\u0264\3\2\2\2v\u0267\3\2\2\2x\u026f\3\2\2\2z\u0271\3\2\2\2|"+
		"\u0291\3\2\2\2~\u029a\3\2\2\2\u0080\u0081\t\2\2\2\u0081\3\3\2\2\2\u0082"+
		"\u0098\5\2\2\2\u0083\u0084\7\60\2\2\u0084\u0098\7\5\2\2\u0085\u0086\7"+
		"\61\2\2\u0086\u0098\7\5\2\2\u0087\u0088\7\62\2\2\u0088\u0098\7\5\2\2\u0089"+
		"\u008a\7\63\2\2\u008a\u0098\7\5\2\2\u008b\u008c\7\64\2\2\u008c\u0098\7"+
		"\5\2\2\u008d\u008e\7\60\2\2\u008e\u0098\7\6\2\2\u008f\u0090\7\61\2\2\u0090"+
		"\u0098\7\6\2\2\u0091\u0092\7\62\2\2\u0092\u0098\7\6\2\2\u0093\u0094\7"+
		"\63\2\2\u0094\u0098\7\6\2\2\u0095\u0096\7\64\2\2\u0096\u0098\7\6\2\2\u0097"+
		"\u0082\3\2\2\2\u0097\u0083\3\2\2\2\u0097\u0085\3\2\2\2\u0097\u0087\3\2"+
		"\2\2\u0097\u0089\3\2\2\2\u0097\u008b\3\2\2\2\u0097\u008d\3\2\2\2\u0097"+
		"\u008f\3\2\2\2\u0097\u0091\3\2\2\2\u0097\u0093\3\2\2\2\u0097\u0095\3\2"+
		"\2\2\u0098\5\3\2\2\2\u0099\u009a\7&\2\2\u009a\7\3\2\2\2\u009b\u009c\7"+
		".\2\2\u009c\t\3\2\2\2\u009d\u009f\7.\2\2\u009e\u009d\3\2\2\2\u009e\u009f"+
		"\3\2\2\2\u009f\13\3\2\2\2\u00a0\u00a3\5\16\b\2\u00a1\u00a3\5\22\n\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a1\3\2\2\2\u00a3\r\3\2\2\2\u00a4\u00a6\7\22\2"+
		"\2\u00a5\u00a4\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a9"+
		"\7\4\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa"+
		"\u00ab\7&\2\2\u00ab\17\3\2\2\2\u00ac\u00ad\7\'\2\2\u00ad\u00b0\5\24\13"+
		"\2\u00ae\u00af\7\'\2\2\u00af\u00b1\5\26\f\2\u00b0\u00ae\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1\21\3\2\2\2\u00b2\u00b4\7&\2\2\u00b3\u00b5\5\20\t"+
		"\2\u00b4\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7"+
		"\3\2\2\2\u00b7\23\3\2\2\2\u00b8\u00bc\5\4\3\2\u00b9\u00bc\7\5\2\2\u00ba"+
		"\u00bc\7\6\2\2\u00bb\u00b8\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00ba\3\2"+
		"\2\2\u00bc\u00c0\3\2\2\2\u00bd\u00bf\5\26\f\2\u00be\u00bd\3\2\2\2\u00bf"+
		"\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c3\3\2"+
		"\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00c4\7\7\2\2\u00c4\25\3\2\2\2\u00c5\u00c6"+
		"\7)\2\2\u00c6\27\3\2\2\2\u00c7\u00c8\7)\2\2\u00c8\u00c9\7\61\2\2\u00c9"+
		"\u00ca\5\34\17\2\u00ca\31\3\2\2\2\u00cb\u00db\5\34\17\2\u00cc\u00d7\5"+
		"<\37\2\u00cd\u00d6\5L\'\2\u00ce\u00d6\5J&\2\u00cf\u00d0\7\20\2\2\u00d0"+
		"\u00d6\5N(\2\u00d1\u00d2\7\5\2\2\u00d2\u00d6\5P)\2\u00d3\u00d6\5\b\5\2"+
		"\u00d4\u00d6\5H%\2\u00d5\u00cd\3\2\2\2\u00d5\u00ce\3\2\2\2\u00d5\u00cf"+
		"\3\2\2\2\u00d5\u00d1\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d4\3\2\2\2\u00d6"+
		"\u00d9\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00db\3\2"+
		"\2\2\u00d9\u00d7\3\2\2\2\u00da\u00cb\3\2\2\2\u00da\u00cc\3\2\2\2\u00db"+
		"\33\3\2\2\2\u00dc\u00e1\5\36\20\2\u00dd\u00de\7\62\2\2\u00de\u00e0\5\36"+
		"\20\2\u00df\u00dd\3\2\2\2\u00e0\u00e3\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1"+
		"\u00e2\3\2\2\2\u00e2\35\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e4\u00e9\5 \21"+
		"\2\u00e5\u00e6\7\63\2\2\u00e6\u00e8\5 \21\2\u00e7\u00e5\3\2\2\2\u00e8"+
		"\u00eb\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\37\3\2\2"+
		"\2\u00eb\u00e9\3\2\2\2\u00ec\u00f1\5\"\22\2\u00ed\u00ee\7\64\2\2\u00ee"+
		"\u00f0\5\"\22\2\u00ef\u00ed\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3"+
		"\2\2\2\u00f1\u00f2\3\2\2\2\u00f2!\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4\u00f5"+
		"\5&\24\2\u00f5#\3\2\2\2\u00f6\u00f8\7\64\2\2\u00f7\u00f6\3\2\2\2\u00f7"+
		"\u00f8\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fa\7\65\2\2\u00fa%\3\2\2\2"+
		"\u00fb\u00fd\7\60\2\2\u00fc\u00fb\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fe"+
		"\3\2\2\2\u00fe\u00ff\5(\25\2\u00ff\'\3\2\2\2\u0100\u010b\5*\26\2\u0101"+
		"\u010a\5L\'\2\u0102\u010a\5J&\2\u0103\u0104\7\20\2\2\u0104\u010a\5N(\2"+
		"\u0105\u0106\7\5\2\2\u0106\u010a\5P)\2\u0107\u010a\5\b\5\2\u0108\u010a"+
		"\5H%\2\u0109\u0101\3\2\2\2\u0109\u0102\3\2\2\2\u0109\u0103\3\2\2\2\u0109"+
		"\u0105\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u0108\3\2\2\2\u010a\u010d\3\2"+
		"\2\2\u010b\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c)\3\2\2\2\u010d\u010b"+
		"\3\2\2\2\u010e\u0128\5d\63\2\u010f\u0128\5f\64\2\u0110\u0112\5$\23\2\u0111"+
		"\u0110\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0128\5\26"+
		"\f\2\u0114\u0128\5\30\r\2\u0115\u0117\5$\23\2\u0116\u0115\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0128\7&\2\2\u0119\u011b\5$\23"+
		"\2\u011a\u0119\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u0128"+
		"\5:\36\2\u011d\u0128\5\60\31\2\u011e\u0128\5\64\33\2\u011f\u0128\5\66"+
		"\34\2\u0120\u0128\58\35\2\u0121\u0128\7$\2\2\u0122\u0128\5x=\2\u0123\u0128"+
		"\5\62\32\2\u0124\u0128\7\13\2\2\u0125\u0128\5,\27\2\u0126\u0128\5.\30"+
		"\2\u0127\u010e\3\2\2\2\u0127\u010f\3\2\2\2\u0127\u0111\3\2\2\2\u0127\u0114"+
		"\3\2\2\2\u0127\u0116\3\2\2\2\u0127\u011a\3\2\2\2\u0127\u011d\3\2\2\2\u0127"+
		"\u011e\3\2\2\2\u0127\u011f\3\2\2\2\u0127\u0120\3\2\2\2\u0127\u0121\3\2"+
		"\2\2\u0127\u0122\3\2\2\2\u0127\u0123\3\2\2\2\u0127\u0124\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0127\u0126\3\2\2\2\u0128+\3\2\2\2\u0129\u012a\7(\2\2\u012a"+
		"\u012b\5P)\2\u012b-\3\2\2\2\u012c\u012d\7!\2\2\u012d\u0131\5J&\2\u012e"+
		"\u012f\7!\2\2\u012f\u0131\5L\'\2\u0130\u012c\3\2\2\2\u0130\u012e\3\2\2"+
		"\2\u0131/\3\2\2\2\u0132\u0133\7\25\2\2\u0133\u0134\5\34\17\2\u0134\u0137"+
		"\5:\36\2\u0135\u0136\7\26\2\2\u0136\u0138\5\34\17\2\u0137\u0135\3\2\2"+
		"\2\u0137\u0138\3\2\2\2\u0138\61\3\2\2\2\u0139\u013a\7!\2\2\u013a\u013b"+
		"\7&\2\2\u013b\u013c\7\"\2\2\u013c\u013d\5N(\2\u013d\u013e\5\34\17\2\u013e"+
		"\63\3\2\2\2\u013f\u0140\7\27\2\2\u0140\u0141\5\34\17\2\u0141\u0142\5:"+
		"\36\2\u0142\65\3\2\2\2\u0143\u0144\7\3\2\2\u0144\u0145\5\34\17\2\u0145"+
		"\67\3\2\2\2\u0146\u0147\7\30\2\2\u0147\u0148\5\34\17\2\u01489\3\2\2\2"+
		"\u0149\u014c\5> \2\u014a\u014c\5B\"\2\u014b\u0149\3\2\2\2\u014b\u014a"+
		"\3\2\2\2\u014c;\3\2\2\2\u014d\u014e\7\5\2\2\u014e\u0152\5\n\6\2\u014f"+
		"\u0151\5@!\2\u0150\u014f\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2\2"+
		"\2\u0152\u0153\3\2\2\2\u0153\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0156"+
		"\5\34\17\2\u0156\u0157\7\7\2\2\u0157=\3\2\2\2\u0158\u0159\7\6\2\2\u0159"+
		"\u015d\5\n\6\2\u015a\u015c\5@!\2\u015b\u015a\3\2\2\2\u015c\u015f\3\2\2"+
		"\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u0160\3\2\2\2\u015f\u015d"+
		"\3\2\2\2\u0160\u0161\5\34\17\2\u0161\u0162\7\7\2\2\u0162?\3\2\2\2\u0163"+
		"\u0165\5F$\2\u0164\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0164\3\2\2"+
		"\2\u0166\u0167\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u0169\5Z.\2\u0169A\3"+
		"\2\2\2\u016a\u016b\7\n\2\2\u016b\u016d\5\n\6\2\u016c\u016e\5@!\2\u016d"+
		"\u016c\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u016d\3\2\2\2\u016f\u0170\3\2"+
		"\2\2\u0170\u0171\3\2\2\2\u0171\u0172\7\r\2\2\u0172C\3\2\2\2\u0173\u0175"+
		"\7\35\2\2\u0174\u0173\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0177\3\2\2\2"+
		"\u0176\u0178\5\f\7\2\u0177\u0176\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179"+
		"\3\2\2\2\u0179\u017a\5\26\f\2\u017a\u017b\7\21\2\2\u017b\u017c\5\34\17"+
		"\2\u017cE\3\2\2\2\u017d\u0181\5D#\2\u017e\u0181\5\34\17\2\u017f\u0181"+
		"\5r:\2\u0180\u017d\3\2\2\2\u0180\u017e\3\2\2\2\u0180\u017f\3\2\2\2\u0181"+
		"G\3\2\2\2\u0182\u0183\7+\2\2\u0183I\3\2\2\2\u0184\u0185\7\b\2\2\u0185"+
		"\u018c\5\n\6\2\u0186\u0187\5R*\2\u0187\u0188\7\17\2\2\u0188\u0189\5\n"+
		"\6\2\u0189\u018b\3\2\2\2\u018a\u0186\3\2\2\2\u018b\u018e\3\2\2\2\u018c"+
		"\u018a\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018f\3\2\2\2\u018e\u018c\3\2"+
		"\2\2\u018f\u0190\5R*\2\u0190\u0191\7\t\2\2\u0191K\3\2\2\2\u0192\u0193"+
		"\7\b\2\2\u0193\u0194\5\n\6\2\u0194\u0195\5x=\2\u0195\u0196\7\t\2\2\u0196"+
		"M\3\2\2\2\u0197\u0198\5\2\2\2\u0198\u0199\5P)\2\u0199O\3\2\2\2\u019a\u019b"+
		"\5\n\6\2\u019b\u019c\5R*\2\u019c\u019d\7\7\2\2\u019dQ\3\2\2\2\u019e\u01a0"+
		"\5\34\17\2\u019f\u019e\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01a6\3\2\2\2"+
		"\u01a1\u01a2\7)\2\2\u01a2\u01a3\7\16\2\2\u01a3\u01a5\5\34\17\2\u01a4\u01a1"+
		"\3\2\2\2\u01a5\u01a8\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7"+
		"S\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a9\u01aa\7\34\2\2\u01aa\u01ab\7\3\2\2"+
		"\u01ab\u01ac\5\f\7\2\u01ac\u01ad\7)\2\2\u01ad\u01ae\5\34\17\2\u01aeU\3"+
		"\2\2\2\u01af\u01b0\7\34\2\2\u01b0\u01b2\7\3\2\2\u01b1\u01b3\5\f\7\2\u01b2"+
		"\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2"+
		"\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b7\5\34\17\2\u01b7W\3\2\2\2\u01b8\u01bb"+
		"\5T+\2\u01b9\u01bb\5V,\2\u01ba\u01b8\3\2\2\2\u01ba\u01b9\3\2\2\2\u01bb"+
		"Y\3\2\2\2\u01bc\u01be\5X-\2\u01bd\u01bc\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf"+
		"\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0[\3\2\2\2\u01c1\u01bf\3\2\2\2"+
		"\u01c2\u01c3\7\32\2\2\u01c3\u01c6\5\f\7\2\u01c4\u01c5\7\24\2\2\u01c5\u01c7"+
		"\5\34\17\2\u01c6\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2"+
		"\u01c8\u01c9\5\34\17\2\u01c9]\3\2\2\2\u01ca\u01cc\7\32\2\2\u01cb\u01cd"+
		"\5\f\7\2\u01cc\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01cc\3\2\2\2\u01ce"+
		"\u01cf\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01d1\7\24\2\2\u01d1\u01d3\5"+
		"\34\17\2\u01d2\u01d0\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4"+
		"\u01d5\5\34\17\2\u01d5\u01db\3\2\2\2\u01d6\u01d7\7\24\2\2\u01d7\u01d8"+
		"\5\34\17\2\u01d8\u01d9\5\34\17\2\u01d9\u01db\3\2\2\2\u01da\u01ca\3\2\2"+
		"\2\u01da\u01d6\3\2\2\2\u01db_\3\2\2\2\u01dc\u01dd\5\34\17\2\u01dd\u01de"+
		"\7\2\2\3\u01dea\3\2\2\2\u01df\u01e1\7%\2\2\u01e0\u01df\3\2\2\2\u01e0\u01e1"+
		"\3\2\2\2\u01e1\u01e5\3\2\2\2\u01e2\u01e4\7&\2\2\u01e3\u01e2\3\2\2\2\u01e4"+
		"\u01e7\3\2\2\2\u01e5\u01e3\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e8\3\2"+
		"\2\2\u01e7\u01e5\3\2\2\2\u01e8\u01e9\7\f\2\2\u01e9c\3\2\2\2\u01ea\u01eb"+
		"\7\n\2\2\u01eb\u01ec\5\n\6\2\u01ec\u01ed\7-\2\2\u01ed\u01ee\7\r\2\2\u01ee"+
		"\u01fb\3\2\2\2\u01ef\u01f0\7\n\2\2\u01f0\u01f1\5\n\6\2\u01f1\u01f2\7,"+
		"\2\2\u01f2\u01f4\5\n\6\2\u01f3\u01f5\5l\67\2\u01f4\u01f3\3\2\2\2\u01f5"+
		"\u01f6\3\2\2\2\u01f6\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f8\3\2"+
		"\2\2\u01f8\u01f9\7\r\2\2\u01f9\u01fb\3\2\2\2\u01fa\u01ea\3\2\2\2\u01fa"+
		"\u01ef\3\2\2\2\u01fbe\3\2\2\2\u01fc\u01fd\7\n\2\2\u01fd\u01fe\5\n\6\2"+
		"\u01fe\u0200\5t;\2\u01ff\u0201\7\23\2\2\u0200\u01ff\3\2\2\2\u0200\u0201"+
		"\3\2\2\2\u0201\u0205\3\2\2\2\u0202\u0204\7&\2\2\u0203\u0202\3\2\2\2\u0204"+
		"\u0207\3\2\2\2\u0205\u0203\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0208\3\2"+
		"\2\2\u0207\u0205\3\2\2\2\u0208\u020c\5\n\6\2\u0209\u020b\5l\67\2\u020a"+
		"\u0209\3\2\2\2\u020b\u020e\3\2\2\2\u020c\u020a\3\2\2\2\u020c\u020d\3\2"+
		"\2\2\u020d\u020f\3\2\2\2\u020e\u020c\3\2\2\2\u020f\u0211\7\r\2\2\u0210"+
		"\u0212\5b\62\2\u0211\u0210\3\2\2\2\u0211\u0212\3\2\2\2\u0212g\3\2\2\2"+
		"\u0213\u0214\7 \2\2\u0214\u0215\5\n\6\2\u0215\u0219\5\24\13\2\u0216\u0217"+
		"\7\f\2\2\u0217\u0218\7&\2\2\u0218\u021a\5j\66\2\u0219\u0216\3\2\2\2\u0219"+
		"\u021a\3\2\2\2\u021ai\3\2\2\2\u021b\u021d\7\4\2\2\u021c\u021b\3\2\2\2"+
		"\u021c\u021d\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u021f\7 \2\2\u021f\u0220"+
		"\5\n\6\2\u0220\u0224\5\f\7\2\u0221\u0225\5\4\3\2\u0222\u0225\7\5\2\2\u0223"+
		"\u0225\7\6\2\2\u0224\u0221\3\2\2\2\u0224\u0222\3\2\2\2\u0224\u0223\3\2"+
		"\2\2\u0225\u022c\3\2\2\2\u0226\u0227\5\f\7\2\u0227\u0228\5\26\f\2\u0228"+
		"\u0229\5\n\6\2\u0229\u022b\3\2\2\2\u022a\u0226\3\2\2\2\u022b\u022e\3\2"+
		"\2\2\u022c\u022a\3\2\2\2\u022c\u022d\3\2\2\2\u022d\u022f\3\2\2\2\u022e"+
		"\u022c\3\2\2\2\u022f\u0236\7\7\2\2\u0230\u0232\7\3\2\2\u0231\u0233\7&"+
		"\2\2\u0232\u0231\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0232\3\2\2\2\u0234"+
		"\u0235\3\2\2\2\u0235\u0237\3\2\2\2\u0236\u0230\3\2\2\2\u0236\u0237\3\2"+
		"\2\2\u0237k\3\2\2\2\u0238\u023c\5n8\2\u0239\u023c\5p9\2\u023a\u023c\5"+
		"r:\2\u023b\u0238\3\2\2\2\u023b\u0239\3\2\2\2\u023b\u023a\3\2\2\2\u023c"+
		"m\3\2\2\2\u023d\u0240\5j\66\2\u023e\u023f\7\f\2\2\u023f\u0241\7&\2\2\u0240"+
		"\u023e\3\2\2\2\u0240\u0241\3\2\2\2\u0241\u0242\3\2\2\2\u0242\u0244\5\n"+
		"\6\2\u0243\u0245\5\32\16\2\u0244\u0243\3\2\2\2\u0244\u0245\3\2\2\2\u0245"+
		"\u024a\3\2\2\2\u0246\u0247\5j\66\2\u0247\u0248\7#\2\2\u0248\u024a\3\2"+
		"\2\2\u0249\u023d\3\2\2\2\u0249\u0246\3\2\2\2\u024ao\3\2\2\2\u024b\u024c"+
		"\5h\65\2\u024c\u024d\5\32\16\2\u024dq\3\2\2\2\u024e\u024f\7&\2\2\u024f"+
		"\u0250\7\16\2\2\u0250\u0251\5\n\6\2\u0251\u0252\5\34\17\2\u0252s\3\2\2"+
		"\2\u0253\u0265\3\2\2\2\u0254\u0265\7\37\2\2\u0255\u0257\7\4\2\2\u0256"+
		"\u0255\3\2\2\2\u0256\u0257\3\2\2\2\u0257\u025b\3\2\2\2\u0258\u025c\5\4"+
		"\3\2\u0259\u025c\7\6\2\2\u025a\u025c\7\5\2\2\u025b\u0258\3\2\2\2\u025b"+
		"\u0259\3\2\2\2\u025b\u025a\3\2\2\2\u025c\u0260\3\2\2\2\u025d\u025f\5v"+
		"<\2\u025e\u025d\3\2\2\2\u025f\u0262\3\2\2\2\u0260\u025e\3\2\2\2\u0260"+
		"\u0261\3\2\2\2\u0261\u0263\3\2\2\2\u0262\u0260\3\2\2\2\u0263\u0265\7\7"+
		"\2\2\u0264\u0253\3\2\2\2\u0264\u0254\3\2\2\2\u0264\u0256\3\2\2\2\u0265"+
		"u\3\2\2\2\u0266\u0268\7\35\2\2\u0267\u0266\3\2\2\2\u0267\u0268\3\2\2\2"+
		"\u0268\u0269\3\2\2\2\u0269\u026a\5\f\7\2\u026a\u026b\5\26\f\2\u026b\u026c"+
		"\5\n\6\2\u026cw\3\2\2\2\u026d\u0270\5z>\2\u026e\u0270\5~@\2\u026f\u026d"+
		"\3\2\2\2\u026f\u026e\3\2\2\2\u0270y\3\2\2\2\u0271\u0275\7\31\2\2\u0272"+
		"\u0274\5\26\f\2\u0273\u0272\3\2\2\2\u0274\u0277\3\2\2\2\u0275\u0273\3"+
		"\2\2\2\u0275\u0276\3\2\2\2\u0276\u027b\3\2\2\2\u0277\u0275\3\2\2\2\u0278"+
		"\u027a\5|?\2\u0279\u0278\3\2\2\2\u027a\u027d\3\2\2\2\u027b\u0279\3\2\2"+
		"\2\u027b\u027c\3\2\2\2\u027c\u0281\3\2\2\2\u027d\u027b\3\2\2\2\u027e\u0280"+
		"\5D#\2\u027f\u027e\3\2\2\2\u0280\u0283\3\2\2\2\u0281\u027f\3\2\2\2\u0281"+
		"\u0282\3\2\2\2\u0282\u0284\3\2\2\2\u0283\u0281\3\2\2\2\u0284\u0286\7\6"+
		"\2\2\u0285\u0287\5^\60\2\u0286\u0285\3\2\2\2\u0287\u0288\3\2\2\2\u0288"+
		"\u0286\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u028c\3\2\2\2\u028a\u028b\7\36"+
		"\2\2\u028b\u028d\5\34\17\2\u028c\u028a\3\2\2\2\u028c\u028d\3\2\2\2\u028d"+
		"\u028e\3\2\2\2\u028e\u028f\7\7\2\2\u028f{\3\2\2\2\u0290\u0292\7\35\2\2"+
		"\u0291\u0290\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0294\3\2\2\2\u0293\u0295"+
		"\5\f\7\2\u0294\u0293\3\2\2\2\u0294\u0295\3\2\2\2\u0295\u0296\3\2\2\2\u0296"+
		"\u0297\5\26\f\2\u0297\u0298\7\33\2\2\u0298\u0299\5\34\17\2\u0299}\3\2"+
		"\2\2\u029a\u029c\7\31\2\2\u029b\u029d\5|?\2\u029c\u029b\3\2\2\2\u029d"+
		"\u029e\3\2\2\2\u029e\u029c\3\2\2\2\u029e\u029f\3\2\2\2\u029f\u02a0\3\2"+
		"\2\2\u02a0\u02a1\5:\36\2\u02a1\177\3\2\2\2M\u0097\u009e\u00a2\u00a5\u00a8"+
		"\u00b0\u00b6\u00bb\u00c0\u00d5\u00d7\u00da\u00e1\u00e9\u00f1\u00f7\u00fc"+
		"\u0109\u010b\u0111\u0116\u011a\u0127\u0130\u0137\u014b\u0152\u015d\u0166"+
		"\u016f\u0174\u0177\u0180\u018c\u019f\u01a6\u01b4\u01ba\u01bf\u01c6\u01ce"+
		"\u01d2\u01da\u01e0\u01e5\u01f6\u01fa\u0200\u0205\u020c\u0211\u0219\u021c"+
		"\u0224\u022c\u0234\u0236\u023b\u0240\u0244\u0249\u0256\u025b\u0260\u0264"+
		"\u0267\u026f\u0275\u027b\u0281\u0288\u028c\u0291\u0294\u029e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}