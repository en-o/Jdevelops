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
 * <p>扫描指定包下所有 @XmlMapper 注解的接口</p>
 * <p>为每个接口创建代理对象并注册到 Spring 容器</p>
 *
 * @author tnnn
 */
public class XmlMapperScanner implements BeanDefinitionRegistryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperScanner.class);

    /**
     * 要扫描的基础包路径
     */
    private final String basePackage;

    // 不再在构造函数中注入，而是延迟获取
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * XML Mapper 注册器
     */
    private final XmlMapperRegistry registry;

    public XmlMapperScanner(String basePackage, XmlMapperRegistry registry) {
        this.basePackage = basePackage;
        this.registry = registry;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanRegistry) throws BeansException {
        LOG.info("Scanning XML Mapper interfaces in package: {}", basePackage);

        // 扫描 @XmlMapper 注解的接口
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> mapperInterfaces = reflections.getTypesAnnotatedWith(XmlMapper.class);

        if (mapperInterfaces.isEmpty()) {
            LOG.warn("No XML Mapper interfaces found in package: {}", basePackage);
            return;
        }

        LOG.info("Found {} XML Mapper interface(s)", mapperInterfaces.size());

        // 为每个接口创建代理并注册到 Spring 容器
        for (Class<?> mapperInterface : mapperInterfaces) {
            if (!mapperInterface.isInterface()) {
                LOG.warn("@XmlMapper can only be used on interfaces, skipping: {}", mapperInterface.getName());
                continue;
            }

            registerMapperProxy(beanRegistry, mapperInterface);
        }
    }

    /**
     * 注册 Mapper 代理到 Spring 容器
     */
    private void registerMapperProxy(BeanDefinitionRegistry registry, Class<?> mapperInterface) {
        try {
            String beanName = generateBeanName(mapperInterface);

            // 检查是否已注册
            if (registry.containsBeanDefinition(beanName)) {
                LOG.debug("Bean already registered: {}", beanName);
                return;
            }

            // 创建 BeanDefinition
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(mapperInterface);
            beanDefinition.setAutowireCandidate(true);

            beanDefinition.setInstanceSupplier(() -> XmlMapperProxyFactory.createProxy(mapperInterface, this.registry));

//
//            // 延迟获取 XmlMapperRegistry，在真正创建代理时才获取
//            beanDefinition.setInstanceSupplier(() -> {
//                XmlMapperRegistry xmlMapperRegistry = beanFactory.getBean(XmlMapperRegistry.class);
//
//                LOG.debug("Creating proxy for {} with registry containing {} mappers",
//                        mapperInterface.getSimpleName(),
//                        xmlMapperRegistry.getRegisteredMappers().size());
//
//                return XmlMapperProxyFactory.createProxy(mapperInterface, xmlMapperRegistry);
//            });
//            beanDefinition.setAutowireCandidate(true);

            // 注册到容器
            registry.registerBeanDefinition(beanName, beanDefinition);

            LOG.info("Registered XML Mapper: {} -> {}", mapperInterface.getSimpleName(), beanName);
        } catch (Exception e) {
            LOG.error("Failed to register XML Mapper: {}", mapperInterface.getName(), e);
        }
    }

    /**
     * 生成 Bean 名称
     * <p>使用接口简单名称的首字母小写作为 Bean 名称</p>
     */
    private String generateBeanName(Class<?> mapperInterface) {
        String simpleName = mapperInterface.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

        // 在这里确保 XmlMapperRegistry 已经初始化
        try {
            XmlMapperRegistry registry = beanFactory.getBean(XmlMapperRegistry.class);
            LOG.info("XmlMapperRegistry is ready with {} registered mappers",
                    registry.getRegisteredMappers().size());
        } catch (Exception e) {
            LOG.warn("XmlMapperRegistry not yet available: {}", e.getMessage());
        }
    }
}
