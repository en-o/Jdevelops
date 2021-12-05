package cn.jdevelop.redis.core.scan;

import cn.jdevelop.redis.core.config.RedisConfig;
import cn.jdevelop.redis.core.config.RedisProxy;
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
        RedisConfig.class,
        RedisProxy.class
       })
@ComponentScan(basePackages =  "cn.jdevelop.redis.core.**")
public class EnableAutoScanConfiguration {
}
