package cn.tannn.jdevelops.renewpwd;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;
import java.util.HashMap;

/**
 * 数据库密码处理器 - 启动时对密码进行处理
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        String password = ENV.getProperty("spring.datasource.password");
        // 需要解密
        if (password != null && password.startsWith("ENC(")) {
            String encrypted = password.substring(4, password.length() - 1);
            password = AESUtil.decrypt(encrypted);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("spring.datasource.password", password);
        ENV.getPropertySources().addFirst(
                new MapPropertySource("renewpwdConfigPropertySource", map)
        );
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
