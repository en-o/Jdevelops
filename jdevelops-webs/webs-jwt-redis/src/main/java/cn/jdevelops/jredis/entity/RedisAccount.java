package cn.jdevelops.jredis.entity;

import lombok.*;

/**
 * 用户信息 存redis的 用默认的SurenessAccount报了错
 * @author tomsun28
 * @date 16:20 2019-05-19
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisAccount<T> {

    /** 用户唯一编码(一般都用用户的登录名 **/
    private String userCode;

    /** 用户密码 **/
    private String password;

    /** 加密的盐 **/
    private String salt;

    /** 是否被禁用 **/
    private boolean disabledAccount;

    /** 是否被锁定 **/
    private boolean excessiveAttempts;

    /** 用户其他数据 **/
    private T userInfo;

}
