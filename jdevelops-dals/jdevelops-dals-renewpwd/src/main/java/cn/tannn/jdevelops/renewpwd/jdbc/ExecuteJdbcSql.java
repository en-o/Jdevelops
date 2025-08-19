package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.RootAccess;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.DatabaseUtils;
import cn.tannn.jdevelops.renewpwd.util.PasswordUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.text.MessageFormat;

/**
 * sql 操作
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/10 01:33
 */
public class ExecuteJdbcSql {


    private static PasswordUpdateListener passwordUpdateListener;

    private static final Logger log = LoggerFactory.getLogger(ExecuteJdbcSql.class);


    public static void setPasswordUpdateListener(PasswordUpdateListener listener) {
        passwordUpdateListener = listener;
    }

    /**
     * 验证数据源配置是否有效
     *
     * @param environment     ConfigurableEnvironment
     * @param currentPassword 当前密码
     * @param backupPassword  密码已过期 (1862) 错误时，将数据库密码修改为备份密码
     */
    public static boolean validateDatasourceConfig(ConfigurableEnvironment environment
            , String currentPassword, String backupPassword) {
        try {

            // 如果备份密码为空，则使用当前密码
            backupPassword = backupPassword.isEmpty() ? currentPassword : backupPassword;

            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            log.debug("[renewpwd] 数据源配置: url={}, username={}, password={}",
                    url, username, currentPassword);

            if (url == null || username == null || currentPassword == null) {
                log.warn("[renewpwd] 数据源配置不完整: url={}, username={}, password={}",
                        url, username, currentPassword != null ? "***" : "null");
                return false;
            }
            // 创建临时连接测试
            return testDatabaseConnection(url, username, driverClassName, currentPassword, backupPassword);
        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常", e);
            return false;
        }
    }


