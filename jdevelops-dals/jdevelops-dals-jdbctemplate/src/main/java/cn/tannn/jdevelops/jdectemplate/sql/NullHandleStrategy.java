package cn.tannn.jdevelops.jdectemplate.sql;

/**
 * SQL空值处理策略枚举
 */
public enum NullHandleStrategy {
    IGNORE,                         // 忽略空值
    NULL_AS_IS_NULL,               // 将null转换为IS NULL
    NULL_AS_IS_NOT_NULL,          // 将null转换为IS NOT NULL
    EMPTY_AS_IS_NULL,             // 将空字符串转换为IS NULL
    EMPTY_AS_IS_NOT_NULL,         // 将空字符串转换为IS NOT NULL
    NULL_AND_EMPTY_AS_IS_NULL,    // 将null和空字符串转换为IS NULL
    NULL_AND_EMPTY_AS_IS_NOT_NULL // 将null和空字符串转换为IS NOT NULL
}
