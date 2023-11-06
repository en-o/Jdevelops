package cn.jdevelops.sboot.authentication.jredis.entity.only;

import lombok.*;

import java.util.Objects;

/**
 *  redis 存储的 token 数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 12:08
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageToken {

    /**
     * subject  用户唯一编码，建议登录名
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

    public Boolean getAlwaysOnline() {
        return !Objects.isNull(alwaysOnline) && alwaysOnline;
    }

    public Boolean getOnlyOnline() {
        return !Objects.isNull(alwaysOnline) && alwaysOnline;
    }
}
