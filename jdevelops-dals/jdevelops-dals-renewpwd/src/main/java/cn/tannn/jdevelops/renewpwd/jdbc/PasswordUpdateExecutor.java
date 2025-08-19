package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 密码更新执行器
 * 统一封装不同数据库的密码更新逻辑
 */
public class PasswordUpdateExecutor {

    private static final Logger log = LoggerFactory.getLogger(PasswordUpdateExecutor.class);

    private PasswordUpdateExecutor() {
    }

    /**
     * 执行密码更新操作
     *
     * @param connection     数据库连接
     * @param username       用户名
     * @param newPassword    新密码
     * @param dbType         数据库类型
     * @param resetExpiryDay 重置过期天数（仅对PostgreSQL和KingBase8有效）
     * @return true 如果更新成功
     * @throws SQLException 如果更新过程中发生SQL异常
     */
    public static boolean executePasswordUpdate(Connection connection, String username,
                                                String newPassword, DbType dbType,
                                                Integer resetExpiryDay) throws SQLException {
        if (connection == null || username == null || newPassword == null || newPassword.isEmpty()) {
            log.error("[renewpwd] 密码更新参数无效: connection={}, username={}, newPassword={}",
                    connection != null, username, newPassword != null ? "***" : "null");
            return false;
        }

        try (Statement statement = connection.createStatement()) {
            switch (dbType) {
                case MYSQL:
                    return MySqlJdbc.extractedUpdatePassword(username, newPassword, statement);
                case POSTGRE_SQL:
                case KINGBASE8:
                    return PgJdbc.extractedUpdatePassword(username, newPassword, statement, resetExpiryDay);
                default:
                    log.error("[renewpwd] 不支持的数据库类型进行密码更新: {}", dbType);
                    return true; // 返回true表示失败（与原有逻辑保持一致）
            }
        }
    }

    /**
     * 验证新密码是否生效
     *
     * @param url             数据库连接URL
     * @param username        用户名
     * @param newPassword     新密码
     * @param driverClassName 驱动类名
     * @return true 如果新密码验证成功
     */
    public static boolean validateNewPassword(String url, String username,
                                              String newPassword, String driverClassName, DbType dbType) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionManager.createConnection(url, username, newPassword,
                    driverClassName, 3000, 3000, dbType);
            boolean isValid = DatabaseConnectionManager.isConnectionValid(connection, 2, dbType);
            log.info("[renewpwd] 新密码验证结果: {}", isValid ? "成功" : "失败");
            return isValid;
        } catch (Exception e) {
            log.error("[renewpwd] 新密码测试失败: {}", e.getMessage());
            return false;
        } finally {
            DatabaseConnectionManager.closeConnection(connection);
        }
    }

    /**
     * 完整的密码更新流程
     * 包括更新密码和验证新密码
     *
     * @param url             数据库连接URL
     * @param username        连接用户名
     * @param connectPassword 连接密码
     * @param targetUsername  要更新密码的用户名
     * @param newPassword     新密码
     * @param driverClassName 驱动类名
     * @param resetExpiryDay  重置过期天数
     * @param dbType          数据库类型
     * @return true 如果密码更新并验证成功
     */
    public static boolean updateAndValidatePassword(String url, String username, String connectPassword,
                                                    String targetUsername, String newPassword,
                                                    String driverClassName, Integer resetExpiryDay,
                                                    DbType dbType) {
        Connection connection = null;

        try {
            log.info("[renewpwd] 开始更新过期密码: username={}", targetUsername);

            // 创建连接
            connection = DatabaseConnectionManager.createConnection(url, username, connectPassword,
                    driverClassName, 5000, 5000, dbType);

            log.info("[renewpwd] 连接成功，开始密码更新流程");

            // 执行密码更新
            boolean updateResult = executePasswordUpdate(connection, targetUsername, newPassword,
                    dbType, resetExpiryDay);
            if (updateResult) {
                log.error("[renewpwd] 密码更新失败");
                return false;
            }

            // 关闭当前连接，准备用新密码测试
            DatabaseConnectionManager.closeConnection(connection);
            connection = null;

            // 验证新密码是否生效
            log.info("[renewpwd] 开始验证新密码");
            if (validateNewPassword(url, targetUsername, newPassword, driverClassName, dbType)) {
                log.info("[renewpwd] 新密码验证成功，过期密码已更新");
                return true;
            } else {
                log.error("[renewpwd] 新密码验证失败");
                return false;
            }

        } catch (Exception e) {
            log.error("[renewpwd] 更新用户密码异常", e);
            return false;
        } finally {
            DatabaseConnectionManager.closeConnection(connection);
        }
    }
}
