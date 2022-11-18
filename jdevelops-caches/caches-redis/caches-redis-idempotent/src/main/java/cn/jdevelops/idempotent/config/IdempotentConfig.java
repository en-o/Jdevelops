package cn.jdevelops.idempotent.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 幂等配置类
 * @author tn
 * @version 1
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "jdevelops.idempotent")
@Component
@Getter
@Setter
@ToString
public class IdempotentConfig {

    /**
     * 过期时间
     * 默认  60*1000(1分钟) 单位 毫秒
     */
    private long expireTime = 60000;

    /**
     * 分组名 默认token
     */
    private String groupStr = "token";
}
