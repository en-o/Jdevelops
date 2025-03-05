package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.demo.ElasticSearchQueryBuilder;
import cn.tannn.jdevelops.es.antlr.tools.EnumValueValidator;
import cn.tannn.jdevelops.es.antlr.tools.RegexValueValidator;
import cn.tannn.jdevelops.es.exception.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 表达书测试用例
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/2/25 16:12
 */
@DisplayName("ES查询构建器测试")
public class ExpressionTest {
    private ElasticSearchQueryBuilder queryBuilder;
    private EnumValueValidator enumValidator;
    private RegexValueValidator regexValidator;

    @BeforeEach
    void setUp() {
        // 创建枚举值验证器
        enumValidator = new EnumValueValidator();
        enumValidator.registerAllowedValues("sex", Set.of("男", "女"));
        enumValidator.registerAllowedValues("status", Set.of("active", "inactive"));

        // 创建正则表达式验证器
        regexValidator = new RegexValueValidator();
        regexValidator.registerPattern("phone", "^1[3-9]\\d{9}$");
        regexValidator.registerPattern("email", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        // 创建查询构建器
        queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .withFieldTransformer(field -> switch (field) {
                    case "手机" -> "phone";
                    case "邮箱" -> "email";
                    case "姓名" -> "userName";
                    default -> field;
                })
                .addValueValidator(enumValidator)
                .addValueValidator(regexValidator)
                .build();
    }

    @Nested
    @DisplayName("基础比较操作符测试")
    class BasicOperatorsTest {

        @Test
        @DisplayName("等于操作符 ==")
        void testEqualOperator() {
            Query query = queryBuilder.buildQuery("userName == \"张三\"");
            assertEquals(
                    "Query: {\"term\":{\"userName\":{\"value\":\"张三\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("不等于操作符 !=")
        void testNotEqualOperator() {
            Query query = queryBuilder.buildQuery("age != \"25\"");
            assertEquals(
                    "Query: {\"bool\":{\"must_not\":[{\"term\":{\"age\":{\"value\":\"25\"}}}]}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("大于等于操作符 >=")
        void testGreaterThanOrEqualOperator() {
            Query query = queryBuilder.buildQuery("age >= \"18\"");
            assertEquals(
                    "Query: {\"range\":{\"age\":{\"gte\":\"18\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("小于等于操作符 <=")
        void testLessThanOrEqualOperator() {
            Query query = queryBuilder.buildQuery("age <= \"60\"");
            assertEquals(
                    "Query: {\"range\":{\"age\":{\"lte\":\"60\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("大于操作符 >")
        void testGreaterThanOperator() {
            Query query = queryBuilder.buildQuery("score > \"90\"");
            assertEquals(
                    "Query: {\"range\":{\"score\":{\"gt\":\"90\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("小于操作符 <")
        void testLessThanOperator() {
            Query query = queryBuilder.buildQuery("score < \"60\"");
            assertEquals(
                    "Query: {\"range\":{\"score\":{\"lt\":\"60\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("模糊匹配操作符 +=")
        void testLikeOperator() {
            Query query = queryBuilder.buildQuery("description += \"测试\"");
            assertEquals(
                    "Query: {\"match\":{\"description\":{\"query\":\"测试\"}}}",
                    query.toString()
            );
        }
    }

    @Nested
    @DisplayName("正则表达式操作符测试")
    class RegexOperatorsTest {

        @Test
        @DisplayName("正则匹配操作符 =~")
        void testRegexMatchOperator() {
            Query query = queryBuilder.buildQuery("emails =~ \".*@gmail\\.com$\"");
            assertEquals(
                    "Query: {\"regexp\":{\"emails\":{\"case_insensitive\":true,\"value\":\".*@gmail\\\\.com$\"}}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("正则不匹配操作符 !~")
        void testRegexNotMatchOperator() {
            Query query = queryBuilder.buildQuery("emails !~ \".*@spam\\.com$\"");
            assertEquals(
                    "Query: {\"bool\":{\"must_not\":[{\"regexp\":{\"emails\":{\"case_insensitive\":true,\"value\":\".*@spam\\\\.com$\"}}}]}}",
                    query.toString()
            );
        }
    }

    @Nested
    @DisplayName("存在性操作符测试")
    class ExistenceOperatorsTest {

        @Test
        @DisplayName("存在操作符 exists")
        void testExistsOperator() {
            Query query = queryBuilder.buildQuery("phone exists");
            assertEquals(
                    "Query: {\"exists\":{\"field\":\"phone\"}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("不存在操作符 not exists")
        void testNotExistsOperator() {
            Query query = queryBuilder.buildQuery("phone not exists");
            assertEquals(
                    "Query: {\"bool\":{\"must_not\":[{\"exists\":{\"field\":\"phone\"}}]}}",
                    query.toString()
            );
        }
    }

    @Nested
    @DisplayName("包含操作符测试")
    class ContainmentOperatorsTest {

        @Test
        @DisplayName("包含操作符 in")
        void testInOperator() {
            Query query = queryBuilder.buildQuery("status in [\"inactive\", \"active\"]");
            assertEquals(
                    "Query: {\"terms\":{\"status\":[\"inactive\",\"active\"]}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("不包含操作符 not in")
        void testNotInOperator() {

            assertThrows(ElasticsearchException.class, () -> {
                queryBuilder.buildQuery("status not in [\"deleted\", \"banned\"]");
            });
            assertEquals(
                    "Query: {\"bool\":{\"must_not\":[{\"terms\":{\"status\":[\"inactive\",\"active\"]}}]}}",
                    queryBuilder.buildQuery("status not in [\"inactive\", \"active\"]")
                            .toString()
            );
        }
    }

    @Nested
    @DisplayName("复合查询测试")
    class CompoundQueryTest {

        @Test
        @DisplayName("AND 复合查询")
        void testAndCompoundQuery() {
            Query query = queryBuilder.buildQuery(
                    "age >= \"18\" and sex == \"男\" and (status == \"active\" or score > \"90\")"
            );
            assertEquals(
                    "Query: {\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"range\":{\"age\":{\"gte\":\"18\"}}},{\"term\":{\"sex\":{\"value\":\"男\"}}}]}},{\"bool\":{\"should\":[{\"term\":{\"status\":{\"value\":\"active\"}}},{\"range\":{\"score\":{\"gt\":\"90\"}}}]}}]}}",
                    query.toString()
            );
        }

        @Test
        @DisplayName("OR 复合查询")
        void testOrCompoundQuery() {
            Query query = queryBuilder.buildQuery(
                    "status == \"active\" or (age >= \"18\" and sex == \"女\")"
            );
            assertEquals(
                    "Query: {\"bool\":{\"should\":[" +
                            "{\"term\":{\"status\":{\"value\":\"active\"}}}," +
                            "{\"bool\":{\"must\":[" +
                            "{\"range\":{\"age\":{\"gte\":\"18\"}}}," +
                            "{\"term\":{\"sex\":{\"value\":\"女\"}}}" +
                            "]}}" +
                            "]}}",
                    query.toString()
            );
        }
    }

    @Nested
    @DisplayName("字段转换测试")
    class FieldTransformationTest {

        @Test
        @DisplayName("字段名转换测试")
        void testFieldTransformation() {
            Query query = queryBuilder.buildQuery("手机==13800138000");
            assertEquals(
                    "Query: {\"term\":{\"phone\":{\"value\":\"13800138000\"}}}",
                    query.toString()
            );
        }
    }

    @Nested
    @DisplayName("值验证测试")
    class ValueValidationTest {

        @Test
        @DisplayName("枚举值验证测试 - 有效值")
        void testEnumValidation_ValidValue() {
            Query query = queryBuilder.buildQuery("sex == \"男\"");
            assertDoesNotThrow(() -> {
                assertEquals(
                        "Query: {\"term\":{\"sex\":{\"value\":\"男\"}}}",
                        query.toString()
                );
            });
        }

        @Test
        @DisplayName("枚举值验证测试 - 无效值")
        void testEnumValidation_InvalidValue() {
            assertThrows(ElasticsearchException.class, () ->
                    queryBuilder.buildQuery("sex == \"未知\"")
            );
        }

        @Test
        @DisplayName("正则表达式验证测试 - 有效值")
        void testRegexValidation_ValidValue() {
            Query query = queryBuilder.buildQuery("phone == \"13800138000\"");
            assertDoesNotThrow(() -> {
                assertEquals(
                        "Query: {\"term\":{\"phone\":{\"value\":\"13800138000\"}}}",
                        query.toString()
                );
            });
        }

        @Test
        @DisplayName("正则表达式验证测试 - 无效值")
        void testRegexValidation_InvalidValue() {
            assertThrows(ElasticsearchException.class, () ->
                    queryBuilder.buildQuery("phone == \"123\"")
            );
        }
    }

    @Test
    @DisplayName("综合查询测试")
    void testComplexQuery() {
        String complexQuery = """
                (手机 == "13800138000" and sex == "男" and status == "active") or
                (email == "123123@qq.com" and age > "18") or
                (userName += "张" and score >= "90" and status in ["active"])
                """;

        Query query = queryBuilder.buildQuery(complexQuery);
        assertNotNull(query);
        assertTrue(query.toString().contains("\"bool\""));
        // 具体的断言可以根据实际的查询结果来编写
    }
}
