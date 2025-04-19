// Generated from G:/tan/code/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES_Upgrade.g4 by ANTLR 4.13.2
package cn.tannn.jdevelops.es.antlr.business.meta;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ES_UpgradeParser}.
 */
public interface ES_UpgradeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(ES_UpgradeParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(ES_UpgradeParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(ES_UpgradeParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(ES_UpgradeParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(ES_UpgradeParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(ES_UpgradeParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(ES_UpgradeParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(ES_UpgradeParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(ES_UpgradeParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(ES_UpgradeParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(ES_UpgradeParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(ES_UpgradeParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterStandardComparison(ES_UpgradeParser.StandardComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitStandardComparison(ES_UpgradeParser.StandardComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterBetweenComparison(ES_UpgradeParser.BetweenComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitBetweenComparison(ES_UpgradeParser.BetweenComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterExistenceComparison(ES_UpgradeParser.ExistenceComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitExistenceComparison(ES_UpgradeParser.ExistenceComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void enterExistsOp(ES_UpgradeParser.ExistsOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void exitExistsOp(ES_UpgradeParser.ExistsOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void enterNotExistsOp(ES_UpgradeParser.NotExistsOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void exitNotExistsOp(ES_UpgradeParser.NotExistsOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(ES_UpgradeParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(ES_UpgradeParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterQuotedStringValue(ES_UpgradeParser.QuotedStringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitQuotedStringValue(ES_UpgradeParser.QuotedStringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnquotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedStringValue(ES_UpgradeParser.UnquotedStringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnquotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedStringValue(ES_UpgradeParser.UnquotedStringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterIntValue(ES_UpgradeParser.IntValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitIntValue(ES_UpgradeParser.IntValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterDecimalValue(ES_UpgradeParser.DecimalValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitDecimalValue(ES_UpgradeParser.DecimalValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterArrayValues(ES_UpgradeParser.ArrayValuesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitArrayValues(ES_UpgradeParser.ArrayValuesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterNullValue(ES_UpgradeParser.NullValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitNullValue(ES_UpgradeParser.NullValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#quotedString}.
	 * @param ctx the parse tree
	 */
	void enterQuotedString(ES_UpgradeParser.QuotedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#quotedString}.
	 * @param ctx the parse tree
	 */
	void exitQuotedString(ES_UpgradeParser.QuotedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#unquotedString}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedString(ES_UpgradeParser.UnquotedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#unquotedString}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedString(ES_UpgradeParser.UnquotedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#arrayValue}.
	 * @param ctx the parse tree
	 */
	void enterArrayValue(ES_UpgradeParser.ArrayValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#arrayValue}.
	 * @param ctx the parse tree
	 */
	void exitArrayValue(ES_UpgradeParser.ArrayValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ES_UpgradeParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ES_UpgradeParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ES_UpgradeParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ES_UpgradeParser.ValueContext ctx);
}