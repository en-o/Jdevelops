package cn.jdevelops.search.es.service;

import cn.jdevelops.search.es.dto.ConditionDTO;
import cn.jdevelops.search.es.dto.EqDTO;
import cn.jdevelops.search.es.dto.SortDTO;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;

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
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @throws IOException IOException
	 */
	Map<String, Object> searchDataById(String index, String esOnlyId) throws IOException;

	/**
	 * 通过ID获取数据
	 *
	 * @param index    索引，类似数据库
	 * @param esOnlyId ES索引唯一值
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @throws IOException IOException
	 */
	GetResponse getById(String index, String esOnlyId) throws IOException;

	/**
	 * 一次性查询多个指定结果
	 *
	 * @param multiGetRequest 条件
	 * @return MultiGetResponse 返回参数
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2022/3/16 16:13
	 */
	MultiGetResponse executMget(MultiGetRequest multiGetRequest) throws IOException;

	/**
	 * 一次性查询多个指定结果
	 * @param indexName 索引名称
	 * @param ids ES 唯一编号集合
	 * @return org.elasticsearch.action.get.MultiGetResponse
	 * @author lxw
	 * @date 2022/6/13 18:31
	 * @exception IOException IOException
	 */
	MultiGetResponse executMget(String indexName,List<String> ids) throws IOException;

	/**
	 * 查询
	 *
	 * @param request 请求对象
	 * @return org.elasticsearch.action.search.SearchResponse
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/6/24 11:56
	 */

	SearchResponse executeSearch(SearchRequest request) throws IOException;


	/**
	 * 统计
	 *
	 * @param countRequest 统计条件
	 * @return CountResponse 返回参数
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2022/3/16 16:09
	 */
	CountResponse executeCount(CountRequest countRequest) throws IOException;

	/**
	 * 无条件分页检索示例
	 *
	 * @param index     索引名称集合["anjuan","xiangmu","wenben"]
	 * @param startPage 起始页
	 * @param pageSize  每页大小
	 * @return com.detabes.search.es.vo.EsPage
	 * @throws IOException IOException
	 */
	SearchResponse page(List<String> index, Integer startPage, Integer pageSize) throws IOException;

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
	SearchResponse getSearch(List<String> index
			, List<EqDTO> eqDTOList
			, List<String> terms, List<String> fields
			, String nested, List<String> nestedFields
			, List<ConditionDTO> conditionDTOList
			, List<List<List<ConditionDTO>>> listList
			, String highlightField
			, Integer startPage
			, Integer pageSize
			, List<SortDTO> sortDTOList) throws IOException;

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
	 * 分组统计示例，根据实际需求自己些
	 *
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/12/14 14:12
	 */
	List<Map<String, Object>> groupExample() throws IOException;
}
