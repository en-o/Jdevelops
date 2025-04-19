package cn.tannn.jdevelops.es.antlr;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 操作符支持大小写
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/26 10:01
 */
public class OperatorCaseTest {
    @Test
    void testOperatorCaseInsensitivity() {
        ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder().build();

        // 测试 IN 操作符大小写
        List<String> inQueries = Arrays.asList(
                "status in [\"active\"]",
                "status IN [\"active\"]",
                "status In [\"active\"]"
        );

        // 测试 NOT IN 操作符大小写
        List<String> notInQueries = Arrays.asList(
                "status not in [\"deleted\"]",
                "status NOT IN [\"deleted\"]",
                "status Not In [\"deleted\"]"
        );

        // 测试 EXISTS 操作符大小写
        List<String> existsQueries = Arrays.asList(
                "status exists",
                "status EXISTS",
                "status Exists"
        );

        // 验证所有 IN 查询生成相同的结果
        Query firstInQuery = queryBuilder.buildDemoQuery(inQueries.get(0));
        inQueries.forEach(query ->
                assertEquals(
                        firstInQuery.toString(),
                        queryBuilder.buildDemoQuery(query).toString(),
                        "IN operator should be case insensitive"
                )
        );

        // 验证所有 NOT IN 查询生成相同的结果
        Query firstNotInQuery = queryBuilder.buildDemoQuery(notInQueries.get(0));
        notInQueries.forEach(query ->
                assertEquals(
                        firstNotInQuery.toString(),
                        queryBuilder.buildDemoQuery(query).toString(),
                        "NOT IN operator should be case insensitive"
                )
        );

        // 验证所有 EXISTS 查询生成相同的结果
        Query firstExistsQuery = queryBuilder.buildDemoQuery(existsQueries.get(0));
        existsQueries.forEach(query ->
                assertEquals(
                        firstExistsQuery.toString(),
                        queryBuilder.buildDemoQuery(query).toString(),
                        "EXISTS operator should be case insensitive"
                )
        );
    }

    @Test
    void testChineseSupport() {
        ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder().build();

        // 测试中文字段名
        assertDoesNotThrow(() ->
                queryBuilder.buildDemoQuery("姓名 == \"张三\"")
        );

        // 测试中文值
        assertDoesNotThrow(() ->
                queryBuilder.buildDemoQuery("status in [\"活跃\", \"暂停\"]")
        );

        // 测试中文字段名和值的组合
        assertDoesNotThrow(() ->
                queryBuilder.buildDemoQuery("状态 in [\"活跃\", \"暂停\"]")
        );
    }
}
