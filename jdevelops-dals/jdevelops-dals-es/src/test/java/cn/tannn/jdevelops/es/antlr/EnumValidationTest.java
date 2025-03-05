package cn.tannn.jdevelops.es.antlr;

import cn.tannn.jdevelops.es.antlr.demo.ElasticSearchQueryBuilder;
import cn.tannn.jdevelops.es.antlr.tools.EnumValueValidator;
import cn.tannn.jdevelops.es.exception.ElasticsearchException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 枚举校验
 *
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2025/2/25 22:47
 */
public class EnumValidationTest {
    @Test
    void testEnumValidation() {
        EnumValueValidator validator = new EnumValueValidator();
        validator.registerAllowedValues("status", Set.of("active", "inactive"));

        // 测试单个值
        assertTrue(validator.validate("status", "active"));
        assertTrue(validator.validate("status", "inactive"));

        // 测试数组值
        assertTrue(validator.validate("status", "active,inactive"));

        // 测试无效值
        assertThrows(ElasticsearchException.class, () ->
                validator.validate("status", "invalid")
        );

        // 测试数组中的无效值
        assertThrows(ElasticsearchException.class, () ->
                validator.validate("status", "active,invalid,inactive")
        );
    }

    @Test
    void testQueryWithEnumValidation() {
        // 设置查询构建器
        EnumValueValidator enumValidator = new EnumValueValidator();
        enumValidator.registerAllowedValues("status", Set.of("active", "inactive"));

        ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder()
                .addValueValidator(enumValidator)
                .build();

        // 测试有效的单值查询
        assertDoesNotThrow(() ->
                queryBuilder.buildQuery("status == \"active\"")
        );

        // 测试有效的数组查询
        assertDoesNotThrow(() ->
                queryBuilder.buildQuery("status in [\"inactive\", \"active\"]")
        );

        // 测试无效值
        assertThrows(ElasticsearchException.class, () ->
                queryBuilder.buildQuery("status == \"invalid\"")
        );

        // 测试无效的数组值
        assertThrows(ElasticsearchException.class, () ->
                queryBuilder.buildQuery("status in [\"active\", \"invalid\"]")
        );
    }
}
