package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.core.ElasticService;
import cn.tannn.jdevelops.es.util.EsBasicsUtil;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElasticSearchQueryBuilderTest {
//
//    @Autowired
//    private ElasticService elasticService;

    @Test
    void buildQuery() {
        //  String expression = "title += \"论坚定理想信念（2023年）\" and years == 2021 ";
        String expression = "(name == \"tan\" and age >= 18 and (nick==\"s\" or zz==123)) OR (status == \"active\" and sex == 1 or title== \"sda\")";
        Query query = ElasticSearchQueryBuilder.buildQuery(expression);
//        System.out.println(query.toString());
        assertEquals("Query: {\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"term\":{\"name\":{\"value\":\"tan\"}}},{\"range\":{\"age\":{\"gte\":\"18\"}}}]}},{\"bool\":{\"should\":[{\"term\":{\"nick\":{\"value\":\"s\"}}},{\"term\":{\"zz\":{\"value\":\"123\"}}}]}}]}},{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"status\":{\"value\":\"active\"}}},{\"term\":{\"sex\":{\"value\":\"1\"}}}]}},{\"term\":{\"title\":{\"value\":\"sda\"}}}]}}]}}"
                ,query.toString());

    }


    @Test
    void requestEs() {
//        String expression = "title += \"论坚定理想信念（2023年）\" and years == 2021 ";
//        Query query = ElasticSearchQueryBuilder.buildQuery(expression);
//        System.err.println("============>"+query.toString());
//        SearchRequest.Builder request = new SearchRequest.Builder();
//        request.index("zzz_news");
//        request.query(query);
//        EsBasicsUtil.setIncludesFields(request,"title,years");
//        List<Map> maps = elasticService.searchList(request.build(), Map.class);
//        maps.forEach(System.out::println);
    }
}
