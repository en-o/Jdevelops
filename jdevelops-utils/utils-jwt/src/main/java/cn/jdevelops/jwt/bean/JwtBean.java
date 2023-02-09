package cn.jdevelops.jwt.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * jwt秘钥跟过期时间
 * @author tn
 * @version 1
 * @date 2020/6/19 11:23
 */

@ConfigurationProperties(prefix = "jdevelops.jwt")
@Component
@Getter
@Setter
@ToString
public class JwtBean {

    /**
     * token私钥
     */
    private String tokenSecret;


    /**
     * jwt token时间
     * 默认  过期时间为一天 (24*60*60*1000) 单位 毫秒
     */
    private long expireTime = 86400000;




    /**
     * token redis 时间
     * 默认  过期时间为一天 (24*60*60*1000) 单位 毫秒
     *
     */
    private long loginExpireTime = 86400000;

    public String getTokenSecret() {
        if(Objects.isNull(tokenSecret)||tokenSecret.length()<=0){
            return "U0JBUElKV1RkV2FuZzkyNjQ1NAadasdawq1qqwez123w@123";
        }
        return tokenSecret;
    }
}
