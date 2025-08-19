package cn.tannn.jdevelops.renewpwd.pojo;

import org.springframework.core.env.ConfigurableEnvironment;

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


    /**
     * 根据驱动类名获取数据库类型
     * @param driverClassName 驱动类名
     * @return DbType
     */
    public static DbType getDbType(String driverClassName) {
        if (driverClassName == null) {
            return null;
        }
        String lower = driverClassName.toLowerCase();
        if (lower.contains("mysql")) {
            return DbType.MYSQL;
        } else if (lower.contains("oracle")) {
            return DbType.ORACLE;
        } else if (lower.contains("kingbase8")) {
            return DbType.KINGBASE8;
        } else if (lower.contains("postgresql")) {
            return DbType.POSTGRE_SQL;
        } else {
            throw new RuntimeException("不支持的数据库驱动: " + driverClassName);
        }

    }

}
