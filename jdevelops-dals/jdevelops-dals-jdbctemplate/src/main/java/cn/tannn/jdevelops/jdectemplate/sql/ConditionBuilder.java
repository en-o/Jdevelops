package cn.tannn.jdevelops.jdectemplate.sql;

/**
 * SQL条件构建器接口
 */
public interface ConditionBuilder {
    void appendWhereOrAnd();
    void appendOrCondition();
}
