package cn.jdevelops.authentication.jredis.service;

import cn.jdevelops.authentication.jredis.entity.StorageUserRole;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiPermission;

import java.util.List;

/**
 * 用户权限
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/9 16:20
 */
public interface RedisUserRole {


    /**
     * 存储用户权限  [永不过期，改变时也要重置权限信息]
     *
     * @param role 用户权限
     */
    void storage(StorageUserRole role);

    /**
     * 刷新用户权限
     *
     * @param role 新的权限
     */
    void refresh(StorageUserRole role);

    /**
     * 查询用户权限
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     * @return 用户权限对象
     */
    StorageUserRole load(String subject);

    /**
     * 验证用户权限  [根据权限抛出相应的异常]
     *
     * @param subject    token.subject[用户唯一值(一般用用户的登录名]
     * @param annotation 权限注解
     */
    void verify(String subject, ApiPermission annotation);

    /**
     * 验证用户权限  [根据权限抛出相应的异常]
     *
     * @param token      token
     * @param annotation 权限注解
     */
    void verifyByToken(String token, ApiPermission annotation);

    /**
     * 删除 userRole
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(String subject);

    /**
     * 删除  userRole
     * @param token token
     */
    void removeByToken(String token);

    /**
     * 删除 userRole
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void remove(List<String> subject);

}
