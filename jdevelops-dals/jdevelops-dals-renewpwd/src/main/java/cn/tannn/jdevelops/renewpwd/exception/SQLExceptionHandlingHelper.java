package cn.tannn.jdevelops.renewpwd.exception;

import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * SQLExceptionHandlingHelper
 * 提供静态工具方法，用于日志记录、异常分类处理、慢查询检测和告警发送。
 * 为代理类及处理器提供统一的辅助功能。
 */
final class SQLExceptionHandlingHelper {

    private static final Logger log = LoggerFactory.getLogger(SQLExceptionHandlingHelper.class);

    /* ================= 异常处理入口 ================= */

    static void handleDataSourceException(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation) {
        logException(proxy, "DATASOURCE", e, operation, null, 0);
        if (proxy.getConfig().getException().isAlertEnabled()) {
            sendAlert(proxy, "DATASOURCE", e, operation, null, 0, "数据源连接异常");
        }
    }

    static void handleConnectionException(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation, Object[] params, long duration) {
        logException(proxy, "CONNECTION", e, operation, params, duration);
        classifyAndHandle(proxy, e, operation);
        if (proxy.getConfig().getException().isAlertEnabled()) {
            sendAlert(proxy, "CONNECTION", e, operation, params, duration, "连接操作异常");
        }
    }

    static void handleStatementException(SQLExceptionHandlingDataSourceProxy proxy,
                                         SQLException e, String operation, Object[] params, long duration) {
        logException(proxy, "SQL_EXECUTION", e, operation, params, duration);
        classifyAndHandle(proxy, e, operation);
        if (proxy.getConfig().getException().isAlertEnabled()) {
            sendAlert(proxy, "SQL_EXECUTION", e, operation, params, duration, "SQL执行异常");
        }
    }

    /* ================= 慢查询 ================= */

    static void handleSlowQuery(SQLExceptionHandlingDataSourceProxy proxy,
                                String operation, Object[] params, long duration) {
        String paramStr = buildParamString(proxy, params);
        log.warn("慢查询检测 - 操作: {}, 耗时: {}ms, 阈值: {}ms, 参数: {}",
                operation, duration, proxy.getConfig().getException().getSlowQueryThreshold(), paramStr);

        if (proxy.getConfig().getException().isAlertEnabled()) {
            sendAlert(proxy, "SLOW_QUERY", null, operation, params, duration,
                    "慢查询检测: 耗时" + duration + "ms，超过阈值"
                            + proxy.getConfig().getException().getSlowQueryThreshold() + "ms");
        }
    }

    /* ================= 日志 ================= */

    private static void logException(SQLExceptionHandlingDataSourceProxy proxy,
                                     String level, SQLException e, String operation,
                                     Object[] params, long duration) {
        String paramStr = buildParamString(proxy, params);

        switch (proxy.getConfig().getException().getLogLevel().toUpperCase()) {
            case "ERROR":
                log.error("=== [{}] SQL异常处理 ===", level);
                log.error("操作: {}", operation);
                log.error("执行时间: {}ms", duration);
                log.error("SQL状态码: {}", e.getSQLState());
                log.error("错误码: {}", e.getErrorCode());
                log.error("错误信息: {}", e.getMessage());
                log.error("参数: {}", paramStr);
                break;
            case "WARN":
                log.warn("[{}] SQL异常 - 操作: {}, 状态码: {}, 错误码: {}, 信息: {}",
                        level, operation, e.getSQLState(), e.getErrorCode(), e.getMessage());
                break;
            default:
                log.debug("[{}] SQL异常详情 - 操作: {}, 耗时: {}ms, 状态码: {}, 错误码: {}, 参数: {}",
                        level, operation, duration, e.getSQLState(), e.getErrorCode(), paramStr);
        }
    }

    /* ================= 成功执行日志 ================= */

    static void logSuccessfulExecution(SQLExceptionHandlingDataSourceProxy proxy,
                                       String operation, Object[] params, long duration) {
        if (!proxy.getConfig().getException().isLogSuccessfulOperations()) return;

        String paramStr = proxy.getConfig().getException().isLogParameters() && params != null
                ? Arrays.toString(params) : "参数已隐藏";

        switch (proxy.getConfig().getException().getLogLevel().toUpperCase()) {
            case "DEBUG":
                log.debug("SQL执行成功 - 操作: {}, 耗时: {}ms, 参数: {}", operation, duration, paramStr);
                break;
            case "INFO":
                log.info("SQL执行成功 - 操作: {}, 耗时: {}ms", operation, duration);
                break;
            default:
                log.trace("SQL执行成功 - 操作: {}, 耗时: {}ms, 参数: {}", operation, duration, paramStr);
        }
    }

    /* ================= 异常分类 ================= */

