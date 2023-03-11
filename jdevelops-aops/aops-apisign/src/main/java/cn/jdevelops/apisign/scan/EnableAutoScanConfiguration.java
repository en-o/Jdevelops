package cn.jdevelops.apisign.scan;

import cn.jdevelops.apisign.bean.ApiSignBean;
import cn.jdevelops.apisign.config.SignAppInterceptor;
import cn.jdevelops.apisign.config.SignWebConfig;
import cn.jdevelops.apisign.filter.HttpServletRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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
        HttpServletRequestFilter.class,
        ApiSignBean.class})
public class EnableAutoScanConfiguration {
}
