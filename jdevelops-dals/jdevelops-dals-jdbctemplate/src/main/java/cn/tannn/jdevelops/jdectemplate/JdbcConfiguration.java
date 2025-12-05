package cn.tannn.jdevelops.jdectemplate;

import cn.tannn.jdevelops.jdectemplate.config.JdbcTemplateConfig;
import cn.tannn.jdevelops.jdectemplate.proxysql.core.JdbcProxyCreator;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.config.XmlMapperAutoConfiguration;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperScanner;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * JdbcTemplate 配置
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@Import(XmlMapperAutoConfiguration.class)
public class JdbcConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcConfiguration.class);

    @Bean
    public JdbcTemplateConfig jdbcTemplateConfig() {
        return new JdbcTemplateConfig();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner jdbcDalsRunner(ApplicationContext context
            , JdbcTemplate jdbcTemplate
            , JdbcTemplateConfig jdbcTemplateConfig) {
        return x -> JdbcProxyCreator.jdbcSelectProxy(context
                , jdbcTemplate, jdbcTemplateConfig.getBasePackage());
    }

}
