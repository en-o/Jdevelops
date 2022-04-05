package cn.jdevelops.qiniu.driver;

import cn.jdevelops.file.config.OSSConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@Configuration
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = {"ossConfig"})
    @Bean
    public OSSConfig ossConfig(){
        return new OSSConfig();
    }


}
