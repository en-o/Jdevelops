package cn.tannn.jdevelops.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * 自定义缓存读写机制CachingConfigurerSupport
 *
 * @author tnnn
 * @date 2024-03-12 08:25
 */

/**
 * 自定义缓存读写机制CachingConfigurerSupport
 *
 * @author tnnn
 * @date 2024-03-12 08:25
 */
@EnableCaching
public class CustomCacheConfig  implements CachingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(CustomCacheConfig.class);

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName());
            sb.append(method.getDeclaringClass().getSimpleName());
            Arrays.stream(params).map(Object::toString).forEach(sb::append);
            return sb.toString();
        };
    }

}

