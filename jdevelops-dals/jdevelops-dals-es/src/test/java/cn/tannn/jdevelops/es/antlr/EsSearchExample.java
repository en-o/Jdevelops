package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.util.EsBasicsUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import jakarta.annotation.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 检索示例
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/26 11:33
 */
public class EsSearchExample {

    @Resource
    private ElasticsearchClient client;

//    @Test
    void requestEs() throws IOException {
        var queryBuilder = new ElasticSearchQueryBuilder.Builder().build();
        String expression = "title += \"论坚定理想信念（2023年）\" and years == 2021 ";
        Query query = queryBuilder.buildDemoQuery(expression);

        System.err.println("============>"+query.toString());

        SearchRequest.Builder request = new SearchRequest.Builder();
        // 设置索引
        request.index("zzz_news");
        request.query(query);
        // 设置返回字段
        EsBasicsUtil.setIncludesFields(request,"title,years");

        SearchResponse<Map> searchResponse = client.search(request.build(), Map.class);
        // 结果处理
        long total = 0L;
        if (searchResponse != null && searchResponse.hits().total() != null) {
            total = searchResponse.hits().total().value();
            List<Map<String, Object>> list = new ArrayList<>();
            List<Hit<Map>> hits = searchResponse.hits().hits();
            for (Hit<Map> hit : hits) {
                Map<String, Object> source = hit.source();
                if (source == null) {
                    continue;
                }
                source.put("_index", hit.index());
                source.put("_id", hit.id());
                source.put("_score", hit.score());
                list.add(source);
            }
        }
    }
}
