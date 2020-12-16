package com.detabes.apisign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tn
 * @version 1
 * @ClassName webConfig
 * @description 拦截器
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
