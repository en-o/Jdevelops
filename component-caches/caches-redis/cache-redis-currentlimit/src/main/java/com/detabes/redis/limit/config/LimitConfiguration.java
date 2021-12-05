package com.detabes.redis.limit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lmz
 * @date 2021/8/24  10:14
 */
@Component
@ConfigurationProperties(prefix = "cache.redis", ignoreUnknownFields = false)
public class LimitConfiguration {
    /**
     * 接口次数访问限制开关,默认true 开启
     */
    private Boolean enable;

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        if (enable==null){
            enable=true;
        }
        return enable;
    }
}
