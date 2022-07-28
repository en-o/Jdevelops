package cn.jdevelops.jredis.entity;

import lombok.*;

import java.util.Objects;

/**
 * 登录用户的token
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
public class LoginTokenRedis {

    /**
     * 用户唯一编码，建议登录名
     * = subject
     */
    String userCode;

    /**
     * token
     */
    String token;

    /**
     * 是否永久在线（默认 false
     */
    Boolean alwaysOnline;

    public boolean isAlwaysOnline() {
        return !Objects.isNull(alwaysOnline) && alwaysOnline;
    }
}
