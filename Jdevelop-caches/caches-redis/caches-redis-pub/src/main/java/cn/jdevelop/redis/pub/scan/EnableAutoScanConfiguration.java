package cn.jdevelop.redis.pub.scan;

import cn.jdevelop.redis.pub.config.RedisCacheConfig;
import cn.jdevelop.redis.pub.entity.ReidsCacheBean;
import cn.jdevelop.redis.pub.server.impl.RedisReceiverImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ReidsCacheBean.class,
        RedisCacheConfig.class, })
@ComponentScan(basePackages =  "cn.jdevelop.redis.pub.**")
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "redisReceiverImpl")
    @Bean
    public RedisReceiverImpl redisReceiverImpl(){
        return new RedisReceiverImpl();
    }
}
