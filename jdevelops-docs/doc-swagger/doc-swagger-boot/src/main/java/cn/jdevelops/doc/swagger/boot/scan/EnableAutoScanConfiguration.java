package cn.jdevelops.doc.swagger.boot.scan;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import cn.jdevelops.doc.core.swagger.config.ConsoleConfig;
import cn.jdevelops.doc.swagger.boot.core.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ConsoleConfig.class, SwaggerBean.class})
public class EnableAutoScanConfiguration {


    @ConditionalOnMissingBean(SwaggerConfig.class)
    @Bean
    public SwaggerConfig swaggerConfig(){
        return new SwaggerConfig();
    }
    @Bean(value = "defaultApi")
    @ConditionalOnMissingBean(name = "defaultApi")
    public Docket defaultApi(SwaggerConfig swaggerConfig,SwaggerBean swaggerBean) {
        return swaggerConfig.createRestApi(swaggerBean.getGroupName(), swaggerBean.getBasePackage());
    }
}
