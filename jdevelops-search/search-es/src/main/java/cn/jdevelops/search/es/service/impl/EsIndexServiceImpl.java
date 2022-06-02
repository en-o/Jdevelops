package cn.jdevelops.search.es.service.impl;

import cn.jdevelops.search.es.service.EsIndexService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ES索引操作实现
 *
 * @author lxw
 * @version V1.0
 * @date 2021/6/23
 **/
@Slf4j
@Service
public class EsIndexServiceImpl implements EsIndexService {

	private final RestHighLevelClient restHighLevelClient;

	public EsIndexServiceImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public boolean createIndex(String index) throws IOException {
		if (isIndexExist(index)) {
			log.error("Index is  exits!");
			return false;
		}
		//1.创建索引请求
		CreateIndexRequest request = new CreateIndexRequest(index);
		//1.settings
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("index.max_ngram_diff", "10")
				.startObject("analysis");
		builder.startObject("analyzer")
				.startObject("ngram_analyzer")
				.field("tokenizer", "ngram_tokenizer")
				.field("filter", "lowercase")
				.endObject()
				.endObject();

		builder.startObject("tokenizer")
				.startObject("ngram_tokenizer")
				.field("type", "ngram")
				.field("min_gram", "1")
				.field("max_gram", "1")
				.array("token_chars", "letter", "digit")
				.endObject()
				.endObject();
		builder.startObject("normalizer")
				.startObject("lowercase")
				.field("type", "custom")
				.field("filter", "lowercase")
				.endObject()
				.endObject();
		builder.endObject().endObject();
		request.settings(builder);
		//2.执行客户端请求
		CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}

	@Override
	public boolean isIndexExist(String index) throws IOException {
		GetIndexRequest request = new GetIndexRequest(index);
		return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public boolean deleteIndex(String index) throws IOException {
		if (!isIndexExist(index)) {
			log.error("Index is not exits!");
			return false;
		}
		DeleteIndexRequest request = new DeleteIndexRequest(index);
		AcknowledgedResponse delete = restHighLevelClient.indices()
				.delete(request, RequestOptions.DEFAULT);
		return delete.isAcknowledged();
	}

	@Override
	public boolean createIndexMapping(String index, List<String> fields) throws Exception {
		boolean b = isIndexExist(index);
		if (b) {
			deleteIndex(index);
		}
		b = createIndex(index);
		if (!b) {
			log.error("Index is create fail!");
			return false;
		}
		//1.设置mapping
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject("properties");
		// ES索引唯一值，将每条数据中指定唯一值，便于索引操作
		builder.startObject("esOnlyId").field("type", "keyword").endObject();

		//组件常规映射字段
		for (String field : fields) {
			timeOrDate2Keyword(builder, field);
		}
		builder.endObject().endObject();
		return submitMapping(index, builder);
	}

	@Override
	public boolean createIndexMapping(String index, String field) throws Exception {
		boolean b = isIndexExist(index);
		if (!b) {
			log.error("Index is not exist!");
			return false;
		}
		//1.设置mapping
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject("properties");
		timeOrDate2Keyword(builder, field);
		builder.endObject().endObject();
		return submitMapping(index, builder);
	}

	@Override
	public boolean createIndexMapping(String index, String nested, List<String> nestedFields) throws Exception {
		boolean b = isIndexExist(index);
		if (!b) {
			log.error("Index is not exist!");
			return false;
		}
		//1.设置mapping
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject("properties");
		// -------------嵌套映射开始
		builder.startObject(nested).field("type", "nested").startObject("properties");
		for (String field : nestedFields) {
			timeOrDate2Keyword(builder, field);
		}
		builder.endObject().endObject();
		// -------------嵌套映射结束
		builder.endObject().endObject();
		return submitMapping(index, builder);
	}

	/**
	 * 提交映射请求
	 *
	 * @param index   索引名称
	 * @param builder 索引结构
	 * @return boolean true is success
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/6/22 14:23
	 */
	private boolean submitMapping(String index, XContentBuilder builder) throws IOException {
		//1.添加mapping
		PutMappingRequest mappingRequest = new PutMappingRequest(index).source(builder);
		//2.执行客户端请求
		AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);
		return acknowledgedResponse.isAcknowledged();
	}

	/**
	 * 创建索映射
	 *
	 * @param index   索引名称
	 * @param mapping mapping映射json字符串
	 * @return boolean
	 * @throws IOException Exception
	 * @author lxw
	 * @date 2022/4/13 16:29
	 */
	@Override
	public boolean submitMapping(String index, String mapping) throws IOException {
		boolean b = isIndexExist(index);
		if (b) {
			deleteIndex(index);
		}
		//1.创建索引请求
		CreateIndexRequest request = new CreateIndexRequest(index);
		request.mapping(mapping, XContentType.JSON);
		//2.执行客户端请求
		CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}

