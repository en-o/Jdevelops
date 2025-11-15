package cn.tannn.jdevelops.mq.redis;

import cn.tannn.jdevelops.mq.redis.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Redis Stream消息队列实现
 *
 * @author tan
 * @see <a href="https://www.cnblogs.com/wzh2010/p/18030912">redis stream cli doc</a>
 */
@Service
public class ResMqService extends MqService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StringRedisTemplate stringRedisTemplate;
    private final ResMqProperties resMqProperties;

    public ResMqService(StringRedisTemplate redisTemplate
            , ResMqProperties resMqProperties) {
        this.stringRedisTemplate = redisTemplate;
        this.resMqProperties = resMqProperties;
    }


    /**
     * 发送对象信息
     * @param topic 主题
     * @param message 消息
     * @return 发送成功失败
     */
    @Override
    public boolean syncSend_Object(String topic, Object message) {
        try {
            topic = resMqProperties.getPrefix() + topic;
            Map<String, Object> stringObjectMap = MapUtil.transBean2Map(message);
            MapRecord<String, String, Object> record = MapRecord.create(topic, stringObjectMap);
            RecordId recordId = stringRedisTemplate.opsForStream().add(record
                    // 无默认长度限制：如果不设置 MAXLEN 参数，Redis Stream 会无限制地增长，直到耗尽可用内存。
                    // 唯一限制是 Redis 的内存限制： 64位系统默认 maxmemory 为 0（无限制）|| 32位系统隐式限制为 3GB
//                    , RedisStreamCommands.XAddOptions.maxlen(11) // 大约100条，精确
                    , RedisStreamCommands.XAddOptions.maxlen(resMqProperties.getMaxQueueSize())
                            .approximateTrimming(resMqProperties.getApproximateTrimming()) // 大约100条，允许稍微超出
            );
            logger.info("mq========= syncSend topic:{}, RecordId:{}", topic, recordId);
            return true;
        } catch (Exception e) {
            logger.error("mq========= Sync Send Error: {}", topic, e);
        }
        // todo 后去用其他方式去兜底
        logger.warn("文件队列上传失败 file_index_meta:{}", message);
        return false;
    }

    /**
     * 发送map信息
     * @param topic 主题
     * @param message 消息
     * @return 发送成功失败
     */
    @Override
    public boolean syncSend_Map(String topic, Map<String, Object> message) {
        try {
            topic = resMqProperties.getPrefix() + topic;
            MapRecord<String, String, Object> record = MapRecord.create(topic, message);
            RecordId recordId = stringRedisTemplate.opsForStream().add(record
                    // 无默认长度限制：如果不设置 MAXLEN 参数，Redis Stream 会无限制地增长，直到耗尽可用内存。
                    // 唯一限制是 Redis 的内存限制： 64位系统默认 maxmemory 为 0（无限制）|| 32位系统隐式限制为 3GB
//                    , RedisStreamCommands.XAddOptions.maxlen(11) // 大约100条，精确
                    , RedisStreamCommands.XAddOptions.maxlen(resMqProperties.getMaxQueueSize())
                            .approximateTrimming(resMqProperties.getApproximateTrimming()) // 大约100条，允许稍微超出
            );
            logger.info("mq========= syncSend topic:{}, RecordId:{}", topic, recordId);
            return true;
        } catch (Exception e) {
            logger.error("mq========= Sync Send Error: {}", topic, e);
        }
        // todo 后去用其他方式去兜底
        logger.warn("文件队列上传失败 file_index_meta:{}", message);
        return false;
    }


}
