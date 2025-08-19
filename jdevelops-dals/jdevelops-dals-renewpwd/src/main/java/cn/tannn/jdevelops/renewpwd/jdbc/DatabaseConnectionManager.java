package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                                              String driverClassName, DbType dbType) throws SQLException, ClassNotFoundException {
        return createConnection(url, username, password, driverClassName, 5000, 5000, dbType);
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
     * 针对不同数据库类型采用不同的验证策略
     *
     * @param connection 数据库连接
     * @param timeout 验证超时时间（秒）
     * @param dbType 数据库类型
     * @return true 如果连接有效且账号可用
     * @throws SQLException 如果查询执行失败 - 给上面自动错误处理
     */
    public static boolean isConnectionValid(Connection connection, int timeout, DbType dbType) throws SQLException {
        if (connection == null) {
            return false;
        }

        try {
            // 首先检查基本连接有效性
            if (!connection.isValid(timeout)) {
                return false;
            }

            // 对于MySQL和Oracle，需要额外验证账号是否过期
            if (dbType == DbType.MYSQL || dbType == DbType.ORACLE) {
                return validateAccountStatus(connection, dbType);
            }

            // 对于其他数据库，基本连接验证就足够了
            return true;

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            log.debug("[renewpwd] 连接有效性验证失败", e);
            return false;
        }
    }

    /**
     * 验证账号状态（主要针对MySQL和Oracle）
     * 通过执行实际查询来验证账号是否可用
     *
     * @param connection 数据库连接
     * @param dbType 数据库类型
     * @return true 如果账号状态正常
     * @throws SQLException 如果查询执行失败 - 给上面自动错误处理
     */
    private static boolean validateAccountStatus(Connection connection, DbType dbType) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(5); // 设置查询超时为5秒

            String testQuery = getTestQuery(dbType);
            log.debug("[renewpwd] 执行账号状态验证查询: {}", testQuery);

            resultSet = statement.executeQuery(testQuery);

            // 如果能成功执行查询并获取结果，说明账号状态正常
            boolean hasResult = resultSet.next();
            log.debug("[renewpwd] 账号状态验证结果: {}", hasResult);
            return hasResult;

        } catch (SQLException e) {
            // 检查是否是账号过期相关的错误
            if (isAccountExpiredError(e, dbType)) {
                log.warn("[renewpwd] 检测到账号过期: {}", e.getMessage());

            }

            // 其他SQL异常也视为验证失败
            log.debug("[renewpwd] 账号状态验证SQL异常: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.debug("[renewpwd] 账号状态验证异常", e);
            return false;

        } finally {
            // 安全关闭资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.debug("[renewpwd] 关闭ResultSet失败", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.debug("[renewpwd] 关闭Statement失败", e);
                }
            }
        }
    }

    /**
     * 获取不同数据库类型的测试查询
     *
     * @param dbType 数据库类型
     * @return 测试查询SQL
     */
    private static String getTestQuery(DbType dbType) {
        switch (dbType) {
            case MYSQL:
                return "SELECT 1";
            case ORACLE:
                return "SELECT 1 FROM DUAL";
            case POSTGRE_SQL:
            case KINGBASE8:
                return "SELECT 1";
            default:
                return "SELECT 1";
        }
    }

    /**
     * 判断SQL异常是否为账号过期错误
     *
     * @param e SQL异常
     * @param dbType 数据库类型
     * @return true 如果是账号过期错误
     */
    private static boolean isAccountExpiredError(SQLException e, DbType dbType) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            return false;
        }

        errorMessage = errorMessage.toLowerCase();

        switch (dbType) {
            case MYSQL:
                // MySQL账号过期的常见错误信息
                return errorMessage.contains("password expired") ||
                        errorMessage.contains("password has expired") ||
                        errorMessage.contains("password must be reset") ||
                        e.getErrorCode() == 1820; // MySQL账号过期错误码

            case ORACLE:
                // Oracle账号过期的常见错误信息
                return errorMessage.contains("password expired") ||
                        errorMessage.contains("account locked") ||
                        errorMessage.contains("account expired") ||
                        e.getErrorCode() == 28001 || // 账号过期
                        e.getErrorCode() == 28000;   // 账号锁定

            default:
                // 其他数据库的通用过期关键词
                return errorMessage.contains("expired") ||
                        errorMessage.contains("password") && errorMessage.contains("reset");
        }
    }

}
