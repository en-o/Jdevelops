package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接管理器
 * 统一管理数据库连接的创建、配置和关闭
 */
public class DatabaseConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private DatabaseConnectionManager() {
    }

    /**
     * 创建数据库连接配置
     *
     * @param username 用户名
     * @param password 密码
     * @param connectionTimeout 连接超时时间（毫秒）
     * @param socketTimeout socket超时时间（毫秒）
     * @param dbType 数据库类型
     * @return 连接属性配置
     */
    public static Properties createConnectionProperties(String username, String password,
                                                      int connectionTimeout, int socketTimeout,
                                                      DbType dbType) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("connectTimeout", String.valueOf(connectionTimeout));
        props.setProperty("socketTimeout", String.valueOf(socketTimeout));

        // 数据库特定配置
        switch (dbType) {
            case MYSQL:
                props.setProperty("characterEncoding", "UTF-8");
                props.setProperty("useUnicode", "true");
                props.setProperty("useSSL", "false");
                props.setProperty("disconnectOnExpiredPasswords", "false");
                break;
            case POSTGRE_SQL:
            case KINGBASE8:
                // PostgreSQL和KingBase8可以添加特定配置
                break;
            default:
                // 其他数据库类型的默认配置
                break;
        }

        return props;
    }

    /**
     * 创建标准数据库连接
     *
     * @param url 数据库连接URL
     * @param username 用户名
     * @param password 密码
     * @param driverClassName 驱动类名
     * @return 数据库连接
     * @throws SQLException 如果连接创建失败
     * @throws ClassNotFoundException 如果驱动类找不到
     */
    public static Connection createConnection(String url, String username, String password,
                                            String driverClassName) throws SQLException, ClassNotFoundException {
        return createConnection(url, username, password, driverClassName, 5000, 5000, null);
    }

    /**
     * 创建数据库连接（带超时配置）
     *
     * @param url 数据库连接URL
     * @param username 用户名
     * @param password 密码
     * @param driverClassName 驱动类名
     * @param connectionTimeout 连接超时时间（毫秒）
     * @param socketTimeout socket超时时间（毫秒）
     * @param dbType 数据库类型
     * @return 数据库连接
     * @throws SQLException 如果连接创建失败
     * @throws ClassNotFoundException 如果驱动类找不到
     */
    public static Connection createConnection(String url, String username, String password,
                                            String driverClassName, int connectionTimeout,
                                            int socketTimeout, DbType dbType)
                                            throws SQLException, ClassNotFoundException {
        // 加载数据库驱动
        if (driverClassName != null) {
            Class.forName(driverClassName);
        }

        // 创建连接属性
        Properties props = createConnectionProperties(username, password, connectionTimeout, socketTimeout, dbType);

        // 处理MySQL过期密码的特殊URL配置
        String connectionUrl = url;
        if (dbType == DbType.MYSQL && !url.contains("disconnectOnExpiredPasswords")) {
            connectionUrl = url + (url.contains("?") ? "&" : "?") + "disconnectOnExpiredPasswords=false";
        }

        log.debug("[renewpwd] 创建数据库连接: url={}, username={}", connectionUrl, username);
        return DriverManager.getConnection(connectionUrl, props);
    }

    /**
     * 安全关闭数据库连接
     *
     * @param connection 要关闭的连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                log.debug("[renewpwd] 数据库连接已关闭");
            } catch (SQLException e) {
                log.debug("[renewpwd] 关闭数据库连接失败", e);
            }
        }
    }

    /**
     * 验证数据库连接有效性
     *
     * @param connection 数据库连接
     * @param timeout 验证超时时间（秒）
     * @return true 如果连接有效
     */
    public static boolean isConnectionValid(Connection connection, int timeout) {
        if (connection == null) {
            return false;
        }

        try {
            return connection.isValid(timeout);
        } catch (SQLException e) {
            log.debug("[renewpwd] 连接有效性验证失败", e);
            return false;
        }
    }
}
