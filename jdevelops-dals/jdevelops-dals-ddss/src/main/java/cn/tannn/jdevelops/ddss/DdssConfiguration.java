package cn.tannn.jdevelops.ddss;

import cn.tannn.jdevelops.ddss.config.DynamicDataSourceProperties;
import cn.tannn.jdevelops.ddss.core.DynamicDataSource;
import cn.tannn.jdevelops.ddss.core.DynamicDataSourceAspect;
import cn.tannn.jdevelops.ddss.service.DynamicDatasourceService;
import cn.tannn.jdevelops.ddss.util.DynamicDataSourceUtil;
import cn.tannn.jdevelops.ddss.util.DynamicSpringBeanUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

/**
 * 注入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 下午2:03
 */
public class DdssConfiguration {

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


    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }

    @Bean
    public DynamicDataSourceProperties dynamicDataSourceProperties(){
        return new DynamicDataSourceProperties();
    }

}
