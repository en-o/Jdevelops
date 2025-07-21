package cn.tannn.jdevelops.redis;

import cn.tannn.jdevelops.redis.limit.LoginLimitConfig;
import cn.tannn.jdevelops.redis.limit.LoginLimitService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.RedisConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 注册
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 上午9:56
 */

@ConditionalOnWebApplication
public class RedisConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RedisConfiguration.class);

    @Bean
    public LoginLimitConfig loginLimitConfig() {
        return new LoginLimitConfig();
    }


    @Bean
    public LoginLimitService loginLimitService(LoginLimitConfig loginLimitConfig) {
        return new LoginLimitService(loginLimitConfig);
    }


    @ConditionalOnProperty(
            name = "jdevelops.redis.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om,Object.class);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        try {
            if (!isConnected(redisTemplate)) {
                LOG.error("redis连接失败！请检查redis客户端是否启动成功/redis配置信息是否正确");
                throw new RedisConnectionException("Redis连接失败");
            }
            LOG.info("Redis缓存初始化成功 - 主机: {}, 端口: {}",
                    lettuceConnectionFactory.getHostName(),
                    lettuceConnectionFactory.getPort());
        } catch (RedisConnectionException e) {
            LOG.error("Redis连接初始化错误: {}", e.getMessage(), e);
            // 根据具体应用场景选择处理方式
            // 1. 优雅降级
            // 2. 使用本地缓存
            // 3. 重试连接
            // 4. 通知运维
            throw new RedisConnectionException("Redis初始化失败", e);
        }
        return redisTemplate;
    }


    @Bean
    public RedisOperateService redisOperateService(@Autowired RedisTemplate<String, Object> redisTemplate) {
        return new RedisOperateService(redisTemplate);
    }


    /**
     * 验证 redis是否连接
     *
     * @return boolean
     */
    private boolean isConnected(RedisTemplate<String, Object> redisTemplate) {
        try {
            String pong = redisTemplate.execute(RedisConnectionCommands::ping);
            return "PONG".equals(pong);
        } catch (Exception e) {
            LOG.error("redis连接失败 ==========> {}", e.getMessage());
            return false;
        }
    }

}
