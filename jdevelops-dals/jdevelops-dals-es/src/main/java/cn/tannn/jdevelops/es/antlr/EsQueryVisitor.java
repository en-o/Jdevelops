package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.meta.ESBaseVisitor;
import cn.tannn.jdevelops.es.antlr.meta.ESParser;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

/**
 * 通过visitor模式实现的es查询的组装
 * 访问者模式，主动遍历
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024-08-14 09:40:44
 */
public class EsQueryVisitor extends ESBaseVisitor<Query> {

    /**
     * 处理 and 表达式
     * @param ctx the parse tree
     */
    @Override
    public Query visitAndExpression(ESParser.AndExpressionContext ctx) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        // and的左边
        boolQuery.must(visit(ctx.expression(0)));
        // and的右边
        boolQuery.must(visit(ctx.expression(1)));
        return boolQuery.build()._toQuery();
    }

    /**
     * 处理 or 表达式
     * @param ctx the parse tree
     */
    @Override
    public Query visitOrExpression(ESParser.OrExpressionContext ctx) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        // or的左边
        boolQuery.should(visit(ctx.expression(0)));
        // or的右边
        boolQuery.should(visit(ctx.expression(1)));
        return boolQuery.build()._toQuery();
    }

    /**
     *  表达式 [field op value]
     * @param ctx the parse tree
     */
    @Override
    public Query visitComparisonExpression(ESParser.ComparisonExpressionContext ctx) {
        ESParser.ComparisonContext comparison = ctx.comparison();
        String field = comparison.IDENTIFIER().getText();
        String value = comparison.value.getText().replaceAll("^\"|\"$", "");
        String operator = comparison.op.getText();
        switch (operator) {
            case "==":
                return TermQuery.of(r -> r.field(field).value(value))._toQuery();
            case "!=":
                return QueryBuilders.bool().mustNot(MatchPhraseQuery.of(pre -> pre.field(field).query(value))._toQuery()).build()._toQuery();
            case ">=":
                return RangeQuery.of(r -> r.field(field).gte(JsonData.of(value)))._toQuery();
            case "<=":
                return RangeQuery.of(r -> r.field(field).lte(JsonData.of(value)))._toQuery();
            case ">":
                return RangeQuery.of(r -> r.field(field).gt(JsonData.of(value)))._toQuery();
            case "<":
                return RangeQuery.of(r -> r.field(field).lt(JsonData.of(value)))._toQuery();
            case "+=":
                return MatchQuery.of(r -> r.field(field).query(value))._toQuery();
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }



    /**
     * 处理嵌套 （括号）
     * @param ctx the parse tree
     */
    @Override
    public Query visitParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }
}
