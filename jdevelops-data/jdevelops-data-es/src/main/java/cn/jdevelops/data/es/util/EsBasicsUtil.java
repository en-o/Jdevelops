package cn.jdevelops.data.es.util;

import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.data.es.constant.EsConstant;
import cn.jdevelops.data.es.dto.SortDTO;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Es基础工具类
 *
 * @author lxw
 * @version V1.0
 * @date 2021/12/14
 **/
public class EsBasicsUtil {


    /**
     * 判断拼接条件，返回查询块
     *
     * @param field      字段
     * @param fieldValue 值
     * @param symbol     匹配类型
     *                   <p>
     *                   PS:symbol包含：<br/>
     *                   EQ 就是 EQUAL等于 <br/>
     *                   GT 就是 GREATER THAN大于　<br/>
     *                   LT 就是 LESS THAN小于 <br/>
     *                   GTE 就是 GREATER THAN OR EQUAL 大于等于 <br/>
     *                   LTE 就是 LESS THAN OR EQUAL 小于等于 <br/>
     *                   LIKE  就是 模糊 <br/>
     *                   <p/>
     * @return org.elasticsearch.index.query.BoolQueryBuilder
     */
    public static Query setConSymbol(String field, String fieldValue, String symbol) {

        switch (symbol.trim()) {
            // GT 就是 GREATER THAN大于 不要加“.keyword”
            case EsConstant.GT:
                return RangeQuery.of(r -> r.field(field).gt(JsonData.of(fieldValue)))._toQuery();
            // LT 就是 LESS THAN小于 不要加“.keyword”
            case EsConstant.LT:
                return RangeQuery.of(r -> r.field(field).lt(JsonData.of(fieldValue)))._toQuery();
            // GE 就是 GREATER THAN OR EQUAL 大于等于 不要加“.keyword”
            case EsConstant.GTE:
                return RangeQuery.of(r -> r.field(field).gte(JsonData.of(fieldValue)))._toQuery();
            // LE 就是 LESS THAN OR EQUAL 小于等于 不要加“.keyword”
            case EsConstant.LTE:
                return RangeQuery.of(r -> r.field(field).lte(JsonData.of(fieldValue)))._toQuery();
            // LIKE  就是 模糊 不要加“.keyword”
            case EsConstant.LIKE:
                return MatchQuery.of(m -> m.field(field).query(fieldValue))._toQuery();
            default:
                // 默认返回 ( EQ 就是 EQUAL等于)必须加 “.keyword”
                return TermQuery.of(r -> r.field(field + ".keyword").value(fieldValue))._toQuery();
        }
    }

    /**
     * 多个字段模糊匹配同一个值
     *
     * @param term   模糊匹配值
     * @param fields 需要查询的字段
     * @return org.elasticsearch.index.query.BoolQueryBuilder
     * @author lxw
     * @date 2021/12/14 14:30
     */
    public static BoolQuery.Builder setFieldsLike(String term, List<String> fields) {
        BoolQuery.Builder bq2 = new BoolQuery.Builder();
        for (String field : fields) {
            if (isBlank(field)) {
                continue;
            }
            bq2.should(MatchPhraseQuery.of(m -> m.field(field).query(term))._toQuery());
        }
        return bq2;
    }


    /**
     * 拼接嵌套条件
     *
     * @param nested       索引嵌套结构名称
     * @param term         检索词
     * @param nestedFields 被检索的嵌套字段
     */
    public static void setNested(BoolQuery.Builder boolQuery, String nested, String term, List<String> nestedFields) {
        if (isNotBlank(term)) {
            BoolQuery.Builder bs = new BoolQuery.Builder();
            for (String field : nestedFields) {
                if (isNotBlank(field)) {
                    bs.should(NestedQuery.of(n -> n.path(nested).query(
                                    MatchPhraseQuery.of(m -> m.
                                            field(nested + "." + field)
                                            .query(term))._toQuery())
                            )._toQuery()
                    );
                }
            }
            boolQuery.must(bs.build()._toQuery());
        }
    }

