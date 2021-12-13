package cn.jdevelops.apisign.bean;

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

@ConfigurationProperties(prefix = "jdevelops.apisign")
@Component
@Getter
@Setter
@ToString
public class ApiSignBean {

    /**
     * token私钥
     */
    private String salt = "MD5database";


}
