package cn.tannn.jdevelops.redis.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

/**
 * 登录限制配置
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 08:25
 */
@ConfigurationProperties(prefix = "jdevelops.login.limit")
public class LoginLimitConfig {

    /**
     * 过期时间
     * <pr>
     * 默认  600*1000(10分钟) 单位 毫秒
     * </pr>
     */
    private long expireTime = 600000;

    /**
     * 密码错误次数
     * <pr>
     * 默认5
     * </pr>
     */
    private Integer limit = 5;


    /**
     * 前缀，项目名，防止 redis dir 重复
     */
    private String prefix;


    @Override
    public String toString() {
        return new StringJoiner(", ", LoginLimitConfig.class.getSimpleName() + "[", "]")
                .add("expireTime=" + expireTime)
                .add("limit=" + limit)
                .add("prefix='" + prefix + "'")
                .toString();
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getPrefix() {
        return prefix == null ? "project" : prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
