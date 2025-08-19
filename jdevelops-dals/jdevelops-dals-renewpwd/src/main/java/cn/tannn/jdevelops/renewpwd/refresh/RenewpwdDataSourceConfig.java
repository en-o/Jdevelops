package cn.tannn.jdevelops.renewpwd.refresh;

import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingDataSourceProxy;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.refresh.dataconfig.DataSourceConfigStrategy;
import cn.tannn.jdevelops.renewpwd.refresh.dataconfig.HikariConfigStrategy;
import cn.tannn.jdevelops.renewpwd.util.RenewpwdEnableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * 数据源配置类，使用策略模式处理不同类型的数据源配置
 * 通过 @RefreshScope 实现动态刷新
 * - ⚠ 引入依赖就会强行被注册这个类。
 * 支持SQL异常全局拦截处理
 */
@Configuration(proxyBeanMethods = false)
public class RenewpwdDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(RenewpwdDataSourceConfig.class);

    private final Environment environment;
    private final List<DataSourceConfigStrategy> strategies;

    /**
     * 通过构造函数注入所有策略 Bean
     */
    public RenewpwdDataSourceConfig(Environment environment,
                                    List<DataSourceConfigStrategy> strategies) {
        this.environment = environment;
        this.strategies = strategies;
    }

    /**
     * 数据源 - 添加SQL异常处理代理
     */
    @Bean
    @Primary
    @RefreshScope
    @DependsOn("renewPwdApplicationContextHolder") // 确保依赖
    @ConditionalOnProperty(name = "jdevelops.renewpwd.enabled", havingValue = "true", matchIfMissing = true)
    public DataSource dataSource(DataSourceProperties properties,
                                 RenewpwdProperties renewpwdProperties) {

        DataSource dataSource = properties
                .initializeDataSourceBuilder()
                .build();

        // 使用策略模式处理不同数据源的配置绑定
        applyDataSourceConfiguration(dataSource);
        log.info("SQL Exception Handling DataSource Proxy created successfully");
        if (RenewpwdEnableUtils.isRenewpwdEnabled(environment)) {

            log.info("Creating SQL Exception Handling DataSource Proxy...");
            log.info("Exception Handling Config: enabled={}, alertEnabled={}, logLevel={}",
                    renewpwdProperties.getEnabled(), renewpwdProperties.getException().isAlertEnabled(), renewpwdProperties.getException().getLogLevel());

            // 创建代理数据源，实现全局SQL异常拦截
            SQLExceptionHandlingDataSourceProxy proxyDataSource =
                    new SQLExceptionHandlingDataSourceProxy(dataSource, renewpwdProperties, properties.getDriverClassName());

            return proxyDataSource;
        }
        return dataSource;
    }

    /**
     * 应用数据源配置策略
     */
    private void applyDataSourceConfiguration(DataSource dataSource) {
        String className = dataSource.getClass().getSimpleName();
        boolean configured = false;

        log.debug("Applying configuration for DataSource: {}", className);

        // 遍历所有策略，找到支持当前数据源类型的策略
        for (DataSourceConfigStrategy strategy : strategies) {
            if (Arrays.stream(strategy.getSupportedDataSourceTypes())
                    .anyMatch(type -> type.equalsIgnoreCase(className))) {
                try {
                    strategy.configureDataSource(dataSource, environment);
                    log.info("{} configured successfully by {}",
                            className, strategy.getClass().getSimpleName());
                    configured = true;
                    // 找到匹配的策略就退出
                    break;
                } catch (Exception e) {
                    log.error("Failed to configure {} with {}: {}",
                            className, strategy.getClass().getSimpleName(), e.getMessage(), e);
                }
            }
        }

        // 如果没有找到匹配的策略，使用默认策略
        if (!configured) {
            log.warn("No specific strategy found for: {}, using default HikariConfigStrategy", className);
            try {
                new HikariConfigStrategy().configureDataSource(dataSource, environment);
                log.info("{} configured successfully by default HikariConfigStrategy", className);
            } catch (Exception e) {
                log.error("Failed to configure {} with default HikariConfigStrategy: {}",
                        className, e.getMessage(), e);
            }
        }
    }
}
