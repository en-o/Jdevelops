package cn.tannn.jdevelops.frameworks.web.starter.scan;

import cn.tannn.jdevelops.frameworks.web.starter.config.InterceptUrl;
import cn.tannn.jdevelops.frameworks.web.starter.context.service.JdevelopsContext;
import cn.tannn.jdevelops.frameworks.web.starter.context.service.impl.JdevelopsContextForSpring;
import cn.tannn.jdevelops.frameworks.web.starter.service.url.UrlService;
import cn.tannn.jdevelops.frameworks.web.starter.service.url.UrlServiceImpl;
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

    @ConditionalOnMissingBean(UrlService.class)
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
    @ConditionalOnMissingBean(JdevelopsContext.class)
    @Bean
    public JdevelopsContext getJdevelopsContext(){
        return new JdevelopsContextForSpring();
    }



}
