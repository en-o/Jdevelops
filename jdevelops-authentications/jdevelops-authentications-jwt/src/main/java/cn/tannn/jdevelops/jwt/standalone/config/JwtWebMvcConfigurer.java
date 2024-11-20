package cn.tannn.jdevelops.jwt.standalone.config;

import cn.tannn.jdevelops.jwt.standalone.interceptor.JwtWebApiInterceptor;
import cn.tannn.jdevelops.utils.jwt.config.JwtConfig;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * web拦截器
 * @author tn
 * @version 1
 * @date 2020/6/18 16:04
 */
@Configuration
@ConditionalOnMissingBean(JwtWebMvcConfigurer.class)
public class JwtWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private InterceptorConfig interceptorBean;

    @Resource
    private JwtConfig jwtConfig;

    @Bean
    public JwtWebApiInterceptor webApiInterceptor() {
        return new JwtWebApiInterceptor(jwtConfig);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截
        Set<String> addPathPatterns = interceptorBean.getAddPathPatterns();
        if(addPathPatterns.isEmpty()){
            addPathPatterns.add("/**");
        }

        //放行
        Set<String> excludePathPatterns = interceptorBean.getExcludePathPatterns();
        excludePathPatterns.add("/swagger-resources/**");
        excludePathPatterns.add("/webjars/**");
        excludePathPatterns.add("/v2/api-docs/**");
        excludePathPatterns.add("/error");
        excludePathPatterns.add("/v3/api-docs/**");
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
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    }

}
