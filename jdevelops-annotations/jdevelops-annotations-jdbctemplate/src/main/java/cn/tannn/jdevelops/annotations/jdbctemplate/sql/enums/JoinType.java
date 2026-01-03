package cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums;

/**
 * 连接类型枚举
 */
public enum JoinType {
    INNER("INNER JOIN"),
    LEFT("LEFT JOIN"),
    RIGHT("RIGHT JOIN"),
    FULL("FULL JOIN");

    private final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
