package cn.jdevelops.redis.core;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * redis
 * <p>
 *          //获取锁
 *         boolean isOK = RedisLock.tryLock(redisTemplate, REDIS_KEY+"lock", REDIS_KEY+"lock", 20000);
 *         if (isOK) {}
 *         // 处理完业务，释放锁===
 *         RedisLock.unlock(redisTemplate, REDIS_KEY+"lock", REDIS_KEY+"lock");
 * </p>
 *
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/109:43
 */
public class RedisLock {


    private static final Long SUCCESS = 1L;

    /**
     * 加锁，无阻塞
     *
     * @param redisTemplate redisTemplate
     * @param key key
     * @param value value
     * @param expireTime 超时时间(毫秒)
     * @return Boolean
     */
    public static Boolean tryLock(RedisTemplate<String,Object> redisTemplate, String key, String value, long expireTime) {
        try {
            //SET命令返回OK ，则证明获取锁成功
            return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param redisTemplate redisTemplate
     * @param key key
     * @param value value
     * @return Boolean
     */
    public static Boolean unlock(RedisTemplate<String,Object> redisTemplate, String key, String value) {
        try {
            Object result = redisTemplate.delete(Collections.singletonList(key));
            if (SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
