package cn.tannn.jdevelops.renewpwd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;

import javax.sql.DataSource;
import java.util.*;

/**
 * 默认配置刷新处理器实现
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
@Component
public class DefaultConfigsRefreshHandler implements ConfigsRefreshHandler, ApplicationContextAware {


    private static final Logger log = LoggerFactory.getLogger(DefaultConfigsRefreshHandler.class);

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }



    @Override
    public void refreshDataSourceConfig() {
        log.info("[renewpwd] 开始刷新数据源配置");
        try {
            // 1. 重新绑定数据源配置属性
            rebindDataSourceProperties();
            // 2. 重建数据源 Bean
            refreshDataSourceBeans();
            log.info("[renewpwd] 数据源配置刷新完成");
        } catch (Exception e) {
            log.error("[renewpwd] 数据源配置刷新失败", e);
        }
    }

    /**
     * 重新绑定数据源配置属性
     */
    private void rebindDataSourceProperties() {
        try {
            ConfigurationPropertiesBindingPostProcessor bindingPostProcessor =
                    applicationContext.getBean(ConfigurationPropertiesBindingPostProcessor.class);

            Map<String, Object> dataSourceBeans = applicationContext.getBeansWithAnnotation(
                    org.springframework.boot.context.properties.ConfigurationProperties.class);

            for (Map.Entry<String, Object> entry : dataSourceBeans.entrySet()) {
                String beanName = entry.getKey();
                Object bean = entry.getValue();

                // 检查是否是数据源配置 Bean
                org.springframework.boot.context.properties.ConfigurationProperties annotation =
                        bean.getClass().getAnnotation(org.springframework.boot.context.properties.ConfigurationProperties.class);

                if (annotation != null && annotation.prefix().startsWith("spring.datasource")) {
                    bindingPostProcessor.postProcessBeforeInitialization(bean, beanName);
                    log.info("[renewpwd] 重新绑定数据源配置 Bean: {}", beanName);
                }
            }
        } catch (Exception e) {
            log.warn("[renewpwd] 重新绑定数据源配置失败", e);
        }
    }

    /**
     * 重建数据源相关 Bean
     */
    private void refreshDataSourceBeans() {
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        // 获取所有数据源 Bean
        String[] dataSourceBeans = applicationContext.getBeanNamesForType(DataSource.class);

        for (String beanName : dataSourceBeans) {
            try {
                if (beanFactory.containsSingleton(beanName)) {
                    // 关闭旧的数据源连接
                    closeDataSourceGracefully(beanName);

                    // 销毁 Bean，让 Spring 重新创建
                    beanFactory.destroySingleton(beanName);

                    log.info("[renewpwd] 标记数据源 Bean 需要重新创建: {}", beanName);
                }
            } catch (Exception e) {
                log.warn("[renewpwd] 刷新数据源 Bean 失败: {}", beanName, e);
            }
        }
        Set<String> targetBeanTypes = new HashSet<>();
        targetBeanTypes.add("javax.sql.DataSource");
        targetBeanTypes.add("org.springframework.jdbc.core.JdbcTemplate");
        // 刷新各种数据相关 Bean
        refreshBeansByClassName(beanFactory, "org.springframework.jdbc.core.JdbcTemplate", "JdbcTemplate");
        refreshBeansByClassName(beanFactory, "org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate", "NamedParameterJdbcTemplate");
        refreshBeansByClassName(beanFactory, "org.mybatis.spring.SqlSessionTemplate", "SqlSessionTemplate");

    }


    /**
     * 动态刷新指定类型的 Bean
     * @param beanFactory Bean 工厂
     * @param className 要刷新的类名
     * @param displayName 用于日志显示的名称
     */
    private void refreshBeansByClassName(DefaultListableBeanFactory beanFactory, String className, String displayName) {
        try {
            Class<?> clazz = Class.forName(className);
            String[] beanNames = applicationContext.getBeanNamesForType(clazz);

            for (String beanName : beanNames) {
                try {
                    if (beanFactory.containsSingleton(beanName)) {
                        beanFactory.destroySingleton(beanName);
                        log.info("[renewpwd] 标记 {} Bean 需要重新创建: {}", displayName, beanName);
                    }
                } catch (Exception e) {
                    log.warn("[renewpwd] 刷新 {} Bean 失败: {}", displayName, beanName, e);
                }
            }
        } catch (ClassNotFoundException e) {
            log.debug("[renewpwd] {} 类不存在，跳过刷新", displayName);
        } catch (Exception e) {
            log.warn("[renewpwd] 刷新 {} Bean 过程中出现异常", displayName, e);
        }
    }



    /**
     * 优雅关闭数据源
     */
    private void closeDataSourceGracefully(String beanName) {
        try {
            Object bean = applicationContext.getBean(beanName);

            // 如果 Bean 实现了 DisposableBean 接口，调用 destroy 方法
            if (bean instanceof org.springframework.beans.factory.DisposableBean) {
                ((org.springframework.beans.factory.DisposableBean) bean).destroy();
                log.debug("[renewpwd] 调用 DisposableBean.destroy() 方法: {}", beanName);
            }

            // 如果 Bean 实现了 AutoCloseable 接口，调用 close 方法
            if (bean instanceof AutoCloseable) {
                ((AutoCloseable) bean).close();
                log.debug("[renewpwd] 调用 AutoCloseable.close() 方法: {}", beanName);
            }

            // 如果 Bean 实现了 Closeable 接口，调用 close 方法
            if (bean instanceof java.io.Closeable) {
                ((java.io.Closeable) bean).close();
                log.debug("[renewpwd] 调用 Closeable.close() 方法: {}", beanName);
            }

        } catch (Exception e) {
            log.debug("[renewpwd] Bean 销毁过程中出现异常: {}", beanName, e);
        }
    }

}