    /**
     * 测试数据库连接是否有效
     *
     * @param url             数据库连接URL
     * @param username        数据库用户名
     * @param driverClassName 数据库驱动类名
     * @param currentPassword 当前密码
     * @param backupPassword  密码已过期 (1862) 错误时，将数据库密码修改为备份密码
     * @return true 如果连接有效或密码已成功更新，false 如果连接无效或更新失败
     */
    public static boolean testDatabaseConnection(String url
            , String username
            , String driverClassName
            , String currentPassword
            , String backupPassword
    ) {
        java.sql.Connection connection = null;
        try {
            // 加载数据库驱动
            if (driverClassName != null) {
                Class.forName(driverClassName);
            }

            log.info("[renewpwd] 正在验证数据库连接: url={}, username={}", url, username);

            // 设置连接超时时间
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", username);
            props.setProperty("password", currentPassword);
            props.setProperty("connectTimeout", "5000"); // 5秒连接超时
            props.setProperty("socketTimeout", "5000");   // 5秒socket超时

            connection = java.sql.DriverManager.getConnection(url, props);

            // 测试连接有效性
            if (connection.isValid(3)) {
                log.info("[renewpwd] 数据库连接验证成功");
                return true;
            } else {
                log.warn("[renewpwd] 数据库连接无效");
                return false;
            }

        } catch (java.sql.SQLException e) {
            // 当前项目目前只支持mysql
            int vendorCode = e.getErrorCode();
            // SELECT * FROM performance_schema.events_errors_summary_global_by_error  where error_number = 'vendorCode'
            if (vendorCode == 1045) {
                log.error("[renewpwd] 数据库连接验证失败，可能是用户名或密码错误: {}", e.getMessage());
                return false;
            } else if (DatabaseUtils.isPasswordExpiredError(vendorCode, driverClassName)) {
                log.error("[renewpwd] 数据库连接验证失败，密码已过期必须更改密码才能登录: {}, 尝试更新密码", e.getMessage());
                // 我这里就不替换里面了，用原来的密码再改一次
                return updateUserPassword(url, username, currentPassword, backupPassword, driverClassName);
            } else {
                log.error("[renewpwd] 数据库连接验证失败: {}", e.getMessage());
            }
            return false;
        } catch (ClassNotFoundException e) {
            log.error("[renewpwd] 数据库驱动类未找到: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[renewpwd] 数据库连接验证异常", e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException e) {
                    log.debug("[renewpwd] 关闭测试连接失败", e);
                }
            }
        }
    }


    /**
     * 更新用户密码
     *
     * @param environment        ConfigurableEnvironment
     * @param renewpwdProperties 配置文件
     * @return 新密码 ，null=更新失败
     */
    public static String updateUserPassword(ConfigurableEnvironment environment
            , RenewpwdProperties renewpwdProperties
            , DbType dbType) {
        try {

            String url = environment.getProperty("spring.datasource.url");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            // 获取当前密码
            String springDatasourcePassword = environment.getProperty(RenewpwdConstant.DATASOURCE_PASSWORD_KEY
                    , RenewpwdConstant.DEFAULT_PASSWORD);

            log.error("[renewpwd] 密码已过期必须更改密码才能登录, 尝试更新密码");


            // 获取备用密码和主密码
            String backPassword = renewpwdProperties.getBackupPasswordDecrypt();
            String masterPassword = renewpwdProperties.getMasterPasswordDecrypt();

            String username;
            String connectionPassword;
            // 当前密码如果等于主密码，则使用备用密码作为新密码，否则使用主密码
            String newPassword = springDatasourcePassword.equals(masterPassword) ? backPassword : masterPassword;

            // 处理不同数据库
            if (dbType.equals(DbType.MYSQL)) {
                username = environment.getProperty("spring.datasource.username");
                connectionPassword = springDatasourcePassword;

            } else if (dbType.equals(DbType.POSTGRE_SQL) || dbType.equals(DbType.KINGBASE8)) {
                // 对于pgsql和kingbase8，使用root账户来更新密码
                RootAccess root = renewpwdProperties.getRoot();
                if (root == null) {
                    log.error("[renewpwd] pgsql/KINGBASE8 必须配置root超级账户");
                    return null;
                }
                username = root.getUsername();
                connectionPassword = root.getPassword();
                if (username == null || username.isEmpty() || connectionPassword == null || connectionPassword.isEmpty()) {
                    log.error("[renewpwd] pgsql/KINGBASE8 root账户密码不能为空");
                    return null;
                }
            } else {
                log.error("[renewpwd] 不支持过期密码更新的数据库类型: {}", dbType);
                return null;
            }

            log.debug("[renewpwd] 更新密码 - 连接数据源: url={}, username={}, password={}",
                    url, username, connectionPassword);

            if (url == null || username == null || newPassword == null) {
                log.warn("[renewpwd]  更新密码 - 数据源配置不完整: url={}, username={}, password={}",
                        url, username, "***");
                return null;
            }
            // 尝试解密密码
            connectionPassword = AESUtil.decryptPassword(connectionPassword, renewpwdProperties.getPwdEncryptKey());

            // 如果备份密码为空，则使用master密码
            newPassword = newPassword.isEmpty() ? masterPassword : newPassword;

            // 验证当前密码和备用密码的有效性
            if (!updateUserPassword(url, username, connectionPassword, newPassword, driverClassName)) {
                log.error("[renewpwd] 用户密码更新验证失败");
                return null;
            }
            return newPassword;
        } catch (Exception e) {
            log.error("[renewpwd]  更新密码 - 验证数据源配置时发生异常", e);
            return null;
        }


    }


    /**
     * 更新用户密码 - 强制更新
     *
     * @param environment ConfigurableEnvironment
     * @param newPassword 新密码
     */
    public static boolean updateUserPasswordForce(ConfigurableEnvironment environment
            , String newPassword) {
        java.sql.Connection connection = null;
        java.sql.Statement statement = null;
        try {
            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            log.debug("[renewpwd] 强制更新密码 - 数据源配置: url={}, username={}, password={}",
                    url, username, newPassword);

            if (url == null || username == null || newPassword == null) {
                log.warn("[renewpwd]  强制更新密码 - 数据源配置不完整: url={}, username={}, password={}",
                        url, username, newPassword != null ? "***" : "null");
                return false;
            }

            log.info("[renewpwd] 开始更新强制更新密码: username={}", username);

            // 配置连接属性以支持过期密码
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", username);
            props.setProperty("password", newPassword);
            props.setProperty("connectTimeout", "5000");
            props.setProperty("socketTimeout", "5000");

            // 关键配置：允许使用过期密码连接
            props.setProperty("disconnectOnExpiredPasswords", "false");
            // 或者在URL中添加参数也可以
            String connectionUrl = url;
            if (!url.contains("disconnectOnExpiredPasswords")) {
                connectionUrl = url + (url.contains("?") ? "&" : "?") +
                        "disconnectOnExpiredPasswords=false";
            }

            log.info("[renewpwd] 尝试使用新密码连接数据库");
            connection = java.sql.DriverManager.getConnection(connectionUrl, props);

            // 检查连接是否处于受限模式（只能执行密码更改）
            log.info("[renewpwd] 连接成功，开始强制更新密码流程");

            statement = connection.createStatement();

            // 如果SET PASSWORD失败，尝试ALTER USER
            // 首先获取当前用户信息
            String currentUserHost = getCurrentUserHostForExpiredPassword(statement, username);
            if (newPassword.isEmpty()) {
                log.error("[renewpwd] 新密码不能为空");
                return false;
            }
            String alterUserSQL = String.format("ALTER USER '%s'@'%s' IDENTIFIED BY '%s'",
                    username, currentUserHost, newPassword);

            log.debug("[renewpwd] 执行SQL: ALTER USER '{}' @'{}' IDENTIFIED BY '***'", username, currentUserHost);
            statement.executeUpdate(alterUserSQL);
            log.info("[renewpwd] 使用ALTER USER强制更新密码成功: username={}@{}", username, currentUserHost);

            return true;

        } catch (Exception e) {
            log.error("[renewpwd]  强制更新密码 - 验证数据源配置时发生异常", e);
            return false;
        }
    }


    /**
     * 更新用户密码
     * 当检测到1862错误码时，执行ALTER USER命令更改密码
     *
     * @param url             数据库连接URL
     * @param username        数据库用户名
     * @param currentPassword 旧密码 用于连接验证
     * @param newPassword     新密码 用于更新
     * @param driverClassName 数据库驱动类名
     * @return true 如果密码更新成功并且新密码验证通过，false 如果更新失败或验证失败
     */
    private static boolean updateUserPassword(String url
            , String username
            , String currentPassword
            , String newPassword
            , String driverClassName) {
        java.sql.Connection connection = null;
        java.sql.Statement statement = null;
        java.sql.ResultSet resultSet = null;

        try {
            log.info("[renewpwd] 开始更新过期密码: username={}", username);


            // 配置连接属性以支持过期密码
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", username);
            props.setProperty("password", currentPassword);
            props.setProperty("connectTimeout", "5000");
            props.setProperty("socketTimeout", "5000");

            // 关键配置：允许使用过期密码连接
            props.setProperty("disconnectOnExpiredPasswords", "false");
            // 或者在URL中添加参数也可以
            String connectionUrl = url;
            if (!url.contains("disconnectOnExpiredPasswords")) {
                connectionUrl = url + (url.contains("?") ? "&" : "?") +
                        "disconnectOnExpiredPasswords=false";
            }

            log.info("[renewpwd] 尝试使用过期密码连接数据库");
            connection = java.sql.DriverManager.getConnection(connectionUrl, props);

            // 检查连接是否处于受限模式（只能执行密码更改）
            log.info("[renewpwd] 连接成功，开始密码更新流程");

            statement = connection.createStatement();

            // 在过期密码模式下，优先尝试SET PASSWORD语句（更简单）
            String setPasswordSQL = String.format("SET PASSWORD = PASSWORD('%s')", newPassword);

            try {
                log.debug("[renewpwd] 尝试执行: SET PASSWORD = PASSWORD('***')");
                statement.executeUpdate(setPasswordSQL);
                log.info("[renewpwd] 使用SET PASSWORD更新密码成功");
            } catch (java.sql.SQLException e) {
                log.warn("[renewpwd] SET PASSWORD失败，尝试ALTER USER方式: {}", e.getMessage());

                // 如果SET PASSWORD失败，尝试ALTER USER
                // 首先获取当前用户信息
                String currentUserHost = getCurrentUserHostForExpiredPassword(statement, username);
                if (newPassword == null || newPassword.isEmpty()) {
                    log.error("[renewpwd] 新密码不能为空");
                    return false;
                }
                String alterUserSQL = String.format("ALTER USER '%s'@'%s' IDENTIFIED BY '%s'",
                        username, currentUserHost, newPassword);

                log.debug("[renewpwd] 执行SQL: ALTER USER '{}' @'{}' IDENTIFIED BY '***'", username, currentUserHost);
                statement.executeUpdate(alterUserSQL);
                log.info("[renewpwd] 使用ALTER USER更新密码成功: username={}@{}", username, currentUserHost);
            }

            // 关闭当前连接，准备用新密码测试
            statement.close();
            connection.close();
            connection = null;
            statement = null;

            // 验证新密码是否生效
            log.info("[renewpwd] 开始验证新密码");
            if (testNewPassword(url, username, newPassword, driverClassName)) {
                log.info("[renewpwd] 新密码验证成功，过期密码已更新");
                if (passwordUpdateListener != null) {
                    passwordUpdateListener.onPasswordUpdated(newPassword);
                }
                return true;
            } else {
                log.error("[renewpwd] 新密码验证失败");
                return false;
            }

        } catch (java.sql.SQLException e) {
            log.error("[renewpwd] 更新用户密码失败: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[renewpwd] 更新用户密码异常", e);
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (java.sql.SQLException e) {
                log.debug("[renewpwd] 关闭连接失败", e);
            }
        }
    }


    /**
     * 获取过期密码情况下的当前用户主机信息
     * 专门处理密码过期时的用户信息查询
     */
    private static String getCurrentUserHostForExpiredPassword(java.sql.Statement statement, String username) {
        java.sql.ResultSet resultSet = null;
        try {
            // 在过期密码模式下，某些查询可能受限，先尝试简单的查询
            try {
                resultSet = statement.executeQuery("SELECT CURRENT_USER()");

                if (resultSet.next()) {
                    String currentUser = resultSet.getString(1);
                    if (currentUser != null && currentUser.contains("@")) {
                        String host = currentUser.split("@")[1];
                        log.debug("[renewpwd] 在过期密码模式下获取到用户主机: {}", host);
                        return host;
                    }
                }
            } catch (java.sql.SQLException e) {
                log.warn("[renewpwd] 过期密码模式下无法执行CURRENT_USER(): {}", e.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (java.sql.SQLException e) {
                        // ignore
                    }
                }
            }

            // 如果上面的方法失败，尝试USER()函数
            try {
                resultSet = statement.executeQuery("SELECT USER()");
                if (resultSet.next()) {
                    String user = resultSet.getString(1);
                    if (user != null && user.contains("@")) {
                        // USER() 返回的是连接用户信息，取主机部分
                        String connectHost = user.split("@")[1];
                        log.debug("[renewpwd] 从USER()获取连接主机: {}", connectHost);
                        // 对于过期密码情况，通常使用 % 通配符是最安全的选择
                        // 因为无法查询mysql.user表来确定精确匹配
                        return "%";
                    }
                }
            } catch (java.sql.SQLException e) {
                log.warn("[renewpwd] 过期密码模式下无法执行USER(): {}", e.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (java.sql.SQLException e) {
                        // ignore
                    }
                }
            }

        } catch (Exception e) {
            log.error("[renewpwd] 获取过期密码用户主机信息异常", e);
        }

        // 默认返回通配符，这在大多数情况下都能工作
        log.info("[renewpwd] 无法确定具体主机，在过期密码模式下使用通配符 %");
        return "%";
    }

    /**
     * 从mysql.user表查询用户主机信息（备用方法）
     */
    private static String getUserHostFromTable(java.sql.Statement statement) {
        java.sql.ResultSet resultSet = null;
        try {
            // 查询当前连接的用户信息
            resultSet = statement.executeQuery("SELECT USER()");
            if (resultSet.next()) {
                String user = resultSet.getString(1);
                if (user != null && user.contains("@")) {
                    String username = user.split("@")[0];
                    String clientHost = user.split("@")[1];

                    // 从mysql.user表查询匹配的Host
                    resultSet.close();
                    resultSet = statement.executeQuery(
                            MessageFormat.format("SELECT Host FROM mysql.user WHERE User = ''{0}'' ORDER BY Host", username)
                    );

                    // 优先匹配具体的主机，然后是通配符
                    String specificHost = null;
                    String wildcardHost = null;

                    while (resultSet.next()) {
                        String host = resultSet.getString("Host");
                        if (host.equals(clientHost)) {
                            // 找到精确匹配
                            return host;
                        } else if (host.equals("%")) {
                            wildcardHost = host;
                        } else if (host.contains("%") && clientHost.matches(host.replace("%", ".*"))) {
                            specificHost = host;
                        }
                    }

                    // 返回最匹配的主机
                    return specificHost != null ? specificHost : wildcardHost;
                }
            }
        } catch (java.sql.SQLException e) {
            log.error("[renewpwd] 从mysql.user表查询主机信息失败: {}", e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (java.sql.SQLException e) {
                    log.debug("[renewpwd] 关闭ResultSet失败", e);
                }
            }
        }

        // 如果都失败了，返回通配符作为最后的尝试
        log.warn("[renewpwd] 无法确定具体主机，使用通配符 %");
        return "%";
    }

    /**
     * 测试新密码是否有效
     */
    private static boolean testNewPassword(String url, String username, String newPassword, String driverClassName) {
        java.sql.Connection connection = null;
        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", username);
            props.setProperty("password", newPassword);
            props.setProperty("connectTimeout", "3000");
            props.setProperty("socketTimeout", "3000");

            connection = java.sql.DriverManager.getConnection(url, props);
            return connection.isValid(2);

        } catch (java.sql.SQLException e) {
            log.error("[renewpwd] 新密码测试失败: {}", e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException e) {
                    log.debug("[renewpwd] 关闭测试连接失败", e);
                }
            }
        }
    }
}
