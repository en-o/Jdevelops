package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.jdectemplate.exception.JdbcTemplateException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 注解扫描器
 */
public class AnnotationScanner {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyCreator.class);

    /**
     * 查找所有带有指定注解的类。
     *
     * @param basePackage       要扫描的基础包
     * @param annotationClasses 要查找的注解类列表
     * @return 包含注解的类集合
     */
    public static Set<Class<?>> getClassesAnnotatedWithAny(String basePackage
            , List<Class<? extends Annotation>> annotationClasses) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> result = new HashSet<>();
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            result.addAll(reflections.getTypesAnnotatedWith(annotationClass));
        }
        return result;
    }


    /**
     * 查找所有带有指定注解的类。
     * <p> 默认扫描{@link Controller} {@link Service} {@link Component} </p>
     *
     * @param basePackage 要扫描的基础包
     * @return 包含注解的类集合
     */
    public static Set<Class<?>> defClassesAnnotatedWithAny(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> result = new HashSet<>();
        List<Class<? extends Annotation>> classes = List.of(Controller.class, Service.class, Component.class);
        for (Class<? extends Annotation> annotationClass : classes) {
            result.addAll(reflections.getTypesAnnotatedWith(annotationClass));
        }
        return result;
    }

    /**
     * 查找所有带有指定注解的字段。
     *
     * @param basePackage       要扫描的基础包
     * @param annotationClasses 要查找的注解类列表
     * @return 包含注解的字段集合
     */
    public static Set<Field> getFieldsAnnotatedWithAny(String basePackage, List<Class<? extends Annotation>> annotationClasses) {
        Reflections reflections = new Reflections(basePackage);
        Set<Field> result = new HashSet<>();

        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            result.addAll(reflections.getFieldsAnnotatedWith(annotationClass));
        }

        return result;
    }


    /**
     * 获取自定注解的属性字段
     *
     * @param aClass Class
     * @return Field
     */
    public static List<Field> findAnnotatedField(Class<?> aClass, Class<? extends Annotation> annotation) {
        ArrayList<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotation)) {
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }


    /**
     * 扫描指定路径下的类 （必须要是 spring bean里的容器
     *
     * @param scanPackages 指定路径
     * @return BeanDefinition
     */
    public static Set<BeanDefinition> scanPackages(List<String> scanPackages) {

        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        // 扫描指定路径 , true 扫描spring 注解 @Component, @Repository, @Service, and @Controller
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        // 调用一般只会出现在这几个spring注解之下, 如何自定义注解的会加大开发复杂度
        provider.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Bean.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));
        try {
            provider.addIncludeFilter(new AnnotationTypeFilter(org.springframework.boot.test.context.SpringBootTest.class));
            provider.addIncludeFilter(new AnnotationTypeFilter(org.springframework.boot.test.context.TestConfiguration.class));
        } catch (Exception e) {
            LOG.warn("no import spring-boot-starter-test");
        }
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(scanPackage);
            if (candidateComponents.isEmpty()) {
                LOG.warn("consumer[{}]扫描不到可用对象，请检查包路径是否正确", scanPackage);
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
        try {
            String beanName = beanDefinition.getBeanClassName();
            // beanDefinition.getSource().toString().contains("test-classes")
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
        } catch (Exception e) {
            LOG.error("jdbc proxy scanner getBean error", e);
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
            throw new JdbcTemplateException(e, 12, "class_not_exists");
        }
    }
}