    private static void classifyAndHandle(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation) {
        String sqlState = e.getSQLState();
        if (sqlState == null) return;

        boolean isCritical = Arrays.asList(proxy.getConfig().getException().getCriticalSqlStates())
                .contains(sqlState);

        switch (sqlState) {
            case "40001":
            case "40P01":
                handleDeadlock(proxy, e, operation, isCritical);
                break;
            case "08001":
            case "08006":
            case "08S01":
                handleConnectionIssue(proxy, e, operation, isCritical);
                break;
            case "23000":
            case "23505":
            case "23001":
                handleConstraintViolation(proxy, e, operation, isCritical);
                break;
            case "42000":
            case "42601":
                handleSyntaxError(proxy, e, operation, isCritical);
                break;
            case "28000":
                handleAuthorizationError(proxy, e, operation, isCritical);
                break;
            case "HY000":
                handleMySQLGenericError(proxy, e, operation, isCritical);
                break;
            default:
                handleGenericSQLException(proxy, e, operation, isCritical);
        }
    }

    /* ========= 分类处理器（保持原始日志语句） ======== */

    private static void handleDeadlock(SQLExceptionHandlingDataSourceProxy proxy,
                                       SQLException e, String operation, boolean isCritical) {
        log.warn("死锁检测 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleConnectionIssue(SQLExceptionHandlingDataSourceProxy proxy,
                                              SQLException e, String operation, boolean isCritical) {
        log.warn("连接问题 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleConstraintViolation(SQLExceptionHandlingDataSourceProxy proxy,
                                                  SQLException e, String operation, boolean isCritical) {
        log.warn("约束违反 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleSyntaxError(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation, boolean isCritical) {
        log.error("SQL语法错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleAuthorizationError(SQLExceptionHandlingDataSourceProxy proxy,
                                                 SQLException e, String operation, boolean isCritical) {
        log.error("权限错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleMySQLGenericError(SQLExceptionHandlingDataSourceProxy proxy,
                                                SQLException e, String operation, boolean isCritical) {
        log.error("MySQL通用错误 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    private static void handleGenericSQLException(SQLExceptionHandlingDataSourceProxy proxy,
                                                  SQLException e, String operation, boolean isCritical) {
        log.error("未分类SQL异常 - 操作: {}, 错误码: {}, 关键级别: {}", operation, e.getErrorCode(), isCritical);
    }

    /* ================= 告警 ================= */

    private static void sendAlert(SQLExceptionHandlingDataSourceProxy proxy,
                                  String level, SQLException e, String operation,
                                  Object[] params, long duration, String description) {
        String alertKey = level + "_" + operation;
        long currentTime = System.currentTimeMillis();
        Long lastTime = proxy.lastAlertTime.get(alertKey);
        if (lastTime != null &&
                (currentTime - lastTime) < (proxy.getConfig().getException().getAlertIntervalSeconds() * 1000)) {
            log.debug("告警被频率控制限制: {}", alertKey);
            return;
        }
        proxy.lastAlertTime.put(alertKey, currentTime);

        String alertMsg;
        if (e != null) {
            alertMsg = String.format(
                    "[%s] %s - 操作: %s, 状态码: %s, 错误码: %d, 耗时: %dms, 错误: %s",
                    level, description, operation, e.getSQLState(), e.getErrorCode(), duration, e.getMessage()
            );
        } else {
            alertMsg = String.format(
                    "[%s] %s - 操作: %s, 耗时: %dms",
                    level, description, operation, duration
            );
        }
        log.warn("告警发送: {}", alertMsg);
    }

    /* ================= 工具 ================= */

    private static String buildParamString(SQLExceptionHandlingDataSourceProxy proxy, Object[] params) {
        return proxy.getConfig().getException().isLogParameters() && params != null
                ? Arrays.toString(params) : "参数已隐藏";
    }

    /* ================= 统计 ================= */

    static String buildStatistics(SQLExceptionHandlingDataSourceProxy proxy) {
        return String.format(
                "SQL执行统计 - 总操作数: %d, 异常数: %d, 慢查询数: %d, 异常率: %.2f%%, 慢查询率: %.2f%%",
                proxy.totalOperations.get(),
                proxy.totalExceptions.get(),
                proxy.totalSlowQueries.get(),
                proxy.totalOperations.get() > 0 ? (proxy.totalExceptions.get() * 100.0 / proxy.totalOperations.get()) : 0,
                proxy.totalOperations.get() > 0 ? (proxy.totalSlowQueries.get() * 100.0 / proxy.totalOperations.get()) : 0
        );
    }

    static void resetStatistics(SQLExceptionHandlingDataSourceProxy proxy) {
        proxy.totalOperations.set(0);
        proxy.totalExceptions.set(0);
        proxy.totalSlowQueries.set(0);
        proxy.lastAlertTime.clear();
        log.info("SQL执行统计信息已重置");
    }


    /**
     * 判断是否为SQL执行方法
     */
    static boolean isExecuteMethod(String methodName) {
        return methodName.startsWith("execute") ||
                methodName.equals("executeQuery") ||
                methodName.equals("executeUpdate") ||
                methodName.equals("executeBatch") ||
                methodName.equals("executeLargeBatch");
    }


    private SQLExceptionHandlingHelper() {
    }
}
