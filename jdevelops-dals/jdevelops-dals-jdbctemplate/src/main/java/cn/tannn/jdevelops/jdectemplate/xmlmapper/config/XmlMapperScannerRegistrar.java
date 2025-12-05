package cn.tannn.jdevelops.jdectemplate.xmlmapper.config;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.XmlMapperScan;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * XML Mapper 扫描注册器
 * <p>通过 @XmlMapperScan 注解触发，在 Bean 定义阶段就注册 Mapper 代理</p>
 *
 * @author tnnn
 */
public class XmlMapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperScannerRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 @XmlMapperScan 注解的属性
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(XmlMapperScan.class.getName()));

        if (annoAttrs == null) {
            LOG.warn("@XmlMapperScan annotation not found");
            return;
        }

        // 获取扫描包路径
        Set<String> basePackages = new LinkedHashSet<>();

        // 从 value 或 basePackages 属性获取
        String[] packages = annoAttrs.getStringArray("basePackages");
        if (packages.length == 0) {
            packages = annoAttrs.getStringArray("value");
        }
        basePackages.addAll(Arrays.asList(packages));

        // 从 basePackageClasses 获取包路径
        Class<?>[] basePackageClasses = annoAttrs.getClassArray("basePackageClasses");
        for (Class<?> clazz : basePackageClasses) {
            basePackages.add(clazz.getPackage().getName());
        }

        // 如果没有指定包路径，使用注解所在类的包
        if (basePackages.isEmpty()) {
            try {
                String className = importingClassMetadata.getClassName();
                Class<?> clazz = Class.forName(className);
                basePackages.add(clazz.getPackage().getName());
            } catch (ClassNotFoundException e) {
                LOG.error("Failed to get package from importing class", e);
            }
        }

        // 扫描并注册 Mapper
        for (String basePackage : basePackages) {
            scanAndRegister(registry, basePackage);
        }
    }

    /**
     * 扫描指定包并注册 Mapper 接口
     */
    private void scanAndRegister(BeanDefinitionRegistry registry, String basePackage) {
        if (basePackage == null || basePackage.isEmpty()) {
            LOG.warn("XML Mapper base package is empty, skipping scan");
            return;
        }

        LOG.info("Scanning XML Mapper interfaces in package: {}", basePackage);

        try {
            // 扫描 @XmlMapper 注解的接口
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> mapperInterfaces = reflections.getTypesAnnotatedWith(XmlMapper.class);

            if (mapperInterfaces.isEmpty()) {
                LOG.warn("No XML Mapper interfaces found in package: {}", basePackage);
                return;
            }

            LOG.info("Found {} XML Mapper interface(s) in package: {}", mapperInterfaces.size(), basePackage);

            // 为每个接口注册 BeanDefinition
            for (Class<?> mapperInterface : mapperInterfaces) {
                if (!mapperInterface.isInterface()) {
                    LOG.warn("@XmlMapper can only be used on interfaces, skipping: {}", mapperInterface.getName());
                    continue;
                }

                registerMapperInterface(registry, mapperInterface);
            }
        } catch (Exception e) {
            LOG.error("Failed to scan XML Mapper interfaces in package: " + basePackage, e);
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
}
