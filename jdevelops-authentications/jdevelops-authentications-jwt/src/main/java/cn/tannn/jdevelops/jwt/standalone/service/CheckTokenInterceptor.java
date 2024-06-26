package cn.tannn.jdevelops.jwt.standalone.service;



import cn.tannn.jdevelops.spi.SPI;

import java.lang.reflect.Method;

/**
 * 请各自的服务去实现该接口, 并注入到spring上下文中去
 * @author Tianms
 * @date 2020/4/19 11:18
 */
@SPI
public interface CheckTokenInterceptor {

    /**
     * token校验方法  默认不放行
     * @param token token
     * @return boolean
     */
    boolean checkToken(String token);


    /**
     * 刷新token缓存 (默认不刷新
     * @param token token
     */
    default void refreshToken(String token){}


    /**
     * 检查用户状态(不正常的直接抛异常出去
     * @param token token
     * @throws Exception ExpiredRedisException
     */
    default void checkUserStatus(String token) throws Exception{}

    /**
     * 检查用户权限
     * @param subject token.subject[用户唯一编码，建议登录名]
     * @param method 请求接口的权限注解
     * @throws Exception ExpiredRedisException
     */
    default void checkUserPermission(String subject, Method method) throws Exception{}
}
