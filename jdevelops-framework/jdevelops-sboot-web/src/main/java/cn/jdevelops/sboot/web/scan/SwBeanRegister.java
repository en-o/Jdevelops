package cn.jdevelops.sboot.web.scan;

import cn.jdevelops.sboot.web.config.InterceptUrl;
import cn.jdevelops.sboot.web.context.service.JdevelopsContext;
import cn.jdevelops.sboot.web.context.service.impl.JdevelopsContextForSpring;
import cn.jdevelops.sboot.web.service.url.UrlService;
import cn.jdevelops.sboot.web.service.url.UrlServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import(InterceptUrl.class)
public class SwBeanRegister {

    @ConditionalOnMissingBean(name = "urlService")
    @Bean
    public UrlService urlService(){
        return new UrlServiceImpl();
    }


    /**
     *
     * 注入上下文Bean
     * jdevelopsContext
     * @return JdevelopsContext
     */
    @ConditionalOnMissingBean(name = "jdevelopsContext")
    @Bean
    public JdevelopsContext getJdevelopsContext(){
        return new JdevelopsContextForSpring();
    }



}
