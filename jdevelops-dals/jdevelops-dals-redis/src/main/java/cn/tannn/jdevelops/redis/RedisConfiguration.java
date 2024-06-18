package cn.tannn.jdevelops.redis;

import cn.tannn.jdevelops.redis.limit.LoginLimitConfig;
import cn.tannn.jdevelops.redis.limit.LoginLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 注册
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 上午9:56
 */

@ConditionalOnWebApplication
public class RedisConfiguration {

    @Bean
    public LoginLimitConfig loginLimitConfig(){
        return new LoginLimitConfig();
    }

    @Bean
    public CustomCacheConfig customCacheConfig(){
        return new CustomCacheConfig();
    }

    @Bean
    public LoginLimitService loginLimitService(LoginLimitConfig loginLimitConfig){
        return new LoginLimitService(loginLimitConfig);
    }

    @Bean
    public RedisOperateService redisOperateService(@Autowired RedisTemplate<String, Object> redisTemplate){
        return new RedisOperateService(redisTemplate);
    }


}
