package cn.tannn.jdevelops.jdectemplate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * 扫描
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-23 10:21
 */
public class ScanPackagesUtils {
    private static final Logger log = LoggerFactory.getLogger(ScanPackagesUtils.class);

    /**
     * 扫描指定路径下的类 （必须要是 spring bean里的容器
     *
     * @param scanPackages 指定路径
     * @return BeanDefinition
     */
    public static Set<BeanDefinition> scanPackages(String[] scanPackages) {

        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        // 扫描指定路径 , true 扫描spring 注解 @Component, @Repository, @Service, and @Controller
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        // rpc的接口调用一般只会出现在这几个spring注解之下, 如何自定义注解的会加大开发复杂度
        provider.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Bean.class));
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(scanPackage);
            if (candidateComponents.isEmpty()) {
                log.warn("consumer[{}]扫描不到可用对象，请检查包路径是否正确", scanPackage);
            } else {
                beanDefinitions.addAll(candidateComponents);
            }
        }
        return beanDefinitions;
    }

    /**
     * 查询 spring bean
     *
     * @param applicationContext ApplicationContext
     * @param beanDefinition     beanDefinition
     * @return 扫描出来的spring bean
     */
    public static Object getBean(ApplicationContext applicationContext, BeanDefinition beanDefinition) {
        Object bean = null;
        String beanName = beanDefinition.getBeanClassName();
        if (applicationContext.containsBean(beanName)) {
            // 如果该名称在上下文中已注册,则使用该名称获取实例
            bean = applicationContext.getBean(beanName);
        } else {
            // 否则,使用 getBeanNamesForType 获取该类型的所有 Bean 名称
            String[] beanNames = applicationContext
                    .getBeanNamesForType(getBeanClassFromDefinition(beanDefinition));
            if (beanNames.length > 0) {
                // 如果存在该类型的 Bean,则使用第一个名称获取实例
                bean = applicationContext.getBean(beanNames[0]);
            }
        }
        return bean;
    }

    /**
     * getBean时没有Class 自己for一个
     *
     * @param beanDefinition BeanDefinition
     * @return Class
     */
    private static Class<?> getBeanClassFromDefinition(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class_not_exists", e);
        }
    }
}
