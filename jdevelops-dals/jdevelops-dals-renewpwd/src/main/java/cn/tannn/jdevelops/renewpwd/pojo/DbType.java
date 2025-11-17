package cn.tannn.jdevelops.renewpwd.pojo;


import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/19 09:46
 */
public enum DbType {

    MYSQL,

    ORACLE,

    KINGBASE8,

    POSTGRE_SQL;

    // 数据库标识符映射表
    private static final Map<String, DbType> DB_IDENTIFIER_MAP = new HashMap<>();

    static {
        DB_IDENTIFIER_MAP.put(":mysql:", DbType.MYSQL);
        DB_IDENTIFIER_MAP.put(":mariadb:", DbType.MYSQL); // MariaDB兼容MySQL
        DB_IDENTIFIER_MAP.put(":oracle:", DbType.ORACLE);
        DB_IDENTIFIER_MAP.put(":kingbase8:", DbType.KINGBASE8);
        DB_IDENTIFIER_MAP.put(":kingbase:", DbType.KINGBASE8); // 兼容不同版本
        DB_IDENTIFIER_MAP.put(":postgresql:", DbType.POSTGRE_SQL);
        // 注释的表示还没进行支持
//        DB_IDENTIFIER_MAP.put(":h2:", DbType.H2);
//        DB_IDENTIFIER_MAP.put(":sqlite:", DbType.SQLITE);
//        DB_IDENTIFIER_MAP.put(":sqlserver:", DbType.SQL_SERVER);
//        DB_IDENTIFIER_MAP.put(":jtds:sqlserver:", DbType.SQL_SERVER); // JTDS驱动
    }

    /**
     * 根据连接URL获取数据库类型
     *
     * @param connectionUrl 数据库连接URL
     * @return DbType 数据库类型，如果URL为null或不支持则返回null
     */
    public static DbType getDbType(String connectionUrl) {
        // 参数校验
        if (connectionUrl == null || connectionUrl.trim().isEmpty()) {
            return null;
        }

        // 转为小写进行匹配
        String lowerUrl = connectionUrl.toLowerCase().trim();

        // 遍历映射表进行匹配
        for (Map.Entry<String, DbType> entry : DB_IDENTIFIER_MAP.entrySet()) {
            if (lowerUrl.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        // 不支持的数据库类型
        throw new IllegalArgumentException("不支持的数据库类型: " + connectionUrl);
    }


    /**
     * 判断连接URL是否为指定的数据库类型
     * @param connectionUrl 数据库连接URL
     * @param targetDbType 目标数据库类型
     * @return boolean 是否为指定的数据库类型
     */
    public static boolean isSpecificDatabase(String connectionUrl, DbType targetDbType) {
        if (connectionUrl == null || targetDbType == null) {
            return false;
        }
        DbType actualDbType = getDbType(connectionUrl);
        return targetDbType.equals(actualDbType);
    }

    /**
     * 判断是否为MySQL数据库
     * @param connectionUrl 数据库连接URL
     * @return boolean 是否为MySQL数据库
     */
    public static boolean isMysql(String connectionUrl) {
        return isSpecificDatabase(connectionUrl, DbType.MYSQL);
    }
}
