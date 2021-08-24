package com.detabes.redis.limit.aop;

import com.detabes.exception.exception.BusinessException;
import com.detabes.http.core.IpUtil;
import com.detabes.redis.limit.annation.Limiter;
import com.detabes.redis.limit.config.LimitConfiguration;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author lmz
 * @projectName currentlimit-redis
 * @packageName com.lmz.code.currentlimit.aop
 * @company Peter
 * @date 2021/8/19  14:47
 * @description 限流切面
 */
@Aspect
@Component
@Log4j2
public class LimiterAspect {
    private final static String SEPARATOR = ":";
    private final static String REDIS_LIMIT_KEY_PREFIX = "limit:";
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<Long> redisScript;
    private final LimitConfiguration limitConfiguration;

    public LimiterAspect(StringRedisTemplate stringRedisTemplate, RedisScript<Long> redisScript,
                         LimitConfiguration limitConfiguration) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisScript = redisScript;
        this.limitConfiguration = limitConfiguration;
    }

    @Pointcut("@annotation(com.detabes.redis.limit.annation.Limiter)")
    public void rateLimit() {

    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        // 如果是关闭状态直接通过，不进行限流
        if (!limitConfiguration.getEnable()){
            return point.proceed();
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过自定义注解找到限流接口
        Limiter rateLimiter = AnnotationUtils.findAnnotation(method, Limiter.class);
        if (rateLimiter != null) {
            String key = rateLimiter.key();
            // 默认用类名+方法名做限流的 key 前缀
            if (StringUtils.isBlank(key)) {
                key = method.getDeclaringClass().getName() + SEPARATOR + method.getName();
            }
            // 最终限流的 key 为 前缀 + IP地址
            // 此时需要考虑局域网多用户访问的情况
            // TODO 后续需要加上方法参数更加合理
            key = key + SEPARATOR + IpUtil.getRealIp();
            long max = rateLimiter.max();
            long timeout = rateLimiter.timeout();
            TimeUnit timeUnit = rateLimiter.timeUnit();
            boolean limited = shouldLimited(key, max, timeout, timeUnit);
            if (limited) {
                throw  new BusinessException("您的访问速度太快,请休息一下吧！");
            }
        }

        return point.proceed();
    }

    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        // 最终的 key 格式为：
        // limit:自定义key:IP
        // limit:类名.方法名:IP
        key = REDIS_LIMIT_KEY_PREFIX + key;
        // 统一使用单位毫秒
        long ttl = timeUnit.toMillis(timeout);
        // 当前时间毫秒数
        long now = Instant.now().toEpochMilli();
        long expired = now - ttl;
        // 注意这里的传入值必须转为String,否则会报错 java.lang.Long cannot be cast to java.lang.String
        Long executeTimes = stringRedisTemplate.execute(redisScript, Collections.singletonList(key), now + "", ttl + "", expired + "", max + "");
        if (executeTimes != null) {
            if (executeTimes == 0) {
                log.error("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
                return true;
            } else {
                log.info("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
                return false;
            }
        }
        return false;
    }
}
