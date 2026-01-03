package cn.tannn.jdevelops.jdectemplate.xmlmapper.proxy;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * XML Mapper 代理创建器
 * <p>扫描并为 @XmlMapper 注解的接口创建代理对象</p>
 *
 * @author tnnn
 */
public class XmlMapperScanner {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperScanner.class);

    /**
     * 扫描并注册 XML Mapper 代理
     *
     * @param context ApplicationContext
     * @param registry XmlMapperRegistry
     * @param basePackage 扫描包路径
     */
    public static void createXmlMapperProxies(ApplicationContext context,
                                               XmlMapperRegistry registry,
                                               String basePackage) {

        if (basePackage == null || basePackage.isEmpty()) {
            LOG.warn("XML Mapper 功能未启用，因为没有设置包扫描路径");
            return;
        }

        LOG.info("Scanning XML Mapper interfaces in package: {}", basePackage);

        // 扫描 @XmlMapper 注解的接口
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> mapperInterfaces = reflections.getTypesAnnotatedWith(XmlMapper.class);

        if (mapperInterfaces.isEmpty()) {
            LOG.warn("No XML Mapper interfaces found in package: {}", basePackage);
            return;
        }

        LOG.info("Found {} XML Mapper interface(s)", mapperInterfaces.size());

        // 获取 BeanDefinitionRegistry
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) context.getAutowireCapableBeanFactory();

        // 为每个接口创建代理并注册到 Spring 容器
        for (Class<?> mapperInterface : mapperInterfaces) {
            if (!mapperInterface.isInterface()) {
                LOG.warn("@XmlMapper can only be used on interfaces, skipping: {}", mapperInterface.getName());
                continue;
            }

            registerMapperProxy(beanRegistry, mapperInterface, registry);
        }
    }

    /**
     * 注册 Mapper 代理到 Spring 容器
     */
    private static void registerMapperProxy(BeanDefinitionRegistry registry,
                                             Class<?> mapperInterface,
                                             XmlMapperRegistry xmlMapperRegistry) {
        try {
            String beanName = generateBeanName(mapperInterface);

            // 检查是否已注册
            if (registry.containsBeanDefinition(beanName)) {
                LOG.debug("Bean already registered: {}", beanName);
                return;
            }

            // 创建代理对象
            Object proxy = XmlMapperProxyFactory.createProxy(mapperInterface, xmlMapperRegistry);

            // 创建 BeanDefinition
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(mapperInterface);
            beanDefinition.setInstanceSupplier(() -> proxy);
            beanDefinition.setAutowireCandidate(true);

            // 注册到容器
            registry.registerBeanDefinition(beanName, beanDefinition);

            LOG.info("Registered XML Mapper: {} -> {}", mapperInterface.getSimpleName(), beanName);
        } catch (Exception e) {
            LOG.error("Failed to register XML Mapper: {}", mapperInterface.getName(), e);
        }
    }

    /**
     * 生成 Bean 名称
     */
    private static String generateBeanName(Class<?> mapperInterface) {
        String simpleName = mapperInterface.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
