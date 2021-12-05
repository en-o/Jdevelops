package cn.jdevelop.apisign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * webConfig 拦截器
 * @author tn
 * @version 1
 * @date 2020/6/16 9:44
 */
@Configuration
@Order(10)
public class SignWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignAppInterceptor());
    }
}
