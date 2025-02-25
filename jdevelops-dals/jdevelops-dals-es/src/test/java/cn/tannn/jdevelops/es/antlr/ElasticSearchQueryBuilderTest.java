package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.tools.EnumValueValidator;
import cn.tannn.jdevelops.es.antlr.tools.RegexValueValidator;
import cn.tannn.jdevelops.es.exception.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ElasticSearchQueryBuilderTest {
//
//    @Autowired
//    private ElasticService elasticService;

    @Test
    void testRegexQueries() {
        var queryBuilder = new ElasticSearchQueryBuilder.Builder().build();

        // 测试正则表达式匹配
        var regex = List.of(
                "name=~.*test.*",
                "email=~.*@gmail\\.com",
                "title!~temp.*"
        );

        for (String query : regex) {
            Query result = queryBuilder.buildQuery(query);
            assertNotNull(result, "Regex query should not be null for: " + query);
            String queryString = result.toString();
            System.out.println("queryString = " + queryString);
            if (query.contains("!~")) {
                assertTrue(queryString.contains("\"must_not\""),
                        "Negative regex should use must_not for: " + query);
            }
            assertTrue(queryString.contains("\"wildcard\""),
                    "Query should use wildcard for: " + query);
        }
    }
    @Test
    void buildQuotation() {

        // 测试所有可能的字符串格式
        List<String> queries = List.of(
                "name == \"tan4\"",
                "name==123",
//                "name==tan1",                   // 不带引号
//                "name==abc123",                // 不带引号，含数字
//                "name==test_value",            // 不带引号，含下划线
//                "title==hello.world",          // 不带引号，含点号
                "name == \"quoted value\"",     // 双引号
                "name == 'single quoted'",      // 单引号
//                "name == tan2",
                "name==\"tan3\"",
                "name=='tan5'",
                "name == 'tan6'"
//                "email==user@example1.com",
//                "email == user@exampl2e.com"
        );
        // 创建查询构建器
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .build();

        for (String query : queries) {
            Query result = queryBuilder.buildQuery(query);
            System.out.println(result);
            assertTrue(result.toString().contains("\"value\":"),
                    "Query should contain value for: " + query);
        }
        // 测试数组查询
        Query arrayQuery = queryBuilder.buildQuery("tags in [\"tag1\", \"tag2\", 'tag3']");
        assertTrue(arrayQuery.toString().contains("\"terms\":"),
                "Array query should use terms query");
    }

    /**
     * 直接build
     */
    @Test
    void buildRun() {
        // 创建查询构建器
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .build();
        //  String expression = "title += \"论坚定理想信念（2023年）\" and years == 2021 ";
        String expression = "(name == \"tan\" and age >= 18 and (nick==\"s\" or zz==123)) OR (status == \"active\" and sex == 1 or title== \"sda\")";
//        System.out.println(query.toString());
        assertEquals("Query: {\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"term\":{\"name\":{\"value\":\"tan\"}}},{\"range\":{\"age\":{\"gte\":\"18\"}}}]}},{\"bool\":{\"should\":[{\"term\":{\"nick\":{\"value\":\"s\"}}},{\"term\":{\"zz\":{\"value\":\"123\"}}}]}}]}},{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"status\":{\"value\":\"active\"}}},{\"term\":{\"sex\":{\"value\":\"1\"}}}]}},{\"term\":{\"title\":{\"value\":\"sda\"}}}]}}]}}"
                ,queryBuilder.buildQuery(expression).toString());
    }


    /**
     * 通过正则验证值的正确性
     */
    @Test
    void buildRun2() {
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .build();
       assertEquals("Query: {\"bool\":{\"must\":[{\"match\":{\"title\":{\"query\":\"论坚定理想信念（2023年）\"}}},{\"term\":{\"years\":{\"value\":\"2021\"}}}]}}"
               , queryBuilder.buildQuery("title += \"论坚定理想信念（2023年）\" and years == 2021 ").toString());

    }

    /**
     * 替换 field
     */
    @Test
    void buildReplace() {
        // 创建查询构建器 - 设置字段转换 abc 转 phone  . name 转 username
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .withFieldTransformer(field -> switch (field) {
                    case "abc" -> "phone";
                    case "name" -> "userName";
                    default -> field;
                })
                .build();

        String expression = "(abc == \"13800138000\" and sex == \"男\") or (name =~ \".*张.*\")";
        assertEquals("Query: {\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"phone\":{\"value\":\"13800138000\"}}},{\"term\":{\"sex\":{\"value\":\"男\"}}}]}},{\"wildcard\":{\"userName\":{\"case_insensitive\":true,\"wildcard\":\".*张.*\"}}}]}}"
                ,queryBuilder.buildQuery(expression).toString());
    }


    /**
     * 值枚举验证
     */
    @Test
    void buildEnumValidator() {
        // 创建枚举值验证器
        var enumValidator = new EnumValueValidator();
        // 验证sex的值必须是 1，2
        enumValidator.registerAllowedValues("sex", Set.of("1", "2"));
        // 创建查询构建器 - 设置字段转换 abc 转 phone  . name 转 username
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .addValueValidator(enumValidator)
                .build();

        assertThrows(ElasticsearchException.class, () -> {
            queryBuilder.buildQuery("(abc == \"13800138000\" and sex == \"男\") or (name =~ \".*张.*\")");
        });

        assertEquals("Query: {\"bool\":{\"must\":[{\"term\":{\"abc\":{\"value\":\"13800138000\"}}},{\"term\":{\"sex\":{\"value\":\"1\"}}}]}}"
                ,queryBuilder.buildQuery("(abc == \"13800138000\" and sex == \"1\")").toString());
    }


    /**
     * 通过正则验证值的正确性
     */
    @Test
    void buildRegexValidator() {
        // 创建正则表达式验证器
        var regexValidator = new RegexValueValidator();
        // 需要验证的字段和正则方式
        regexValidator.registerPattern("phone", "^1[3-9]\\d{9}$");
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .addValueValidator(regexValidator)
                .build();

        assertThrows(ElasticsearchException.class, () -> {
            queryBuilder.buildQuery("(phone == \"1380000\" and sex == \"男\") or (name =~ \".*张.*\")");
        });

    }


    /**
     * full
     */
    @Test
    void buildFull() {
        // 创建枚举值验证器
        var enumValidator = new EnumValueValidator();
        enumValidator.registerAllowedValues("sex", Set.of("1", "2"));

        // 创建正则表达式验证器
        var regexValidator = new RegexValueValidator();
        regexValidator.registerPattern("phone", "^1[3-9]\\d{9}$");


        // 创建查询构建器
        var queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .withFieldTransformer(field -> switch (field) {
                    case "abc" -> "phone";
                    case "name" -> "userName";
                    default -> field;
                })
                .addValueValidator(enumValidator)
                .addValueValidator(regexValidator)
                .build();

        //  String expression = "title += \"论坚定理想信念（2023年）\" and years == 2021 ";
        String expression = "(name == \"tan\" and age >= 18 and (nick==\"s\" or zz==123)) OR (status == \"active\" and sex == 1 or title== \"sda\")";
        Query query = queryBuilder.buildQuery(expression);
//        System.out.println(query.toString());
        assertEquals("Query: {\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"term\":{\"userName\":{\"value\":\"tan\"}}},{\"range\":{\"age\":{\"gte\":\"18\"}}}]}},{\"bool\":{\"should\":[{\"term\":{\"nick\":{\"value\":\"s\"}}},{\"term\":{\"zz\":{\"value\":\"123\"}}}]}}]}},{\"bool\":{\"should\":[{\"bool\":{\"must\":[{\"term\":{\"status\":{\"value\":\"active\"}}},{\"term\":{\"sex\":{\"value\":\"1\"}}}]}},{\"term\":{\"title\":{\"value\":\"sda\"}}}]}}]}}"
                ,query.toString());

        // 测试 枚举验证
        String expressionRegex = "(abc == \"13800138000\" and sex == \"男\")";
        assertThrows(ElasticsearchException.class, () -> {
            queryBuilder.buildQuery(expressionRegex);
        });

        // 测试 正则验证手机号
        String expressionRegex2 = "(abc == \"13800138\" and sex == \"1\") or (name =~ \".*张.*\")";
        assertThrows(ElasticsearchException.class, () -> {
            queryBuilder.buildQuery(expressionRegex2);
        });
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
