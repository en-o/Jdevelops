package com.detabes.search.es.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 索引操做工具类
 *
 * @author lxw
 * @version V1.0
 * @date 2021/5/12
 **/
@Slf4j
@Component
public class EsIndexUtil {
    private RestHighLevelClient restHighLevelClient;

    public EsIndexUtil(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 创建索引
     * PS：相当于创建了一个表，一个类型的仓库
     *
     * @param index 索引
     * @return boolean
     */
    public boolean createIndex(String index) throws IOException {
        if (isIndexExist(index)) {
            log.error("Index is  exits!");
            return false;
        }
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //2.执行客户端请求
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index 索引
     * @return boolean
     */
    public boolean isIndexExist(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     *
     * @param index 索引
     * @return boolean
     */
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

    /**
     * 单个字段创建索引库映射
     * PS: index 必须存在，若不存在则无法创建映射。
     *
     * @param index 索引名称
     * @param field 常规字段
     * @return boolean
     */
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
        builder.startObject(field)
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword").field("type", "keyword").field("ignore_above", "256").endObject()
                .endObject()
                .field("analyzer", "ik_max_word")
                .endObject();
        builder.endObject().endObject();
        //2.添加mapping
        PutMappingRequest mappingRequest = Requests.putMappingRequest(index).type("_doc").source(builder);
        //2.执行客户端请求
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);
        boolean acknowledged = acknowledgedResponse.isAcknowledged();
        System.out.println(acknowledged);
        return acknowledged;
    }

    /**
     * 创建索引库映射
     * PS: index不存在会先创建，存在则会先执行删除，再创建index，最后创建字段映射
     * <p>
     * {
     * id: 1,
     * name: 111,
     * sunFile:{
     * id: 1-1,
     * name, 1-111
     * }
     * }
     * <p/>
     *
     * @param index        索引名称
     * @param fields       常规字段
     * @param nested       嵌套名称
     * @param nestedFields 嵌套查询字段
     * @return boolean
     */
    public boolean createIndexMapping(String index, List<String> fields, String nested, List<String> nestedFields) throws Exception {
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
        builder.startObject("tableId").field("type", "keyword").endObject()
                .startObject("dataId").field("type", "keyword").endObject()
                .startObject("tableChName").field("type", "keyword").endObject()
                .startObject("tableEnName").field("type", "keyword").endObject()
                .startObject("business_destroy_state").field("type", "keyword").endObject()
                .startObject("is_appraisal").field("type", "keyword").endObject()
                .startObject("is_edit").field("type", "keyword").endObject()
                .startObject("isLibrary").field("type", "keyword").endObject();

        //组件常规映射字段
        for (String field : fields) {
            builder.startObject(field)
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword").field("type", "keyword").field("ignore_above", "256").endObject()
                    .endObject()
                    .field("analyzer", "ik_max_word")
                    .endObject();
        }
        // 组件常规结束

        // 组件子集映射 开始
        if (StringUtils.isNoneBlank(nested) && nestedFields != null && nestedFields.size() > 0) {
            builder.startObject(nested).field("type", "nested").startObject("properties")
                    .startObject("tableId").field("type", "keyword").endObject()
                    .startObject("dataId").field("type", "keyword").endObject()
                    .startObject("tableChName").field("type", "keyword").endObject()
                    .startObject("tableEnName").field("type", "keyword").endObject()
                    .startObject("business_destroy_state").field("type", "keyword").endObject()
                    .startObject("is_appraisal").field("type", "keyword").endObject()
                    .startObject("is_edit").field("type", "keyword").endObject()
                    .startObject("isLibrary").field("type", "keyword").endObject();
            for (String field : nestedFields) {
                builder.startObject(field)
                        .field("type", "text")
                        .startObject("fields")
                        .startObject("keyword").field("type", "keyword").field("ignore_above", "256").endObject()
                        .endObject()
                        .field("analyzer", "ik_max_word")
                        .endObject();
            }
        }
        builder.endObject().endObject();
        // 组件子集映射结束
        builder.endObject().endObject();
        //2.添加mapping
        PutMappingRequest mappingRequest = Requests.putMappingRequest(index).type("_doc").source(builder);
        //2.执行客户端请求
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);
        boolean acknowledged = acknowledgedResponse.isAcknowledged();
        System.out.println(acknowledged);
        return true;
    }

