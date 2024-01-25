package cn.jdevelops.authentication.sas.client.config;

import cn.jdevelops.authentication.sas.client.core.SasAuthorizeHttpRequests;
import cn.jdevelops.authentication.sas.client.core.WebSecurityConfig;
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
public class ClientScanBean {

    /**
     * 加载基础配置
     */
    @ConditionalOnMissingBean(SasAuthorizeHttpRequests.class)
    @Bean
    public SasAuthorizeHttpRequests sasAuthorizeHttpRequests(){
        return new SasAuthorizeHttpRequests();
    }


    /**
     * WebSecurityConfig
     */
    @ConditionalOnMissingBean(WebSecurityConfig.class)
    @Bean
    public WebSecurityConfig webSecurityConfig(SasAuthorizeHttpRequests sasAuthorizeHttpRequests){
        return new WebSecurityConfig(sasAuthorizeHttpRequests);
    }

}
