package cn.jdevelop.cloud.gateway.scan;

import cn.jdevelop.cloud.gateway.config.ServerConfig;
import cn.jdevelop.cloud.gateway.exception.core.ExceptionHandlerConfiguration;
import cn.jdevelop.cloud.gateway.factory.DocNameRoutePredicateFactory;
import cn.jdevelop.cloud.gateway.sawagger.config.SwaggerResourceConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({SwaggerResourceConfig.class,
        DocNameRoutePredicateFactory.class,
        ServerConfig.class})
@ComponentScan( basePackages = "com.databstech.doc.gateway.**",
        excludeFilters= {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ExceptionHandlerConfiguration.class)}
                )
public class EnableAutoScanConfiguration {
}
