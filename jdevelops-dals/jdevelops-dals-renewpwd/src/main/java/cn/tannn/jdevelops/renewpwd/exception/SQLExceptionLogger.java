package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * SQL异常日志记录器
 * 统一管理SQL异常的日志记录逻辑
 */
public class SQLExceptionLogger {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionLogger.class);

    private SQLExceptionLogger() {
    }

    /**
     * 异常日志上下文
     */
    public static class ExceptionLogContext {
        private final String level;
        private final SQLException exception;
        private final String operation;
        private final Object[] params;
        private final long duration;
        private final String logLevel;
        private final boolean logParameters;

        public ExceptionLogContext(String level, SQLException exception, String operation,
                                 Object[] params, long duration, String logLevel, boolean logParameters) {
            this.level = level;
            this.exception = exception;
            this.operation = operation;
            this.params = params;
            this.duration = duration;
            this.logLevel = logLevel;
            this.logParameters = logParameters;
        }

        // Getters
        public String getLevel() { return level; }
        public SQLException getException() { return exception; }
        public String getOperation() { return operation; }
        public Object[] getParams() { return params; }
        public long getDuration() { return duration; }
        public String getLogLevel() { return logLevel; }
        public boolean isLogParameters() { return logParameters; }
    }

    /**
     * 记录异常日志
     *
     * @param context 异常日志上下文
     */
    public static void logException(ExceptionLogContext context) {
        String paramStr = buildParamString(context.isLogParameters(), context.getParams());
        SQLException e = context.getException();

        switch (context.getLogLevel().toUpperCase()) {
            case "ERROR":
                logErrorLevel(context, paramStr, e);
                break;
            case "WARN":
                logWarnLevel(context, e);
                break;
            default:
                logDebugLevel(context, paramStr, e);
                break;
        }
    }

    /**
     * 记录异常日志 - 基于配置对象
     *
     * @param config 配置对象
     * @param level 日志级别
     * @param e 异常对象
     * @param operation 操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     */
    public static void logException(RenewpwdProperties config, String level, SQLException e,
                                  String operation, Object[] params, long duration) {
        ExceptionLogContext context = new ExceptionLogContext(
                level, e, operation, params, duration,
                config.getException().getLogLevel(),
                config.getException().isLogParameters()
        );
        logException(context);
    }

    /**
     * 记录异常日志 - 基于代理对象
     *
     * @param proxy 数据源代理实例
     * @param level 日志级别
     * @param e 异常对象
     * @param operation 操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     */
    public static void logException(SQLExceptionHandlingDataSourceProxy proxy, String level,
                                  SQLException e, String operation, Object[] params, long duration) {
        logException(proxy.getConfig(), level, e, operation, params, duration);
    }

    // ================= 私有方法 =================

    /**
     * ERROR级别日志记录
     */
    private static void logErrorLevel(ExceptionLogContext context, String paramStr, SQLException e) {
        log.error("=== [{}] SQL异常处理 ===", context.getLevel());
        log.error("操作: {}", context.getOperation());
        log.error("执行时间: {}ms", context.getDuration());
        log.error("SQL状态码: {}", e.getSQLState());
        log.error("错误码: {}", e.getErrorCode());
        log.error("错误信息: {}", e.getMessage());
        log.error("参数: {}", paramStr);
    }

    /**
     * WARN级别日志记录
     */
    private static void logWarnLevel(ExceptionLogContext context, SQLException e) {
        log.warn("[{}] SQL异常 - 操作: {}, 状态码: {}, 错误码: {}, 信息: {}",
                context.getLevel(), context.getOperation(), e.getSQLState(),
                e.getErrorCode(), e.getMessage());
    }

    /**
     * DEBUG级别日志记录
     */
    private static void logDebugLevel(ExceptionLogContext context, String paramStr, SQLException e) {
        log.debug("[{}] SQL异常详情 - 操作: {}, 耗时: {}ms, 状态码: {}, 错误码: {}, 参数: {}",
                context.getLevel(), context.getOperation(), context.getDuration(),
                e.getSQLState(), e.getErrorCode(), paramStr);
    }

    /**
     * 构建参数字符串
     */
    private static String buildParamString(boolean isLogParameters, Object[] params) {
        return isLogParameters && params != null ? Arrays.toString(params) : "参数已隐藏";
    }
}
