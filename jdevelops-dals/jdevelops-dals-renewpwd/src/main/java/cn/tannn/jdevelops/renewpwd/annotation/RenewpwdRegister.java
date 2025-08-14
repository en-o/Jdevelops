package cn.tannn.jdevelops.renewpwd.annotation;

import cn.tannn.jdevelops.renewpwd.DefaultRenewPwdRefresh;
import cn.tannn.jdevelops.renewpwd.proerty.DatabasePwdEnvironmentPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义配置源处理器注册到spring
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/13 16:32
 */
public class RenewpwdRegister implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(RenewpwdRegister.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata
            , BeanDefinitionRegistry registry) {

        // 只有在启用refresh时才注册 tconfig 相关的处理器
        if (isRefreshEnabled(importingClassMetadata, registry)) {
            registerClass(registry, DatabasePwdEnvironmentPostProcessor.class);
            registerClass(registry, DefaultRenewPwdRefresh.class);
            log.info("TConfig is enabled, register property sources processor and value processor");
        }else {
            log.warn("TConfig is disabled, skip registration");
        }
    }


    private boolean isRefreshEnabled(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 从Environment获取配置 (优先级最高)
        Environment environment = getEnvironment(registry);
        if (environment != null) {
            String configValue = environment.getProperty("jdevelops.renewpwd.enabled");
            if (configValue != null) {
                return Boolean.parseBoolean(configValue);
            }
        }
        // 从注解获取配置
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableRenewpwd.class.getName());
        if (attributes != null) {
            return (Boolean) attributes.get("enable");
        }
        // 默认启用
        return false;
    }

    private Environment getEnvironment(BeanDefinitionRegistry registry) {
        if (registry instanceof org.springframework.context.support.GenericApplicationContext) {
            return ((org.springframework.context.support.GenericApplicationContext) registry).getEnvironment();
        }
        return null;
    }


    /**
     * 注册
     */
    private void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        log.info("register property sources processor, register {}", aClass.getName());

        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(x -> aClass.getName().equals(x)).findFirst();

        if (first.isPresent()) {
            log.info("property sources processor already registered");
            return;
        }

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(aClass).getBeanDefinition();
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
    }
}
