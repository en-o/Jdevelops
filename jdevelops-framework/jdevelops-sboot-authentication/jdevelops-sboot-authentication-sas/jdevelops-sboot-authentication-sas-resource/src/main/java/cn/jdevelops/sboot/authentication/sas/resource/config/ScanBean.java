package cn.jdevelops.sboot.authentication.sas.resource.config;

import cn.jdevelops.sboot.authentication.sas.resource.core.ResourceServerConfig;
import cn.jdevelops.sboot.authentication.sas.resource.core.SasAuthorizeHttpRequests;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 扫描
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/9 13:23
 */
@ConditionalOnWebApplication
public class ScanBean {

    /**
     * 加载基础配置
     */
    @ConditionalOnMissingBean(SasAuthorizeHttpRequests.class)
    @Bean
    public SasAuthorizeHttpRequests sasAuthorizeHttpRequests(){
        return new SasAuthorizeHttpRequests();
    }
    /**
     * EnableMethodSecurity
     */
    @ConditionalOnMissingBean(ResourceServerConfig.class)
    @Bean
    public ResourceServerConfig resourceServerConfig(SasAuthorizeHttpRequests sasAuthorizeHttpRequests){
        return new ResourceServerConfig(sasAuthorizeHttpRequests);
    }

}
