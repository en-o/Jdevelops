package cn.jdevelops.util.jwt.scan;

import cn.jdevelops.util.jwt.bean.InterceptorBean;
import cn.jdevelops.util.jwt.bean.JwtBean;
import cn.jdevelops.util.jwt.util.ContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "contextUtil")
    @Bean
    public ContextUtil contextUtil(){
        return new ContextUtil();
    }

    @ConditionalOnMissingBean(name = "interceptorBean")
    @Bean
    public InterceptorBean interceptorBean(){
        return new InterceptorBean();
    }

    @ConditionalOnMissingBean(name = "jwtBean")
    @Bean
    public JwtBean jwtBean(){
        return new JwtBean();
    }
}
