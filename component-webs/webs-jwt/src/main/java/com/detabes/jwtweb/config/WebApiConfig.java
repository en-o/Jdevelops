package com.detabes.jwtweb.config;
import com.detabes.jwtweb.bean.InterceptorBean;
import com.detabes.jwtweb.interceptor.WebApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tn
 * @version 1
 * @ClassName WebApiConfig
 * @description web拦截器
 * @date 2020/6/18 16:04
 */
@Configuration
public class WebApiConfig implements WebMvcConfigurer {

    @Autowired(required=false)
    private InterceptorBean interceptorBean;

    @Bean
    public WebApiInterceptor webApiInterceptor() {
        return new WebApiInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截
        Set<String> addPathPatterns = interceptorBean.getAddPathPatterns();
        if(null==addPathPatterns||addPathPatterns.size() <= 0){
            addPathPatterns.add("/**");
        }

        //放行
        Set<String> excludePathPatterns = interceptorBean.getExcludePathPatterns();
        excludePathPatterns.add("/swagger-resources/**");
        excludePathPatterns.add("/webjars/**");
        excludePathPatterns.add("/v2/**");
        excludePathPatterns.add("/v3/**");
        excludePathPatterns.add("/swagger**/**");
        excludePathPatterns.add("/swagger-ui.html/**");
        excludePathPatterns.add("/doc.html/**");
        excludePathPatterns.add("/user/login/**");



        List<String> excludePathPatternsList = new ArrayList<>(excludePathPatterns);
        List<String> addPathPatternsList = new ArrayList<>(addPathPatterns);


        registry.addInterceptor(webApiInterceptor()).addPathPatterns(addPathPatternsList).excludePathPatterns(excludePathPatternsList);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    }

}
