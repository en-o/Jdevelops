package cn.tannn.jdevelops.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 我常用的redis操作
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 上午10:10
 */
public class RedisOperateService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisOperateService.class);
    /**
     * 一小时/秒
     */
    public static final long ONE_HOUR = 60 * 60L;

    /**
     * 一天/秒
     */
    public static final long ONE_DAY = 24 * 60 * 60L;

    /**
     * 一周/秒
     */
    public static final long ONE_WEEK = 7 * 24 * 60 * 60L;


    private final RedisTemplate<String, Object> redisTemplate;


    public RedisOperateService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询 key
     *
     * @param pattern 可以使用正则表达式（通配符 *）
     * @return keys
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    /**
     * 获取剩余过期时间
     *
     * @param key 键
     * @return 秒 [返回0代表为永久有效]
     */
    public Long findExpire(String key) {
        try {
            // 永不过期
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            LOG.error("指定缓存永不失效失败", e);

        }
        return 0L;
    }

    /**
     * 获取剩余过期时间
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return timeUnit [返回0代表为永久有效]
     */
    public Long findExpire(String key, TimeUnit timeUnit) {
        try {
            // 永不过期
            return redisTemplate.getExpire(key, timeUnit);
        } catch (Exception e) {
            LOG.error("指定缓存永不失效失败", e);

        }
        return 0L;
    }

    /**
     * 指定缓存永不失效
     *
     * @param key 键
     */
    public boolean expire(String key) {
        try {
            // 永不过期
            redisTemplate.persist(key);
            return true;
        } catch (Exception e) {
            LOG.error("指定缓存永不失效失败", e);
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            LOG.error("指定缓存失效时间失败", e);
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param time     时间
     * @param timeUnit 事件单位
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            LOG.error("指定缓存失效时间失败", e);
            return false;
        }
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            LOG.error("判断key是否存在失败", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 目录结构的存储 （hash)
     *
     * @param folder 目录结构（包含key)[结构示例：xx:xx:key]
     * @param key    key
     * @param value  值
     */
    public void catalogueHash(String folder, String key, Object value) {
        redisTemplate.boundHashOps(folder).put(key, value);
    }

    /**
     * 目录结构的存储 （hash)
     *
     * @param folder 目录结构（包含key)[结构示例：xx:xx:key]
     * @param key    key
     * @param value  值
     * @param time   过期时间/秒 [ <=0 设置不过期]
     */
    public void catalogueHash(String folder, String key, Object value, long time) {
        redisTemplate.boundHashOps(folder).put(key, value);
        if (time <= 0) {
            redisTemplate.persist(folder);
        } else {
            redisTemplate.expire(folder, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 目录结构的存储 （hash)
     *
     * @param folder   目录结构（包含key)[结构示例：xx:xx:key]
     * @param key      key
     * @param value    值
     * @param time     过期时间 [ <=0 设置不过期]
     * @param timeUnit 时间单位
     */
    public void catalogueHash(String folder, String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.boundHashOps(folder).put(key, value);
        if (time <= 0) {
            redisTemplate.persist(folder);
        } else {
            redisTemplate.expire(folder, time, timeUnit);
        }
    }


    /**
     * 获取目录结构里的数据
     *
     * @param folder 目录结构（包含key)[结构示例：xx:xx:key]
     * @param key    key
     * @return 值，可能为空噢
     */
    public Object findCatalogueHash(String folder, String key) {
        return redisTemplate.boundHashOps(folder).get(key);
    }


    /**
     * 有序集合 ZSet
     *
     * @param key    key[此key可以是目录结构 xx:xx:xx]
     * @param values 值
     * @param score  key已经存在更新,反之
     */
    public void orderlyZSet(String key, Object values, double score) {
        // 绑定到特定的 ZSet 键
        if (values == null) {
            return;
        }
        BoundZSetOperations<String, Object> ops = redisTemplate.boundZSetOps(key);
        // 添加元素到 ZSet 中
        ops.add(values, score);
    }

    /**
     * 获取指定key的所有 有序数据
     *
     * @param key key[此key可以是目录结构 xx:xx:xx]
     */
    public Set<Object> orderlyZGets(String key) {
        BoundZSetOperations<String, Object> ops = redisTemplate.boundZSetOps(key);
        //  获取 ZSet 中的所有元素
        return ops.range(0, -1);
    }

    /**
     * 根据 score 获取范围数据
     *
     * @param key key[此key可以是目录结构 xx:xx:xx]
     * @param scoreStart 开始score
     * @param scoreEnd 结束score
     */
    public Set<Object> orderlyZGetByScore(String key, double scoreStart, double scoreEnd) {
        BoundZSetOperations<String, Object> ops = redisTemplate.boundZSetOps(key);
        //  获取 ZSet 中的所有元素
        return  ops.rangeByScore(scoreStart,scoreEnd);
    }


    /**
     * 删除 ZSet 中的元素
     *
     * @param key   key[此key可以是目录结构 xx:xx:xx]
     * @param value 根据值删除(为空删除所有
     */
    public void orderlyRemoveZGets(String key, Object... value) {
        BoundZSetOperations<String, Object> ops = redisTemplate.boundZSetOps(key);
        if(value != null){
            ops.remove(value);
        }else {
            // 删除 ZSet 中的所有元素
            ops.removeRange(0, -1);
        }
    }

}
