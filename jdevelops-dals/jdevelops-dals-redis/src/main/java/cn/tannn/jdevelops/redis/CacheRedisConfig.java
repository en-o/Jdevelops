package cn.tannn.jdevelops.redis;

import cn.tannn.jdevelops.redis.cache.CacheKeyGenerator;
import cn.tannn.jdevelops.redis.cache.CustomCacheProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义过期策略
 *
 * @author  tan
 **/
@EnableCaching
public class CacheRedisConfig {

    private final CustomCacheProperties customCacheProperties;

    public CacheRedisConfig(CustomCacheProperties customCacheProperties) {
        this.customCacheProperties = customCacheProperties;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(getDefaultCacheConfig())
                .withInitialCacheConfigurations(getCacheConfigurations())
                .build();
    }

    /**
     * 获取默认的缓存配置
     */
    private RedisCacheConfiguration getDefaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))  // 默认5分钟过期
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(createJacksonSerializer()))
                .disableCachingNullValues();
    }

    /**
     * 创建Jackson序列化器
     */
    private Jackson2JsonRedisSerializer<Object> createJacksonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);

        // 注册 Java 8 时间模块
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }

    /**
     * 获取特定缓存配置
     */
    private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // 缓存配置
        customCacheProperties.getSpecs().forEach((cacheName, spec) -> {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .prefixCacheNameWith(spec.getPrefix())
                    .entryTtl(Duration.ofMinutes(spec.getTtlMinutes()))
                    .serializeKeysWith(RedisSerializationContext.SerializationPair
                            .fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair
                            .fromSerializer(createJacksonSerializer()))
                    .disableCachingNullValues();
            cacheConfigurations.put(cacheName, config);
        });
        return cacheConfigurations;
    }

    @Bean
    @ConditionalOnMissingBean(name = "cacheKeyGenerator")
    public CacheKeyGenerator cacheKeyGenerator() {
        return new CacheKeyGenerator();
    }

}
