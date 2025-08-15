package cn.tannn.jdevelops.renewpwd.refresh;

import cn.tannn.jdevelops.renewpwd.refresh.dataconfig.DataSourceConfigStrategy;
import cn.tannn.jdevelops.renewpwd.refresh.dataconfig.HikariConfigStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * 数据源配置类，使用策略模式处理不同类型的数据源配置
 * 通过 @RefreshScope 实现动态刷新
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
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



    @Bean
    @Primary
    @RefreshScope
    @ConditionalOnBean(DataSourceConfigStrategy.class)
    public DataSource dataSource(DataSourceProperties properties) {

        DataSource dataSource = properties
                .initializeDataSourceBuilder()
                .build();

        // 使用策略模式处理不同数据源的配置绑定
        applyDataSourceConfiguration(dataSource);

        return dataSource;
    }

    private void applyDataSourceConfiguration(DataSource dataSource) {
        String className = dataSource.getClass().getSimpleName();
        boolean configured = false;

        // 遍历所有策略，找到支持当前数据源类型的策略
        for (DataSourceConfigStrategy strategy : strategies) {
            if (Arrays.stream( strategy.getSupportedDataSourceTypes()).anyMatch(type -> type.equalsIgnoreCase(className))) {
                try {
                    strategy.configureDataSource(dataSource, environment);
                    log.info(className + " configured successfully by " +
                            strategy.getClass().getSimpleName());
                    configured = true;
                    // 找到匹配的策略就退出
                    break;
                } catch (Exception e) {
                    log.error("Failed to configure {} with {}: {}", className, strategy.getClass().getSimpleName(), e.getMessage());
                }
            }
        }

        if (!configured) {
            log.warn("No specific strategy found for: {}, using default HikariConfigStrategy", className);
            try {
                new HikariConfigStrategy().configureDataSource(dataSource, environment);
                log.info(className + " configured successfully by default HikariConfigStrategy");
            } catch (Exception e) {
                log.error("Failed to configure {} with default HikariConfigStrategy: {}", className, e.getMessage());
            }
        }
    }
}
