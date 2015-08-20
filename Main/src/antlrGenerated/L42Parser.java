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
		ClassSep=37, X=38, HashX=39, StringQuote=40, UrlNL=41, Url=42, Doc=43, 
		WS=44, UnOp=45, EqOp=46, BoolOp=47, RelOp=48, DataOp=49, NumParse=50;
	public static final String[] tokenNames = {
		"<INVALID>", "S", "Mdf", "'('", "'\t'", "')'", "'['", "']'", "'{'", "'...'", 
		"'^##'", "'}'", "':'", "';'", "'.'", "'='", "'^'", "'<:'", "'case'", "'if'", 
		"'else'", "'while'", "'loop'", "'with'", "'on'", "'in'", "'catch'", "'var'", 
		"'default'", "'interface'", "'method'", "'using'", "'check'", "'##field'", 
		"'##walkBy'", "Stage", "Path", "'::'", "X", "HashX", "StringQuote", "UrlNL", 
		"Url", "Doc", "WS", "UnOp", "EqOp", "BoolOp", "RelOp", "DataOp", "NumParse"
	};
	public static final int
		RULE_m = 0, RULE_mDec = 1, RULE_path = 2, RULE_docs = 3, RULE_docsOpt = 4, 
		RULE_t = 5, RULE_concreteT = 6, RULE_historicalSeq = 7, RULE_historicalT = 8, 
		RULE_methSelector = 9, RULE_x = 10, RULE_xOp = 11, RULE_eTopForMethod = 12, 
		RULE_eTop = 13, RULE_eL3 = 14, RULE_eL2 = 15, RULE_eL1 = 16, RULE_numParse = 17, 
		RULE_eUnOp = 18, RULE_ePost = 19, RULE_eAtom = 20, RULE_ifExpr = 21, RULE_using = 22, 
		RULE_whileExpr = 23, RULE_signalExpr = 24, RULE_loopExpr = 25, RULE_block = 26, 
		RULE_roundBlockForMethod = 27, RULE_roundBlock = 28, RULE_bb = 29, RULE_curlyBlock = 30, 
		RULE_varDec = 31, RULE_d = 32, RULE_stringParse = 33, RULE_square = 34, 
		RULE_squareW = 35, RULE_mCall = 36, RULE_round = 37, RULE_ps = 38, RULE_k = 39, 
		RULE_on = 40, RULE_onPlus = 41, RULE_nudeE = 42, RULE_classBExtra = 43, 
		RULE_classBReuse = 44, RULE_classB = 45, RULE_mhs = 46, RULE_mht = 47, 
		RULE_member = 48, RULE_methodWithType = 49, RULE_methodImplemented = 50, 
		RULE_nestedClass = 51, RULE_header = 52, RULE_fieldDec = 53, RULE_w = 54, 
		RULE_wSwitch = 55, RULE_i = 56, RULE_wSimple = 57;
	public static final String[] ruleNames = {
		"m", "mDec", "path", "docs", "docsOpt", "t", "concreteT", "historicalSeq", 
		"historicalT", "methSelector", "x", "xOp", "eTopForMethod", "eTop", "eL3", 
		"eL2", "eL1", "numParse", "eUnOp", "ePost", "eAtom", "ifExpr", "using", 
		"whileExpr", "signalExpr", "loopExpr", "block", "roundBlockForMethod", 
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
		public TerminalNode X() { return getToken(L42Parser.X, 0); }
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
			setState(116);
			_la = _input.LA(1);
			if ( !(_la==X || _la==HashX) ) {
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
		public TerminalNode UnOp() { return getToken(L42Parser.UnOp, 0); }
		public TerminalNode DataOp() { return getToken(L42Parser.DataOp, 0); }
		public MContext m() {
			return getRuleContext(MContext.class,0);
		}
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
			setState(124);
			switch (_input.LA(1)) {
			case X:
			case HashX:
				enterOuterAlt(_localctx, 1);
				{
				setState(118); m();
				}
				break;
			case UnOp:
				enterOuterAlt(_localctx, 2);
				{
				setState(119); match(UnOp);
				}
				break;
			case EqOp:
				enterOuterAlt(_localctx, 3);
				{
				setState(120); match(EqOp);
				}
				break;
			case BoolOp:
				enterOuterAlt(_localctx, 4);
				{
				setState(121); match(BoolOp);
				}
				break;
			case RelOp:
				enterOuterAlt(_localctx, 5);
				{
				setState(122); match(RelOp);
				}
				break;
			case DataOp:
				enterOuterAlt(_localctx, 6);
				{
				setState(123); match(DataOp);
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
			setState(126); match(Path);
			}
		}
		catch (RecognitionException re) {
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
			setState(128); match(Doc);
			}
		}
		catch (RecognitionException re) {
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
			setState(131);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(130); match(Doc);
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
			setState(135);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(133); concreteT();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(134); historicalT();
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
			setState(138);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(137); match(Mdf);
				}
			}

			setState(140); match(Path);
			setState(142);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(141); match(Ph);
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
			setState(144); match(ClassSep);
			setState(145); methSelector();
			setState(148);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(146); match(ClassSep);
				setState(147); x();
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
		public TerminalNode Ph() { return getToken(L42Parser.Ph, 0); }
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
			setState(150); match(Path);
			setState(152); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(151); historicalSeq();
				}
				}
				setState(154); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ClassSep );
			setState(157);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(156); match(Ph);
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
			setState(160);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << X) | (1L << HashX) | (1L << UnOp) | (1L << EqOp) | (1L << BoolOp) | (1L << RelOp) | (1L << DataOp))) != 0)) {
				{
				setState(159); mDec();
				}
			}

			setState(162);
			_la = _input.LA(1);
			if ( !(_la==ORoundNoSpace || _la==ORoundSpace) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(163); x();
				}
				}
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(169); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
			setState(171); match(X);
			}
		}
		catch (RecognitionException re) {
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
			setState(173); match(X);
			setState(174); match(EqOp);
			setState(175); eTop();
			}
		}
		catch (RecognitionException re) {
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
			setState(191);
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
			case X:
			case UnOp:
			case DataOp:
			case NumParse:
				enterOuterAlt(_localctx, 1);
				{
				setState(177); eTop();
				}
				break;
			case ORoundNoSpace:
				enterOuterAlt(_localctx, 2);
				{
				setState(178); roundBlockForMethod();
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ORoundNoSpace) | (1L << OSquare) | (1L << Dot) | (1L << StringQuote) | (1L << Doc))) != 0)) {
					{
					setState(186);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						setState(179); squareW();
						}
						break;

					case 2:
						{
						setState(180); square();
						}
						break;

					case 3:
						{
						setState(181); match(Dot);
						setState(182); mCall();
						}
						break;

					case 4:
						{
						setState(183); round();
						}
						break;

					case 5:
						{
						setState(184); docs();
						}
						break;

					case 6:
						{
						setState(185); stringParse();
						}
						break;
					}
					}
					setState(190);
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
		public List<TerminalNode> RelOp() { return getTokens(L42Parser.RelOp); }
		public EL3Context eL3(int i) {
			return getRuleContext(EL3Context.class,i);
		}
		public TerminalNode RelOp(int i) {
			return getToken(L42Parser.RelOp, i);
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
		enterRule(_localctx, 26, RULE_eTop);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(193); eL3();
			setState(198);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(194); match(RelOp);
					setState(195); eL3();
					}
					} 
				}
				setState(200);
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

	public static class EL3Context extends ParserRuleContext {
		public TerminalNode BoolOp(int i) {
			return getToken(L42Parser.BoolOp, i);
		}
		public List<TerminalNode> BoolOp() { return getTokens(L42Parser.BoolOp); }
		public EL2Context eL2(int i) {
			return getRuleContext(EL2Context.class,i);
		}
		public List<EL2Context> eL2() {
			return getRuleContexts(EL2Context.class);
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
			setState(201); eL2();
			setState(206);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(202); match(BoolOp);
					setState(203); eL2();
					}
					} 
				}
				setState(208);
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
			setState(209); eL1();
			setState(214);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(210); match(DataOp);
					setState(211); eL1();
					}
					} 
				}
				setState(216);
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
			setState(217); eUnOp();
			}
		}
		catch (RecognitionException re) {
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
			setState(220);
			_la = _input.LA(1);
			if (_la==DataOp) {
				{
				setState(219); match(DataOp);
				}
			}

			setState(222); match(NumParse);
			}
		}
		catch (RecognitionException re) {
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
			setState(225);
			_la = _input.LA(1);
			if (_la==UnOp) {
				{
				setState(224); match(UnOp);
				}
			}

			setState(227); ePost();
			}
		}
		catch (RecognitionException re) {
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
			setState(229); eAtom();
			setState(239);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(237);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						setState(230); squareW();
						}
						break;

					case 2:
						{
						setState(231); square();
						}
						break;

					case 3:
						{
						setState(232); match(Dot);
						setState(233); mCall();
						}
						break;

					case 4:
						{
						setState(234); round();
						}
						break;

					case 5:
						{
						setState(235); docs();
						}
						break;

					case 6:
						{
						setState(236); stringParse();
						}
						break;
					}
					} 
				}
				setState(241);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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
			setState(265);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(242); classBReuse();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(243); classB();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(245);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(244); numParse();
					}
				}

				setState(247); x();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(248); xOp();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(250);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(249); numParse();
					}
				}

				setState(252); match(Path);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(254);
				_la = _input.LA(1);
				if (_la==DataOp || _la==NumParse) {
					{
					setState(253); numParse();
					}
				}

				setState(256); block();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(257); ifExpr();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(258); whileExpr();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(259); signalExpr();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(260); loopExpr();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(261); match(WalkBy);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(262); w();
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(263); using();
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(264); match(DotDotDot);
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
		enterRule(_localctx, 42, RULE_ifExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267); match(If);
			setState(268); eTop();
			setState(269); block();
			setState(272);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(270); match(Else);
				setState(271); eTop();
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
		enterRule(_localctx, 44, RULE_using);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274); match(Using);
			setState(275); match(Path);
			setState(276); match(Check);
			setState(277); mCall();
			setState(278); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 46, RULE_whileExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280); match(While);
			setState(281); eTop();
			setState(282); block();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 48, RULE_signalExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284); match(S);
			setState(285); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 50, RULE_loopExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(287); match(Loop);
			setState(288); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 52, RULE_block);
		try {
			setState(292);
			switch (_input.LA(1)) {
			case ORoundSpace:
				enterOuterAlt(_localctx, 1);
				{
				setState(290); roundBlock();
				}
				break;
			case OCurly:
				enterOuterAlt(_localctx, 2);
				{
				setState(291); curlyBlock();
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
		enterRule(_localctx, 54, RULE_roundBlockForMethod);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(294); match(ORoundNoSpace);
			setState(295); docsOpt();
			setState(299);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(296); bb();
					}
					} 
				}
				setState(301);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(302); eTop();
			setState(303); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 56, RULE_roundBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(305); match(ORoundSpace);
			setState(306); docsOpt();
			setState(310);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(307); bb();
					}
					} 
				}
				setState(312);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(313); eTop();
			setState(314); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 58, RULE_bb);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(317); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(316); d();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(319); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(322);
			_la = _input.LA(1);
			if (_la==Catch) {
				{
				setState(321); k();
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
		enterRule(_localctx, 60, RULE_curlyBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324); match(OCurly);
			setState(325); docsOpt();
			setState(327); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(326); bb();
				}
				}
				setState(329); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << S) | (1L << Mdf) | (1L << ORoundSpace) | (1L << OCurly) | (1L << DotDotDot) | (1L << If) | (1L << While) | (1L << Loop) | (1L << With) | (1L << Var) | (1L << Using) | (1L << WalkBy) | (1L << Path) | (1L << X) | (1L << UnOp) | (1L << DataOp) | (1L << NumParse))) != 0) );
			setState(331); match(CCurly);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 62, RULE_varDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(333); match(Var);
				}
			}

			setState(337);
			_la = _input.LA(1);
			if (_la==Mdf || _la==Path) {
				{
				setState(336); t();
				}
			}

			setState(339); x();
			setState(340); match(Equal);
			setState(341); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 64, RULE_d);
		try {
			setState(346);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(343); varDec();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(344); eTop();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(345); nestedClass();
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
		enterRule(_localctx, 66, RULE_stringParse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348); match(StringQuote);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 68, RULE_square);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(350); match(OSquare);
			setState(351); docsOpt();
			setState(358);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(352); ps();
					setState(353); match(Semicolon);
					setState(354); docsOpt();
					}
					} 
				}
				setState(360);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(361); ps();
			setState(362); match(CSquare);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 70, RULE_squareW);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364); match(OSquare);
			setState(365); docsOpt();
			setState(366); w();
			setState(367); match(CSquare);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 72, RULE_mCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369); m();
			setState(370); round();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode ORoundNoSpace() { return getToken(L42Parser.ORoundNoSpace, 0); }
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
		enterRule(_localctx, 74, RULE_round);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372); match(ORoundNoSpace);
			setState(373); docsOpt();
			setState(374); ps();
			setState(375); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 76, RULE_ps);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(377); eTop();
				}
				break;
			}
			setState(385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(380); match(X);
				setState(381); match(Colon);
				setState(382); eTop();
				}
				}
				setState(387);
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
		enterRule(_localctx, 78, RULE_k);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388); match(Catch);
			setState(389); match(S);
			setState(391);
			_la = _input.LA(1);
			if (_la==X) {
				{
				setState(390); match(X);
				}
			}

			setState(393);
			_la = _input.LA(1);
			if ( !(_la==ORoundNoSpace || _la==ORoundSpace) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==On) {
				{
				{
				setState(394); on();
				}
				}
				setState(399);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(402);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(400); match(Default);
				setState(401); eTop();
				}
			}

			setState(404); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 80, RULE_on);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406); match(On);
			setState(407); t();
			setState(410);
			_la = _input.LA(1);
			if (_la==Case) {
				{
				setState(408); match(Case);
				setState(409); eTop();
				}
			}

			setState(412); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 82, RULE_onPlus);
		int _la;
		try {
			int _alt;
			setState(430);
			switch (_input.LA(1)) {
			case On:
				enterOuterAlt(_localctx, 1);
				{
				setState(414); match(On);
				setState(416); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(415); t();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(418); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
				setState(422);
				_la = _input.LA(1);
				if (_la==Case) {
					{
					setState(420); match(Case);
					setState(421); eTop();
					}
				}

				setState(424); eTop();
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(426); match(Case);
				setState(427); eTop();
				setState(428); eTop();
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
		enterRule(_localctx, 84, RULE_nudeE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432); eTop();
			setState(433); match(EOF);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 86, RULE_classBExtra);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			_la = _input.LA(1);
			if (_la==Stage) {
				{
				setState(435); match(Stage);
				}
			}

			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Path) {
				{
				{
				setState(438); match(Path);
				}
				}
				setState(443);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(444); match(EndType);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 88, RULE_classBReuse);
		int _la;
		try {
			setState(462);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(446); match(OCurly);
				setState(447); docsOpt();
				setState(448); match(Url);
				setState(449); match(CCurly);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(451); match(OCurly);
				setState(452); docsOpt();
				setState(453); match(UrlNL);
				setState(454); docsOpt();
				setState(456); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(455); member();
					}
					}
					setState(458); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0) );
				setState(460); match(CCurly);
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
		enterRule(_localctx, 90, RULE_classB);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(464); match(OCurly);
			setState(465); docsOpt();
			setState(466); header();
			setState(468);
			_la = _input.LA(1);
			if (_la==Implements) {
				{
				setState(467); match(Implements);
				}
			}

			setState(473);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(470); match(Path);
					}
					} 
				}
				setState(475);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			}
			setState(476); docsOpt();
			setState(480);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Path))) != 0)) {
				{
				{
				setState(477); member();
				}
				}
				setState(482);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(483); match(CCurly);
			setState(485);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(484); classBExtra();
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
		enterRule(_localctx, 92, RULE_mhs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487); match(Method);
			setState(488); docsOpt();
			setState(489); methSelector();
			setState(493);
			_la = _input.LA(1);
			if (_la==EndType) {
				{
				setState(490); match(EndType);
				setState(491); match(Path);
				setState(492); mht();
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
		enterRule(_localctx, 94, RULE_mht);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(495); match(Mdf);
				}
			}

			setState(498); match(Method);
			setState(499); docsOpt();
			setState(500); t();
			setState(502);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << X) | (1L << HashX) | (1L << UnOp) | (1L << EqOp) | (1L << BoolOp) | (1L << RelOp) | (1L << DataOp))) != 0)) {
				{
				setState(501); mDec();
				}
			}

			setState(504);
			_la = _input.LA(1);
			if ( !(_la==ORoundNoSpace || _la==ORoundSpace) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Mdf || _la==Path) {
				{
				{
				setState(505); t();
				setState(506); x();
				setState(507); docsOpt();
				}
				}
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(514); match(CRound);
			setState(521);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(515); match(S);
				setState(517); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(516); match(Path);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(519); 
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
		enterRule(_localctx, 96, RULE_member);
		try {
			setState(526);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(523); methodWithType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(524); methodImplemented();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(525); nestedClass();
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
		enterRule(_localctx, 98, RULE_methodWithType);
		int _la;
		try {
			setState(540);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(528); mht();
				setState(531);
				_la = _input.LA(1);
				if (_la==EndType) {
					{
					setState(529); match(EndType);
					setState(530); match(Path);
					}
				}

				setState(533); docsOpt();
				setState(535);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(534); eTopForMethod();
					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(537); mht();
				setState(538); match(FieldSpecial);
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
		enterRule(_localctx, 100, RULE_methodImplemented);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(542); mhs();
			setState(543); eTopForMethod();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 102, RULE_nestedClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545); match(Path);
			setState(546); match(Colon);
			setState(547); docsOpt();
			setState(548); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 104, RULE_header);
		int _la;
		try {
			setState(566);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(551); match(Interface);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(553);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(552); match(Mdf);
					}
				}

				setState(556);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << X) | (1L << HashX) | (1L << UnOp) | (1L << EqOp) | (1L << BoolOp) | (1L << RelOp) | (1L << DataOp))) != 0)) {
					{
					setState(555); mDec();
					}
				}

				setState(558);
				_la = _input.LA(1);
				if ( !(_la==ORoundNoSpace || _la==ORoundSpace) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(562);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Var) | (1L << Path))) != 0)) {
					{
					{
					setState(559); fieldDec();
					}
					}
					setState(564);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(565); match(CRound);
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
		enterRule(_localctx, 106, RULE_fieldDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(569);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(568); match(Var);
				}
			}

			setState(571); t();
			setState(572); x();
			setState(573); docsOpt();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 108, RULE_w);
		try {
			setState(577);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(575); wSwitch();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(576); wSimple();
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
		enterRule(_localctx, 110, RULE_wSwitch);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(579); match(With);
			setState(583);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(580); x();
					}
					} 
				}
				setState(585);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
			}
			setState(589);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(586); i();
					}
					} 
				}
				setState(591);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			}
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Var) | (1L << Path) | (1L << X))) != 0)) {
				{
				{
				setState(592); varDec();
				}
				}
				setState(597);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(598); match(ORoundSpace);
			setState(600); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(599); onPlus();
				}
				}
				setState(602); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Case || _la==On );
			setState(606);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(604); match(Default);
				setState(605); eTop();
				}
			}

			setState(608); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 112, RULE_i);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(610); match(Var);
				}
			}

			setState(614);
			_la = _input.LA(1);
			if (_la==Mdf || _la==Path) {
				{
				setState(613); t();
				}
			}

			setState(616); x();
			setState(617); match(In);
			setState(618); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 114, RULE_wSimple);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620); match(With);
			setState(622); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(621); i();
				}
				}
				setState(624); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Var) | (1L << Path) | (1L << X))) != 0) );
			setState(626); block();
			}
		}
		catch (RecognitionException re) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\64\u0277\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3\177\n\3\3\4\3\4\3\5\3\5\3\6\5\6\u0086\n\6\3\7"+
		"\3\7\5\7\u008a\n\7\3\b\5\b\u008d\n\b\3\b\3\b\5\b\u0091\n\b\3\t\3\t\3\t"+
		"\3\t\5\t\u0097\n\t\3\n\3\n\6\n\u009b\n\n\r\n\16\n\u009c\3\n\5\n\u00a0"+
		"\n\n\3\13\5\13\u00a3\n\13\3\13\3\13\7\13\u00a7\n\13\f\13\16\13\u00aa\13"+
		"\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\7\16\u00bd\n\16\f\16\16\16\u00c0\13\16\5\16\u00c2\n\16\3"+
		"\17\3\17\3\17\7\17\u00c7\n\17\f\17\16\17\u00ca\13\17\3\20\3\20\3\20\7"+
		"\20\u00cf\n\20\f\20\16\20\u00d2\13\20\3\21\3\21\3\21\7\21\u00d7\n\21\f"+
		"\21\16\21\u00da\13\21\3\22\3\22\3\23\5\23\u00df\n\23\3\23\3\23\3\24\5"+
		"\24\u00e4\n\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\7\25"+
		"\u00f0\n\25\f\25\16\25\u00f3\13\25\3\26\3\26\3\26\5\26\u00f8\n\26\3\26"+
		"\3\26\3\26\5\26\u00fd\n\26\3\26\3\26\5\26\u0101\n\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\5\26\u010c\n\26\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u0113\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\34\3\34\5\34\u0127\n\34\3\35\3\35\3\35\7\35"+
		"\u012c\n\35\f\35\16\35\u012f\13\35\3\35\3\35\3\35\3\36\3\36\3\36\7\36"+
		"\u0137\n\36\f\36\16\36\u013a\13\36\3\36\3\36\3\36\3\37\6\37\u0140\n\37"+
		"\r\37\16\37\u0141\3\37\5\37\u0145\n\37\3 \3 \3 \6 \u014a\n \r \16 \u014b"+
		"\3 \3 \3!\5!\u0151\n!\3!\5!\u0154\n!\3!\3!\3!\3!\3\"\3\"\3\"\5\"\u015d"+
		"\n\"\3#\3#\3$\3$\3$\3$\3$\3$\7$\u0167\n$\f$\16$\u016a\13$\3$\3$\3$\3%"+
		"\3%\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\5(\u017d\n(\3(\3(\3(\7(\u0182"+
		"\n(\f(\16(\u0185\13(\3)\3)\3)\5)\u018a\n)\3)\3)\7)\u018e\n)\f)\16)\u0191"+
		"\13)\3)\3)\5)\u0195\n)\3)\3)\3*\3*\3*\3*\5*\u019d\n*\3*\3*\3+\3+\6+\u01a3"+
		"\n+\r+\16+\u01a4\3+\3+\5+\u01a9\n+\3+\3+\3+\3+\3+\3+\5+\u01b1\n+\3,\3"+
		",\3,\3-\5-\u01b7\n-\3-\7-\u01ba\n-\f-\16-\u01bd\13-\3-\3-\3.\3.\3.\3."+
		"\3.\3.\3.\3.\3.\3.\6.\u01cb\n.\r.\16.\u01cc\3.\3.\5.\u01d1\n.\3/\3/\3"+
		"/\3/\5/\u01d7\n/\3/\7/\u01da\n/\f/\16/\u01dd\13/\3/\3/\7/\u01e1\n/\f/"+
		"\16/\u01e4\13/\3/\3/\5/\u01e8\n/\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u01f0"+
		"\n\60\3\61\5\61\u01f3\n\61\3\61\3\61\3\61\3\61\5\61\u01f9\n\61\3\61\3"+
		"\61\3\61\3\61\3\61\7\61\u0200\n\61\f\61\16\61\u0203\13\61\3\61\3\61\3"+
		"\61\6\61\u0208\n\61\r\61\16\61\u0209\5\61\u020c\n\61\3\62\3\62\3\62\5"+
		"\62\u0211\n\62\3\63\3\63\3\63\5\63\u0216\n\63\3\63\3\63\5\63\u021a\n\63"+
		"\3\63\3\63\3\63\5\63\u021f\n\63\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65"+
		"\3\66\3\66\3\66\5\66\u022c\n\66\3\66\5\66\u022f\n\66\3\66\3\66\7\66\u0233"+
		"\n\66\f\66\16\66\u0236\13\66\3\66\5\66\u0239\n\66\3\67\5\67\u023c\n\67"+
		"\3\67\3\67\3\67\3\67\38\38\58\u0244\n8\39\39\79\u0248\n9\f9\169\u024b"+
		"\139\39\79\u024e\n9\f9\169\u0251\139\39\79\u0254\n9\f9\169\u0257\139\3"+
		"9\39\69\u025b\n9\r9\169\u025c\39\39\59\u0261\n9\39\39\3:\5:\u0266\n:\3"+
		":\5:\u0269\n:\3:\3:\3:\3:\3;\3;\6;\u0271\n;\r;\16;\u0272\3;\3;\3;\2\2"+
		"<\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprt\2\4\3\2()\3\2\5\6\u02a3\2v\3\2\2\2\4~\3\2\2\2"+
		"\6\u0080\3\2\2\2\b\u0082\3\2\2\2\n\u0085\3\2\2\2\f\u0089\3\2\2\2\16\u008c"+
		"\3\2\2\2\20\u0092\3\2\2\2\22\u0098\3\2\2\2\24\u00a2\3\2\2\2\26\u00ad\3"+
		"\2\2\2\30\u00af\3\2\2\2\32\u00c1\3\2\2\2\34\u00c3\3\2\2\2\36\u00cb\3\2"+
		"\2\2 \u00d3\3\2\2\2\"\u00db\3\2\2\2$\u00de\3\2\2\2&\u00e3\3\2\2\2(\u00e7"+
		"\3\2\2\2*\u010b\3\2\2\2,\u010d\3\2\2\2.\u0114\3\2\2\2\60\u011a\3\2\2\2"+
		"\62\u011e\3\2\2\2\64\u0121\3\2\2\2\66\u0126\3\2\2\28\u0128\3\2\2\2:\u0133"+
		"\3\2\2\2<\u013f\3\2\2\2>\u0146\3\2\2\2@\u0150\3\2\2\2B\u015c\3\2\2\2D"+
		"\u015e\3\2\2\2F\u0160\3\2\2\2H\u016e\3\2\2\2J\u0173\3\2\2\2L\u0176\3\2"+
		"\2\2N\u017c\3\2\2\2P\u0186\3\2\2\2R\u0198\3\2\2\2T\u01b0\3\2\2\2V\u01b2"+
		"\3\2\2\2X\u01b6\3\2\2\2Z\u01d0\3\2\2\2\\\u01d2\3\2\2\2^\u01e9\3\2\2\2"+
		"`\u01f2\3\2\2\2b\u0210\3\2\2\2d\u021e\3\2\2\2f\u0220\3\2\2\2h\u0223\3"+
		"\2\2\2j\u0238\3\2\2\2l\u023b\3\2\2\2n\u0243\3\2\2\2p\u0245\3\2\2\2r\u0265"+
		"\3\2\2\2t\u026e\3\2\2\2vw\t\2\2\2w\3\3\2\2\2x\177\5\2\2\2y\177\7/\2\2"+
		"z\177\7\60\2\2{\177\7\61\2\2|\177\7\62\2\2}\177\7\63\2\2~x\3\2\2\2~y\3"+
		"\2\2\2~z\3\2\2\2~{\3\2\2\2~|\3\2\2\2~}\3\2\2\2\177\5\3\2\2\2\u0080\u0081"+
		"\7&\2\2\u0081\7\3\2\2\2\u0082\u0083\7-\2\2\u0083\t\3\2\2\2\u0084\u0086"+
		"\7-\2\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\13\3\2\2\2\u0087"+
		"\u008a\5\16\b\2\u0088\u008a\5\22\n\2\u0089\u0087\3\2\2\2\u0089\u0088\3"+
		"\2\2\2\u008a\r\3\2\2\2\u008b\u008d\7\4\2\2\u008c\u008b\3\2\2\2\u008c\u008d"+
		"\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0090\7&\2\2\u008f\u0091\7\22\2\2\u0090"+
		"\u008f\3\2\2\2\u0090\u0091\3\2\2\2\u0091\17\3\2\2\2\u0092\u0093\7\'\2"+
		"\2\u0093\u0096\5\24\13\2\u0094\u0095\7\'\2\2\u0095\u0097\5\26\f\2\u0096"+
		"\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\21\3\2\2\2\u0098\u009a\7&\2\2"+
		"\u0099\u009b\5\20\t\2\u009a\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009a"+
		"\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009f\3\2\2\2\u009e\u00a0\7\22\2\2"+
		"\u009f\u009e\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\23\3\2\2\2\u00a1\u00a3"+
		"\5\4\3\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a8\t\3\2\2\u00a5\u00a7\5\26\f\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa\3"+
		"\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ab\3\2\2\2\u00aa"+
		"\u00a8\3\2\2\2\u00ab\u00ac\7\7\2\2\u00ac\25\3\2\2\2\u00ad\u00ae\7(\2\2"+
		"\u00ae\27\3\2\2\2\u00af\u00b0\7(\2\2\u00b0\u00b1\7\60\2\2\u00b1\u00b2"+
		"\5\34\17\2\u00b2\31\3\2\2\2\u00b3\u00c2\5\34\17\2\u00b4\u00be\58\35\2"+
		"\u00b5\u00bd\5H%\2\u00b6\u00bd\5F$\2\u00b7\u00b8\7\20\2\2\u00b8\u00bd"+
		"\5J&\2\u00b9\u00bd\5L\'\2\u00ba\u00bd\5\b\5\2\u00bb\u00bd\5D#\2\u00bc"+
		"\u00b5\3\2\2\2\u00bc\u00b6\3\2\2\2\u00bc\u00b7\3\2\2\2\u00bc\u00b9\3\2"+
		"\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be"+
		"\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2"+
		"\2\2\u00c1\u00b3\3\2\2\2\u00c1\u00b4\3\2\2\2\u00c2\33\3\2\2\2\u00c3\u00c8"+
		"\5\36\20\2\u00c4\u00c5\7\62\2\2\u00c5\u00c7\5\36\20\2\u00c6\u00c4\3\2"+
		"\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"+
		"\35\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00d0\5 \21\2\u00cc\u00cd\7\61\2"+
		"\2\u00cd\u00cf\5 \21\2\u00ce\u00cc\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce"+
		"\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\37\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3"+
		"\u00d8\5\"\22\2\u00d4\u00d5\7\63\2\2\u00d5\u00d7\5\"\22\2\u00d6\u00d4"+
		"\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"!\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\5&\24\2\u00dc#\3\2\2\2\u00dd"+
		"\u00df\7\63\2\2\u00de\u00dd\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00e0\3"+
		"\2\2\2\u00e0\u00e1\7\64\2\2\u00e1%\3\2\2\2\u00e2\u00e4\7/\2\2\u00e3\u00e2"+
		"\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e6\5(\25\2\u00e6"+
		"\'\3\2\2\2\u00e7\u00f1\5*\26\2\u00e8\u00f0\5H%\2\u00e9\u00f0\5F$\2\u00ea"+
		"\u00eb\7\20\2\2\u00eb\u00f0\5J&\2\u00ec\u00f0\5L\'\2\u00ed\u00f0\5\b\5"+
		"\2\u00ee\u00f0\5D#\2\u00ef\u00e8\3\2\2\2\u00ef\u00e9\3\2\2\2\u00ef\u00ea"+
		"\3\2\2\2\u00ef\u00ec\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00ee\3\2\2\2\u00f0"+
		"\u00f3\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2)\3\2\2\2"+
		"\u00f3\u00f1\3\2\2\2\u00f4\u010c\5Z.\2\u00f5\u010c\5\\/\2\u00f6\u00f8"+
		"\5$\23\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9"+
		"\u010c\5\26\f\2\u00fa\u010c\5\30\r\2\u00fb\u00fd\5$\23\2\u00fc\u00fb\3"+
		"\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u010c\7&\2\2\u00ff"+
		"\u0101\5$\23\2\u0100\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0102\3\2"+
		"\2\2\u0102\u010c\5\66\34\2\u0103\u010c\5,\27\2\u0104\u010c\5\60\31\2\u0105"+
		"\u010c\5\62\32\2\u0106\u010c\5\64\33\2\u0107\u010c\7$\2\2\u0108\u010c"+
		"\5n8\2\u0109\u010c\5.\30\2\u010a\u010c\7\13\2\2\u010b\u00f4\3\2\2\2\u010b"+
		"\u00f5\3\2\2\2\u010b\u00f7\3\2\2\2\u010b\u00fa\3\2\2\2\u010b\u00fc\3\2"+
		"\2\2\u010b\u0100\3\2\2\2\u010b\u0103\3\2\2\2\u010b\u0104\3\2\2\2\u010b"+
		"\u0105\3\2\2\2\u010b\u0106\3\2\2\2\u010b\u0107\3\2\2\2\u010b\u0108\3\2"+
		"\2\2\u010b\u0109\3\2\2\2\u010b\u010a\3\2\2\2\u010c+\3\2\2\2\u010d\u010e"+
		"\7\25\2\2\u010e\u010f\5\34\17\2\u010f\u0112\5\66\34\2\u0110\u0111\7\26"+
		"\2\2\u0111\u0113\5\34\17\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113"+
		"-\3\2\2\2\u0114\u0115\7!\2\2\u0115\u0116\7&\2\2\u0116\u0117\7\"\2\2\u0117"+
		"\u0118\5J&\2\u0118\u0119\5\34\17\2\u0119/\3\2\2\2\u011a\u011b\7\27\2\2"+
		"\u011b\u011c\5\34\17\2\u011c\u011d\5\66\34\2\u011d\61\3\2\2\2\u011e\u011f"+
		"\7\3\2\2\u011f\u0120\5\34\17\2\u0120\63\3\2\2\2\u0121\u0122\7\30\2\2\u0122"+
		"\u0123\5\34\17\2\u0123\65\3\2\2\2\u0124\u0127\5:\36\2\u0125\u0127\5> "+
		"\2\u0126\u0124\3\2\2\2\u0126\u0125\3\2\2\2\u0127\67\3\2\2\2\u0128\u0129"+
		"\7\5\2\2\u0129\u012d\5\n\6\2\u012a\u012c\5<\37\2\u012b\u012a\3\2\2\2\u012c"+
		"\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e\u0130\3\2"+
		"\2\2\u012f\u012d\3\2\2\2\u0130\u0131\5\34\17\2\u0131\u0132\7\7\2\2\u0132"+
		"9\3\2\2\2\u0133\u0134\7\6\2\2\u0134\u0138\5\n\6\2\u0135\u0137\5<\37\2"+
		"\u0136\u0135\3\2\2\2\u0137\u013a\3\2\2\2\u0138\u0136\3\2\2\2\u0138\u0139"+
		"\3\2\2\2\u0139\u013b\3\2\2\2\u013a\u0138\3\2\2\2\u013b\u013c\5\34\17\2"+
		"\u013c\u013d\7\7\2\2\u013d;\3\2\2\2\u013e\u0140\5B\"\2\u013f\u013e\3\2"+
		"\2\2\u0140\u0141\3\2\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142"+
		"\u0144\3\2\2\2\u0143\u0145\5P)\2\u0144\u0143\3\2\2\2\u0144\u0145\3\2\2"+
		"\2\u0145=\3\2\2\2\u0146\u0147\7\n\2\2\u0147\u0149\5\n\6\2\u0148\u014a"+
		"\5<\37\2\u0149\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u0149\3\2\2\2\u014b"+
		"\u014c\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014e\7\r\2\2\u014e?\3\2\2\2"+
		"\u014f\u0151\7\35\2\2\u0150\u014f\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0153"+
		"\3\2\2\2\u0152\u0154\5\f\7\2\u0153\u0152\3\2\2\2\u0153\u0154\3\2\2\2\u0154"+
		"\u0155\3\2\2\2\u0155\u0156\5\26\f\2\u0156\u0157\7\21\2\2\u0157\u0158\5"+
		"\34\17\2\u0158A\3\2\2\2\u0159\u015d\5@!\2\u015a\u015d\5\34\17\2\u015b"+
		"\u015d\5h\65\2\u015c\u0159\3\2\2\2\u015c\u015a\3\2\2\2\u015c\u015b\3\2"+
		"\2\2\u015dC\3\2\2\2\u015e\u015f\7*\2\2\u015fE\3\2\2\2\u0160\u0161\7\b"+
		"\2\2\u0161\u0168\5\n\6\2\u0162\u0163\5N(\2\u0163\u0164\7\17\2\2\u0164"+
		"\u0165\5\n\6\2\u0165\u0167\3\2\2\2\u0166\u0162\3\2\2\2\u0167\u016a\3\2"+
		"\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016b\3\2\2\2\u016a"+
		"\u0168\3\2\2\2\u016b\u016c\5N(\2\u016c\u016d\7\t\2\2\u016dG\3\2\2\2\u016e"+
		"\u016f\7\b\2\2\u016f\u0170\5\n\6\2\u0170\u0171\5n8\2\u0171\u0172\7\t\2"+
		"\2\u0172I\3\2\2\2\u0173\u0174\5\2\2\2\u0174\u0175\5L\'\2\u0175K\3\2\2"+
		"\2\u0176\u0177\7\5\2\2\u0177\u0178\5\n\6\2\u0178\u0179\5N(\2\u0179\u017a"+
		"\7\7\2\2\u017aM\3\2\2\2\u017b\u017d\5\34\17\2\u017c\u017b\3\2\2\2\u017c"+
		"\u017d\3\2\2\2\u017d\u0183\3\2\2\2\u017e\u017f\7(\2\2\u017f\u0180\7\16"+
		"\2\2\u0180\u0182\5\34\17\2\u0181\u017e\3\2\2\2\u0182\u0185\3\2\2\2\u0183"+
		"\u0181\3\2\2\2\u0183\u0184\3\2\2\2\u0184O\3\2\2\2\u0185\u0183\3\2\2\2"+
		"\u0186\u0187\7\34\2\2\u0187\u0189\7\3\2\2\u0188\u018a\7(\2\2\u0189\u0188"+
		"\3\2\2\2\u0189\u018a\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u018f\t\3\2\2\u018c"+
		"\u018e\5R*\2\u018d\u018c\3\2\2\2\u018e\u0191\3\2\2\2\u018f\u018d\3\2\2"+
		"\2\u018f\u0190\3\2\2\2\u0190\u0194\3\2\2\2\u0191\u018f\3\2\2\2\u0192\u0193"+
		"\7\36\2\2\u0193\u0195\5\34\17\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2\2"+
		"\2\u0195\u0196\3\2\2\2\u0196\u0197\7\7\2\2\u0197Q\3\2\2\2\u0198\u0199"+
		"\7\32\2\2\u0199\u019c\5\f\7\2\u019a\u019b\7\24\2\2\u019b\u019d\5\34\17"+
		"\2\u019c\u019a\3\2\2\2\u019c\u019d\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u019f"+
		"\5\34\17\2\u019fS\3\2\2\2\u01a0\u01a2\7\32\2\2\u01a1\u01a3\5\f\7\2\u01a2"+
		"\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a4\u01a5\3\2"+
		"\2\2\u01a5\u01a8\3\2\2\2\u01a6\u01a7\7\24\2\2\u01a7\u01a9\5\34\17\2\u01a8"+
		"\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\5\34"+
		"\17\2\u01ab\u01b1\3\2\2\2\u01ac\u01ad\7\24\2\2\u01ad\u01ae\5\34\17\2\u01ae"+
		"\u01af\5\34\17\2\u01af\u01b1\3\2\2\2\u01b0\u01a0\3\2\2\2\u01b0\u01ac\3"+
		"\2\2\2\u01b1U\3\2\2\2\u01b2\u01b3\5\34\17\2\u01b3\u01b4\7\2\2\3\u01b4"+
		"W\3\2\2\2\u01b5\u01b7\7%\2\2\u01b6\u01b5\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"\u01bb\3\2\2\2\u01b8\u01ba\7&\2\2\u01b9\u01b8\3\2\2\2\u01ba\u01bd\3\2"+
		"\2\2\u01bb\u01b9\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01be\3\2\2\2\u01bd"+
		"\u01bb\3\2\2\2\u01be\u01bf\7\f\2\2\u01bfY\3\2\2\2\u01c0\u01c1\7\n\2\2"+
		"\u01c1\u01c2\5\n\6\2\u01c2\u01c3\7,\2\2\u01c3\u01c4\7\r\2\2\u01c4\u01d1"+
		"\3\2\2\2\u01c5\u01c6\7\n\2\2\u01c6\u01c7\5\n\6\2\u01c7\u01c8\7+\2\2\u01c8"+
		"\u01ca\5\n\6\2\u01c9\u01cb\5b\62\2\u01ca\u01c9\3\2\2\2\u01cb\u01cc\3\2"+
		"\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce"+
		"\u01cf\7\r\2\2\u01cf\u01d1\3\2\2\2\u01d0\u01c0\3\2\2\2\u01d0\u01c5\3\2"+
		"\2\2\u01d1[\3\2\2\2\u01d2\u01d3\7\n\2\2\u01d3\u01d4\5\n\6\2\u01d4\u01d6"+
		"\5j\66\2\u01d5\u01d7\7\23\2\2\u01d6\u01d5\3\2\2\2\u01d6\u01d7\3\2\2\2"+
		"\u01d7\u01db\3\2\2\2\u01d8\u01da\7&\2\2\u01d9\u01d8\3\2\2\2\u01da\u01dd"+
		"\3\2\2\2\u01db\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01de\3\2\2\2\u01dd"+
		"\u01db\3\2\2\2\u01de\u01e2\5\n\6\2\u01df\u01e1\5b\62\2\u01e0\u01df\3\2"+
		"\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3"+
		"\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e7\7\r\2\2\u01e6\u01e8\5X"+
		"-\2\u01e7\u01e6\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8]\3\2\2\2\u01e9\u01ea"+
		"\7 \2\2\u01ea\u01eb\5\n\6\2\u01eb\u01ef\5\24\13\2\u01ec\u01ed\7\f\2\2"+
		"\u01ed\u01ee\7&\2\2\u01ee\u01f0\5`\61\2\u01ef\u01ec\3\2\2\2\u01ef\u01f0"+
		"\3\2\2\2\u01f0_\3\2\2\2\u01f1\u01f3\7\4\2\2\u01f2\u01f1\3\2\2\2\u01f2"+
		"\u01f3\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\7 \2\2\u01f5\u01f6\5\n"+
		"\6\2\u01f6\u01f8\5\f\7\2\u01f7\u01f9\5\4\3\2\u01f8\u01f7\3\2\2\2\u01f8"+
		"\u01f9\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa\u0201\t\3\2\2\u01fb\u01fc\5\f"+
		"\7\2\u01fc\u01fd\5\26\f\2\u01fd\u01fe\5\n\6\2\u01fe\u0200\3\2\2\2\u01ff"+
		"\u01fb\3\2\2\2\u0200\u0203\3\2\2\2\u0201\u01ff\3\2\2\2\u0201\u0202\3\2"+
		"\2\2\u0202\u0204\3\2\2\2\u0203\u0201\3\2\2\2\u0204\u020b\7\7\2\2\u0205"+
		"\u0207\7\3\2\2\u0206\u0208\7&\2\2\u0207\u0206\3\2\2\2\u0208\u0209\3\2"+
		"\2\2\u0209\u0207\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020c\3\2\2\2\u020b"+
		"\u0205\3\2\2\2\u020b\u020c\3\2\2\2\u020ca\3\2\2\2\u020d\u0211\5d\63\2"+
		"\u020e\u0211\5f\64\2\u020f\u0211\5h\65\2\u0210\u020d\3\2\2\2\u0210\u020e"+
		"\3\2\2\2\u0210\u020f\3\2\2\2\u0211c\3\2\2\2\u0212\u0215\5`\61\2\u0213"+
		"\u0214\7\f\2\2\u0214\u0216\7&\2\2\u0215\u0213\3\2\2\2\u0215\u0216\3\2"+
		"\2\2\u0216\u0217\3\2\2\2\u0217\u0219\5\n\6\2\u0218\u021a\5\32\16\2\u0219"+
		"\u0218\3\2\2\2\u0219\u021a\3\2\2\2\u021a\u021f\3\2\2\2\u021b\u021c\5`"+
		"\61\2\u021c\u021d\7#\2\2\u021d\u021f\3\2\2\2\u021e\u0212\3\2\2\2\u021e"+
		"\u021b\3\2\2\2\u021fe\3\2\2\2\u0220\u0221\5^\60\2\u0221\u0222\5\32\16"+
		"\2\u0222g\3\2\2\2\u0223\u0224\7&\2\2\u0224\u0225\7\16\2\2\u0225\u0226"+
		"\5\n\6\2\u0226\u0227\5\34\17\2\u0227i\3\2\2\2\u0228\u0239\3\2\2\2\u0229"+
		"\u0239\7\37\2\2\u022a\u022c\7\4\2\2\u022b\u022a\3\2\2\2\u022b\u022c\3"+
		"\2\2\2\u022c\u022e\3\2\2\2\u022d\u022f\5\4\3\2\u022e\u022d\3\2\2\2\u022e"+
		"\u022f\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u0234\t\3\2\2\u0231\u0233\5l"+
		"\67\2\u0232\u0231\3\2\2\2\u0233\u0236\3\2\2\2\u0234\u0232\3\2\2\2\u0234"+
		"\u0235\3\2\2\2\u0235\u0237\3\2\2\2\u0236\u0234\3\2\2\2\u0237\u0239\7\7"+
		"\2\2\u0238\u0228\3\2\2\2\u0238\u0229\3\2\2\2\u0238\u022b\3\2\2\2\u0239"+
		"k\3\2\2\2\u023a\u023c\7\35\2\2\u023b\u023a\3\2\2\2\u023b\u023c\3\2\2\2"+
		"\u023c\u023d\3\2\2\2\u023d\u023e\5\f\7\2\u023e\u023f\5\26\f\2\u023f\u0240"+
		"\5\n\6\2\u0240m\3\2\2\2\u0241\u0244\5p9\2\u0242\u0244\5t;\2\u0243\u0241"+
		"\3\2\2\2\u0243\u0242\3\2\2\2\u0244o\3\2\2\2\u0245\u0249\7\31\2\2\u0246"+
		"\u0248\5\26\f\2\u0247\u0246\3\2\2\2\u0248\u024b\3\2\2\2\u0249\u0247\3"+
		"\2\2\2\u0249\u024a\3\2\2\2\u024a\u024f\3\2\2\2\u024b\u0249\3\2\2\2\u024c"+
		"\u024e\5r:\2\u024d\u024c\3\2\2\2\u024e\u0251\3\2\2\2\u024f\u024d\3\2\2"+
		"\2\u024f\u0250\3\2\2\2\u0250\u0255\3\2\2\2\u0251\u024f\3\2\2\2\u0252\u0254"+
		"\5@!\2\u0253\u0252\3\2\2\2\u0254\u0257\3\2\2\2\u0255\u0253\3\2\2\2\u0255"+
		"\u0256\3\2\2\2\u0256\u0258\3\2\2\2\u0257\u0255\3\2\2\2\u0258\u025a\7\6"+
		"\2\2\u0259\u025b\5T+\2\u025a\u0259\3\2\2\2\u025b\u025c\3\2\2\2\u025c\u025a"+
		"\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u0260\3\2\2\2\u025e\u025f\7\36\2\2"+
		"\u025f\u0261\5\34\17\2\u0260\u025e\3\2\2\2\u0260\u0261\3\2\2\2\u0261\u0262"+
		"\3\2\2\2\u0262\u0263\7\7\2\2\u0263q\3\2\2\2\u0264\u0266\7\35\2\2\u0265"+
		"\u0264\3\2\2\2\u0265\u0266\3\2\2\2\u0266\u0268\3\2\2\2\u0267\u0269\5\f"+
		"\7\2\u0268\u0267\3\2\2\2\u0268\u0269\3\2\2\2\u0269\u026a\3\2\2\2\u026a"+
		"\u026b\5\26\f\2\u026b\u026c\7\33\2\2\u026c\u026d\5\34\17\2\u026ds\3\2"+
		"\2\2\u026e\u0270\7\31\2\2\u026f\u0271\5r:\2\u0270\u026f\3\2\2\2\u0271"+
		"\u0272\3\2\2\2\u0272\u0270\3\2\2\2\u0272\u0273\3\2\2\2\u0273\u0274\3\2"+
		"\2\2\u0274\u0275\5\66\34\2\u0275u\3\2\2\2N~\u0085\u0089\u008c\u0090\u0096"+
		"\u009c\u009f\u00a2\u00a8\u00bc\u00be\u00c1\u00c8\u00d0\u00d8\u00de\u00e3"+
		"\u00ef\u00f1\u00f7\u00fc\u0100\u010b\u0112\u0126\u012d\u0138\u0141\u0144"+
		"\u014b\u0150\u0153\u015c\u0168\u017c\u0183\u0189\u018f\u0194\u019c\u01a4"+
		"\u01a8\u01b0\u01b6\u01bb\u01cc\u01d0\u01d6\u01db\u01e2\u01e7\u01ef\u01f2"+
		"\u01f8\u0201\u0209\u020b\u0210\u0215\u0219\u021e\u022b\u022e\u0234\u0238"+
		"\u023b\u0243\u0249\u024f\u0255\u025c\u0260\u0265\u0268\u0272";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}