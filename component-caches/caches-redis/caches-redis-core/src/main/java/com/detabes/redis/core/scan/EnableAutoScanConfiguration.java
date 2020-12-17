package com.detabes.redis.core.scan;

import com.detabes.redis.core.config.RedisConfig;
import com.detabes.redis.core.config.RedisProxy;
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
        RedisConfig.class,
        RedisProxy.class
       })
@ComponentScan(basePackages =  "com.detabes.redis.core.**")
public class EnableAutoScanConfiguration {
}
