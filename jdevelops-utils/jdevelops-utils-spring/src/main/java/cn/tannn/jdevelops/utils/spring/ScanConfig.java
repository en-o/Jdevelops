package cn.tannn.jdevelops.utils.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/28 下午10:37
 */
public class ScanConfig {

    @Bean
    @ConditionalOnMissingBean(name = "environmentUtils")
    public EnvironmentUtils environmentUtils(){
        return new EnvironmentUtils();
    }

    @Bean
    @ConditionalOnMissingBean(name = "contextUtil")
    public ContextUtil contextUtil(){
        return new ContextUtil();
    }
}
