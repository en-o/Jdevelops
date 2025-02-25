// Generated from G:/tan/code/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES.g4 by ANTLR 4.13.2
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
	 * Enter a parse tree produced by {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(ESParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(ESParser.ComparisonContext ctx);
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
	 * Enter a parse tree produced by {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void enterValueType(ESParser.ValueTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 */
	void exitValueType(ESParser.ValueTypeContext ctx);
}