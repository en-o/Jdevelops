package com.detabes.search.es.service;

import com.detabes.search.es.dto.ConditionDTO;
import com.detabes.search.es.dto.EqDTO;
import com.detabes.search.es.dto.SortDTO;
import com.detabes.search.es.dto.SpecialDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 检索接口
 *
 * @author l
 * @version V1.0
 * @date 2021/5/11
 **/
public interface EsSearchService {

	/**
	 * 通过ID获取数据
	 *
	 * @param index    索引，类似数据库
	 * @param esOnlyId ES索引唯一值
	 * @param fields   需要查询的字段，逗号分隔（缺省为全部字段）
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @throws IOException IOException
	 */
	Map<String, Object> searchDataById(String index, String esOnlyId, String fields) throws IOException;

	/**
	 * 无条件分页检索示例
	 *
	 * @param index     索引名称集合["anjuan","xiangmu","wenben"]
	 * @param startPage 起始页
	 * @param pageSize  每页大小
	 * @return com.detabes.search.es.vo.EsPage
	 * @throws IOException IOException
	 */
	SearchResponse getSearch(List<String> index, Integer startPage, Integer pageSize) throws IOException;

	/**
	 * 分页检索示例
	 *
	 * @param index            索引名称集合
	 * @param eqDTOList        等于类型的匹配条件集合(拼接结果效果类似于:
	 *                         <p>
	 *                         and field1 in ('value1','value2','value3'...) <br/>
	 *                         and field2 in ('value1','value2','value3'...) <br/>
	 *                         and field3 in ('value1','value2','value3'...) <br/>
	 *                         ...)<br/>
	 *                         </p><br/>
	 * @param terms            检索词集合
	 * @param fields           被检索字段集合
	 * @param nested           被嵌套查询名称
	 * @param nestedFields     被嵌套检索的字段集合
	 * @param specialDTOList   指定条件模糊检索条件集合(拼接结果效果类似于:
	 *                         <p>
	 *                         and field1 like '%value1%' <br/>
	 *                         and field2 like '%value2%' <br/>
	 *                         and field3 like '%value3%' <br/>
	 *                         ...)<br/>
	 *                         </p><br/>
	 * @param conditionDTOList 逻辑条件集合（拼接结果效果类似于:
	 *                         <p>
	 *                         and (高级组合))<br/>
	 *                         </p><br/>
	 * @param listList         AND 条件集合块(拼接结果效果类似于:
	 *                         <p>
	 *                         and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                         and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                         and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                         ...)<br/>
	 *                         </p><br/>
	 * @param highlightField   高亮字段，一定在fields集合中
	 * @param startPage        起始页
	 * @param pageSize         每页大小
	 * @param sortDTOList      排序集合
	 * @return com.detabes.search.es.vo.EsPage
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/5/20 14:30
	 */
	SearchResponse getSearch(List<String> index, List<EqDTO> eqDTOList
			, List<String> terms, List<String> fields, String nested, List<String> nestedFields
			, List<SpecialDTO> specialDTOList
			, List<ConditionDTO> conditionDTOList
			, List<List<List<ConditionDTO>>> listList
			, String highlightField, Integer startPage, Integer pageSize, List<SortDTO> sortDTOList) throws IOException;

	/**
	 * 文件检索示例
	 *
	 * @param index       索引集合
	 * @param eqDTOList   等于类型的匹配条件集合
	 * @param terms       检索词集合
	 * @param fields      被检索字段集合
	 * @param startPage   起始页
	 * @param pageSize    每页大小
	 * @param sortDTOList 排序集合
	 * @return com.detabes.search.es.vo.EsPage
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/5/20 11:09
	 */
	SearchResponse getSearchFile(List<String> index, List<EqDTO> eqDTOList, List<String> terms, List<String> fields
			, Integer startPage, Integer pageSize, List<SortDTO> sortDTOList) throws IOException;

	/**
	 * 分组统计示例
	 *
	 * @param index        索引名称集合
	 * @param eqDTOList    等于类型的匹配条件集合(拼接结果效果类似于:
	 *                     <p>
	 *                     and field1 in ('value1','value2','value3'...) <br/>
	 *                     and field2 in ('value1','value2','value3'...) <br/>
	 *                     and field3 in ('value1','value2','value3'...) <br/>
	 *                     ...)<br/>
	 *                     </p><br/>
	 * @param terms        检索词集合
	 * @param fields       被检索字段集合
	 * @param nested       被嵌套查询名称
	 * @param nestedFields 被嵌套检索的字段集合
	 * @param listList     AND 条件集合块(拼接结果效果类似于:
	 *                     <p>
	 *                     and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                     and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                     and ((高级组合) or (高级组合) or(高级组合)) <br/>
	 *                     ...)<br/>
	 *                     </p><br/>
	 * @param groupField   分组字段，唯一一个
	 * @return com.detabes.search.es.vo.EsPage
	 * @throws IOException IOException
	 * @date 2021/5/20 14:30
	 */
	List<Map<String, Object>> getGroup(List<String> index, List<EqDTO> eqDTOList
			, List<String> terms, List<String> fields, String nested, List<String> nestedFields
			, List<List<List<ConditionDTO>>> listList
			, String groupField) throws IOException;

