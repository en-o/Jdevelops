// Generated from G:/tan/code/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES.g4 by ANTLR 4.13.2
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
		T__17=18, EXISTS=19, NOT=20, IDENTIFIER=21, STRING=22, INT=23, DECIMAL=24, 
		ARRAY_START=25, ARRAY_END=26, COMMA=27, WS=28;
	public static final int
		RULE_query = 0, RULE_expression = 1, RULE_comparison = 2, RULE_existsOperator = 3, 
		RULE_operator = 4, RULE_valueType = 5, RULE_arrayValue = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "expression", "comparison", "existsOperator", "operator", "valueType", 
			"arrayValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'AND'", "'and'", "'OR'", "'or'", "'('", "')'", "'=='", "'!='", 
			"'>='", "'<='", "'>'", "'<'", "'+='", "'=~'", "'!~'", "'in'", "'not in'", 
			"'null'", "'exists'", "'not'", null, null, null, null, "'['", "']'", 
			"','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "EXISTS", "NOT", "IDENTIFIER", 
			"STRING", "INT", "DECIMAL", "ARRAY_START", "ARRAY_END", "COMMA", "WS"
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
			setState(14);
			expression(0);
			setState(15);
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
			setState(23);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				_localctx = new ParenthesizedExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(18);
				match(T__4);
				setState(19);
				expression(0);
				setState(20);
				match(T__5);
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new ComparisonExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(22);
				comparison();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(33);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(31);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(25);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(26);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__1) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(27);
						expression(5);
						}
						break;
					case 2:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(28);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(29);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__3) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(30);
						expression(4);
						}
						break;
					}
					} 
				}
				setState(35);
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
			setState(42);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new StandardComparisonContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(36);
				match(IDENTIFIER);
				setState(37);
				operator();
				setState(38);
				valueType();
				}
				break;
			case 2:
				_localctx = new ExistenceComparisonContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(40);
				match(IDENTIFIER);
				setState(41);
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
			setState(47);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXISTS:
				_localctx = new ExistsOpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				match(EXISTS);
				}
				break;
			case NOT:
				_localctx = new NotExistsOpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				match(NOT);
				setState(46);
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
			setState(49);
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
	public static class StringValueContext extends ValueTypeContext {
		public TerminalNode STRING() { return getToken(ESParser.STRING, 0); }
		public StringValueContext(ValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).enterStringValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ESListener ) ((ESListener)listener).exitStringValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ESVisitor ) return ((ESVisitor<? extends T>)visitor).visitStringValue(this);
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
			setState(56);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				_localctx = new StringValueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(51);
				match(STRING);
				}
				break;
			case INT:
				_localctx = new IntValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(52);
				match(INT);
				}
				break;
			case DECIMAL:
				_localctx = new DecimalValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(53);
				match(DECIMAL);
				}
				break;
			case ARRAY_START:
				_localctx = new ArrayValuesContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(54);
				arrayValue();
				}
				break;
			case T__17:
				_localctx = new NullValueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(55);
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
		public TerminalNode ARRAY_END() { return getToken(ESParser.ARRAY_END, 0); }
		public List<TerminalNode> STRING() { return getTokens(ESParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(ESParser.STRING, i);
		}
		public List<TerminalNode> INT() { return getTokens(ESParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(ESParser.INT, i);
		}
		public List<TerminalNode> DECIMAL() { return getTokens(ESParser.DECIMAL); }
		public TerminalNode DECIMAL(int i) {
			return getToken(ESParser.DECIMAL, i);
		}
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
			setState(58);
			match(ARRAY_START);
			setState(59);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(60);
				match(COMMA);
				setState(61);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(67);
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
		"\u0004\u0001\u001cF\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u0018\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0005\u0001 \b\u0001\n\u0001\f\u0001#\t"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u0002+\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0003"+
		"\u00030\b\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u00059\b\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0005\u0006?\b\u0006\n\u0006\f\u0006B\t"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0000\u0001\u0002\u0007\u0000"+
		"\u0002\u0004\u0006\b\n\f\u0000\u0004\u0001\u0000\u0001\u0002\u0001\u0000"+
		"\u0003\u0004\u0001\u0000\u0007\u0011\u0001\u0000\u0016\u0018H\u0000\u000e"+
		"\u0001\u0000\u0000\u0000\u0002\u0017\u0001\u0000\u0000\u0000\u0004*\u0001"+
		"\u0000\u0000\u0000\u0006/\u0001\u0000\u0000\u0000\b1\u0001\u0000\u0000"+
		"\u0000\n8\u0001\u0000\u0000\u0000\f:\u0001\u0000\u0000\u0000\u000e\u000f"+
		"\u0003\u0002\u0001\u0000\u000f\u0010\u0005\u0000\u0000\u0001\u0010\u0001"+
		"\u0001\u0000\u0000\u0000\u0011\u0012\u0006\u0001\uffff\uffff\u0000\u0012"+
		"\u0013\u0005\u0005\u0000\u0000\u0013\u0014\u0003\u0002\u0001\u0000\u0014"+
		"\u0015\u0005\u0006\u0000\u0000\u0015\u0018\u0001\u0000\u0000\u0000\u0016"+
		"\u0018\u0003\u0004\u0002\u0000\u0017\u0011\u0001\u0000\u0000\u0000\u0017"+
		"\u0016\u0001\u0000\u0000\u0000\u0018!\u0001\u0000\u0000\u0000\u0019\u001a"+
		"\n\u0004\u0000\u0000\u001a\u001b\u0007\u0000\u0000\u0000\u001b \u0003"+
		"\u0002\u0001\u0005\u001c\u001d\n\u0003\u0000\u0000\u001d\u001e\u0007\u0001"+
		"\u0000\u0000\u001e \u0003\u0002\u0001\u0004\u001f\u0019\u0001\u0000\u0000"+
		"\u0000\u001f\u001c\u0001\u0000\u0000\u0000 #\u0001\u0000\u0000\u0000!"+
		"\u001f\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"\u0003\u0001"+
		"\u0000\u0000\u0000#!\u0001\u0000\u0000\u0000$%\u0005\u0015\u0000\u0000"+
		"%&\u0003\b\u0004\u0000&\'\u0003\n\u0005\u0000\'+\u0001\u0000\u0000\u0000"+
		"()\u0005\u0015\u0000\u0000)+\u0003\u0006\u0003\u0000*$\u0001\u0000\u0000"+
		"\u0000*(\u0001\u0000\u0000\u0000+\u0005\u0001\u0000\u0000\u0000,0\u0005"+
		"\u0013\u0000\u0000-.\u0005\u0014\u0000\u0000.0\u0005\u0013\u0000\u0000"+
		"/,\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u00000\u0007\u0001\u0000"+
		"\u0000\u000012\u0007\u0002\u0000\u00002\t\u0001\u0000\u0000\u000039\u0005"+
		"\u0016\u0000\u000049\u0005\u0017\u0000\u000059\u0005\u0018\u0000\u0000"+
		"69\u0003\f\u0006\u000079\u0005\u0012\u0000\u000083\u0001\u0000\u0000\u0000"+
		"84\u0001\u0000\u0000\u000085\u0001\u0000\u0000\u000086\u0001\u0000\u0000"+
		"\u000087\u0001\u0000\u0000\u00009\u000b\u0001\u0000\u0000\u0000:;\u0005"+
		"\u0019\u0000\u0000;@\u0007\u0003\u0000\u0000<=\u0005\u001b\u0000\u0000"+
		"=?\u0007\u0003\u0000\u0000><\u0001\u0000\u0000\u0000?B\u0001\u0000\u0000"+
		"\u0000@>\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AC\u0001\u0000"+
		"\u0000\u0000B@\u0001\u0000\u0000\u0000CD\u0005\u001a\u0000\u0000D\r\u0001"+
		"\u0000\u0000\u0000\u0007\u0017\u001f!*/8@";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}