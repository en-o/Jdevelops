// Generated from G:/tan/code/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES.g4 by ANTLR 4.13.2
package cn.tannn.jdevelops.es.antlr.demo.meta;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ESParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ESVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ESParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(ESParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(ESParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(ESParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ESParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(ESParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandardComparison(ESParser.StandardComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ESParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistenceComparison(ESParser.ExistenceComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsOp(ESParser.ExistsOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ESParser#existsOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExistsOp(ESParser.NotExistsOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ESParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(ESParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedStringValue(ESParser.QuotedStringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnquotedStringValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedStringValue(ESParser.UnquotedStringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntValue(ESParser.IntValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalValue(ESParser.DecimalValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValues(ESParser.ArrayValuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ESParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(ESParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ESParser#quotedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedString(ESParser.QuotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ESParser#unquotedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedString(ESParser.UnquotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ESParser#arrayValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValue(ESParser.ArrayValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ESParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ESParser.ValueContext ctx);
}
