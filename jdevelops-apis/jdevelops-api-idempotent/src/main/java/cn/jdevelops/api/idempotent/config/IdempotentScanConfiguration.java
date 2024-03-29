package cn.jdevelops.api.idempotent.config;

import cn.jdevelops.api.idempotent.core.ApiIdempotentInterceptor;
import cn.jdevelops.api.idempotent.service.IdempotentService;
import cn.jdevelops.api.idempotent.service.IdempotentServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class IdempotentScanConfiguration {


    @ConditionalOnMissingBean(IdempotentConfig.class)
    @Bean
    public IdempotentConfig idempotentConfig(){
        return new IdempotentConfig();
    }

    @ConditionalOnMissingBean(IdempotentService.class)
    @Bean
    public IdempotentService idempotentService(){
        return new IdempotentServiceImpl();
    }

    @ConditionalOnMissingBean(ApiIdempotentInterceptor.class)
    @Bean
    public ApiIdempotentInterceptor apiIdempotentInterceptor(IdempotentService idempotentService){
        return new ApiIdempotentInterceptor(idempotentService);
    }

}
