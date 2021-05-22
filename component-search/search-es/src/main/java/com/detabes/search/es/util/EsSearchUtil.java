package com.detabes.search.es.util;

import com.detabes.search.es.dto.ConditionDTO;
import com.detabes.search.es.dto.SortDTO;
import com.detabes.search.es.dto.SpecialDTO;
import com.detabes.search.es.vo.EsPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检索工具类
 *
 * @author l
 * @version V1.0
 * @date 2021/5/11
 **/
@Slf4j
@Component
public class EsSearchUtil {
    private RestHighLevelClient restHighLevelClient;

    public EsSearchUtil(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 无条件分页检索
     *
     * @param index     索引名称集合["anjuan","xiangmu","wenben"]
     * @param startPage 起始页
     * @param pageSize  每页大小
     * @return
     * @throws IOException
     */
    public EsPageVO search(List<String> index, Integer startPage, Integer pageSize) throws IOException {
        SearchRequest request = new SearchRequest(index.toArray(new String[index.size()]));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 设置分页
        setPage(sourceBuilder, startPage, pageSize);
        request.source(sourceBuilder);
        return getPage(request, null, startPage, pageSize);
    }

    /**
     * 多分类检索，有特定条件
     *
     * @param index          索引名称集合["anjuan","xiangmu","wenben"]
     * @param dwdm           单位代码集合["001","002","003"]
     * @param tableId        表序号集合["1","2","3"]
     * @param terms          检索词集合[ "文化","历史","数据"]
     * @param highlightField 需要设定的高亮字段，一定是参与检索字段中的其中之一
     * @param fields         常规检索字段
     * @param nested         嵌套查询名称
     * @param nestedFields   嵌套查询字段
     * @param startPage      起始页
     * @param pageSize       每页大小
     * @param specialDTOS    指定条件集合
     * @param sortDTOS       排序数组
     * @return com.detabes.search.es.vo.EsPageVO
     * @author lxw
     * @date 2021/5/12 18:26
     */
    public EsPageVO search1(List<String> index, List<String> dwdm, List<String> tableId, List<String> terms, String highlightField,
							List<String> fields, String nested, List<String> nestedFields, Integer startPage, Integer pageSize,
							List<SpecialDTO> specialDTOS, List<SortDTO> sortDTOS) throws IOException {
        SearchRequest request = new SearchRequest(index.toArray(new String[index.size()]));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 组装表名与单位代码
        boolQueryBuilder.must(setTableIdAndUnitCode(tableId, dwdm));
        // 拼接查询条件
        if (terms != null && terms.size() > 0) {
            boolQueryBuilder.must(setConditionByTerm(terms, fields, nested, nestedFields));
        }
        // 设置指定条件集合
        if (specialDTOS != null && specialDTOS.size() > 0) {
            boolQueryBuilder.must(setSpecialDTOS(specialDTOS));
        }
        sourceBuilder.query(boolQueryBuilder);
        // 设置分页
        setPage(sourceBuilder, startPage, pageSize);
        // 设置排序
        setOrderField(sourceBuilder, sortDTOS);
        // 设置高亮
        setHighlightField(sourceBuilder, highlightField);
        request.source(sourceBuilder);
        return getPage(request, highlightField, startPage, pageSize);
    }

    /**
     * 分页查询
     *
     * @param index            索引文件名称
     * @param highlightField   高亮字段
     * @param fields           需要查询的字段，缺省则查询全部
     * @param conditionDTOList 条件集合
     * @param startPage        起始页
     * @param pageSize         每页大小
     * @return com.detabes.search.es.vo.EsPageVO
     * @author lxw
     * @date 2021/5/11 16:36
     */
    public EsPageVO searchX(List<String> index, String highlightField, String fields, List<ConditionDTO> conditionDTOList
            , Integer startPage, Integer pageSize, List<SortDTO> sortDTOS) throws IOException {
        SearchRequest request = new SearchRequest(index.toArray(new String[index.size()]));
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 高级条件
        boolQueryBuilder.must(setAdvanced(conditionDTOList));
        // 将条件初始化到查询对象
        sourceBuilder.query(boolQueryBuilder);
        // 设置分页
        setPage(sourceBuilder, startPage, pageSize);
        // 设置高亮
        setHighlightField(sourceBuilder, highlightField);
        // 需要查询的字段，缺省则查询全部
        setAppointFields(sourceBuilder, fields);
        // 设置排序
        setOrderField(sourceBuilder, sortDTOS);

        request.source(sourceBuilder);
        return getPage(request, highlightField, startPage, pageSize);
    }

    /**
     * 分组数据
     *
     * @param index        索引名称
     * @param dwdm         单位代码集合
     * @param tableId      表序号集合
     * @param terms        检索词集合
     * @param fields       常规字段集合
     * @param nested       嵌套名称 如：“fubiao”
     * @param nestedFields 嵌套查询字段
     * @param groupField   分组字段，唯一一个
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> getGroup(List<String> index, List<String> dwdm, List<String> tableId, List<String> terms,
                                              List<String> fields, String nested, List<String> nestedFields, String groupField) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index.toArray(new String[index.size()]));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 组装筛选条件
        if (terms != null && terms.size() > 0) {
            boolQueryBuilder.must(setConditionByTerm(terms, fields, nested, nestedFields));
        }
        // 组装表名与单位代码
        boolQueryBuilder.must(setTableIdAndUnitCode(tableId, dwdm));

        // 将条件初始化到查询对象
        sourceBuilder.query(boolQueryBuilder);

        // 根据 groupField 进行分组统计，统计出的列别名叫 sum
        TermsAggregationBuilder field = AggregationBuilders.terms("sum").field(groupField.trim());
//        field.subAggregation(AggregationBuilders.sum("sum_value").field("value"));
        sourceBuilder.aggregation(field);
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //得到这个分组的数据集合
        Terms terms1 = search.getAggregations().get("sum");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < terms1.getBuckets().size(); i++) {
            Map<String, Object> map = new HashMap<>(2);
            //key
            String key = terms1.getBuckets().get(i).getKey().toString();
            //数量
            Long sum = terms1.getBuckets().get(i).getDocCount();
            map.put(key, sum);
            list.add(map);
            log.info("分组名称=======:{},数据条数=========:{}", key, sum);
        }
        return list;
    }

    /**
     * 执行分页查询
     *
     * @param request        请求对象
     * @param highlightField 高亮字段
     * @param startPage      起始页
     * @param pageSize       分页
     * @return com.detabes.search.es.vo.EsPageVO
     * @author lxw
     * @date 2021/5/12 18:13
     */
    public EsPageVO getPage(SearchRequest request, String highlightField, Integer startPage, Integer pageSize) throws IOException {
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.info("totalHits:" + response.getHits().getTotalHits());
        long totalHits = response.getHits().getTotalHits().value;
        long length = response.getHits().getHits().length;
        log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
        if (response.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> maps = handleSearchResponse(response, highlightField);
            return new EsPageVO(startPage, pageSize, (int) totalHits, maps);
        }
        return null;
    }

    /**
     * 设置 表ID和单位代码
     *
     * @param tableId  表ID集合
     * @param unitCode 单位代码集合
     * @return
     */
    private BoolQueryBuilder setTableIdAndUnitCode(List<String> tableId, List<String> unitCode) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (unitCode != null && unitCode.size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("DWDM", unitCode));
        }
        if (tableId != null && tableId.size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("tableId", tableId));
        }
        return boolQueryBuilder;
    }

    /**
     * 多条件常规凭借
     *
     * @param conditionDTOList 条件集合
     * @return SearchSourceBuilder
     * @author l
     */
    private BoolQueryBuilder setAdvanced(List<ConditionDTO> conditionDTOList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (conditionDTOList != null && conditionDTOList.size() > 0) {
            for (ConditionDTO cond : conditionDTOList) {
                if (StringUtils.isBlank(cond.getFieldValue())) {
                    continue;
                }
                // 逻辑类型 AND 就是 并且; OR 就是 或者
                if (StringUtils.equals("AND", cond.getConnectSymbol())) {
                    // 逻辑类型，AND
                    boolQueryBuilder.must(setConSymbol(cond.getField(), cond.getFieldValue(), cond.getSymbol()));
                } else {
                    // 逻辑类型，OR
                    boolQueryBuilder.should(setConSymbol(cond.getField(), cond.getFieldValue(), cond.getSymbol()));
                }
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 拼接指定条件
     *
     * @param specialDTOS 指定条件集合
     * @return
     */
    private BoolQueryBuilder setSpecialDTOS(List<SpecialDTO> specialDTOS) {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        if (specialDTOS != null && specialDTOS.size() > 0) {
            specialDTOS.forEach(i -> {
                bq.must(QueryBuilders.matchPhraseQuery(i.getField(), i.getFieldValue()));
            });
        }
        return bq;
    }

    /**
     * 设置查询条件
     *
     * @param terms        检索词集合
     * @param fields       需要检索常规字段集合
     * @param nested       嵌套名称 如“fubiao”
     * @param nestedFields 需要嵌套检索的字段集合
     * @return
     */
    private BoolQueryBuilder setConditionByTerm(List<String> terms, List<String> fields, String
            nested, List<String> nestedFields) {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        if (terms != null && terms.size() > 0) {
            // 常规查询条件
            if (fields != null && fields.size() > 0) {
                bq.should(setFieldsTerm(terms, fields));
            }
            // 嵌套查询条件
            if (StringUtils.isNoneBlank(nested) && nestedFields != null && nestedFields.size() > 0) {
                 bq.should(QueryBuilders.nestedQuery(nested,setNestedFieldsTerm(nested, terms, nestedFields), ScoreMode.None));
            }
        }
        return bq;
    }

    /**
     * 拼接常规条件
     *
     * @param terms  检索词集合
     * @param fields 被检索字段集合
     * @return BoolQueryBuilder
     */
    private BoolQueryBuilder setFieldsTerm(List<String> terms, List<String> fields) {
        BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
        for (String str : terms) {
            if (StringUtils.isNotBlank(str)) {
                BoolQueryBuilder bq22 = QueryBuilders.boolQuery();
                for (String field : fields) {
                    if(StringUtils.isBlank(field)){
                        continue;
                    }
                    bq22.should(QueryBuilders.matchPhraseQuery(field, str));
                }
                bq2.must(bq22);
            }
        }
        return bq2;
    }

    /**
     * 拼接嵌套条件
     *
     * @param nested       索引嵌套结构名称
     * @param terms        检索词集合
     * @param nestedFields 被检索的嵌套条件
     * @return
     */
    private BoolQueryBuilder setNestedFieldsTerm(String nested, List<String> terms, List<String> nestedFields) {
        BoolQueryBuilder bq3 = QueryBuilders.boolQuery();
        for (String str : terms) {
            if (StringUtils.isNotBlank(str)) {
                BoolQueryBuilder bq33 = QueryBuilders.boolQuery();
                for (String field : nestedFields) {
                    if(StringUtils.isBlank(field)){
                        continue;
                    }
                    bq33.should(QueryBuilders.matchPhraseQuery(nested.trim() + "." + field, str));
                }
                bq3.must(bq33);
            }
        }
        return bq3;
    }

    /**
     * 判断拼接条件<br/>
     *
     * @param field      字段
     * @param fieldValue 值
     * @param symbol     匹配类型
     *                   PS:symbol包含
     *                   <p>
     *                   EQ 就是 EQUAL等于 <br/>
     *                   NE 就是 NOT EQUAL不等于 <br/>
     *                   GT 就是 GREATER THAN大于　<br/>
     *                   LT 就是 LESS THAN小于 <br/>
     *                   GE 就是 GREATER THAN OR EQUAL 大于等于 <br/>
     *                   LE 就是 LESS THAN OR EQUAL 小于等于 <br/>
     *                   LIKE  就是 模糊 <br/>
     *                   <p/>
     */
    private BoolQueryBuilder setConSymbol(String field, String fieldValue, String symbol) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        switch (symbol.trim()) {
            // EQ 就是 EQUAL等于
            case "EQ":
                boolQueryBuilder.must(QueryBuilders.termQuery(field + ".keyword", QueryParser.escape(fieldValue)));
                break; //可选
            // NE 就是 NOT EQUAL不等于
            case "NE":
                boolQueryBuilder.mustNot(QueryBuilders.termQuery(field + ".keyword", QueryParser.escape(fieldValue)));
                //语句
                break; //可选
            // GT 就是 GREATER THAN大于
            case "GT":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gt(QueryParser.escape(fieldValue)));
                break; //可选
            // LT 就是 LESS THAN小于
            case "LT":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lt(QueryParser.escape(fieldValue)));
                break; //可选
            // GE 就是 GREATER THAN OR EQUAL 大于等于
            case "GE":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gte(QueryParser.escape(fieldValue)));
                break; //可选
            // LE 就是 LESS THAN OR EQUAL 小于等于
            case "LE":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lte(QueryParser.escape(fieldValue)));
                break; //可选
            // LIKE  就是 模糊
            case "LIKE":
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, QueryParser.escape(fieldValue)));
                break; //可选
            default: //可选
                //语句
        }
        return boolQueryBuilder;
    }

    /**
     * 设定指定检索字段
     *
     * @param sourceBuilder 查询条件对象
     * @param fields        指定检索字段
     * @return org.elasticsearch.search.builder.SearchSourceBuilder
     * @author lxw
     */
    private SearchSourceBuilder setAppointFields(SearchSourceBuilder sourceBuilder, String fields) {
        if (StringUtils.isNotBlank(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            sourceBuilder.fetchSource(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        return sourceBuilder;
    }

    /**
     * 设置分页
     *
     * @param sourceBuilder 查询对象
     * @param startPage     起始页
     * @param pageSize      每页大小
     * @return org.elasticsearch.search.builder.SearchSourceBuilder
     * @author lxw
     * @date 2021/5/11 16:28
     */
    private SearchSourceBuilder setPage(SearchSourceBuilder sourceBuilder, Integer startPage, Integer pageSize) {
        if (startPage > 0) {
            startPage = startPage - 1;
        }
        //设置分页参数
        Integer startIndex = startPage * pageSize;
        sourceBuilder.from(startIndex);
        sourceBuilder.size(pageSize);
        sourceBuilder.trackTotalHits(true);
        return sourceBuilder;
    }

    /**
     * 设置排序
     *
     * @param sourceBuilder 查询对象
     * @param list          排序对象
     *                      PS：数据如下
     *                      <p>
     *                      orderField 排序字段 <br/>
     *                      orderType 排序方式 0、降序，1、升序<br/>
     *                      </p>
     * @return void
     * @author lxw
     * @date 2021/5/12 18:38
     */
    private SearchSourceBuilder setOrderField(SearchSourceBuilder sourceBuilder, List<SortDTO> list) {
        list.forEach(i -> {
            if (StringUtils.isNotBlank(i.getOrderBy())) {
                if (i.getOrderDesc() == 0) {
                    // 设置排序字段和排序方式，注意：字段是text类型需要拼接.keyword
                    sourceBuilder.sort(i.getOrderBy(), SortOrder.DESC);
                } else {
                    sourceBuilder.sort(i.getOrderBy(), SortOrder.ASC);
                }
            }
        });
        return sourceBuilder;
    }

    /**
     * 设置高亮
     *
     * @param query          查询对象
     * @param highlightField 高亮字段
     * @return org.elasticsearch.search.builder.SearchSourceBuilder
     * @author lxw
     * @date 2021/5/11 16:31
     */
    private SearchSourceBuilder setHighlightField(SearchSourceBuilder query, String highlightField) {
        if (StringUtils.isNotBlank(highlightField)) {
            //高亮
            HighlightBuilder highlight = new HighlightBuilder();
            highlight.field(highlightField);
            //关闭多个高亮
            highlight.requireFieldMatch(false);
            highlight.preTags("<span style='color:red'>");
            highlight.postTags("</span>");
            query.highlighter(highlight);
        }
        return query;
    }

    /**
     * 高亮结果集 特殊处理
     * map转对象 JSONObject.parseObject(JSONObject.toJSONString(map), Content.class)
     *
     * @param searchResponse 结果集
     * @param highlightField 高亮字段
     */
    private List<Map<String, Object>> handleSearchResponse(SearchResponse searchResponse, String highlightField) {
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        if (StringUtils.isNotBlank(highlightField)) {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, HighlightField> high = hit.getHighlightFields();
                HighlightField title = high.get(highlightField);
                //原来的结果
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //解析高亮字段,将原来的字段换为高亮字段
                if (title != null) {
                    Text[] texts = title.fragments();
                    StringBuilder nTitle = new StringBuilder();
                    for (Text text : texts) {
                        nTitle.append(text);
                    }
                    //替换
                    sourceAsMap.put(highlightField, nTitle.toString());
                }
                list.add(sourceAsMap);
            }
        } else {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                list.add(sourceAsMap);
            }
        }
        return list;
    }
}
