package cn.tannn.jdevelops.webs.interceptor;

import cn.tannn.jdevelops.webs.interceptor.core.JdevelopsWebMvcConfig;
import cn.tannn.jdevelops.webs.interceptor.core.InterceptorConfig;
import cn.tannn.jdevelops.webs.interceptor.fiflter.JdevelopsHttpServletRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 拦截器注册
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/5 上午8:51
 */
public class InterceptorAutoconfigure {

    @Bean
    @ConditionalOnMissingBean(InterceptorConfig.class)
    public InterceptorConfig interceptorConfig(){
        return new InterceptorConfig();
    }


    @Bean
    @ConditionalOnMissingBean(JdevelopsHttpServletRequestFilter.class)
    public JdevelopsHttpServletRequestFilter jdevelopsHttpServletRequestFilter(InterceptorConfig interceptorConfig){
        return new JdevelopsHttpServletRequestFilter(interceptorConfig);
    }



    @Bean
    @ConditionalOnMissingBean(JdevelopsWebMvcConfig.class)
    public JdevelopsWebMvcConfig jdevelopsWebMvcConfig(@Autowired List<ApiAfterInterceptor> afterInterceptors,
                                                       @Autowired  List<ApiAsyncInterceptor> asyncInterceptors,
                                                       @Autowired  List<ApiBeforeInterceptor> beforeInterceptors,
                                                       @Autowired  List<ApiFinallyInterceptor> finallyInterceptors,
                                                       @Autowired  List<ApiInterceptor> apiInterceptors){
        return new JdevelopsWebMvcConfig(afterInterceptors
                ,asyncInterceptors
                ,beforeInterceptors
                ,finallyInterceptors
                ,apiInterceptors);
    }
}
