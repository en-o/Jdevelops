package cn.jdevelops.file.oss.driver.local;

import cn.jdevelops.file.oss.api.config.OSSConfig;
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

    /**
     * 配置文件
     */
    @ConditionalOnMissingBean(OSSConfig.class)
    @Bean
    public OSSConfig ossConfig(){
        return new OSSConfig();
    }


    /**
     * 配置映射
     */
    @ConditionalOnMissingBean(FileContextPathConfig.class)
    @Bean
    public FileContextPathConfig fileContextPathConfig(){
        return new FileContextPathConfig();
    }

}
