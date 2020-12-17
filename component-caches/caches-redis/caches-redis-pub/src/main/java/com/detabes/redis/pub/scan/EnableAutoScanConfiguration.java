package com.detabes.redis.pub.scan;

import com.detabes.redis.pub.config.RedisCacheConfig;
import com.detabes.redis.pub.entity.ReidsCacheBean;
import com.detabes.redis.pub.server.impl.RedisReceiverImpl;
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
@Import({ReidsCacheBean.class,
        RedisCacheConfig.class,
        RedisReceiverImpl.class})
@ComponentScan(basePackages =  "com.detabes.redis.pub.**")
public class EnableAutoScanConfiguration {
}
