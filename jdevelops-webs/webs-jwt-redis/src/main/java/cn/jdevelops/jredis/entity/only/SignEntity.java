package cn.jdevelops.jredis.entity.only;

import lombok.*;

import java.util.Map;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignEntity {

    /**
     * 必填： jwt 主题 （用户唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 其他信息数据
     */
    Map<String, Object> map;

    /**
     * 是否永久在线 (默认fales)
     */
    boolean alwaysOnline;




}
