package cn.jdevelops.jwt.scan;

import cn.jdevelops.jwt.bean.InterceptorBean;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.util.ContextUtil;
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

    @ConditionalOnMissingBean(ContextUtil.class)
    @Bean
    public ContextUtil contextUtil(){
        return new ContextUtil();
    }

    @ConditionalOnMissingBean(InterceptorBean.class)
    @Bean
    public InterceptorBean interceptorBean(){
        return new InterceptorBean();
    }

    @ConditionalOnMissingBean(JwtBean.class)
    @Bean
    public JwtBean jwtBean(){
        return new JwtBean();
    }
}
