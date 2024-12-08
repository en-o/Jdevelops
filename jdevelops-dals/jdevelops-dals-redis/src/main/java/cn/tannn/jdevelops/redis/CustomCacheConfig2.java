//package cn.tannn.jdevelops.redis;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionCommands;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//import java.util.Arrays;
//
//
///**
// * 自定义缓存读写机制CachingConfigurerSupport
// *
// * @author tnnn
// * @date 2024-03-12 08:25
// */
//@EnableCaching
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//public class CustomCacheConfig2 {
//
//    private static final Logger LOG = LoggerFactory.getLogger(CustomCacheConfig2.class);
//
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getSimpleName());
//            sb.append(method.getDeclaringClass().getSimpleName());
//            Arrays.stream(params).map(Object::toString).forEach(sb::append);
//            return sb.toString();
//        };
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//        // 设置序列化
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
//        // 配置redisTemplate
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//        // value序列化
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        // value序列化
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        // Hash key序列化
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        // Hash value序列化
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//
//        // Check connection
//        if (isConnected(redisTemplate)) {
//            LOG.info("Redis initialized at {}:{}", lettuceConnectionFactory.getHostName(), lettuceConnectionFactory.getPort());
//        } else {
//            LOG.error("Redis connection failed! Please check the Redis server and configuration.");
//        }
//        return redisTemplate;
//    }
//
//    private boolean isConnected(RedisTemplate<String, Object> redisTemplate) {
//        try {
//            String pong = redisTemplate.execute(RedisConnectionCommands::ping);
//            return "PONG".equals(pong);
//        } catch (Exception e) {
//            LOG.error("Redis connection failed: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    @Bean(name = "customCacheManager  ")
//    public RedisCacheManager customCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))  // Example TTL
//                .disableCachingNullValues();  // Disable caching null values
//
//        return RedisCacheManager.builder(lettuceConnectionFactory)
//                .cacheDefaults(cacheConfig)
//                .build();
//    }
//}
