package cn.tannn.jdevelops.jwt.redis.service;


import cn.tannn.jdevelops.jwt.redis.entity.StorageUserState;

import java.util.List;

/**
 * 用户状态
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/9 16:20
 */
public interface RedisUserState {

    /**
     * 存储用户状态  [永不过期，改变时也要重置状态信息]
     *
     * @param state 用户状态
     */
    void storage(StorageUserState state);

    /**
     * 刷新用户状态
     *
     * @param state 新的状态
     */
    void refresh(StorageUserState state);

    /**
     * 查询用户状态
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     * @return 用户状态对象
     */
    StorageUserState load(String subject);

    /**
     * 验证用户状态  [根据状态抛出相应的异常]
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void verify(String subject) ;

    /**
     * 验证用户状态  [根据状态抛出相应的异常]
     *
     * @param token token
     */
    void verifyByToken(String token) ;


    /**
     * 删除 userState
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(String subject);

    /**
     * 删除  userState
     * @param token token
     */
    void removeByToken(String token);

    /**
     * 删除 userState
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(List<String> subject);

}
