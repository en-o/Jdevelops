package cn.jdevelop.jwt.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt秘钥跟过期时间
 * @author tn
 * @version 1
 * @date 2020/6/19 11:23
 */

@ConfigurationProperties(prefix = "jdevelop.jwt")
@Component
@Getter
@Setter
@ToString
public class JwtBean {

    /**
     * token私钥
     */
    private String tokenSecret = "U0JBUElKV1RkV2FuZzkyNjQ1NA";


    /**
     * 默认  过期时间为一天 (24*60*60*1000) 单位 毫秒
     *
     */
    private long expireTime = 86400000;

}
