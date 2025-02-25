package cn.tannn.jdevelops.es.antlr.tools;

import cn.tannn.jdevelops.es.exception.ElasticsearchException;

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
        if (allowed != null && !allowed.contains(value)) {
            throw new ElasticsearchException("字段 '%s' 的值必须是以下之一: %s".formatted(field, allowed));
        }
        return true;
    }
}
