package cn.tannn.jdevelops.es.util;

import cn.hutool.core.date.DateTime;
import cn.tannn.jdevelops.es.constant.EsConstant;
import cn.tannn.jdevelops.es.dto.EsCondition;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EsUtil
 *
 * @author l
 * @version 1.0
 * @data 2023/7/13 10:44
 */
public class EsUtil {

    /**
     * 一个字段 等于多个值，and连接
     *
     * @param builder builder
     * @param field   field,如果有text类型，则需要自己加上.keyword再传入
     * @param values  values
     * @author lxw
     * @date 2023/7/13 10:45
     **/
    public static void setTermAnd(BoolQuery.Builder builder, String field, List<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        for (String str : values) {
            builder.must(
                    TermQuery.of(t -> t
                            .field(field)
                            .value(str)
                    )._toQuery()
            );
        }

    }

    /**
     * 一个字段 等于多个值，or连接
     *
     * @param builder builder
     * @param field   field,如果有text类型，则需要自己加上.keyword再传入
     * @param values  values
     * @author lxw
     * @date 2023/7/13 10:45
     **/
    public static void setTermsOr(BoolQuery.Builder builder, String field, List<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        List<FieldValue> values2 = new ArrayList<>();
        for (String str : values) {
            values2.add(FieldValue.of(str));
        }
        builder.must(
                TermsQuery.of(t -> t
                        .field(field)
                        .terms(new TermsQueryField.Builder()
                                .value(values2).build())
                )._toQuery()
        );

    }

