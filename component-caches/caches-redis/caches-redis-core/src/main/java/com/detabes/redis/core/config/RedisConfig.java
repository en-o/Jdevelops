package com.detabes.redis.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

import static java.util.Collections.singletonMap;

/**
 * @author Tianms
 * @date 2019年07月30日 10:13
 * @description
 */
@Slf4j
@EnableCaching
//@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getSimpleName());
                sb.append(method.getDeclaringClass().getSimpleName());
                Arrays.stream(params).map(Object::toString).forEach(sb::append);
                return sb.toString();
            }
        };
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
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
        log.info("redis缓存初始化->{}:{}", lettuceConnectionFactory.getHostName(), lettuceConnectionFactory.getPort());
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        // 以锁写入的方式创建RedisCacheWriter对象
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
        // 创建默认缓存配置对象
        /* 默认配置，设置缓存有效期 1小时*/
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        /* 配置test的超时时间为120s*/
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory)).cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(singletonMap("test", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(120)).disableCachingNullValues()))
                .transactionAware().build();
        return cacheManager;
    }
}