	/**
	 * 执行分组聚合统计
	 *
	 * @param searchRequest 参数请求对象
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @throws IOException IOException
	 */
	List<Map<String, Object>> executeGroup(SearchRequest searchRequest) throws IOException;


	/**
	 * 执行分页查询
	 *
	 * @param request 请求对象
	 * @return org.elasticsearch.action.search.SearchResponse
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/6/24 11:56
	 */

	SearchResponse executePage(SearchRequest request) throws IOException;

	/**
	 * 查询所有
	 *
	 * @param searchRequest 请求参数
	 * @return org.elasticsearch.action.search.SearchResponse
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/6/29 17:20
	 */
	List<Map<String, Object>> getAll(SearchRequest searchRequest) throws IOException;

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
	 * @return org.elasticsearch.index.query.BoolQueryBuilder
	 * @author lxw
	 * @date 2021/5/20 11:56
	 */
	BoolQueryBuilder setEqCondition(BoolQueryBuilder boolQueryBuilder, List<EqDTO> eqDTOList);

	/**
	 * 设置多块指定条件 拼接结果效果类似于:
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
	void setAdvancedList(BoolQueryBuilder boolQueryBuilder, List<List<List<ConditionDTO>>> listList);

	/**
	 * 多条件常规拼接拼接结果效果类似于:
	 * <p>
	 * and (高级组合))<br/>
	 * </p><br/>
	 *
	 * @param conditionDTOList 条件集合
	 * @return BoolQueryBuilder
	 * @author l
	 */
	BoolQueryBuilder setAdvanced(List<ConditionDTO> conditionDTOList);

	/**
	 * 设置拼接指定条件 拼接结果效果类似于:
	 * <p>
	 * and field1 like '%value1%' <br/>
	 * and field2 like '%value2%' <br/>
	 * and field3 like '%value3%' <br/>
	 * ...<br/>
	 * </p><br/>
	 *
	 * @param bq             传入值
	 * @param specialDTOList 指定条件集合
	 * @author lxw
	 * @date 2021/6/23 12:56
	 */
	void setSpecialDTOList(BoolQueryBuilder bq, List<SpecialDTO> specialDTOList);

	/**
	 * 设置查询条件
	 *
	 * @param terms        检索词集合
	 * @param fields       需要检索常规字段集合
	 * @param nested       嵌套名称 如“fubiao”
	 * @param nestedFields 需要嵌套检索的字段集合
	 * @return org.elasticsearch.index.query.BoolQueryBuilder
	 */
	BoolQueryBuilder setConditionByTerm(List<String> terms, List<String> fields, String
			nested, List<String> nestedFields);

	/**
	 * 拼接常规条件
	 *
	 * @param terms  检索词集合
	 * @param fields 被检索字段集合
	 * @return BoolQueryBuilder
	 */
	BoolQueryBuilder setFieldsTerm(List<String> terms, List<String> fields);

	/**
	 * 拼接嵌套条件
	 *
	 * @param nested       索引嵌套结构名称
	 * @param terms        检索词集合
	 * @param nestedFields 被检索的嵌套条件
	 * @return org.elasticsearch.index.query.BoolQueryBuilder
	 */
	BoolQueryBuilder setNestedFieldsTerm(String nested, List<String> terms, List<String> nestedFields);

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
	 * @return org.elasticsearch.index.query.BoolQueryBuilder
	 */
	BoolQueryBuilder setConSymbol(String field, String fieldValue, String symbol);

	/**
	 * 设定指定检索字段
	 *
	 * @param sourceBuilder 查询条件对象
	 * @param fields        指定检索字段
	 * @author lxw
	 */
	void setAppointFields(SearchSourceBuilder sourceBuilder, String fields);

	/**
	 * 设置分页
	 *
	 * @param sourceBuilder 查询对象
	 * @param startPage     起始页
	 * @param pageSize      每页大小
	 * @author lxw
	 * @date 2021/5/11 16:28
	 */
	void setPage(SearchSourceBuilder sourceBuilder, Integer startPage, Integer pageSize);

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
	 * @author lxw
	 * @date 2021/5/12 18:38
	 */
	void setOrderField(SearchSourceBuilder sourceBuilder, List<SortDTO> list);

	/**
	 * 设置高亮
	 *
	 * @param query          查询对象
	 * @param highlightField 高亮字段
	 * @author lxw
	 * @date 2021/5/11 16:31
	 */
	void setHighlightField(SearchSourceBuilder query, String highlightField);

	/**
	 * 高亮结果集 特殊处理
	 *
	 * @param searchResponse 结果集
	 * @param highlightField 高亮字段
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @author lxw
	 * @date 2021/6/23 13:07
	 */
	List<Map<String, Object>> handleSearchResponse(SearchResponse searchResponse, String highlightField);
}
