package cn.jdevelops.jredis.entity.only;

import cn.jdevelops.util.jwt.util.EncryptionUtil;
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
public class StorageUserTokenEntity {

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
     * 短的token,还没开始使用
     */
    String shortToken;

    /**
     * 是否永久在线（默认 false
     */
    Boolean alwaysOnline;


    public Boolean getAlwaysOnline() {
        return !Objects.isNull(alwaysOnline) && alwaysOnline;
    }


    public String getShortToken() {
        return EncryptionUtil.encrypt2MD5(token);
    }
}
