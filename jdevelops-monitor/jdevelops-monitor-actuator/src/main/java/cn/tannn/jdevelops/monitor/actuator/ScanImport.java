package cn.tannn.jdevelops.monitor.actuator;

import cn.tannn.jdevelops.monitor.actuator.properties.AppInfoConfiguration;
import cn.tannn.jdevelops.monitor.actuator.util.ActuatorEnvironmentUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/9/9 上午10:09
 */

public class ScanImport {

    @Bean
    @ConditionalOnMissingBean(name = "actuatorEnvironmentUtils")
    public ActuatorEnvironmentUtils environmentUtils(){
        return new ActuatorEnvironmentUtils();
    }

    @Bean
    @ConditionalOnMissingBean(name = "appInfoConfiguration")
    public AppInfoConfiguration appInfoConfiguration(){
        return new AppInfoConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(name = "appInfoContributor")
    public AppInfoContributor appInfoContributor(){
        return new AppInfoContributor();
    }
}
