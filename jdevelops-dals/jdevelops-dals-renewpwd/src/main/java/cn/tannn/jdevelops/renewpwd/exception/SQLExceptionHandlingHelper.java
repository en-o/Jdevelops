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

    private SQLExceptionHandlingHelper() {
    }

    /* ================= 异常处理入口 ================= */

    static void handleDataSourceException(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation) {

        logException(proxy, "DATASOURCE", e, operation, null, 0);
        // 分类处理
        classifyAndHandle(proxy, e, operation);
        if (proxy.getConfig().getException().isAlertEnabled()) {
            sendAlert(proxy, "DATASOURCE", e, operation, null, 0, "数据源连接异常");
        }
    }

    /* ================= 日志 ================= */

    /**
     * 记录异常日志
     * @param proxy 数据源代理实例
     * @param level 日志级别
     * @param e 异常对象
     * @param operation   操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     */
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

    /* ================= 异常分类 ================= */

    /**
     * 根据SQL异常状态码分类处理异常
     * @param proxy 数据源代理实例
     * @param e 异常对象
     * @param operation 操作名称
     */
    private static void classifyAndHandle(SQLExceptionHandlingDataSourceProxy proxy,
                                          SQLException e, String operation) {
        String sqlState = e.getSQLState();
        if (sqlState == null) return;
        // 原始错误码
        int databaseErrorCode = e.getErrorCode();

        switch (sqlState) {
            case "S1000":
                handleConnectionIssue(proxy, e, operation, databaseErrorCode);
                break;
            default:
                handleGenericSQLException(proxy, e, operation, databaseErrorCode);
        }
    }

    /* ========= 分类处理器（保持原始日志语句） ======== */


    private static void handleConnectionIssue(SQLExceptionHandlingDataSourceProxy proxy,
                                              SQLException e, String operation, int databaseErrorCode) {
        log.warn("连接问题 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}", operation, e.getErrorCode(), databaseErrorCode);
    }

    private static void handleAuthorizationError(SQLExceptionHandlingDataSourceProxy proxy,
                                                 SQLException e, String operation, int databaseErrorCode) {
        log.error("权限错误 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}", operation, e.getErrorCode(), databaseErrorCode);
    }


    private static void handleGenericSQLException(SQLExceptionHandlingDataSourceProxy proxy,
                                                  SQLException e, String operation, int databaseErrorCode) {
        log.error("未分类SQL异常 - 操作: {}, SQL标准错误码: {}, 数据库原始错误码: {}", operation, e.getErrorCode(), databaseErrorCode);
    }

    /* ================= 告警 ================= */

    /**
     * 发送告警
     * @param proxy 数据源代理实例
     * @param level 告警级别
     * @param e 异常对象
     * @param operation 操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     * @param description 告警描述
     */
    private static void sendAlert(SQLExceptionHandlingDataSourceProxy proxy,
                                  String level, SQLException e, String operation,
                                  Object[] params, long duration, String description) {
        String alertKey = level + "_" + operation;
        long currentTime = System.currentTimeMillis();
        Long lastTime = proxy.lastAlertTime.get(alertKey);
        if (lastTime != null &&
                (currentTime - lastTime) < (proxy.getConfig().getException().getAlertIntervalSeconds() * 1000L)) {
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


}
