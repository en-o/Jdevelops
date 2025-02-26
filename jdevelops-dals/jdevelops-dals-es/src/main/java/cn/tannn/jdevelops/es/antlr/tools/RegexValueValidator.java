package cn.tannn.jdevelops.es.antlr.tools;

import cn.tannn.jdevelops.es.exception.ElasticsearchException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 正则表达式值验证器
 */
public final class RegexValueValidator implements ValueValidator {
    private final Map<String, Pattern> patterns = new ConcurrentHashMap<>();

    /**
     * 注册字段值的正则表达式模式
     * @param field 字段名
     * @param pattern 正则表达式模式
     */
    public void registerPattern(String field, String pattern) {
        patterns.put(field, Pattern.compile(pattern));
    }

    @Override
    public boolean validate(String field, String value) throws ElasticsearchException {
        var pattern = patterns.get(field);
        if (pattern != null && !pattern.matcher(value).matches()) {
            throw new ElasticsearchException("字段 '%s' 的值不符合要求的格式".formatted(field));
        }
        return true;
    }
}
