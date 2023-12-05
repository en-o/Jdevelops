package cn.jdevelops.sboot.authentication.jredis.service;

import cn.jdevelops.sboot.authentication.jredis.entity.only.StorageToken;

import java.util.List;

/**
 * 用户token
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/9 16:17
 */
public interface RedisToken {
    /**
     * 存储token
     * @param token  StorageToken
     */
    void storage(StorageToken token);

    /**
     * 刷新token有效期
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void refresh(String subject);

    /**
     * 刷新token有效期
     *
     * @param token token
     */
    void refreshByToken(String token);

    /**
     * 删除token
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(String subject);

    /**
     * 删除token
     * @param token token
     */
    void removeByToken(String token);

    /**
     * 删除token
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(List<String> subject);

    /**
     * 删除token
     *
     * @param token token
     */
    void removeByToken(List<String> token);


    /**
     * 查询token
     * @param subject  token.subject[用户唯一值(一般用用户的登录名]
     * @return 存储的token对象, 不存在会报错
     */
    StorageToken load(String subject) ;

    /**
     * 获取存储的用户token详情
     * @param token token
     * @return 存储的token对象, 不存在会报错
     */
    StorageToken loadByToken(String token);


    /**
     * 验证token
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     * @return token正常返回存储的token对象, 不正常直接抛出异常
     * @throws cn.jdevelops.api.exception.exception.TokenException token异常
     */
    StorageToken verify(String subject);

    /**
     * 验证token
     *
     * @param token token
     * @return token正常返回存储的token对象, 不正常直接抛出异常
     * @throws cn.jdevelops.api.exception.exception.TokenException token异常
     */
    StorageToken verifyByToken(String token);



}
