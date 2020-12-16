package com.detabes.doc.swagger.cloud.scan;

import com.detabes.doc.core.swagger.bean.SwaggerBean;
import com.detabes.doc.core.swagger.config.ConsoleConfig;
import com.detabes.doc.swagger.cloud.core.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScanconsole
 * @description 自动扫描
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ConsoleConfig.class, SwaggerBean.class, SwaggerConfig.class})
@ComponentScan("com.detabes.doc.swagger.cloud.**")
public class EnableAutoScanConfiguration {
}