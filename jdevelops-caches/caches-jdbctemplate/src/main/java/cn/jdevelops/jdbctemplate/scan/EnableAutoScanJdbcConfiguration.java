package cn.jdevelops.jdbctemplate.scan;

import cn.jdevelops.jdbctemplate.aspect.QueryAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({QueryAop.class})
public class EnableAutoScanJdbcConfiguration {

}
