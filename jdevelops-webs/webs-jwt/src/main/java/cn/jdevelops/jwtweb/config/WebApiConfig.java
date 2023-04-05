package cn.jdevelops.jwtweb.config;
import cn.jdevelops.util.jwt.bean.JwtBean;
import cn.jdevelops.jwtweb.interceptor.WebApiInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
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
public class WebApiConfig implements WebMvcConfigurer {

    @Resource
    private InterceptorBean interceptorBean;

    @Resource
    private JwtBean jwtBean;

    @Bean
    public WebApiInterceptor webApiInterceptor() {
        return new WebApiInterceptor(jwtBean);
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
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    }

}
