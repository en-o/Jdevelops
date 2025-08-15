package cn.tannn.jdevelops.renewpwd.util;

import cn.tannn.jdevelops.renewpwd.annotation.EnableRenewpwd;
import cn.tannn.jdevelops.renewpwd.pojo.PasswordPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

import static cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant.CONFIG_KEY;

public class RenewpwdEnableUtils {
    private static final Logger log = LoggerFactory.getLogger(RenewpwdEnableUtils.class);

    /**
     * 通过 ApplicationContext 判断是否启用 renewpwd
     * 优先全局配置，再查 PasswordPool bean
     */
    public static boolean isRenewpwdEnabled(ApplicationContext context) {
        // 1. 优先全局配置
        String configValue = context.getEnvironment().getProperty(CONFIG_KEY);
        if (configValue != null) {
            return Boolean.parseBoolean(configValue);
        }
        // 2. PasswordPool bean
        try {
            PasswordPool config = context.getBean(PasswordPool.class);
            return config.getEnabled();
        } catch (Exception e) {
            log.warn("PasswordPool bean not found or not enabled, defaulting to false,{}", e.getMessage());
        }
        // 3. 注解配置 从配置类上获取注解信息
        return getAnnotationEnabledFromConfigClasses(context);
    }

    /**
     * 通过 Environment 和注解元数据判断是否启用 renewpwd
     * 优先全局配置，再查注解
     */
    public static boolean isRenewpwdEnabled(Environment environment, AnnotationMetadata metadata) {
        if (environment != null) {
            String configValue = environment.getProperty(CONFIG_KEY);
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


    private static boolean getAnnotationEnabledFromConfigClasses(ApplicationContext context) {
        // 获取所有配置类
        String[] configBeanNames = context.getBeanNamesForAnnotation(org.springframework.context.annotation.Configuration.class);

        for (String beanName : configBeanNames) {
            try {
                Object configBean = context.getBean(beanName);
                Class<?> configClass = configBean.getClass();

                // 检查是否有 @EnableRenewpwd 注解
                EnableRenewpwd annotation = configClass.getAnnotation(EnableRenewpwd.class);
                if (annotation != null) {
                    log.info("Found @EnableRenewpwd on configuration class: {}", configClass.getName());
                    return annotation.enable(); // 假设注解有 enable 属性
                }
            } catch (Exception e) {
                log.debug("Error checking configuration class {}: {}", beanName, e.getMessage());
            }
        }

        return false;
    }
}
