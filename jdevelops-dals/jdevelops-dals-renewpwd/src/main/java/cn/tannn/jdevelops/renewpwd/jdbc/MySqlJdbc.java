package cn.tannn.jdevelops.renewpwd.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;


/**
 * mysql
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/19 10:20
 */
public class MySqlJdbc {

    private static final Logger log = LoggerFactory.getLogger(MySqlJdbc.class);


    /**
     * 获取过期密码情况下的当前用户主机信息
     * 专门处理密码过期时的用户信息查询
     */
    public static String getCurrentUserHostForExpiredPassword(java.sql.Statement statement, String username) {
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
    public static String getUserHostFromTable(java.sql.Statement statement) {
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
     *
     * @param username 用户名
     * @param newPassword 新密码
     * @param statement Statement对象
     * @return 如果更新成功返回false，否则返回true
     * @throws SQLException
     */
    public static boolean extractedUpdatePassword(String username, String newPassword, Statement statement) throws SQLException {
        // 在过期密码模式下，优先尝试SET PASSWORD语句（更简单）
        String setPasswordSQL = String.format("SET PASSWORD = PASSWORD('%s')", newPassword);

        try {
            log.debug("[renewpwd] 尝试执行: SET PASSWORD = PASSWORD('***')");
            statement.executeUpdate(setPasswordSQL);
            log.info("[renewpwd] 使用SET PASSWORD更新密码成功");
        } catch (SQLException e) {
            log.warn("[renewpwd] SET PASSWORD失败，尝试ALTER USER方式: {}", e.getMessage());

            // 如果SET PASSWORD失败，尝试ALTER USER
            // 首先获取当前用户信息
            String currentUserHost = MySqlJdbc.getCurrentUserHostForExpiredPassword(statement, username);
            if (newPassword == null || newPassword.isEmpty()) {
                log.error("[renewpwd] 新密码不能为空");
                return true;
            }
            String alterUserSQL = String.format("ALTER USER '%s'@'%s' IDENTIFIED BY '%s'",
                    username, currentUserHost, newPassword);

            log.debug("[renewpwd] 执行SQL: ALTER USER '{}' @'{}' IDENTIFIED BY '***'", username, currentUserHost);
            statement.executeUpdate(alterUserSQL);
            log.info("[renewpwd] 使用ALTER USER更新密码成功: username={}@{}", username, currentUserHost);
        }
        return false;
    }

}
