package cn.tannn.jdevelops.es.antlr.business;

import cn.tannn.jdevelops.es.antlr.business.meta.ES_UpgradeBaseVisitor;
import cn.tannn.jdevelops.es.antlr.business.meta.ES_UpgradeParser;
import cn.tannn.jdevelops.es.antlr.tools.FieldTransformer;
import cn.tannn.jdevelops.es.antlr.tools.ValueValidator;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.*;

import static cn.tannn.jdevelops.es.antlr.tools.EsQueryFun.convertToFieldValues;

/**
 * 通过visitor模式实现的es查询的组装
 * 访问者模式，主动遍历
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024-08-14 09:40:44
 */
public class BusEsQueryVisitor extends ES_UpgradeBaseVisitor<Query> {
    /**
     * 键转换
     */
    private final FieldTransformer fieldTransformer;
    /**
     * 值验证
     */
    private final List<ValueValidator> valueValidators;

    /**
     *  添加特殊值映射集合
     */
    private final Map<String, List<String>> specialValues;

    /**
     *
     * @param fieldTransformer 键转换
     * @param valueValidators 值验证
     * @param specialValues 特殊值映射集合
     */
    public BusEsQueryVisitor(FieldTransformer fieldTransformer,
                             List<ValueValidator> valueValidators,
                             Map<String, List<String>> specialValues) {
        this.fieldTransformer = fieldTransformer;
        this.valueValidators = valueValidators;
        this.specialValues = specialValues;
    }

    /**
     * 处理 and 表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitAndExpression(ES_UpgradeParser.AndExpressionContext ctx) {
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
    public Query visitOrExpression(ES_UpgradeParser.OrExpressionContext ctx) {
        return new BoolQuery.Builder()
                .should(visit(ctx.expression(0)))
                .should(visit(ctx.expression(1)))
                .minimumShouldMatch("1")
                .build()
                ._toQuery();
    }

    /**
     * 处理 not  表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitNotExpression(ES_UpgradeParser.NotExpressionContext ctx) {
        return new BoolQuery.Builder()
                .mustNot(visit(ctx.expression()))
                .build()
                ._toQuery();
    }

    /**
     * 处理 Between  表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public Query visitBetweenComparison(ES_UpgradeParser.BetweenComparisonContext ctx) {
        String field = fieldTransformer.transformField(ctx.IDENTIFIER().getText());
        String fromValue = getValueFromContext(ctx.valueType(0));
        String toValue = getValueFromContext(ctx.valueType(1));

        return new RangeQuery.Builder()
                .field(field)
                .gte(JsonData.of(fromValue))
                .lte(JsonData.of(toValue))
                .build()
                ._toQuery();
    }

    @Override
    public Query visitStandardComparison(ES_UpgradeParser.StandardComparisonContext ctx) {
        String originalField = ctx.IDENTIFIER().getText();
        String field = fieldTransformer.transformField(originalField);
        String operator = ctx.operator().getText();
        String rawValue = getValueFromContext(ctx.valueType());

        // 验证值
        if (valueValidators != null && !valueValidators.isEmpty()) {
            valueValidators.forEach(validator -> validator.validate(field, rawValue));
        }

        // 检查是否是特殊值集合
        if (specialValues != null && specialValues.containsKey(field)) {
            List<String> allowedValues = specialValues.get(field);
            if (allowedValues.contains(rawValue)) {
                // 使用特殊查询逻辑
                return buildSpecialQuery(field, rawValue);
            }
        }

        return buildQueryFromOperator(field, operator, rawValue, ctx.valueType());
    }

    private Query buildSpecialQuery(String field, String value) {
        // 这里实现特殊查询逻辑
        // 示例：可以根据不同的field和value组合使用不同的查询方式
        return new TermQuery.Builder()
                .field(field)
                .value(value)
                .boost(2.0f)  // 提高权重
                .build()
                ._toQuery();
    }

    private Query buildQueryFromOperator(String field, String operator, String value, ES_UpgradeParser.ValueTypeContext valueCtx) {
        return switch (operator.toLowerCase()) {
            case "==" -> new TermQuery.Builder()
                    .field(field)
                    .value(value)
                    .build()
                    ._toQuery();
            case "!=" -> new BoolQuery.Builder()
                    .mustNot(new TermQuery.Builder()
                            .field(field)
                            .value(value)
                            .build()._toQuery())
                    .build()
                    ._toQuery();
            case ">" -> new RangeQuery.Builder()
                    .field(field)
                    .gt(JsonData.of(value))
                    .build()
                    ._toQuery();
            case ">=" -> new RangeQuery.Builder()
                    .field(field)
                    .gte(JsonData.of(value))
                    .build()
                    ._toQuery();
            case "<" -> new RangeQuery.Builder()
                    .field(field)
                    .lt(JsonData.of(value))
                    .build()
                    ._toQuery();
            case "<=" -> new RangeQuery.Builder()
                    .field(field)
                    .lte(JsonData.of(value))
                    .build()
                    ._toQuery();
            case "%" -> new WildcardQuery.Builder()
                    .field(field)
                    .value("*" + value + "*")
                    .build()
                    ._toQuery();
            case "%=" -> new FuzzyQuery.Builder()
                    .field(field)
                    .value(value)
                    .build()
                    ._toQuery();
            case "in", "not in" -> buildTermsQuery(field, value, valueCtx, operator.equals("not in"));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private Query buildTermsQuery(String field, String value, ES_UpgradeParser.ValueTypeContext valueCtx, boolean isNot) {
        List<String> values = parseArrayValues(valueCtx);
        var termsQuery = new TermsQuery.Builder()
                .field(field)
                .terms(b -> b.value(convertToFieldValues(Arrays.asList(value.split(",")))))
                .build()
                ._toQuery();

        if (isNot) {
            return new BoolQuery.Builder()
                    .mustNot(termsQuery)
                    .build()
                    ._toQuery();
        }
        return termsQuery;
    }

    private List<String> parseArrayValues(ES_UpgradeParser.ValueTypeContext valueCtx) {
        if (valueCtx instanceof ES_UpgradeParser.ArrayValuesContext arrayCtx) {
            List<String> values = new ArrayList<>();
            for (ES_UpgradeParser.ValueContext valueContext : arrayCtx.arrayValue().value()) {
                values.add(getValueFromValue(valueContext));
            }
            return values;
        }
        return Collections.singletonList(getValueFromContext(valueCtx));
    }

    private String getValueFromValue(ES_UpgradeParser.ValueContext ctx) {
        if (ctx.quotedString() != null) {
            String text = ctx.quotedString().getText();
            return text.substring(1, text.length() - 1);
        }
        return ctx.getText();
    }

    private String getValueFromContext(ES_UpgradeParser.ValueTypeContext ctx) {
        if (ctx instanceof ES_UpgradeParser.QuotedStringValueContext) {
            String text = ctx.getText();
            // 移除引号
            return text.substring(1, text.length() - 1);
        } else if (ctx instanceof ES_UpgradeParser.UnquotedStringValueContext) {
            // 直接返回不带引号的字符串
            return ctx.getText();
        } else if (ctx instanceof ES_UpgradeParser.IntValueContext ||
                ctx instanceof ES_UpgradeParser.DecimalValueContext) {
            return ctx.getText();
        } else if (ctx instanceof ES_UpgradeParser.ArrayValuesContext arrayCtx) {
            List<String> values = new ArrayList<>();
            for (ES_UpgradeParser.ValueContext valueCtx : arrayCtx.arrayValue().value()) {
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
        } else if (ctx instanceof ES_UpgradeParser.NullValueContext) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported value type: " + ctx.getText());
    }
}
