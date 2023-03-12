package cn.jdevelops.sboot.swagger.core.factories;

import cn.jdevelops.sboot.swagger.config.ConsoleConfig;
import cn.jdevelops.sboot.swagger.config.SwaggerConfig;
import cn.jdevelops.sboot.swagger.config.SwaggerProperties;
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
@Import({ConsoleConfig.class, SwaggerProperties.class})
public class SwaggerFactories {

    @ConditionalOnMissingBean(name = "swaggerConfig")
    @Bean
    public SwaggerConfig swaggerConfig(){
        return new SwaggerConfig();
    }
}
