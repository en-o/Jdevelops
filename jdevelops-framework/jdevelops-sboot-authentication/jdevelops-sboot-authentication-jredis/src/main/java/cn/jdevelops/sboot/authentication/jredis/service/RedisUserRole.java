package cn.jdevelops.sboot.authentication.jredis.service;

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
    void storage(String role);

    /**
     * 刷新用户权限
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void refresh(String subject);

    /**
     * 查询用户权限
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     * @return 用户权限对象
     */
    String load(String subject);

    /**
     * 验证用户权限  [根据权限抛出相应的异常]
     *
     * @param subject token.subject[用户唯一值(一般用用户的登录名]
     */
    void verify(String subject);

    /**
     * 验证用户权限  [根据权限抛出相应的异常]
     *
     * @param token token
     */
    void verifyByToken(String token);


}