    /**
     * 设置两个字段相等
     * <p>类似于 sql中的 where 字段1=字段2</p>
     * <p>特别备注: sourceField、targetField 只能是keyword类型，或者数字，不能是text</p>
     *
     * @param boolQuery   boolQuery
     * @param sourceField 字段1
     * @param targetField 字段2
     * @author lxw
     * @date 2023/6/5 16:55
     **/
    public static void setFieldValueEq(BoolQuery.Builder boolQuery, String sourceField, String targetField) {
        boolQuery.must(ScriptQuery.of(o -> o
                .script(Script.of(s -> s
                                .inline(l -> l
                                        .source("doc['" + sourceField + "'] == doc['" + targetField + "']")
                                )
                        )
                )
        )._toQuery());
    }

    /**
     * 设置返回数据中只需要的字段
     *
     * @param sourceBuilder 查询条件对象
     * @param includes      需要返回的字段
     * @author lxw
     */
    public static void setIncludesFields(SearchRequest.Builder sourceBuilder, String includes) {
        if (isNotBlank(includes)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            sourceBuilder.source(c -> c.filter(f -> f.includes(Arrays.asList(includes.split(",")))));
        }
    }

    /**
     * 设置返回数据中需要排除字段
     *
     * @param sourceBuilder 查询条件对象
     * @param excludes      需要排除字段
     * @author lxw
     */
    public static void setExcludesFields(SearchRequest.Builder sourceBuilder, String excludes) {
        if (isNotBlank(excludes)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            sourceBuilder.source(c -> c.filter(f -> f.excludes(Arrays.asList(excludes.split(",")))));
        }
    }

    /**
     * 设置返回字段、排除字段
     *
     * @param sourceBuilder 查询条件对象
     * @param includes      需要返回的字段
     * @param excludes      需要排除字段
     * @author lxw
     */
    public static void setExcludesFields(SearchRequest.Builder sourceBuilder, String includes, String excludes) {
        if (isNotBlank(includes) && isNotBlank(excludes)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            sourceBuilder.source(c -> c.filter(f -> f
                    .includes(Arrays.asList(includes.split(",")))
                    .excludes(Arrays.asList(excludes.split(",")))
            ));
        }
    }


    /**
     * 设置分页 （传入正常的前端 pageIndex 从1开始）
     *
     * @param builder   查询对象
     * @param startPage 起始页
     * @param pageSize  每页大小
     * @author lxw
     * @date 2021/5/11 16:28
     */
    public static void setPage(SearchRequest.Builder builder, Integer startPage, Integer pageSize) {
        if (startPage > 0) {
            startPage = startPage - 1;
        } else {
            startPage = 0;
        }
        //设置分页参数
        int startIndex = startPage * pageSize;
        builder.from(startIndex);
        builder.size(pageSize);
        builder.trackTotalHits(t -> t.enabled(true));
    }

    /**
     * 设置分页 (PageDTO.getPageIndex() 已经处理了一些判断)
     *
     * @param builder   查询对象
     * @param page PageDTO
     * @author lxw
     * @date 2021/5/11 16:28
     */
    public static void setPage(SearchRequest.Builder builder, PageDTO page) {
        //设置分页参数
        int startIndex = page.getPageIndex() * page.getPageSize();
        builder.from(startIndex);
        builder.size(page.getPageSize());
        builder.trackTotalHits(t -> t.enabled(true));
    }

