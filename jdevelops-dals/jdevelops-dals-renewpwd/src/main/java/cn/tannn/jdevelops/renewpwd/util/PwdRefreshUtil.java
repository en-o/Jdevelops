package cn.tannn.jdevelops.renewpwd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.text.MessageFormat;
import java.util.List;

/**
 * 配置刷新工具类
 * 提供Bean刷新相关的工具方法，包括Bean类型判断、配置匹配、刷新策略决策等
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/10 01:33
 */
public class PwdRefreshUtil {


    private static final Logger log = LoggerFactory.getLogger(PwdRefreshUtil.class);
    /**
     * 密码池
     */
    private static List<String> passwords;


    /**
     * 验证数据源配置是否有效
     * @param environment  ConfigurableEnvironment
     * @param masterPassword 当前密码
     * @param backupPassword 密码池（用于重置密码的密码组） 当前还没有用
     */
    public static boolean validateDatasourceConfig(ConfigurableEnvironment environment
            , String masterPassword, List<String> backupPassword) {
        try {
            // 暂时还不知道怎么出，这个用来修改密码用的，但是修改之后不知道怎么将修改之后的通知回去，在第二次启动项目的时候不用主密码而是用修改之后的密码
            passwords = backupPassword;

            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            log.debug("[renewpwd] 数据源配置: url={}, username={}, password={}",
                    url, username, masterPassword);

            if (url == null || username == null || masterPassword == null) {
                log.warn("[renewpwd] 数据源配置不完整: url={}, username={}, password={}",
                        url, username, masterPassword != null ? "***" : "null");
                return false;
            }

            // 创建临时连接测试
            return testDatabaseConnection(url, username, masterPassword, driverClassName);

        } catch (Exception e) {
            log.error("[renewpwd] 验证数据源配置时发生异常", e);
            return false;
        }
    }


    /**
     * 测试数据库连接是否有效
     */
    public static boolean testDatabaseConnection(String url, String username, String password, String driverClassName) {
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
            props.setProperty("password", password);
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
            } else if (vendorCode == 1820 || vendorCode == 1862) {
                log.error("[renewpwd] 数据库连接验证失败，密码已过期必须更改密码才能登录: {}, 尝试更新密码", e.getMessage());
                // 我这里就不替换里面了，用原来的密码再改一次
                return updateUserPassword(url, username, password, password, driverClassName);
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
     * 当检测到1862错误码时，执行ALTER USER命令更改密码
     *
     * @param currentPassword 旧密码
     * @param newPassword     新密码
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