	/**
	 * 包含time或者date的字段类型设定为keyword，且不分词
	 * <p>
	 * 若结尾是"time"，则类型为 keyword；<br/>
	 * 若结尾是 "dateFormat"，则类型为 date； <br/>
	 * 若等于 "status"，则类型为 integer； <br/>
	 * 若等于 "id"，则类型为 integer； <br/>
	 * 若结尾是 "stats"，则类型为 integer； <br/>
	 * 若结尾是 "type"，则类型为 integer； <br/>
	 * </p>
	 *
	 * @param builder 构建器
	 * @param field   字段
	 * @throws IOException IOException
	 * @author lxw
	 * @date 2021/6/23 17:03
	 */
	private void timeOrDate2Keyword(XContentBuilder builder, String field) throws IOException {
		String time = "time";
		String dateFormat = "dateFormat";
		String status = "status";
		String stats = "stats";
		String type = "type";
		String id = "id";
		if (field.toLowerCase().endsWith(dateFormat.toLowerCase())) {
			builder.startObject(field).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject();

		} else if (field.toLowerCase().endsWith(time)) {
			builder.startObject(field).field("type", "keyword").endObject();

		} else if (StringUtils.equals(field.toLowerCase(), status)
				|| StringUtils.equals(field.toLowerCase(), id)
				|| field.toLowerCase().endsWith(type)
				|| field.toLowerCase().endsWith(stats)
		) {
			builder.startObject(field).field("type", "integer").endObject();

		} else {
			builder.startObject(field)
					.field("type", "text")
					.field("analyzer", "ik_max_word")
					.startObject("fields")
					.startObject("keyword")
					.field("type", "keyword")
					.field("ignore_above", "256")
					.endObject()
					.startObject("standard")
					.field("type", "text")
					.field("analyzer", "standard")
					.endObject()
					.startObject("ngram")
					.field("type", "text")
					.field("analyzer", "ngram_analyzer")
					.endObject()
					.endObject()
					.endObject();
		}
	}

	@Override
	public boolean submitData(String index, Object source, String esOnlyId) throws IOException {
		if (null == esOnlyId) {
			return addData(index, source);
		}
		if (this.existsById(index, esOnlyId)) {
			return this.updateDataByIdNoRealTime(index, source, esOnlyId);
		} else {
			return addData(index, source, esOnlyId);
		}
	}

	@Override
	public boolean addData(String index, Object source, String esOnlyId) throws IOException {
		if (StringUtils.isBlank(esOnlyId)) {
			return addData(index, source);
		}
		if (this.existsById(index, esOnlyId)) {
			return this.updateDataByIdNoRealTime(index, source, esOnlyId);
		}
		//创建请求
		IndexRequest request = new IndexRequest(index);
		request.id(esOnlyId);
		request.timeout(TimeValue.timeValueSeconds(1));
		//将数据放入请求 json
		JSON.toJSONString(source);
		request.source(JSON.toJSONString(source), XContentType.JSON);
		//客户端发送请求
		return addData(request);
	}

	@Override
	public boolean addData(String index, Object source) throws IOException {
		return addData(index, source, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
	}

	@Override
	public boolean addData(IndexRequest request) throws IOException {
		//客户端发送请求
		IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
		log.info("添加数据成功 索引为: {}, response 状态: {}, esOnlyId为: {}", response.getIndex(), response.status().getStatus(), response.getId());
		return response.getId() != null;
	}

	@Override
	public boolean deleteDataById(String index, String esOnlyId) throws IOException {
		DeleteRequest request = new DeleteRequest(index, esOnlyId);
		DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
		return deleteResponse.getId() != null;
	}

	@Override
	public boolean updateDataById(String index, Object source, String esOnlyId) throws IOException {
		UpdateRequest updateRequest = new UpdateRequest(index, esOnlyId);
		updateRequest.timeout("1s");
		updateRequest.upsert(JSON.toJSONString(source), XContentType.JSON);
		UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
		log.info("索引为: {}, id为: {},updateResponseID：{}, 更新数据成功", index, esOnlyId, updateResponse.getId());
		return updateResponse.getId() != null;
	}

	@Override
	public boolean updateDataByIdNoRealTime(String index, Object source, String esOnlyId) throws IOException {
		//更新请求
		UpdateRequest updateRequest = new UpdateRequest(index, esOnlyId);
		//保证数据实时更新
		updateRequest.setRefreshPolicy("wait_for");
		updateRequest.timeout("1s");
		updateRequest.doc(JSON.toJSONString(source), XContentType.JSON);
		//执行更新请求
		UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
		log.info("索引为: {}, id为: {},updateResponseID：{}, 实时更新数据成功", index, esOnlyId, updateResponse.getId());
		return updateResponse.getId() != null;
	}

	@Override
	public boolean existsById(String index, String esOnlyId) throws IOException {
		GetRequest request = new GetRequest(index, esOnlyId);
		//不获取返回的_source的上下文
		request.fetchSourceContext(new FetchSourceContext(false));
		request.storedFields("_none_");
		return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public boolean bulkPost(String index, List<Object> objects) throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		//最大数量不得超过20万
		for (Object source : objects) {
			IndexRequest request = new IndexRequest(index);
			request.source(JSON.toJSONString(source), XContentType.JSON);
			bulkRequest.add(request);
		}
		BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		return null != response && response.hasFailures();
	}

	@Override
	public boolean bulkPost(BulkRequest bulkRequest) throws IOException {
		BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		return null != response && response.hasFailures();
	}

	@Override
	public boolean bulkPostAppointId(String index, List<Map<String, Object>> list) throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		//最大数量不得超过20万
		for (Map<String, Object> source : list) {
			IndexRequest request = new IndexRequest(index);
			request.id(source.get("esOnlyId").toString());
			request.source(JSON.toJSONString(source), XContentType.JSON);
			bulkRequest.add(request);
		}
		BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		return null != response && response.hasFailures();
	}

}
