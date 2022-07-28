package cn.jdevelops.jredis.service;


import cn.jdevelops.jredis.entity.LoginTokenRedis;
import cn.jdevelops.jredis.entity.RedisAccount;
import cn.jdevelops.jredis.exception.ExpiredRedisException;

import java.util.List;

/**
 * redis
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 14:06
 */
public interface RedisService {

    /**
     * 存放 用户TOKEN
     *
     * @param loginTokenRedis 存储用户登录token
     */
    void storageUserToken(LoginTokenRedis loginTokenRedis);

    /**
     * 刷新用户token
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     */
    void refreshUserToken(String userCode);


    /**
     * 删除 用户TOKEN
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     */
    void removeUserToken(String userCode);


    /**
     * 验证 用户TOKEN是否存在，存在则返回 token
     * 不存在，或者 token 异常就报错
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     * @return LoginTokenRedis
     * @throws ExpiredRedisException redis异常
     */
    LoginTokenRedis verifyUserToken(String userCode) throws ExpiredRedisException;

    /**
     * 验证用户的状态
     * 有问题则异常 (ExpiredRedisException)
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     * @throws ExpiredRedisException redis异常
     */
    void verifyUserStatus(String userCode) throws ExpiredRedisException;


    /**
     * 存储 用户的状态
     *
     * @param account 用户
     */
    <T> void storageUserStatus(RedisAccount<T> account);

    /**
     * 加载用户的状态
     * 有问题则异常
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     * @return RedisAccount
     */
    <T> RedisAccount<T> loadUserStatus(String userCode);


    /**
     * 存放 用户角色
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     * @param roles    权限集合
     */
    void storageUserRole(String userCode, List<String> roles);


    /**
     * 加载用户角色
     *
     * @param userCode 用户唯一值(一般用用户的登录名
     * @return List (如返回空对象则表示redis中无数据请先自行添加
     */
    List<String> loadUserRole(String userCode);

}
