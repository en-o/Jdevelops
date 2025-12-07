package cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums;

/**
 * SQL模式枚举
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/1 18:03
 */
public enum ParameterMode {
    /**
     * 位置参数模式 (?)
     */
    POSITIONAL,
    /**
     * 命名参数模式 (:paramName)
     */
    NAMED
}
