// Generated from G:/tan/code/Jdevelops/jdevelops-dals/jdevelops-dals-es/src/main/resources/ES_Upgrade.g4 by ANTLR 4.13.2
package cn.tannn.jdevelops.es.antlr.business.meta;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ES_UpgradeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ES_UpgradeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(ES_UpgradeParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(ES_UpgradeParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(ES_UpgradeParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(ES_UpgradeParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(ES_UpgradeParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ES_UpgradeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(ES_UpgradeParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StandardComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandardComparison(ES_UpgradeParser.StandardComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenComparison(ES_UpgradeParser.BetweenComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistenceComparison}
	 * labeled alternative in {@link ES_UpgradeParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistenceComparison(ES_UpgradeParser.ExistenceComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsOp(ES_UpgradeParser.ExistsOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExistsOp}
	 * labeled alternative in {@link ES_UpgradeParser#existsOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExistsOp(ES_UpgradeParser.NotExistsOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(ES_UpgradeParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QuotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedStringValue(ES_UpgradeParser.QuotedStringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnquotedStringValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedStringValue(ES_UpgradeParser.UnquotedStringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntValue(ES_UpgradeParser.IntValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DecimalValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalValue(ES_UpgradeParser.DecimalValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayValues}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValues(ES_UpgradeParser.ArrayValuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ES_UpgradeParser#valueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(ES_UpgradeParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#quotedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedString(ES_UpgradeParser.QuotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#unquotedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedString(ES_UpgradeParser.UnquotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#arrayValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValue(ES_UpgradeParser.ArrayValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ES_UpgradeParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ES_UpgradeParser.ValueContext ctx);
}