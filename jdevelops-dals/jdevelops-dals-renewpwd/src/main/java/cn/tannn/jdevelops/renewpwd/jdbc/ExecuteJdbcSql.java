package cn.tannn.jdevelops.renewpwd.jdbc;

import cn.tannn.jdevelops.renewpwd.exception.PasswordAuthException;
import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingHelper;
import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.RootAccess;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PasswordUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.*;
import java.util.Properties;

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
     * @return true 当前密码有效或密码已成功更新，false 如果配置无效或验证失败
     */
    public static boolean validateDatasourceConfig(ConfigurableEnvironment environment
            , String currentPassword, String backupPassword, RenewpwdProperties config) {
        String driverClassName = null;
        try {

            // 如果备份密码为空，则使用当前密码
            backupPassword = backupPassword.isEmpty() ? currentPassword : backupPassword;

            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            driverClassName = environment.getProperty("spring.datasource.driver-class-name");

            log.debug("[renewpwd] 数据源配置: url={}, username={}, password={}",
                    url, username, currentPassword);

            if (url == null || username == null || currentPassword == null) {
                log.warn("[renewpwd] 数据源配置不完整: url={}, username={}, password={}",
                        url, username, currentPassword != null ? "***" : "null");
                return false;
            }
            // 创建临时连接测试  只会抛出 SQLException
            // 密码已过期 (1862) 错误时，将数据库密码修改为备份密码 , backupPassword
            return testDatabaseConnection(url, username, driverClassName, currentPassword);
        } catch (SQLException sqlException) {
            return SQLExceptionHandlingHelper.handleDataSourceException(environment, config
                    , driverClassName, currentPassword
                    , backupPassword
                    , sqlException, "项目启动时验证数据源配置");
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
     * @return true 如果连接有效或密码已成功更新，false 如果连接无效或更新失败
     */
    public static boolean testDatabaseConnection(String url
            , String username
            , String driverClassName
            , String currentPassword
    ) throws SQLException {
        Connection connection = null;
        try {
            // 加载数据库驱动
            if (driverClassName != null) {
                Class.forName(driverClassName);
            }

            log.info("[renewpwd] 正在验证数据库连接: url={}, username={}", url, username);

            // 设置连接超时时间
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", currentPassword);
            props.setProperty("connectTimeout", "5000"); // 5秒连接超时
            props.setProperty("socketTimeout", "5000");   // 5秒socket超时

            connection = DriverManager.getConnection(url, props);

            // 测试连接有效性
            if (connection.isValid(3)) {
                log.info("[renewpwd] 数据库连接验证成功");
                return true;
            } else {
                log.warn("[renewpwd] 数据库连接无效");
                return false;
            }

        } catch (SQLException e) {
            throw e;
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
                } catch (SQLException e) {
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
     * @param dbType             {@link DbType}
     * @param connectionPassword 连接用的密码[明文]（只针对mysql有效，pgsql是通过配置中的root拿到）
     * @param newPassword 需要更新的密码[明文]
     * @param consistencyComparison true判断当前密码跟修改的密码是否一致，一致则不更新，false不判断
     * @return 新密码 ，null=更新失败
     */
    public static String handlePasswordUpdate(ConfigurableEnvironment environment,
                                              RenewpwdProperties renewpwdProperties,
                                              DbType dbType,
                                              String connectionPassword,
                                              String newPassword,
                                              boolean consistencyComparison) {
        try {
            String url = environment.getProperty("spring.datasource.url");
            String driverClassName = environment.getProperty("spring.datasource.driver-class-name");
            log.error("[renewpwd] 密码已过期必须更改密码才能登录, 尝试更新密码");
            // 如果备份密码为空，则使用当前密码
            newPassword = newPassword.isEmpty() ? connectionPassword : newPassword;

            String username;
            // 处理不同数据库
            if (dbType.equals(DbType.MYSQL)) {
                username = environment.getProperty("spring.datasource.username");
                if (consistencyComparison && connectionPassword.equals(newPassword)) {
                    log.error("[renewpwd] 当前密码与备用密码一致，且连接已断开，无法更新密码，交由业务方自行处理。");
                    return null;
                }
            } else if (dbType.equals(DbType.POSTGRE_SQL) || dbType.equals(DbType.KINGBASE8)) {
                // 对于pgsql和kingbase8，使用root账户来更新密码
                RootAccess root = renewpwdProperties.getRoot();
                if (root == null) {
                    log.error("[renewpwd] pgsql/KINGBASE8 必须配置root超级账户");
                    return null;
                }
                username = root.getUsername();
                connectionPassword = root.getPassword();
                // 尝试解密密码
                connectionPassword = AESUtil.decryptPassword(connectionPassword, renewpwdProperties.getPwdEncryptKey());
                if (username == null || username.isEmpty() || connectionPassword == null || connectionPassword.isEmpty()) {
                    log.error("[renewpwd] pgsql/KINGBASE8 root账户密码不能为空");
                    return null;
                }
            } else {
                log.error("[renewpwd] 不支持过期密码更新的数据库类型: {}", dbType);
                return null;
            }

            // 更新密码
            if (!updateUserPassword(url
                    , username
                    , connectionPassword
                    , newPassword
                    , driverClassName,
                    renewpwdProperties.getResetExpiryDay()
                    , dbType)) {
                log.error("[renewpwd] 用户密码更新验证失败");
                return null;
            }
            // 项目启动的时候用的通知，其他地方通知了也没用到
            if (passwordUpdateListener != null) {
                passwordUpdateListener.onPasswordUpdated(newPassword);
            }
            return newPassword;
        } catch (Exception e) {
            log.error("[renewpwd]  更新密码 - 验证数据源配置时发生异常", e);
            return null;
        }
    }


    /**
     * 更新用户密码
     * 当检测到1862错误码时，执行ALTER USER命令更改密码
     *
     * @param url             数据库连接URL
     * @param username        创建连接的用户名
     * @param connectPassword 创建连接的密码
     * @param newPassword     新密码 用于更新
     * @param driverClassName 数据库驱动类名
     * @param resetExpiryDay  {@link RenewpwdProperties#resetExpiryDay} 重置密码过期天数
     * @param dbType          {@link DbType} 数据库类型
     * @return true 如果密码更新成功并且新密码验证通过，false 如果更新失败或验证失败
     */
    public static boolean updateUserPassword(String url
            , String username
            , String connectPassword
            , String newPassword
            , String driverClassName
            , Integer resetExpiryDay
            , DbType dbType
    ) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            log.info("[renewpwd] 开始更新过期密码: username={}", username);


            // 配置连接属性以支持过期密码
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", connectPassword);
            props.setProperty("connectTimeout", "5000");
            props.setProperty("socketTimeout", "5000");
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("useUnicode", "true");
            props.setProperty("useSSL", "false");
            // 或者在URL中添加参数也可以
            String connectionUrl = url;
            if (dbType.equals(DbType.POSTGRE_SQL) || dbType.equals(DbType.KINGBASE8)) {
                log.info("[renewpwd] 尝试使用root密码连接数据库");
            } else if (dbType.equals(DbType.MYSQL)) {
                // 关键配置：允许使用过期密码连接
                props.setProperty("disconnectOnExpiredPasswords", "false");
                if (!url.contains("disconnectOnExpiredPasswords")) {
                    connectionUrl = url + (url.contains("?") ? "&" : "?") +
                            "disconnectOnExpiredPasswords=false";
                }
                log.info("[renewpwd] 尝试使用过期密码连接数据库");
            } else {
                log.debug("[renewpwd] 不支持的数据库类型: {}", dbType);
            }
            connection = DriverManager.getConnection(connectionUrl, props);

            // 检查连接是否处于受限模式（只能执行密码更改）
            log.info("[renewpwd] 连接成功，开始密码更新流程");

            statement = connection.createStatement();

            if (dbType.equals(DbType.POSTGRE_SQL) || dbType.equals(DbType.KINGBASE8)) {
                // 对于pgsql和kingbase8，使用root账户来更新密码
                if (PgJdbc.extractedUpdatePassword(username, newPassword, statement, resetExpiryDay)) return false;
            } else {
                if (MySqlJdbc.extractedUpdatePassword(username, newPassword, statement)) return false;
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

        } catch (SQLException e) {
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
            } catch (SQLException e) {
                log.debug("[renewpwd] 关闭连接失败", e);
            }
        }
    }

    /**
     * 测试新密码是否有效
     */
    public static boolean testNewPassword(String url, String username, String newPassword, String driverClassName) {
        Connection connection = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", newPassword);
            props.setProperty("connectTimeout", "3000");
            props.setProperty("socketTimeout", "3000");

            connection = DriverManager.getConnection(url, props);
            return connection.isValid(2);

        } catch (SQLException e) {
            log.error("[renewpwd] 新密码测试失败: {}", e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.debug("[renewpwd] 关闭测试连接失败", e);
                }
            }
        }
    }
}
