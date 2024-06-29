package cn.tannn.jdevelops.jwt.redis;


import cn.tannn.jdevelops.jwt.redis.service.RedisLoginService;
import cn.tannn.jdevelops.jwt.redis.service.RedisToken;
import cn.tannn.jdevelops.jwt.redis.service.RedisUserRole;
import cn.tannn.jdevelops.jwt.redis.service.RedisUserState;
import cn.tannn.jdevelops.jwt.redis.service.impl.RedisTokenImpl;
import cn.tannn.jdevelops.jwt.redis.service.impl.RedisUserRoleImpl;
import cn.tannn.jdevelops.jwt.redis.service.impl.RedisUserStateImpl;
import cn.tannn.jdevelops.jwt.standalone.service.LoginService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 装配 spring
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午2:16
 */
public class JredisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedisToken.class)
    public RedisToken redisToken(){
        return new RedisTokenImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RedisUserState.class)
    public RedisUserState redisUserState(){
        return new RedisUserStateImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RedisUserRole.class)
    public RedisUserRole redisUserRole(){
        return new RedisUserRoleImpl();
    }

    @Bean
    @ConditionalOnMissingBean(LoginService.class)
    public RedisLoginService redisLoginService(){
        return new RedisLoginService();
    }
}
