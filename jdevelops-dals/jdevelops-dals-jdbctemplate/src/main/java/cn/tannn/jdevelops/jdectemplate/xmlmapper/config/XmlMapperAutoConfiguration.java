package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.jdectemplate.config.JdbcTemplateConfig;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperScanner;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * XML Mapper 自动配置
 * <p> 默认读取 资源文件下 resources/jmapper/下的文件 </p>
 * <p> 默认启用 </p>
 *
 * @author tnnn
 */
@AutoConfiguration
@ConditionalOnClass({JdbcTemplate.class, DataSource.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
@ConditionalOnProperty(
        prefix = "jdevelops.jdbc.xmlmapper",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class XmlMapperAutoConfiguration {


    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperAutoConfiguration.class);


    @Bean
    @ConditionalOnProperty(prefix = "jdevelops.jdbc.xmlmapper", name = "base-packages")
    public ApplicationRunner xmlMapperScannerRunner(XmlMapperRegistry xmlMapperRegistry,
                                                    JdbcTemplateConfig jdbcTemplateConfig) {
        return args -> {
            if (jdbcTemplateConfig == null) {
                throw new IllegalStateException("JdbcTemplateConfig is not available");
            }

            String basePackages = jdbcTemplateConfig.getXmlmapper().getBasePackages();
            if (!StringUtils.hasText(basePackages)) {
                basePackages = jdbcTemplateConfig.getBasePackage();
            }

            LOG.info("Initializing XML Mapper Scanner for package: {}", basePackages);
             new XmlMapperScanner(basePackages, xmlMapperRegistry);
        };

    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn({"dataSource"})
    public XmlMapperRegistry xmlMapperRegistry(JdbcTemplateConfig jdbcTemplateConfig
            , JdbcTemplate jdbcTemplate) throws Exception {

        LOG.info("Initializing XML Mapper Registry");

        if (jdbcTemplate == null) {
            throw new IllegalStateException(
                    "JdbcTemplate is not available. Please configure a DataSource or disable xmlmapper.");
        }

        if (jdbcTemplateConfig == null || jdbcTemplateConfig.getXmlmapper() == null) {
            throw new IllegalStateException("JdbcTemplateConfig is not properly configured.");
        }

        LOG.debug("JdbcTemplate: {}", jdbcTemplate);
        LOG.debug("Config: {}", jdbcTemplateConfig);

        XmlMapperRegistry registry = new XmlMapperRegistry(jdbcTemplate);

        // 扫描并注册 XML Mapper 文件
        LOG.info("Scanning XML mappers from: {}", jdbcTemplateConfig.getXmlmapper().getLocations());
        registry.scanAndRegisterMappers(jdbcTemplateConfig.getXmlmapper().getLocations());

        LOG.info("XML Mapper Registry initialized, registered mappers: {}",
                registry.getRegisteredMappers());

        return registry;
    }
}