    /**
     * 设置排序
     *
     * @param builder   查询对象
     * @param sort   SortDTO
     * @author lxw
     * @date 2023/2/16 9:57
     **/
    public static void setOrder(SearchRequest.Builder builder, SortDTO sort) {
        if (isNotBlank(sort.getOrderBy())) {
            SortOrder asc = SortOrder.Asc;
            if (sort.getOrderDesc() == 1) {
                asc = SortOrder.Desc;
            }
            SortOrder finalAsc = asc;
            builder.sort(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field(sort.getOrderBy()).order(finalAsc)));
        }
    }


    /**
     * 设置排序
     *
     * @param builder   查询对象
     * @param orderBy   排序字段
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @author lxw
     * @date 2023/2/16 9:57
     **/
    public static void setOrder(SearchRequest.Builder builder, String orderBy, Integer orderDesc) {
        if (isNotBlank(orderBy)) {
            SortOrder asc = SortOrder.Asc;
            if (orderDesc == 1) {
                asc = SortOrder.Desc;
            }
            SortOrder finalAsc = asc;
            builder.sort(sortOptionsBuilder -> sortOptionsBuilder
                    .field(fieldSortBuilder -> fieldSortBuilder
                            .field(orderBy).order(finalAsc)));
        }
    }

    /**
     * 设置排序
     *
     * @param builder  查询对象
     * @param orderBy  排序字段
     * @param finalAsc 正序0--SortOrder.ASC，反序--SortOrder.DESC
     * @author lxw
     * @date 2023/2/16 9:57
     **/

    public static void setOrder(SearchRequest.Builder builder, String orderBy, SortOrder finalAsc) {
        builder.sort(sortOptionsBuilder -> sortOptionsBuilder
                .field(fieldSortBuilder -> fieldSortBuilder
                        .field(orderBy).order(finalAsc)));
    }

    /**
     * 设置高亮
     *
     * @param builder        查询对象
     * @param highlightField 高亮字段
     * @author lxw
     * @date 2021/5/11 16:31
     */
    public static void setHighlightField(SearchRequest.Builder builder, String highlightField) {
        if (isNotBlank(highlightField)) {
            //高亮
            Highlight.Builder highlightBuilder = new Highlight.Builder();
            highlightBuilder.fields(highlightField, new HighlightField.Builder().build())
                    .preTags("<span style='color:red'>")
                    .postTags("</span>")
                    .requireFieldMatch(false);
            builder.highlight(highlightBuilder.build());
        }
    }

    /**
     * 设置高亮
     *
     * @param builder         查询对象
     * @param highlightFields 高亮字段
     * @author lxw
     * @date 2021/5/11 16:31
     */
    public static void setHighlightField(SearchRequest.Builder builder, List<String> highlightFields) {
        if (!CollectionUtils.isEmpty(highlightFields)) {
            //高亮
            Highlight.Builder highlightBuilder = new Highlight.Builder();
            for (String field : highlightFields) {
                highlightBuilder.fields(field, new HighlightField.Builder().build());
            }
            highlightBuilder.preTags("<span style='color:red'>")
                    .postTags("</span>")
                    .requireFieldMatch(false)
                    //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
                    //最大高亮分片数
                    .fragmentSize(800000)
                    //从第一个分片获取高亮片段
                    .numberOfFragments(0);
            builder.highlight(highlightBuilder.build());
        }
    }

    /**
     * 高亮结果集处理示例
     *
     * @param searchResponse 结果集
     * @param highlightField 高亮字段
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author lxw
     * @date 2021/6/23 13:07
     */
    public static <T> List<T> handleSearchResponse(SearchResponse<T> searchResponse, String
            highlightField, Class<T> cc) {
        //解析结果
        List<T> list = new ArrayList<>();
        List<Hit<T>> hits = searchResponse.hits().hits();
        for (Hit<T> hit : hits) {
            T source = hit.source();
            //获取高亮数据添加到实体类中返回
            Map<String, List<String>> highlightMap = hit.highlight();
            if (highlightMap != null && !highlightMap.isEmpty()) {
                List<String> list1 = highlightMap.get(highlightField);
                highlightMap.keySet().forEach(k -> {
                    try {
                        if (k.contains("keyword")) {
                            k = k.replace(".keyword", "");
                        }
                        Class<?> clazz = source.getClass();
                        Field field = clazz.getDeclaredField(k);
                        field.setAccessible(true);
                        field.set(source, highlightMap.get(k).get(0));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            list.add(source);
        }
        return list;
    }


    private static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    private static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    private static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}
