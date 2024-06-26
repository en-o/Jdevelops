package cn.tannn.jdevelops.events.redis.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 消息发布跟订阅的key值bean
 * @author tn
 * @version 1
 * @date 2020/7/3 9:19
 */
@ConfigurationProperties(prefix = "jdevelops.redis.event.pub")
public class RedisEventConfig {

    /**
     * 监听跟发布消息的   频道
     */
    private List<String> patternTopic;

    @Override
    public String toString() {
        return "ReidsCacheBean{" +
                "patternTopic=" + patternTopic +
                '}';
    }

    public List<String> getPatternTopic() {
        return patternTopic;
    }

    public void setPatternTopic(List<String> patternTopic) {
        this.patternTopic = patternTopic;
    }
}
