package cn.tannn.jdevelops.sign.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt秘钥跟过期时间
 *
 * @author tn
 * @version 1
 * @date 2020/6/19 11:23
 */

@ConfigurationProperties(prefix = "jdevelops.apisign")
public class ApiSignConfig {

    /**
     * token私钥
     */
    private String salt = "MD5database";

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "ApiSignConfig{" +
                "salt='" + salt + '\'' +
                '}';
    }
}
