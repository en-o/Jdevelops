package cn.tannn.jdevelops.jdectemplate;

import cn.tannn.jdevelops.jdectemplate.config.JdbcTemplateConfig;
import cn.tannn.jdevelops.jdectemplate.proxysql.core.JdbcProxyCreator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JdbcTemplate 配置
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@EnableConfigurationProperties(JdbcTemplateConfig.class)
public class JdbcConfiguration {

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner jdbcDalsRunner(ApplicationContext context
            , JdbcTemplate jdbcTemplate
            , JdbcTemplateConfig jdbcTemplateConfig) {
        return x -> JdbcProxyCreator.jdbcSelectProxy(context
                , jdbcTemplate, jdbcTemplateConfig.getBasePackage());
    }

}
