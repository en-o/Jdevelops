package cn.tannn.jdevelops.es.antlr.tools;

import cn.tannn.jdevelops.es.exception.ElasticsearchException;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举值验证器
 */
public final class EnumValueValidator implements ValueValidator {
    private final Map<String, Set<String>> allowedValues = new ConcurrentHashMap<>();

    /**
     * 注册字段允许的值
     * @param field 字段名
     * @param values 允许的值集合
     */
    public void registerAllowedValues(String field, Set<String> values) {
        allowedValues.put(field, Set.copyOf(values));
    }

    @Override
    public boolean validate(String field, String value) throws ElasticsearchException {
        var allowed = allowedValues.get(field);
        if (allowed == null) {
            return true; // 如果字段没有注册枚举值，则认为验证通过
        }

        // 检查是否是数组值（通过逗号分隔的字符串）
        if (value != null && value.contains(",")) {
            // 分割并验证每个值
            return validateArrayValues(field, value, allowed);
        }

        // 单个值的验证
        if (!allowed.contains(value)) {
            throw new ElasticsearchException(
                    "字段 '%s' 的值必须是以下之一: %s，当前值: %s"
                            .formatted(field, allowed, value)
            );
        }
        return true;
    }

    /**
     * 验证数组中的所有值
     * @param field 字段名
     * @param arrayValue 逗号分隔的值字符串
     * @param allowed 允许的值集合
     * @return true 如果所有值都验证通过
     * @throws ElasticsearchException 如果任何值验证失败
     */
    private boolean validateArrayValues(String field, String arrayValue, Set<String> allowed)
            throws ElasticsearchException {
        // 分割字符串，去除空白字符
        String[] values = Arrays.stream(arrayValue.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        // 检查每个值
        for (String value : values) {
            if (!allowed.contains(value)) {
                throw new ElasticsearchException(
                        "字段 '%s' 的数组值中包含无效值 '%s'，允许的值为: %s"
                                .formatted(field, value, allowed)
                );
            }
        }
        return true;
    }
}
