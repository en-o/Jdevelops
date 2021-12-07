package cn.jdevelops.doc.swagger.cloud.scan;

import cn.jdevelops.doc.core.swagger.bean.SwaggerBean;
import cn.jdevelops.doc.core.swagger.config.ConsoleConfig;
import cn.jdevelops.doc.swagger.cloud.core.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ConsoleConfig.class, SwaggerBean.class, SwaggerConfig.class})
@ComponentScan("cn.jdevelops.doc.swagger.cloud.**")
public class EnableAutoScanConfiguration {
}