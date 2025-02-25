package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.meta.ESBaseVisitor;
import cn.tannn.jdevelops.es.antlr.meta.ESParser;
import cn.tannn.jdevelops.es.antlr.tools.FieldTransformer;
import cn.tannn.jdevelops.es.antlr.tools.ValueValidator;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.List;

/**
 * 通过visitor模式实现的es查询的组装
 * 访问者模式，主动遍历
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024-08-14 09:40:44
 */
public class EsQueryVisitor extends ESBaseVisitor<Query> {

    private final FieldTransformer fieldTransformer;
    private final List<ValueValidator> valueValidators;

    public EsQueryVisitor(FieldTransformer fieldTransformer
            , List<ValueValidator> valueValidators) {
        this.fieldTransformer = fieldTransformer;
        this.valueValidators = valueValidators;
    }

    /**
     * 处理 and 表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitAndExpression(ESParser.AndExpressionContext ctx) {
        return new BoolQuery.Builder()
                .must(visit(ctx.expression(0)))
                .must(visit(ctx.expression(1)))
                .build()
                ._toQuery();
    }

    /**
     * 处理 or 表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitOrExpression(ESParser.OrExpressionContext ctx) {
        return new BoolQuery.Builder()
                .should(visit(ctx.expression(0)))
                .should(visit(ctx.expression(1)))
                .build()
                ._toQuery();
    }

    /**
     * 表达式 [field op value]
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitComparisonExpression(ESParser.ComparisonExpressionContext ctx) {
        var comparison = ctx.comparison();
        String originalField = comparison.IDENTIFIER().getText();
        String field = fieldTransformer.transformField(originalField);
        String value = comparison.value.getText().replaceAll("^\"|\"$", "");

        // 验证值
        valueValidators.forEach(validator -> validator.validate(field, value));

        return switch (comparison.op.getText()) {
            case "==" -> new TermQuery.Builder().field(field).value(value).build()._toQuery();
            case "!=" -> new BoolQuery.Builder()
                    .mustNot(new MatchPhraseQuery.Builder().field(field).query(value).build()._toQuery())
                    .build()._toQuery();
            case ">=" -> new RangeQuery.Builder().field(field).gte(JsonData.of(value)).build()._toQuery();
            case "<=" -> new RangeQuery.Builder().field(field).lte(JsonData.of(value)).build()._toQuery();
            case ">" -> new RangeQuery.Builder().field(field).gt(JsonData.of(value)).build()._toQuery();
            case "<" -> new RangeQuery.Builder().field(field).lt(JsonData.of(value)).build()._toQuery();
            case "+=" -> new MatchQuery.Builder().field(field).query(value).build()._toQuery();
            case "=~" -> new RegexpQuery.Builder().field(field).value(value).build()._toQuery();
            case "!~" -> new BoolQuery.Builder()
                    .mustNot(new RegexpQuery.Builder().field(field).value(value).build()._toQuery())
                    .build()._toQuery();
            case "exists" -> new ExistsQuery.Builder().field(field).build()._toQuery();
            case "not exists" -> new BoolQuery.Builder()
                    .mustNot(new ExistsQuery.Builder().field(field).build()._toQuery())
                    .build()._toQuery();
            default -> throw new IllegalArgumentException("未知的操作符: " + comparison.op.getText());
        };
    }


    /**
     * 处理嵌套 （括号）
     *
     * @param ctx the parse tree
     */
    public Query visitParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }
}
