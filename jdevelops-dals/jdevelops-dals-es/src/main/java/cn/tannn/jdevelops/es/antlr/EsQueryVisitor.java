package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.meta.ESBaseVisitor;
import cn.tannn.jdevelops.es.antlr.meta.ESParser;
import cn.tannn.jdevelops.es.antlr.tools.EsQueryFun;
import cn.tannn.jdevelops.es.antlr.tools.FieldTransformer;
import cn.tannn.jdevelops.es.antlr.tools.ValueValidator;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.ArrayList;
import java.util.Arrays;
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
     *   数据标注验证
     * @param ctx the parse tree
     * @return Query
     */
    @Override
    public Query visitStandardComparison(ESParser.StandardComparisonContext ctx) {
        String originalField = ctx.IDENTIFIER().getText();
        String field = fieldTransformer.transformField(originalField);
        String operator = ctx.operator().getText();
        String rawValue = getValueFromContext(ctx.valueType());

        // 验证值
        if (valueValidators != null && !valueValidators.isEmpty()) {
            valueValidators.forEach(validator -> validator.validate(field, rawValue));
        }

        return buildQueryFromOperator(field, operator, rawValue, ctx.valueType());
    }

    /**
     *  exists 处理
     * @param ctx the parse tree
     * @return Query
     */
    @Override
    public Query visitExistenceComparison(ESParser.ExistenceComparisonContext ctx) {
        String field = fieldTransformer.transformField(ctx.IDENTIFIER().getText());

        // 使用 instanceof 来判断是否是 NotExistsOp
        boolean isNotExists = ctx.existsOperator() instanceof ESParser.NotExistsOpContext;

        var existsQuery = new ExistsQuery.Builder()
                .field(field)
                .build()
                ._toQuery();

        if (isNotExists) {
            return new BoolQuery.Builder()
                    .mustNot(existsQuery)
                    .build()
                    ._toQuery();
        }
        return existsQuery;
    }

    private String getValueFromContext(ESParser.ValueTypeContext ctx) {
        if (ctx instanceof ESParser.QuotedStringValueContext) {
            String text = ctx.getText();
            // 移除引号
            return text.substring(1, text.length() - 1);
        } else if (ctx instanceof ESParser.UnquotedStringValueContext) {
            // 直接返回不带引号的字符串
            return ctx.getText();
        } else if (ctx instanceof ESParser.IntValueContext ||
                ctx instanceof ESParser.DecimalValueContext) {
            return ctx.getText();
        } else if (ctx instanceof ESParser.ArrayValuesContext arrayCtx) {
            List<String> values = new ArrayList<>();
            for (ESParser.ValueContext valueCtx : arrayCtx.arrayValue().value()) {
                if (valueCtx.quotedString() != null) {
                    String text = valueCtx.quotedString().getText();
                    values.add(text.substring(1, text.length() - 1));
                } else if (valueCtx.unquotedString() != null) {
                    values.add(valueCtx.unquotedString().getText());
                } else {
                    values.add(valueCtx.getText());
                }
            }
            return String.join(",", values);
        } else if (ctx instanceof ESParser.NullValueContext) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported value type: " + ctx.getText());
    }


    private Query buildQueryFromOperator(String field, String operator, String value, ESParser.ValueTypeContext valueCtx) {
        String lowerCase = operator.toLowerCase();
        return switch (lowerCase) {
            case "==" -> EsQueryFun.buildTermQuery(field, value);
            case "!=" -> EsQueryFun.buildNotQuery(EsQueryFun.buildTermQuery(field, value));
            case ">=" -> EsQueryFun.buildRangeQuery(field, value, EsQueryFun.RangeType.GTE);
            case "<=" -> EsQueryFun.buildRangeQuery(field, value, EsQueryFun.RangeType.LTE);
            case ">" -> EsQueryFun.buildRangeQuery(field, value, EsQueryFun.RangeType.GT);
            case "<" -> EsQueryFun.buildRangeQuery(field, value, EsQueryFun.RangeType.LT);
            case "+=" -> EsQueryFun.buildMatchQuery(field, value);
            case "=+" -> EsQueryFun.buildPrefixQuery(field, value);
            case "=~" -> EsQueryFun.buildRegexpQuery(field, value, false);
            case "!~" -> EsQueryFun.buildNotQuery(EsQueryFun.buildRegexpQuery(field, value, false));
            case "in", "not in" -> EsQueryFun.buildTermsQuery(field, value, valueCtx, lowerCase.equals("not in"));
            default -> throw new IllegalArgumentException("未知的操作符: " + lowerCase);
        };
    }



    /**
     * 处理嵌套 （括号）
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitParenthesizedExpression(ESParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }




}
