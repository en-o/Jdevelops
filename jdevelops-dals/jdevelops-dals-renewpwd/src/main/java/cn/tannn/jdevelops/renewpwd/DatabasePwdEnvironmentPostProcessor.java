package cn.tannn.jdevelops.renewpwd;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.*;
import java.util.HashMap;

/**
 * 数据库密码处理器
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        String password = env.getProperty("spring.datasource.password");
        // 需要解密
        if (password != null && password.startsWith("ENC(")) {
            String encrypted = password.substring(4, password.length() - 1);
            password = AESUtil.decrypt(encrypted);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("spring.datasource.password", password);
        env.getPropertySources().addFirst(
                new MapPropertySource("decrypted", map)
        );
    }


}
