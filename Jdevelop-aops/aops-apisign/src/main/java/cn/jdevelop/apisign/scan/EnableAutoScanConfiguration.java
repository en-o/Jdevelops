package cn.jdevelop.apisign.scan;

import cn.jdevelop.apisign.config.SignAppInterceptor;
import cn.jdevelop.apisign.config.SignWebConfig;
import cn.jdevelop.apisign.filter.HttpServletRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({
        SignAppInterceptor.class,
        SignWebConfig.class,
        HttpServletRequestFilter.class})
@ComponentScan("cn.jdevelop.apisign.**")
public class EnableAutoScanConfiguration {
}
