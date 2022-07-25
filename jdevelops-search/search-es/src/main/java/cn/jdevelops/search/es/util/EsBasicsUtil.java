package cn.jdevelops.search.es.util;


import cn.jdevelops.search.es.constant.EsConstant;
import cn.jdevelops.search.es.dto.ConditionDTO;
import cn.jdevelops.search.es.dto.EqDTO;
import cn.jdevelops.search.es.dto.SortDTO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
	 * 设置字段等于多个值的条件拼接 拼接结果效果类似于:
	 * <p>
	 * and field1 in ('value1','value2','value3'...) <br/>
	 * and field2 in ('value1','value2','value3'...) <br/>
	 * and field3 in ('value1','value2','value3'...) <br/>
	 * ...<br/>
	 * </p><br/>
	 *
	 * @param boolQueryBuilder 传入bool
	 * @param eqDTOList        等于多值的条件集合
	 * @author lxw
	 * @date 2021/5/20 11:56
	 */
	public static void setEqCondition(BoolQueryBuilder boolQueryBuilder, List<EqDTO> eqDTOList) {
		if (eqDTOList == null || eqDTOList.isEmpty()) {
			return;
		}
		eqDTOList.parallelStream().forEach(
				i -> {
					if (i.getField() != null && i.getFieldValue() != null && !i.getFieldValue().isEmpty()) {
						boolQueryBuilder.filter(QueryBuilders.termsQuery(i.getField(), i.getFieldValue()));
					}
				}
		);
	}

	/**
	 * 复杂条件查询，拼接结果效果类似于:
	 * <p>
	 * and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 * and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 * and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 * ...<br/>
	 * </p><br/>
	 *
	 * @param boolQueryBuilder 传入值
	 * @param listList         条件集合
	 * @author lxw
	 * @date 2021/5/20 12:05
	 */
	public static void setAdvancedList(BoolQueryBuilder boolQueryBuilder, List<List<List<ConditionDTO>>> listList) {
		if (listList == null || listList.isEmpty()) {
			return;
		}
		listList.forEach(i -> {
			if (i != null && !i.isEmpty()) {
				BoolQueryBuilder bq2 = QueryBuilders.boolQuery();
				i.forEach(f -> bq2.should(setAdvanced(f)));
				boolQueryBuilder.must(bq2);
			}
		});
	}

	/**
	 * 多条件按照与或非拼接，返回一个新的条件块 BoolQueryBuilder
	 * <p>
	 * and(x=1 or x=2 ) and(y=1 or y=3) and(z!=1 or z!=2)...
	 * </p>
	 *
	 * @param conditionDTOList 条件集合
	 * @return BoolQueryBuilder
	 * @author l
	 */
	public static BoolQueryBuilder setAdvanced(List<ConditionDTO> conditionDTOList) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (conditionDTOList != null && !conditionDTOList.isEmpty()) {
			for (ConditionDTO cond : conditionDTOList) {
				if (StringUtils.isBlank(cond.getFieldValue())) {
					continue;
				}
				// 与 或 非
				if (StringUtils.equals(cond.getConnectSymbol(), EsConstant.AND)) {
					boolQueryBuilder.must(setConSymbol(cond.getField(), cond.getFieldValue(), cond.getSymbol()));
				} else if (StringUtils.equals(cond.getConnectSymbol(), EsConstant.OR)) {
					boolQueryBuilder.should(setConSymbol(cond.getField(), cond.getFieldValue(), cond.getSymbol()));
				} else if (StringUtils.equals(cond.getConnectSymbol(), EsConstant.NOT)) {
					boolQueryBuilder.mustNot(setConSymbol(cond.getField(), cond.getFieldValue(), cond.getSymbol()));
				}
			}
		}
		return boolQueryBuilder;
	}

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
	public static QueryBuilder setConSymbol(String field, String fieldValue, String symbol) {
		switch (symbol.trim()) {
			// GT 就是 GREATER THAN大于 不要加“.keyword”
			case EsConstant.GT:
				return QueryBuilders.rangeQuery(field).gt(fieldValue);
			// LT 就是 LESS THAN小于 不要加“.keyword”
			case EsConstant.LT:
				return QueryBuilders.rangeQuery(field).lt(fieldValue);
			// GE 就是 GREATER THAN OR EQUAL 大于等于 不要加“.keyword”
			case EsConstant.GTE:
				return QueryBuilders.rangeQuery(field).gte(fieldValue);
			// LE 就是 LESS THAN OR EQUAL 小于等于 不要加“.keyword”
			case EsConstant.LTE:
				return QueryBuilders.rangeQuery(field).lte(fieldValue);
			// LIKE  就是 模糊 不要加“.keyword”
			case EsConstant.LIKE:
			default:
				// 默认返回 ( EQ 就是 EQUAL等于)必须加 “.keyword”
				return QueryBuilders.termQuery(field + ".keyword", fieldValue);
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
	public static BoolQueryBuilder setFieldsLike(String term, List<String> fields) {
		BoolQueryBuilder bq22 = QueryBuilders.boolQuery();
		for (String field : fields) {
			if (StringUtils.isBlank(field)) {
				continue;
			}
			bq22.should(QueryBuilders.matchPhraseQuery(field, term));
		}
		return bq22;
	}

	/**
	 * 拼接嵌套条件
	 *
	 * @param nested       索引嵌套结构名称
	 * @param term         检索词
	 * @param nestedFields 被检索的嵌套字段
	 * @return org.elasticsearch.index.query.BoolQueryBuilder
	 */
	public static BoolQueryBuilder setNested(String nested, String term, List<String> nestedFields) {
		if (StringUtils.isNotBlank(term)) {
			BoolQueryBuilder bq33 = QueryBuilders.boolQuery();
			for (String field : nestedFields) {
				if (StringUtils.isNotBlank(field)) {
					bq33.should(QueryBuilders.matchPhraseQuery(nested.trim() + "." + field, term));
				}
			}
		}
		return null;
	}

	/**
	 * 设置返回数据中只需要的字段
	 *
	 * @param sourceBuilder 查询条件对象
	 * @param includes      需要返回的字段
	 * @author lxw
	 */
	public static void setIncludesFields(SearchSourceBuilder sourceBuilder, String includes) {
		if (StringUtils.isNotBlank(includes)) {
			//只查询特定字段。如果需要查询所有字段则不设置该项。
			sourceBuilder.fetchSource(new FetchSourceContext(true, includes.split(","), Strings.EMPTY_ARRAY));
		}
	}

	/**
	 * 设置返回数据中需要排除字段
	 *
	 * @param sourceBuilder 查询条件对象
	 * @param excludes      需要排除字段
	 * @author lxw
	 */
	public static void setExcludesFields(SearchSourceBuilder sourceBuilder, String excludes) {
		if (StringUtils.isNotBlank(excludes)) {
			//只查询特定字段。如果需要查询所有字段则不设置该项。
			sourceBuilder.fetchSource(new FetchSourceContext(true, Strings.EMPTY_ARRAY, excludes.split(",")));
		}
	}

	/**
	 * 设置返回数据中需要排除字段
	 *
	 * @param sourceBuilder 查询条件对象
	 * @param includes      需要返回的字段
	 * @param excludes      需要排除字段
	 * @author lxw
	 */
	public static void setExcludesFields(SearchSourceBuilder sourceBuilder, String includes, String excludes) {
		if (StringUtils.isNotBlank(includes) && StringUtils.isNotBlank(excludes)) {
			//只查询特定字段。如果需要查询所有字段则不设置该项。
			sourceBuilder.fetchSource(new FetchSourceContext(true, includes.split(","), excludes.split(",")));
		}
	}

	/**
	 * 设置分页
	 *
	 * @param sourceBuilder 查询对象
	 * @param startPage     起始页
	 * @param pageSize      每页大小
	 * @author lxw
	 * @date 2021/5/11 16:28
	 */
	public static void setPage(SearchSourceBuilder sourceBuilder, Integer startPage, Integer pageSize) {
		if (startPage > 0) {
			startPage = startPage - 1;
		} else {
			startPage = 0;
		}
		//设置分页参数
		int startIndex = startPage * pageSize;
		sourceBuilder.from(startIndex);
		sourceBuilder.size(pageSize);
		sourceBuilder.trackTotalHits(true);
	}

	/**
	 * 设置排序
	 *
	 * @param sourceBuilder 查询对象
	 * @param list          排序对象
	 *                      <p>
	 *                      orderField 排序字段(特别注意：如果字段类型是text，需要拼接“.keyword”) <br/>
	 *                      orderType 排序方式 0、降序，1、升序<br/>
	 *                      </p>
	 * @author lxw
	 * @date 2021/5/12 18:38
	 */
	public static void setOrderField(SearchSourceBuilder sourceBuilder, List<SortDTO> list) {
		list.forEach(i -> {
			if (StringUtils.isNotBlank(i.getOrderBy())) {
				if (i.getOrderDesc() == 0) {
					sourceBuilder.sort(i.getOrderBy(), SortOrder.DESC);
				} else {
					sourceBuilder.sort(i.getOrderBy(), SortOrder.ASC);
				}
			}
		});
	}

	/**
	 * 设置高亮
	 *
	 * @param query          查询对象
	 * @param highlightField 高亮字段
	 * @author lxw
	 * @date 2021/5/11 16:31
	 */
	public static void setHighlightField(SearchSourceBuilder query, String highlightField) {
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

	/**
	 * 设置高亮
	 *
	 * @param query           查询对象
	 * @param highlightFields 高亮字段
	 * @author lxw
	 * @date 2021/5/11 16:31
	 */
	public static void setHighlightField(SearchSourceBuilder query, List<String> highlightFields) {
		if (!CollectionUtils.isEmpty(highlightFields)) {
			//高亮
			HighlightBuilder highlight = new HighlightBuilder();
			for (String field : highlightFields) {
				highlight.field(field);
			}
			//关闭多个高亮
			highlight.requireFieldMatch(false);
			highlight.preTags("<span style='color:red'>");
			highlight.postTags("</span>");
			//下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
			//最大高亮分片数
			highlight.fragmentSize(800000);
			//从第一个分片获取高亮片段
			highlight.numOfFragments(0);
			query.highlighter(highlight);
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
	public static List<Map<String, Object>> handleSearchResponse(SearchResponse searchResponse, String highlightField) {
		//解析结果
		ArrayList<Map<String, Object>> list = new ArrayList<>();
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
		return list;
	}

	/**
	 * 分组结果处理示例
	 *
	 * @param searchResponse 查询结果
	 * @param groupFiled     分组字段
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @author lxw
	 * @date 2021/12/14 14:03
	 */
	public static List<Map<String, Object>> groupHandle(SearchResponse searchResponse, String groupFiled) {
		//得到这个分组的数据集合
		Terms terms1 = searchResponse.getAggregations().get(groupFiled);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < terms1.getBuckets().size(); i++) {
			Map<String, Object> map = new HashMap<>(2);
			//key
			String key = terms1.getBuckets().get(i).getKey().toString();
			//数量
			Long sum = terms1.getBuckets().get(i).getDocCount();
			map.put(key, sum);
			list.add(map);
		}
		return list;
	}
}
