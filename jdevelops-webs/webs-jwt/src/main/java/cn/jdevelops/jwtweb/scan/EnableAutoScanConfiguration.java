package cn.jdevelops.jwtweb.scan;

import cn.jdevelops.jwt.bean.InterceptorBean;
import cn.jdevelops.jwtweb.config.WebApiConfig;
import cn.jdevelops.jwtweb.holder.ApplicationContextHolder;
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
        InterceptorBean.class
})
@ComponentScan("cn.jdevelops.jwtweb.**")
public class EnableAutoScanConfiguration {
}
