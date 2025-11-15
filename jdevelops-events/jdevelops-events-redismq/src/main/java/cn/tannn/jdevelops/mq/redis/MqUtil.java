package cn.tannn.jdevelops.mq.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/23 12:14
 */
public class MqUtil {

    private static final  Logger logger= LoggerFactory.getLogger(MqUtil.class);

    /**
     * Consumer Group初始化方法
     */
    public static void initConsumerGroup(RedisTemplate<String, Object> redisTemplate
            , cn.tannn.jdevelops.events.redis.mq.ResMqProperties resMqProperties
            , String streamKey
            , String groupName
            , ReadOffset readOffset
    ) {
        try {
            // 确保Stream存在
            ensureStreamExists(redisTemplate, resMqProperties, streamKey);

            // 尝试创建Consumer Group
            redisTemplate.opsForStream().createGroup( resMqProperties.getPrefix()+streamKey, readOffset, groupName);
            logger.info("✅ Consumer Group初始化成功: {}", groupName);

        } catch (Exception e) {
            logger.warn("ℹ️ Consumer Group已存在，跳过创建: {}", groupName);
        }
    }

    /**
     * 确保Stream存在的辅助方法
     */
    public static void ensureStreamExists(RedisTemplate<String, Object> redisTemplate
            , cn.tannn.jdevelops.events.redis.mq.ResMqProperties resMqProperties, String streamKey) {
        streamKey = resMqProperties.getPrefix()+streamKey;
        if (!redisTemplate.hasKey(streamKey)) {
            logger.warn("📝 Stream不存在，创建初始消息");
            redisTemplate.opsForStream().add(
                    StreamRecords.string(Map.of("_init", "stream_created"))
                            .withStreamKey(streamKey)
                    // 无默认长度限制：如果不设置 MAXLEN 参数，Redis Stream 会无限制地增长，直到耗尽可用内存。
                    // 唯一限制是 Redis 的内存限制： 64位系统默认 maxmemory 为 0（无限制）|| 32位系统隐式限制为 3GB
                    , RedisStreamCommands.XAddOptions.maxlen(resMqProperties.getMaxQueueSize()).approximateTrimming(resMqProperties.getApproximateTrimming()) // 大约100条，允许稍微超出
            );
        }
    }
}
