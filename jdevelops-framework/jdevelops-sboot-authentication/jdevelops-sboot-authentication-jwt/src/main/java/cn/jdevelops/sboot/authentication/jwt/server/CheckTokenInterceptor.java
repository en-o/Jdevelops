package cn.jdevelops.sboot.authentication.jwt.server;


import cn.jdevelops.spi.SPI;

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
     * @param subject 用户唯一编码
     */
    default void refreshToken(String subject){}


    /**
     * 检查用户状态(不正常的直接抛异常出去
     * @param subject 用户唯一编码
     * @throws Exception ExpiredRedisException
     */
    default void checkUserStatus(String subject) throws Exception{}
}