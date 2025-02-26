package cn.tannn.jdevelops.es.antlr.tools;

import cn.tannn.jdevelops.es.antlr.meta.ESParser;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.Arrays;
import java.util.List;

/**
 * es查询方法
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/26 09:39
 */
public class EsQueryFun {

    /**
     * 构建精确匹配查询
     */
    public static Query buildTermQuery(String field, String value) {
        return new TermQuery.Builder()
                .field(field)
                .value(value)
                .build()
                ._toQuery();
    }

    /**
     * 构建否定查询
     */
    public static Query buildNotQuery(Query query) {
        return new BoolQuery.Builder()
                .mustNot(query)
                .build()
                ._toQuery();
    }

    /**
     * 构建范围查询
     */
    public static Query buildRangeQuery(String field, String value, RangeType rangeType) {
        RangeQuery.Builder builder = new RangeQuery.Builder().field(field);
        return switch (rangeType) {
            case GT -> builder.gt(JsonData.of(value)).build()._toQuery();
            case GTE -> builder.gte(JsonData.of(value)).build()._toQuery();
            case LT -> builder.lt(JsonData.of(value)).build()._toQuery();
            case LTE -> builder.lte(JsonData.of(value)).build()._toQuery();
        };
    }

    /**
     * 构建模糊匹配查询(text)
     */
    public static Query buildMatchQuery(String field, String value) {
        return new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()
                ._toQuery();
    }

    /**
     * 模糊匹配 - 单词（keyword）前缀匹配，适用于不分词字段
     */
    public static Query buildPrefixQuery(String field, String value) {
        return PrefixQuery.of(r -> r.field(field).value(value))._toQuery();
    }
    /**
     * 构建正则表达式查询
     */
    public static Query buildRegexpQuery(String field, String value, boolean caseSensitive) {
        return new RegexpQuery.Builder()
                .field(field)
                .value(value)
                .caseInsensitive(!caseSensitive)
                .build()
                ._toQuery();
    }

    /**
     * 构建词条集合查询
     */
    public static Query buildTermsQuery(String field, String value, ESParser.ValueTypeContext valueCtx, boolean isNot) {
        if (!(valueCtx instanceof ESParser.ArrayValuesContext)) {
            throw new IllegalArgumentException("数组操作符需要数组值类型");
        }

        var termsQuery = new TermsQuery.Builder()
                .field(field)
                .terms(b -> b.value(convertToFieldValues(Arrays.asList(value.split(",")))))
                .build()
                ._toQuery();

        return isNot ? buildNotQuery(termsQuery) : termsQuery;
    }

    /**
     * 范围查询类型枚举
     */
    public  enum RangeType {
        GT, GTE, LT, LTE
    }

    /**
     * 转换字段值为 FieldValue 列表
     */
    public static List<FieldValue> convertToFieldValues(List<String> values) {
        return values.stream()
                .map(String::trim)
                .map(value -> {
                    try {
                        if (value.contains(".")) {
                            return FieldValue.of(Double.parseDouble(value));
                        } else {
                            return FieldValue.of(Long.parseLong(value));
                        }
                    } catch (NumberFormatException e) {
                        return FieldValue.of(value);
                    }
                })
                .toList();
    }
}
