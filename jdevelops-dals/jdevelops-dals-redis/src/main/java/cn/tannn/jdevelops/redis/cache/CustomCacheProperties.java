package cn.tannn.jdevelops.redis.cache;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * use:
 <pre>
    &#064;Cacheable(value  = "specs.key", keyGenerator = "cacheKeyGenerator")

 redis:
     cache:
         specs:
             userCache:
                 ttlMinutes: 10
                 prefix: user
             productCache:
                 ttlMinutes: 60
                 prefix: product
 </pre>
 */
@ConfigurationProperties(prefix = "jdevelops.redis.cache")
public class CustomCacheProperties {

    /**
     * 缓存配置
     */
    private Map<String, CacheSpec> specs;


    public static class CacheSpec {
        /**
         * 缓存时间/分钟
         */
        private long ttlMinutes;
        /**
         * Key
         */
        private String prefix;

        public long getTtlMinutes() {
            return ttlMinutes;
        }

        public void setTtlMinutes(long ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }

        public String getPrefix() {
            if (prefix == null || prefix.isEmpty()) {
                return "";
            }
            return prefix.endsWith(":") ? prefix : prefix + ":";
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return "CacheSpec{" +
                    "ttlMinutes=" + ttlMinutes +
                    ", prefix='" + prefix + '\'' +
                    '}';
        }
    }


    public Map<String, CacheSpec> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, CacheSpec> specs) {
        this.specs = specs;
    }

    @Override
    public String toString() {
        return "CustomCacheProperties{" +
                "specs=" + specs +
                '}';
    }
}
