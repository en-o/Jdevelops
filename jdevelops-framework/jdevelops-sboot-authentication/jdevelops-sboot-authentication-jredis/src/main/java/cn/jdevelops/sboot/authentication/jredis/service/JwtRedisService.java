package cn.jdevelops.sboot.authentication.jredis.service;


import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;


/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:06
 */
public interface JwtRedisService {




    /**
     * 权限验证
     * @param subject  用户唯一值(一般用用户的登录名
     * @param annotation 权限注解
     * @param <RB> RedisAccount
     * @throws ExpiredRedisException ExpiredRedisException
     */
    <RB extends BasicsAccount> void verifyUserPermission(String subject, ApiPermission annotation) throws ExpiredRedisException;
}
