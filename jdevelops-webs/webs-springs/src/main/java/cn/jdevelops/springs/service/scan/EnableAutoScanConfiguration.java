package cn.jdevelops.springs.service.scan;

import cn.jdevelops.springs.service.url.UrlService;
import cn.jdevelops.springs.service.config.InterceptUrl;
import cn.jdevelops.springs.service.url.impl.UrlServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import(InterceptUrl.class)
@ComponentScan("cn.jdevelops.springs.service.**")
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "urlService")
    @Bean
    public UrlService urlService(){
        return new UrlServiceImpl();
    }

}
