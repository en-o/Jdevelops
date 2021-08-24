package com.detabes.redis.limit.config;

import com.detabes.redis.limit.aop.LimiterAspect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

/**
 * @author lmz
 * @projectName currentlimit-redis
 * @packageName com.lmz.code.currentlimit.config
 * @company Peter
 * @date 2021/8/19  14:34
 * @description 初始化lua脚本
 */
@Component
@Import(LimiterAspect.class)
@EnableConfigurationProperties(LimitConfiguration.class)
public class RedisConfig {
    @Bean
    public RedisScript<Long> limitRedisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/limit.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}


