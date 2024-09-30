package cn.tannn.jdevelops.monitor.actuator.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 获取上下文
 * @author tn
 */
public class ActuatorEnvironmentUtils implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        ActuatorEnvironmentUtils.environment = environment;
    }

    /**
     * 获取 Environment
     * @return Environment
     */
    public static Environment findEnvironment(){
        return ActuatorEnvironmentUtils.environment;
    }

    /**
     * 获取环境变量中的配置属性
     * @param key server.port
     * @return value
     */
    public static String searchByKey(String key){
        return ActuatorEnvironmentUtils.environment.getProperty(key);
    }
}
