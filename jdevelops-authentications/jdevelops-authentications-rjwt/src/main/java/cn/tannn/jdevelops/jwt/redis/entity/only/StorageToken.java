package cn.tannn.jdevelops.jwt.redis.entity.only;

import cn.tannn.jdevelops.jwt.redis.entity.sign.RedisSignEntity;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * redis 存储的 token 数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 12:08
 */
public class StorageToken {

    /**
     * subject  token.subject[用户唯一编码，建议登录名]
     */
    String subject;

    /**
     * token
     */
    String token;

    /**
     * 是否永久在线（默认 false
     */
    Boolean alwaysOnline;

    /**
     * redis用 唯一登录 ，以前的是否会被挤下线 （默认false：不挤）
     */
    Boolean onlyOnline;

    public StorageToken() {
    }

    public StorageToken(RedisSignEntity<?> loginMeta, String token) {
        this.subject = loginMeta.getSubject();
        this.token = token;
        this.alwaysOnline = loginMeta.getAlwaysOnline();
        this.onlyOnline = loginMeta.getOnlyOnline();
    }

    public StorageToken(String subject, String token, Boolean alwaysOnline, Boolean onlyOnline) {
        this.subject = subject;
        this.token = token;
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }

    public Boolean getAlwaysOnline() {
        if (Objects.isNull(alwaysOnline)) {
            return false;
        }
        return alwaysOnline;
    }

    public void setOnlyOnline(Boolean onlyOnline) {
        this.onlyOnline = onlyOnline;
    }

    public Boolean getOnlyOnline() {
        if (Objects.isNull(onlyOnline)) {
            return false;
        }
        return onlyOnline;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StorageToken.class.getSimpleName() + "[", "]")
                .add("subject='" + subject + "'")
                .add("token='" + token + "'")
                .add("alwaysOnline=" + alwaysOnline)
                .add("onlyOnline=" + onlyOnline)
                .toString();
    }
}
