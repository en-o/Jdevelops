package cn.tannn.jdevelops.es.util;

import cn.tannn.jdevelops.es.exception.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.HighlighterType;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Es 查询 前置处理条件
 **/
public class EsSearchBeforeUtil {

    /**
     * fix: Result window is too large, from + size must be less than or equal to: [10000] but was [10010]. See the scroll api for a more efficient way to request large data sets.
     * This limit can be set by changing the [index.max_result_window] index level setting.
     * 设置分页 （传入正常的前端 pageIndex 从1开始）
     *
     * @param searchBuilder 查询对象
     * @param startPage     起始页,从1开始
     * @param pageSize      每页大小
     */
    public static void setPage(SearchRequest.Builder searchBuilder, Integer startPage, Integer pageSize) {
        if (startPage > 0) {
            startPage = startPage - 1;
        } else {
            startPage = 0;
        }
        //设置分页参数
        int startIndex = startPage * pageSize;

        if (startIndex > 10000 || (startIndex + pageSize) > 10000) {
            throw new ElasticsearchException("查询数据不允许超过1万条");
        }

        searchBuilder.from(startIndex);
        searchBuilder.size(pageSize);
    }



    /**
     * 设置排序
     *
     * @param searchBuilder 查询对象
     * @param field         排序字段
     * @param way           正序0--Direction.ASC，反序1--Direction.DESC
     **/
    public static void setOrder(SearchRequest.Builder searchBuilder, String field, Integer way) {
        if (isNotBlank(field)) {
            searchBuilder.sort(s -> s.field(getOrder(field, way)));
        }
    }

    /**
     * 获取排序
     *
     * @param field 字段
     * @param way   排序方式，正序0--Direction.ASC，反序1--Direction.DESC
     * @return co.elastic.clients.elasticsearch._types.FieldSort
     * @author lxw
     * @date 2024/11/8 14:05
     **/
    public static FieldSort getOrder(String field, Integer way) {
        SortOrder asc;
        if (way == 1) {
            asc = SortOrder.Desc;
        } else {
            asc = SortOrder.Asc;
        }
        return FieldSort.of(s -> s.field(field).order(asc));
    }

    /**
     * 设置高亮
     *
     * @param builder        查询对象
     * @param highlightField 高亮字段
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
     */
    public static void setHighlightField(SearchRequest.Builder builder, List<String> highlightFields) {
        if (!CollectionUtils.isEmpty(highlightFields)) {
            //高亮
            Highlight.Builder highlightBuilder = new Highlight.Builder();
            highlightBuilder.preTags("<span style='color:red'>")
                    .postTags("</span>")
                    //主要用于处理多字段高亮的情况。它的作用是指示在多字段高亮时，是否要求每个字段的高亮部分必须都匹配查询的字段。默认值： true
                    //如果为 true，在多字段查询时，每个字段必须匹配查询条件才能返回高亮。
                    //如果为 false，只要一个字段匹配查询条件，就返回该字段的高亮部分。
                    .requireFieldMatch(false)
                    .type(HighlighterType.Unified)
                    //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
                    //最大高亮分片数
                    .fragmentSize(800000)
                    //从第一个分片获取高亮片段
                    .numberOfFragments(0);
            for (String field : highlightFields) {
                highlightBuilder.fields(field, new HighlightField.Builder().build());
            }
            builder.highlight(highlightBuilder.build());
        }
    }


    /**
     * 设置限定字段
     *
     * @param searchBuilder sourceBuilder
     * @param includes      需要返回的字段
     * @param excludes      需要排除字段
     **/
    public static void setExcludesFields(SearchRequest.Builder searchBuilder, List<String> includes, List<String> excludes) {
        if (CollectionUtils.isEmpty(excludes)) {
            excludes = List.of("fullText");
        }
        List<String> finalExcludes = excludes;
        if (!CollectionUtils.isEmpty(includes)) {
            searchBuilder.source(c -> c.filter(f -> f.includes(includes).excludes(finalExcludes)));
        } else {
            searchBuilder.source(c -> c.filter(f -> f.excludes(finalExcludes)));
        }

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
