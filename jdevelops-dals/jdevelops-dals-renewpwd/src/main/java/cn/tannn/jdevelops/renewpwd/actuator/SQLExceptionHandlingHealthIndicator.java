package cn.tannn.jdevelops.renewpwd.actuator;

import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingDataSourceProxy;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.SQLExceptionHandlingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL异常处理健康指示器
 * 集成到Spring Boot Actuator的/health端点
 *
 * @author tannn
 * @since 1.0.0
 */
@Component("sqlExceptionHandlingHealthIndicator")
@ConditionalOnClass(name = "org.springframework.boot.actuate.health.HealthIndicator")
@ConditionalOnProperty(
        prefix = "jdevelops.renewpwd",
        name = "enabled",
        havingValue = "true"
)
public class SQLExceptionHandlingHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RenewpwdProperties config;

    @Override
    public Health health() {
        try {
            return checkSQLExceptionHandling();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("errorClass", e.getClass().getSimpleName())
                    .build();
        }
    }

    private Health checkSQLExceptionHandling() {
        Health.Builder builder = Health.up();
        Map<String, Object> details = new HashMap<>();

        // 检查代理是否启用
        boolean proxyEnabled = dataSource instanceof SQLExceptionHandlingDataSourceProxy;
        details.put("proxyEnabled", proxyEnabled);
        details.put("configEnabled", config.getEnabled());

        if (!proxyEnabled) {
            return Health.down()
                    .withDetail("reason", "SQL异常处理代理未启用")
                    .withDetail("suggestion", "检查配置: renewpwd.datasource.exception-handling.enabled")
                    .withDetails(details)
                    .build();
        }

        // 获取代理统计信息
        SQLExceptionHandlingDataSourceProxy proxy = (SQLExceptionHandlingDataSourceProxy) dataSource;
        String statistics = proxy.getStatistics();
        details.put("statistics", statistics);

        // 检查数据库连接
        try (Connection connection = dataSource.getConnection()) {
            boolean isConnectable = !connection.isClosed();
            details.put("databaseConnectable", isConnectable);

            if (!isConnectable) {
                return Health.down()
                        .withDetail("reason", "数据库连接不可用")
                        .withDetails(details)
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("reason", "数据库连接测试失败")
                    .withDetail("connectionError", e.getMessage())
                    .withDetails(details)
                    .build();
        }

        // 添加配置信息
        details.put("config", getConfigSummary());

        return builder.withDetails(details).build();
    }

    private Map<String, Object> getConfigSummary() {
        Map<String, Object> configSummary = new HashMap<>();
        configSummary.put("alertEnabled", config.getException().isAlertEnabled());
        configSummary.put("logLevel", config.getException().getLogLevel());
        configSummary.put("slowQueryThreshold", config.getException().getSlowQueryThreshold() + "ms");
        configSummary.put("connectionTimeout", config.getException().getConnectionTimeout() + "ms");
        configSummary.put("queryTimeout", config.getException().getQueryTimeout() + "ms");
        return configSummary;
    }
}
