package com.detabes.web;

import com.detabes.spring.properties.DataBaseProperties;
import com.detabes.spring.schema.LocalDataSourceLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tn
 * @className startersDetabesWebConfiguration
 * @date 2021-08-02 09:00
 */
@Configuration
public class StartersDetabesWebConfiguration {

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
