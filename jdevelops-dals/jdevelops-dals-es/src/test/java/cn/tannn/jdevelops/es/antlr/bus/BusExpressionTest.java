
package cn.tannn.jdevelops.es.antlr.bus;

import cn.tannn.jdevelops.es.antlr.ElasticSearchQueryBuilder;
import org.junit.jupiter.api.Test;

/**
 *  bus 测试
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/26 09:50
 */
public class BusExpressionTest {

    @Test
    void testQueries() {
        ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder().build();

//        // 创建特殊值映射
//        Map<String, List<String>> specialValues = new HashMap<>();
//        specialValues.put("status", Arrays.asList("active", "pending", "completed"));
//
//        // 创建访问者实例
//        BusEsQueryVisitor visitor = new BusEsQueryVisitor(
//                new YourFieldTransformer(),
//                Collections.singletonList(new YourValueValidator()),
//                specialValues
//        );
//
//        // 使用示例
//        String query = "status in [active, pending] AND (age >= 18 OR city == '北京')";
//        // 解析并构建查询
//        ESParser parser = getParser(query); // 假设你有方法来获取解析器
//        Query esQuery = visitor.visit(parser.query());

    }

}