    /**
     * 数字类型范围闭区间查询 [0,5] 等同于0<= 且 >= 5
     *
     * @param builder builder
     * @param field   field
     * @param begin   begin
     * @param end     end
     * @author lxw
     * @date 2023/7/13 10:47
     **/
    public static void setNumberEQ(BoolQuery.Builder builder, String field, Integer begin, Integer end) {
        if (begin != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .gte(JsonData.of(begin))
                    )._toQuery()
            );
        }
        if (end != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .lte(JsonData.of(end))
                    )._toQuery()
            );
        }
    }

    /**
     * 数字类型范围开区间查询 （0,5） 等同于0< 且 > 5
     *
     * @param builder builder
     * @param field   field
     * @param begin   begin
     * @param end     end
     * @author lxw
     * @date 2023/7/13 10:47
     **/
    public static void setNumberNE(BoolQuery.Builder builder, String field, Integer begin, Integer end) {
        if (begin != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .gt(JsonData.of(begin))
                    )._toQuery()
            );
        }
        if (end != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .lt(JsonData.of(end))
                    )._toQuery()
            );
        }
    }

    /**
     * 日期类型 闭区间
     *
     * @param builder builder
     * @param field   field
     * @param format  format
     * @param begin   begin
     * @param end     end
     * @author lxw
     * @date 2023/7/13 10:53
     **/
    public static void setDateEQ(BoolQuery.Builder builder, String field, TimeFormatEnum format, String begin, String end) {
        if (begin != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .gte(JsonData.of(begin))
                            .format(format.getFormat())
                    )._toQuery()
            );
        }
        if (end != null) {
            if (StringUtils.equals(format.getFormat(), TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR.getFormat())) {
                int i = Integer.parseInt(end);
                builder.must(RangeQuery.of(r -> r
                                .field(field)
                                .lt(JsonData.of(i + 1))
                                .format(format.getFormat())
                        )._toQuery()
                );
            } else {
                builder.must(RangeQuery.of(r -> r
                                .field(field)
                                .lte(JsonData.of(end))
                                .format(format.getFormat())
                        )._toQuery()
                );
            }
        }
    }

    /**
     * 日期类型 闭区间
     *
     * @param builder builder
     * @param field   field
     * @param format  format
     * @param begin   begin
     * @param end     end
     * @author lxw
     * @date 2023/7/13 10:53
     **/
    public static void setDateNE(BoolQuery.Builder builder, String field, TimeFormatEnum format, String begin, String end) {
        if (begin != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .gt(JsonData.of(begin))
                            .format(format.getFormat())
                    )._toQuery()
            );
        }
        if (end != null) {
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .lt(JsonData.of(end))
                            .format(format.getFormat())
                    )._toQuery()
            );
        }
    }

    /**
     * 设置时间等于多个值，or连接
     *
     * @param builder builder
     * @param field   field
     * @param years   year
     * @author lxw
     * @date 2023/7/13 11:06
     **/
    public static void setRangYears(BoolQuery.Builder builder, String field, List<String> years) {
        if (years == null || years.isEmpty()) {
            return;
        }
        for (String year : years) {
            int i = Integer.parseInt(year);
            builder.must(RangeQuery.of(r -> r
                            .field(field)
                            .gte(JsonData.of(i))
                            .lt(JsonData.of(i + 1))
                            .format(TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR.getFormat())
                    )._toQuery()
            );
        }
    }

    /**
     * 设置时间字段，截至当前年为止的所有数据，包含当前年
     *
     * @param builder builder
     * @param field   field
     * @author lxw
     * @date 2023/7/14 15:38
     **/
    public static void setLtCurrentYear(BoolQuery.Builder builder, String field) {
        builder.must(RangeQuery.of(r -> r
                        .field(field)
                        .lt(JsonData.of("now+1y/y"))
                )._toQuery()
        );
    }


    /***
     * 高级检索下拉多条件默认拼接（精确 match_phrase，模糊 match 。索引字段类型中，必须是text）
     * @param builder builder
     * @param highSelects highSelects
     * @author lxw
     * @date 2023/7/14 13:51
     **/
    public static void defaultHighSelect(BoolQuery.Builder builder, List<EsCondition> highSelects) {
        if (highSelects == null || highSelects.isEmpty()) {
            return;
        }
        BoolQuery.Builder builder3 = new BoolQuery.Builder();
        for (EsCondition condition : highSelects) {
            if (StringUtils.isNotBlank(condition.getFieldValue())) {
                Query query;
                if (StringUtils.equalsIgnoreCase(condition.getSymbol(), EsConstant.EQ)) {
                    query = MatchPhraseQuery.of(pre -> pre.field(condition.getField()).query(condition.getFieldValue()))._toQuery();
                } else {
                    query = MatchQuery.of(pre -> pre.field(condition.getField()).query(condition.getFieldValue()))._toQuery();
                }
                // 与 或 非
                if (StringUtils.equalsIgnoreCase(condition.getConnectSymbol(), EsConstant.AND)) {
                    builder3.must(query);
                } else if (StringUtils.equalsIgnoreCase(condition.getConnectSymbol(), EsConstant.OR)) {
                    builder3.should(query);
                } else if (StringUtils.equalsIgnoreCase(condition.getConnectSymbol(), EsConstant.NOT)) {
                    builder3.mustNot(query);
                }
            }
        }
        builder.must(builder3.build()._toQuery());
    }

    /**
     * 日期直方图取值
     *
     * @param datePrint datePrint
     * @author lxw
     * @date 2023/7/13 11:28
     **/
    public static List<Map<String, Object>> dateHistogramBucket(List<DateHistogramBucket> datePrint) {
        List<Map<String, Object>> list = new ArrayList<>();
        int year = DateTime.now().getYear();
        for (DateHistogramBucket bucket : datePrint) {
            if (bucket.docCount() > 0 && Integer.parseInt(bucket.keyAsString()) <= year) {
                Map<String, Object> map = new HashMap<>();
                map.put(bucket.keyAsString(), bucket.docCount());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 直接聚合取值使用的agg
     *
     * @param datePrint datePrint
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author lxw
     * @date 2023/7/13 11:33
     **/
    public static List<Map<String, Object>> stringTermsBucket(List<StringTermsBucket> datePrint) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StringTermsBucket bucket : datePrint) {
            Map<String, Object> map = new HashMap<>();
            map.put(bucket.key().stringValue(), bucket.docCount());
            list.add(map);
        }
        return list;
    }

    /**
     * 直接聚合取值使用的agg,且只返回key的集合
     *
     * @param datePrint datePrint
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author lxw
     * @date 2023/7/13 11:33
     **/
    public static List<String> stringTermsBucket2ListKey(List<StringTermsBucket> datePrint) {
        List<String> list = new ArrayList<>();
        for (StringTermsBucket bucket : datePrint) {
            list.add(bucket.key().stringValue());
        }
        return list;
    }


}
