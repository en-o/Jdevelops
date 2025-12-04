package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * XML Mapper 自动配置
 * <p> 默认读取 资源文件下 resources/jmapper/下的文件 </p>
 * @author tnnn
 */
@Configuration
@ConditionalOnProperty(prefix = "jdevelops.jdbctemplate.xmlmapper", name = "enabled", havingValue = "true")
public class XmlMapperAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperAutoConfiguration.class);

    @Value("${jdevelops.jdbctemplate.xmlmapper.locations:classpath*:jmapper/**/*.xml}")
    private String mapperLocations;

    @Bean
    public XmlMapperRegistry xmlMapperRegistry(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Initializing XML Mapper Registry");
        XmlMapperRegistry registry = new XmlMapperRegistry(jdbcTemplate);

        // 扫描并注册 XML Mapper
        LOG.info("Scanning XML mappers from: {}", mapperLocations);
        registry.scanAndRegisterMappers(mapperLocations);

        LOG.info("XML Mapper Registry initialized, registered mappers: {}",
                registry.getRegisteredMappers());

        return registry;
    }
}
