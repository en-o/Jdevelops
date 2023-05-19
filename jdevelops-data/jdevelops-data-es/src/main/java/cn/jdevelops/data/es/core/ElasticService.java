package cn.jdevelops.data.es.core;

import cn.jdevelops.data.es.entity.EsPageResult;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ElasticServiceImpl
 *
 * @author l
 * @version 1.0
 * @data 2023/2/10 11:37
 */
@Service
public interface ElasticService {

    /**
     * 验证索引是否存在
     *
     * @param indexName 索引名称
     * @return boolean true 存在
     * @author lxw
     * @date 2023/2/10 11:38
     **/
    public boolean existIndex(String indexName) throws IOException;

    /**
     * 创建索引，仅仅是索引名称，无任何mapping
     *
     * @param indexName 索引名称
     * @return boolean true 成功
     * @author lxw
     * @date 2023/2/10 11:56
     **/
    public boolean createIndex(String indexName) throws IOException;

    /**
     * 创建索引，带mapping的json文件流
     * <pre>
     *  获取mapping的json文件流示例：
     *  {@code
     *      ClassPathResource classPathResource = new ClassPathResource("esmapping/test8.json");
     *      InputStream input = classPathResource.getInputStream();
     *  }
     * </pre>
     *
     * @param indexName 索引名称
     * @param input     mapping的json文件流
     * @return boolean
     * @author lxw
     * @date 2023/2/10 12:10
     **/
    public boolean createIndex(String indexName, InputStream input) throws IOException;

    /**
     * 删除索引结构
     *
     * @param indexName 索引名称
     * @return boolean true 成功
     * @author lxw
     * @date 2023/2/10 12:03
     **/
    public boolean deleteIndex(String indexName) throws IOException;

    /**
     * 新增文档
     *
     * @param indexName 索引名称
     * @param id        id
     * @param t         对象
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/26 18:21
     **/
    public <T> Result addDocument(String indexName, String id, T t) throws IOException;

    /**
     * 修改数据
     *
     * @param indexName 索引名称
     * @param id        唯一id
     * @param t         数据
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/25 10:08
     **/
    public <T> Result updateDocument(String indexName, String id, T t) throws IOException;

    /**
     * 删除单条数据
     *
     * @param indexName 索引名称
     * @param id        数据唯一索引id
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/14 15:51
     **/
    public Result deleteById(String indexName, String id) throws IOException;

    /**
     * 根据查询条件删除数据
     *
     * @param indexName 索引名称
     * @param query     Query
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/14 15:51
     **/
    public Long deleteByQuery(String indexName, Query query) throws IOException;


    /**
     * 批量新增、修改数据操作
     *
     * @param indexName 索引名称
     * @param list      数据集
     * @param key       数据中唯一key
     * @return boolean
     * @author lxw
     * @date 2023/2/10 18:05
     **/
    public <T> boolean bulkAddDocument(String indexName, List<T> list, String key) throws Exception;

    /**
     * 批量操作新增、编辑，组装参数示例：
     * <pre>{@code
     *
     *     BulkRequest.Builder br = new BulkRequest.Builder();
     *        for (Test8Bean product : products) {
     *             br.operations(op -> op
     *                     .index(idx -> idx
     *                             .index("abc_test9")
     *                             .id(product.getNo())
     *                             .document(product)
     *                     )
     *             );
     *         }
     *     br.build();
     * }</pre>
     *
     * @param builder 数据操作
     * @return boolean
     * @author lxw
     * @date 2023/2/10 18:06
     **/
    public boolean bulkAddDocument(BulkRequest.Builder builder) throws IOException;
    /**
     * 批量删除数据
     *
     * @param indexName 索引名称
     * @param ids       ids
     * @return boolean
     * @author lxw
     * @date 2023/4/14 15:45
     **/
    public boolean batchDeleteDocument(String indexName, List<String> ids) throws IOException;

    /**
     * 验证数据是否存在
     *
     * @param index 索引名称
     * @param id    数据唯一索引id
     * @return boolean true 存在
     * @author lxw
     * @date 2023/2/10 15:21
     **/
    public boolean existsById(String index, String id) throws IOException;


    /**
     * 获取单条数据
     *
     * @param indexName 索引名称
     * @param id        数据唯一索引id
     * @param cc        返回对象类型
     * @return T
     * @author lxw
     * @date 2023/2/10 18:32
     **/
    public <T> T getById(String indexName, String id, Class<T> cc) throws IOException;

    /**
     * 根据ID批量查询
     * <pre>{@code
     *    # 结果取值示例
     *    for (MultiGetResponseItem<SiteStoreDO> doc : mgetResponse.docs()) {
     *       SiteStoreDO source = doc.result().source();
     *   }
     * }</pre>
     *
     * @param indexName 索引名称
     * @param ids       ids
     * @return java.util.List<co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem < T>>
     * @author lxw
     * @date 2023/4/25 11:43
     **/
    public <T> MgetResponse<T> mget(String indexName, List<String> ids, Class<T> cc) throws IOException;


    /**
     * 查询数量
     *
     * @param indexName 索引
     * @param query     查询条件
     * @return long
     * @author lxw
     * @date 2023/4/26 10:59
     **/
    public long count(String indexName, Query query) throws IOException;

    /**
     * 获取所有数据
     *
     * @param indexName 索引名称
     * @param cc        返回对象类型
     * @return T
     * @author lxw
     * @date 2023/2/10 18:32
     **/
    public <T> List<T> getAll(String indexName, Query query, Class<T> cc) throws IOException;


    /**
     * 查询数据,需自己处理结果集
     *
     * @param request        request
     * @param tDocumentClass tDocumentClass
     * @return co.elastic.clients.elasticsearch.core.SearchResponse<T>
     * @author lxw
     * @date 2023/4/21 10:28
     **/
    public <T> SearchResponse<T> search(SearchRequest request, Class<T> tDocumentClass) throws IOException;


    /**
     * 分页查询
     *
     * @param request 条件
     * @param cc      返回类
     * @return cn.iocoder.yudao.framework.common.pojo.PageResult<T>
     * @author lxw
     * @date 2023/4/23 10:14
     **/
    public <T> EsPageResult<T> searchPage(SearchRequest request, Class<T> cc);


    /**
     * 只返回当前页结果集
     *
     * @param request 条件
     * @param cc      返回类
     * @return java.util.List<T>
     * @author lxw
     * @date 2023/4/23 10:14
     **/
    public <T> List<T> searchList(SearchRequest request, Class<T> cc);


}
