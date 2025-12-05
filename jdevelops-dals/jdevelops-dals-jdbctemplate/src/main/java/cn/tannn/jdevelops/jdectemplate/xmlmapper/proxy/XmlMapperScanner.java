package cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Set;

/**
 * XML Mapper 扫描器
 */
public class XmlMapperScanner implements BeanDefinitionRegistryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperScanner.class);

    private final String basePackage;

    // 不再在构造函数中注入，而是延迟获取
    private ConfigurableListableBeanFactory beanFactory;

    public XmlMapperScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanRegistry) throws BeansException {
        LOG.info("Scanning XML Mapper interfaces in package: {}", basePackage);

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> mapperInterfaces = reflections.getTypesAnnotatedWith(XmlMapper.class);

        if (mapperInterfaces.isEmpty()) {
            LOG.warn("No XML Mapper interfaces found in package: {}", basePackage);
            return;
        }

        LOG.info("Found {} XML Mapper interface(s)", mapperInterfaces.size());

        for (Class<?> mapperInterface : mapperInterfaces) {
            if (!mapperInterface.isInterface()) {
                LOG.warn("@XmlMapper can only be used on interfaces, skipping: {}", mapperInterface.getName());
                continue;
            }

            registerMapperProxy(beanRegistry, mapperInterface);
        }
    }

    private void registerMapperProxy(BeanDefinitionRegistry registry, Class<?> mapperInterface) {
        try {
            String beanName = generateBeanName(mapperInterface);

            if (registry.containsBeanDefinition(beanName)) {
                LOG.debug("Bean already registered: {}", beanName);
                return;
            }

            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(mapperInterface);

            // 延迟获取 XmlMapperRegistry，在真正创建代理时才获取
            beanDefinition.setInstanceSupplier(() -> {
                XmlMapperRegistry xmlMapperRegistry = beanFactory.getBean(XmlMapperRegistry.class);
                return XmlMapperProxyFactory.createProxy(mapperInterface, xmlMapperRegistry);
            });

            beanDefinition.setAutowireCandidate(true);
            // 设置为延迟加载
            beanDefinition.setLazyInit(true);

            registry.registerBeanDefinition(beanName, beanDefinition);

            LOG.info("Registered XML Mapper: {} -> {}", mapperInterface.getSimpleName(), beanName);
        } catch (Exception e) {
            LOG.error("Failed to register XML Mapper: {}", mapperInterface.getName(), e);
        }
    }

    private String generateBeanName(Class<?> mapperInterface) {
        String simpleName = mapperInterface.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 保存 BeanFactory 引用，用于后续延迟获取 XmlMapperRegistry
        this.beanFactory = beanFactory;
    }
}
