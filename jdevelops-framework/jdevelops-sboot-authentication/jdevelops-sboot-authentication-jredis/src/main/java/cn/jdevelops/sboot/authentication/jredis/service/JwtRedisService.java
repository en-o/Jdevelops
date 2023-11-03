package cn.jdevelops.sboot.authentication.jredis.service;



import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageToken;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.util.jwt.exception.LoginException;

import java.util.List;


/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:06
 */
public interface JwtRedisService {

    /**
     * 存放 用户TOKEN
     *
     * @param storageToken 存储用户登录token
     */
    void storageUserToken(StorageToken storageToken);

    /**
     * 刷新用户token
     *
     * @param subject 用户唯一值(一般用用户的登录名
     */
    void refreshUserToken(String subject);


    /**
     * 删除 用户TOKEN
     *
     * @param subject 用户唯一值(一般用用户的登录名
     */
    void removeUserToken(String subject);

    /**
     * 删除 用户TOKEN
     *
     * @param subject 用户唯一值(一般用用户的登录名
     */
    void removeUserToken(List<String> subject);

    /**
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageToken verifyUserTokenBySubject(String subject) throws ExpiredRedisException;

    /**
     * 验证 用户TOKEN是否存在，存在则返回 token的详细信息 {@link StorageToken}
     * 不存在，或者 token 异常就报错
     *
     * @param token toekn
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageToken verifyUserTokenByToken(String token) throws ExpiredRedisException;


    /**
     * 获取存储的用户token详情
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     */
    StorageToken loadUserStorageTokenBySubject(String subject) ;

    /**
     * 获取存储的用户token详情
     * @param token token
     * @return LoginTokenRedis
     * @throws LoginException LoginException
     */
    StorageToken loadUserStorageTokenByToken(String token) throws  LoginException;


    /**
     * 验证用户的状态
     * 有问题则异常 (ExpiredRedisException)
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @throws ExpiredRedisException redis异常
     */
    <RB extends BasicsAccount> void verifyUserStatus(String subject) throws ExpiredRedisException;


    /**
     * 存储 用户的状态
     * 永不过期 改变时也要该redis
     * @param account 用户
     */
    <RB extends BasicsAccount> void storageUserStatus(RB account);

    /**
     * 加载用户的状态
     * 有问题则异常
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @param resultBean  RB.class
     * @return RedisAccount
     */
    <RB extends BasicsAccount> RB loadUserStatus(String subject, Class<RB> resultBean);


    /**
     * 权限验证
     * @param subject  用户唯一值(一般用用户的登录名
     * @param annotation 权限注解
     * @param <RB> RedisAccount
     * @throws ExpiredRedisException ExpiredRedisException
     */
    <RB extends BasicsAccount> void verifyUserPermission(String subject, ApiPermission annotation) throws ExpiredRedisException;
}
