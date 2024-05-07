package cn.jdevelops.config.standalone.spring;

import cn.jdevelops.config.standalone.service.ConfigsService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 自定义的配置源处理器 (spring 项目启动开始进行注册自定义配置源
 * <p> BeanFactoryPostProcessor 在容器实例化bean之前“处理”或“修改”BeanFactory配置
 * <p> EnvironmentAware 所有配置信息
 * <p> PriorityOrdered 定义对象排序顺序
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:53
 */
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware, PriorityOrdered {
    /**
     * 属性源里 当前配置中心的名字
     */
    private final static String T_PROPERTY_SOURCES = "jdevelopsPropertySources";
    private final static String T_PROPERTY_SOURCE = "jdevelopsPropertySource";
    Environment environment;

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        if (ENV.getPropertySources().contains(T_PROPERTY_SOURCES)) {
            return;
        }
        // 持久化对象
        ConfigsService configsService = applicationContext.getBean(ConfigsService.class);
        // 配置数据
        ConfigPropertySourceService configPropertySourceService = ConfigPropertySourceService.getDefault(
                applicationContext
                ,configsService);
        PropertySource propertySource = new PropertySource(T_PROPERTY_SOURCE, configPropertySourceService);
        CompositePropertySource composite = new CompositePropertySource(T_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);
        // 将配置中心得到属性置顶(拥有更高的优先级
        ENV.getPropertySources().addFirst(composite);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
