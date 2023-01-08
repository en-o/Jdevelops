package cn.jdevelops.delay.redis;

import cn.jdevelops.delay.core.entity.DelayQueueMessage;
import cn.jdevelops.delay.core.factory.DelayFactory;
import cn.jdevelops.delay.core.service.DelayService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jdk延时队列
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:34
 */
@Service
public class RedisDelayServiceImpl implements DelayService<DelayQueueMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RedisDelayServiceImpl.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private DelayFactory<DelayQueueMessage> delayRunFactory;

    /**
     * 延时队列的key
     */
    private static final String DELAY_QUEUE ="delay:redis_delay_queue";

    /**
     * redis 脚本
     */
    @Resource(name = "delayRedisScript")
    private DefaultRedisScript<List<String>> delayRedisScript;


    /**
     * 线程池
     */
    private static final String NAME = "RedisDelayMessageTask-thread-";
    private final AtomicInteger seq = new AtomicInteger(1);
    private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, r ->
            new Thread(r, NAME + seq.getAndIncrement()));


    @Override
    public void produce(DelayQueueMessage delayMessage) {
        //生产者把消息丢到消息队列中
        //序列化
        String value = JSON.toJSONString(delayMessage);
        // 有序
        redisTemplate.opsForZSet().add(DELAY_QUEUE, value, delayMessage.getDelayTime());

    }

    @Override
    public void produce(List<DelayQueueMessage> delayMessage) {
        delayMessage.forEach(message -> {
            produce(message);
        });
    }

    @Override
    public void consumeDelay() {
        // IllegalArgumentException 的话 initialDelay = 1， period = 1 直接写死
        // 初始化
        long initialDelay =Math.round(Math.random()*10+10);
        // 周期 周期小于或等于零时会抛异常
        long period = Math.round(Math.random()*10);
        logger.info("开始消费延时队列数据...");
        pool.scheduleAtFixedRate(()->{
            try {
                Set<String> set = runLuaScript(DELAY_QUEUE);
                if (!CollectionUtils.isEmpty(set)){
                    set.forEach(s -> {
                        DelayQueueMessage redisDelayMessage = JSON.toJavaObject(JSON.parseObject(s), DelayQueueMessage.class);
                        delayRunFactory.delayExecute(redisDelayMessage);
                    });
                }
            }catch (Throwable e){
                logger.error("RemindMessageTask error..",e);
            }
        }, initialDelay,period, TimeUnit.SECONDS);
    }

    /**
     * 运行lua脚本
     * @param key 延时队列的key
     * @return 返回数据
     */
    public Set<String> runLuaScript(String key) {
        double min =  0;
        double max = System.currentTimeMillis();
        List<String> execute = redisTemplate.execute(delayRedisScript, Collections.singletonList(key), min, max);
        return new HashSet<>(execute);
    }
}
