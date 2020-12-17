package com.detabes.apisign.scan;

import com.detabes.apisign.config.SignAppInterceptor;
import com.detabes.apisign.config.SignWebConfig;
import com.detabes.apisign.filter.HttpServletRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScan
 * @description 自动扫描
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({
        SignAppInterceptor.class,
        SignWebConfig.class,
        HttpServletRequestFilter.class})
@ComponentScan("com.detabes.apisign.**")
public class EnableAutoScanConfiguration {
}
