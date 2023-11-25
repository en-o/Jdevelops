package cn.jdevelops.data.ddss.config;


import cn.jdevelops.data.ddss.core.DynamicDataSource;
import cn.jdevelops.data.ddss.service.DynamicDatasourceService;
import cn.jdevelops.data.ddss.util.DynamicDataSourceUtil;
import cn.jdevelops.data.ddss.util.DynamicSpringBeanUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

/**
 * 配置多数据源
 *
 * @author tan
 */
@AutoConfiguration
public class DynamicDataSourceConfig {

    /**
     * get dynamic metadata
     * @return DyDatasourceService
     */
    @Bean
    @ConditionalOnMissingBean(DynamicDatasourceService.class)
    public DynamicDatasourceService dyDatasourceService() {
        return new DynamicDatasourceService();
    }

    /**
     * spring boot get bean util
     * @return DySpringBeanUtil
     */
    @Bean
    @ConditionalOnMissingBean(DynamicSpringBeanUtil.class)
    public DynamicSpringBeanUtil dySpringBeanUtil() {
        return new DynamicSpringBeanUtil();
    }


    /**
     * 获取默认数据库的配置
     * @return DataSourceProperties
     */
    @Bean("defDataSourceProperties")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties defDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 加载默认数据源
     *
     * @param defDataSourceProperties DataSourceProperties 👆
     * @return DataSource
     */
    @Bean
    public HikariDataSource defaultDataSource(DataSourceProperties defDataSourceProperties) {
        //默认数据源
        return DynamicDataSourceUtil.buildDataSource(defDataSourceProperties);
    }


    /**
     * 动态数据数据源
     *
     * @param defaultDataSource DataSource
     * @return DynamicDataSource
     */
    @Bean
    @Primary
    @DependsOn({"defaultDataSource"})
    public DynamicDataSource dynamicDataSource(HikariDataSource defaultDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //设置targetDataSources(通过数据库配置获取，首次创建没有数据源,通过后续的 {@link DynamicDataSource#setDataSource})进行动态加载源
        dynamicDataSource.setTargetDataSources(DynamicDataSource.targetDataSources);
        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

}

