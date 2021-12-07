package cn.jdevelops.redis.pub.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息发布跟订阅的key值bean
 * @author tn
 * @version 1
 * @date 2020/7/3 9:19
 */
@ConfigurationProperties(prefix = "jdevelops.redis.cache.pub")
@Component
@Getter
@Setter
@ToString
public class ReidsCacheBean {

    /**
     * 监听跟发布消息的   频道
     */
    private List<String> patternTopic;

}
