package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.meta.ESBaseVisitor;
import cn.tannn.jdevelops.es.antlr.meta.ESParser;
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

        // 获取操作符
        String operator = ctx.operator().getText();

        // 获取值
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
        if (ctx instanceof ESParser.StringValueContext) {
            String text = ctx.getText();
            // 如果是带引号的字符串，去掉引号
            if ((text.startsWith("\"") && text.endsWith("\"")) ||
                    (text.startsWith("'") && text.endsWith("'"))) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        } else if (ctx instanceof ESParser.NumberValueContext) {
            return ctx.getText();
        } else if (ctx instanceof ESParser.ArrayValuesContext arrayCtx) {
            List<String> values = new ArrayList<>();
            ESParser.ArrayValueContext array = arrayCtx.arrayValue();
            for (ESParser.ValueContext valueCtx : array.value()) {
                String value = valueCtx.getText();
                // 处理数组中的带引号字符串
                if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                values.add(value);
            }
            return String.join(",", values);
        } else if (ctx instanceof ESParser.NullValueContext) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported value type: " + ctx.getText());
    }


    private Query buildQueryFromOperator(String field, String operator, String value, ESParser.ValueTypeContext valueCtx) {
        return switch (operator) {
            case "==" -> new TermQuery.Builder().field(field).value(value).build()._toQuery();
            case "!=" -> new BoolQuery.Builder()
                    .mustNot(new TermQuery.Builder().field(field).value(value).build()._toQuery())
                    .build()._toQuery();
            case ">=" -> new RangeQuery.Builder().field(field).gte(JsonData.of(value)).build()._toQuery();
            case "<=" -> new RangeQuery.Builder().field(field).lte(JsonData.of(value)).build()._toQuery();
            case ">" -> new RangeQuery.Builder().field(field).gt(JsonData.of(value)).build()._toQuery();
            case "<" -> new RangeQuery.Builder().field(field).lt(JsonData.of(value)).build()._toQuery();
            case "+=" -> new MatchQuery.Builder().field(field).query(value).build()._toQuery();
            case "=~" -> new WildcardQuery.Builder() // 使用wildcard查询替代regexp查询，提供更好的模式匹配支持
                    .field(field)
                    .wildcard(value)
                    .caseInsensitive(true)
                    .build()
                    ._toQuery();
            case "!~" -> new BoolQuery.Builder()  // 对于否定的模式匹配，使用must_not + wildcard
                    .mustNot(new WildcardQuery.Builder()
                            .field(field)
                            .wildcard(value)
                            .caseInsensitive(true)
                            .build()
                            ._toQuery())
                    .build()
                    ._toQuery();
            case "in", "not in" -> {
                if (!(valueCtx instanceof ESParser.ArrayValuesContext)) {
                    throw new IllegalArgumentException("Operator '" + operator + "' requires an array value");
                }
                var termsQuery = new TermsQuery.Builder()
                        .field(field)
                        .terms(b -> b.value(convertToFieldValues(Arrays.asList(value.split(",")))))
                        .build()
                        ._toQuery();

                if (operator.equals("in")) {
                    yield termsQuery;
                } else {
                    yield new BoolQuery.Builder()
                            .mustNot(termsQuery)
                            .build()
                            ._toQuery();
                }
            }
            default -> throw new IllegalArgumentException("未知的操作符: " + operator);
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

    /**
     * 将字符串列表转换为 FieldValue 列表
     * @param values 字符串值列表
     * @return FieldValue 列表
     */
    private List<FieldValue> convertToFieldValues(List<String> values) {
        return values.stream()
                .map(value -> {
                    // 尝试将字符串解析为数字
                    try {
                        if (value.contains(".")) {
                            return FieldValue.of(Double.parseDouble(value));
                        } else {
                            return FieldValue.of(Long.parseLong(value));
                        }
                    } catch (NumberFormatException e) {
                        // 如果不是数字，则作为字符串处理
                        return FieldValue.of(value);
                    }
                })
                .toList();
    }


}
