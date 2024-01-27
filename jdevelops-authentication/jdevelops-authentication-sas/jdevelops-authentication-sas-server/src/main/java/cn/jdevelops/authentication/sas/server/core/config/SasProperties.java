package cn.jdevelops.authentication.sas.server.core.config;

import cn.jdevelops.authentication.sas.server.core.entity.SasAuthorizeHttpRequests;
import cn.jdevelops.authentication.sas.server.core.entity.SasTokenSettings;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 基础可配信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/9 11:13
 */
@ConfigurationProperties(prefix = "jdevelops.sas")
@AutoConfiguration
public class SasProperties {

    /**
     * 注册客户端时设置的token信息
     */
    @NestedConfigurationProperty
    SasTokenSettings  token;

    /**
     * 放行与拦截
     */
    @NestedConfigurationProperty
    SasAuthorizeHttpRequests requests;

    public SasTokenSettings getToken() {
       if (token == null){
           return new SasTokenSettings();
       }
        return token;
    }

    public void setToken(SasTokenSettings token) {
        this.token = token;
    }

    public SasAuthorizeHttpRequests getRequests() {
        if(null == requests){
            return new SasAuthorizeHttpRequests();
        }
        return requests;
    }

    public void setRequests(SasAuthorizeHttpRequests requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "SasProperties{" +
                "token=" + token +
                ", requests=" + requests +
                '}';
    }
}
