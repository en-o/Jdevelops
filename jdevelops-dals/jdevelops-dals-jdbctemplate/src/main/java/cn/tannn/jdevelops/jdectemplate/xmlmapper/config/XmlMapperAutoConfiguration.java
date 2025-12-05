package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.jdectemplate.config.JdbcTemplateConfig;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperScanner;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * XML Mapper 自动配置
 * <p> 默认读取 资源文件下 resources/jmapper/下的文件 </p>
 * <p> 默认启用 </p>
 *
 * @author tnnn
 */
@ConditionalOnProperty(prefix = "jdevelops.jdbc.xmlmapper", name = "enabled", havingValue = "true", matchIfMissing = true)
public class XmlMapperAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperAutoConfiguration.class);

    @Bean
    public XmlMapperRegistry xmlMapperRegistry(JdbcTemplate jdbcTemplate, JdbcTemplateConfig config) throws Exception {
        LOG.info("Initializing XML Mapper Registry");
        XmlMapperRegistry registry = new XmlMapperRegistry(jdbcTemplate);

        // 扫描并注册 XML Mapper 文件
        LOG.info("Scanning XML mappers from: {}", config.getXmlmapper().getLocations());
        registry.scanAndRegisterMappers(config.getXmlmapper().getLocations());

        LOG.info("XML Mapper Registry initialized, registered mappers: {}",
                registry.getRegisteredMappers());

        return registry;
    }

    /**
     * XML Mapper 接口扫描器
     * <p>扫描 @XmlMapper 注解的接口并创建代理</p>
     * <p>只有配置了 basePackages 时才启用</p>
     */
    @Bean
    @ConditionalOnProperty(prefix = "jdevelops.jdbc.xmlmapper", name = "base-packages")
    public XmlMapperScanner xmlMapperScanner(JdbcTemplateConfig config, XmlMapperRegistry registry) {
        // 优先使用 xmlmapper.base-packages，如果没有配置则使用 jdbc.base-package
        String basePackages = config.getXmlmapper().getBasePackages();
        if (!StringUtils.hasText(basePackages)) {
            basePackages = config.getBasePackage();
        }

        LOG.info("Initializing XML Mapper Scanner for package: {}", basePackages);
        return new XmlMapperScanner(basePackages, registry);
    }
}
