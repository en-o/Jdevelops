package cn.jdevelops.api.exception.scan;

import cn.jdevelops.api.exception.aspect.ExceptionAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-06-24 17:35
 */
@ConditionalOnWebApplication
@Import({ExceptionAspect.class})
public class EnableAutoScanConfiguration {
}
