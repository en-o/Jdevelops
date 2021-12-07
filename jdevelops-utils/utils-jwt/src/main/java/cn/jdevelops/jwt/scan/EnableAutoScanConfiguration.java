package cn.jdevelops.jwt.scan;

import cn.jdevelops.jwt.util.ContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ ContextUtil.class})
@ComponentScan("cn.jdevelops.jwt.**")
public class EnableAutoScanConfiguration {
}
