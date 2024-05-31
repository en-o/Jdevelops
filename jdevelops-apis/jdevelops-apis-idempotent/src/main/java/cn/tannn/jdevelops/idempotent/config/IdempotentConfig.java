package cn.tannn.jdevelops.idempotent.config;

import cn.tannn.jdevelops.idempotent.annotation.ApiIdempotent;
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
public class IdempotentConfig {

    /**
     * 全局：过期时间  <br/>
     * 默认  60*1000(1分钟) 单位 毫秒 <br/>
     * ps 以{@link  ApiIdempotent#expireTime()}为准，当不存在是才使用配置文件中的配置
     */
    private long expireTime = 60000;

    /**
     * 分组名 默认token
     */
    private String groupStr = "token";


    /**
     * 参数是否加密（加密过后存储的数据会变短减少缓存空间和对比时间（默认是）
     */
    private boolean parameterEncryption = true;


    @Override
    public String toString() {
        return "IdempotentConfig{" +
                "expireTime=" + expireTime +
                ", groupStr='" + groupStr + '\'' +
                ", parameterEncryption=" + parameterEncryption +
                '}';
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public boolean isParameterEncryption() {
        return parameterEncryption;
    }

    public void setParameterEncryption(boolean parameterEncryption) {
        this.parameterEncryption = parameterEncryption;
    }
}
