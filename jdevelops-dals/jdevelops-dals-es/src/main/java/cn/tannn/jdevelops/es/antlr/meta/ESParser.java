// Generated from D:/代码/self/Jdevelops开发工具集/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES.g4 by ANTLR 4.13.2
package cn.tannn.jdevelops.es.antlr.meta;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ESParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, EXISTS=19, NOT=20, IDENTIFIER=21, DOUBLE_QUOTED_STRING=22, SINGLE_QUOTED_STRING=23, 
		BARE_STRING=24, INT=25, DECIMAL=26, ARRAY_START=27, ARRAY_END=28, COMMA=29, 
		WS=30;
	public static final int
		RULE_query = 0, RULE_expression = 1, RULE_comparison = 2, RULE_existsOperator = 3, 
		RULE_operator = 4, RULE_valueType = 5, RULE_arrayValue = 6, RULE_value = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "expression", "comparison", "existsOperator", "operator", "valueType", 
			"arrayValue", "value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'AND'", "'and'", "'OR'", "'or'", "'('", "')'", "'=='", "'!='", 
			"'>='", "'<='", "'>'", "'<'", "'+='", "'=~'", "'!~'", "'in'", "'not in'", 
			"'null'", "'exists'", "'not'", null, null, null, null, null, null, "'['", 
			"']'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "EXISTS", "NOT", "IDENTIFIER", 
			"DOUBLE_QUOTED_STRING", "SINGLE_QUOTED_STRING", "BARE_STRING", "INT", 
			"DECIMAL", "ARRAY_START", "ARRAY_END", "COMMA", "WS"
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
	public String getGrammarFileName() { return "ES.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ESParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ESParser.EOF, 0); }
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			expression(0);
			setState(17);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesizedExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParenthesizedExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterParenthesizedExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitParenthesizedExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitParenthesizedExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonExpressionContext extends ExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public ComparisonExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterComparisonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitComparisonExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				_localctx = new ParenthesizedExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(20);
				match(T__4);
				setState(21);
				expression(0);
				setState(22);
				match(T__5);
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new ComparisonExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24);
				comparison();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(35);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(33);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(27);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(28);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__1) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(29);
						expression(5);
						}
						break;
					case 2:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(30);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(31);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__3) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(32);
						expression(4);
						}
						break;
					}
					} 
				}
				setState(37);
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
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonContext extends ParserRuleContext {
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
	 
		public ComparisonContext() { }
		public void copyFrom(ComparisonContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StandardComparisonContext extends ComparisonContext {
		public TerminalNode IDENTIFIER() { return getToken(ESParser.IDENTIFIER, 0); }
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ValueTypeContext valueType() {
			return getRuleContext(ValueTypeContext.class,0);
		}
		public StandardComparisonContext(ComparisonContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterStandardComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitStandardComparison(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitStandardComparison(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExistenceComparisonContext extends ComparisonContext {
		public TerminalNode IDENTIFIER() { return getToken(ESParser.IDENTIFIER, 0); }
		public ExistsOperatorContext existsOperator() {
			return getRuleContext(ExistsOperatorContext.class,0);
		}
		public ExistenceComparisonContext(ComparisonContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterExistenceComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitExistenceComparison(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitExistenceComparison(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_comparison);
		try {
			setState(44);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new StandardComparisonContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(38);
				match(IDENTIFIER);
				setState(39);
				operator();
				setState(40);
				valueType();
				}
				break;
			case 2:
				_localctx = new ExistenceComparisonContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(42);
				match(IDENTIFIER);
				setState(43);
				existsOperator();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExistsOperatorContext extends ParserRuleContext {
		public ExistsOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existsOperator; }
	 
		public ExistsOperatorContext() { }
		public void copyFrom(ExistsOperatorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExistsOpContext extends ExistsOperatorContext {
		public TerminalNode EXISTS() { return getToken(ESParser.EXISTS, 0); }
		public ExistsOpContext(ExistsOperatorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterExistsOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitExistsOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitExistsOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotExistsOpContext extends ExistsOperatorContext {
		public TerminalNode NOT() { return getToken(ESParser.NOT, 0); }
		public TerminalNode EXISTS() { return getToken(ESParser.EXISTS, 0); }
		public NotExistsOpContext(ExistsOperatorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterNotExistsOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitNotExistsOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitNotExistsOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistsOperatorContext existsOperator() throws RecognitionException {
		ExistsOperatorContext _localctx = new ExistsOperatorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_existsOperator);
		try {
			setState(49);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXISTS:
				_localctx = new ExistsOpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				match(EXISTS);
				}
				break;
			case NOT:
				_localctx = new NotExistsOpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				match(NOT);
				setState(48);
				match(EXISTS);
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

	@SuppressWarnings("CheckReturnValue")
	public static class OperatorContext extends ParserRuleContext {
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 262016L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
	public static class ValueTypeContext extends ParserRuleContext {
		public ValueTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueType; }
	 
		public ValueTypeContext() { }
		public void copyFrom(ValueTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SingleQuotedStringValueContext extends ValueTypeContext {
		public TerminalNode SINGLE_QUOTED_STRING() { return getToken(ESParser.SINGLE_QUOTED_STRING, 0); }
		public SingleQuotedStringValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterSingleQuotedStringValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitSingleQuotedStringValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitSingleQuotedStringValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullValueContext extends ValueTypeContext {
		public NullValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterNullValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitNullValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitNullValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DecimalValueContext extends ValueTypeContext {
		public TerminalNode DECIMAL() { return getToken(ESParser.DECIMAL, 0); }
		public DecimalValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterDecimalValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitDecimalValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitDecimalValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class QuotedStringValueContext extends ValueTypeContext {
		public TerminalNode DOUBLE_QUOTED_STRING() { return getToken(ESParser.DOUBLE_QUOTED_STRING, 0); }
		public QuotedStringValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterQuotedStringValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitQuotedStringValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitQuotedStringValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayValuesContext extends ValueTypeContext {
		public ArrayValueContext arrayValue() {
			return getRuleContext(ArrayValueContext.class,0);
		}
		public ArrayValuesContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterArrayValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitArrayValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitArrayValues(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BareStringValueContext extends ValueTypeContext {
		public TerminalNode BARE_STRING() { return getToken(ESParser.BARE_STRING, 0); }
		public BareStringValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterBareStringValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitBareStringValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitBareStringValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntValueContext extends ValueTypeContext {
		public TerminalNode INT() { return getToken(ESParser.INT, 0); }
		public IntValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterIntValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitIntValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitIntValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueTypeContext valueType() throws RecognitionException {
		ValueTypeContext _localctx = new ValueTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_valueType);
		try {
			setState(60);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOUBLE_QUOTED_STRING:
				_localctx = new QuotedStringValueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				match(DOUBLE_QUOTED_STRING);
				}
				break;
			case SINGLE_QUOTED_STRING:
				_localctx = new SingleQuotedStringValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(54);
				match(SINGLE_QUOTED_STRING);
				}
				break;
			case BARE_STRING:
				_localctx = new BareStringValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(55);
				match(BARE_STRING);
				}
				break;
			case INT:
				_localctx = new IntValueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(56);
				match(INT);
				}
				break;
			case DECIMAL:
				_localctx = new DecimalValueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				match(DECIMAL);
				}
				break;
			case ARRAY_START:
				_localctx = new ArrayValuesContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(58);
				arrayValue();
				}
				break;
			case T__17:
				_localctx = new NullValueContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(59);
				match(T__17);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayValueContext extends ParserRuleContext {
		public TerminalNode ARRAY_START() { return getToken(ESParser.ARRAY_START, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode ARRAY_END() { return getToken(ESParser.ARRAY_END, 0); }
		public List<TerminalNode> COMMA() { return getTokens(ESParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ESParser.COMMA, i);
		}
		public ArrayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterArrayValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitArrayValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitArrayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueContext arrayValue() throws RecognitionException {
		ArrayValueContext _localctx = new ArrayValueContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(ARRAY_START);
			setState(63);
			value();
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(64);
				match(COMMA);
				setState(65);
				value();
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(71);
			match(ARRAY_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode DOUBLE_QUOTED_STRING() { return getToken(ESParser.DOUBLE_QUOTED_STRING, 0); }
		public TerminalNode SINGLE_QUOTED_STRING() { return getToken(ESParser.SINGLE_QUOTED_STRING, 0); }
		public TerminalNode BARE_STRING() { return getToken(ESParser.BARE_STRING, 0); }
		public TerminalNode INT() { return getToken(ESParser.INT, 0); }
		public TerminalNode DECIMAL() { return getToken(ESParser.DECIMAL, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 130023424L) != 0)) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001eL\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u001a\b\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\"\b"+
		"\u0001\n\u0001\f\u0001%\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u0002-\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u00032\b\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005=\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0005\u0006C\b\u0006\n\u0006\f\u0006F\t\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0000\u0001\u0002\b\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0000\u0004\u0001\u0000\u0001\u0002\u0001\u0000"+
		"\u0003\u0004\u0001\u0000\u0007\u0011\u0001\u0000\u0016\u001aO\u0000\u0010"+
		"\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004,\u0001"+
		"\u0000\u0000\u0000\u00061\u0001\u0000\u0000\u0000\b3\u0001\u0000\u0000"+
		"\u0000\n<\u0001\u0000\u0000\u0000\f>\u0001\u0000\u0000\u0000\u000eI\u0001"+
		"\u0000\u0000\u0000\u0010\u0011\u0003\u0002\u0001\u0000\u0011\u0012\u0005"+
		"\u0000\u0000\u0001\u0012\u0001\u0001\u0000\u0000\u0000\u0013\u0014\u0006"+
		"\u0001\uffff\uffff\u0000\u0014\u0015\u0005\u0005\u0000\u0000\u0015\u0016"+
		"\u0003\u0002\u0001\u0000\u0016\u0017\u0005\u0006\u0000\u0000\u0017\u001a"+
		"\u0001\u0000\u0000\u0000\u0018\u001a\u0003\u0004\u0002\u0000\u0019\u0013"+
		"\u0001\u0000\u0000\u0000\u0019\u0018\u0001\u0000\u0000\u0000\u001a#\u0001"+
		"\u0000\u0000\u0000\u001b\u001c\n\u0004\u0000\u0000\u001c\u001d\u0007\u0000"+
		"\u0000\u0000\u001d\"\u0003\u0002\u0001\u0005\u001e\u001f\n\u0003\u0000"+
		"\u0000\u001f \u0007\u0001\u0000\u0000 \"\u0003\u0002\u0001\u0004!\u001b"+
		"\u0001\u0000\u0000\u0000!\u001e\u0001\u0000\u0000\u0000\"%\u0001\u0000"+
		"\u0000\u0000#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$\u0003"+
		"\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000&\'\u0005\u0015\u0000"+
		"\u0000\'(\u0003\b\u0004\u0000()\u0003\n\u0005\u0000)-\u0001\u0000\u0000"+
		"\u0000*+\u0005\u0015\u0000\u0000+-\u0003\u0006\u0003\u0000,&\u0001\u0000"+
		"\u0000\u0000,*\u0001\u0000\u0000\u0000-\u0005\u0001\u0000\u0000\u0000"+
		".2\u0005\u0013\u0000\u0000/0\u0005\u0014\u0000\u000002\u0005\u0013\u0000"+
		"\u00001.\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u00002\u0007\u0001"+
		"\u0000\u0000\u000034\u0007\u0002\u0000\u00004\t\u0001\u0000\u0000\u0000"+
		"5=\u0005\u0016\u0000\u00006=\u0005\u0017\u0000\u00007=\u0005\u0018\u0000"+
		"\u00008=\u0005\u0019\u0000\u00009=\u0005\u001a\u0000\u0000:=\u0003\f\u0006"+
		"\u0000;=\u0005\u0012\u0000\u0000<5\u0001\u0000\u0000\u0000<6\u0001\u0000"+
		"\u0000\u0000<7\u0001\u0000\u0000\u0000<8\u0001\u0000\u0000\u0000<9\u0001"+
		"\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000<;\u0001\u0000\u0000\u0000"+
		"=\u000b\u0001\u0000\u0000\u0000>?\u0005\u001b\u0000\u0000?D\u0003\u000e"+
		"\u0007\u0000@A\u0005\u001d\u0000\u0000AC\u0003\u000e\u0007\u0000B@\u0001"+
		"\u0000\u0000\u0000CF\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000"+
		"DE\u0001\u0000\u0000\u0000EG\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000"+
		"\u0000GH\u0005\u001c\u0000\u0000H\r\u0001\u0000\u0000\u0000IJ\u0007\u0003"+
		"\u0000\u0000J\u000f\u0001\u0000\u0000\u0000\u0007\u0019!#,1<D";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}