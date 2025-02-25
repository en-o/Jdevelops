// Generated from D:/代码/self/Jdevelops开发工具集/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES.g4 by ANTLR 4.13.2
package cn.tannn.jdevelops.es.antlr.meta;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ESParser}.
 */
public interface ESListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ESParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(ESParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(ESParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(ESParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(ESParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(ESParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(ESParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(ESParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(ESParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterStandardComparison(ESParser.StandardComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitStandardComparison(ESParser.StandardComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterExistenceComparison(ESParser.ExistenceComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitExistenceComparison(ESParser.ExistenceComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void enterExistsOp(ESParser.ExistsOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void exitExistsOp(ESParser.ExistsOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void enterNotExistsOp(ESParser.NotExistsOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 */
	void exitNotExistsOp(ESParser.NotExistsOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(ESParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(ESParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterQuotedStringValue(ESParser.QuotedStringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitQuotedStringValue(ESParser.QuotedStringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingleQuotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterSingleQuotedStringValue(ESParser.SingleQuotedStringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleQuotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitSingleQuotedStringValue(ESParser.SingleQuotedStringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BareStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterBareStringValue(ESParser.BareStringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BareStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitBareStringValue(ESParser.BareStringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterIntValue(ESParser.IntValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitIntValue(ESParser.IntValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterDecimalValue(ESParser.DecimalValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitDecimalValue(ESParser.DecimalValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterArrayValues(ESParser.ArrayValuesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitArrayValues(ESParser.ArrayValuesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterNullValue(ESParser.NullValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitNullValue(ESParser.NullValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESParser#arrayValue}.
	 * @param ctx the parse tree
	 */
	void enterArrayValue(ESParser.ArrayValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#arrayValue}.
	 * @param ctx the parse tree
	 */
	void exitArrayValue(ESParser.ArrayValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ESParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ESParser.ValueContext ctx);
}