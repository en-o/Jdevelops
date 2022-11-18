package cn.jdevelops.idempotent.config;

import cn.jdevelops.idempotent.interceptor.ApiIdempotentInterceptor;
import cn.jdevelops.idempotent.service.IdempotentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 注册拦截器
 * @author tannn
 */
@Configuration
public class IdempotentMVCConfig implements WebMvcConfigurer {


    @Resource
    private IdempotentService idempotentService;

    /**
     * 将自定义拦截器作为bean写入配置
     * @return ApiIdempotentInterceptor
     */
    @Bean
    public ApiIdempotentInterceptor apiIdempotentInterceptor(IdempotentService idempotentService){
        return new ApiIdempotentInterceptor(idempotentService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiIdempotentInterceptor(idempotentService));
    }
}
