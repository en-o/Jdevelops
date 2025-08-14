package cn.tannn.jdevelops.renewpwd.proerty;

import cn.tannn.jdevelops.renewpwd.pojo.PasswordPool;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PwdRefreshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库密码处理器 - 启动时对密码进行处理
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {
    private final static String PROPERTY_SOURCES = "renewpwdConfigPropertySources";
    private final static String PROPERTY_SOURCE = "renewpwdConfigPropertySource";
    private static final Logger log = LoggerFactory.getLogger(DatabasePwdEnvironmentPostProcessor.class);
    private Environment environment;



    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        String password = ENV.getProperty("spring.datasource.password");
        List<String> passwords = new ArrayList<>();
       try {
           Binder binder = Binder.get(environment);
           PasswordPool passwordPool = binder.bind("jdevelops.renewpwd", Bindable.of(PasswordPool.class))
                   .orElseThrow(() -> new IllegalStateException("无法加载密码配置"));
           passwords = passwordPool.getPasswords();
       }catch (Exception e){
           if( e instanceof IllegalStateException){
               log.warn("{}，使用datasource的密码进行处理",e.getMessage());
           }else {
               log.warn("没有配置密码池，使用datasource的密码进行处理");
           }
       }

        if(!PwdRefreshUtil.validateDatasourceConfig(ENV,password,passwords)){
            throw new RuntimeException("数据库密码配置错误，请检查配置文件或环境变量");
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
