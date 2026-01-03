package cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums;

/**
 * 查询类型枚举
 */
public enum QueryType {
    EQ,           // 等于
    LIKE,         // 模糊查询
    LEFT_LIKE,    // 左模糊
    RIGHT_LIKE,   // 右模糊
    IN,           // IN 查询
    BETWEEN,      // 范围查询
    IS_NULL,      // 空值查询
    IS_NOT_NULL,  // 非空查询
    GT,           // 大于
    GE,           // 大于等于
    LT,           // 小于
    LE,           // 小于等于
    NE,           // 不等于
    CUSTOM        // 自定义操作符
}
