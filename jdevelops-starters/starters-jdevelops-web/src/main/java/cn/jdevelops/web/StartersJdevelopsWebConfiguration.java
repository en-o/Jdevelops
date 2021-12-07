package cn.jdevelops.web;

import cn.jdevelops.spring.properties.DataBaseProperties;
import cn.jdevelops.spring.schema.LocalDataSourceLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tn
 * @date 2021-08-02 09:00
 */
@Configuration
public class StartersJdevelopsWebConfiguration {

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
