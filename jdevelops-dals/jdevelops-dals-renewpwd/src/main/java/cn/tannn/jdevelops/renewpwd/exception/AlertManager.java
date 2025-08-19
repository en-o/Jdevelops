package cn.tannn.jdevelops.renewpwd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

/**
 * 告警管理器
 * 统一管理SQL异常告警的发送和频率控制
 */
public class AlertManager {

    private static final Logger log = LoggerFactory.getLogger(AlertManager.class);

    private AlertManager() {
    }

    /**
     * 告警上下文
     */
    public static class AlertContext {
        private final String level;
        private final SQLException exception;
        private final String operation;
        private final Object[] params;
        private final long duration;
        private final String description;

        public AlertContext(String level, SQLException exception, String operation,
                          Object[] params, long duration, String description) {
            this.level = level;
            this.exception = exception;
            this.operation = operation;
            this.params = params;
            this.duration = duration;
            this.description = description;
        }

        // Getters
        public String getLevel() { return level; }
        public SQLException getException() { return exception; }
        public String getOperation() { return operation; }
        public Object[] getParams() { return params; }
        public long getDuration() { return duration; }
        public String getDescription() { return description; }
    }

    /**
     * 频率控制上下文
     */
    public static class RateLimitContext {
        private final Map<String, Long> lastAlertTime;
        private final long alertIntervalSeconds;

        public RateLimitContext(Map<String, Long> lastAlertTime, long alertIntervalSeconds) {
            this.lastAlertTime = lastAlertTime;
            this.alertIntervalSeconds = alertIntervalSeconds;
        }

        public Map<String, Long> getLastAlertTime() { return lastAlertTime; }
        public long getAlertIntervalSeconds() { return alertIntervalSeconds; }
    }

    /**
     * 发送告警（无频率控制）
     *
     * @param context 告警上下文
     */
    public static void sendAlert(AlertContext context) {
        String alertMsg = buildAlertMessage(context);
        log.warn("告警发送: {}", alertMsg);
    }

    /**
     * 发送告警（带频率控制）
     *
     * @param context 告警上下文
     * @param rateLimitContext 频率控制上下文
     */
    public static void sendAlertWithRateLimit(AlertContext context, RateLimitContext rateLimitContext) {
        if (!shouldSendAlert(context, rateLimitContext)) {
            String alertKey = context.getLevel() + "_" + context.getOperation();
            log.debug("告警被频率控制限制: {}", alertKey);
            return;
        }

        updateLastAlertTime(context, rateLimitContext);
        sendAlert(context);
    }

    /**
     * 发送告警 - 基于代理对象（带频率控制）
     *
     * @param proxy 数据源代理实例
     * @param level 告警级别
     * @param e 异常对象
     * @param operation 操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     * @param description 告警描述
     */
    public static void sendAlert(SQLExceptionHandlingDataSourceProxy proxy, String level,
                               SQLException e, String operation, Object[] params,
                               long duration, String description) {
        AlertContext alertContext = new AlertContext(level, e, operation, params, duration, description);
        RateLimitContext rateLimitContext = new RateLimitContext(
                proxy.lastAlertTime,
                proxy.getConfig().getException().getAlertIntervalSeconds()
        );

        sendAlertWithRateLimit(alertContext, rateLimitContext);
    }

    /**
     * 发送告警 - 无频率控制版本
     *
     * @param level 告警级别
     * @param e 异常对象
     * @param operation 操作名称
     * @param params 操作参数
     * @param duration 执行时长（毫秒）
     * @param description 告警描述
     */
    public static void sendAlert(String level, SQLException e, String operation,
                               Object[] params, long duration, String description) {
        AlertContext context = new AlertContext(level, e, operation, params, duration, description);
        sendAlert(context);
    }

    // ================= 私有方法 =================

    /**
     * 构建告警消息
     */
    private static String buildAlertMessage(AlertContext context) {
        if (context.getException() != null) {
            return String.format(
                    "[%s] %s - 操作: %s, 状态码: %s, 错误码: %d, 耗时: %dms, 错误: %s",
                    context.getLevel(), context.getDescription(), context.getOperation(),
                    context.getException().getSQLState(), context.getException().getErrorCode(),
                    context.getDuration(), context.getException().getMessage()
            );
        } else {
            return String.format(
                    "[%s] %s - 操作: %s, 耗时: %dms",
                    context.getLevel(), context.getDescription(), context.getOperation(), context.getDuration()
            );
        }
    }

    /**
     * 判断是否应该发送告警
     */
    private static boolean shouldSendAlert(AlertContext context, RateLimitContext rateLimitContext) {
        String alertKey = context.getLevel() + "_" + context.getOperation();
        long currentTime = System.currentTimeMillis();
        Long lastTime = rateLimitContext.getLastAlertTime().get(alertKey);

        return lastTime == null ||
               (currentTime - lastTime) >= (rateLimitContext.getAlertIntervalSeconds() * 1000L);
    }

    /**
     * 更新最后告警时间
     */
    private static void updateLastAlertTime(AlertContext context, RateLimitContext rateLimitContext) {
        String alertKey = context.getLevel() + "_" + context.getOperation();
        long currentTime = System.currentTimeMillis();
        rateLimitContext.getLastAlertTime().put(alertKey, currentTime);
    }
}
