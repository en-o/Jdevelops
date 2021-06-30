package com.detabes.search.es.service.impl;

import com.detabes.search.es.dto.ConditionDTO;
import com.detabes.search.es.dto.EqDTO;
import com.detabes.search.es.dto.SortDTO;
import com.detabes.search.es.dto.SpecialDTO;
import com.detabes.search.es.service.EsSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
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

	private RestHighLevelClient restHighLevelClient;

	public EsSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public Map<String, Object> searchDataById(String index, String esOnlyId, String fields) throws IOException {
		GetRequest request = new GetRequest(index, esOnlyId);
		if (StringUtils.isNotEmpty(fields)) {
			//只查询特定字段。如果需要查询所有字段则不设置该项。
			request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
		}
		GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
		return response.getSource();
	}

	@Override
	public SearchResponse getSearch(List<String> index, Integer startPage, Integer pageSize) throws IOException {
		SearchRequest request = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		// 设置分页
		setPage(sourceBuilder, startPage, pageSize);
		request.source(sourceBuilder);
		return executePage(request);
	}

	@Override
	public SearchResponse getSearch(List<String> index, List<EqDTO> eqDTOList, List<String> terms, List<String> fields
			, String nested, List<String> nestedFields
			, List<SpecialDTO> specialDTOList, List<ConditionDTO> conditionDTOList
			, List<List<List<ConditionDTO>>> listList, String highlightField
			, Integer startPage, Integer pageSize, List<SortDTO> sortDTOList) throws IOException {
		SearchRequest request = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// AND 组装 eqDOTS
		setEqCondition(boolQueryBuilder, eqDTOList);
		// AND 组装 常规条件集合
		if (terms != null && terms.size() > 0) {
			boolQueryBuilder.must(setConditionByTerm(terms, fields, nested, nestedFields));
		}
		// AND 组装 模糊条件集合
		if (specialDTOList != null && specialDTOList.size() > 0) {
			setSpecialDTOList(boolQueryBuilder, specialDTOList);
		}
		// AND 高级条件组
		boolQueryBuilder.must(setAdvanced(conditionDTOList));
		// AND 组装多块条件集合
		setAdvancedList(boolQueryBuilder, listList);

		sourceBuilder.query(boolQueryBuilder);
		// 设置分页
		setPage(sourceBuilder, startPage, pageSize);
		// 设置排序
		setOrderField(sourceBuilder, sortDTOList);
		// 设置高亮
		setHighlightField(sourceBuilder, highlightField);
		request.source(sourceBuilder);
		return executePage(request);
	}

	@Override
	public SearchResponse getSearchFile(List<String> index, List<EqDTO> eqDTOList, List<String> terms, List<String> fields
			, Integer startPage, Integer pageSize, List<SortDTO> sortDTOList) throws IOException {
		SearchRequest request = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 组装表名与单位代码
		setEqCondition(boolQueryBuilder, eqDTOList);
		// 拼接查询条件
		if (terms != null && terms.size() > 0) {
			boolQueryBuilder.must(setConditionByTerm(terms, fields, null, null));
		}
		sourceBuilder.query(boolQueryBuilder);
		// 设置分页
		setPage(sourceBuilder, startPage, pageSize);
		// 设置排序
		setOrderField(sourceBuilder, sortDTOList);
		request.source(sourceBuilder);
		return executePage(request);
	}

	@Override
	public List<Map<String, Object>> getGroup(List<String> index, List<EqDTO> eqDTOList
			, List<String> terms, List<String> fields
			, String nested, List<String> nestedFields
			, List<List<List<ConditionDTO>>> listList
			, String groupField) throws IOException {
		SearchRequest searchRequest = new SearchRequest(index.toArray(new String[]{}));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		getBoolQueryBuilder(boolQueryBuilder, eqDTOList, terms, fields, nested, nestedFields, listList);
		// 将条件初始化到查询对象
		sourceBuilder.query(boolQueryBuilder);
		// 根据 groupField 进行分组统计，统计出的列别名叫 sum
		TermsAggregationBuilder field = AggregationBuilders.terms("sum").field(groupField.trim());
		sourceBuilder.aggregation(field);
		searchRequest.source(sourceBuilder);
		return executeGroup(searchRequest);
	}

	private void getBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder, List<EqDTO> eqDTOList
			, List<String> terms, List<String> fields
			, String nested, List<String> nestedFields
			, List<List<List<ConditionDTO>>> listList) {
		// AND 组装 eqDOTS
		setEqCondition(boolQueryBuilder, eqDTOList);
		// AND 组装 常规条件集合
		if (terms != null && terms.size() > 0) {
			boolQueryBuilder.must(setConditionByTerm(terms, fields, nested, nestedFields));
		}
		// AND 组装多块条件集合
		setAdvancedList(boolQueryBuilder, listList);
	}

	@Override
	public List<Map<String, Object>> executeGroup(SearchRequest searchRequest) throws IOException {
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

	@Override
	public SearchResponse executePage(SearchRequest request) throws IOException {
		SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		long totalHits = response.getHits().getTotalHits().value;
		long length = response.getHits().getHits().length;
		log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
		int status = 200;
		if (response.status().getStatus() == status) {
			return response;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getAll(SearchRequest searchRequest) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		list.clear();
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
		boolean succeeded = clearScrollResponse.isSucceeded();
		return list;
	}

	@Override
	public BoolQueryBuilder setEqCondition(BoolQueryBuilder boolQueryBuilder, List<EqDTO> eqDTOList) {
		if (eqDTOList == null || eqDTOList.isEmpty()) {
			return boolQueryBuilder;
		}
		eqDTOList.forEach(i -> {
			if (i.getField() != null && i.getFieldValue() != null && i.getFieldValue().size() > 0) {
				boolQueryBuilder.filter(QueryBuilders.termsQuery(i.getField(), i.getFieldValue()));
			}
		});
		return boolQueryBuilder;
	}

	@Override
	public void setAdvancedList(BoolQueryBuilder boolQueryBuilder, List<List<List<ConditionDTO>>> listList) {
		if (listList == null || listList.isEmpty()) {
			return;
		}
		listList.forEach(i -> {
			if (i != null && i.size() > 0) {
				BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
				i.forEach(f -> {
					if (f != null && f.size() > 0) {
						bq2.should(setAdvanced(f));
					}
				});
				boolQueryBuilder.must(bq2);
			}
		});
	}

	@Override
	public BoolQueryBuilder setAdvanced(List<ConditionDTO> conditionDTOList) {
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

	@Override
	public void setSpecialDTOList(BoolQueryBuilder bq, List<SpecialDTO> specialDTOList) {
		if (specialDTOList != null && specialDTOList.size() > 0) {
			specialDTOList.forEach(i -> bq.must(QueryBuilders.matchPhraseQuery(i.getField(), i.getFieldValue())));
		}
	}

	@Override
	public BoolQueryBuilder setConditionByTerm(List<String> terms, List<String> fields, String nested, List<String> nestedFields) {
		BoolQueryBuilder bq = QueryBuilders.boolQuery();
		if (terms != null && terms.size() > 0) {
			// 常规查询条件
			if (fields != null && fields.size() > 0) {
				bq.should(setFieldsTerm(terms, fields));
			}
			// 嵌套查询条件
			if (StringUtils.isNoneBlank(nested) && nestedFields != null && nestedFields.size() > 0) {
				bq.should(QueryBuilders.nestedQuery(nested, setNestedFieldsTerm(nested, terms, nestedFields), ScoreMode.None));
			}
		}
		return bq;
	}

	@Override
	public BoolQueryBuilder setFieldsTerm(List<String> terms, List<String> fields) {
		BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
		for (String str : terms) {
			if (StringUtils.isNotBlank(str)) {
				BoolQueryBuilder bq22 = QueryBuilders.boolQuery();
				for (String field : fields) {
					if (StringUtils.isBlank(field)) {
						continue;
					}
					bq22.should(QueryBuilders.matchPhraseQuery(field, str));
				}
				bq2.must(bq22);
			}
		}
		return bq2;
	}

	@Override
	public BoolQueryBuilder setNestedFieldsTerm(String nested, List<String> terms, List<String> nestedFields) {
		BoolQueryBuilder bq3 = QueryBuilders.boolQuery();
		for (String str : terms) {
			if (StringUtils.isNotBlank(str)) {
				BoolQueryBuilder bq33 = QueryBuilders.boolQuery();
				for (String field : nestedFields) {
					if (StringUtils.isBlank(field)) {
						continue;
					}
					bq33.should(QueryBuilders.matchPhraseQuery(nested.trim() + "." + field, str));
				}
				bq3.must(bq33);
			}
		}
		return bq3;
	}

	@Override
	public BoolQueryBuilder setConSymbol(String field, String fieldValue, String symbol) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		switch (symbol.trim()) {
			// EQ 就是 EQUAL等于 必须加 “.keyword”
			case "EQ":
				boolQueryBuilder.must(QueryBuilders.termQuery(field + ".keyword", fieldValue));
				break; //可选
			// NE 就是 NOT EQUAL不等于 必须加 “.keyword”
			case "NE":
				boolQueryBuilder.mustNot(QueryBuilders.termQuery(field + ".keyword", fieldValue));
				//语句
				break; //可选
			// GT 就是 GREATER THAN大于 不要加“.keyword”
			case "GT":
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gt(fieldValue));
				break; //可选
			// LT 就是 LESS THAN小于 不要加“.keyword”
			case "LT":
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lt(fieldValue));
				break; //可选
			// GE 就是 GREATER THAN OR EQUAL 大于等于 不要加“.keyword”
			case "GE":
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gte(fieldValue));
				break; //可选
			// LE 就是 LESS THAN OR EQUAL 小于等于 不要加“.keyword”
			case "LE":
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lte(fieldValue));
				break; //可选
			// LIKE  就是 模糊 不要加“.keyword”
			case "LIKE":
				boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, fieldValue));
				break; //可选
			default: //可选
				//语句
		}
		return boolQueryBuilder;
	}

	@Override
	public void setAppointFields(SearchSourceBuilder sourceBuilder, String fields) {
		if (StringUtils.isNotBlank(fields)) {
			//只查询特定字段。如果需要查询所有字段则不设置该项。
			sourceBuilder.fetchSource(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
		}
	}

	@Override
	public void setPage(SearchSourceBuilder sourceBuilder, Integer startPage, Integer pageSize) {
		if (startPage > 0) {
			startPage = startPage - 1;
		}
		//设置分页参数
		int startIndex = startPage * pageSize;
		sourceBuilder.from(startIndex);
		sourceBuilder.size(pageSize);
		sourceBuilder.trackTotalHits(true);
	}

	@Override
	public void setOrderField(SearchSourceBuilder sourceBuilder, List<SortDTO> list) {
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
	}

	@Override
	public void setHighlightField(SearchSourceBuilder query, String highlightField) {
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
	}

	@Override
	public List<Map<String, Object>> handleSearchResponse(SearchResponse searchResponse, String highlightField) {
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
