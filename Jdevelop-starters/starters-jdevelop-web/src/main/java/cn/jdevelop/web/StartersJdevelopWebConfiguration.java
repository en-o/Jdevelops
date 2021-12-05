package cn.jdevelop.web;

import cn.jdevelop.spring.properties.DataBaseProperties;
import cn.jdevelop.spring.schema.LocalDataSourceLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tn
 * @date 2021-08-02 09:00
 */
@Configuration
public class StartersJdevelopWebConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = DataBaseProperties.class)
    public DataBaseProperties dataBaseProperties() {
        return new DataBaseProperties();
    }

    @Bean
    @ConditionalOnMissingBean(value = LocalDataSourceLoader.class)
    public LocalDataSourceLoader localDataSourceLoader() {
        return new LocalDataSourceLoader();
    }
}
