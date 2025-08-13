package cn.tannn.jdevelops.renewpwd;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 数据库密码处理器 - 启动时对密码进行处理
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {
    private final static String PROPERTY_SOURCES = "renewpwdConfigPropertySources";
    private final static String PROPERTY_SOURCE = "renewpwdConfigPropertySource";
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        String password = ENV.getProperty("spring.datasource.password");
        String backupPassword = ENV.getProperty("jdevelops.renewpwd.backupPassword");
        // 这里需要判断怎么算是启用
        if(backupPassword != null){
            password = backupPassword;
        }

        // 需要解密
        if (password != null && password.startsWith("ENC(")) {
            String encrypted = password.substring(4, password.length() - 1);
            password = AESUtil.decrypt(encrypted);
        }
        RenewPasswordService configService = new RenewPasswordService();
        configService.initialize(password);
        // 将实例注册到Spring容器中，确保是单例
        beanFactory.registerSingleton("renewPasswordService", configService);

        // 构建spring 配置格式- 配置源的集成机制
        RenewpwdPropertySource propertySource = new RenewpwdPropertySource(PROPERTY_SOURCES, configService);
        CompositePropertySource composite = new CompositePropertySource(PROPERTY_SOURCE);
        composite.addPropertySource(propertySource);
        // 将配置中心得到属性置顶(拥有更高的优先级
        ENV.getPropertySources().addFirst(composite);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
