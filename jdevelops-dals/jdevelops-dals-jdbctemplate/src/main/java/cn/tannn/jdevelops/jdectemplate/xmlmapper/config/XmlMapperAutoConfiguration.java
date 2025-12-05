package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.jdectemplate.config.JdbcTemplateConfig;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperScanner;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
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

    /**
     * 使用 ObjectProvider 实现延迟加载，避免过早触发 Bean 创建
     */
    @Bean
    @ConditionalOnMissingBean
    public XmlMapperRegistry xmlMapperRegistry(
            ObjectProvider<JdbcTemplate> jdbcTemplateProvider,
            ObjectProvider<JdbcTemplateConfig> configProvider) throws Exception {

        LOG.info("Initializing XML Mapper Registry");

        // 延迟获取 Bean，此时配置已经完全加载
        JdbcTemplate jdbcTemplate = jdbcTemplateProvider.getIfAvailable();
        JdbcTemplateConfig config = configProvider.getIfAvailable();

        if (jdbcTemplate == null) {
            throw new IllegalStateException(
                    "JdbcTemplate is not available. Please configure a DataSource or disable xmlmapper with jdevelops.jdbc.xmlmapper.enabled=false");
        }

        if (config == null || config.getXmlmapper() == null) {
            throw new IllegalStateException(
                    "JdbcTemplateConfig is not properly configured.");
        }

        LOG.debug("JdbcTemplate DataSource: {}", jdbcTemplate.getDataSource());
        LOG.debug("Config locations: {}", config.getXmlmapper().getLocations());

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
    public XmlMapperScanner xmlMapperScanner(
            ObjectProvider<JdbcTemplateConfig> configProvider,
            XmlMapperRegistry registry) {

        JdbcTemplateConfig config = configProvider.getIfAvailable();
        if (config == null) {
            throw new IllegalStateException("JdbcTemplateConfig is not available");
        }

        // 优先使用 xmlmapper.base-packages，如果没有配置则使用 jdbc.base-package
        String basePackages = config.getXmlmapper().getBasePackages();
        if (!StringUtils.hasText(basePackages)) {
            basePackages = config.getBasePackage();
        }

        LOG.info("Initializing XML Mapper Scanner for package: {}", basePackages);
        return new XmlMapperScanner(basePackages, registry);
    }
}
