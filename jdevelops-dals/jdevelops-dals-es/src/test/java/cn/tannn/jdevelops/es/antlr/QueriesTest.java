package cn.tannn.jdevelops.es.antlr;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 继续测试
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/26 09:50
 */
public class QueriesTest {

    @Test
    void testQueries() {
        ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder().build();

        // 测试精确匹配
        assertQueryWorks(queryBuilder, "name == \"john\"");

        // 测试否定查询
        assertQueryWorks(queryBuilder, "name != \"john\"");

        // 测试范围查询
        assertQueryWorks(queryBuilder, "age >= 18");
        assertQueryWorks(queryBuilder, "price < 100.50");

        // 测试正则表达式查询
        assertQueryWorks(queryBuilder, "email =~ \".*@gmail\\.com\"");
        assertQueryWorks(queryBuilder, "email !~ \".*@spam\\.com\"");

        // 测试数组查询
        assertQueryWorks(queryBuilder, "status in [\"active\", \"pending\"]");
        assertQueryWorks(queryBuilder, "status not in [\"deleted\", \"banned\"]");

    }

    private void assertQueryWorks(ElasticSearchQueryBuilder queryBuilder, String queryString) {
        Query query = queryBuilder.buildQuery(queryString);
        assertNotNull(query, "Query should not be null for: " + queryString);
        assertTrue(query.toString().length() > 0,
                "Query should generate valid ES query string for: " + queryString);
    }
}
