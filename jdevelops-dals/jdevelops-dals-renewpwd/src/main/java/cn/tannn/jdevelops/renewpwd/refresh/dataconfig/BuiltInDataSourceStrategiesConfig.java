package cn.tannn.jdevelops.renewpwd.refresh.dataconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuiltInDataSourceStrategiesConfig {

    @Bean
    public DataSourceConfigStrategy hikariConfigStrategy() {
        return new HikariConfigStrategy();
    }

    @Bean
    public DataSourceConfigStrategy druidConfigStrategy() {
        return new DruidConfigStrategy();
    }

    @Bean
    public DataSourceConfigStrategy tomcatConfigStrategy() {
        return new TomcatConfigStrategy();
    }

    @Bean
    public DataSourceConfigStrategy dbcp2ConfigStrategy() {
        return new Dbcp2ConfigStrategy();
    }
}
