package cn.jdevelops.search.es.service.impl;

import cn.jdevelops.search.es.constant.EsConstant;
import cn.jdevelops.search.es.dto.ConditionDTO;
import cn.jdevelops.search.es.dto.EqDTO;
import cn.jdevelops.search.es.dto.SortDTO;
import cn.jdevelops.search.es.service.EsSearchService;
import cn.jdevelops.search.es.util.EsBasicsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检索接口实现
 *
 * @author lxw
 * @version V1.0
 * @date 2021/6/23
 **/
@Slf4j
@Service
public class EsSearchServiceImpl implements EsSearchService {

	private final RestHighLevelClient restHighLevelClient;

	public EsSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}


	@Override
	public Map<String, Object> searchDataById(String index, String esOnlyId) throws IOException {
		GetRequest request = new GetRequest(index, esOnlyId);
		GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
		return response.getSource();
	}

	@Override
	public SearchResponse executeSearch(SearchRequest request) throws IOException {
		SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		if (response.status().getStatus() == EsConstant.HTTP_200) {
			long totalHits = response.getHits().getTotalHits().value;
			long length = response.getHits().getHits().length;
			log.info("共查询到[{}]条数据,当前页[{}]条数据", totalHits, length);
			return response;
		}
		return null;
	}

	@Override
	public SearchResponse page(List<String> index, Integer startPage, Integer pageSize) throws IOException {
		SearchRequest request = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		// 设置分页
		EsBasicsUtil.setPage(sourceBuilder, startPage, pageSize);
		request.source(sourceBuilder);
		return executeSearch(request);
	}

	@Override
	public SearchResponse getSearch(List<String> index, List<EqDTO> eqDTOList, List<String> terms, List<String> fields, String nested, List<String> nestedFields, List<ConditionDTO> conditionDTOList, List<List<List<ConditionDTO>>> listList, String highlightField, Integer startPage, Integer pageSize, List<SortDTO> sortDTOList) throws IOException {
		SearchRequest request = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// AND 组装 eqDOTS
		EsBasicsUtil.setEqCondition(boolQueryBuilder, eqDTOList);

		// AND 组装 常规条件集合
		if (terms != null && !terms.isEmpty()) {
			// 常规查询条件
			if (fields != null && !fields.isEmpty()) {
				terms.parallelStream().forEach(i -> {
					if (StringUtils.isNotBlank(i)) {
						boolQueryBuilder.must(EsBasicsUtil.setFieldsLike(i, fields));
					}
				});
			}
			// 嵌套查询条件
			if (StringUtils.isNoneBlank(nested) && nestedFields != null && !nestedFields.isEmpty()) {
				terms.parallelStream().forEach(str -> {
					if (StringUtils.isNotBlank(str)) {
						boolQueryBuilder.must(QueryBuilders.nestedQuery(nested, EsBasicsUtil.setNested(nested, str, nestedFields), ScoreMode.None));
					}
				});
			}
		}

		// AND 高级条件组
		boolQueryBuilder.must(EsBasicsUtil.setAdvanced(conditionDTOList));
		// AND 组装多块条件集合
		EsBasicsUtil.setAdvancedList(boolQueryBuilder, listList);

		sourceBuilder.query(boolQueryBuilder);
		// 设置分页
		EsBasicsUtil.setPage(sourceBuilder, startPage, pageSize);
		// 设置排序
		EsBasicsUtil.setOrderField(sourceBuilder, sortDTOList);
		// 设置高亮
		EsBasicsUtil.setHighlightField(sourceBuilder, highlightField);
		request.source(sourceBuilder);
		return executeSearch(request);
	}

	@Override
	public List<Map<String, Object>> getAll(SearchRequest searchRequest) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		searchRequest.scroll(TimeValue.timeValueMinutes(5));
		SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		for (SearchHit hit : response.getHits().getHits()) {
			//4.1、首页数据
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			list.add(sourceAsMap);
		}
		String scrollId = response.getScrollId();
		while (true) {
			// 5、循环创建SearchScrollRequest
			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
			//6、再指定scroll的生存时间，若不指定它会归零
			scrollRequest.scroll(TimeValue.timeValueMinutes(2L));
			//7、执行查询获取结果
			SearchResponse scrollResp = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
			scrollId = scrollResp.getScrollId();
			//8、判断是否查询到了数据输出
			SearchHit[] hits = scrollResp.getHits().getHits();
			if (hits != null && hits.length > 0) {
				for (SearchHit hit : hits) {
					//循环输出
					Map<String, Object> sourceAsMap = hit.getSourceAsMap();
					list.add(sourceAsMap);
				}
			} else {
				//9、若无数据则退出
				break;
			}
		}
		//10、创建ClearScrollRequest
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		//11、指定scrollId
		clearScrollRequest.addScrollId(scrollId);
		//12、删除scrollId
		ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
		//13、根据它返回判断删除成功没
		clearScrollResponse.isSucceeded();
		return list;
	}

	@Override
	public List<Map<String, Object>> groupExample() throws IOException {
		SearchRequest searchRequest = new SearchRequest("indexName");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.mustNot(QueryBuilders.termQuery("applyStats", 3));
		AggregationBuilder agg1 = AggregationBuilders.terms("agg1").field("applyDays.keyword");
		AggregationBuilder agg2 = AggregationBuilders.terms("agg2").field("studentType");
		agg1.subAggregation(agg2);
		sourceBuilder.aggregation(agg1);
		searchRequest.source(sourceBuilder);
		sourceBuilder.size(0);
		SearchResponse response = executeSearch(searchRequest);
		Terms terms1 = response.getAggregations().get("agg1");
		Terms terms2;
		List<Map<String, Object>> list = new ArrayList<>();
		for (Terms.Bucket bucket1 : terms1.getBuckets()) {
			Map<String, Object> map = new HashMap<>(4);
			terms2 = bucket1.getAggregations().get("agg2");
			List<Map<String, Object>> sub = new ArrayList<>();
			for (Terms.Bucket bucket2 : terms2.getBuckets()) {
				Map<String, Object> map2 = new HashMap<>(2);
				map2.put("count", bucket2.getDocCount());
				map2.put("studentType", "本科生");
				sub.add(map2);
			}
			map.put("data", sub);
			map.put("name", bucket1.getKey().toString() + " 天");
			map.put("count", bucket1.getDocCount());
			list.add(map);
		}
		return list;
	}
}