    /**
     * 新增/更新数据
     *
     * @param object 要新增/更新的数据
     * @param index  索引，类似数据库
     * @param id     数据ID
     * @return string
     */
    public String submitData(Object object, String index, String id) throws IOException {
        if (null == id) {
            return addData(object, index);
        }
        if (this.existsById(index, id)) {
            return this.updateDataByIdNoRealTime(object, index, id);
        } else {
            return addData(object, index, id);
        }
    }

    /**
     * 新增数据，自定义id
     *
     * @param object 要增加的数据
     * @param index  索引，类似数据库
     * @param id     数据ID,为null时es随机生成
     * @return String
     */
    public String addData(Object object, String index, String id) throws IOException {
        if (null == id) {
            return addData(object, index);
        }
        if (this.existsById(index, id)) {
            return this.updateDataByIdNoRealTime(object, index, id);
        }
        //创建请求
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.timeout(TimeValue.timeValueSeconds(1));
        //将数据放入请求 json
        request.source(JSON.toJSONString(object), XContentType.JSON);
        //客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        log.info("添加数据成功 索引为: {}, response 状态: {}, id为: {}", index, response.status().getStatus(), response.getId());
        return response.getId();
    }

    /**
     * 数据添加 随机id
     *
     * @param object 要增加的数据
     * @param index  索引，类似数据库
     * @return string
     */
    public String addData(Object object, String index) throws IOException {
        return addData(object, index, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param id    索引数据ID
     * @return string
     */
    public String deleteDataById(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return deleteResponse.getId();
    }

    /**
     * 通过ID 更新数据
     *
     * @param object 要更新数据
     * @param index  索引，类似数据库
     * @param id     索引数据ID
     * @return
     */
    public String updateDataById(Object object, String index, String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.timeout("1s");
        updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("索引为: {}, id为: {},updateResponseID：{}, 更新数据成功", index, id, updateResponse.getId());
        return updateResponse.getId();
    }

    /**
     * 通过ID 更新数据,保证实时性
     *
     * @param object 要增加的数据
     * @param index  索引，类似数据库
     * @param id     索引数据ID
     * @return string
     */
    public String updateDataByIdNoRealTime(Object object, String index, String id) throws IOException {
        //更新请求
        UpdateRequest updateRequest = new UpdateRequest(index, id);

        //保证数据实时更新
        updateRequest.setRefreshPolicy("wait_for");

        updateRequest.timeout("1s");
        updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);
        //执行更新请求
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("索引为: {}, id为: {},updateResponseID：{}, 实时更新数据成功", index, id, updateResponse.getId());
        return updateResponse.getId();
    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param id     索引数据ID
     * @param fields 需要查询的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public Map<String, Object> searchDataById(String index, String id, String fields) throws IOException {
        GetRequest request = new GetRequest(index, id);
        if (StringUtils.isNotEmpty(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 通过ID判断文档是否存在
     *
     * @param index 索引，类似数据库
     * @param id    索引数据ID
     * @return
     */
    public boolean existsById(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        //不获取返回的_source的上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 随机ID，批量插入
     * PS: false 指得是没有失败的数据
     *
     * @param index   索引，类似数据库
     * @param objects 数据
     * @return
     */
    public boolean bulkPost(String index, List<?> objects) {
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse response = null;
        //最大数量不得超过20万
        for (Object object : objects) {
            IndexRequest request = new IndexRequest(index);
            request.source(JSON.toJSONString(object), XContentType.JSON);
            bulkRequest.add(request);
        }
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null != response && response.hasFailures();
    }

    /**
     * 指定ID，批量插入
     * PS: false 指得是没有失败的数据
     *
     * @param index 索引，类似数据库
     * @param list  数据 map 中必须有一个key:esId,且在数据中唯一，用作设定索引唯一值
     * @return
     */
    public boolean bulkPostAppointId(String index, List<Map<String, Object>> list) {
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse response = null;
        //最大数量不得超过20万
        for (Map<String, Object> object : list) {
            IndexRequest request = new IndexRequest(index);
            request.id(object.get("esId").toString());
            request.source(JSON.toJSONString(object), XContentType.JSON);
            bulkRequest.add(request);
        }
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null != response && response.hasFailures();
    }
}
