package cn.tannn.jdevelops.jwt.standalone;

import cn.tannn.jdevelops.jwt.standalone.config.InterceptorConfig;
import cn.tannn.jdevelops.jwt.standalone.config.JwtWebMvcConfigurer;
import cn.tannn.jdevelops.jwt.standalone.service.LoginService;
import cn.tannn.jdevelops.jwt.standalone.service.impl.DefLoginService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 装配 spring
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午2:16
 */
public class JstandaloneAutoConfiguration {

    @Bean
    public JwtWebMvcConfigurer jwtWebMvcConfigurer(){
        return new JwtWebMvcConfigurer();
    }

    @Bean
    public InterceptorConfig interceptorConfig(){
        return new InterceptorConfig();
    }

    @Bean
    @ConditionalOnMissingBean(LoginService.class)
    public LoginService loginService(){
        return new DefLoginService();
    }
}
