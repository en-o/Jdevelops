package cn.jdevelops.sboot.authentication.jredis.service;



import cn.jdevelops.sboot.authentication.jredis.entity.base.BasicsAccount;
import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageUserTokenEntity;
import cn.jdevelops.sboot.authentication.jwt.exception.ExpiredRedisException;
import cn.jdevelops.util.jwt.exception.LoginException;


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
     * @param storageUserTokenEntity 存储用户登录token
     */
    void storageUserToken(StorageUserTokenEntity storageUserTokenEntity);

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
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageUserTokenEntity verifyUserTokenBySubject(String subject) throws ExpiredRedisException;

    /**
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param token toekn
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    StorageUserTokenEntity verifyUserTokenByToken(String token) throws ExpiredRedisException;


    /**
     * 获取存储的用户token详情
     * @param subject 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     */
    StorageUserTokenEntity loadUserTokenInfoBySubject(String subject) ;

    /**
     * 获取存储的用户token详情
     * @param token token
     * @return LoginTokenRedis
     * @throws LoginException LoginException
     */
    StorageUserTokenEntity loadUserTokenInfoByToken(String token) throws  LoginException;


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
     * @return RedisAccount
     */
    <RB extends BasicsAccount> RB loadUserStatus(String subject);



}
