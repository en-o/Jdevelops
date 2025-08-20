package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.RenewPwdRefresh;
import cn.tannn.jdevelops.renewpwd.jdbc.ExecuteJdbcSql;
import cn.tannn.jdevelops.renewpwd.pojo.DbType;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.util.DatabaseUtils;
import cn.tannn.jdevelops.renewpwd.util.RenewPwdApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.SQLException;

/**
 * SQL异常分类处理器
 * 根据SQL状态码对异常进行分类处理
 */
public class SQLExceptionClassifier {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionClassifier.class);

    /**
     * 异常处理上下文
     */
    public static class ExceptionHandleContext {
        private final ConfigurableEnvironment environment;
        private final RenewpwdProperties config;
        private final String connectionPassword;
        private final String newPassword;
        private final String driverClassName;
        private final SQLException exception;
        private final String operation;

        public ExceptionHandleContext(ConfigurableEnvironment environment, RenewpwdProperties config,
                                    String connectionPassword, String newPassword, String driverClassName,
                                    SQLException exception, String operation) {
            this.environment = environment;
            this.config = config;
            this.connectionPassword = connectionPassword;
            this.newPassword = newPassword;
            this.driverClassName = driverClassName;
            this.exception = exception;
            this.operation = operation;
        }

        // Getters
        public ConfigurableEnvironment getEnvironment() { return environment; }
        public RenewpwdProperties getConfig() { return config; }
        public String getConnectionPassword() { return connectionPassword; }
        public String getNewPassword() { return newPassword; }
        public String getDriverClassName() { return driverClassName; }
        public SQLException getException() { return exception; }
        public String getOperation() { return operation; }

        public boolean hasEnvironmentAndConfig() {
            return environment != null && config != null;
        }
    }

    private SQLExceptionClassifier() {
    }

    /**
     * 根据SQL异常状态码分类处理异常
     *
     * @param context 异常处理上下文
     * @return false 操作失败，true 操作成功
     */
    public static boolean classifyAndHandle(ExceptionHandleContext context) {
        String sqlState = context.getException().getSQLState();
        if (sqlState == null) {
            return false;
        }

        return switch (sqlState) {
            case "S1000" -> handleConnectionIssue(context);
            case "28000" -> handleMySQLAuthenticationFailure(context);
            case "28P01" -> // PostgreSQL的认证失败 https://www.postgresql.org/docs/current/errcodes-appendix.html
                    handlePostgreSQLAuthorizationError(context);
            default -> handleGenericSQLException(context);
        };
    }

    /**
     * 根据SQL异常状态码分类处理异常 - 简化版本
     *
     * @param environment 环境配置
     * @param config 配置属性
     * @param connectionPassword 连接密码
     * @param newPassword 新密码
     * @param driverClassName 驱动类名
     * @param e 异常对象
     * @param operation 操作名称
     * @return false 操作失败，true 操作成功
     */
    public static boolean classifyAndHandle(ConfigurableEnvironment environment, RenewpwdProperties config,
                                          String connectionPassword, String newPassword, String driverClassName,
                                          SQLException e, String operation) {
        ExceptionHandleContext context = new ExceptionHandleContext(
                environment, config, connectionPassword, newPassword, driverClassName, e, operation);
        return classifyAndHandle(context);
    }

    /**
     * 根据SQL异常状态码分类处理异常 - 最简版本
     *
     * @param driverClassName 驱动名
     * @param e 异常对象
     * @param operation 操作名称
     *                  不需要返回值
     */
    public static void classifyAndHandle(String driverClassName, SQLException e, String operation) {
        ExceptionHandleContext context = new ExceptionHandleContext(
                null, null, null, null, driverClassName, e, operation);
         classifyAndHandle(context);
    }

    // ================= 分类处理器 =================

    /**
     * 处理连接问题 (S1000)
     */
    private static boolean handleConnectionIssue(ExceptionHandleContext context) {
        log.error("连接问题 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}, driver: {}",
                context.getOperation(), context.getException().getSQLState(),
                context.getException().getErrorCode(), context.getDriverClassName());

        // 检查是否为MySQL密码过期错误
        if (DatabaseUtils.isPasswordExpiredError_MYSQL(context.getException().getErrorCode(),
                                                      context.getDriverClassName())) {
            if (context.hasEnvironmentAndConfig()) {
                // 这个是第一次启动的时候处理的方法所以不需要刷新
                return ExecuteJdbcSql.handlePasswordUpdate(context.getEnvironment(), context.getConfig(),
                        DbType.MYSQL, context.getConnectionPassword(), context.getNewPassword(), false) != null;
            }
            return getRenewPwdRefresh().fixPassword();
        }
        return false;
    }

    /**
     * 处理MySQL认证失败 (28000)
     */
    private static boolean handleMySQLAuthenticationFailure(ExceptionHandleContext context) {
        // 目前对接的三个库只有mysql的认证失败是28000，mysql需要操作的只有S1000
        log.error("mysql 认证失败禁止操作 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}, driver: {}",
                context.getOperation(), context.getException().getSQLState(),
                context.getException().getErrorCode(), context.getDriverClassName());
        return false;
    }

    /**
     * 处理PostgreSQL权限错误 (28P01)
     */
    private static boolean handlePostgreSQLAuthorizationError(ExceptionHandleContext context) {
        log.error("权限错误 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}, driver: {}",
                context.getOperation(), context.getException().getSQLState(),
                context.getException().getErrorCode(), context.getDriverClassName());

        if (context.getException().getErrorCode() == 0) {
            // 0表示密码错误
            if (context.hasEnvironmentAndConfig()) {
                // 这个是第一次启动的时候处理的方法所以不需要刷新
                return ExecuteJdbcSql.handlePasswordUpdate(context.getEnvironment(), context.getConfig(),
                        DbType.POSTGRE_SQL, context.getConnectionPassword(), context.getNewPassword(), true) != null;
            }
            return getRenewPwdRefresh().updatePassword(DbType.POSTGRE_SQL);
        } else if (DatabaseUtils.isPasswordError(context.getException().getErrorCode(), context.getDriverClassName())) {
            // 这个是兜底判断，按理说应该删掉
            if (context.hasEnvironmentAndConfig()) {
                // 这个是第一次启动的时候处理的方法所以不需要刷新
                return ExecuteJdbcSql.handlePasswordUpdate(context.getEnvironment(), context.getConfig(),
                        DbType.MYSQL, context.getConnectionPassword(), context.getNewPassword(), true) != null;
            }
            return getRenewPwdRefresh().updatePassword(DbType.MYSQL);
        }
        return false;
    }

    /**
     * 处理通用SQL异常
     */
    private static boolean handleGenericSQLException(ExceptionHandleContext context) {
        log.error("未分类SQL异常 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}, driver: {}",
                context.getOperation(), context.getException().getSQLState(),
                context.getException().getErrorCode(), context.getDriverClassName());
        return false;
    }

    // ================= 工具方法 =================

    /**
     * 获取密码刷新服务
     */
    private static RenewPwdRefresh getRenewPwdRefresh() {
        if (RenewPwdApplicationContextHolder.getContext() == null) {
            log.error("RenewPwdApplicationContextHolder is not initialized.");
            throw new IllegalStateException("RenewPwdApplicationContextHolder is not initialized.");
        }
        return RenewPwdApplicationContextHolder.getContext().getBean(RenewPwdRefresh.class);
    }
}
