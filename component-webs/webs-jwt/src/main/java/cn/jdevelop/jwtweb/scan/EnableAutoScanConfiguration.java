package cn.jdevelop.jwtweb.scan;

import cn.jdevelop.jwtweb.bean.InterceptorBean;
import cn.jdevelop.jwtweb.config.WebApiConfig;
import cn.jdevelop.jwtweb.holder.ApplicationContextHolder;
import cn.jdevelop.jwtweb.server.impl.DefaultInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({WebApiConfig.class,
        ApplicationContextHolder.class,
        InterceptorBean.class,
        DefaultInterceptor.class
})
@ComponentScan("cn.jdevelop.jwtweb.**")
public class EnableAutoScanConfiguration {
}
