package cn.jdevelops.data.es.core;

import cn.jdevelops.api.result.response.PageResult;
import cn.jdevelops.data.es.entity.EsPageResult;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ElasticServiceImpl
 *
 * @author l
 * @version 1.0
 * @data 2023/2/10 11:37
 */
@ConditionalOnMissingBean(ElasticService.class)
public class ElasticServiceImpl implements ElasticService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Resource
    private ElasticsearchClient client;

    /**
     * 验证索引是否存在
     *
     * @param indexName 索引名称
     * @return boolean true 存在
     * @author lxw
     * @date 2023/2/10 11:38
     **/
    @Override
    public boolean existIndex(String indexName) throws IOException {
        BooleanResponse exists = client.indices().exists(e -> e.index(indexName));
        return exists.value();
    }

    /**
     * 创建索引，仅仅是索引名称，无任何mapping
     *
     * @param indexName 索引名称
     * @return boolean true 成功
     * @author lxw
     * @date 2023/2/10 11:56
     **/
    @Override
    public boolean createIndex(String indexName) throws IOException {
        if (existIndex(indexName)) {
            logger.error("Index is  exits!");
            return false;
        }
        boolean created = Boolean.TRUE.equals(client.indices().create(c -> c.index(indexName)).acknowledged());
        logger.info("索引创建结果：" + created);
        return created;
    }

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
    @Override
    public boolean createIndex(String indexName, InputStream input) throws IOException {
        if (existIndex(indexName)) {
            logger.error("Index is  exits!");
            return false;
        }
        boolean created = Boolean.TRUE.equals(client.indices().create(c -> c.index(indexName).withJson(input)).acknowledged());
        logger.info("索引创建结果：" + created);
        return created;
    }

    /**
     * 删除索引结构
     *
     * @param indexName 索引名称
     * @return boolean true 成功
     * @author lxw
     * @date 2023/2/10 12:03
     **/
    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        if (!existIndex(indexName)) {
            logger.error("Index is not exits!");
            return false;
        }
        boolean deleted = client.indices().delete(d -> d.index(indexName)).acknowledged();
        logger.info("索引删除结果：" + deleted);
        return deleted;
    }

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
    @Override
    public <T> Result addDocument(String indexName, String id, T t) throws IOException {
        CreateRequest<T> createRequest = CreateRequest.of(c -> c
                .index(indexName.trim())
                .id(id.trim())
                .document(t)
                .refresh(Refresh.True)
        );
        CreateResponse createResponse = client.create(createRequest);
        return createResponse.result();
    }

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
    @Override
    public <T> Result updateDocument(String indexName, String id, T t) throws IOException {
        IndexRequest<T> indexRequest = IndexRequest.of(i -> i
                .index(indexName.trim())
                .id(id.trim())
                .document(t)
                .refresh(Refresh.True)
        );
        IndexResponse index = client.index(indexRequest);
        return index.result();
    }

    /**
     * 删除单条数据
     *
     * @param indexName 索引名称
     * @param id        数据唯一索引id
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/14 15:51
     **/
    @Override
    public Result deleteById(String indexName, String id) throws IOException {
        DeleteRequest of = DeleteRequest.of(i -> i
                .index(indexName)
                .id(id)
                .refresh(Refresh.True)
        );
        DeleteResponse delete = client.delete(of);
        return delete.result();

    }

    /**
     * 根据查询条件删除数据
     *
     * @param indexName 索引名称
     * @param query     Query
     * @return co.elastic.clients.elasticsearch._types.Result
     * @author lxw
     * @date 2023/4/14 15:51
     **/
    @Override
    public Long deleteByQuery(String indexName, Query query) throws IOException {
        DeleteByQueryRequest request = DeleteByQueryRequest.of(c -> c
                .index(indexName)
                .query(query)
                .refresh(true)
        );
        DeleteByQueryResponse delete = client.deleteByQuery(request);
        return delete.deleted();

    }

    /**
     * 批量删除数据
     *
     * @param indexName 索引名称
     * @param ids       ids
     * @return boolean
     * @author lxw
     * @date 2023/4/14 15:45
     **/
    @Override
    public boolean deleteByIds(String indexName, List<String> ids) throws IOException {
        // 构建一个批量数据集合

        BulkRequest.Builder br = new BulkRequest.Builder();
        for (String id : ids) {
            br.operations(op -> op.delete(
                    d -> d.index(indexName).id(id)
            ));
        }
        br.refresh(Refresh.True);
        BulkResponse bulk = client.bulk(br.build());
        return !bulk.errors();
    }


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
    @Override
    public <T> boolean bulkAddDocument(String indexName, List<T> list, String key) throws Exception {

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (T t : list) {
            Class<?> clazz = t.getClass();
            Field field = clazz.getDeclaredField(key);
            field.setAccessible(true);
            Object o = field.get(t);
            br.operations(op -> op.index(idx -> idx.index(indexName).id(o.toString()).document(t)));
        }
        br.refresh(Refresh.True);
        BulkResponse bulk = client.bulk(br.build());

        return !bulk.errors();
    }

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
    @Override
    public boolean bulkAddDocument(BulkRequest.Builder builder) throws IOException {
        builder.refresh(Refresh.True);
        BulkResponse bulk = client.bulk(builder.build());
        return !bulk.errors();
    }

    /**
     * 验证数据是否存在
     *
     * @param index 索引名称
     * @param id    数据唯一索引id
     * @return boolean true 存在
     * @author lxw
     * @date 2023/2/10 15:21
     **/
    @Override
    public boolean existsById(String index, String id) throws IOException {
        BooleanResponse exists = client.exists(e -> e.index(index).id(id));
        return exists.value();
    }


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
    @Override
    public <T> T getById(String indexName, String id, Class<T> cc) throws IOException {
        GetResponse<T> response = client.get(g -> g.index(indexName).id(id), cc);
        if (response.found()) {
            return response.source();
        }
        return null;
    }

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
    @Override
    public <T> MgetResponse<T> mget(String indexName, List<String> ids, Class<T> cc) throws IOException {
        MgetRequest mgetRequest = MgetRequest.of(m -> m.index(indexName.trim()).ids(ids));
        return client.mget(mgetRequest, cc);
    }


    /**
     * 查询数量
     *
     * @param indexName 索引
     * @param query     查询条件
     * @return long
     * @author lxw
     * @date 2023/4/26 10:59
     **/
    @Override
    public long count(String indexName, Query query) throws IOException {
        CountRequest countRequest = CountRequest.of(c -> c
                .index(indexName.trim())
                .query(query)
        );
        CountResponse count = client.count(countRequest);
        return count.count();
    }

    /**
     * 获取所有数据
     *
     * @param indexName 索引名称
     * @param cc        返回对象类型
     * @return T
     * @author lxw
     * @date 2023/2/10 18:32
     **/
    @Override
    public <T> List<T> getAll(String indexName, Query query, Class<T> cc) throws IOException {
        // 返回数据集合
        List<T> list = new ArrayList<>();
        // scrollId 集合，最后清除使用
        List<String> scrollIds = new ArrayList<>();
        // 使用scroll深度分页查询
        SearchRequest searchRequest = new SearchRequest.
                Builder().index(indexName)
                .query(query)
                .size(5000)
                .scroll(Time.of(t -> t.time("2m")))
                .build();

        SearchResponse<T> response = client.search(searchRequest, cc);
        String scrollId = response.scrollId();
        scrollIds.add(scrollId);
        System.out.println("size: " + response.hits().hits().size());

        for (Hit<T> hit : response.hits().hits()) {
            list.add(hit.source());
        }
        //循环查询，直到查不到数据
        while (true) {
            String finalScrollId = scrollId;
            ScrollResponse<T> scrollResponse = client.scroll(s -> s.scrollId(finalScrollId).scroll(Time.of(t -> t.time("2m"))), cc);
            scrollId = scrollResponse.scrollId();
            scrollIds.add(scrollId);
            List<Hit<T>> hits = scrollResponse.hits().hits();
            if (hits == null || hits.size() == 0) {
                break;
            }
            System.out.println("size: " + hits.size());
            for (Hit<T> hit : hits) {
                list.add(hit.source());
            }
        }
        //清除 scroll
        client.clearScroll(c -> c.scrollId(scrollIds));
        return list;
    }

    /**
     * 查询数据,需自己处理结果集
     *
     * @param request        request
     * @param tDocumentClass tDocumentClass
     * @return co.elastic.clients.elasticsearch.core.SearchResponse<T>
     * @author lxw
     * @date 2023/4/21 10:28
     **/
    @Override
    public <T> SearchResponse<T> search(SearchRequest request, Class<T> tDocumentClass) throws IOException {
        return client.search(request, tDocumentClass);
    }


    /**
     * 分页查询
     *
     * @param request 条件
     * @param cc      返回类
     * @return cn.iocoder.yudao.framework.common.pojo.PageResult<T>
     * @author lxw
     * @date 2023/4/23 10:14
     **/
    @Override
    public <T> PageResult<T> searchPage(SearchRequest request, Class<T> cc) {
        List<T> list = new ArrayList<>();
        try {
            SearchResponse<T> search = client.search(request, cc);
            if (search != null) {
                long total = search.hits().total().value();
                List<Hit<T>> hits = search.hits().hits();
                for (Hit<T> hit : hits) {
                    list.add(hit.source());
                }
                // 转换返回
                return EsPageResult.page(total, list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EsPageResult.page(0L, list);
    }


    /**
     * 只返回当前页结果集
     *
     * @param request 条件
     * @param cc      返回类
     * @return java.util.List<T>
     * @author lxw
     * @date 2023/4/23 10:14
     **/
    @Override
    public <T> List<T> searchList(SearchRequest request, Class<T> cc) {
        List<T> list = new ArrayList<>();
        try {
            SearchResponse<T> search = client.search(request, cc);
            if (search != null) {
                List<Hit<T>> hits = search.hits().hits();
                for (Hit<T> hit : hits) {
                    list.add(hit.source());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }


}
