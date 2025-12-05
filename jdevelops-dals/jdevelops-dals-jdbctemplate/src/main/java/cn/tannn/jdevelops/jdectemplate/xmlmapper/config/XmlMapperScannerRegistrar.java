package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy.XmlMapperProxyFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Set;

/**
 * XML Mapper 扫描注册器
 * <p>在 Bean 定义阶段就注册 Mapper 代理，让 IDE 能识别</p>
 *
 * @author tnnn
 */
public class XmlMapperScannerRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperScannerRegistrar.class);

    private Environment environment;
    private String basePackages;

    public XmlMapperScannerRegistrar(String basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (basePackages == null || basePackages.isEmpty()) {
            LOG.warn("XML Mapper base packages not configured, skipping scan");
            return;
        }

        LOG.info("Scanning XML Mapper interfaces in package: {}", basePackages);

        try {
            // 扫描 @XmlMapper 注解的接口
            Reflections reflections = new Reflections(basePackages);
            Set<Class<?>> mapperInterfaces = reflections.getTypesAnnotatedWith(XmlMapper.class);

            if (mapperInterfaces.isEmpty()) {
                LOG.warn("No XML Mapper interfaces found in package: {}", basePackages);
                return;
            }

            LOG.info("Found {} XML Mapper interface(s)", mapperInterfaces.size());

            // 为每个接口注册 BeanDefinition
            for (Class<?> mapperInterface : mapperInterfaces) {
                if (!mapperInterface.isInterface()) {
                    LOG.warn("@XmlMapper can only be used on interfaces, skipping: {}", mapperInterface.getName());
                    continue;
                }

                registerMapperInterface(registry, mapperInterface);
            }
        } catch (Exception e) {
            LOG.error("Failed to scan XML Mapper interfaces", e);
        }
    }

    /**
     * 注册 Mapper 接口的 BeanDefinition
     */
    private void registerMapperInterface(BeanDefinitionRegistry registry, Class<?> mapperInterface) {
        String beanName = generateBeanName(mapperInterface);

        // 检查是否已注册
        if (registry.containsBeanDefinition(beanName)) {
            LOG.debug("Bean already registered: {}", beanName);
            return;
        }

        // 创建 BeanDefinition，使用 FactoryBean 模式
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(XmlMapperFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(mapperInterface);
        beanDefinition.setAutowireCandidate(true);

        // 注册到容器
        registry.registerBeanDefinition(beanName, beanDefinition);

        LOG.info("Registered XML Mapper: {} -> {}", mapperInterface.getSimpleName(), beanName);
    }

    /**
     * 生成 Bean 名称
     */
    private String generateBeanName(Class<?> mapperInterface) {
        String simpleName = mapperInterface.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不需要在这里做任何事情
    }
}
