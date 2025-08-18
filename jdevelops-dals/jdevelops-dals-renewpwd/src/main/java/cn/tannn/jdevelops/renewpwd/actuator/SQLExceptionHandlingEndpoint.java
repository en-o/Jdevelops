package cn.tannn.jdevelops.renewpwd.actuator;

import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingDataSourceProxy;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.SQLExceptionHandlingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL异常处理自定义Actuator端点
 * 提供详细的监控和管理功能
 *
 * 访问路径:
 * - GET /actuator/sql-exception-handling - 获取总体信息
 * - GET /actuator/sql-exception-handling/statistics - 获取统计信息
 * - GET /actuator/sql-exception-handling/config - 获取配置信息
 * - POST /actuator/sql-exception-handling/reset-statistics - 重置统计
 *
 * @author tannn
 * @since 1.0.0
 */
@Component
@Endpoint(id = "sql-exception-handling")
@ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
@ConditionalOnProperty(
        prefix = "jdevelops.renewpwd",
        name = "enabled",
        havingValue = "true"
)
public class SQLExceptionHandlingEndpoint {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RenewpwdProperties config;

    /**
     * 获取SQL异常处理总体信息
     * GET /actuator/sql-exception-handling
     */
    @ReadOperation
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();

        boolean proxyEnabled = dataSource instanceof SQLExceptionHandlingDataSourceProxy;
        info.put("enabled", proxyEnabled);
        info.put("configEnabled", config.getEnabled());
        info.put("timestamp", System.currentTimeMillis());

        if (proxyEnabled) {
            SQLExceptionHandlingDataSourceProxy proxy = (SQLExceptionHandlingDataSourceProxy) dataSource;
            info.put("statistics", proxy.getStatistics());
            info.put("dataSourceClass", dataSource.getClass().getSimpleName());
        }

        info.put("config", getDetailedConfig());

        return info;
    }

    /**
     * 获取统计信息
     * GET /actuator/sql-exception-handling/statistics
     */
    @ReadOperation
    public Map<String, Object> statistics(@Selector String selector) {
        Map<String, Object> result = new HashMap<>();

        if (!"statistics".equals(selector)) {
            result.put("error", "Invalid selector. Use 'statistics'");
            return result;
        }

        if (dataSource instanceof SQLExceptionHandlingDataSourceProxy) {
            SQLExceptionHandlingDataSourceProxy proxy = (SQLExceptionHandlingDataSourceProxy) dataSource;
            result.put("statisticsText", proxy.getStatistics());
            result.put("timestamp", System.currentTimeMillis());
            result.put("proxyEnabled", true);
        } else {
            result.put("proxyEnabled", false);
            result.put("message", "SQL异常处理代理未启用");
        }

        return result;
    }

    /**
     * 获取配置信息
     * GET /actuator/sql-exception-handling/config
     */
    @ReadOperation
    public Map<String, Object> config(@Selector String selector) {
        Map<String, Object> result = new HashMap<>();

        if (!"config".equals(selector)) {
            result.put("error", "Invalid selector. Use 'config'");
            return result;
        }

        result.put("config", getDetailedConfig());
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }

    /**
     * 重置统计信息
     * POST /actuator/sql-exception-handling/reset-statistics
     */
    @WriteOperation
    public Map<String, Object> resetStatistics() {
        Map<String, Object> result = new HashMap<>();

        if (dataSource instanceof SQLExceptionHandlingDataSourceProxy) {
            SQLExceptionHandlingDataSourceProxy proxy = (SQLExceptionHandlingDataSourceProxy) dataSource;
            proxy.resetStatistics();

            result.put("success", true);
            result.put("message", "统计信息已重置");
            result.put("timestamp", System.currentTimeMillis());
        } else {
            result.put("success", false);
            result.put("message", "SQL异常处理代理未启用，无法重置统计");
        }

        return result;
    }

    /**
     * 获取详细配置信息
     */
    private Map<String, Object> getDetailedConfig() {
        Map<String, Object> detailedConfig = new HashMap<>();

        // 基础配置
        Map<String, Object> basic = new HashMap<>();
        basic.put("enabled", config.getEnabled());
        basic.put("alertEnabled", config.getException().isAlertEnabled());
        basic.put("logLevel", config.getException().getLogLevel());
        basic.put("logSuccessfulOperations", config.getException().isLogSuccessfulOperations());
        basic.put("logParameters", config.getException().isLogParameters());
        detailedConfig.put("basic", basic);

        // 性能配置
        Map<String, Object> performance = new HashMap<>();
        performance.put("slowQueryThreshold", config.getException().getSlowQueryThreshold() + "ms");
        performance.put("connectionTimeout", config.getException().getConnectionTimeout() + "ms");
        performance.put("queryTimeout", config.getException().getQueryTimeout() + "ms");
        performance.put("longTransactionThreshold", config.getException().getLongTransactionThreshold() + "ms");
        detailedConfig.put("performance", performance);

        // 监控配置
        Map<String, Object> monitoring = new HashMap<>();
        monitoring.put("enableConnectionLeakDetection", config.getException().isEnableConnectionLeakDetection());
        monitoring.put("connectionLeakDetectionThreshold", config.getException().getConnectionLeakDetectionThreshold() + "ms");
        monitoring.put("enableSqlInjectionDetection", config.getException().isEnableSqlInjectionDetection());
        monitoring.put("enableBatchMonitoring", config.getException().isEnableBatchMonitoring());
        monitoring.put("enableTransactionMonitoring", config.getException().isEnableTransactionMonitoring());
        monitoring.put("enableConnectionPoolMonitoring", config.getException().isEnableConnectionPoolMonitoring());
        detailedConfig.put("monitoring", monitoring);

        // 告警配置
        Map<String, Object> alerting = new HashMap<>();
        alerting.put("alertIntervalSeconds", config.getException().getAlertIntervalSeconds() + "s");
        alerting.put("exceptionFrequencyThreshold", config.getException().getExceptionFrequencyThreshold() + "/min");
        alerting.put("connectionPoolUsageThreshold", config.getException().getConnectionPoolUsageThreshold() + "%");
        alerting.put("criticalSqlStates", config.getException().getCriticalSqlStates());
        detailedConfig.put("alerting", alerting);

        // 阈值配置
        Map<String, Object> thresholds = new HashMap<>();
        thresholds.put("deadlockRetryCount", config.getException().getDeadlockRetryCount());
        thresholds.put("batchSizeThreshold", config.getException().getBatchSizeThreshold());
        thresholds.put("exceptionStatisticsWindow", config.getException().getExceptionStatisticsWindow() + "s");
        detailedConfig.put("thresholds", thresholds);

        return detailedConfig;
    }
}
