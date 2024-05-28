package cn.tannn.jdevelops.utils.spring;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 获取上下文
 * @author tn
 */
public class EnvironmentUtils implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        EnvironmentUtils.environment = environment;
    }

    /**
     * 获取 Environment
     * @return Environment
     */
    public static Environment findEnvironment(){
        return EnvironmentUtils.environment;
    }

    /**
     * 获取环境变量中的配置属性
     * @param key server.port
     * @return value
     */
    public static String searchByKey(String key){
        return EnvironmentUtils.environment.getProperty(key);
    }
}
