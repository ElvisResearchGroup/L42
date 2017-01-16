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
		Method=30, Using=31, Check=32, Refine=33, FieldSpecial=34, WalkBy=35, 
		Stage=36, Path=37, ClassSep=38, ClassMethSep=39, MX=40, X=41, HashX=42, 
		ContextId=43, StringQuote=44, UrlNL=45, Url=46, Doc=47, WS=48, UnOp=49, 
		EqOp=50, BoolOp=51, RelOp=52, DataOp=53, NumParse=54;
	public static final String[] tokenNames = {
		"<INVALID>", "S", "Mdf", "'('", "'\t'", "')'", "'['", "']'", "'{'", "'...'", 
		"'^##'", "'}'", "':'", "';'", "Dot", "'='", "'fwd'", "'implements'", "'case'", 
		"'if'", "'else'", "'while'", "'loop'", "'with'", "'on'", "'in'", "'catch'", 
		"'var'", "'default'", "'interface'", "'method'", "'use'", "'check'", "'refine'", 
		"'##field'", "'##walkBy'", "Stage", "Path", "ClassSep", "'::'", "MX", 
		"X", "HashX", "ContextId", "StringQuote", "UrlNL", "Url", "Doc", "WS", 
		"UnOp", "EqOp", "BoolOp", "RelOp", "DataOp", "NumParse"
	};
	public static final int
		RULE_m = 0, RULE_mDec = 1, RULE_path = 2, RULE_docs = 3, RULE_docsOpt = 4, 
		RULE_t = 5, RULE_concreteT = 6, RULE_historicalSeq = 7, RULE_historicalT = 8, 
		RULE_methSelector = 9, RULE_x = 10, RULE_xOp = 11, RULE_eTopForMethod = 12, 
		RULE_eTop = 13, RULE_eL3 = 14, RULE_eL2 = 15, RULE_eL1 = 16, RULE_numParse = 17, 
		RULE_eUnOp = 18, RULE_ePost = 19, RULE_eAtom = 20, RULE_mxRound = 21, 
		RULE_contextId = 22, RULE_useSquare = 23, RULE_ifExpr = 24, RULE_using = 25, 
		RULE_whileExpr = 26, RULE_signalExpr = 27, RULE_loopExpr = 28, RULE_block = 29, 
		RULE_roundBlockForMethod = 30, RULE_roundBlock = 31, RULE_bb = 32, RULE_curlyBlock = 33, 
		RULE_varDec = 34, RULE_d = 35, RULE_stringParse = 36, RULE_square = 37, 
		RULE_squareW = 38, RULE_mCall = 39, RULE_round = 40, RULE_ps = 41, RULE_k1 = 42, 
		RULE_kMany = 43, RULE_kProp = 44, RULE_k = 45, RULE_ks = 46, RULE_on = 47, 
		RULE_onPlus = 48, RULE_nudeE = 49, RULE_classBExtra = 50, RULE_classBReuse = 51, 
		RULE_classB = 52, RULE_impls = 53, RULE_mhs = 54, RULE_mht = 55, RULE_member = 56, 
		RULE_methodWithType = 57, RULE_methodImplemented = 58, RULE_nestedClass = 59, 
		RULE_header = 60, RULE_fieldDec = 61, RULE_w = 62, RULE_wSwitch = 63, 
		RULE_i = 64, RULE_wSimple = 65;
	public static final String[] ruleNames = {
		"m", "mDec", "path", "docs", "docsOpt", "t", "concreteT", "historicalSeq", 
		"historicalT", "methSelector", "x", "xOp", "eTopForMethod", "eTop", "eL3", 
		"eL2", "eL1", "numParse", "eUnOp", "ePost", "eAtom", "mxRound", "contextId", 
		"useSquare", "ifExpr", "using", "whileExpr", "signalExpr", "loopExpr", 
		"block", "roundBlockForMethod", "roundBlock", "bb", "curlyBlock", "varDec", 
		"d", "stringParse", "square", "squareW", "mCall", "round", "ps", "k1", 
		"kMany", "kProp", "k", "ks", "on", "onPlus", "nudeE", "classBExtra", "classBReuse", 
		"classB", "impls", "mhs", "mht", "member", "methodWithType", "methodImplemented", 
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
			setState(132);
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
			setState(155);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(134); m();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(135); match(UnOp);
				setState(136); match(ORoundNoSpace);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(137); match(EqOp);
				setState(138); match(ORoundNoSpace);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(139); match(BoolOp);
				setState(140); match(ORoundNoSpace);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(141); match(RelOp);
				setState(142); match(ORoundNoSpace);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(143); match(DataOp);
				setState(144); match(ORoundNoSpace);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(145); match(UnOp);
				setState(146); match(ORoundSpace);
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(147); match(EqOp);
				setState(148); match(ORoundSpace);
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(149); match(BoolOp);
				setState(150); match(ORoundSpace);
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(151); match(RelOp);
				setState(152); match(ORoundSpace);
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(153); match(DataOp);
				setState(154); match(ORoundSpace);
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
			setState(157); match(Path);
			}
		}
		catch (RecognitionException re) {
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
			setState(159); match(Doc);
			}
		}
		catch (RecognitionException re) {
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
			setState(162);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(161); match(Doc);
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
			setState(166);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164); concreteT();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165); historicalT();
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
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
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
			setState(169);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(168); match(Ph);
				}
			}

			setState(172);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(171); match(Mdf);
				}
			}

			setState(174); match(Path);
			setState(175); docsOpt();
			}
		}
		catch (RecognitionException re) {
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
		public XContext x() {
			return getRuleContext(XContext.class,0);
		}
		public List<TerminalNode> ClassMethSep() { return getTokens(L42Parser.ClassMethSep); }
		public TerminalNode ClassMethSep(int i) {
			return getToken(L42Parser.ClassMethSep, i);
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
			setState(177); match(ClassMethSep);
			setState(178); methSelector();
			setState(181);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(179); match(ClassMethSep);
				setState(180); x();
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
		public TerminalNode Ph() { return getToken(L42Parser.Ph, 0); }
		public List<HistoricalSeqContext> historicalSeq() {
			return getRuleContexts(HistoricalSeqContext.class);
		}
		public HistoricalSeqContext historicalSeq(int i) {
			return getRuleContext(HistoricalSeqContext.class,i);
		}
		public TerminalNode Path() { return getToken(L42Parser.Path, 0); }
		public DocsOptContext docsOpt() {
			return getRuleContext(DocsOptContext.class,0);
		}
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
			setState(184);
			_la = _input.LA(1);
			if (_la==Ph) {
				{
				setState(183); match(Ph);
				}
			}

			setState(186); match(Path);
			setState(187); docsOpt();
			setState(189); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(188); historicalSeq();
				}
				}
				setState(191); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ClassMethSep );
			}
		}
		catch (RecognitionException re) {
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
			setState(196);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(193); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(194); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(195); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(198); x();
				}
				}
				setState(203);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(204); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
			setState(206); match(X);
			}
		}
		catch (RecognitionException re) {
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
			setState(208); match(X);
			setState(209); match(EqOp);
			setState(210); eTop();
			}
		}
		catch (RecognitionException re) {
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
			setState(227);
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
			case ContextId:
			case UnOp:
			case NumParse:
				enterOuterAlt(_localctx, 1);
				{
				setState(212); eTop();
				}
				break;
			case ORoundNoSpace:
				enterOuterAlt(_localctx, 2);
				{
				setState(213); roundBlockForMethod();
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ORoundNoSpace) | (1L << OSquare) | (1L << Dot) | (1L << StringQuote) | (1L << Doc))) != 0)) {
					{
					setState(222);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						setState(214); squareW();
						}
						break;

					case 2:
						{
						setState(215); square();
						}
						break;

					case 3:
						{
						setState(216); match(Dot);
						setState(217); mCall();
						}
						break;

					case 4:
						{
						setState(218); match(ORoundNoSpace);
						setState(219); round();
						}
						break;

					case 5:
						{
						setState(220); docs();
						}
						break;

					case 6:
						{
						setState(221); stringParse();
						}
						break;
					}
					}
					setState(226);
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
			setState(229); eL3();
			setState(234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(230); match(BoolOp);
					setState(231); eL3();
					}
					} 
				}
				setState(236);
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
			setState(237); eL2();
			setState(242);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(238); match(RelOp);
					setState(239); eL2();
					}
					} 
				}
				setState(244);
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
			setState(245); eL1();
			setState(250);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(246); match(DataOp);
					setState(247); eL1();
					}
					} 
				}
				setState(252);
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
			setState(253); eUnOp();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 34, RULE_numParse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255); match(NumParse);
			}
		}
		catch (RecognitionException re) {
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
			setState(258);
			_la = _input.LA(1);
			if (_la==UnOp) {
				{
				setState(257); match(UnOp);
				}
			}

			setState(260); ePost();
			}
		}
		catch (RecognitionException re) {
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
			setState(262); eAtom();
			setState(273);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(271);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						setState(263); squareW();
						}
						break;

					case 2:
						{
						setState(264); square();
						}
						break;

					case 3:
						{
						setState(265); match(Dot);
						setState(266); mCall();
						}
						break;

					case 4:
						{
						setState(267); match(ORoundNoSpace);
						setState(268); round();
						}
						break;

					case 5:
						{
						setState(269); docs();
						}
						break;

					case 6:
						{
						setState(270); stringParse();
						}
						break;
					}
					} 
				}
				setState(275);
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
		enterRule(_localctx, 40, RULE_eAtom);
		int _la;
		try {
			setState(305);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(276); classBReuse();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(277); classB();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(279);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(278); numParse();
					}
				}

				setState(281); x();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(282); xOp();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(284);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(283); numParse();
					}
				}

				setState(286); match(Path);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(288);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(287); numParse();
					}
				}

				setState(290); block();
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(291); ifExpr();
				}
				break;

			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(292); whileExpr();
				}
				break;

			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(293); signalExpr();
				}
				break;

			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(294); loopExpr();
				}
				break;

			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(295); match(WalkBy);
				}
				break;

			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(296); w();
				}
				break;

			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(297); using();
				}
				break;

			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(298); match(DotDotDot);
				}
				break;

			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(299); mxRound();
				}
				break;

			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(300); useSquare();
				}
				break;

			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(302);
				_la = _input.LA(1);
				if (_la==NumParse) {
					{
					setState(301); numParse();
					}
				}

				setState(304); contextId();
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
			setState(307); match(MX);
			setState(308); round();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 44, RULE_contextId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310); match(ContextId);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 46, RULE_useSquare);
		try {
			setState(316);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(312); match(Using);
				setState(313); square();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(314); match(Using);
				setState(315); squareW();
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
		enterRule(_localctx, 48, RULE_ifExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318); match(If);
			setState(319); eTop();
			setState(320); block();
			setState(323);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(321); match(Else);
				setState(322); eTop();
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
		enterRule(_localctx, 50, RULE_using);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325); match(Using);
			setState(326); match(Path);
			setState(327); match(Check);
			setState(328); mCall();
			setState(329); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 52, RULE_whileExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(331); match(While);
			setState(332); eTop();
			setState(333); block();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 54, RULE_signalExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335); match(S);
			setState(336); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 56, RULE_loopExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338); match(Loop);
			setState(339); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 58, RULE_block);
		try {
			setState(343);
			switch (_input.LA(1)) {
			case ORoundSpace:
				enterOuterAlt(_localctx, 1);
				{
				setState(341); roundBlock();
				}
				break;
			case OCurly:
				enterOuterAlt(_localctx, 2);
				{
				setState(342); curlyBlock();
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
		enterRule(_localctx, 60, RULE_roundBlockForMethod);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(345); match(ORoundNoSpace);
			setState(346); docsOpt();
			setState(350);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(347); bb();
					}
					} 
				}
				setState(352);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(353); eTop();
			setState(354); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 62, RULE_roundBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(356); match(ORoundSpace);
			setState(357); docsOpt();
			setState(361);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(358); bb();
					}
					} 
				}
				setState(363);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			setState(364); eTop();
			setState(365); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 64, RULE_bb);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(368); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(367); d();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(370); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(372); ks();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 66, RULE_curlyBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374); match(OCurly);
			setState(375); docsOpt();
			setState(377); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(376); bb();
				}
				}
				setState(379); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << S) | (1L << Mdf) | (1L << ORoundSpace) | (1L << OCurly) | (1L << DotDotDot) | (1L << Ph) | (1L << If) | (1L << While) | (1L << Loop) | (1L << With) | (1L << Var) | (1L << Using) | (1L << WalkBy) | (1L << Path) | (1L << MX) | (1L << X) | (1L << ContextId) | (1L << UnOp) | (1L << NumParse))) != 0) );
			setState(381); match(CCurly);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 68, RULE_varDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(383); match(Var);
				}
			}

			setState(387);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(386); t();
				}
			}

			setState(389); x();
			setState(390); match(Equal);
			setState(391); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 70, RULE_d);
		try {
			setState(396);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(393); varDec();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(394); eTop();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(395); nestedClass();
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
		enterRule(_localctx, 72, RULE_stringParse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398); match(StringQuote);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 74, RULE_square);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(400); match(OSquare);
			setState(401); docsOpt();
			setState(408);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(402); ps();
					setState(403); match(Semicolon);
					setState(404); docsOpt();
					}
					} 
				}
				setState(410);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			setState(411); ps();
			setState(412); match(CSquare);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 76, RULE_squareW);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414); match(OSquare);
			setState(415); docsOpt();
			setState(416); w();
			setState(417); match(CSquare);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 78, RULE_mCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419); m();
			setState(420); round();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 80, RULE_round);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422); docsOpt();
			setState(423); ps();
			setState(424); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 82, RULE_ps);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(426); eTop();
				}
				break;
			}
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==X) {
				{
				{
				setState(429); match(X);
				setState(430); match(Colon);
				setState(431); eTop();
				}
				}
				setState(436);
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
		enterRule(_localctx, 84, RULE_k1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437); match(Catch);
			setState(438); match(S);
			setState(439); t();
			setState(440); match(X);
			setState(441); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 86, RULE_kMany);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(443); match(Catch);
			setState(444); match(S);
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
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(450); eTop();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode S() { return getToken(L42Parser.S, 0); }
		public TerminalNode On() { return getToken(L42Parser.On, 0); }
		public TContext t(int i) {
			return getRuleContext(TContext.class,i);
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
		enterRule(_localctx, 88, RULE_kProp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(452); match(S);
			setState(453); match(On);
			setState(455); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(454); t();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(457); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			setState(459); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 90, RULE_k);
		try {
			setState(464);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(461); k1();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(462); kMany();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(463); kProp();
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
		enterRule(_localctx, 92, RULE_ks);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(466); k();
					}
					} 
				}
				setState(471);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
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
		enterRule(_localctx, 94, RULE_on);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472); match(On);
			setState(473); t();
			setState(476);
			_la = _input.LA(1);
			if (_la==Case) {
				{
				setState(474); match(Case);
				setState(475); eTop();
				}
			}

			setState(478); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 96, RULE_onPlus);
		int _la;
		try {
			int _alt;
			setState(496);
			switch (_input.LA(1)) {
			case On:
				enterOuterAlt(_localctx, 1);
				{
				setState(480); match(On);
				setState(482); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(481); t();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(484); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
				setState(488);
				_la = _input.LA(1);
				if (_la==Case) {
					{
					setState(486); match(Case);
					setState(487); eTop();
					}
				}

				setState(490); eTop();
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(492); match(Case);
				setState(493); eTop();
				setState(494); eTop();
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
		enterRule(_localctx, 98, RULE_nudeE);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498); eTop();
			setState(499); match(EOF);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 100, RULE_classBExtra);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			_la = _input.LA(1);
			if (_la==Stage) {
				{
				setState(501); match(Stage);
				}
			}

			setState(507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Path) {
				{
				{
				setState(504); match(Path);
				}
				}
				setState(509);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(510); match(EndType);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 102, RULE_classBReuse);
		int _la;
		try {
			setState(528);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(512); match(OCurly);
				setState(513); docsOpt();
				setState(514); match(Url);
				setState(515); match(CCurly);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(517); match(OCurly);
				setState(518); docsOpt();
				setState(519); match(UrlNL);
				setState(520); docsOpt();
				setState(522); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(521); member();
					}
					}
					setState(524); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Refine) | (1L << Path))) != 0) );
				setState(526); match(CCurly);
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
		enterRule(_localctx, 104, RULE_classB);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(530); match(OCurly);
			setState(531); docsOpt();
			setState(532); header();
			setState(534);
			_la = _input.LA(1);
			if (_la==Implements) {
				{
				setState(533); impls();
				}
			}

			setState(539);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(536); fieldDec();
					}
					} 
				}
				setState(541);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			setState(542); docsOpt();
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Method) | (1L << Refine) | (1L << Path))) != 0)) {
				{
				{
				setState(543); member();
				}
				}
				setState(548);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(549); match(CCurly);
			setState(551);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(550); classBExtra();
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
		public List<TerminalNode> Path() { return getTokens(L42Parser.Path); }
		public List<DocsOptContext> docsOpt() {
			return getRuleContexts(DocsOptContext.class);
		}
		public DocsOptContext docsOpt(int i) {
			return getRuleContext(DocsOptContext.class,i);
		}
		public TerminalNode Path(int i) {
			return getToken(L42Parser.Path, i);
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
		enterRule(_localctx, 106, RULE_impls);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(553); match(Implements);
			setState(556); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(554); match(Path);
					setState(555); docsOpt();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(558); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
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
		enterRule(_localctx, 108, RULE_mhs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(560); match(Method);
			setState(561); docsOpt();
			setState(562); methSelector();
			setState(566);
			_la = _input.LA(1);
			if (_la==EndType) {
				{
				setState(563); match(EndType);
				setState(564); match(Path);
				setState(565); mht();
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
		public TerminalNode Refine() { return getToken(L42Parser.Refine, 0); }
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
		enterRule(_localctx, 110, RULE_mht);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(569);
			_la = _input.LA(1);
			if (_la==Refine) {
				{
				setState(568); match(Refine);
				}
			}

			setState(572);
			_la = _input.LA(1);
			if (_la==Mdf) {
				{
				setState(571); match(Mdf);
				}
			}

			setState(574); match(Method);
			setState(575); docsOpt();
			setState(576); t();
			setState(580);
			switch (_input.LA(1)) {
			case MX:
			case HashX:
			case UnOp:
			case EqOp:
			case BoolOp:
			case RelOp:
			case DataOp:
				{
				setState(577); mDec();
				}
				break;
			case ORoundNoSpace:
				{
				setState(578); match(ORoundNoSpace);
				}
				break;
			case ORoundSpace:
				{
				setState(579); match(ORoundSpace);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(588);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				{
				setState(582); t();
				setState(583); x();
				setState(584); docsOpt();
				}
				}
				setState(590);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(591); match(CRound);
			setState(598);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(592); match(S);
				setState(594); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(593); match(Path);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(596); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
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
		enterRule(_localctx, 112, RULE_member);
		try {
			setState(603);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(600); methodWithType();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(601); methodImplemented();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(602); nestedClass();
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
		enterRule(_localctx, 114, RULE_methodWithType);
		int _la;
		try {
			setState(617);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(605); mht();
				setState(608);
				_la = _input.LA(1);
				if (_la==EndType) {
					{
					setState(606); match(EndType);
					setState(607); match(Path);
					}
				}

				setState(610); docsOpt();
				setState(612);
				switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
				case 1:
					{
					setState(611); eTopForMethod();
					}
					break;
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(614); mht();
				setState(615); match(FieldSpecial);
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
		enterRule(_localctx, 116, RULE_methodImplemented);
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
		enterRule(_localctx, 118, RULE_nestedClass);
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
		enterRule(_localctx, 120, RULE_header);
		int _la;
		try {
			setState(644);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(628); match(Interface);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(630);
				_la = _input.LA(1);
				if (_la==Mdf) {
					{
					setState(629); match(Mdf);
					}
				}

				setState(635);
				switch (_input.LA(1)) {
				case MX:
				case HashX:
				case UnOp:
				case EqOp:
				case BoolOp:
				case RelOp:
				case DataOp:
					{
					setState(632); mDec();
					}
					break;
				case ORoundSpace:
					{
					setState(633); match(ORoundSpace);
					}
					break;
				case ORoundNoSpace:
					{
					setState(634); match(ORoundNoSpace);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(640);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path))) != 0)) {
					{
					{
					setState(637); fieldDec();
					}
					}
					setState(642);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(643); match(CRound);
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
		enterRule(_localctx, 122, RULE_fieldDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(646); match(Var);
				}
			}

			setState(649); t();
			setState(650); x();
			setState(651); docsOpt();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 124, RULE_w);
		try {
			setState(655);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(653); wSwitch();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(654); wSimple();
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
		enterRule(_localctx, 126, RULE_wSwitch);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(657); match(With);
			setState(661);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(658); x();
					}
					} 
				}
				setState(663);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			}
			setState(667);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(664); i();
					}
					} 
				}
				setState(669);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			}
			setState(673);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0)) {
				{
				{
				setState(670); varDec();
				}
				}
				setState(675);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(676); match(ORoundSpace);
			setState(678); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(677); onPlus();
				}
				}
				setState(680); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Case || _la==On );
			setState(684);
			_la = _input.LA(1);
			if (_la==Default) {
				{
				setState(682); match(Default);
				setState(683); eTop();
				}
			}

			setState(686); match(CRound);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 128, RULE_i);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(689);
			_la = _input.LA(1);
			if (_la==Var) {
				{
				setState(688); match(Var);
				}
			}

			setState(692);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Path))) != 0)) {
				{
				setState(691); t();
				}
			}

			setState(694); x();
			setState(695); match(In);
			setState(696); eTop();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 130, RULE_wSimple);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(698); match(With);
			setState(700); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(699); i();
				}
				}
				setState(702); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Mdf) | (1L << Ph) | (1L << Var) | (1L << Path) | (1L << X))) != 0) );
			setState(704); block();
			}
		}
		catch (RecognitionException re) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\38\u02c5\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u009e\n"+
		"\3\3\4\3\4\3\5\3\5\3\6\5\6\u00a5\n\6\3\7\3\7\5\7\u00a9\n\7\3\b\5\b\u00ac"+
		"\n\b\3\b\5\b\u00af\n\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00b8\n\t\3\n\5"+
		"\n\u00bb\n\n\3\n\3\n\3\n\6\n\u00c0\n\n\r\n\16\n\u00c1\3\13\3\13\3\13\5"+
		"\13\u00c7\n\13\3\13\7\13\u00ca\n\13\f\13\16\13\u00cd\13\13\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\7\16\u00e1\n\16\f\16\16\16\u00e4\13\16\5\16\u00e6\n\16\3\17\3\17\3"+
		"\17\7\17\u00eb\n\17\f\17\16\17\u00ee\13\17\3\20\3\20\3\20\7\20\u00f3\n"+
		"\20\f\20\16\20\u00f6\13\20\3\21\3\21\3\21\7\21\u00fb\n\21\f\21\16\21\u00fe"+
		"\13\21\3\22\3\22\3\23\3\23\3\24\5\24\u0105\n\24\3\24\3\24\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\7\25\u0112\n\25\f\25\16\25\u0115\13"+
		"\25\3\26\3\26\3\26\5\26\u011a\n\26\3\26\3\26\3\26\5\26\u011f\n\26\3\26"+
		"\3\26\5\26\u0123\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\5\26\u0131\n\26\3\26\5\26\u0134\n\26\3\27\3\27\3\27\3\30\3"+
		"\30\3\31\3\31\3\31\3\31\5\31\u013f\n\31\3\32\3\32\3\32\3\32\3\32\5\32"+
		"\u0146\n\32\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\37\3\37\5\37\u015a\n\37\3 \3 \3 \7 \u015f\n \f"+
		" \16 \u0162\13 \3 \3 \3 \3!\3!\3!\7!\u016a\n!\f!\16!\u016d\13!\3!\3!\3"+
		"!\3\"\6\"\u0173\n\"\r\"\16\"\u0174\3\"\3\"\3#\3#\3#\6#\u017c\n#\r#\16"+
		"#\u017d\3#\3#\3$\5$\u0183\n$\3$\5$\u0186\n$\3$\3$\3$\3$\3%\3%\3%\5%\u018f"+
		"\n%\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\7\'\u0199\n\'\f\'\16\'\u019c\13\'\3"+
		"\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3*\3*\3*\3*\3+\5+\u01ae\n+\3+\3+\3"+
		"+\7+\u01b3\n+\f+\16+\u01b6\13+\3,\3,\3,\3,\3,\3,\3-\3-\3-\6-\u01c1\n-"+
		"\r-\16-\u01c2\3-\3-\3.\3.\3.\6.\u01ca\n.\r.\16.\u01cb\3.\3.\3/\3/\3/\5"+
		"/\u01d3\n/\3\60\7\60\u01d6\n\60\f\60\16\60\u01d9\13\60\3\61\3\61\3\61"+
		"\3\61\5\61\u01df\n\61\3\61\3\61\3\62\3\62\6\62\u01e5\n\62\r\62\16\62\u01e6"+
		"\3\62\3\62\5\62\u01eb\n\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u01f3\n"+
		"\62\3\63\3\63\3\63\3\64\5\64\u01f9\n\64\3\64\7\64\u01fc\n\64\f\64\16\64"+
		"\u01ff\13\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3"+
		"\65\6\65\u020d\n\65\r\65\16\65\u020e\3\65\3\65\5\65\u0213\n\65\3\66\3"+
		"\66\3\66\3\66\5\66\u0219\n\66\3\66\7\66\u021c\n\66\f\66\16\66\u021f\13"+
		"\66\3\66\3\66\7\66\u0223\n\66\f\66\16\66\u0226\13\66\3\66\3\66\5\66\u022a"+
		"\n\66\3\67\3\67\3\67\6\67\u022f\n\67\r\67\16\67\u0230\38\38\38\38\38\3"+
		"8\58\u0239\n8\39\59\u023c\n9\39\59\u023f\n9\39\39\39\39\39\39\59\u0247"+
		"\n9\39\39\39\39\79\u024d\n9\f9\169\u0250\139\39\39\39\69\u0255\n9\r9\16"+
		"9\u0256\59\u0259\n9\3:\3:\3:\5:\u025e\n:\3;\3;\3;\5;\u0263\n;\3;\3;\5"+
		";\u0267\n;\3;\3;\3;\5;\u026c\n;\3<\3<\3<\3=\3=\3=\3=\3=\3>\3>\3>\5>\u0279"+
		"\n>\3>\3>\3>\5>\u027e\n>\3>\7>\u0281\n>\f>\16>\u0284\13>\3>\5>\u0287\n"+
		">\3?\5?\u028a\n?\3?\3?\3?\3?\3@\3@\5@\u0292\n@\3A\3A\7A\u0296\nA\fA\16"+
		"A\u0299\13A\3A\7A\u029c\nA\fA\16A\u029f\13A\3A\7A\u02a2\nA\fA\16A\u02a5"+
		"\13A\3A\3A\6A\u02a9\nA\rA\16A\u02aa\3A\3A\5A\u02af\nA\3A\3A\3B\5B\u02b4"+
		"\nB\3B\5B\u02b7\nB\3B\3B\3B\3B\3C\3C\6C\u02bf\nC\rC\16C\u02c0\3C\3C\3"+
		"C\2\2D\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<"+
		">@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\2\3\4\2**,,\u02f8"+
		"\2\u0086\3\2\2\2\4\u009d\3\2\2\2\6\u009f\3\2\2\2\b\u00a1\3\2\2\2\n\u00a4"+
		"\3\2\2\2\f\u00a8\3\2\2\2\16\u00ab\3\2\2\2\20\u00b3\3\2\2\2\22\u00ba\3"+
		"\2\2\2\24\u00c6\3\2\2\2\26\u00d0\3\2\2\2\30\u00d2\3\2\2\2\32\u00e5\3\2"+
		"\2\2\34\u00e7\3\2\2\2\36\u00ef\3\2\2\2 \u00f7\3\2\2\2\"\u00ff\3\2\2\2"+
		"$\u0101\3\2\2\2&\u0104\3\2\2\2(\u0108\3\2\2\2*\u0133\3\2\2\2,\u0135\3"+
		"\2\2\2.\u0138\3\2\2\2\60\u013e\3\2\2\2\62\u0140\3\2\2\2\64\u0147\3\2\2"+
		"\2\66\u014d\3\2\2\28\u0151\3\2\2\2:\u0154\3\2\2\2<\u0159\3\2\2\2>\u015b"+
		"\3\2\2\2@\u0166\3\2\2\2B\u0172\3\2\2\2D\u0178\3\2\2\2F\u0182\3\2\2\2H"+
		"\u018e\3\2\2\2J\u0190\3\2\2\2L\u0192\3\2\2\2N\u01a0\3\2\2\2P\u01a5\3\2"+
		"\2\2R\u01a8\3\2\2\2T\u01ad\3\2\2\2V\u01b7\3\2\2\2X\u01bd\3\2\2\2Z\u01c6"+
		"\3\2\2\2\\\u01d2\3\2\2\2^\u01d7\3\2\2\2`\u01da\3\2\2\2b\u01f2\3\2\2\2"+
		"d\u01f4\3\2\2\2f\u01f8\3\2\2\2h\u0212\3\2\2\2j\u0214\3\2\2\2l\u022b\3"+
		"\2\2\2n\u0232\3\2\2\2p\u023b\3\2\2\2r\u025d\3\2\2\2t\u026b\3\2\2\2v\u026d"+
		"\3\2\2\2x\u0270\3\2\2\2z\u0286\3\2\2\2|\u0289\3\2\2\2~\u0291\3\2\2\2\u0080"+
		"\u0293\3\2\2\2\u0082\u02b3\3\2\2\2\u0084\u02bc\3\2\2\2\u0086\u0087\t\2"+
		"\2\2\u0087\3\3\2\2\2\u0088\u009e\5\2\2\2\u0089\u008a\7\63\2\2\u008a\u009e"+
		"\7\5\2\2\u008b\u008c\7\64\2\2\u008c\u009e\7\5\2\2\u008d\u008e\7\65\2\2"+
		"\u008e\u009e\7\5\2\2\u008f\u0090\7\66\2\2\u0090\u009e\7\5\2\2\u0091\u0092"+
		"\7\67\2\2\u0092\u009e\7\5\2\2\u0093\u0094\7\63\2\2\u0094\u009e\7\6\2\2"+
		"\u0095\u0096\7\64\2\2\u0096\u009e\7\6\2\2\u0097\u0098\7\65\2\2\u0098\u009e"+
		"\7\6\2\2\u0099\u009a\7\66\2\2\u009a\u009e\7\6\2\2\u009b\u009c\7\67\2\2"+
		"\u009c\u009e\7\6\2\2\u009d\u0088\3\2\2\2\u009d\u0089\3\2\2\2\u009d\u008b"+
		"\3\2\2\2\u009d\u008d\3\2\2\2\u009d\u008f\3\2\2\2\u009d\u0091\3\2\2\2\u009d"+
		"\u0093\3\2\2\2\u009d\u0095\3\2\2\2\u009d\u0097\3\2\2\2\u009d\u0099\3\2"+
		"\2\2\u009d\u009b\3\2\2\2\u009e\5\3\2\2\2\u009f\u00a0\7\'\2\2\u00a0\7\3"+
		"\2\2\2\u00a1\u00a2\7\61\2\2\u00a2\t\3\2\2\2\u00a3\u00a5\7\61\2\2\u00a4"+
		"\u00a3\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\13\3\2\2\2\u00a6\u00a9\5\16\b"+
		"\2\u00a7\u00a9\5\22\n\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3\2\2\2\u00a9"+
		"\r\3\2\2\2\u00aa\u00ac\7\22\2\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3\2\2"+
		"\2\u00ac\u00ae\3\2\2\2\u00ad\u00af\7\4\2\2\u00ae\u00ad\3\2\2\2\u00ae\u00af"+
		"\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b1\7\'\2\2\u00b1\u00b2\5\n\6\2\u00b2"+
		"\17\3\2\2\2\u00b3\u00b4\7)\2\2\u00b4\u00b7\5\24\13\2\u00b5\u00b6\7)\2"+
		"\2\u00b6\u00b8\5\26\f\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8"+
		"\21\3\2\2\2\u00b9\u00bb\7\22\2\2\u00ba\u00b9\3\2\2\2\u00ba\u00bb\3\2\2"+
		"\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\7\'\2\2\u00bd\u00bf\5\n\6\2\u00be\u00c0"+
		"\5\20\t\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00bf\3\2\2\2"+
		"\u00c1\u00c2\3\2\2\2\u00c2\23\3\2\2\2\u00c3\u00c7\5\4\3\2\u00c4\u00c7"+
		"\7\5\2\2\u00c5\u00c7\7\6\2\2\u00c6\u00c3\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6"+
		"\u00c5\3\2\2\2\u00c7\u00cb\3\2\2\2\u00c8\u00ca\5\26\f\2\u00c9\u00c8\3"+
		"\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00cf\7\7\2\2\u00cf\25\3\2\2"+
		"\2\u00d0\u00d1\7+\2\2\u00d1\27\3\2\2\2\u00d2\u00d3\7+\2\2\u00d3\u00d4"+
		"\7\64\2\2\u00d4\u00d5\5\34\17\2\u00d5\31\3\2\2\2\u00d6\u00e6\5\34\17\2"+
		"\u00d7\u00e2\5> \2\u00d8\u00e1\5N(\2\u00d9\u00e1\5L\'\2\u00da\u00db\7"+
		"\20\2\2\u00db\u00e1\5P)\2\u00dc\u00dd\7\5\2\2\u00dd\u00e1\5R*\2\u00de"+
		"\u00e1\5\b\5\2\u00df\u00e1\5J&\2\u00e0\u00d8\3\2\2\2\u00e0\u00d9\3\2\2"+
		"\2\u00e0\u00da\3\2\2\2\u00e0\u00dc\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00df"+
		"\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3"+
		"\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5\u00d6\3\2\2\2\u00e5\u00d7\3\2"+
		"\2\2\u00e6\33\3\2\2\2\u00e7\u00ec\5\36\20\2\u00e8\u00e9\7\65\2\2\u00e9"+
		"\u00eb\5\36\20\2\u00ea\u00e8\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3"+
		"\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\35\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef"+
		"\u00f4\5 \21\2\u00f0\u00f1\7\66\2\2\u00f1\u00f3\5 \21\2\u00f2\u00f0\3"+
		"\2\2\2\u00f3\u00f6\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5"+
		"\37\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f7\u00fc\5\"\22\2\u00f8\u00f9\7\67"+
		"\2\2\u00f9\u00fb\5\"\22\2\u00fa\u00f8\3\2\2\2\u00fb\u00fe\3\2\2\2\u00fc"+
		"\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd!\3\2\2\2\u00fe\u00fc\3\2\2\2"+
		"\u00ff\u0100\5&\24\2\u0100#\3\2\2\2\u0101\u0102\78\2\2\u0102%\3\2\2\2"+
		"\u0103\u0105\7\63\2\2\u0104\u0103\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106"+
		"\3\2\2\2\u0106\u0107\5(\25\2\u0107\'\3\2\2\2\u0108\u0113\5*\26\2\u0109"+
		"\u0112\5N(\2\u010a\u0112\5L\'\2\u010b\u010c\7\20\2\2\u010c\u0112\5P)\2"+
		"\u010d\u010e\7\5\2\2\u010e\u0112\5R*\2\u010f\u0112\5\b\5\2\u0110\u0112"+
		"\5J&\2\u0111\u0109\3\2\2\2\u0111\u010a\3\2\2\2\u0111\u010b\3\2\2\2\u0111"+
		"\u010d\3\2\2\2\u0111\u010f\3\2\2\2\u0111\u0110\3\2\2\2\u0112\u0115\3\2"+
		"\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114)\3\2\2\2\u0115\u0113"+
		"\3\2\2\2\u0116\u0134\5h\65\2\u0117\u0134\5j\66\2\u0118\u011a\5$\23\2\u0119"+
		"\u0118\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u0134\5\26"+
		"\f\2\u011c\u0134\5\30\r\2\u011d\u011f\5$\23\2\u011e\u011d\3\2\2\2\u011e"+
		"\u011f\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u0134\7\'\2\2\u0121\u0123\5$"+
		"\23\2\u0122\u0121\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u0124\3\2\2\2\u0124"+
		"\u0134\5<\37\2\u0125\u0134\5\62\32\2\u0126\u0134\5\66\34\2\u0127\u0134"+
		"\58\35\2\u0128\u0134\5:\36\2\u0129\u0134\7%\2\2\u012a\u0134\5~@\2\u012b"+
		"\u0134\5\64\33\2\u012c\u0134\7\13\2\2\u012d\u0134\5,\27\2\u012e\u0134"+
		"\5\60\31\2\u012f\u0131\5$\23\2\u0130\u012f\3\2\2\2\u0130\u0131\3\2\2\2"+
		"\u0131\u0132\3\2\2\2\u0132\u0134\5.\30\2\u0133\u0116\3\2\2\2\u0133\u0117"+
		"\3\2\2\2\u0133\u0119\3\2\2\2\u0133\u011c\3\2\2\2\u0133\u011e\3\2\2\2\u0133"+
		"\u0122\3\2\2\2\u0133\u0125\3\2\2\2\u0133\u0126\3\2\2\2\u0133\u0127\3\2"+
		"\2\2\u0133\u0128\3\2\2\2\u0133\u0129\3\2\2\2\u0133\u012a\3\2\2\2\u0133"+
		"\u012b\3\2\2\2\u0133\u012c\3\2\2\2\u0133\u012d\3\2\2\2\u0133\u012e\3\2"+
		"\2\2\u0133\u0130\3\2\2\2\u0134+\3\2\2\2\u0135\u0136\7*\2\2\u0136\u0137"+
		"\5R*\2\u0137-\3\2\2\2\u0138\u0139\7-\2\2\u0139/\3\2\2\2\u013a\u013b\7"+
		"!\2\2\u013b\u013f\5L\'\2\u013c\u013d\7!\2\2\u013d\u013f\5N(\2\u013e\u013a"+
		"\3\2\2\2\u013e\u013c\3\2\2\2\u013f\61\3\2\2\2\u0140\u0141\7\25\2\2\u0141"+
		"\u0142\5\34\17\2\u0142\u0145\5<\37\2\u0143\u0144\7\26\2\2\u0144\u0146"+
		"\5\34\17\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146\63\3\2\2\2\u0147"+
		"\u0148\7!\2\2\u0148\u0149\7\'\2\2\u0149\u014a\7\"\2\2\u014a\u014b\5P)"+
		"\2\u014b\u014c\5\34\17\2\u014c\65\3\2\2\2\u014d\u014e\7\27\2\2\u014e\u014f"+
		"\5\34\17\2\u014f\u0150\5<\37\2\u0150\67\3\2\2\2\u0151\u0152\7\3\2\2\u0152"+
		"\u0153\5\34\17\2\u01539\3\2\2\2\u0154\u0155\7\30\2\2\u0155\u0156\5\34"+
		"\17\2\u0156;\3\2\2\2\u0157\u015a\5@!\2\u0158\u015a\5D#\2\u0159\u0157\3"+
		"\2\2\2\u0159\u0158\3\2\2\2\u015a=\3\2\2\2\u015b\u015c\7\5\2\2\u015c\u0160"+
		"\5\n\6\2\u015d\u015f\5B\"\2\u015e\u015d\3\2\2\2\u015f\u0162\3\2\2\2\u0160"+
		"\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u0163\3\2\2\2\u0162\u0160\3\2"+
		"\2\2\u0163\u0164\5\34\17\2\u0164\u0165\7\7\2\2\u0165?\3\2\2\2\u0166\u0167"+
		"\7\6\2\2\u0167\u016b\5\n\6\2\u0168\u016a\5B\"\2\u0169\u0168\3\2\2\2\u016a"+
		"\u016d\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016e\3\2"+
		"\2\2\u016d\u016b\3\2\2\2\u016e\u016f\5\34\17\2\u016f\u0170\7\7\2\2\u0170"+
		"A\3\2\2\2\u0171\u0173\5H%\2\u0172\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174"+
		"\u0172\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0176\3\2\2\2\u0176\u0177\5^"+
		"\60\2\u0177C\3\2\2\2\u0178\u0179\7\n\2\2\u0179\u017b\5\n\6\2\u017a\u017c"+
		"\5B\"\2\u017b\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017b\3\2\2\2\u017d"+
		"\u017e\3\2\2\2\u017e\u017f\3\2\2\2\u017f\u0180\7\r\2\2\u0180E\3\2\2\2"+
		"\u0181\u0183\7\35\2\2\u0182\u0181\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0185"+
		"\3\2\2\2\u0184\u0186\5\f\7\2\u0185\u0184\3\2\2\2\u0185\u0186\3\2\2\2\u0186"+
		"\u0187\3\2\2\2\u0187\u0188\5\26\f\2\u0188\u0189\7\21\2\2\u0189\u018a\5"+
		"\34\17\2\u018aG\3\2\2\2\u018b\u018f\5F$\2\u018c\u018f\5\34\17\2\u018d"+
		"\u018f\5x=\2\u018e\u018b\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018d\3\2\2"+
		"\2\u018fI\3\2\2\2\u0190\u0191\7.\2\2\u0191K\3\2\2\2\u0192\u0193\7\b\2"+
		"\2\u0193\u019a\5\n\6\2\u0194\u0195\5T+\2\u0195\u0196\7\17\2\2\u0196\u0197"+
		"\5\n\6\2\u0197\u0199\3\2\2\2\u0198\u0194\3\2\2\2\u0199\u019c\3\2\2\2\u019a"+
		"\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019d\3\2\2\2\u019c\u019a\3\2"+
		"\2\2\u019d\u019e\5T+\2\u019e\u019f\7\t\2\2\u019fM\3\2\2\2\u01a0\u01a1"+
		"\7\b\2\2\u01a1\u01a2\5\n\6\2\u01a2\u01a3\5~@\2\u01a3\u01a4\7\t\2\2\u01a4"+
		"O\3\2\2\2\u01a5\u01a6\5\2\2\2\u01a6\u01a7\5R*\2\u01a7Q\3\2\2\2\u01a8\u01a9"+
		"\5\n\6\2\u01a9\u01aa\5T+\2\u01aa\u01ab\7\7\2\2\u01abS\3\2\2\2\u01ac\u01ae"+
		"\5\34\17\2\u01ad\u01ac\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01b4\3\2\2\2"+
		"\u01af\u01b0\7+\2\2\u01b0\u01b1\7\16\2\2\u01b1\u01b3\5\34\17\2\u01b2\u01af"+
		"\3\2\2\2\u01b3\u01b6\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5"+
		"U\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b7\u01b8\7\34\2\2\u01b8\u01b9\7\3\2\2"+
		"\u01b9\u01ba\5\f\7\2\u01ba\u01bb\7+\2\2\u01bb\u01bc\5\34\17\2\u01bcW\3"+
		"\2\2\2\u01bd\u01be\7\34\2\2\u01be\u01c0\7\3\2\2\u01bf\u01c1\5\f\7\2\u01c0"+
		"\u01bf\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c2\u01c3\3\2"+
		"\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c5\5\34\17\2\u01c5Y\3\2\2\2\u01c6\u01c7"+
		"\7\3\2\2\u01c7\u01c9\7\32\2\2\u01c8\u01ca\5\f\7\2\u01c9\u01c8\3\2\2\2"+
		"\u01ca\u01cb\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01cd"+
		"\3\2\2\2\u01cd\u01ce\5\34\17\2\u01ce[\3\2\2\2\u01cf\u01d3\5V,\2\u01d0"+
		"\u01d3\5X-\2\u01d1\u01d3\5Z.\2\u01d2\u01cf\3\2\2\2\u01d2\u01d0\3\2\2\2"+
		"\u01d2\u01d1\3\2\2\2\u01d3]\3\2\2\2\u01d4\u01d6\5\\/\2\u01d5\u01d4\3\2"+
		"\2\2\u01d6\u01d9\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8"+
		"_\3\2\2\2\u01d9\u01d7\3\2\2\2\u01da\u01db\7\32\2\2\u01db\u01de\5\f\7\2"+
		"\u01dc\u01dd\7\24\2\2\u01dd\u01df\5\34\17\2\u01de\u01dc\3\2\2\2\u01de"+
		"\u01df\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0\u01e1\5\34\17\2\u01e1a\3\2\2"+
		"\2\u01e2\u01e4\7\32\2\2\u01e3\u01e5\5\f\7\2\u01e4\u01e3\3\2\2\2\u01e5"+
		"\u01e6\3\2\2\2\u01e6\u01e4\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01ea\3\2"+
		"\2\2\u01e8\u01e9\7\24\2\2\u01e9\u01eb\5\34\17\2\u01ea\u01e8\3\2\2\2\u01ea"+
		"\u01eb\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed\5\34\17\2\u01ed\u01f3\3"+
		"\2\2\2\u01ee\u01ef\7\24\2\2\u01ef\u01f0\5\34\17\2\u01f0\u01f1\5\34\17"+
		"\2\u01f1\u01f3\3\2\2\2\u01f2\u01e2\3\2\2\2\u01f2\u01ee\3\2\2\2\u01f3c"+
		"\3\2\2\2\u01f4\u01f5\5\34\17\2\u01f5\u01f6\7\2\2\3\u01f6e\3\2\2\2\u01f7"+
		"\u01f9\7&\2\2\u01f8\u01f7\3\2\2\2\u01f8\u01f9\3\2\2\2\u01f9\u01fd\3\2"+
		"\2\2\u01fa\u01fc\7\'\2\2\u01fb\u01fa\3\2\2\2\u01fc\u01ff\3\2\2\2\u01fd"+
		"\u01fb\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u0200\3\2\2\2\u01ff\u01fd\3\2"+
		"\2\2\u0200\u0201\7\f\2\2\u0201g\3\2\2\2\u0202\u0203\7\n\2\2\u0203\u0204"+
		"\5\n\6\2\u0204\u0205\7\60\2\2\u0205\u0206\7\r\2\2\u0206\u0213\3\2\2\2"+
		"\u0207\u0208\7\n\2\2\u0208\u0209\5\n\6\2\u0209\u020a\7/\2\2\u020a\u020c"+
		"\5\n\6\2\u020b\u020d\5r:\2\u020c\u020b\3\2\2\2\u020d\u020e\3\2\2\2\u020e"+
		"\u020c\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0211\7\r"+
		"\2\2\u0211\u0213\3\2\2\2\u0212\u0202\3\2\2\2\u0212\u0207\3\2\2\2\u0213"+
		"i\3\2\2\2\u0214\u0215\7\n\2\2\u0215\u0216\5\n\6\2\u0216\u0218\5z>\2\u0217"+
		"\u0219\5l\67\2\u0218\u0217\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021d\3\2"+
		"\2\2\u021a\u021c\5|?\2\u021b\u021a\3\2\2\2\u021c\u021f\3\2\2\2\u021d\u021b"+
		"\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u0220\3\2\2\2\u021f\u021d\3\2\2\2\u0220"+
		"\u0224\5\n\6\2\u0221\u0223\5r:\2\u0222\u0221\3\2\2\2\u0223\u0226\3\2\2"+
		"\2\u0224\u0222\3\2\2\2\u0224\u0225\3\2\2\2\u0225\u0227\3\2\2\2\u0226\u0224"+
		"\3\2\2\2\u0227\u0229\7\r\2\2\u0228\u022a\5f\64\2\u0229\u0228\3\2\2\2\u0229"+
		"\u022a\3\2\2\2\u022ak\3\2\2\2\u022b\u022e\7\23\2\2\u022c\u022d\7\'\2\2"+
		"\u022d\u022f\5\n\6\2\u022e\u022c\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u022e"+
		"\3\2\2\2\u0230\u0231\3\2\2\2\u0231m\3\2\2\2\u0232\u0233\7 \2\2\u0233\u0234"+
		"\5\n\6\2\u0234\u0238\5\24\13\2\u0235\u0236\7\f\2\2\u0236\u0237\7\'\2\2"+
		"\u0237\u0239\5p9\2\u0238\u0235\3\2\2\2\u0238\u0239\3\2\2\2\u0239o\3\2"+
		"\2\2\u023a\u023c\7#\2\2\u023b\u023a\3\2\2\2\u023b\u023c\3\2\2\2\u023c"+
		"\u023e\3\2\2\2\u023d\u023f\7\4\2\2\u023e\u023d\3\2\2\2\u023e\u023f\3\2"+
		"\2\2\u023f\u0240\3\2\2\2\u0240\u0241\7 \2\2\u0241\u0242\5\n\6\2\u0242"+
		"\u0246\5\f\7\2\u0243\u0247\5\4\3\2\u0244\u0247\7\5\2\2\u0245\u0247\7\6"+
		"\2\2\u0246\u0243\3\2\2\2\u0246\u0244\3\2\2\2\u0246\u0245\3\2\2\2\u0247"+
		"\u024e\3\2\2\2\u0248\u0249\5\f\7\2\u0249\u024a\5\26\f\2\u024a\u024b\5"+
		"\n\6\2\u024b\u024d\3\2\2\2\u024c\u0248\3\2\2\2\u024d\u0250\3\2\2\2\u024e"+
		"\u024c\3\2\2\2\u024e\u024f\3\2\2\2\u024f\u0251\3\2\2\2\u0250\u024e\3\2"+
		"\2\2\u0251\u0258\7\7\2\2\u0252\u0254\7\3\2\2\u0253\u0255\7\'\2\2\u0254"+
		"\u0253\3\2\2\2\u0255\u0256\3\2\2\2\u0256\u0254\3\2\2\2\u0256\u0257\3\2"+
		"\2\2\u0257\u0259\3\2\2\2\u0258\u0252\3\2\2\2\u0258\u0259\3\2\2\2\u0259"+
		"q\3\2\2\2\u025a\u025e\5t;\2\u025b\u025e\5v<\2\u025c\u025e\5x=\2\u025d"+
		"\u025a\3\2\2\2\u025d\u025b\3\2\2\2\u025d\u025c\3\2\2\2\u025es\3\2\2\2"+
		"\u025f\u0262\5p9\2\u0260\u0261\7\f\2\2\u0261\u0263\7\'\2\2\u0262\u0260"+
		"\3\2\2\2\u0262\u0263\3\2\2\2\u0263\u0264\3\2\2\2\u0264\u0266\5\n\6\2\u0265"+
		"\u0267\5\32\16\2\u0266\u0265\3\2\2\2\u0266\u0267\3\2\2\2\u0267\u026c\3"+
		"\2\2\2\u0268\u0269\5p9\2\u0269\u026a\7$\2\2\u026a\u026c\3\2\2\2\u026b"+
		"\u025f\3\2\2\2\u026b\u0268\3\2\2\2\u026cu\3\2\2\2\u026d\u026e\5n8\2\u026e"+
		"\u026f\5\32\16\2\u026fw\3\2\2\2\u0270\u0271\7\'\2\2\u0271\u0272\7\16\2"+
		"\2\u0272\u0273\5\n\6\2\u0273\u0274\5\34\17\2\u0274y\3\2\2\2\u0275\u0287"+
		"\3\2\2\2\u0276\u0287\7\37\2\2\u0277\u0279\7\4\2\2\u0278\u0277\3\2\2\2"+
		"\u0278\u0279\3\2\2\2\u0279\u027d\3\2\2\2\u027a\u027e\5\4\3\2\u027b\u027e"+
		"\7\6\2\2\u027c\u027e\7\5\2\2\u027d\u027a\3\2\2\2\u027d\u027b\3\2\2\2\u027d"+
		"\u027c\3\2\2\2\u027e\u0282\3\2\2\2\u027f\u0281\5|?\2\u0280\u027f\3\2\2"+
		"\2\u0281\u0284\3\2\2\2\u0282\u0280\3\2\2\2\u0282\u0283\3\2\2\2\u0283\u0285"+
		"\3\2\2\2\u0284\u0282\3\2\2\2\u0285\u0287\7\7\2\2\u0286\u0275\3\2\2\2\u0286"+
		"\u0276\3\2\2\2\u0286\u0278\3\2\2\2\u0287{\3\2\2\2\u0288\u028a\7\35\2\2"+
		"\u0289\u0288\3\2\2\2\u0289\u028a\3\2\2\2\u028a\u028b\3\2\2\2\u028b\u028c"+
		"\5\f\7\2\u028c\u028d\5\26\f\2\u028d\u028e\5\n\6\2\u028e}\3\2\2\2\u028f"+
		"\u0292\5\u0080A\2\u0290\u0292\5\u0084C\2\u0291\u028f\3\2\2\2\u0291\u0290"+
		"\3\2\2\2\u0292\177\3\2\2\2\u0293\u0297\7\31\2\2\u0294\u0296\5\26\f\2\u0295"+
		"\u0294\3\2\2\2\u0296\u0299\3\2\2\2\u0297\u0295\3\2\2\2\u0297\u0298\3\2"+
		"\2\2\u0298\u029d\3\2\2\2\u0299\u0297\3\2\2\2\u029a\u029c\5\u0082B\2\u029b"+
		"\u029a\3\2\2\2\u029c\u029f\3\2\2\2\u029d\u029b\3\2\2\2\u029d\u029e\3\2"+
		"\2\2\u029e\u02a3\3\2\2\2\u029f\u029d\3\2\2\2\u02a0\u02a2\5F$\2\u02a1\u02a0"+
		"\3\2\2\2\u02a2\u02a5\3\2\2\2\u02a3\u02a1\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4"+
		"\u02a6\3\2\2\2\u02a5\u02a3\3\2\2\2\u02a6\u02a8\7\6\2\2\u02a7\u02a9\5b"+
		"\62\2\u02a8\u02a7\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02a8\3\2\2\2\u02aa"+
		"\u02ab\3\2\2\2\u02ab\u02ae\3\2\2\2\u02ac\u02ad\7\36\2\2\u02ad\u02af\5"+
		"\34\17\2\u02ae\u02ac\3\2\2\2\u02ae\u02af\3\2\2\2\u02af\u02b0\3\2\2\2\u02b0"+
		"\u02b1\7\7\2\2\u02b1\u0081\3\2\2\2\u02b2\u02b4\7\35\2\2\u02b3\u02b2\3"+
		"\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b6\3\2\2\2\u02b5\u02b7\5\f\7\2\u02b6"+
		"\u02b5\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02b9\5\26"+
		"\f\2\u02b9\u02ba\7\33\2\2\u02ba\u02bb\5\34\17\2\u02bb\u0083\3\2\2\2\u02bc"+
		"\u02be\7\31\2\2\u02bd\u02bf\5\u0082B\2\u02be\u02bd\3\2\2\2\u02bf\u02c0"+
		"\3\2\2\2\u02c0\u02be\3\2\2\2\u02c0\u02c1\3\2\2\2\u02c1\u02c2\3\2\2\2\u02c2"+
		"\u02c3\5<\37\2\u02c3\u0085\3\2\2\2Q\u009d\u00a4\u00a8\u00ab\u00ae\u00b7"+
		"\u00ba\u00c1\u00c6\u00cb\u00e0\u00e2\u00e5\u00ec\u00f4\u00fc\u0104\u0111"+
		"\u0113\u0119\u011e\u0122\u0130\u0133\u013e\u0145\u0159\u0160\u016b\u0174"+
		"\u017d\u0182\u0185\u018e\u019a\u01ad\u01b4\u01c2\u01cb\u01d2\u01d7\u01de"+
		"\u01e6\u01ea\u01f2\u01f8\u01fd\u020e\u0212\u0218\u021d\u0224\u0229\u0230"+
		"\u0238\u023b\u023e\u0246\u024e\u0256\u0258\u025d\u0262\u0266\u026b\u0278"+
		"\u027d\u0282\u0286\u0289\u0291\u0297\u029d\u02a3\u02aa\u02ae\u02b3\u02b6"+
		"\u02c0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}