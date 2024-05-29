package cn.tannn.jdevelops.delays.redis;


import cn.tannn.jdevelops.delays.core.entity.DelayQueueMessage;
import cn.tannn.jdevelops.delays.core.service.DelayService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;


/**
 * 注入配置
 * @author tnnn
 */
@AutoConfiguration
public  class EnableAutoRedisDelay {

    /**
     * 加载lua脚本
     * @return DefaultRedisScript
     */
    @ConditionalOnMissingBean(DefaultRedisScript.class)
    @Bean
    public DefaultRedisScript<List> delayRedisScript() {
        DefaultRedisScript<List> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(List.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/delay.lua")));
        return defaultRedisScript;
    }




    /**
     * 得到消息之后的执行动作
     * @return DelayService
     */
    @ConditionalOnMissingBean(DelayService.class)
    @Bean
    public DelayService<DelayQueueMessage> delayService() {
        return new RedisDelayService<>(DelayQueueMessage.class);
    }


    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
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
        return redisTemplate;
    }

}
