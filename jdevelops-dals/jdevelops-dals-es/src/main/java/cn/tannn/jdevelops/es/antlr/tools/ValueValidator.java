package cn.tannn.jdevelops.es.antlr.tools;

/**
 * 值验证接口
 */
public sealed interface ValueValidator permits EnumValueValidator, RegexValueValidator {
    /**
     * 验证字段值是否有效
     * @param field 字段名
     * @param value 字段值
     * @return 验证通过返回true，否则返回false
     */
    boolean validate(String field, String value) ;
}
