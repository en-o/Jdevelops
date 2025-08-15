package cn.tannn.jdevelops.renewpwd.annotation;

import cn.tannn.jdevelops.renewpwd.DefaultRenewPwdRefresh;
import cn.tannn.jdevelops.renewpwd.proerty.DatabasePwdEnvironmentPostProcessor;
import cn.tannn.jdevelops.renewpwd.refresh.dataconfig.BuiltInDataSourceStrategiesConfig;
import cn.tannn.jdevelops.renewpwd.util.RenewpwdEnableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;


/**
 * 自定义配置源处理器注册到spring
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/13 16:32
 */
public class RenewpwdRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RenewpwdRegister.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata
            , BeanDefinitionRegistry registry) {

        // 只有在启用refresh时才注册 tconfig 相关的处理器
        if (RenewpwdEnableUtils.isRenewpwdEnabled(environment, importingClassMetadata)) {
            registerClass(registry, DatabasePwdEnvironmentPostProcessor.class);
            registerClass(registry, DefaultRenewPwdRefresh.class);
            registerClass(registry, BuiltInDataSourceStrategiesConfig.class);
            log.info("TConfig is enabled, register property sources processor and value processor");
        } else {
            log.warn("TConfig is disabled, skip registration");
        }
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

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // 确保在自动配置之后执行
    }
}
